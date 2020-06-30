package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelFluidInserter;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.client.tmt.TmtUtil;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityFluidInserter;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.fluid_inserter;

/**
 * @author Pabilo8
 * @since 15-06-2019
 */

@SideOnly(Side.CLIENT)
public class FluidInserterRenderer extends TileEntitySpecialRenderer<TileEntityFluidInserter>
{
	private static ModelFluidInserter model = new ModelFluidInserter();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/metal_device/fluid_inserter.png";

	@Override
	public void render(TileEntityFluidInserter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y, (float)z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();

			float f5 = 1f/16f;

			model.getBlockRotation(EnumFacing.NORTH, false);

			boolean renderOut = true, renderIn = true;

			switch(te.outputFacing)
			{
				case SOUTH:
				{
					model.rotate(model.inserterOutput, 0f, 1.57079633F, 0f);
				}
				break;
				case EAST:
				{
					model.rotate(model.inserterOutput, 0f, -3.1415927f, 0f);
				}
				break;
				case WEST:
				{
					model.rotate(model.inserterOutput, 0f, 0f, 0f);
				}
				break;
				case NORTH:
				{
					model.rotate(model.inserterOutput, 0f, -1.57079633F, 0f);
				}
				break;
				default:
				{
					//EnumFacing.UP
					renderOut = false;
					//model.rotate(model.inserterOutput, 0f, 0, -1.57079633F);
				}
				break;
			}

			switch(te.inputFacing)
			{
				case SOUTH:
				{
					model.rotate(model.inserterInput, 0f, 1.57079633F, 0f);
				}
				break;
				case EAST:
				{
					model.rotate(model.inserterInput, 0f, -3.1415927f, 0f);
				}
				break;
				case WEST:
				{
					model.rotate(model.inserterInput, 0f, 0f, 0f);
				}
				break;
				case NORTH:
				{
					model.rotate(model.inserterInput, 0f, -1.57079633F, 0f);
				}
				break;
				default:
				{
					//EnumFacing.UP
					renderIn = false;
					//model.rotate(model.inserterInput, 0f, 0, -1.57079633F);
				}
				break;
			}

			if(renderIn)
			{
				for(ModelRendererTurbo mod : model.inserterInput)
					mod.render(f5);
			}
			if(renderOut)
			{
				for(ModelRendererTurbo mod : model.inserterOutput)
					mod.render(f5);
			}

			model.render();

			model.rotate(model.inserterGaugeArrow, 0, TmtUtil.AngleToTMT(45-(te.fluidToTake/((float)fluid_inserter.maxOutput*20f))*360f), 0);
			for(ModelRendererTurbo mod : model.inserterGaugeArrow)
				mod.render(f5);

			GlStateManager.scale(2f, 2f, 2f);
			GlStateManager.translate(0.0625f, 0.03125f, -0.4375);
			if(te.conn_data!=null)
				InserterRenderer.renderItem.renderItem(te.conn_data, TransformType.GROUND);
			GlStateManager.translate(0.375f, 0.1875f, 0.375f);
			GlStateManager.scale(0.65f, 0.65f, 0.65f);
			if(te.conn_mv!=null)
				InserterRenderer.renderItem.renderItem(te.conn_mv, TransformType.GROUND);

			GlStateManager.popMatrix();
			return;

		}
		else
		{

			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();

			ClientUtils.bindTexture(texture);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			model.rotate(model.inserterGaugeArrow, 0, 0, 0);
			model.getBlockRotation(EnumFacing.NORTH, false);

			model.render();

			GlStateManager.pushMatrix();
			model.rotate(model.inserterOutput, 0f, -1.57079633F, 0f);
			for(ModelRendererTurbo mod : model.inserterInput)
				mod.render(1f/16f);


			model.rotate(model.inserterOutput, 0f, -1.57079633F, 0f);
			for(ModelRendererTurbo mod : model.inserterOutput)
				mod.render(1f/16f);

			GlStateManager.popMatrix();

			GlStateManager.scale(2f, 2f, 2f);
			GlStateManager.translate(0.0625f, 0.03125f, -0.4375);
			if(te.conn_data!=null)
				InserterRenderer.renderItem.renderItem(te.conn_data, TransformType.GROUND);
			GlStateManager.translate(0.375f, 0.1875f, 0.375f);
			GlStateManager.scale(0.65f, 0.65f, 0.65f);
			if(te.conn_mv!=null)
				InserterRenderer.renderItem.renderItem(te.conn_mv, TransformType.GROUND);


			GlStateManager.popMatrix();
			return;
		}
	}
}
