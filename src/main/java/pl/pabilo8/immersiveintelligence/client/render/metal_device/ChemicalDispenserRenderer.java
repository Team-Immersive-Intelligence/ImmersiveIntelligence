package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityChemicalDispenser;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 01.06.2019
 */
@RegisteredTileRenderer(name = "block/device/chemical_dispenser", clazz = TileEntityChemicalDispenser.class)
public class ChemicalDispenserRenderer extends IITileRenderer<TileEntityChemicalDispenser>
{
	private static AMTModel model = null;

	@Override
	public void draw(TileEntityChemicalDispenser te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//apply animation
		for(AMT model : model)
			IIAnimationUtils.setModelRotation(model, te.pitch, 0, te.yaw);

		//apply rotation for block facing
		applyStandardRotation(te.facing);

		//render
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		ChemicalDispenserRenderer.model = new AMTModel(state, model);
	}

	@Override
	protected void nullifyModels()
	{
		model = IIAnimationUtils.disposeOf(model);
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
