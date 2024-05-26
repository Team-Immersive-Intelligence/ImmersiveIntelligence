package pl.pabilo8.immersiveintelligence.api.ammo.enums;

import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Ammunition core types, each has its own characteristics, making it fulfill a different role.
 */
public enum CoreTypes implements ISerializableEnum
{
	SOFTPOINT(2, 1f, ComponentEffectShape.STAR, 1f, 0, 1f, ComponentRole.GENERAL_PURPOSE),

	SHAPED(1, 1.25f, ComponentEffectShape.CONE, 0.4f, 1, 1f, ComponentRole.SHAPED),
	SHAPED_SABOT(1, 1.25f, ComponentEffectShape.LINE, 0.6f, 1, 1f, ComponentRole.SHAPED),

	PIERCING(1, 0.75f, ComponentEffectShape.STAR, 1.2f, 1, 1f, ComponentRole.PIERCING),
	PIERCING_SABOT(0, 0f, ComponentEffectShape.LINE, 1.4f, 3, 0.85f, ComponentRole.PIERCING),

	CANISTER(4, 1.25f, ComponentEffectShape.ORB, 0.2f, -2, 0.125f, ComponentRole.GENERAL_PURPOSE),
	CLUSTER(2, 1.25f, ComponentEffectShape.CONE, 0.2f, -4, 0.125f, ComponentRole.GENERAL_PURPOSE);

	private final int componentSlots, penHardnessBonus;
	private final float componentEffectivenessMod, damageMod, penDepthMod;
	@Nonnull
	private final ComponentRole role;
	@Nonnull
	private final ComponentEffectShape effectShape;

	CoreTypes(int componentSlots, float componentEffectivenessMod, @Nonnull ComponentEffectShape effectShape, float penDepthMod, int penHardnessBonus, float damageMod, @Nullable ComponentRole role)
	{
		this.componentSlots = componentSlots;
		this.penDepthMod = penDepthMod;
		this.penHardnessBonus = penHardnessBonus;
		this.damageMod = damageMod;
		this.componentEffectivenessMod = componentEffectivenessMod;
		this.role = role;
		this.effectShape = effectShape;
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

	/**
	 * @return Role of this core type in ammunition piece
	 */
	@Nonnull
	public ComponentRole getRole()
	{
		return role;
	}

	/**
	 * @return EffectShape for this core type
	 */
	@Nonnull
	public ComponentEffectShape getEffectShape()
	{
		return effectShape;
	}

	@Nonnull
	public static CoreTypes v(String s)
	{
		String ss = s.toUpperCase();
		return Arrays.stream(values()).filter(e -> e.name().equals(ss)).findFirst().orElse(SOFTPOINT);
	}
}
