package pl.pabilo8.immersiveintelligence.client.model;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;

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
	private final String texture;
	private ItemStack renderStack = ItemStack.EMPTY;
	private EntityEquipmentSlot renderSlot = EntityEquipmentSlot.HEAD;

	public TMTArmorModel(int textureWidthIn, int textureHeightIn, String texture)
	{
		//yOffset
		super(1, 0, textureWidthIn, textureHeightIn);
		this.texture = texture;
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
		GlStateManager.pushMatrix();
		actualRender(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.popMatrix();

	}

	public void renderAsPart()
	{
		ClientUtils.bindTexture(this.texture);
		if(renderSlot==EntityEquipmentSlot.HEAD)
			for(ModelRendererTurbo mod : headModel)
				mod.render(0.0625f);
		else if(renderSlot==EntityEquipmentSlot.CHEST)
		{
			GlStateManager.translate(0, 0.5f, 0);
			GlStateManager.scale(0.85f, 0.85f, 0.85f);
			for(ModelRendererTurbo mod : bodyModel)
				mod.render(0.0625f);
			GlStateManager.translate(0.25f, -0.125f, 0);
			for(ModelRendererTurbo mod : leftArmModel)
				mod.render(0.0625f);
			GlStateManager.translate(-0.5f, 0f, 0);
			for(ModelRendererTurbo mod : rightArmModel)
				mod.render(0.0625f);
		}
		else if(renderSlot==EntityEquipmentSlot.LEGS)
		{
			GlStateManager.translate(0.25f-0.0625, 0.75, 0);
			for(ModelRendererTurbo mod : leftLegModel)
				mod.render(0.0625f);
			GlStateManager.translate(-0.25f, 0, 0);
			for(ModelRendererTurbo mod : rightLegModel)
				mod.render(0.0625f);
		}
		else if(renderSlot==EntityEquipmentSlot.FEET)
		{
			GlStateManager.translate(0.25f-0.0625, 0.75, 0);
			for(ModelRendererTurbo mod : leftFootModel)
				mod.render(0.0625f);
			GlStateManager.translate(-0.25f, 0, 0);
			GlStateManager.rotate(-35, 0, 1, 0);
			for(ModelRendererTurbo mod : rightFootModel)
				mod.render(0.0625f);
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
			renderChild(this.bipedHead, headModel, scale);
		else if(renderSlot==EntityEquipmentSlot.CHEST)
		{
			renderChild(this.bipedBody, bodyModel, scale);
			renderChild(this.bipedLeftArm, leftArmModel, scale);
			renderChild(this.bipedRightArm, rightArmModel, scale);
			renderAddons(renderStack, renderSlot, scale, ageInTicks);
		}
		else if(renderSlot==EntityEquipmentSlot.LEGS)
		{
			renderChild(this.bipedLeftLeg, leftLegModel, scale);
			renderChild(this.bipedRightLeg, rightLegModel, scale);
		}
		else if(renderSlot==EntityEquipmentSlot.FEET)
		{
			renderChild(this.bipedLeftLeg, leftFootModel, scale);
			renderChild(this.bipedRightLeg, rightFootModel, scale);
		}

		GlStateManager.popMatrix();
	}

	public void renderAddons(ItemStack renderStack, EntityEquipmentSlot renderSlot, float scale, float ageInTicks)
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

	public void renderChild(ModelRenderer biped, ModelRendererTurbo[] models, float scale)
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

			ClientUtils.bindTexture(this.texture);
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
}
