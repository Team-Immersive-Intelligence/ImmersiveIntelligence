package pl.pabilo8.immersiveintelligence.api.data.device;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IAdvancedTooltipItem;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;

import java.util.List;

/**
 * @author Pabilo8
 * @since 25-06-2019
 */
public interface IDataStorageItem extends IAdvancedTooltipItem
{
	DataPacket getStoredData(ItemStack stack);

	void writeDataToItem(DataPacket packet, ItemStack stack);

	@SideOnly(Side.CLIENT)
	@Override
	default void addAdvancedInformation(ItemStack stack, int offsetX, List<Integer> offsetsY)
	{
		//Display all the variables stored in a data storage item

		IDataStorageItem ds = (IDataStorageItem)stack.getItem();
		DataPacket packet = ds.getStoredData(stack);

		GlStateManager.translate(offsetX, offsetsY.get(0), 700);
		GlStateManager.scale(.5f, .5f, 1);

		int i = 0;
		for(DataType type : packet)
		{
			GlStateManager.color(1f, 1f, 1f, 1f);
			IIClientUtils.bindTexture(type.getTextureLocation());
			Gui.drawModalRectWithCustomSizedTexture(0, i*20, 0, 0, 16, 16, 16, 16);
			i++;
		}
	}
}
