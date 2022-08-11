package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityDataMerger;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerDataMerger;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 30-06-2019
 */
public class GuiDataMerger extends GuiIEContainerBase implements ITabbedGui
{
	public static final String texture = ImmersiveIntelligence.MODID+":textures/gui/data_merger.png";

	public TileEntityDataMerger tile;
	public InventoryPlayer playerInv;
	public DataPacket packet;

	public GuiDataMerger(InventoryPlayer inventoryPlayer, TileEntityDataMerger tile)
	{
		super(new ContainerDataMerger(inventoryPlayer, tile));
		this.ySize = 222;
		this.playerInv = inventoryPlayer;
		this.tile = tile;
		this.packet = tile.packet;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.buttonList.clear();
		this.buttonList.add(new GuiButtonIE(0, guiLeft+4, guiTop+16, 8, 6, "", texture, 128, 222).setHoverOffset(8, 0));
		this.buttonList.add(new GuiButtonIE(1, guiLeft+4, guiTop+24, 8, 6, "", texture, 128, 228).setHoverOffset(8, 0));

	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		NBTTagCompound tag = new NBTTagCompound();
		if(button.id==0)
		{
			tile.mode += 1;
			if(tile.mode > 2)
				tile.mode = 0;
			tag.setShort("mode", tile.mode);
			ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(tile, tag));
		}
		else if(button.id==1)
		{
			tile.mode -= 1;
			if(tile.mode < 0)
				tile.mode = 2;
			tag.setShort("mode", tile.mode);
			ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(tile, tag));
		}
	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.fontRenderer.drawString(I18n.format(CommonProxy.DESCRIPTION_KEY+"data_merger."+tile.mode), guiLeft+14, guiTop+19, Utils.COLOR_H1);

		ArrayList<String> tooltip = new ArrayList<String>();

		int i = 0;
		for(char c : DataPacket.varCharacters)
		{
			int xoff = (int)Math.floor(i/6f), yoff = i%6;
			int col = 0xefefef;
			if(packet.getPacketVariable(c) instanceof DataTypeInteger)
			{
				int o = ((DataTypeInteger)packet.getPacketVariable(c)).value;
				switch(o)
				{
					case -2:
						col = 0x26732e;
						break;
					case -1:
						col = 0x7c9d4b;
						break;
					case 1:
						col = 0xe5a64d;
						break;
					case 2:
						col = 0x922020;
						break;
				}
			}
			this.fontRenderer.drawString(String.valueOf(c), guiLeft+54+(xoff*20)+6-(int)(fontRenderer.getCharWidth(c)/2f), guiTop+16+(yoff*20)+2, col);
			i += 1;
		}

	}

	@Override
	public void onGuiClosed()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("packet", packet.toNBT());
		ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(tile, tag));
		super.onGuiClosed();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(mouseX >= guiLeft+54&&mouseX <= guiLeft+166&&mouseY >= guiTop+16&&mouseY <= guiTop+128)
		{
			boolean fw;
			if(mouseButton==0&&!Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			{
				fw = true;
			}
			else if(mouseButton==1||Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			{
				fw = false;
			}
			else
				return;

			int i_pos_x = (int)Math.floor((mouseX-guiLeft-54)/20f);
			int i_pos_y = (int)Math.floor((mouseY-guiTop-16)/20f);

			if(!(mouseX <= guiLeft+(i_pos_x*20)+12+54&&mouseY <= guiTop+(i_pos_y*20)+12+16))
			{
			}
			else
				switchMode(DataPacket.varCharacters[i_pos_y+(i_pos_x*6)], fw);
		}
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
	}

	void switchMode(char c, boolean forward)
	{
		IDataType p = packet.getPacketVariable(c);
		if(p instanceof DataTypeInteger)
		{
			int i = ((DataTypeInteger)p).value;
			i += forward?1: -1;
			if(i > 2)
				i = -2;
			else if(i < -2)
				i = 2;
			packet.setVariable(c, new DataTypeInteger(i));
		}
		else
			packet.setVariable(c, new DataTypeInteger(forward?1: -1));
	}

}
