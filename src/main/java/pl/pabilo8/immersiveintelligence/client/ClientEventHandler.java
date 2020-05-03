package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import blusunrize.immersiveengineering.api.tool.ZoomHandler.IZoomTool;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.FogMode;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GLContext;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.camera.CameraHandler;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.client.render.MachinegunRenderer;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageMachinegunSync;

/**
 * Created by Pabilo8 on 27-09-2019.
 */
public class ClientEventHandler implements IResourceManagerReloadListener
{
	private static final String[] II_BULLET_TOOLTIP = {"\u00A0\u00A0II_BULLET_HERE\u00A0"};
	private static boolean mgAiming = false;

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		//TODO: Model Reloading
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void renderAdditionalBlockBounds(DrawBlockHighlightEvent event)
	{
		WorldClient world = ClientUtils.mc().world;
		Tessellator tessellator = Tessellator.getInstance();

		if(world!=null&&world.provider!=null)
			PenetrationRegistry.blockDamageClient.forEach(
					(dimensionBlockPos, aFloat) ->
					{
						if(dimensionBlockPos!=null&&dimensionBlockPos.dimension==world.provider.getDimension()&&aFloat!=null&&world.isBlockLoaded(dimensionBlockPos))
						{
							pl.pabilo8.immersiveintelligence.api.Utils.tesselateBlockBreak(tessellator, world, dimensionBlockPos, aFloat, event.getPartialTicks());
						}
					}
			);
	}

	@SubscribeEvent()
	public void onFogUpdate(EntityViewRenderEvent.RenderFogEvent event)
	{
		if(event.getEntity() instanceof EntityLivingBase&&((EntityLivingBase)event.getEntity()).getActivePotionEffect(IIPotions.suppression)!=null)
		{
			PotionEffect effect = ((EntityLivingBase)event.getEntity()).getActivePotionEffect(IIPotions.suppression);
			int timeLeft = effect.getDuration();
			int amplifier = effect.getAmplifier();
			if(amplifier < 0)
				amplifier = 254+amplifier;

			float f1 = MathHelper.clamp((float)amplifier/255f, 0f, 1f);
			//if(timeLeft < 20)
			//f1 += (event.getFarPlaneDistance()/4)*(1-timeLeft/20f);

			GlStateManager.setFog(FogMode.LINEAR);
			GlStateManager.setFogStart((float)Math.pow((1f-f1), 2)*12); //(
			GlStateManager.setFogEnd((float)Math.pow((1f-f1), 2)*16);
			GlStateManager.setFogDensity(.00625f+(.00625f*f1));

			if(GLContext.getCapabilities().GL_NV_fog_distance)
				GlStateManager.glFogi(34138, 34139);
		}
	}

	@SubscribeEvent()
	public void onFogColourUpdate(EntityViewRenderEvent.FogColors event)
	{
		if(event.getEntity() instanceof EntityLivingBase&&((EntityLivingBase)event.getEntity()).getActivePotionEffect(IIPotions.suppression)!=null)
		{
			event.setRed(0);
			event.setGreen(0);
			event.setBlue(0);
		}
	}

	@SubscribeEvent()
	public void onFOVUpdate(FOVUpdateEvent event)
	{
		EntityPlayer player = ClientUtils.mc().player;
		if(player.getRidingEntity() instanceof EntityMachinegun&&ZoomHandler.isZooming)
		{

				float[] steps = EntityMachinegun.scope.getZoomSteps(((EntityMachinegun)player.getRidingEntity()).gun, player);
				if(steps!=null&&steps.length > 0)
				{
					int curStep = -1;
					float dist = 0;
					for(int i = 0; i < steps.length; i++)
						if(curStep==-1||Math.abs(steps[i]-ZoomHandler.fovZoom) < dist)
						{
							curStep = i;
							dist = Math.abs(steps[i]-ZoomHandler.fovZoom);
						}
					if(curStep!=-1)
						ZoomHandler.fovZoom = steps[curStep];
					else
						ZoomHandler.fovZoom = event.getFov();
				}
			event.setNewfov(ZoomHandler.fovZoom);
			event.setCanceled(true);
		}

	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onRenderOverlayPre(RenderGameOverlayEvent.Pre event)
	{
		if(ClientUtils.mc().player.getRidingEntity() instanceof EntityMachinegun)
		{
			ZoomHandler.isZooming = ClientProxy.keybind_machinegunScope.isKeyDown();

			if(ZoomHandler.isZooming^mgAiming)
			{
				mgAiming = !mgAiming;

				NBTTagCompound tag = new NBTTagCompound();
				tag.setBoolean("clientMessage", true);
				tag.setBoolean("aiming", mgAiming);
				IIPacketHandler.INSTANCE.sendToServer(new MessageMachinegunSync(ClientUtils.mc().player.getRidingEntity(), tag));
			}

			if(ZoomHandler.isZooming)
			{
				ClientUtils.mc().gameSettings.thirdPersonView = 0;
				EntityMachinegun mg = (EntityMachinegun)ClientUtils.mc().player.getRidingEntity();

				float px = mg.getPosition().getX(), py = mg.getPosition().getY(), pz = mg.getPosition().getZ();

				float yaw = mg.gunYaw;
				float pitch = mg.gunPitch;

				EntityLivingBase psg = (EntityLivingBase)mg.getPassengers().get(0);
				float true_head_angle = MathHelper.wrapDegrees(psg.prevRotationYawHead-mg.setYaw);
				float true_head_angle2 = MathHelper.wrapDegrees(psg.rotationPitch);

				if(mg.gunYaw < true_head_angle)
					yaw += ClientUtils.mc().getRenderPartialTicks()*2f;
				else if(mg.gunYaw > true_head_angle)
					yaw -= ClientUtils.mc().getRenderPartialTicks()*2f;

				if(Math.ceil(mg.gunYaw) <= Math.ceil(true_head_angle)+1f&&Math.ceil(mg.gunYaw) >= Math.ceil(true_head_angle)-1f)
					yaw = true_head_angle;

				yaw += mg.recoilYaw;

				if(mg.gunPitch < true_head_angle2)
					pitch += ClientUtils.mc().getRenderPartialTicks();
				else if(mg.gunPitch > true_head_angle2)
					pitch -= ClientUtils.mc().getRenderPartialTicks();

				if(Math.ceil(mg.gunPitch) <= Math.ceil(true_head_angle2)+1f&&Math.ceil(mg.gunPitch) >= Math.ceil(true_head_angle2)-1f)
					pitch = true_head_angle2;

				pitch += mg.recoilPitch;

				double true_angle = Math.toRadians(180-mg.setYaw-yaw);
				double true_angle2 = Math.toRadians(pitch);

				boolean hasScope = EntityMachinegun.scope.canZoom(mg.gun, null);

				Vec3d gun_end = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(2.25f-(hasScope?1.25f: 0), true_angle, true_angle2);
				Vec3d gun_height = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(0.25f+(hasScope?0.125f: 0f), true_angle, true_angle2+90);

				CameraHandler.INSTANCE.setCameraPos(px+0.5+(0.85*(gun_end.x+gun_height.x)), py-1.5f+0.4025+(0.85*(gun_end.y+gun_height.y)), pz+0.5+(0.85*(gun_end.z+gun_height.z)));
				CameraHandler.INSTANCE.setCameraAngle(mg.setYaw+yaw, pitch, 0);
				CameraHandler.INSTANCE.setEnabled(true);
			}
			else
				CameraHandler.INSTANCE.setEnabled(false);
		}

		dothing:
		if(ZoomHandler.isZooming&&event.getType()==RenderGameOverlayEvent.ElementType.CROSSHAIRS)
		{
			ItemStack stack_m = ClientUtils.mc().player.getHeldItem(EnumHand.MAIN_HAND);
			ItemStack stack_o = ClientUtils.mc().player.getHeldItem(EnumHand.OFF_HAND);

			if(ZoomHandler.isZooming&&(ClientUtils.mc().player.getRidingEntity() instanceof EntityMachinegun||(stack_m.getItem() instanceof IAdvancedZoomTool||stack_o.getItem() instanceof IAdvancedZoomTool)))
			{
				event.setCanceled(true);

				ItemStack stack = stack_m.getItem() instanceof IAdvancedZoomTool?stack_m: stack_o;

				if(ClientUtils.mc().player.getRidingEntity() instanceof EntityMachinegun)
				{
					if(!EntityMachinegun.scope.canZoom(((EntityMachinegun)ClientUtils.mc().player.getRidingEntity()).gun, ClientUtils.mc().player))
					{
						ZoomHandler.isZooming = false;
						break dothing;
					}
					ClientUtils.bindTexture(EntityMachinegun.scope.getZoomOverlayTexture(((EntityMachinegun)ClientUtils.mc().player.getRidingEntity()).gun, ClientUtils.mc().player));
				}
				else
					ClientUtils.bindTexture(((IAdvancedZoomTool)stack.getItem()).getZoomOverlayTexture(stack, ClientUtils.mc().player));


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
				if(!equipped.isEmpty()&&equipped.getItem() instanceof IZoomTool||(ClientUtils.mc().player.getRidingEntity() instanceof EntityMachinegun))
				{
					IZoomTool tool;
					if(ClientUtils.mc().player.getRidingEntity() instanceof EntityMachinegun)
						tool = EntityMachinegun.scope;
					else
						tool = (IZoomTool)equipped.getItem();
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

	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event)
	{
		if(event.getItemStack().getItem() instanceof ItemIIBulletMagazine)
		{
			if(ItemNBTHelper.hasKey(event.getItemStack(), "bullets"))
			{
				if(ItemNBTHelper.getTag(event.getItemStack()).hasKey("bullet0"))
					event.getToolTip().add("   "+new ItemStack(ItemNBTHelper.getTagCompound(event.getItemStack(), "bullet0")).getDisplayName());
				if(ItemNBTHelper.getTag(event.getItemStack()).hasKey("bullet1"))
					event.getToolTip().add("   "+new ItemStack(ItemNBTHelper.getTagCompound(event.getItemStack(), "bullet1")).getDisplayName());
				if(ItemNBTHelper.getTag(event.getItemStack()).hasKey("bullet2"))
					event.getToolTip().add("   "+new ItemStack(ItemNBTHelper.getTagCompound(event.getItemStack(), "bullet2")).getDisplayName());
				if(ItemNBTHelper.getTag(event.getItemStack()).hasKey("bullet3"))
					event.getToolTip().add("   "+new ItemStack(ItemNBTHelper.getTagCompound(event.getItemStack(), "bullet3")).getDisplayName());
			}
		}
	}

	@SubscribeEvent()
	public void onRenderTooltip(RenderTooltipEvent.PostText event)
	{
		ItemStack stack = event.getStack();
		if(stack.getItem() instanceof ItemIIBulletMagazine)
		{

			int line = event.getLines().size()-Utils.findSequenceInList(event.getLines(), II_BULLET_TOOLTIP, (s, s2) -> s.equals(s2.substring(2)));

			if(line==-1)
				return;

			int currentX = event.getX();
			int currentY = event.getY()+10;

			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.enableRescaleNormal();
			GlStateManager.translate(currentX, currentY, 700);
			GlStateManager.scale(.5f, .5f, 1);


			MachinegunRenderer.drawBulletsList(stack);

			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();

		}
	}

	@SubscribeEvent
	public void cameraSetup(EntityViewRenderEvent.CameraSetup event)
	{
		if(CameraHandler.INSTANCE.isEnabled())
		{
			event.setRoll(CameraHandler.INSTANCE.getRoll());
		}
	}
}
