package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;

/**
 * AMT type for drawing the player's hand in first person mode
 *
 * @author Pabilo8
 * @since 26.07.2022
 */
public class AMTHand extends AMT
{
	private final EnumHand hand;
	private final ModelRenderer handModel;
	private final EntityPlayerSP player = ClientUtils.mc().player;

	public AMTHand(String name, Vec3d originPos, EnumHand hand)
	{
		super(name, originPos);
		this.hand = hand;

		//get player renderer
		Render<AbstractClientPlayer> render = ClientUtils.mc().getRenderManager().getEntityRenderObject(player);
		RenderPlayer renderP = ((RenderPlayer)render);
		//no renderer means no model either
		if(renderP==null)
		{
			handModel = null;
			return;
		}

		//get player's model
		ModelPlayer model = renderP.getMainModel();

		//get primary hand
		EnumHandSide handSide = player.getPrimaryHand();
		if(hand==EnumHand.OFF_HAND)
			handSide = handSide.opposite();

		//create a custom hand model based on the player's one
		handModel = new ModelRenderer(model);
		if(handSide==EnumHandSide.RIGHT)
		{
			handModel.cubeList.addAll(model.bipedRightArm.cubeList);
			handModel.cubeList.addAll(model.bipedRightArmwear.cubeList);
		}
		else
		{
			handModel.cubeList.addAll(model.bipedLeftArm.cubeList);
			handModel.cubeList.addAll(model.bipedLeftArmwear.cubeList);
		}
		handModel.rotationPointY = -10;
		//remove the "new" hand from the player model
		model.boxList.remove(handModel);

	}

	public AMTHand(String name, IIModelHeader header, EnumHand hand)
	{
		this(name, header.getOffset(name), hand);
	}

	@Override
	protected void preDraw()
	{
		GlStateManager.translate(originPos.x, originPos.y, originPos.z);

		if(off!=null)
			GlStateManager.translate(-off.x, off.y, off.z);

		if(rot!=null)
		{
			GlStateManager.rotate((float)rot.y, 0, 1, 0);
			GlStateManager.rotate((float)rot.z, 0, 0, 1);
			GlStateManager.rotate((float)-rot.x, 1, 0, 0);
		}
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		IIClientUtils.bindTexture(player.getLocationSkin());
		if(handModel==null)
			return;

		GlStateManager.pushMatrix();
		GlStateManager.scale(1, 1.5, 1);
		handModel.render(0.125f);
		GlStateManager.popMatrix();
		ClientUtils.bindAtlas();
	}

	@Override
	public void disposeOf()
	{
		if(handModel==null)
			return;
		// TODO: 11.02.2023 do it the hacky way
		/*if(listID!=-1)
			GlStateManager.glDeleteLists(listID, 1);*/
	}
}
