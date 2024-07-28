package pl.pabilo8.immersiveintelligence.client.render.ammunition;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
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
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		if(Minecraft.isAmbientOcclusionEnabled())
			GlStateManager.shadeModel(7425);
		else
			GlStateManager.shadeModel(7424);

		ClientUtils.bindAtlas();
		if(entity.isRiding())
		{
			GlStateManager.pushMatrix();

			GlStateManager.popMatrix();
		}
		AmmoRegistry.getModel(IIContent.itemNavalMine)
				.renderAmmoComplete(entity, partialTicks);

		GlStateManager.cullFace(CullFace.BACK);
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
			GlStateManager.translate(0.5f, -0.25f, 0.5f);
			switch(IIContent.itemNavalMine.stackToSub(stack))
			{
				case BULLET:
					AmmoRegistry.getModel(IIContent.itemNavalMine)
							.renderAmmoComplete(false, stack);
					break;
				case CORE:
					AmmoRegistry.getModel(IIContent.itemNavalMine)
							.renderCore(stack);
					break;
			}
			GlStateManager.popMatrix();
		}
	}
}
