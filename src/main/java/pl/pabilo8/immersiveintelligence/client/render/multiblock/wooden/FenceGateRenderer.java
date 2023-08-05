package pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity.TileEntityGateBase;

/**
 * @author Pabilo8
 * @since 21-06-2019
 */
public class FenceGateRenderer<T extends TileEntityGateBase<T>> extends TileEntitySpecialRenderer<T>
{
	//TODO: 16.07.2023 bring to AMT
	@Override
	public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			if(!te.formed||te.isDummy()||!te.getWorld().isBlockLoaded(te.getPos(), false))
				return;

			final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
			BlockPos blockPos = te.getPos();
			IBlockState state = getWorld().getBlockState(blockPos);

			state = state.getBlock().getActualState(state, getWorld(), blockPos);
			state = state.withProperty(IEProperties.DYNAMICRENDER, true);
			IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState(state);
			IBlockState fenceState = te.getFenceState(null);

			boolean[] connections = new boolean[8];

			if(te.hasWorld())
			{
				EnumFacing fR = te.mirrored?te.facing.rotateYCCW(): te.facing.rotateY();
				EnumFacing fL = te.mirrored?te.facing.rotateY(): te.facing.rotateYCCW();

				for(int i = 0; i < 4; i++)
				{
					connections[i] = Utils.canFenceConnectTo(te.getWorld(),
							blockPos.down().up(i), fL, state.getMaterial()
					);
					connections[i+4] = Utils.canFenceConnectTo(te.getWorld(),
							blockPos.down().up(i).offset(fR, 7), fR, state.getMaterial()
					);
				}
			}
			else
				connections = new boolean[]{false, false, false, false, false, false, false, false};


			/*IBlockState fenceStateR = te.getFenceState();
			IBlockState fenceStateL = te.getFenceState();
*/
			IBlockState fenceStateR = te.getFenceState(EnumFacing.WEST);
			IBlockState fenceStateL = te.getFenceState(EnumFacing.EAST);
			IBakedModel modelFence = blockRenderer.getBlockModelShapes().getModelForState(fenceState);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder worldRenderer = tessellator.getBuffer();

			ClientUtils.bindAtlas();
			GlStateManager.pushMatrix();
			GlStateManager.translate(x+0.5, y-1, z+0.5);
			//GlStateManager.translate(.5, 1.5, .5);

			float angle = MathHelper.clamp(te.gateAngle+(partialTicks*(te.open?3.5f: -6f)), 0f, 115f);

			RenderHelper.disableStandardItemLighting();
			GlStateManager.blendFunc(770, 771);
			GlStateManager.enableBlend();
			GlStateManager.disableCull();
			if(Minecraft.isAmbientOcclusionEnabled())
				GlStateManager.shadeModel(7425);
			else
				GlStateManager.shadeModel(7424);

			GlStateManager.rotate(te.facing.getHorizontalAngle()+(te.mirrored?0: 180), 0, 1, 0);

			if(te.facing.getAxis()==Axis.X)
				GlStateManager.translate(-7, 0, 0);


			GlStateManager.pushMatrix();

			// TODO: 23.12.2021 efficiency
			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			worldRenderer.setTranslation(-blockPos.getX()-0.5, -blockPos.getY(), -blockPos.getZ()-0.5);
			worldRenderer.color(255, 255, 255, 255);
			for(int i = 0; i < 4; i++)
				blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), modelFence,
						connections[te.facing.getAxis()==Axis.X?(i+4): i]?fenceStateR: fenceState,
						blockPos.up(i), worldRenderer, true);
			worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();

			GlStateManager.rotate(90+angle, 0, 1, 0);
			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			worldRenderer.setTranslation(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
			worldRenderer.color(255, 255, 255, 255);
			blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), model, state, blockPos, worldRenderer, true);
			worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();
			GlStateManager.popMatrix();

			GlStateManager.translate(7, 0, 0);

			GlStateManager.pushMatrix();

			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			worldRenderer.setTranslation(-blockPos.getX()-0.5, -blockPos.getY(), -blockPos.getZ()-0.5);
			worldRenderer.color(255, 255, 255, 255);
			for(int i = 0; i < 4; i++)
				blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), modelFence,
						connections[te.facing.getAxis()==Axis.X?i: (i+4)]?fenceStateL: fenceState,
						blockPos.up(i), worldRenderer, true);
			worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();

			GlStateManager.rotate(-90-angle, 0, 1, 0);
			worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			worldRenderer.setTranslation(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
			worldRenderer.color(255, 255, 255, 255);
			blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), model, state, blockPos, worldRenderer, true);
			worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
			tessellator.draw();
			GlStateManager.popMatrix();

			RenderHelper.enableStandardItemLighting();

			GlStateManager.popMatrix();
		}
	}
}
