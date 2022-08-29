package pl.pabilo8.immersiveintelligence.api.utils;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 06.07.2020
 * <p>
 * Doesn't do anything on its own, it's just a reference object with icon to draw and items to use when upgrading
 */
public class MachineUpgrade
{
	private static final List<MachineUpgrade> registeredUpgrades = new ArrayList<>();

	protected String name;
	protected ResourceLocation icon;
	private int progress=0;
	private int steps=1;
	private final List<IngredientStack> requiredStacks = new ArrayList<>();

	public MachineUpgrade(String name, ResourceLocation icon)
	{
		this.name = name;
		this.icon = icon;
		registeredUpgrades.add(this);
	}

	@Nullable
	public static MachineUpgrade getUpgradeByID(String id)
	{
		return registeredUpgrades.stream().filter(machineUpgrade -> machineUpgrade.name.equals(id)).findFirst().orElse(null);
	}

	public MachineUpgrade addStack(IngredientStack stack)
	{
		requiredStacks.add(stack);
		return this;
	}

	public MachineUpgrade setRequiredProgress(int progress)
	{
		this.progress=progress;
		return this;
	}

	public MachineUpgrade setRequiredSteps(int steps)
	{
		this.steps=steps;
		return this;
	}

	public String getName()
	{
		return name;
	}

	public ResourceLocation getIcon()
	{
		return icon;
	}

	public List<IngredientStack> getRequiredStacks()
	{
		return requiredStacks;
	}

	public static List<MachineUpgrade> getUpgradesFromNBT(NBTTagCompound tag)
	{
		List<MachineUpgrade> upgrades = new ArrayList<>();
		for(MachineUpgrade machineUpgrade : registeredUpgrades)
		{
			if(tag.hasKey(machineUpgrade.name))
				upgrades.add(machineUpgrade);
		}
		return upgrades;
	}

	public static List<MachineUpgrade> getMatchingUpgrades(IUpgradableMachine machine)
	{
		return registeredUpgrades.stream().filter(machine::upgradeMatches).collect(Collectors.toList());
	}

	public int getProgressRequired()
	{
		return progress;
	}

	public int getSteps()
	{
		return steps;
	}
}
