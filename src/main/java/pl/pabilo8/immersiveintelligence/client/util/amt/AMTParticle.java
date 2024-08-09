package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.fx.builder.ParticleBuilder;
import pl.pabilo8.immersiveintelligence.client.fx.particles.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.utils.DrawStages;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIModelHeader;

import java.util.function.Consumer;

/**
 * AMT type for drawing particle effects (gunshots, smoke, etc.)
 *
 * @author Pabilo8
 * @since 21.08.2022
 */
public class AMTParticle extends AMT
{
	private IIParticle particle;

	public AMTParticle(String name, Vec3d originPos)
	{
		super(name, originPos);
	}

	public AMTParticle(String name, IIModelHeader header)
	{
		this(name, header.getOffset(name));
	}

	/**
	 * Sets the displayed particle
	 *
	 * @param particleBuilder Builder of type T Particles
	 * @param <T>             Particle type
	 * @return this
	 */
	public <T extends IIParticle> AMTParticle setParticle(ParticleBuilder<T> particleBuilder)
	{
		return setParticle(particleBuilder, tParticleBuilder -> {
		});
	}

	/**
	 * Sets the displayed particle
	 *
	 * @param particleBuilder   Builder of type T Particles
	 * @param additionalOptions Use to apply additional settings
	 * @param <T>               Particle type
	 * @return this
	 */
	public <T extends IIParticle> AMTParticle setParticle(ParticleBuilder<T> particleBuilder, Consumer<ParticleBuilder<T>> additionalOptions)
	{
		additionalOptions.accept(particleBuilder);
		this.particle = particleBuilder.buildParticle(Vec3d.ZERO, Vec3d.ZERO, rot);
		this.particle.enableAMTDrawMode();
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
		return setParticle(ParticleRegistry.getParticleBuilder(particleName));
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(particle==null)
			return;

		//Uses the custom property value for age
		particle.setProgress(property);
		GlStateManager.translate(originPos.x, originPos.y, originPos.z);

		//Set default particle drawing conditions
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		GlStateManager.disableCull();

		//Set up BufferBuilder with the particle stage
		DrawStages drawStage = particle.getDrawStage();
		drawStage.prepareRender(buf, 0);

		//Render
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
}
