package pl.pabilo8.immersiveintelligence.client.render.hans;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.EyeEmotions;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.MouthEmotions;
import pl.pabilo8.immersiveintelligence.common.entity.hans.HansAnimations.MouthShapes;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 02.05.2021
 */
public class LayerHansEmotions implements LayerRenderer<EntityHans>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("immersiveengineering", "textures/items/white.png");
	private final HansRenderer hansRenderer;
	private final ModelBiped modelHans;

	//eyes
	private final IIColor EYEBROW_COLOUR = IIColor.fromPackedRGB(0x2e2623);
	private final IIColor EYE_BACK_COLOUR = IIColor.fromPackedRGB(0xf1f1f1);
	private final IIColor EYELID_COLOUR = IIColor.fromPackedRGB(0xdea893);
	//mouth
	private final IIColor LIP_COLOUR = IIColor.fromPackedRGB(0xc59986);
	private final IIColor MOUTH_COLOUR = IIColor.fromPackedRGB(0x473e3a);
	private final IIColor TEETH_COLOUR = IIColor.fromPackedRGB(0xc1c1c1);
	private final IIColor TONGUE_COLOUR = IIColor.fromPackedRGB(0xc58686);

	public LayerHansEmotions(HansRenderer renderer)
	{
		this.hansRenderer = renderer;
		this.modelHans = (ModelBiped)hansRenderer.mainModel;
	}

	public void doRenderLayer(EntityHans hans, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		GlStateManager.pushMatrix();

		if(hans.isSneaking())
		{
			GlStateManager.translate(0, 0.25, 0);
		}

		//GlStateManager.enableBlend();
		GlStateManager.color(1f, 1f, 1f);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		GlStateManager.scale(0.0625, 0.0625, 0.0625);

		ModelRenderer biped = modelHans.bipedHead;
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

		EyeEmotions emotionE = hans.eyeEmotion;
		MouthEmotions emotionM = hans.mouthEmotion;
		MouthShapes mouthShape = hans.mouthShape;

		//double wTime = (Math.abs(((hans.ticksExisted%5+partialTicks)/5d-0.5f)/0.5f));

		double lookOffset = 0;
		double eyebrowThickness = emotionE.eyebrowThickness;
		double eyebrowHeightDiffRight = emotionE.eyebrowHeightDiffRight;
		double eyebrowHeightDiffLeft = emotionE.eyebrowHeightDiffLeft;
		double eyeBlink = Math.min((hans.ticksExisted%140+partialTicks)/5f, 1f);
		double eyeBlinkHalf = 1f-Math.abs((eyeBlink-0.5f)/0.5f);

		double lipBottomOffset, lipBottomWidth, lipTopOffset, lipTopWidth, tongueHeight;
		boolean upperTeethVisible;

		if(hans.mouthShapeQueue.size() > 0)
		{
			double mouthProgress = (hans.speechProgress+partialTicks)/hans.mouthShapeQueue.get(0).getFirst();
			double[] mouthShapeVals = HansAnimations.getMouthShapeInBetween(hans.mouthShape, hans.mouthShapeQueue.get(0).getSecond(), emotionM, mouthProgress);

			lipBottomOffset = mouthShapeVals[0];
			lipBottomWidth = mouthShapeVals[1];
			lipTopOffset = mouthShapeVals[2];
			lipTopWidth = mouthShapeVals[3];
			tongueHeight = mouthShapeVals[4];
			upperTeethVisible = hans.mouthShapeQueue.get(0).getSecond().upperTeethVisible;
		}
		else
		{
			lipBottomOffset = mouthShape.lipBottomOffset;
			lipBottomWidth = mouthShape.lipBottomWidth;
			lipTopOffset = mouthShape.lipTopOffset;
			lipTopWidth = mouthShape.lipTopWidth;
			tongueHeight = mouthShape.tongueHeight;
			upperTeethVisible = mouthShape.upperTeethVisible;
		}

		//rendering color only is broken here for some reason
		this.hansRenderer.bindTexture(TEXTURE);
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

		drawHansEyes(buffer, lookOffset, eyebrowThickness, eyebrowHeightDiffRight, eyebrowHeightDiffLeft, eyeBlinkHalf, hans.eyeColor);
		drawHansMouth(buffer, lipBottomOffset, lipBottomWidth, lipTopOffset, lipTopWidth, tongueHeight, upperTeethVisible);

		tessellator.draw();
		//GlStateManager.disableBlend();

		GlStateManager.popMatrix();


	}

	public boolean shouldCombineTextures()
	{
		return true;
	}

	public static void drawTexturedModalRect(BufferBuilder buff, double x, double y, double z, double width, double height, double depth, double hdiff, IIColor color)
	{
		float[] rgb = color.getFloatRGB();
		float us = 0f, vs = 0f, ue = 1f, ve = 1f;

		buff.pos(x, y, z).tex(us, vs).color(rgb[0], rgb[1], rgb[2], 1f).endVertex();
		buff.pos(x, y+height, z+depth).tex(us, ve).color(rgb[0], rgb[1], rgb[2], 1f).endVertex();
		buff.pos(x+width, y+height+hdiff, z+depth).tex(ue, ve).color(rgb[0], rgb[1], rgb[2], 1f).endVertex();
		buff.pos(x+width, y+hdiff, z).tex(ue, ve).color(rgb[0], rgb[1], rgb[2], 1f).endVertex();
	}

	private void drawHansEyes(BufferBuilder buffer, double lookOffset, double eyebrowThickness, double eyebrowHeightDiffRight, double eyebrowHeightDiffLeft, double eyeBlinkProgress, IIColor eyeColour)
	{
		//
		drawTexturedModalRect(buffer, 1, -5-eyebrowThickness+(0.125f*eyeBlinkProgress), -4.014f, 2, eyebrowThickness, 0, eyebrowHeightDiffRight, EYEBROW_COLOUR);
		drawTexturedModalRect(buffer, 1, -5, -4.011f, 2, 1, 0, 0, EYE_BACK_COLOUR);
		drawTexturedModalRect(buffer, 1+Math.max(lookOffset, 0), -5, -4.012f, 1, 1, 0, 0, eyeColour);

		drawTexturedModalRect(buffer, -3, -5-eyebrowThickness+(0.125f*eyeBlinkProgress)+eyebrowHeightDiffLeft, -4.014f, 2, eyebrowThickness, 0, -eyebrowHeightDiffLeft, EYEBROW_COLOUR);
		drawTexturedModalRect(buffer, -3, -5, -4.011f, 2, 1, 0, 0, EYE_BACK_COLOUR);
		drawTexturedModalRect(buffer, -2+Math.min(lookOffset, 0), -5, -4.012f, 1, 1, 0, 0, eyeColour);

		drawTexturedModalRect(buffer, 1, -5, -4.013f, 2, eyeBlinkProgress, 0, 0, EYELID_COLOUR);
		drawTexturedModalRect(buffer, -3, -5, -4.013f, 2, eyeBlinkProgress, 0, 0, EYELID_COLOUR);

	}

	private void drawHansMouth(BufferBuilder buffer, double lipBottomOffset, double lipBottomWidth, double lipTopOffset, double lipTopWidth, double tongueHeight, boolean upperTeethVisible)
	{
		//(lipTopWidth+lipBottomWidth)/2d
		drawTexturedModalRect(buffer, -1-((Math.min(lipTopWidth, lipBottomWidth)-1)), -2, -4.011f, 2*Math.min(lipTopWidth, lipBottomWidth), 1, 0, 0, MOUTH_COLOUR);
		drawTexturedModalRect(buffer, -0.5, -1-tongueHeight, -4.012f, 1, tongueHeight, 0, 0, TONGUE_COLOUR);

		drawTexturedModalRect(buffer, -1-((lipTopWidth-1)), -2-(lipTopOffset*0.55), -4.016f, 2*lipTopWidth, 1-(lipTopOffset*0.55), 0, 0, LIP_COLOUR);
		drawTexturedModalRect(buffer, -1-((lipTopWidth-1))+0.125, -2-(lipTopOffset*0.4), upperTeethVisible?-4.015f: -4.013f, 1.75*lipTopWidth, 1-(lipTopOffset*0.4), 0, 0, TEETH_COLOUR);


		drawTexturedModalRect(buffer, -1-((lipBottomWidth-1)), -2+(2*lipBottomOffset*0.55), -4.014f, 2*lipBottomWidth, 1-(lipBottomOffset*0.55), 0, 0, LIP_COLOUR);
		drawTexturedModalRect(buffer, -1-((lipBottomWidth-1))+0.125, -2+(2*lipBottomOffset*0.4), -4.013f, 1.75*lipBottomWidth, 1-(lipBottomOffset*0.4), 0, 0, TEETH_COLOUR);
	}
}