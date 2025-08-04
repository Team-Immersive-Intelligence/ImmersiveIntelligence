package pl.pabilo8.immersiveintelligence.api.data.device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IAdvancedTooltipItem;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;

import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 25.06.2019
 */
public interface IDataStorageItem extends IAdvancedTooltipItem
{
	DataPacket getStoredData(ItemStack stack);

	void writeDataToItem(ItemStack stack, DataPacket packet);

	@SideOnly(Side.CLIENT)
	@Override
	default void addAdvancedInformation(ItemStack stack, int offsetX, List<Integer> offsetsY)
	{
		//Display all the variables stored in a data storage item

		IDataStorageItem ds = (IDataStorageItem)stack.getItem();
		DataPacket packet = ds.getStoredData(stack);

		IIClientUtils.bindAtlas();
		GlStateManager.translate(offsetX, offsetsY.get(0), 700);
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.scale(.5f, .5f, 1);

		ClientUtils.bindAtlas();
		IIDrawUtils draw = IIDrawUtils.startTextured();
		for(DataVariable dataVariable : packet)
		{
			TextureAtlasSprite sprite = ClientUtils.getSprite(dataVariable.getValue().getTypeMeta().getTextureLocation());
			draw.drawTexRect(0, 0, 16, 16, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV())
					.addOffset(0, 20);
		}
		draw.finish();
	}
}
