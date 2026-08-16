package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.scanners;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.CustomMapData;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoMapDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers.MapLayerBuilder;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.function.Supplier;

/**
 * Base class for automatic map scanners that detect blocks/entities and add markers.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.12.2025
 */
public abstract class MapScanner
{
	public final String layerName;
	protected int scanRange = 128; //Blocks
	protected int updateInterval = 100; //Ticks between full scans
	protected int lastScanTick = 0;

	//Marker styling
	protected ResLoc markerTexture;
	protected float markerSize = 8.0f;
	protected IIColor markerColor = IIColor.WHITE;
	protected float markerRotation = 0.0f;

	//Rectangle styling
	protected IIColor rectangleColor = IIColor.MC_RED.withAlpha(128);
	protected float rectangleBorderWidth = 1.0f;
	protected Supplier<Boolean> updateCondition = () -> true;
	private boolean usesNoiseShader = false;

	public MapScanner(String layerName)
	{
		this.layerName = layerName;
	}

	/**
	 * Called periodically to update scanner and add markers to the map.
	 */
	public void update(DecoMapDisplay mapDisplay, World world, CustomMapData mapData)
	{
		if(world==null)
			return;
		if(!updateCondition.get())
		{
			this.lastScanTick = (int)(world.getTotalWorldTime()-this.updateInterval);
			mapDisplay.withLayer(layerName)
					.clear();
			return;
		}

		//Check if it's time for a full scan
		if(world.getTotalWorldTime()-this.lastScanTick < this.updateInterval)
			return;
		this.lastScanTick = (int)world.getTotalWorldTime();

		//Get or create the layer
		MapLayerBuilder layer = mapDisplay.withLayer(this.layerName)
				.withNoiseShader(this.usesNoiseShader)
				.clear();

		//Calculate scan area based on map center and scale
		int mapRadiusBlocks = mapData.size/2;

		int minX = mapData.xCenter-mapRadiusBlocks;
		int maxX = mapData.xCenter+mapRadiusBlocks;
		int minZ = mapData.zCenter-mapRadiusBlocks;
		int maxZ = mapData.zCenter+mapRadiusBlocks;

		//Perform the scan
		scanArea(mapDisplay, world, mapData, layer, minX, maxX, minZ, maxZ);
	}

	/**
	 * Scan the specified area and add markers to the layer.
	 */
	protected abstract void scanArea(DecoMapDisplay mapDisplay, World world,
	                                 CustomMapData mapData,
	                                 MapLayerBuilder layer,
	                                 int minX, int maxX, int minZ, int maxZ);

	//--- Configuration methods ---

	public MapScanner withUpdateCondition(Supplier<Boolean> updateCondition)
	{
		this.updateCondition = updateCondition;
		return this;
	}

	public MapScanner withScanRange(int range)
	{
		this.scanRange = MathHelper.clamp(range, 16, 512);
		return this;
	}

	public MapScanner withUpdateInterval(int ticks, Supplier<Boolean> updateCondition)
	{
		this.updateInterval = Math.max(1, ticks);
		this.updateCondition = updateCondition;
		return this;
	}

	public MapScanner withMarkerStyle(ResLoc texture, float size, IIColor color)
	{
		this.markerTexture = texture;
		this.markerSize = size;
		this.markerColor = color;
		return this;
	}

	public MapScanner withMarkerRotation(float rotation)
	{
		this.markerRotation = rotation;
		return this;
	}

	public MapScanner withRectangleStyle(IIColor color, float borderWidth)
	{
		this.rectangleColor = color;
		this.rectangleBorderWidth = borderWidth;
		return this;
	}

	public MapScanner withNoiseShader()
	{
		this.usesNoiseShader = true;
		return this;
	}
}
