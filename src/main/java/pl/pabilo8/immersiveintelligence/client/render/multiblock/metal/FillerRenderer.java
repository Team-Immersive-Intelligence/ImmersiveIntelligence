package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.crafting.FillerRecipe;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIBooleanAnimation;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBullet.BulletState;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFiller;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIMultiblockProcess;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 28.12.2023
 * @ii-approved 0.3.1
 * @since 1.05.2021
 */
@RegisteredTileRenderer(name = "multiblock/filler", clazz = TileEntityFiller.class)
public class FillerRenderer extends IIMultiblockRenderer<TileEntityFiller>
{
	AMTModel model;
	private IIBooleanAnimation active;
	private IIAnimationCompiledMap work, work2;
	private AMTFillerBullet item, itemOut;

	@Override
	public void drawAnimated(TileEntityFiller te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		applyStandardMirroring(te, true);

		active.apply(!te.getRedstoneAtPos(0));

		//Work animation
		if(!te.processQueue.isEmpty())
		{
			boolean hasSecondProcess = te.processQueue.size() > 1;

			//First process
			IIMultiblockProcess<FillerRecipe> recipe = te.processQueue.get(0);
			float progress = te.getProductionProgress(recipe, partialTicks);

			work.apply(progress);
			(hasSecondProcess?itemOut: item).setStack(recipe.recipe, Math.min((progress-0.3f)/0.3f, 1f));

			//Second process, if present
			if(hasSecondProcess)
			{
				recipe = te.processQueue.get(1);
				progress = te.getProductionProgress(recipe, partialTicks);

				work2.apply(progress);
				item.setStack(recipe.recipe, Math.min((progress-0.3f)/0.3f, 1f));
			}

		}
		else
			model.defaultize();

		//Finally, render
		model.render(tes, buf);

		applyStandardMirroring(te, false);
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		active.apply(false);
		model.render(tes, buf);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model,
				h -> new AMT[]{
						item = new AMTFillerBullet("item", h),
						itemOut = new AMTFillerBullet("item_out", h)
				}
		);
		active = new IIBooleanAnimation(
				this.model.getPart("conveyor_on"),
				this.model.getPart("conveyor_off")
		);
		work = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "filler/work"));
		work2 = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "filler/work2"));
	}

	@Override
	protected void nullifyModels()
	{
		super.nullifyModels();
		model = AMTUtils.disposeOf(model);
	}

	/**
	 * Renders a bullet or regular item as fallback
	 */
	private static class AMTFillerBullet extends AMT
	{
		private final AMTBullet bullet;
		private boolean isBullet;
		private float transition;
		private ItemStack stackFrom, stackInto;

		public AMTFillerBullet(String name, AMTModelHeader header)
		{
			super(name, header);
			bullet = new AMTBullet(name, header, null);
		}

		public void setStack(FillerRecipe recipe, float transition)
		{
			//Special handling for ammo
			if(isBullet = recipe.getBullet()!=null)
			{
				bullet.setModel(AmmoRegistry.getGenericModel(recipe.getBullet()));
				bullet.withState(BulletState.CASING);
				bullet.withGunpowderPercentage(transition);
			}
			else
			{
				this.transition = transition;
				this.stackFrom = recipe.itemInput.getExampleStack();
				this.stackInto = recipe.itemOutput;
			}
		}

		@Override
		protected void draw(Tessellator tes, BufferBuilder buf)
		{
			if(isBullet)
				bullet.render(tes, buf);
			else
			{
				GlStateManager.translate(2f, .25f, -0.625f);
				GlStateManager.rotate(90f, 1.0F, 0.0F, 0.0F);

				if(transition==0)
					ClientUtils.mc().getRenderItem().renderItem(stackFrom, TransformType.GROUND);
				else if(transition==1)
					ClientUtils.mc().getRenderItem().renderItem(stackInto, TransformType.GROUND);
				else
				{
					float h0 = -.5f;
					float h1 = h0+transition;

					GL11.glEnable(GL11.GL_STENCIL_TEST);

					GlStateManager.colorMask(false, false, false, false);
					GlStateManager.depthMask(false);

					GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xFF);
					GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);

					GL11.glStencilMask(0xFF);
					GlStateManager.clear(GL11.GL_STENCIL_BUFFER_BIT);

					GlStateManager.rotate(ClientUtils.mc().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

					GlStateManager.disableTexture2D();
					buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
					ClientUtils.renderBox(buf, -.5, h0, -.5, .5, h1, .5);
					tes.draw();
					GlStateManager.enableTexture2D();

					GlStateManager.rotate(-(ClientUtils.mc().getRenderManager().playerViewY), 0.0F, 1.0F, 0.0F);

					GlStateManager.colorMask(true, true, true, true);
					GlStateManager.depthMask(true);

					GL11.glStencilMask(0x00);

					GL11.glStencilFunc(GL11.GL_EQUAL, 0, 0xFF);
					ClientUtils.mc().getRenderItem().renderItem(stackFrom, TransformType.GROUND);

					GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
					ClientUtils.mc().getRenderItem().renderItem(stackInto, TransformType.GROUND);

					GL11.glDisable(GL11.GL_STENCIL_TEST);
				}
			}
		}

		@Override
		public void defaultize()
		{
			super.defaultize();
			transition = 0f;
			stackFrom = ItemStack.EMPTY;
			stackInto = ItemStack.EMPTY;
			if(bullet!=null)
			{
				bullet.setModel(null);
				bullet.defaultize();
			}
		}

		@Override
		public void disposeOf()
		{
			AMTUtils.disposeOf(bullet);
		}
	}
}
