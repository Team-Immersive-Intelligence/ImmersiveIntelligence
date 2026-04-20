package pl.pabilo8.immersiveintelligence.common.compat.ee;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ITeslaEntity;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
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
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Carver (carver@iiteam.net)
 * @since 18.04.2026
 * @updated 18.04.2026
 */

public class AmmoComponentLustrianCrystal extends AmmoComponent
{
	public AmmoComponentLustrianCrystal()
	{
		super("lustrian_crystal", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0xba3641));
	}

	@Override

	public IngredientStack getMaterial()
	{
		Item red_crystal = Item.REGISTRY.getObject(new ResourceLocation("ee", "red_crystal_item"));
		return new IngredientStack(new ItemStack(red_crystal, 1));
	}

	@Override

	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{

		SoundEvent sound1 = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("ee:crystal:break"));
		world.playSound(null, pos.x, pos.y, pos.z, sound1, SoundCategory.NEUTRAL, 2.0F, 1.0F);

		BlockPos ppos = new BlockPos(pos);
		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(8*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			//Unstable, also used for machinery. PotionCorrupted
			Potion corrupted = Potion.REGISTRY.getObject(ResLoc.of("ee:corrupted"));

			e.addPotionEffect(new PotionEffect(corrupted, 180, 2));
			e.attackEntityFrom(DamageSource.MAGIC, 8);

		}
		float radius = multiplier*10;
		IIAmmoUtils.applyEMPEffect(world, new BlockPos(pos), radius, (int)(2000000*multiplier));
	}

}
