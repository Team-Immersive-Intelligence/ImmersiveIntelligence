package pl.pabilo8.immersiveintelligence.api.utils.minecart;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 17.07.2019
 */
public interface IMinecartBlockPickable
{
	Tuple<ItemStack, EntityMinecart> getBlockForPickup();

	void setMinecartBlock(ItemStack stack);
}
