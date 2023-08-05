package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;

/**
 * AMT type for drawing IE wiring
 *
 * @author Pabilo8
 * @since 21.08.2022
 */
public class AMTWire extends AMT
{
	private Vec3d[] points;
	private int listID = -1;
	private int color;
	private float diameter;

	public AMTWire(String name, Vec3d originPos, Vec3d start, Vec3d end, int color, float diameter)
	{
		super(name, originPos);
		setConnection(start, end, color);
		this.diameter = diameter;
	}

	public AMTWire(String name, IIModelHeader header, Vec3d start, Vec3d end, int color, float diameter)
	{
		this(name, header.getOffset(name), start, end, color, diameter);
	}

	public void setConnection(Vec3d start, Vec3d end, int color)
	{
		disposeOf();
		points = ApiUtils.getConnectionCatenary(start, end, 1.02);
		this.color = color;

	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(listID!=-1)
			GlStateManager.callList(listID);
		else
		{
			listID = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(listID, GL11.GL_COMPILE);

			ClientUtils.mc().getTextureManager().bindTexture(new ResourceLocation("immersiveengineering:textures/blocks/wire.png"));

			GlStateManager.disableCull();

			BufferBuilder buffer = Tessellator.getInstance().getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			//float[] rgb = IIUtils.rgbIntToRGB(color);

			for(int i = 0; i < points.length-1; i++)
			{
				IIClientUtils.drawRope(buf, points[i].x, points[i].y, points[i].z, points[i+1].x, points[i+1].y, points[i+1].z, diameter, 0);
				IIClientUtils.drawRope(buf, points[i].x, points[i].y, points[i].z, points[i+1].x, points[i+1].y, points[i+1].z, 0, diameter);
			}

			buf.setTranslation(0, 0, 0);
			tes.draw();

			ClientUtils.bindAtlas();

			GL11.glEndList();
		}
	}

	@Override
	public void disposeOf()
	{
		if(listID!=-1)
			GlStateManager.glDeleteLists(listID, 1);
	}
}
