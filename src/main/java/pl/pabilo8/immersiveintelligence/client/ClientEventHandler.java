package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import blusunrize.immersiveengineering.api.tool.ZoomHandler.IZoomTool;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.FogMode;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GLContext;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Vehicles.Motorbike;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.camera.CameraHandler;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.api.utils.IEntityOverlayText;
import pl.pabilo8.immersiveintelligence.client.render.MachinegunRenderer;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMotorbike;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageEntityNBTSync;

import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 27-09-2019
 */
public class ClientEventHandler implements ISelectiveResourceReloadListener
{
	private static final String[] II_BULLET_TOOLTIP = {"\u00A0\u00A0II_BULLET_HERE\u00A0"};
	private static final String texture_gui = ImmersiveIntelligence.MODID+":textures/gui/hud_elements.png";
	private static boolean mgAiming = false;

	public static void drawMachinegunGui(EntityMachinegun mg, RenderGameOverlayEvent.Post event)
	{
		GlStateManager.pushMatrix();
		ClientUtils.bindTexture(texture_gui);
		int width = event.getResolution().getScaledWidth();
		int height = event.getResolution().getScaledHeight();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableAlpha();
		boolean drawWater = mg.tankCapacity > 0, hasShield = mg.maxShieldStrength > 0, hasSecondMag = mg.hasSecondMag;
		int bars = 1+(hasShield?1: 0)+(drawWater?1: 0);

		for(int i = 0; i < (hasSecondMag?2: 1); i += 1)
		{
			GlStateManager.pushMatrix();
			ClientUtils.drawTexturedRect(width-26-(18*bars), height-36-(i*18), 16, 16, 16/256f, 32/256f, 16/256f, 32/256f);
			for(int j = 0; j < bars+1; j += 1)
				ClientUtils.drawTexturedRect(width-16-(j*16), height-36-(i*18), 16, 16, 32/256f, 48/256f, 16/256f, 32/256f);

			if((i==0&&!mg.mag1Empty))
			{
				int tlen = ClientUtils.font().getStringWidth(String.valueOf(mg.bullets1));
				ClientUtils.font().drawString(String.valueOf(mg.bullets1), width-tlen-(18*bars), height-32-(i*18), 0xffffff, false);
				ClientUtils.mc().getRenderItem().renderItemAndEffectIntoGUI(mg.magazine1, width-44-(18*bars), height-36-(i*18));
			}
			else if((i==1&&!mg.mag2Empty))
			{
				int tlen = ClientUtils.font().getStringWidth(String.valueOf(mg.bullets2));
				ClientUtils.font().drawString(String.valueOf(mg.bullets2), width-tlen-(18*bars), height-32-(i*18), Lib.COLOUR_I_ImmersiveOrange, false);
				ClientUtils.mc().getRenderItem().renderItemAndEffectIntoGUI(mg.magazine2, width-44-(18*bars), height-36-(i*18));
			}

			GlStateManager.popMatrix();
			ClientUtils.bindTexture(texture_gui);
		}
		GlStateManager.enableBlend();

		ClientUtils.drawTexturedRect(width-19, height-18, 18, 18, 0/256f, 18/256f, 62/256f, 78/256f);
		ClientUtils.drawTexturedRect(width-16, height-64, 12, 46, 3/256f, 15/256f, 16/256f, 62/256f);
		ClientUtils.drawTexturedRect(width-18, height-17, 16, 16, 16/256f, 32/256f, 0, 16/256f);
		ClientUtils.drawGradientRect(width-14, height-19-(int)((mg.overheating/100f)*43), width-6, height-19, 0xffdf9916, 0x0fba0f0f);

		FluidStack fluid = mg.tank.getFluid();
		if(drawWater)
		{
			ClientUtils.drawTexturedRect(width-37, height-18, 18, 18, 0/256f, 18/256f, 62/256f, 78/256f);
			ClientUtils.drawTexturedRect(width-34, height-64, 12, 46, 3/256f, 15/256f, 16/256f, 62/256f);
			ClientUtils.drawTexturedRect(width-36, height-17, 16, 16, 0, 16/256f, 0, 16/256f);
			if(fluid!=null)
			{
				float hh = 43*((float)mg.tank.getFluidAmount()/(float)mg.tankCapacity);
				ClientUtils.drawRepeatedFluidSprite(fluid, width-32, height-62+(43-hh), 8, hh);

			}
			GlStateManager.enableBlend();
			GlStateManager.translate(-18, 0, 0);
		}

		if(hasShield)
		{
			ClientUtils.bindTexture(texture_gui);
			ClientUtils.drawTexturedRect(width-37, height-18, 18, 18, 0/256f, 18/256f, 62/256f, 78/256f);
			ClientUtils.drawTexturedRect(width-34, height-64, 12, 46, 3/256f, 15/256f, 16/256f, 62/256f);
			ClientUtils.drawTexturedRect(width-36, height-17, 16, 16, 32/256f, 48/256f, 0, 16/256f);
			ClientUtils.drawGradientRect(width-32, height-19-(int)((mg.shieldStrength/mg.maxShieldStrength)*43), width-24, height-19, 0xcfcfcfcf, 0x0cfcfcfc);
			GlStateManager.enableBlend();
		}

		if(drawWater)
			GlStateManager.translate(18, 0, 0);

		//ClientUtils.drawTexturedRect(width-17,height-17,16,16,0,16/256f,0,16/256f);

		GlStateManager.popMatrix();
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate)
	{
		if(resourcePredicate.test(VanillaResourceType.MODELS))
			EvenMoreImmersiveModelRegistry.instance.reloadRegisteredModels();
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

	@SubscribeEvent
	public void onFOVUpdate(FOVModifier event)
	{
		EntityPlayer player = ClientUtils.mc().player;
		if(player.getRidingEntity() instanceof EntityMachinegun&&mgAiming)
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
					ZoomHandler.fovZoom = event.getFOV();
				event.setFOV(ZoomHandler.fovZoom*event.getFOV());
			}
			ZoomHandler.isZooming = true;
		}

	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent.Post event)
	{
		RayTraceResult mop = ClientUtils.mc().objectMouseOver;
		EntityPlayer player = ClientUtils.mc().player;

		if(ClientUtils.mc().player!=null&&event.getType()==RenderGameOverlayEvent.ElementType.TEXT)
		{
			if(mop!=null&&mop.typeOfHit==Type.ENTITY&&mop.entityHit instanceof IEntityOverlayText)
			{
				boolean hammer = !player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()&&Utils.isHammer(player.getHeldItem(EnumHand.MAIN_HAND));

				IEntityOverlayText overlayBlock = (IEntityOverlayText)mop.entityHit;
				String[] text = overlayBlock.getOverlayText(ClientUtils.mc().player, mop, hammer);
				boolean useNixie = overlayBlock.useNixieFont(ClientUtils.mc().player, mop);
				if(text!=null&&text.length > 0)
				{
					FontRenderer font = useNixie?blusunrize.immersiveengineering.client.ClientProxy.nixieFontOptional: ClientUtils.font();
					int col = (useNixie&&IEConfig.nixietubeFont)?Lib.colour_nixieTubeText: 0xffffff;
					int i = 0;
					for(String s : text)
						if(s!=null)
							font.drawString(s, event.getResolution().getScaledWidth()/2+8, event.getResolution().getScaledHeight()/2+8+(i++)*font.FONT_HEIGHT, col, true);
				}

			}
			if(player.getRidingEntity() instanceof EntityMachinegun)
				drawMachinegunGui((EntityMachinegun)player.getRidingEntity(), event);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onRenderOverlayPre(RenderGameOverlayEvent.Pre event)
	{
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

		if(ClientUtils.mc().player.getRidingEntity() instanceof EntityMachinegun)
		{
			ZoomHandler.isZooming = ClientProxy.keybind_machinegunScope.isKeyDown();
			EntityMachinegun mg = (EntityMachinegun)ClientUtils.mc().player.getRidingEntity();

			if(ZoomHandler.isZooming^mgAiming)
			{
				mgAiming = !mgAiming;

				NBTTagCompound tag = new NBTTagCompound();
				tag.setBoolean("clientMessage", true);
				tag.setBoolean("aiming", mgAiming);
				IIPacketHandler.INSTANCE.sendToServer(new MessageEntityNBTSync(ClientUtils.mc().player.getRidingEntity(), tag));
			}

			if(ZoomHandler.isZooming)
			{
				ClientUtils.mc().gameSettings.thirdPersonView = 0;

				float px = (float)mg.posX, py = (float)mg.posY, pz = (float)mg.posZ;

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

				if(mg.gunPitch < true_head_angle2)
					pitch += ClientUtils.mc().getRenderPartialTicks();
				else if(mg.gunPitch > true_head_angle2)
					pitch -= ClientUtils.mc().getRenderPartialTicks();

				yaw = MathHelper.clamp(yaw, -45, 45);
				pitch = MathHelper.clamp(pitch, -20, 20);

				yaw += mg.recoilYaw;
				pitch += mg.recoilPitch;

				double true_angle = Math.toRadians(180-mg.setYaw-yaw);
				double true_angle2 = Math.toRadians(pitch);

				boolean hasScope = EntityMachinegun.scope.canZoom(mg.gun, null);

				Vec3d gun_end = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(2.25f-(hasScope?1.25f: 0), true_angle, true_angle2);
				Vec3d gun_height = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(0.25f+(hasScope?0.125f: 0f), true_angle, true_angle2+90);

				CameraHandler.INSTANCE.setCameraPos(px+(0.85*(gun_end.x+gun_height.x)), py-1.5f+0.4025+(0.85*(gun_end.y+gun_height.y)), pz+(0.85*(gun_end.z+gun_height.z)));
				CameraHandler.INSTANCE.setCameraAngle(mg.setYaw+yaw, pitch, 0);
				CameraHandler.INSTANCE.setEnabled(true);
			}
			else
				CameraHandler.INSTANCE.setEnabled(false);
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

		if(Motorbike.cameraRoll&&ClientUtils.mc().player.getRidingEntity() instanceof EntityMotorbike)
		{
			EntityMotorbike entity = (EntityMotorbike)ClientUtils.mc().player.getRidingEntity();
			float tilt = entity.tilt;
			if(entity.turnLeft)
				tilt -= event.getRenderPartialTicks()*0.1;
			else if(entity.turnRight)
				tilt += event.getRenderPartialTicks()*0.1;
			else if(tilt!=0)
			{
				tilt = (float)(tilt < 0?tilt+(event.getRenderPartialTicks()*0.1f): tilt-(event.getRenderPartialTicks()*0.1f));
				if(Math.abs(tilt) < 0.01f)
					tilt = 0;
			}

			event.setRoll((MathHelper.clamp(tilt, -1f, 1f)*15f));
		}

		if(CameraHandler.INSTANCE.isEnabled())
		{
			event.setRoll(CameraHandler.INSTANCE.getRoll());
		}
	}
}
