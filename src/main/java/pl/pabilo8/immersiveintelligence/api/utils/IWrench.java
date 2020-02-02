package pl.pabilo8.immersiveintelligence.api.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Pabilo8 on 19-01-2020.
 */
public interface IWrench
{
	boolean canBeUsed(ItemStack stack);

	void damageWrench(ItemStack stack, EntityPlayer player);
}
