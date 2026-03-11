package pl.pabilo8.immersiveintelligence.common.item.tools;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.items.tools.ItemIEShovel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 29.08.2020
 * <p>
 * A tool used in construction of barricades, fortifications
 * Also required for burying mines
 * Can 'field reinforce' selected blocks to make them stronger / upgrade them
 */
@IIItemProperties(category = IICategory.TOOLS)
@GeneratedItemModels(itemName = "trench_shovel", type = ItemModelType.ITEM_SIMPLE_TOOL, texturePath = "tools/trench_shovel")
public class ItemIITrenchShovel extends ItemIEShovel
{
	public ItemIITrenchShovel()
	{
		super(Lib.MATERIAL_Steel, "trench_shovel", "shovel", "plateSteel");
		IIUtils.fixupItem(this, "trench_shovel");
	}


	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(IIStringUtil.getItalicString(I18n.format(IIReference.DESCRIPTION_KEY+"trench_shovel")));
	}

}
