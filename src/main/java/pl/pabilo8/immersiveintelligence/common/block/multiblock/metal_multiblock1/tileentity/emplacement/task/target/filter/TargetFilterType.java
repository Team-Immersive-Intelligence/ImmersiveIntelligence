package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import lombok.Getter;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Identifies the supported target condition families.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.08.2026
 */
@Getter
public enum TargetFilterType
{
	INVALID("invalid", InvalidTargetFilter::new, false),
	ANY("any", AnyTargetFilter::new, true),
	ENTITY_ID("entity_id", EntityIdTargetFilter::new, true),
	MOD_ID("mod_id", ModIdTargetFilter::new, true),
	ENTITY_TYPE("entity_type", EntityTypeTargetFilter::new, true),
	NAME("name", NameTargetFilter::new, true),
	HEALTH("health", HealthTargetFilter::new, true),
	MAX_HEALTH("max_health", MaxHealthTargetFilter::new, true),
	DISTANCE("distance", DistanceTargetFilter::new, true),
	ON_GROUND("on_ground", OnGroundTargetFilter::new, true),
	IN_WATER("in_water", InWaterTargetFilter::new, true),
	ON_FIRE("on_fire", OnFireTargetFilter::new, true),
	FACTION_RELATIONSHIP("faction_relationship", FactionRelationshipTargetFilter::new, true);

	private final String id;
	private final Supplier<TargetFilter> factory;
	private final boolean userSelectable;

	TargetFilterType(String id, Supplier<TargetFilter> factory, boolean userSelectable)
	{
		this.id = id;
		this.factory = factory;
		this.userSelectable = userSelectable;
	}

	/**
	 * @return a usable default filter of this type
	 */
	public TargetFilter createDefault()
	{
		return factory.get();
	}

	@Nullable
	public static TargetFilterType fromId(String id)
	{
		if(id==null)
			return null;
		for(TargetFilterType type : values())
			if(type.id.equals(id))
				return type;
		return null;
	}
}
