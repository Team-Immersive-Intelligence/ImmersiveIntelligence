package pl.pabilo8.immersiveintelligence.client.render.ammunition;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.ammunition.TripmineRenderer.TripmineItemStackRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine.ItemBlockMineBase;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityTripMine;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoMine;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 02.02.2021
 */
@RegisteredTileRenderer(name = "tripmine", clazz = TileEntityTripMine.class, teisrClazz = TripmineItemStackRenderer.class)
public class TripmineRenderer extends IITileRenderer<TileEntityTripMine>
{
	private IAmmoModel<ItemBlockMineBase, EntityAmmoMine> model;

	@Override
	public void draw(TileEntityTripMine te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		GlStateManager.translate(0.5, 0, 0.5);
		if(te.grass)
		{
			GlStateManager.pushMatrix();
			ClientUtils.bindAtlas();

			GlStateManager.translate(-0.5, 0, -0.5);
			float[] colors = IIColor.fromPackedRGB(getWorld().getBiome(te.getPos()).getGrassColorAtPos(te.getPos())&0x7FFFFFFF).getFloatRGB();

			GL11.glShadeModel(GL11.GL_SMOOTH);
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.ZERO);

			IIAnimationUtils.getBRD().getBlockModelRenderer().renderModelBrightnessColor(
					ClientUtils.mc().getBlockRendererDispatcher().getModelForState(Blocks.TALLGRASS.getStateFromMeta(1)), 1f,
					colors[0], colors[1], colors[2]);
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}

		GlStateManager.translate(0, 0.03125f*(16-te.digLevel)-0.5, 0);
		model.renderAmmoComplete(false, te.getMineStack());
	}

	@Override
	protected Tuple<IBlockState, IBakedModel> getModelFromBlockState(TileEntityTripMine te)
	{
		return ACCEPTABLE;
	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		model = AmmoRegistry.getModel((ItemBlockMineBase)IIContent.blockTripmine.itemBlock);
	}

	@Override
	protected void nullifyModels()
	{

	}

	public static class TripmineItemStackRenderer extends TileEntityItemStackRenderer
	{
		@Override
		public void renderByItem(ItemStack stack, float partialTicks)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0.35, 0);
			GlStateManager.scale(1.75, 1.75, 1.75);

			ItemBlockMineBase ammo = (ItemBlockMineBase)IIContent.blockTripmine.itemBlock;
			assert ammo!=null;
			if(ammo.isBulletCore(stack))
				AmmoRegistry.getModel(ammo).renderCore(stack);
			else
				AmmoRegistry.getModel(ammo).renderAmmoComplete(false, stack);

			GlStateManager.popMatrix();
		}
	}
}