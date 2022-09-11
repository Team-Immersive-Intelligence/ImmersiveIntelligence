package pl.pabilo8.immersiveintelligence.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelRadioExplosives;
import pl.pabilo8.immersiveintelligence.common.block.mines.tileentity.TileEntityRadioExplosives;

/**
 * @author Pabilo8
 * @since 02.02.2021
 */
public class RadioExplosivesRenderer extends TileEntitySpecialRenderer<TileEntityRadioExplosives> implements IReloadableModelContainer<RadioExplosivesRenderer>
{
	private static ModelRadioExplosives model;

	@Override
	public void render(TileEntityRadioExplosives te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x+0.5, y+0.5, z+0.5);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		GlStateManager.rotate(-90,0,0,1);
		switch(te.facing)
		{
			case UP:
			{
				GlStateManager.rotate(180, 0, 0, 1);
			}
			break;
			case DOWN:
			{
				//model.rotateAll(0f, 0f, 0f);
			}
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

		GlStateManager.translate(0.125,-0.25,0);

		model.renderCasing(0f, 0xffffff);
		model.renderCore(te.coreColor, EnumCoreTypes.CANISTER);

		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelRadioExplosives();
		AmmoRegistry.INSTANCE.registeredModels.remove("radio_explosives");
		AmmoRegistry.INSTANCE.registeredModels.put("radio_explosives", model);
	}

	public static class RadioExplosivesItemStackRenderer extends TileEntityItemStackRenderer
	{
		@Override
		public void renderByItem(ItemStack itemStackIn, float partialTicks)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5, 0.25, 0.5);
			GlStateManager.scale(1.5, 1.5, 1.5f);

			assert itemStackIn.getItem() instanceof IAmmo;
			IAmmo bullet = (IAmmo)itemStackIn.getItem();

			model.renderCasing(0, bullet.getPaintColor(itemStackIn));
			//model.renderCore(bullet.getCore(itemStackIn).getColour(), EnumCoreTypes.CANISTER);

			GlStateManager.popMatrix();
		}
	}
}