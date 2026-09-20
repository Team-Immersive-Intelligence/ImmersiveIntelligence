package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.radar;

import lombok.Value;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetConfiguration;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetDecisionTree;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetDecisionTreePreset;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetEvaluationContext;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.TargetEntityProperties;
import pl.pabilo8.immersiveintelligence.common.util.TerrainVisibilityMatrix;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.*;

/**
 * Filters visible Radar contacts with a configurable target decision tree.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.09.2026
 */
public class RadarTargetManager implements INBTSerializable<NBTTagCompound>
{
	private static final String KEY_TARGET_CONFIG_EDIT = "target_config_edit";
	private static final int SCAN_INTERVAL = 20;
	private static final int TERRAIN_VISIBILITY_UPDATE_INTERVAL = 200;
	private static final int OUTPUT_HEARTBEAT = 40;
	private static final int MAX_REPORTED_TARGETS = 32;

	private TargetConfiguration targetConfiguration = TargetConfiguration.createDefault();
	private final TargetEvaluationContext evaluationContext = new TargetEvaluationContext();
	private final TerrainVisibilityMatrix terrainVisibility = new TerrainVisibilityMatrix();
	private final Set<Entity> normalizedCandidates = Collections.newSetFromMap(new IdentityHashMap<>());
	private int[] lastReportedEntityIds = new int[0];
	private long lastScanTick = Long.MIN_VALUE;
	private long lastVisibilityUpdate = Long.MIN_VALUE;
	private long lastOutputTick;
	private boolean forceScan = true;
	private boolean outputSnapshotSent;

	/**
	 * @return a detached configuration safe for editing in a GUI
	 */
	public TargetConfiguration copyTargetConfiguration()
	{
		return targetConfiguration.copy();
	}

	/**
	 * Marks a target configuration as an intentional GUI update.
	 */
	public NBTTagCompound createTargetConfigurationUpdate(TargetConfiguration configuration)
	{
		NBTTagCompound update = configuration.serializeNBT();
		update.setBoolean(KEY_TARGET_CONFIG_EDIT, true);
		return update;
	}

	/**
	 * Applies a validated target-tree update received from the Radar GUI.
	 */
	public boolean applyClientUpdate(NBTTagCompound update)
	{
		if(update==null||!update.getBoolean(KEY_TARGET_CONFIG_EDIT))
			return false;
		TargetConfiguration parsed = new TargetConfiguration();
		parsed.deserializeNBT(update);
		if(!parsed.isValid())
			return false;
		targetConfiguration = parsed;
		invalidateRuntime();
		return true;
	}

	/**
	 * Updates Radar contacts and publishes the current filtered snapshot.
	 */
	public void update(TileEntityRadar radar, boolean scanEnabled, boolean outputEnabled)
	{
		if(!outputEnabled)
		{
			forceScan = true;
			outputSnapshotSent = false;
			lastReportedEntityIds = new int[0];
			return;
		}
		if(!scanEnabled)
		{
			forceScan = true;
			publish(radar, Collections.emptyList());
			return;
		}

		World world = radar.getWorld();
		long worldTime = world.getTotalWorldTime();
		if(forceScan||worldTime < lastScanTick||worldTime-lastScanTick >= SCAN_INTERVAL)
			scan(radar);
	}

	private void scan(TileEntityRadar radar)
	{
		forceScan = false;
		World world = radar.getWorld();
		lastScanTick = world.getTotalWorldTime();
		TargetDecisionTreePreset activePreset = targetConfiguration.getActivePreset();
		AxisAlignedBB detectionRange = radar.getDetectionRangeBB();
		if(activePreset==null||!activePreset.getTree().isValid()||detectionRange==null)
		{
			publish(radar, Collections.emptyList());
			return;
		}

		updateTerrainVisibility(radar, detectionRange);
		normalizedCandidates.clear();
		List<ScoredTarget> matches = new ArrayList<>();
		Vec3d origin = radar.getRadarCenter();
		TargetDecisionTree tree = activePreset.getTree();
		List<Entity> candidates = world.getEntitiesWithinAABB(Entity.class, detectionRange);
		for(int i = 0, size = candidates.size(); i < size; i++)
		{
			Entity rawCandidate = candidates.get(i);
			Entity candidate = TargetEntityProperties.normalize(rawCandidate);
			if(!candidate.isEntityAlive()||!normalizedCandidates.add(candidate)||!isTerrainVisible(candidate))
				continue;
			evaluationContext.resetNormalized(world, radar.getOwnerIdentity(), origin, rawCandidate, candidate);
			long score = tree.score(evaluationContext);
			if(score!=TargetDecisionTree.NO_MATCH)
				matches.add(new ScoredTarget(candidate, score));
		}

		matches.sort(Comparator.comparingLong(ScoredTarget::getScore).reversed()
				.thenComparingInt(target -> target.getEntity().getEntityId()));
		List<Entity> reported = new ArrayList<>(Math.min(matches.size(), MAX_REPORTED_TARGETS));
		for(int i = 0; i < matches.size()&&i < MAX_REPORTED_TARGETS; i++)
			reported.add(matches.get(i).getEntity());
		publish(radar, reported);
	}

	private void updateTerrainVisibility(TileEntityRadar radar, AxisAlignedBB bounds)
	{
		World world = radar.getWorld();
		Vec3d origin = radar.getRadarCenter();
		long worldTime = world.getTotalWorldTime();
		if(!terrainVisibility.isConfiguredFor(world, origin, bounds)||worldTime < lastVisibilityUpdate
				||worldTime-lastVisibilityUpdate >= TERRAIN_VISIBILITY_UPDATE_INTERVAL)
		{
			terrainVisibility.update(world, origin, bounds, radar.getTerrainVisibilityIgnoredPositions());
			lastVisibilityUpdate = worldTime;
		}
	}

	private boolean isTerrainVisible(Entity entity)
	{
		return terrainVisibility.isVisible(new Vec3d(entity.posX, entity.posY+entity.height*0.5d, entity.posZ));
	}

	private void publish(TileEntityRadar radar, List<Entity> entities)
	{
		int size = Math.min(entities.size(), MAX_REPORTED_TARGETS);
		long worldTime = radar.getWorld().getTotalWorldTime();
		boolean changed = !outputSnapshotSent||size!=lastReportedEntityIds.length;
		for(int i = 0; !changed&&i < size; i++)
			changed = entities.get(i).getEntityId()!=lastReportedEntityIds[i];
		boolean heartbeat = size > 0&&outputSnapshotSent&&worldTime-lastOutputTick >= OUTPUT_HEARTBEAT;
		if(!changed&&!heartbeat)
			return;

		int[] ids = new int[size];
		Entity[] output = new Entity[size];
		for(int i = 0; i < size; i++)
		{
			ids[i] = entities.get(i).getEntityId();
			output[i] = entities.get(i);
		}
		radar.sendDetectedTargets(output);
		lastReportedEntityIds = ids;
		lastOutputTick = worldTime;
		outputSnapshotSent = true;
	}

	private void invalidateRuntime()
	{
		forceScan = true;
		outputSnapshotSent = false;
		lastReportedEntityIds = new int[0];
		lastScanTick = Long.MIN_VALUE;
		lastVisibilityUpdate = Long.MIN_VALUE;
		terrainVisibility.invalidate();
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return targetConfiguration.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		if(nbt!=null&&nbt.hasKey(TargetConfiguration.KEY_PRESETS, EasyNBT.TAG_LIST))
		{
			TargetConfiguration parsed = new TargetConfiguration();
			parsed.deserializeNBT(nbt);
			targetConfiguration = parsed.isValid()?parsed: TargetConfiguration.createDefault();
		}
		else
			targetConfiguration = TargetConfiguration.createDefault();
		invalidateRuntime();
	}

	@Value
	private static class ScoredTarget
	{
		Entity entity;
		long score;
	}
}
