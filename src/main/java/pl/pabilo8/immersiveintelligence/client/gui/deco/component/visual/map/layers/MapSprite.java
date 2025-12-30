package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers;

import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 28.12.2025
 */
public class MapSprite
{
	public final ResourceLocation location;
	public final boolean usesBlockAtlas;
	public final int worldX, worldZ;
	public final float sizePx;
	public final IIColor color;
	public final float[] uv;
	public final float rotationDeg;

	MapSprite(ResourceLocation location, boolean usesBlockAtlas, int worldX, int worldZ, float sizePx, IIColor color, float[] uv, float rotationDeg)
	{
		this.location = location;
		this.usesBlockAtlas = usesBlockAtlas;
		this.worldX = worldX;
		this.worldZ = worldZ;
		this.sizePx = sizePx;
		this.color = color;
		this.uv = uv;
		this.rotationDeg = rotationDeg;
	}
}
