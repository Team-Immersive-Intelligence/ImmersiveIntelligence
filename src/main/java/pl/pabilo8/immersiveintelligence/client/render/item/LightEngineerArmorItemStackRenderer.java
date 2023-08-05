package pl.pabilo8.immersiveintelligence.client.render.item;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.model.armor.ModelLightEngineerArmor;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradeableArmor;

/**
 * @author Pabilo8
 * @since 13-10-2019
 */
public class LightEngineerArmorItemStackRenderer extends TileEntityItemStackRenderer
{
	public static LightEngineerArmorItemStackRenderer instance = new LightEngineerArmorItemStackRenderer();

	@Override
	public void renderByItem(ItemStack itemStackIn, float partialTicks)
	{
		GlStateManager.pushMatrix();

		RenderHelper.enableStandardItemLighting();
		if(itemStackIn.getItem() instanceof ItemIIUpgradeableArmor)
		{
			ModelLightEngineerArmor model = ModelLightEngineerArmor.getModel(((ItemIIUpgradeableArmor)itemStackIn.getItem()).armorType, itemStackIn);
			model.renderAsPart();
		}

		GlStateManager.popMatrix();
	}
}
