package pl.pabilo8.immersiveintelligence.api.bullets;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public interface IBulletComponent
{
	//Gets the name of the component
	String getName();

	//Material used to create the component
	IngredientStack getMaterial();

	//Gets the density (density * amount = mass)
	float getDensity();

	//Runs when a bullet is exploding
	void onEffect(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet);

	//Gets the component role
	EnumComponentRole getRole();

	//Gets the component colour
	int getColour();

	default boolean showInManual()
	{
		return true;
	}

	default int getNBTColour(NBTTagCompound nbt)
	{
		return getColour();
	}

	default boolean hasTrail()
	{
		return false;
	}

	default void spawnParticleTrail(EntityBullet bullet, NBTTagCompound nbt)
	{

	}
}
