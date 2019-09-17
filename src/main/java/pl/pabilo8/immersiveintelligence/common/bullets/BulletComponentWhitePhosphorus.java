package pl.pabilo8.immersiveintelligence.common.bullets;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityBullet;

/**
 * Created by Pabilo8 on 30-08-2019.
 */
public class BulletComponentWhitePhosphorus implements IBulletComponent
{
	@Override
	public String getName()
	{
		return "white_phosphorus";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dustWhitePhosphorus");
	}

	@Override
	public float getDensity()
	{
		return 1f;
	}

	@Override
	public void onExplosion(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet)
	{
		//TODO: White Phosphorus
		//world.createExplosion(bullet, pos.getX(), pos.getY(), pos.getZ(), amount*8f, true);
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
		return EnumComponentRole.INCENDIARY;
	}
}
