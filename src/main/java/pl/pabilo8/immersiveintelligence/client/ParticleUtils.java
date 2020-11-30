package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.fx.*;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

import java.util.Random;

/**
 * @author Pabilo8
 * @since 17.07.2020
 * <p>
 * A class for creating II particle effects
 * IE has methods in its proxy, while II uses this class
 */
public class ParticleUtils
{
	public static void spawnTracerFX(double x, double y, double z, double mx, double my, double mz, float size, int color)
	{
		Particle particle = new ParticleTracer(ClientUtils.mc().world, x, y, z, mx, my, mz, size, color);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	public static void spawnGunfireFX(double x, double y, double z, double mx, double my, double mz, float size)
	{
		Particle particle = new ParticleGunfire(ClientUtils.mc().world, x, y, z, mx, my, mz, size);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	public static void spawnTMTModelFX(double x, double y, double z, double mx, double my, double mz, float size, ModelRendererTurbo model, String texture)
	{
		Particle particle = new ParticleTMTModel(ClientUtils.mc().world, x, y, z, mx, my, mz, size, model, texture);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	public static void spawnFlameFX(double x, double y, double z, double mx, double my, double mz, float size, int lifeTime)
	{
		Particle particle = new ParticleFlame(ClientUtils.mc().world, x, y, z, mx, my, mz, size, lifeTime);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	public static void spawnExplosionFX(double x, double y, double z, double mx, double my, double mz, float size)
	{
		Particle particle = new ParticleExplosion(ClientUtils.mc().world, x, y, z, mx, my, mz, size);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	public static void spawnFlameExplosion(double x, double y, double z, float size)
	{
		spawnFlameExplosion(x, y, z, size, Utils.RAND);
	}

	public static void spawnFlameExplosion(double x, double y, double z, float size, Random rand)
	{

		for(int i = 0; i < 20*size; i += 1)
		{
			Vec3d v = new Vec3d(1, 0, 0).rotateYaw(i/20f*360f);
			ParticleCloud particle = (ParticleCloud)ClientUtils.mc().effectRenderer.spawnEffectParticle(EnumParticleTypes.CLOUD.getParticleID(), x, y+0.25, z, v.x*0.25, 0.125, v.z*0.25, 4);
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
			ParticleUtils.spawnExplosionFX(x, y+0.025+(level*0.65f), z, v.x*0.25*(4/level*0.175), 0.0125, v.z*0.25*(4/level*0.175), 8*size);
		}
	}
}

/*

 */