package pl.pabilo8.immersiveintelligence.api.ammo;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.HitEffect;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration_handlers.*;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration_handlers.PenetrationHandlerConcretes.*;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration_handlers.PenetrationHandlerMetals.*;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration_handlers.PenetrationHandlerWood.PenetrationHandlerLog;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration_handlers.PenetrationHandlerWood.PenetrationHandlerPlanks;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 05-03-2020
 */
public class IIPenetrationRegistry
{
	public static HashMap<Predicate<Entity>, IPenetrationHandler> registeredEntities = new HashMap<>();
	//Blocks first
	public static HashMap<Predicate<IBlockState>, IPenetrationHandler> registeredBlocks = new HashMap<>();
	//Materials second
	public static LinkedHashMap<Predicate<Material>, IPenetrationHandler> registeredMaterials = new LinkedHashMap<>();

	public static ArrayList<DamageBlockPos> blockDamage = new ArrayList<>();
	public static ArrayList<DamageBlockPos> blockDamageClient = new ArrayList<DamageBlockPos>()
	{
		@Override
		public boolean add(DamageBlockPos damageBlockPos)
		{
			if(size() > Graphics.maxPenetratedBlocks)
				remove(0);
			return super.add(damageBlockPos);
		}
	};

	private static final IPenetrationHandler DEFAULT = new PenetrationHandlerGeneral();

	static
	{
		IIAmmoUtils.batchRegisterHandler(new PenetrationHandlerSteel(), IEContent.blockMetalDecoration0,
				IEContent.blockMetalDevice0, IEContent.blockMetalDevice1, IEContent.blockMetalMultiblock);
		IIAmmoUtils.batchRegisterHandler(new PenetrationHandlerSteel(), IIContent.blockMetalDecoration,
				IIContent.blockMetalMultiblock0, IIContent.blockMetalMultiblock1);
		registeredBlocks.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "logWood"), new PenetrationHandlerLog());
		IIAmmoUtils.batchRegisterHandler(new PenetrationHandlerDirt(), Blocks.GRASS, Blocks.DIRT);

		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerIron(), "iron");
		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerCopper(), "copper");
		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerSteel(), "steel");

		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerGold(), "gold");
		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerGold(), "electrum");
		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerGold(), "silver");
		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerGold(), "platinum");

		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerBronze(), "bronze");
		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerBronze(), "lead");
		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerBronze(), "constantan");

		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerTungsten(), "tungsten");

		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerAluminium(), "aluminum");
		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerAluminium(), "tin");
		IIAmmoUtils.registerMetalMaterial(new PenetrationHandlerAluminium(), "zinc");

		registeredBlocks.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "uberConcrete"), new PenetrationHandlerUberConcrete());
		registeredBlocks.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "sturdyBricksConcrete"), new PenetrationHandlerPanzerConcrete());
		registeredBlocks.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "bricksConcrete"), new PenetrationHandlerConcreteBricks());

		registeredBlocks.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "leadedConcrete"), new PenetrationHandlerLeadedConcrete());
		registeredBlocks.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "concrete"), new PenetrationHandlerConcrete());

		registeredMaterials.put(material -> material==Material.GLASS, new PenetrationHandlerGlass());

		registeredMaterials.put(material -> material==Material.ANVIL, new PenetrationHandlerIron());
		registeredMaterials.put(material -> material==Material.IRON, new PenetrationHandlerIron());

		registeredMaterials.put(material -> material==Material.ROCK, new PenetrationHandlerStone());
		registeredMaterials.put(material -> material==Material.GRASS, new PenetrationHandlerGrass());
		registeredMaterials.put(material -> material==Material.GROUND, new PenetrationHandlerDirt());
		registeredMaterials.put(material -> material==Material.GOURD, new PenetrationHandlerClay());
		registeredMaterials.put(material -> material==Material.SAND, new PenetrationHandlerSand());
		registeredMaterials.put(material -> material==Material.LEAVES, new PenetrationHandlerLeaves());
		registeredMaterials.put(material -> material==Material.WOOD, new PenetrationHandlerPlanks());
		//registeredMaterials.put(material -> true, DEFAULT);
	}

	public interface IPenetrationHandler
	{
		/**
		 * @return integrity - the block's penetration hp
		 */
		float getIntegrity();

		/**
		 * @return density (constant resistance multiplier)
		 */
		float getReduction();

		@Nullable
		default SoundEvent getSpecialSound(HitEffect effect)
		{
			return null;
		}

		PenMaterialTypes getPenetrationType();
	}

	public static IPenetrationHandler getPenetrationHandler(IBlockState state)
	{
		for(Entry<Predicate<IBlockState>, IPenetrationHandler> e : IIPenetrationRegistry.registeredBlocks.entrySet())
			if(e.getKey().test(state))
				return e.getValue();

		for(Entry<Predicate<Material>, IPenetrationHandler> e : IIPenetrationRegistry.registeredMaterials.entrySet())
			if(e.getKey().test(state.getMaterial()))
				return e.getValue();

		return DEFAULT;
	}

	public static float getBlockHitpoints(IPenetrationHandler pen, BlockPos pos, World world)
	{
		float hp = pen.getIntegrity()/pen.getReduction();
		DamageBlockPos blockHitPos = new DamageBlockPos(pos, world, pen.getIntegrity());

		for(DamageBlockPos damageBlockPos : IIPenetrationRegistry.blockDamage)
		{
			if(damageBlockPos.equals(blockHitPos))
				return damageBlockPos.damage;
		}

		IIPenetrationRegistry.blockDamage.add(new DamageBlockPos(blockHitPos, hp));
		return hp;

	}

}
