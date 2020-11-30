package pl.pabilo8.immersiveintelligence.common.ammunition_system.explosives;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class BulletComponentRDX implements IBulletComponent
{
	@Override
	public String getName()
	{
		return "rdx";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("materialHexogen");
	}

	@Override
	public float getDensity()
	{
		return 1.25f;
	}

	@Override
	public void onExplosion(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet)
	{
		world.createExplosion(bullet, pos.getX(), pos.getY(), pos.getZ(), amount*16f, true);
	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.EXPLOSIVE;
	}

	@Override
	public int getColour()
	{
		return 0xd2c294;
	}
}
