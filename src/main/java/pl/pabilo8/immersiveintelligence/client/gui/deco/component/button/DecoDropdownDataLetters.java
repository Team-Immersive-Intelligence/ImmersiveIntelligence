package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays.DecoElementDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.09.2021
 */
public class DecoDropdownDataLetters extends DecoDropdown<Character> implements DecoElementDisplay<Character>
{
	private static final int ENTRY_SIZE = 11;
	private DataPacket constraints = new DataPacket();

	public DecoDropdownDataLetters(int x, int y)
	{
		super(x, y);
		withSize(18, 18);
		withBackground(DecoTextures.RES_TEXTURES_DECO_COMPONENT_TEXT_FIELD);
		withEntriesInGrid(6);
		withEntries(new String(DataPacket.VARIABLE_NAMES)
				.chars()
				.mapToObj(i -> (Character)(char)i)
				.toArray(Character[]::new)
		);

		withDisplayFunction(this);
		withOnKeyTyped((gui, typedChar, keyCode) -> {
			if(isDropped()&&isSelectable(typedChar))
				withSelectedEntry((Character)typedChar);
			return true;
		});
		calculateSlideLength();
	}

	@Override
	protected int calculateSlideLength()
	{
		this.maxDropHeight = 6*ENTRY_SIZE;
		this.dropdownWidth = 6*ENTRY_SIZE;
		this.entryMaxWidth = ENTRY_SIZE;
		this.maxScroll = 0;
		this.scrollStep = 1;
		this.scroll = 0;

		return maxDropHeight;
	}

	/**
	 * Sets the constraints for selectable variable names.
	 *
	 * @param constraints The DataPacket this dropdown is constrained to.
	 * @return this
	 */
	public DecoDropdownDataLetters withConstraints(DataPacket constraints)
	{
		this.constraints = constraints;
		return this;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		bindAtlas();

		IIDrawUtils draw = IIDrawUtils.startTexturedColored();
		IIColor buttonColor = enabled?(pressed?BACKGROUND_PRESSED: (hovered?BACKGROUND_HOVERED: BACKGROUND)): BACKGROUND_DISABLED;

		//Blinking search text cursor
		blinkTime = (blinkTime+1)%40;

		//Background
		DecoGuiUtils.drawRepeatedRect(draw, x, y, width, height, backgroundLocation, buttonColor, 32, 8);
		draw.finish();

		//Selected entry or search text
		GlStateManager.pushMatrix();
		GlStateManager.translate(x+2, y+2, 0);
		if(isFocused()&&!text.isEmpty())
		{
			String drawn = blinkTime > 20?(text+"_"): text;
			fontRenderer.drawString(drawn, 0, 0, getTextColor(false).getPackedRGB());
		}
		else
		{
			Character entry = getSelectedEntry();
			if(entry!=null)
				display.displayElement(entry, width, fontRenderer, false);
		}
		GlStateManager.popMatrix();
	}

	@Override
	public void drawUpperLayer(int mouseX, int mouseY, float partialTicks)
	{
		/*if(dropped)
			drawList(x, y+height, mouseX, mouseY, partialTicks);*/
		super.drawUpperLayer(mouseX, mouseY, partialTicks);
	}

	@Override
	public int displayElement(Character character, int width, IIFontRenderer font, int mouseX, int mouseY, float partialTicks, boolean heightProbe)
	{
		if(heightProbe)
			return width;
		IIColor color;
		int offsetX = 1, offsetY = 1;
		if(width!=ENTRY_SIZE)
		{
			color = IIReference.COLOR_IMMERSIVE_ORANGE;
			offsetX = (width-font.getStringWidth(String.valueOf(character)))/2-2;
			offsetY = (height-font.FONT_HEIGHT)/2-1;
		}
		else if(isSelectable(character))
		{
			if(IIMath.isPointInRectangle(0, 0, ENTRY_SIZE, ENTRY_SIZE, mouseX, mouseY))
				color = IIReference.COLOR_IMMERSIVE_ORANGE;
			else
				color = IIColor.WHITE;
		}
		else
			color = IIColor.MC_DARK_GRAY;

		font.drawString(String.valueOf(character), offsetX, offsetY, color.getPackedRGB());
		return width;
	}

	@Override
	public boolean isSelectable(Character character)
	{
		return DataPacket.isValidVariable(character)&&!constraints.has(character);
	}
}
