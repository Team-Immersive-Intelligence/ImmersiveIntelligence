package pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiButtonItemAdvanced;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine.ContainerArithmeticLogicMachineStorage;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.MessageGuiNBT;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Pabilo8 on 30-06-2019.
 * Some code you write, some you steal, but in most cases you adapt the existing solutions...
 */
public class GuiArithmeticLogicMachineStorage extends GuiIEContainerBase
{
	public static final String texture_storage = ImmersiveIntelligence.MODID+":textures/gui/arithmetic_logic_machine_storage.png";

	public TileEntityArithmeticLogicMachine tile;
	public InventoryPlayer playerInv;
	IItemHandler handler;
	public ContainerArithmeticLogicMachineStorage container;

	DataPacket list;

	//It was necessary to make the Gui control the Container
	public GuiArithmeticLogicMachineStorage(InventoryPlayer inventoryPlayer, TileEntityArithmeticLogicMachine tile)
	{
		super(new ContainerArithmeticLogicMachineStorage(inventoryPlayer, tile));
		this.ySize = 222;
		this.playerInv = inventoryPlayer;
		this.tile = tile;
		this.container = (ContainerArithmeticLogicMachineStorage)this.inventorySlots;
		this.handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		refreshStoredData();
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		if(getSlotUnderMouse()!=null)
			initGui();
	}

	@Override
	public void initGui()
	{

		IIPacketHandler.INSTANCE.sendToServer(new MessageBooleanAnimatedPartsSync(true, 0, tile.getPos()));

		super.initGui();
		refreshStoredData();
		this.buttonList.clear();

		this.buttonList.add(new GuiButtonIE(0, guiLeft-28, guiTop+4, 28, 24, "", texture_storage, 204, 0));
		if(!container.getInventory().get(0).isEmpty())
			this.buttonList.add(new GuiButtonItemAdvanced(1, guiLeft-28, guiTop+28, 28, 24, texture_storage, 176, 24, container.getInventory().get(0), 6, 2).setHoverOffset(28, 0));
		if(!container.getInventory().get(1).isEmpty())
			this.buttonList.add(new GuiButtonItemAdvanced(2, guiLeft-28, guiTop+52, 28, 24, texture_storage, 176, 24, container.getInventory().get(1), 6, 2).setHoverOffset(28, 0));
		if(!container.getInventory().get(2).isEmpty())
			this.buttonList.add(new GuiButtonItemAdvanced(3, guiLeft-28, guiTop+76, 28, 24, texture_storage, 176, 24, container.getInventory().get(2), 6, 2).setHoverOffset(28, 0));
		if(!container.getInventory().get(3).isEmpty())
			this.buttonList.add(new GuiButtonItemAdvanced(4, guiLeft-28, guiTop+100, 28, 24, texture_storage, 176, 24, container.getInventory().get(3), 6, 2).setHoverOffset(28, 0));
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		NBTTagCompound tag = new NBTTagCompound();

		if(button.id==0)
		{
			this.initGui();
		}
		else if(button.id==1)
		{
			syncDataToServer();
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1, tile.getPos(), playerInv.player));
		}
		else if(button.id==2)
		{
			syncDataToServer();
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2, tile.getPos(), playerInv.player));
		}
		else if(button.id==3)
		{
			syncDataToServer();
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3, tile.getPos(), playerInv.player));
		}
		else if(button.id==4)
		{
			syncDataToServer();
			IIPacketHandler.INSTANCE.sendToServer(new MessageGuiNBT(IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_4, tile.getPos(), playerInv.player));
		}
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.arithmetic_logic_machine.storage"), 8, 6, 0x0a0a0a);
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);

		ArrayList<String> tooltip = new ArrayList<String>();

		if(!handler.getStackInSlot(0).isEmpty()&&mx >= guiLeft-28&&mx < guiLeft&&my >= guiTop+28&&my < guiTop+52)
			tooltip.add(handler.getStackInSlot(0).getDisplayName());

		if(!handler.getStackInSlot(1).isEmpty()&&mx >= guiLeft-28&&mx < guiLeft&&my >= guiTop+52&&my < guiTop+76)
			tooltip.add(handler.getStackInSlot(1).getDisplayName());

		if(!handler.getStackInSlot(2).isEmpty()&&mx >= guiLeft-28&&mx < guiLeft&&my >= guiTop+76&&my < guiTop+100)
			tooltip.add(handler.getStackInSlot(2).getDisplayName());

		if(!handler.getStackInSlot(3).isEmpty()&&mx >= guiLeft-28&&mx < guiLeft&&my >= guiTop+100&&my < guiTop+124)
			tooltip.add(handler.getStackInSlot(3).getDisplayName());

		//Draw the punchcard progress bar
		GlStateManager.pushMatrix();

		mc.getTextureManager().bindTexture(new ResourceLocation(texture_storage));
		//ClientUtils.bindTexture();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();

		GlStateManager.popMatrix();

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, fontRenderer, -1, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}

	@Override
	public void onGuiClosed()
	{
		syncDataToServer();
		IIPacketHandler.INSTANCE.sendToServer(new MessageBooleanAnimatedPartsSync(false, 0, tile.getPos()));
		super.onGuiClosed();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture_storage);

		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	//Used to refresh gui variables after one of the variables is changed
	void refreshStoredData()
	{
		tile = (TileEntityArithmeticLogicMachine)tile.getWorld().getTileEntity(tile.getPos());
	}

	void syncDataToServer()
	{
		//Nothing important ^^ (but i should leave it for easier copying in future)
	}
}
