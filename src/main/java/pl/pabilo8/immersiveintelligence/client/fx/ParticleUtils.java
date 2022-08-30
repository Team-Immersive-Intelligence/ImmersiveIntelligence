package pl.pabilo8.immersiveintelligence.client.fx;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleCloud;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleGasCloud.ParticleFlareFlash;
import pl.pabilo8.immersiveintelligence.client.fx.nuke.ParticleAtomFog;
import pl.pabilo8.immersiveintelligence.client.fx.nuke.ParticleAtomicBoomCore;
import pl.pabilo8.immersiveintelligence.client.fx.nuke.ParticleAtomicBoomRing;
import pl.pabilo8.immersiveintelligence.client.fx.nuke.ParticleShockwave;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityAtomicBoom;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

	public static void spawnExplosionBoomFX(World world, double x, double y, double z, float radius, float strength, boolean flaming, boolean damagesTerrain)
	{
		ParticleUtils.spawnShockwave(x, y, z, radius, 0.75f*strength);
		if(radius < 6)
			ParticleUtils.spawnExplosionFX(x, y, z, 0, 0, 0, radius*1.85f);

		Set<BlockPos> positions = new HashSet<>(new IIExplosion(world, null, x, y, z, radius, strength, flaming, true).generateAffectedBlockPositions());

		//Custom sounds
		world.playSound(Minecraft.getMinecraft().player, x, y, z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F+(world.rand.nextFloat()-world.rand.nextFloat())*0.2F)*0.7F);

		for(BlockPos position : positions)
		{
			IBlockState state = world.getBlockState(position);

			double d0 = (float)position.getX()+world.rand.nextFloat();
			double d1 = (float)position.getY()+world.rand.nextFloat();
			double d2 = (float)position.getZ()+world.rand.nextFloat();
			double d3 = d0-x;
			double d4 = d1-y;
			double d5 = d2-z;
			double d6 = MathHelper.sqrt(d3*d3+d4*d4+d5*d5);
			d3 = d3/d6;
			d4 = d4/d6;
			d5 = d5/d6;
			double d7 = 0.5D/(d6/(double)radius+0.1D);
			d7 = d7*(double)(world.rand.nextFloat()*world.rand.nextFloat()+0.3F);
			d3 = d3*d7;
			d4 = d4*d7;
			d5 = d5*d7;


			ParticleUtils.spawnBlockFragmentFX(d0, d1, d2, d3, d4, d5, 1f, state);

			world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0+x)/2.0D, (d1+y)/2.0D, (d2+z)/2.0D, d3, d4, d5);
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5);
			Vec3d vv = new Vec3d(position).addVector(Utils.RAND.nextFloat(), 0, Utils.RAND.nextFloat());

			ParticleUtils.spawnExplosionFX(vv.x, vv.y, vv.z, 0, 0, 0, 2f);
			//if(state.getBlock().isFlammable(world,position,
			//					EnumFacing.getFacingFromVector((float)x-position.getX(),(float)y-position.getY(),(float)z-position.getZ())))
			if(flaming)
			{
				for(int i = 0; i < 7; i += 1)
				{
					vv = new Vec3d(position).addVector(Utils.RAND.nextFloat(), 0, Utils.RAND.nextFloat());
					ParticleUtils.spawnFlameFX(vv.x, vv.y, vv.z, (Utils.RAND.nextFloat()-0.5f)*2f*strength, Utils.RAND.nextFloat()*0.01f, (Utils.RAND.nextFloat()-0.5f)*2f*strength, 4f, 40+(10*i));
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, vv.x, vv.y, vv.z, (Utils.RAND.nextFloat()-0.5f)*0.25f*strength, Utils.RAND.nextFloat()*0.01f, (Utils.RAND.nextFloat()-0.5f)*0.25f*strength);
				}
			}
			for(int i = 0; i < 16; i += 1)
			{
				vv = new Vec3d(position).addVector(Utils.RAND.nextFloat(), 0, Utils.RAND.nextFloat());
				ParticleUtils.spawnBlockFragmentFX(vv.x, vv.y, vv.z, (Utils.RAND.nextFloat()-0.5f)*2f*strength, Utils.RAND.nextFloat()*strength*1.5f, (Utils.RAND.nextFloat()-0.5f)*2f*strength, 1f, state);
			}
		}
	}

	public static void spawnBlockFragmentFX(double x, double y, double z, double mx, double my, double mz, float size, IBlockState state)
	{
		ParticleBlockFragment particle = new ParticleBlockFragment(ClientUtils.mc().world, x, y, z, mx, my, mz, size, state);
		particle.init();
		//Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		particleRenderer.addEffect(particle);
	}

	public static void spawnTracerFX(double x, double y, double z, double mx, double my, double mz, float size, int color)
	{
		ParticleTracer particle = new ParticleTracer(ClientUtils.mc().world, x, y, z, mx, my, mz, size, color);
		particleRenderer.addEffect(particle);
	}

	public static void spawnGunfireFX(double x, double y, double z, double mx, double my, double mz, float size)
	{
		ParticleGunfire particle = new ParticleGunfire(ClientUtils.mc().world, x, y, z, mx, my, mz, size);
		particleRenderer.addEffect(particle);
	}

	public static void spawnTMTModelFX(double x, double y, double z, double mx, double my, double mz, float size, ModelRendererTurbo model, ResourceLocation texture)
	{
		Particle particle = new ParticleTMTModel(ClientUtils.mc().world, x, y, z, mx, my, mz, size, model, texture);
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	public static void spawnFlameFX(double x, double y, double z, double mx, double my, double mz, float size, int lifeTime)
	{
		ParticleFlame particle = new ParticleFlame(ClientUtils.mc().world, x, y, z, mx, my, mz, size, lifeTime);
		particleRenderer.addEffect(particle);
	}

	public static void spawnExplosionFX(double x, double y, double z, double mx, double my, double mz, float size)
	{
		ParticleExplosion particle = new ParticleExplosion(ClientUtils.mc().world, x, y, z, mx, my, mz, size);
		particleRenderer.addEffect(particle);
	}

	public static void spawnExplosionPhosphorusFX(double x, double y, double z)
	{
		for(int i = 0; i < 40; i += 1)
		{
			Vec3d v = new Vec3d(1, 0, 0).rotateYaw(i/10f*360f);
			float level = (float)Math.floor(i/10f);
			ParticleExplosion particle = new ParticleExplosion(ClientUtils.mc().world, x, y+0.025+(level*0.65f), z, v.x*0.25*(4/level*0.175), 0.0125, v.z*0.25*(4/level*0.175), 4);
			particleRenderer.addEffect(particle);
		}

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

	public static void spawnShockwave(double x, double y, double z, float size, float speed)
	{
		for(int i = 0; i < 50*(size/20); i += 1)
		{
			Vec3d v = new Vec3d(1, 0, 0).rotateYaw(i/50f*360f);
			ParticleShockwave particle = new ParticleShockwave(ClientUtils.mc().world, x, y, z, v.x*speed, -speed*0.02f, v.z*speed, size);
			particle.setMaxAge((int)(40*(speed/2.5f)));
			particleRenderer.addEffect(particle);
		}
	}

	public static void spawnGasCloud(double x, double y, double z, float size, Fluid fluid)
	{
		Vec3d v = new Vec3d(Utils.RAND.nextFloat()-0.5, 0, Utils.RAND.nextFloat()-0.5).scale(size);
		ParticleGasCloud particle = new ParticleGasCloud(ClientUtils.mc().world, x+v.x, y, z+v.z, size*16, fluid);
		particle.setMaxAge((int)(80*size));
		particleRenderer.addEffect(particle);
	}

	public static void spawnFog(double x, double y, double z, float size, float speed, float yspeed)
	{
		for(int i = 0; i < 36*(size/20); i += 1)
		{
			Vec3d v = new Vec3d(1, 0, 0).rotateYaw(i/36f*360f);
			ParticleAtomFog particle = new ParticleAtomFog(ClientUtils.mc().world, x, y, z, v.x*speed, yspeed, v.z*speed, size);
			particle.setMaxAge((int)(40*(size/20)));
			particleRenderer.addEffect(particle);
		}
	}

	public static void spawnAtomicBoomCore(EntityAtomicBoom atomicBoom, double x, double y, double z, float size, float speed, float yspeed)
	{
		for(int i = 0; i < 36*(size/20); i += 1)
		{
			Vec3d v = new Vec3d(1.5, 0, 0).rotateYaw(i/36f*360f);
			ParticleAtomicBoomCore particle = new ParticleAtomicBoomCore(ClientUtils.mc().world, x+v.x, y, z+v.z, v.x*speed, yspeed, v.z*speed, size);
			particle.setMaxAge((int)(120*(size/20)));
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
			//particleRenderer.addEffect(particle);
			//atomicBoom.particles.add(particle);
		}
	}

	public static void spawnAtomicBoomRing(EntityAtomicBoom atomicBoom, double x, double y, double z, float size, float speed, float yspeed)
	{
		for(int i = 0; i < 36*(size/20); i += 1)
		{
			Vec3d v = new Vec3d(1, 0, 0).rotateYaw(i/36f*360f);
			ParticleAtomicBoomRing particle = new ParticleAtomicBoomRing(ClientUtils.mc().world, x, y, z, v.x*speed, yspeed, v.z*speed, size);
			particle.setMaxAge((int)(40*(size/20)));
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
			//particleRenderer.addEffect(particle);
			//atomicBoom.particles.add(particle);
		}
	}

	public static void spawnFlareFX(double x, double y, double z, int colour, float size)
	{
		ParticleFlareFlash particle = new ParticleFlareFlash(ClientUtils.mc().world, x, y, z, colour, size*6f);
		particle.setMaxAge(20);
		particleRenderer.addEffect(particle);
	}

	public static void spawnFlareTraceFX(double x, double y, double z, int colour, float size)
	{
		ParticleFlareTrace particle = new ParticleFlareTrace(ClientUtils.mc().world, x, y, z, size, colour, 125);
		particleRenderer.addEffect(particle);
	}
}