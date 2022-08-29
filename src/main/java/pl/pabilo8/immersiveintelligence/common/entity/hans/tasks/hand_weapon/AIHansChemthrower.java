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
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;

/**
 * @author Pabilo8
 * @since 23.04.2021
 */
public class AIHansChemthrower extends AIHansHandWeapon
{
	private final int burstTime;

	private final static ItemChemthrower CHEM = (ItemChemthrower)IEContent.itemChemthrower;

	public AIHansChemthrower(EntityHans hans)
	{
		super(hans, 10, 4, 0.9f);
		this.burstTime = 20;
	}

	@Override
	protected boolean isValidWeapon()
	{
		return getWeapon().getItem() instanceof ItemChemthrower;
	}

	@Override
	protected boolean hasAnyAmmo()
	{
		ItemStack chemthrower = getWeapon();
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

	@Override
	protected void executeTask()
	{
		assert attackTarget!=null;

		final ItemStack chemthrower = getWeapon();
		lookOnTarget();

		if(this.rangedAttackTime < 0)
		{
			hans.resetActiveHand();
			rangedAttackTime += 1;
		}
		if(this.rangedAttackTime < burstTime)
		{
			hans.hasAmmo = true;
			lookOnTarget();
			hans.rotationPitch -= 25;
			ItemNBTHelper.setBoolean(chemthrower, "ignite", true);
			onUsingTick(chemthrower, this.hans, IEContent.itemChemthrower.getMaxItemUseDuration(chemthrower)-rangedAttackTime);
			hans.rotationPitch += 25;

			if(rangedAttackTime >= burstTime)
			{
				// TODO: 19.05.2021 add balistic calculation
				CHEM.onPlayerStoppedUsing(chemthrower, this.hans.world, this.hans, CHEM.getMaxItemUseDuration(chemthrower)-rangedAttackTime);
				rangedAttackTime = -10;
			}
		}
	}

	@Override
	protected float calculateBallisticAngle(ItemStack ammo, EntityLivingBase attackTarget)
	{
		return 0;
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