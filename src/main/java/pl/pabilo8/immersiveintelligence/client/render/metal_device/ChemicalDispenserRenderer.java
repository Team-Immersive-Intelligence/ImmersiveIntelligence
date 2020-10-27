package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.ModelBlockBase;
import pl.pabilo8.immersiveintelligence.client.model.connector.ModelChemicalDispenser;
import pl.pabilo8.immersiveintelligence.client.render.direction_transformer.DirectionTransformerWallMountable;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityChemicalDispenser;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class ChemicalDispenserRenderer extends TileEntitySpecialRenderer<TileEntityChemicalDispenser>
{
	static RenderItem renderItem = ClientUtils.mc().getRenderItem();
	private static ModelChemicalDispenser model = new ModelChemicalDispenser();
	private static DirectionTransformerWallMountable t = new DirectionTransformerWallMountable();
	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/metal_device/chemical_dispenser.png";

	@Override
	public void render(TileEntityChemicalDispenser te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			t.startTileModel(x, y, z, texture);
			t.transformTileDirection(te.facing);

			model.render();

			GlStateManager.pushMatrix();

			GlStateManager.translate(0.125, 0.25f, -0.875);
			GlStateManager.scale(2f, 2f, 2f);

			if(TileEntityChemicalDispenser.conn_data!=null)
				renderItem.renderItem(TileEntityChemicalDispenser.conn_data, TransformType.GROUND);
			GlStateManager.translate(0.375f, 0.1875f, 0.375f);
			GlStateManager.scale(0.65f, 0.65f, 0.65f);
			if(TileEntityChemicalDispenser.conn_mv!=null)
				renderItem.renderItem(TileEntityChemicalDispenser.conn_mv, TransformType.GROUND);

			GlStateManager.popMatrix();

			GlStateManager.translate(0.5f, 0.5f, -0.5f);

			ClientUtils.bindTexture(texture);


			if(te.getFacing()==EnumFacing.UP)
			{
				GlStateManager.rotate(te.yaw, 1, 0, 0);
				GlStateManager.rotate(-te.pitch, 0, 0, 1);
			}
			else if(te.getFacing()==EnumFacing.DOWN)
			{
				GlStateManager.rotate(te.yaw, 1, 0, 0);
				GlStateManager.rotate(te.pitch, 0, 0, 1);
			}
			else if(te.getFacing()==EnumFacing.EAST)
			{
				GlStateManager.rotate(te.yaw, 0, 0, 1);
				GlStateManager.rotate(-te.pitch, 1, 0, 0);
			}
			else if(te.getFacing()==EnumFacing.WEST)
			{
				GlStateManager.rotate(-te.yaw, 0, 0, 1);
				GlStateManager.rotate(-te.pitch, 1, 0, 0);
			}
			else if(te.getFacing()==EnumFacing.NORTH)
			{
				GlStateManager.rotate(-te.yaw, 0, 0, 1);
				GlStateManager.rotate(-te.pitch, 1, 0, 0);
			}
			else if(te.getFacing()==EnumFacing.SOUTH)
			{
				GlStateManager.rotate(-te.yaw, 0, 0, 1);
				GlStateManager.rotate(te.pitch, 1, 0, 0);
			}

			for(ModelRendererTurbo mod : model.barrelModel)
				mod.render(0.0625f);

			t.endModel();
		}
		else
		{
			t.startItemModel(x, y, z+1, texture);
			ModelBlockBase.getCommonConnectorModelRotation(EnumFacing.DOWN, model);

			GlStateManager.scale(0.75f, 0.75f, 0.75f);
			GlStateManager.translate(0.25, 0, 0);
			model.render();

			GlStateManager.translate(0.5f, 0.5f, -0.5f);

			for(ModelRendererTurbo mod : model.barrelModel)
				mod.render(0.0625f);

			GlStateManager.popMatrix();
			return;
		}
	}
}
