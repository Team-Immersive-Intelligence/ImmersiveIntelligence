package pl.pabilo8.immersiveintelligence.client.render.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIBooleanAnimation;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IBipedModifyingRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIItemRendererAMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IIItemRendererAMT.RegisteredItemRenderer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIBinoculars;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;

/**
 * Renders the regular and infrared binoculars.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 20.11.2026
 * @since 20.01.2021
 */
@RegisteredItemRenderer(name = "items/tools/binoculars")
public class BinocularsRenderer extends IIItemRendererAMT<ItemIIBinoculars> implements IBipedModifyingRenderer
{
	private AMTModel model;
	private AMT binoculars;
	private IIBooleanAnimation infraredState;

	public BinocularsRenderer()
	{
		super(IIContent.itemBinoculars, ResLoc.of(IIReference.RES_ITEM_MODEL, "tools/binoculars")
				.withExtension(ResLoc.EXT_OBJ), true);
	}

	@Override
	public void draw(ItemStack stack, TransformType transform, BufferBuilder buf, Tessellator tes, float partialTicks)
	{
		//Don't draw when sneaking
		if(ItemNBTHelper.getBoolean(stack, "sneaking"))
			return;

		boolean infrared = item.isAdvanced(stack);
		binoculars.setVisible(!infrared);
		if(infrared)
			infraredState.apply(item.isEnabled(stack));
		else
			infraredState.applyVisibility(false);
		model.render(tes, buf);
	}

	@Override
	public void compileModels(OBJModel model, AMTModelHeader header)
	{
		this.model = new AMTModel(DefaultVertexFormats.ITEM, model, header, AMTModelHeader::createParts);
		this.binoculars = this.model.getPart("binoculars");
		this.infraredState = new IIBooleanAnimation(
				this.model.getPart("infrared_binoculars_on"),
				this.model.getPart("infrared_binoculars_off")
		);
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(model);
	}

	@Override
	public boolean onBipedRender(EntityLivingBase livingBase, ModelBiped biped, ItemStack stack, EnumHand hand, float swingProgress, float partialTicks)
	{
		if(!ItemNBTHelper.getBoolean(stack, "sneaking"))
			return false;

		//Set player angles
		biped.bipedRightArm.rotateAngleY = biped.bipedHead.rotateAngleY-0.25f;
		biped.bipedLeftArm.rotateAngleY = biped.bipedHead.rotateAngleY+0.25f;
		biped.bipedRightArm.rotateAngleX = biped.bipedHead.rotateAngleX-2f;
		biped.bipedLeftArm.rotateAngleX = biped.bipedRightArm.rotateAngleX;

		//Select model variant
		boolean infrared = item.isAdvanced(stack);
		binoculars.setVisible(!infrared);
		if(infrared)
			infraredState.apply(item.isEnabled(stack));
		else
			infraredState.applyVisibility(false);

		//Offset the model
		GlStateManager.pushMatrix();
		GlStateManager.translate(
				(biped.bipedHead.rotationPointX+biped.bipedHead.offsetX)/16f,
				(biped.bipedHead.rotationPointY+biped.bipedHead.offsetY)/16f+0.2f,
				(biped.bipedHead.rotationPointZ+biped.bipedHead.offsetZ)/16f
		);
		GlStateManager.rotate((float)Math.toDegrees(biped.bipedHead.rotateAngleY), 0, 1, 0);
		GlStateManager.rotate((float)Math.toDegrees(biped.bipedHead.rotateAngleX), 1, 0, 0);
		GlStateManager.rotate((float)Math.toDegrees(biped.bipedHead.rotateAngleZ), 0, 0, 1);
		GlStateManager.translate(0, -0.25f-0.0625f, -0.25);

		//Render model
		int i = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		ClientUtils.bindAtlas();
		Tessellator tes = Tessellator.getInstance();
		model.render(tes, tes.getBuffer());

		//Use previous bound (mob) texture
		GlStateManager.bindTexture(i);
		GlStateManager.popMatrix();
		return true;
	}
}
