package pl.pabilo8.immersiveintelligence.api.bullets;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.penhandlers.*;
import pl.pabilo8.immersiveintelligence.api.bullets.penhandlers.PenetrationHandlerConcretes.*;
import pl.pabilo8.immersiveintelligence.api.bullets.penhandlers.PenetrationHandlerMetals.*;
import pl.pabilo8.immersiveintelligence.api.bullets.penhandlers.PenetrationHandlerWood.PenetrationHandlerLog;
import pl.pabilo8.immersiveintelligence.api.bullets.penhandlers.PenetrationHandlerWood.PenetrationHandlerPlanks;
import pl.pabilo8.immersiveintelligence.common.IIContent;

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
public class PenetrationRegistry
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
			if(size() > 16)
				remove(0);
			return super.add(damageBlockPos);
		}
	};

	private static final IPenetrationHandler DEFAULT = new PenetrationHandlerGeneral();

	static
	{
		BulletHelper.batchRegisterHandler(new PenetrationHandlerSteel(), IEContent.blockMetalDecoration0,
				IEContent.blockMetalDevice0, IEContent.blockMetalDevice1, IEContent.blockMetalMultiblock);
		BulletHelper.batchRegisterHandler(new PenetrationHandlerSteel(), IIContent.blockMetalDecoration,
				IIContent.blockMetalMultiblock0, IIContent.blockMetalMultiblock1);
		registeredBlocks.put(iBlockState -> Utils.compareBlockstateOredict(iBlockState, "logWood"), new PenetrationHandlerLog());

		BulletHelper.registerMetalMaterial(new PenetrationHandlerIron(), "iron");
		BulletHelper.registerMetalMaterial(new PenetrationHandlerCopper(), "copper");
		BulletHelper.registerMetalMaterial(new PenetrationHandlerSteel(), "steel");

		BulletHelper.registerMetalMaterial(new PenetrationHandlerGold(), "gold");
		BulletHelper.registerMetalMaterial(new PenetrationHandlerGold(), "electrum");
		BulletHelper.registerMetalMaterial(new PenetrationHandlerGold(), "silver");
		BulletHelper.registerMetalMaterial(new PenetrationHandlerGold(), "platinum");

		BulletHelper.registerMetalMaterial(new PenetrationHandlerBronze(), "bronze");
		BulletHelper.registerMetalMaterial(new PenetrationHandlerBronze(), "lead");
		BulletHelper.registerMetalMaterial(new PenetrationHandlerBronze(), "constantan");

		BulletHelper.registerMetalMaterial(new PenetrationHandlerTungsten(), "tungsten");

		BulletHelper.registerMetalMaterial(new PenetrationHandlerAluminium(), "aluminum");
		BulletHelper.registerMetalMaterial(new PenetrationHandlerAluminium(), "tin");
		BulletHelper.registerMetalMaterial(new PenetrationHandlerAluminium(), "zinc");

		registeredBlocks.put(iBlockState -> Utils.compareBlockstateOredict(iBlockState, "uberConcrete"), new PenetrationHandlerUberConcrete());
		registeredBlocks.put(iBlockState -> Utils.compareBlockstateOredict(iBlockState, "sturdyBricksConcrete"), new PenetrationHandlerPanzerConcrete());
		registeredBlocks.put(iBlockState -> Utils.compareBlockstateOredict(iBlockState, "bricksConcrete"), new PenetrationHandlerConcreteBricks());

		registeredBlocks.put(iBlockState -> Utils.compareBlockstateOredict(iBlockState, "leadedConcrete"), new PenetrationHandlerLeadedConcrete());
		registeredBlocks.put(iBlockState -> Utils.compareBlockstateOredict(iBlockState, "concrete"), new PenetrationHandlerConcrete());

		registeredMaterials.put(material -> material==Material.GLASS, new PenetrationHandlerGlass());

		registeredMaterials.put(material -> material==Material.ANVIL, new PenetrationHandlerIron());
		registeredMaterials.put(material -> material==Material.IRON, new PenetrationHandlerIron());

		registeredMaterials.put(material -> material==Material.ROCK, new PenetrationHandlerStone());
		registeredMaterials.put(material -> material==Material.GRASS, new PenetrationHandlerDirt());
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
		float getDensity();

		@Nullable
		default SoundEvent getSpecialSound(HitEffect effect)
		{
			return null;
		}

		PenMaterialTypes getPenetrationType();
	}

	public enum HitEffect
	{
		IMPACT,
		RICOCHET
	}

	public static IPenetrationHandler getPenetrationHandler(IBlockState state)
	{
		for(Entry<Predicate<IBlockState>, IPenetrationHandler> e : PenetrationRegistry.registeredBlocks.entrySet())
			if(e.getKey().test(state))
				return e.getValue();

		for(Entry<Predicate<Material>, IPenetrationHandler> e : PenetrationRegistry.registeredMaterials.entrySet())
			if(e.getKey().test(state.getMaterial()))
				return e.getValue();

		return DEFAULT;
	}

	public static float getBlockHitpoints(IPenetrationHandler pen, BlockPos pos, World world)
	{
		float hp = pen.getIntegrity()/pen.getDensity();
		DamageBlockPos blockHitPos = new DamageBlockPos(pos, world, pen.getIntegrity());

		for(DamageBlockPos damageBlockPos : PenetrationRegistry.blockDamage)
		{
			if(damageBlockPos.equals(blockHitPos))
				return damageBlockPos.damage;
		}

		PenetrationRegistry.blockDamage.add(new DamageBlockPos(blockHitPos, hp));
		return hp;

	}

}
