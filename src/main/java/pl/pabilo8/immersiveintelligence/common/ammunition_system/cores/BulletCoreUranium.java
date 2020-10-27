package pl.pabilo8.immersiveintelligence.common.ammunition_system.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCoreType;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class BulletCoreUranium implements IBulletCoreType
{
	@Override
	public String getName()
	{
		return "CoreUranium";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("plateUranium");
	}

	@Override
	public float getDensity()
	{
		return 0.45f;
	}

	@Override
	public void onExplosion(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet)
	{

	}

	@Override
	public float getPenetrationModifier(NBTTagCompound tag)
	{
		return 8f;
	}

	@Override
	public float getDamageModifier(NBTTagCompound tag)
	{
		return 6.5f;
	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.PIERCING;
	}

	@Override
	public float getExplosionModifier()
	{
		return 0.75f;
	}

	@Override
	public int getColour()
	{
		return 0x659269;
	}
}
