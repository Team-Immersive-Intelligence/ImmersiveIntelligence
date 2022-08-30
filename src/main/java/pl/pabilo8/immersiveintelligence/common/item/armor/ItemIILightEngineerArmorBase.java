package pl.pabilo8.immersiveintelligence.common.item.armor;

import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.CorrosionHandler.IAcidProtectionEquipment;
import pl.pabilo8.immersiveintelligence.api.CorrosionHandler.ICorrosionProtectionEquipment;
import pl.pabilo8.immersiveintelligence.api.utils.IRadiationProtectionEquipment;
import pl.pabilo8.immersiveintelligence.common.IIContent;

/**
 * @author Pabilo8
 * @since 08.01.2022
 */
public abstract class ItemIILightEngineerArmorBase extends ItemIIUpgradeableArmor implements ICorrosionProtectionEquipment, IRadiationProtectionEquipment, IAcidProtectionEquipment, IIEEnergyItem
{
	public ItemIILightEngineerArmorBase(EntityEquipmentSlot slot, String upgradeType)
	{
		super(IIContent.ARMOR_MATERIAL_LIGHT_ENGINEER, slot, upgradeType);
	}

	@Override
	public boolean canCorrode(ItemStack stack)
	{
		return !hasUpgrade(stack, "hazmat");
	}

	@Override
	public boolean protectsFromRadiation(ItemStack stack)
	{
		return hasUpgrade(stack, "hazmat");
	}

	@Override
	public boolean protectsFromAcid(ItemStack stack)
	{
		return hasUpgrade(stack, "hazmat");
	}

	@Override
	public int getMaxEnergyStored(ItemStack stack)
	{
		return 4092;
	}

	@Override
	String getMaterialName(ArmorMaterial material)
	{
		return "light_engineer_armor";
	}
}
