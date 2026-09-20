package pl.pabilo8.immersiveintelligence.api.ammo;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.*;
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
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.08.2026
 * @ii-approved 0.3.1
 * @since 05.03.2020
 */
public class PenetrationRegistry
{
	public static final String PARTICLE_DEBRIS_PEBBLE = "debris/pebble";
	//Explosion Debris
	public static final String PARTICLE_DEBRIS_BRANCH_LEAF = "debris/branch_leaf";
	public static final String PARTICLE_DEBRIS_BRANCH_NEEDLE = "debris/branch_needle";
	public static final String PARTICLE_DEBRIS_BRANCH_CACTUS = "debris/branch_cactus";
	public static final String PARTICLE_DEBRIS_BRICK = "debris/brick";
	public static final String PARTICLE_DEBRIS_BRICK_BIG = "debris/big_brick";
	public static final String PARTICLE_DEBRIS_CLOTH = "debris/cloth";
	public static final String PARTICLE_DEBRIS_FLESH = "debris/flesh";
	public static final String PARTICLE_DEBRIS_METAL = "debris/metal";
	public static final String PARTICLE_DEBRIS_GLASS = "debris/glass";
	public static final String PARTICLE_DEBRIS_PLANK = "debris/plank";
	public static final String PARTICLE_DEBRIS_STRAW = "debris/straw";
	public static final String PARTICLE_DEBRIS_MECHANICAL = "debris/rivet";
	public static final String PARTICLE_DEBRIS_LIGHT_MACHINE = "debris/light_machine";
	public static final String PARTICLE_DEBRIS_HEAVY_MACHINE = "debris/heavy_machine";
	/**
	 * Default penetration handler used as fallback
	 */
	private static final IPenetrationHandler DEFAULT = PenetrationHandler.builder(PenetrationHardness.ROCK, 1f, 150f, PARTICLE_DEBRIS_PEBBLE).build();
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
		registerState(state -> state.getBlock().blockHardness==-1, new PenetrationHandlerInvulnerable());
		//Fluids
		for(Fluid fluid : FluidRegistry.getRegisteredFluids().values())
		{
			Block fluidBlock = fluid.getBlock();
			if(fluidBlock!=null)
				registerState(state -> state.getBlock()==fluidBlock, new PenetrationHandlerFluid(fluid));
		}

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
		PenetrationHandler penHandlerWoodenMechanical = PenetrationHandler.builder(PenetrationHardness.IRON, 1f, 250, PARTICLE_DEBRIS_MECHANICAL)
				.withHitSound(IISounds.hitWood)
				.build();
		PenetrationHandler penHandlerMechanical = PenetrationHandler.builder(PenetrationHardness.IRON, 1f, 250, PARTICLE_DEBRIS_MECHANICAL)
				.withHitSound(IISounds.hitMetal)
				.withImpactParticle("debris/metal_hit")
				.withRicochetParticle("debris/metal_ricochet")
				.build();
		PenetrationHandler penHandlerHeavyMachine = PenetrationHandler.builder(PenetrationHardness.STEEL, 1.3f, 350, PARTICLE_DEBRIS_HEAVY_MACHINE)
				.withHitSound(IISounds.hitMetal)
				.withImpactParticle("debris/metal_hit")
				.withRicochetParticle("debris/metal_ricochet")
				.build();
		PenetrationHandler penHandlerLightMachine = PenetrationHandler.builder(PenetrationHardness.STEEL, 1.1f, 200, PARTICLE_DEBRIS_LIGHT_MACHINE)
				.withHitSound(IISounds.hitMetal)
				.withImpactParticle("debris/metal_hit")
				.withRicochetParticle("debris/metal_ricochet")
				.build();

		batchRegisterHandler(penHandlerWoodenMechanical, IIContent.blockWoodenMultiblock);
		batchRegisterHandler(penHandlerMechanical, IEContent.blockMetalMultiblock,
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
				PenetrationHandler.builder(PenetrationHardness.UBERCONCRETE, 3f, 350, PARTICLE_DEBRIS_BRICK_BIG)
						.withHitSound(IISounds.hitStone)
						.build()
		);
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "sturdyBricksConcrete"),
				PenetrationHandler.builder(PenetrationHardness.PANZERCONCRETE, 2f, 250, PARTICLE_DEBRIS_BRICK_BIG)
						.withHitSound(IISounds.hitStone)
						.build()
		);
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "bricksConcrete"),
				PenetrationHandler.builder(PenetrationHardness.CONCRETE, 1.33f, 200, PARTICLE_DEBRIS_BRICK)
						.withHitSound(IISounds.hitStone)
						.build()
		);
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "leadedConcrete"),
				PenetrationHandler.builder(PenetrationHardness.CONCRETE, 1.66f, 200, PARTICLE_DEBRIS_BRICK)
						.withHitSound(IISounds.hitStone)
						.build()
		);
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "concrete"),
				PenetrationHandler.builder(PenetrationHardness.CONCRETE, 1f, 150, PARTICLE_DEBRIS_PEBBLE)
						.withHitSound(IISounds.hitStone)
						.build()
		);

		//Bricks, Stone, Rocks
		registerState(state -> state.getBlock()==Blocks.BRICK_BLOCK||state.getBlock()==Blocks.BRICK_STAIRS,
				PenetrationHandler.builder(PenetrationHardness.BRICKS, 1f, 250, PARTICLE_DEBRIS_BRICK)
						.withHitSound(IISounds.hitStone)
						.build()
		);
		registerState(state -> state.getBlock()==Blocks.STONEBRICK||state.getBlock()==Blocks.STONE_BRICK_STAIRS,
				PenetrationHandler.builder(PenetrationHardness.BRICKS, 1f, 250, PARTICLE_DEBRIS_BRICK)
						.withHitSound(IISounds.hitStone)
						.build()
		);
		registerOre("stone", PenetrationHandler.builder(PenetrationHardness.ROCK, 1f, 200, PARTICLE_DEBRIS_PEBBLE)
				.withHitSound(IISounds.hitStone)
				.build()
		);
		registerOre("cobblestone", PenetrationHandler.builder(PenetrationHardness.ROCK, 1f, 200, PARTICLE_DEBRIS_PEBBLE)
				.withHitSound(IISounds.hitStone)
				.build()
		);
		registerOre("sandstone", PenetrationHandler.builder(PenetrationHardness.ROCK, 1f, 200, PARTICLE_DEBRIS_PEBBLE)
				.withHitSound(IISounds.hitStone)
				.build()
		);

		//ground
		registerMaterial(Material.ROCK, PenetrationHandler.builder(PenetrationHardness.ROCK, 1f, 150, PARTICLE_DEBRIS_PEBBLE)
				.withHitSound(IISounds.hitStone)
				.build()
		);
		registerMaterial(Material.GRASS, PenetrationHandler.builder(PenetrationHardness.GROUND, 1f, 150, PARTICLE_DEBRIS_PEBBLE)
				.withHitSound(IISounds.hitGrass)
				.build()
		);
		registerMaterial(Material.GROUND, PenetrationHandler.builder(PenetrationHardness.GROUND, 1f, 150, PARTICLE_DEBRIS_PEBBLE)
				.withHitSound(IISounds.hitDirt)
				.build()
		);
		registerMaterial(Material.SAND, PenetrationHandler.builder(PenetrationHardness.GROUND, 1f, 150, PARTICLE_DEBRIS_PEBBLE)
				.withHitSound(IISounds.hitSand)
				.build()
		);

		//wood
		registerOre("plankWood", PenetrationHandler.builder(PenetrationHardness.WOOD, 0.8f, 100, PARTICLE_DEBRIS_PLANK)
				.withHitSound(IISounds.hitWood)
				.build()
		);
		registerOre("stairWood", PenetrationHandler.builder(PenetrationHardness.WOOD, 0.8f, 100, PARTICLE_DEBRIS_PLANK)
				.withHitSound(IISounds.hitWood)
				.build()
		);
		registerOre("slabWood", PenetrationHandler.builder(PenetrationHardness.WOOD, 0.8f, 100, PARTICLE_DEBRIS_PLANK)
				.withHitSound(IISounds.hitWood)
				.build()
		);
		registerOre("plankTreatedWood", PenetrationHandler.builder(PenetrationHardness.WOOD, 0.8f, 100, PARTICLE_DEBRIS_PLANK)
				.withHitSound(IISounds.hitWood)
				.build()
		);
		registerOre("stairTreatedWood", PenetrationHandler.builder(PenetrationHardness.WOOD, 0.8f, 100, PARTICLE_DEBRIS_PLANK)
				.withHitSound(IISounds.hitWood)
				.build()
		);
		registerOre("slabTreatedWood", PenetrationHandler.builder(PenetrationHardness.WOOD, 0.8f, 100, PARTICLE_DEBRIS_PLANK)
				.withHitSound(IISounds.hitWood)
				.build()
		);
		registerOre("logWood", PenetrationHandler.builder(PenetrationHardness.WOOD, 1f, 250, PARTICLE_DEBRIS_BRICK)
				.withHitSound(IISounds.hitWood)
				.withFlammableVariant(state -> {
					if(state.getBlock()==IIContent.blockCharredLog)
						return null;

					IBlockState charred = IIContent.blockCharredLog.getDefaultState();
					if(state.getPropertyKeys().contains(BlockLog.LOG_AXIS))
						charred = charred.withProperty(BlockLog.LOG_AXIS, state.getValue(BlockLog.LOG_AXIS));
					return charred;
				})
				.build()
		);

		//Glass
		registerOre("paneGlass", PenetrationHandler.builder(PenetrationHardness.FRAGILE, 0.125f, 20, PARTICLE_DEBRIS_GLASS)
				.withImpactSound(SoundEvents.BLOCK_GLASS_BREAK)
				.build()
		);
		registerMaterial(Material.GLASS, PenetrationHandler.builder(PenetrationHardness.FRAGILE, 0.75f, 80, PARTICLE_DEBRIS_GLASS)
				.withImpactSound(SoundEvents.BLOCK_GLASS_BREAK)
				.build()
		);

		//Wool
		registerMaterial(new Material[]{Material.CARPET, Material.CLOTH},
				PenetrationHandler.builder(PenetrationHardness.FRAGILE, 1f, 50, PARTICLE_DEBRIS_CLOTH).withImpactSound(IISounds.impactFoliage).build()
		);

		//leaves
		registerMaterial(new Material[]{Material.LEAVES, Material.VINE},
				PenetrationHandler.builder(PenetrationHardness.FRAGILE, 0.5f, 300, PARTICLE_DEBRIS_BRANCH_LEAF)
						.withImpactSound(IISounds.impactFoliage)
						.build()
		);
		registerState(state -> state.getBlock()==Blocks.LEAVES&&state.getBlock().getMetaFromState(state)==2, //spruce leaves
				PenetrationHandler.builder(PenetrationHardness.FRAGILE, 0.5f, 300, PARTICLE_DEBRIS_BRANCH_NEEDLE)
						.withImpactSound(IISounds.impactFoliage)
						.build()
		);
		registerMaterial(new Material[]{Material.CACTUS},
				PenetrationHandler.builder(PenetrationHardness.FRAGILE, 0.8f, 100, PARTICLE_DEBRIS_BRANCH_CACTUS)
						.withImpactSound(IISounds.impactFoliage)
						.build()
		);

		//grass, crops, etc.
		registerState(state -> state.getMaterial()==Material.GRASS&&state.getBlock() instanceof IPlantable,
				PenetrationHandler.builder(PenetrationHardness.GROUND, 1f, 300, PARTICLE_DEBRIS_STRAW)
						.withHitSound(IISounds.hitGrass)
						.build()
		);
		registerMaterial(Material.GOURD, PenetrationHandler.builder(PenetrationHardness.FRAGILE, 1f, 150, PARTICLE_DEBRIS_STRAW)
				.withImpactSound(IISounds.impactFoliage)
				.build()
		);

		//EntityLivingBase
		REGISTERED_ENTITIES.put(entity -> entity instanceof EntityLivingBase,
				PenetrationHandler.builder(PenetrationHardness.FRAGILE, 1f, 0, PARTICLE_DEBRIS_FLESH)
						.withImpactSound(IISounds.impactFlesh)
						.build()
		);
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
