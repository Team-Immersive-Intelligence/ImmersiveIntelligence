package pl.pabilo8.immersiveintelligence.api.utils.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author Pabilo8
 * @since 19-01-2020
 */
public interface IWrench
{
	boolean canBeUsed(ItemStack stack);

	void damageWrench(ItemStack stack, EntityPlayer player);
}
