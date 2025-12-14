package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrecisionAssembler;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.06.2019
 */
public class PrecisionAssemblerRenderer extends TileEntitySpecialRenderer<TileEntityPrecisionAssembler> implements IReloadableModelContainer<PrecisionAssemblerRenderer>
{
	private static final RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelPrecisionAssembler model;
	private static ModelPrecisionAssembler modelFlipped;

	@Override
	public void render(TileEntityPrecisionAssembler te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/precision_assembler.png";
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+2, (float)y-1, (float)z-1);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			//A bit of trick here, because it's pointless to rewrite the whole code just because only one part is mirrored :v
			GlStateManager.pushMatrix();
			ModelPrecisionAssembler currentModel = te.mirrored?modelFlipped: model;
			currentModel.getBlockRotation(te.facing, te.mirrored);
			currentModel.render();
			for(ModelRendererTurbo mod : model.drawer1Model)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.drawer2Model)
				mod.render(0.0625f);

			GlStateManager.popMatrix();
			GlStateManager.popMatrix();

		}
		else if(te==null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x-0.35, y-1.15, z-0.6);
			GlStateManager.rotate(90, 0, 1, 0);
			GlStateManager.rotate(-7.5f, 0, 0, 1);
			GlStateManager.rotate(-7.5f, 1, 0, 0);
			GlStateManager.scale(0.3, 0.3, 0.3);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			ClientUtils.bindTexture(texture);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.drawer1Model)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.drawer2Model)
				mod.render(0.0625f);

			GlStateManager.popMatrix();
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelPrecisionAssembler(false);
		modelFlipped = new ModelPrecisionAssembler(true);
		modelFlipped.flipAllZ();
	}
}
