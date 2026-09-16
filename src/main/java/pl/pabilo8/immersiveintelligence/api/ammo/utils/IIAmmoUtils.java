package pl.pabilo8.immersiveintelligence.api.ammo.utils;

import blusunrize.immersiveengineering.api.tool.ITeslaEntity;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import blusunrize.immersiveengineering.api.tool.RailgunHandler.RailgunProjectileProperties;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.stone.BlockTypes_StoneDecoration;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEInternalFluxHandler;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
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
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoBallisticsCache.BallisticFireMode;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoBallisticsCache.BallisticSolution;
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
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

/**
 * Provides shared ammunition, ballistic, penetration, and effect utilities.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 14.08.2026
 * @since 14.03.2020
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

	public static void breakArmor(Entity entity, int damageToArmor)
	{
		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase ent = (EntityLivingBase)entity;
			PotionEffect effect = ent.getActivePotionEffect(IIPotions.brokenArmor);
			if(effect==null)
				effect = new PotionEffect(IIPotions.brokenArmor, 60, damageToArmor, false, false);
			else
			{
				effect.duration = 10;
				effect.combine(new PotionEffect(IIPotions.brokenArmor, 60, Math.min(255, effect.getAmplifier()+damageToArmor)));
			}
			for(ItemStack stack : ent.getArmorInventoryList())
				stack.damageItem(damageToArmor, ent);

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
	 * @param precision  legacy precision parameter; cache precision is shared between callers
	 * @return optimal ballistic shooting angle
	 */
	public static float calculateBallisticAngle(Vec3d posShooter, Vec3d posTarget, ItemStack ammoStack, float precision)
	{
		IAmmoTypeItem<?, ?> ammoItem = AmmoRegistry.getAmmoItem(ammoStack);
		if(ammoItem==null)
			return 0;

		Vec3d direction = posTarget.subtract(posShooter);
		BallisticSolution solution = AmmoBallisticsCache.get(ammoItem, ammoStack)
				.getArtillerySolution(Math.hypot(direction.x, direction.z), direction.y);
		return solution.isValid()?90F-(float)solution.getElevation(): Float.NaN;
	}

	/**
	 * Compatibility overload for non-ammo callers. Results are backed by the shared cache;
	 * {@code anglePrecision} is retained for source compatibility.
	 *
	 * @param distance       distance to target
	 * @param height         height difference between the gun and target
	 * @param force          speed (blocks/tick) of the bullet
	 * @param gravity        gravity of the bullet
	 * @param drag           drag factor of the bullet
	 * @param anglePrecision legacy precision parameter
	 * @return optimal ballistic shooting angle
	 */
	public static float calculateBallisticAngle(double distance, double height, float force, double gravity, double drag, double anglePrecision)
	{
		if(!IIMath.isNumberFinite(distance, height, force, gravity, drag, anglePrecision)
				||distance < 0||force <= 0||gravity < 0||drag <= 0||drag > 1||anglePrecision <= 0)
			return Float.NaN;

		BallisticSolution solution = getLegacyBallistics(force, gravity, drag)
				.getArtillerySolution(distance, height);
		return solution.isValid()?90F-(float)solution.getElevation(): Float.NaN;
	}

	public static float getDirectFireAngle(ItemStack ammoStack, Vec3d toTarget)
	{
		return getDirectFireAngle(ammoStack, toTarget, 1D);
	}

	public static float getDirectFireAngle(ItemStack ammoStack, Vec3d toTarget, double velocityModifier)
	{
		IAmmoTypeItem<?, ?> ammo = AmmoRegistry.getAmmoItem(ammoStack);
		if(ammo==null||toTarget==null)
			return Float.NaN;
		return AmmoBallisticsCache.get(ammo, ammoStack, velocityModifier)
				.getDirectFireAngle(Math.hypot(toTarget.x, toTarget.z), toTarget.y);
	}

	public static float getArtilleryFireAngle(ItemStack ammoStack, Vec3d toTarget)
	{
		return getArtilleryFireAngle(ammoStack, toTarget, 1D);
	}

	public static float getArtilleryFireAngle(ItemStack ammoStack, Vec3d toTarget, double velocityModifier)
	{
		IAmmoTypeItem<?, ?> ammo = AmmoRegistry.getAmmoItem(ammoStack);
		if(ammo==null||toTarget==null)
			return Float.NaN;
		return AmmoBallisticsCache.get(ammo, ammoStack, velocityModifier)
				.getArtilleryAngle(Math.hypot(toTarget.x, toTarget.z), toTarget.y);
	}

	private static CachedBallisticStats getProjectileBallistics(double velocity, double mass)
	{
		return AmmoBallisticsCache.get(AmmoBallistics.projectile(
				Arrays.asList("ii_projectile", mass), velocity,
				EntityAmmoProjectile.GRAVITY*mass, 1D-EntityAmmoProjectile.DRAG,
				EntityAmmoProjectile.MAX_TICKS
		));
	}

	private static CachedBallisticStats getLegacyBallistics(double velocity, double gravity, double drag)
	{
		Object identity = Arrays.asList("legacy_projectile", gravity, drag);
		return AmmoBallisticsCache.get(AmmoBallistics.custom(identity, velocity,
				EntityAmmoProjectile.MAX_TICKS, state -> {
					state.multiplyMotion(drag);
					state.addMotion(0, -gravity);
					state.move();
				}));
	}

	public static float calculateFireAngle(double initialVelocity, double gravity, double distance, double heightDifference)
	{
		if(!IIMath.isNumberFinite(initialVelocity, gravity, distance, heightDifference)
				||initialVelocity <= 0||gravity < 0||distance < 0)
			return Float.NaN;

		BallisticSolution solution = getLegacyBallistics(initialVelocity, gravity, 1D)
				.getDirectSolution(distance, heightDifference);
		return solution.isValid()?(float)Math.toRadians(solution.getElevation()): Float.NaN;
	}

	public static float getIEDirectRailgunAngle(ItemStack ammo, Vec3d toTarget)
	{
		RailgunProjectileProperties p = RailgunHandler.getProjectileProperties(ammo);
		if(p==null||toTarget==null)
			return Float.NaN;

		CachedBallisticStats stats = AmmoBallisticsCache.get(AmmoBallistics.dragAfterMove(
				Arrays.asList("ie_railgun", p.gravity), 20D, p.gravity, 0.99D,
				EntityAmmoProjectile.MAX_TICKS
		));
		return stats.getDirectFireAngle(Math.hypot(toTarget.x, toTarget.z), toTarget.y);
	}

	/**
	 * Calculates target lead using cached impact time and then resolves the requested cached arc.
	 */
	public static float[] getInterceptionAngles(Vec3d shooterPos, Vec3d shooterVel,
												Vec3d targetPos, Vec3d targetVel,
												CachedBallisticStats ballistics,
												BallisticFireMode fireMode)
	{
		Vec3d relativeVelocity = targetVel.subtract(shooterVel);
		Vec3d direction = targetPos.subtract(shooterPos);
		if(ballistics==null||fireMode==null)
			return getUncompensatedAngles(direction);
		Vec3d solvedDirection = direction;
		BallisticSolution solution = null;

		for(int iteration = 0; iteration < 3; iteration++)
		{
			BallisticSolution next = ballistics.getSolution(Math.hypot(direction.x, direction.z),
					direction.y, fireMode);
			if(!next.isValid())
			{
				direction = solvedDirection;
				break;
			}
			solution = next;
			solvedDirection = direction;
			if(iteration < 2)
				direction = targetPos.add(relativeVelocity.scale(solution.getImpactTime())).subtract(shooterPos);
		}

		if(solution==null||!solution.isValid())
			return getUncompensatedAngles(direction);
		float yaw = (float)Math.toDegrees(Math.atan2(-direction.x, direction.z));
		return new float[]{MathHelper.wrapDegrees(yaw), -(float)solution.getElevation()};
	}

	private static float[] getUncompensatedAngles(Vec3d direction)
	{
		float yaw = (float)Math.toDegrees(Math.atan2(-direction.x, direction.z));
		float pitch = (float)-Math.toDegrees(Math.atan2(direction.y, Math.hypot(direction.x, direction.z)));
		return new float[]{MathHelper.wrapDegrees(yaw), pitch};
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

		//Do not display info for bullet cores
		if(ammo.isBulletCore(stack))
			return;

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
			ArrayList<String> componentTooltips = new ArrayList<>();
			int componentSlotsTaken = 0;
			if(components.length > 0)
			{
				componentTooltips.add(IIReference.COLOR_IMMERSIVE_ORANGE.getHexCol(I18n.format(IIReference.DESC_BULLETS+"components")));
				for(AmmoComponent comp : components)
				{
					componentTooltips.add("   "+comp.getTranslatedName());
					componentSlotsTaken += comp.getSlotsTaken();
				}
			}
			tooltip.add(I18n.format(IIReference.DESC_BULLETS+"component_slots", ammo.getCoreType(stack).getComponentSlots()-componentSlotsTaken));
			tooltip.addAll(componentTooltips);
		}

		//Performance tab
		IIAmmoProjectile annotation = IIUtils.getAnnotation(IIAmmoProjectile.class, ammo);
		if(annotation!=null&&ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LCONTROL, IIReference.DESC_BULLETS+"ballistics", tooltip))
		{
			//Ballistics section
			CachedBallisticStats stats = AmmoBallisticsCache.get(ammo, stack);

			tooltip.add(IIReference.COLOR_ENGINEERS_BLUE.getHexCol(I18n.format(IIReference.DESC_BULLETS+"performance")));
			tooltip.add(I18n.format(IIReference.DESC_BULLETS+"damage_dealt", ammo.getDamage()*core.getDamageModifier()*coreType.getDamageMod()));
			tooltip.add(I18n.format(IIReference.DESC_BULLETS+"standard_velocity", Utils.formatDouble(stats.getVelocity(), "0.###")));

			//Max distance tooltip
			if(annotation.artillery())
			{
				tooltip.add(I18n.format(IIReference.DESC_BULLETS+"max_artillery_range",
						Utils.formatDouble(stats.getMaxArtilleryRange(), "0.##")));
				tooltip.add(I18n.format(IIReference.DESC_BULLETS+"max_artillery_height",
						Utils.formatDouble(stats.getMaxHeightReached(), "0.##")));
				tooltip.add(I18n.format(IIReference.DESC_BULLETS+"max_direct_range",
						Utils.formatDouble(stats.getMaxDirectRange(), "0.##")));
			}
			else
				tooltip.add(I18n.format(IIReference.DESC_BULLETS+"max_range",
						Utils.formatDouble(stats.getMaxDirectRange(), "0.##")));

			//Penetration section
			tooltip.add(IIReference.COLOR_ENGINEERS_BLUE.getHexCol(I18n.format(IIReference.DESC_BULLETS+"armor_penetration")));

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
				.filter(c -> c!=ComponentRole.GENERAL_PURPOSE)
				.distinct()
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


	/**
	 * Applies an EMP effect and returns the positions of affected targets.
	 *
	 * @param world           effect world
	 * @param pos             effect centre
	 * @param radius          effect radius
	 * @param extractedEnergy energy removed from each target
	 * @return affected tile and entity positions
	 */
	public static List<Vec3d> applyEMPEffect(World world, BlockPos pos, float radius, int extractedEnergy)
	{
		Set<Vec3d> affectedTargets = new LinkedHashSet<>();
		Set<BlockPos> blocks = IIUtils.getBlocksInOrb(world, new BlockPos(pos), radius);
		for(BlockPos pp : blocks)
		{
			TileEntity te = world.getTileEntity(pp);
			if(te instanceof TileEntityMultiblockPart)
				te = ((TileEntityMultiblockPart<?>)te).master();

			if(te!=null)
			{
				boolean affected = false;
				if(te instanceof IIEInternalFluxHandler)
				{
					((IIEInternalFluxHandler)te).getFluxStorage().modifyEnergyStored(-extractedEnergy);
					affected = true;
				}
				else
					for(EnumFacing facing : EnumFacing.values())
						if(te.hasCapability(CapabilityEnergy.ENERGY, facing))
						{
							IEnergyStorage cap = te.getCapability(CapabilityEnergy.ENERGY, facing);
							if(cap!=null)
							{
								cap.extractEnergy(extractedEnergy, false);
								affected = true;
								break;
							}
						}

				if(affected)
					affectedTargets.add(new Vec3d(te.getPos()).addVector(0.5, 0.5, 0.5));
			}
		}

		for(EntityLivingBase e : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ()).grow(radius)))
			if(!(e instanceof ITeslaEntity))
			{
				affectedTargets.add(e.getPositionVector().addVector(0, e.height*0.5, 0));
				ElectricDamageSource dmgsrc = IEDamageSources.causeTeslaDamage(IEConfig.Machines.teslacoil_damage, false);

				if(!world.isRemote)
					if(dmgsrc.apply(e))
					{
						int prevFire = e.fire;
						e.setFire(prevFire+1);
						e.addPotionEffect(new PotionEffect(IEPotions.stunned, 128));
					}

				for(ItemStack stack : e.getArmorInventoryList())
					if((stack.hasCapability(CapabilityEnergy.ENERGY, null)))
					{
						IEnergyStorage cap = stack.getCapability(CapabilityEnergy.ENERGY, null);
						if(cap!=null)
							if(cap.extractEnergy(extractedEnergy, false)==0)
								if(ItemNBTHelper.hasKey(stack, "Energy"))
									ItemNBTHelper.setInt(stack, "Energy", Math.max(0, ItemNBTHelper.getInt(stack, "Energy")-extractedEnergy));
								else if(ItemNBTHelper.hasKey(stack, "energy"))
									ItemNBTHelper.setInt(stack, "energy", Math.max(0, ItemNBTHelper.getInt(stack, "energy")-extractedEnergy));
								else if(ItemNBTHelper.hasKey(stack, "Power"))
									ItemNBTHelper.setInt(stack, "Power", Math.max(0, ItemNBTHelper.getInt(stack, "Power")-extractedEnergy));
								else if(ItemNBTHelper.hasKey(stack, "power"))
									ItemNBTHelper.setInt(stack, "power", Math.max(0, ItemNBTHelper.getInt(stack, "power")-extractedEnergy));
					}
			}

		return new ArrayList<>(affectedTargets);
	}
}
