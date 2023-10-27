package pl.pabilo8.immersiveintelligence.api.utils.tools;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Pabilo8
 * @since 19.08.2021
 */
public interface IPortableRadioEquipment
{
	boolean canReceiveRadio(World world, EntityLivingBase wearer, ItemStack stack, int frequency);

	void onReceiveRadio(World world, EntityLivingBase wearer, ItemStack stack, int frequency);

	default void sendRadio(World world, EntityLivingBase wearer, ItemStack stack, int frequency)
	{

	}
}
