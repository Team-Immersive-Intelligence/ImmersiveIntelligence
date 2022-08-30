package pl.pabilo8.immersiveintelligence.common.compat.jei.gui_handlers;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine.GuiDataInputMachineBase;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 24.03.2021
 */
public class DataInputMachineGuiHandler implements IAdvancedGuiHandler<GuiDataInputMachineBase>
{
	public DataInputMachineGuiHandler()
	{

	}

	@Override
	public Class<GuiDataInputMachineBase> getGuiContainerClass()
	{
		return GuiDataInputMachineBase.class;
	}

	/**
	 * Modeled after {@link GuiRecipeBook#render(int, int, float)}
	 */
	@Override
	public List<Rectangle> getGuiExtraAreas(GuiDataInputMachineBase gui)
	{
		List<Rectangle> areas = new ArrayList<>();
		areas.add(new Rectangle(gui.getGuiLeft()-28, gui.getGuiTop()+4, 28, gui.TABS.size()*24));

		areas.add(new Rectangle(gui.getGuiLeft()+176+(int)(146*(gui.sideManual.manualTime/100f)), gui.getGuiTop()+56, 32, 18)); //manual button
		areas.add(new Rectangle(gui.getGuiLeft()+gui.getXSize()-20, gui.getGuiTop(), (int)(164*(gui.sideManual.manualTime/100f)), 198)); //manual
		return areas;
	}
}
