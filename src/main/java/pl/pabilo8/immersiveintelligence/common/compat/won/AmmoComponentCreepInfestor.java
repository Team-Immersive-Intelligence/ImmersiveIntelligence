package pl.pabilo8.immersiveintelligence.common.compat.won;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.compat.srp.ScapeAndRunParasitesHelper;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.Set;


public class AmmoComponentCreepInfestor extends AmmoComponent
{
	public AmmoComponentCreepInfestor()
	{
		super("infestor_bomb", 1f, ComponentRole.SPECIAL, IIColor.fromPackedRGB(0xb35b2c));
	}

	@Override
	public IngredientStack getMaterial()
	{
		Item creepsample = Item.REGISTRY.getObject(WyrmsOfNyrusHelper.RES_WON.with( "creepsludge"));

		return new IngredientStack(new ItemStack(creepsample, 1));
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{

		SoundEvent sound1 = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("wyrmsofnyrus:creep"));
		SoundEvent sound2 = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("wyrmsofnyrus:shellbreak"));


		world.playSound(null, pos.x, pos.y, pos.z, sound1, SoundCategory.NEUTRAL, 2.5F, 1.0F);
		world.playSound(null, pos.x, pos.y, pos.z, sound1, SoundCategory.NEUTRAL, 2.5F, 0.5F);
		world.playSound(null, pos.x, pos.y, pos.z, sound1, SoundCategory.NEUTRAL, 2.5F, 1.5F);

		world.playSound(null, pos.x, pos.y, pos.z, sound2, SoundCategory.NEUTRAL, 2.0F, 1.0F);

		world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_ZOMBIE_INFECT, SoundCategory.NEUTRAL, 2.0F, 1.0F);
		world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.NEUTRAL, 1.0F, 2.0F);


		BlockPos ppos = new BlockPos(pos);
		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(5)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			e.addPotionEffect(new PotionEffect(MobEffects.WITHER, 128, 0));
		}

		Entity e1 = EntityList.createEntityByIDFromName(ResLoc.of("wyrmsofnyrus:creepling"), world);
		e1.setPosition(pos.z, pos.y, pos.z);
		world.spawnEntity(e1);
		world.spawnEntity(e1);
		world.spawnEntity(e1);



		/* Converted blocks
		hivecreepblock - dirt?
		hivecreeptop - grass block
		creepstone - stone
		creepsludge - sand
		creeplog - log
		deepleaves - leaves


		creepedgrass - grass 2?
		creepeddirt - dirt 2?
		 creepedsand - sand 2?
		  creepedstone - stone 2?
		 */

		Block creepHiveCreepBlock = Block.REGISTRY.getObject(WyrmsOfNyrusHelper.RES_WON.with("hivecreepblock"));
		Block creepHiveCreepTopBlock = Block.REGISTRY.getObject(WyrmsOfNyrusHelper.RES_WON.with("hivecreeptop"));
		Block creepStoneBlock = Block.REGISTRY.getObject(WyrmsOfNyrusHelper.RES_WON.with("creepstone"));
		Block creepSandBlock = Block.REGISTRY.getObject(WyrmsOfNyrusHelper.RES_WON.with("creepsludge"));
		Block creepLogBlock = Block.REGISTRY.getObject(WyrmsOfNyrusHelper.RES_WON.with("creeplog"));
		Block creepLeavesBlock = Block.REGISTRY.getObject(WyrmsOfNyrusHelper.RES_WON.with("deepleaves"));


		Set<BlockPos> blocks1 = IIUtils.getBlocksInOrb(world, new BlockPos(pos), 4*componentSize);


		for(BlockPos firePos : blocks1)
		{
			IBlockState creepbricks = (creepStoneBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "stoneBrickSmooth"))
				world.setBlockState(firePos, creepbricks);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacedrocks = (creepStoneBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "stone"))
				world.setBlockState(firePos, replacedrocks);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacedirt = (creepHiveCreepBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "dirt"))
				world.setBlockState(firePos, replacedirt);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacelog = (creepLogBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "logWood"))
				world.setBlockState(firePos, replacelog);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacewood = (creepLogBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "wood"))
				world.setBlockState(firePos, replacewood);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replaceterracotta = (creepStoneBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "hardenedClay")||Utils.isOreBlockAt(world, firePos, "terracotta")||Utils.isOreBlockAt(world, firePos, "stainedHardenedClay"))
				world.setBlockState(firePos, replaceterracotta);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacecobble = (creepStoneBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "cobblestone"))
				world.setBlockState(firePos, replacecobble);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacesand = (creepSandBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "sand"))
				world.setBlockState(firePos, replacesand);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replaceleaves = (creepLeavesBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "treeLeaves"))
				world.setBlockState(firePos, replaceleaves);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacegrass = (creepHiveCreepTopBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "grass"))
				world.setBlockState(firePos, replacegrass);
		}

	}
		//TODO: add block infestation block placer.
}
