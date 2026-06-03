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
import pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon.EntityMortar;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.Optional;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.04.2021
 */
//TODO: 15.02.2024 create a superclass for AIHansHowitzer and AIHansMortar
public class AIHansMortar extends EntityAIBase
{
	// TODO: 31.08.2021 yaw
	private final EntityLiving hans;
	private EntityMortar mortar = null;

	private Optional<Entity> target = Optional.empty();

	public AIHansMortar(EntityLiving hans)
	{
		this.hans = hans;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		if(hans.getRidingEntity() instanceof EntityMortar)
		{
			mortar = ((EntityMortar)hans.getRidingEntity());
		}
		else
		{
			hans.tasks.removeTask(this);
			return false;
		}

		if(mortar==null||mortar.isDead)
		{
			hans.tasks.removeTask(this);
			return false;
		}

		target = Optional.ofNullable(hans.getAttackTarget());
		return true;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		if(target.isPresent())
		{
			Entity t = target.get();
			this.hans.faceEntity(t, 10, 10);

			float pp = getAnglePrediction(mortar.getPositionVector().addVector(0, 1, 0), t.getPositionVector(), new Vec3d(t.motionX, t.motionY, t.motionZ))[1];

			boolean pitchUp = (90+mortar.rotationPitch)-pp < 0;
			boolean pitchDown = (90+mortar.rotationPitch)-pp > 0;
			if(mortar.controls!=null)
			{
				mortar.controls.setKey("pitchUp", pitchUp);
				mortar.controls.setKey("pitchDown", pitchDown);
			}
			if(Math.abs((90+mortar.rotationPitch)-pp) <= 5)
			{
				if(hans.getHeldItemMainhand().isEmpty())
				{
					ItemStack shell = IIContent.itemAmmoMortar.getAmmoStack(IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentTracerPowder, IIContent.ammoComponentWhitePhosphorus);
					IIContent.itemAmmoMortar.setComponentNBT(shell, EasyNBT.parseNBT("{colour: %s}", IIColor.MC_DARK_RED));

					hans.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, shell);
				}

				if(mortar.controls!=null)
				{
					if(mortar.shootingProgress==0)
						mortar.controls.setKey("fire", true);
					if(mortar.controls.getKey("fire"))
						mortar.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, mortar.getEntityBoundingBox()).forEach(Entity::setDead);
				}
			}
		}
	}

	public boolean isAimedAt(float yaw, float pitch)
	{
		return pitch==mortar.rotationPitch;
	}

	//TODO: 15.02.2024 revisit
	public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		Vec3d dist = posTurret.subtract(posTarget.add(motion));
		Vec3d norm = dist.normalize();

		float yy = (float)((Math.atan2(norm.x, norm.z)*180D)/3.1415927410125732D);
		float pp = Math.round(IIAmmoUtils.calculateBallisticAngle(posTurret, posTarget.add(motion), hans.getHeldItemMainhand(), 0.01f));

		return new float[]{MathHelper.wrapDegrees(180-yy), 90-pp};
	}
}
