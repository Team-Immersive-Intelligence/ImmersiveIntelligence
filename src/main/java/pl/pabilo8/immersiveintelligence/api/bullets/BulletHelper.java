package pl.pabilo8.immersiveintelligence.api.bullets;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBlockDamageSync;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 14-03-2020
 */
public class BulletHelper
{
	public static EntityBullet createBullet(World world, ItemStack stack, Vec3d pos, Vec3d dir, float force)
	{
		return new EntityBullet(world, stack, pos.x, pos.y, pos.z, force, dir.x, dir.y, dir.z);
	}

	public static EntityBullet createBullet(World world, ItemStack stack, Vec3d pos, Vec3d dir)
	{
		return new EntityBullet(world, stack, pos.x, pos.y, pos.z, 1f, dir.x, dir.y, dir.z);
	}

	public static void dealBlockDamage(World world, float bulletDamage, BlockPos pos, IPenetrationHandler pen)
	{
		DamageBlockPos dimensionBlockPos = new DamageBlockPos(pos, world, pen.getIntegrity());
		float newHp = PenetrationRegistry.getBlockHitpoints(pen, pos, world)-(bulletDamage*pen.getDensity());
		if(newHp > 0)
		{
			List<DamageBlockPos> list = PenetrationRegistry.blockDamage.stream().filter(damageBlockPos -> damageBlockPos.equals(dimensionBlockPos)).collect(Collectors.toList());
			if(list.size() > 0)
				list.forEach(damageBlockPos -> damageBlockPos.damage = newHp);
			else
				PenetrationRegistry.blockDamage.add(new DamageBlockPos(dimensionBlockPos, newHp));
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBlockDamageSync(new DamageBlockPos(dimensionBlockPos, newHp/(pen.getIntegrity()/pen.getDensity()))), Utils.targetPointFromPos(dimensionBlockPos, world, 32));
		}
		else if(newHp <= 0)
		{
			PenetrationRegistry.blockDamage.removeIf(damageBlockPos -> damageBlockPos.equals(dimensionBlockPos));
			world.getBlockState(pos).getBlock().breakBlock(world, pos, world.getBlockState(pos));
			world.destroyBlock(dimensionBlockPos, false);
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBlockDamageSync(new DamageBlockPos(dimensionBlockPos, 0f)), Utils.targetPointFromPos(dimensionBlockPos, world, 32));
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
			PenetrationRegistry.registeredBlocks.put(iBlockState -> iBlockState.getBlock()==b, handler);
	}

	public static void suppress(World world, double posX, double posY, double posZ, float supressionRadius, int suppressionPower)
	{
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX, posY, posZ, posX, posY, posZ).grow(supressionRadius));
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
				stack.damageItem(damageToArmour, ent);

			ent.addPotionEffect(effect);
		}
	}
}
