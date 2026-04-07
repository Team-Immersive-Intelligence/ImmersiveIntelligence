package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * For drawing fluids in tanks
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.07.2022
 */
public class AMTFluid extends AMT
{
	@Nullable
	private FluidStack stack = null;
	/**
	 * Fill level in range [0..1].
	 */
	private float level = 1f;
	/**
	 * Maximum Y level among all layers.
	 */
	private float totalHeight = 0f;
	private boolean flowing = false;
	private final List<FluidLayer> layers = new ArrayList<>();

	public AMTFluid(String name, Vec3d originPos)
	{
		super(name, originPos);
	}

	public AMTFluid(String name, AMTModelHeader header)
	{
		super(name, header);
	}

	public AMTFluid withSize(Vec3d size)
	{
		this.layers.clear();
		this.totalHeight = 0f;
		withFluidLayer(0, 0, 0, size.x, size.z);
		withFluidLayer(size.y, 0, 0, size.x, size.z);
		return this;
	}

	public AMTFluid withFluidLayer(double yLevel, double xOffset, double zOffset, double xSize, double zSize)
	{
		this.layers.add(new FluidLayer(yLevel, xOffset, zOffset, xSize, zSize));
		this.layers.sort(Comparator.comparingDouble(l -> l.yLevel));
		recomputeTotalHeight();
		return this;
	}

	public AMTFluid withLevel(float level)
	{
		// callers typically pass tankAmount/capacity
		this.level = IIMath.clamp(level, 0f, 1f);
		return this;
	}

	public AMTFluid withFluid(@Nullable FluidStack stack)
	{
		this.stack = stack;
		return this;
	}

	public AMTFluid withFluidTank(@Nonnull FluidTank tank)
	{
		this.stack = tank.getFluid();
		this.level = this.stack==null?0f: IIMath.clamp(this.stack.amount/(float)tank.getCapacity(), 0f, 1f);
		return this;
	}

	public AMTFluid withFlowing(boolean flowing)
	{
		this.flowing = flowing;
		return this;
	}

	private void recomputeTotalHeight()
	{
		float max = 0f;
		for(FluidLayer layer : layers)
			max = (float)Math.max(max, layer.yLevel);
		this.totalHeight = max;
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(stack==null||layers.size() < 2)
			return;

		final float clampedLevel = IIMath.clamp(this.level, 0f, 1f);
		if(clampedLevel <= 0f||totalHeight <= 0f)
			return;

		Fluid fluid = stack.getFluid();
		final TextureAtlasSprite sprite = ClientUtils.getSprite(flowing?fluid.getFlowing(stack): fluid.getStill(stack));
		final double localHeight = this.totalHeight*clampedLevel;
		if(localHeight <= 0)
			return;

		//UV + color
		final IIColor color = IIColor.fromPackedRGB(fluid.getColor(stack));
		final double u0 = sprite.getMinU(), u1 = sprite.getMaxU();
		final double v0 = sprite.getMinV(), v1 = sprite.getMaxV();

		//Positions
		double topX0 = 0, topZ0 = 0, topX1 = 0, topZ1 = 0, topY = 0;
		double heightDrawn = 0;

		//Draw sides
		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
		for(int i = 0; i < layers.size()-1&&heightDrawn < localHeight; i++)
		{
			FluidLayer first = layers.get(i);
			FluidLayer last = layers.get(i+1);

			final double segmentHeight = last.yLevel-first.yLevel;
			final double layerHeight = Math.min(segmentHeight, localHeight-heightDrawn);
			if(layerHeight <= 0||segmentHeight <= 0)
				continue;
			final double fraction = layerHeight/segmentHeight;

			//base rectangle at this segment start
			final double x0 = first.xMin();
			final double z0 = first.zMin();
			final double x1b = first.xMax();
			final double z1b = first.zMax();

			//top face coordinates
			topX0 = IIMath.clampedLerp(first.xOffset, last.xOffset, fraction);
			topZ0 = IIMath.clampedLerp(first.zOffset, last.zOffset, fraction);
			topX1 = IIMath.clampedLerp(first.xOffset+first.xSize, last.xOffset+last.xSize, fraction);
			topZ1 = IIMath.clampedLerp(first.zOffset+first.zSize, last.zOffset+last.zSize, fraction);

			final double y0 = first.yLevel;
			topY = first.yLevel+layerHeight;

			//4 walls: north(-z), south(+z), west(-x), east(+x)
			putQuad(buf, color,
					x0, y0, z0, u0, v1,
					topX0, topY, topZ0, u0, v0,
					topX1, topY, topZ0, u1, v0,
					x1b, y0, z0, u1, v1,
					0, 0, -1);

			putQuad(buf, color,
					x1b, y0, z1b, u0, v1,
					topX1, topY, topZ1, u0, v0,
					topX0, topY, topZ1, u1, v0,
					x0, y0, z1b, u1, v1,
					0, 0, 1);

			putQuad(buf, color,
					x0, y0, z1b, u0, v1,
					topX0, topY, topZ1, u0, v0,
					topX0, topY, topZ0, u1, v0,
					x0, y0, z0, u1, v1,
					-1, 0, 0);

			putQuad(buf, color,
					x1b, y0, z0, u0, v1,
					topX1, topY, topZ0, u0, v0,
					topX1, topY, topZ1, u1, v0,
					x1b, y0, z1b, u1, v1,
					1, 0, 0);

			heightDrawn += layerHeight;
		}

		renderTopFace(buf, sprite, color, u0, v0, topX0, topZ0, topX1, topZ1, topY);

		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.translate(originPos.x, originPos.y, originPos.z);
		GlStateManager.scale(0.0625, 0.0625, 0.0625);

		ClientUtils.bindAtlas();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		tes.draw();
	}

	private static void renderTopFace(BufferBuilder buf, TextureAtlasSprite sprite, IIColor color,
									  double u0, double v0,
									  double x0, double z0, double x1, double z1, double y)
	{
		final double xMin = Math.min(x0, x1), xMax = Math.max(x0, x1);
		final double zMin = Math.min(z0, z1), zMax = Math.max(z0, z1);

		for(double x = xMin; x < xMax; x += 16)
			for(double z = zMin; z < zMax; z += 16)
			{
				final double dx = Math.min(xMax-x, 16);
				final double dz = Math.min(zMax-z, 16);

				final double x2 = x+dx;
				final double z2 = z+dz;

				final float uu = sprite.getInterpolatedU(dx);
				final float vv = sprite.getInterpolatedV(dz);

				putQuad(buf, color,
						x, y, z, u0, v0,
						x, y, z2, u0, vv,
						x2, y, z2, uu, vv,
						x2, y, z, uu, v0,
						0, 1, 0);
			}
	}

	private static void putQuad(BufferBuilder buf, IIColor color,
								double x0, double y0, double z0, double u0, double v0,
								double x1, double y1, double z1, double u1, double v1,
								double x2, double y2, double z2, double u2, double v2,
								double x3, double y3, double z3, double u3, double v3,
								int nx, int ny, int nz)
	{
		putVertex(buf, color, x0, y0, z0, u0, v0, nx, ny, nz);
		putVertex(buf, color, x1, y1, z1, u1, v1, nx, ny, nz);
		putVertex(buf, color, x2, y2, z2, u2, v2, nx, ny, nz);
		putVertex(buf, color, x3, y3, z3, u3, v3, nx, ny, nz);
	}

	private static void putVertex(BufferBuilder buf, IIColor color, double x, double y, double z, double u, double v, int nx, int ny, int nz)
	{
		buf.pos(x, y, z).tex(u, v)
				.color(color.red, color.green, color.blue, 255)
				.normal(nx, ny, nz)
				.endVertex();
	}

	@Override
	public void disposeOf()
	{

	}

	@Override
	public void applyProperties(EasyNBT nbt)
	{
		super.applyProperties(nbt);
		nbt.checkSetVec3D("size", this::withSize);
		nbt.checkSetString("fluid", f -> withFluid(new FluidStack(FluidRegistry.getFluid(f), 1000)));
		nbt.checkSetFloat("level", this::withLevel);
		nbt.checkSetBoolean("flowing", this::withFlowing);
	}


	@Override
	protected AMT renamedCopy(String newName)
	{
		AMTFluid clone = new AMTFluid(newName, originPos);
		clone.layers.addAll(this.layers);
		clone.flowing = this.flowing;
		clone.level = this.level;
		clone.stack = this.stack==null?null: this.stack.copy();
		return clone;
	}

	private static class FluidLayer
	{
		final double yLevel;
		final double xOffset;
		final double zOffset;
		final double xSize;
		final double zSize;

		private FluidLayer(double yLevel, double xOffset, double zOffset, double xSize, double zSize)
		{
			this.yLevel = yLevel;
			this.xOffset = xOffset;
			this.zOffset = zOffset;
			this.xSize = xSize;
			this.zSize = zSize;
		}

		double xMin()
		{
			return xOffset;
		}

		double zMin()
		{
			return zOffset;
		}

		double xMax()
		{
			return xOffset+xSize;
		}

		double zMax()
		{
			return zOffset+zSize;
		}
	}
}
