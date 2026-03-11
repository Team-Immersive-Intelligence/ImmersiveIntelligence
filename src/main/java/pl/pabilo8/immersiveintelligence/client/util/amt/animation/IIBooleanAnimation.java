package pl.pabilo8.immersiveintelligence.client.util.amt.animation;

import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;

/**
 * A wrapper for simple on/off animation<br>
 * Used for switching between active and inactive part
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.08.2022
 */
public class IIBooleanAnimation
{
	private final AMT active, inactive;

	public IIBooleanAnimation(AMT active, AMT inactive)
	{
		this.active = active;
		this.inactive = inactive;
	}

	public void applyVisibility(boolean visible)
	{
		if(!visible)
		{
			active.setVisible(false);
			inactive.setVisible(false);
		}
	}

	public void apply(boolean active)
	{
		this.active.setVisible(active);
		this.inactive.setVisible(!active);
	}
}
