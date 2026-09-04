package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * Provides shared rendering helpers for Deco GUI components.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.09.2026
 */
@SideOnly(Side.CLIENT)
public final class DecoUtils
{
	private DecoUtils()
	{
	}

	/**
	 * Draws a Deco frame into an existing textured and colored draw batch.
	 *
	 * @param draw   draw batch to use
	 * @param x      frame x position
	 * @param y      frame y position
	 * @param width  frame width
	 * @param height frame height
	 * @param frame  frame style and side configuration
	 */
	public static void drawFrame(IIDrawUtils draw, int x, int y, int width, int height, DecoFrame frame)
	{
		if(frame.cornersOnly)
			drawFrameCorners(draw, x, y, width, height, frame.style, frame.color, frame.sides);
		else
			drawFrame(draw, x, y, width, height, frame.style, frame.color, frame.sides, frame.frameThickness);
	}

	private static void drawFrameCorners(IIDrawUtils draw, int x, int y, int width, int height, ResLoc style, IIColor color, boolean[] sides)
	{
		TextureAtlasSprite sprite = ClientUtils.getSprite(style);
		int cornerSize = 16;

		//Top-left corner
		if(sides[0]&&sides[3])
			draw.drawTexColorRect(x, y, cornerSize, cornerSize, color,
					sprite.getMinU(), sprite.getInterpolatedU(8), sprite.getMinV(), sprite.getInterpolatedV(8));
		//Top-right corner
		if(sides[0]&&sides[1])
			draw.drawTexColorRect(x+width-cornerSize, y, cornerSize, cornerSize, color,
					sprite.getInterpolatedU(16-8), sprite.getInterpolatedU(16), sprite.getMinV(), sprite.getInterpolatedV(8));
		//Bottom-left corner
		if(sides[2]&&sides[3])
			draw.drawTexColorRect(x, y+height-cornerSize, cornerSize, cornerSize, color,
					sprite.getMinU(), sprite.getInterpolatedU(8), sprite.getInterpolatedV(16-8), sprite.getInterpolatedV(16));
		//Bottom-right corner
		if(sides[2]&&sides[1])
			draw.drawTexColorRect(x+width-cornerSize, y+height-cornerSize, cornerSize, cornerSize, color,
					sprite.getInterpolatedU(16-8), sprite.getInterpolatedU(16), sprite.getInterpolatedV(16-8), sprite.getInterpolatedV(16));
	}

	private static void drawFrame(IIDrawUtils draw, int x, int y, int width, int height, ResLoc style, IIColor color, boolean[] sides, int frameThickness)
	{
		TextureAtlasSprite sprite = ClientUtils.getSprite(style);

		//Top-Left mappings
		float minU = sprite.getMinU();
		float minUU = sprite.getInterpolatedU(frameThickness/2f);
		float minV = sprite.getMinV();
		float minVV = sprite.getInterpolatedV(frameThickness/2f);
		//Bottom-Right mappings
		float maxU = sprite.getInterpolatedU(16-frameThickness/2f);
		float maxUU = sprite.getInterpolatedU(16);
		float maxV = sprite.getInterpolatedV(16-frameThickness/2f);
		float maxVV = sprite.getInterpolatedV(16);

		//Draw main frame

		//Top
		if(sides[0])
			draw.drawRepeatedTexColorRect(x+frameThickness, y, width-frameThickness*2, frameThickness, color,
					32-2*frameThickness, frameThickness, minUU, maxU, minV, minVV);
		//Bottom
		if(sides[1])
			draw.drawRepeatedTexColorRect(x+frameThickness, y+height-frameThickness, width-frameThickness*2, frameThickness, color,
					32-2*frameThickness, frameThickness, minUU, maxU, maxV, maxVV);
		//Left
		if(sides[2])
			draw.drawRepeatedTexColorRect(x, y+frameThickness, frameThickness, height-frameThickness*2, color,
					frameThickness, 32-2*frameThickness, minU, minUU, minVV, maxV);
		//Right
		if(sides[3])
			draw.drawRepeatedTexColorRect(x+width-frameThickness, y+frameThickness, frameThickness, height-frameThickness*2, color,
					frameThickness, 32-2*frameThickness, maxU, maxUU, minVV, maxV);

		//Draw squares on frame edges
		if(sides[0]||sides[3])
			draw.drawTexColorRect(x, y, frameThickness, frameThickness, color,
					minU, minUU, minV, minVV);
		if(sides[0]||sides[1])
			draw.drawTexColorRect(x+width-frameThickness, y, frameThickness, frameThickness, color,
					maxU, maxUU, minV, minVV);
		if(sides[2]||sides[3])
			draw.drawTexColorRect(x, y+height-frameThickness, frameThickness, frameThickness, color,
					minU, minUU, maxV, maxVV);
		if(sides[2]||sides[1])
			draw.drawTexColorRect(x+width-frameThickness, y+height-frameThickness, frameThickness, frameThickness, color,
					maxU, maxUU, maxV, maxVV);
	}
}
