package pl.pabilo8.immersiveintelligence.api.ammo;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.common.IPlantable;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandlerInvulnerable;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.PenetrationHandlerMetal;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;

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
	private static final IPenetrationHandler DEFAULT = new PenetrationHandler(PenetrationHardness.ROCK, 1f, 150f, null, null);
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
		registerMetalMaterial(PenetrationHandlerMetal.create("tin", PenetrationHardness.FRAGILE, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("zinc", PenetrationHardness.FRAGILE, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("copper", PenetrationHardness.FRAGILE, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("gold", PenetrationHardness.FRAGILE, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("silver", PenetrationHardness.FRAGILE, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("platinum", PenetrationHardness.FRAGILE, 1.0f, 150f));

		//More durable metals
		registerMetalMaterial(PenetrationHandlerMetal.create("brass", PenetrationHardness.WEAK_METAL, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("bronze", PenetrationHardness.WEAK_METAL, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("invar", PenetrationHardness.WEAK_METAL, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("constantan", PenetrationHardness.WEAK_METAL, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("electrum", PenetrationHardness.WEAK_METAL, 1.0f, 150f));

		//Steel and tougher
		registerMetalMaterial(PenetrationHandlerMetal.create("lead", PenetrationHardness.IRON, 1.0f, 200f));
		registerMetalMaterial(PenetrationHandlerMetal.create("iron", PenetrationHardness.IRON, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("steel", PenetrationHardness.STEEL, 1.0f, 150f));
		registerMetalMaterial(PenetrationHandlerMetal.create("tungsten", PenetrationHardness.TUNGSTEN, 1.0f, 150f));
		REGISTERED_MATERIALS.put(material -> material==Material.ANVIL, PenetrationHandlerMetal.get("iron"));
		REGISTERED_MATERIALS.put(material -> material==Material.IRON, PenetrationHandlerMetal.get("iron"));

		//Multiblocks and machines
		batchRegisterHandler(PenetrationHandlerMetal.get("steel"),
				IEContent.blockMetalDecoration0,
				IEContent.blockMetalDevice0, IEContent.blockMetalDevice1, IEContent.blockMetalMultiblock,
				IIContent.blockMetalDecoration,
				IIContent.blockMetalMultiblock0, IIContent.blockMetalMultiblock1);

		//Concrete
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "uberConcrete"),
				new PenetrationHandler(PenetrationHardness.UBERCONCRETE, 1f, 150, IISounds.hitStone));
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "sturdyBricksConcrete"),
				new PenetrationHandler(PenetrationHardness.PANZERCONCRETE, 1f, 150, IISounds.hitStone));
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "bricksConcrete"),
				new PenetrationHandler(PenetrationHardness.CONCRETE, 1f, 150, IISounds.hitStone));
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "leadedConcrete"),
				new PenetrationHandler(PenetrationHardness.CONCRETE, 1f, 150, IISounds.hitStone));
		registerState(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "concrete"),
				new PenetrationHandler(PenetrationHardness.CONCRETE, 1f, 150, IISounds.hitStone));

		//ground
		registerMaterial(Material.ROCK, new PenetrationHandler(PenetrationHardness.ROCK, 1f, 150, IISounds.hitStone));
		registerMaterial(Material.GRASS, new PenetrationHandler(PenetrationHardness.GROUND, 1f, 150, IISounds.hitGrass));
		registerMaterial(Material.GROUND, new PenetrationHandler(PenetrationHardness.GROUND, 1f, 150, IISounds.hitDirt));
		registerMaterial(Material.SAND, new PenetrationHandler(PenetrationHardness.GROUND, 1f, 150, IISounds.hitSand));

		//wood
		registerOre("planksWood",
				new PenetrationHandler(PenetrationHardness.WOOD, 1f, 150, IISounds.hitWood));
		registerOre("logWood",
				new PenetrationHandler(PenetrationHardness.WOOD, 1f, 150, IISounds.hitWood));

		//Glass
		registerMaterial(Material.GLASS, new PenetrationHandler(PenetrationHardness.FRAGILE, 1f, 150, SoundEvents.BLOCK_GLASS_BREAK, null));

		//leaves
		registerMaterial(new Material[]{Material.LEAVES, Material.VINE},
				new PenetrationHandler(PenetrationHardness.FRAGILE, 1f, 150, IISounds.impactFoliage, null));

		//TODO: 29.03.2024 tall grass sound
		//grass, crops, etc.
		registerState(state -> state.getMaterial()==Material.GRASS&&state.getBlock() instanceof IPlantable,
				new PenetrationHandler(PenetrationHardness.GROUND, 1f, 150, IISounds.hitGrass));
		registerMaterial(Material.GOURD, new PenetrationHandler(PenetrationHardness.FRAGILE, 1f, 150, IISounds.impactFoliage, null));

		//EntityLivingBase
		REGISTERED_ENTITIES.put(entity -> entity instanceof EntityLivingBase,
				new PenetrationHandler(PenetrationHardness.FRAGILE, 1f, 0, IISounds.impactFlesh, null));
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
		REGISTERED_BLOCKS.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "block"+IIUtils.toCamelCase(handler.getName(), false)), handler);
		if(hasSlab)
			REGISTERED_BLOCKS.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "slab"+IIUtils.toCamelCase(handler.getName(), false)), handler);
		if(hasSheetMetal)
			REGISTERED_BLOCKS.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "blockSheetmetal"+IIUtils.toCamelCase(handler.getName(), false)), handler);
		if(hasSheetmetalSlab)
			REGISTERED_BLOCKS.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "slabSheetmetal"+IIUtils.toCamelCase(handler.getName(), false)), handler);

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
