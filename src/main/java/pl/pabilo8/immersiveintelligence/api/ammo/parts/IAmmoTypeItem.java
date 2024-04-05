package pl.pabilo8.immersiveintelligence.api.ammo.parts;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseTypes;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IAdvancedTooltipItem;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @param <T> entity created by this ammo
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 30.12.2023
 */
public interface IAmmoTypeItem<T extends IAmmoType<T, E>, E extends EntityAmmoBase<? super E>> extends IAmmoType<T, E>, IAdvancedTooltipItem
{
	//--- NBT Keys ---//
	String NBT_CORE = "core";
	String NBT_CORE_TYPE = "core_type";
	String NBT_COMPONENTS = "components";
	String NBT_COMPONENTS_NBT = "component_nbt";
	String NBT_PAINT = "paint_color";
	String NBT_FUSE = "fuse";
	String NBT_FUSE_PARAM = "fuse_param";

	//--- Materials and Crafting ---//

	//TODO: 18.03.2024 replace with "propellant"

	/**
	 * @return Gunpowder needed to make a bullet in mB
	 * @see pl.pabilo8.immersiveintelligence.api.crafting.DustStack
	 */
	default int getGunpowderNeeded()
	{
		return 0;
	}

	/**
	 * @return How many metal nuggets does it cost to produce a single {@link AmmoCore} of this ammo type
	 */
	int getCoreMaterialNeeded();


	//--- ItemStack ---//

	default void makeDefault(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_CORE))
			ItemNBTHelper.setString(stack, NBT_CORE, AmmoRegistry.MISSING_CORE.getName());
		if(!ItemNBTHelper.hasKey(stack, NBT_CORE_TYPE))
			ItemNBTHelper.setString(stack, NBT_CORE_TYPE, getAllowedCoreTypes()[0].getName());
		if(!isBulletCore(stack)&&!ItemNBTHelper.hasKey(stack, NBT_FUSE))
			ItemNBTHelper.setString(stack, NBT_FUSE, getAllowedFuseTypes()[0].getName());
	}

	/**
	 * @param stack bullet stack
	 * @return mass of the bullet from stack NBT, calculated from core, components and initial casing  mass
	 */
	@Override
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
	@Override
	default float getCoreMass(AmmoCore core, AmmoComponent[] components)
	{
		return (float)(getInitialMass()*(1f+core.getDensity()+Arrays.stream(components).mapToDouble(AmmoComponent::getDensity).sum()));
	}

	/**
	 * @return Returns allowed fuse types, these affect the way a bullet entity will detect collision
	 * @see FuseTypes
	 */
	FuseTypes[] getAllowedFuseTypes();

	/**
	 * @param stack bullet core stack to be checked
	 * @return whether a component can be added to the stack
	 */
	@Override
	default boolean hasFreeComponentSlots(ItemStack stack)
	{
		return getComponents(stack).length < getCoreType(stack).getComponentSlots();
	}

	@Override
	default AmmoComponent[] getComponents(ItemStack stack)
	{
		return EasyNBT.wrapNBT(stack)
				.streamList(NBTTagString.class, NBT_COMPONENTS, EasyNBT.TAG_STRING)
				.map(NBTTagString::getString)
				.map(AmmoRegistry::getComponent)
				.filter(Objects::nonNull)
				.toArray(AmmoComponent[]::new);
	}

	@Override
	default NBTTagCompound[] getComponentsNBT(ItemStack stack)
	{
		return EasyNBT.wrapNBT(stack)
				.streamList(NBTTagCompound.class, NBT_COMPONENTS_NBT, EasyNBT.TAG_COMPOUND)
				.toArray(NBTTagCompound[]::new);
	}

	@Override
	default ItemStack setComponentNBT(ItemStack stack, NBTTagCompound... tagCompounds)
	{
		EasyNBT.wrapNBT(stack).withList(NBT_COMPONENTS, (Object[])tagCompounds);
		return stack;
	}

	@Override
	default void addComponents(ItemStack stack, AmmoComponent component, NBTTagCompound componentNBT)
	{
		EasyNBT.wrapNBT(stack)
				.appendList(NBT_COMPONENTS, EasyNBT.TAG_STRING, new NBTTagString(component.getName()))
				.appendList(NBT_COMPONENTS_NBT, EasyNBT.TAG_COMPOUND, componentNBT.copy());
	}

	@Override
	default ItemStack setPaintColour(ItemStack stack, int color)
	{
		ItemNBTHelper.setInt(stack, NBT_PAINT, color);
		return stack;
	}

	@Override
	default int getPaintColor(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, NBT_PAINT))
			return ItemNBTHelper.getInt(stack, NBT_PAINT);
		return -1;
	}

	@Override
	default void setFuseType(ItemStack stack, FuseTypes type)
	{
		ItemNBTHelper.setString(stack, NBT_FUSE, type.getName());
	}

	@Override
	default FuseTypes getFuseType(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_FUSE))
			makeDefault(stack);
		return FuseTypes.v(ItemNBTHelper.getString(stack, NBT_FUSE));
	}

	@Override
	default AmmoCore getCore(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_CORE))
			makeDefault(stack);
		return AmmoRegistry.getCore(ItemNBTHelper.getString(stack, NBT_CORE));
	}

	@Override
	default CoreTypes getCoreType(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_CORE_TYPE))
			makeDefault(stack);
		return CoreTypes.v(ItemNBTHelper.getString(stack, NBT_CORE_TYPE));
	}

	/**
	 * @param stack ammunition stack
	 * @return time in ticks for timed fuse, distance in blocks for proximity fuse
	 */
	@Override
	default int getFuseParameter(ItemStack stack)
	{
		return ItemNBTHelper.getInt(stack, NBT_FUSE_PARAM);
	}

	/**
	 * Sets a parameter for the fuse - time in ticks for timed fuse, distance in blocks for proximity fuse
	 *
	 * @param stack ammunition stack the value is applied to
	 * @param p     value to be set
	 */
	@Override
	default void setFuseParameter(ItemStack stack, int p)
	{
		ItemNBTHelper.setInt(stack, NBT_FUSE_PARAM, p);
	}

	//--- ItemStack Getters ---//

	/**
	 * Proper method to get a bullet, <u><b>use this instead of creating ItemStacks and setting NBT</b></u>
	 *
	 * @return a bullet ItemStack for given parameters
	 */
	default ItemStack getBulletWithParams(AmmoCore core, CoreTypes coreType, AmmoComponent... components)
	{
		String[] compNames = Arrays.stream(components).map(AmmoComponent::getName).toArray(String[]::new);
		return getBulletWithParams(core.getName(), coreType.getName(), compNames);
	}

	/**
	 * Proper method to get a bullet core, <u><b>use this instead of creating ItemStacks and setting NBT</b></u>
	 *
	 * @return a bullet core ItemStack for given parameters
	 */
	default ItemStack getBulletCore(AmmoCore core, CoreTypes coreType)
	{
		return getBulletCore(core.getName(), coreType.getName());
	}

	/**
	 * Same as {@link #getBulletWithParams(AmmoCore, CoreTypes, AmmoComponent...)}, but uses String names
	 * Can be used instead of the above method, <u><b>but things may broke after bullet part names are changed</b></u>
	 */
	ItemStack getBulletWithParams(String core, String coreType, String... components);

	/**
	 * Same as {@link #getBulletWithParams(AmmoCore, CoreTypes, AmmoComponent...)}, but uses String names
	 * Can be used instead of the above method, <u><b>but things may broke after bullet part names are changed</b></u>
	 */
	ItemStack getBulletCore(String core, String coreType);

	/**
	 * @param stack stack to be checked
	 * @return whether the stack is a bullet core
	 */
	boolean isBulletCore(ItemStack stack);

	//--- IAdvancedTooltipItem ---//

	@SideOnly(Side.CLIENT)
	@Override
	default void addAdvancedInformation(ItemStack stack, int offsetX, List<Integer> offsetsY)
	{
		AmmoComponent[] components = getComponents(stack);
		if(components.length > 0&&ItemTooltipHandler.canExpandTooltip(Keyboard.KEY_LSHIFT))
			ItemTooltipHandler.drawItemList(offsetX, offsetsY.get(0),
					Arrays.stream(components)
							.map(c -> c.getMaterial().getExampleStack())
							.toArray(ItemStack[]::new)
			);
	}

	/**
	 * Annotation for in II Ammo Types that are projectiles
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface IIAmmoProjectile
	{

	}
}
