package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleFactory;
import pl.pabilo8.immersiveintelligence.client.fx.particles.AbstractParticle;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleDrawStages;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.function.Consumer;

/**
 * AMT type for drawing particle effects (gunshots, smoke, etc.)
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.08.2022
 */
public class AMTParticle extends AMT
{
	private AbstractParticle particle;

	public AMTParticle(String name, Vec3d originPos)
	{
		super(name, originPos);
	}

	public AMTParticle(String name, AMTModelHeader header)
	{
		this(name, header.getOffset(name));
	}

	/**
	 * Sets the displayed particle
	 *
	 * @param particleFactory Builder of type T Particles
	 * @param <T>             Particle type
	 * @return this
	 */
	public <T extends AbstractParticle> AMTParticle setParticle(ParticleFactory<T> particleFactory)
	{
		return setParticle(particleFactory, tParticleBuilder -> {
		});
	}

	/**
	 * Sets the displayed particle
	 *
	 * @param particleFactory   Builder of type T Particles
	 * @param additionalOptions Use to apply additional settings
	 * @param <T>               Particle type
	 * @return this
	 */
	public <T extends AbstractParticle> AMTParticle setParticle(ParticleFactory<T> particleFactory, Consumer<ParticleFactory<T>> additionalOptions)
	{
		additionalOptions.accept(particleFactory);
		this.particle = particleFactory.create(Vec3d.ZERO, Vec3d.ZERO, 0, 0);
		return this;
	}

	/**
	 * Sets the displayed particle using a registered particle builder
	 *
	 * @param particleName Name of the particle
	 * @return this
	 */
	public AMTParticle setParticle(String particleName)
	{
		return setParticle(ParticleRegistry.getParticle(particleName));
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(particle==null)
			return;

		//Uses the custom property value for age
		particle.setProperty(ParticleProperties.PROGRESS, property);
		GlStateManager.translate(originPos.x, originPos.y, originPos.z);

		//Set default particle drawing conditions
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		GlStateManager.disableCull();

		//Set up BufferBuilder with the particle stage
		ParticleDrawStages drawStage = particle.getDrawStage();
		drawStage.prepareRender(buf, 0);

		//Render
		AbstractParticle.interPos = Vec3d.ZERO;
		particle.preRender(
				ClientUtils.mc().getRenderPartialTicks(),
				ActiveRenderInfo.getRotationX(),
				ActiveRenderInfo.getRotationXZ(),
				ActiveRenderInfo.getRotationZ(),
				ActiveRenderInfo.getRotationYZ(),
				ActiveRenderInfo.getRotationXY()
		);
		particle.render(buf,
				ClientUtils.mc().getRenderPartialTicks(),
				ActiveRenderInfo.getRotationX(),
				ActiveRenderInfo.getRotationXZ(),
				ActiveRenderInfo.getRotationZ(),
				ActiveRenderInfo.getRotationYZ(),
				ActiveRenderInfo.getRotationXY()
		);
		tes.draw();
		drawStage.clear();
	}

	@Override
	public void disposeOf()
	{

	}

	@Override
	public void applyProperties(EasyNBT nbt)
	{
		super.applyProperties(nbt);
		nbt.checkSetString("particle", this::setParticle);
	}
}
