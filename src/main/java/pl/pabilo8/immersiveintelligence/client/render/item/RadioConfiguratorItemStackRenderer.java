package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelAdvancedRadioConfigurator;
import pl.pabilo8.immersiveintelligence.client.model.item.ModelRadioConfigurator;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

/**
 * @author Pabilo8
 * @since 13-10-2019
 */
public class RadioConfiguratorItemStackRenderer extends TileEntityItemStackRenderer
{
	public static RadioConfiguratorItemStackRenderer instance = new RadioConfiguratorItemStackRenderer();
	@SideOnly(Side.CLIENT)
	private static ModelRadioConfigurator model_basic = new ModelRadioConfigurator();
	@SideOnly(Side.CLIENT)
	private static ModelAdvancedRadioConfigurator model_advanced = new ModelAdvancedRadioConfigurator();

	@Override
	public void renderByItem(ItemStack itemStackIn, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180, 0, 1, 0);
		GlStateManager.translate(-1f, 0f, 0f);

		if(CommonProxy.item_radio_configurator.isBasic(itemStackIn))
		{
			String texture_basic = ImmersiveIntelligence.MODID+":textures/items/tools/radio_configurator.png";
			ClientUtils.bindTexture(texture_basic);
			float rot1 = (ItemNBTHelper.getInt(itemStackIn, "Frequency")%5)/5f;
			float rot2 = (float)Math.floor(ItemNBTHelper.getInt(itemStackIn, "Frequency")/5f)/((float)IIConfig.radioBasicMaxFrequency/5f);

			for(ModelRendererTurbo mod : model_basic.baseModel)
				mod.render(0.0625f);
			GlStateManager.pushMatrix();
			GlStateManager.translate(-0.3125f*rot2, 0, 0);
			for(ModelRendererTurbo mod : model_basic.sliderModel1)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();

			GlStateManager.translate(-0.3125f*rot1, 0, 0);
			for(ModelRendererTurbo mod : model_basic.sliderModel2)
				mod.render(0.0625f);
			GlStateManager.popMatrix();
		}
		else
		{
			String texture_advanced = ImmersiveIntelligence.MODID+":textures/items/tools/advanced_radio_configurator.png";
			ClientUtils.bindTexture(texture_advanced);
			float num1 = IIConfig.radioBasicMaxFrequency/8, num2 = num1/8;

			float rot1 = ((ItemNBTHelper.getInt(itemStackIn, "Frequency")/8)%8)/8f;
			float rot2 = (ItemNBTHelper.getInt(itemStackIn, "Frequency")%8)/8f;
			float rot3 = (float)Math.floor(ItemNBTHelper.getInt(itemStackIn, "Frequency")/64f)/((float)IIConfig.radioAdvancedMaxFrequency/64f);

			for(ModelRendererTurbo mod : model_advanced.baseModel)
				mod.render(0.0625f);

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.59375f, 0.53125f, -0.4375f);
			GlStateManager.rotate(rot1*360f, 0, 0, 1);
			for(ModelRendererTurbo mod : model_advanced.gaugeModel1)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.40625f, 0.53125f, -0.4375f);

			GlStateManager.rotate(rot2*360f, 0, 0, 1);
			for(ModelRendererTurbo mod : model_advanced.gaugeModel2)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.8333333333333334, 0.53125f, -0.4375f);
			GlStateManager.rotate(rot3*360f, 0, 0, 1);
			for(ModelRendererTurbo mod : model_advanced.gaugeModel3)
				mod.render(0.0625f);
			GlStateManager.popMatrix();
		}


		GlStateManager.popMatrix();
	}
}
