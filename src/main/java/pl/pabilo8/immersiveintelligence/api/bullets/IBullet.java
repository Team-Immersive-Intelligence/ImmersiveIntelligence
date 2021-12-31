package pl.pabilo8.immersiveintelligence.api.bullets;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 31-07-2019
 */
public interface IBullet
{
	/**
	 * @return Casing Type Name, ie. revolver_1bCal for a Revolver Cartridge
	 */
	String getName();

	/**
	 * @return Component effect multiplier, i.e. explosion size, smoke cloud duration, etc.
	 */
	float getComponentMultiplier();

	/**
	 * @return Gunpowder needed to make a bullet in mB
	 * @see pl.pabilo8.immersiveintelligence.api.crafting.DustStack
	 */
	default int getGunpowderNeeded()
	{
		return 0;
	}

	/**
	 * @return How many metal nuggets does it cost to produce a single bullet core
	 */
	int getCoreMaterialNeeded();

	/**
	 * @return The mass of the casing (used to calculate gravity in combination with the components
	 */
	float getInitialMass();

	/**
	 * @return Default speed modifier of the projectile (blocks/tick)
	 * Guns can apply additional velocity by using ${@link BulletHelper#createBullet(World, ItemStack, Vec3d, Vec3d, float)}.
	 */
	float getDefaultVelocity();

	/**
	 * @return Bullet caliber in Block-Caliber (bCal) = 1/16 of a block
	 */
	float getCaliber();

	/**
	 * @return A bullet model, it's required to extend {@link IBulletModel},<br>
	 * {@link pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo} is preferred, but you can use any renderer you like
	 */
	@Nonnull
	@SideOnly(Side.CLIENT)
	Class<? extends IBulletModel> getModel();

	/**
	 * @return How much damage the bullet deals (in half-hearts)
	 */
	float getDamage();

	/**
	 * @param amount of items to return
	 * @return the casing ItemStack, most of the time requested by guns after firing a bullet
	 */
	ItemStack getCasingStack(int amount);

	/**
	 * @return Returns allowed core types, these affect the final damage, penetration and overall performance
	 * @see EnumCoreTypes
	 */
	EnumCoreTypes[] getAllowedCoreTypes();

	/**
	 * @return A radius in which living entities will be given the {@link pl.pabilo8.immersiveintelligence.common.IIPotions#suppression} effect, 0 if there is no suppression effect.
	 */
	default float getSupressionRadius()
	{
		return 0;
	}

	/**
	 * @return Effect amplifier added by {@link #getSupressionRadius()}
	 */
	default int getSuppressionPower()
	{
		return 0;
	}

	/**
	 * Internal Item method
	 *
	 * @param stack bullet stack
	 * @return Bullet core from stack NBT
	 */
	IBulletCore getCore(ItemStack stack);

	/**
	 * Internal Item method
	 *
	 * @param stack bullet stack
	 * @return bullet core type from stack NBT
	 */
	EnumCoreTypes getCoreType(ItemStack stack);

	/**
	 * Internal Item method
	 *
	 * @param stack bullet stack
	 * @return bullet components in appropriate order from stack NBT
	 */
	IBulletComponent[] getComponents(ItemStack stack);

	/**
	 * Internal Item method
	 *
	 * @param stack bullet stack
	 * @return NBT Tags for each bullet component in appropriate order from stack NBT
	 */
	NBTTagCompound[] getComponentsNBT(ItemStack stack);

	/**
	 * @param stack        bullet stack
	 * @param tagCompounds for each bullet component in appropriate order
	 * @return stack with bullet component
	 */
	ItemStack setComponentNBT(ItemStack stack, NBTTagCompound... tagCompounds);

	/**
	 * Internal Item method
	 *
	 * @param stack bullet stack
	 * @return paint color in RGBint format from stack NBT
	 */
	int getPaintColor(ItemStack stack);

	/**
	 * Internal Item method
	 *
	 * @param stack bullet stack
	 * @param color to be applied on the bullet stack
	 * @return painted bullet stack
	 */
	ItemStack setPaintColour(ItemStack stack, int color);

	/**
	 * @param stack bullet stack
	 * @return mass of the bullet from stack NBT, calculated from core, components and initial casing  mass
	 */
	default float getMass(ItemStack stack)
	{
		if(stack.isEmpty())
			return 0.01f;
		return getCoreMass(getCore(stack), getComponents(stack));
	}

	/**
	 * @param core       bullet core
	 * @param components inside bullet core
	 * @return combined mass of core, components and initial casing mass
	 */
	default float getCoreMass(IBulletCore core, IBulletComponent[] components)
	{
		return (float)(getInitialMass()*(1f+core.getDensity()+Arrays.stream(components).mapToDouble(IBulletComponent::getDensity).sum()));
	}

	/**
	 * @return whether the bullet entity should load chunks while flying, true for artillery shells (6bCal, 8bCal, mortar)
	 */
	default boolean shouldLoadChunks()
	{
		return false;
	}

	/**
	 * Used for registering sprites to be used for items
	 *
	 * @param map the TextureMap to register to
	 */
	void registerSprites(TextureMap map);


	/**
	 * Proper method to get a bullet, <u><b>use this instead of creating ItemStacks and setting NBT</b></u>
	 *
	 * @return a bullet ItemStack for given parameters
	 */
	default ItemStack getBulletWithParams(IBulletCore core, EnumCoreTypes coreType, IBulletComponent... components)
	{
		String[] compNames = Arrays.stream(components).map(IBulletComponent::getName).toArray(String[]::new);
		return getBulletWithParams(core.getName(), coreType.getName(), compNames);
	}

	/**
	 * Same as {@link #getBulletWithParams(IBulletCore, EnumCoreTypes, IBulletComponent...)}, but uses String names
	 * Can be used instead of the above method, <u><b>but things may broke after bullet part names are changed</b></u>
	 */
	ItemStack getBulletWithParams(String core, String coreType, String... components);
}
