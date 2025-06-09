package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.IIMultiblockRecipe;

import java.util.function.BiFunction;

/**
 * @author Pabilo8
 * @updated 08.06.2025
 * @ii-approved 0.3.1
 * @since 08-08-2019
 */
public class AmmunitionAssemblerRecipe extends IIMultiblockRecipe
{
	public final IAmmoTypeItem<?, ?> ammoItem;
	public final BiFunction<ItemStack, ItemStack, ItemStack> process;
	public final IngredientStack coreInput, casingInput;
	public final boolean advanced;

	public AmmunitionAssemblerRecipe(BiFunction<ItemStack, ItemStack, ItemStack> process, Object coreInput, Object casingInput, int energy, int time, boolean advanced)
	{
		super(coreInput, casingInput, advanced);
		this.process = process;
		this.coreInput = ApiUtils.createIngredientStack(coreInput);
		this.casingInput = ApiUtils.createIngredientStack(casingInput);
		this.ammoItem = ((IAmmoTypeItem<?, ?>)this.coreInput.getExampleStack().getItem());

		this.setTimeAndEnergy(time, energy);
		this.inputList = Lists.newArrayList(this.coreInput, this.casingInput);
		this.outputList = getExampleItems();
		this.advanced = advanced;
	}

	private NonNullList<ItemStack> getExampleItems()
	{
		return NonNullList.from(ItemStack.EMPTY,
				process.apply(coreInput.getExampleStack(), casingInput.getExampleStack().copy())
		);
	}

	@Override
	public NonNullList<ItemStack> getActualItemOutputs(TileEntity te)
	{
		if(te instanceof TileEntityAmmunitionAssembler)
			return NonNullList.from(ItemStack.EMPTY, process.apply(((TileEntityAmmunitionAssembler)te).inventory.get(0), ((TileEntityAmmunitionAssembler)te).inventory.get(1).copy()));
		return NonNullList.from(ItemStack.EMPTY);
	}

	@Override
	public int getMultipleProcessTicks()
	{
		return 0;
	}
}