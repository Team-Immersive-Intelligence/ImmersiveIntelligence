package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoTextBasedComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.function.Consumer;

/**
 * A standard switch of the Deco GUI system.<br>
 * Similar to {@link DecoCheckbox}, but used for on/off toggles instead of selecting options in a list.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 12.01.2025
 * @ii-approved 0.3.1
 * @since 18.07.2021
 */
public class DecoSwitch extends DecoTextBasedComponent<DecoSwitch>
{
	private static final int X_SIZE = 16, SWITCH_X_SIZE = 8, Y_SIZE = 9;
	private static final int MAX_SWITCH_TICKS = 20;
	private ResLoc movingPartLocation = DecoTextures.RES_TEXTURES_DECO_COMPONENT_SWITCH_MOVING;
	private IIColor colorOff = IIReference.COLOR_SWITCH_OFF;
	private IIColor colorRight = IIReference.COLOR_SWITCH_ON;
	private Consumer<Boolean> onToggle;

	private boolean state;
	private int timer = 0;

	public DecoSwitch(int x, int y)
	{
		super(x, y);
		backgroundLocation = DecoTextures.RES_TEXTURES_DECO_COMPONENT_SWITCH;
		withSize(120, 11);
		withOnPressed((gui, mouseButton, mx, my) -> {
			if(mouseButton==MouseButton.LEFT)
			{
				timer = (state = !state)?0: MAX_SWITCH_TICKS;
				if(onToggle!=null)
					onToggle.accept(state);
				return true;
			}
			return false;
		});
	}

	@Deprecated
	public DecoSwitch(int buttonId, int x, int y, int textWidth, int sliderWidth, int backWidth, int h, int u, int v, boolean state, ResourceLocation texture, IIColor textColor, IIColor colorOff, IIColor colorRight, String name, boolean firstTime)
	{
		this(x, y);
		if(firstTime)
			withCurrentState(state);
	}

	public DecoSwitch withText(String text)
	{
		this.text = text;
		this.setWidth(X_SIZE+2+fontRenderer.getStringWidth(text));
		return this;
	}

	public DecoSwitch withCurrentState(boolean state)
	{
		timer = (this.state = state)?MAX_SWITCH_TICKS: 0;
		return this;
	}

	public DecoSwitch withMovingPartLocation(ResLoc movingPartLocation)
	{
		this.movingPartLocation = movingPartLocation;
		return this;
	}

	public DecoSwitch withColorOff(IIColor colorOff)
	{
		this.colorOff = colorOff;
		return this;
	}

	public DecoSwitch withColorRight(IIColor colorRight)
	{
		this.colorRight = colorRight;
		return this;
	}

	public DecoSwitch withOnToggle(Consumer<Boolean> onToggle)
	{
		this.onToggle = onToggle;
		return this;
	}

	public boolean getState()
	{
		return state;
	}

	@Override
	protected boolean initialize()
	{
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		//Progress the switch animation
		timer = MathHelper.clamp(timer+(state?1: -1), 0, MAX_SWITCH_TICKS);

		//Create alignment for the checkbox box
		int alignX = DecoAlignment.LEFT.getAlignX(x, X_SIZE, width);
		int alignY = DecoAlignment.LEFT.getAlignY(y, Y_SIZE, height);

		//Prepare textures
		TextureAtlasSprite spriteSwitch = ClientUtils.getSprite(backgroundLocation);
		TextureAtlasSprite spriteMovingPart = ClientUtils.getSprite(movingPartLocation);
		bindAtlas();

		//Draw the switch box
		IIDrawUtils draw = IIDrawUtils.startTexturedColored()
				.drawTexColorRect(alignX, alignY, X_SIZE, Y_SIZE,
						getBackgroundColor(),
						spriteSwitch.getMinU(), spriteSwitch.getInterpolatedU(X_SIZE),
						spriteSwitch.getMinV(), spriteSwitch.getInterpolatedV(Y_SIZE)
				);

		//Draw the moving part of the switch
		int movingPartX = alignX+(int)(timer*(X_SIZE-SWITCH_X_SIZE)/(float)MAX_SWITCH_TICKS);
		draw.drawTexColorRect(movingPartX, alignY, SWITCH_X_SIZE, Y_SIZE,
				state?colorRight: colorOff,
				spriteMovingPart.getMinU(), spriteMovingPart.getInterpolatedU(SWITCH_X_SIZE),
				spriteMovingPart.getMinV(), spriteMovingPart.getInterpolatedV(Y_SIZE)
		).finish();

		//Draw text
		if(!text.isEmpty())
			fontRenderer.drawString(text, alignX+X_SIZE+2, alignY+1, getTextColor(true).getPackedARGB());

	}

	@Override
	protected boolean canBeClicked(int mouseX, int mouseY)
	{
		return IIMath.isPointInRectangle(x, y, x+X_SIZE, y+Y_SIZE, mouseX, mouseY);
	}


	@Override
	public void cleanup()
	{

	}
}
