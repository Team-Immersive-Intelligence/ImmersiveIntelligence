package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IBipedModifyingRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIItemRendererAMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIItemRendererAMT.RegisteredItemRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.ISpecificHandRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIMineDetector;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

/**
 * Renders the articulated mine detector from the player's grip.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 20.11.2026
 * @since 28.01.2021
 */
@RegisteredItemRenderer(name = "items/tools/mine_detector")
public class MineDetectorRenderer extends IIItemRendererAMT<ItemIIMineDetector> implements IBipedModifyingRenderer, ISpecificHandRenderer
{
	private AMTModel model;
	private IIAnimationCompiledMap reach, reachFirstPerson, onGround;

	public MineDetectorRenderer()
	{
		super(IIContent.itemMineDetector, ResLoc.of(IIReference.RES_ITEM_MODEL, "tools/mine_detector")
				.withExtension(ResLoc.EXT_OBJ), true);
	}

	@Override
	public void draw(ItemStack stack, TransformType transform, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		//Only draw on ground and fixed models
		if(is1stPerson(transform)||is3rdPerson(transform))
			return;
		this.onGround.apply(0f);
		this.model.render(tes, buf);
	}

	@Override
	public boolean onBipedRender(EntityLivingBase living, ModelBiped biped, ItemStack stack, EnumHand hand, float swingProgress, float partialTicks)
	{
		if(model==null)
			return false;
		float reach = getReach(living);
		Tessellator tes = Tessellator.getInstance();

		//Set transforms, cache previous texture
		GlStateManager.pushMatrix();
		GlStateManager.rotate((float)Math.toDegrees(biped.bipedBody.rotateAngleY), 0, 1, 0);
		GlStateManager.scale(1, -1, 1);
		GlStateManager.translate(0, -1.5, -2);
		int prevTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);

		//Draw 3rd person model
		IIClientUtils.bindAtlas();
		this.reach.apply(reach);
		this.model.render(tes, tes.getBuffer());

		//Rollback texture
		GlStateManager.popMatrix();
		GlStateManager.bindTexture(prevTexture);

		//Set biped angles
		biped.bipedRightArm.rotateAngleY = biped.bipedBody.rotateAngleY-0.45f;
		biped.bipedLeftArm.rotateAngleY = biped.bipedBody.rotateAngleY+0.45f;

		biped.bipedRightArm.rotateAngleX = -1.25f-(1f-reach)*0.5f;
		biped.bipedLeftArm.rotateAngleX = biped.bipedRightArm.rotateAngleX;
		return true;
	}

	@Override
	public void compileModels(OBJModel model, AMTModelHeader header)
	{
		this.model = new AMTModel(DefaultVertexFormats.ITEM, model, header, AMTModelHeader::createParts);
		this.reach = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("tools/mine_detector/angle"));
		this.reachFirstPerson = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("tools/mine_detector/first_person"));
		this.onGround = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("tools/mine_detector/on_ground"));
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(model);
	}

	@Override
	public boolean doHandRender(ItemStack stack, EnumHand hand, ItemStack otherHand, float swingProgress, float partialTicks)
	{
		EntityLivingBase player = ClientUtils.mc().player;
		if(this.model==null||player==null)
			return false;

		RayTraceResult hit = player.rayTrace(5, partialTicks);
		double distance = 2;
		double height = -player.getEyeHeight();
		if(hit!=null&&hit.typeOfHit==Type.BLOCK)
		{
			Vec3d offset = hit.hitVec.subtract(player.getPositionEyes(partialTicks));
			distance = MathHelper.clamp(Math.sqrt(offset.x*offset.x+offset.z*offset.z), 0.5, 2);
			height = offset.y;
		}

		float progress = MathHelper.clamp((float)distance-1f, 0f, 1f);
		float pitch = player.prevRotationPitch+(player.rotationPitch-player.prevRotationPitch)*partialTicks;

		//Set transforms, cache previous texture
		int prevTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		GlStateManager.pushMatrix();

		//The target lies along the camera yaw. Apply pitch to place it in camera space.
		GlStateManager.rotate(pitch, 1, 0, 0);
		GlStateManager.translate(0, height-0.5, -4);
//			GlStateManager.rotate(180, 0, 1, 0);

		//Anchor the animated base at the target.
		IIClientUtils.bindAtlas();
		Tessellator tes = Tessellator.getInstance();
		this.reachFirstPerson.apply(1f-progress);
		this.model.render(tes, tes.getBuffer());

		GlStateManager.bindTexture(prevTexture);
		GlStateManager.popMatrix();
		return true;
	}

	private float getReach(EntityLivingBase living)
	{
		RayTraceResult result = living.rayTrace(5f, 0);
		if(result!=null&&result.typeOfHit!=Type.MISS)
		{
			Vec3d diff = result.hitVec.subtract(living.getPositionVector());
			float distance = (float)Math.sqrt(Math.abs(diff.x*diff.z));
			return 1f-MathHelper.clamp(distance, 0f, 1f);
		}
		return 0f;
	}
}
