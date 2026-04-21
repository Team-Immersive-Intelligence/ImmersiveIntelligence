package pl.pabilo8.immersiveintelligence.common.compat.tbl;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentRole;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityWhitePhosphorus;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageParticleEffect;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 14.04.2026
 */

public class AmmoComponentUndyingEmbers extends AmmoComponent
{
	public AmmoComponentUndyingEmbers(){ super("undying_embers", 1f, ComponentRole.SPECIAL, IIColor.fromPackedRGB(0xff9900));
}

	@Override

	public IngredientStack getMaterial()
	{
		Item undying_embers = Item.REGISTRY.getObject(new ResourceLocation("thebetweenlands", "undying_embers"));

		return new IngredientStack(new ItemStack(undying_embers, 1));
	}

	@Override
	public void onEffect(World world, Vec3d pos, Vec3d dir, ComponentEffectShape shape, NBTTagCompound tag, float size, float multiplier, @Nullable Entity owner)
	{
		if(shape==ComponentEffectShape.ORB)
		{
			if(world.isRemote)
				return;

			Vec3d v = dir.scale(-1); // Reverse direction for effect shaping
			IIPacketHandler.playRangedSound(world, pos, IISounds.explosionIncendiary, SoundCategory.NEUTRAL, (int)(40*multiplier), 1f, 1f);

			BlockPos ppos = new BlockPos(pos);
			EntityLivingBase[] entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(ppos).grow(10*multiplier)).toArray(new EntityLivingBase[0]);
			for(EntityLivingBase e : entities)
			{
				e.setFire(20);
			}

			// fragments
			for(int i = 0; i < 30*multiplier; i++)
			{
				Vec3d vecDir = new Vec3d(1, 0, 0).rotateYaw(i/(30f*multiplier)*360f).add(v);
				EntityWhitePhosphorus shrap = new EntityWhitePhosphorus(world, pos.x+vecDir.x, pos.y+v.y+1f, pos.z+vecDir.z, 0, 0, 0);

				shrap.motionX = vecDir.x*0.35f;
				shrap.motionY = 0.1f+(Utils.RAND.nextDouble()*0.2f);
				shrap.motionZ = vecDir.z*0.35f;

				world.spawnEntity(shrap);
				world.spawnEntity(shrap);
				world.spawnEntity(shrap);
			}

			// Send particle effect message to nearby clients
			IIPacketHandler.sendToClient(new MessageParticleEffect("cremains", world,
					pos.addVector(0, 1, 0),
					Vec3d.ZERO, 0, 0, null
			));

			// Area of effect cloud (applies potion effects)
			EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(world, pos.x+v.x, pos.y+v.y+1f, pos.z+v.z);
			cloud.addEffect(new PotionEffect(IIPotions.brokenArmor, Math.round(240), 2));
			cloud.addEffect(new PotionEffect(IEPotions.flammable, Math.round(240), 4));
			cloud.addEffect(new PotionEffect(IEPotions.stunned, Math.round(160), 2));
			cloud.addEffect(new PotionEffect(IEPotions.flashed, Math.round(270), 1));
			cloud.setRadius(3f*multiplier);
			cloud.setDuration(Math.round(20f+(10f*Utils.RAND.nextFloat())));
			cloud.setParticle(EnumParticleTypes.FLAME);
			world.spawnEntity(cloud);
		}
		// BURST shape: a more explosive spread of shrapnel and effects
		else if(shape==ComponentEffectShape.STAR)
		{
			if(world.isRemote)
				return;

			// Play a more intense sound for burst explosion
			IIPacketHandler.playRangedSound(world, pos, IISounds.explosionIncendiary, SoundCategory.NEUTRAL, (int)(60*multiplier), 1.2f, 1.2f);

			// Spawn shrapnel in a wider, explosive pattern
			for(int i = 0; i < 50*multiplier; i++)
			{
				// Adjust angles to spread more widely for burst effect
				Vec3d vecDir = new Vec3d(1, 0, 0).rotateYaw(i/(50f*multiplier)*360f).add(dir);
				EntityWhitePhosphorus shrap = new EntityWhitePhosphorus(world, pos.x+vecDir.x, pos.y+dir.y+1f, pos.z+vecDir.z, 0, 0, 0);

				// Burst fragments have higher speed and randomness
				shrap.motionX = vecDir.x*0.6f;
				shrap.motionY = 0.2f+(Utils.RAND.nextDouble()*0.3f);
				shrap.motionZ = vecDir.z*0.6f;

				world.spawnEntity(shrap);
				world.spawnEntity(shrap);
				world.spawnEntity(shrap);
			}

			// Send particle effect with burst ID
			IIPacketHandler.sendToClient(new MessageParticleEffect("cremains_burst", world,
					pos.addVector(0, 1, 0),
					Vec3d.ZERO, 0, 0, null
			));


			// A larger, faster dissipating cloud
			EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(world, pos.x+dir.x, pos.y+dir.y+1f, pos.z+dir.z);
			cloud.addEffect(new PotionEffect(IIPotions.brokenArmor, Math.round(180), 3));
			cloud.addEffect(new PotionEffect(IEPotions.flammable, Math.round(180), 5));
			cloud.addEffect(new PotionEffect(IEPotions.stunned, Math.round(120), 3));
			cloud.addEffect(new PotionEffect(IEPotions.flashed, Math.round(220), 2));
			cloud.setRadius(4.5f*multiplier); // Burst shape has a larger radius
			cloud.setDuration(Math.round(15f+(8f*Utils.RAND.nextFloat()))); // Burst effect lasts a bit shorter
			cloud.setParticle(EnumParticleTypes.LAVA);
			world.spawnEntity(cloud);

			Set<BlockPos> blocks = IIUtils.getBlocksInOrb(world, new BlockPos(pos), 6*componentSize);
			for(BlockPos firePos : blocks)
			{
				IBlockState placed = (Blocks.FIRE).getDefaultState();

				if(world.isAirBlock(firePos)&&world.getBlockState(firePos.down()).isTopSolid())
					world.setBlockState(firePos, placed);
				if(Utils.isOreBlockAt(world, firePos, "wood")||Utils.isOreBlockAt(world, firePos, "logWood"))
					world.setBlockState(firePos, placed);
			}
		}
	}
}

