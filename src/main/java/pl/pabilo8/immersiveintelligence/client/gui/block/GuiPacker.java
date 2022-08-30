package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiLabelNoShadow;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiPackerTaskList;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDropdownList;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonSwitch;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblocks.metal.tileentities.first.TileEntityPacker;
import pl.pabilo8.immersiveintelligence.common.block.multiblocks.metal.tileentities.first.TileEntityPacker.PackerActionType;
import pl.pabilo8.immersiveintelligence.common.block.multiblocks.metal.tileentities.first.TileEntityPacker.PackerPutMode;
import pl.pabilo8.immersiveintelligence.common.block.multiblocks.metal.tileentities.first.TileEntityPacker.PackerTask;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPacker;
import pl.pabilo8.immersiveintelligence.common.util.IILib;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiPacker extends GuiIEContainerBase
{
	private static final ResourceLocation TEXTURE_ICONS = new ResourceLocation("immersiveintelligence:textures/gui/emplacement_icons.png");
	private static final ResourceLocation TEXTURE_PACKER = new ResourceLocation("immersiveintelligence:textures/gui/packer.png");
	TileEntityPacker tile;

	private GuiButtonIE buttonAdd, buttonRemove, buttonDuplicate, buttonClear;
	private GuiButtonState buttonRepeat;
	GuiPackerTaskList taskList;

	private int scroll;
	private final int maxScroll;
	private final ContainerPacker container;

	//page buttons
	private GuiButtonDropdownList putModeList;
	private GuiButtonSwitch switchOreDict, switchNBT;//, switchExpire;
	private GuiTextField textFieldAmount;//, textFieldExpire;

	public GuiPacker(InventoryPlayer inventoryPlayer, TileEntityPacker tile)
	{
		super(new ContainerPacker(inventoryPlayer, tile));
		this.xSize = 256;
		this.ySize = 211;
		this.tile = tile;
		this.container = ((ContainerPacker)inventorySlots);
		this.container.ghostUpdateFunction = this::saveBasicData;

		maxScroll = 18*(int)(Math.floor(container.slots.length/3f)-6);
		scroll = 0;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		buttonList.clear();
		labelList.clear();

		addLabel(5, 6, 78, 11, IILib.COLOR_H1, "Tasks");

		buttonAdd = addButton(new GuiButtonIE(buttonList.size(), guiLeft+6, guiTop+96+4+8, 12, 12, "", TEXTURE_PACKER.toString(), 0, 223));
		buttonRemove = addButton(new GuiButtonIE(buttonList.size(), guiLeft+6+12, guiTop+96+4+8, 12, 12, "", TEXTURE_PACKER.toString(), 24, 223));
		//buttonRemove.enabled = selected!=null;
		buttonDuplicate = addButton(new GuiButtonIE(buttonList.size(), guiLeft+6+24, guiTop+96+4+8, 12, 12, "", TEXTURE_PACKER.toString(), 48, 223));
		//buttonDuplicate.enabled = selected!=null;
		buttonClear = addButton(new GuiButtonIE(buttonList.size(), guiLeft+6+36, guiTop+96+4+8, 12, 12, "", TEXTURE_PACKER.toString(), 72, 223));

		buttonRepeat = addButton(new GuiButtonState(buttonList.size(), guiLeft+72, guiTop+5, 12, 12, "", tile.repeatActions, TEXTURE_PACKER.toString(), 0, 235, 0));

		taskList = addButton(taskList==null?new GuiPackerTaskList(buttonList.size(), guiLeft+6, guiTop+19, 78, 84, tile.hasUpgrade(IIContent.UPGRADE_UNPACKER_CONVERSION), tile.tasks, this::saveGuiToTask): taskList);
		taskList.recalculateEntries();

		container.ghostSlot.xPos = -2147483647;
		container.ghostSlot.yPos = -2147483647;
		container.ghostSlot.inventory.setInventorySlotContents(0, ItemStack.EMPTY);

		if(taskList.selectedOption!=-1)
		{
			PackerTask task = tile.tasks.get(taskList.selectedOption);
			if(task!=null)
			{
				addLabel(84, 10, 89, 0, IILib.COLOR_H1, "Task: "+
						I18n.format(IILib.DESCRIPTION_KEY+"metal_multiblock1.packer.task."+task.actionType.getActionName(tile.hasUpgrade(IIContent.UPGRADE_UNPACKER_CONVERSION)))
				).setCentered();

				addLabel(88, 26, IILib.COLOR_H1, "Item:");

				addLabel(88, 42, IILib.COLOR_H1, "Mode:");
				putModeList = addButton(new GuiButtonDropdownList(buttonList.size(), guiLeft+84+32, guiTop+10+6+20, 52, 20,
						PackerPutMode.values().length,
						Arrays.stream(PackerPutMode.values())
								.map(PackerPutMode::getName)
								.toArray(String[]::new)));
				putModeList.setTranslationFunc(s -> I18n.format(IILib.DESCRIPTION_KEY+"metal_multiblock1.packer.mode."+s));
				putModeList.selectedEntry = task.mode.ordinal();

				addLabel(88, 54, IILib.COLOR_H1, "Amount:");
				switchOreDict = switchOreDict==null?addSwitch(88, 78, 85, IILib.COLOR_H2, 0xb51500, 0x95ed00, task.stack.oreName!=null, "Uses OreDict", false): addButton(switchOreDict);
				switchNBT = switchNBT==null?addSwitch(88, 90, 85, IILib.COLOR_H2, 0xb51500, 0x95ed00, task.stack.useNBT, "NBT Sensitive:", false): addButton(switchNBT);
				//switchExpire = switchExpire==null?addSwitch(88, 90, 85, Utils.COLOR_H2, 0xb51500, 0x95ed00, false, "Expire After:", false): addButton(switchExpire);//task.stack.expires

				textFieldAmount = new GuiTextField(buttonList.size(), mc.fontRenderer, guiLeft+84+4, guiTop+10+6+10+32+1, 80, 14);
				textFieldAmount.setText(task.mode==PackerPutMode.ALL_POSSIBLE?"*": String.valueOf(task.stack.inputSize));
				textFieldAmount.setEnabled(task.mode!=PackerPutMode.ALL_POSSIBLE);

				//textFieldExpire = new GuiTextField(buttonList.size(), mc.fontRenderer, guiLeft+84+4, guiTop+10+6+10+32+1+42, 80, 14);
				//textFieldExpire.setEnabled(switchExpire.state);

				container.ghostSlot.xPos = 150;
				container.ghostSlot.yPos = 16;
				container.ghostSlot.inventory.setInventorySlotContents(0, task.stack.getExampleStack());
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if(this.taskList.selectedOption!=-1)
		{
			this.textFieldAmount.mouseClicked(mouseX, mouseY, 0);
			//this.textFieldExpire.mouseClicked(mouseX, mouseY, 0);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(this.taskList.selectedOption!=-1)
		{
			this.textFieldAmount.textboxKeyTyped(typedChar, keyCode);
			//this.textFieldExpire.textboxKeyTyped(typedChar, keyCode);
		}
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void actionPerformed(@Nonnull GuiButton button) throws IOException
	{
		if(button==buttonRepeat)
		{
			tile.repeatActions = buttonRepeat.state;
			saveBasicData();
		}
		else if(button==taskList)
		{
			//switchExpire = null;
			switchOreDict = null;
			switchNBT = null;
			initGui();
		}
		else if(button==buttonAdd)
		{
			tile.tasks.add(new PackerTask(PackerPutMode.ALL_POSSIBLE, PackerActionType.ITEM, new IngredientStack("*")));
			saveBasicData();
			initGui();
		}
		else if(button==buttonRemove)
		{
			if(taskList.selectedOption!=-1)
			{
				tile.tasks.remove(taskList.selectedOption);
				taskList.selectedOption = -1;
				saveBasicData();
				initGui();
			}
		}
		else if(button==buttonDuplicate)
		{
			if(taskList.selectedOption!=-1)
			{
				tile.tasks.add(new PackerTask(tile.tasks.get(taskList.selectedOption).toNBT()));
				saveBasicData();
				initGui();
			}
		}
		else if(button==buttonClear)
		{
			tile.tasks.clear();
			taskList.selectedOption = -1;
			saveBasicData();
			initGui();
		}
		else if(taskList.selectedOption!=-1)
		{
			//prevent resetting gui whilst dropping the list
			saveGuiToTask(taskList.selectedOption);
			if(!putModeList.dropped)
			{
				initGui();
			}
		}
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{

	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		saveBasicData();
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		//Handle scrolling slots
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int mouseChange = Mouse.getDWheel();

		if(Mouse.isButtonDown(0)&&isPointInRegion(238, 7, 11, 108, mx, my))
			scroll = ((int)(maxScroll*MathHelper.clamp((my-guiTop-7)/94f, 0, 1)))/18*18;
		else if(isPointInRegion(181, 5, 60, 112, mx, my))
			scroll -= Integer.signum(mouseChange)*18;

		scroll = MathHelper.clamp(scroll, 0, maxScroll);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		IIClientUtils.bindTexture(TEXTURE_PACKER);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, 122);
		this.drawTexturedModalRect(guiLeft+40, guiTop+122, 0, 122, 176, 89);

		this.drawTexturedModalRect(guiLeft+239, guiTop+8+(92*(scroll/((float)maxScroll))), 161, 211, 9, 14);

		//draw paper
		if(taskList.selectedOption!=-1)
		{
			this.drawTexturedModalRect(guiLeft+85, guiTop+3, 176, 122, 64, 119);
			this.drawTexturedModalRect(guiLeft+85+64, guiTop+3, 232, 122, 24, 119);
			if(textFieldAmount!=null)
				textFieldAmount.drawTextBox();
			/*
			if(textFieldExpire!=null)
				textFieldExpire.drawTextBox();
			*/

			ClientUtils.drawSlot(guiLeft+150, guiTop+16, 16, 16, 128);
		}

		for(Slot slot : container.slots)
		{
			if(slot==null)
				continue;

			int j = slot.getSlotIndex()-1;
			int x = j%3*18;
			int y = (j/3*18)-scroll;

			if((y < 0||y > 90))
			{
				slot.xPos = -2147483647;
				slot.yPos = -2147483647;
			}
			else
			{
				slot.xPos = 184+x;
				slot.yPos = 8+y;
			}

		}
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);

		this.renderHoveredToolTip(mx, my);

		ArrayList<String> tooltip = new ArrayList<>();
		if(buttonAdd.isMouseOver())
			tooltip.add(I18n.format("desc.immersiveintelligence.metal_multiblock1.emplacement.add"));
		else if(buttonRemove.isMouseOver())
			tooltip.add(I18n.format("desc.immersiveintelligence.metal_multiblock1.emplacement.remove"));
		else if(buttonDuplicate.isMouseOver())
			tooltip.add(I18n.format("desc.immersiveintelligence.metal_multiblock1.emplacement.duplicate"));
		else if(buttonClear.isMouseOver())
			tooltip.add(I18n.format("desc.immersiveintelligence.metal_multiblock1.emplacement.clear"));

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}

	protected GuiButtonSwitch addSwitch(int x, int y, int textWidth, int textColor, int color1, int color2, boolean state, String name, boolean firstTime)
	{
		return addButton(new GuiButtonSwitch(buttonList.size(), guiLeft+x, guiTop+y, textWidth, 8, 18, 9, 18, 52, state, TEXTURE_ICONS, textColor, color1, color2, name, firstTime));
	}

	protected GuiLabelNoShadow addLabel(int x, int y, int textColor, String... text)
	{
		return addLabel(x, y, 0, 0, textColor, text);
	}

	protected GuiLabelNoShadow addLabel(int x, int y, int w, int h, int textColor, String... text)
	{
		GuiLabelNoShadow guiLabel = new GuiLabelNoShadow(this.fontRenderer, labelList.size(), guiLeft+x, guiTop+y, w, h, textColor);
		Arrays.stream(text).forEachOrdered(guiLabel::addLine);
		labelList.add(guiLabel);
		return guiLabel;
	}

	private void saveGuiToTask(int task)
	{
		if(task==-1)
			return;

		PackerTask t = tile.tasks.get(task);
		t.mode = PackerPutMode.values()[putModeList.selectedEntry];


		IngredientStack is = null;
		ItemStack stack = container.ghostSlot.getStack();
		if(switchOreDict.state&&!stack.isEmpty())
		{
			int[] oids = OreDictionary.getOreIDs(stack);
			if(oids.length > 0)
				is = new IngredientStack(OreDictionary.getOreName(oids[0]));
		}
		t.stack = is==null?(stack.isEmpty()?new IngredientStack("*"): new IngredientStack(stack)): is;
		try
		{
			t.stack.inputSize = Integer.parseInt(textFieldAmount.getText());
		}
		catch(NumberFormatException ignored)
		{

		}
		t.stack.useNBT = switchNBT.state;
	}

	private void saveBasicData()
	{
		if(taskList.selectedOption!=-1)
			saveGuiToTask(taskList.selectedOption);

		NBTTagCompound tag = new NBTTagCompound();

		tag.setTag("tasks", tile.writeTasks());
		tag.setBoolean("repeatActions", buttonRepeat.state);

		ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(tile, tag));
	}

}
