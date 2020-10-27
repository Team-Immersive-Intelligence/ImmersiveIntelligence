package pl.pabilo8.immersiveintelligence.api.bullets;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.penhandlers.*;
import pl.pabilo8.immersiveintelligence.api.bullets.penhandlers.PenetrationHandlerConcretes.PenetrationHandlerConcrete;
import pl.pabilo8.immersiveintelligence.api.bullets.penhandlers.PenetrationHandlerMetals.*;
import pl.pabilo8.immersiveintelligence.api.bullets.penhandlers.PenetrationHandlerWood.PenetrationHandlerLog;
import pl.pabilo8.immersiveintelligence.api.bullets.penhandlers.PenetrationHandlerWood.PenetrationHandlerPlanks;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 05-03-2020
 */
public class PenetrationRegistry
{
	public static HashMap<Predicate<Entity>, IPenetrationHandler> registeredEntities = new HashMap<>();
	//Blocks first
	public static HashMap<Predicate<IBlockState>, IPenetrationHandler> registeredBlocks = new HashMap<>();
	//Materials second
	public static HashMap<Predicate<Material>, IPenetrationHandler> registeredMaterials = new HashMap<>();

	public static ArrayList<DamageBlockPos> blockDamage = new ArrayList<>();
	public static ArrayList<DamageBlockPos> blockDamageClient = new ArrayList<>();

	static
	{
		PenetrationHelper.batchRegisterHandler(new PenetrationHandlerSteel(), IEContent.blockMetalDecoration0,
				IEContent.blockMetalDevice0, IEContent.blockMetalDevice1, IEContent.blockMetalMultiblock);
		PenetrationHelper.batchRegisterHandler(new PenetrationHandlerSteel(), CommonProxy.block_metal_decoration,
				CommonProxy.block_metal_multiblock0, CommonProxy.block_metal_multiblock1);
		registeredBlocks.put(iBlockState -> Utils.compareBlockstateOredict(iBlockState, "logWood"), new PenetrationHandlerLog());

		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerIron(), "iron");
		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerCopper(), "copper");
		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerSteel(), "steel");

		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerGold(), "gold");
		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerGold(), "electrum");
		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerGold(), "silver");
		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerGold(), "platinum");

		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerBronze(), "bronze");
		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerBronze(), "lead");
		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerBronze(), "constantan");

		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerTungsten(), "tungsten");

		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerAluminium(), "aluminum");
		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerAluminium(), "tin");
		PenetrationHelper.registerMetalMaterial(new PenetrationHandlerAluminium(), "zinc");

		registeredBlocks.put(iBlockState -> Utils.compareBlockstateOredict(iBlockState, "leadedConcrete"), new PenetrationHandlerConcrete());
		registeredBlocks.put(iBlockState -> Utils.compareBlockstateOredict(iBlockState, "concrete"), new PenetrationHandlerConcrete());

		registeredMaterials.put(material -> material==Material.GLASS, new PenetrationHandlerGlass());

		registeredMaterials.put(material -> material==Material.ANVIL, new PenetrationHandlerIron());
		registeredMaterials.put(material -> material==Material.IRON, new PenetrationHandlerIron());

		registeredMaterials.put(material -> material==Material.ROCK, new PenetrationHandlerStone());
		registeredMaterials.put(material -> material==Material.GROUND, new PenetrationHandlerDirt());
		registeredMaterials.put(material -> material==Material.GOURD, new PenetrationHandlerClay());
		registeredMaterials.put(material -> material==Material.SAND, new PenetrationHandlerSand());
		registeredMaterials.put(material -> material==Material.LEAVES, new PenetrationHandlerLeaves());
		registeredMaterials.put(material -> material==Material.WOOD, new PenetrationHandlerPlanks());
		registeredMaterials.put(material -> true, new PenetrationHandlerGeneral());
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
		float getDensity();

		@Nullable
		default SoundEvent getSpecialSound(HitEffect effect)
		{
			return null;
		}
	}

	public enum HitEffect
	{
		PENETRATION,
		PARTIAL_PENETRATION,
		RICOCHET
	}

}
