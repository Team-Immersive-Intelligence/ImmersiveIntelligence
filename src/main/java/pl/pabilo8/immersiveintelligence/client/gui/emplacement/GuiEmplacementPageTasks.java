package pl.pabilo8.immersiveintelligence.client.gui.emplacement;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IStringSerializable;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiEmplacementTaskList;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonCheckboxII;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonSwitch;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityEmplacement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author Pabilo8
 * @since 16.07.2021
 */
public class GuiEmplacementPageTasks extends GuiEmplacement
{
	int currentTab = 0;
	int prevScrollPos = 0;
	private GuiButtonIE[] taskTabButtons = new GuiButtonIE[0];
	private GuiTextField valueEdit;

	private GuiButtonSwitch buttonEnabled;
	private GuiButtonCheckboxII buttonInverted;
	private GuiButtonIE buttonAdd, buttonRemove, buttonDuplicate, buttonClear;
	private GuiButtonIE buttonTypePrev, buttonTypeNext;
	private GuiEmplacementTaskList buttonTaskList;

	private TaskFilter selected = null;

	private boolean tasksModified = false;
	private ArrayList<TaskFilter> taskFilters = new ArrayList<>();

	public GuiEmplacementPageTasks(InventoryPlayer inventoryPlayer, TileEntityEmplacement tile)
	{
		super(inventoryPlayer, tile, IIGuiList.GUI_EMPLACEMENT_TASKS);
		title = I18n.format(CommonProxy.DESCRIPTION_KEY+"metal_multiblock1.emplacement.tasks");
	}

	@Override
	public void initGui()
	{
		super.initGui();

		taskTabButtons = new GuiButtonIE[]{
				addTaskTabButton(0),
				addTaskTabButton(1),
				addTaskTabButton(2),
				addTaskTabButton(3),
		};

		buttonEnabled = addSwitch(122+11, 17, 60, 0x0a0a0a, 0xb51500, 0x95ed00, currentTab==tile.defaultTargetMode,
				I18n.format(CommonProxy.DESCRIPTION_KEY+"metal_multiblock1.emplacement.task_enabled"), tasksModified);

		addLabel(122, 32+16-12, 83, 0, 0x0a0a0a, "Selector Preset").setCentered();
		buttonInverted = addButton(new GuiButtonCheckboxII(buttonList.size(), guiLeft+122, guiTop+32+33-6+44, I18n.format(CommonProxy.DESCRIPTION_KEY+"metal_multiblock1.emplacement.task_negation"), !tile.redstoneControl));
		if(selected!=null)
			buttonInverted.state = selected.negation;
		addLabel(122, 32+33+16, 83, 0, 0x0a0a0a, "Filter").setCentered();

		buttonAdd = addButton(new GuiButtonIE(buttonList.size(), guiLeft+4, guiTop+32+2+96, 48, 12, I18n.format(CommonProxy.DESCRIPTION_KEY+"metal_multiblock1.emplacement.add"), TEXTURE_ICONS.toString(), 0, 89));
		buttonRemove = addButton(new GuiButtonIE(buttonList.size(), guiLeft+4+48, guiTop+32+2+96, 48, 12, I18n.format(CommonProxy.DESCRIPTION_KEY+"metal_multiblock1.emplacement.remove"), TEXTURE_ICONS.toString(), 0, 89));
		buttonRemove.enabled = selected!=null;
		buttonDuplicate = addButton(new GuiButtonIE(buttonList.size(), guiLeft+4+48+48, guiTop+32+2+96, 48, 12, I18n.format(CommonProxy.DESCRIPTION_KEY+"metal_multiblock1.emplacement.duplicate"), TEXTURE_ICONS.toString(), 0, 89));
		buttonDuplicate.enabled = selected!=null;
		buttonClear = addButton(new GuiButtonIE(buttonList.size(), guiLeft+4+48+48+48, guiTop+32+2+96, 48, 12, I18n.format(CommonProxy.DESCRIPTION_KEY+"metal_multiblock1.emplacement.clear"), TEXTURE_ICONS.toString(), 0, 89));

		buttonTypePrev = addButton(new GuiButtonIE(buttonList.size(), guiLeft+120, guiTop+32+8, 12, 12, "", TEXTURE_ICONS.toString(), 96, 89)).setHoverOffset(12, 0);
		buttonTypeNext = addButton(new GuiButtonIE(buttonList.size(), guiLeft+192, guiTop+32+8, 12, 12, "", TEXTURE_ICONS.toString(), 120, 89)).setHoverOffset(12, 0);

		valueEdit = new GuiTextField(buttonList.size(), this.fontRenderer, guiLeft+120, guiTop+86, 84, 13);
		//this.valueEdit.setFocused(true);

		if(selected!=null)
			addLabel(120+12, 32+15, 60, 0, 0x0a0a0a, CommonProxy.DESCRIPTION_KEY+"metal_multiblock1.emplacement.target."+selected.type.getName()).setCentered();

		buttonInverted.enabled = (selected!=null);
		buttonTypePrev.enabled = (selected!=null);
		buttonTypeNext.enabled = (selected!=null);
		valueEdit.setEnabled(selected!=null);
		if(selected!=null)
			valueEdit.setText(selected.filter);

		if(!tasksModified)
			loadTaskIntoList();
		buttonTaskList = addButton(new GuiEmplacementTaskList(buttonList.size(), guiLeft+6, guiTop+34, 93, 94, taskFilters.toArray(new TaskFilter[0])));
		if(selected!=null)
			buttonTaskList.selectedOption = taskFilters.indexOf(selected);

	}

	private void loadTaskIntoList()
	{
		tasksModified = true;
		taskFilters.clear();
		NBTTagCompound nbt = tile.defaultTaskNBT[currentTab];
		NBTTagList tagList = nbt.getTagList("filters", 10);
		for(NBTBase nbtBase : tagList)
			if(nbtBase instanceof NBTTagCompound)
			{
				taskFilters.add(new TaskFilter(((NBTTagCompound)nbtBase)));
			}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		if(button instanceof GuiButtonIE&&Arrays.stream(taskTabButtons).anyMatch(guiButtonIE -> guiButtonIE==button))
		{
			syncDataToServer();
			currentTab = Integer.parseInt(button.displayString)-1;
			tasksModified = false;
			selected = null;
			initGui();
		}
		else if(button==buttonAdd)
		{
			taskFilters.add(new TaskFilter(EnumTaskType.MOBS, false, ""));
			initGui();
		}
		else if(button==buttonRemove)
		{
			taskFilters.remove(selected);
			initGui();
		}
		else if(button==buttonDuplicate)
		{
			taskFilters.add(new TaskFilter(selected.type, selected.negation, selected.filter));
			initGui();
		}
		else if(button==buttonClear)
		{
			taskFilters.clear();
			initGui();
		}
		else if(button==buttonTaskList)
		{
			if(buttonTaskList.selectedOption==-1)
				selected = null;
			else
			{
				if(isShiftKeyDown())
				{
					if(taskFilters.get(buttonTaskList.selectedOption)==selected)
						selected = null;
					taskFilters.remove(buttonTaskList.selectedOption);
				}
				else
					selected = taskFilters.get(buttonTaskList.selectedOption);
			}
			initGui();
		}
		else if(button==buttonInverted)
		{
			selected.negation = buttonInverted.state;
		}
		else if(button==buttonTypeNext)
		{
			selected.type = EnumTaskType.values()[Utils.cycleInt(true, selected.type.ordinal(), 0, EnumTaskType.values().length-1)];
			initGui();
		}
		else if(button==buttonTypePrev)
		{
			selected.type = EnumTaskType.values()[Utils.cycleInt(false, selected.type.ordinal(), 0, EnumTaskType.values().length-1)];
			initGui();
		}
	}

	@Override
	protected void syncDataToServer()
	{
		super.syncDataToServer();
		NBTTagCompound nbt = new NBTTagCompound();
		if(buttonEnabled.state)
		{
			tile.defaultTargetMode=currentTab;
			nbt.setInteger("defaultTargetMode", currentTab);
		}
		else if(tile.defaultTargetMode==currentTab)
		{
			tile.defaultTargetMode=-1;
			nbt.setInteger("defaultTargetMode", -1);
		}

		NBTTagCompound filterTag = new NBTTagCompound();
		NBTTagList tagList = new NBTTagList();
		for(TaskFilter filter : taskFilters)
		{
			NBTTagCompound cc = new NBTTagCompound();
			cc.setString("type", filter.type.getName().toLowerCase());
			cc.setBoolean("negation", filter.negation);
			cc.setString("filter", filter.filter);
			tagList.appendTag(cc);
		}
		filterTag.setTag("filters", tagList);
		nbt.setTag("defaultTaskNBT"+(currentTab+1), filterTag);

		ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(this.tile, nbt));
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		syncDataToServer();
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if(valueEdit!=null)
			this.valueEdit.updateCursorCounter();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.valueEdit.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException
	{
		if(valueEdit!=null)
		{
			this.valueEdit.textboxKeyTyped(par1, par2);
			if(selected!=null)
				selected.filter = this.valueEdit.getText();
			if(!(par2==Keyboard.KEY_E&&this.valueEdit.isFocused()))
				super.keyTyped(par1, par2);
		}
		else
		{
			super.keyTyped(par1, par2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mx, int my)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mx, my);
		bindIcons();
		this.drawTexturedModalRect(guiLeft+128, guiTop+26, 56, 113, 77, 2);
		this.drawTexturedModalRect(guiLeft+4, guiTop+32, 56, 115, 107, 96);

		bindTexture();
		this.drawTexturedModalRect(guiLeft+113, guiTop+122-96, 205, 44, 6, 96);
		this.drawTexturedModalRect(guiLeft+113, guiTop+122, 205, 140, 6, 6);
		ClientUtils.drawRepeatedSprite(guiLeft+113+6, guiTop+122, 86, 6, 20, 6, 211/256f, 231/256f, 140/256f, 146/256f);

		bindIcons();
		//this.drawTexturedModalRect(guiLeft+101, guiTop+33+80, 153, 101, 9, 14);

		this.valueEdit.drawTextBox();

	}

	private GuiButtonIE addTaskTabButton(int id)
	{
		return addButton(new GuiButtonIE(buttonList.size(), guiLeft+(id*32), guiTop+16, 32, 12, String.valueOf(id+1), TEXTURE_ICONS.toString(), id==currentTab?56: 87, 101));
	}


	public static class TaskFilter
	{
		public EnumTaskType type;
		protected boolean negation;
		protected String filter;

		public TaskFilter(EnumTaskType type, boolean negation, String filter)
		{
			this.type = type;
			this.negation = negation;
			this.filter = filter;
		}

		public TaskFilter(NBTTagCompound tag)
		{
			this(EnumTaskType.valueOf(tag.getString("type").toUpperCase()), tag.getBoolean("negation"), tag.getString("filter"));
		}
	}

	public enum EnumTaskType implements IStringSerializable
	{
		MOBS,
		ANIMALS,
		PLAYERS,
		NPCS,
		VEHICLES,
		SHELLS,
		TEAM,
		NAME;

		@Override
		public String getName()
		{
			return this.toString().toLowerCase(Locale.ENGLISH);
		}
	}
}
