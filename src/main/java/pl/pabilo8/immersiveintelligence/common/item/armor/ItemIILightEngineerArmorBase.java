package pl.pabilo8.immersiveintelligence.common.item.armor;

import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEEnergyItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.protection.protection.capability.ProtectionCapabilities;
import pl.pabilo8.immersiveintelligence.api.protection.protection.capability.ProtectionCapabilityProvider;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradeableArmor;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.01.2022
 */
public abstract class ItemIILightEngineerArmorBase extends ItemIIUpgradeableArmor implements IIEEnergyItem
{
	public ItemIILightEngineerArmorBase(EntityEquipmentSlot slot, String upgradeType)
	{
		super(IIContent.ARMOR_MATERIAL_LIGHT_ENGINEER, slot, upgradeType);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		ICapabilityProvider parent = super.initCapabilities(stack, nbt);
		if(stack.isEmpty()||parent==null)
			return parent;

		return new ProtectionCapabilityProvider(parent)
				.with(ProtectionCapabilities.CORROSION_PROTECTION, () -> protectsFromCorrosion(stack))
				.with(ProtectionCapabilities.RADIATION_PROTECTION, () -> protectsFromRadiation(stack))
				.with(ProtectionCapabilities.ACID_PROTECTION, () -> protectsFromAcid(stack))
				.with(ProtectionCapabilities.GAS_PROTECTION, () -> protectsFromGases(stack))
				.with(ProtectionCapabilities.INFRARED_PROTECTION, () -> isInvisibleToInfrared(stack));
	}

	protected boolean protectsFromCorrosion(ItemStack stack)
	{
		return hasUpgrade(stack, "hazmat");
	}

	protected boolean protectsFromRadiation(ItemStack stack)
	{
		return hasUpgrade(stack, "hazmat");
	}

	protected boolean protectsFromAcid(ItemStack stack)
	{
		return hasUpgrade(stack, "hazmat");
	}

	protected boolean protectsFromGases(ItemStack stack)
	{
		return false;
	}

	protected boolean isInvisibleToInfrared(ItemStack stack)
	{
		return false;
	}

	public boolean protectsFromHeat(ItemStack stack)
	{
		return hasUpgrade(stack, "heatcoat");
	}

	@Override
	public int getMaxEnergyStored(ItemStack stack)
	{
		return 4092;
	}

	@Override
	protected String getMaterialName(ArmorMaterial material)
	{
		return "light_engineer_armor";
	}

	@Override
	public String getSkinnableName()
	{
		return "engineer_light";
	}

	@Override
	public String getSkinnableDefaultTextureLocation()
	{
		return ImmersiveIntelligence.MODID+":textures/armor/";
	}
}
