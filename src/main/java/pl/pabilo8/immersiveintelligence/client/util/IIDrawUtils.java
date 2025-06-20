package pl.pabilo8.immersiveintelligence.client.util;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import java.util.function.BiConsumer;

/**
 * <p>
 * This class provides efficient drawing of multiple rects using {@link net.minecraft.client.renderer.BufferBuilder}.<br>
 * When rendering a single rect, use methods from {@link pl.pabilo8.immersiveintelligence.client.IIClientUtils}, {@link net.minecraft.client.gui.Gui} or similar classes.<br>
 * </p>
 *
 * <pre> {@code
 * IIDrawUtils.startTextured(buf)
 *  .drawRect(1,2,3,4,0,0,1,1)
 *  .drawRect(5,6,7,8,0,0,0.5,1)
 *  .finish();
 * }</pre>
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 16.02.2023
 */
@SideOnly(Side.CLIENT)
public class IIDrawUtils
{
	/**
	 * Class is used in a builder-like fashion
	 */
	private static final IIDrawUtils INSTANCE = new IIDrawUtils();
	private VertexFormat format;
	private BufferBuilder buf;
	private Tessellator tes;
	private float offX, offY, rotation;

	//--- Begin Methods ---//
	private static IIDrawUtils start(BufferBuilder buf, VertexFormat format)
	{
		INSTANCE.format = format;
		INSTANCE.offX = 0;
		INSTANCE.offY = 0;
		INSTANCE.rotation = 0;
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

	public IIDrawUtils drawColorRect(float x, float y, float w, float h, IIColor color)
	{
		buf.pos(offX+x, offY+y+h, 0)
				.color(color.red, color.green, color.blue, color.alpha)
				.endVertex();
		buf.pos(offX+x+w, offY+y+h, 0)
				.color(color.red, color.green, color.blue, color.alpha)
				.endVertex();
		buf.pos(offX+x+w, offY+y, 0)
				.color(color.red, color.green, color.blue, color.alpha)
				.endVertex();
		buf.pos(offX+x, offY+y, 0)
				.color(color.red, color.green, color.blue, color.alpha)
				.endVertex();
		return this;
	}

	public IIDrawUtils drawColorGradient(int x, float y, int w, int h, IIColor colorBottom, IIColor colorTop)
	{
		buf.pos(offX+x, offY+y+h, 0)
				.color(colorBottom.red, colorBottom.green, colorBottom.blue, colorBottom.alpha)
				.endVertex();
		buf.pos(offX+x+w, offY+y+h, 0)
				.color(colorBottom.red, colorBottom.green, colorBottom.blue, colorBottom.alpha)
				.endVertex();
		buf.pos(offX+x+w, offY+y, 0)
				.color(colorTop.red, colorTop.green, colorTop.blue, colorTop.alpha)
				.endVertex();
		buf.pos(offX+x, offY+y, 0)
				.color(colorTop.red, colorTop.green, colorTop.blue, colorTop.alpha)
				.endVertex();
		return this;
	}

	public IIDrawUtils drawTexColorRect(float x, float y, float w, float h, IIColor color, float... uv)
	{
		buf.pos(offX+x, offY+y+h, 0)
				.tex(uv[0], uv[3])
				.color(color.red, color.green, color.blue, color.alpha)
				.endVertex();
		buf.pos(offX+x+w, offY+y+h, 0)
				.tex(uv[1], uv[3])
				.color(color.red, color.green, color.blue, color.alpha)
				.endVertex();
		buf.pos(offX+x+w, offY+y, 0)
				.tex(uv[1], uv[2])
				.color(color.red, color.green, color.blue, color.alpha)
				.endVertex();
		buf.pos(offX+x, offY+y, 0)
				.tex(uv[0], uv[2])
				.color(color.red, color.green, color.blue, color.alpha)
				.endVertex();
		return this;
	}

	public void drawRepeatedColorRect(int x, int y, int width, int height, IIColor color, ResourceLocation texture, int tileSize)
	{
		for(int yy = 0; yy < height; yy += tileSize)
			for(int xx = 0; xx < width; xx += tileSize)
			{
				TextureAtlasSprite sprite = ClientUtils.getSprite(texture);
				drawTexColorRect(x+xx, y+yy,
						MathHelper.clamp(width-xx, 0, tileSize),
						MathHelper.clamp(height-yy, 0, tileSize),
						color,
						sprite.getMinU(), sprite.getInterpolatedU(Math.min(width-xx, tileSize)/2f),
						sprite.getMinV(), sprite.getInterpolatedV(Math.min(height-yy, tileSize)/2f)
				);
			}
	}

	public void drawRepeatedColorRect(int x, int y, int width, int height, IIColor color, ResourceLocation texture,
									  int tWidth, int tHeight, float... uv)
	{
		TextureAtlasSprite sprite = ClientUtils.getSprite(texture);
		float u = sprite.getInterpolatedU(uv[0]);
		float uu = sprite.getInterpolatedU(uv[1])-u;
		float v = sprite.getInterpolatedV(uv[2]);
		float vv = sprite.getInterpolatedV(uv[3])-v;

		for(int yy = 0; yy < height; yy += tHeight)
			for(int xx = 0; xx < width; xx += tWidth)
			{
				drawTexColorRect(x+xx, y+yy,
						MathHelper.clamp(width-xx, 0, tWidth),
						MathHelper.clamp(height-yy, 0, tHeight),
						color,
						u, u+uu*(Math.min(width-xx, tWidth)/2f/16f),
						v, v+vv*(Math.min(height-yy, tHeight)/2f/16f)
				);
			}
	}

	public IIDrawUtils drawConnectedColorRect(float x, float y, float w, float h, IIColor color,
											  int tWidth, int tHeight, float... uv)
	{
		float tw = w/tWidth;
		float th = h/tHeight;

		// Split into smaller parts
		if(tw < 2||th < 2)
		{
			if(tw < 2)
			{
				drawTexColorRect(x, y, w/2, h, color, uv[0], uv[0]+(uv[1]-uv[0])*tw/2, uv[2], uv[3]);
				drawTexColorRect(x+w/2, y, w/2, h, color, uv[1]-(uv[1]-uv[0])*tw/2, uv[1], uv[2], uv[3]);
			}
			else
			{
				drawTexColorRect(x, y, w, h/2, color, uv[0], uv[1], uv[2], uv[2]+(uv[3]-uv[2])*th/2);
				drawTexColorRect(x, y+h/2, w, h/2, color, uv[0], uv[1], uv[3]-(uv[3]-uv[2])*th/2, uv[3]);
			}
			return this;
		}

		for(int i = 0; i < tw; i++)
			for(int j = 0; j < th; j++)
			{
				float realW = Math.min(tWidth, w-i*tWidth);
				float realH = Math.min(tHeight, h-j*tHeight);
				drawTexColorRect(x+i*tWidth, y+j*tHeight,
						realW, realH, color,
						uv[0],
						uv[0]+(uv[1]-uv[0])*(Math.min(1, tw-i)),
						uv[2],
						uv[2]+(uv[3]-uv[2])*(Math.min(1, th-j))
				);
			}
		return this;
	}

	public IIDrawUtils drawConnectedColorRect(float x, float y, float w, float h, IIColor color,
											  int texSizeX, int texSizeY, int xMargin, int yMargin, float... uv)
	{
		int iSizeX = Math.min(texSizeX-2*xMargin, Math.min((int)w, texSizeX)/2);
		int iSizeY = Math.min(texSizeY-2*yMargin, Math.min((int)h, texSizeY)/2);
		float tStartX = (xMargin/(float)texSizeX)*(uv[1]-uv[0])+uv[0];
		float tStartY = (yMargin/(float)texSizeY)*(uv[3]-uv[2])+uv[2];

		for(int yy = 0; yy < h; yy += iSizeY)
		{
			int drawHeight = Math.min(iSizeY, (int)h-yy);
			boolean isTop = yy==0;
			boolean isBottom = yy+iSizeY >= h;
			float texY = isTop?uv[2]: (isBottom?uv[3]-(drawHeight/(float)texSizeY)*(uv[3]-uv[2]): tStartY);

			for(int xx = 0; xx < w; xx += iSizeX)
			{
				int drawWidth = Math.min(iSizeX, (int)w-xx);
				boolean isLeft = xx==0;
				boolean isRight = xx+iSizeX >= w;
				float texX = isLeft?uv[0]: (isRight?uv[1]-(drawWidth/(float)texSizeX)*(uv[1]-uv[0]): tStartX);

				drawTexColorRect(
						x+xx, y+yy, drawWidth, drawHeight, color,
						texX, texX+(drawWidth/(float)texSizeX)*(uv[1]-uv[0]),
						texY, texY+(drawHeight/(float)texSizeY)*(uv[3]-uv[2])
				);
			}
		}
		return this;
	}

	public IIDrawUtils drawConnectedColorRect(float x, float y, float w, float h, IIColor color, ResourceLocation spriteLocation,
											  int texSizeX, int texSizeY, int xMargin, int yMargin)
	{
		TextureAtlasSprite sprite = ClientUtils.getSprite(spriteLocation);
		return drawConnectedColorRect(x, y, w, h, color, texSizeX, texSizeY, xMargin, yMargin,
				sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
	}

	//--- Offset and Rotation ---//

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

	public IIDrawUtils addRotation(float angle)
	{
		finish();
		GlStateManager.translate(offX, offY, 0);
		rotation += angle;
		GlStateManager.rotate(angle, 0, 0, 1);
		offX = offY = 0;
		buf.begin(GL11.GL_QUADS, format);
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
