package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageEntityNBTSync;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 05.04.2021
 */
public class AIHansMachinegun extends EntityAIBase
{
	private final EntityLiving hans;
	private EntityMachinegun mg;

	@Nullable
	private Entity target = null;

	public AIHansMachinegun(EntityLiving hans)
	{
		this.hans = hans;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		if(hans.getRidingEntity() instanceof EntityMachinegun)
		{
			mg = ((EntityMachinegun)hans.getRidingEntity());
		}
		else
		{
			hans.tasks.removeTask(this);
			return false;
		}

		if(mg==null||mg.isDead)
		{
			hans.tasks.removeTask(this);
			return false;
		}

		return true;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		target = hans.getAttackTarget();
		if(mg!=null)
		{
			mg.shoot = false;

			NBTTagCompound update = new NBTTagCompound();
			mg.writeEntityToNBT(update);
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageEntityNBTSync(mg, update), Utils.targetPointFromEntity(mg, 24));

			if(mg.magazine1.isEmpty())
			{
				if(hans.getHeldItemMainhand().isEmpty())
				{

					ItemStack magazine = ItemIIBulletMagazine.getMagazine("machinegun", IIContent.itemAmmoSubmachinegun.getBulletWithParams("core_brass", "piercing", "tracer_powder"));
					NBTTagCompound tag = new NBTTagCompound();
					tag.setInteger("colour", 0xff0000);
					IIContent.itemAmmoMachinegun.setComponentNBT(magazine, tag);
					hans.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, magazine);
				}
			}
			else if(target!=null)
			{
				hans.getLookHelper().setLookPositionWithEntity(target, hans.getHorizontalFaceSpeed(), hans.getVerticalFaceSpeed());
				if(isAimedAt())
				{
					mg.shoot = true;
				}
			}

		}
	}

	public boolean isAimedAt()
	{
		return MathHelper.wrapDegrees(hans.rotationPitch)-mg.gunPitch < 5&&MathHelper.wrapDegrees(hans.rotationYawHead)-MathHelper.wrapDegrees(mg.rotationYaw) < 5;
	}

	public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		Vec3d vv = posTurret.subtract(posTarget);
		float motionXZ = MathHelper.sqrt(vv.x*vv.x+vv.z*vv.z);
		Vec3d motionVec = new Vec3d(motion.x, motion.y, motion.z).scale(2f).addVector(0, 0, 0f);
		vv = vv.add(motionVec).normalize();
		float yy = (float)((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
		float pp = Math.round(Utils.calculateBallisticAngle(
				posTurret.distanceTo(posTarget.add(motion))+10
				, (posTarget.y+motion.y)-posTurret.y,
				IIContent.itemAmmoMachinegun.getDefaultVelocity(),
				EntityBullet.GRAVITY*0.2f,
				1f-EntityBullet.DRAG, 0.01));

		return new float[]{MathHelper.wrapDegrees(180-yy), 90-pp};
	}
}
