package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoTextBasedComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoColors;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard.DecoClipboardUtils;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Displays and selects one angle inside a clamped range.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.09.2026
 */
public class DecoGauge extends DecoTextBasedComponent<DecoGauge>
{
	@Getter
	private float angle;
	private float minAngle = -180f;
	private float maxAngle = 180f;
	private boolean displayCross = true, displayValues = true;
	private IIColor crossColor = DecoColors.H2;
	private IIColor angleColor = IIReference.COLOR_IMMERSIVE_ORANGE;
	@Nullable
	private Supplier<Float> valueListener;
	@Nullable
	private Consumer<Float> onValueChanged;

	/**
	 * Creates an angle gauge at the specified position.
	 */
	public DecoGauge(int x, int y)
	{
		super(x, y);
		withSize(64, 76);
		withOnPressed((gauge, button, mouseX, mouseY) -> {
			if(button!=MouseButton.LEFT)
				return false;
			gauge.handleMouse(mouseX, mouseY);
			gauge.setFocused(true);
			return true;
		});
		withOnDragged((gauge, button, mouseX, mouseY) -> {
			if(button!=MouseButton.LEFT)
				return false;
			gauge.handleMouse(mouseX, mouseY);
			return true;
		});
		withOnReleased((gauge, button, mouseX, mouseY) -> {
			gauge.handleMouse(mouseX, mouseY);
			gauge.setFocused(false);
			return true;
		});
	}

	private void handleMouse(int mouseX, int mouseY)
	{
		float centerX = x+width/2f;
		float centerY = y+getGaugeHeight()/2f;
		float dx = mouseX-centerX;
		float dy = centerY-mouseY;
		if(dx==0f&&dy==0f)
			return;
		setAngle((float)Math.toDegrees(Math.atan2(dy, dx)));
		if(onValueChanged!=null)
			onValueChanged.accept(angle);
	}

	/**
	 * Sets the displayed angle.
	 */
	public DecoGauge withAngle(float angle)
	{
		setAngle(angle);
		return this;
	}

	/**
	 * Sets the permitted angle range.
	 */
	public DecoGauge withRange(float minAngle, float maxAngle)
	{
		if(Float.isNaN(minAngle)||Float.isInfinite(minAngle)||Float.isNaN(maxAngle)||Float.isInfinite(maxAngle))
			return this;
		this.minAngle = Math.min(minAngle, maxAngle);
		this.maxAngle = Math.max(minAngle, maxAngle);
		setAngle(angle);
		return this;
	}

	/**
	 * Enables or disables the cardinal cross.
	 */
	public DecoGauge withDisplayCross(boolean displayCross)
	{
		this.displayCross = displayCross;
		return this;
	}

	/**
	 * Enables or disables cardinal angle labels.
	 */
	public DecoGauge withDisplayValues(boolean displayValues)
	{
		this.displayValues = displayValues;
		return this;
	}

	/**
	 * Sets a source that refreshes the displayed angle.
	 */
	public DecoGauge withValueListener(@Nullable Supplier<Float> valueListener)
	{
		this.valueListener = valueListener;
		return this;
	}

	/**
	 * Sets the callback invoked after user input.
	 */
	public DecoGauge withOnValueChanged(@Nullable Consumer<Float> onValueChanged)
	{
		this.onValueChanged = onValueChanged;
		return this;
	}

	/**
	 * Sets the cross and selected-angle colours.
	 */
	public DecoGauge withLineColors(IIColor crossColor, IIColor angleColor)
	{
		this.crossColor = crossColor==null?DecoColors.H2: crossColor;
		this.angleColor = angleColor==null?IIReference.COLOR_IMMERSIVE_ORANGE: angleColor;
		return this;
	}

	private void setAngle(float angle)
	{
		if(Float.isNaN(angle)||Float.isInfinite(angle))
			return;
		this.angle = MathHelper.clamp(angle, minAngle, maxAngle);
	}

	@Override
	protected boolean initialize()
	{
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		if(valueListener!=null)
		{
			Float listenedValue = valueListener.get();
			if(listenedValue!=null)
				setAngle(listenedValue);
		}

		int gaugeHeight = getGaugeHeight();
		float centerX = x+width/2f;
		float centerY = y+gaugeHeight/2f;
		float radius = Math.max(1f, Math.min(width, gaugeHeight)/2f-(displayValues?9f: 2f));

		GlStateManager.disableTexture2D();
		if(displayCross)
			drawCross(centerX, centerY, radius);
		GlStateManager.glLineWidth(2f);
		double radians = Math.toRadians(angle);
		IIDrawUtils.startColoredLines()
				.drawColorLine(centerX, centerY,
						centerX+(float)Math.cos(radians)*radius,
						centerY-(float)Math.sin(radians)*radius, angleColor)
				.finish();
		GlStateManager.glLineWidth(1f);
		GlStateManager.enableTexture2D();

		if(displayValues)
			drawValues(centerX, centerY, gaugeHeight);
		if(!text.isEmpty())
			drawCentered(text+": "+formatAngle(angle)+"°", x+width/2, y+height-fontRenderer.FONT_HEIGHT);
	}

	private void drawCross(float centerX, float centerY, float radius)
	{
		IIDrawUtils draw = IIDrawUtils.startColoredLines();
		if(contains(0f))
			draw.drawColorLine(centerX, centerY, centerX+radius, centerY, crossColor);
		if(contains(90f))
			draw.drawColorLine(centerX, centerY, centerX, centerY-radius, crossColor);
		if(contains(-90f))
			draw.drawColorLine(centerX, centerY, centerX, centerY+radius, crossColor);
		if(contains(180f)||contains(-180f))
			draw.drawColorLine(centerX, centerY, centerX-radius, centerY, crossColor);
		draw.finish();
	}

	private void drawValues(float centerX, float centerY, int gaugeHeight)
	{
		if(contains(0f))
			drawRight("0", x+width, Math.round(centerY)-fontRenderer.FONT_HEIGHT/2);
		if(contains(90f))
			drawCentered("90", Math.round(centerX), y);
		if(contains(-90f))
			drawCentered("-90", Math.round(centerX), y+gaugeHeight-fontRenderer.FONT_HEIGHT);
		if(contains(180f)||contains(-180f))
			fontRenderer.drawString("180", x, Math.round(centerY)-fontRenderer.FONT_HEIGHT/2,
					getTextColor(false).getPackedARGB());
	}

	private void drawCentered(String value, int centerX, int drawY)
	{
		fontRenderer.drawString(value, centerX-fontRenderer.getStringWidth(value)/2, drawY,
				getTextColor(false).getPackedARGB());
	}

	private void drawRight(String value, int rightX, int drawY)
	{
		fontRenderer.drawString(value, rightX-fontRenderer.getStringWidth(value), drawY,
				getTextColor(false).getPackedARGB());
	}

	private boolean contains(float value)
	{
		return value >= minAngle&&value <= maxAngle;
	}

	private int getGaugeHeight()
	{
		return Math.max(1, height-(text.isEmpty()?0: fontRenderer.FONT_HEIGHT+2));
	}

	private String formatAngle(float value)
	{
		int rounded = Math.round(value);
		return Math.abs(value-rounded) < 0.05f?String.valueOf(rounded): String.format(java.util.Locale.ROOT, "%.1f", value);
	}

	@Override
	public void onGuiEvent(DecoGuiEvent event)
	{
		switch(event)
		{
			case COPY:
				DecoClipboardUtils.copy(angle);
				break;
			case PASTE:
				Object pasted = DecoClipboardUtils.paste();
				if(pasted instanceof Number)
				{
					float oldAngle = angle;
					setAngle(((Number)pasted).floatValue());
					if(onValueChanged!=null&&oldAngle!=angle)
						onValueChanged.accept(angle);
				}
				break;
			default:
				super.onGuiEvent(event);
		}
	}

	@Override
	protected boolean canBeClicked(int mouseX, int mouseY)
	{
		return mouseX >= x&&mouseX <= x+width&&mouseY >= y&&mouseY <= y+getGaugeHeight();
	}

	@Override
	public void cleanup()
	{

	}
}
