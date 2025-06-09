package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.DataInputMachine;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.IIMultiblockRecipe;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * @author Pabilo8
 * @since 08.01.2024
 */
public class DataProgrammingRecipe extends IIMultiblockRecipe
{
	public final IngredientStack input;
	public final DataProgrammingFunction operationFrom;

	public DataProgrammingRecipe(IngredientStack input, @Nullable DataProgrammingFunction operation)
	{
		super(input);
		this.input = input;
		this.operationFrom = operation;
		this.setTimeAndEnergy(
				DataInputMachine.timePunchtapeProduction,
				DataInputMachine.energyUsagePunchtape
		);
	}

	@Override
	public int getMultipleProcessTicks()
	{
		return 0;
	}

	public interface DataProgrammingFunction
	{
		ItemStack apply(ItemStack itemInput, DataPacket dataInput, Consumer<DataPacket> saveOperation);
	}
}
