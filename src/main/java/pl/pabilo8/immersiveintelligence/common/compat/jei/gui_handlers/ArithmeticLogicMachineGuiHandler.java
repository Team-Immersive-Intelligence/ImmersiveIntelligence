package pl.pabilo8.immersiveintelligence.common.compat.jei.gui_handlers;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.client.gui.GuiAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticLogicMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticLogicMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticMachineVariables;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 24.03.2021
 */
public abstract class ArithmeticLogicMachineGuiHandler<T extends GuiContainer> implements IAdvancedGuiHandler<T>
{
	public ArithmeticLogicMachineGuiHandler()
	{

	}

	/**
	 * Modeled after {@link GuiRecipeBook#render(int, int, float)}
	 */
	@Override
	public List<Rectangle> getGuiExtraAreas(T guiContainer)
	{
		List<Rectangle> areas = new ArrayList<>();
		IItemHandler handler = getHandler(guiContainer);
		//Storage
		areas.add(new Rectangle(guiContainer.getGuiLeft()-28, guiContainer.getGuiTop()+4, 28, 48));
		//Circuits
		for(int i = 0; i < 4; i++)
		{
			if(!handler.getStackInSlot(i).isEmpty())
				areas.add(new Rectangle(guiContainer.getGuiLeft()-28, guiContainer.getGuiTop()+4+(24*(i+1)), 28, 24));
		}
		return areas;
	}

	abstract IItemHandler getHandler(T guiContainer);

	public static class Variables extends ArithmeticLogicMachineGuiHandler<GuiArithmeticMachineVariables>
	{
		@Override
		public Class<GuiArithmeticMachineVariables> getGuiContainerClass()
		{
			return GuiArithmeticMachineVariables.class;
		}

		@Override
		IItemHandler getHandler(GuiArithmeticMachineVariables guiContainer)
		{
			return guiContainer.handler;
		}
	}

	public static class Edit extends ArithmeticLogicMachineGuiHandler<GuiArithmeticLogicMachineEdit>
	{
		@Override
		public Class<GuiArithmeticLogicMachineEdit> getGuiContainerClass()
		{
			return GuiArithmeticLogicMachineEdit.class;
		}

		@Override
		IItemHandler getHandler(GuiArithmeticLogicMachineEdit guiContainer)
		{
			return guiContainer.handler;
		}
	}

	public static class Storage extends ArithmeticLogicMachineGuiHandler<GuiArithmeticLogicMachineStorage>
	{
		@Override
		public Class<GuiArithmeticLogicMachineStorage> getGuiContainerClass()
		{
			return GuiArithmeticLogicMachineStorage.class;
		}

		@Override
		IItemHandler getHandler(GuiArithmeticLogicMachineStorage guiContainer)
		{
			return guiContainer.handler;
		}
	}
}
