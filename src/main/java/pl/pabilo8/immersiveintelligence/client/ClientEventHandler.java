package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import blusunrize.immersiveengineering.api.tool.ZoomHandler.IZoomTool;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.pabilo8.immersiveintelligence.api.IAdvancedZoomTool;

/**
 * Created by Pabilo8 on 27-09-2019.
 */
public class ClientEventHandler implements IResourceManagerReloadListener
{
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		//TODO: Model Reloading
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onRenderOverlayPre(RenderGameOverlayEvent.Pre event)
	{
		if(ZoomHandler.isZooming&&event.getType()==RenderGameOverlayEvent.ElementType.CROSSHAIRS)
		{
			ItemStack stack_m = Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND);
			ItemStack stack_o = Minecraft.getMinecraft().player.getHeldItem(EnumHand.OFF_HAND);

			if(ZoomHandler.isZooming&&(stack_m.getItem() instanceof IAdvancedZoomTool||stack_o.getItem() instanceof IAdvancedZoomTool))
			{
				event.setCanceled(true);

				ItemStack stack = stack_m.getItem() instanceof IAdvancedZoomTool?stack_m: stack_o;
				ClientUtils.bindTexture(((IAdvancedZoomTool)stack.getItem()).getZoomOverlayTexture(stack, Minecraft.getMinecraft().player));
				int width = event.getResolution().getScaledWidth();
				int height = event.getResolution().getScaledHeight();
				int resMin = Math.min(width, height);
				float offsetX = (width-resMin)/2f;
				float offsetY = (height-resMin)/2f;

				if(resMin==width)
				{
					ClientUtils.drawColouredRect(0, 0, width, (int)offsetY+1, 0xff000000);
					ClientUtils.drawColouredRect(0, (int)offsetY+resMin, width, (int)offsetY+1, 0xff000000);
				}
				else
				{
					ClientUtils.drawColouredRect(0, 0, (int)offsetX+1, height, 0xff000000);
					ClientUtils.drawColouredRect((int)offsetX+resMin, 0, (int)offsetX+1, height, 0xff000000);
				}
				GlStateManager.enableBlend();
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);

				GlStateManager.translate(offsetX, offsetY, 0);
				ClientUtils.drawTexturedRect(0, 0, resMin, resMin, 0f, 1f, 0f, 1f);

				ClientUtils.bindTexture("immersiveengineering:textures/gui/hud_elements.png");
				ClientUtils.drawTexturedRect(218/256f*resMin, 64/256f*resMin, 24/256f*resMin, 128/256f*resMin, 64/256f, 88/256f, 96/256f, 224/256f);
				ItemStack equipped = ClientUtils.mc().player.getHeldItem(EnumHand.MAIN_HAND);
				if(!equipped.isEmpty()&&equipped.getItem() instanceof IZoomTool)
				{
					IZoomTool tool = (IZoomTool)equipped.getItem();
					float[] steps = tool.getZoomSteps(equipped, ClientUtils.mc().player);
					if(steps!=null&&steps.length > 1)
					{
						int curStep = -1;
						float dist = 0;

						float totalOffset = 0;
						float stepLength = 118/(float)steps.length;
						float stepOffset = (stepLength-7)/2f;
						GlStateManager.translate(223/256f*resMin, 64/256f*resMin, 0);
						GlStateManager.translate(0, (5+stepOffset)/256*resMin, 0);
						for(int i = 0; i < steps.length; i++)
						{
							ClientUtils.drawTexturedRect(0, 0, 8/256f*resMin, 7/256f*resMin, 88/256f, 96/256f, 96/256f, 103/256f);
							GlStateManager.translate(0, stepLength/256*resMin, 0);
							totalOffset += stepLength;

							if(curStep==-1||Math.abs(steps[i]-ZoomHandler.fovZoom) < dist)
							{
								curStep = i;
								dist = Math.abs(steps[i]-ZoomHandler.fovZoom);
							}
						}
						GlStateManager.translate(0, -totalOffset/256*resMin, 0);

						if(curStep >= 0&&curStep < steps.length)
						{
							GlStateManager.translate(6/256f*resMin, curStep*stepLength/256*resMin, 0);
							ClientUtils.drawTexturedRect(0, 0, 8/256f*resMin, 7/256f*resMin, 88/256f, 98/256f, 103/256f, 110/256f);
							ClientUtils.font().drawString((1/steps[curStep])+"x", (int)(16/256f*resMin), 0, 0xffffff);
							GlStateManager.translate(-6/256f*resMin, -curStep*stepLength/256*resMin, 0);
						}
						GlStateManager.translate(0, -((5+stepOffset)/256*resMin), 0);
						GlStateManager.translate(-223/256f*resMin, -64/256f*resMin, 0);
					}
				}

				GlStateManager.translate(-offsetX, -offsetY, 0);
			}
		}
	}
}
