package pl.pabilo8.immersiveintelligence.common.compat.srp;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
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
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.Set;

public class AmmoComponentInfester extends AmmoComponent
{
	public AmmoComponentInfester()
	{
		super("infestor_bomb", 1f, ComponentRole.SPECIAL, IIColor.fromPackedRGB(0x696244));
	}
	//Todo: find color for hex

	@Override
	public IngredientStack getMaterial()
	{
		Item parasitecanister = Item.REGISTRY.getObject(new ResourceLocation("srparasites", "parasitecanister"));

		return new IngredientStack(new ItemStack(parasitecanister, 1, 2)); //grotesque lump. Meta 0 is unharvestable/does not yield compatible loot.
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float componentSize, float multiplier, Entity owner)
	{
		new IIExplosion(world, owner, pos, dir,
				7*componentSize, 4*multiplier, ComponentEffectShape.ORB, false, componentSize > 0.125f, false)
				.doExplosion();

		SoundEvent sound1 = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("srparasites:rathol_explotion"));
		//for summoning sound buthol_explotion / mob_explotion general small explosion / rathol_explotion - big flesh explosion.

		world.playSound(null, pos.x, pos.y, pos.z, sound1, SoundCategory.NEUTRAL, 2.5F, 1.0F);


		BlockPos ppos = new BlockPos(pos);
		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(5)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e : entities)
		{
			Potion coth = Potion.REGISTRY.getObject(ResLoc.of("srparasites:coth"));
			Potion viral = Potion.REGISTRY.getObject(ResLoc.of("srparasites:viral"));
			Potion corrosive = Potion.REGISTRY.getObject(ResLoc.of("srparasites:corrosive"));
			Potion conta = Potion.REGISTRY.getObject(ResLoc.of("srparasites:conta"));

			e.addPotionEffect(new PotionEffect(coth, 380,1));
			e.addPotionEffect(new PotionEffect(viral, 380,2));
			e.addPotionEffect(new PotionEffect(corrosive, 380,3));
			e.addPotionEffect(new PotionEffect(conta, 500,0));
		}

		Entity e1 = EntityList.createEntityByIDFromName(ResLoc.of("srparasites:ata"), world);
		e1.setPosition(pos.z, pos.y, pos.z);
		world.spawnEntity(e1);
		world.spawnEntity(e1);
		world.spawnEntity(e1);
		world.spawnEntity(e1);
		world.spawnEntity(e1);

		/*convertable blocks:
		infested_stone_bricks
		infestedrubble (stone)
		infestedstain (dirt)
		infestedtrunk (log)
		infested_terracotta
		infested_planks
		infested_cobblestone
		infestedsand*/

		Block parasiteBricksBlock = Block.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("infested_stone_bricks"));
		Block parasiteStoneBlock = Block.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("infestedrubble"));
		Block parasiteDirtBlock = Block.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("infestedstain"));
		Block parasiteLogBlock = Block.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("infestedtrunk"));
		Block parasitePlanksBlock = Block.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("infested_planks"));
		Block parasiteTerracottaBlock = Block.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("infested_terracotta"));
		Block parasiteCobbleBlock = Block.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("infested_cobblestone"));
		Block parasiteSandBlock = Block.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("infestedsand"));
		Block parasiteLeavesBlock = Block.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("infested_leaves"));

		Block parasiteInfestedRemains = Block.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("infestremain"));


		Set<BlockPos> blocks1 = IIUtils.getBlocksInOrb(world, new BlockPos(pos), 6*componentSize);


		Set<BlockPos> blocks = IIUtils.getBlocksInOrb(world, new BlockPos(pos), 7*componentSize);
		for(BlockPos firePos : blocks)
		{
			IBlockState placed = (parasiteInfestedRemains).getDefaultState();
			if(world.isAirBlock(firePos)&&world.getBlockState(firePos.down()).isTopSolid())
				world.setBlockState(firePos, placed);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacedbricks = (parasiteBricksBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "stoneBrickSmooth"))
				world.setBlockState(firePos, replacedbricks);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacedrocks = (parasiteStoneBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "stone"))
				world.setBlockState(firePos, replacedrocks);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacedirt = (parasiteDirtBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "dirt"))
				world.setBlockState(firePos, replacedirt);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacelog = (parasiteLogBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "logWood"))
				world.setBlockState(firePos, replacelog);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacewood = (parasitePlanksBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "wood"))
				world.setBlockState(firePos, replacewood);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replaceterracotta = (parasiteTerracottaBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "hardenedClay")||Utils.isOreBlockAt(world, firePos, "terracotta")||Utils.isOreBlockAt(world, firePos, "stainedHardenedClay"))
				world.setBlockState(firePos, replaceterracotta);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacecobble = (parasiteCobbleBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "cobblestone"))
				world.setBlockState(firePos, replacecobble);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replacesand = (parasiteSandBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "sand"))
				world.setBlockState(firePos, replacesand);
		}

		for(BlockPos firePos : blocks1)
		{
			IBlockState replaceleaves = (parasiteLeavesBlock).getDefaultState();

			if(Utils.isOreBlockAt(world, firePos, "treeLeaves"))
				world.setBlockState(firePos, replaceleaves);
		}

		//TODO: add block infestation block placer.

	}
}
