package pl.pabilo8.immersiveintelligence.api.api.protection;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.api.protection.capability.IRadiationEmitter;
import pl.pabilo8.immersiveintelligence.api.api.protection.capability.ProtectionCapabilities;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISaveData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Registry and handler for nuclear radiation.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 22.07.2026
 */
public final class RadiationHandler implements INBTSerializable<NBTTagCompound>
{
	public static final RadiationHandler INSTANCE = new RadiationHandler();
	private static final int UPDATE_INTERVAL = 40;
	private static final float MINIMUM_EXPOSURE = 0.01f;

	private final Map<RadiationKey, RadiationCenter> centers = new LinkedHashMap<>();
	private final Map<Integer, Map<Long, List<RadiationCenter>>> centerIndex = new HashMap<>();
	private final Map<Integer, Map<Long, List<EmitterSource>>> emitterIndex = new HashMap<>();
	private final Map<Long, List<EmitterSource>> clientEmitterIndex = new HashMap<>();
	@Nullable
	private World clientEmitterWorld;
	private long clientEmitterUpdate = Long.MIN_VALUE;

	private RadiationHandler()
	{
	}

	//--- Persistent centres ---//

	@Nonnull
	public RadiationCenter addRadiationCenter(@Nonnull World world, @Nonnull BlockPos position, float radius, float strength)
	{
		return addRadiationCenter(world.provider.getDimension(), position, radius, strength);
	}

	@Nonnull
	public RadiationCenter addRadiationCenter(int dimension, @Nonnull BlockPos position, float radius, float strength)
	{
		return putRadiationCenter(dimension, position, radius, strength, true);
	}

	@Nonnull
	public RadiationCenter addOrIncreaseRadiationCenter(@Nonnull World world, @Nonnull BlockPos position, float radius, float strength)
	{
		return addOrIncreaseRadiationCenter(world.provider.getDimension(), position, radius, strength);
	}

	@Nonnull
	public RadiationCenter addOrIncreaseRadiationCenter(int dimension, @Nonnull BlockPos position, float radius, float strength)
	{
		RadiationKey key = new RadiationKey(dimension, position);
		RadiationCenter existing = centers.get(key);
		if(existing==null)
			return addRadiationCenter(dimension, position, radius, strength);

		unindex(existing);
		existing.setRadius(Math.max(existing.getRadius(), radius));
		existing.setStrength(existing.getStrength()+Math.max(0, strength));
		index(existing);
		IISaveData.setDirty();
		return existing;
	}

	@Nullable
	public RadiationCenter getRadiationCenter(@Nonnull World world, @Nonnull BlockPos position)
	{
		return getRadiationCenter(world.provider.getDimension(), position);
	}

	@Nullable
	public RadiationCenter getRadiationCenter(int dimension, @Nonnull BlockPos position)
	{
		return centers.get(new RadiationKey(dimension, position));
	}

	public boolean removeRadiationCenter(@Nonnull World world, @Nonnull BlockPos position)
	{
		return removeRadiationCenter(world.provider.getDimension(), position);
	}

	public boolean removeRadiationCenter(int dimension, @Nonnull BlockPos position)
	{
		RadiationCenter removed = centers.remove(new RadiationKey(dimension, position));
		if(removed==null)
			return false;
		unindex(removed);
		IISaveData.setDirty();
		return true;
	}

	public boolean setRadiationStrength(@Nonnull World world, @Nonnull BlockPos position, float strength)
	{
		return setRadiationStrength(world.provider.getDimension(), position, strength);
	}

	public boolean setRadiationStrength(int dimension, @Nonnull BlockPos position, float strength)
	{
		RadiationCenter center = getRadiationCenter(dimension, position);
		if(center==null)
			return false;
		unindex(center);
		center.setStrength(strength);
		index(center);
		IISaveData.setDirty();
		return true;
	}

	public boolean modifyRadiationStrength(@Nonnull World world, @Nonnull BlockPos position, float change)
	{
		return modifyRadiationStrength(world.provider.getDimension(), position, change);
	}

	public boolean modifyRadiationStrength(int dimension, @Nonnull BlockPos position, float change)
	{
		RadiationCenter center = getRadiationCenter(dimension, position);
		return center!=null&&setRadiationStrength(dimension, position, center.getStrength()+change);
	}

	public boolean setRadiationRadius(@Nonnull World world, @Nonnull BlockPos position, float radius)
	{
		return setRadiationRadius(world.provider.getDimension(), position, radius);
	}

	public boolean setRadiationRadius(int dimension, @Nonnull BlockPos position, float radius)
	{
		RadiationCenter center = getRadiationCenter(dimension, position);
		if(center==null)
			return false;
		unindex(center);
		center.setRadius(radius);
		index(center);
		IISaveData.setDirty();
		return true;
	}

	@Nonnull
	public Collection<RadiationCenter> getRadiationCenters()
	{
		return Collections.unmodifiableCollection(centers.values());
	}

	//--- Exposure processing ---//

	public void tick(@Nonnull World world)
	{
		if(world.isRemote||world.getTotalWorldTime()%UPDATE_INTERVAL!=0)
			return;

		rebuildEmitterIndex(world);
		if(IIPotions.radiation==null)
			return;

		for(Entity entity : new ArrayList<>(world.loadedEntityList))
		{
			if(!(entity instanceof EntityLivingBase)||!entity.isEntityAlive())
				continue;
			EntityLivingBase living = (EntityLivingBase)entity;
			if(living instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)living;
				/*if(player.isCreative()||player.isSpectator())
					continue;*/
			}
			if(ProtectionHandler.isProtectedFromRadiation(living))
				continue;

			float exposure = getRadiationAt(world, living.getPositionVector().addVector(0, living.height*0.5, 0));
			if(exposure < MINIMUM_EXPOSURE)
				continue;

			int amplifier = MathHelper.clamp(MathHelper.ceil(exposure)-1, 0, 4);
			living.addPotionEffect(new PotionEffect(IIPotions.radiation, UPDATE_INTERVAL*3, amplifier, false, false));
		}
	}

	/**
	 * Gets the strongest linear fall-off of a radiation source at the specified position.
	 * The result is 1 at the source centre and 0 at its radius edge.
	 *
	 * @param world    the source world
	 * @param position the test position
	 * @return the strongest source proximity in the 0-1 range
	 */
	public float getRadiationProximity(@Nonnull World world, @Nonnull Vec3d position)
	{
		int dimension = world.provider.getDimension();
		long chunk = chunkKey(MathHelper.floor(position.x)>>4, MathHelper.floor(position.z)>>4);
		float proximity = 0;

		Map<Long, List<RadiationCenter>> persistent = centerIndex.get(dimension);
		if(persistent!=null)
		{
			List<RadiationCenter> candidates = persistent.get(chunk);
			if(candidates!=null)
				for(RadiationCenter center : candidates)
				{
					Vec3d source = new Vec3d(center.getPosition()).addVector(0.5, 0.5, 0.5);
					proximity = Math.max(proximity, getLinearFalloff(source, center.getRadius(), position));
				}
		}

		Map<Long, List<EmitterSource>> dynamic;
		if(world.isRemote)
		{
			refreshClientEmitterIndex(world);
			dynamic = clientEmitterIndex;
		}
		else
			dynamic = emitterIndex.get(dimension);
		if(dynamic!=null)
		{
			List<EmitterSource> candidates = dynamic.get(chunk);
			if(candidates!=null)
				for(EmitterSource source : candidates)
					proximity = Math.max(proximity, source.getProximityAt(position));
		}
		return MathHelper.clamp(proximity, 0, 1);
	}

	public float getRadiationAt(@Nonnull World world, @Nonnull BlockPos position)
	{
		return getRadiationAt(world, new Vec3d(position).addVector(0.5, 0.5, 0.5));
	}

	public float getRadiationAt(@Nonnull World world, @Nonnull Vec3d position)
	{
		int dimension = world.provider.getDimension();
		long chunk = chunkKey(MathHelper.floor(position.x)>>4, MathHelper.floor(position.z)>>4);
		float radiation = 0;

		Map<Long, List<RadiationCenter>> persistent = centerIndex.get(dimension);
		if(persistent!=null)
		{
			List<RadiationCenter> candidates = persistent.get(chunk);
			if(candidates!=null)
				for(RadiationCenter center : candidates)
					radiation += center.getRadiationAt(position);
		}

		Map<Long, List<EmitterSource>> dynamic = emitterIndex.get(dimension);
		if(dynamic!=null)
		{
			List<EmitterSource> candidates = dynamic.get(chunk);
			if(candidates!=null)
				for(EmitterSource source : candidates)
					radiation += source.getRadiationAt(position);
		}
		return radiation;
	}

	public void clearEmitterIndex(@Nonnull World world)
	{
		emitterIndex.remove(world.provider.getDimension());
	}

	private void rebuildEmitterIndex(World world)
	{
		Map<Long, List<EmitterSource>> index = new HashMap<>();
		emitterIndex.put(world.provider.getDimension(), index);
		collectEmitters(world, index);
	}

	private void refreshClientEmitterIndex(World world)
	{
		long update = world.getTotalWorldTime()/10;
		if(clientEmitterWorld==world&&clientEmitterUpdate==update)
			return;

		clientEmitterWorld = world;
		clientEmitterUpdate = update;
		clientEmitterIndex.clear();
		collectEmitters(world, clientEmitterIndex);
	}

	private void collectEmitters(World world, Map<Long, List<EmitterSource>> index)
	{
		if(ProtectionCapabilities.RADIATION_EMITTER==null)
			return;

		for(Entity entity : new ArrayList<>(world.loadedEntityList))
			if(entity.isEntityAlive()&&entity.hasCapability(ProtectionCapabilities.RADIATION_EMITTER, null))
				addEmitter(index, entity.getCapability(ProtectionCapabilities.RADIATION_EMITTER, null),
						entity.getPositionVector().addVector(0, entity.height*0.5, 0));

		for(TileEntity tile : new ArrayList<>(world.loadedTileEntityList))
			if(!tile.isInvalid()&&tile.hasCapability(ProtectionCapabilities.RADIATION_EMITTER, null))
				addEmitter(index, tile.getCapability(ProtectionCapabilities.RADIATION_EMITTER, null),
						new Vec3d(tile.getPos()).addVector(0.5, 0.5, 0.5));
	}

	private void addEmitter(Map<Long, List<EmitterSource>> index, @Nullable IRadiationEmitter emitter, Vec3d position)
	{
		if(emitter==null||!emitter.isRadiationActive())
			return;
		float radius = emitter.getRadiationRadius();
		float strength = emitter.getRadiationStrength();
		if(radius <= 0||strength <= 0)
			return;

		EmitterSource source = new EmitterSource(position, radius, strength);
		index(index, position.x, position.z, radius, source);
	}

	//--- Spatial index ---//

	@Nonnull
	private RadiationCenter putRadiationCenter(int dimension, BlockPos position, float radius, float strength, boolean dirty)
	{
		RadiationKey key = new RadiationKey(dimension, position);
		RadiationCenter previous = centers.remove(key);
		if(previous!=null)
			unindex(previous);

		RadiationCenter center = new RadiationCenter(dimension, position, radius, strength);
		centers.put(key, center);
		index(center);
		if(dirty)
			IISaveData.setDirty();
		return center;
	}

	private void index(RadiationCenter center)
	{
		if(center.getRadius() <= 0||center.getStrength() <= 0)
			return;
		Map<Long, List<RadiationCenter>> index = centerIndex.computeIfAbsent(center.getDimension(), ignored -> new HashMap<>());
		index(index, center.getPosition().getX()+0.5, center.getPosition().getZ()+0.5, center.getRadius(), center);
	}

	private void unindex(RadiationCenter center)
	{
		Map<Long, List<RadiationCenter>> index = centerIndex.get(center.getDimension());
		if(index==null)
			return;
		for(List<RadiationCenter> bucket : index.values())
			bucket.remove(center);
		index.values().removeIf(List::isEmpty);
		if(index.isEmpty())
			centerIndex.remove(center.getDimension());
	}

	private static <T> void index(Map<Long, List<T>> index, double x, double z, float radius, T source)
	{
		int minChunkX = MathHelper.floor(x-radius)>>4;
		int maxChunkX = MathHelper.floor(x+radius)>>4;
		int minChunkZ = MathHelper.floor(z-radius)>>4;
		int maxChunkZ = MathHelper.floor(z+radius)>>4;
		for(int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++)
			for(int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++)
				index.computeIfAbsent(chunkKey(chunkX, chunkZ), ignored -> new ArrayList<>()).add(source);
	}

	private static long chunkKey(int x, int z)
	{
		return (x&0xffffffffL)|((z&0xffffffffL)<<32);
	}

	private static float getLinearFalloff(Vec3d source, float radius, Vec3d point)
	{
		if(radius <= 0)
			return 0;
		double distanceSq = source.squareDistanceTo(point);
		double radiusSq = radius*radius;
		if(distanceSq >= radiusSq)
			return 0;
		return 1f-(float)(Math.sqrt(distanceSq)/radius);
	}

	//--- Persistence ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagList list = new NBTTagList();
		for(RadiationCenter center : centers.values())
			list.appendTag(center.serializeNBT());
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("centers", list);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		centers.clear();
		centerIndex.clear();
		emitterIndex.clear();
		clientEmitterIndex.clear();
		clientEmitterWorld = null;
		clientEmitterUpdate = Long.MIN_VALUE;

		NBTTagList list = nbt.getTagList("centers", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++)
		{
			RadiationCenter center = new RadiationCenter();
			center.deserializeNBT(list.getCompoundTagAt(i));
			putRadiationCenter(center.getDimension(), center.getPosition(), center.getRadius(), center.getStrength(), false);
		}
	}

	private static final class RadiationKey
	{
		private final int dimension;
		private final BlockPos position;

		private RadiationKey(int dimension, BlockPos position)
		{
			this.dimension = dimension;
			this.position = position.toImmutable();
		}

		@Override
		public boolean equals(Object object)
		{
			if(this==object)
				return true;
			if(!(object instanceof RadiationKey))
				return false;
			RadiationKey key = (RadiationKey)object;
			return dimension==key.dimension&&position.equals(key.position);
		}

		@Override
		public int hashCode()
		{
			return 31*dimension+position.hashCode();
		}
	}

	private static final class EmitterSource
	{
		private final Vec3d position;
		private final float radius;
		private final float strength;

		private EmitterSource(Vec3d position, float radius, float strength)
		{
			this.position = position;
			this.radius = radius;
			this.strength = strength;
		}

		private float getRadiationAt(Vec3d point)
		{
			return strength*getProximityAt(point);
		}

		private float getProximityAt(Vec3d point)
		{
			return getLinearFalloff(position, radius, point);
		}
	}
}
