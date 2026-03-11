package pl.pabilo8.immersiveintelligence.common.util.diplomacy;

import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 03.09.2025
 */
public enum PermissionLevel implements ISerializableEnum
{
	OWNER_ALLOW,
	MEMBER_ALLOW,
	ALLIES_ALLOW,
	ENEMY_ALLOW,
	OTHERS_ALLOW;

	public boolean atLeast(PermissionLevel other)
	{
		return this.ordinal() <= other.ordinal();
	}
}


