package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.client.animation.AMT;
import pl.pabilo8.immersiveintelligence.client.animation.IIAnimationLoader;
import pl.pabilo8.immersiveintelligence.client.animation.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityChemicalDispenser;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class ChemicalDispenserRenderer extends IITileRenderer<TileEntityChemicalDispenser>
{
	private static AMT[] models = null;

	@Override
	public void draw(TileEntityChemicalDispenser te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//apply animation
		for(AMT model : models)
			IIAnimationUtils.setModelRotation(model,te.pitch, 0, te.yaw);

		//apply rotation for block facing
		applyStandardRotation(te.facing);

		//render
		for(AMT mod : models)
			mod.render(tes, buf);
	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		models = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()));
	}

	@Override
	protected void nullifyModels()
	{
		models = IIAnimationUtils.disposeOf(models);
	}

	@Override
	protected void applyStandardRotation(EnumFacing facing)
	{
		GlStateManager.translate(0.5, 0.5, 0.5);
		switch(facing)
		{
			default:
				break;
			case DOWN:
			{
				GlStateManager.rotate(-90, 0, 1, 0);
			}
			break;
			case UP:
			{

			}
			break;
			case NORTH:
			{
				GlStateManager.rotate(90, 1, 0, 0);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(-90, 1, 0, 0);
			}
			break;
			case WEST:
			{
				GlStateManager.rotate(90, 0, 1, 0);
				GlStateManager.rotate(90, 1, 0, 0);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(-90, 0, 1, 0);
				GlStateManager.rotate(90, 1, 0, 0);
			}
			break;
		}
		GlStateManager.translate(-0.5, -0.5, -0.5);
	}
}
