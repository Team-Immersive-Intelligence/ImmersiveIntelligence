package pl.pabilo8.immersiveintelligence.api.ammo.utils;

import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.api.tool.RailgunHandler.RailgunProjectileProperties;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem.IIAmmoProjectile;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoBallisticsCache.CachedBallisticStats;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIConcreteDecoration.ConcreteDecorations;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIMetalBase.Metals;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Pabilo8
 * @since 14-03-2020
 */
public class IIAmmoUtils
{
	//--- Global Values ---//
	public static boolean ammoBreaksBlocks = Weapons.blockDamage;
	public static boolean ammoExplodesBlocks = Ammunition.blockDamage;
	public static boolean ammoRicochets = true;

	//--- Common Component Methods ---//

	public static void suppress(World world, double posX, double posY, double posZ, float supressionRadius, int suppressionPower)
	{
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX, posY, posZ, posX, posY, posZ).grow(supressionRadius));
		for(EntityLivingBase entity : entities)
		{
			PotionEffect effect = entity.getActivePotionEffect(IIPotions.suppression);
			if(effect==null)
				effect = new PotionEffect(IIPotions.suppression, 120, suppressionPower, false, false);
			else
			{
				effect.duration = 10;
				effect.combine(new PotionEffect(IIPotions.suppression, 120, Math.min(255, effect.getAmplifier()+suppressionPower)));
			}
			entity.addPotionEffect(effect);
		}
	}

	public static void breakArmour(Entity entity, int damageToArmour)
	{
		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase ent = (EntityLivingBase)entity;
			PotionEffect effect = ent.getActivePotionEffect(IIPotions.brokenArmor);
			if(effect==null)
				effect = new PotionEffect(IIPotions.brokenArmor, 60, damageToArmour, false, false);
			else
			{
				effect.duration = 10;
				effect.combine(new PotionEffect(IIPotions.brokenArmor, 60, Math.min(255, effect.getAmplifier()+damageToArmour)));
			}
			for(ItemStack stack : ent.getArmorInventoryList())
				stack.damageItem(damageToArmour, ent);

			ent.addPotionEffect(effect);
		}
	}

	//--- Ballistic Calculation ---//

	/**
	 * Overload for {@link #calculateBallisticAngle(double, double, float, double, double, double)} with default drag and gravity meant for entity-based guns
	 *
	 * @param posShooter position of the shooter
	 * @param posTarget  position of the target
	 * @param ammoStack  ammo stack
	 * @param precision  precision of the calculation
	 * @return optimal ballistic shooting angle
	 */
	public static float calculateBallisticAngle(Vec3d posShooter, Vec3d posTarget, ItemStack ammoStack, float precision)
	{
		Vec3d dist = posShooter.subtract(posTarget);
		IAmmoTypeItem<?, ?> ammoItem = AmmoRegistry.getAmmoItem(ammoStack);

		if(ammoItem==null)
			return 0;

		return calculateBallisticAngle(new Vec3d(dist.x, 0, dist.z).distanceTo(Vec3d.ZERO),
				dist.y,
				ammoItem.getVelocity(),
				EntityAmmoProjectile.GRAVITY*ammoItem.getMass(ammoStack),
				1f-EntityAmmoProjectile.DRAG,
				precision
		);
	}

	//TODO: 15.02.2024 use an equation instead of simulating the trajectory

	/**
	 * Pitch calculation for artillery stolen from Pneumaticcraft. Huge thanks to desht and MineMaarten for this amazing code!
	 * <a href="https://github.com/TeamPneumatic/pnc-repressurized/blob/master/src/main/java/me/desht/pneumaticcraft/common/tileentity/TileEntityAirCannon.java">https://github.com/TeamPneumatic/pnc-repressurized/blob/master/src/main/java/me/desht/pneumaticcraft/common/tileentity/TileEntityAirCannon.java</a>
	 *
	 * @param distance       distance to target
	 * @param height         height difference between the gun and target
	 * @param force          speed (blocks/s) of the bullet
	 * @param gravity        gravity of the bullet
	 * @param drag           drag factor of the bullet
	 * @param anglePrecision precision with which the angle will be searched, the lower the number, the higher the precision
	 * @return optimal ballistic shooting angle
	 * @author desht
	 * @author MineMaarten
	 */
	public static float calculateBallisticAngle(double distance, double height, float force, double gravity, double drag, double anglePrecision)
	{
		double bestAngle = 0;
		double bestDistance = Float.MAX_VALUE;
		if(gravity==0D)
			return 90F-(float)(Math.atan(height/distance)*180F/Math.PI);
		/*
		 * simulate the trajectory for angles from 45 to 90 degrees,
		 * returning the angle which lands the projectile closest to the target distance
		 */
		for(double i = Math.PI*anglePrecision; i < Math.PI*0.5D; i += anglePrecision)
		{
			double motionX = MathHelper.cos((float)i)*force;// calculate the x component of the vector
			double motionY = MathHelper.sin((float)i)*force;// calculate the y component of the vector
			double posX = 0;
			double posY = 0;
			while(posY > height||motionY > 0)
			{
				// simulate movement, until we reach the y-level required
				motionX *= drag;
				motionY *= drag;
				motionY -= gravity;
				posX += motionX;
				posY += motionY;
			}
			double distanceToTarget = Math.abs(distance-posX);
			if(distanceToTarget < bestDistance)
			{
				bestDistance = distanceToTarget;
				bestAngle = i;
			}
		}

		return 90F-(float)(bestAngle*180D/Math.PI);
	}

	//TODO: 15.02.2024 check out optimized version
	/*
	//Optimized version
	public static float calculateBallisticAngle(double distance, double height, float force, double gravity, double drag, double anglePrecision) {
    double lowerBound = Math.PI * anglePrecision;
    double upperBound = Math.PI * 0.5D;
    double bestAngle = 0;
    double bestDistance = Double.MAX_VALUE;

    while (Math.abs(upperBound - lowerBound) > anglePrecision) {
        double midPoint = (lowerBound + upperBound) / 2;
        double motionX = MathHelper.cos((float)midPoint) * force;
        double motionY = MathHelper.sin((float)midPoint) * force;
        double posX = 0;
        double posY = 0;

        while (posY > height || motionY > 0) {
            motionX *= drag;
            motionY *= drag;
            motionY -= gravity;
            posX += motionX;
            posY += motionY;
        }

        double distanceToTarget = Math.abs(distance - posX);
        if (distanceToTarget < bestDistance) {
            bestDistance = distanceToTarget;
            bestAngle = midPoint;
        }

        if (posX < distance) {
            lowerBound = midPoint;
        } else {
            upperBound = midPoint;
        }
    }

    return 90F - (float)(bestAngle * 180D / Math.PI);
}
	 */

	public static float getDirectFireAngle(double initialVelocity, double mass, Vec3d toTarget)
	{
		double force = initialVelocity;
		double dist = toTarget.distanceTo(new Vec3d(0, toTarget.y, 0));
		double gravityMotionY = 0, motionY = 0, baseMotionY = toTarget.normalize().y, baseMotionYC;

		while(dist > 0)
		{
			force -= EntityAmmoProjectile.DRAG*force;
			gravityMotionY -= EntityAmmoProjectile.GRAVITY*mass;
			baseMotionYC = baseMotionY*(force/(initialVelocity));
			motionY += (baseMotionYC+gravityMotionY);
			dist -= force;
		}

		toTarget = toTarget.addVector(0, motionY-baseMotionY, 0).normalize();

		/*return (float)Math.toDegrees(calculateFireAngle(initialVelocity,
				mass*EntityAmmoProjectile.GRAVITY,
				toTarget.distanceTo(new Vec3d(0, toTarget.y, 0)),
				toTarget.y
		));*/


		return (float)Math.toDegrees((Math.atan2(toTarget.y, toTarget.distanceTo(new Vec3d(0, toTarget.y, 0)))));
	}

	public static float calculateFireAngle(double initialVelocity, double gravity, double distance, double heightDifference)
	{
		double velocity2 = Math.pow(initialVelocity, 2);
		double gravity2 = Math.pow(gravity, 2);
		double distance2 = Math.pow(distance, 2);
		double underRoot = Math.pow(initialVelocity, 4)-gravity*(gravity2*distance2+2*heightDifference*velocity2);

		//No real solutions, the target is out of reach
		if(underRoot < 0)
			return Float.NaN;

		double positive = Math.atan((velocity2+Math.sqrt(underRoot))/(gravity*distance));
		double negative = Math.atan((velocity2-Math.sqrt(underRoot))/(gravity*distance));

		//Return the smaller angle (for the faster trajectory)
		return (float)Math.min(positive, negative);
	}

	public static float getIEDirectRailgunAngle(ItemStack ammo, Vec3d toTarget)
	{
		RailgunProjectileProperties p = RailgunHandler.getProjectileProperties(ammo);
		if(p!=null)
		{
			float force = 20;
			float gravity = (float)p.gravity;

			double gravityMotionY = 0, motionY = 0, baseMotionY = toTarget.normalize().y, baseMotionYC = baseMotionY;
			double dist = toTarget.distanceTo(new Vec3d(0, toTarget.y, 0));
			while(dist > 0)
			{
				dist -= force;
				force *= 0.99;
				baseMotionYC *= 0.99f;
				gravityMotionY -= gravity/force;
				motionY += (baseMotionYC+gravityMotionY);
			}

			toTarget = toTarget.addVector(0, motionY-baseMotionY, 0).normalize();
		}

		return (float)Math.toDegrees((Math.atan2(toTarget.y, toTarget.distanceTo(new Vec3d(0, toTarget.y, 0)))));
	}

	//TODO: 15.02.2024 implement on emplacements
	public static float[] getInterceptionAngles(Vec3d shooterPos, Vec3d shooterVel, Vec3d targetPos, Vec3d targetVel, double projectileSpeed, double mass)
	{
		Vec3d vv = shooterPos.subtract(shooterVel).subtract(targetPos).add(targetVel).normalize();
		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
		float pp = (float)Math.toDegrees((Math.atan2(vv.y, vv.distanceTo(new Vec3d(0, vv.y, 0)))))
				+getDirectFireAngle(projectileSpeed, mass, shooterPos.subtract(targetPos));

		return new float[]{yy, pp};
	}

	//--- Item Tooltips ---//

	/**
	 * Adds tooltip information to the ammunition item
	 *
	 * @param ammo    ammunition type
	 * @param stack   ammunition ItemStack
	 * @param world   world
	 * @param tooltip tooltip list
	 */
	@SideOnly(Side.CLIENT)
	public static void createAmmoTooltip(IAmmoTypeItem<?, ?> ammo, ItemStack stack, @Nullable World world, List<String> tooltip)
	{
		//add category tooltip
		tooltip.add(getFormattedBulletTypeName(ammo, stack));
		//get common parameters
		AmmoCore core = ammo.getCore(stack);
		CoreType coreType = ammo.getCoreType(stack);

		//composition tab
		if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LSHIFT, IIReference.DESC_BULLETS+"composition", tooltip))
		{
			//get parameters
			FuseType fuse = ammo.getFuseType(stack);
			AmmoComponent[] components = ammo.getComponents(stack);

			//information section

			tooltip.add(IIReference.COLOR_IMMERSIVE_ORANGE.getHexCol(I18n.format(IIReference.DESC_BULLETS+"details")));

			//core + type
			IIClientUtils.addTooltip(tooltip, IIReference.CHARICON_BULLET_CONTENTS, IIReference.DESC_BULLETS+"core",
					IIStringUtil.getItalicString(I18n.format(IIReference.DESCRIPTION_KEY+"bullet_core_type."+coreType.getName())),
					core.getColor().getHexCol(I18n.format("item."+ImmersiveIntelligence.MODID+".bullet.component."+core.getName()+".name"))
			);

			//fuse
			if(ammo.getAllowedFuseTypes().length > 0)
			{
				if(fuse!=FuseType.CONTACT)
					IIClientUtils.addTooltip(tooltip, fuse.symbol, "desc.immersiveintelligence.bullet_fuse.tooltip."+fuse.getName(), ammo.getFuseParameter(stack));
				else
					IIClientUtils.addTooltip(tooltip, fuse.symbol, IIReference.DESC_BULLETS+"fuse", I18n.format("desc.immersiveintelligence.bullet_fuse."+fuse.getName()));
			}

			//mass
			tooltip.add(I18n.format(IIReference.DESC_BULLETS+"mass", Utils.formatDouble(ammo.getMass(stack), "0.##")));

			//components section
			if(components.length > 0)
			{
				tooltip.add(IIReference.COLOR_IMMERSIVE_ORANGE.getHexCol(I18n.format(IIReference.DESC_BULLETS+"components")));
				for(AmmoComponent comp : components)
					tooltip.add("   "+comp.getTranslatedName());
			}
		}

		//Performance tab
		IIAmmoProjectile annotation = IIUtils.getAnnotation(IIAmmoProjectile.class, ammo);
		if(annotation!=null&&!ammo.isBulletCore(stack)
				&&ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LCONTROL, IIReference.DESC_BULLETS+"ballistics", tooltip))
		{
			//Ballistics section
			CachedBallisticStats stats = AmmoBallisticsCache.get(ammo, stack);

			tooltip.add(IIReference.COLOR_PRUSSIAN_BLUE.getHexCol(I18n.format(IIReference.DESC_BULLETS+"performance")));
			tooltip.add(I18n.format(IIReference.DESC_BULLETS+"damage_dealt", ammo.getDamage()*core.getDamageModifier()*coreType.getDamageMod()));
			tooltip.add(I18n.format(IIReference.DESC_BULLETS+"standard_velocity", Utils.formatDouble(ammo.getVelocity(), "0.###")));

			//Max distance tooltip
			if(annotation.artillery())
			{
				tooltip.add(I18n.format(IIReference.DESC_BULLETS+"max_artillery_range",
						Utils.formatDouble(stats.getGetMaxArtilleryRange(), "0.##")));
				tooltip.add(I18n.format(IIReference.DESC_BULLETS+"max_artillery_height",
						Utils.formatDouble(stats.getMaxHeightReached(), "0.##")));
				tooltip.add(I18n.format(IIReference.DESC_BULLETS+"max_direct_range",
						Utils.formatDouble(stats.getMaxDirectRange(), "0.##")));
			}
			else
				tooltip.add(I18n.format(IIReference.DESC_BULLETS+"max_range",
						Utils.formatDouble(stats.getMaxDirectRange(), "0.##")));

			//Penetration section
			tooltip.add(IIReference.COLOR_PRUSSIAN_BLUE.getHexCol(I18n.format(IIReference.DESC_BULLETS+"armor_penetration")));

			//list of block penetration tests
			listPenetratedAmount(tooltip, ammo, core, coreType, Blocks.GLASS, 0);
			listPenetratedAmount(tooltip, ammo, core, coreType, Blocks.DIRT, 0);
			listPenetratedAmount(tooltip, ammo, core, coreType, Blocks.LOG, 0);
			listPenetratedAmount(tooltip, ammo, core, coreType, Blocks.BRICK_BLOCK, 0);
			listPenetratedAmount(tooltip, ammo, core, coreType, IEContent.blockStoneDecoration, BlockTypes_StoneDecoration.CONCRETE_TILE.getMeta());
			listPenetratedAmount(tooltip, ammo, core, coreType, IIContent.blockConcreteDecoration, ConcreteDecorations.STURDY_CONCRETE_BRICKS.getMeta());
			listPenetratedAmount(tooltip, ammo, core, coreType, IIContent.blockMetalStorage, Metals.TUNGSTEN.getMeta());
			listPenetratedAmount(tooltip, ammo, core, coreType, IIContent.blockConcreteDecoration, ConcreteDecorations.UBERCONCRETE.getMeta());
		}
	}

	private static void listPenetratedAmount(List<String> tooltip, IAmmoTypeItem<?, ?> ammo, AmmoCore core, CoreType coreType, Block block, int meta)
	{
		//get penetration handler
		IPenetrationHandler penHandler = PenetrationRegistry.getPenetrationHandler(block.getStateFromMeta(meta));
		int penetratedAmount = getPenetratedAmount(ammo, core, coreType, penHandler, penHandler.getPenetrationHardness());

		//add penetration information
		String displayName = new ItemStack(block, 1, meta).getDisplayName();
		if(penetratedAmount < 1)
			tooltip.add(TextFormatting.RED+"✕ "+displayName);
		else
			tooltip.add(TextFormatting.DARK_GREEN+String.format("⦴ %s: %d B", displayName, penetratedAmount));

	}

	private static String getFormattedBulletTypeName(IAmmoType<?, ?> ammo, ItemStack stack)
	{
		StringBuilder builder = new StringBuilder();

		//Add all components with role different from "general purpose"
		Stream.concat(Stream.of(ammo.getCoreType(stack).getRole()),
						Arrays.stream(ammo.getComponents(stack)).map(AmmoComponent::getRole))
				.filter(c -> c==ComponentRole.GENERAL_PURPOSE)
				.map(c -> c.getColor().getHexCol(I18n.format(IIReference.DESCRIPTION_KEY+"bullet_type."+c.getName())))
				.forEach(c -> builder.append(c).append(" - "));

		//If no components with different role were found, add general purpose
		if(builder.toString().isEmpty())
		{
			builder.append(I18n.format(IIReference.DESCRIPTION_KEY+"bullet_type."+ComponentRole.GENERAL_PURPOSE.getName()));
			builder.append(" - ");
		}

		//trim last " - "
		builder.delete(builder.length()-3, builder.length());

		//Display the type if the item has a custom name
		if(stack.hasDisplayName())
			builder.append(" ").append(TextFormatting.GRAY).append(stack.getItem().getItemStackDisplayName(stack));
		return builder.toString();
	}

	//--- Public Utility Methods ---//

	/**
	 * @param coreMaterial ammunition's core material
	 * @param coreType     ammunition's core type
	 * @return combined hardness of the core material and core type
	 */
	public static PenetrationHardness getCombinedHardness(AmmoCore coreMaterial, CoreType coreType)
	{
		//Bedrock level is the highest present, but it's not penetrable
		int index = MathHelper.clamp(coreMaterial.getPenetrationHardness().ordinal()+coreType.getPenHardnessBonus(), 0, PenetrationHardness.values().length-2);
		return PenetrationHardness.values()[index];
	}

	/**
	 * @param ammoType ammunition type
	 * @param coreType ammunition core type
	 * @return combined penetration depth of the ammunition type and core type
	 */
	public static float getCombinedDepth(IAmmoType<?, ?> ammoType, CoreType coreType)
	{
		return ammoType.getPenetrationDepth()*coreType.getPenDepthMod();
	}


	/**
	 * @param ammoType      ammunition type
	 * @param coreMaterial  ammunition core material
	 * @param coreType      ammunition core type
	 * @param penHandler    block's penetration handler
	 * @param blockHardness block's hardness
	 * @return amount of blocks penetrated by the ammo
	 */
	public static int getPenetratedAmount(IAmmoType<?, ?> ammoType, AmmoCore coreMaterial, CoreType coreType,
										  IPenetrationHandler penHandler, PenetrationHardness blockHardness)
	{
		float penetrationDepth = getCombinedDepth(ammoType, coreType);
		PenetrationHardness ammoHardness = getCombinedHardness(coreMaterial, coreType);

		if(ammoHardness.compareTo(blockHardness) >= 0)
			return (int)Math.floor(penetrationDepth/penHandler.getThickness());
		return 0;
	}
}
