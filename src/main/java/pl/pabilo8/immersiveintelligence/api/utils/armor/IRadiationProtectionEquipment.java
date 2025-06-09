package pl.pabilo8.immersiveintelligence.api.utils.armor;

import net.minecraft.item.ItemStack;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.07.2021
 */
public interface IRadiationProtectionEquipment
{
	boolean protectsFromRadiation(ItemStack stack);
}
