package pl.pabilo8.immersiveintelligence.client.render.vehicle;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTLoader;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTCachedModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIEntityRenderer.RegisteredEntityRenderer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

@RegisteredEntityRenderer(clazz = EntityFieldHowitzer.class, name = "vehicle/field_howitzer")
public class FieldHowitzerRenderer extends IIVehicleRenderer<EntityFieldHowitzer>
{
	private static AMTCachedModel<EntityFieldHowitzer> model;

	public FieldHowitzerRenderer(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void draw(EntityFieldHowitzer entity, BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}

	@Override
	public void compileModels()
	{

	}

	@Override
	public void registerSprites(TextureMap map)
	{
		AMTLoader.preloadTexturesFromMTL(ResLoc.of(IIReference.RES_ENTITY_MODEL, "vehicle/field_howitzer/field_howitzer.obj").withExtension(ResLoc.EXT_MTL), map);
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(model);
	}

	@Override
	public boolean handleBipedRotations(ModelBiped model, EntityFieldHowitzer entity, EntityLivingBase passenger, float partialTicks)
	{
		return false;
	}
}
