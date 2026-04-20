package pl.pabilo8.immersiveintelligence.common.compat.srp;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ITeslaEntity;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.*;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.Set;

/**
 * * @author Carver (carver@iiteam.net)
 * @since 16.04.2026
 * @updated 19.04.2026
 */

public class AmmoComponentSummonerCore extends AmmoComponent
{
	public AmmoComponentSummonerCore ()
	{
		super("summoner_core", 1f, ComponentRole.SPECIAL, IIColor.fromPackedARGB(0x754886));
	}

	@Override
	public IngredientStack getMaterial()
	{
		Item summonercore = Item.REGISTRY.getObject(new ResourceLocation("srparasites", "ada_summoner_drop"));

		return new IngredientStack(new ItemStack(summonercore, 1));
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float size, float multiplier, Entity owner)
	{


		new IIExplosion(world, owner, pos, dir,
				5, 0, ComponentEffectShape.ORB, false, false, false)
				.doExplosion();

		Entity e1 = EntityList.createEntityByIDFromName(ResLoc.of("srparasites:mudo"), world);
		e1.setPosition(pos.z, pos.y, pos.z);
		world.spawnEntity(e1);
		world.spawnEntity(e1);
		world.spawnEntity(e1);

		BlockPos ppos = new BlockPos(pos);
		EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(3)).toArray(new EntityLivingBase[0]);
		for(EntityLivingBase e2 : entities)
		{
			Potion viral = Potion.REGISTRY.getObject(ResLoc.of("srparasites:viral"));
			e2.addPotionEffect(new PotionEffect(viral, 180, 0));
		}

		Block parasiteInfestedRemains = Block.REGISTRY.getObject(ScapeAndRunParasitesHelper.RES_SRP.with("infestremain"));

		Set<BlockPos> blocks = IIUtils.getBlocksInOrb(world, new BlockPos(pos), 4*componentSize);
		for(BlockPos firePos : blocks)
		{
			IBlockState placed = (parasiteInfestedRemains).getDefaultState();

			if(world.isAirBlock(firePos)&&world.getBlockState(firePos.down()).isTopSolid())
				world.setBlockState(firePos, placed);

		}

		SoundEvent sound1 = ForgeRegistries.SOUND_EVENTS.getValue(ResLoc.of("srparasites:buthol_explotion"));
		//for summoning sound buthol_explotion / mob_explotion general small explosion / rathol_explotion - big flesh explosion.

		world.playSound(null, pos.x, pos.y, pos.z, sound1, SoundCategory.NEUTRAL, 2.0F, 1.0F);

		world.playSound(null, new BlockPos(pos), IISounds.explosionFlare, SoundCategory.NEUTRAL, 1, 0.5f);
		world.playSound(null, new BlockPos(pos), IESounds.tesla, SoundCategory.NEUTRAL, 1, 0.5f);

		float radius = multiplier*10;
		IIAmmoUtils.applyEMPEffect(world, new BlockPos(pos), radius, (int)(4000000*multiplier));
	}
}
