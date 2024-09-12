package pl.pabilo8.immersiveintelligence.client.gui.block;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.client.config.GuiSlider.ISlider;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiSliderII;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.gui.ContainerChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 10-07-2019
 */
public class GuiChemicalPainter extends GuiIEContainerBase implements ISlider
{
	public static final ResourceLocation TEXTURE = new ResourceLocation("immersiveintelligence:textures/gui/chemical_painter.png");
	public static final ResourceLocation TEXTURE_ICONS = new ResourceLocation("immersiveintelligence:textures/gui/emplacement_icons.png");
	TileEntityChemicalPainter tile;

	private GuiSliderII sliderRed, sliderGreen, sliderBlue;
	private GuiSliderII sliderCyan, sliderMagenta, sliderYellow, sliderBlack;
	private GuiSliderII sliderHue, sliderSaturation, sliderValue;

	private ColorMode colorMode = ColorMode.RGB;
	private IIColor color;

	GuiButtonIE buttonModeRGB, buttonModeCMYK, buttonModeHSV;

	GuiButtonIE buttonActiveColor;
	int colorDelay = 0;

	public GuiChemicalPainter(EntityPlayer player, TileEntityChemicalPainter tile)
	{
		super(new ContainerChemicalPainter(player, tile));
		this.ySize = 203;
		this.tile = tile;
		this.color = tile.color;
	}

	@Override
	public void initGui()
	{
		buttonList.clear();
		super.initGui();

		switch(colorMode)
		{
			case RGB:
			{
				float[] rgb = color.getFloatRGB();
				sliderRed = getSlider(0, "Red", rgb[0]);
				sliderGreen = getSlider(1, "Green", rgb[1]);
				sliderBlue = getSlider(2, "Blue", rgb[2]);
			}
			break;
			case CMYK:
			{
				float[] cmyk = color.getCMYK();
				sliderCyan = getSlider(0, "Cyan", cmyk[0]);
				sliderMagenta = getSlider(1, "Magenta", cmyk[1]);
				sliderYellow = getSlider(2, "Yellow", cmyk[2]);
				sliderBlack = getSlider(3, "Black", cmyk[3]);
			}
			break;
			case HSV:
			{
				float[] hsv = color.getHSV();
				sliderHue = getSlider(0, "Hue", hsv[0]);
				sliderSaturation = getSlider(1, "Saturation", hsv[1]);
				sliderValue = getSlider(2, "Value", hsv[2]);
			}
			break;
		}

		buttonActiveColor = addButton(new GuiButtonIE(buttonList.size(), guiLeft+4, guiTop+88, 18, 18, "", TEXTURE_ICONS.toString(), 163, 142));

		this.buttonModeRGB = addButton(new GuiButtonIE(1, guiLeft+176, guiTop+79, 12, 12, "R",
				TEXTURE_ICONS.toString(), 144, 89)
				.setHoverOffset(12, 0));
		this.buttonModeCMYK = addButton(new GuiButtonIE(1, guiLeft+176, guiTop+79+12, 12, 12, "C",
				TEXTURE_ICONS.toString(), 144, 89)
				.setHoverOffset(12, 0));
		this.buttonModeHSV = addButton(new GuiButtonIE(1, guiLeft+176, guiTop+79+24, 12, 12, "H",
				TEXTURE_ICONS.toString(), 144, 89)
				.setHoverOffset(12, 0));

		saveBasicData();
	}

	@Override
	protected void actionPerformed(@Nonnull GuiButton button) throws IOException
	{
		if(button==buttonModeRGB)
		{
			colorMode = ColorMode.RGB;
			initGui();
		}
		else if(button==buttonModeCMYK)
		{
			colorMode = ColorMode.CMYK;
			initGui();
		}
		else if(button==buttonModeHSV)
		{
			colorMode = ColorMode.HSV;
			initGui();
		}
		else if(button==buttonActiveColor)
		{
			colorDelay = 20;
			setClipboardString(tile.color.getHexRGB());
		}
		super.actionPerformed(button);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		IIClientUtils.drawStringCentered(this.fontRenderer, I18n.format("tile.immersiveintelligence.metal_multiblock1.chemical_painter.name"),
				8, 6, xSize-12, 0, IIReference.COLOR_H1.getPackedRGB());

		float[] rgb = color.getFloatRGB();
		GlStateManager.color(rgb[0], rgb[1], rgb[2]);
		IIClientUtils.bindTexture(TEXTURE_ICONS);
		drawTexturedModalRect(7, 91, 163, 160, 12, 12);
		GlStateManager.color(1.0F, 1.0F, 1.0F);

	}

	/**
	 * Draws the background layer of this container (behind the items).
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		IIClientUtils.bindTexture(TEXTURE);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		IIClientUtils.drawPowerBar(guiLeft+159, guiTop+23, 7, 47, tile.getEnergyStored(null)/(float)tile.getMaxEnergyStored(null));

		for(int i = 0; i < 4; i++)
			ClientUtils.handleGuiTank(tile.tanks[i], guiLeft+49+22*i, guiTop+23, 17, 47, 177, 0, 20, 51, mx, my, TEXTURE.toString(), null);

		if(tile.active)
			drawTexturedModalRect(guiLeft+20, guiTop+20, 176, 51, 24, Math.round(51*(tile.processTime/(float)tile.processTimeMax)));

	}

	@Override
	public void drawScreen(int mx, int my, float partial)
	{
		super.drawScreen(mx, my, partial);
		this.renderHoveredToolTip(mx, my);

		colorDelay = Math.max(--colorDelay, 0);

		ArrayList<String> tooltip = new ArrayList<>();

		if(mx > guiLeft+159&&mx < guiLeft+166&&my > guiTop+23&&my < guiTop+70)
			tooltip.add(IIUtils.getPowerLevelString(tile));
		else if(buttonModeRGB.isMouseOver())
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.chemical_painter.color.rgb"));
		else if(buttonModeCMYK.isMouseOver())
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.chemical_painter.color.cmyk"));
		else if(buttonModeHSV.isMouseOver())
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.chemical_painter.color.hsv"));
		else if(buttonActiveColor.isMouseOver())
			tooltip.add(
					colorDelay > 0?
							TextFormatting.GREEN+TextFormatting.ITALIC.toString()+I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.chemical_painter.color_copied")+TextFormatting.RESET:
							color.getHexCol(I18n.format("item.fireworksCharge."+color.getDyeColor().getName()))
			);
		else
		{
			for(int i = 0; i < 4; i++)
				ClientUtils.handleGuiTank(tile.tanks[i], guiLeft+49+22*i, guiTop+23, 17, 47, 177, 0, 20, 51, mx, my, TEXTURE.toString(), tooltip);
		}

		if(!tooltip.isEmpty())
		{
			ClientUtils.drawHoveringText(tooltip, mx, my, IIClientUtils.fontRegular, guiLeft+xSize, -1);
			RenderHelper.enableGUIStandardItemLighting();
		}
	}

	public GuiSliderII getSlider(int id, String name, float value)
	{
		GuiSliderII slider = addButton(new GuiSliderII(buttonList.size(), guiLeft+25+((id%2)*75), guiTop+80+8+(17*(int)Math.floor(id/2f)), 70, name, value, IIReference.COLOR_H1));
		slider.parent = this;
		return slider;
	}

	@Override
	public void onGuiClosed()
	{
		saveBasicData();
		super.onGuiClosed();
	}

	public void saveBasicData()
	{
		IIPacketHandler.sendToServer(new MessageIITileSync(tile, EasyNBT.newNBT().withInt("color", color.getPackedRGB())));
	}

	@Override
	public void onChangeSliderValue(GuiSlider slider)
	{
		switch(colorMode)
		{
			case RGB:
				color = IIColor.fromFloatRGB((float)sliderRed.sliderValue, (float)sliderGreen.sliderValue, (float)sliderBlue.sliderValue);
				break;
			case CMYK:
				color = IIColor.fromCMYK((float)sliderCyan.sliderValue, (float)sliderMagenta.sliderValue, (float)sliderYellow.sliderValue, (float)sliderBlack.sliderValue);
				break;
			case HSV:
				color = IIColor.fromHSV((float)sliderHue.sliderValue, (float)sliderSaturation.sliderValue, (float)sliderValue.sliderValue);
				break;
		}
	}

	enum ColorMode
	{
		RGB,
		CMYK,
		HSV
	}
}
