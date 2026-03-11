package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataDebugger;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2020
 */
@SideOnly(Side.CLIENT)
@RegisteredTileRenderer(name = "block/device/data_debugger", clazz = TileEntityDataDebugger.class)
public class DataDebuggerRenderer extends IITileRenderer<TileEntityDataDebugger>
{
	private AMTModel model;
	private IIAnimationCompiledMap construction;

	@Override
	public void draw(TileEntityDataDebugger te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		if(te.setupTime > 0)
		{
			float progress = AMTUtils.getAnimationProgress(te.setupTime, 25, true, partialTicks);

			//apply animation
			construction.apply(progress);

			//apply rotation for block facing
			applyStandardRotation(te.getFacing());

			//render
			model.render(tes, buf);
		}
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model);
		this.construction = IIAnimationCompiledMap.create(this.model,
				new ResourceLocation(ImmersiveIntelligence.MODID, "data_debugger_construction"));
	}

	@Override
	protected void nullifyModels()
	{
		this.model = AMTUtils.disposeOf(model);
		this.construction = null;
	}
}
