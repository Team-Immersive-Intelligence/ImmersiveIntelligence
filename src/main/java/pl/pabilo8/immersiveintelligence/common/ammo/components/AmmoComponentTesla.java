package pl.pabilo8.immersiveintelligence.common.ammo.components;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ITeslaEntity;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 10.07.2021
 */
public class AmmoComponentTesla extends AmmoComponent
{
	public AmmoComponentTesla()
	{
		super("tesla", 1f, ComponentRole.EMP, IIColor.fromPackedARGB(0x6b778a));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack(new ItemStack(IEContent.blockMetalDevice0, 1, BlockTypes_MetalDevice0.CAPACITOR_LV.getMeta()));
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentAmount, float multiplier, Entity owner)
	{
		float radius = multiplier*10;
		int extracted = (int)(4000000*multiplier);

		world.playSound(null, new BlockPos(pos), IISounds.explosionFlare, SoundCategory.NEUTRAL, 1, 0.5f);
		world.playSound(null, new BlockPos(pos), IESounds.tesla, SoundCategory.NEUTRAL, 1, 0.5f);

		for(int x = (int)(-radius/2); x < radius/2; x++)
			for(int y = (int)(-radius/2); y < radius/2; y++)
				for(int z = (int)(-radius/2); z < radius/2; z++)
				{
					BlockPos pp = new BlockPos(pos).add(x, y, z);
					TileEntity te = world.getTileEntity(pp);
					if(te instanceof TileEntityMultiblockPart)
						te = ((TileEntityMultiblockPart<?>)te).master();

					if(te!=null)
					{
						if(te instanceof TileEntityMultiblockMetal)
						{
							((TileEntityMultiblockMetal<?, ?>)te).energyStorage.extractEnergy(extracted, false);
						}
						else
						{
							for(EnumFacing facing : EnumFacing.values())
							{
								if((te.hasCapability(CapabilityEnergy.ENERGY, facing)))
								{
									IEnergyStorage cap = te.getCapability(CapabilityEnergy.ENERGY, facing);
									if(cap!=null)
									{
										cap.extractEnergy(extracted, false);
										break;
									}
								}
							}
						}
					}
				}

		for(EntityLivingBase e : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z).grow(radius)))
			if(!(e instanceof ITeslaEntity))
			{
				ElectricDamageSource dmgsrc = IEDamageSources.causeTeslaDamage(IEConfig.Machines.teslacoil_damage, false);

				if(!world.isRemote)
				{
					if(dmgsrc.apply(e))
					{
						int prevFire = e.fire;
						e.setFire(prevFire+1);
						e.addPotionEffect(new PotionEffect(IEPotions.stunned, 128));
					}
				}

				for(ItemStack stack : e.getArmorInventoryList())
				{
					if((stack.hasCapability(CapabilityEnergy.ENERGY, null)))
					{
						IEnergyStorage cap = stack.getCapability(CapabilityEnergy.ENERGY, null);
						if(cap!=null)
							if(cap.extractEnergy(extracted, false)==0)
							{
								//Get off Rolf's property before Rolf gets his  b e a t i n g  s t i c k
								//For items that don't want to be extracted from
								if(ItemNBTHelper.hasKey(stack, "Energy"))
									ItemNBTHelper.setInt(stack, "Energy", Math.max(0, ItemNBTHelper.getInt(stack, "Energy")-extracted));
								else if(ItemNBTHelper.hasKey(stack, "energy"))
									ItemNBTHelper.setInt(stack, "energy", Math.max(0, ItemNBTHelper.getInt(stack, "energy")-extracted));
								else if(ItemNBTHelper.hasKey(stack, "Power"))
									ItemNBTHelper.setInt(stack, "Power", Math.max(0, ItemNBTHelper.getInt(stack, "Power")-extracted));
								else if(ItemNBTHelper.hasKey(stack, "power"))
									ItemNBTHelper.setInt(stack, "power", Math.max(0, ItemNBTHelper.getInt(stack, "power")-extracted));

							}
					}
				}
			}

		//BulletHelper.suppress(world, pos.x, pos.y, pos.z, 10f*amount, (int)(255*amount));
	}
}
