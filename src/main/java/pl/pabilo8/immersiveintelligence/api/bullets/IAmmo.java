package pl.pabilo8.immersiveintelligence.api.bullets;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IAdvancedTooltipItem;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 31-07-2019
 */
public interface IAmmo extends IAdvancedTooltipItem
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
	 * Guns can apply additional velocity by using ${@link AmmoUtils#createBullet(World, ItemStack, Vec3d, Vec3d, float)}.
	 */
	float getDefaultVelocity();

	/**
	 * @return Bullet caliber in Block-Caliber (bCal) = 1/16 of a block
	 */
	float getCaliber();

	/**
	 * @return A bullet model, it's required to extend {@link IBulletModel},<br>
	 * {@link pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo} is preferred, but you can use any renderer you like
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
	IAmmoCore getCore(ItemStack stack);

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
	IAmmoComponent[] getComponents(ItemStack stack);

	/**
	 * Internal Item method
	 *
	 * @param stack        bullet stack
	 * @param component    added component
	 * @param componentNBT
	 * @return bullet components in appropriate order from stack NBT
	 */
	void addComponents(ItemStack stack, IAmmoComponent component, NBTTagCompound componentNBT);

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
	default float getCoreMass(IAmmoCore core, IAmmoComponent[] components)
	{
		return (float)(getInitialMass()*(1f+core.getDensity()+Arrays.stream(components).mapToDouble(IAmmoComponent::getDensity).sum()));
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
	default ItemStack getBulletWithParams(IAmmoCore core, EnumCoreTypes coreType, IAmmoComponent... components)
	{
		String[] compNames = Arrays.stream(components).map(IAmmoComponent::getName).toArray(String[]::new);
		return getBulletWithParams(core.getName(), coreType.getName(), compNames);
	}

	/**
	 * Proper method to get a bullet core, <u><b>use this instead of creating ItemStacks and setting NBT</b></u>
	 *
	 * @return a bullet core ItemStack for given parameters
	 */
	default ItemStack getBulletCore(IAmmoCore core, EnumCoreTypes coreType)
	{
		return getBulletCore(core.getName(), coreType.getName());
	}

	/**
	 * Same as {@link #getBulletWithParams(IAmmoCore, EnumCoreTypes, IAmmoComponent...)}, but uses String names
	 * Can be used instead of the above method, <u><b>but things may broke after bullet part names are changed</b></u>
	 */
	ItemStack getBulletWithParams(String core, String coreType, String... components);

	/**
	 * Same as {@link #getBulletWithParams(IAmmoCore, EnumCoreTypes, IAmmoComponent...)}, but uses String names
	 * Can be used instead of the above method, <u><b>but things may broke after bullet part names are changed</b></u>
	 */
	ItemStack getBulletCore(String core, String coreType);

	/**
	 * @param stack stack to be checked
	 * @return whether the stack is a bullet core
	 */
	boolean isBulletCore(ItemStack stack);

	/**
	 * @return Returns allowed fuse types, these affect the way a bullet entity will detect collision
	 * @see EnumFuseTypes
	 */
	EnumFuseTypes[] getAllowedFuseTypes();

	/**
	 * @param stack
	 * @param type  type of the fuse
	 */
	void setFuseType(ItemStack stack, EnumFuseTypes type);

	/**
	 * @param stack
	 * @return fuse type used in the bullet
	 */
	EnumFuseTypes getFuseType(ItemStack stack);

	/**
	 * @param stack bullet core stack to be checked
	 * @return whether a component can be added to the stack
	 */
	default boolean hasFreeComponentSlots(ItemStack stack)
	{
		return getComponents(stack).length < getCoreType(stack).getComponentSlots();
	}

	/**
	 * @param stack ammunition stack
	 * @return time in ticks for timed fuse, distance in blocks for proximity fuse
	 */
	default int getFuseParameter(ItemStack stack)
	{
		return ItemNBTHelper.getInt(stack, "fuse_param");
	}

	/**
	 * Sets a parameter for the fuse - time in ticks for timed fuse, distance in blocks for proximity fuse
	 *
	 * @param stack ammunition stack the value is applied to
	 * @param p     value to be set
	 */
	default void setFuseParameter(ItemStack stack, int p)
	{
		ItemNBTHelper.setInt(stack, "fuse_param", p);
	}

	/**
	 * @return whether this ammo is a projectile (and not, i.e. a mine)
	 */
	default boolean isProjectile()
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	default void addAdvancedInformation(ItemStack stack, int offsetX, List<Integer> offsetsY)
	{
		IAmmoComponent[] components = getComponents(stack);
		if(components.length > 0&&ItemTooltipHandler.canExpandTooltip(Keyboard.KEY_LSHIFT))
			ItemTooltipHandler.drawItemList(offsetX, offsetsY.get(0),
					Arrays.stream(components)
							.map(c -> c.getMaterial().getExampleStack())
							.toArray(ItemStack[]::new)
			);
	}
}
