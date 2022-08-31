package pl.pabilo8.immersiveintelligence.client.fx;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.particles.*;
import pl.pabilo8.immersiveintelligence.client.fx.particles.ParticleGasCloud.ParticleFlareFlash;
import pl.pabilo8.immersiveintelligence.client.fx.nuke.ParticleAtomFog;
import pl.pabilo8.immersiveintelligence.client.fx.nuke.ParticleAtomicBoomCore;
import pl.pabilo8.immersiveintelligence.client.fx.nuke.ParticleAtomicBoomRing;
import pl.pabilo8.immersiveintelligence.client.fx.nuke.ParticleShockwave;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityAtomicBoom;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

import java.security.Provider;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Pabilo8
 * @since 17.07.2020
 * <p>
 * A class for creating II particle effects
 * IE has methods in its proxy, while II uses this class
 */
@SideOnly(Side.CLIENT)
public class ParticleUtils
{
	@SideOnly(Side.CLIENT)
	public static ParticleRenderer particleRenderer = new ParticleRenderer();

	public static Supplier<Float> randFloat = Utils.RAND::nextFloat;
	public static Supplier<Double> randDouble = Utils.RAND::nextDouble;

	public static void spawnExplosionBoomFX(World world, Vec3d pos, float radius, float strength, boolean flaming, boolean damagesTerrain)
	{
		ParticleUtils.spawnShockwave(pos, radius, 0.75f*strength);
		if(radius < 6)
			ParticleUtils.spawnExplosionFX(pos, Vec3d.ZERO, radius*1.85f);

		Set<BlockPos> positions = new HashSet<>(new IIExplosion(world, null, pos, radius, strength, flaming, true).generateAffectedBlockPositions());

		//Custom sounds
		world.playSound(Minecraft.getMinecraft().player, pos.x, pos.y, pos.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F+(world.rand.nextFloat()-world.rand.nextFloat())*0.2F)*0.7F);

		for(BlockPos position : positions)
		{
			IBlockState state = world.getBlockState(position);

			Vec3d vecPos = new Vec3d(position).add(getPositiveRand(randFloat));
			Vec3d vecMotion = vecPos.subtract(pos);

			double d6 = vecMotion.lengthVector();
			vecMotion = vecMotion.scale(1/d6);

			double d7 = 0.5D/(d6/(double)radius+0.1D);
			d7 = d7*(randDouble.get()*randDouble.get()+0.3);
			vecMotion = vecMotion.scale(d7);

			ParticleUtils.spawnBlockFragmentFX(vecPos, vecMotion, 1f, state);


			spawnVanillaParticle(EnumParticleTypes.EXPLOSION_NORMAL, vecPos.add(pos).scale(0.5), vecMotion);
			spawnVanillaParticle(EnumParticleTypes.SMOKE_NORMAL, vecPos, vecMotion);

			Vec3d vv = new Vec3d(position).add(getRandXZ(randFloat));
			ParticleUtils.spawnExplosionFX(vv, Vec3d.ZERO, 2f);

			if(flaming)
			{
				for(int i = 0; i < 7; i += 1)
				{
					vv = new Vec3d(position)
							.add(withY(getPositiveXZRand(randFloat), 0));

					//generate random
					Vec3d motion = withY(getPositiveXZRand(randFloat).scale(strength),
							Utils.RAND.nextFloat()*0.01f
					);
					ParticleUtils.spawnFlameFX(vv, motion, 4f, 40+(10*i));

					//generate random again
					motion = withY(getPositiveXZRand(randFloat).scale(strength),
							Utils.RAND.nextFloat()*0.01f
					);
					spawnVanillaParticle(EnumParticleTypes.SMOKE_LARGE, vv, motion);
				}
			}
			for(int i = 0; i < 4; i += 1)
			{
				vv = new Vec3d(position).add(getRandXZ(randFloat));
				spawnBlockFragmentFX(vv, withY(getRandXZ(randFloat).scale(strength*0.0125), randFloat.get()*strength*0.1), 1f, state);
			}
		}
	}

	private static Vec3d getPositiveRand(Supplier<Float> s)
	{
		return new Vec3d(s.get(), s.get(), s.get());
	}

	private static Vec3d getPositiveXZRand(Supplier<Float> s)
	{
		return new Vec3d(s.get(), 0, s.get());
	}

	private static Vec3d getRandXZ(Supplier<Float> s)
	{
		return new Vec3d((Utils.RAND.nextFloat()-0.5f), 0, (Utils.RAND.nextFloat()-0.5f)).scale(2);
	}

	public static void spawnBlockFragmentFX(Vec3d pos, Vec3d motion, float size, IBlockState state)
	{
		ParticleBlockFragment particle = new ParticleBlockFragment(getWorld(), pos, motion, size, state);
		particle.init();
		//Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		particleRenderer.addEffect(particle);
	}

	public static void spawnTracerFX(Vec3d pos, Vec3d motion, float size, int color)
	{
		ParticleTracer particle = new ParticleTracer(getWorld(), pos, motion, size, color);
		particleRenderer.addEffect(particle);
	}

	public static void spawnGunfireFX(Vec3d pos, Vec3d motion, float size)
	{
		ParticleGunfire particle = new ParticleGunfire(getWorld(), pos, motion, size);
		particleRenderer.addEffect(particle);
	}

	public static void spawnTMTModelFX(Vec3d pos, Vec3d motion, float size, ModelRendererTurbo model, ResourceLocation texture)
	{
		Particle particle = new ParticleTMTModel(getWorld(), pos, motion, size, model, texture);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	public static void spawnFlameFX(Vec3d pos, Vec3d motion, float size, int lifeTime)
	{
		ParticleFlame particle = new ParticleFlame(getWorld(), pos, motion, size, lifeTime);
		particleRenderer.addEffect(particle);
	}

	public static void spawnExplosionFX(Vec3d pos, Vec3d motion, float size)
	{
		ParticleExplosion particle = new ParticleExplosion(getWorld(), pos, motion, size);
		particleRenderer.addEffect(particle);
	}

	public static void spawnExplosionPhosphorusFX(Vec3d pos)
	{
		for(int i = 0; i < 40; i += 1)
		{
			float level = (float)Math.floor(i/10f);
			Vec3d v = new Vec3d(1, 0, 0).rotateYaw(i/10f*360f).scale(0.25*(4/level*0.175));

			ParticleExplosion particle = new ParticleExplosion(getWorld(), pos.addVector(0, 0.025+(level*0.65f), 0), v, 4);
			particleRenderer.addEffect(particle);
		}

	}

	public static void spawnFlameExplosion(Vec3d pos, float size, Random rand)
	{

		for(int i = 0; i < 20*size; i += 1)
		{
			Vec3d v = new Vec3d(1, 0, 0).rotateYaw(i/20f*360f);

			ParticleCloud particle = (ParticleCloud)spawnVanillaParticle(EnumParticleTypes.CLOUD, pos, withY(v.scale(0.25), 0.125));
			if(particle!=null)
			{
				particle.setRBGColorF(rand.nextFloat()*0.125f, rand.nextFloat()*0.125f, 0);
				particle.multipleParticleScaleBy(2.5f);
				particle.setMaxAge(10);
			}

		}

		for(int i = 0; i < 100*size; i += 1)
		{
			Vec3d v = new Vec3d(1, 0, 0).rotateYaw(i/25f*360f);
			float level = (float)Math.floor(i/25f);

			ParticleUtils.spawnExplosionFX(
					pos.addVector(0, 0.025+(level*0.65f), 0),
					withY(v.scale(0.25*(4/level*0.175)), 0.0125),
					8*size);

		}
	}

	public static void spawnShockwave(Vec3d pos, float size, float speed)
	{
		for(int i = 0; i < 50*(size/20); i += 1)
		{
			Vec3d v = new Vec3d(1, 0, 0).rotateYaw(i/50f*360f);

			ParticleShockwave particle = new ParticleShockwave(getWorld(), pos, withY(v.scale(speed), -speed*0.02f), size);
			particle.setMaxAge((int)(40*(speed/2.5f)));
			particleRenderer.addEffect(particle);
		}
	}

	public static void spawnGasCloud(Vec3d pos, float size, Fluid fluid)
	{
		Vec3d v = pos.add(getPositiveXZRand(randFloat).scale(size));
		ParticleGasCloud particle = new ParticleGasCloud(getWorld(), v, size*16, fluid);
		particle.setMaxAge((int)(80*size));
		particleRenderer.addEffect(particle);
	}

	public static void spawnFog(Vec3d pos, float size, float speed, float yspeed)
	{
		for(int i = 0; i < 36*(size/20); i += 1)
		{
			Vec3d v = new Vec3d(1, 0, 0).rotateYaw(i/36f*360f);
			ParticleAtomFog particle = new ParticleAtomFog(getWorld(), pos, withY(scaleXZ(v, speed), yspeed), size);
			particle.setMaxAge((int)(40*(size/20)));
			particleRenderer.addEffect(particle);
		}
	}

	public static void spawnAtomicBoomCore(EntityAtomicBoom entity, Vec3d pos, float size, float speed, float yspeed)
	{
		for(int i = 0; i < 36*(size/20); i += 1)
		{
			Vec3d v = new Vec3d(1.5, 0, 0).rotateYaw(i/36f*360f);
			ParticleAtomicBoomCore particle = new ParticleAtomicBoomCore(getWorld(),
					pos.add(withY(v, 0)),
					withY(scaleXZ(v, speed), yspeed),
					size);
			particle.setMaxAge((int)(120*(size/20)));
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}
	}

	public static void spawnAtomicBoomRing(EntityAtomicBoom entity, Vec3d pos, float size, float speed, float yspeed)
	{
		for(int i = 0; i < 36*(size/20); i += 1)
		{
			Vec3d v = new Vec3d(1, 0, 0).rotateYaw(i/36f*360f);
			ParticleAtomicBoomRing particle = new ParticleAtomicBoomRing(getWorld(), pos, withY(scaleXZ(v, speed), yspeed), size);
			particle.setMaxAge((int)(40*(size/20)));
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}
	}

	public static void spawnFlareFX(Vec3d pos, int colour, float size)
	{
		ParticleFlareFlash particle = new ParticleFlareFlash(getWorld(), pos, colour, size*6f);
		particle.setMaxAge(20);
		particleRenderer.addEffect(particle);
	}

	public static void spawnFlareTraceFX(Vec3d pos, int colour, float size)
	{
		ParticleFlareTrace particle = new ParticleFlareTrace(getWorld(), pos, size, colour, 125);
		particleRenderer.addEffect(particle);
	}

	//--- Utils ---//

	private static WorldClient getWorld()
	{
		return ClientUtils.mc().world;
	}

	private static Particle spawnVanillaParticle(EnumParticleTypes particle, Vec3d pos, Vec3d motion)
	{
		return ClientUtils.mc().effectRenderer.spawnEffectParticle(particle.getParticleID(),
				pos.x, pos.y, pos.z,
				motion.x, motion.y, motion.z);
	}

	private static Vec3d scaleXZ(Vec3d vector, double scale)
	{
		return new Vec3d(vector.x*scale, vector.y, vector.z*scale);
	}

	private static Vec3d withY(Vec3d vec3d, double y)
	{
		return new Vec3d(vec3d.x, y, vec3d.z);
	}
}