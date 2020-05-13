package pl.pabilo8.immersiveintelligence.common.bullets.shrapnel;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ShrapnelHandler;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityShrapnel;

/**
 * Created by Pabilo8 on 30-08-2019.
 */
public class BulletComponentShrapnel implements IBulletComponent
{
	public BulletComponentShrapnel(String material)
	{
		name = material;
	}

	String name;

	@Override
	public String getName()
	{
		return "shrapnel_"+name;
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dust"+Character.toUpperCase(name.charAt(0))+name.substring(1));
	}

	@Override
	public float getDensity()
	{
		return 1f;
	}

	@Override
	public void onExplosion(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet)
	{
		int lower = 0;

		Vec3d v = new Vec3d(0, -1, 0);
		Vec3d throwerPos = new Vec3d(pos.offset(EnumFacing.UP, 3));
		for(int i = 0; i < 50*amount; i++)
		{
			Vec3d vecDir = v.addVector(Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f);

			EntityShrapnel shrap = new EntityShrapnel(world, throwerPos.x+v.x*2, throwerPos.y+v.y*2,
					throwerPos.z+v.z*2, 0, 0, 0, name);
			shrap.motionX = vecDir.x*2;
			shrap.motionY = vecDir.y*0.05f;
			shrap.motionZ = vecDir.z*2;
			if(!world.isRemote)
				world.spawnEntity(shrap);
		}
	}

	@Override
	public float getPenetrationModifier(NBTTagCompound tag)
	{
		return 0;
	}

	@Override
	public float getDamageModifier(NBTTagCompound tag)
	{
		return 0;
	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.SHRAPNEL;
	}

	@Override
	public int getColour()
	{
		return ShrapnelHandler.registry.get(name).color;
	}
}
