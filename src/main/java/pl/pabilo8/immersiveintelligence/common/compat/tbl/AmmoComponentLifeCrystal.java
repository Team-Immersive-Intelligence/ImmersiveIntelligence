package pl.pabilo8.immersiveintelligence.common.compat.tbl;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ITeslaEntity;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Carver (carver@iiteam.net)
 * @since 15.04.2026
 */

public class AmmoComponentLifeCrystal extends AmmoComponent
{
	public AmmoComponentLifeCrystal()
	{
		super("life_crystal", 0.82f, ComponentRole.SPECIAL, IIColor.fromPackedRGB(0xe2e2e2));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("life_crystal");
	}

	@Override
	public int getSlotsTaken()
	{
		return 3;
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{
		world.playSound(null, new BlockPos(pos), IISounds.explosionFlare, SoundCategory.NEUTRAL, 1, 0.5f);
		world.playSound(null, new BlockPos(pos), IESounds.tesla, SoundCategory.NEUTRAL, 1, 0.5f);
		SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("thebetweenlands:lightning"));
		world.playSound(null, pos.x, pos.y, pos.z, sound, SoundCategory.NEUTRAL, 1.0F, 0.5F);
		SoundEvent sound2 = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("minecraft:block_glass_break"));
		world.playSound(null, pos.x, pos.y, pos.z, sound2, SoundCategory.NEUTRAL, 1.0F, 0.3F);

		BlockPos ppos = new BlockPos(pos);
		new IIExplosion(world, owner, pos, dir,
				10, 50*multiplier, ComponentEffectShape.ORB, false, true, false)
				.doExplosion();

		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(5)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e1 : entities)
		{
			e1.hurtResistantTime = 0;
			e1.attackEntityFrom(DamageSource.MAGIC, 500);
			e1.addPotionEffect(new PotionEffect(IIPotions.radiation, 400, 10));
		}

		EntityLivingBase[] entities2 = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(15)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e1 : entities2)
		{
			e1.addPotionEffect(new PotionEffect(IEPotions.flashed, 40, 1));
			e1.addPotionEffect(new PotionEffect(IIPotions.radiation, 120, 0));
			e1.attackEntityFrom(DamageSource.MAGIC, 10);
		}

		float radius = multiplier*10;
		IIAmmoUtils.applyEMPEffect(world, new BlockPos(pos), radius, (int)(4000000*multiplier));
	}
}
