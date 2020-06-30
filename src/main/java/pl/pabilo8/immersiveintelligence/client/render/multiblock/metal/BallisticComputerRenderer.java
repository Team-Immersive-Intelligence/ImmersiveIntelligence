package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.metal.ModelBallisticComputer;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.tmt.Coord2D;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.Shape2D;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.TileEntityBallisticComputer;

/**
 * Created by Pabilo8 on 28-06-2019.
 */
public class BallisticComputerRenderer extends TileEntitySpecialRenderer<TileEntityBallisticComputer> implements IReloadableModelContainer<BallisticComputerRenderer>
{
	private static ModelBallisticComputer model;
	private static ModelBallisticComputer modelFlipped;

	static
	{
		model = new ModelBallisticComputer();
		modelFlipped = new ModelBallisticComputer();

		modelFlipped.baseModel[2].flip = true;
		modelFlipped.baseModel[2].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(16, 0, 16, 0), new Coord2D(16, 16, 16, 16), new Coord2D(4, 16, 4, 16), new Coord2D(0, 12, 0, 12)}), 1, 16, 16, 62, 1, ModelRendererTurbo.MR_FRONT, new float[]{12, 6, 12, 16, 16}); // ShapeMiddleWall
		modelFlipped.baseModel[2].setRotationPoint(16F, -8F, 0F);
		modelFlipped.baseModel[2].rotateAngleY = 1.57079633F;

		modelFlipped.flipAllZ();

		for(ModelRendererTurbo[] mod : model.parts.values())
		{
			for(ModelRendererTurbo m : mod)
				m.rotateAngleY *= -1;
		}
	}

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/ballistic_computer.png";

	@Override
	public void render(TileEntityBallisticComputer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y-1f, (float)z);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(te.hasWorld())
			{
				GlStateManager.translate(0f, 1f, 1f);
				GlStateManager.rotate(90F, 0F, 1F, 0F);
			}

			ModelBallisticComputer modelCurrent = te.mirrored?modelFlipped: model;

			modelCurrent.getBlockRotation(te.facing, te.mirrored);
			modelCurrent.render();

			if(te.progress > 0)
			{
				GlStateManager.pushMatrix();

				ClientUtils.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE.toString());
				TextureAtlasSprite sprite = ClientUtils.mc().getTextureMapBlocks().getAtlasSprite(IEContent.fluidBiodiesel.getStill(new FluidStack(IEContent.fluidBiodiesel, 1000)).toString());

				if(te.progress < 3)
				{
					sprite = ClientUtils.mc().getTextureMapBlocks().getAtlasSprite(IEContent.fluidConcrete.getStill(new FluidStack(IEContent.fluidConcrete, 1000)).toString());
				}
				else if(te.progress < 6)
					GlStateManager.color(0.75f, 0.75f, 0.75f);
				else if(te.progress < 10)
					GlStateManager.color(0.25f, 0.25f, 0.25f);
				else if(te.progress < 32)
					GlStateManager.color(0.5f, 0.5f, 0.5f);

				int iW = sprite.getIconWidth();
				int iH = sprite.getIconHeight();

				GlStateManager.rotate(90, 1, 0, 0);
				GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);
				GlStateManager.translate(26, -7, -12);
				GlStateManager.rotate(13, 0, 0, 1);
				if(iW > 0&&iH > 0)
					ClientUtils.drawRepeatedSprite(0, 0, 3, 2, iW, iH, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());


				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();

		}
	}

	@Override
	public void reloadModels()
	{
		model = new ModelBallisticComputer();
		modelFlipped = new ModelBallisticComputer();

		modelFlipped.baseModel[2].flip = true;
		modelFlipped.baseModel[2].addShape3D(0F, 0F, 0F, new Shape2D(new Coord2D[]{new Coord2D(0, 0, 0, 0), new Coord2D(16, 0, 16, 0), new Coord2D(16, 16, 16, 16), new Coord2D(4, 16, 4, 16), new Coord2D(0, 12, 0, 12)}), 1, 16, 16, 62, 1, ModelRendererTurbo.MR_FRONT, new float[]{12, 6, 12, 16, 16}); // ShapeMiddleWall
		modelFlipped.baseModel[2].setRotationPoint(16F, -8F, 0F);
		modelFlipped.baseModel[2].rotateAngleY = 1.57079633F;

		modelFlipped.flipAllZ();
	}
}