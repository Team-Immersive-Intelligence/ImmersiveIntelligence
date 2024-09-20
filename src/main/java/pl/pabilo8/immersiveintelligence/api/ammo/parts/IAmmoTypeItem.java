package pl.pabilo8.immersiveintelligence.api.ammo.parts;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IAdvancedTooltipItem;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
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
 * @updated 26.05.2024
 * @since 30.12.2023
 */
public interface IAmmoTypeItem<T extends IAmmoType<T, E>, E extends EntityAmmoBase<? super E>> extends IAmmoType<T, E>, IAdvancedTooltipItem
{
	//--- NBT Keys ---//
	String NBT_CORE = "core";
	String NBT_CORE_TYPE = "core_type";
	String NBT_COMPONENTS = "components";
	String NBT_COMPONENTS_DATA = "component_nbt";
	String NBT_PAINT = "paint_color";
	String NBT_FUSE = "fuse";
	String NBT_FUSE_PARAM = "fuse_param";

	//--- Materials and Crafting ---//

	/**
	 * @return Propellant needed to make a bullet in mB
	 * @see pl.pabilo8.immersiveintelligence.api.crafting.DustStack DustStack - for solid fuels
	 * @see net.minecraftforge.fluids.FluidStack FluidStack - for fluid fuels
	 */
	default int getPropellantNeeded()
	{
		return 0;
	}

	PropellantType getAllowedPropellants();

	/**
	 * @return How many metal nuggets does it cost to produce a single {@link AmmoCore} of this ammo type
	 */
	int getCoreMaterialNeeded();


	//--- ItemStack ---//

	default void makeDefault(ItemStack stack)
	{
		EasyNBT nbt = EasyNBT.wrapNBT(stack);
		if(nbt.hasKey(NBT_CORE))
			nbt.withString(NBT_CORE, AmmoRegistry.MISSING_CORE.getName());
		if(nbt.hasKey(NBT_CORE_TYPE))
			nbt.withString(NBT_CORE_TYPE, getAllowedCoreTypes()[0].getName());
		if(!isBulletCore(stack))
			nbt.withString(NBT_FUSE, getAllowedFuseTypes()[0].getName());
	}

	/**
	 * @param stack bullet stack
	 * @return mass of the bullet from stack NBT, calculated from core, components and initial casing  mass
	 */
	@Override
	default double getMass(ItemStack stack)
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
	default double getCoreMass(AmmoCore core, AmmoComponent[] components)
	{
		return getCasingMass()*(1f+core.getDensity()+Arrays.stream(components).mapToDouble(AmmoComponent::getDensity).sum());
	}

	/**
	 * @return Returns allowed fuse types, these affect the way a bullet entity will detect collision
	 * @see FuseType
	 */
	FuseType[] getAllowedFuseTypes();

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
				.streamList(NBTTagCompound.class, NBT_COMPONENTS_DATA, EasyNBT.TAG_COMPOUND)
				.toArray(NBTTagCompound[]::new);
	}

	@Override
	default ItemStack setComponentNBT(ItemStack stack, NBTTagCompound... tagCompounds)
	{
		EasyNBT.wrapNBT(stack).withList(NBT_COMPONENTS, (Object[])tagCompounds);
		return stack;
	}

	@Override
	default boolean addComponents(ItemStack stack, AmmoComponent component, NBTTagCompound componentNBT)
	{
		if(!hasFreeComponentSlots(stack))
			return false;
		EasyNBT.wrapNBT(stack)
				.appendList(NBT_COMPONENTS, EasyNBT.TAG_STRING, new NBTTagString(component.getName()))
				.appendList(NBT_COMPONENTS_DATA, EasyNBT.TAG_COMPOUND, componentNBT.copy());
		return true;
	}

	@Override
	default ItemStack setPaintColor(ItemStack stack, @Nullable IIColor color)
	{
		ItemNBTHelper.setInt(stack, NBT_PAINT, color==null?-1: color.getPackedRGB());
		return stack;
	}

	@Override
	@Nullable
	default IIColor getPaintColor(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, NBT_PAINT))
		{
			int color = ItemNBTHelper.getInt(stack, NBT_PAINT);
			return color==-1?null: IIColor.fromPackedRGB(color);
		}
		return null;
	}

	@Override
	default void setFuseType(ItemStack stack, FuseType type)
	{
		ItemNBTHelper.setString(stack, NBT_FUSE, type.getName());
	}

	@Override
	default FuseType getFuseType(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_FUSE))
			makeDefault(stack);
		return FuseType.v(ItemNBTHelper.getString(stack, NBT_FUSE));
	}

	@Override
	default AmmoCore getCore(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_CORE))
			makeDefault(stack);
		return AmmoRegistry.getCore(ItemNBTHelper.getString(stack, NBT_CORE));
	}

	@Override
	default CoreType getCoreType(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_CORE_TYPE))
			makeDefault(stack);
		return CoreType.v(ItemNBTHelper.getString(stack, NBT_CORE_TYPE));
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
	ItemStack getAmmoStack(AmmoCore core, CoreType coreType, FuseType fuse, AmmoComponent... components);

	/**
	 * Proper method to get a bullet core, <u><b>use this instead of creating ItemStacks and setting NBT</b></u>
	 *
	 * @return a bullet core ItemStack for given parameters
	 */
	ItemStack getAmmoCoreStack(AmmoCore core, CoreType coreType);

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
		boolean artillery() default false;
	}
}
