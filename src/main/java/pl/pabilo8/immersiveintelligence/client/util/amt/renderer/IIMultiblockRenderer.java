package pl.pabilo8.immersiveintelligence.client.util.amt.renderer;

import blusunrize.immersiveengineering.api.IEProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTQuads;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 11.08.2023
 */
public abstract class IIMultiblockRenderer<T extends TileEntityMultiblockIIBase<T>> extends IITileRenderer<T>
{
	private Vec3d offset;
	private IBlockState baseState = null;
	private AMTQuads baseModel = null;

	public abstract void drawAnimated(T te, BufferBuilder buf, float partialTicks, Tessellator tes);

	public abstract void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes);

	@Override
	public void draw(T te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Draw a display for a non-existent tile entity (IE manual)
		if(!te.hasWorld())
		{
//			GlStateManager.translate(offset.x-1, offset.y-1, offset.z-1);

			GlStateManager.pushMatrix();
			if(baseModel!=null)
				baseModel.render(tes, buf);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, 1);
			GlStateManager.rotate(90, 0, 1, 0);
			drawSimple(buf, partialTicks, tes);
			GlStateManager.popMatrix();

		}
		//Draw an existing tile entity
		else
			drawAnimated(te, buf, partialTicks, tes);
	}

	@Override
	protected boolean shouldNotRender(T te)
	{
		return te==null||te.isDummy();
	}

	protected void applyStandardMirroring(T te, boolean start)
	{
		if(start)
		{
			applyStandardRotation(te.facing);
			if(te.getIsMirrored())
				mirrorRender();
		}
		else if(te.getIsMirrored())
			unMirrorRender();
	}

	/**
	 * @param yawAngle the angle to transform
	 * @param te       the multiblock tile entity
	 * @return a global yaw angle transformed for the render-space based on the tile entity's facing and mirroring, 0-360
	 */
	protected float getTransformedYaw(T te, float yawAngle)
	{
		float teAngle = te.facing.getHorizontalAngle();
		float angle = te.getIsMirrored()?(teAngle+yawAngle): (teAngle-yawAngle);
		if(te.facing.getAxis()==Axis.X&&te.getIsMirrored())
			angle = angle+180;

		return IIMath.positiveModulo(angle, 360);
	}

	@Override
	protected Tuple<IBlockState, IBakedModel> getModelFromBlockState(T te)
	{
		if(!te.hasWorld()&&baseState!=null)
		{
			BlockRendererDispatcher brd = AMTUtils.getBRD();
			baseState = baseState.withProperty(IEProperties.DYNAMICRENDER, true);
			return new Tuple<>(baseState, brd.getBlockModelShapes().getModelForState(baseState));
		}

		return super.getModelFromBlockState(te);
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(baseModel);
	}

	public void setFastMultiblockState(MultiblockStuctureBase<T> mb, IBlockState state)
	{
		this.baseState = state.withProperty(IEProperties.FACING_HORIZONTAL, EnumFacing.EAST);
		this.offset = new Vec3d(mb.getOffset());
		baseModel = new AMTQuads("batched", offset,
				AMTUtils.getBRD().getModelForState(baseState).getQuads(baseState, null, 0).toArray(new BakedQuad[0])
		);
	}
}
