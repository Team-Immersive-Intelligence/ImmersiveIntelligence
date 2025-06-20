package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoTextBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * A standard checkbox of the Deco GUI system.<br>
 * Similar to {@link DecoSwitch}, but used for selecting options in a list instead of an on/off toggle.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 12.01.2025
 * @ii-approved 0.3.1
 * @since 18.07.2021
 */
public class DecoCheckbox extends GuiComponentDecoTextBase<DecoCheckbox>
{
	private static int BOX_SIZE = 9;
	private boolean checked = false;

	public DecoCheckbox(int x, int y)
	{
		super(x, y);
		backgroundLocation = IIReference.RES_TEXTURES_DECO_COMPONENT_CHECKBOX;
		withSize(120, 11);
		withOnPressed((gui, mouseX, mouseY) -> {
			checked = !checked;
			return true;
		});
	}

	/**
	 * @deprecated Use {@link #DecoCheckbox(int, int)} instead
	 */
	@Deprecated
	public DecoCheckbox(int buttonId, int x, int y, String name, boolean checked)
	{
		this(x, y);
//
//		super(buttonId, x, y, 9, 9, name, state, "immersiveintelligence:textures/gui/emplacement_icons.png", 44, 52, -1);
	}

	public DecoCheckbox withChecked(boolean checked)
	{
		this.checked = checked;
		return this;
	}

	public boolean isChecked()
	{
		return checked;
	}

	@Override
	protected boolean initialize()
	{
		return true;
	}


	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		//Create alignment for the checkbox box
		int alignX = DecoAlignment.LEFT.getAlignX(x, BOX_SIZE, width);
		int alignY = DecoAlignment.LEFT.getAlignY(y, BOX_SIZE, height);

		//Draw checkbox
		TextureAtlasSprite sprite = ClientUtils.getSprite(backgroundLocation);
		bindAtlas();
		IIDrawUtils draw = IIDrawUtils.startTexturedColored()
				.drawTexColorRect(alignX, alignY, BOX_SIZE, BOX_SIZE,
						getBackgroundColor(),
						sprite.getMinU(), sprite.getInterpolatedU(BOX_SIZE),
						sprite.getMinV(), sprite.getInterpolatedV(BOX_SIZE)
				);

		//Draw check symbol if checked
		if(checked)
			draw.drawTexColorRect(alignX+1, alignY-2, BOX_SIZE, BOX_SIZE,
					IIColor.WHITE,
					sprite.getInterpolatedU(BOX_SIZE), sprite.getMaxU(),
					sprite.getMinV(), sprite.getInterpolatedV(BOX_SIZE+1)
			);
		draw.finish();

		//Draw text
		if(!text.isEmpty())
			fontRenderer.drawString(text, alignX+BOX_SIZE+2, alignY+1, getTextColor(true).getPackedARGB());
	}

	@Override
	protected boolean canBeClicked(int mouseX, int mouseY)
	{
		return IIMath.isPointInRectangle(x, y, x+BOX_SIZE, y+BOX_SIZE, mouseX, mouseY);
	}

	@Override
	public void cleanup()
	{

	}
}
