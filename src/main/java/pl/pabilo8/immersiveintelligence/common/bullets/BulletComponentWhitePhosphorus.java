package pl.pabilo8.immersiveintelligence.common.bullets;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.entities.EntityChemthrowerShot;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

import java.util.List;

/**
 * Created by Pabilo8 on 30-08-2019.
 */
public class BulletComponentWhitePhosphorus implements IBulletComponent
{
	@Override
	public String getName()
	{
		return "white_phosphorus";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dustWhitePhosphorus");
	}

	@Override
	public float getDensity()
	{
		return 1f;
	}

	@Override
	public void onExplosion(float amount, NBTTagCompound tag, World world, BlockPos pos, EntityBullet bullet)
	{
		int lower = 0;
		for(int l = 0; l < 24; l += 1)
		{
			if(world.isAirBlock(pos.offset(EnumFacing.DOWN, l))&&!world.isAirBlock(pos.offset(EnumFacing.DOWN, l+1)))
			{
				lower = l;
				break;
			}
		}

		EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(world, pos.getX(), pos.getY()-lower, pos.getZ());
		cloud.addEffect(new PotionEffect(IEPotions.flammable, Math.round(420*amount), 6));
		cloud.addEffect(new PotionEffect(IEPotions.stunned, Math.round(320*amount), 3));
		cloud.addEffect(new PotionEffect(IEPotions.flashed, Math.round(520*amount), 2));
		cloud.setParticle(EnumParticleTypes.CLOUD);
		cloud.setRadius(amount*10f);
		cloud.setDuration(Math.round(amount*320f));
		cloud.motionY = -5f;
		world.spawnEntity(cloud);

		ImmersiveIntelligence.logger.info("otak "+2048*amount);

		List<Entity> list = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.offset(EnumFacing.DOWN, lower)).grow(amount*8f));

		FluidStack fs = new FluidStack(FluidRegistry.getFluid("lava"), Math.round(24*amount));
		Vec3d v = new Vec3d(0, -1, 0);
		Vec3d throwerPos = new Vec3d(pos.offset(EnumFacing.UP, 5).offset(EnumFacing.DOWN, lower));
		for(int i = 0; i < 720*amount; i++)
		{
			Vec3d vecDir = v.add(Utils.RAND.nextGaussian()*.25f, Utils.RAND.nextGaussian()*.25f+(Math.floor(i/16f)*0.35f), Utils.RAND.nextGaussian()*.25f);

			EntityChemthrowerShot chem = new EntityChemthrowerShot(world, throwerPos.x+v.x*0.875, throwerPos.y+v.y*0.875,
					throwerPos.z+v.z*0.875, 0, 0, 0, fs);
			chem.setFire(120);
			chem.motionX = vecDir.x;
			chem.motionY = vecDir.y;
			chem.motionZ = vecDir.z;
			if(!world.isRemote)
				world.spawnEntity(chem);
		}

		for(Entity ent : list)
		{
			ent.setFire(Math.round(240*amount));
		}

		if(amount > 0.45)
		{
			EntityAreaEffectCloud cloud1 = new EntityAreaEffectCloud(world, pos.getX()+amount*10f, pos.getY()-lower, pos.getZ());
			cloud1.addEffect(new PotionEffect(IEPotions.flammable, Math.round(240*amount), 4));
			cloud1.addEffect(new PotionEffect(IEPotions.stunned, Math.round(160*amount), 2));
			cloud1.addEffect(new PotionEffect(IEPotions.flashed, Math.round(270*amount), 1));
			cloud1.setRadius(amount*4f);
			cloud1.setParticle(EnumParticleTypes.CLOUD);
			cloud1.setDuration(Math.round(amount*240f));
			world.spawnEntity(cloud1);

			EntityAreaEffectCloud cloud2 = new EntityAreaEffectCloud(world, pos.getX()-amount*10f, pos.getY()-lower, pos.getZ());
			cloud2.addEffect(new PotionEffect(IEPotions.flammable, Math.round(240*amount), 4));
			cloud2.addEffect(new PotionEffect(IEPotions.stunned, Math.round(160*amount), 2));
			cloud2.addEffect(new PotionEffect(IEPotions.flashed, Math.round(270*amount), 1));
			cloud2.setRadius(amount*4f);
			cloud2.setParticle(EnumParticleTypes.CLOUD);
			cloud2.setDuration(Math.round(amount*240f));
			world.spawnEntity(cloud2);

			EntityAreaEffectCloud cloud3 = new EntityAreaEffectCloud(world, pos.getX(), pos.getY()-lower, pos.getZ()+amount*10f);
			cloud3.addEffect(new PotionEffect(IEPotions.flammable, Math.round(240*amount), 4));
			cloud3.addEffect(new PotionEffect(IEPotions.stunned, Math.round(160*amount), 2));
			cloud3.addEffect(new PotionEffect(IEPotions.flashed, Math.round(270*amount), 1));
			cloud3.setRadius(amount*4f);
			cloud3.setDuration(Math.round(amount*240f));
			cloud3.setParticle(EnumParticleTypes.CLOUD);
			world.spawnEntity(cloud3);

			EntityAreaEffectCloud cloud4 = new EntityAreaEffectCloud(world, pos.getX(), pos.getY()-lower, pos.getZ()-amount*10f);
			cloud4.addEffect(new PotionEffect(IEPotions.flammable, Math.round(240*amount), 4));
			cloud4.addEffect(new PotionEffect(IEPotions.stunned, Math.round(160*amount), 2));
			cloud4.addEffect(new PotionEffect(IEPotions.flashed, Math.round(270*amount), 1));
			cloud4.setRadius(amount*4f);
			cloud4.setDuration(Math.round(amount*240f));
			cloud4.setParticle(EnumParticleTypes.CLOUD);
			world.spawnEntity(cloud4);
		}
	}

	@Override
	public float getPenetrationModifier(NBTTagCompound tag)
	{
		return 0.25f;
	}

	@Override
	public float getDamageModifier(NBTTagCompound tag)
	{
		return 0.25f;
	}

	@Override
	public EnumComponentRole getRole()
	{
		return EnumComponentRole.INCENDIARY;
	}

	@Override
	public int getColour()
	{
		return 0xd3dbac;
	}
}
