package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Reference to either an entity or a block position target.
 * Also stores optional shot limits for request-style fire missions.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 22.02.2026
 */
public class TargetCoordinateReference implements INBTSerializable<NBTTagCompound>
{
	@Nullable
	private BlockPos position;
	private final EntityReference<Entity> entityReference;
	private Supplier<World> worldSupplier;

	private boolean shotsAreFinite = false;
	private int shotsRemaining = 0;

	public TargetCoordinateReference(Supplier<World> worldSupplier)
	{
		this.worldSupplier = worldSupplier;
		this.position = null;
		this.entityReference = new EntityReference<>(worldSupplier);
	}

	public TargetCoordinateReference withWorldSupplier(Supplier<World> worldSupplier)
	{
		this.worldSupplier = worldSupplier;
		return this;
	}

	public TargetCoordinateReference withEntity(Entity entity)
	{
		this.position = null;
		this.entityReference.set(entity);
		return this;
	}

	public TargetCoordinateReference withEntityID(int entityID)
	{
		this.position = null;
		this.entityReference.deserializeNBT(new NBTTagInt(entityID));
		return this;
	}

	public TargetCoordinateReference withPosition(BlockPos position)
	{
		this.position = position;
		this.entityReference.set(null);
		return this;
	}

	public TargetCoordinateReference withShotLimit(int shotsRemaining)
	{
		this.shotsAreFinite = true;
		this.shotsRemaining = Math.max(0, shotsRemaining);
		return this;
	}

	public TargetCoordinateReference withInfiniteShots()
	{
		this.shotsAreFinite = false;
		this.shotsRemaining = 0;
		return this;
	}

	/**
	 * @return whether the task should be executed or removed from memory
	 */
	public boolean shouldBeExecuted()
	{
		return shouldBeExecuted(worldSupplier==null?null: worldSupplier.get());
	}

	/**
	 * @return whether the task should be executed or removed from memory
	 */
	public boolean shouldBeExecuted(@Nullable World world)
	{
		if(shotsAreFinite&&shotsRemaining <= 0)
			return false;

		Entity entity = this.entityReference.get();
		if(entity!=null)
			return !entity.isDead;

		if(position!=null)
		{
			//Unloaded chunks are inconclusive, not a completed mission.
			if(world==null||(!world.isRemote&&!(world.isBlockLoaded(position))))
				return true;
			//Either the mission requires destroying a block or firing an amount of shots
			return !world.isAirBlock(position)^shotsAreFinite;
		}

		return false;
	}

	@Nullable
	public Vec3d supplyCoordinates()
	{
		Entity entity = this.entityReference.get();
		if(entity!=null)
			return new Vec3d(entity.posX, entity.posY+entity.height*0.5, entity.posZ);
		return position!=null?new Vec3d(position).addVector(0.5, 0.5, 0.5): null;
	}

	@Nullable
	public Vec3d supplyMotion()
	{
		Entity entity = this.entityReference.get();
		if(entity!=null)
			return IIEntityUtils.getEntityMotion(entity);
		return Vec3d.ZERO;
	}

	@Nullable
	public Entity getEntity()
	{
		return entityReference.get();
	}

	public int getEntityID()
	{
		return entityReference.serializeNBT().getInt();
	}

	@Nullable
	public BlockPos getPosition()
	{
		return position;
	}

	public boolean hasFiniteShots()
	{
		return shotsAreFinite;
	}

	public int getShotsRemaining()
	{
		return shotsRemaining;
	}

	/**
	 * Called by the weapon after a shot has been made to lower the counter.
	 */
	public void notifyAfterShot()
	{
		if(shotsAreFinite&&shotsRemaining > 0)
			shotsRemaining--;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		EasyNBT nbt = EasyNBT.newNBT()
				.withBoolean("shotsAreFinite", shotsAreFinite)
				.withInt("shotsRemaining", shotsRemaining);

		if(entityReference.get()!=null||getEntityID()!=0)
			nbt.withSerializable("entity", entityReference);
		else if(position!=null)
			nbt.withPos("pos", position);
		return nbt.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		EasyNBT enbt = EasyNBT.wrapNBT(nbt);
		this.shotsAreFinite = enbt.getBoolean("shotsAreFinite");
		this.shotsRemaining = enbt.getInt("shotsRemaining");

		if(nbt.hasKey("entity"))
			entityReference.deserializeNBT((NBTTagInt)nbt.getTag("entity"));
		else
			entityReference.set(null);

		position = null;
		if(enbt.hasKey("pos"))
			position = enbt.getPos("pos");
	}
}
