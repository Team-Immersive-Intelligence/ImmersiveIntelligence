package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text;

import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoArrows;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A single-line Deco text input.
 * Numeric filters right-align their values and may expose increment/decrement arrows.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.07.2025
 */
public class DecoTextField extends DecoTextInputBase<DecoTextField>
{
	private DecoArrows arrows;

	public DecoTextField(int x, int y)
	{
		super(x, y);
	}

	/**
	 * Adds a configurable arrow control to the right side of this field, only visible for numeric inputs.
	 *
	 * @param configure action used to configure the created arrows, usually through
	 *                  {@link DecoArrows#withOnArrow(Consumer)}
	 */
	public DecoTextField withArrows(Consumer<DecoArrows> configure)
	{
		Objects.requireNonNull(configure, "configure");
		if(arrows!=null)
		{
			children.remove(arrows);
			arrows.cleanup();
		}

		arrows = new DecoArrows(0, 0);
		configure.accept(arrows);
		updateArrows();
		ensureCursorVisible();
		return this;
	}

	@Override
	protected int getTrailingDecorationWidth()
	{
		return arrows!=null&&children.contains(arrows)?arrows.width: 0;
	}

	@Override
	protected void onBoundsChanged()
	{
		updateArrowsPosition();
	}

	@Override
	protected void onFilterChanged()
	{
		updateArrows();
		ensureCursorVisible();
	}

	@Override
	public DecoTextField withDisabled(boolean disabled)
	{
		super.withDisabled(disabled);
		updateArrows();
		return this;
	}

	private void updateArrows()
	{
		if(arrows==null)
			return;
		boolean active = getFilter().isNumeric();
		if(active&&!children.contains(arrows))
			children.add(arrows);
		else if(!active)
			children.remove(arrows);
		arrows.visible = active;
		arrows.enabled = active&&enabled;
		updateArrowsPosition();
	}

	private void updateArrowsPosition()
	{
		if(arrows==null)
			return;
		arrows.x = x+width-arrows.width;
		arrows.y = y+Math.max(0, (height-arrows.height)/2);
	}
}
