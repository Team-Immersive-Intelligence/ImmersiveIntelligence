package pl.pabilo8.immersiveintelligence.common.item;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataStorageItem;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;

/**
 * @author Pabilo8
 * @since 25-06-2019
 */
public class ItemIIPunchtape extends ItemIIBase implements IDataStorageItem
{
	public ItemIIPunchtape()
	{
		super("punchtape", 8);
	}

	@Override
	public DataPacket getStoredData(ItemStack stack)
	{
		DataPacket data = new DataPacket();
		data.fromNBT(ItemNBTHelper.getTagCompound(stack, "stored_data"));
		return data;
	}

	@Override
	public void writeDataToItem(DataPacket packet, ItemStack stack)
	{
		ItemNBTHelper.setTagCompound(stack, "stored_data", packet.toNBT());
	}
}
