package pl.pabilo8.immersiveintelligence.common.item.data;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataStorageItem;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 25-06-2019
 */
@IIItemProperties(category = IICategory.ELECTRONICS)
public class ItemIIPunchtape extends ItemIIBase implements IDataStorageItem
{
	public ItemIIPunchtape()
	{
		super("punchtape", 8);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		DataPacket packet = getStoredData(stack);
		if(packet.hasAnyVariables())
		{
			if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LSHIFT, IIReference.DESCRIPTION_KEY+"data_storage_shift", tooltip))
			{
				tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"data_storage"));
				packet.variables.forEach(
						(c, t) -> tooltip.add("   "+t.getTypeColor().getHexCol(I18n.format(IIReference.DATA_KEY+"datatype."+t.getName()))+" "+TextFormatting.GRAY+c)
				);
			}
		}
		else
			tooltip.add(TextFormatting.ITALIC+I18n.format(IIReference.DESCRIPTION_KEY+"punchtape.empty"));

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
