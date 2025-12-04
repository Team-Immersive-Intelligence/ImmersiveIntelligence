package pl.pabilo8.immersiveintelligence.client.render.vehicle;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIEntityRenderer.RegisteredEntityRenderer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityMotorbike;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;

@RegisteredEntityRenderer(clazz = EntityFieldHowitzer.class, name = "vehicle/motorbike")
public class MotorbikeRenderer extends IIVehicleRenderer<EntityMotorbike>
{
	public MotorbikeRenderer(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void draw(EntityMotorbike entity, BufferBuilder buf, float partialTicks, Tessellator tes)
	{

	}

	@Override
	public void compileModels()
	{

	}

	@Override
	protected void nullifyModels()
	{

	}

	@Override
	public boolean handleBipedRotations(ModelBiped model, EntityMotorbike entity, EntityLivingBase passenger, float partialTicks)
	{
		Entity riding = passenger.getRidingEntity();
		if(!(riding instanceof EntityVehicleSeat))
			return false;
		EntityVehicleSeat seat = (EntityVehicleSeat)riding;

		if(seat.seatID.equals("rider"))
		{
			model.bipedBody.rotateAngleX += 0.25f;
			model.bipedRightLeg.rotationPointZ = 2;
			model.bipedLeftLeg.rotationPointZ = 2;

			model.bipedRightArm.rotateAngleZ = 0;
			model.bipedLeftArm.rotateAngleZ = 0;

			model.bipedRightArm.rotationPointZ = -1.5f;
			model.bipedLeftArm.rotationPointZ = -1.5f;

			model.bipedRightArm.rotateAngleX = -1.35f;
			model.bipedRightArm.rotateAngleY = 0.5f;
			model.bipedLeftArm.rotateAngleX = -1.35f;
			model.bipedLeftArm.rotateAngleY = -0.5f;
		}

		model.bipedRightLeg.rotateAngleY = 0.65f;
		model.bipedLeftLeg.rotateAngleY = -0.65f;
		model.bipedRightLeg.rotateAngleX = -0.65f;
		model.bipedLeftLeg.rotateAngleX = -0.65f;
		return true;
	}
}
