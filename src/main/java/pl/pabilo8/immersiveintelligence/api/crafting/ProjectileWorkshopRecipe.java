package pl.pabilo8.immersiveintelligence.api.crafting;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ProjectileWorkshop;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.06.2025
 **/
public class ProjectileWorkshopRecipe extends IIMultiblockRecipe
{
	public static final String FILLING_RECIPE_NAME = "core_filling";
	public static final String CORE_PRODUCTION_RECIPE_PREFIX = "core_production_";
	public static final ProjectileWorkshopRecipe CORE_FILLING = new ProjectileWorkshopRecipe();

	@Nullable
	public IAmmoTypeItem<?, ?> ammo;
	public boolean isFilling;
	public ItemStack effect = ItemStack.EMPTY, ingredient = ItemStack.EMPTY;

	/**
	 * Single hard-coded recipe used by Core Filler mode.
	 */
	private ProjectileWorkshopRecipe()
	{
		super(FILLING_RECIPE_NAME);
		this.isFilling = true;

		this.setTimeAndEnergy(
				ProjectileWorkshop.fillingTime,
				ProjectileWorkshop.fillingEnergyUsage
		);
	}

	/**
	 * Production recipe
	 *
	 * @param ammo     Ammo to be produced
	 * @param coreType Core type
	 */
	public ProjectileWorkshopRecipe(IAmmoTypeItem<?, ?> ammo, AmmoCore core, CoreType coreType)
	{
		super(core, coreType);
		this.ammo = ammo;
		this.isFilling = false;

		this.ingredient = core.getMaterial().getExampleStack();
		this.effect = ammo.getAmmoCoreStack(core, coreType);

		this.setTimeAndEnergy(
				ProjectileWorkshop.productionTime*ammo.getCaliber(),
				ProjectileWorkshop.productionEnergyUsage*ammo.getCaliber()
		);
	}

	public ItemStack getEffect()
	{
		return effect.copy();
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		return null;
	}
}
