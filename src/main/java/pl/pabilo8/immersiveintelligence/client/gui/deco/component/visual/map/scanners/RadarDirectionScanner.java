package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.scanners;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.CustomMapData;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoMapDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers.MapLayerBuilder;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.function.Supplier;

/**
 * Scanner for drawing radar direction lines on the map.
 * Gets direction from a supplier and draws a line from map center to edge.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.12.2025
 */
public class RadarDirectionScanner extends MapScanner
{
	private final Supplier<Integer> directionSupplier;
	private float lineLength = 100.0f; //Default length in blocks
	private IIColor lineColor = IIReference.COLOR_IMMERSIVE_ORANGE;
	private float lineWidth = 2.0f;
	private boolean drawToEdge = true;

	//For pulsing effect
	private boolean pulseEffect = true;
	private float pulseSpeed = 2.0f;
	private float minAlpha = 128;
	private float maxAlpha = 255;

	public RadarDirectionScanner(String layerName, Supplier<Integer> directionSupplier, Supplier<Boolean> updateCondition)
	{
		super(layerName);
		this.directionSupplier = directionSupplier;
		this.withUpdateInterval(1, updateCondition); //Update every tick for smooth animation
	}

	@Override
	public void update(DecoMapDisplay mapDisplay, World world, CustomMapData mapData)
	{
		if(!updateCondition.get()||world==null||directionSupplier==null)
			return;

		//Get current direction
		Integer direction = directionSupplier.get();
		if(direction==null) return;

		//Normalize angle to 0-360
		float angle = direction%360;
		if(angle < 0) angle += 360;

		//Get or create the layer
		MapLayerBuilder layer = mapDisplay.withLayer(layerName);
		layer.clear(); //Clear previous frame

		//Calculate line length
		float effectiveLength = lineLength;
		if(drawToEdge&&mapData!=null)
		{
			//Calculate distance to map edge in current direction
			float mapRadiusBlocks = mapData.size;
			effectiveLength = mapRadiusBlocks*0.9f; //Leave some margin
		}

		//Apply pulse effect if enabled
		IIColor effectiveColor = lineColor;
		if(pulseEffect)
		{
			float time = world.getTotalWorldTime()+Minecraft.getMinecraft().getRenderPartialTicks();
			float pulse = (MathHelper.sin(time*pulseSpeed*0.1f)+1.0f)*0.5f;
			int alpha = (int)(minAlpha+pulse*(maxAlpha-minAlpha));
			effectiveColor = lineColor.withAlpha(alpha);
		}

		//Get map center
		int centerX = mapData.xCenter;
		int centerZ = mapData.zCenter;

		//Draw main direction line
		layer.addDirectionalLine(centerX, centerZ, angle, effectiveLength, effectiveColor, lineWidth);
		layer.addDirectionalLine(centerX, centerZ, angle-1, effectiveLength, effectiveColor.withBrightness(0.7f), lineWidth);
		layer.addDirectionalLine(centerX, centerZ, angle-2, effectiveLength, effectiveColor.withBrightness(0.5f), lineWidth);

		//Optional: Draw cardinal direction indicators
		drawCardinalDirections(layer, centerX, centerZ, mapData);

		//Optional: Draw angle text (if we had text support)
		//drawAngleLabel(layer, centerX, centerZ, angle, effectiveLength);
	}

	private void drawCardinalDirections(MapLayerBuilder layer, int centerX, int centerZ, CustomMapData mapData)
	{
		float mapRadiusBlocks = mapData.size;
		float indicatorLength = mapRadiusBlocks*0.8f;
		//North
		layer.addDirectionalLine(centerX, centerZ, 0, indicatorLength,
				lineColor.withAlpha(100), 1.0f);
		//East
		layer.addDirectionalLine(centerX, centerZ, 90, indicatorLength,
				lineColor.withAlpha(100), 1.0f);
		//South
		layer.addDirectionalLine(centerX, centerZ, 180, indicatorLength,
				lineColor.withAlpha(100), 1.0f);
		//West
		layer.addDirectionalLine(centerX, centerZ, 270, indicatorLength,
				lineColor.withAlpha(100), 1.0f);
	}

	//--- Configuration methods ---

	public RadarDirectionScanner withLineLength(float length)
	{
		this.lineLength = Math.max(1.0f, length);
		return this;
	}

	public RadarDirectionScanner withLineColor(IIColor color)
	{
		this.lineColor = color;
		return this;
	}

	public RadarDirectionScanner withLineWidth(float width)
	{
		this.lineWidth = Math.max(0.5f, width);
		return this;
	}

	public RadarDirectionScanner withDrawToEdge(boolean drawToEdge)
	{
		this.drawToEdge = drawToEdge;
		return this;
	}

	public RadarDirectionScanner withPulseEffect(boolean enabled, float speed,
												 int minAlpha, int maxAlpha)
	{
		this.pulseEffect = enabled;
		this.pulseSpeed = Math.max(0.1f, speed);
		this.minAlpha = MathHelper.clamp(minAlpha, 0, 255);
		this.maxAlpha = MathHelper.clamp(maxAlpha, 0, 255);
		return this;
	}

	@Override
	protected void scanArea(DecoMapDisplay mapDisplay, World world,
							CustomMapData mapData,
							MapLayerBuilder layer,
							int minX, int maxX, int minZ, int maxZ)
	{
		//This method is not used for radar scanner
		//The update method handles everything
	}
}
