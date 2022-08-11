package pl.pabilo8.immersiveintelligence.client.model.misc;

import net.minecraft.client.model.*;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.HansArmAnimation;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.HansLegAnimation;

/**
 * @author Pabilo8
 * @since 18.05.2021
 */
public class ModelHansBiped extends ModelPlayer
{
	public ModelRenderer bipedLeftHand, bipedRightHand, bipedLeftFoot, bipedRightFoot;
	public ModelRenderer bipedLeftHandWear, bipedRightHandWear, bipedLeftFootWear, bipedRightFootWear;
	private final ModelHansBiped bipedHelper;

	public ModelHansBiped(float expand)
	{
		this(expand, false);
	}

	public ModelHansBiped(float expand, boolean slimArms)
	{
		this(expand, slimArms,
				new BipedTextureUVs(40, 16, 40, 16,
						48, 48, 40, 32,
						0, 16, 0, 16,
						0, 48, 0, 32,
						16, 16
				), false
		);
	}

	private ModelHansBiped(float expand, boolean slimArms, BipedTextureUVs uvs, boolean slave)
	{
		super(expand, slimArms);

		this.bipedLeftHand = addLimb(this.bipedLeftArm, true, uvs.armLeftU, uvs.armLeftV, -1.0F, -2.0F, -2.0F, expand);
		this.bipedRightHand = addLimb(this.bipedRightArm, false, uvs.armRightU, uvs.armRightV, -3.0F, -2.0F, -2.0F, expand);
		this.bipedLeftHandWear = addLimb(this.bipedLeftArmwear, true, uvs.armWearLeftU, uvs.armWearLeftV, -1.0F, -2.0F, -2.0F, expand+0.25F);
		this.bipedRightHandWear = addLimb(this.bipedRightArmwear, false, uvs.armWearRightU, uvs.armWearRightV, -3.0F, -2.0F, -2.0F, expand+0.25F);

		this.bipedLeftFoot = addLimb(this.bipedLeftLeg, true, uvs.legLeftU, uvs.legLeftV, -2.0F, 0.0F, -2.0F, expand);
		this.bipedLeftFoot.rotationPointY += 2;
		this.bipedRightFoot = addLimb(this.bipedRightLeg, false, uvs.legRightU, uvs.legRightV, -2.0F, 0.0F, -2.0F, expand);
		this.bipedRightFoot.rotationPointY += 2;


		this.bipedLeftFootWear = addLimb(this.bipedLeftLegwear, true, uvs.legWearLeftU, uvs.legWearLeftV, -2.0F, 0.0F, -2.0F, expand+0.25F);
		this.bipedLeftFootWear.rotationPointY += 2;
		this.bipedRightFootWear = addLimb(this.bipedRightLegwear, false, uvs.legWearRightU, uvs.legWearRightV, -2.0F, 0.0F, -2.0F, expand+0.25F);
		this.bipedRightFootWear.rotationPointY += 2;

		this.bipedHelper = slave?null: new ModelHansBiped(expand, slimArms, uvs, true);
	}

	private ModelRenderer addLimb(ModelRenderer bipedLeftArm, boolean l, int u, int v, float x, float y, float z, float expand)
	{
		bipedLeftArm.cubeList.clear();
		bipedLeftArm.cubeList.add(new ModelBoxCustomizable(bipedLeftArm, u, v, x, y, z, 4, 5, 4, expand, 0, -4));

		ModelRenderer mod = new ModelRenderer(this);
		mod.rotationPointY = 4f;
		mod.cubeList.add(new ModelBoxCustomizable(mod, u, v+5, x, -1, z, 4, 7, 4, expand, -5, 0));

		bipedLeftArm.addChild(mod);
		return mod;
	}

	public static class BipedTextureUVs
	{
		int armLeftU, armLeftV;
		int armRightU, armRightV;

		int armWearLeftU, armWearLeftV;
		int armWearRightU, armWearRightV;

		int legLeftU, legLeftV;
		int legRightU, legRightV;

		int legWearLeftU, legWearLeftV;
		int legWearRightU, legWearRightV;

		int bodyU, bodyV;

		public BipedTextureUVs(int armLeftU, int armLeftV, int armRightU, int armRightV,
							   int armWearLeftU, int armWearLeftV, int armWearRightU, int armWearRightV,
							   int legLeftU, int legLeftV, int legRightU, int legRightV,
							   int legWearLeftU, int legWearLeftV, int legWearRightU, int legWearRightV,
							   int bodyU, int bodyV)
		{
			this.armLeftU = armLeftU;
			this.armLeftV = armLeftV;
			this.armRightU = armRightU;
			this.armRightV = armRightV;
			this.armWearLeftU = armWearLeftU;
			this.armWearLeftV = armWearLeftV;
			this.armWearRightU = armWearRightU;
			this.armWearRightV = armWearRightV;
			this.legLeftU = legLeftU;
			this.legLeftV = legLeftV;
			this.legRightU = legRightU;
			this.legRightV = legRightV;
			this.legWearLeftU = legWearLeftU;
			this.legWearLeftV = legWearLeftV;
			this.legWearRightU = legWearRightU;
			this.legWearRightV = legWearRightV;
			this.bodyU = bodyU;
			this.bodyV = bodyV;
		}
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		bipedRightFoot.rotationPointX = 0f;
		bipedLeftFoot.rotationPointX = 0f;
		bipedRightFoot.rotationPointY = 6f;
		bipedLeftFoot.rotationPointY = 6f;
		bipedRightFoot.rotationPointZ = 0f;
		bipedLeftFoot.rotationPointZ = 0f;

		bipedRightFoot.rotateAngleX = 0;
		bipedLeftFoot.rotateAngleX = 0;
		bipedRightFoot.rotateAngleY = 0;
		bipedLeftFoot.rotateAngleY = 0;
		bipedRightFoot.rotateAngleZ = 0;
		bipedLeftFoot.rotateAngleZ = 0;

		bipedRightHand.rotateAngleX = 0;
		bipedLeftHand.rotateAngleX = 0;
		bipedRightHand.rotateAngleY = 0;
		bipedLeftHand.rotateAngleY = 0;
		bipedRightHand.rotateAngleZ = 0;
		bipedLeftHand.rotateAngleZ = 0;

		bipedRightArm.rotationPointY = 2f;
		bipedLeftArm.rotationPointY = 2f;

		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		if(entityIn instanceof EntityHans)
		{
			EntityHans hans = (EntityHans)entityIn;

			if(hans.getLegAnimation()==hans.prevLegAnimation)
			{
				handleLegAnimation(hans, hans.getLegAnimation(), ageInTicks); //less overhead ^^

			}
			else
			{
				// TODO: 25.09.2021 optimize
				//**lots** of overhead
				copyModelAngles(this.bipedLeftLeg, bipedHelper.bipedLeftLeg);
				copyModelAngles(this.bipedRightLeg, bipedHelper.bipedRightLeg);
				copyModelAngles(this.bipedLeftArm, bipedHelper.bipedLeftArm);
				copyModelAngles(this.bipedRightArm, bipedHelper.bipedRightArm);
				copyModelAngles(this.bipedBody, bipedHelper.bipedBody);

				copyModelAngles(this.bipedLeftHand, bipedHelper.bipedLeftHand);
				copyModelAngles(this.bipedRightHand, bipedHelper.bipedRightHand);
				copyModelAngles(this.bipedLeftFoot, bipedHelper.bipedLeftFoot);
				copyModelAngles(this.bipedRightFoot, bipedHelper.bipedRightFoot);

				handleLegAnimation(hans, hans.prevLegAnimation, ageInTicks);
				handleArmAnimation(hans, hans.prevArmAnimation, ageInTicks);

				bipedHelper.handleLegAnimation(hans, hans.getLegAnimation(), ageInTicks);
				final float progress = 1f-MathHelper.clamp((hans.legAnimationTimer-(ageInTicks%1))/8f, 0, 1);

				lerpModelAngles(bipedLeftLeg, bipedHelper.bipedLeftLeg, progress);
				lerpModelAngles(bipedRightLeg, bipedHelper.bipedRightLeg, progress);
				lerpModelAngles(bipedLeftArm, bipedHelper.bipedLeftArm, progress);
				lerpModelAngles(bipedRightArm, bipedHelper.bipedRightArm, progress);
				lerpModelAngles(bipedBody, bipedHelper.bipedBody, progress);

				lerpModelAngles(bipedLeftFoot, bipedHelper.bipedLeftFoot, progress);
				lerpModelAngles(bipedRightFoot, bipedHelper.bipedRightFoot, progress);
				lerpModelAngles(bipedLeftHand, bipedHelper.bipedLeftHand, progress);
				lerpModelAngles(bipedRightHand, bipedHelper.bipedRightHand, progress);
			}

			if(hans.armAnimation==hans.prevArmAnimation)
				handleArmAnimation(hans, hans.armAnimation, ageInTicks);
			else
			{
				copyModelAngles(this.bipedLeftLeg, bipedHelper.bipedLeftLeg);
				copyModelAngles(this.bipedRightLeg, bipedHelper.bipedRightLeg);
				copyModelAngles(this.bipedLeftArm, bipedHelper.bipedLeftArm);
				copyModelAngles(this.bipedRightArm, bipedHelper.bipedRightArm);
				copyModelAngles(this.bipedLeftHand, bipedHelper.bipedLeftHand);
				copyModelAngles(this.bipedRightHand, bipedHelper.bipedRightHand);


				handleArmAnimation(hans, hans.prevArmAnimation, ageInTicks);
				bipedHelper.handleArmAnimation(hans, hans.armAnimation, ageInTicks);
				final float progress = 1f-MathHelper.clamp((hans.armAnimationTimer-(ageInTicks%1))/8f, 0, 1);

				lerpModelAngles(bipedLeftArm, bipedHelper.bipedLeftArm, progress);
				lerpModelAngles(bipedRightArm, bipedHelper.bipedRightArm, progress);
				lerpModelAngles(bipedLeftHand, bipedHelper.bipedLeftHand, progress);
				lerpModelAngles(bipedRightHand, bipedHelper.bipedRightHand, progress);
			}

		}

		copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
		copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
		copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
		copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
		copyModelAngles(this.bipedBody, this.bipedBodyWear);

		copyModelAngles(bipedLeftFoot, bipedLeftFootWear);
		copyModelAngles(bipedRightFoot, bipedRightFootWear);
		copyModelAngles(bipedLeftHand, bipedLeftHandWear);
		copyModelAngles(bipedRightHand, bipedRightHandWear);
	}

	private void lerpModelAngles(ModelRenderer main, ModelRenderer second, float progress)
	{
		main.rotateAngleX = (float)MathHelper.clampedLerp(main.rotateAngleX, second.rotateAngleX, progress);
		main.rotateAngleY = (float)MathHelper.clampedLerp(main.rotateAngleY, second.rotateAngleY, progress);
		main.rotateAngleZ = (float)MathHelper.clampedLerp(main.rotateAngleZ, second.rotateAngleZ, progress);
		main.rotationPointX = (float)MathHelper.clampedLerp(main.rotationPointX, second.rotationPointX, progress);
		main.rotationPointY = (float)MathHelper.clampedLerp(main.rotationPointY, second.rotationPointY, progress);
		main.rotationPointZ = (float)MathHelper.clampedLerp(main.rotationPointZ, second.rotationPointZ, progress);
	}

	private void handleArmAnimation(EntityHans hans, HansArmAnimation animation, float ageInTicks)
	{
		switch(animation)
		{
			default:
			case NORMAL:
				break;
			case SALUTE:
			{
				bipedRightArm.rotateAngleX -= 2.5f;
				bipedRightArm.rotateAngleZ -= 0.5f;

				bipedRightHand.rotateAngleZ -= 0.75f;
			}
			break;
			case SURRENDER:
			{
				bipedRightArm.rotateAngleZ = 3.14f-0.25f;
				bipedLeftArm.rotateAngleZ -= 3.14f-0.25f;
			}
			break;

			case SQUAD_ORDER_ONWARDS:
			{
				float v = Math.min((ageInTicks%40)/22f, 1);
				float v1 = v%0.5f/0.5f;
				float v2 = v > 0.5f?(v-0.5f)/0.25f: 0;

				final float kickLeg = 0.25f;
				final float landLeg = -0.55f;

				bipedLeftHand.rotateAngleX = Utils.clampedLerp3Par(kickLeg, landLeg-v2*0.125f, kickLeg, v1);

			}
			break;
		}
	}

	private void handleLegAnimation(EntityHans hans, HansLegAnimation animation, float ageInTicks)
	{
		switch(animation)
		{
			default:
			case STANDING:
				break;
			case LYING:
			{
				bipedBody.rotateAngleX += 1.5f;

				float a1 = 1f-((bipedLeftLeg.rotateAngleX+0.3f)/0.6f);
				float a2 = 1f-((bipedRightLeg.rotateAngleX+0.3f)/0.6f);

				bipedRightLeg.rotateAngleY = -Math.abs(a1);
				bipedLeftLeg.rotateAngleY = Math.abs(a2);

				if(hans.getAttackTarget()==null)
				{
					bipedRightArm.rotateAngleY += Math.abs(a1*0.75f);
					bipedLeftArm.rotateAngleY += Math.abs(a2*0.75f);
				}

				bipedLeftFoot.rotateAngleZ = bipedLeftLeg.rotateAngleY;
				bipedRightFoot.rotateAngleZ = bipedRightLeg.rotateAngleY;

				bipedRightLeg.rotateAngleX = 1.5f;
				bipedLeftLeg.rotateAngleX = 1.5f;

				bipedRightLeg.rotateAngleZ = 0f;
				bipedLeftLeg.rotateAngleZ = 0f;

				bipedRightArm.rotateAngleX = -1.57f;
				bipedLeftArm.rotateAngleX = -1.57f;

				bipedRightLeg.rotationPointY = 1f;
				bipedLeftLeg.rotationPointY = 1f;

				bipedRightArm.rotationPointY = 1f;
				bipedLeftArm.rotationPointY = 1f;

				bipedRightLeg.rotationPointZ = 12f;
				bipedLeftLeg.rotationPointZ = 12f;
			}
			break;
			case KNEELING:
			{
				bipedLeftLeg.rotateAngleX = -1.57f;
				bipedLeftLegwear.rotateAngleX = -1.57f;
				bipedLeftFoot.rotateAngleX = 1.65f;

				bipedRightLeg.rotateAngleX = 0.25f;
				bipedRightFoot.rotateAngleX = 1.65f-0.25f;
			}
			break;
			case SQUATTING:
			{
				bipedLeftLeg.rotateAngleX = -1.57f-0.25f;
				bipedRightLeg.rotateAngleX = -1.57f-0.25f;

				bipedRightLeg.rotateAngleY = 0.625f;
				bipedLeftLeg.rotateAngleY = -0.625f;

				bipedRightLeg.rotationPointZ = 2f;
				bipedLeftLeg.rotationPointZ = 2f;

				bipedLeftFoot.rotateAngleX = 1.57f+0.425f;
				bipedRightFoot.rotateAngleX = 1.57f+0.425f;

				bipedBody.rotateAngleX = 0.3f;
				//bipedBody.rotationPointZ = 10f;

				bipedRightArm.rotateAngleX = -0.45f;
				bipedRightArm.rotateAngleY = 0.45f;

				bipedLeftArm.rotateAngleX = -0.45f;
				bipedLeftArm.rotateAngleY = -0.45f;

				bipedRightHand.rotateAngleX = -0.75f;
				bipedLeftHand.rotateAngleX = -0.75f;
			}
			break;
			case KAZACHOK:
			{
				float v = (ageInTicks%22)/22f;
				float v1 = v < 0.5f?v*2f: 1f;
				float v2 = v > 0.5f?(v-0.5f)/0.5f: 0;

				bipedRightLeg.rotateAngleY = 0.25f;
				bipedLeftLeg.rotateAngleY = -0.25f;

				final float kickLeg = -1.57f+0.25f;
				final float landLeg = -1.57f-0.25f;
				final float kickFoot = 1.57f+0.35f;

				bipedLeftLeg.rotateAngleX = Utils.clampedLerp3Par(kickLeg, landLeg-v2*0.125f, kickLeg, v1);
				bipedRightLeg.rotateAngleX = Utils.clampedLerp3Par(kickLeg, landLeg-v1*0.125f, kickLeg, v2);

				bipedLeftFoot.rotateAngleX = Utils.clampedLerp3Par(kickFoot, -0.25f, kickFoot, v1);
				bipedRightFoot.rotateAngleX = Utils.clampedLerp3Par(kickFoot, -0.25f, kickFoot, v2);

				bipedBody.rotateAngleX = -0.1f;

				bipedRightArm.rotateAngleX = -1.57f;
				bipedRightArm.rotateAngleY = -0.95f;
				bipedRightHand.rotateAngleX = -0.125f;
				bipedRightHand.rotateAngleZ = -0.35f;

				bipedLeftArm.rotateAngleX = -1.57f;
				bipedLeftArm.rotateAngleY = 0.95f;

				bipedLeftHand.rotateAngleX = -0.125f;
				bipedLeftHand.rotateAngleZ = 0.35f;
			}
			break;
			case SWIMMING:
			{

			}
			break;
		}
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		/*
		double wTime = (Math.abs(((entityIn.ticksExisted%40)/40d-0.5f)/0.5f));
		//this.bipedLeftArm.rotateAngleX = (float)(wTime*-2);
		this.leftHand.rotationPointY = 3f+(float)(wTime);
		this.leftHand.rotateAngleX = (float)(wTime*-2);
		 */

		GlStateManager.pushMatrix();
		/*
		if(entityIn instanceof EntityHans&&((EntityHans)entityIn).isKneeing)
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		 */

		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.popMatrix();

	}

	@Override
	public void postRenderArm(float scale, EnumHandSide side)
	{
		ModelRenderer hand = side==EnumHandSide.LEFT?bipedLeftHand: bipedRightHand;
		ModelRenderer arm = this.getArmForSide(side);

		float f = 0.5F*(float)(side==EnumHandSide.RIGHT?1: -1);
		arm.rotationPointX += f;
		hand.rotationPointY -= 3;
		hand.rotationPointZ += 1;
		arm.postRender(scale);
		hand.postRender(scale);
		hand.rotationPointY += 3;
		hand.rotationPointZ -= 1;
		arm.rotationPointX -= f;
	}

	//stolen from the Betweenlands,
	private static class ModelBoxCustomizable extends ModelBox
	{
		public static final int SIDE_LEFT = 1;

		public static final int SIDE_RIGHT = 2;

		public static final int SIDE_TOP = 4;

		public static final int SIDE_BOTTOM = 8;

		public static final int SIDE_FRONT = 16;

		public static final int SIDE_BACK = 32;

		public static final int SIDE_ALL = SIDE_LEFT|SIDE_RIGHT|SIDE_TOP|SIDE_BOTTOM|SIDE_FRONT|SIDE_BACK;

		private PositionTextureVertex[] vertexPositions;

		private TexturedQuad[] quadList;

		private final float posX1;

		private final float posY1;

		private final float posZ1;

		private final float posX2;

		private final float posY2;

		private final float posZ2;

		private int visibleSides;

		public ModelBoxCustomizable(ModelRenderer model, int u, int v, float x1, float y1, float z1, int width, int height, int depth, float expand, int topVOffset, int bottomUOffset)
		{
			super(model, u, v, x1, y1, z1, width, height, depth, expand);
			posX1 = x1;
			posY1 = y1;
			posZ1 = z1;
			posX2 = x1+width;
			posY2 = y1+height;
			posZ2 = z1+depth;
			vertexPositions = new PositionTextureVertex[8];
			quadList = new TexturedQuad[6];
			float x2 = x1+width;
			float y2 = y1+height;
			float z2 = z1+depth;
			x1 -= expand;
			y1 -= expand;
			z1 -= expand;
			x2 += expand;
			y2 += expand;
			z2 += expand;
			if(model.mirror)
			{
				float x = x2;
				x2 = x1;
				x1 = x;
			}
			PositionTextureVertex v000 = new PositionTextureVertex(x1, y1, z1, 1.3563156E-19F, 1.3563156E-19F);
			PositionTextureVertex v100 = new PositionTextureVertex(x2, y1, z1, 1.3563264E-19F, 4.966073E28F);
			PositionTextureVertex v110 = new PositionTextureVertex(x2, y2, z1, 1.9364292E31F, 1.7032407E25F);
			PositionTextureVertex v010 = new PositionTextureVertex(x1, y2, z1, 1.9441665E31F, 1.9264504E-19F);
			PositionTextureVertex v001 = new PositionTextureVertex(x1, y1, z2, 4.076745E22F, 1.3563156E-19F);
			PositionTextureVertex v101 = new PositionTextureVertex(x2, y1, z2, 2.0113521E-19F, 3.0309808E24F);
			PositionTextureVertex v111 = new PositionTextureVertex(x2, y2, z2, 6.977187E22F, 1.88877E31F);
			PositionTextureVertex v011 = new PositionTextureVertex(x1, y2, z2, 1.0943429E31F, 2.0958594E-19F);
			vertexPositions[0] = v000;
			vertexPositions[1] = v100;
			vertexPositions[2] = v110;
			vertexPositions[3] = v010;
			vertexPositions[4] = v001;
			vertexPositions[5] = v101;
			vertexPositions[6] = v111;
			vertexPositions[7] = v011;
			quadList[0] = new TexturedQuad(new PositionTextureVertex[]{v101, v100, v110, v111}, u+depth+width, v+depth, u+depth+width+depth, v+depth+height, model.textureWidth, model.textureHeight);
			quadList[1] = new TexturedQuad(new PositionTextureVertex[]{v000, v001, v011, v010}, u, v+depth, u+depth, v+depth+height, model.textureWidth, model.textureHeight);
			quadList[2] = new TexturedQuad(new PositionTextureVertex[]{v101, v001, v000, v100}, u+depth, v+topVOffset, u+depth+width, v+depth+topVOffset, model.textureWidth, model.textureHeight);
			quadList[3] = new TexturedQuad(new PositionTextureVertex[]{v110, v010, v011, v111}, u+depth+width+bottomUOffset, v+depth+topVOffset, u+depth+width+width+bottomUOffset, v+topVOffset, model.textureWidth, model.textureHeight);
			quadList[4] = new TexturedQuad(new PositionTextureVertex[]{v100, v000, v010, v110}, u+depth, v+depth, u+depth+width, v+depth+height, model.textureWidth, model.textureHeight);
			quadList[5] = new TexturedQuad(new PositionTextureVertex[]{v001, v101, v111, v011}, u+depth+width+depth, v+depth, u+depth+width+depth+width, v+depth+height, model.textureWidth, model.textureHeight);
			if(model.mirror)
			{
				for(int i = 0; i < quadList.length; i++)
				{
					quadList[i].flipFace();
				}
			}
			setVisibleSides(SIDE_ALL);
		}

		public void setVisibleSides(int visibleSides)
		{
			this.visibleSides = visibleSides&SIDE_ALL;
		}

		@Override
		public void render(BufferBuilder vb, float scale)
		{
			for(int i = 0; i < quadList.length; i++)
			{
				if((visibleSides&(1<<i))!=0)
				{
					quadList[i].draw(vb, scale);
				}
			}
		}
	}
}
