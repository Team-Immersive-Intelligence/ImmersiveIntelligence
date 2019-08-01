package pl.pabilo8.immersiveintelligence.api.bullets;

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

	//Bullet size, used in rendering
	float getSize();

	//Model name, client only
	String getModelName();


}
