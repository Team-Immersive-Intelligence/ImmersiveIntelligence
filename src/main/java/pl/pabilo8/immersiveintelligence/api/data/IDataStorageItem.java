package pl.pabilo8.immersiveintelligence.api.data;

import net.minecraft.item.ItemStack;

/**
 * @author Pabilo8
 * @since 25-06-2019
 */
public interface IDataStorageItem
{
	DataPacket getStoredData(ItemStack stack);

	void writeDataToItem(DataPacket packet, ItemStack stack);
}
