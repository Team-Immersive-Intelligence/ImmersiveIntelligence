package pl.pabilo8.immersiveintelligence.common.bullets;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCoreType;
import pl.pabilo8.immersiveintelligence.common.entity.EntityBullet;

/**
 * Created by Pabilo8 on 30-08-2019.
 */
public class BulletCoreTungsten implements IBulletCoreType
{
	@Override
	public String getName()
	{
		return "CoreTungsten";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("bulletCoreTungsten");
	}

	@Override
	public float getDensity()
	{
		return 0.35f;
	}

	@Override
	public void onExplosion(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet)
	{

	}

	@Override
	public float getPenetrationModifier(NBTTagCompound tag)
	{
		return 5f;
	}

	@Override
	public float getDamageModifier(NBTTagCompound tag)
	{
		return 3.5f;
	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.PIERCING;
	}

	@Override
	public float getExplosionModifier()
	{
		return 0.25f;
	}

	@Override
	public int getColour()
	{
		return 0x2e3035;
	}
}
