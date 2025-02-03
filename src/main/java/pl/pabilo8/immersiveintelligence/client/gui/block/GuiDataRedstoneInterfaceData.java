package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoTemplate;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoBackgroundBuilder.SlotStyle;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerRedstoneDataInterface;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * @author Pabilo8
 * @updated 02.02.2024
 * @since 09-02-2020
 */
@DecoTemplate(name = "data_redstone_interface", style = "steel")
public class GuiDataRedstoneInterfaceData extends DecoGui<TileEntityRedstoneInterface, ContainerRedstoneDataInterface>
{
	@SyncNBT
	public int scroll;
	public int maxScroll;
	DataPacket list;

	public GuiDataRedstoneInterfaceData(EntityPlayer player, TileEntityRedstoneInterface tile)
	{
		super(player, tile, IIGuiList.GUI_DATA_REDSTONE_INTERFACE_DATA);
		this.list = tile.storedData;
	}

	@Override
	public void onInit()
	{
		//Create background
		startBackground()
				.withBox(IIReference.GUI_BG_STEEL, 0, 0, 176, 128+8)
				.withTitleBar("desc.immersiveintelligence.data_to_redstone_module")
				.withBox(IIReference.GUI_BG_WOODEN, 0, 128+8, 176, 92)
				.withTitleBar("Inventory", DecoAlignment.TOP_LEFT)

				.withNextLayer()
				.withBox(IIReference.GUI_BG_STEEL, IIReference.RES_TEXTURES_DECO_SQUARE, 0, 8, 32, 120)

				.withInventorySlots(SlotStyle.VANILLA, container.playerInventory)
				.withInventorySlots(SlotStyle.IE_INPUT, container.dataInput)
				.withInventorySlots(SlotStyle.IE_OUTPUT, container.dataOutput)
				.build();

		//Add components
		addComponents(
				new DecoButton(40, 20)
						.withSize(100, 20)
						.withText("Test Button")
						.withIcon(IIContent.itemDataWireCoil.getStack(1)),
				new DecoTab()
						.withLink(IIGuiList.GUI_DATA_REDSTONE_INTERFACE_DATA)
						.withIcon(IIContent.itemDataWireCoil.getStack(1))
						.withTranslatedTooltip("desc.immersiveintelligence.data_to_redstone_module"),
				new DecoTab()
						.withLink(IIGuiList.GUI_DATA_REDSTONE_INTERFACE_REDSTONE)
						.withIcon(new ItemStack(IEContent.itemWireCoil, 1, 5))
						.withTranslatedTooltip("desc.immersiveintelligence.redstone_to_data_module"),
				new DecoCheckbox(40, 40)
						.withText("Test Checkbox")
						.withChecked(true)
						.withTranslatedTooltip("1"),
				new DecoSwitch(40, 60)
						.withText("Test Switch")
						.withCurrentState(true)
						.withTranslatedTooltip("2"),
				new DecoDropdown<String>(40, 80)
						.withEntries("Test1", "Test2", "Test3", "Test4", "Test5", "Test6", "Test7", "Test8", "Test9", "Test10", "Test11", "Test12", "Test13")
						.withSelectedEntry(0)
						.withTranslatedTooltip("3")
						.withEntriesInGrid(3),

				new DecoButton(40, 100)
						.withSize(100, 20)
						.withText("Test Button 2")

				//TODO: 12.01.2025 List Component
				//TODO: 12.01.2025 Punchtape progress bar

		);
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);

	/*	ArrayList<String> tooltip = new ArrayList<>();

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
		}*/
	}

	void syncDataToServer()
	{
		if(tile==null)
			return;
		IIPacketHandler.sendToServer(new MessageIITileSync(tile,
				EasyNBT.newNBT().withTag("storedData", this.list.toNBT())
		));
	}
}
