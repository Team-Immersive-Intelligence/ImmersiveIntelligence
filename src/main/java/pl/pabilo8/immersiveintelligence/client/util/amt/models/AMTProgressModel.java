package pl.pabilo8.immersiveintelligence.client.util.amt.models;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIAnimationGroup;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIShaderLine;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation.IIVectorLine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

/**
 * Generic progress model for AMT-based construction/upgrade rendering.
 *
 * @param <DEVICE> Device type
 * @param <STAGE>  Stage enum type
 * @author Pabilo8 (pabilo@iiteam.net)
 */
@ParametersAreNonnullByDefault
public abstract class AMTProgressModel<DEVICE, STAGE> implements AMTRenderable
{
	protected final IIAnimationCompiledMap animation;
	protected final AMTModel model;
	protected final AMT assembledModel;
	protected final int steps;

	public AMTProgressModel(ResourceLocation model, ResourceLocation animation)
	{
		this(new AMTModel(DefaultVertexFormats.BLOCK, model), animation);
	}

	public AMTProgressModel(AMTModel model, ResourceLocation animation)
	{
		IIAnimation loaded = AMTLoader.loadAnimation(animation);
		this.animation = IIAnimationCompiledMap.create(model, new IIAnimation(animation,
				Arrays.stream(loaded.groups)
						.map(g -> new IIAnimationGroup(g.groupName, g.position, expandScale(g.scale), g.rotation, null, vecToAlpha(g.position), null))
						.toArray(IIAnimationGroup[]::new)));
		this.steps = this.animation.size();
		this.model = new AMTModel(model.stream().filter(this.animation::containsKey).toArray(AMT[]::new));
		this.assembledModel = this.model.batch("batched");
	}

	private IIVectorLine expandScale(@Nullable IIVectorLine scale)
	{
		if(scale==null)
		{
			final Vec3d expanded = new Vec3d(1.005f, 1.005f, 1.005f);
			return new IIVectorLine(new float[]{0f, 1f}, new Vec3d[]{expanded, expanded});
		}
		return new IIVectorLine(scale.timeframes, scale.values, 1.005f);
	}

	@Nullable
	protected IIShaderLine vecToAlpha(@Nullable IIVectorLine position)
	{
		if(position==null||position.values.length < 2)
			return null;
		return new IIShaderLine(Shaders.ALPHA,
				new float[]{0f, position.timeframes[0], position.timeframes[position.timeframes.length-1], 1f},
				new Float[][]{
						{0f},
						{0f},
						{1f},
						{1f}
				}
		);
	}

	/**
	 * Render the progress animation for the given device.
	 *
	 * @param device       the device
	 * @param tes          tessellator
	 * @param buf          buffer
	 * @param partialTicks partial ticks
	 * @return the stage enum
	 */
	public abstract STAGE renderProgress(DEVICE device, Tessellator tes, BufferBuilder buf, float partialTicks);

	@Override
	public void disposeOf()
	{
		AMTUtils.disposeOf(model);
	}

	@Override
	@Nonnull
	public AxisAlignedBB getBoundingBox()
	{
		return model.getBoundingBox();
	}

	@Override
	public void defaultize()
	{
		model.defaultize();
	}

	@Override
	public void render(Tessellator tes, BufferBuilder buf)
	{
		model.render(tes, buf);
	}

	public AMT getPart(String name)
	{
		return model.getPartRecursive(name);
	}
}

