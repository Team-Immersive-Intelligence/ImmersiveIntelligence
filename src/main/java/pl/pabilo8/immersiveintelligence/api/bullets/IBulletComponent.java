package pl.pabilo8.immersiveintelligence.api.bullets;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

/**
 * Created by Pabilo8 on 30-08-2019.
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
	void onExplosion(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet);

	//Penetration added by this component (0 - nothing added , 1 - add 1x penetration, 0.5 - add half the penetration)
	float getPenetrationModifier(NBTTagCompound tag);

	//Damage added by this component (0 - nothing added , 1 - add 1x damage, 0.5 - add half the damage)
	float getDamageModifier(NBTTagCompound tag);

	//Gets the component role
	EnumComponentRole getRole();

	//Gets the component colour
	int getColour();

	default int getNBTColour(NBTTagCompound nbt)
	{
		return getColour();
	}

	default boolean hasTrail()
	{
		return false;
	}

	default int getTrailColour(NBTTagCompound nbt)
	{
		return -1;
	}
}
