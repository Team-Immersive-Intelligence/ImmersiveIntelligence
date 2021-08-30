package pl.pabilo8.immersiveintelligence.common.compat.jei.gui_handlers;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticLogicMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticMachineVariables;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineVariables;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 24.03.2021
 */
public abstract class DataInputMachineGuiHandler<T extends GuiContainer> implements IAdvancedGuiHandler<T>
{
	public DataInputMachineGuiHandler()
	{

	}

	/**
	 * Modeled after {@link GuiRecipeBook#render(int, int, float)}
	 */
	@Override
	public List<Rectangle> getGuiExtraAreas(T guiContainer)
	{
		List<Rectangle> areas = new ArrayList<>();
		areas.add(new Rectangle(guiContainer.getGuiLeft()-28, guiContainer.getGuiTop()+4, 28, 48));
		return areas;
	}

	public static class Variables extends DataInputMachineGuiHandler<GuiDataInputMachineVariables>
	{
		@Override
		public Class<GuiDataInputMachineVariables> getGuiContainerClass()
		{
			return GuiDataInputMachineVariables.class;
		}

	}

	public static class Edit extends DataInputMachineGuiHandler<GuiDataInputMachineEdit>
	{
		@Override
		public Class<GuiDataInputMachineEdit> getGuiContainerClass()
		{
			return GuiDataInputMachineEdit.class;
		}

	}

	public static class Storage extends DataInputMachineGuiHandler<GuiDataInputMachineStorage>
	{
		@Override
		public Class<GuiDataInputMachineStorage> getGuiContainerClass()
		{
			return GuiDataInputMachineStorage.class;
		}

	}
}
