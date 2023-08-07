package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationCompiledMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 14.12.2022
 */
public abstract class IIEntityRenderer<E extends Entity> extends Render<E> implements IReloadableModelContainer<IIEntityRenderer<E>>
{
	private boolean unCompiled = true;

	protected IIEntityRenderer(RenderManager render, String name)
	{
		super(render);
		subscribeToList(name);
	}

	@Override
	public final void doRender(@Nonnull E entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		if(unCompiled)
		{
			nullifyModels();
			compileModels();
			unCompiled = false;
			return;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		if(Minecraft.isAmbientOcclusionEnabled())
			GlStateManager.shadeModel(7425);
		else
			GlStateManager.shadeModel(7424);

		ClientUtils.bindAtlas();
		draw(entity, Tessellator.getInstance().getBuffer(), partialTicks, Tessellator.getInstance());

		GlStateManager.cullFace(CullFace.BACK);
		GlStateManager.popMatrix();

	}

	@Override
	public final void reloadModels()
	{
		unCompiled = true;
	}

	@Nullable
	@Override
	protected final ResourceLocation getEntityTexture(@Nonnull E e)
	{
		return null;
	}

	//--- abstract methods ---//

	/**
	 * @param entity       Entity to be rendered
	 * @param buf          Buffer, by default provided by the Tessellator
	 * @param partialTicks partial time of drawing
	 * @param tes          Tessellator drawing the models, by default the vanilla one
	 */
	public abstract void draw(E entity, BufferBuilder buf, float partialTicks, Tessellator tes);

	/**
	 * Load the {@link AMT} and prepare {@link IIAnimationCompiledMap} here.
	 */
	public abstract void compileModels();


	/**
	 * Called when cached models, animations should be unloaded/reloaded
	 */
	protected abstract void nullifyModels();
}
