package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import blusunrize.immersiveengineering.api.tool.ZoomHandler.IZoomTool;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.network.MessageRequestBlockUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.FogMode;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.opengl.GLContext;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Vehicles.Motorbike;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.Submachinegun;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.camera.CameraHandler;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.api.utils.IEntityOverlayText;
import pl.pabilo8.immersiveintelligence.api.utils.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.client.render.MachinegunRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.BinocularsRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.MineDetectorRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.SubmachinegunItemStackRenderer;
import pl.pabilo8.immersiveintelligence.client.tmt.TmtUtil;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMotorbike;
import pl.pabilo8.immersiveintelligence.common.entity.EntityTripodPeriscope;
import pl.pabilo8.immersiveintelligence.common.entity.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIINavalMine;
import pl.pabilo8.immersiveintelligence.common.items.tools.ItemIIBinoculars;
import pl.pabilo8.immersiveintelligence.common.items.tools.ItemIIMineDetector;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIMachinegun;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIISubmachinegun;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageEntityNBTSync;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 27-09-2019
 */
public class ClientEventHandler implements ISelectiveResourceReloadListener
{
	private static final String[] II_BULLET_TOOLTIP = {"\u00A0\u00A0II\u00A0", "\u00A0\u00A0AMMO\u00A0", "\u00A0\u00A0HERE\u00A0", "\u00A0\u00A0--\u00A0"};
	private static final String texture_gui = ImmersiveIntelligence.MODID+":textures/gui/hud_elements.png";
	private static boolean mgAiming = false;
	public static ArrayList<EntityPlayer> aimingPlayers = new ArrayList<>();

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
					(damageBlockPos) ->
					{
						if(damageBlockPos!=null&&damageBlockPos.dimension==world.provider.getDimension()&&damageBlockPos.damage > 0&&world.isBlockLoaded(damageBlockPos))
						{
							pl.pabilo8.immersiveintelligence.api.Utils.tesselateBlockBreak(tessellator, world, damageBlockPos, event.getPartialTicks());
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
		if(event.getEntity() instanceof EntityLivingBase&&((EntityLivingBase)event.getEntity()).isPotionActive(IIPotions.infrared_vision))
		{
			float r = event.getRed(), g = event.getGreen(), b = event.getBlue();
			float f15 = Math.min(Objects.requireNonNull(((EntityLivingBase)event.getEntity()).getActivePotionEffect(IIPotions.infrared_vision)).getAmplifier(), 4)/4f;
			float f6 = 1.0F/event.getRed();

			if(f6 > 1.0F/event.getGreen())
			{
				f6 = 1.0F/event.getGreen();
			}

			if(f6 > 1.0F/event.getBlue())
			{
				f6 = 1.0F/event.getBlue();
			}

			// Forge: fix MC-4647 and MC-10480
			if(Float.isInfinite(f6)) f6 = Math.nextAfter(f6, 0.0);

			event.setRed(r*(1.0F-f15)+r*f6*f15);
			event.setGreen(g*(1.0F-f15)+g*f6*f15);
			event.setBlue(b*(1.0F-f15)+b*f6*f15);
		}
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
		if(player.getRidingEntity() instanceof IEntityZoomProvider&&mgAiming)
		{
			IEntityZoomProvider ridingEntity = (IEntityZoomProvider)player.getRidingEntity();

			float[] steps = ridingEntity.getZoom().getZoomSteps(ridingEntity.getZoomStack(), player);
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
			if(mop!=null)
				if(mop.typeOfHit==Type.BLOCK&&!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()&&pl.pabilo8.immersiveintelligence.api.Utils.isTachometer(player.getHeldItem(EnumHand.MAIN_HAND)))
				{
					int col = IEConfig.nixietubeFont?Lib.colour_nixieTubeText: 0xffffff;
					String[] text = null;
					TileEntity tileEntity = player.world.getTileEntity(mop.getBlockPos());

					if(tileEntity!=null&&tileEntity.hasCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, mop.sideHit.getOpposite()))
					{
						IRotaryEnergy energy = tileEntity.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, mop.sideHit.getOpposite());

						if(energy!=null)
						{
							float int_torque = energy.getTorque();
							float ext_torque = energy.getOutputRotationSpeed();
							float int_speed = energy.getRotationSpeed();
							float ext_speed = energy.getOutputRotationSpeed();
							if(int_torque!=ext_torque&&int_speed!=ext_speed)
								text = new String[]{
										I18n.format(CommonProxy.INFO_KEY+"tachometer.internal_torque", int_torque),
										I18n.format(CommonProxy.INFO_KEY+"tachometer.internal_speed", int_speed),
										I18n.format(CommonProxy.INFO_KEY+"tachometer.external_torque", ext_torque),
										I18n.format(CommonProxy.INFO_KEY+"tachometer.external_speed", ext_speed)
								};
							else
								text = new String[]{
										I18n.format(CommonProxy.INFO_KEY+"tachometer.torque", int_torque),
										I18n.format(CommonProxy.INFO_KEY+"tachometer.speed", int_speed)
								};
						}
					}

					//here add new block types

					if(text!=null)
					{
						if(player.world.getTotalWorldTime()%20==0)
						{
							ImmersiveEngineering.packetHandler.sendToServer(new MessageRequestBlockUpdate(mop.getBlockPos()));
						}
						int i = 0;
						for(String s : text)
							if(s!=null)
								ClientUtils.font().drawString(s, event.getResolution().getScaledWidth()/2+8, event.getResolution().getScaledHeight()/2+8+(i++)*ClientUtils.font().FONT_HEIGHT, col, true);
					}
				}
				else if(mop.typeOfHit==Type.ENTITY&&mop.entityHit instanceof IEntityOverlayText)
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
			if(ZoomHandler.isZooming&&player.getRidingEntity() instanceof EntityTripodPeriscope)
				drawTripodGui(((EntityTripodPeriscope)player.getRidingEntity()), event);
		}
	}

	private void drawTripodGui(EntityTripodPeriscope ridingEntity, RenderGameOverlayEvent.Post event)
	{
		ClientUtils.font().drawString(I18n.format(CommonProxy.INFO_KEY+"yaw", CameraHandler.INSTANCE.getYaw()),
				event.getResolution().getScaledWidth()/2+8, event.getResolution().getScaledHeight()/2+8, 0xffffff, true);
		ClientUtils.font().drawString(I18n.format(CommonProxy.INFO_KEY+"pitch", CameraHandler.INSTANCE.getPitch()),
				event.getResolution().getScaledWidth()/2+8, event.getResolution().getScaledHeight()/2+16, 0xffffff, true);

		RayTraceResult traceResult = CameraHandler.INSTANCE.rayTrace(90, event.getPartialTicks());
		BlockPos pos = ClientUtils.mc().player.getPosition();

		ClientUtils.font().drawString(I18n.format(CommonProxy.INFO_KEY+"distance", (traceResult==null||traceResult.typeOfHit==Type.MISS)?I18n.format(CommonProxy.INFO_KEY+"distance_unknown"): traceResult.getBlockPos().getDistance(pos.getX(), pos.getY(), pos.getZ())),
				event.getResolution().getScaledWidth()/2+8, event.getResolution().getScaledHeight()/2+24, 0xffffff, true);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onRenderOverlayPre(RenderGameOverlayEvent.Pre event)
	{
		ItemStack stack_m = ClientUtils.mc().player.getHeldItem(EnumHand.MAIN_HAND);
		ItemStack stack_o = ClientUtils.mc().player.getHeldItem(EnumHand.OFF_HAND);

		if(event.getType()==RenderGameOverlayEvent.ElementType.CROSSHAIRS&&stack_m.getItem() instanceof ItemIISubmachinegun&&ItemNBTHelper.getInt(stack_m, "aiming") > 0)
		{
			event.setCanceled(true);
			return;
		}

		Entity ridingEntity = ClientUtils.mc().player.getRidingEntity();

		dothing:
		if(ZoomHandler.isZooming&&event.getType()==RenderGameOverlayEvent.ElementType.CROSSHAIRS)
		{
			if(ZoomHandler.isZooming&&(ridingEntity instanceof IEntityZoomProvider||(stack_m.getItem() instanceof IAdvancedZoomTool||stack_o.getItem() instanceof IAdvancedZoomTool)))
			{
				event.setCanceled(true);

				ItemStack stack = stack_m.getItem() instanceof IAdvancedZoomTool?stack_m: stack_o;

				if(ridingEntity instanceof IEntityZoomProvider)
				{
					IEntityZoomProvider entityZoomProvider = ((IEntityZoomProvider)ridingEntity);
					if(!entityZoomProvider.getZoom().canZoom(entityZoomProvider.getZoomStack(), ClientUtils.mc().player))
					{
						ZoomHandler.isZooming = false;
						break dothing;
					}
					ClientUtils.bindTexture(entityZoomProvider.getZoom().getZoomOverlayTexture(entityZoomProvider.getZoomStack(), ClientUtils.mc().player));
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
				if(!equipped.isEmpty()&&equipped.getItem() instanceof IZoomTool||(ridingEntity instanceof IEntityZoomProvider))
				{
					IZoomTool tool;
					if(ridingEntity instanceof IEntityZoomProvider)
						tool = ((IEntityZoomProvider)ridingEntity).getZoom();
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

		if(ridingEntity instanceof IEntityZoomProvider)
		{
			ZoomHandler.isZooming = ClientProxy.keybind_zoom.isKeyDown();

			if(ZoomHandler.isZooming^mgAiming)
			{
				mgAiming = !mgAiming;

				if(ridingEntity instanceof EntityMachinegun)
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setBoolean("clientMessage", true);
					tag.setBoolean("aiming", mgAiming);
					IIPacketHandler.INSTANCE.sendToServer(new MessageEntityNBTSync(ridingEntity, tag));
				}
			}

			if(ZoomHandler.isZooming)
			{
				ClientUtils.mc().gameSettings.thirdPersonView = 0;


				if(ridingEntity instanceof EntityMachinegun)
				{
					EntityMachinegun mg = (EntityMachinegun)ridingEntity;
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

					yaw = mg.tripod?MathHelper.clamp(yaw, -82.5F, 82.5F): MathHelper.clamp(yaw, -45.0F, 45.0F);
					pitch = MathHelper.clamp(pitch, -20, 20);

					yaw += mg.recoilYaw;
					pitch += mg.recoilPitch;

					double true_angle = Math.toRadians(180-mg.setYaw-yaw);
					double true_angle2 = Math.toRadians(pitch);

					boolean hasScope = mg.getZoom().canZoom(mg.gun, null);

					Vec3d gun_end = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(2.25f-(hasScope?1.25f: 0), true_angle, true_angle2);
					Vec3d gun_height = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(0.25f+(hasScope?0.125f: 0f), true_angle, true_angle2+90);

					CameraHandler.INSTANCE.setCameraPos(px+(0.85*(gun_end.x+gun_height.x)), py-1.5f+0.4025+(0.85*(gun_end.y+gun_height.y)), pz+(0.85*(gun_end.z+gun_height.z)));
					CameraHandler.INSTANCE.setCameraAngle(mg.setYaw+yaw, pitch, 0);
					CameraHandler.INSTANCE.setEnabled(true);
				}
				else if(ridingEntity instanceof EntityTripodPeriscope)
				{
					EntityTripodPeriscope mg = (EntityTripodPeriscope)ridingEntity;
					float px = (float)mg.posX, py = (float)mg.posY, pz = (float)mg.posZ;

					CameraHandler.INSTANCE.setCameraPos(px, py+1.25, pz);
					ClientUtils.mc().player.rotationPitch = MathHelper.clamp(ClientUtils.mc().player.rotationPitch, -50, 50);
					ClientUtils.mc().player.prevRotationPitch = MathHelper.clamp(ClientUtils.mc().player.prevRotationPitch, -50, 50);
					CameraHandler.INSTANCE.setCameraAngle(mg.periscopeYaw, ClientUtils.mc().player.rotationPitch, 0);
					CameraHandler.INSTANCE.setEnabled(true);
				}

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

			int currentX = event.getX();
			int currentY = event.getY();
			for(int i = 0; i < event.getLines().size(); i += 1)
				if(event.getLines().get(i).contains("   "))
				{
					currentY += i*10;
					break;
				}

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
		if(Minecraft.getMinecraft().gameSettings.thirdPersonView==0)
		{
			ItemStack stack = ClientUtils.mc().player.getHeldItemMainhand();
			if(Submachinegun.cameraRecoil&&stack.getItem() instanceof ItemIISubmachinegun)
			{
				event.setPitch(event.getPitch()-ItemNBTHelper.getFloat(stack, "recoilV"));
				event.setYaw(event.getYaw()+ItemNBTHelper.getFloat(stack, "recoilH"));
			}
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
		}

		if(CameraHandler.INSTANCE.isEnabled())
		{
			event.setRoll(CameraHandler.INSTANCE.getRoll());
		}
	}

	public static void handleBipedRotations(ModelBiped model, Entity entity)
	{
		if(!Config.IEConfig.fancyItemHolding)
			return;

		model.bipedHead.rotateAngleZ = 0f;
		model.bipedHeadwear.rotateAngleZ = 0f;

		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase player = (EntityLivingBase)entity;

			// TODO: 26.09.2020 Find a better way, or make an animation API!
			if(aimingPlayers.contains(entity))
			{
				model.bipedHead.rotateAngleZ = -0.35f;
				model.bipedHeadwear.rotateAngleZ = -0.35f;
			}

			Entity ridingEntity = player.getRidingEntity();
			if(ridingEntity instanceof EntityMachinegun)
			{
				EntityMachinegun mg = ((EntityMachinegun)ridingEntity);

				float ff = (float)(-1.35f-(Math.toRadians(mg.gunPitch))*1.25);
				float true_head_angle = MathHelper.wrapDegrees(player.prevRotationYawHead-mg.setYaw);

				float partialTicks = ClientUtils.mc().getRenderPartialTicks();
				float wtime;

				ridingEntity.applyOrientationToEntity(player);
				model.bipedHead.rotateAngleX *= -0.35f;
				model.bipedHeadwear.rotateAngleX *= -0.35f;
				model.bipedLeftArm.rotateAngleY = .08726f+(3.14f/6f);

				if(player.world.getBlockState(player.getPosition()).getMaterial().isSolid())
				{
					if(Math.abs(mg.gunYaw-true_head_angle) > 5)
					{
						wtime = (Math.abs((((entity.getEntityWorld().getTotalWorldTime()+partialTicks)%20)/20f)-0.5f)/0.5f);
						wtime *= 0.25f;

						if(mg.setupTime > 0)
							wtime = 0;
						if(mg.gunYaw < true_head_angle)
						{
							model.bipedRightLeg.rotateAngleY = -(wtime)*2f;
							model.bipedLeftLeg.rotateAngleY = (wtime)*2f;
						}
						else if(mg.gunYaw > true_head_angle)
						{
							model.bipedRightLeg.rotateAngleY = -(wtime)*2f;
							model.bipedLeftLeg.rotateAngleY = (wtime)*2f;
						}
					}

					model.bipedBody.rotateAngleX += 1.5f;
					model.bipedRightLeg.rotateAngleX += 1.5f;
					model.bipedLeftLeg.rotateAngleX += 1.5f;

					model.bipedRightArm.rotateAngleX += ff-0.5;
					model.bipedLeftArm.rotateAngleX += ff-0.5;

					model.bipedRightLeg.rotationPointY = 0f;
					model.bipedLeftLeg.rotationPointY = 0f;

					model.bipedRightLeg.rotationPointZ = 12f;
					model.bipedLeftLeg.rotationPointZ = 12f;

					float maxRotation = mg.tripod?82.5F: 45.0F;

					model.bipedRightLeg.rotateAngleY += (mg.gunYaw/maxRotation);
					model.bipedLeftLeg.rotateAngleY += (mg.gunYaw/maxRotation);

					//model.bipedLeftLeg.rotateAngleY += ff+1f;

				}
				else
				{
					wtime = (Math.abs((((entity.getEntityWorld().getTotalWorldTime()+partialTicks)%40)/40f)-0.5f)/0.5f)-0.5f;
					wtime *= 0.65f;
					if(mg.setupTime > 0)
						wtime = 0;
					model.bipedBody.rotateAngleX -= 0.0625f;
					if(Math.abs(mg.gunYaw-true_head_angle) > 5)
						if(mg.gunYaw < true_head_angle)
						{
							model.bipedRightLeg.rotateAngleX = (wtime)*2f;
							model.bipedLeftLeg.rotateAngleX = -(wtime)*2f;
						}
						else if(mg.gunYaw > true_head_angle)
						{
							model.bipedRightLeg.rotateAngleX = -(wtime)*2f;
							model.bipedLeftLeg.rotateAngleX = (wtime)*2f;
						}

					model.bipedRightArm.rotateAngleX = ff;
					model.bipedLeftArm.rotateAngleX = ff;
				}


				//model.bipedRightArm.rotateAngleY = -.08726f+model.bipedHead.rotateAngleY;
			}
			else if(ridingEntity instanceof EntityTripodPeriscope)
			{
				EntityTripodPeriscope mg = ((EntityTripodPeriscope)ridingEntity);
				float true_head_angle = MathHelper.wrapDegrees(player.prevRotationYawHead);

				float partialTicks = ClientUtils.mc().getRenderPartialTicks();
				float wtime = (Math.abs((((entity.getEntityWorld().getTotalWorldTime()+partialTicks)%40)/40f)-0.5f)/0.5f)-0.5f;
				wtime *= 0.65f;

				model.bipedRightArm.rotateAngleX = -1.75f;
				model.bipedRightArm.rotateAngleY = -0.5f;

				model.bipedLeftArm.rotateAngleX = -2.25f;
				model.bipedLeftArm.rotateAngleY = 0.25f;

				model.bipedHead.rotateAngleX = 0;
				model.bipedHeadwear.rotateAngleX = 0;
				model.bipedHead.rotateAngleY = 0;
				model.bipedHeadwear.rotateAngleY = 0;
				if(Math.abs(mg.periscopeYaw-true_head_angle) > 5)
					if(mg.periscopeYaw < true_head_angle)
					{
						model.bipedRightLeg.rotateAngleZ = (wtime)*2f;
						model.bipedLeftLeg.rotateAngleZ = -(wtime)*2f;
					}
					else if(mg.periscopeYaw > true_head_angle)
					{
						model.bipedRightLeg.rotateAngleZ = -(wtime)*2f;
						model.bipedLeftLeg.rotateAngleZ = (wtime)*2f;
					}

			}
			else if(ridingEntity instanceof EntityVehicleSeat)
			{
				EntityVehicleSeat seat = ((EntityVehicleSeat)ridingEntity);

				if(player.getLowestRidingEntity() instanceof EntityMotorbike)
				{
					if(seat.seatID==0)
					{
						model.bipedRightArm.rotateAngleX = -1.5f;
						model.bipedRightArm.rotateAngleY = 0.5f;
						model.bipedLeftArm.rotateAngleX = -1.5f;
						model.bipedLeftArm.rotateAngleY = -0.5f;
					}

					model.bipedRightLeg.rotateAngleY = 0.65f;
					model.bipedLeftLeg.rotateAngleY = -0.65f;
					model.bipedRightLeg.rotateAngleX = -0.65f;
					model.bipedLeftLeg.rotateAngleX = -0.65f;

				}
				else
				{

				}

			}
			else
				for(EnumHand hand : EnumHand.values())
				{
					ItemStack heldItem = player.getHeldItem(hand);
					if(!heldItem.isEmpty())
					{
						boolean right = (hand==EnumHand.MAIN_HAND)==(player.getPrimaryHand()==EnumHandSide.RIGHT);
						if(heldItem.getItem() instanceof ItemIIMachinegun)
						{
							if(right)
							{
								model.bipedRightArm.rotateAngleX *= 0.25f;
								model.bipedLeftArm.rotateAngleX = model.bipedRightArm.rotateAngleX;
							}
							else
							{
								model.bipedLeftArm.rotateAngleX *= 0.25f;
								model.bipedRightArm.rotateAngleX = model.bipedLeftArm.rotateAngleX;
							}
						}
						else if(heldItem.getItem() instanceof ItemIISubmachinegun&&hand!=EnumHand.OFF_HAND)
						{
							if(right)
							{
								model.bipedRightArm.rotateAngleX = -1.65f+model.bipedHead.rotateAngleX;
								model.bipedRightArm.rotateAngleY = -.08726f-TmtUtil.AngleToTMT(15f)+model.bipedHead.rotateAngleY;
								// TODO: 26.09.2020 vector calculation
								float v = model.bipedBody.rotateAngleY-model.bipedRightArm.rotateAngleY;

								model.bipedLeftArm.rotateAngleX = -1.65f+model.bipedHead.rotateAngleX;
								model.bipedLeftArm.rotateAngleY = 1.25f-model.bipedBody.rotateAngleY-v;
								//model.bipedLeftArm.rotateAngleZ = -v;
								//model.bipedLeftArm.rotateAngleY = -.08726f+v+model.bipedHead.rotateAngleY;
							}
							else
							{
								model.bipedLeftArm.rotateAngleX = -1.39626f+model.bipedHead.rotateAngleX;
								model.bipedLeftArm.rotateAngleY = .08726f+model.bipedHead.rotateAngleY;
							}
						}
						else if(player.isSneaking()&&heldItem.getItem() instanceof ItemIIBinoculars)
						{
							model.bipedRightArm.rotateAngleY = model.bipedHead.rotateAngleY-0.25f;
							model.bipedLeftArm.rotateAngleY = model.bipedHead.rotateAngleY+0.25f;

							model.bipedRightArm.rotateAngleX = model.bipedHead.rotateAngleX-2f;
							model.bipedLeftArm.rotateAngleX = model.bipedRightArm.rotateAngleX;

							int id = heldItem.getMetadata();
							BinocularsRenderer.INSTANCE.render(id==1?(ItemNBTHelper.getBoolean(heldItem, "wasUsed")?2: 1): id, model.bipedHead, true);
						}
						else if(heldItem.getItem() instanceof ItemIIMineDetector)
						{
							model.bipedRightArm.rotateAngleY = model.bipedBody.rotateAngleY-0.45f;
							model.bipedLeftArm.rotateAngleY = model.bipedBody.rotateAngleY+0.45f;

							model.bipedRightArm.rotateAngleX = model.bipedHead.rotateAngleX-1.35f;
							model.bipedLeftArm.rotateAngleX = model.bipedRightArm.rotateAngleX;

							MineDetectorRenderer.instance.renderBase(player,2.125f,true);
						}
						else if(heldItem.getItem() instanceof ItemIINavalMine)
						{
							if(right)
							{
								model.bipedRightArm.rotateAngleX -= 0.5f;
								model.bipedLeftArm.rotateAngleX = model.bipedRightArm.rotateAngleX;
							}
							else
							{
								model.bipedLeftArm.rotateAngleX -= 0.5f;
								model.bipedRightArm.rotateAngleX = model.bipedLeftArm.rotateAngleX;
							}
						}

					}
				}
		}
	}

	public void setModelVisibilities(RenderPlayer renderPlayer, AbstractClientPlayer clientPlayer)
	{
		ModelPlayer mainModel = renderPlayer.getMainModel();
		mainModel.rightArmPose = ArmPose.EMPTY;
		mainModel.leftArmPose = ArmPose.EMPTY;
	}

	@SubscribeEvent
	public void onRenderHand(RenderSpecificHandEvent event)
	{
		if(event.getHand()==EnumHand.MAIN_HAND)
		{
			ItemStack stack = event.getItemStack();
			if(stack.getItem() instanceof ItemIIMineDetector)
			{
				MineDetectorRenderer.instance.renderBase(ClientUtils.mc().player,2.125f,false);
				event.setCanceled(true);
			}
			else if(stack.getItem() instanceof ItemIISubmachinegun)
			{
				int aiming = ItemNBTHelper.getInt(stack, "aiming");
				int reloading = ItemNBTHelper.getInt(stack, "reloading");
				float reload = MathHelper.clamp(
						reloading+(reloading > 0?event.getPartialTicks(): 0),
						0,
						Submachinegun.clipReloadTime
				);
				reload /= Submachinegun.clipReloadTime;

				float preciseAim = MathHelper.clamp(
						aiming+(aiming > 0?(Minecraft.getMinecraft().player.isSneaking()?event.getPartialTicks(): -3*event.getPartialTicks()): 0),
						0,
						Submachinegun.aimTime
				);
				preciseAim /= Submachinegun.aimTime;

				GlStateManager.pushMatrix();
				GlStateManager.translate(11.5/16f, -11/16f, -1.25+2/16f);
				GlStateManager.rotate(2f, 1, 0, 0);
				GlStateManager.rotate(8.5f, 0, 1, 0);

				if(reloading > 0)
				{
					float rpart = reload <= 0.33?reload/0.33f: (reload <= 0.66?1f: (1f-((reload-0.66f)/0.33f)));
					GlStateManager.rotate(rpart*90f, 0, 1, 0);
					GlStateManager.rotate(rpart*85f, 0, 0, 1);
				}
				if(preciseAim > 0)
				{
					GlStateManager.translate(-preciseAim*0.725, 0.2*preciseAim, 0);
					GlStateManager.rotate(preciseAim*-8.5f, 0, 1, 0);
					GlStateManager.rotate(preciseAim*-2.25f, 1, 0, 0);

				}
				SubmachinegunItemStackRenderer.instance.renderByItem(stack, event.getPartialTicks());


				event.setCanceled(true);
				GlStateManager.popMatrix();
			}
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		aimingPlayers.clear();
	}

	@SubscribeEvent
	public void onTickClientTick(ClientTickEvent event)
	{
		ParticleUtils.particleRenderer.updateParticles();
	}

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event)
	{
		ParticleUtils.particleRenderer.renderParticles(event.getPartialTicks());
	}
}
