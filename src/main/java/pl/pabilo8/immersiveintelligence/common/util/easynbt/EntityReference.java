package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * References an entity in a world by its ID, allowing it to be serialized to NBT.
 * Use with {@link SyncNBT} and {@link NBTSerialisation}.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.08.2026
 * @ii-approved 0.3.1
 * @since 22.08.2025
 */
public class EntityReference<TYPE extends Entity> implements INBTSerializable<NBTTagInt>
{
	private Supplier<World> worldSupplier;
	@Nullable
	private TYPE entity;
	private int entityID;

	public EntityReference(Supplier<World> worldSupplier)
	{
		this(worldSupplier, null);
	}

	public EntityReference(@Nonnull Supplier<World> worldSupplier, @Nullable TYPE entity)
	{
		this.worldSupplier = worldSupplier==null?() -> null: worldSupplier;
		set(entity);
	}

	/**
	 * Updates the world used for lazy entity resolution.
	 */
	public EntityReference<TYPE> withWorldSupplier(@Nullable Supplier<World> worldSupplier)
	{
		this.worldSupplier = worldSupplier==null?() -> null: worldSupplier;
		return this;
	}

	@Nullable
	public TYPE get()
	{
		if(entity==null&&entityID!=0)
			resolveEntity();
		return entity;
	}

	public void set(@Nullable TYPE entity)
	{
		this.entity = entity;
		this.entityID = entity==null?0: entity.getEntityId();
	}

	@Override
	public NBTTagInt serializeNBT()
	{
		return new NBTTagInt(entity==null?entityID: entity.getEntityId());
	}

	@Override
	public void deserializeNBT(NBTTagInt nbt)
	{
		this.entityID = nbt==null?0: nbt.getInt();
		this.entity = null;
		resolveEntity();
	}

	@SuppressWarnings("unchecked")
	private void resolveEntity()
	{
		World world = worldSupplier.get();
		if(world==null||entityID==0)
			return;
		try
		{
			Entity resolved = world.getEntityByID(entityID);
			if(resolved!=null)
				this.entity = (TYPE)resolved;
		} catch(ClassCastException ignored)
		{
			this.entity = null;
		}
	}
}
