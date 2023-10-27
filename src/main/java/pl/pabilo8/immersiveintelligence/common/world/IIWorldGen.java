package pl.pabilo8.immersiveintelligence.common.world;

import com.google.common.collect.ArrayListMultimap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ores;
import pl.pabilo8.immersiveintelligence.common.IILogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Pabilo8 on 30-03-2020.
 * Copy of {@link blusunrize.immersiveengineering.common.world.IEWorldGen} with added dimension category support
 */
public class IIWorldGen implements IWorldGenerator
{
	public static ArrayList<OreGen> orespawnList = new ArrayList<>();
	public static ArrayList<Integer> oreDimBlacklistOverworld = new ArrayList<>();
	public static ArrayList<Integer> oreDimBlacklistNether = new ArrayList<>();
	public static ArrayList<Integer> oreDimBlacklistEnd = new ArrayList<>();
	public static HashMap<String, Boolean> retrogenMap = new HashMap<>();
	public static ArrayListMultimap<Integer, ChunkPos> retrogenChunks = ArrayListMultimap.create();

	public static IIWorldGenRubberTree worldGenRubberTree = new IIWorldGenRubberTree();

	public static OreGen addOreGen(String name, IBlockState state, int maxVeinSize, int minY, int maxY, int chunkOccurence, int weight, EnumOreType type)
	{
		Block block = type==EnumOreType.OVERWORLD?Blocks.STONE: type==EnumOreType.NETHER?Blocks.NETHERRACK: Blocks.END_STONE;
		OreGen gen = new OreGen(name, state, maxVeinSize, block, minY, maxY, chunkOccurence, weight, type);
		orespawnList.add(gen);
		return gen;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		this.generateOres(random, chunkX, chunkZ, world, true);
		this.generateTrees(random, chunkX, chunkZ, world, true);
	}

	private void generateTrees(Random random, int chunkX, int chunkZ, World world, boolean newGeneration)
	{
		if(Ores.genRubberTrees&&random.nextInt(Ores.genRubberTreesChance)==0)
		{
			final int x = chunkX * 16 + 8 + random.nextInt(16);
			final int z = chunkZ * 16 + 8 + random.nextInt(16);
			final BlockPos pos = new BlockPos(x, 64, z);
			final Biome biome = world.getBiomeForCoordsBody(pos);

			if(biome.isHighHumidity()&&biome.getTemperature(pos)>0.9)
				IIWorldGen.worldGenRubberTree.generate(world,random,world.getTopSolidOrLiquidBlock(pos));

		}
	}

	public void generateOres(Random random, int chunkX, int chunkZ, World world, boolean newGeneration)
	{
		int dim = world.provider.getDimension();
		for(OreGen gen : orespawnList)
		{
			EnumOreType type = gen.getType();
			if(((type==EnumOreType.OVERWORLD&&oreDimBlacklistOverworld.contains(dim))||
					(type==EnumOreType.NETHER&&oreDimBlacklistNether.contains(dim))||
					(type==EnumOreType.END&&oreDimBlacklistEnd.contains(dim))
			))
				break;
			if(newGeneration||retrogenMap.get("retrogen_"+gen.name))
				gen.generate(world, random, chunkX*16, chunkZ*16);
		}
	}

	@SubscribeEvent
	public void chunkSave(ChunkDataEvent.Save event)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		event.getData().setTag("ImmersiveIntelligence", nbt);
		nbt.setBoolean(IIConfig.Ores.retrogenKey, true);
	}

	@SubscribeEvent
	public void chunkLoad(ChunkDataEvent.Load event)
	{
		int dimension = event.getWorld().provider.getDimension();
		if((!event.getData().getCompoundTag("ImmersiveIntelligence").hasKey(IIConfig.Ores.retrogenKey))&&(Ores.retrogenPlatinum||Ores.retrogenSalt||Ores.retrogenTungsten||Ores.retrogenZinc||Ores.retrogenFluorite||Ores.retrogenPhosphorus))
		{
			if(IIConfig.Ores.retrogenLogFlagChunk)
				IILogger.info("Chunk "+event.getChunk().getPos()+" has been flagged for Ore RetroGeneration by II.");
			retrogenChunks.put(dimension, event.getChunk().getPos());
		}
	}

	@SubscribeEvent
	public void serverWorldTick(TickEvent.WorldTickEvent event)
	{
		if(event.side==Side.CLIENT||event.phase==TickEvent.Phase.START)
			return;
		int dimension = event.world.provider.getDimension();
		int counter = 0;
		List<ChunkPos> chunks = retrogenChunks.get(dimension);
		if(chunks!=null&&chunks.size() > 0)
			for(int i = 0; i < 2; i++)
			{
				chunks = retrogenChunks.get(dimension);
				if(chunks==null||chunks.size()==0)
					break;
				counter++;
				ChunkPos loc = chunks.get(0);
				long worldSeed = event.world.getSeed();
				Random fmlRandom = new Random(worldSeed);
				long xSeed = (fmlRandom.nextLong() >> 3);
				long zSeed = (fmlRandom.nextLong() >> 3);
				fmlRandom.setSeed(xSeed*loc.x+zSeed*loc.z^worldSeed);
				this.generateOres(fmlRandom, loc.x, loc.z, event.world, false);
				chunks.remove(0);
			}
		if(counter > 0&&IIConfig.Ores.retrogenLogRemaining)
			IILogger.info("Retrogen was performed on "+counter+" Chunks, "+Math.max(0, chunks.size())+" chunks remaining");
	}

	public enum EnumOreType
	{
		OVERWORLD,
		NETHER,
		END,
	}

	public static class OreGen
	{
		String name;
		WorldGenMinable mineableGen;
		int minY;
		int maxY;
		int chunkOccurence;
		int weight;
		EnumOreType type;

		public OreGen(String name, IBlockState state, int maxVeinSize, Block replaceTarget, int minY, int maxY, int chunkOccurence, int weight, EnumOreType type)
		{
			this.name = name;
			this.mineableGen = new WorldGenMinable(state, maxVeinSize, BlockMatcher.forBlock(replaceTarget));
			this.minY = minY;
			this.maxY = maxY;
			this.chunkOccurence = chunkOccurence;
			this.weight = weight;
			this.type = type;
		}

		public void generate(World world, Random rand, int x, int z)
		{
			BlockPos pos;
			for(int i = 0; i < chunkOccurence; i++)
				if(rand.nextInt(100) < weight)
				{
					pos = new BlockPos(x+rand.nextInt(16), minY+rand.nextInt(maxY-minY), z+rand.nextInt(16));
					mineableGen.generate(world, rand, pos);
				}
		}

		public EnumOreType getType()
		{
			return type;
		}
	}
}
