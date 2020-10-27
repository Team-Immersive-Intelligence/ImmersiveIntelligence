package pl.pabilo8.immersiveintelligence.common.ammunition_system;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class BulletComponentTracerPowder implements IBulletComponent
{
	@Override
	public String getName()
	{
		return "tracer_powder";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack(new ItemStack(CommonProxy.item_tracer_powder));
	}

	@Override
	public float getDensity()
	{
		return 1f;
	}

	@Override
	public void onExplosion(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet)
	{

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
		return EnumComponentRole.TRACER;
	}

	@Override
	public int getColour()
	{
		return 0xffffff;
	}

	@Override
	public boolean hasTrail()
	{
		return true;
	}

	@Override
	public int getTrailColour(NBTTagCompound nbt)
	{
		return nbt.hasKey("colour")?nbt.getInteger("colour"): 0xffffff;
	}

	@Override
	public int getNBTColour(NBTTagCompound nbt)
	{
		return nbt.hasKey("colour")?nbt.getInteger("colour"): 0xffffff;
	}
}
