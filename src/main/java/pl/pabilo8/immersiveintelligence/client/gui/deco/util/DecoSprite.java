package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 06.01.2026
 */
public class DecoSprite
{
	private final ResLoc location;
	private final float[] localUV, mapUV;
	private final int sizeX, sizeY;
	private final boolean isAtlas, connected;

	private DecoSprite(ResLoc location, float[] localUV, float[] mapUV, int sizeX, int sizeY, boolean isAtlas, boolean connected)
	{
		this.location = location;
		this.localUV = localUV;
		this.mapUV = mapUV;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.isAtlas = isAtlas;
		this.connected = connected;
	}

	//--- Texture Sprite ---//

	public static DecoSprite textureSprite(ResLoc location, int sizeX, int sizeY, float[] localUV, boolean connected)
	{
		return new DecoSprite(location, localUV, localUV, sizeX, sizeY, false, connected);
	}

	public static DecoSprite textureSprite(ResLoc location, int sizeX, int sizeY, boolean connected)
	{
		return textureSprite(location, sizeX, sizeY, new float[]{0, 1, 0, 1}, connected);
	}

	public static DecoSprite textureSprite(ResLoc location, int sizeX, int sizeY)
	{
		return textureSprite(location, sizeX, sizeY, false);
	}

	//--- Atlas Sprite ---//

	public static DecoSprite atlasSprite(ResLoc location, int size, float[] localUV, boolean connected)
	{
		TextureAtlasSprite sprite = ClientUtils.getSprite(location);
		float[] mapUV = new float[]{
				sprite.getInterpolatedU(localUV[0]*16f),
				sprite.getInterpolatedU(localUV[1]*16f),
				sprite.getInterpolatedV(localUV[2]*16f),
				sprite.getInterpolatedV(localUV[3]*16f)
		};
		return new DecoSprite(location, localUV, mapUV, size, size, true, connected);
	}

	public static DecoSprite atlasSprite(ResLoc location, int size, boolean connected)
	{
		return atlasSprite(location, size, new float[]{0, 1, 0, 1}, connected);
	}

	public static DecoSprite atlasSprite(ResLoc location, int size)
	{
		return atlasSprite(location, size, false);
	}

	//--- Getters ---//

	public ResLoc getLocation()
	{
		return location;
	}

	public float[] getLocalUV()
	{
		return localUV;
	}

	public float[] getMapUV()
	{
		return mapUV;
	}

	public int getSizeX()
	{
		return sizeX;
	}

	public int getSizeY()
	{
		return sizeY;
	}

	public boolean isAtlas()
	{
		return isAtlas;
	}

	public boolean isConnected()
	{
		return connected;
	}
}
