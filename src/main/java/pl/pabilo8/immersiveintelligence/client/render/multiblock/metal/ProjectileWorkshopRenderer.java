package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.client.render.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @updated 09.07.2024
 * @ii-approved 0.3.1
 * @since 21-06-2019
 */
@RegisteredTileRenderer(name = "projectile_workshop", clazz = TileEntityProjectileWorkshop.class)
public class ProjectileWorkshopRenderer extends IIMultiblockRenderer<TileEntityProjectileWorkshop>
{
	//TODO: 09.07.2024 model for upgrades
	AMT[] model;
	IIAnimationCompiledMap lid1, lid2;

	@Override
	public void drawAnimated(TileEntityProjectileWorkshop te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		lid1.apply(te.lid1.getProgress(partialTicks));
		lid2.apply(te.lid2.getProgress(partialTicks));

		applyStandardMirroring(te, true);
		for(AMT amt : model)
			amt.render(tes, buf);

	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		model = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()));
		/*active = new IIBooleanAnimation(
				IIAnimationUtils.getPart(model, "conveyor_on"),
				IIAnimationUtils.getPart(model, "conveyor_off")
		);*/
		lid1 = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "projectile_workshop/left_door"));
		lid2 = IIAnimationCompiledMap.create(model, ResLoc.of(IIReference.RES_II, "projectile_workshop/right_door"));
	}
}
