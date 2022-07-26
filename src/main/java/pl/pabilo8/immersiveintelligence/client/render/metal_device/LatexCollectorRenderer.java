package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.LatexCollector;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityLatexCollector;

import static blusunrize.immersiveengineering.api.IEProperties.FACING_HORIZONTAL;

/**
 * @author Pabilo8
 * @since 30.08.2020
 */
public class LatexCollectorRenderer extends TileEntitySpecialRenderer<TileEntityLatexCollector>
{
	// TODO: 11.07.2022 adapt to AMT

	@Override
	public void render(TileEntityLatexCollector te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			if(!te.bucket.isEmpty())
			{

				float progress = MathHelper.clamp(1f-((te.bucketTime-partialTicks)/10f), 0, 1);

				GlStateManager.enableBlend();
				GlStateManager.pushMatrix();
				GlStateManager.enableAlpha();
				GlStateManager.color(1f, 1f, 1f, 0.25f);
				GlStateManager.translate(0, 0, 0);

				final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
				BlockPos blockPos = te.getPos();
				IBlockState state = getWorld().getBlockState(te.getPos()).withProperty(IEProperties.BOOLEANS[0], true).withProperty(FACING_HORIZONTAL, te.facing);
				IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState(state);

				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder worldRenderer = tessellator.getBuffer();

				RenderHelper.disableStandardItemLighting();
				GlStateManager.blendFunc(770, 771);
				GlStateManager.enableBlend();
				GlStateManager.disableCull();

				ClientUtils.bindAtlas();

				worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
				Vec3d vv = new Vec3d(blockPos).scale(-1).add(new Vec3d(0, 0.5, 0).add(new Vec3d(te.facing.getOpposite().getDirectionVec())).scale(0.5).scale((1-Math.min(progress, 1))));
				worldRenderer.setTranslation(vv.x, vv.y, vv.z);

				blockRenderer.getBlockModelRenderer().renderModel(te.getWorld(), model, state, blockPos, worldRenderer, true);
				worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
				tessellator.draw();

				float atime = te.timer;
				if(!te.bucket.isEmpty()&&te.timer==0&&te.bucket.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
				{
					IFluidHandlerItem cap = te.bucket.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
					if(cap!=null)
					{
						FluidStack drain = cap.drain(1000, false);
						atime += drain!=null&&drain.amount==1000?LatexCollector.collectTime: 0;
					}
				}
				float amount = Math.min((atime+(!te.bucket.isEmpty()&&te.isNextToTree()?(partialTicks*te.getIncomeModifier()): 0))/(float)LatexCollector.collectTime, 1f);
				if(progress >= 1&&amount > 0)
				{
					GlStateManager.translate(0.5, 0, 0.5);
					GlStateManager.rotate(-te.facing.getHorizontalAngle(), 0, 1, 0);
					GlStateManager.translate(-0.25, 0.3125+(0.655*amount), -0.125);
					GlStateManager.rotate(90, 1, 0, 0);
					GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);
					ClientUtils.drawRepeatedFluidSprite(new FluidStack(IIContent.fluidLatex, 1), 0f, 0f, 8, 8);
				}

				RenderHelper.enableStandardItemLighting();

				GlStateManager.popMatrix();
				GlStateManager.disableBlend();
			}

			GlStateManager.popMatrix();

		}
	}
}
