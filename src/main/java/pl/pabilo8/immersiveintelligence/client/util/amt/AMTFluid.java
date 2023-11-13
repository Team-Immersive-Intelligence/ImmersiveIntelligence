package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;

import javax.annotation.Nullable;

/**
 * For drawing fluids in tanks
 *
 * @author Pabilo8
 * @since 26.07.2022
 */
public class AMTFluid extends AMT
{
	/**
	 * Size in voxels
	 */
	private final Vec3d size;
	@Nullable
	private FluidStack stack = null;
	private float level = 1;
	private boolean flowing = false;

	public AMTFluid(String name, Vec3d originPos, Vec3d size)
	{
		super(name, originPos);
		this.size = size;
	}

	public AMTFluid(String name, IIModelHeader header, Vec3d size)
	{
		super(name, header);
		this.size = size;
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(stack==null)
			return;

		Fluid fluid = stack.getFluid();

		ClientUtils.bindAtlas();
		TextureAtlasSprite sprite = ClientUtils.getSprite(flowing?fluid.getFlowing(stack): fluid.getStill(stack));

		int col = fluid.getColor(stack);
		double y = size.y*level;
		double u = sprite.getMinU(), uu = sprite.getMaxU(), v = sprite.getMinV(), vv = v+((sprite.getMaxV()-v)*Math.min(1, y/sprite.getIconHeight()));

		double ux = u+((uu-u)*Math.min(1, (size.x)/sprite.getIconWidth())), uz = u+(uu*Math.min(1, (size.z)/sprite.getIconWidth()));

		GlStateManager.color((col>>16&255)/255.0f, (col>>8&255)/255.0f, (col&255)/255.0f, 1);

		GlStateManager.translate(-originPos.x/16f, originPos.y/16f, originPos.z/16f);

		BufferBuilder buff = Tessellator.getInstance().getBuffer();

		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		IIClientUtils.drawFace(buff, 0, y, 0, size.x, 0, 0, u, ux, v, vv);
		IIClientUtils.drawFace(buff, 0, y, size.z, size.x, 0, size.z, u, ux, v, vv);
		IIClientUtils.drawFace(buff, 0, y, 0, 0, 0, size.z, u, ux, v, vv);
		IIClientUtils.drawFace(buff, size.x, y, 0, size.x, 0, size.z, u, ux, v, vv);
//		IIClientUtils.drawFace(buff, 0, y, 0, -size.x, y, -size.z, u, ux, v, vv);
//		IIClientUtils.drawFace(buff, 0, 0, 0, -size.x, 0, -size.z, u, ux, v, vv);

		buff.pos(0, y, size.z).tex(u, vv).endVertex();
		buff.pos(size.x, y, size.z).tex(uu, vv).endVertex();
		buff.pos(size.x, y, 0).tex(uu, v).endVertex();
		buff.pos(0, y, 0).tex(u, v).endVertex();

		GlStateManager.disableCull();
		Tessellator.getInstance().draw();
		GlStateManager.enableCull();

		GlStateManager.color(1f, 1f, 1f);
	}

	@Override
	public void disposeOf()
	{

	}

	public void setLevel(float level)
	{
		this.level = level;
	}

	public void setFluid(@Nullable FluidStack stack)
	{
		this.stack = stack;
	}

	public void setFlowing(boolean flowing)
	{
		this.flowing = flowing;
	}
}
