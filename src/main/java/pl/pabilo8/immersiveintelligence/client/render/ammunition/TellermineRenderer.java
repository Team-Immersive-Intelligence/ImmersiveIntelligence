package pl.pabilo8.immersiveintelligence.client.render.ammunition;

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
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine.ItemBlockMineBase;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityTellermine;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoMine;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import blusunrize.immersiveengineering.client.ClientUtils;

/**
 * Handles rendering of a landmine entity
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 05.03.2024
 * @ii-approved 0.3.1
 * @since 02.02.2021
 */
@RegisteredTileRenderer(name = "tellermine", clazz = TileEntityTellermine.class, teisrClazz = TellermineRenderer.TellermineItemStackRenderer.class)
public class TellermineRenderer extends IITileRenderer<TileEntityTellermine>
{
	private IAmmoModel<ItemBlockMineBase, EntityAmmoMine> model;

	@Override
	public void draw(TileEntityTellermine te, BufferBuilder buf, float partialTicks, Tessellator tes)
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

			AMTUtils.getBRD().getBlockModelRenderer().renderModelBrightnessColor(
					ClientUtils.mc().getBlockRendererDispatcher().getModelForState(Blocks.TALLGRASS.getStateFromMeta(1)), 1f,
					colors[0], colors[1], colors[2]);
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}

		GlStateManager.translate(0, 0.03125f*(16-te.digLevel)-0.5, 0);
		model.renderAmmoComplete(false, te.getMineStack());
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = AmmoRegistry.getModel((ItemBlockMineBase)IIContent.blockTellermine.itemBlock);
	}

	@Override
	protected void nullifyModels()
	{

	}

	@Override
	protected Tuple<IBlockState, IBakedModel> getModelFromBlockState(TileEntityTellermine te)
	{
		return ACCEPTABLE;
	}

	public static class TellermineItemStackRenderer extends TileEntityItemStackRenderer
	{
		@Override
		public void renderByItem(ItemStack stack, float partialTicks)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5, 0.25, 0.5);
			GlStateManager.scale(1.5, 1.5, 1.5f);

			ItemBlockMineBase ammo = (ItemBlockMineBase)IIContent.blockTellermine.itemBlock;
			assert ammo!=null;
			if(ammo.isBulletCore(stack))
				AmmoRegistry.getModel(ammo).renderCore(stack);
			else
				AmmoRegistry.getModel(ammo).renderAmmoComplete(false, stack);

			GlStateManager.popMatrix();
		}
	}
}
