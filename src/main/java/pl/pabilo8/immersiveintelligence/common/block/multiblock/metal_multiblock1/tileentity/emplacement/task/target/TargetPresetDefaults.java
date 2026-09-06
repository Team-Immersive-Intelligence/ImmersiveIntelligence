package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target;

import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.*;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomaticStatus;

import javax.annotation.Nullable;

/**
 * Creates independent built-in Emplacement target presets.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
public final class TargetPresetDefaults
{
	public static final String MOBS = "mobs";
	public static final String EVERYONE = "everyone";
	public static final String ENEMY_FACTIONS = "enemy_factions";
	public static final String SHELLS = "shells";
	public static final String CUSTOM = "custom";
	private static final String NAME_KEY = "ii.gui.emplacement.target_preset.";

	private TargetPresetDefaults()
	{
	}

	public static TargetConfiguration createConfiguration()
	{
		TargetConfiguration configuration = new TargetConfiguration();
		configuration.addPreset(create(MOBS));
		configuration.addPreset(create(EVERYONE));
		configuration.addPreset(create(ENEMY_FACTIONS));
		configuration.addPreset(create(SHELLS));
		configuration.addPreset(create(CUSTOM));
		configuration.setActivePresetId(MOBS);
		return configuration;
	}

	@Nullable
	public static TargetDecisionTreePreset create(String id)
	{
		if(MOBS.equals(id))
			return preset(MOBS, new EntityTypeTargetFilter(TargetEntityType.MOB), 1);
		if(EVERYONE.equals(id))
			return preset(EVERYONE, new AnyTargetFilter(), 1);
		if(ENEMY_FACTIONS.equals(id))
			return preset(ENEMY_FACTIONS, new FactionRelationshipTargetFilter(DiplomaticStatus.ENEMY), 1);
		if(SHELLS.equals(id))
			return preset(SHELLS, new EntityTypeTargetFilter(TargetEntityType.PROJECTILE), 1);
		if(CUSTOM.equals(id))
			return preset(CUSTOM, new AnyTargetFilter(), 0);
		return null;
	}

	private static TargetDecisionTreePreset preset(String id, TargetFilter rootFilter, int weight)
	{
		return new TargetDecisionTreePreset(id, NAME_KEY+id, true,
				new TargetDecisionTree(new TargetDecisionTreeNode(rootFilter, weight)));
	}
}
