package pl.pabilo8.immersiveintelligence.client.render.entity.vehicle;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIEntityRenderer.RegisteredEntityRenderer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldGun;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

/**
 * @author Pabilo8
 * @since 14.12.2022
 */
@RegisteredEntityRenderer(clazz = EntityFieldGun.class, name = "vehicle/field_gun")
public class FieldGunRenderer extends IIVehicleRenderer<EntityFieldGun>
{
	private static AMTModel model;

	public FieldGunRenderer(RenderManager render)
	{
		super(render);
	}

	@Override
	public void draw(EntityFieldGun entity, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		model.render(tes, buf);
	}

	@Override
	public void compileModels()
	{
		model = new AMTModel(DefaultVertexFormats.BLOCK, IIReference.RES_ENTITY_MODEL.with("long_field_gun_grouped.obj"));
	}

	@Override
	public void registerSprites(TextureMap map)
	{
		AMTLoader.preloadTexturesFromMTL(ResLoc.of(IIReference.RES_ENTITY_MODEL, "long_field_gun_grouped").withExtension(ResLoc.EXT_MTL), map);
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(model);
	}

	@Override
	public boolean handleBipedRotations(ModelBiped model, EntityFieldGun entity, EntityLivingBase passenger, float partialTicks)
	{
		return false;
	}
}
