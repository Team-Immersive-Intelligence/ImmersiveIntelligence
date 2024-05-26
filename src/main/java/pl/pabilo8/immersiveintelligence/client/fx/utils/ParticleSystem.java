package pl.pabilo8.immersiveintelligence.client.fx.utils;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.fx.particles.IIParticle;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;

import java.util.*;

/**
 * Drawing Stages partially stolen from LadyLib's Particles<br>
 * Based on Embers' Particle Renderer<br>
 * Since the rework, introduces a novel, .obj model based particle format and particle chaining
 *
 * @author Pabilo8
 * @author EpicSquid
 * @updated 05.04.2024
 * @ii-approved 0.3.1
 * @since 23.12.2020
 */
@SideOnly(Side.CLIENT)
public class ParticleSystem
{
	/**
	 * The instance of the particle system
	 */
	public static final ParticleSystem INSTANCE = new ParticleSystem();

	//--- Constants ---//
	public static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");

	//--- Fields ---//
	private int particleAmount = 0;
	private final Map<DrawStages, Queue<IIParticle>> particles = new HashMap<DrawStages, Queue<IIParticle>>()
	{
		@Override
		public void clear()
		{
			super.clear();
			particleAmount = 0;
		}
	};
	private final TreeMap<Integer, List<IIParticle>> scheduledParticles = new TreeMap<>();

	//--- Update and Rendering ---//

	/**
	 * Updates all particles in the system.<br>
	 * Called every tick.
	 */
	public void updateParticles()
	{
		//if the world is null, clear all particles
		if(ClientUtils.mc().world==null)
		{
			particles.clear();
			return;
		}

		synchronized(scheduledParticles)
		{
			//spawn particles scheduled for this tick
			List<IIParticle> current = scheduledParticles.remove(0);
			if(current!=null)
				current.forEach(this::privateAddEffect);
			//cycle through the scheduled particles and decrement their timers
			scheduledParticles.keySet().forEach(i -> scheduledParticles.put(i-1, scheduledParticles.remove(i)));
		}

		int count = 0;
		//go through every particle indiscriminately
		synchronized(particles)
		{
			for(Queue<IIParticle> particleQueue : particles.values())
			{
				for(Iterator<IIParticle> iterator = particleQueue.iterator(); iterator.hasNext(); )
				{
					//particles cost a lot less to update than to render, so we can update more of them
					if(++count > Graphics.maxSimulatedParticles)
						break;
					IIParticle particle = iterator.next();
					particle.onUpdate();
					if(!particle.isAlive())
					{
						particle.onDeath();
						iterator.remove();
						particleAmount--;
					}
				}
			}
		}
	}

	private void privateAddEffect(IIParticle particle)
	{
		particles.computeIfAbsent(particle.getDrawStage(), i -> new ArrayDeque<>()).add(particle);
		particleAmount++;
	}

	/**
	 * Renders all particles in the system.<br>
	 * Called every frame.
	 *
	 * @param partialTicks partial render ticks
	 */
	public void renderParticles(float partialTicks)
	{
		float x = ActiveRenderInfo.getRotationX();
		float z = ActiveRenderInfo.getRotationZ();
		float yz = ActiveRenderInfo.getRotationYZ();
		float xy = ActiveRenderInfo.getRotationXY();
		float xz = ActiveRenderInfo.getRotationXZ();
		EntityPlayer player = Minecraft.getMinecraft().player;

		if(player!=null)
		{
			updateParticleFields(partialTicks, player);

			GlStateManager.pushMatrix();

			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
			GlStateManager.disableCull();

			GlStateManager.depthMask(false);

			Tessellator tess = Tessellator.getInstance();
			BufferBuilder buffer = tess.getBuffer();

			drawParticles:
			synchronized(particles)
			{
				for(Map.Entry<DrawStages, Queue<IIParticle>> particleStage : particles.entrySet())
				{
					int particleCount = 0;
					particleStage.getKey().prepareRender(buffer, partialTicks);
					for(IIParticle particle : particleStage.getValue())
					{
						if(++particleCount > Graphics.maxDrawnParticles)
						{
							tess.draw();
							particleStage.getKey().clear();
							break drawParticles;
						}
						particle.render(buffer, partialTicks, x, xz, z, yz, xy);
					}
					tess.draw();
					particleStage.getKey().clear();
				}
			}

			GlStateManager.enableCull();
			GlStateManager.depthMask(true);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableBlend();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
			GlStateManager.popMatrix();
		}

	}

	/**
	 * Updates static fields of the particle system used for rendering
	 *
	 * @param partialTicks partial render ticks
	 * @param player       player entity
	 */
	private static void updateParticleFields(float partialTicks, EntityPlayer player)
	{
		IIParticle.interpTicks = partialTicks;
		IIParticle.interpPosX = player.lastTickPosX+(player.posX-player.lastTickPosX)*partialTicks;
		IIParticle.interpPosY = player.lastTickPosY+(player.posY-player.lastTickPosY)*partialTicks;
		IIParticle.interpPosZ = player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*partialTicks;
		IIParticle.cameraViewDir = player.getLook(partialTicks);
	}

	//--- External ---//

	/**
	 * Adds a particle to the system
	 *
	 * @param particle the particle to add
	 */
	public void addEffect(IIParticle particle)
	{
		synchronized(particles)
		{
			scheduledParticles.computeIfAbsent(0, i -> new ArrayList<>()).add(particle);
		}
	}

	/**
	 * Schedules a particle to be added to the system after a delay
	 *
	 * @param particle the particle to add
	 * @param delay    the delay in ticks
	 * @param <T>      the type of the particle
	 */
	public <T extends IIParticle> void scheduleEffect(T particle, int delay)
	{
		synchronized(scheduledParticles)
		{
			scheduledParticles.computeIfAbsent(delay, i -> new ArrayList<>()).add(particle);
		}
	}

	public synchronized void reload()
	{
		particles.clear();
	}
}
