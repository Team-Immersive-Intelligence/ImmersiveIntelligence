package pl.pabilo8.immersiveintelligence.client.gui.data_input_machine;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityDataInputMachine;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Created by Pabilo8 on 30-06-2019.
 * Rework on 31.08.2021.
 */
public class GuiDataInputMachineStorage extends GuiDataInputMachineBase
{
	public GuiDataInputMachineStorage(InventoryPlayer inventoryPlayer, TileEntityDataInputMachine tile)
	{
		super(inventoryPlayer, tile, IIGuiList.GUI_DATA_INPUT_MACHINE_STORAGE);
		title = I18n.format("tile.immersiveintelligence.metal_multiblock.data_input_machine.storage");
	}

	@Override
	public void initGui()
	{
		super.initGui();
		addLabel(101, 23, Utils.COLOR_H2, I18n.format("tile.immersiveintelligence.metal_multiblock.data_input_machine.label_punchtapes")).setCentered();
	}

	@Override
	protected void actionPerformed(@Nonnull GuiButton button) throws IOException
	{
		super.actionPerformed(button);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);

	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		super.drawGuiContainerBackgroundLayer(f, mx, my);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

}
