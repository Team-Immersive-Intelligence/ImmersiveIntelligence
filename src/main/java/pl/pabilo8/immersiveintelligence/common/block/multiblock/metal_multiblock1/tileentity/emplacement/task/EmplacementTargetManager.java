package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.function.Supplier;

/**
 * Stores emplacement requests. Fire Missions are requests, not jobs: they expire when the target is gone
 * or when the finite shot counter reaches zero.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 14.09.2025
 */
public class EmplacementTargetManager implements INBTSerializable<NBTTagCompound>, Cloneable
{
	private Supplier<World> worldSupplier = () -> null;
	public boolean paused = false;
	public EasyCollection<EmplacementFireMission, NBTTagCompound> fireMissions = new EasyCollection<>(() -> new EmplacementFireMission(worldSupplier));

	public EmplacementTargetManager()
	{

	}

	public EmplacementTargetManager(Supplier<World> worldSupplier)
	{
		setWorldSupplier(worldSupplier);
	}

	public EmplacementTargetManager setWorldSupplier(Supplier<World> worldSupplier)
	{
		this.worldSupplier = worldSupplier==null?() -> null: worldSupplier;
		for(EmplacementFireMission mission : fireMissions)
			mission.setWorldSupplier(this.worldSupplier);
		return this;
	}

	public EasyCollection<EmplacementFireMission, NBTTagCompound> getFireMissions()
	{
		return fireMissions;
	}

	public void addEntityMission(Entity entity, int shots)
	{
		EmplacementFireMission mission = new EmplacementFireMission(worldSupplier)
				.withEntity(entity);
		if(shots > 0)
			mission.withShotLimit(shots);
		fireMissions.add(mission);
	}

	public void addPositionMission(BlockPos position, int shots)
	{
		EmplacementFireMission mission = new EmplacementFireMission(worldSupplier)
				.withPosition(position);
		if(shots > 0)
			mission.withShotLimit(shots);
		fireMissions.add(mission);
	}

	public void skipTask(int taskID)
	{
		paused = false;
		if(taskID > 0&&taskID < fireMissions.size())
		{
			EmplacementFireMission mission = fireMissions.remove(taskID);
			fireMissions.add(0, mission);
		}
	}

	public void stopTask(boolean switchToDefault)
	{
		paused = true;
	}

	public void resumeTask(boolean switchToDefault)
	{
		paused = false;
	}

	@Nullable
	public TargetCoordinateReference provideNextTask()
	{
		if(paused)
			return null;

		World world = worldSupplier.get();
		for(Iterator<EmplacementFireMission> iterator = fireMissions.iterator(); iterator.hasNext(); )
		{
			EmplacementFireMission mission = iterator.next();
			mission.setWorldSupplier(worldSupplier);
			if(!mission.shouldRemain(world))
			{
				iterator.remove();
				continue;
			}
			return mission.target;
		}
		return null;
	}

	public void pruneFinishedMissions()
	{
		World world = worldSupplier.get();
		fireMissions.removeIf(mission -> !mission.shouldRemain(world));
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT()
				.withBoolean("paused", paused)
				.withSerializable("fire_missions", fireMissions)
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		EasyNBT easyNBT = EasyNBT.wrapNBT(nbt);
		this.paused = easyNBT.getBoolean("paused");
		this.fireMissions.deserializeNBT(easyNBT.getList("fire_missions", EasyNBT.TAG_COMPOUND));
		setWorldSupplier(worldSupplier);
	}

	@Override
	public EmplacementTargetManager clone()
	{
		EmplacementTargetManager manager = new EmplacementTargetManager(worldSupplier);
		manager.deserializeNBT(this.serializeNBT());
		return manager;
	}

	public static class EmplacementFireMission implements INBTSerializable<NBTTagCompound>
	{
		public FireMissionTargetType type = FireMissionTargetType.POSITION;
		public String name = "";
		public TargetCoordinateReference target;
		private Supplier<World> worldSupplier;

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

		public EmplacementFireMission setWorldSupplier(Supplier<World> worldSupplier)
		{
			this.worldSupplier = worldSupplier==null?() -> null: worldSupplier;
			this.target.withWorldSupplier(this.worldSupplier);
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

		public EmplacementFireMission withShotLimit(int shots)
		{
			this.target.withShotLimit(shots);
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

		@Nonnull
		public String getDisplayName()
		{
			if(name!=null&&!name.isEmpty())
				return name;
			if(type==FireMissionTargetType.ENTITY)
				return "Entity #"+target.getEntityID();
			BlockPos pos = target.getPosition();
			return pos==null?"Position": pos.getX()+", "+pos.getY()+", "+pos.getZ();
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return EasyNBT.newNBT()
					.withEnum("type", type)
					.withString("name", name==null?"": name)
					.withSerializable("target", target)
					.unwrap();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			EasyNBT easyNBT = EasyNBT.wrapNBT(nbt);
			this.type = easyNBT.hasKey("type")?easyNBT.getEnum("type", FireMissionTargetType.class): FireMissionTargetType.POSITION;
			this.name = easyNBT.getString("name");
			this.target = new TargetCoordinateReference(worldSupplier);
			this.target.deserializeNBT(easyNBT.getCompound("target"));
		}
	}

	public enum FireMissionTargetType implements ISerializableEnum
	{
		POSITION,
		ENTITY;

		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}
}
