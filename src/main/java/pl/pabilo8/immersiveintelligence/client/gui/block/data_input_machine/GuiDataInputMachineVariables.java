package pl.pabilo8.immersiveintelligence.client.gui.block.data_input_machine;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiDataVariableList;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageGuiNBT;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Pabilo8 on 30-06-2019.
 * Rework on 31.08.2021.
 */
public class GuiDataInputMachineVariables extends GuiDataInputMachineBase
{
	private GuiDataVariableList variableList;

	public GuiDataInputMachineVariables(EntityPlayer player, TileEntityDataInputMachine tile)
	{
		super(player, tile, IIGuiList.GUI_DATA_INPUT_MACHINE_VARIABLES);
		title = I18n.format("tile.immersiveintelligence.metal_multiblock.data_input_machine.programming");
	}

	@Override
	public void initGui()
	{
		super.initGui();
		variableList = addButton(new GuiDataVariableList(buttonList.size(), guiLeft+32, guiTop+12, 128+11, 114, tile.storedData));
		if(positionEqual(proxy, tile))
		{
			if(proxy.storedGuiData.hasKey("scrollPercent"))
				variableList.setScrollPercent(proxy.storedGuiData.getFloat("scrollPercent"));
		}
	}

	@Override
	protected void actionPerformed(@Nonnull GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		if(button==variableList)
		{
			if(variableList.add)
			{
				for(char c : DataPacket.varCharacters)
				{
					if(!list.variables.containsKey(c))
					{
						//Save gui scroll, tile pos for validation
						saveBasicData();
						proxy.storedGuiData.setString("variableToEdit", String.valueOf(c));
						//Set variable and change gui
						refreshStoredData();
						syncDataToServer();

						preparedForChange=true;
						IIPacketHandler.sendToServer(new MessageGuiNBT(IIGuiList.GUI_DATA_INPUT_MACHINE_EDIT, tile));

						break;
					}
				}
			}
			else if(variableList.edit)
			{
				refreshStoredData();
				syncDataToServer();
				saveBasicData();
				proxy.storedGuiData.setString("variableToEdit",
						String.valueOf(tile.storedData.variables.keySet()
								.stream()
								.sorted(Comparator.comparingInt(o -> ArrayUtils.indexOf(DataPacket.varCharacters, o)))
								.toArray(Character[]::new)[variableList.selectedOption])
				);

				//Set variable and change gui
				refreshStoredData();
				syncDataToServer();

				preparedForChange=true;
				IIPacketHandler.sendToServer(new MessageGuiNBT(IIGuiList.GUI_DATA_INPUT_MACHINE_EDIT, tile));
			}
			else if(variableList.delete)
			{
				list.removeVariable(
						tile.storedData.variables.keySet()
								.stream()
								.sorted(Comparator.comparingInt(o -> ArrayUtils.indexOf(DataPacket.varCharacters, o)))
								.toArray(Character[]::new)[variableList.selectedOption]
				);
				syncDataToServer();
				variableList.recalculateEntries();
			}
		}
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		GlStateManager.enableBlend();

		ArrayList<String> tooltip = new ArrayList<>();

		if(variableList.add)
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+("variable_add_desc")));
		else if(variableList.selectedOption!=-1&&(variableList.edit||variableList.delete))
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+(variableList.delete?"variable_remove_desc": "variable_set_desc")));

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, -1, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}

	@Override
	void refreshStoredData()
	{
		super.refreshStoredData();
		if(variableList!=null)
			variableList.recalculateEntries();
	}

	@Override
	public void saveBasicData()
	{
		super.saveBasicData();
		proxy.storedGuiData.setFloat("scrollPercent", variableList.getScrollPercent());
	}
}
