package pl.pabilo8.immersiveintelligence.api.bullets;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 31-07-2019
 */
public interface IBullet
{
	//Casing Type Name, ie. Revolver Cartridge
	String getName();

	//Maximum component capactity (how much you can fit into the bullet)
	float getComponentCapacity();

	//Gunpowder needed to make a bullet
	int getGunpowderNeeded();

	//Core material needed to make a core for the bullet
	int getCoreMaterialNeeded();

	//The mass of the casing (used to calculate gravity in combination with the components
	float getInitialMass();

	//Bullet caliber in blocks (NOT bCal)
	float getCaliber();

	//Model name, client only
	@Nonnull
	Class<? extends IBulletModel> getModel();

	//How much damage the bullet deals (in half-hearts)
	float getDamage();

	//Return a casing ItemStack
	ItemStack getCasingStack(int amount);

	//Returns allowed core types
	EnumCoreTypes[] getAllowedCoreTypes();

	//Create a smoke cloud on bullet spawn
	default void doPuff(EntityBullet bullet)
	{

	}

	default float getSupressionRadius()
	{
		return 0;
	}

	default int getSuppressionPower()
	{
		return 0;
	}

	IBulletCore getCore(ItemStack stack);

	EnumCoreTypes getCoreType(ItemStack stack);

	IBulletComponent[] getComponents(ItemStack stack);

	NBTTagCompound[] getComponentsNBT(ItemStack stack);

	int getPaintColor(ItemStack stack);

	default boolean shouldLoadChunks()
	{
		return false;
	}

	void registerSprites(TextureMap map);

	default float getMass(ItemStack stack)
	{
		return getMass(getCore(stack), getComponents(stack));
	}

	default float getMass(IBulletCore core, IBulletComponent[] components)
	{
		return (float)(getInitialMass()*(1f+core.getDensity()+Arrays.stream(components).mapToDouble(IBulletComponent::getDensity).sum()));
	}

	default ItemStack getBulletWithParams(IBulletCore core, EnumCoreTypes coreType, IBulletComponent... components)
	{
		String[] compNames = Arrays.stream(components).map(IBulletComponent::getName).toArray(String[]::new);
		return getBulletWithParams(core.getName(), coreType.getName(), compNames);
	}

	ItemStack getBulletWithParams(String core, String coreType, String... components);

	ItemStack setPaintColour(ItemStack stack, int color);

	ItemStack setComponentNBT(ItemStack stack, NBTTagCompound... tagCompounds);
}
