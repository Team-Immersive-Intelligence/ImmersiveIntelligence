package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.fx.factories.ParticleFactory;
import pl.pabilo8.immersiveintelligence.client.fx.particles.AbstractParticle;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleDrawStages;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.nio.FloatBuffer;
import java.util.function.Consumer;

/**
 * AMT type for drawing particle effects (gunshots, smoke, etc.)
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 16.08.2026
 * @since 21.08.2022
 */
public class AMTParticle extends AMT
{
	private final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
	private AbstractParticle particle;
	private boolean correctRotation;

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

	/**
	 * Sets whether inherited model rotation is removed before particle rendering.
	 *
	 * @param correctRotation whether the particle uses camera-space rotation
	 * @return this
	 */
	public AMTParticle setCorrectRotation(boolean correctRotation)
	{
		this.correctRotation = correctRotation;
		return this;
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(particle==null)
			return;

		//Use the custom property value for particle progress.
		particle.setProperty(ParticleProperties.PROGRESS, property);

		GlStateManager.pushMatrix();
		GlStateManager.translate(originPos.x, originPos.y, originPos.z);

		float x = ActiveRenderInfo.getRotationX();
		float xz = ActiveRenderInfo.getRotationXZ();
		float z = ActiveRenderInfo.getRotationZ();
		float yz = ActiveRenderInfo.getRotationYZ();
		float xy = ActiveRenderInfo.getRotationXY();

		//Camera-facing particles must not inherit rotations from AMT parents or the model renderer.
		if(correctRotation)
		{
			removeModelViewRotation();
			x = xz = 1f;
			z = yz = xy = 0f;
		}

		//Set default particle drawing conditions
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		GlStateManager.disableCull();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();

		//Set up BufferBuilder with the particle stage
		ParticleDrawStages drawStage = particle.getDrawStage();
		drawStage.prepareRender(buf, 0);

		//Render
		AbstractParticle.interPos = Vec3d.ZERO;
		particle.preRender(0, x, xz, z, yz, xy);
		particle.render(buf, 0, x, xz, z, yz, xy);
		tes.draw();
		drawStage.clear();

		GlStateManager.depthMask(true);
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

	/**
	 * Removes rotation from the current model-view matrix and keeps position and scale.
	 */
	private void removeModelViewRotation()
	{
		modelView.clear();
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelView);

		float scaleX = length(modelView.get(0), modelView.get(1), modelView.get(2));
		float scaleY = length(modelView.get(4), modelView.get(5), modelView.get(6));
		float scaleZ = length(modelView.get(8), modelView.get(9), modelView.get(10));
		float x = modelView.get(12);
		float y = modelView.get(13);
		float z = modelView.get(14);

		modelView.clear();
		modelView.put(new float[]{
				scaleX, 0, 0, 0,
				0, scaleY, 0, 0,
				0, 0, scaleZ, 0,
				x, y, z, 1
		});
		modelView.flip();
		GL11.glLoadMatrix(modelView);
	}

	private static float length(float x, float y, float z)
	{
		return (float)Math.sqrt(x*x+y*y+z*z);
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
		nbt.checkSetBoolean("correct_rotation", this::setCorrectRotation);
	}

	@Override
	protected AMT renamedCopy(String newName)
	{
		AMTParticle particle = new AMTParticle(newName, originPos);
		particle.particle = this.particle;
		particle.correctRotation = this.correctRotation;
		return particle;
	}
}
