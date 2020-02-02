package pl.pabilo8.immersiveintelligence.api.utils;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

/**
 * Created by Pabilo8 on 17-07-2019.
 */
public interface IMinecartBlockPickable
{
	Tuple<ItemStack, EntityMinecart> getBlockForPickup();

	void setMinecartBlock(ItemStack stack);
}
