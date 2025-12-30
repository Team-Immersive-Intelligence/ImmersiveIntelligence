package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 29.12.2025
 */
public class CustomMapData
{
	public final String mapName;
	public int xCenter, zCenter;
	public final int dimension;
	public int size;
	public int[] rgbColors; //Store colors as ARGB integers

	public CustomMapData(String mapName, int xCenter, int zCenter, int dimension, int size)
	{
		this.mapName = mapName;
		this.xCenter = xCenter;
		this.zCenter = zCenter;
		this.dimension = dimension;
		this.size = size;
		this.rgbColors = new int[this.size*this.size];
	}
}
