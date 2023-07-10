package pl.pabilo8.immersiveintelligence.common.ammo;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityIIChemthrowerShot;

/**
 * @author Pabilo8
 * @since 19.03.2021
 */
public class AmmoComponentFish implements IAmmoComponent
{
	//cheers, Sheperd
	@Override
	public String getName()
	{
		return "fish";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack(new ItemStack(Items.FISH, 1, 0));
	}

	@Override
	public float getDensity()
	{
		return 0.125f;
	}

	@Override
	public void onEffect(float amount, EnumCoreTypes coreType, NBTTagCompound tag, Vec3d pos, Vec3d dir, World world)
	{
		Vec3d v = new Vec3d(0, -1, 0);
		Vec3d throwerPos = pos.addVector(0, 3, 0);
		for(int i = 0; i < 100*amount; i++)
		{
			Vec3d vecDir = v.addVector(Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f);

			EntityIIChemthrowerShot shot = new EntityIIChemthrowerShot(world, throwerPos.x+v.x*2, throwerPos.y+v.y*2, throwerPos.z+v.z*2, 0, 0, 0, new FluidStack(FluidRegistry.WATER, 1));
			shot.motionX = vecDir.x*2;
			shot.motionY = vecDir.y*0.05f;
			shot.motionZ = vecDir.z*2;
			world.spawnEntity(shot);
		}
		Utils.dropStackAtPos(world, new BlockPos(pos).up(), new ItemStack(Items.FISH));
	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.SPECIAL;
	}

	@Override
	public int getColour()
	{
		return 0x6b778a;
	}

	@Override
	public boolean showInManual()
	{
		return false;
	}

}
