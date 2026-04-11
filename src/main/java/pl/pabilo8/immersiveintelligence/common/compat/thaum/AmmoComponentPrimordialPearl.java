package pl.pabilo8.immersiveintelligence.common.compat.thaum;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ITeslaEntity;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityIIChemthrowerShot;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 11.04.2026
 * @since 08.04.2026
 */

public class AmmoComponentPrimordialPearl extends AmmoComponent

{
	public AmmoComponentPrimordialPearl()
	{
		super("primordial pearl", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0xff3dae));
	}

	@Override

	public IngredientStack getMaterial()
	{
		Item primordialpearl = Item.REGISTRY.getObject(new ResourceLocation("thaumcraft", "primordial_pearl"));
		return new IngredientStack(new ItemStack(primordialpearl, 1));
	}

	@Override

	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{
		float radius = multiplier*10;
		int extracted = (int)(2000000*multiplier);

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
		BlockPos ppos = new BlockPos(pos);
		new IIExplosion(world, owner, pos, dir, 30*componentSize, 50*multiplier, shape, false, componentSize > 0.125f, false)
				.doExplosion();

		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(50*multiplier)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			Potion fluxtaint = Potion.REGISTRY.getObject(ResLoc.of("thaumcraft:flux_taint"));
			Potion fluxexhaust = Potion.REGISTRY.getObject(ResLoc.of("thaumcraft:vis_exhaust"));
			Potion fluxexhaustinfect = Potion.REGISTRY.getObject(ResLoc.of("thaumcraft:infvisexhaust"));
			Potion unhunger = Potion.REGISTRY.getObject(ResLoc.of("thaumcraft:unhunger"));
			Potion thaummarhia = Potion.REGISTRY.getObject(ResLoc.of("thaumcraft:thaumarhia"));

			e.addPotionEffect(new PotionEffect(fluxtaint, 460, 4));
			e.addPotionEffect(new PotionEffect(fluxexhaust, 4000, 10));
			e.addPotionEffect(new PotionEffect(fluxexhaustinfect, 4000, 10));
			e.addPotionEffect(new PotionEffect(unhunger, 460, 0));
			e.addPotionEffect(new PotionEffect(thaummarhia, 120, 8));

			e.hurtResistantTime = 0;
		}

		Entity e = EntityList.createEntityByIDFromName(ResLoc.of("thaumcraft:flux_rift"), world);
		e.setPosition(pos.z, pos.y, pos.z);
		world.spawnEntity(e);
		world.spawnEntity(e);
		world.spawnEntity(e);
		world.spawnEntity(e);
		world.spawnEntity(e);

		Fluid fluid = FluidRegistry.getFluidStack("flux_goo", 10000).getFluid();
		Block fluidBlock = fluid.getBlock();

		if(world.isRemote)
			return;

		Vec3d v = new Vec3d(0, -1, 0);
		BlockPos p = new BlockPos(pos);
		Vec3d throwerPos = new Vec3d(p.offset(EnumFacing.UP, 3));

		if(multiplier >= 0.5&&fluid.canBePlacedInWorld())
			for(int i = 0; i < 5; i++)
				if(world.isAirBlock(p.up(i)))
					world.setBlockState(p.up(i), fluid.getBlock().getDefaultState());
		for(int i = 0; i < 100*multiplier; i++)
		{
			Vec3d vecDir = v.addVector(Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f);

			world.spawnEntity(
					new EntityIIChemthrowerShot(world, throwerPos.x+v.x*2, throwerPos.y+v.y*2,
							throwerPos.z+v.z*2, 0, 0, 0, new FluidStack(fluid, (int)(multiplier*1000)))
							.withMotion(vecDir.x*2, vecDir.y*0.05f, vecDir.z*2)
			);
			EntityIIChemthrowerShot shot = new EntityIIChemthrowerShot(world, throwerPos.x+v.x*2, throwerPos.y+v.y*2,
					throwerPos.z+v.z*2, 0, 0, 0, new FluidStack(fluid, (int)(multiplier*1000)));
			shot.motionX = vecDir.x*2;
			shot.motionY = vecDir.y*0.05f;
			shot.motionZ = vecDir.z*2;
			world.spawnEntity(shot);

		}
	}
}

//primordial_pearl_0
//itemeldritchobject:3
