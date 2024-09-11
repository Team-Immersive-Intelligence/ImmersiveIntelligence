package pl.pabilo8.immersiveintelligence.common.ammo.components.factory;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityGasCloud;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityIIChemthrowerShot;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 30-08-2019
 */
public class AmmoComponentFluid extends AmmoComponent
{
	Fluid fluid;

	public AmmoComponentFluid(Fluid fluid)
	{
		super((fluid.isGaseous()?"gas_": "fluid_")+IIStringUtil.toSnakeCase(fluid.getName()),
				Math.max(fluid.getDensity(), 0)/1000f,
				ComponentRole.CHEMICAL,
				IIColor.fromPackedARGB(fluid.getColor())
		);
		this.fluid = fluid;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getTranslatedName()
	{
		return fluid.getLocalizedName(null);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack(new FluidStack(fluid, 1000));
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentAmount, float multiplier, Entity owner)
	{
		if(world.isRemote)
			return;

		Vec3d v = new Vec3d(0, -1, 0);
		BlockPos p = new BlockPos(pos);
		Vec3d throwerPos = new Vec3d(p.offset(EnumFacing.UP, 3));
		if(fluid.isGaseous())
		{
			EntityGasCloud gasCloud = new EntityGasCloud(world, throwerPos.x+v.x*2, throwerPos.y+v.y*2,
					throwerPos.z+v.z*2, new FluidStack(fluid, (int)(multiplier*1000)));
			world.spawnEntity(gasCloud);
		}
		else
		{
			//greater/equal to howi shell
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
}
