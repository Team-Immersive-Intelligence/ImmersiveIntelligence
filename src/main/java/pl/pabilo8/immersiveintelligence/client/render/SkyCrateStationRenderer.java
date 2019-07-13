package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.BaseBlockModel;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden.ModelSkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCrateStation.TileEntitySkyCrateStationParent;

/**
 * Created by Pabilo8 on 2019-06-01.
 */
public class SkyCrateStationRenderer extends TileEntitySpecialRenderer<TileEntitySkyCrateStationParent>
{
	private static ModelSkyCrateStation model = new ModelSkyCrateStation();
	private static final ModelSkyCrateStation modelDefault = new ModelSkyCrateStation();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/skycrate_station.png";

	@Override
	public void render(TileEntitySkyCrateStationParent te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y, (float)z);
			GlStateManager.rotate(180F, 0F, 0F, 1F);
			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			BaseBlockModel.copyModelAngles(model, modelDefault);
			BaseBlockModel.getCommonMultiblockRotation(te.facing, model);

			model.getTranslation(te.facing, model);

			model.render();

			BaseBlockModel.copyModelPositions(model, modelDefault);

			GlStateManager.popMatrix();

		}
	}
}
