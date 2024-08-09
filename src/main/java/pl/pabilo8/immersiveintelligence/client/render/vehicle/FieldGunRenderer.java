package pl.pabilo8.immersiveintelligence.client.render.vehicle;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.render.IIEntityRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldGun;

/**
 * @author Pabilo8
 * @since 14.12.2022
 */
public class FieldGunRenderer extends IIEntityRenderer<EntityFieldGun>
{
	private static AMT[] models;

	public FieldGunRenderer(RenderManager render)
	{
		super(render, "field_gun");
	}

	@Override
	public void draw(EntityFieldGun entity, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		for(AMT amt : models)
			amt.render(tes, buf);
		GlStateManager.enableCull();
	}

	@Override
	public void compileModels()
	{
		models = IIAnimationUtils.getAMTFromRes(
				new ResourceLocation(ImmersiveIntelligence.MODID, "models/entity/long_field_gun_grouped.obj"),
				new ResourceLocation(ImmersiveIntelligence.MODID, "models/entity/long_field_gun_grouped.obj.amt"),
				header -> new AMT[]{}
		);
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		/*ResLoc.MODEL_MTL.of(ENTITY_MODEL,"combat_drone");
		ResLoc.MODEL_OBJ.of(ENTITY_MODEL,"combat_drone");
		ResLoc.MODEL_OBJIE.of(ENTITY_MODEL,"combat_drone");
		ResLoc.ANIMATION.of("drone/propellers");*/

		IIAnimationLoader.preloadTexturesFromMTL(new ResourceLocation(ImmersiveIntelligence.MODID, "models/entity/long_field_gun_grouped.mtl"), map);
	}

	@Override
	protected void nullifyModels()
	{
		IIAnimationUtils.disposeOf(models);
	}
}
