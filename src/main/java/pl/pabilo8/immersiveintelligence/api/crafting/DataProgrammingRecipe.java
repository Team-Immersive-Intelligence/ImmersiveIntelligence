package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.DataInputMachine;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.IIMultiblockRecipe;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.01.2024
 */
public class DataProgrammingRecipe extends IIMultiblockRecipe
{
	public final IngredientStack input;
	public final DataProgrammingFunction operationFrom;
	public final boolean showItem;

	public DataProgrammingRecipe(IngredientStack input, boolean showItem, @Nullable DataProgrammingFunction operation)
	{
		super(input);
		this.input = input;
		this.operationFrom = operation;
		this.showItem = showItem;
		this.setTimeAndEnergy(
				DataInputMachine.timePunchtapeProduction,
				DataInputMachine.energyUsagePunchtape
		);
	}

	public interface DataProgrammingFunction
	{
		ItemStack apply(ItemStack itemInput, DataPacket dataInput, Consumer<DataPacket> saveOperation);
	}
}
