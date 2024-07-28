package pl.pabilo8.immersiveintelligence.api.ammo;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.common.IPlantable;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandlerInvulnerable;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandlerMetal;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @updated 17.03.2024
 * @ii-approved 0.3.1
 * @since 05-03-2020
 */
public class PenetrationRegistry
{
	/**
	 * Default penetration handler used as fallback
	 */
	private static final IPenetrationHandler DEFAULT = new PenetrationHandler(PenetrationHardness.ROCK, 1f, 150f, IIParticles.PARTICLE_DEBRIS_PEBBLE, null, null);
	/**
	 * Registry of PenetrationHandlers for entities
	 */
	private static final HashMap<Predicate<Entity>, IPenetrationHandler> REGISTERED_ENTITIES = new HashMap<>();
	/**
	 * Registry of PenetrationHandlers for blocks
	 */
	private static final HashMap<Predicate<IBlockState>, IPenetrationHandler> REGISTERED_BLOCKS = new HashMap<>();
	/**
	 * Registry of PenetrationHandlers for block materials
	 */
	private static final LinkedHashMap<Predicate<Material>, IPenetrationHandler> REGISTERED_MATERIALS = new LinkedHashMap<>();

	public static void init()
	{
		//Bedrock
		registerState(state -> state.getBlock().blockHardness==-1, new PenetrationHandlerInvulnerable(PenetrationHardness.BEDROCK, Integer.MAX_VALUE));
		//Fluids
		registerState(state -> state.getBlock().blockHardness==-1, new PenetrationHandlerInvulnerable(PenetrationHardness.FOLIAGE, 0f));

		//Fragile metals
		registerMetalMaterial(PenetrationHandlerMetal.create("aluminum", PenetrationHardness.FRAGILE, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("tin", PenetrationHardness.FRAGILE, 0.75f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("zinc", PenetrationHardness.FRAGILE, 0.75f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("copper", PenetrationHardness.FRAGILE, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("gold", PenetrationHardness.FRAGILE, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("silver", PenetrationHardness.FRAGILE, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("platinum", PenetrationHardness.FRAGILE, 1.0f, 150f));

		//More durable metals
		registerMetalMaterial(PenetrationHandlerMetal.create("brass", PenetrationHardness.WEAK_METAL, 1.25f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("bronze", PenetrationHardness.WEAK_METAL, 1.25f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("invar", PenetrationHardness.WEAK_METAL, 1.25f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("constantan", PenetrationHardness.WEAK_METAL, 1.33f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("electrum", PenetrationHardness.WEAK_METAL, 1.33f, 150f));

		//Steel and tougher
		registerMetalMaterial(PenetrationHandlerMetal.create("lead", PenetrationHardness.IRON, 1.33f, 200f));
		registerMetalMaterial(PenetrationHandlerMetal.create("iron", PenetrationHardness.IRON, 1.33f, 250f));
		registerMetalMaterial(PenetrationHandlerMetal.create("steel", PenetrationHardness.STEEL, 1.5f, 300f));
		registerMetalMaterial(PenetrationHandlerMetal.create("tungsten", PenetrationHardness.TUNGSTEN, 1.5f, 350f));
		REGISTERED_MATERIALS.put(material -> material==Material.ANVIL, PenetrationHandlerMetal.get("iron"));
		REGISTERED_MATERIALS.put(material -> material==Material.IRON, PenetrationHandlerMetal.get("iron"));

		//Multiblocks and machines
		PenetrationHandler penHandlerMechanical = new PenetrationHandler(PenetrationHardness.IRON,
				1f, 250, IIParticles.PARTICLE_DEBRIS_MECHANICAL, IISounds.hitMetal);
		PenetrationHandler penHandlerHeavyMachine = new PenetrationHandler(PenetrationHardness.STEEL,
				1.3f, 350, IIParticles.PARTICLE_DEBRIS_HEAVY_MACHINE, IISounds.hitMetal);
		PenetrationHandler penHandlerLightMachine = new PenetrationHandler(PenetrationHardness.STEEL,
				1.1f, 200, IIParticles.PARTICLE_DEBRIS_LIGHT_MACHINE, IISounds.hitMetal);

		batchRegisterHandler(penHandlerMechanical, IEContent.blockMetalMultiblock,
				IIContent.blockWoodenMultiblock,
				IIContent.blockMechanicalConnector, IIContent.blockGearbox, IIContent.blockMechanicalDevice, IIContent.blockMechanicalDevice1);
		batchRegisterHandler(penHandlerHeavyMachine, IEContent.blockMetalMultiblock,
				IIContent.blockMetalMultiblock0, IIContent.blockMetalMultiblock1);
		batchRegisterHandler(penHandlerLightMachine,
				IEContent.blockMetalDecoration0, IEContent.blockMetalDecoration1, IEContent.blockMetalDecoration2,
				IEContent.blockMetalDevice0, IEContent.blockMetalDevice1,
				IIContent.blockDataConnector, IIContent.blockMetalDevice, IIContent.blockMetalDevice1,
				IIContent.blockMetalDecoration);

		//Concrete
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "uberConcrete"),
				new PenetrationHandler(PenetrationHardness.UBERCONCRETE, 3f, 350, IIParticles.PARTICLE_DEBRIS_BRICK_BIG, IISounds.hitStone));
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "sturdyBricksConcrete"),
				new PenetrationHandler(PenetrationHardness.PANZERCONCRETE, 2f, 250, IIParticles.PARTICLE_DEBRIS_BRICK_BIG, IISounds.hitStone));
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "bricksConcrete"),
				new PenetrationHandler(PenetrationHardness.CONCRETE, 1.33f, 200, IIParticles.PARTICLE_DEBRIS_BRICK, IISounds.hitStone));
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "leadedConcrete"),
				new PenetrationHandler(PenetrationHardness.CONCRETE, 1.66f, 200, IIParticles.PARTICLE_DEBRIS_BRICK, IISounds.hitStone));
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "concrete"),
				new PenetrationHandler(PenetrationHardness.CONCRETE, 1f, 150, IIParticles.PARTICLE_DEBRIS_PEBBLE, IISounds.hitStone));

		//Bricks, Stone, Rocks
		registerState(state -> state.getBlock()==Blocks.BRICK_BLOCK||state.getBlock()==Blocks.BRICK_STAIRS,
				new PenetrationHandler(PenetrationHardness.BRICKS, 1f, 250, IIParticles.PARTICLE_DEBRIS_BRICK, IISounds.hitStone));
		registerState(state -> state.getBlock()==Blocks.STONEBRICK||state.getBlock()==Blocks.STONE_BRICK_STAIRS,
				new PenetrationHandler(PenetrationHardness.BRICKS, 1f, 250, IIParticles.PARTICLE_DEBRIS_BRICK, IISounds.hitStone));
		registerOre("stone", new PenetrationHandler(PenetrationHardness.ROCK, 1f, 200, IIParticles.PARTICLE_DEBRIS_PEBBLE, IISounds.hitStone));
		registerOre("cobblestone", new PenetrationHandler(PenetrationHardness.ROCK, 1f, 200, IIParticles.PARTICLE_DEBRIS_PEBBLE, IISounds.hitStone));
		registerOre("sandstone", new PenetrationHandler(PenetrationHardness.ROCK, 1f, 200, IIParticles.PARTICLE_DEBRIS_PEBBLE, IISounds.hitStone));

		//ground
		registerMaterial(Material.ROCK, new PenetrationHandler(PenetrationHardness.ROCK, 1f, 150, IIParticles.PARTICLE_DEBRIS_PEBBLE, IISounds.hitStone));
		registerMaterial(Material.GRASS, new PenetrationHandler(PenetrationHardness.GROUND, 1f, 150, IIParticles.PARTICLE_DEBRIS_PEBBLE, IISounds.hitGrass));
		registerMaterial(Material.GROUND, new PenetrationHandler(PenetrationHardness.GROUND, 1f, 150, IIParticles.PARTICLE_DEBRIS_PEBBLE, IISounds.hitDirt));
		registerMaterial(Material.SAND, new PenetrationHandler(PenetrationHardness.GROUND, 1f, 150, IIParticles.PARTICLE_DEBRIS_PEBBLE, IISounds.hitSand));

		//wood
		registerOre("planksWood",
				new PenetrationHandler(PenetrationHardness.WOOD, 0.8f, 100, IIParticles.PARTICLE_DEBRIS_PLANK, IISounds.hitWood));
		registerOre("logWood",
				new PenetrationHandler(PenetrationHardness.WOOD, 1f, 250, IIParticles.PARTICLE_DEBRIS_PLANK, IISounds.hitWood));

		//Glass
		registerOre("paneGlass", new PenetrationHandler(PenetrationHardness.FRAGILE, 0.125f, 20, IIParticles.PARTICLE_DEBRIS_GLASS, SoundEvents.BLOCK_GLASS_BREAK, null));
		registerMaterial(Material.GLASS, new PenetrationHandler(PenetrationHardness.FRAGILE, 0.75f, 80, IIParticles.PARTICLE_DEBRIS_GLASS, SoundEvents.BLOCK_GLASS_BREAK, null));

		//Wool
		registerMaterial(new Material[]{Material.CARPET, Material.CLOTH},
				new PenetrationHandler(PenetrationHardness.FRAGILE, 1f, 50, IIParticles.PARTICLE_DEBRIS_CLOTH, IISounds.impactFoliage, null));

		//leaves
		registerMaterial(new Material[]{Material.LEAVES, Material.VINE},
				new PenetrationHandler(PenetrationHardness.FRAGILE, 0.5f, 300, IIParticles.PARTICLE_DEBRIS_BRANCH_LEAF, IISounds.impactFoliage, null));
		registerMaterial(new Material[]{Material.CACTUS},
				new PenetrationHandler(PenetrationHardness.FRAGILE, 0.8f, 100, IIParticles.PARTICLE_DEBRIS_BRANCH_LEAF, IISounds.impactFoliage, null));


		//grass, crops, etc.
		registerState(state -> state.getMaterial()==Material.GRASS&&state.getBlock() instanceof IPlantable,
				new PenetrationHandler(PenetrationHardness.GROUND, 1f, 300, IIParticles.PARTICLE_DEBRIS_STRAW, IISounds.hitGrass));
		registerMaterial(Material.GOURD, new PenetrationHandler(PenetrationHardness.FRAGILE, 1f, 150, IIParticles.PARTICLE_DEBRIS_STRAW, IISounds.impactFoliage, null));

		//EntityLivingBase
		REGISTERED_ENTITIES.put(entity -> entity instanceof EntityLivingBase,
				new PenetrationHandler(PenetrationHardness.FRAGILE, 1f, 0, IIParticles.PARTICLE_DEBRIS_FLESH, IISounds.impactFlesh, null));
	}

	//--- Registration ---//

	/**
	 * Registers a new penetration handler for a given BlockState
	 *
	 * @param check   BlockState check
	 * @param handler penetration handler
	 */
	public static void registerState(Predicate<IBlockState> check, IPenetrationHandler handler)
	{
		REGISTERED_BLOCKS.put(check, handler);
	}

	/**
	 * Registers a new penetration handler for a given Material
	 *
	 * @param check   Material check
	 * @param handler penetration handler
	 */
	public static void registerMaterial(Material check, IPenetrationHandler handler)
	{
		REGISTERED_MATERIALS.put(material -> material==check, handler);
	}

	/**
	 * Registers a new penetration handler for multiple Materials
	 *
	 * @param materials array of Materials to check
	 * @param handler   penetration handler
	 */
	public static void registerMaterial(Material[] materials, IPenetrationHandler handler)
	{
		final HashSet<Material> hashSet = new HashSet<>(Arrays.asList(materials));
		REGISTERED_MATERIALS.put(hashSet::contains, handler);
	}

	//TODO: 29.03.2024 multi-ore version of this

	/**
	 * Registers a new penetration handler for a given OreDict entry
	 *
	 * @param oreName OreDict entry
	 * @param handler penetration handler
	 */
	public static void registerOre(String oreName, IPenetrationHandler handler)
	{
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, oreName), handler);
	}

	public static void registerMetalMaterial(PenetrationHandlerMetal handler)
	{
		registerMetalMaterial(handler, true, true, true);
	}

	public static void registerMetalMaterial(PenetrationHandlerMetal handler, boolean hasSlab, boolean hasSheetMetal, boolean hasSheetmetalSlab)
	{
		REGISTERED_BLOCKS.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "block"+IIStringUtil.toCamelCase(handler.getName(), false)), handler);
		if(hasSlab)
			REGISTERED_BLOCKS.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "slab"+IIStringUtil.toCamelCase(handler.getName(), false)), handler);
		if(hasSheetMetal)
			REGISTERED_BLOCKS.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "blockSheetmetal"+IIStringUtil.toCamelCase(handler.getName(), false)), handler);
		if(hasSheetmetalSlab)
			REGISTERED_BLOCKS.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "slabSheetmetal"+IIStringUtil.toCamelCase(handler.getName(), false)), handler);

	}

	public static void batchRegisterHandler(IPenetrationHandler handler, Block... blocks)
	{
		for(Block b : blocks)
			REGISTERED_BLOCKS.put(iBlockState -> iBlockState.getBlock()==b, handler);
	}

	//--- Getters ---//

	public static IPenetrationHandler getPenetrationHandler(IBlockState state)
	{
		for(Entry<Predicate<IBlockState>, IPenetrationHandler> e : REGISTERED_BLOCKS.entrySet())
			if(e.getKey().test(state))
				return e.getValue();

		for(Entry<Predicate<Material>, IPenetrationHandler> e : REGISTERED_MATERIALS.entrySet())
			if(e.getKey().test(state.getMaterial()))
				return e.getValue();

		return DEFAULT;
	}

	public static IPenetrationHandler getPenetrationHandler(Entity entity)
	{
		for(Entry<Predicate<Entity>, IPenetrationHandler> e : REGISTERED_ENTITIES.entrySet())
			if(e.getKey().test(entity))
				return e.getValue();
		return DEFAULT;
	}

}
