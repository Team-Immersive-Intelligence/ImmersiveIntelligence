package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.metal_device.ModelInserter;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityInserter;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.inserter;

/**
 * Created by Pabilo8 on 2019-05-26.
 */
@SideOnly(Side.CLIENT)
public class InserterRenderer extends TileEntitySpecialRenderer<TileEntityInserter>
{
	static RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelInserter model = new ModelInserter();

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

			//Fuck Minecraft direction system, really, fuck it
			float dir = 180-te.armDirection, next_dir = 180-te.nextDirection;

			float added = next_dir > dir?(100f/inserter.grabTime*(partialTicks/20f)):
					(next_dir < dir?-(100f/inserter.grabTime*(partialTicks/20f)): 0);
			float progress = 1f-(((float)te.pickProgress+(added))/100f);

			dir += added;

			GlStateManager.pushMatrix();

			GlStateManager.translate(0.5f, 0.375f, -0.5);
			GlStateManager.rotate(dir, 0, 1, 0);

			for(ModelRendererTurbo mod : model.inserterBaseTurntable)
				mod.render(0.0625f);

			GlStateManager.translate(0f, 0.125f, 0);
			GlStateManager.rotate(15+55*progress, 1, 0, 0);

			for(ModelRendererTurbo mod : model.inserterLowerArm)
				mod.render(0.0625f);

			GlStateManager.translate(0f, 0.875f, 0);
			GlStateManager.rotate(135-(95f*progress), 1, 0, 0);
			GlStateManager.translate(0f, 0.0625f, 0.03125f);

			for(ModelRendererTurbo mod : model.inserterMidAxle)
				mod.render(0.0625f);

			for(ModelRendererTurbo mod : model.inserterUpperArm)
				mod.render(0.0625f);

			GlStateManager.translate(0f, 0.625f, 0.03125f);

			GlStateManager.pushMatrix();

			GlStateManager.translate(0.125f, -0.03125f, -0.03125f);

			GlStateManager.rotate(-45f*progress, 0f, 0f, 1f);

			for(ModelRendererTurbo mod : model.inserterItemPicker1)
				mod.render(0.0625f);

			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();

			GlStateManager.translate(-0.125f, -0.03125f, -0.03125f);

			GlStateManager.rotate(45f*progress, 0f, 0f, 1f);

			for(ModelRendererTurbo mod : model.inserterItemPicker2)
				mod.render(0.0625f);

			GlStateManager.popMatrix();

			renderItem.renderItem(te.insertionHandler.getStackInSlot(0), TransformType.GROUND);

			GlStateManager.popMatrix();

			GlStateManager.scale(2f, 2f, 2f);
			GlStateManager.translate(0.0625f, 0.03125f, -0.4375);
			if(te.conn_data!=null)
				renderItem.renderItem(te.conn_data, TransformType.GROUND);
			GlStateManager.translate(0.375f, 0.1875f, 0.375f);
			GlStateManager.scale(0.65f, 0.65f, 0.65f);
			if(te.conn_mv!=null)
				renderItem.renderItem(te.conn_mv, TransformType.GROUND);

			//EntityRenderer.drawNameplate(this.getFontRenderer(), dir+" / "+te.armDirection+" / "+te.nextDirection, 0, 1, 0, 0, 0, 0, true, false);

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


			model.getBlockRotation(EnumFacing.NORTH, model);
			model.render();

			float progress = 0.15f;

			GlStateManager.pushMatrix();

			GlStateManager.translate(0.5f, 0.375f, -0.5);
			//GlStateManager.rotate(180,0,1,0);

			for(ModelRendererTurbo mod : model.inserterBaseTurntable)
				mod.render(0.0625f);

			GlStateManager.translate(0f, 0.125f, 0);
			GlStateManager.rotate(15+55*progress, 1, 0, 0);

			for(ModelRendererTurbo mod : model.inserterLowerArm)
				mod.render(0.0625f);

			GlStateManager.translate(0f, 0.875f, 0);
			GlStateManager.rotate(135-(95f*progress), 1, 0, 0);
			GlStateManager.translate(0f, 0.0625f, 0.03125f);

			for(ModelRendererTurbo mod : model.inserterMidAxle)
				mod.render(0.0625f);

			for(ModelRendererTurbo mod : model.inserterUpperArm)
				mod.render(0.0625f);

			GlStateManager.translate(0f, 0.625f, 0.03125f);

			GlStateManager.pushMatrix();

			GlStateManager.translate(0.125f, -0.03125f, -0.03125f);

			GlStateManager.rotate(-45f*progress, 0f, 0f, 1f);

			for(ModelRendererTurbo mod : model.inserterItemPicker1)
				mod.render(0.0625f);

			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();

			GlStateManager.translate(-0.125f, -0.03125f, -0.03125f);

			GlStateManager.rotate(45f*progress, 0f, 0f, 1f);

			for(ModelRendererTurbo mod : model.inserterItemPicker2)
				mod.render(0.0625f);

			GlStateManager.popMatrix();

			GlStateManager.popMatrix();


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
