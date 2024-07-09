package pl.pabilo8.immersiveintelligence.client.render.ammunition;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.ammunition.RadioExplosivesRenderer.RadioExplosivesItemStackRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine.ItemBlockMineBase;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityRadioExplosives;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoMine;

/**
 * Handles rendering of a radio explosives entity
 *
 * @author Pabilo8
 * @updated 05.03.2024
 * @ii-approved 0.3.1
 * @since 02.02.2021
 */
@RegisteredTileRenderer(name = "radio_explosives", clazz = TileEntityRadioExplosives.class, teisrClazz = RadioExplosivesItemStackRenderer.class)
public class RadioExplosivesRenderer extends IITileRenderer<TileEntityRadioExplosives>
{
	private IAmmoModel<ItemBlockMineBase, EntityAmmoMine> model;

	@Override
	public void draw(TileEntityRadioExplosives te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		applyStandardRotation(te.facing);
		float debugProgress = IIAnimationUtils.getDebugProgress(te.getWorld(), 30, partialTicks);
		GlStateManager.translate(0.5, 0, 0.5);
//		GlStateManager.rotate(debugProgress*360f, 0, 1, 0);

		model.renderAmmoComplete(false, te.getMineStack());
	}

	@Override
	protected void applyStandardRotation(EnumFacing facing)
	{
//		GlStateManager.translate(-0.5, 0, -0.5);
		GlStateManager.translate(0.5, 0, 0.5);
		GlStateManager.rotate(90-facing.getHorizontalAngle(), 0, 1, 0);
		if(facing==EnumFacing.UP)
		{
			GlStateManager.rotate(-90, 0, 0, 1);
			GlStateManager.translate(-0.5, -0.5, 0);
		}
		else if(facing==EnumFacing.DOWN)
		{
			GlStateManager.rotate(90, 0, 0, 1);
			GlStateManager.translate(0.5, -0.5, 0);
		}
		GlStateManager.translate(-0.625, 0.125, -0.5);

//		GlStateManager.rotate(-90, 0, 1, 0);

		/*

		GlStateManager.translate(0.5, 0, -0.5);*/
	}

	@Override
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		model = AmmoRegistry.getModel((ItemBlockMineBase)IIContent.blockRadioExplosives.itemBlock);
	}

	@Override
	protected void nullifyModels()
	{

	}

	@Override
	protected Tuple<IBlockState, IBakedModel> getModelFromBlockState(TileEntityRadioExplosives te)
	{
		return ACCEPTABLE;
	}

	public static class RadioExplosivesItemStackRenderer extends TileEntityItemStackRenderer
	{
		@Override
		public void renderByItem(ItemStack stack, float partialTicks)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.25, 0.25, 0);
			GlStateManager.scale(1.5, 1.5, 1.5f);

			ItemBlockMineBase ammo = (ItemBlockMineBase)IIContent.blockRadioExplosives.itemBlock;
			assert ammo!=null;
			if(ammo.isBulletCore(stack))
				AmmoRegistry.getModel(ammo).renderCore(stack);
			else
				AmmoRegistry.getModel(ammo).renderAmmoComplete(false, stack);
			GlStateManager.popMatrix();
		}
	}
}