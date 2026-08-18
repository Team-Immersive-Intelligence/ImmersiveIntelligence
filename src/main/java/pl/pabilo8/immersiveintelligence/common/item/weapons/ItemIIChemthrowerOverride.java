package pl.pabilo8.immersiveintelligence.common.item.weapons;

import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemChemthrower;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Overrides.Chemthrower;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityIIChemthrowerShot;

/**
 * Overrides IE's chemthrower to use II chemthrower projectiles and particle effects.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.08.2026
 */
public class ItemIIChemthrowerOverride extends ItemChemthrower
{
	/**
	 * Creates the chemthrower override and replaces the IE item registration entry.
	 */
	public ItemIIChemthrowerOverride()
	{
		super();
		IEContent.registeredIEItems.removeIf(item -> item instanceof ItemChemthrower);
		IEContent.registeredIEItems.add(this);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase user, int count)
	{
		//Can't use without fluid
		FluidStack fluidStack = getFluid(stack);
		if(fluidStack==null||fluidStack.getFluid()==null)
		{
			user.stopActiveHand();
			return;
		}

		//Compute if firing is still possible with fluid already consumed
		int duration = getMaxItemUseDuration(stack)-count;
		int consumption = IEConfig.Tools.chemthrower_consumption;
		if(consumption*duration > fluidStack.amount)
		{
			user.stopActiveHand();
			return;
		}

		//Set range and scatter
		boolean gas = fluidStack.getFluid().isGaseous(fluidStack)||ChemthrowerHandler.isGas(fluidStack.getFluid());
		float scatter = gas?Chemthrower.chemthrowerScatterGas: Chemthrower.chemthrowerScatterFluid;
		float range = gas?Chemthrower.chemthrowerRangeGas: Chemthrower.chemthrowerRangeFluid;

		//Reduce spraying if focus upgrade is installed
		if(getUpgrades(stack).getBoolean("focus"))
		{
			range += Chemthrower.chemthrowerRangeFocusModifier;
			scatter -= Chemthrower.chemthrowerScatterFocusModifier;
		}

		boolean ignite = ChemthrowerHandler.isFlammable(fluidStack.getFluid())&&ItemNBTHelper.getBoolean(stack, "ignite");
		Vec3d look = user.getLookVec();
		//Create shot entities
		for(int i = 0; i < Chemthrower.chemthrowerShotsPerTick; i++)
		{
			Vec3d direction = look.addVector(
					user.getRNG().nextGaussian()*scatter,
					user.getRNG().nextGaussian()*scatter,
					user.getRNG().nextGaussian()*scatter
			);

			if(!user.world.isRemote)
			{
				EntityIIChemthrowerShot shot = new EntityIIChemthrowerShot(user.world, user,
						direction.x*0.25, direction.y*0.25, direction.z*0.25, fluidStack)
						.withMotion(
								user.motionX+direction.x*range,
								user.motionY+direction.y*range,
								user.motionZ+direction.z*range
						);
				if(ignite)
					shot.setFire(10);
				user.world.spawnEntity(shot);
			}

			//Recoil / pushback
			user.motionX -= direction.x*Chemthrower.chemthrowerPushback*range;
			user.motionY -= direction.y*Chemthrower.chemthrowerPushback*range;
			user.motionZ -= direction.z*Chemthrower.chemthrowerPushback*range;
		}

		//Play spraying sound
		if(count%4==0)
			user.world.playSound(null, user.posX, user.posY, user.posZ,
					ignite?IESounds.sprayFire: IESounds.spray,
					SoundCategory.PLAYERS, 0.5f, ignite?1.5f: 0.75f);
	}
}
