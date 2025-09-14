package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.ITypeNBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.02.2024
 */
public abstract class EmplacementWeapon implements ITypeNBTSerializable<NBTTagCompound>
{
	/**
	 * Acts as a hitbox container for the weapon
	 */
	public EntityEmplacementWeapon entity = null;
	public float pitch = 0;
	public float yaw = 0;
	public int health = 0;
	protected float nextPitch = 0, nextYaw = 0;

	/**
	 * @return name of the emplacement, must be the same as the name in the weapon registry
	 */
	public abstract String getName();

	public float getYawTurnSpeed()
	{
		return 2;
	}

	public float getPitchTurnSpeed()
	{
		return 2;
	}

	/**
	 * @param yaw   destination
	 * @param pitch destination
	 * @return whether the gun is pointing to the pitch and yaw given
	 */
	public boolean isAimedAt(float yaw, float pitch)
	{
		return pitch==this.pitch&&MathHelper.wrapDegrees(yaw)==MathHelper.wrapDegrees(this.yaw);
	}

	/**
	 * Calculates final aiming angle of the weapon
	 *
	 * @param posTurret of the emplacement
	 * @param posTarget to be attacked
	 * @param motion    for moving entities, {@link Vec3d#ZERO} for other cases
	 * @return the final aiming angle
	 */
	public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		Vec3d vv = posTurret.subtract(posTarget).add(motion).normalize();
		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
		float pp = (float)Math.toDegrees((Math.atan2(vv.y, vv.distanceTo(new Vec3d(0, vv.y, 0)))));

		return new float[]{yy, pp};
	}

	/**
	 * Rotate the gun
	 *
	 * @param yaw   destination
	 * @param pitch destination
	 */
	public void aimAt(float yaw, float pitch)
	{
		if(Float.isNaN(this.pitch))
			this.pitch = 0;
		if(Float.isNaN(this.yaw))
			this.yaw = 0;

		nextPitch = pitch;
		nextYaw = MathHelper.wrapDegrees(yaw);
		float p = pitch-this.pitch;
		this.pitch += Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, this.getPitchTurnSpeed());
		float y = MathHelper.wrapDegrees(360+nextYaw-this.yaw);

		if(Math.abs(p) < this.getPitchTurnSpeed()*0.5f)
			this.pitch = this.nextPitch;
		if(Math.abs(y) < this.getYawTurnSpeed()*0.5f)
			this.yaw = this.nextYaw;
		else
			this.yaw = MathHelper.wrapDegrees(this.yaw+(Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, this.getYawTurnSpeed())));

		this.pitch = this.pitch%180;
	}

	/**
	 * @param door whether the emplacement door is open
	 * @return whether turret is setup
	 */
	public abstract boolean isSetUp(boolean door);

	/**
	 * Used for turret setup
	 *
	 * @param door whether the emplacement door is open
	 */
	public void doSetUp(boolean door)
	{

	}

	public abstract boolean requiresPlatformRefill();

	/**
	 * Called after the weapon is installed or loaded from NBT
	 * Initialize sight AABB here
	 */
	public void init(TileEntityEmplacement te, boolean firstTime)
	{
		//Default action, in most cases sending "I'm attacking xyz" signals is unnecessary
		//Exception is the IR Observer, which overrides this method
		if(firstTime)
		{
			this.health = getMaxHealth();
			this.nextPitch = this.pitch = -90;
			this.nextYaw = this.yaw = te.facing.getHorizontalAngle();
			te.sendAttackSignal = false;


			if(!te.getWorld().isRemote)
			{
				//Spawn weapon entity for fancy hitboxes
				Vec3d vv = te.getWeaponCenter().subtract(0, 1, 0);
				entity = new EntityEmplacementWeapon(te.getWorld());
				entity.setPosition(vv.x, vv.y, vv.z);
				te.getWorld().spawnEntity(entity);
			}
		}
	}

	/**
	 * Used for reloading and other actions
	 * For setup delay use {@link #doSetUp(boolean)}
	 *
	 * @param te     the emplacement tile entity
	 * @param active whether the weapon is active
	 */
	public void tick(TileEntityEmplacement te, boolean active)
	{

	}

	/**
	 * Used for shooting action
	 */
	public void shoot(TileEntityEmplacement te)
	{
		if(entity!=null)
			blusunrize.immersiveengineering.common.util.Utils.attractEnemies(entity, 24);
	}

	/**
	 * @param forClient whether the NBT tag is for client or server
	 * @return nbt tag with weapon's saved data
	 */
	@Nonnull
	public NBTTagCompound saveToNBT(boolean forClient)
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setFloat("yaw", yaw);
		tag.setFloat("pitch", pitch);
		tag.setInteger("health", health);
		return tag;
	}

	/**
	 * Reads from NBT tag
	 *
	 * @param tagCompound provided nbt tag (saved in tile's NBT tag "emplacement")
	 */
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		yaw = tagCompound.getFloat("yaw");
		pitch = tagCompound.getFloat("pitch");
		health = tagCompound.hasKey("health")?tagCompound.getInteger("health"): getMaxHealth();
	}

	public void syncWithClient(TileEntityEmplacement te)
	{
		if(!te.getWorld().isRemote)
			IIPacketHandler.sendToClient(te, new MessageIITileSync(te, EasyNBT.newNBT()
					.withString("weaponName", getName())
					.withTag("currentWeapon", saveToNBT(true))
			));
	}

	public abstract boolean canShoot(TileEntityEmplacement te);

	@Nullable
	public IFluidHandler getFluidHandler(boolean in)
	{
		return null;
	}

	@Nullable
	public IItemHandler getItemHandler(boolean in)
	{
		return null;
	}

	public void handleDataPacket(DataPacket packet)
	{

	}

	@Nonnull
	public abstract AxisAlignedBB getVisionAABB();

	public boolean canSeeEntity(Entity entity)
	{
		return !entity.isInvisible();
	}

	public void syncWithEntity(EntityEmplacementWeapon entity)
	{
		if(this.entity==null)
			this.entity = entity;
		if(this.entity!=entity)
			return;

		entity.rotationYaw = yaw;
		entity.rotationPitch = pitch;
		entity.setHealth(health);
		entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getMaxHealth());

	}

	public abstract EmplacementHitboxEntity[] getCollisionBoxes();

	public void syncWeaponHealth(TileEntityEmplacement te)
	{
		IIPacketHandler.sendToClient(te, new MessageIITileSync(te, EasyNBT.newNBT().withInt("health", health)));
	}

	public abstract NonNullList<ItemStack> getBaseInventory();

	@SideOnly(Side.CLIENT)
	public abstract void renderStorageInventory(GuiEmplacementPageStorage gui, int mx, int my, float partialTicks, boolean first);

	public abstract void performPlatformRefill(TileEntityEmplacement te);

	public abstract int getEnergyUpkeepCost();

	//--- Damage ---//

	public abstract int getMaxHealth();

	public int getHealth()
	{
		return health;
	}

	public void applyDamage(float damage)
	{
		this.health -= damage;

	}

	public boolean isDead()
	{
		return health <= 0;
	}

	//--- Graphics ---//

	@SideOnly(Side.CLIENT)
	public void spawnDebrisExplosion(TileEntityEmplacement te)
	{
		/*double true_angle = Math.toRadians((-yaw) > 180?360f-(-yaw): (-yaw));
		double true_angle2 = Math.toRadians((-yaw-90) > 180?360f-(-yaw-90): (-yaw-90));
		Random rand = new Random(431L);

		Tuple<ResourceLocation, List<ModelRendererTurbo>> stuff = getDebris();
		List<ModelRendererTurbo> models = stuff.getSecond();
		ResourceLocation texture = stuff.getFirst();

		Vec3d weaponCenter = te.getWeaponCenter();
		ParticleRegistry.spawnExplosionBoomFX(entity.world, weaponCenter, null, new IIExplosion(
				entity.world, null, weaponCenter, Vec3d.ZERO, 5, 4,
				ComponentEffectShape.ORB, true, true, false
		));

		for(ModelRendererTurbo mod : models)
		{
			Vec3d vx = IIMath.offsetPosDirection((float)(mod.rotationPointX*0.0625), true_angle, 0);
			Vec3d vz = IIMath.offsetPosDirection((float)(-mod.rotationPointZ*0.0625), true_angle+90, 0);
			Vec3d vo = weaponCenter
					.add(vx)
					.add(IIMath.offsetPosDirection((float)(mod.rotationPointY*0.0625), -true_angle2, 0))
					.add(vz);
			Vec3d vecDir = new Vec3d(rand.nextGaussian()*0.075, rand.nextGaussian()*0.15, rand.nextGaussian()*0.075);

			ParticleRegistry.spawnTMTModelFX(vo, vx.add(vz).addVector(0, 0.25+vecDir.y, 0).scale(0.66), 0.0625f, mod, texture);
		}*/
	}

	//--- NBT ---//


	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{

	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return null;
	}
}
