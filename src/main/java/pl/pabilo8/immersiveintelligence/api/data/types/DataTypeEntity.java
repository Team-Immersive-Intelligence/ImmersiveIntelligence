package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeEntity extends DataType
{
	//primary info
	public int entityID = 0;
	public int dimensionID = 0;
	//additional info, for ease of access
	public String entityClass = "";
	public String customName = "";
	public Vec3d lastPos = Vec3d.ZERO;

	public DataTypeEntity(Entity entity)
	{
		this(entity, BlockPos.ORIGIN);
	}

	public DataTypeEntity(Entity entity, BlockPos seenFrom)
	{
		this.entityID = entity.getEntityId();
		this.dimensionID = entity.dimension;
		this.customName = entity.getCustomNameTag();
		EntityEntry entry = EntityRegistry.getEntry(entity.getClass());
		if(entry!=null)
			this.entityClass = Optional.ofNullable(entry.getRegistryName()).orElse(new ResourceLocation("unknown")).toString();

		this.lastPos = entity.getPositionVector().subtract(seenFrom.getX(), seenFrom.getY(), seenFrom.getZ());
	}

	public DataTypeEntity()
	{

	}

	@Nonnull
	@Override
	public String getName()
	{
		return "entity";
	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		this.entityID = n.getInteger("entityID");
		this.dimensionID = n.getInteger("dimensionID");
		this.entityClass = n.getString("entityClass");
		this.customName = n.getString("customName");
		this.lastPos = new Vec3d(n.getDouble("last_x"), n.getDouble("last_y"), n.getDouble("last_z"));
	}

	@Nonnull
	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		actualValueToNBT(nbt);
		return nbt;
	}

	public NBTTagCompound actualValueToNBT(NBTTagCompound nbt)
	{
		if(entityID!=0)
			nbt.setInteger("entityID", entityID);
		if(dimensionID!=0)
			nbt.setInteger("dimensionID", dimensionID);
		if(!entityClass.isEmpty())
			nbt.setString("entityClass", entityClass);
		if(!customName.isEmpty())
			nbt.setString("customName", customName);
		if(lastPos!=Vec3d.ZERO)
		{
			nbt.setDouble("last_x", lastPos.x);
			nbt.setDouble("last_y", lastPos.y);
			nbt.setDouble("last_z", lastPos.z);
		}
		return nbt;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof DataTypeEntity))
			return false;
		if(obj==this)
			return true;
		DataTypeEntity casted = (DataTypeEntity)obj;

		return casted.entityID==entityID&&casted.dimensionID==dimensionID;
	}

	@Override
	public String toString()
	{
		return actualValueToNBT(new NBTTagCompound()).toString();
	}
}
