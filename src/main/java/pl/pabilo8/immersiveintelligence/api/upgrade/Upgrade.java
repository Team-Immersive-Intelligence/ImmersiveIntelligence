package pl.pabilo8.immersiveintelligence.api.upgrade;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradePurpose;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents an installable {@link IUpgradableDevice} upgrade, part of a device {@link UpgradeTechTree}.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 29.08.2025
 * @ii-approved 0.3.1
 * @since 06.07.2020
 */
@ParametersAreNonnullByDefault
public class Upgrade
{
	private static Map<ResourceLocation, Upgrade> UPGRADE_REGISTRY = new HashMap<>();

	private final List<IngredientStack> requiredStacks = new ArrayList<>();
	private final List<UpgradeBenefit> benefits = new ArrayList<>();
	private final ResLoc id, icon;
	private int progressRequired, progressStages;
	private UpgradePurpose purpose;

	public Upgrade(String name)
	{
		this(IIReference.RES_II.with(name));
	}

	public Upgrade(ResourceLocation id)
	{
		this.id = ResLoc.of(id);
		this.progressRequired = this.progressStages = 1;
		this.purpose = UpgradePurpose.SPECIAL;
		this.icon = IIReference.RES_II.with("gui/upgrade/"+this.id.getResourcePath());
		UPGRADE_REGISTRY.put(id, this);
	}

	//--- Setters ---//

	/**
	 * Sets the item stacks required to install this upgrade
	 *
	 * @return this
	 */
	public Upgrade withCost(IngredientStack... stacks)
	{
		Collections.addAll(requiredStacks, stacks);
		return this;
	}

	/**
	 * Sets the progress stages, used for calculating client progress that is adjusted by the amount of animated components drawn during the upgrade install.
	 *
	 * @return this
	 */
	public Upgrade withProgressStages(int progressRequired)
	{
		this.progressStages = progressRequired;
		return this;
	}


	public Upgrade withRequiredProgress(int progress)
	{
		this.progressRequired = progress;
		return this;
	}

	public Upgrade withType(UpgradePurpose type)
	{
		this.purpose = type;
		return this;
	}

	public Upgrade withBenefit(UpgradeBenefit benefit)
	{
		this.benefits.add(benefit);
		return this;
	}

	//--- Getters ---//

	public ResourceLocation getId()
	{
		return id;
	}

	@Deprecated
	public String getName()
	{
		return id.getResourcePath();
	}

	public UpgradePurpose getPurpose()
	{
		return purpose;
	}

	public ResourceLocation getIcon()
	{
		return icon;
	}

	public List<IngredientStack> getRequiredStacks()
	{
		return requiredStacks;
	}

	public int getProgressRequired()
	{
		return progressRequired;
	}

	public int getProgressStages()
	{
		return progressStages;
	}

	public String getLocalizedName()
	{
		return I18n.format(IIReference.INFO_KEY+"machineupgrade.name",
				I18n.format("machineupgrade.immersiveintelligence."+id.getResourcePath()));
	}

	public List<String> getLocalizedBenefitNames()
	{
		return benefits.stream()
				.map(UpgradeBenefit::getLocalizedName)
				.collect(Collectors.toList());
	}

	//--- Utils ---//

	@Nullable
	@Deprecated
	public static Upgrade getUpgradeByID(String id)
	{
		return getUpgradeByID(IIReference.RES_II.with(id));
	}

	@Nullable
	public static Upgrade getUpgradeByID(ResourceLocation id)
	{
		return UPGRADE_REGISTRY.get(id);
	}

	public static List<Upgrade> getAllUpgrades()
	{
		return new ArrayList<>(UPGRADE_REGISTRY.values());
	}
}
