package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.DecoMapDefaultColorMapper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.IDecoMapColorMapper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers.MapLayerBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers.MapLine;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers.MapRect;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.layers.MapSprite;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map.scanners.MapScanner;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

/**
 * Displays a custom map for a selected world region and allows drawing layered overlays
 * (rectangles and sprites) in world coordinates.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 27.12.2025
 */
@ParametersAreNonnullByDefault
public class DecoMapDisplay extends DecoComponent<DecoMapDisplay>
{
	//--- Region / Map backing --- //
	@Nullable
	private World world;
	private int centerX, centerZ;
	private int radiusBlocks = 64;
	/**
	 * Base zoom factor applied when sampling world coords into the map background.
	 */
	private float baseZoom = 1f;

	//--- Custom Map Data --- //
	@Nullable
	private CustomMapData customMapData;
	@Nullable
	private DynamicTexture mapTexture;
	private IDecoMapColorMapper colorMapper = DecoMapDefaultColorMapper.TERRAIN;
	private final List<MapScanner> scanners = new ArrayList<>();

	//--- Interaction --- //
	private boolean panningEnabled = false;
	private boolean zoomScrollEnabled = false;
	private float zoomMin = 0.5f;
	private float zoomMax = 1.0f;

	private float zoom = 1.0f, panX = 0.0f, panY = 0.0f;
	private int lastMouseX, lastMouseY;

	//--- Layers --- //
	private final Map<String, MapLayerBuilder> layers = new LinkedHashMap<>();

	public DecoMapDisplay(int x, int y)
	{
		super(x, y);
		withOnDragged(this::onMouseDragged);
		withOnPressed(this::onMousePressed);
		withOnTooltip(this::onTooltip);
		withOnScroll(this::onScroll);
	}

	//--- Setters ---//

	public DecoMapDisplay withRegion(World world, int centerX, int centerZ, int radiusBlocks)
	{
		this.world = world;
		this.centerX = centerX;
		this.centerZ = centerZ;
		this.radiusBlocks = Math.max(1, radiusBlocks);
		this.initialized = false;
		this.baseZoom = Math.min(width, height)/(float)this.radiusBlocks;
		return this;
	}

	/**
	 * Overload using the client's player world.
	 */
	public DecoMapDisplay withRegion(int centerX, int centerZ, int radiusBlocks)
	{
		return withRegion(ClientUtils.mc().world, centerX, centerZ, radiusBlocks);
	}

	/**
	 * Sets the color mapper for this map display.
	 */
	public DecoMapDisplay withColorMapper(IDecoMapColorMapper mapper)
	{
		this.colorMapper = mapper;
		this.initialized = false;
		return this;
	}

	/**
	 * Adds a scanner that will automatically detect and add markers to the map.
	 */
	public DecoMapDisplay withScanner(MapScanner scanner)
	{
		this.scanners.add(scanner);
		this.initialized = false;
		return this;
	}

	/**
	 * Removes all scanners of a specific type.
	 */
	public DecoMapDisplay removeScanner(String layerName)
	{
		scanners.removeIf(mapScanner -> mapScanner.layerName.equals(layerName));
		return this;
	}

	/**
	 * Clears all scanners.
	 */
	public DecoMapDisplay clearScanners()
	{
		scanners.clear();
		return this;
	}

	public DecoMapDisplay withPanning(boolean enabled)
	{
		this.panningEnabled = enabled;
		return this;
	}

	public DecoMapDisplay withZoomScrolling(double minZoom, double maxZoom)
	{
		this.zoomScrollEnabled = true;
		this.zoomMin = (float)Math.min(minZoom, maxZoom);
		this.zoomMax = (float)Math.max(minZoom, maxZoom);
		this.zoom = MathHelper.clamp(this.zoom, this.zoomMin, this.zoomMax);
		return this;
	}

	public DecoMapDisplay withBaseZoom(float baseZoom)
	{
		this.baseZoom = Math.max(0.0001f, baseZoom);
		this.initialized = false;
		return this;
	}

	/**
	 * Creates or returns a named layer builder.
	 */
	public MapLayerBuilder withLayer(String name)
	{
		return layers.computeIfAbsent(name, n -> new MapLayerBuilder(this));
	}

	@Override
	protected boolean initialize()
	{
		if(world==null)
		{
			customMapData = null;
			return true;
		}

		//Create custom map data instead of vanilla MapData
		String mapName = "deco_map_"+System.currentTimeMillis()+"_"+world.provider.getDimension();
		this.customMapData = new CustomMapData(mapName, centerX, centerZ, world.provider.getDimension(), radiusBlocks);
		this.updateMapData();

		//Create texture for rendering
		if(mapTexture!=null)
			mapTexture.deleteGlTexture();
		mapTexture = new DynamicTexture(radiusBlocks, radiusBlocks);
		updateTexture();

		return this.customMapData!=null;
	}

	private void updateTexture()
	{
		if(customMapData==null||mapTexture==null) return;

		int[] textureData = mapTexture.getTextureData();

		//Fill texture with map colors
		if(customMapData.rgbColors.length==textureData.length)
			System.arraycopy(customMapData.rgbColors, 0, textureData, 0, textureData.length);
		else //Patch for OptiFine
		{
			for(int i = 0; i < textureData.length; i++)
				textureData[i] = (i < customMapData.rgbColors.length)?customMapData.rgbColors[i]: 0x00000000;
		}

		mapTexture.updateDynamicTexture();
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		if(world==null||customMapData==null||mapTexture==null)
			return;

		//Update scanner data, they use their own intervals
		updateScanners();

		//Custom map background rendering
		Minecraft mc = ClientUtils.mc();
		GlStateManager.pushMatrix();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		parentGui.scissorStart(x, y, width, height);

		//Clip/transform into component rect.
		GlStateManager.translate(x, y, 0);

		//Center the map inside component, apply pan+zoom in pixel space.
		float viewW = width;
		float viewH = height;

		GlStateManager.translate(viewW/2f+panX, viewH/2f+panY, 0);
		GlStateManager.scale(zoom*baseZoom, zoom*baseZoom, 1f);
		GlStateManager.translate(-64f, -64f, 0);

		//Draw custom map texture
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(mc.getTextureManager().getDynamicTextureLocation("deco_map", mapTexture));
		IIDrawUtils.startTextured()
				.drawTexRect(0, 0, radiusBlocks, radiusBlocks, 0, 1, 0, 1)
				.finish();
		//Draw overlays
		drawLayers(customMapData, partialTicks);

		parentGui.scissorEnd();

		GlStateManager.popMatrix();
	}

	private void updateScanners()
	{
		if(world==null||customMapData==null) return;

		for(MapScanner scanner : scanners)
			scanner.update(this, world, customMapData);
	}

	private void drawLayers(CustomMapData data, float partialTicks)
	{
		//Compute mapping: world block -> map pixel (0..127).
		float blocksPerPixel = 1;
		float mapMinX = data.xCenter-64f*blocksPerPixel;
		float mapMinZ = data.zCenter-64f*blocksPerPixel;

		//Draw rectangles and sprites per layer (in insertion order).
		for(MapLayerBuilder layer : layers.values())
		{
			if(layer.usesNoiseShader)
				ShaderUtil.useShader(Shaders.NOISE, partialTicks);

			//Rects.
			GlStateManager.disableTexture2D();
			if(!layer.rectangles.isEmpty())
			{
				IIDrawUtils draw = IIDrawUtils.startColored();
				for(MapRect r : layer.rectangles)
				{
					float px0 = (r.fromX-mapMinX)/blocksPerPixel;
					float pz0 = (r.fromZ-mapMinZ)/blocksPerPixel;
					float px1 = (r.toX-mapMinX)/blocksPerPixel;
					float pz1 = (r.toZ-mapMinZ)/blocksPerPixel;

					float rx = Math.min(px0, px1);
					float ry = Math.min(pz0, pz1);
					float rw = Math.abs(px1-px0);
					float rh = Math.abs(pz1-pz0);

					//Clamp to map area.
					float cx = MathHelper.clamp(rx, 0, radiusBlocks);
					float cy = MathHelper.clamp(ry, 0, radiusBlocks);
					float cRight = MathHelper.clamp(rx+rw, 0, radiusBlocks);
					float cBottom = MathHelper.clamp(ry+rh, 0, radiusBlocks);

					float cw = Math.max(0, cRight-cx);
					float ch = Math.max(0, cBottom-cy);

					if(cw > 0&&ch > 0)
						draw.drawColorRect(cx, cy, cw, ch, r.color);
				}
				draw.finish();
			}

			if(!layer.lines.isEmpty())
			{
				GlStateManager.pushMatrix();
				GlStateManager.disableDepth();
				for(MapLine l : layer.lines)
				{
					GlStateManager.enableOutlineMode(l.color.getPackedRGB());
					//GlStateManager.glLineWidth(l.width);
					float px0 = (l.fromX-mapMinX)/blocksPerPixel;
					float pz0 = (l.fromZ-mapMinZ)/blocksPerPixel;
					float px1 = (l.toX-mapMinX)/blocksPerPixel;
					float pz1 = (l.toZ-mapMinZ)/blocksPerPixel;
					IIDrawUtils.startColoredLines()
							.drawColorLine(px0, pz0, px1, pz1, l.color)
							.finish();
				}
				GlStateManager.enableDepth();
				GlStateManager.popMatrix();
				GlStateManager.disableOutlineMode();
			}

			GlStateManager.enableTexture2D();
			bindAtlas();
			for(MapSprite s : layer.sprites)
			{
				float px = (s.worldX-mapMinX)/blocksPerPixel;
				float pz = (s.worldZ-mapMinZ)/blocksPerPixel;

				if(px < -32||px > 160||pz < -32||pz > 160)
					continue;

				float size = s.sizePx;
				float drawX = px-size/2f;
				float drawY = pz-size/2f;

				GlStateManager.pushMatrix();
				GlStateManager.translate(drawX+size/2f, drawY+size/2f, 0);
				GlStateManager.rotate(s.rotationDeg, 0, 0, 1);
				GlStateManager.translate(-(drawX+size/2f), -(drawY+size/2f), 0);

				IIDrawUtils draw = IIDrawUtils.startTexturedColored();
				if(s.usesBlockAtlas)
					bindAtlas();
				else
					IIClientUtils.bindTexture(s.location);

				draw.drawTexColorRect(drawX, drawY, size, size, s.color, s.uv[0], s.uv[1], s.uv[2], s.uv[3]);
				draw.finish();

				GlStateManager.popMatrix();
			}
			if(layer.usesNoiseShader)
				ShaderUtil.releaseShader();
		}
	}

	@Override
	public void cleanup()
	{
		layers.clear();
		customMapData = null;
		if(mapTexture!=null)
		{
			mapTexture.deleteGlTexture();
			mapTexture = null;
		}
	}

	private void updateMapData()
	{
		//Generate the map colors in RGB format
		if(world==null||customMapData==null)
			return;

		final int MAP_SIZE = radiusBlocks;
		final int blocksPerPixel = 1;

		//Initialize colors arrays if needed
		if(customMapData.rgbColors==null||customMapData.rgbColors.length!=MAP_SIZE*MAP_SIZE)
			customMapData.rgbColors = new int[MAP_SIZE*MAP_SIZE];

		//Prepare the color mapper
		colorMapper.prepare(world, this.radiusBlocks);

		float mapMinX = customMapData.xCenter-64f*blocksPerPixel;
		float mapMinZ = customMapData.zCenter-64f*blocksPerPixel;

		//Sample each map pixel with a 4x4 sub-sampling grid and pick the most common color
		for(int px = 0; px < MAP_SIZE; px++)
			for(int pz = 0; pz < MAP_SIZE; pz++)
			{
				int[] colorCounts = new int[256]; //Track frequency of RGB colors
				int[] colorValues = new int[256]; //Store actual RGB values
				Arrays.fill(colorValues, -1);

				int sumY = 0;
				int samples = 0;

				for(int sx = 0; sx < 4; sx++)
					for(int sz = 0; sz < 4; sz++)
					{
						float sampleXf = mapMinX+(px+(sx+0.5f)/4f)*blocksPerPixel;
						float sampleZf = mapMinZ+(pz+(sz+0.5f)/4f)*blocksPerPixel;
						int bx = MathHelper.floor(sampleXf);
						int bz = MathHelper.floor(sampleZf);

						int topY;
						try
						{
							topY = world.getHeight(bx, bz);
						} catch(Throwable t)
						{
							//Fallback if method not available / any error: assume 64
							topY = 64;
						}
						int sampleY = Math.max(0, topY-1);
						BlockPos pos = new BlockPos(bx, sampleY, bz);
						net.minecraft.block.state.IBlockState state = world.getBlockState(pos);

						//Get block color from the color mapper
						int blockColor = colorMapper.getColor(state, pos, world, sampleY);

						//Skip transparent colors
						if((blockColor&0xFF000000)==0)
							continue;

						//Apply height-based shading
						blockColor = colorMapper.applyHeightShading(blockColor, sampleY);

						//Track this color
						int colorHash = blockColor&0xFFFFFF; //Ignore alpha for counting
						if(colorValues[colorHash&0xFF]==-1)
							colorValues[colorHash&0xFF] = blockColor;
						colorCounts[colorHash&0xFF]++;
						sumY += sampleY;
						samples++;
					}

				//Choose the most frequent color in the samples
				int bestIndex = 0;
				int bestCount = 0;
				for(int i = 0; i < colorCounts.length; i++)
					if(colorCounts[i] > bestCount)
					{
						bestCount = colorCounts[i];
						bestIndex = i;
					}

				//Get the actual color value
				int finalColor;
				//Default to transparent if no samples
				if(bestCount > 0&&colorValues[bestIndex]!=-1)
					finalColor = colorValues[bestIndex];
				else
					finalColor = 0x00000000;

				customMapData.rgbColors[px+pz*MAP_SIZE] = finalColor;
			}

		//Update texture with new data
		updateTexture();
	}

	private void normalizeView()
	{
		assert world!=null&&customMapData!=null;

		float s = zoom*baseZoom;
		float half = radiusBlocks/2f;

		// Horizontal
		float mapScreenW = radiusBlocks*s;
		if(mapScreenW <= width)
			panX = 0f;
		else
		{
			float minPanX = width/2f-half*s;
			float maxPanX = -width/2f+half*s;
			panX = MathHelper.clamp(panX, minPanX, maxPanX);
		}

		// Vertical
		float mapScreenH = radiusBlocks*s;
		if(mapScreenH <= height)
			panY = 0f;
		else
		{
			float minPanY = height/2f-half*s;
			float maxPanY = -height/2f+half*s;
			panY = MathHelper.clamp(panY, minPanY, maxPanY);
		}
	}

	//--- Actions ---//

	private boolean onScroll(DecoMapDisplay gui, int mouseScroll, int mouseX, int mouseY)
	{
		if(!zoomScrollEnabled) return false;

		float step = 0.05f;
		zoom = MathHelper.clamp(zoom+Math.signum(mouseScroll)*step, zoomMin, zoomMax);
		normalizeView();
		return true;
	}

	private boolean onMousePressed(DecoMapDisplay gui, MouseButton button, int mouseX, int mouseY)
	{
		if(panningEnabled&&button==MouseButton.LEFT)
		{
			lastMouseX = mouseX;
			lastMouseY = mouseY;
			return true;
		}
		return false;
	}

	private boolean onMouseDragged(DecoMapDisplay gui, MouseButton button, int mouseX, int mouseY)
	{
		if(!panningEnabled||button!=MouseButton.LEFT)
			return false;

		int dx = mouseX-lastMouseX;
		int dy = mouseY-lastMouseY;
		panX += dx;
		panY += dy;

		lastMouseX = mouseX;
		lastMouseY = mouseY;
		normalizeView();
		return true;
	}

	private Collection<String> onTooltip(DecoMapDisplay decoMapDisplay)
	{
		return Collections.emptyList();
	}
}
