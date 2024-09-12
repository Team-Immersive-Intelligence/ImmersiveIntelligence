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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.api.PackerHandler;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerActionType;
import pl.pabilo8.immersiveintelligence.api.PackerHandler.PackerTask;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiPackerTaskList;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDropdownList;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.elements.label.GuiLabelNoShadow;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPacker;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerPacker;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 25.08.2022
 */
public class GuiPacker extends GuiIEContainerBase
{
	private static final ResourceLocation TEXTURE_ICONS = new ResourceLocation("immersiveintelligence:textures/gui/emplacement_icons.png");
	private static final ResourceLocation TEXTURE_PACKER = new ResourceLocation("immersiveintelligence:textures/gui/packer.png");
	private static final ResourceLocation TEXTURE_PAGE = new ResourceLocation("immersiveintelligence:textures/gui/printed_page.png");
	TileEntityPacker tile;
	final SideResourceDisplay sideDisplay;
	private final ContainerPacker container;

	private GuiButtonIE buttonAdd, buttonRemove, buttonDuplicate, buttonClear, buttonSideInput, buttonSideOutput;
	private GuiButtonState buttonRepeat;
	private GuiPackerTaskList taskList;
	private GuiButtonDropdownList putModeList;
	private GuiButtonSwitch switchOreDict, switchNBT, switchDirection;
	private GuiTextField textFieldAmount;

	private final static IIColor COLOR_IN = IIColor.fromPackedRGB(0x4c7bb1), COLOR_OUT = IIColor.fromPackedRGB(0xffb515);

	public GuiPacker(EntityPlayer player, TileEntityPacker tile)
	{
		super(new ContainerPacker(player, tile));
		this.xSize = 256+80;
		this.ySize = 211;
		this.tile = tile;
		this.container = ((ContainerPacker)inventorySlots);
		this.container.ghostUpdateFunction = this::saveBasicData;

		if(tile.hasUpgrade(IIContent.UPGRADE_PACKER_FLUID))
			sideDisplay = new SideFluidDisplay(this);
		else if(tile.hasUpgrade(IIContent.UPGRADE_PACKER_ENERGY))
			sideDisplay = new SideEnergyDisplay(this);
		else
			sideDisplay = new SideItemDisplay(this);

	}

	@Override
	public void initGui()
	{
		super.initGui();
		buttonList.clear();
		labelList.clear();

		addLabel(5, 6, 78, 11, IIReference.COLOR_H1, "Tasks");

		buttonAdd = addButton(new GuiButtonIE(buttonList.size(), guiLeft+6, guiTop+96+4+8, 12, 12, "", TEXTURE_PACKER.toString(), 0, 223));
		buttonRemove = addButton(new GuiButtonIE(buttonList.size(), guiLeft+6+12, guiTop+96+4+8, 12, 12, "", TEXTURE_PACKER.toString(), 24, 223));
		buttonDuplicate = addButton(new GuiButtonIE(buttonList.size(), guiLeft+6+24, guiTop+96+4+8, 12, 12, "", TEXTURE_PACKER.toString(), 48, 223));
		buttonClear = addButton(new GuiButtonIE(buttonList.size(), guiLeft+6+36, guiTop+96+4+8, 12, 12, "", TEXTURE_PACKER.toString(), 72, 223));

		buttonRepeat = addButton(new GuiButtonState(buttonList.size(), guiLeft+72, guiTop+5, 12, 12, "", tile.repeatActions, TEXTURE_PACKER.toString(), 0, 235, 0));

		taskList = addButton(taskList==null?new GuiPackerTaskList(buttonList.size(), guiLeft+6, guiTop+19, 78, 84, tile.tasks, this::saveGuiToTask): taskList);
		taskList.recalculateEntries();

		if(sideDisplay instanceof SideItemDisplay)
		{
			buttonSideInput = addButton(new GuiButtonIE(buttonList.size(), guiLeft+xSize-80, guiTop-12, 39, 12, "Input", TEXTURE_PACKER.toString(), 0, 211));
			buttonSideOutput = addButton(new GuiButtonIE(buttonList.size(), guiLeft+xSize-40, guiTop-12, 39, 12, "Output", TEXTURE_PACKER.toString(), 0, 211));
		}
		else
			buttonSideInput = buttonSideOutput = null;

		if(container.ghostSlot!=null)
		{
			container.ghostSlot.xPos = -2147483647;
			container.ghostSlot.yPos = -2147483647;
			container.ghostSlot.inventory.setInventorySlotContents(0, ItemStack.EMPTY);
		}

		if(taskList.selectedOption!=-1)
		{
			PackerTask task = tile.tasks.get(taskList.selectedOption);
			if(task!=null)
			{
				addLabel(84, 10, 168, 0, IIReference.COLOR_H1, "Task: "+
						I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.packer.task."+task.actionType.getActionName(task.unpack))
				).setCentered();
				sideDisplay.initPage(task);
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
		//task repeating
		if(button==buttonRepeat)
		{
			tile.repeatActions = buttonRepeat.state;
			saveBasicData();
		}
		//input/output tabs
		else if(button==buttonSideInput||button==buttonSideOutput)
		{
			sideDisplay.outputPage = button==buttonSideOutput;
			saveBasicData();
			initGui();
		}
		//task list component
		else if(button==taskList)
		{
			//switchExpire = null;
			switchOreDict = null;
			switchNBT = null;
			initGui();
		}
		//task list buttons
		else if(button==buttonAdd)
		{
			tile.tasks.add(new PackerTask(PackerHandler.PackerPutMode.ALL_POSSIBLE, sideDisplay.getActionType(), new IngredientStack("*")));
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
		//when task was unselected
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
		//draw background
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		IIClientUtils.bindTexture(TEXTURE_PACKER);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 122);
		drawTexturedModalRect(guiLeft+81, guiTop+122, 0, 122, 176, 89);
		drawTexturedModalRect(guiLeft+256, guiTop, 176, 122, 80, 122);

		//draw paper

		//draw side / resource display
		sideDisplay.draw(mx, my);

		//draw task edit page
		if(taskList.selectedOption!=-1)
		{
			IIClientUtils.bindTexture(TEXTURE_PAGE);
			this.drawTexturedModalRect(guiLeft+85, guiTop+3, 0, 0, 84, 59);
			this.drawTexturedModalRect(guiLeft+85+84, guiTop+3, 148-84, 0, 84, 59);
			this.drawTexturedModalRect(guiLeft+85, guiTop+3+59, 0, 137, 84, 59);
			this.drawTexturedModalRect(guiLeft+85+84, guiTop+3+59, 148-84, 137, 84, 59);
			IIClientUtils.bindTexture(TEXTURE_PACKER);

			if(textFieldAmount!=null)
				textFieldAmount.drawTextBox();
			/*
			if(textFieldExpire!=null)
				textFieldExpire.drawTextBox();
			*/

			sideDisplay.drawPage(mx, my);
		}
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);

		this.renderHoveredToolTip(mx, my);

		ArrayList<String> tooltip = new ArrayList<>();
		//task list
		if(buttonAdd.isMouseOver())
			tooltip.add(I18n.format("desc.immersiveintelligence.metal_multiblock1.emplacement.add"));
		else if(buttonRemove.isMouseOver())
			tooltip.add(I18n.format("desc.immersiveintelligence.metal_multiblock1.emplacement.remove"));
		else if(buttonDuplicate.isMouseOver())
			tooltip.add(I18n.format("desc.immersiveintelligence.metal_multiblock1.emplacement.duplicate"));
		else if(buttonClear.isMouseOver())
			tooltip.add(I18n.format("desc.immersiveintelligence.metal_multiblock1.emplacement.clear"));
			//task
		else if(buttonSideInput!=null&&buttonSideInput.isMouseOver())
			tooltip.add(I18n.format("desc.immersiveintelligence.metal_multiblock1.packer.side_input"));
		else if(buttonSideOutput!=null&&buttonSideOutput.isMouseOver())
			tooltip.add(I18n.format("desc.immersiveintelligence.metal_multiblock1.packer.side_output"));
		else if(switchDirection!=null&&switchDirection.isMouseOver())
			tooltip.add(I18n.format("desc.immersiveintelligence.metal_multiblock1.packer.switch_direction"));
		else if(buttonRepeat.isMouseOver())
			tooltip.add(buttonRepeat.state?
					I18n.format("desc.immersiveintelligence.metal_multiblock1.packer.mode_iteration"):
					I18n.format("desc.immersiveintelligence.metal_multiblock1.packer.mode_single"));

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}

	protected GuiButtonSwitch addSwitch(int x, int y, int textWidth, IIColor textColor, IIColor color1, IIColor color2, boolean state, String name, boolean firstTime)
	{
		return addButton(new GuiButtonSwitch(buttonList.size(), guiLeft+x, guiTop+y, textWidth, 8, 18, 9, 18, 52, state, TEXTURE_ICONS, textColor, color1, color2, name, firstTime));
	}

	protected GuiLabelNoShadow addLabel(int x, int y, IIColor textColor, String... text)
	{
		return addLabel(x, y, 0, 0, textColor, text);
	}

	protected GuiLabelNoShadow addLabel(int x, int y, int w, int h, IIColor textColor, String... text)
	{
		GuiLabelNoShadow guiLabel = new GuiLabelNoShadow(this.fontRenderer, labelList.size(), guiLeft+x, guiTop+y, w, h, textColor);
		Arrays.stream(text).forEachOrdered(guiLabel::addLine);
		labelList.add(guiLabel);
		return guiLabel;
	}

	/**
	 * Saves button values to task
	 */
	private void saveGuiToTask(int task)
	{
		if(task==-1)
			return;

		PackerTask t = tile.tasks.get(task);
		t.mode = PackerHandler.PackerPutMode.values()[putModeList.selectedEntry];
		sideDisplay.saveTask(t);
	}

	/**
	 * Sends an NBT sync message<br>
	 * Called when current task is to be changed or before closing the gui
	 */
	private void saveBasicData()
	{
		if(taskList.selectedOption!=-1)
			saveGuiToTask(taskList.selectedOption);

		NBTTagCompound tag = new NBTTagCompound();

		tag.setTag("tasks", tile.writeTasks());
		tag.setBoolean("repeatActions", buttonRepeat.state);

		ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(tile, tag));
	}

	private static abstract class SideResourceDisplay
	{
		protected final GuiPacker gui;
		protected boolean outputPage = false;

		public SideResourceDisplay(GuiPacker gui)
		{
			this.gui = gui;
		}

		/**
		 * Draws this side display
		 */
		abstract void draw(int mx, int my);

		/**
		 * Returns action type: {@link PackerActionType#ITEM}, {@link PackerActionType#FLUID} or {@link PackerActionType#ENERGY}
		 */
		abstract PackerActionType getActionType();

		/**
		 * Sets current button values to task, so it can later be sent via a NBT message
		 */
		void saveTask(PackerTask task)
		{
			//save number
			try {task.stack.inputSize = Integer.parseInt(gui.textFieldAmount.getText());} catch(
					NumberFormatException ignored) {}
			task.unpack = gui.switchDirection.state;
		}

		public void initPage(PackerTask task)
		{
			gui.addLabel(88, 42, IIReference.COLOR_H1, "Mode:");
			gui.putModeList = gui.addButton(new GuiButtonDropdownList(gui.buttonList.size(), gui.guiLeft+84+32+20, gui.guiTop+10+6+20, 112, 20,
					PackerHandler.PackerPutMode.values().length,
					Arrays.stream(PackerHandler.PackerPutMode.values())
							.map(PackerHandler.PackerPutMode::getName)
							.toArray(String[]::new)));
			gui.putModeList.setTranslationFunc(s -> I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.packer.mode."+s));
			gui.putModeList.selectedEntry = task.mode.ordinal();

			gui.addLabel(88, 54, IIReference.COLOR_H1, "Amount:");
			gui.addLabel(88+80, 54, IIReference.COLOR_H1, "Expires After:");

			gui.textFieldAmount = new GuiTextField(gui.buttonList.size(), gui.mc.fontRenderer, gui.guiLeft+84+4, gui.guiTop+10+6+10+32+1, 72, 14);
			gui.textFieldAmount.setText(task.mode==PackerHandler.PackerPutMode.ALL_POSSIBLE?"*": String.valueOf(task.stack.inputSize));
			gui.textFieldAmount.setEnabled(task.mode!=PackerHandler.PackerPutMode.ALL_POSSIBLE);

			gui.switchDirection = gui.switchDirection==null?gui.addSwitch(232, 4, 85, IIReference.COLOR_H2, COLOR_IN, COLOR_OUT, task.unpack, "", false): gui.addButton(gui.switchDirection);
		}

		public abstract void drawPage(int mx, int my);
	}

	private static class SideItemDisplay extends SideResourceDisplay
	{
		//slot scrolling
		private int scroll;
		private final int maxScroll;

		public SideItemDisplay(GuiPacker gui)
		{
			super(gui);
			maxScroll = 18*(int)(Math.floor((54)/3f)-6);
			scroll = 0;
		}

		@Override
		void draw(int mx, int my)
		{
			//Handle scrolling slots
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			int mouseChange = Mouse.getDWheel();

			if(Mouse.isButtonDown(0)&&gui.isPointInRegion(238+80, 7, 11, 108, mx, my))
				scroll = ((int)(maxScroll*MathHelper.clamp((my-gui.guiTop-7)/94f, 0, 1)))/18*18;
			else if(gui.isPointInRegion(181+80, 5, 60+11, 112, mx, my))
				scroll -= Integer.signum(mouseChange)*18;

			scroll = MathHelper.clamp(scroll, 0, maxScroll);


			GlStateManager.pushMatrix();
			//GlStateManager.translate(guiLeft+239+80, guiTop+8, 0);
			GlStateManager.rotate(90, 0, 0, 0);
			gui.drawTexturedModalRect(0, 0, 148, 224, 108, 12);
			GlStateManager.popMatrix();

			gui.drawTexturedModalRect(gui.guiLeft+239+80, gui.guiTop+8+(92*(scroll/((float)maxScroll))), 161, 211, 9, 14);

			int pageScroll = outputPage?(scroll+108+maxScroll): scroll;
			for(Slot slot : gui.container.slots)
			{
				if(slot==null)
					continue;

				int j = slot.getSlotIndex()-1;
				int x = j%3*18;
				int y = (j/3*18)-pageScroll;

				if((y < 0||y > 90))
				{
					slot.xPos = -2147483647;
					slot.yPos = -2147483647;
				}
				else
				{
					slot.xPos = 184+80+x;
					slot.yPos = 8+y;

					//draw slot texture underneath
					gui.drawTexturedModalRect(gui.guiLeft+184+80-1+x, gui.guiTop+8-1+y, 48, 235, 18, 18);
				}

			}
		}

		@Override
		PackerActionType getActionType()
		{
			return PackerActionType.ITEM;
		}

		@Override
		void saveTask(PackerTask task)
		{
			super.saveTask(task);
			IngredientStack is = null;
			ItemStack stack = gui.container.ghostSlot.getStack();
			if(gui.switchOreDict.state&&!stack.isEmpty())
			{
				int[] oreIDs = OreDictionary.getOreIDs(stack);
				if(oreIDs.length > 0)
					is = new IngredientStack(OreDictionary.getOreName(oreIDs[0]));
			}
			task.stack = is==null?(stack.isEmpty()?new IngredientStack("*"): new IngredientStack(stack)): is;
			task.stack.useNBT = gui.switchNBT.state;
		}

		@Override
		public void initPage(PackerTask task)
		{
			super.initPage(task);

			gui.addLabel(88, 26, IIReference.COLOR_H1, "Item:");
			gui.switchOreDict = gui.switchOreDict==null?gui.addSwitch(88, 78, 85, IIReference.COLOR_H2, IIColor.fromPackedRGB(0xb51500), IIColor.fromPackedRGB(0x95ed00), task.stack.oreName!=null, "Uses OreDict", false): gui.addButton(gui.switchOreDict);
			gui.switchNBT = gui.switchNBT==null?gui.addSwitch(88, 90, 85, IIReference.COLOR_H2, IIColor.fromPackedRGB(0xb51500), IIColor.fromPackedRGB(0x95ed00), task.stack.useNBT, "NBT Sensitive:", false): gui.addButton(gui.switchNBT);
			//switchExpire = switchExpire==null?addSwitch(88, 90, 85, Utils.COLOR_H2, 0xb51500, 0x95ed00, false, "Expire After:", false): addButton(switchExpire);//task.stack.expires

			//textFieldExpire = new GuiTextField(buttonList.size(), mc.fontRenderer, guiLeft+84+4, guiTop+10+6+10+32+1+42, 80, 14);
			//textFieldExpire.setEnabled(switchExpire.state);

			gui.container.ghostSlot.xPos = 150;
			gui.container.ghostSlot.yPos = 16;
			gui.container.ghostSlot.inventory.setInventorySlotContents(0, task.stack.getExampleStack());
		}

		@Override
		public void drawPage(int mx, int my)
		{
			ClientUtils.drawSlot(gui.guiLeft+150, gui.guiTop+16, 16, 16, 128);
		}
	}

	private static class SideFluidDisplay extends SideResourceDisplay
	{
		public SideFluidDisplay(GuiPacker gui)
		{
			super(gui);
		}

		@Override
		void draw(int mx, int my)
		{
			//CTMB's GUI system needs to be moved to II, it really needs to
			IIClientUtils.bindTexture(TEXTURE_ICONS);
			gui.drawTexturedModalRect(gui.guiLeft+264, gui.guiTop+12, 40, 0, 40, 40);

			gui.drawTexturedModalRect(gui.guiLeft+264, gui.guiTop+12, 40, 0, 40, 40);
			gui.drawTexturedModalRect(gui.guiLeft+264, gui.guiTop+12+40, 40, 19, 40, 20);
			gui.drawTexturedModalRect(gui.guiLeft+264, gui.guiTop+12+60, 40, 10, 40, 40);

			int hh = 0;

			for(FluidStack fluid : gui.tile.fluidTank.fluids)
			{
				int height = (int)((fluid.amount/(float)gui.tile.fluidTank.getCapacity())*96);
				hh += height;
				ClientUtils.drawRepeatedFluidSprite(fluid, gui.guiLeft+264, gui.guiTop+12+2+96-hh, 40, height);
			}

			IIClientUtils.bindTexture(TEXTURE_ICONS);
			gui.drawTexturedModalRect(gui.guiLeft+264, gui.guiTop+12, 80, 0, 40, 40);
			gui.drawTexturedModalRect(gui.guiLeft+264, gui.guiTop+12+40, 80, 19, 40, 20);
			gui.drawTexturedModalRect(gui.guiLeft+264, gui.guiTop+12+60, 80, 10, 40, 40);

			gui.drawTexturedModalRect(gui.guiLeft+308, gui.guiTop+34, 0, 50, 18, 18);
			gui.drawTexturedModalRect(gui.guiLeft+308, gui.guiTop+70, 0, 50, 18, 18);

			//ClientUtils.handleGuiTank(gui.tile.fluidTank, gui.guiLeft+264, gui.guiTop+8, 40, 50, 80, 0, 40, 50, mx, my, TEXTURE_PACKER.toString(), null);
		}

		@Override
		PackerActionType getActionType()
		{
			return PackerActionType.FLUID;
		}

		@Override
		void saveTask(PackerTask task)
		{
			super.saveTask(task);
		}

		@Override
		public void initPage(PackerTask task)
		{
			super.initPage(task);
		}

		@Override
		public void drawPage(int mx, int my)
		{
			// TODO: 25.08.2022 draw tank
		}
	}

	private static class SideEnergyDisplay extends SideResourceDisplay
	{
		public SideEnergyDisplay(GuiPacker gui)
		{
			super(gui);
		}

		@Override
		void draw(int mx, int my)
		{
			IIClientUtils.bindTexture(TEXTURE_ICONS);

			gui.drawTexturedModalRect(gui.guiLeft+308, gui.guiTop+34, 0, 50, 18, 18);
			gui.drawTexturedModalRect(gui.guiLeft+308, gui.guiTop+70, 0, 50, 18, 18);
		}

		@Override
		PackerActionType getActionType()
		{
			return PackerActionType.ENERGY;
		}

		@Override
		void saveTask(PackerTask task)
		{
			super.saveTask(task);
		}

		@Override
		public void initPage(PackerTask task)
		{
			super.initPage(task);
		}

		@Override
		public void drawPage(int mx, int my)
		{
			// TODO: 25.08.2022 draw capacitor
		}
	}

}