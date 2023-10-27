package pl.pabilo8.immersiveintelligence.client.model;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * @author Pabilo8
 * @since 13.09.2020
 */
public class TMTArmorModel extends ModelBiped
{
	public ModelRendererTurbo[] headModel, bodyModel, leftArmModel, rightArmModel, leftLegModel, rightLegModel, leftFootModel, rightFootModel;
	//List of parts for group flipping / translation / rotation
	public HashMap<String, ModelRendererTurbo[]> parts = new HashMap<>();
	private String texture;
	private ItemStack renderStack = ItemStack.EMPTY;
	protected EntityEquipmentSlot renderSlot = EntityEquipmentSlot.HEAD;

	public TMTArmorModel(int textureWidthIn, int textureHeightIn, String texture)
	{
		//yOffset
		super(1, 0, textureWidthIn, textureHeightIn);
		this.texture = texture;
	}

	protected void setTexture(String tex)
	{
		texture = tex;
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if(entity instanceof EntityLivingBase)
		{
			isChild = ((EntityLivingBase)entity).isChild();
			isSneak = entity.isSneaking();
			isRiding = entity.isRiding();
			this.setLivingAnimations((EntityLivingBase)entity, limbSwing, limbSwingAmount, ClientUtils.timer().renderPartialTicks);
		}
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		GlStateManager.pushMatrix();
		actualRender(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.popMatrix();

	}

	public void renderAsPart()
	{
		ClientUtils.bindTexture(this.texture);
		if(renderSlot==EntityEquipmentSlot.HEAD)
		{
			for(ModelRendererTurbo mod : headModel)
				mod.render(0.0625f);
			renderAddons(renderStack, renderSlot, null, 0, false, 0.0625f);
		}
		else if(renderSlot==EntityEquipmentSlot.CHEST)
		{
			GlStateManager.translate(0, 0.5f, 0);
			GlStateManager.scale(0.85f, 0.85f, 0.85f);
			GlStateManager.pushMatrix();
			for(ModelRendererTurbo mod : bodyModel)
				mod.render(0.0625f);
			renderAddons(renderStack, renderSlot, bodyModel, 0, false, 0.0625f);

			GlStateManager.translate(0.25f, -0.125f, 0);
			ClientUtils.bindTexture(this.texture);
			for(ModelRendererTurbo mod : leftArmModel)
				mod.render(0.0625f);
			renderAddons(renderStack, renderSlot, leftArmModel, 0, false, 0.0625f);

			GlStateManager.translate(-0.5f, 0f, 0);
			ClientUtils.bindTexture(this.texture);
			for(ModelRendererTurbo mod : rightArmModel)
				mod.render(0.0625f);
			renderAddons(renderStack, renderSlot, rightArmModel, 0, false, 0.0625f);

			GlStateManager.popMatrix();

		}
		else if(renderSlot==EntityEquipmentSlot.LEGS)
		{
			GlStateManager.translate(0.25f-0.0625, 0.75, 0);
			GlStateManager.pushMatrix();
			for(ModelRendererTurbo mod : leftLegModel)
				mod.render(0.0625f);
			renderAddons(renderStack, renderSlot, leftLegModel, 0, false, 0.0625f);
			GlStateManager.translate(-0.25f, 0, 0);
			ClientUtils.bindTexture(this.texture);
			for(ModelRendererTurbo mod : rightLegModel)
				mod.render(0.0625f);
			renderAddons(renderStack, renderSlot, rightLegModel, 0, false, 0.0625f);
			GlStateManager.popMatrix();



		}
		else if(renderSlot==EntityEquipmentSlot.FEET)
		{
			GlStateManager.translate(0.25f-0.0625, 0.75, 0);
			GlStateManager.pushMatrix();
			for(ModelRendererTurbo mod : leftFootModel)
				mod.render(0.0625f);
			renderAddons(renderStack, renderSlot, leftFootModel, 0, false, 0.0625f);
			GlStateManager.translate(-0.25f, 0, 0);
			GlStateManager.rotate(-35, 0, 1, 0);
			ClientUtils.bindTexture(this.texture);
			for(ModelRendererTurbo mod : rightFootModel)
				mod.render(0.0625f);
			renderAddons(renderStack, renderSlot, rightFootModel, 0, false, 0.0625f);
			GlStateManager.popMatrix();
		}
	}

	protected void actualRender(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		GlStateManager.pushMatrix();

		if(entity.isSneaking())
		{
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}
		if(renderSlot==EntityEquipmentSlot.HEAD)
		{
			renderChild(this.bipedHead, headModel, scale, texture);
		}
		else if(renderSlot==EntityEquipmentSlot.CHEST)
		{
			renderChild(this.bipedBody, bodyModel, scale, texture);
			renderChild(this.bipedLeftArm, leftArmModel, scale, texture);
			renderChild(this.bipedRightArm, rightArmModel, scale, texture);
		}
		else if(renderSlot==EntityEquipmentSlot.LEGS)
		{
			renderChild(this.bipedLeftLeg, leftLegModel, scale, texture);
			renderChild(this.bipedRightLeg, rightLegModel, scale, texture);
		}
		else if(renderSlot==EntityEquipmentSlot.FEET)
		{
			renderChild(this.bipedLeftLeg, leftFootModel, scale, texture);
			renderChild(this.bipedRightLeg, rightFootModel, scale, texture);
		}
		renderAddons(renderStack, renderSlot, null, ageInTicks, true, scale);

		GlStateManager.popMatrix();
	}

	public void renderAddons(ItemStack renderStack, EntityEquipmentSlot renderSlot, @Nullable ModelRendererTurbo[] part, float ageInTicks, boolean entity, float scale)
	{

	}

	public void init()
	{

	}

	public void flipAll()
	{
		parts.put("head", headModel);
		parts.put("body", bodyModel);
		parts.put("leftLeg", leftLegModel);
		parts.put("rightLeg", rightLegModel);
		parts.put("leftArm", leftArmModel);
		parts.put("rightArm", rightArmModel);
		parts.put("rightFoot", rightFootModel);
		parts.put("leftFoot", leftFootModel);

		for(ModelRendererTurbo[] mod : parts.values())
			flip(mod);
	}

	public void flip(ModelRendererTurbo[] model)
	{
		for(ModelRendererTurbo part : model)
		{
			part.doMirror(false, true, true);
			part.setRotationPoint(part.rotationPointX, -part.rotationPointY, -part.rotationPointZ);
		}
	}

	public void renderChild(ModelRenderer biped, ModelRendererTurbo[] models, float scale, String texture)
	{
		if(!biped.isHidden&&biped.showModel)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(biped.rotationPointX*scale, biped.rotationPointY*scale, biped.rotationPointZ*scale);

			if(biped.rotateAngleY!=0.0F)
			{
				GlStateManager.rotate(biped.rotateAngleY*(180F/(float)Math.PI), 0.0F, 1.0F, 0.0F);
			}

			if(biped.rotateAngleX!=0.0F)
			{
				GlStateManager.rotate(biped.rotateAngleX*(180F/(float)Math.PI), 1.0F, 0.0F, 0.0F);
			}

			if(biped.rotateAngleZ!=0.0F)
			{
				GlStateManager.rotate(biped.rotateAngleZ*(180F/(float)Math.PI), 0.0F, 0.0F, 1.0F);
			}

			GlStateManager.rotate(180, 0, 0, 1);
			GlStateManager.rotate(180, 0, 1, 0);
			GlStateManager.scale(1.06, 1.06, 1.06);

			ClientUtils.bindTexture(texture);
			for(ModelRendererTurbo model : models)
				model.render(scale, false);

			GlStateManager.popMatrix();
		}
	}

	protected TMTArmorModel prepareForRender(EntityEquipmentSlot part, ItemStack stack)
	{
		this.renderStack = stack;
		this.renderSlot = part;
		return this;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity)
	{
		if(entity instanceof EntityLivingBase)
			swingProgress = ((EntityLivingBase)entity).getSwingProgress(ClientUtils.timer().renderPartialTicks);
		if(entity instanceof EntityArmorStand)
			setRotationAnglesStand(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
		else if(entity instanceof EntitySkeleton||entity instanceof EntityZombie)
			setRotationAnglesZombie(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
		else
			super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
	}

	public void setRotationAnglesZombie(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		float f6 = MathHelper.sin(this.swingProgress*3.141593F);
		float f7 = MathHelper.sin((1.0F-(1.0F-this.swingProgress)*(1.0F-this.swingProgress))*3.141593F);
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightArm.rotateAngleY = (-(0.1F-f6*0.6F));
		this.bipedLeftArm.rotateAngleY = (0.1F-f6*0.6F);
		this.bipedRightArm.rotateAngleX = -1.570796F;
		this.bipedLeftArm.rotateAngleX = -1.570796F;
		this.bipedRightArm.rotateAngleX -= f6*1.2F-f7*0.4F;
		this.bipedLeftArm.rotateAngleX -= f6*1.2F-f7*0.4F;
		this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks*0.09F)*0.05F+0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks*0.09F)*0.05F+0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks*0.067F)*0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks*0.067F)*0.05F;
	}

	public void setRotationAnglesStand(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity)
	{
		if((entity instanceof EntityArmorStand))
		{
			EntityArmorStand entityarmorstand = (EntityArmorStand)entity;
			this.bipedHead.rotateAngleX = (0.01745329F*entityarmorstand.getHeadRotation().getX());
			this.bipedHead.rotateAngleY = (0.01745329F*entityarmorstand.getHeadRotation().getY());
			this.bipedHead.rotateAngleZ = (0.01745329F*entityarmorstand.getHeadRotation().getZ());
			this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
			this.bipedBody.rotateAngleX = (0.01745329F*entityarmorstand.getBodyRotation().getX());
			this.bipedBody.rotateAngleY = (0.01745329F*entityarmorstand.getBodyRotation().getY());
			this.bipedBody.rotateAngleZ = (0.01745329F*entityarmorstand.getBodyRotation().getZ());
			this.bipedLeftArm.rotateAngleX = (0.01745329F*entityarmorstand.getLeftArmRotation().getX());
			this.bipedLeftArm.rotateAngleY = (0.01745329F*entityarmorstand.getLeftArmRotation().getY());
			this.bipedLeftArm.rotateAngleZ = (0.01745329F*entityarmorstand.getLeftArmRotation().getZ());
			this.bipedRightArm.rotateAngleX = (0.01745329F*entityarmorstand.getRightArmRotation().getX());
			this.bipedRightArm.rotateAngleY = (0.01745329F*entityarmorstand.getRightArmRotation().getY());
			this.bipedRightArm.rotateAngleZ = (0.01745329F*entityarmorstand.getRightArmRotation().getZ());
			this.bipedLeftLeg.rotateAngleX = (0.01745329F*entityarmorstand.getLeftLegRotation().getX());
			this.bipedLeftLeg.rotateAngleY = (0.01745329F*entityarmorstand.getLeftLegRotation().getY());
			this.bipedLeftLeg.rotateAngleZ = (0.01745329F*entityarmorstand.getLeftLegRotation().getZ());
			this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
			this.bipedRightLeg.rotateAngleX = (0.01745329F*entityarmorstand.getRightLegRotation().getX());
			this.bipedRightLeg.rotateAngleY = (0.01745329F*entityarmorstand.getRightLegRotation().getY());
			this.bipedRightLeg.rotateAngleZ = (0.01745329F*entityarmorstand.getRightLegRotation().getZ());
			this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
			copyModelAngles(this.bipedHead, this.bipedHeadwear);
		}
	}
}
