package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelInserter;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityInserter;

/**
 * Created by Pabilo8 on 2019-05-26.
 */
@SideOnly(Side.CLIENT)
public class InserterRenderer extends TileEntitySpecialRenderer<TileEntityInserter>
{
	static RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	private static ModelInserter model = new ModelInserter();
	private static ModelInserter model_normal = new ModelInserter();

	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/metal_device/inserter.png";

	@Override
	public void render(TileEntityInserter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate(x+1, y, z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			float f5 = 1f/16f;

			model.getBlockRotation(EnumFacing.NORTH, model);

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
					model.rotate(model.inserterOutput, 0f, 0, -1.57079633F);
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
					model.rotate(model.inserterInput, 0f, 0, -1.57079633F);
				}
				break;
			}

			model.render();

			model.rotate(model.inserterBaseTurntable, te.pickProgress/100f, 0f, 0f);

			GlStateManager.scale(2f, 2f, 2f);
			GlStateManager.translate(0.0625f, 0.03125f, -0.4375);
			if(te.conn_data!=null)
				renderItem.renderItem(te.conn_data, TransformType.GROUND);
			GlStateManager.translate(0.375f, 0.1875f, 0.375f);
			GlStateManager.scale(0.65f, 0.65f, 0.65f);
			if(te.conn_mv!=null)
				renderItem.renderItem(te.conn_mv, TransformType.GROUND);

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


			//model.copyModelAngles(model, model_normal);
			//model.copyModelPositions(model, model_normal);

			model.getBlockRotation(EnumFacing.NORTH, model);
			model.render();

			GlStateManager.scale(2f, 2f, 2f);
			GlStateManager.translate(0.0625f, 0.03125f, -0.4375);
			if(te.conn_data!=null)
				renderItem.renderItem(te.conn_data, TransformType.GROUND);
			GlStateManager.translate(0.375f, 0.1875f, 0.375f);
			GlStateManager.scale(0.65f, 0.65f, 0.65f);
			if(te.conn_mv!=null)
				renderItem.renderItem(te.conn_mv, TransformType.GROUND);

			GlStateManager.popMatrix();
			return;
		}
	}
}
