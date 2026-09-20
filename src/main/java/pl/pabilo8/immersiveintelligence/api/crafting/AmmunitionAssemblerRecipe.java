package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionAssembler;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.06.2025
 * @ii-approved 0.3.1
 * @since 08.08.2019
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
				process.apply(coreInput.getExampleStack().copy(), casingInput.getExampleStack().copy())
		);
	}

	@Override
	public NonNullList<ItemStack> getActualItemOutputs(TileEntity te)
	{
		if(te instanceof TileEntityAmmunitionAssembler)
			return NonNullList.from(ItemStack.EMPTY, process.apply(((TileEntityAmmunitionAssembler)te).inventory.get(0), ((TileEntityAmmunitionAssembler)te).inventory.get(1).copy()));
		return NonNullList.from(ItemStack.EMPTY);
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		ItemStack casingStack = casingInput.getExampleStack();
		NonNullList<ItemStack> cores = NonNullList.create(), outputs = NonNullList.create();
		for(CoreType allowedCoreType : this.ammoItem.getAllowedCoreTypes())
		{
			cores.add(ammoItem.getAmmoCoreStack(IIContent.ammoCoreIron, allowedCoreType));
			outputs.add(process.apply(cores.get(cores.size()-1), casingStack.copy()));
		}

		return new IIRecipeLayoutBuilder(144, 64)
				.withSlot(8+2, 5+1, new IngredientStack(cores), IOType.INPUT, "frame_input")
				.withSlot(8+2, 9+4-1+20, this.casingInput, IOType.INPUT, "frame_input")
				.withSlot(134-12-4, 9+20-8, new IngredientStack(outputs), IOType.OUTPUT, "frame_output")
				.withMultiblockModel(30, -2)
				.withTimeInfo()
				.withPowerInfo()
				.build();
	}
}
