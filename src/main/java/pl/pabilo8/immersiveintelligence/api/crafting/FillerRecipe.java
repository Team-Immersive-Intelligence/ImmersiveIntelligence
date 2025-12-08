package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.06.2025
 * @ii-approved 0.3.1
 * @since 14.04.2020
 */
public class FillerRecipe extends IIMultiblockRecipe
{
	public final IngredientStack itemInput;
	public final ItemStack itemOutput;

	public DustStack dust;
	//for bullets only
	IAmmoTypeItem<?, ?> bullet = null;

	public FillerRecipe(ItemStack itemOutput, Object itemInput, DustStack dust, int time, int energy)
	{
		super(ApiUtils.createIngredientStack(itemInput), dust);
		this.itemOutput = itemOutput;
		this.itemInput = ApiUtils.createIngredientStack(itemInput);
		this.dust = dust;

		if(itemOutput.getItem() instanceof IAmmoTypeItem)
			bullet = ((IAmmoTypeItem<?, ?>)itemOutput.getItem());

		this.setTimeAndEnergy(time, energy);
	}


	public <T extends Item & IAmmoTypeItem<?, ?>> FillerRecipe(T bulletItem, int time, int energy)
	{
		this(getFilledBulletCasing(bulletItem), new IngredientStack(bulletItem.getCasingStack(1)).setUseNBT(true),
				new DustStack("gunpowder", bulletItem.getPropellantNeeded()), time, energy);
		this.bullet = bulletItem;
	}

	@Deprecated
	private static <T extends Item & IAmmoTypeItem<?, ?>> ItemStack getFilledBulletCasing(T bulletItem)
	{
		ItemStack casingStack = bulletItem.getCasingStack(1);
		ItemNBTHelper.setBoolean(casingStack, "ii_FilledCasing", true);
		return casingStack;
	}

	public DustStack getDust()
	{
		return this.dust;
	}

	@Nullable
	public IAmmoTypeItem<?, ?> getBullet()
	{
		return this.bullet;
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		return new IIRecipeLayoutBuilder(156, 68)
				.withInputSlot(2, 20, itemInput)
				.withDustTank(2+22, 4, dust, IOType.INPUT)
				.withOutputSlot(135, 20, itemOutput)
				.withMultiblockModel(32+8, 0)
				.withTimeInfo()
				.withPowerInfo()
				.build();
	}
}
