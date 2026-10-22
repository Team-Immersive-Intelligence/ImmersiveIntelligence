package pl.pabilo8.immersiveintelligence.common.entity.ammo.types;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageParticleEffect;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.vecmath.Vector2f;

/**
 * Keeps a missile loaded and applies its booster physics.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.10.2026
 * @ii-approved 0.3.1
 * @since 02.02.2024
 */
public class EntityAmmoMissile extends EntityAmmoChunkLoadingProjectile
{
	public static final int BOOSTER_TIME = 100;
	@SyncNBT(events = SyncEvents.ENTITY_COLLISION)
	public int fuelRemaining = BOOSTER_TIME;

	public EntityAmmoMissile(World world)
	{
		super(world);
	}

	@Override
	protected void updatePhysics()
	{
		//Gravity suppressed by missile jet
		if(fuelRemaining > 0)
			fuelRemaining--;
		else
			super.updatePhysics();
	}

	/**
	 * @return whether the missile jet is still suppressing gravity
	 */
	public boolean isBoosterActive()
	{
		return fuelRemaining > 0;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		if(compound.hasKey("fuel_remaining"))
			this.fuelRemaining = Math.max(0, EasyNBT.wrapNBT(compound).getInt("fuel_remaining"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		EasyNBT.wrapNBT(compound).withInt("fuel_remaining", fuelRemaining);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		//Missile jet particles
		if(!world.isRemote&&!isDead&&isBoosterActive())
			IIPacketHandler.sendToClient(new MessageParticleEffect("ammo/rocket_smoke", world, getPositionVector(), Vec3d.ZERO, new Vector2f(),
					EasyNBT.newNBT().withVec3d("stretch", new Vec3d(prevPosX, prevPosY, prevPosZ)))
			);
	}

	@Override
	protected void onHitRicochet(RayTraceResult hit, IPenetrationHandler handler)
	{
		//Set the position to the hit position
		this.posX = hit.hitVec.x;
		this.posY = hit.hitVec.y;
		this.posZ = hit.hitVec.z;

		detonate();
	}


}
