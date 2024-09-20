package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageEntityNBTSync;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

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
			mg = ((EntityMachinegun)hans.getRidingEntity());
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
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageEntityNBTSync(mg, update), IIPacketHandler.targetPointFromEntity(mg, 24));

			if(mg.magazine1.isEmpty())
			{
				if(hans.getHeldItemMainhand().isEmpty())
				{

					ItemStack ammoStack = IIContent.itemAmmoMachinegun.getAmmoStack(IIContent.ammoCoreBrass, CoreType.PIERCING, FuseType.CONTACT, IIContent.ammoComponentTracerPowder);
					IIContent.itemAmmoMachinegun.setComponentNBT(ammoStack, EasyNBT.parseNBT("{colour: %s}", IIColor.MC_DARK_RED));
					ItemStack magazine = IIContent.itemBulletMagazine.getMagazine(Magazines.MACHINEGUN, ammoStack);


					hans.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, magazine);
				}
			}
			else if(target!=null)
			{
				hans.getLookHelper().setLookPositionWithEntity(target, hans.getHorizontalFaceSpeed(), hans.getVerticalFaceSpeed());
				if(isAimedAt())
					mg.shoot = true;
			}

		}
	}

	public boolean isAimedAt()
	{
		//TODO: 15.02.2024 use new calculation method
		return MathHelper.wrapDegrees(hans.rotationPitch)-mg.gunPitch < 5&&MathHelper.wrapDegrees(hans.rotationYawHead)-MathHelper.wrapDegrees(mg.rotationYaw) < 5;
	}
}
