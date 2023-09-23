package pl.pabilo8.immersiveintelligence.api.bullets;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.PenMaterialTypes;
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
			if(size() > Graphics.maxPenetratedBlocks)
				remove(0);
			return super.add(damageBlockPos);
		}
	};

	private static final IPenetrationHandler DEFAULT = new PenetrationHandlerGeneral();

	static
	{
		AmmoUtils.batchRegisterHandler(new PenetrationHandlerSteel(), IEContent.blockMetalDecoration0,
				IEContent.blockMetalDevice0, IEContent.blockMetalDevice1, IEContent.blockMetalMultiblock);
		AmmoUtils.batchRegisterHandler(new PenetrationHandlerSteel(), IIContent.blockMetalDecoration,
				IIContent.blockMetalMultiblock0, IIContent.blockMetalMultiblock1);
		registeredBlocks.put(iBlockState -> IIUtils.compareBlockstateOredict(iBlockState, "logWood"), new PenetrationHandlerLog());
		AmmoUtils.batchRegisterHandler(new PenetrationHandlerDirt(),Blocks.GRASS, Blocks.DIRT);

		AmmoUtils.registerMetalMaterial(new PenetrationHandlerIron(), "iron");
		AmmoUtils.registerMetalMaterial(new PenetrationHandlerCopper(), "copper");
		AmmoUtils.registerMetalMaterial(new PenetrationHandlerSteel(), "steel");

		AmmoUtils.registerMetalMaterial(new PenetrationHandlerGold(), "gold");
		AmmoUtils.registerMetalMaterial(new PenetrationHandlerGold(), "electrum");
		AmmoUtils.registerMetalMaterial(new PenetrationHandlerGold(), "silver");
		AmmoUtils.registerMetalMaterial(new PenetrationHandlerGold(), "platinum");

		AmmoUtils.registerMetalMaterial(new PenetrationHandlerBronze(), "bronze");
		AmmoUtils.registerMetalMaterial(new PenetrationHandlerBronze(), "lead");
		AmmoUtils.registerMetalMaterial(new PenetrationHandlerBronze(), "constantan");

		AmmoUtils.registerMetalMaterial(new PenetrationHandlerTungsten(), "tungsten");

		AmmoUtils.registerMetalMaterial(new PenetrationHandlerAluminium(), "aluminum");
		AmmoUtils.registerMetalMaterial(new PenetrationHandlerAluminium(), "tin");
		AmmoUtils.registerMetalMaterial(new PenetrationHandlerAluminium(), "zinc");

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
