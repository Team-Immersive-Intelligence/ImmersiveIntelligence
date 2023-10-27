package pl.pabilo8.immersiveintelligence.api.utils.armor;

import net.minecraft.item.ItemStack;

/**
 * @author Pabilo8
 * @since 09.07.2021
 */
public interface IRadiationProtectionEquipment
{
	boolean protectsFromRadiation(ItemStack stack);
}
