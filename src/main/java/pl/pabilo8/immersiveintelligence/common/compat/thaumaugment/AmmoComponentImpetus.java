package pl.pabilo8.immersiveintelligence.common.compat.thaumaugment;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ItemIITracerPowder;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import javax.vecmath.Vector2f;
//import thaumcraft.api.aura.AuraHelper;

/**
 * @author Carver (carver@iiteam.net)
 * @since 12.04.2026
 */

public class AmmoComponentImpetus extends AmmoComponent
{
	public AmmoComponentImpetus()
	{
		super("impetus", 0.25f, ComponentRole.SPECIAL, IIColor.fromPackedRGB(0x000b10));
	}

	@Override
	public int getSlotsTaken()
	{
		return 3;
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("impetus");
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{
		IIAmmoUtils.applyEMPEffect(world, new BlockPos(pos), multiplier*8, (int)(32000000*multiplier));

		new IIExplosion(world, owner, pos, dir,
				4*componentSize, 100*multiplier, ComponentEffectShape.ORB, false, true, false)
				.doExplosion();

		//AuraHelper.polluteAura(world, pos, 100.0F, true);
		//AuraHelper.drainVis(world, pos, 200.0F, false);

		SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("thaumicaugmentation:impulse_cannon_railgun"));
		world.playSound(null, pos.x, pos.y, pos.z, sound, SoundCategory.NEUTRAL, 8.0F, 0.5F);

		Entity rift = EntityList.createEntityByIDFromName(ResLoc.of("thaumcraft:fluxrift"), world);
		rift.setPosition(pos.z, pos.y, pos.z);
		world.spawnEntity(rift);

		BlockPos ppos = new BlockPos(pos);

		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(5*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			e.addPotionEffect(new PotionEffect(IEPotions.flashed, 80, 5));
			e.hurtResistantTime = 0;
			e.attackEntityFrom(IIDamageSources.RADIATION_DAMAGE, 100);
			e.addPotionEffect(new PotionEffect(IIPotions.radiation, 100, 4));
		}
	}

	@Override
	public boolean spawnParticleTrail(EntityAmmoBase<?> ammo, NBTTagCompound nbt)
	{
		IIColor color = nbt.hasKey(ItemIITracerPowder.NBT_TRACER_COLOUR)?IIColor.fromPackedRGB(nbt.getInteger(ItemIITracerPowder.NBT_TRACER_COLOUR)): IIColor.BLACK;
		ParticleRegistry.spawnParticle("ammo/tracer", ammo.getPositionVector(), IIEntityUtils.getEntityMotion(ammo),
						new Vector2f((float)Math.toRadians(ammo.rotationYaw), (float)Math.toRadians(ammo.rotationPitch+90)))
				.withProperty(ParticleProperties.COLOR, color)
				.withProperty(ParticleProperties.SIZE, ammo.getAmmoType().getCaliber()/8f)
				.withProperty(ParticleProperties.MAX_LIFETIME, 2000);
		return true;
	}

	@Override
	public IIColor getColor(NBTTagCompound nbt)
	{
		return nbt!=null&&nbt.hasKey(ItemIITracerPowder.NBT_TRACER_COLOUR)?IIColor.fromPackedRGB(nbt.getInteger(ItemIITracerPowder.NBT_TRACER_COLOUR)): IIColor.BLACK;
	}
}

