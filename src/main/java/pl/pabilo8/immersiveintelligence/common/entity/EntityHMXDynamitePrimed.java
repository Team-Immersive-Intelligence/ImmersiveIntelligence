package pl.pabilo8.immersiveintelligence.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityHMXDynamitePrimed extends Entity
{
	private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(EntityHMXDynamitePrimed.class, DataSerializers.VARINT);
	private int fuse;
	private EntityLivingBase placedBy;

	public EntityHMXDynamitePrimed(World worldIn) {
		super(worldIn);
		this.fuse = 0; // Match TNT fuse time (80 ticks or 4 seconds)
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
	}

	public EntityHMXDynamitePrimed(World worldIn, double x, double y, double z, @Nullable EntityLivingBase igniter) {
		this(worldIn);
		this.setPosition(x, y, z);
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.placedBy = igniter;
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(FUSE, 80); // TNT fuse time is 80 ticks
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setShort("Fuse", (short) this.getFuse());
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.setFuse(compound.getShort("Fuse"));
	}

	@Override
	public void onUpdate() {
		if (!this.hasNoGravity()) {
			this.motionY -= 0.04; // Apply gravity
		}

		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.98;
		this.motionY *= 0.98;
		this.motionZ *= 0.98;

		if (this.onGround) {
			this.motionX *= 0.7;
			this.motionZ *= 0.7;
			this.motionY *= -0.5;
		}

		this.fuse--;

		// TNT-style smoke particles and sound when ignited
		if (this.fuse <= 0) {
			this.setDead();

			if (!this.world.isRemote) {
				this.explode();
			}
		} else {
			this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0);
		}

		if (this.fuse == 1 && !this.world.isRemote) {
			this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}

	private void explode() {
		float explosionStrength = 6.0F; // TNT explosion strength (4.0F)
		Explosion explosion = this.world.createExplosion(this, this.posX, this.posY, this.posZ, explosionStrength, true);
	}

	@Nullable
	public EntityLivingBase getPlacedBy() {
		return this.placedBy;
	}

	public int getFuse() {
		return this.fuse;
	}

	public void setFuse(int fuse) {
		this.dataManager.set(FUSE, fuse);
		this.fuse = fuse;
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (FUSE.equals(key)) {
			this.fuse = this.dataManager.get(FUSE);
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}
}
