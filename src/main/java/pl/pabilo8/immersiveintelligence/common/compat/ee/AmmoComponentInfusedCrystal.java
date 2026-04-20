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
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

public class AmmoComponentInfusedCrystal extends AmmoComponent
{
	public AmmoComponentInfusedCrystal()
	{
		super("infused_crystal", 1.25f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0x4a0c1f));
	}

	@Override

	public IngredientStack getMaterial()
	{
		Item infused_crystal = Item.REGISTRY.getObject(new ResourceLocation("ee", "infused_crystal"));
		return new IngredientStack(new ItemStack(infused_crystal, 1));
	}

	@Override

	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{

		new IIExplosion(world, owner, pos, dir,
				9*componentSize, 10*multiplier, shape, false, componentSize > 0.125f, false)
				.doExplosion();

		SoundEvent sound1 = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("ee:crystal:break"));
		world.playSound(null, pos.x, pos.y, pos.z, sound1, SoundCategory.NEUTRAL, 2.0F, 1.0F);

		BlockPos ppos = new BlockPos(pos);
		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(8*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			//primary effect is gravity altercation
			Potion levitation = Potion.REGISTRY.getObject(ResLoc.of("minecraft:levitation"));
			Potion corrupted = Potion.REGISTRY.getObject(ResLoc.of("ee:corrupted"));

			e.addPotionEffect(new PotionEffect(corrupted, 180, 2));
			e.addPotionEffect(new PotionEffect(levitation, 180, 3));
			e.attackEntityFrom(DamageSource.MAGIC, 10);
		}
		float radius = multiplier*10;
		IIAmmoUtils.applyEMPEffect(world, new BlockPos(pos), radius, (int)(4000000*multiplier));
	}
}
