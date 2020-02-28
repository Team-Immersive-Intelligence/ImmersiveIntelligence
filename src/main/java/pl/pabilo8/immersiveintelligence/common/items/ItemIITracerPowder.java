package pl.pabilo8.immersiveintelligence.common.items;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientProxy;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Pabilo8 on 10-11-2019.
 */
public class ItemIITracerPowder extends ItemIIBase
{
	public ItemIITracerPowder()
	{
		super("tracer_powder", 64);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		int col = getColour(stack);
		tooltip.add(I18n.translateToLocalFormatted(Lib.DESC_INFO+"colour", "<hexcol="+Integer.toHexString(col)+":#"+Integer.toHexString(col)+">"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public FontRenderer getFontRenderer(ItemStack stack)
	{
		return ClientProxy.itemFont;
	}


	public static void setColour(ItemStack stack, int rgb)
	{
		ItemNBTHelper.setInt(stack, "colour", rgb);
	}

	public static void setColour(ItemStack stack, int r, int g, int b)
	{
		setColour(stack, MathHelper.rgb(r, g, b));
	}

	@Override
	public int getColourForIEItem(ItemStack stack, int pass)
	{
		return getColour(stack);
	}

	public static int getColour(ItemStack stack)
	{
		if(!ItemNBTHelper.hasKey(stack, "colour"))
			setColour(stack, 0xffffff);
		return ItemNBTHelper.getInt(stack, "colour");
	}

	@Override
	public boolean hasCustomItemColours()
	{
		return true;
	}
}
