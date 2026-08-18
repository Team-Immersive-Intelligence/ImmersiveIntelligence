package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoMapDisplay;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 28.12.2025
 */
public class MapLayerBuilder
{
	private final DecoMapDisplay parent;
	public final List<MapRect> rectangles = new ArrayList<>();
	public final List<MapSprite> sprites = new ArrayList<>();
	public final List<MapLine> lines = new ArrayList<>();
	public boolean usesNoiseShader = false;

	public MapLayerBuilder(DecoMapDisplay parent)
	{
		this.parent = parent;
	}

	public DecoMapDisplay build()
	{
		return parent;
	}

	public MapLayerBuilder clear()
	{
		rectangles.clear();
		sprites.clear();
		lines.clear();
		return this;
	}

	/**
	 * Adds a world\-space rectangle overlay.
	 */
	public MapLayerBuilder addRectangle(int fromX, int fromZ, int toX, int toZ, IIColor color)
	{
		rectangles.add(new MapRect(fromX, fromZ, toX, toZ, color));
		return this;
	}


	/**
	 * Adds a frame overlay consisting of 4 rectangles.
	 */
	public MapLayerBuilder addRectangleFrame(int fromX, int fromZ, int toX, int toZ, IIColor color, IIColor outline)
	{
		addRectangle(fromX, fromZ, toX, toZ, color);
		addLine(fromX, fromZ, toX, fromZ, outline, 1);
		addLine(toX, fromZ, toX, toZ, outline, 1);
		addLine(toX, toZ, fromX, toZ, outline, 1);
		addLine(fromX, toZ, fromX, fromZ, outline, 1);
		return this;
	}

	/**
	 * Adds a sprite overlay (direct texture) at world coordinates.
	 * Uses UV 0..1 by default.
	 */
	public MapLayerBuilder withSprite(ResLoc location, int worldX, int worldZ, float sizePx, IIColor color)
	{
		return withSprite(location, false, worldX, worldZ, sizePx, 0f, color, 0f);
	}

	public MapLayerBuilder withSprite(ResLoc location, boolean usesBlockAtlas, int worldX, int worldZ, float sizePx, IIColor color, float rotationDeg)
	{
		return withSprite(location, usesBlockAtlas, worldX, worldZ, sizePx, 8f, color, rotationDeg);
	}

	public MapLayerBuilder withSprite(ResLoc location, boolean usesBlockAtlas, int worldX, int worldZ,
	                                  float sizePx, float textureSize, IIColor color, float rotationDeg)
	{
		float[] uv;
		if(usesBlockAtlas)
		{
			TextureAtlasSprite sprite = ClientUtils.getSprite(location);
			uv = new float[]{
					sprite.getMinU(),
					sprite.getMaxU(),
					sprite.getMinV(),
					sprite.getMaxV()
			};
		}
		else
			uv = new float[]{0f, 1f, 0f, 1f};

		sprites.add(new MapSprite(location, usesBlockAtlas, worldX, worldZ, sizePx, color, uv, rotationDeg));
		return this;
	}

	/**
	 * Sets whether this layer uses the noise shader.
	 *
	 * @param usesNoiseShader whether to use the noise shader
	 * @return this
	 */
	public MapLayerBuilder withNoiseShader(boolean usesNoiseShader)
	{
		this.usesNoiseShader = usesNoiseShader;
		return this;
	}

	/**
	 * Adds a line overlay between two world coordinates.
	 */
	public MapLayerBuilder addLine(int fromX, int fromZ, int toX, int toZ, IIColor color, float width)
	{
		lines.add(new MapLine(fromX, fromZ, toX, toZ, color, width));
		return this;
	}

	/**
	 * Adds a directional line from center to a point at given angle and distance.
	 */
	public MapLayerBuilder addDirectionalLine(int centerX, int centerZ, float angleDeg, float distance, IIColor color, float width)
	{
		//Convert polar coordinates (angle, distance) to cartesian
		double angleRad = Math.toRadians(angleDeg);
		int toX = centerX+(int)(distance*Math.cos(angleRad));
		int toZ = centerZ+(int)(distance*Math.sin(angleRad));

		return addLine(centerX, centerZ, toX, toZ, color, width);
	}
}
