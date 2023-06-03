package pl.pabilo8.immersiveintelligence.client.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.function.BiConsumer;

/**
 * <p>
 * This class provides efficient drawing of multiple rects using {@link net.minecraft.client.renderer.BufferBuilder}.<br>
 * When rendering a single rect, use methods from {@link pl.pabilo8.immersiveintelligence.client.IIClientUtils}, {@link net.minecraft.client.gui.Gui} or similar classes.<br>
 * </p>
 *
 * <p>
 * <code>
 * IIDrawUtils.startTextured(1,2,3,4,0,0,1,1)<br>
 * .drawRect(5,6,7,8,0,0,1,1)<br>
 * .drawRect()<br>
 * .finish();<br>
 * </code>
 * </p>
 *
 * @author Pabilo8
 * @since 16.02.2023
 */
@SideOnly(Side.CLIENT)
public class IIDrawUtils
{
	public static final float[] NO_COLOR = {1f, 1f, 1f};

	/**
	 * Class is used in a builder-like fashion
	 */
	private static final IIDrawUtils INSTANCE = new IIDrawUtils();
	private VertexFormat format;
	private BufferBuilder buf;
	private Tessellator tes;
	private float offX, offY;

	//--- Begin Methods ---//
	private static IIDrawUtils start(BufferBuilder buf, VertexFormat format)
	{
		INSTANCE.format = format;
		INSTANCE.offX = 0;
		INSTANCE.offY = 0;
		INSTANCE.buf = buf;
		INSTANCE.tes = Tessellator.getInstance();
		INSTANCE.buf.begin(GL11.GL_QUADS, format);
		return INSTANCE;
	}

	/**
	 * Used for drawing textured rects
	 *
	 * @param buf BufferBuilder to be drawn on
	 */
	public static IIDrawUtils startTextured(BufferBuilder buf)
	{
		return start(buf, DefaultVertexFormats.POSITION_TEX);
	}

	/**
	 * Used for drawing textured rects<br>
	 * Uses the {@link Tessellator}'s buffer
	 */
	public static IIDrawUtils startTextured()
	{
		return startTextured(Tessellator.getInstance().getBuffer());
	}

	/**
	 * Used for drawing colored rects
	 *
	 * @param buf BufferBuilder to be drawn on
	 */
	public static IIDrawUtils startColored(BufferBuilder buf)
	{
		return start(buf, DefaultVertexFormats.POSITION_COLOR);
	}

	/**
	 * Used for drawing colored rects<br>
	 * Uses the {@link Tessellator}'s buffer
	 */
	public static IIDrawUtils startColored()
	{
		return startColored(Tessellator.getInstance().getBuffer());
	}

	/**
	 * Used for drawing textured and colored rects
	 *
	 * @param buf BufferBuilder to be drawn on
	 */
	public static IIDrawUtils startTexturedColored(BufferBuilder buf)
	{
		return start(buf, DefaultVertexFormats.POSITION_TEX_COLOR);
	}

	/**
	 * Used for drawing textured and colored rects<br>
	 * Uses the {@link Tessellator}'s buffer
	 */
	public static IIDrawUtils startTexturedColored()
	{
		return startTexturedColored(Tessellator.getInstance().getBuffer());
	}

	//--- Draw Methods ---//

	public IIDrawUtils drawTexRect(float x, float y, float w, float h, float... uv)
	{
		buf.pos(offX+x, offY+y+h, 0)
				.tex(uv[0], uv[3])
				.endVertex();
		buf.pos(offX+x+w, offY+y+h, 0)
				.tex(uv[1], uv[3])
				.endVertex();
		buf.pos(offX+x+w, offY+y, 0)
				.tex(uv[1], uv[2])
				.endVertex();
		buf.pos(offX+x, offY+y, 0)
				.tex(uv[0], uv[2])
				.endVertex();
		return this;
	}

	public IIDrawUtils drawColorRect(float x, float y, float w, float h, float r, float g, float b, float a)
	{
		buf.pos(offX+x, offY+y+h, 0)
				.color(r, g, b, a)
				.endVertex();
		buf.pos(offX+x+w, offY+y+h, 0)
				.color(r, g, b, a)
				.endVertex();
		buf.pos(offX+x+w, offY+y, 0)
				.color(r, g, b, a)
				.endVertex();
		buf.pos(offX+x, offY+y, 0)
				.color(r, g, b, a)
				.endVertex();
		return this;
	}

	public IIDrawUtils drawColorGradient(int x, float y, int w, int h, int colorBottom, int colorTop)
	{
		int r = (colorBottom>>16)&0x0ff, g = (colorBottom>>8)&0x0ff, b = colorBottom&0x0ff, a = colorBottom<<8;
		int r2 = (colorTop>>16)&0x0ff, g2 = (colorTop>>8)&0x0ff, b2 = colorTop&0x0ff, a2 = colorTop<<8;

		buf.pos(offX+x, offY+y+h, 0)
				.tex(0, 0)
				.color(r, g, b, a)
				.endVertex();
		buf.pos(offX+x+w, offY+y+h, 0)
				.tex(0, 0)
				.color(r, g, b, a)
				.endVertex();
		buf.pos(offX+x+w, offY+y, 0)
				.tex(0, 0)
				.color(r2, g2, b2, a2)
				.endVertex();
		buf.pos(offX+x, offY+y, 0)
				.tex(0, 0)
				.color(r2, g2, b2, a2)
				.endVertex();
		return this;
	}

	public IIDrawUtils drawTexColorRect(int x, float y, int w, int h, float[] argb, float... uv)
	{
		return drawTexColorRect(x, y, w, h, argb[0], argb[1], argb[2], 1f, uv);
	}

	public IIDrawUtils drawTexColorRect(float x, float y, float w, float h, float r, float g, float b, float a, float... uv)
	{
		buf.pos(offX+x, offY+y+h, 0)
				.tex(uv[0], uv[3])
				.color(r, g, b, a)
				.endVertex();
		buf.pos(offX+x+w, offY+y+h, 0)
				.tex(uv[1], uv[3])
				.color(r, g, b, a)
				.endVertex();
		buf.pos(offX+x+w, offY+y, 0)
				.tex(uv[1], uv[2])
				.color(r, g, b, a)
				.endVertex();
		buf.pos(offX+x, offY+y, 0)
				.tex(uv[0], uv[2])
				.color(r, g, b, a)
				.endVertex();
		return this;
	}

	//--- Offset ---//

	public IIDrawUtils setOffset(float x, float y)
	{
		this.offX = x;
		this.offY = y;
		return this;
	}

	public IIDrawUtils addOffset(float x, float y)
	{
		this.offX += x;
		this.offY += y;
		return this;
	}

	//--- Interrupt Method ---//

	public IIDrawUtils inBetween(BiConsumer<Integer, Integer> draw)
	{
		finish();
		draw.accept((int)offX, (int)offY);
		buf.begin(GL11.GL_QUADS, format);
		return this;
	}

	//--- End Method ---//

	public void finish()
	{
		tes.draw();
	}
}
