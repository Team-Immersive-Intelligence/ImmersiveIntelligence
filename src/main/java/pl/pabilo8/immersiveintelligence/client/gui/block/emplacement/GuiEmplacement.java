package pl.pabilo8.immersiveintelligence.client.gui.block.emplacement;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.gui.ITabbedGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSlider;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerEmplacement;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerEmplacement.ContainerEmplacementStorage;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageGuiNBT;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.07.2021
 */
public abstract class GuiEmplacement extends GuiIEContainerBase implements ITabbedGui
{
	protected final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/emplacement.png");
	public final ResourceLocation TEXTURE_ICONS = new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/emplacement_icons.png");
	protected final TileEntityEmplacement tile;
	protected String title = I18n.format("tile."+ImmersiveIntelligence.MODID+".metal_multiblock1.emplacement.name");
	private final LinkedHashMap<DecoTab, IIGUI> TABS = new LinkedHashMap<>();
	private final IIGUI thisGui;
	private DecoBar barEnergy, barArmor;

	protected final static IIColor COLOR_IN = IIColor.fromPackedRGB(0x4c7bb1), COLOR_OUT = IIColor.fromPackedRGB(0xffb515);

	public GuiEmplacement(EntityPlayer player, TileEntityEmplacement tile, IIGUI gui)
	{
		super(gui==IIGUI.EMPLACEMENT_STORAGE?new ContainerEmplacementStorage(player, tile): new ContainerEmplacement(player, tile));
		this.tile = tile;
		thisGui = gui;
		this.xSize = 240;
		this.ySize = 238;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		buttonList.clear();
		labelList.clear();
		TABS.clear();

		addTab(IIGUI.EMPLACEMENT_STORAGE, "storage_module");
		addTab(IIGUI.EMPLACEMENT_TASKS, "tasks_module");
		addTab(IIGUI.EMPLACEMENT_STATUS, "status_module");

		addLabel(8, 10, IIReference.COLOR_H1, title);

//		barEnergy = DecoBar.createEnergyBar(guiLeft+213, guiTop+22, 7, 48);
//		barArmor = DecoBar.createArmorBar(guiLeft+222, guiTop+22, 7, 48);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		ArrayList<String> tooltip = new ArrayList<>();
		for(Entry<DecoTab, IIGUI> entry : TABS.entrySet())
		{
			if(entry.getKey().isMouseOver())
			{
				//TODO: 12.01.2025 tooltips
//				tooltip.add(entry.getKey().displayString);
				break;
			}
		}
		/*if(barEnergy.mouseOver(mouseX, mouseY))
			tooltip.add(IIUtils.getPowerLevelString(tile));
		if(tile.currentWeapon!=null&&barArmor.mouseOver(mouseX, mouseY))
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"tooltip.armor",
					(getHealth())*100
			));*/

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, -guiLeft+mouseX, -guiTop+mouseY, fontRenderer, -1, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}

	private float getHealth()
	{
		return tile.currentWeapon.getHealth()/(float)tile.currentWeapon.getMaxHealth();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		if(button instanceof DecoTab)
		{
			syncDataToServer();
			IIPacketHandler.sendToServer(new MessageGuiNBT(TABS.get(button), tile));
		}
	}

	/**
	 * Used for syncing data with server, called before changing guis (tabs)
	 */
	protected void syncDataToServer()
	{

	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bindTexture();
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		/*barEnergy.draw(tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null));
		if(tile.currentWeapon!=null)
			barArmor.draw(getHealth());*/
	}

	protected void bindTexture()
	{
		mc.getTextureManager().bindTexture(TEXTURE);
	}

	public void bindIcons()
	{
		mc.getTextureManager().bindTexture(TEXTURE_ICONS);
	}

	protected void addTab(IIGUI gui, String name)
	{
		final int vOffset = TABS.size()*24;
		DecoTab button = new DecoTab(buttonList.size(), guiLeft-28, guiTop+4+vOffset, 28, 24, thisGui==gui?28: 0, 101+vOffset,
				TEXTURE_ICONS, I18n.format(IIReference.DESCRIPTION_KEY+name));
		TABS.put(button, gui);
		addButton(button);
	}

	protected DecoSwitch addSwitch(int x, int y, int textWidth, IIColor textColor, IIColor color1, IIColor color2, boolean state, String name, boolean firstTime)
	{
		return addButton(new DecoSwitch(buttonList.size(), guiLeft+x, guiTop+y, textWidth, 8, 18, 9, 18, 52, state, TEXTURE_ICONS, textColor, color1, color2, name, firstTime));
	}

	protected DecoSwitch addSwitch(int x, int y, int textWidth, IIColor textColor, IIColor color1, IIColor color2, boolean state, String name)
	{
		return addSwitch(x, y, textWidth, textColor, color1, color2, state, name, false);
	}

	protected DecoSlider addSlider(int x, int y, int width, IIColor textColor, float value, String name)
	{
		return addButton(new DecoSlider(buttonList.size(), guiLeft+x, guiTop+y, width, name, value, textColor));
	}

	protected DecoLabel addLabel(int x, int y, IIColor textColor, String... text)
	{
		return addLabel(x, y, 0, 0, textColor, text);
	}


	protected DecoLabel addLabel(int x, int y, int w, int h, IIColor textColor, String... text)
	{
		DecoLabel guiLabel = new DecoLabel(fontRenderer, guiLeft+x, guiTop+y)
				.withSize(w, h)
				.withTextColor(textColor)
				.withText(text);
		labelList.add(guiLabel);
		return guiLabel;
	}
}
