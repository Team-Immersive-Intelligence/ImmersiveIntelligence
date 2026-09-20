package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementTargetManager.FireMissionTargetType;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.TargetingLimits;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Stores one temporary Fire Mission Request.
 */
public class EmplacementFireMission implements INBTSerializable<NBTTagCompound>
{
	@Getter
	private String id = UUID.randomUUID().toString();
	public FireMissionTargetType type = FireMissionTargetType.POSITION;
	public TargetCoordinateReference target;
	private Supplier<World> worldSupplier;
	@Getter
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

	public EmplacementFireMission withAimingOnly()
	{
		this.target.withAimingOnly(true);
		return this;
	}

	public boolean isAimingOnly()
	{
		return target.isAimingOnly();
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

	@Override
	public NBTTagCompound serializeNBT()
	{
		return EasyNBT.newNBT()
				.withString("id", id)
				.withEnum("type", type)
				.withSerializable("target", target)
				.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.valid = false;
		String readId = nbt.getString("id");
		FireMissionTargetType readType = FireMissionTargetType.fromName(nbt.getString("type"));
		if(readId.isEmpty()||readId.length() > TargetingLimits.MAX_STRING_LENGTH
				||readType==null
				||!nbt.hasKey("target", EasyNBT.TAG_COMPOUND))
			return;

		TargetCoordinateReference readTarget = new TargetCoordinateReference(worldSupplier);
		readTarget.deserializeNBT(nbt.getCompoundTag("target"));
		if(readTarget.getShotsRemaining() > TargetingLimits.MAX_SHOTS
				||(readType==FireMissionTargetType.ENTITY&&!readTarget.isEntityTarget())
				||(readType==FireMissionTargetType.POSITION&&!readTarget.isPositionTarget()))
			return;
		this.id = readId;
		this.type = readType;
		this.target = readTarget;
		this.valid = true;
	}
}
