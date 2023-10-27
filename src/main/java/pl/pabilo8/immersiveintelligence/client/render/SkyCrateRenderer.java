package pl.pabilo8.immersiveintelligence.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkycrateMount;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden.ModelSkyCrate;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden.ModelSkyCrateElectric;
import pl.pabilo8.immersiveintelligence.client.util.tmt.TmtUtil;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;
import pl.pabilo8.immersiveintelligence.common.item.ItemIISkycrateMount;

/**
 * @author Pabilo8
 * @since 08-06-2019
 */
public class SkyCrateRenderer extends Render<EntitySkyCrate>
{
	public static String texture_mechanical = ImmersiveIntelligence.MODID+":textures/entity/skycrate.png";
	public static String texture_electric = ImmersiveIntelligence.MODID+":textures/entity/skycrate_electric.png";

	public static ModelSkyCrate model_mechanical = new ModelSkyCrate();
	public static ModelSkyCrateElectric model_electric = new ModelSkyCrateElectric();

	public SkyCrateRenderer(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
		this.shadowSize = 0.5F;
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	public void doRender(EntitySkyCrate entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();


		GlStateManager.translate(x, y, z);

		if(entity.mount.getItem() instanceof ItemIISkycrateMount)
		{
			GlStateManager.scale(0.85, 0.85, 0.85);
			GlStateManager.translate(0, -1.125, 0);
			GlStateManager.rotate(180.0F-TmtUtil.TMTToAngle(entity.rotationYaw), 0.0F, 1.0F, 0.0F);
			ISkycrateMount mount = (ISkycrateMount)entity.mount.getItem();
			mount.render(entity.mount, entity.world, partialTicks, entity.energy);
			GlStateManager.translate(0, 0.5, 0);
			ClientUtils.mc().getRenderItem().renderItem(entity.crate, TransformType.NONE);
		}


		GlStateManager.popMatrix();

		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	public boolean isMultipass()
	{
		return false;
	}


	@Override
	protected ResourceLocation getEntityTexture(EntitySkyCrate entity)
	{
		return new ResourceLocation(texture_mechanical);
	}
}
