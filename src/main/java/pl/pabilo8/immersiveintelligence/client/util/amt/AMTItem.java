package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

/**
 * AMT type for drawing items ({@link ItemStack}s)
 *
 * @author Pabilo8
 * @since 26.07.2022
 */
public class AMTItem extends AMT
{
	private ItemStack stack;

	public AMTItem(String name, Vec3d originPos)
	{
		super(name, originPos);
	}

	public AMTItem(String name, IIModelHeader header)
	{
		super(name, header);
	}

	@Override
	protected void preDraw()
	{
		if(off!=null)
			GlStateManager.translate(-off.x, off.y, off.z);

		GlStateManager.translate(originPos.x, originPos.y, originPos.z);

		if(rot!=null)
		{
			GlStateManager.rotate((float)rot.y, 0, 1, 0);
			GlStateManager.rotate((float)rot.z, 0, 0, 1);
			GlStateManager.rotate((float)-rot.x, 1, 0, 0);
		}

		GlStateManager.scale(-1f, 1f, -1f);

		if(scale!=null)
			GlStateManager.scale(scale.x, scale.y, scale.z);
	}

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(stack!=null)
		{
			//GlStateManager.scale(0.0625,0.0625,0.0625);
			//GlStateManager.translate(originPos.x,originPos.y,originPos.z);

//			GlStateManager.cullFace(GlStateManager.CullFace.BACK);
//			GlStateManager.cullFace();
//			IILogger.info();
			GlStateManager.pushMatrix();
			CullFace cf = (GL11.glGetInteger(GL11.GL_CULL_FACE_MODE)==GL11.GL_FRONT)?CullFace.FRONT: CullFace.BACK;
//			GlStateManager.scale(-1, -1, -1);

			ClientUtils.mc().getRenderItem().renderItem(stack, TransformType.NONE);

			GlStateManager.cullFace(cf);
			GlStateManager.popMatrix();


//			IILogger.info(GL11.glGetInteger(GL11.GL_CULL_FACE_MODE));
		}
	}

	@Override
	public void disposeOf()
	{

	}

	public void setStack(ItemStack stack)
	{
		this.stack = stack;
	}
}
