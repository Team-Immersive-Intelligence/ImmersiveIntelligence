package pl.pabilo8.immersiveintelligence.common.item.mechanical;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.MechanicalDevices;
import pl.pabilo8.immersiveintelligence.api.rotary.IMotorGear;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorGear.MotorGear;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 27-12-2019
 */
// TODO: 04.09.2022 move to capabilities
@IBatchOredictRegister(oreDict = "gear")
public class ItemIIMotorGear extends ItemIISubItemsBase<MotorGear> implements IMotorGear
{
	public ItemIIMotorGear()
	{
		super("motor_gear", 64, MotorGear.values());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		float mod = getGearTorqueModifier(stack);
		//speed : torque
		float speed = mod < 1&&mod!=0?1f/mod: 1;
		float torque = mod >= 1?mod: 1;

		tooltip.add(I18n.format(IIReference.INFO_KEY+"gear_ratio",
				speed==(int)speed?String.valueOf((int)speed): String.valueOf(speed),
				torque==(int)torque?String.valueOf((int)torque): String.valueOf(torque)
		));
	}

	@Override
	public float getGearTorqueModifier(ItemStack stack)
	{
		return stackToSub(stack).torqueMod;
	}

	@GeneratedItemModels(itemName = "motor_gear")
	public enum MotorGear implements IIItemEnum
	{
		COPPER(MechanicalDevices.gearTorqueModifier[0]),
		BRASS(MechanicalDevices.gearTorqueModifier[1]),
		IRON(MechanicalDevices.gearTorqueModifier[2]),
		STEEL(MechanicalDevices.gearTorqueModifier[3]),
		TUNGSTEN(MechanicalDevices.gearTorqueModifier[4]);

		final float torqueMod;

		MotorGear(float torqueMod)
		{
			this.torqueMod = torqueMod;
		}
	}

}
