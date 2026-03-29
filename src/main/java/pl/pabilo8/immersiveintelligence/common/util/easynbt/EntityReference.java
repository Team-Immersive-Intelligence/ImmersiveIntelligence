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
 * @ii-approved 0.3.1
 * @since 22.08.2025
 */
public class EntityReference<TYPE extends Entity> implements INBTSerializable<NBTTagInt>
{
	private final Supplier<World> worldSupplier;
	@Nullable
	private TYPE entity;

	public EntityReference(Supplier<World> worldSupplier)
	{
		this(worldSupplier, null);
	}

	public EntityReference(@Nonnull Supplier<World> worldSupplier, @Nullable TYPE entity)
	{
		this.worldSupplier = worldSupplier;
		this.entity = entity;
	}

	@Nullable
	public TYPE get()
	{
		return entity;
	}

	public void set(@Nullable TYPE entity)
	{
		this.entity = entity;
	}

	@Override
	public NBTTagInt serializeNBT()
	{
		return new NBTTagInt(entity==null?0: entity.getEntityId());
	}

	@Override
	public void deserializeNBT(NBTTagInt nbt)
	{
		World world = worldSupplier.get();
		if(world==null)
			return;
		int id = nbt.getInt();
		if(id!=0)
			try
			{
				//noinspection unchecked
				set((TYPE)world.getEntityByID(id));
			} catch(ClassCastException e)
			{
				set(null);
			}
		else
			set(null);
	}
}
