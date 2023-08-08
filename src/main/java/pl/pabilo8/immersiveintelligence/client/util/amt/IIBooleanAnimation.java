package pl.pabilo8.immersiveintelligence.client.util.amt;

/**
 * A wrapper for simple on/off animation<br>
 * Used for switching between active and inactive part
 *
 * @author Pabilo8
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
		this.active.visible &= visible;
		this.inactive.visible &= visible;
	}

	public void apply(boolean active)
	{
		this.active.visible = active;
		this.inactive.visible = !active;
	}
}
