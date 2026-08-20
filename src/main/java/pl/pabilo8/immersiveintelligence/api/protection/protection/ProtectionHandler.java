package pl.pabilo8.immersiveintelligence.api.protection.protection;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import pl.pabilo8.immersiveintelligence.api.protection.protection.capability.*;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * Central capability-based handler for radiation, gas, infrared and acid protective equipment.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 22.07.2026
 */
public final class ProtectionHandler
{
	private ProtectionHandler()
	{
	}

	public static boolean isProtectedFromGas(EntityLivingBase entity)
	{
		return hasPartialArmorProtection(entity, ProtectionCapabilities.GAS_PROTECTION,
				IGasProtection::protectsFromGases);
	}

	public static boolean isInvisibleToInfrared(EntityLivingBase entity)
	{
		return hasPartialArmorProtection(entity, ProtectionCapabilities.INFRARED_PROTECTION,
				IInfraredProtection::isInvisibleToInfrared);
	}

	public static boolean isProtectedFromAcid(EntityLivingBase entity)
	{
		return hasCompleteArmorProtection(entity, ProtectionCapabilities.ACID_PROTECTION,
				IAcidProtection::protectsFromAcid);
	}

	public static boolean isProtectedFromRadiation(EntityLivingBase entity)
	{
		return hasCompleteArmorProtection(entity, ProtectionCapabilities.RADIATION_PROTECTION,
				IRadiationProtection::protectsFromRadiation);
	}

	public static boolean isProtectedFromCorrosion(ItemStack stack)
	{
		return test(stack, ProtectionCapabilities.CORROSION_PROTECTION, ICorrosionProtection::protectsFromCorrosion);
	}

	private static <T> boolean hasPartialArmorProtection(EntityLivingBase entity, @Nullable Capability<T> capability,
	                                                     Predicate<T> predicate)
	{
		//Check for the entity itself
		if(capability!=null&&entity.hasCapability(capability, null)
				&&predicate.test(entity.getCapability(capability, null)))
			return true;

		//Check for armor pieces
		for(ItemStack stack : entity.getArmorInventoryList())
			if(test(stack, capability, predicate))
				return true;
		return false;
	}

	private static <T> boolean hasCompleteArmorProtection(EntityLivingBase entity, @Nullable Capability<T> capability,
	                                                      Predicate<T> predicate)
	{
		//Check for the entity itself
		if(capability!=null&&entity.hasCapability(capability, null)
				&&predicate.test(entity.getCapability(capability, null)))
			return true;

		//Check for armor pieces
		for(ItemStack stack : entity.getArmorInventoryList())
			if(!test(stack, capability, predicate))
				return false;
		return true;
	}

	private static <T> boolean test(ItemStack stack, @Nullable Capability<T> capability, Predicate<T> predicate)
	{
		if(stack.isEmpty()||capability==null||!stack.hasCapability(capability, null))
			return false;
		T value = stack.getCapability(capability, null);
		return value!=null&&predicate.test(value);
	}
}
