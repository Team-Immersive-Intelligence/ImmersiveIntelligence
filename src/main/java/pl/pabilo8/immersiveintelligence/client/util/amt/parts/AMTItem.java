package pl.pabilo8.immersiveintelligence.client.util.amt.parts;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.render.TileRenderAutoWorkbench;
import blusunrize.immersiveengineering.client.render.TileRenderAutoWorkbench.BlueprintLines;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.nio.DoubleBuffer;

/**
 * AMT type for drawing items ({@link ItemStack}s)
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 22.08.2026
 * @since 26.07.2022
 */
public class AMTItem extends AMT
{
	private static final DoubleBuffer CLIP_PLANE_BUFFER = BufferUtils.createDoubleBuffer(4);

	private ItemStack stack, stackInto;
	private boolean drawStacked = false;
	private boolean drawSchematic = false;

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

	public AMTItem setStack(ItemStack stack)
	{
		this.stack = stack;
		return this;
	}

	public AMTItem setStack(ItemStack stackFrom, ItemStack stackInto)
	{
		this.stack = stackFrom;
		this.stackInto = stackInto;
		return this;
	}

	/**
	 * Renders an item transition with a model-space horizontal clipping plane.
	 *
	 * @param stackFrom     item shown at transition start
	 * @param stackInto     item shown at transition end
	 * @param transformType item camera transform
	 * @param transition    transition progress in the 0-1 range
	 * @param fromBottom    whether stackInto replaces stackFrom from bottom to top
	 */
	public static void renderTransition(ItemStack stackFrom, ItemStack stackInto, TransformType transformType, float transition, boolean fromBottom)
	{
		float clamped = MathHelper.clamp(transition, 0f, 1f);
		if(clamped <= 0f)
		{
			ClientUtils.mc().getRenderItem().renderItem(stackFrom, transformType);
			return;
		}
		if(clamped >= 1f)
		{
			ClientUtils.mc().getRenderItem().renderItem(stackInto, transformType);
			return;
		}

		double height = fromBottom?-0.5d+clamped: 0.5d-clamped;

		GL11.glPushAttrib(GL11.GL_TRANSFORM_BIT);
		try
		{
			GL11.glEnable(GL11.GL_CLIP_PLANE0);
			setClipPlane(height, fromBottom);
			ClientUtils.mc().getRenderItem().renderItem(stackFrom, transformType);

			setClipPlane(height, !fromBottom);
			ClientUtils.mc().getRenderItem().renderItem(stackInto, transformType);
		} finally
		{
			GL11.glPopAttrib();
		}
	}

	private static void setClipPlane(double height, boolean keepAbove)
	{
		CLIP_PLANE_BUFFER.clear();
		CLIP_PLANE_BUFFER.put(0d);
		CLIP_PLANE_BUFFER.put(keepAbove?1d: -1d);
		CLIP_PLANE_BUFFER.put(0d);
		CLIP_PLANE_BUFFER.put(keepAbove?-height: height);
		CLIP_PLANE_BUFFER.flip();
		GL11.glClipPlane(GL11.GL_CLIP_PLANE0, CLIP_PLANE_BUFFER);
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
			renderTransition(stack, stackInto, TransformType.FIXED, property, false);
		else
		{

			if(drawStacked)
				for(int i = 0; i < stack.getCount(); i++)
				{
					ClientUtils.mc().getRenderItem().renderItem(stack, TransformType.FIXED);
					GlStateManager.translate(0, 0.0625f, 0.0625f);
				}
			else if(drawSchematic)
			{
				BlueprintLines blueprint = TileRenderAutoWorkbench.getBlueprintDrawable(stack, ClientUtils.mc().world);
				GlStateManager.disableCull();
				GlStateManager.disableTexture2D();
				GlStateManager.enableBlend();
				float texScale = 32f;
				GlStateManager.scale(1/texScale, 1/texScale, 1/texScale);
				GlStateManager.color(1, 1, 1, 0.25f*MathHelper.clamp(1f, 0, 1));
				blueprint.draw(2*2f);
				GlStateManager.scale(texScale, texScale, texScale);
				GlStateManager.enableAlpha();
				GlStateManager.enableTexture2D();
				GlStateManager.enableCull();
			}
			else
				ClientUtils.mc().getRenderItem().renderItem(stack, TransformType.FIXED);

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
		nbt.checkSetBoolean("schematic", drawSchematic -> this.drawSchematic = drawSchematic);
	}


	@Override
	protected AMT renamedCopy(String newName)
	{
		AMTItem clone = new AMTItem(newName, originPos);
		clone.stack = stack;
		clone.stackInto = stackInto;
		clone.drawSchematic = drawSchematic;
		clone.drawStacked = drawStacked;
		return clone;
	}
}
