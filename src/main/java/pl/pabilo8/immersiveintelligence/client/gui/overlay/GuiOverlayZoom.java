package pl.pabilo8.immersiveintelligence.client.gui.overlay;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.CameraHandler;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;

/**
 * @author Pabilo8
 * @since 13.09.2022
 */
public class GuiOverlayZoom extends GuiOverlayBase
{
	@Override
	public boolean shouldDraw(EntityPlayer player, RayTraceResult mouseOver)
	{
		return CameraHandler.zoom!=null;
	}

	@Override
	public void draw(EntityPlayer player, RayTraceResult mouseOver, int width, int height)
	{
		//Begin drawing
		GlStateManager.pushMatrix();
		int resMin = Math.min(width, height);

		drawReticle(width, height, resMin, player);

		//Draw zoom steps bar
		if(CameraHandler.zoom.getZoomSteps(CameraHandler.stack, player).length > 1)
			drawZoomStepsBar(width, height, resMin, player);

		GlStateManager.popMatrix();
	}

	private void drawZoomStepsBar(int width, int height, int resMin, EntityPlayer player)
	{
		float[] steps = CameraHandler.zoom.getZoomSteps(CameraHandler.stack, player);
		assert steps!=null&&steps.length >= 1;

		//Start drawing
		IIClientUtils.bindTexture(TEXTURE_IE_HUD);
		IIDrawUtils draw = IIDrawUtils.startTextured()
				.setOffset(100/256f*resMin,0)
				.drawTexRect(218/256f*resMin, 64/256f*resMin, 24/256f*resMin, 128/256f*resMin, 64/256f, 88/256f, 96/256f, 224/256f);

		int curStep = -1;
		float dist = 0;
		float stepLength = 118/(float)steps.length;
		float stepOffset = (stepLength-7)/2f;

		//Offset to the zoom bar
		draw.addOffset(223/256f*resMin, (64/256f*resMin)+(5+stepOffset)/256*resMin);

		for(int i = 0; i < steps.length; i++)
		{
			//Draw zoom level markers
			draw.drawTexRect(0, i*(stepLength/256*resMin), 8/256f*resMin, 7/256f*resMin, 88/256f, 96/256f, 96/256f, 103/256f);

			if(curStep==-1||Math.abs(steps[i]-CameraHandler.fovZoom) < dist)
			{
				curStep = i;
				dist = Math.abs(steps[i]-CameraHandler.fovZoom);
			}
		}

		draw.addOffset(6/256f*resMin, curStep*stepLength/256*resMin);
		//Offset to the zoom bar
		draw.drawTexRect(0, 0, 8/256f*resMin, 7/256f*resMin, 88/256f, 98/256f, 103/256f, 110/256f);
		draw.finish();

		//Draw magnification amount
		ClientUtils.font().drawString(1/steps[curStep]+"x", (int)(resMin*1.385f), (int)(100/256f*resMin), 0xffffff);

	}

	private void drawReticle(int width, int height, int resMin, EntityPlayer player)
	{
		IIClientUtils.bindTexture(CameraHandler.zoom.getZoomOverlayTexture(CameraHandler.stack, player));
		float progress = CameraHandler.zoom.getZoomProgress(CameraHandler.stack, player);
		float centerX = (width-resMin)/2f, centerY = (height-resMin)/2f;

		IIDrawUtils draw = IIDrawUtils.startTexturedColored();
		GlStateManager.enableBlend();

		//Draw background
		if(resMin==width)
		{
			draw.drawTexColorRect(0, 0, width, (int)centerY+1, 0, 0, 0, progress, 0, 0, 0, 0);
			draw.drawTexColorRect(0, (int)centerY+resMin, width, (int)centerY+1, 0, 0, 0, progress, 0, 0, 0, 0);
		}
		else
		{
			draw.drawTexColorRect(0, 0, (int)centerX+1, height, 0, 0, 0, progress, 0, 0, 0, 0);
			draw.drawTexColorRect((int)centerX+resMin, 0, (int)centerX+1, height, 0, 0, 0, progress, 0, 0, 0, 0);
		}

		//Draw central reticle
		draw.drawTexColorRect(centerX, centerY, resMin, resMin, 1, 1, 1, progress, 0f, 1f, 0f, 1f);
		draw.finish();

	}
}
