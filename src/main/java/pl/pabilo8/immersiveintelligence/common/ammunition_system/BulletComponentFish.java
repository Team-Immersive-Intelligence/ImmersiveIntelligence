package pl.pabilo8.immersiveintelligence.common.ammunition_system;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.entities.EntityChemthrowerShot;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

/**
 * @author Pabilo8
 * @since 19.03.2021
 */
public class BulletComponentFish implements IBulletComponent
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
	public void onEffect(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet)
	{
		Vec3d v = new Vec3d(0, -1, 0);
		Vec3d throwerPos = new Vec3d(pos.offset(EnumFacing.UP, 3));
		for(int i = 0; i < 100*amount; i++)
		{
			Vec3d vecDir = v.addVector(Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f);

			EntityChemthrowerShot shot = new EntityChemthrowerShot(world, throwerPos.x+v.x*2, throwerPos.y+v.y*2,
					throwerPos.z+v.z*2, 0, 0, 0, new FluidStack(FluidRegistry.WATER, 1));
			shot.motionX = vecDir.x*2;
			shot.motionY = vecDir.y*0.05f;
			shot.motionZ = vecDir.z*2;
			world.spawnEntity(shot);
		}
		Utils.dropStackAtPos(world,pos.up(),new ItemStack(Items.FISH));
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

}
