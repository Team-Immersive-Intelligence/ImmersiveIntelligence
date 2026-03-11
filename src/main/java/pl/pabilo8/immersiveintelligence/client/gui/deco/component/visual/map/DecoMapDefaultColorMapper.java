package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.chunk.IChunkOwnership;

/**
 * Default color mappers for DecoMapDisplay.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.12.2025
 */
public enum DecoMapDefaultColorMapper implements IDecoMapColorMapper
{
	/**
	 * Vanilla Minecraft map colors - uses the block's map color property.
	 */
	TERRAIN
			{
				@Override
				public int getColor(IBlockState state, BlockPos pos, World world, int sampleY)
				{
					MapColor mapColor = state.getMapColor(world, pos);
					if(mapColor==null)
						return 0x00000000; //Transparent for null colors
					int color;
					if(mapColor==MapColor.GRASS)
						color = world.getBiome(pos).getGrassColorAtPos(pos);
					else if(mapColor==MapColor.FOLIAGE)
						color = world.getBiome(pos).getFoliageColorAtPos(pos);
					else //Get the base color value
						color = mapColor.getMapColor(mapColor.colorIndex);

					//Apply the map color's built-in shading based on color index
					int colorIndex = mapColor.colorIndex;
					int shade = colorIndex%4;

					//Darken or lighten based on shade
					float shadeFactor = 0.7f+(shade*0.1f);
					return applyShading(color, shadeFactor);
				}
			},

	/**
	 * Grayscale mapper - converts everything to shades of gray.
	 * Lighter shades for harder blocks, darker for softer.
	 */
	GRAYSCALE
			{
				@Override
				public int getColor(IBlockState state, BlockPos pos, World world, int sampleY)
				{
					return 0xff121212;
				}

				@Override
				public int applyHeightShading(int color, int sampleY)
				{
					//For grayscale, use stronger height-based shading
					int shadeLevel = (sampleY%8+8)%8; //0-7 range
					float shadeFactor = 0.6f+(shadeLevel*0.15f);
					return applyShading(color, shadeFactor);
				}
			},

	/**
	 * Height-based mapper - colors based on elevation only.
	 * Creates a topographic map effect.
	 */
	TOPOGRAPHIC
			{
				@Override
				public int getColor(IBlockState state, BlockPos pos, World world, int sampleY)
				{
					sampleY -= world.getSeaLevel();
					//Skip air/water
					if(state.getBlock().isAir(state, world, pos)||state.getMaterial().isLiquid())
						return 0x00000000; //Transparent for air/water
					if(sampleY < 32)
						return 0xFF5D4037; //Dark brown
					else if(sampleY < 48)
						return 0xFF795548; //Brown
					else if(sampleY < 64)
						return 0xFF7CB342; //Green
					else if(sampleY < 80)
						return 0xFF9CCC65; //Light green
					else if(sampleY < 172)
						return 0xFFBCAAA4; //Light brown
					else
						return 0xFF9E9E9E; //Gray
				}

				@Override
				public int applyHeightShading(int color, int sampleY)
				{
					//For topographic, we already color by height, so minimal additional shading
					return color;
				}
			},

	/**
	 * Light source mapper - shows artificial light intensity (torches, glowstone, etc.).
	 * Ignores daylight, only shows block light levels (0-15).
	 */
	LIGHT
			{
				@Override
				public int getColor(IBlockState state, BlockPos pos, World world, int sampleY)
				{
					// Get block light level (0-15)
					int blockLight = world.getLight(pos, false);

					// Ignore sky light - we only care about artificial light
					if(blockLight==0)
					{
						return 0x00000000; // Transparent for no light
					}

					// Map light level to color: dark orange -> bright yellow
					float intensity = blockLight/15.0f;

					// Base color for artificial light: warm yellow/orange
					int r = (int)(255*intensity);
					int g = (int)(180*intensity);
					int b = (int)(50*intensity);

					// Add some glow effect based on intensity
					if(intensity > 0.7f)
					{
						// Very bright lights get a slight white core
						r = Math.min(255, r+30);
						g = Math.min(255, g+30);
					}

					// Alpha based on intensity (dimmer lights are more transparent)
					int alpha = 80+(int)(175*intensity);

					return (alpha<<24)|(r<<16)|(g<<8)|b;
				}

				@Override
				public int applyHeightShading(int color, int sampleY)
				{
					// For light mapper, we don't apply height shading
					return color;
				}
			},

	/**
	 * Factions mapper - shows grayscale terrain with faction-owned chunks colored by faction color.
	 */
	FACTIONS
			{
				@Override
				public int getColor(IBlockState state, BlockPos pos, World world, int sampleY)
				{
					Chunk chunk = world.getChunkFromBlockCoords(pos);
					IChunkOwnership ownership = DiplomacyUtils.getChunkOwnership(chunk);
					if(ownership==null||ownership.getOwner()==DiplomacyUtils.NEUTRAL)
						return getGrayscaleTerrainColor(state, pos, world, sampleY);

					IIColor factionColor = ownership.getOwner().getColor();
					int argb = factionColor.getPackedARGB();

					// Make faction colors semi-transparent so terrain shows through
					int alpha = (argb>>24)&0xFF;
					alpha = MathHelper.clamp(alpha, 128, 200); // Keep it somewhat transparent

					return (alpha<<24)|(argb&0x00FFFFFF);
				}

				private int getGrayscaleTerrainColor(IBlockState state, BlockPos pos, World world, int sampleY)
				{
					// Skip air/water - return transparent
					if(state.getBlock().isAir(state, world, pos)||state.getMaterial().isLiquid())
					{
						return 0x00000000;
					}

					// Use block hardness to determine gray level, but keep it in medium range
					float hardness = state.getBlockHardness(world, pos);
					float normalized = MathHelper.clamp(hardness/50f, 0f, 1f);
					float brightness = 0.3f+(normalized*0.4f); // Range: 0.3-0.7 (medium grays)

					int gray = (int)(brightness*255f);
					gray = MathHelper.clamp(gray, 80, 180); // Keep in medium gray range

					// Return ARGB (full opacity for terrain)
					return 0xFF000000|(gray<<16)|(gray<<8)|gray;
				}
			},

	/**
	 * Alliances mapper - shows terrain in grayscale, chunks colored by relation to player's faction.
	 * Own: blue, Allies: green, Neutral: yellow, Enemy: red, Unclaimed: grayscale.
	 */
	ALLIANCES
			{
				private final OwnerIdentity playerFaction = DiplomacyUtils.getLocalPlayerIdentity();

				@Override
				public int getColor(IBlockState state, BlockPos pos, World world, int sampleY)
				{
					Chunk chunk = world.getChunkFromBlockCoords(pos);
					IChunkOwnership ownership = DiplomacyUtils.getChunkOwnership(chunk);
					if(ownership==null)
						return getGrayscaleTerrainColor(state, pos, world, sampleY);

					//Color based on relation to player's faction
					OwnerIdentity owner = ownership.getOwner();
					switch(playerFaction.getRelationTowards(owner))
					{
						case MEMBER:
							return 0xBB3F76E4;
						case ALLIED:
							return 0xBB4CAF50; // Semi-transparent green
						case ENEMY:
							return 0xBBF44336;
						case NEUTRAL:
						default:
							return owner==DiplomacyUtils.NEUTRAL?getGrayscaleTerrainColor(state, pos, world, sampleY): 0xBBFFC107;
					}
				}

				private int getGrayscaleTerrainColor(IBlockState state, BlockPos pos, World world, int sampleY)
				{
					// Same as FACTIONS mapper - medium grays
					if(state.getBlock().isAir(state, world, pos)||state.getMaterial().isLiquid())
					{
						return 0x00000000;
					}

					float hardness = state.getBlockHardness(world, pos);
					float normalized = MathHelper.clamp(hardness/50f, 0f, 1f);
					float brightness = 0.3f+(normalized*0.4f);

					int gray = (int)(brightness*255f);
					gray = MathHelper.clamp(gray, 80, 180);

					return 0xFF000000|(gray<<16)|(gray<<8)|gray;
				}
			};


	@Override
	public String geLocaleKey()
	{
		return "ii.gui.map_display.color_mapper.";
	}
}
