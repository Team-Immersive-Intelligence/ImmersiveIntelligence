package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataDebugger;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation;

/**
 * @author Pabilo8
 * @since 30.08.2020
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "block/device/data_debugger", clazz = TileEntityDataDebugger.class)
public class DataDebuggerRenderer extends IITileRenderer<TileEntityDataDebugger>
{
	private static IIAnimation construction;
	private static IIAnimationCompiledMap constructionMap;
	private static AMT[] models = null;

	@Override
	public void draw(TileEntityDataDebugger te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		if(te.setupTime > 0)
		{
			float progress = IIAnimationUtils.getAnimationProgress(te.setupTime, 25, true, partialTicks);

			//apply animation
			constructionMap.apply(progress);

			//apply rotation for block facing
			applyStandardRotation(te.getFacing());

			//render
			for(AMT mod : models)
				mod.render(tes, buf);
		}
	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		construction = IIAnimationLoader.loadAnimation(new ResourceLocation(ImmersiveIntelligence.MODID, "data_debugger_construction"));
		models = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()));
		constructionMap = IIAnimationCompiledMap.create(models, construction);
	}

	@Override
	protected void nullifyModels()
	{
		models = IIAnimationUtils.disposeOf(models);
		constructionMap = null;
		construction = null;
	}
}
