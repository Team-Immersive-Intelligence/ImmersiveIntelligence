package pl.pabilo8.immersiveintelligence.api.bullets;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

/**
 * Created by Pabilo8 on 31-07-2019.
 */
public interface IBulletCasingType
{
	//Casing Type Name, ie. Revolver Cartridge
	String getName();

	//Maximum component capactity (how much you can fit into the bullet)
	float getComponentCapacity();

	//Gunpowder needed to make a bullet
	int getGunpowderNeeded();

	//Core materil needed to make a core for the bullet
	int getCoreMaterialNeeded();

	//The mass of the casing (used to calculate gravity in combination with the components
	float getInitialMass();

	//Bullet size, used in rendering
	float getSize();

	//Stacksize
	int getStackSize();

	//Model name, client only
	Class<? extends IBulletModel> getModel();

	//How many blocks the bullet can penetrate (distance * hardness * density)
	//Usually described in quantity of stone blocks penetrated (as stone has a density of 1.0)
	float getPenetration();

	//How much damage the bullet deals (in half-hearts)
	float getDamage();

	//Return a casing ItemStack
	ItemStack getStack(int amount);

	//Whether a bullet is throwable by players
	boolean isThrowable();

	//Create a smoke cloud on bullet spawn
	default void doPuff(EntityBullet bullet)
	{
	}
}
