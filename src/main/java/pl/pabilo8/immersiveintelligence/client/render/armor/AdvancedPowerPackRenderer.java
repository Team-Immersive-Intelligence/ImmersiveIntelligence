package pl.pabilo8.immersiveintelligence.client.render.armor;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModelBuilder;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIArmorRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIArmorRenderer.RegisteredArmorRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIAdvancedPowerPack;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * Renders the capacitor backpack with the selected paint colour.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 12.11.2026
 */
@RegisteredArmorRenderer(name = "armor/backpack/advanced_powerpack")
public class AdvancedPowerPackRenderer extends IIArmorRenderer<ItemIIAdvancedPowerPack>
{
	public AdvancedPowerPackRenderer()
	{
		super(IIContent.itemAdvancedPowerPack, ARMOR_MODELS.with("backpack/advanced_powerpack"));
	}

	@Override
	public void reloadModels()
	{
		this.model = AMTCachedModelBuilder.startItemModel()
				.withModel(modelLocation)
				.withHeader(modelLocation.withExtension(ResLoc.EXT_OBJAMT))
				.withHeaderProvider(itemStack -> new AMTModelHeader().withPartProperties("body_paint",
						EasyNBT.newNBT().withColor("bakedColor", IIColor.fromPackedRGB(item.getColor(itemStack)))
				))
				.build();
	}

	@Override
	protected AMTModel getModelVariant(AMTCachedModel<ItemStack> cache, ItemStack stack, EntityLivingBase wearer, float partialTicks)
	{
		ItemStack copy = stack.copy();
		ItemNBTHelper.remove(copy, "energy");
		return cache.getVariant(copy, item.getColor(copy));
	}
}
