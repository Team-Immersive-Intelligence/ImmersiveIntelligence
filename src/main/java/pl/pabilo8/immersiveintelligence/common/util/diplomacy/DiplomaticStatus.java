package pl.pabilo8.immersiveintelligence.common.util.diplomacy;

import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.09.2025
 */
public enum DiplomaticStatus implements ISerializableEnum
{
	MEMBER,
	ALLIED,
	NEUTRAL,
	ENEMY;

	public boolean atLeast(DiplomaticStatus other)
	{
		return this.ordinal() <= other.ordinal();
	}
}
