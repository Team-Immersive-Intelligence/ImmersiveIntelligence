package pl.pabilo8.immersiveintelligence.api.utils;

import crafttweaker.api.item.IngredientStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 06.07.2020
 * <p>
 * Doesn't do anything on its own, it's just a reference object with icon to draw and items to use when upgrading
 */
public class MachineUpgrade
{
	private static List<MachineUpgrade> registeredUpgrades = new ArrayList<>();

	private ResourceLocation name;
	private ResourceLocation icon;
	private List<IngredientStack> requiredStacks = new ArrayList<>();

	public MachineUpgrade(ResourceLocation name, ResourceLocation icon)
	{
		this.name = name;
		this.icon = icon;
		registeredUpgrades.add(this);
	}

	public MachineUpgrade addStack(IngredientStack stack)
	{
		requiredStacks.add(stack);
		return this;
	}

	public ResourceLocation getName()
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
			if(tag.hasKey(machineUpgrade.name.toString()))
				upgrades.add(machineUpgrade);
		}
		return upgrades;
	}
}
