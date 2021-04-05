package pl.pabilo8.immersiveintelligence.common.entity.hans_tasks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Tools.TripodPeriscope;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.EntityVehicleSeat;

import java.util.Optional;

/**
 * @author Pabilo8
 * @since 05.04.2021
 */
public class AIHansHowitzer extends EntityAIBase
{
	private final EntityFieldHowitzer howitzer;
	private final int seat;
	private Optional<Entity> target = Optional.empty();

	public AIHansHowitzer(EntityFieldHowitzer howitzer, int seat)
	{
		this.howitzer = howitzer;
		this.seat = seat;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		target = this.howitzer.world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(howitzer.getPosition()).offset(-0.5, 0, -0.5).grow(40f).expand(0, 40, 0),
				input -> input instanceof EntityMob&&
						((EntityMob)input).getHealth() > 0).stream().sorted((o1, o2) -> (int)((o1.width*o1.height)-(o2.width*o2.height))*10).findFirst();
		return true;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{

			if(seat==0)
			{
				if(!(howitzer.turnLeft||howitzer.turnRight||howitzer.forward||howitzer.backward)&&target.isPresent())
				{
					howitzer.gunPitchUp=howitzer.gunPitch-45<0;
					howitzer.gunPitchDown=howitzer.gunPitch-45>0;
					if(howitzer.gunPitch==45)
					{
						if(howitzer.shell.isEmpty())
						{
							Entity entity = EntityVehicleSeat.getOrCreateSeat(howitzer, 0).getPassengers().get(0);

							//it should be
							if(entity instanceof EntityHans&&((EntityHans)entity).getHeldItemMainhand().isEmpty())
							{
								ItemStack shell = IIContent.itemAmmoLightArtillery.getBulletWithParams("core_brass", "canister", "hmx","tracer_powder");
								NBTTagCompound tag = new NBTTagCompound();
								tag.setInteger("colour", 0xff0000);
								IIContent.itemAmmoMachinegun.setComponentNBT(shell, new NBTTagCompound(), tag);
								entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND,shell);
							}
							else if(howitzer.reloadProgress==0&&!howitzer.reloadKeyPress)
								howitzer.reloadKeyPress=true;
						}
						else
						{
							if(howitzer.shootingProgress==0&&!howitzer.fireKeyPress)
								howitzer.fireKeyPress=true;
							else
								howitzer.fireKeyPress=false;
						}
						if(howitzer.fireKeyPress)
							howitzer.getWorld().getEntitiesWithinAABB(EntityItem.class,howitzer.getEntityBoundingBox()).forEach(Entity::setDead);
					}
				}
			}
			else
			{

				if(target.isPresent())
				{
					Entity entity = target.get();
					float[] yp = getAnglePrediction(howitzer.getPositionVector(),entity.getPositionVector(),new Vec3d(entity.motionX,entity.motionY,entity.motionZ));
					if(!isAimedAt(yp[0],yp[1]))
					{
						float y =  MathHelper.wrapDegrees(360+yp[0]-this.howitzer.rotationYaw);
						howitzer.turnRight=Math.signum(y)>0;
						howitzer.turnLeft=Math.signum(y)<0;

						if(Math.abs(y) < 5)
						{
							howitzer.turnRight=false;
							howitzer.turnLeft=false;
							howitzer.rotationYaw=MathHelper.wrapDegrees(yp[0]);
							howitzer.prevRotationYaw=MathHelper.wrapDegrees(yp[0]);
						}
					}
					else
					{
						howitzer.turnRight=false;
						howitzer.turnLeft=false;
					}
				}
				else
				{
					howitzer.turnRight=false;
					howitzer.turnLeft=false;
				}
			}
	}

	public boolean isAimedAt(float yaw, float pitch)
	{
		return pitch==howitzer.gunPitch&&MathHelper.wrapDegrees(yaw)==howitzer.rotationYaw;
	}

	public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		Vec3d vv = posTurret.subtract(posTarget);
		float motionXZ = MathHelper.sqrt(vv.x*vv.x+vv.z*vv.z);
		Vec3d motionVec = new Vec3d(motion.x, motion.y, motion.z).scale(2f).addVector(0, 0, 0f);
		vv = vv.add(motionVec).normalize();
		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
		float pp = (float)((Math.atan2(vv.y, motionXZ)*180D));
		pp = MathHelper.clamp(pp, 0, 90);

		return new float[]{MathHelper.wrapDegrees(180-yy), pp};
	}

	/**
	 * Rotate the gun
	 *
	 * @param yaw   destination
	 * @param pitch destination
	 */
	public void aimAt(float yaw, float pitch)
	{
		/*
		float nextPitch = pitch;
		float nextYaw = MathHelper.wrapDegrees(yaw);
		float p = pitch-howitzer.gun;
		howitzer.pitch += Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, this.getPitchTurnSpeed());
		float y = MathHelper.wrapDegrees(360+nextYaw-this.yaw);

		if(Math.abs(p) < this.getPitchTurnSpeed()*0.5f)
			this.pitch = this.nextPitch;
		if(Math.abs(y) < this.getYawTurnSpeed()*0.5f)
			this.yaw = this.nextYaw;
		else
			this.yaw = MathHelper.wrapDegrees(this.yaw+(Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, this.getYawTurnSpeed())));

		this.pitch = this.pitch%180;
		 */
	}
}
