package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.IESmartObjModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationCompiledMap;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Pabilo8
 * @since 05.04.2022
 */
public abstract class IITileRenderer<T extends TileEntity> extends TileEntitySpecialRenderer<T> implements IReloadableModelContainer<IITileRenderer<T>>
{
	private boolean unCompiled = true;

	//--- rendering wrapper ---//

	@Override
	public final void render(@Nullable T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(shouldNotRender(te))
			return;
		//fixes crash on loading
		if(te!=null&&unCompiled)
		{
			Tuple<IBlockState, IBakedModel> model = getModelFromBlockState(te);
			if(model.getSecond() instanceof IESmartObjModel)
			{
				nullifyModels();
				compileModels(model);
				unCompiled = false;
			}
			else
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
		draw(te, Tessellator.getInstance().getBuffer(), partialTicks, Tessellator.getInstance());

		GlStateManager.cullFace(CullFace.BACK);
		GlStateManager.popMatrix();
	}

	@Override
	public final void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
	{

	}

	protected Tuple<IBlockState, IBakedModel> getModelFromBlockState(T te)
	{
		if(!te.hasWorld())
			return null;

		//Grab model + dynamic render state
		final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		IBlockState state = te.getWorld().getBlockState(te.getPos());

		state = state.getBlock().getActualState(state, te.getWorld(), te.getPos());
		state = state.withProperty(IEProperties.DYNAMICRENDER, true);
		return new Tuple<>(state, blockRenderer.getBlockModelShapes().getModelForState(state));
	}

	/**
	 * Performs a standardised rotation task for the animated model
	 *
	 * @param facing current facing of the TileEntity
	 */
	protected void applyStandardRotation(EnumFacing facing)
	{
		GlStateManager.translate(0.5, 0.5, 0.5);
		GlStateManager.rotate(facing.getHorizontalAngle(), 0, -1, 0);
		GlStateManager.translate(-0.5, -0.5, -0.5);
	}

	/**
	 * Mirrors (scales by -1) the rendering on X axis<br>
	 * Changes culled face to front (due to the flip)
	 */
	protected final void mirrorRender()
	{
		GlStateManager.cullFace(CullFace.FRONT);
		GlStateManager.scale(-1, 1, 1);
		GlStateManager.translate(-1, 0, 0);
	}

	protected final void unMirrorRender()
	{
		GlStateManager.cullFace(CullFace.BACK);
	}

	@Override
	public final void reloadModels()
	{
		unCompiled = true;
	}

	//--- abstract methods ---//

	/**
	 * @param te           TileEntity to be rendered
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

	/**
	 * Called when cached models, animations should be unloaded/reloaded
	 */
	protected abstract void nullifyModels();

	/**
	 * @param te TileEntity to be rendered
	 * @return true if the TileEntity shouldn't be rendered
	 */
	protected boolean shouldNotRender(T te)
	{
		return te==null;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface RegisteredTileRenderer
	{
		String name();

		Class<? extends TileEntity> clazz();

	}
}
