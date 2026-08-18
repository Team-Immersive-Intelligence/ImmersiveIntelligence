package pl.pabilo8.immersiveintelligence.client.fx.utils;


import net.minecraftforge.common.config.Config.Comment;

/**
 * Represents particle effect details (intensity, presence of additional effects), based on vanilla minecraft settings
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.07.2026
 */
public enum ParticleDetail
{
	@Comment(value = "High particle detail and count")
	DETAILED,
	@Comment(value = "Reduced particle count")
	REDUCED,
	@Comment(value = "Minimal particle effects")
	MINIMAL,
	@Comment(value = "No particle effects")
	DISABLED;

	public boolean isHigh()
	{
		return this==DETAILED;
	}

	public boolean isMedium()
	{
		return this.ordinal() < MINIMAL.ordinal();
	}

	public boolean isLow()
	{
		return this.ordinal() < REDUCED.ordinal();
	}

	public boolean isEnabled()
	{
		return this!=DISABLED;
	}
}
