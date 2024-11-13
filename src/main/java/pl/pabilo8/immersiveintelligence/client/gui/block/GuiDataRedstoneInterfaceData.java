package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.ITabbedGui;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.DataInputMachine;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerRedstoneDataInterface;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageGuiNBT;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 09-02-2020
 */
public class GuiDataRedstoneInterfaceData extends GuiIEContainerBase implements ITabbedGui
{
	public static final String texture = ImmersiveIntelligence.MODID+":textures/gui/data_redstone_interface.png";

	public int scroll;
	public int maxScroll;

	public TileEntityRedstoneInterface tile;
	public InventoryPlayer playerInv;
	public ContainerRedstoneDataInterface container;
	DataPacket list;
	private boolean wasDown = false;

	//It was necessary to make the Gui control the Container
	public GuiDataRedstoneInterfaceData(EntityPlayer player, TileEntityRedstoneInterface tile)
	{
		//Tricky, but definitely doable!
		super(new ContainerRedstoneDataInterface(player, tile));

		this.ySize = 222;
		this.playerInv = player.inventory;
		this.tile = tile;
		this.container = (ContainerRedstoneDataInterface)this.inventorySlots;

		this.list = tile.storedData;

		refreshStoredData();

		ClientProxy proxy = (ClientProxy)ImmersiveIntelligence.proxy;
		if(positionEqual(proxy, tile))
			if(proxy.storedGuiData.hasKey("scrollPercent"))
				scroll = Math.round(proxy.storedGuiData.getFloat("scrollPercent")*maxScroll);

		refreshStoredData();
	}

	@Override
	public void initGui()
	{
		super.initGui();
		refreshStoredData();
		this.buttonList.clear();

		this.buttonList.add(new GuiButtonIE(0, guiLeft-28, guiTop+4, 28, 24, "", texture, 204, 0));
		this.buttonList.add(new GuiButtonIE(1, guiLeft-28, guiTop+28, 28, 24, "", texture, 176, 24).setHoverOffset(28, 0));
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		NBTTagCompound tag = new NBTTagCompound();

		if(button.id==0)
			this.initGui();
		else if(button.id==1)
		{
			syncDataToServer();
			IIPacketHandler.sendToServer(new MessageGuiNBT(IIGuiList.GUI_DATA_REDSTONE_INTERFACE_REDSTONE, tile));
		}
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.redstone_interface.data"), 4, 2, IIReference.COLOR_H1.getPackedRGB());
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);

		ArrayList<String> tooltip = new ArrayList<>();

		if(mx >= guiLeft-28&&mx < guiLeft&&my >= guiTop+4&&my < guiTop+28)
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"data_to_redstone_module"));

		if(mx >= guiLeft-28&&mx < guiLeft&&my >= guiTop+28&&my < guiTop+56)
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"redstone_to_data_module"));

		//Draw the punchcard progress bar
		GlStateManager.pushMatrix();

		mc.getTextureManager().bindTexture(new ResourceLocation(texture));
		//ClientUtils.bindTexture();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();

		this.drawTexturedModalRect(guiLeft+5, guiTop+44, 176, 48, 16, Math.round(48*(tile.productionProgress/DataInputMachine.timePunchtapeProduction)));

		GlStateManager.popMatrix();


		GlStateManager.pushMatrix();

		//GLScissor forces the drawing functions to draw only inside given coordinates
		//https://www.khronos.org/registry/OpenGL-Refpages/es2.0/xhtml/glScissor.xml

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		scissor(guiLeft+32, guiTop+12, 128, 114);

		//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);


		int i = 0;
		for(char c : DataPacket.varCharacters)
			if(list.variables.containsKey(c))
			{
				DataType data = list.getPacketVariable(c);
				//Base
				int drawx = guiLeft+32;
				int drawy = guiTop+12+(i*24)-scroll;

				GL11.glPushMatrix();
				GlStateManager.disableLighting();

				boolean isIn = IIMath.isPointInRectangle(drawx, drawy, drawx+128, drawy+20, mx, my);
				boolean canDelete = isIn&&isShiftKeyDown();

				if(canDelete)
				{
					tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"variable_remove_desc"));
					if(Mouse.isButtonDown(0)&&!wasDown)
					{
						onVariableRemoveButtonClick(c, true);
						GL11.glPopMatrix();
						break;
					}
				}

				GL11.glColor4f(canDelete?0.5f: 1.0F, canDelete?0.5f: 1.0F, canDelete?0.5f: 1.0F, 1.0F);

				GlStateManager.enableAlpha();

				ClientUtils.bindTexture(texture);

				this.drawTexturedModalRect(drawx, drawy, 0, 222, 128, 20);

				IIClientUtils.bindTexture(data.getTextureLocation());
				ClientUtils.bindTexture(texture);

				//Variable type based effects
				float[] rgb = data.getTypeColor().getFloatRGB();
				GL11.glColor4f(rgb[0], rgb[1], rgb[2], 1f);
				this.drawTexturedModalRect(drawx, drawy, 173, 222, 8, 20);
				this.drawTexturedModalRect(drawx+52, drawy, 184, 222, 22, 20);
				this.drawTexturedModalRect(drawx+120, drawy, 173+32+4, 222, 8, 20);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);


				byte changeButton = 0;

				if(IIMath.isPointInRectangle(drawx+52, drawy+4, drawx+60, drawy+9, mx, my))
				{
					this.drawTexturedModalRect(drawx+52, drawy+4, 72, 242, 8, 6);
					changeButton = 1;
				}
				this.drawTexturedModalRect(drawx+52, drawy+4, 64, 242, 8, 6);

				if(IIMath.isPointInRectangle(drawx+52, drawy+10, drawx+60, drawy+15, mx, my))
				{
					this.drawTexturedModalRect(drawx+52, drawy+10, 72, 248, 8, 6);
					changeButton = 2;
				}
				this.drawTexturedModalRect(drawx+52, drawy+10, 64, 248, 8, 6);

				if(IIMath.isPointInRectangle(drawx+22, drawy+4, drawx+30, drawy+9, mx, my))
				{
					this.drawTexturedModalRect(drawx+22, drawy+4, 72, 242, 8, 6);
					changeButton = 3;
				}
				this.drawTexturedModalRect(drawx+22, drawy+4, 64, 242, 8, 6);

				if(IIMath.isPointInRectangle(drawx+22, drawy+10, drawx+30, drawy+15, mx, my))
				{
					this.drawTexturedModalRect(drawx+22, drawy+10, 72, 248, 8, 6);
					changeButton = 4;
				}
				this.drawTexturedModalRect(drawx+22, drawy+10, 64, 248, 8, 6);

				if(IIMath.isPointInRectangle(drawx+62, drawy+4, drawx+125, drawy+17, mx, my))
					changeButton = 5;

				if(list.getPacketVariable(c) instanceof DataTypeArray)
				{
					DataTypeArray array = (DataTypeArray)list.getPacketVariable(c);
					GlStateManager.pushMatrix();
					if(array.value.length < 2||!(array.value[0] instanceof DataTypeInteger)||!(array.value[1] instanceof DataTypeInteger))
						array.value = new DataTypeInteger[]{new DataTypeInteger(0), new DataTypeInteger(0)};

					float[] color = EnumDyeColor.byMetadata(((DataTypeInteger)array.value[0]).value).getColorComponentValues();
					GlStateManager.color(color[0], color[1], color[2]);
					//this.drawTexturedModalRect(drawx+3, drawy+3, 155, 222, 16, 14);

					this.drawTexturedModalRect(drawx+33, drawy+3, 155, 222, 16, 14);
					this.fontRenderer.drawString(I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock.redstone_interface.modes."+((DataTypeInteger)array.value[1]).value), drawx+64, drawy+6, data.getTypeColor().getPackedRGB(), true);

					GlStateManager.popMatrix();

					DataTypeInteger i1 = (DataTypeInteger)array.value[0];
					DataTypeInteger i2 = (DataTypeInteger)array.value[1];

					if(Mouse.isButtonDown(0)&&!wasDown)
						switch(changeButton)
						{
							case 0:
								break;
							case 1:
								i1.value = IIUtils.cycleInt(true, i1.value, 0, 15);
								break;
							case 2:
								i1.value = IIUtils.cycleInt(false, i1.value, 0, 15);
								break;
							case 3:
							{
								char d = IIUtils.cycleDataPacketChars(c, true, false);
								for(int j = 0; j < DataPacket.varCharacters.length; j += 1)
								{
									if(!list.variables.containsKey(d))
									{
										list.setVariable(d, list.getPacketVariable(c));
										list.removeVariable(c);
										syncDataToServer();
										break;
									}
									d = IIUtils.cycleDataPacketChars(d, true, false);
								}
							}

							break;
							case 4:
							{
								char d = IIUtils.cycleDataPacketChars(c, false, false);
								for(int j = 0; j < DataPacket.varCharacters.length; j += 1)
								{
									if(!list.variables.containsKey(d))
									{
										list.setVariable(d, list.getPacketVariable(c));
										list.removeVariable(c);
										syncDataToServer();
										break;

									}
									d = IIUtils.cycleDataPacketChars(d, false, false);
								}
							}
							break;
							case 5:
							{
								i2.value = IIUtils.cycleInt(true, i2.value, 0, 5);
							}
							break;
						}

				}

				//'Set' Button
				/*this.drawTexturedModalRect(drawx+93, drawy+5, hovered?32: 0, 242, 32, 12);
				this.fontRenderer.drawString(I18n.format(CommonProxy.DESCRIPTION_KEY+(isShiftKeyDown()?"variable_remove": "variable_set")), drawx+96, drawy+7, hovered?Lib.COLOUR_I_ImmersiveOrange: 0xffffff, true);
				if(hovered)
				{
					if(!isShiftKeyDown())
					{
						onVariableEditButtonClick(c, true);
						tooltip.add(I18n.format(CommonProxy.DESCRIPTION_KEY+(isShiftKeyDown()?"variable_remove_desc": "variable_set_desc")));
					}
					else
					{
						onVariableRemoveButtonClick(c, true);
					}
				}*/

				GL11.glPopMatrix();

				//Draw variable name (single character)
				this.fontRenderer.drawString(String.valueOf(c), drawx+8, drawy+5, Lib.COLOUR_I_ImmersiveOrange, true);

				i += 1;
			}
		GL11.glPushMatrix();

		//Draw 'add' button
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture);

		this.drawTexturedModalRect(guiLeft+87, guiTop+12+(i*24)-scroll, 137, 222, 18, 18);
		GL11.glPopMatrix();
		boolean hovered = isPointInRegion(87, 12+(i*24)-scroll, 18, 18, mx, my);

		this.fontRenderer.drawString("+", guiLeft+93, guiTop+17+(i*24)-scroll, hovered?Lib.COLOUR_I_ImmersiveOrange: 0xffffff, true);

		if(hovered)
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"variable_add_desc"));

		//Check for button click
		if(hovered&&!wasDown&&Mouse.isButtonDown(0))
		{
			boolean done = false;
			for(char c : DataPacket.varCharacters)
				if(!list.variables.containsKey(c))
				{
					//Save gui scroll, tile pos for validation
					ClientProxy proxy = (ClientProxy)ImmersiveIntelligence.proxy;
					saveGuiData(proxy);
					list.setVariable(c, new DataTypeArray(new DataTypeInteger(0), new DataTypeInteger(0)));
					//Set variable and change gui
					refreshStoredData();
					syncDataToServer();

					break;
				}

		}

		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		GlStateManager.popMatrix();

		wasDown = Mouse.isButtonDown(0);

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
		IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(false, 0, tile.getPos()));
		IIPacketHandler.sendToServer(new MessageBooleanAnimatedPartsSync(false, 1, tile.getPos()));
		super.onGuiClosed();
	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(texture);

		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		int mouseChange = Mouse.getDWheel();
		scroll -= Integer.signum(mouseChange)*40;

		if((isPointInRegion(161, 12, 9, 114, mx, my))&&Mouse.isButtonDown(0))
			scroll = (int)((my-(15./2)-guiTop-10.)*maxScroll)/114;

		scroll = Math.min(scroll, maxScroll);
		scroll = Math.max(0, scroll);

		this.drawTexturedModalRect(guiLeft+161, guiTop+12+(int)(100*(scroll*1./maxScroll)), 128, 222, 9, 14);

	}

	//Used to refresh gui variables after one of the variables is changed
	void refreshStoredData()
	{
		maxScroll = (Math.max(list.variables.size()-4, 0))*24;
		wasDown = false;

		tile = (TileEntityRedstoneInterface)tile.getWorld().getTileEntity(tile.getPos());
		this.list = tile.storedData;
	}

	void syncDataToServer()
	{
		if(tile==null)
			return;
		IIPacketHandler.sendToServer(new MessageIITileSync(tile,
				EasyNBT.newNBT().withTag("storedData", this.list.toNBT())
		));
	}

	//Stolen from Flaxbeard (https://github.com/Flaxbeard/QuestionablyImmersive/blob/dev/src/main/java/flaxbeard/questionablyimmersive/client/gui/GuiCokeOvenBattery.java)
	private void scissor(int x, int y, int xSize, int ySize)
	{
		ScaledResolution res = new ScaledResolution(mc);
		x = x*res.getScaleFactor();
		ySize = ySize*res.getScaleFactor();
		y = mc.displayHeight-(y*res.getScaleFactor())-ySize;
		xSize = xSize*res.getScaleFactor();
		GL11.glScissor(x, y, xSize, ySize);
	}


	void onVariableRemoveButtonClick(char variable, boolean hovered)
	{
		//Check for button click
		if(hovered&&!wasDown&&Mouse.isButtonDown(0)&&list.variables.containsKey(variable))
		{
			list.removeVariable(variable);
			syncDataToServer();
			maxScroll = (Math.max(list.variables.size()-4, 0))*24;
			wasDown = false;
		}
	}

	void saveGuiData(ClientProxy proxy)
	{
		//I am absolutely 100% sure its client. How would a server even come here after all this rendering and proxy doohickeys?!
		//But this is actually forge, so i should be kind of cautious... everything can happen ^^.

		proxy.storedGuiData = new NBTTagCompound();
		saveBasicData(proxy, tile);
		if(maxScroll!=0)
			proxy.storedGuiData.setFloat("scrollPercent", scroll/maxScroll);
	}
}
