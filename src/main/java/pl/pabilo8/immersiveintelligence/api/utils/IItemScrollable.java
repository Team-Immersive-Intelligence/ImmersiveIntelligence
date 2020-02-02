package pl.pabilo8.immersiveintelligence.api.utils;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

/**
 * Created by Pabilo8 on 13-07-2019.
 */
public interface IItemScrollable
{
	void onScroll(ItemStack stack, boolean forward, EntityPlayerMP player);
}
