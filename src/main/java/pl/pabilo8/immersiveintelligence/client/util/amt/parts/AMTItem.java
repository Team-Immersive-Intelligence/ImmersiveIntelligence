package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * AMT type for drawing items ({@link ItemStack}s)
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.07.2022
 */
public class AMTItem extends AMT
{
	private ItemStack stack, stackInto;
	private boolean drawStacked = false;

	public AMTItem(String name, Vec3d originPos)
	{
		super(name, originPos);
	}

	public AMTItem(String name, AMTModelHeader header)
	{
		super(name, header);
	}

	public AMTItem setStacking(boolean drawStacked)
	{
		this.drawStacked = drawStacked;
		return this;
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
		if(stack==null)
			return;
		GlStateManager.pushMatrix();
		CullFace cf = (GL11.glGetInteger(GL11.GL_CULL_FACE_MODE)==GL11.GL_FRONT)?CullFace.FRONT: CullFace.BACK;

		if(stackInto!=null)
		{
			if(property==0)
				ClientUtils.mc().getRenderItem().renderItem(stack, TransformType.NONE);
			else if(property==1)
				ClientUtils.mc().getRenderItem().renderItem(stackInto, TransformType.NONE);
			else
			{
				//Use stencil buffer to interpolate between stack and stackInto based on special property
				GL11.glEnable(GL11.GL_STENCIL_TEST);
				GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);

				//Draw first item where stencil == 0
				GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
				GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
				GL11.glColorMask(false, false, false, false);
				GL11.glDepthMask(false);

				//Draw mask for interpolation
				GlStateManager.rotate(ClientUtils.mc().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

				GlStateManager.disableTexture2D();
				buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
				ClientUtils.renderBox(buf, .5, .5f, .5, -.5, -.5+property, -.5);
				tes.draw();
				GlStateManager.enableTexture2D();

				GlStateManager.rotate(-(ClientUtils.mc().getRenderManager().playerViewY), 0.0F, 1.0F, 0.0F);

				GL11.glColorMask(true, true, true, true);
				GL11.glDepthMask(true);

				//Draw stack where stencil == 1
				GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
				GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
				ClientUtils.mc().getRenderItem().renderItem(stack, TransformType.NONE);

				//Draw stackInto where stencil == 0
				GL11.glStencilFunc(GL11.GL_EQUAL, 0, 0xFF);
				ClientUtils.mc().getRenderItem().renderItem(stackInto, TransformType.NONE);

				GL11.glDisable(GL11.GL_STENCIL_TEST);
			}
		}
		else
		{

			if(drawStacked)
				for(int i = 0; i < stack.getCount(); i++)
				{
					ClientUtils.mc().getRenderItem().renderItem(stack, TransformType.NONE);
					GlStateManager.translate(0, 0.0625f, 0.0625f);
				}
			else
				ClientUtils.mc().getRenderItem().renderItem(stack, TransformType.NONE);

		}
		GlStateManager.cullFace(cf);
		GlStateManager.popMatrix();
	}

	@Override
	public void disposeOf()
	{

	}

	@Override
	public void applyProperties(EasyNBT nbt)
	{
		super.applyProperties(nbt);
		nbt.checkSetItemStack("stack", stack -> this.stack = stack);
		nbt.checkSetItemStack("stackInto", stackInto -> this.stackInto = stackInto);
		nbt.checkSetBoolean("drawStacked", drawStacked -> this.drawStacked = drawStacked);
	}

	public void setStack(ItemStack stack)
	{
		this.stack = stack;
	}

	public void setStack(ItemStack stackFrom, ItemStack stackInto)
	{
		this.stack = stackFrom;
		this.stackInto = stackInto;
	}
}
