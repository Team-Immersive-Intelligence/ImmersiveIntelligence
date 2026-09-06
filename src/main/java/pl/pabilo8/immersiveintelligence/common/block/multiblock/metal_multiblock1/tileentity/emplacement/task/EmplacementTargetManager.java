package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter.TargetEntityProperties;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

/**
 * Schedules Fire Mission Requests and one autonomous target-tree Job.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 31.08.2026
 * @ii-approved 0.3.1
 * @since 14.09.2025
 */
public class EmplacementTargetManager implements INBTSerializable<NBTTagCompound>, Cloneable
{
	private static final String KEY_PAUSED = "paused";
	private static final String KEY_FIRE_MISSIONS = "fire_missions";
	private static final String KEY_FIRE_MISSION_REVISION = "fire_mission_revision";
	private static final String KEY_POSITION_MISSION_REVISION = "position_mission_revision";
	private static final String KEY_TARGET_CONFIG_EDIT = "target_config_edit";
	private static final String KEY_FIRE_MISSION_EDIT = "fire_mission_edit";
	private static final String KEY_BASE_REVISION = "base_revision";
	private static final int AUTONOMOUS_SCAN_INTERVAL = 8;
	private static final int SHARED_TARGET_HEARTBEAT = 40;
	private static final int MAX_SHARED_TARGETS = 32;

	private Supplier<World> worldSupplier = () -> null;
	public boolean paused;
	private TargetConfiguration targetConfiguration = TargetConfiguration.createDefault();
	public EasyCollection<EmplacementFireMission, NBTTagCompound> fireMissions = createMissionCollection();
	private int fireMissionRevision;
	private int positionMissionRevision;

	private final TargetEvaluationContext evaluationContext = new TargetEvaluationContext();
	private final Set<Entity> normalizedCandidates = Collections.newSetFromMap(new IdentityHashMap<>());
	private final ArrayList<Entity> sharedTargets = new ArrayList<>();
	private TargetCoordinateReference autonomousTarget = new TargetCoordinateReference(worldSupplier).withInfiniteShots();
	private boolean forceAutonomousScan = true;
	private int[] lastSharedEntityIds = new int[0];
	private long lastSharedSendTick;
	private boolean sharedSnapshotSent;

	public EmplacementTargetManager()
	{
	}

	public EmplacementTargetManager(Supplier<World> worldSupplier)
	{
		setWorldSupplier(worldSupplier);
	}

	private EasyCollection<EmplacementFireMission, NBTTagCompound> createMissionCollection()
	{
		return new EasyCollection<>(() -> new EmplacementFireMission(worldSupplier));
	}

	public EmplacementTargetManager setWorldSupplier(Supplier<World> worldSupplier)
	{
		this.worldSupplier = worldSupplier==null?() -> null: worldSupplier;
		for(EmplacementFireMission mission : fireMissions)
			mission.setWorldSupplier(this.worldSupplier);
		this.autonomousTarget.withWorldSupplier(this.worldSupplier);
		return this;
	}

	public EasyCollection<EmplacementFireMission, NBTTagCompound> getFireMissions()
	{
		return fireMissions;
	}

	public TargetConfiguration getTargetConfiguration()
	{
		return targetConfiguration;
	}

	public TargetConfiguration copyTargetConfiguration()
	{
		return targetConfiguration.copy();
	}

	public NBTTagCompound createTargetConfigurationUpdate(TargetConfiguration configuration)
	{
		NBTTagCompound update = configuration.serializeNBT();
		update.setBoolean(KEY_TARGET_CONFIG_EDIT, true);
		return update;
	}

	public FireMissionEdit createFireMissionEdit()
	{
		return new FireMissionEdit(worldSupplier, positionMissionRevision, fireMissions.serializeNBT());
	}

	public void addEntityMission(Entity entity, int shots)
	{
		if(entity==null||fireMissions.size() >= TargetingLimits.MAX_FIRE_MISSIONS)
			return;
		EmplacementFireMission mission = new EmplacementFireMission(worldSupplier).withEntity(entity);
		setMissionShots(mission, shots);
		fireMissions.add(mission);
		fireMissionRevision++;
	}

	public void addPositionMission(BlockPos position, int shots)
	{
		if(position==null||fireMissions.size() >= TargetingLimits.MAX_FIRE_MISSIONS)
			return;
		EmplacementFireMission mission = new EmplacementFireMission(worldSupplier).withPosition(position);
		setMissionShots(mission, shots);
		fireMissions.add(mission);
		fireMissionRevision++;
		positionMissionRevision++;
	}

	private void setMissionShots(EmplacementFireMission mission, int shots)
	{
		if(shots > 0)
			mission.withShotLimit(Math.min(shots, TargetingLimits.MAX_SHOTS));
		else
			mission.withInfiniteShots();
	}

	public void clearFireMissions()
	{
		if(fireMissions.isEmpty())
			return;
		boolean hadPositionMission = fireMissions.stream().anyMatch(EmplacementFireMission::isPositionMission);
		fireMissions.clear();
		fireMissionRevision++;
		if(hadPositionMission)
			positionMissionRevision++;
		forceAutonomousScan = true;
	}

	/**
	 * Decrements a completed shot and invalidates stale Fire Mission editor snapshots.
	 *
	 * @return true if persistent Fire Mission state changed
	 */
	public boolean notifyAfterShot(@Nullable TargetCoordinateReference target)
	{
		if(target==null)
			return false;
		int previousShots = target.getShotsRemaining();
		target.notifyAfterShot();
		if(previousShots==target.getShotsRemaining())
			return false;
		for(EmplacementFireMission mission : fireMissions)
			if(mission.target==target)
			{
				fireMissionRevision++;
				if(mission.isPositionMission())
					positionMissionRevision++;
				return true;
			}
		return false;
	}

	public void skipTask(int taskID)
	{
		paused = false;
		if(taskID > 0&&taskID < fireMissions.size())
		{
			EmplacementFireMission mission = fireMissions.remove(taskID);
			fireMissions.add(0, mission);
			fireMissionRevision++;
			if(mission.isPositionMission())
				positionMissionRevision++;
		}
	}

	public void stopTask(boolean switchToDefault)
	{
		paused = true;
	}

	public void resumeTask(boolean switchToDefault)
	{
		paused = false;
		forceAutonomousScan = true;
	}

	/**
	 * Applies a validated, explicit manager payload received from an Emplacement GUI.
	 */
	public boolean applyClientUpdate(NBTTagCompound update)
	{
		if(update==null)
			return false;
		boolean targetConfigurationEdit = update.getBoolean(KEY_TARGET_CONFIG_EDIT);
		boolean fireMissionEdit = update.getBoolean(KEY_FIRE_MISSION_EDIT);
		if(targetConfigurationEdit==fireMissionEdit)
			return false;
		if(targetConfigurationEdit)
		{
			TargetConfiguration parsed = new TargetConfiguration();
			parsed.deserializeNBT(update);
			if(!parsed.isValid())
				return false;
			targetConfiguration = parsed;
			invalidateAutonomousRuntime();
			return true;
		}
		if(fireMissionEdit)
		{
			if(update.getInteger(KEY_BASE_REVISION)!=positionMissionRevision
					||!update.hasKey(KEY_FIRE_MISSIONS, EasyNBT.TAG_LIST))
				return false;
			EasyCollection<EmplacementFireMission, NBTTagCompound> parsed =
					parseMissions(update.getTagList(KEY_FIRE_MISSIONS, EasyNBT.TAG_COMPOUND));
			if(parsed==null||parsed.stream().anyMatch(mission -> !mission.isPositionMission()))
				return false;
			EasyCollection<EmplacementFireMission, NBTTagCompound> merged = mergePositionMissions(parsed);
			if(merged==null)
				return false;
			fireMissions = merged;
			setWorldSupplier(worldSupplier);
			fireMissionRevision++;
			positionMissionRevision++;
			forceAutonomousScan = true;
			return true;
		}
		return false;
	}

	/**
	 * Returns the highest-priority executable Request, or the autonomous Job target.
	 */
	@Nullable
	public TargetCoordinateReference updateAndGetTarget(TileEntityEmplacement emplacement,
	                                                    @Nullable TargetCoordinateReference previousTarget)
	{
		if(!emplacement.dataControlEnabled)
			sharedSnapshotSent = false;
		if(paused)
		{
			if(emplacement.dataControlEnabled)
				publishSharedTargets(emplacement, Collections.emptyList());
			return null;
		}

		World world = worldSupplier.get();
		EmplacementWeapon weapon = emplacement.currentWeapon;
		EmplacementFireMission executable = null;
		boolean removed = false;
		boolean removedPosition = false;
		for(Iterator<EmplacementFireMission> iterator = fireMissions.iterator(); iterator.hasNext(); )
		{
			EmplacementFireMission mission = iterator.next();
			mission.setWorldSupplier(worldSupplier);
			if(!mission.shouldRemain(world))
			{
				iterator.remove();
				removed = true;
				removedPosition |= mission.isPositionMission();
				continue;
			}
			if(executable==null&&weapon!=null&&weapon.canExecuteFireMission(mission.target))
				executable = mission;
		}
		if(removed)
		{
			fireMissionRevision++;
			if(removedPosition)
				positionMissionRevision++;
			forceAutonomousScan = true;
			emplacement.markDirty();
		}

		if(executable!=null)
		{
			if(emplacement.dataControlEnabled&&isScanTick(emplacement))
				scanAutonomousTargets(emplacement);
			return executable.target;
		}

		boolean leavingRequest = previousTarget!=null&&previousTarget!=autonomousTarget;
		return updateAutonomousTarget(emplacement, leavingRequest);
	}

	public void pruneFinishedMissions()
	{
		World world = worldSupplier.get();
		boolean removed = false;
		boolean removedPosition = false;
		for(Iterator<EmplacementFireMission> iterator = fireMissions.iterator(); iterator.hasNext(); )
		{
			EmplacementFireMission mission = iterator.next();
			if(mission.shouldRemain(world))
				continue;
			iterator.remove();
			removed = true;
			removedPosition |= mission.isPositionMission();
		}
		if(removed)
		{
			fireMissionRevision++;
			if(removedPosition)
				positionMissionRevision++;
			forceAutonomousScan = true;
		}
	}

	@Nullable
	private TargetCoordinateReference updateAutonomousTarget(TileEntityEmplacement emplacement, boolean immediate)
	{
		TargetDecisionTreePreset active = targetConfiguration.getActivePreset();
		EmplacementWeapon weapon = emplacement.currentWeapon;
		if(active==null||weapon==null||!active.getTree().isValid())
		{
			clearAutonomousTarget();
			if(emplacement.dataControlEnabled)
				publishSharedTargets(emplacement, Collections.emptyList());
			return null;
		}

		boolean cachedValid = isAutonomousTargetValid(weapon);
		if(immediate||forceAutonomousScan||!cachedValid||isScanTick(emplacement))
			scanAutonomousTargets(emplacement);
		return autonomousTarget.getEntity()==null?null: autonomousTarget;
	}

	private boolean isScanTick(TileEntityEmplacement emplacement)
	{
		World world = emplacement.getWorld();
		long positionHash = emplacement.getPos().toLong();
		int phase = Math.floorMod((int)(positionHash^(positionHash >>> 32)), AUTONOMOUS_SCAN_INTERVAL);
		return (world.getTotalWorldTime()+phase)%AUTONOMOUS_SCAN_INTERVAL==0;
	}

	private boolean isAutonomousTargetValid(EmplacementWeapon weapon)
	{
		Entity entity = autonomousTarget.getEntity();
		AxisAlignedBB detection = weapon.getDetectionRangeBB();
		return entity!=null&&!entity.isDead&&detection!=null&&detection.intersects(entity.getEntityBoundingBox())
				&&(!(entity instanceof EntityLivingBase livingBase)||livingBase.getHealth() > 0)
				&&weapon.isVisibleTarget(entity)&&weapon.canSelectAutonomousTarget(entity);
	}

	private void scanAutonomousTargets(TileEntityEmplacement emplacement)
	{
		forceAutonomousScan = false;
		TargetDecisionTreePreset active = targetConfiguration.getActivePreset();
		EmplacementWeapon weapon = emplacement.currentWeapon;
		AxisAlignedBB detection = weapon==null?null: weapon.getDetectionRangeBB();
		if(active==null||weapon==null||detection==null)
		{
			clearAutonomousTarget();
			if(emplacement.dataControlEnabled)
				publishSharedTargets(emplacement, Collections.emptyList());
			return;
		}

		World world = emplacement.getWorld();
		List<Entity> candidates = world.getEntitiesWithinAABB(Entity.class, detection);
		OwnerIdentity ownerIdentity = emplacement.getOwnerIdentity();
		Vec3d targetingOrigin = emplacement.getWeaponCenter();
		normalizedCandidates.clear();
		sharedTargets.clear();
		TargetDecisionTree tree = active.getTree();
		Entity current = autonomousTarget.getEntity();
		Entity best = null;
		long bestScore = TargetDecisionTree.NO_MATCH;

		for(int i = 0, size = candidates.size(); i < size; i++)
		{
			Entity rawCandidate = candidates.get(i);
			Entity candidate = TargetEntityProperties.normalize(rawCandidate);
			if(candidate==null||!normalizedCandidates.add(candidate)||!weapon.isVisibleTarget(candidate))
				continue;
			evaluationContext.resetNormalized(world, ownerIdentity, targetingOrigin, rawCandidate, candidate);
			long score = tree.score(evaluationContext);
			if(score==TargetDecisionTree.NO_MATCH)
				continue;

			if(sharedTargets.size() < MAX_SHARED_TARGETS)
				sharedTargets.add(candidate);
			if(weapon.canSelectAutonomousTarget(candidate)
					&&(score > bestScore||(score==bestScore&&candidate==current)))
			{
				best = candidate;
				bestScore = score;
			}
		}

		if(best==null)
			clearAutonomousTarget();
		else
		{
			autonomousTarget.withEntity(best).withInfiniteShots();
			moveSelectedTargetFirst(best);
		}
		if(emplacement.dataControlEnabled)
			publishSharedTargets(emplacement, sharedTargets);
	}

	private void moveSelectedTargetFirst(Entity selected)
	{
		int index = sharedTargets.indexOf(selected);
		if(index > 0)
			Collections.swap(sharedTargets, 0, index);
		else if(index < 0)
		{
			sharedTargets.add(0, selected);
			if(sharedTargets.size() > MAX_SHARED_TARGETS)
				sharedTargets.remove(sharedTargets.size()-1);
		}
	}

	private void publishSharedTargets(TileEntityEmplacement emplacement, List<Entity> entities)
	{
		int size = Math.min(entities.size(), MAX_SHARED_TARGETS);
		long worldTime = emplacement.getWorld().getTotalWorldTime();
		boolean changed = !sharedSnapshotSent||size!=lastSharedEntityIds.length;
		for(int i = 0; !changed&&i < size; i++)
			changed = entities.get(i).getEntityId()!=lastSharedEntityIds[i];
		boolean heartbeat = sharedSnapshotSent&&worldTime-lastSharedSendTick >= SHARED_TARGET_HEARTBEAT;
		if(!changed&&!heartbeat)
			return;

		int[] ids = new int[size];
		Entity[] output = new Entity[size];
		for(int i = 0; i < size; i++)
		{
			ids[i] = entities.get(i).getEntityId();
			output[i] = entities.get(i);
		}
		emplacement.handleSendingEnemyPos(output);
		lastSharedEntityIds = ids;
		lastSharedSendTick = worldTime;
		sharedSnapshotSent = true;
	}

	private void clearAutonomousTarget()
	{
		autonomousTarget.clearTarget().withInfiniteShots();
	}

	private void invalidateAutonomousRuntime()
	{
		clearAutonomousTarget();
		forceAutonomousScan = true;
		sharedSnapshotSent = false;
		lastSharedEntityIds = new int[0];
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound result = targetConfiguration.serializeNBT();
		result.setBoolean(KEY_PAUSED, paused);
		result.setTag(KEY_FIRE_MISSIONS, fireMissions.serializeNBT());
		result.setInteger(KEY_FIRE_MISSION_REVISION, fireMissionRevision);
		result.setInteger(KEY_POSITION_MISSION_REVISION, positionMissionRevision);
		return result;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey(KEY_PAUSED))
			this.paused = nbt.getBoolean(KEY_PAUSED);
		if(nbt.hasKey(TargetConfiguration.KEY_PRESETS, EasyNBT.TAG_LIST))
		{
			TargetConfiguration parsed = new TargetConfiguration();
			parsed.deserializeNBT(nbt);
			this.targetConfiguration = parsed.isValid()?parsed: new TargetConfiguration();
		}
		if(nbt.hasKey(KEY_FIRE_MISSIONS, EasyNBT.TAG_LIST))
		{
			EasyCollection<EmplacementFireMission, NBTTagCompound> parsed =
					parseMissions(nbt.getTagList(KEY_FIRE_MISSIONS, EasyNBT.TAG_COMPOUND));
			this.fireMissions = parsed==null?createMissionCollection(): parsed;
			this.fireMissionRevision = Math.max(0, nbt.getInteger(KEY_FIRE_MISSION_REVISION));
			this.positionMissionRevision = nbt.hasKey(KEY_POSITION_MISSION_REVISION)?
					Math.max(0, nbt.getInteger(KEY_POSITION_MISSION_REVISION)):
					this.fireMissionRevision;
		}
		setWorldSupplier(worldSupplier);
		invalidateAutonomousRuntime();
	}

	@Nullable
	private EasyCollection<EmplacementFireMission, NBTTagCompound> parseMissions(NBTTagList list)
	{
		if(list.tagCount() > TargetingLimits.MAX_FIRE_MISSIONS)
			return null;
		EasyCollection<EmplacementFireMission, NBTTagCompound> parsed = createMissionCollection();
		Set<String> ids = new HashSet<>();
		for(int i = 0; i < list.tagCount(); i++)
		{
			EmplacementFireMission mission = new EmplacementFireMission(worldSupplier);
			mission.deserializeNBT(list.getCompoundTagAt(i));
			if(!mission.isValid()||!ids.add(mission.getId()))
				return null;
			parsed.add(mission);
		}
		return parsed;
	}

	@Nullable
	private EasyCollection<EmplacementFireMission, NBTTagCompound> mergePositionMissions(
			EasyCollection<EmplacementFireMission, NBTTagCompound> editedPositions)
	{
		int entityCount = (int)fireMissions.stream().filter(mission -> !mission.isPositionMission()).count();
		if(entityCount+editedPositions.size() > TargetingLimits.MAX_FIRE_MISSIONS)
			return null;

		Map<String, EmplacementFireMission> pending = new LinkedHashMap<>();
		for(EmplacementFireMission mission : editedPositions)
			pending.put(mission.getId(), mission);

		EasyCollection<EmplacementFireMission, NBTTagCompound> merged = createMissionCollection();
		for(EmplacementFireMission current : fireMissions)
		{
			if(!current.isPositionMission())
			{
				merged.add(current);
				continue;
			}
			EmplacementFireMission replacement = pending.remove(current.getId());
			if(replacement!=null)
				merged.add(replacement);
		}
		for(EmplacementFireMission added : pending.values())
			merged.add(added);
		return merged;
	}

	@Override
	public EmplacementTargetManager clone()
	{
		EmplacementTargetManager manager = new EmplacementTargetManager(worldSupplier);
		manager.deserializeNBT(serializeNBT());
		return manager;
	}

	/**
	 * Stores one temporary Fire Mission Request.
	 */
	public static class EmplacementFireMission implements INBTSerializable<NBTTagCompound>
	{
		private String id = UUID.randomUUID().toString();
		public FireMissionTargetType type = FireMissionTargetType.POSITION;
		public String name = "";
		public TargetCoordinateReference target;
		private Supplier<World> worldSupplier;
		private boolean valid = true;

		public EmplacementFireMission()
		{
			this(() -> null);
		}

		public EmplacementFireMission(Supplier<World> worldSupplier)
		{
			this.worldSupplier = worldSupplier==null?() -> null: worldSupplier;
			this.target = new TargetCoordinateReference(this.worldSupplier)
					.withPosition(BlockPos.ORIGIN)
					.withShotLimit(1);
		}

		public String getId()
		{
			return id;
		}

		public boolean isValid()
		{
			return valid;
		}

		public EmplacementFireMission setWorldSupplier(Supplier<World> worldSupplier)
		{
			this.worldSupplier = worldSupplier==null?() -> null: worldSupplier;
			this.target.withWorldSupplier(this.worldSupplier);
			return this;
		}

		public EmplacementFireMission withName(String name)
		{
			this.name = TargetingLimits.clampString(name);
			return this;
		}

		public EmplacementFireMission withEntity(Entity entity)
		{
			this.type = FireMissionTargetType.ENTITY;
			this.target.withEntity(entity);
			return this;
		}

		public EmplacementFireMission withEntityID(int entityID)
		{
			this.type = FireMissionTargetType.ENTITY;
			this.target.withEntityID(entityID);
			return this;
		}

		public EmplacementFireMission withPosition(BlockPos position)
		{
			this.type = FireMissionTargetType.POSITION;
			this.target.withPosition(position);
			return this;
		}

		public boolean isPositionMission()
		{
			return type==FireMissionTargetType.POSITION;
		}

		public EmplacementFireMission withShotLimit(int shots)
		{
			this.target.withShotLimit(Math.min(Math.max(0, shots), TargetingLimits.MAX_SHOTS));
			return this;
		}

		public EmplacementFireMission withInfiniteShots()
		{
			this.target.withInfiniteShots();
			return this;
		}

		public boolean isJob()
		{
			return false;
		}

		public boolean shouldRemain(@Nullable World world)
		{
			return target.shouldBeExecuted(world);
		}

		public EmplacementFireMission copyWithNewId()
		{
			EmplacementFireMission copy = new EmplacementFireMission(worldSupplier);
			copy.deserializeNBT(serializeNBT());
			copy.id = UUID.randomUUID().toString();
			return copy;
		}

		@Nonnull
		public String getDisplayName()
		{
			return name==null?"": name;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return EasyNBT.newNBT()
					.withString("id", id)
					.withEnum("type", type)
					.withString("name", name==null?"": name)
					.withSerializable("target", target)
					.unwrap();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			this.valid = false;
			String readId = nbt.getString("id");
			String readName = nbt.getString("name");
			FireMissionTargetType readType = FireMissionTargetType.fromName(nbt.getString("type"));
			if(readId.isEmpty()||readId.length() > TargetingLimits.MAX_STRING_LENGTH
					||readName.length() > TargetingLimits.MAX_STRING_LENGTH||readType==null
					||!nbt.hasKey("target", EasyNBT.TAG_COMPOUND))
				return;

			TargetCoordinateReference readTarget = new TargetCoordinateReference(worldSupplier);
			readTarget.deserializeNBT(nbt.getCompoundTag("target"));
			if(readTarget.getShotsRemaining() > TargetingLimits.MAX_SHOTS
					||(readType==FireMissionTargetType.ENTITY&&!readTarget.isEntityTarget())
					||(readType==FireMissionTargetType.POSITION&&!readTarget.isPositionTarget()))
				return;
			this.id = readId;
			this.name = readName;
			this.type = readType;
			this.target = readTarget;
			this.valid = true;
		}
	}

	public enum FireMissionTargetType implements ISerializableEnum
	{
		POSITION,
		ENTITY;

		@Nullable
		public static FireMissionTargetType fromName(String name)
		{
			if(name==null)
				return null;
			for(FireMissionTargetType type : values())
				if(type.getName().equals(name))
					return type;
			return null;
		}
	}

	/**
	 * Carries a revision-checked Fire Mission GUI edit.
	 */
	public static class FireMissionEdit implements INBTSerializable<NBTTagCompound>
	{
		private Supplier<World> worldSupplier = () -> null;
		private int baseRevision;
		private EasyCollection<EmplacementFireMission, NBTTagCompound> missions =
				new EasyCollection<>(() -> new EmplacementFireMission(worldSupplier));

		public FireMissionEdit()
		{
		}

		private FireMissionEdit(Supplier<World> worldSupplier, int baseRevision, NBTTagList missions)
		{
			this.worldSupplier = worldSupplier==null?() -> null: worldSupplier;
			this.baseRevision = baseRevision;
			this.missions = new EasyCollection<>(() -> new EmplacementFireMission(this.worldSupplier));
			this.missions.deserializeNBT(missions);
		}

		public EasyCollection<EmplacementFireMission, NBTTagCompound> getMissions()
		{
			return missions;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagList editableMissions = new NBTTagList();
			for(EmplacementFireMission mission : missions)
				if(mission.isPositionMission())
					editableMissions.appendTag(mission.serializeNBT());
			return EasyNBT.newNBT()
					.withBoolean(KEY_FIRE_MISSION_EDIT, true)
					.withInt(KEY_BASE_REVISION, baseRevision)
					.withTag(KEY_FIRE_MISSIONS, editableMissions)
					.unwrap();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			this.baseRevision = nbt.getInteger(KEY_BASE_REVISION);
			if(nbt.hasKey(KEY_FIRE_MISSIONS, EasyNBT.TAG_LIST))
				this.missions.deserializeNBT(nbt.getTagList(KEY_FIRE_MISSIONS, EasyNBT.TAG_COMPOUND));
		}
	}
}
