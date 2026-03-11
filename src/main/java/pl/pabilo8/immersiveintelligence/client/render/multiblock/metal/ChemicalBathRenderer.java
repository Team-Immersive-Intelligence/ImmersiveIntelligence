package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelChemicalBath;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.util.tmt.Shape2D;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityChemicalBath;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.06.2019
 */
public class ChemicalBathRenderer extends TileEntitySpecialRenderer<TileEntityChemicalBath> implements IReloadableModelContainer<ChemicalBathRenderer>
{
	private static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/chemical_bath.png";
	private static ModelChemicalBath model;
	private static ModelChemicalBath modelFlipped;

	@Override
	public void render(@Nullable TileEntityChemicalBath te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(TEXTURE);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+2, (float)y-2, (float)z);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			ModelChemicalBath renderModel = te.mirrored?modelFlipped: model;
			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			renderModel.getBlockRotation(te.facing, te.mirrored);
			renderModel.render();

			ClientUtils.bindTexture(TEXTURE);
			for(ModelRendererTurbo mod : model.sliderModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.sliderLoweringModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.itemPickerLeftTopModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.itemPickerLeftBottomModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.itemPickerRightTopModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.itemPickerRightBottomModel)
				mod.render(0.0625f);

			GlStateManager.popMatrix();

		}
		else if(te==null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x-0.35, y-0.25, z);
			GlStateManager.rotate(-90, 0, 1, 0);
			GlStateManager.rotate(7.5f, 0, 0, 1);
			GlStateManager.rotate(7.5f, 1, 0, 0);
			GlStateManager.scale(0.3, 0.3, 0.3);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			ClientUtils.bindTexture(TEXTURE);
			for(ModelRendererTurbo mod : model.baseModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.sliderModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.sliderLoweringModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.itemPickerLeftTopModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.itemPickerLeftBottomModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.itemPickerRightTopModel)
				mod.render(0.0625f);
			for(ModelRendererTurbo mod : model.itemPickerRightBottomModel)
				mod.render(0.0625f);

			GlStateManager.popMatrix();
		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelChemicalBath();
		modelFlipped = new ModelChemicalBath();

		modelFlipped.baseModel[6].flip = true;
		modelFlipped.baseModel[6].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 16, 0, 16), new Coord2D(48, 16, 48, 16), new Coord2D(48, 8, 48, 8), new Coord2D(42, 0, 42, 0), new Coord2D(6, 0, 6, 0), new Coord2D(0, 8, 0, 8)}), 1, 48, 16, 120, 1, ModelRendererTurbo.MR_FRONT, new float[]{8, 10, 36, 10, 8, 48}, true); // BathFront
		modelFlipped.baseModel[6].setRotationPoint(48F, -4F, 16F);

		modelFlipped.baseModel[7].flip = true;
		modelFlipped.baseModel[7].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 16, 0, 16), new Coord2D(48, 16, 48, 16), new Coord2D(48, 8, 48, 8), new Coord2D(42, 0, 42, 0), new Coord2D(6, 0, 6, 0), new Coord2D(0, 8, 0, 8)}), 1, 48, 16, 120, 1, ModelRendererTurbo.MR_FRONT, new float[]{8, 10, 36, 10, 8, 48}, true); // BathBack
		modelFlipped.baseModel[7].setRotationPoint(48F, -4F, 63F);

		modelFlipped.flipAllZ();
	}
}
