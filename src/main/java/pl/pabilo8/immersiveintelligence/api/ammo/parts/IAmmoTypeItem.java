package pl.pabilo8.immersiveintelligence.api.ammo.parts;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.ammo.IIAmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IAdvancedTooltipItem;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @param <T> entity created by this ammo
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 30.12.2023
 */
public interface IAmmoItem<T extends EntityAmmoBase> extends IAmmo<T>, IAdvancedTooltipItem
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

	/**
	 * @return Gunpowder needed to make a bullet in mB
	 * @see pl.pabilo8.immersiveintelligence.api.crafting.DustStack
	 */
	default int getGunpowderNeeded()
	{
		return 0;
	}

	/**
	 * @return How many metal nuggets does it cost to produce a single {@link IAmmoCore} of this ammo type
	 */
	int getCoreMaterialNeeded();


	//--- ItemStack ---//

	default void makeDefault(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_CORE))
			ItemNBTHelper.setString(stack, NBT_CORE, IIAmmoRegistry.MISSING_CORE.getName());
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
	default float getCoreMass(IAmmoCore core, IAmmoComponent[] components)
	{
		return (float)(getInitialMass()*(1f+core.getDensity()+Arrays.stream(components).mapToDouble(IAmmoComponent::getDensity).sum()));
	}

	/**
	 * @return Returns allowed fuse types, these affect the way a bullet entity will detect collision
	 * @see EnumFuseTypes
	 */
	EnumFuseTypes[] getAllowedFuseTypes();

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
	default IAmmoComponent[] getComponents(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, NBT_COMPONENTS))
		{
			ArrayList<IAmmoComponent> arrayList = new ArrayList<>();
			NBTTagList components = (NBTTagList)ItemNBTHelper.getTag(stack).getTag(NBT_COMPONENTS);
			for(int i = 0; i < components.tagCount(); i++)
				arrayList.add(IIAmmoRegistry.getComponent(components.getStringTagAt(i)));
			return arrayList.toArray(new IAmmoComponent[0]);
		}
		return new IAmmoComponent[0];
	}

	@Override
	default NBTTagCompound[] getComponentsNBT(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, NBT_COMPONENTS_NBT))
		{
			ArrayList<NBTTagCompound> arrayList = new ArrayList<>();
			NBTTagList components = (NBTTagList)ItemNBTHelper.getTag(stack).getTag(NBT_COMPONENTS_NBT);
			for(int i = 0; i < components.tagCount(); i++)
				arrayList.add(components.getCompoundTagAt(i));
			return arrayList.toArray(new NBTTagCompound[0]);
		}
		return new NBTTagCompound[0];
	}

	@Override
	default void addComponents(ItemStack stack, IAmmoComponent component, NBTTagCompound componentNBT)
	{
		NBTTagList comps = ItemNBTHelper.getTag(stack).getTagList(NBT_COMPONENTS, 8);
		NBTTagList nbts = ItemNBTHelper.getTag(stack).getTagList(NBT_COMPONENTS_NBT, 10);

		comps.appendTag(new NBTTagString(component.getName()));
		nbts.appendTag(componentNBT.copy());

		ItemNBTHelper.getTag(stack).setTag(NBT_COMPONENTS, comps);
		ItemNBTHelper.getTag(stack).setTag(NBT_COMPONENTS_NBT, nbts);
	}

	@Override
	default int getPaintColor(ItemStack stack)
	{
		if(ItemNBTHelper.hasKey(stack, NBT_PAINT))
			return ItemNBTHelper.getInt(stack, NBT_PAINT);
		return -1;
	}

	@Override
	default void setFuseType(ItemStack stack, EnumFuseTypes type)
	{
		ItemNBTHelper.setString(stack, NBT_FUSE, type.getName());
	}

	@Override
	default EnumFuseTypes getFuseType(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_FUSE))
			makeDefault(stack);
		return EnumFuseTypes.v(ItemNBTHelper.getString(stack, NBT_FUSE));
	}

	@Override
	default IAmmoCore getCore(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_CORE))
			makeDefault(stack);
		return IIAmmoRegistry.getCore(ItemNBTHelper.getString(stack, NBT_CORE));
	}

	@Override
	default EnumCoreTypes getCoreType(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, NBT_CORE_TYPE))
			makeDefault(stack);
		return EnumCoreTypes.v(ItemNBTHelper.getString(stack, NBT_CORE_TYPE));
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

	//--- IAdvancedTooltipItem ---//

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
