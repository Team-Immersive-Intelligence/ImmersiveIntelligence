package pl.pabilo8.immersiveintelligence.client.render.ammunition;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine.ItemBlockMineBase;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityTellermine;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoMine;

/**
 * Handles rendering of a landmine entity
 *
 * @author Pabilo8
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
		model.renderAmmoComplete(false, te.getMineStack());
	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		model = AmmoRegistry.getModel((ItemBlockMineBase)IIContent.blockTellermine.itemBlock);
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