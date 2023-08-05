package pl.pabilo8.immersiveintelligence.common.item;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.item.ItemIITracerPowder.Powders;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 10-11-2019
 */
public class ItemIITracerPowder extends ItemIISubItemsBase<Powders>
{
	public ItemIITracerPowder()
	{
		super("tracer_powder", 64, Powders.values());
	}

	enum Powders implements IIItemEnum
	{
		@IIItemProperties(oreDict = {"dustTracer"})
		TRACER_POWDER,
		@IIItemProperties(oreDict = {"dustFlare"})
		FLARE_POWDER
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		int col = getColour(stack);
		tooltip.add(I18n.format(Lib.DESC_INFO+"colour", "<hexcol="+Integer.toHexString(col)+":#"+Integer.toHexString(col)+">"));
	}

	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public FontRenderer getFontRenderer(@Nonnull ItemStack stack)
	{
		return IIClientUtils.fontRegular;
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
