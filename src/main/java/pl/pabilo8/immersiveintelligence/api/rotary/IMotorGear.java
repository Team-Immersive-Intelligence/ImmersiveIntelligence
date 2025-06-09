package pl.pabilo8.immersiveintelligence.api.rotary;

import net.minecraft.item.ItemStack;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.12.2019
 */
public interface IMotorGear
{
	float getGearTorqueModifier(ItemStack stack);
}
