package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author Pabilo8
 * @since 05.04.2021
 */
//TODO: 15.02.2024 create a superclass for AIHansHowitzer and AIHansMortar
public class AIHansHowitzer extends EntityAIBase
{
	private final EntityLiving hans;
	private EntityFieldHowitzer howitzer = null;
	private int seat = 0;

	private Optional<Entity> target = Optional.empty();

	public AIHansHowitzer(EntityLiving hans)
	{
		this.hans = hans;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		if(hans.getRidingEntity() instanceof EntityVehicleSeat&&hans.getRidingEntity().getRidingEntity() instanceof EntityFieldHowitzer)
		{
			seat = ((EntityVehicleSeat)hans.getRidingEntity()).seatID;
			howitzer = ((EntityFieldHowitzer)hans.getRidingEntity().getRidingEntity());
		}
		else
		{
			hans.tasks.removeTask(this);
			return false;
		}

		if(howitzer==null||howitzer.isDead)
		{
			hans.tasks.removeTask(this);
			return false;
		}

		if(seat==0)
			target = Optional.ofNullable(hans.getAttackTarget());
		else
		{
			List<Entity> passengers = EntityVehicleSeat.getOrCreateSeat(howitzer, 0).getPassengers();
			if(passengers.size() > 0&&passengers.get(0) instanceof EntityLiving)
			{
				EntityLiving entityLiving = (EntityLiving)passengers.get(0);
				target = Optional.ofNullable(entityLiving.getAttackTarget());
			}
		}
		return true;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		Vec3d positionVector = howitzer.getPositionVector().addVector(0, 1, 0);
		if(seat==0)
		{
			if(!(howitzer.turnLeft||howitzer.turnRight||howitzer.forward||howitzer.backward)&&target.isPresent())
			{
				Entity t = target.get();
				this.hans.faceEntity(t, 10, 10);
				float pp;

				if(positionVector.distanceTo(t.getPositionVector()) > 40)
					pp = getAnglePrediction(positionVector, IIEntityUtils.getEntityCenter(t), new Vec3d(t.motionX, t.motionY, t.motionZ))[1];
				else
					pp = IIAmmoUtils.getDirectFireAngle(IIContent.itemAmmoLightArtillery.getVelocity(), 3.4f, positionVector.subtract(t.getPositionVector()));

				howitzer.gunPitchUp = howitzer.gunPitch-pp < 0;
				howitzer.gunPitchDown = howitzer.gunPitch-pp > 0;
				if(Math.abs(howitzer.gunPitch-pp) < 5)
				{
					if(howitzer.shell.isEmpty())
					{
						Entity entity = EntityVehicleSeat.getOrCreateSeat(howitzer, 0).getPassengers().get(0);

						//it should be
						if(entity instanceof EntityHans&&((EntityHans)entity).getHeldItemMainhand().isEmpty())
						{
							ItemStack shell = IIContent.itemAmmoLightArtillery.getAmmoStack(IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentTracerPowder, IIContent.ammoComponentHMX);
							IIContent.itemAmmoLightArtillery.setComponentNBT(shell, EasyNBT.parseNBT("{colour: %s}", IIColor.MC_DARK_RED));
							entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, shell);
						}
						else if(howitzer.reloadProgress==0&&!howitzer.reloadKeyPress)
							howitzer.reloadKeyPress = true;
					}
					else
					{
						howitzer.fireKeyPress = howitzer.shootingProgress==0&&!howitzer.fireKeyPress;
					}
					if(howitzer.fireKeyPress)
						howitzer.getWorld().getEntitiesWithinAABB(EntityItem.class, howitzer.getEntityBoundingBox()).forEach(Entity::setDead);
				}
			}
		}
		else
		{

			if(target.isPresent())
			{
				Entity entity = target.get();
				this.hans.faceEntity(entity, 10, 10);

				float[] yp = getAnglePrediction(positionVector.addVector(0, 1, 0), IIEntityUtils.getEntityCenter(entity), new Vec3d(entity.motionX, entity.motionY, entity.motionZ));
				if(!isAimedAt(yp[0], yp[1]))
				{
					float y = MathHelper.wrapDegrees(360+yp[0]-this.howitzer.rotationYaw);
					howitzer.turnRight = Math.signum(y) > 0;
					howitzer.turnLeft = Math.signum(y) < 0;

					if(Math.abs(y) < 5)
					{
						howitzer.turnRight = false;
						howitzer.turnLeft = false;
						howitzer.rotationYaw = MathHelper.wrapDegrees(yp[0]);
						howitzer.prevRotationYaw = MathHelper.wrapDegrees(yp[0]);
					}
				}
				else
				{
					howitzer.turnRight = false;
					howitzer.turnLeft = false;
				}
			}
			else
			{
				howitzer.turnRight = false;
				howitzer.turnLeft = false;
			}
		}
	}

	public boolean isAimedAt(float yaw, float pitch)
	{
		return pitch==howitzer.gunPitch&&MathHelper.wrapDegrees(yaw)==howitzer.rotationYaw;
	}

	public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		Vec3d dist = posTurret.subtract(posTarget.add(motion));
		Vec3d norm = dist.normalize();

		float yy = (float)((Math.atan2(norm.x, norm.z)*180D)/3.1415927410125732D);
		float pp = Math.round(IIAmmoUtils.calculateBallisticAngle(posTurret, posTarget.add(motion), hans.getHeldItemMainhand(), 0.01f));

		return new float[]{MathHelper.wrapDegrees(180-yy), 90-pp};
	}
}
