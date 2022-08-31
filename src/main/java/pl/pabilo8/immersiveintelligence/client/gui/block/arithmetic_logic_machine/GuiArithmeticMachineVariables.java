package pl.pabilo8.immersiveintelligence.client.gui.block.arithmetic_logic_machine;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiDataVariableList;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblocks.metal.tileentities.first.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageGuiNBT;
import pl.pabilo8.immersiveintelligence.common.util.IILib;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Pabilo8 on 30-06-2019.
 * Some code you write, some you steal, but in most cases you adapt the existing solutions...
 */
public class GuiArithmeticMachineVariables extends GuiArithmeticLogicMachineBase
{
	private GuiDataVariableList variableList;
	private final int page;


	//It was necessary to make the Gui control the Container
	public GuiArithmeticMachineVariables(InventoryPlayer inventoryPlayer, TileEntityArithmeticLogicMachine tile, int page)
	{
		super(inventoryPlayer, tile, getPage(page));
		this.page = page;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		variableList = addButton(new GuiDataVariableList(buttonList.size(), guiLeft+32, guiTop+20, 128+11, 106,
				IIContent.itemCircuit.getStoredData(handler.getStackInSlot(page))
		));

		if(positionEqual(proxy, tile))
		{
			if(proxy.storedGuiData.hasKey("scrollPercent"))
				variableList.setScrollPercent(proxy.storedGuiData.getFloat("scrollPercent"));
		}
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
	}

	@Override
	protected void actionPerformed(@Nonnull GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		if(!handler.getStackInSlot(page).isEmpty())
		{
			DataPacket list = IIContent.itemCircuit.getStoredData(handler.getStackInSlot(page));
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
							proxy.storedGuiData.setInteger("circuitToEdit", page);
							proxy.storedGuiData.setString("variableToEdit", String.valueOf(c));
							//Set variable and change gui
							refreshStoredData();
							syncDataToServer();

							preparedForChange=true;
							IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT, tile.getPos()));

							break;
						}
					}
				}
				else if(variableList.edit)
				{
					refreshStoredData();
					syncDataToServer();
					saveBasicData();
					proxy.storedGuiData.setInteger("circuitToEdit", page);
					proxy.storedGuiData.setString("variableToEdit",
							String.valueOf(list.variables.keySet()
									.stream()
									.sorted(Comparator.comparingInt(o -> ArrayUtils.indexOf(DataPacket.varCharacters, o)))
									.toArray(Character[]::new)[variableList.selectedOption])
					);

					//Set variable and change gui
					refreshStoredData();
					syncDataToServer();

					preparedForChange=true;
					IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_EDIT, tile.getPos()));
				}
				else if(variableList.delete)
				{
					list.removeVariable(
							list.variables.keySet()
									.stream()
									.sorted(Comparator.comparingInt(o -> ArrayUtils.indexOf(DataPacket.varCharacters, o)))
									.toArray(Character[]::new)[variableList.selectedOption]
					);
					IIContent.itemCircuit.writeDataToItem(list,handler.getStackInSlot(page));
					syncDataToServer();
					initGui();
				}


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
			tooltip.add(I18n.format(IILib.DESCRIPTION_KEY+("variable_add_desc")));
		else if(variableList.selectedOption!=-1&&(variableList.edit||variableList.delete))
			tooltip.add(I18n.format(IILib.DESCRIPTION_KEY+(variableList.delete?"variable_remove_desc": "variable_set_desc")));

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
	protected void syncDataToServer()
	{
		super.syncDataToServer();

		DataPacket storedData = IIContent.itemCircuit.getStoredData(handler.getStackInSlot(page));
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound expTag = new NBTTagCompound();

		expTag.setInteger("page", page);
		expTag.setTag("list", storedData.toNBT());
		tag.setTag("expressions",expTag);
		ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(tile, tag));
	}

	@Override
	public void saveBasicData()
	{
		super.saveBasicData();
		proxy.storedGuiData.setFloat("scrollPercent", variableList.getScrollPercent());
	}
}
