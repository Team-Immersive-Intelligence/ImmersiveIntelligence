package pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.hand_weapon;

import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.Config.IEConfig.Tools;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.entities.EntityChemthrowerShot;
import blusunrize.immersiveengineering.common.items.ItemChemthrower;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.tasks.AIHansBase;

import java.util.List;

/**
 * @author Pabilo8
 * @since 23.04.2021
 */
public class AIHansChemthrower extends AIHansBase
{
	private EntityLivingBase attackTarget;
	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	private int rangedAttackTime;
	private final double entityMoveSpeed;
	private int seeTime;
	private final int burstTime;
	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	private final float maxAttackDistance;

	private final static ItemChemthrower CHEM = (ItemChemthrower)IEContent.itemChemthrower;

	public AIHansChemthrower(EntityHans hans, double movespeed, int burstTime, float maxAttackDistanceIn)
	{
		super(hans);
		this.rangedAttackTime = -1;
		this.entityMoveSpeed = movespeed;
		this.maxAttackDistance = maxAttackDistanceIn*maxAttackDistanceIn;
		this.burstTime = burstTime;
		this.setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		EntityLivingBase entitylivingbase = this.hans.getAttackTarget();

		if(entitylivingbase==null)
		{
			return false;
		}
		else
		{
			this.attackTarget = entitylivingbase;
			ItemStack chemthrower = hans.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
			return chemthrower.getItem() instanceof ItemChemthrower&&hasAmmo(chemthrower);
		}
	}

	private boolean hasAmmo(ItemStack chemthrower)
	{
		if(chemthrower.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
		{
			IFluidHandlerItem capability = chemthrower.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if(capability==null)
				return false;
			FluidStack drain = capability.drain(Tools.chemthrower_consumption, false);
			return drain!=null&&drain.amount==Tools.chemthrower_consumption;
		}
		return false;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting()
	{
		return this.shouldExecute()||!this.hans.getNavigator().noPath()||!(this.attackTarget==null||hans.getPositionVector().distanceTo(attackTarget.getPositionVector()) < EntityHans.MELEE_DISTANCE);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask()
	{
		if(attackTarget!=null)
			hans.setSneaking(false);

		this.attackTarget = null;
		this.seeTime = 0;
		this.rangedAttackTime = -1;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		if(attackTarget==null)
			return;
		if(attackTarget.isDead)
		{
			hans.setSneaking(false);
			resetTask();
			return;
		}
		double d0 = this.hans.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
		boolean flag = this.hans.getEntitySenses().canSee(this.attackTarget)&&canShootEntity(this.attackTarget);

		if(flag)
		{
			++this.seeTime;
		}
		else
		{
			this.seeTime = 0;
		}

		if(d0 <= (double)this.maxAttackDistance&&this.seeTime >= 20)
		{
			this.hans.getNavigator().clearPath();
		}
		else
		{
			this.hans.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
		}

		//this.hans.getPositionVector().add(this.hans.getLookVec()).subtract();
		final Vec3d add = this.attackTarget.getPositionVector().add(this.attackTarget.getLookVec());
		this.hans.getLookHelper().setLookPosition(add.x, add.y, add.z, 30, 30);

		if(!flag)
		{
			return;
		}

		// TODO: 19.05.2021 fluid backpacks

		final ItemStack chemthrower = hans.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

		if(this.rangedAttackTime < 0)
		{
			hans.resetActiveHand();
			rangedAttackTime += 1;
		}
		if(this.rangedAttackTime < burstTime)
		{
			if(chemthrower.getItem() instanceof ItemChemthrower)
			{
				if(hasAmmo(chemthrower))
				{
					hans.hasAmmo = true;
					hans.faceEntity(attackTarget, 30, 0);
					hans.rotationPitch -= 25;
					ItemNBTHelper.setBoolean(chemthrower, "ignite", true);
					onUsingTick(chemthrower, this.hans, IEContent.itemChemthrower.getMaxItemUseDuration(chemthrower)-rangedAttackTime);
					hans.rotationPitch += 25;
				}
				CHEM.onUpdate(chemthrower, this.hans.world, this.hans, 0, true);

				if(rangedAttackTime >= burstTime)
				{
					// TODO: 19.05.2021 add balistic calculation
					CHEM.onPlayerStoppedUsing(chemthrower, this.hans.world, this.hans, CHEM.getMaxItemUseDuration(chemthrower)-rangedAttackTime);
					rangedAttackTime = -10;
				}
			}
		}
	}

	@Override
	public void setRequiredAnimation()
	{

	}

	//for the wanker who decided it's safe to cast EntityLivingBase to EntityPlayer without checking, imagine there are other entities in the game
	public void onUsingTick(ItemStack stack, EntityLivingBase living, int count)
	{
		FluidStack fs = CHEM.getFluid(stack);
		if(fs!=null&&fs.getFluid()!=null)
		{
			int duration = CHEM.getMaxItemUseDuration(stack)-count;
			int consumed = IEConfig.Tools.chemthrower_consumption;
			if(consumed*duration <= fs.amount)
			{
				Vec3d v = living.getLookVec();
				int split = 8;
				boolean isGas = fs.getFluid().isGaseous()||ChemthrowerHandler.isGas(fs.getFluid());

				float scatter = isGas?.15f: .05f;
				float range = isGas?.5f: 1f;
				if(CHEM.getUpgrades(stack).getBoolean("focus"))
				{
					range += .25f;
					scatter -= .025f;
				}

				Vec3d look = living.getPositionEyes(0).add(living.getLookVec());
				boolean ignite = ChemthrowerHandler.isFlammable(fs.getFluid())&&ItemNBTHelper.getBoolean(stack, "ignite");
				for(int i = 0; i < split; i++)
				{
					Vec3d vecDir = v.addVector(living.getRNG().nextGaussian()*scatter, living.getRNG().nextGaussian()*scatter, living.getRNG().nextGaussian()*scatter);
					EntityChemthrowerShot chem = new EntityChemthrowerShot(living.world, look.x, look.y, look.z, vecDir.x*0.25, vecDir.y*0.25, vecDir.z*0.25, fs);

					// Apply momentum from the player.
					chem.motionX = living.motionX+vecDir.x*range;
					chem.motionY = living.motionY+vecDir.y*range;
					chem.motionZ = living.motionZ+vecDir.z*range;

					// Apply a small amount of backforce.
					if(!living.onGround)
					{
						living.motionX -= vecDir.x*0.0025*range;
						living.motionY -= vecDir.y*0.0025*range;
						living.motionZ -= vecDir.z*0.0025*range;
					}
					if(ignite)
						chem.setFire(10);

					if(!living.world.isRemote)
						living.world.spawnEntity(chem);
				}
				if(count%4==0)
				{
					if(ignite)
						living.world.playSound(null, living.posX, living.posY, living.posZ, IESounds.sprayFire, SoundCategory.PLAYERS, .5f, 1.5f);
					else
						living.world.playSound(null, living.posX, living.posY, living.posZ, IESounds.spray, SoundCategory.PLAYERS, .5f, .75f);
				}
			}
			else
				living.stopActiveHand();
		}
		else
			living.stopActiveHand();
	}
}