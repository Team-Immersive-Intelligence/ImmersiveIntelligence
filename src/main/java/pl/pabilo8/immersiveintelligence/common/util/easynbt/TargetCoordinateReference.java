package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 22.02.2026
 */
public class TargetCoordinateReference implements INBTSerializable<NBTTagCompound>
{
	@Nullable
	private BlockPos position;
	private final EntityReference<Entity> entityReference;

	private boolean shotsAreFinite = false;
	private int shotsRemaining = 0;

	public TargetCoordinateReference(Supplier<World> worldSupplier)
	{
		this.position = null;
		this.entityReference = new EntityReference<>(worldSupplier);
	}

	public TargetCoordinateReference withEntity(Entity entity)
	{
		this.position = null;
		this.entityReference.set(entity);
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
		this.shotsRemaining = shotsRemaining;
		return this;
	}

	/**
	 * @return whether the task should be executed or removed from memory
	 */
	public boolean shouldBeExecuted()
	{
		return !shotsAreFinite||shotsRemaining > 0;
	}

	@Nullable
	public Vec3d supplyCoordinates()
	{
		Entity entity = this.entityReference.get();
		if(entity!=null)
			return new Vec3d(entity.posX, entity.posY, entity.posZ);
		return position!=null?new Vec3d(position): null;
	}

	/**
	 * Called by the weapon after a shot has been made to lower the counter
	 */
	public void notifyAfterShot()
	{
		if(shotsAreFinite)
			shotsRemaining--;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		EasyNBT nbt = EasyNBT.newNBT();
		if(entityReference.get()!=null)
			nbt.withSerializable("entity", entityReference);
		else if(position!=null)
			nbt.withPos("pos", position);
		return nbt.unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		EasyNBT enbt = EasyNBT.wrapNBT(nbt);
		if(nbt.hasKey("entity"))
			entityReference.deserializeNBT((NBTTagInt)nbt.getTag("entity"));
		else
			entityReference.set(null);
		position = null;
		if(enbt.hasKey("pos"))
			position = enbt.getPos("pos");
	}
}
