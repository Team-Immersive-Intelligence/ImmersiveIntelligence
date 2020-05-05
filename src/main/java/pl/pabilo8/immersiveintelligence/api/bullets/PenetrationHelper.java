package pl.pabilo8.immersiveintelligence.api.bullets;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBlockDamageSync;

import java.util.List;

/**
 * Created by Pabilo8 on 14-03-2020.
 */
public class PenetrationHelper
{

	public static void dealBlockDamage(World world, float bulletDamage, DimensionBlockPos pos, float hp, IPenetrationHandler pen)
	{
		float newHp = hp-bulletDamage;
		if(newHp > 0)
		{
			PenetrationRegistry.blockDamage.replace(pos, newHp);
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBlockDamageSync(newHp/(pen.getIntegrity()/pen.getDensity()), pos), Utils.targetPointFromPos(pos, world, 32));
		}
		else
		{
			if(newHp <= 0)
			{
				PenetrationRegistry.blockDamage.remove(pos);
				world.destroyBlock(pos, false);
				IIPacketHandler.INSTANCE.sendToAllAround(new MessageBlockDamageSync(-1f, pos), Utils.targetPointFromPos(pos, world, 32));
			}
		}


	}

	public static void registerMetalMaterial(IPenetrationHandler handler, String name)
	{
		registerMetalMaterial(handler, name, true, true, true);
	}

	public static void registerMetalMaterial(IPenetrationHandler handler, String name, boolean hasSlab, boolean hasSheetMetal, boolean hasSheetmetalSlab)
	{
		PenetrationRegistry.registeredBlocks.put(iBlockState -> Utils.compareBlockstateOredict(iBlockState, "block"+Utils.toCamelCase(name, false)), handler);
		if(hasSlab)
			PenetrationRegistry.registeredBlocks.put(iBlockState -> Utils.compareBlockstateOredict(iBlockState, "slab"+Utils.toCamelCase(name, false)), handler);
		if(hasSheetMetal)
			PenetrationRegistry.registeredBlocks.put(iBlockState -> Utils.compareBlockstateOredict(iBlockState, "blockSheetmetal"+Utils.toCamelCase(name, false)), handler);
		if(hasSheetmetalSlab)
			PenetrationRegistry.registeredBlocks.put(iBlockState -> Utils.compareBlockstateOredict(iBlockState, "slabSheetmetal"+Utils.toCamelCase(name, false)), handler);

	}

	public static void batchRegisterHandler(IPenetrationHandler handler, Block... blocks)
	{
		for(Block b : blocks)
		{
			PenetrationRegistry.registeredBlocks.put(iBlockState -> iBlockState.getBlock()==b, handler);
		}
	}

	public static void supress(World world, double posX, double posY, double posZ, float supressionRadius, int suppressionPower)
	{
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX, posY, posZ, posX+1, posY+1, posZ+1).grow(supressionRadius));
		for(EntityLivingBase entity : entities)
		{
			PotionEffect effect = entity.getActivePotionEffect(IIPotions.suppression);
			if(effect==null)
				effect = new PotionEffect(IIPotions.suppression, 120, suppressionPower, false, false);
			else
			{
				effect.duration = 10;
				effect.combine(new PotionEffect(IIPotions.suppression, 120, Math.min(255, effect.getAmplifier()+suppressionPower)));
			}
			entity.addPotionEffect(effect);
		}
	}

	public static void breakArmour(Entity entity, int damageToArmour)
	{
		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase ent = (EntityLivingBase)entity;
			PotionEffect effect = ent.getActivePotionEffect(IIPotions.broken_armor);
			if(effect==null)
				effect = new PotionEffect(IIPotions.broken_armor, 60, damageToArmour, false, false);
			else
			{
				effect.duration = 10;
				effect.combine(new PotionEffect(IIPotions.broken_armor, 60, Math.min(255, effect.getAmplifier()+damageToArmour)));
			}
			for(ItemStack stack : ent.getArmorInventoryList())
			{
				stack.damageItem(damageToArmour, ent);
			}

			ent.addPotionEffect(effect);
		}
	}
}
