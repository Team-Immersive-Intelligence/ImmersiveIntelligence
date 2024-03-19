package pl.pabilo8.immersiveintelligence.client.render.ammunition;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.ammo.IIAmmoRegistry;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.naval_mine.EntityNavalMine;

import javax.annotation.Nonnull;

/**
 * Handles rendering of a naval mine entity
 *
 * @author Pabilo8
 * @updated 5.02.2024
 * @ii-approved 0.3.1
 * @since 21.01.2021
 */
public class NavalMineRenderer extends Render<EntityNavalMine>
{
	public NavalMineRenderer(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void doRender(EntityNavalMine entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();

		if(entity.isRiding())
		{
			/*double mountedYOffset = Math.abs(entity.posY-entity.getRidingEntity().posY)*16f;
			 */
			GlStateManager.pushMatrix();

			/*GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);
			GlStateManager.translate(0, -18, 0);*/

			//TODO: 05.03.2024 wire rendering

			IIAmmoRegistry.getModel(IIContent.itemNavalMine)
					.renderAmmoComplete(entity, partialTicks);

			GlStateManager.popMatrix();
		}


		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityNavalMine entity)
	{
		return null;
	}

	/**
	 * Renders a naval mine ItemStack
	 */
	public static class NavalMineItemstackRenderer extends TileEntityItemStackRenderer
	{
		@Override
		public void renderByItem(@Nonnull ItemStack stack, float partialTicks)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.5f, 0f, 0.5f);
			switch(IIContent.itemNavalMine.stackToSub(stack))
			{
				case BULLET:
					IIAmmoRegistry.getModel(IIContent.itemNavalMine)
							.renderAmmoComplete(true, stack);
					break;
				case CORE:
					IIAmmoRegistry.getModel(IIContent.itemNavalMine)
							.renderCore(stack);
					break;
			}
			GlStateManager.popMatrix();
		}
	}
}
