package pl.pabilo8.immersiveintelligence.client.animation;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

/**
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

	@Override
	protected void draw(Tessellator tes, BufferBuilder buf)
	{
		if(stack!=null)
		{
			//GlStateManager.scale(0.0625,0.0625,0.0625);
			GlStateManager.translate(originPos.x,originPos.y,originPos.z);
			ClientUtils.mc().getRenderItem().renderItem(stack, TransformType.NONE);
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
