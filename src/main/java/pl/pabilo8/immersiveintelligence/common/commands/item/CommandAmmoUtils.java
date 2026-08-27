package pl.pabilo8.immersiveintelligence.common.commands.item;

import net.minecraft.command.CommandException;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Validates ammunition command arguments before ammunition is created.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.08.2026
 */
public final class CommandAmmoUtils
{
	private CommandAmmoUtils()
	{
	}

	/**
	 * Gets a registered ammunition type.
	 */
	public static IAmmoTypeItem<?, ?> resolveAmmoType(String name) throws CommandException
	{
		IAmmoTypeItem<?, ?> ammoType = AmmoRegistry.getAmmoItem(name);
		if(ammoType==null)
			throw new CommandException("Unknown ammunition type '%s'.", name);
		return ammoType;
	}

	/**
	 * Gets a registered ammunition core material.
	 */
	public static AmmoCore resolveCore(String name) throws CommandException
	{
		AmmoCore core = AmmoRegistry.getCore(name);
		if(core==AmmoRegistry.MISSING_CORE)
			throw new CommandException("Unknown ammunition core material '%s'.", name);
		return core;
	}

	/**
	 * Gets and validates a core type for an ammunition type.
	 */
	public static CoreType resolveCoreType(IAmmoTypeItem<?, ?> ammoType, String name) throws CommandException
	{
		CoreType coreType = findEnum(CoreType.class, name);
		if(coreType==null)
			throw new CommandException("Unknown ammunition core type '%s'.", name);
		if(!Arrays.asList(ammoType.getAllowedCoreTypes()).contains(coreType))
			throw new CommandException("Core type '%s' is not supported by ammunition type '%s'.", name, ammoType.getName());
		return coreType;
	}

	/**
	 * Gets and validates a fuse type for an ammunition type.
	 */
	public static FuseType resolveFuseType(IAmmoTypeItem<?, ?> ammoType, String name) throws CommandException
	{
		FuseType fuseType = findEnum(FuseType.class, name);
		if(fuseType==null)
			throw new CommandException("Unknown ammunition fuse type '%s'.", name);
		if(!Arrays.asList(ammoType.getAllowedFuseTypes()).contains(fuseType))
			throw new CommandException("Fuse type '%s' is not supported by ammunition type '%s'.", name, ammoType.getName());
		return fuseType;
	}

	/**
	 * Gets one registered ammunition component.
	 */
	public static AmmoComponent resolveComponent(String name) throws CommandException
	{
		AmmoComponent component = AmmoRegistry.getComponent(name);
		if(component==null)
			throw new CommandException("Unknown ammunition component '%s'.", name);
		return component;
	}

	/**
	 * Validates one component for the specified ammunition and core type.
	 */
	public static void validateComponent(IAmmoTypeItem<?, ?> ammoType, CoreType coreType, AmmoComponent component, int usedSlots) throws CommandException
	{
		if(!component.matchesBullet(ammoType))
			throw new CommandException("Component '%s' cannot be used with ammunition type '%s'.", component.getName(), ammoType.getName());

		int slotsTaken = Math.max(0, component.getSlotsTaken());
		if(usedSlots+slotsTaken > coreType.getComponentSlots())
			throw new CommandException("Component '%s' needs %d slots, but core type '%s' has %d slots and %d are already used.",
					component.getName(), slotsTaken, coreType.getName(), coreType.getComponentSlots(), usedSlots);
	}

	/**
	 * Gets and validates all ammunition components from command arguments.
	 */
	public static AmmoComponent[] resolveComponents(IAmmoTypeItem<?, ?> ammoType, CoreType coreType, String[] names) throws CommandException
	{
		ArrayList<AmmoComponent> components = new ArrayList<>(names.length);
		int usedSlots = 0;
		for(String name : names)
		{
			AmmoComponent component = resolveComponent(name);
			validateComponent(ammoType, coreType, component, usedSlots);
			components.add(component);
			usedSlots += Math.max(0, component.getSlotsTaken());

			if(components.size() > coreType.getComponentSlots())
				throw new CommandException("Too many components for core type '%s'. It has %d component slots.",
						coreType.getName(), coreType.getComponentSlots());
		}
		return components.toArray(new AmmoComponent[0]);
	}

	/**
	 * Tests whether an ammunition type supports the specified configuration.
	 */
	public static boolean supports(IAmmoTypeItem<?, ?> ammoType, CoreType coreType, FuseType fuseType, @Nullable AmmoComponent component)
	{
		if(!Arrays.asList(ammoType.getAllowedCoreTypes()).contains(coreType)
				||!Arrays.asList(ammoType.getAllowedFuseTypes()).contains(fuseType))
			return false;
		return component==null||(component.matchesBullet(ammoType)&&Math.max(0, component.getSlotsTaken()) <= coreType.getComponentSlots());
	}

	/**
	 * Gets valid component names for tab completion.
	 */
	public static List<String> getValidComponentNames(IAmmoTypeItem<?, ?> ammoType, CoreType coreType, String[] existingNames)
	{
		int usedSlots = 0;
		int usedComponents = 0;
		for(String name : existingNames)
		{
			AmmoComponent component = AmmoRegistry.getComponent(name);
			if(component==null||!component.matchesBullet(ammoType))
				return Collections.emptyList();
			usedSlots += Math.max(0, component.getSlotsTaken());
			usedComponents++;
		}

		if(usedSlots >= coreType.getComponentSlots()||usedComponents >= coreType.getComponentSlots())
			return Collections.emptyList();

		final int remainingSlots = coreType.getComponentSlots()-usedSlots;
		return AmmoRegistry.getAllComponents().stream()
				.filter(component -> component.matchesBullet(ammoType))
				.filter(component -> Math.max(0, component.getSlotsTaken()) <= remainingSlots)
				.map(AmmoComponent::getName)
				.collect(Collectors.toList());
	}

	@Nullable
	private static <E extends Enum<E> & ISerializableEnum> E findEnum(Class<E> enumClass, String name)
	{
		return Arrays.stream(enumClass.getEnumConstants())
				.filter(value -> value.getName().equalsIgnoreCase(name))
				.findFirst()
				.orElse(null);
	}
}
