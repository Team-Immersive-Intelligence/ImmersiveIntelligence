package pl.pabilo8.immersiveintelligence.api.ammo.enums;

import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Function;

public enum EnumCoreTypes implements ISerializableEnum
{
	SOFTPOINT(2, 0.5f, 1f, 1f, EnumComponentRole.GENERAL_PURPOSE),

	SHAPED(1, 1.35f, 1.25f, 1f, EnumComponentRole.SHAPED),
	SHAPED_SABOT(1, 1.35f, 1.25f, 1f, EnumComponentRole.SHAPED),

	PIERCING(1, 1.5f, 0.75f, 1f, EnumComponentRole.PIERCING),
	PIERCING_SABOT(0, 2f, 0f, 0.85f, EnumComponentRole.PIERCING),

	CANISTER(4, 0.125f, 1.25f, 0.125f, EnumComponentRole.GENERAL_PURPOSE),
	CLUSTER(2, 0.125f, 1.25f, 0.125f, EnumComponentRole.GENERAL_PURPOSE);

	private final int componentSlots;
	private final float componentEffectivenessMod;
	private final Function<PenMaterialTypes, Float> getPenEffectiveness;
	private final Function<PenMaterialTypes, Float> getDamageMod;
	@Nullable
	private final EnumComponentRole role;

	EnumCoreTypes(int componentSlots, float penHardnessMod, float componentEffectivenessMod, float damageMod, @Nullable EnumComponentRole role)
	{
		this.componentSlots = componentSlots;
		this.getPenEffectiveness = penMaterialTypes -> penHardnessMod;
		this.componentEffectivenessMod = componentEffectivenessMod;
		this.getDamageMod = penMaterialTypes -> damageMod;
		this.role = role;
	}

	public float getPenMod(PenMaterialTypes p)
	{
		return getPenEffectiveness.apply(p);
	}

	public float getDamageMod(PenMaterialTypes p)
	{
		return getDamageMod.apply(p);
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
	public EnumComponentRole getRole()
	{
		return role;
	}

	@Nonnull
	public static EnumCoreTypes v(String s)
	{
		String ss = s.toUpperCase();
		return Arrays.stream(values()).filter(e -> e.name().equals(ss)).findFirst().orElse(SOFTPOINT);
	}
}
