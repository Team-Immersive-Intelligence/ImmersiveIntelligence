package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

import javax.annotation.Nullable;

/**
 * An {@link AMT} to {@link ModelBiped} animation adapter element, allowing to apply AMT animations onto traditional java models used by entity renderers.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 23.11.2025
 */
public class AMTBipedAdapter extends AMT
{
	private static final ModelBiped previousBipedModel = new ModelBiped();
	private static boolean drawn = false;
	private final AMT partHead, partBody, partRightArm, partLeftArm, partRightLeg, partLeftLeg;
	boolean shouldDraw = false;

	public AMTBipedAdapter(String name, AMTModelHeader header)
	{
		this(name, header.getOffset(name));
	}

	public AMTBipedAdapter(String name, Vec3d originPos)
	{
		super(name, originPos);
		// Contrary to vanilla, AMT Biped models have head and arms dependent on the body.
		// The body part rotates from the bottom in the imitation model, but from the chest in-game
		setChildren(
				this.partBody = new AMTLocator(name+"_body", new Vec3d(0, 12, 2)) // Body rotates from chest (y=12) in-game
						.withChildren(
								this.partHead = new AMTLocator(name+"_head", new Vec3d(0, 0, 0)), // Head is 12 units above body origin
								this.partRightArm = new AMTLocator(name+"_right_arm", new Vec3d(-6, -1, 0)), // Right arm position
								this.partLeftArm = new AMTLocator(name+"_left_arm", new Vec3d(6, -1, 0)) // Left arm position
						),
				this.partRightLeg = new AMTLocator(name+"_right_leg", new Vec3d(-2, 12, 0)), // Right leg at hip level
				this.partLeftLeg = new AMTLocator(name+"_left_leg", new Vec3d(2, 12, 0)) // Left leg at hip level
		);
	}

	/**
	 * Applies the current AMTLocator positions and rotations to the given ModelBiped
	 *
	 * @param biped the ModelBiped to apply to
	 */
	public void applyAnimationStateTo(ModelBiped biped)
	{
		if(shouldDraw)
		{
			biped.setVisible(false);
			return;
		}

		partBody.setScale(new Vec3d(1, -1, 1));
		if(partBody.rot!=null)
			partBody.setRotation(new Vec3d(partBody.rot.x, partBody.rot.y, -partBody.rot.z));

		applyPartState(partBody, biped.bipedBody, previousBipedModel.bipedBody);
		applyPartState(partHead, biped.bipedHead, previousBipedModel.bipedHead);
		applyPartState(partRightArm, biped.bipedRightArm, previousBipedModel.bipedRightArm);
		applyPartState(partLeftArm, biped.bipedLeftArm, previousBipedModel.bipedLeftArm);
		applyPartState(partRightLeg, biped.bipedRightLeg, previousBipedModel.bipedRightLeg);
		applyPartState(partLeftLeg, biped.bipedLeftLeg, previousBipedModel.bipedLeftLeg);

		if(partBody.rot!=null)
			partBody.setRotation(new Vec3d(partBody.rot.x, partBody.rot.y, -partBody.rot.z));

		ModelBase.copyModelAngles(biped.bipedHead, biped.bipedHeadwear);
		drawn = true;
	}

	/**
	 * Apply AMT locator state into a biped part.
	 *
	 * @param child       AMT locator for this part
	 * @param mr          target ModelRenderer
	 * @param defaultPart storage for reset
	 */
	private void applyPartState(AMT child, ModelRenderer mr, ModelRenderer defaultPart)
	{
		if(!drawn)
			ModelBase.copyModelAngles(mr, defaultPart);

		// Calculate the transformation matrix for this part
		Matrix4 mat = new Matrix4().setIdentity();
		if(child==partHead||child==partRightArm||child==partLeftArm)
			applyTransformToMatrix(mat, partBody);

		// Apply the child's own transformation
		applyTransformToMatrix(mat, child);

		// Convert matrix to ModelRenderer transformations
		// For rotation points, we need to consider the part's original position
		Vec3d transformedOffset = mat.apply(child.originPos);

		// Set the rotation point (adjusted for Minecraft's coordinate system)
		mr.rotationPointX = (float)transformedOffset.x;
		mr.rotationPointY = (float)transformedOffset.y;
		mr.rotationPointZ = (float)transformedOffset.z;

		// Extract rotation from matrix and apply to ModelRenderer
		if(child.rot!=null)
		{
			// Apply rotations in Minecraft's order (Z, Y, X)
			// Note: AMT uses degrees, ModelRenderer uses radians
			mr.rotateAngleX = (float)Math.toRadians(-child.rot.x);
			mr.rotateAngleY = (float)Math.toRadians(child.rot.y);
			mr.rotateAngleZ = (float)Math.toRadians(child.rot.z);
		}

		if(child==partBody)
		{
			mr.rotationPointY += 12.0f;
		}
	}

	/**
	 * Apply an AMT's transformation to a matrix
	 */
	private void applyTransformToMatrix(Matrix4 mat, AMT amt)
	{
		if(amt.off!=null)
			mat.translate(-amt.off.x, -amt.off.y, -amt.off.z);

		mat.translate(amt.originPos.x, amt.originPos.y, amt.originPos.z);

		if(amt.rot!=null)
		{
			// Apply rotations in YZX order (as in AMT.preDraw)
			mat.rotate((float)Math.toRadians(amt.rot.y), 0, 1, 0);
			mat.rotate((float)Math.toRadians(amt.rot.z), 0, 0, 1);
			mat.rotate((float)Math.toRadians(-amt.rot.x), 1, 0, 0);
		}

		mat.translate(-amt.originPos.x, -amt.originPos.y, -amt.originPos.z);
		if(amt.scale!=null)
			mat.scale(amt.scale.x, amt.scale.y, amt.scale.z);
	}

	@Nullable
	public static ModelBiped getPreviousBipedModel()
	{
		if(!drawn)
			return null;
		drawn = false;
		return previousBipedModel;
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(shouldDraw)
		{
			//TODO: 23.11.2025 drawing a custom biped model
			// This would be used if we want to render the AMT representation directly
		}
	}

	@Override
	public void disposeOf()
	{
		// Clean up resources if needed
	}
}
