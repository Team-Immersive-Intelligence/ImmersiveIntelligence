package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelNavalMine;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityNavalMine;

/**
 * @author Pabilo8
 * @since 21.01.2021
 */
public class NavalMineRenderer extends Render<EntityNavalMine> implements IReloadableModelContainer<NavalMineRenderer>
{
	public static NavalMineItemstackRenderer instance = new NavalMineItemstackRenderer();
	public static ModelNavalMine model = new ModelNavalMine();
	public static final String TEXTURE = ImmersiveIntelligence.MODID+":textures/entity/bullets/naval_mine.png";

	public NavalMineRenderer(RenderManager renderManager)
	{
		super(renderManager);
		subscribeToList("naval_mine");
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

		ClientUtils.bindTexture(TEXTURE);

		if(entity.isRiding())
		{
			double mountedYOffset = Math.abs(entity.posY-entity.getRidingEntity().posY)*16f;

			GlStateManager.pushMatrix();

			GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);
			GlStateManager.translate(0, -18, 0);

			for(int j = 0; j < 4; j++)
			{
				for(int i = 0; i < mountedYOffset; i += 15)
				{
					float l = (float)MathHelper.clamp(mountedYOffset-i, 0, 15);
					ClientUtils.drawTexturedRect(-1.5f, -i, 3, l, 0, 0.046875, 0, l*0.015625);
				}
				GlStateManager.rotate(90, 0, 1, 0);
			}


			GlStateManager.popMatrix();
		}

		GlStateManager.disableLighting();
		RenderHelper.enableStandardItemLighting();
		for(ModelRendererTurbo mod : model.baseModel)
			mod.render();
		for(ModelRendererTurbo mod : model.topModel)
			mod.render();

		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	@Override
	public void reloadModels()
	{
		model = new ModelNavalMine();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityNavalMine entity)
	{
		return null;
	}

	public static class NavalMineItemstackRenderer extends TileEntityItemStackRenderer
	{
		@Override
		public void renderByItem(ItemStack stack, float partialTicks)
		{
			GlStateManager.pushMatrix();

			GlStateManager.translate(0.5f, 0f, 0.5f);
			IAmmo b = (IAmmo)stack.getItem();
			if(stack.getMetadata()==0)
				model.renderBulletUsed(b.getCore(stack).getColour(), b.getCoreType(stack), b.getPaintColor(stack));
			else
				model.renderCore(b.getCore(stack).getColour(), b.getCoreType(stack));

			GlStateManager.popMatrix();
		}
	}
}
