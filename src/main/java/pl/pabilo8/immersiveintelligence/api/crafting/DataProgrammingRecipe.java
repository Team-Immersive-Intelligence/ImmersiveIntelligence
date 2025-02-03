package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.DataInputMachine;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIIMultiblockRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Pabilo8
 * @since 08.01.2024
 */
public class DataProgrammingRecipe implements IIIMultiblockRecipe
{
	public static final List<DataProgrammingRecipe> RECIPE_LIST = new ArrayList<>();

	public final IngredientStack input;
	@Nullable
	public final BiFunction<DataPacket, ItemStack, ItemStack> operationFrom;
	@Nullable
	public final BiFunction<DataPacket, ItemStack, DataPacket> operationTo;

	public DataProgrammingRecipe(IngredientStack input,
								 @Nullable BiFunction<DataPacket, ItemStack, ItemStack> operationFrom,
								 @Nullable BiFunction<DataPacket, ItemStack, DataPacket> operationTo)
	{
		this.input = input;
		this.operationFrom = operationFrom;
		this.operationTo = operationTo;
		RECIPE_LIST.add(this);
	}

	public static DataProgrammingRecipe addRecipe(IngredientStack input,
												  @Nullable BiFunction<DataPacket, ItemStack, ItemStack> operationFrom,
												  @Nullable BiFunction<DataPacket, ItemStack, DataPacket> operationTo)
	{
		return new DataProgrammingRecipe(input, operationFrom, operationTo);
	}

	@Override
	public int getTotalProcessTime()
	{
		return DataInputMachine.timePunchtapeProduction;
	}

	@Override
	public int getTotalProcessEnergy()
	{
		return DataInputMachine.energyUsagePunchtape;
	}

	@Override
	public int getMultipleProcessTicks()
	{
		return 0;
	}

	@Override
	public EasyNBT writeToNBT()
	{
		return null;
	}
}
