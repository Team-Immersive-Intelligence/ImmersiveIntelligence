package pl.pabilo8.immersiveintelligence.api.rotary;

import net.minecraft.item.ItemStack;

/**
 * @author Pabilo8
 * @since 26-12-2019
 */
public interface IMotorGear
{
	float getGearTorqueModifier(ItemStack stack);
}
