package pl.pabilo8.immersiveintelligence.common.entity.ammo.types;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import javax.vecmath.Vector2f;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 02.02.2024
 */
public class EntityAmmoMissile extends EntityAmmoProjectile
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
	@SideOnly(Side.CLIENT)
	protected void spawnTrailParticles()
	{
		//Missile jet particles
		ParticleRegistry.spawnParticle("ammo/rocket_smoke", getPositionVector(),
						IIEntityUtils.getEntityMotion(this).scale(-2),
						new Vector2f((float)Math.toRadians(rotationYaw), (float)Math.toRadians(rotationPitch-90)))
				.withProperty(ParticleProperties.SIZE, ammoType.getCaliber()/12f);
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
