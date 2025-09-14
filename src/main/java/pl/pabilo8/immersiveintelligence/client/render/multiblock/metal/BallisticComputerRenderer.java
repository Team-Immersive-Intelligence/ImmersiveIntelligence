package pl.pabilo8.immersiveintelligence.client.render.multiblock.metal;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIMultiblockRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityBallisticComputer;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
@RegisteredTileRenderer(name = "ballistic_computer", clazz = TileEntityBallisticComputer.class)
public class BallisticComputerRenderer extends IIMultiblockRenderer<TileEntityBallisticComputer>
{
	//TODO: 12.08.2025 Replace with AMTFluid

	@Override
	public void drawAnimated(TileEntityBallisticComputer te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//draw cocoa
		if(te.progress > 0)
		{
			GlStateManager.pushMatrix();

			ClientUtils.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE.toString());
			TextureAtlasSprite sprite = ClientUtils.mc().getTextureMapBlocks().getAtlasSprite(IEContent.fluidBiodiesel.getStill(new FluidStack(IEContent.fluidBiodiesel, 1000)).toString());

			if(te.progress < 3)
				sprite = ClientUtils.mc().getTextureMapBlocks().getAtlasSprite(IEContent.fluidConcrete.getStill(new FluidStack(IEContent.fluidConcrete, 1000)).toString());
			else if(te.progress < 6)
				GlStateManager.color(0.75f, 0.75f, 0.75f);
			else if(te.progress < 10)
				GlStateManager.color(0.25f, 0.25f, 0.25f);
			else if(te.progress < 32)
				GlStateManager.color(0.5f, 0.5f, 0.5f);

			int iW = sprite.getIconWidth();
			int iH = sprite.getIconHeight();

			GlStateManager.rotate(90, 1, 0, 0);

			GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);
			if(te.mirrored)
				GlStateManager.translate(26, 5, -12);
			else
				GlStateManager.translate(26, -7, -12);
			GlStateManager.rotate(-13, 0, 0, 1);
			GlStateManager.translate(-1, 0, 0);

			if(iW > 0&&iH > 0)
				ClientUtils.drawRepeatedSprite(0, 0, 3, 2, iW, iH, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());


			GlStateManager.popMatrix();
		}
	}

	@Override
	public void drawSimple(BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{

	}
}
