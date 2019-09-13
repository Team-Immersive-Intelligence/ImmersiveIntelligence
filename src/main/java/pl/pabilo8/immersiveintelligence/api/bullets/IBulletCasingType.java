package pl.pabilo8.immersiveintelligence.api.bullets;

import net.minecraft.item.ItemStack;

/**
 * Created by Pabilo8 on 31-07-2019.
 */
public interface IBulletCasingType
{
	//Casing Type Name, ie. Revolver Cartridge
	String getName();

	//Maximum capactity of the first component (how much you can fit into the bullet)
	float getFirstComponentCapacity();

	//Maximum capactity of the second component (how much you can fit into the bullet)
	float getSecondComponentCapacity();

	//Gunpowder needed to make a bullet
	int getGunpowderNeeded();

	//Brass needed to make a casing
	int getBrassNeeded();

	//The mass of the casing (used to calculate gravity in combination with the components
	float getInitialMass();

	//Bullet size, used in rendering
	float getSize();

	//Stacksize
	int getStackSize();

	//Model name, client only
	String getModelName();

	//How many blocks the bullet can penetrate
	float getPenetration();

	//How much damage the bullet deals (in half-hearts)
	float getDamage();

	//Return a casing ItemStack
	ItemStack getStack(int amount);

}
