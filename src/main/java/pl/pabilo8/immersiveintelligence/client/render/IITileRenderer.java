package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.client.animation.AMT;
import pl.pabilo8.immersiveintelligence.client.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.animation.IIAnimationUtils;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 05.04.2022
 */
public abstract class IITileRenderer<T extends TileEntity> extends TileEntitySpecialRenderer<T> implements IReloadableModelContainer<IITileRenderer<T>>
{
	boolean unCompiled = true;

	//--- rendering wrapper ---//

	@Override
	public final void render(@Nullable T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			// TODO: 05.04.2022 test if remove?
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
			if(Minecraft.isAmbientOcclusionEnabled())
				GlStateManager.shadeModel(7425);
			else
				GlStateManager.shadeModel(7424);

			if(unCompiled)
			{
				compileModels(IIAnimationUtils.getAnimationBakedModel(te));
				unCompiled = false;
			}

			draw(te, Tessellator.getInstance().getBuffer(), partialTicks, Tessellator.getInstance());

			GlStateManager.popMatrix();
		}
	}

	@Override
	public final void reloadModels()
	{
		unCompiled = true;
		nullifyModels();
	}

	//--- abstract methods ---//

	/**
	 * @param te
	 * @param buf          Buffer, by default provided by the Tessellator
	 * @param partialTicks partial time of drawing
	 * @param tes          Tessellator drawing the models, by default the vanilla one
	 */
	public abstract void draw(T te, BufferBuilder buf, float partialTicks, Tessellator tes);

	/**
	 * Load the {@link AMT} and prepare {@link IIAnimationCompiledMap} here.
	 *
	 * @param sModel tuple of the blockstate and model, use it to load model groups
	 */
	public abstract void compileModels(Tuple<IBlockState, IBakedModel> sModel);

	protected abstract void nullifyModels();
}
