package pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.map;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.chunk.chunk.IChunkOwnership;

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
					//Skip air/water
					if(state.getBlock().isAir(state, world, pos)||state.getMaterial().isLiquid())
						return 0x00000000; //Transparent for air/water
					float heightFraction = (sampleY-world.getSeaLevel())/(float)(world.getHeight()-world.getSeaLevel());

					if(heightFraction <= -0.5)
						return 0xFF5D4037; //Dark brown
					else if(heightFraction <= 0)
						return 0xFF795548; //Brown
					else if(heightFraction < 0.25)
						return 0xFF7CB342; //Green
					else if(heightFraction < 0.5)
						return 0xFF9CCC65; //Light green
					else if(heightFraction < 0.75)
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
					float blockLight = 0;
					for(int x = -7; x < 7; x++)
						for(int z = -7; z < 7; z++)
							blockLight = Math.max(world.getLight(pos.add(x, world.getHeight(x, z), z).down())*MathHelper.sqrt(x*x+z*z), blockLight);

					if(blockLight==0)
						return 0x00000000;

					return IIColor.MC_YELLOW.withAlpha(1f-(blockLight/16f)).getPackedARGB();
				}

				@Override
				public int applyHeightShading(int color, int sampleY)
				{
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
					DiplomacyHandler diplomacy = DiplomacyHandler.getInstance(true);
					IChunkOwnership ownership = diplomacy.getChunkOwnership(chunk);
					if(ownership==null||ownership.getOwner()==DiplomacyHandler.NEUTRAL)
						return getGrayscaleTerrainColor(state, pos, world, sampleY);

					IIColor factionColor = ownership.getOwner().getColor();
					int argb = factionColor.getPackedARGB();

					//Make faction colors semi-transparent so terrain shows through
					int alpha = (argb>>24)&0xFF;
					alpha = MathHelper.clamp(alpha, 128, 200); //Keep it somewhat transparent

					return (alpha<<24)|(argb&0x00FFFFFF);
				}

				private int getGrayscaleTerrainColor(IBlockState state, BlockPos pos, World world, int sampleY)
				{
					//Skip air/water - return transparent
					if(state.getBlock().isAir(state, world, pos)||state.getMaterial().isLiquid())
						return 0x00000000;

					//Use block hardness to determine gray level, but keep it in medium range
					float hardness = state.getBlockHardness(world, pos);
					float normalized = MathHelper.clamp(hardness/50f, 0f, 1f);
					float brightness = 0.3f+(normalized*0.4f); //Range: 0.3-0.7 (medium grays)

					int gray = (int)(brightness*255f);
					gray = MathHelper.clamp(gray, 80, 180); //Keep in medium gray range

					//Return ARGB (full opacity for terrain)
					return 0xFF000000|(gray<<16)|(gray<<8)|gray;
				}
			},

	/**
	 * Alliances mapper - shows terrain in grayscale, chunks colored by relation to player's faction.
	 * Own: blue, Allies: green, Neutral: yellow, Enemy: red, Unclaimed: grayscale.
	 */
	ALLIANCES
			{
				private final OwnerIdentity playerFaction = DiplomacyHandler.getLocalPlayerIdentity();

				@Override
				public int getColor(IBlockState state, BlockPos pos, World world, int sampleY)
				{
					Chunk chunk = world.getChunkFromBlockCoords(pos);
					IChunkOwnership ownership = DiplomacyHandler.getInstance(true).getChunkOwnership(chunk);
					if(ownership==null)
						return getGrayscaleTerrainColor(state, pos, world, sampleY);

					//Color based on relation to player's faction
					OwnerIdentity owner = ownership.getOwner();
					switch(playerFaction.getRelationTowards(owner))
					{
						case MEMBER:
							return 0xBB3F76E4;
						case ALLIED:
							return 0xBB4CAF50; //Semi-transparent green
						case ENEMY:
							return 0xBBF44336;
						case NEUTRAL:
						default:
							return owner==DiplomacyHandler.NEUTRAL?getGrayscaleTerrainColor(state, pos, world, sampleY): 0xBBFFC107;
					}
				}

				private int getGrayscaleTerrainColor(IBlockState state, BlockPos pos, World world, int sampleY)
				{
					//Same as FACTIONS mapper - medium grays
					if(state.getBlock().isAir(state, world, pos)||state.getMaterial().isLiquid())
						return 0x00000000;

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
