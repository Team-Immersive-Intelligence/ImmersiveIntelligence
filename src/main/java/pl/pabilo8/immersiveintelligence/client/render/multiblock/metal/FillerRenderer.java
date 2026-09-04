package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
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
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTItem;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFiller;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIMultiblockProcess;

import javax.annotation.Nonnull;

/**
 * Renders the Filler multiblock and its filling process.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 22.08.2026
 * @ii-approved 0.3.1
 * @since 1.05.2021
 */
@RegisteredTileRenderer(name = "multiblock/filler", clazz = TileEntityFiller.class)
public class FillerRenderer extends IIMultiblockRenderer<TileEntityFiller>
{
	AMTModel model;
	private IIBooleanAnimation active;
	private IIAnimationCompiledMap work, work2, fan;
	private AMTFillerBullet item, itemOut;

	@Override
	public void drawAnimated(TileEntityFiller te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		applyStandardMirroring(te, true);
		model.defaultize();

		//Active animation
		boolean canBeActive = !te.getRedstoneAtPos(0)&&te.energyStorage.getEnergyStored() > 0;
		active.apply(canBeActive);
		fan.apply(canBeActive?AMTUtils.getDebugProgress(100, partialTicks): 0);

		//Work animation
		if(!te.processQueue.isEmpty())
		{
			boolean hasSecondProcess = te.processQueue.size() > 1;

			//First process
			IIMultiblockProcess<FillerRecipe> recipe = te.processQueue.get(0);
			float progress = te.getProductionProgress(recipe, partialTicks);

			work.apply(progress);
			float transition = (progress-0.3f)/0.3f;

			//The work animation moves the first process from item to item_out at 2/3 progress.
			itemOut.setStack(recipe.recipe, transition);

			if(!hasSecondProcess)
				item.setStack(recipe.recipe, transition);

			//Second process, if present
			if(hasSecondProcess)
			{
				recipe = te.processQueue.get(1);
				progress = te.getProductionProgress(recipe, partialTicks);

				work2.apply(progress);
				item.setStack(recipe.recipe, progress);
			}
			else if(progress > 0.66f)
				item.setVisible(false);
		}

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
		fan = IIAnimationCompiledMap.create(this.model, ResLoc.of(IIReference.RES_II, "filler/fan"));
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
			transition = MathHelper.clamp(transition, 0f, 1f);

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
				GlStateManager.translate(2f, 0.25f*0.75f, -0.625f);
				GlStateManager.rotate(90f, 1.0F, 0.0F, 0.0F);

				boolean culling = GL11.glGetBoolean(GL11.GL_CULL_FACE);
				GlStateManager.disableCull();
				AMTItem.renderTransition(stackFrom, stackInto, TransformType.GROUND, transition, true);
				if(culling)
					GlStateManager.enableCull();
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
		protected AMT renamedCopy(String newName)
		{
			return this;
		}

		@Override
		public void disposeOf()
		{
			AMTUtils.disposeOf(bullet);
		}

		@Override
		@Nonnull
		public AxisAlignedBB getBoundingBox()
		{
			return new AxisAlignedBB(originPos, originPos);
		}
	}
}
