package pl.pabilo8.immersiveintelligence.api.ammo.enums;

import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public enum CoreTypes implements ISerializableEnum
{
	SOFTPOINT(2, 1f, 0, 1f, 1f, ComponentRole.GENERAL_PURPOSE),

	SHAPED(1, 0.4f, 1, 1f, 1.25f, ComponentRole.SHAPED),
	SHAPED_SABOT(1, 0.6f, 1, 1f, 1.25f, ComponentRole.SHAPED),

	PIERCING(1, 1.2f, 1, 1f, 0.75f, ComponentRole.PIERCING),
	PIERCING_SABOT(0, 1.4f, 3, 0.85f, 0f, ComponentRole.PIERCING),

	CANISTER(4, 0.2f, -2, 0.125f, 1.25f, ComponentRole.GENERAL_PURPOSE),
	CLUSTER(2, 0.2f, -4, 0.125f, 1.25f, ComponentRole.GENERAL_PURPOSE);

	private final int componentSlots, penHardnessBonus;
	private final float componentEffectivenessMod, damageMod, penDepthMod;
	@Nullable
	private final ComponentRole role;

	CoreTypes(int componentSlots, float penDepthMod, int penHardnessBonus, float damageMod, float componentEffectivenessMod, @Nullable ComponentRole role)
	{
		this.componentSlots = componentSlots;
		this.penDepthMod = penDepthMod;
		this.penHardnessBonus = penHardnessBonus;
		this.damageMod = damageMod;
		this.componentEffectivenessMod = componentEffectivenessMod;
		this.role = role;
	}

	public float getPenDepthMod()
	{
		return penDepthMod;
	}

	public int getPenHardnessBonus()
	{
		return penHardnessBonus;
	}

	public float getDamageMod()
	{
		return damageMod;
	}

	public int getComponentSlots()
	{
		return componentSlots;
	}

	public float getComponentEffectivenessMod()
	{
		return componentEffectivenessMod;
	}

	@Nullable
	public ComponentRole getRole()
	{
		return role;
	}

	@Nonnull
	public static CoreTypes v(String s)
	{
		String ss = s.toUpperCase();
		return Arrays.stream(values()).filter(e -> e.name().equals(ss)).findFirst().orElse(SOFTPOINT);
	}
}
