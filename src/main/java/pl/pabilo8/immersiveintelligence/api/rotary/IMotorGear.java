package pl.pabilo8.immersiveintelligence.api.rotary;

import net.minecraft.item.ItemStack;

/**
 * Created by Pabilo8 on 26-12-2019.
 */
public interface IMotorGear
{
	/**
	 * @param stack the Itemstack
	 * @return Durability of the stack
	 */
	int getGearDurability(ItemStack stack);

	/**
	 * @param stack the Itemstack
	 */
	void damageGear(ItemStack stack, int amount);

	/**
	 * @param stack the Itemstack
	 * @return Max Durability of the stack
	 */
	int getGearMaxDurability(ItemStack stack);

	/**
	 * Gear will not work for lower values, it wouldn't wear off though.
	 *
	 * @param stack the Itemstack
	 * @return Minimal RPM of the stack
	 */
	int getGearMinTorque(ItemStack stack);

	/**
	 * When exceeded the gear will take multiplied damage. (Combines with Max Torque)
	 *
	 * @param stack the Itemstack
	 * @return Maximum RPM the stack can handle
	 */
	int getGearMaxRPM(ItemStack stack);

	/**
	 * When exceeded the gear will take multiplied damage. (Combines with Max RPM)
	 *
	 * @param stack the Itemstack
	 * @return Maximum Torque the stack can handle
	 */
	int getGearMaxTorque(ItemStack stack);

}
