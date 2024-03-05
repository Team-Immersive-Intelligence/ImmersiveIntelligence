package pl.pabilo8.immersiveintelligence.client.render.ammunition;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.ammo.IIAmmoRegistry;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.ammunition.RadioExplosivesRenderer.RadioExplosivesItemStackRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine.ItemBlockMineBase;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityRadioExplosives;

/**
 * Handles rendering of a radio explosives entity
 *
 * @author Pabilo8
 * @updated 05.03.2024
 * @ii-approved 0.3.1
 * @since 02.02.2021
 */
@RegisteredTileRenderer(name = "radio_explosives", clazz = TileEntityRadioExplosives.class, teisrClazz = RadioExplosivesItemStackRenderer.class)
public class RadioExplosivesRenderer extends TileEntitySpecialRenderer<TileEntityRadioExplosives>
{
	@Override
	public void render(TileEntityRadioExplosives te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x+0.5, y+0.5, z+0.5);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		GlStateManager.rotate(-90, 0, 0, 1);
		switch(te.facing)
		{
			case UP:
			{
				GlStateManager.rotate(180, 0, 0, 1);
			}
			break;
			case DOWN:
				break;
			case NORTH:
			{
				GlStateManager.rotate(-90, 1, 0, 0);
				GlStateManager.rotate(90, 0, 0, 1);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(90, 1, 0, 0);
				GlStateManager.rotate(90, 0, 0, 1);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(90, 0, 0, 1);
			}
			break;
			case WEST:
			{
				GlStateManager.rotate(180, 0, 1, 0);
				GlStateManager.rotate(-90, 0, 0, 1);
			}
			break;
		}

		GlStateManager.translate(0.125, -0.25, 0);

		/*model.renderCasing(0f, 0xffffff);
		model.renderCore(te.coreColor, EnumCoreTypes.CANISTER);*/

		GlStateManager.popMatrix();
	}

	public static class RadioExplosivesItemStackRenderer extends TileEntityItemStackRenderer
	{
		@Override
		public void renderByItem(ItemStack stack, float partialTicks)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5, 0.25, 0.5);
			GlStateManager.scale(1.5, 1.5, 1.5f);

			ItemBlockMineBase ammo = (ItemBlockMineBase)IIContent.blockRadioExplosives.itemBlock;
			assert ammo!=null;
			if(ammo.isBulletCore(stack))
				IIAmmoRegistry.getModel(ammo).renderCore(stack);
			else
				IIAmmoRegistry.getModel(ammo).renderAmmoComplete(false, stack);

			//model.renderCore(bullet.getCore(stack).getColour(), EnumCoreTypes.CANISTER);
			GlStateManager.popMatrix();
		}
	}
}