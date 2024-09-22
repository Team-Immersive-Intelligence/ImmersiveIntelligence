package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.lib.manual.IManualPage;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualInstance.ManualEntry;
import blusunrize.lib.manual.gui.GuiManual;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.FogMode;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GLContext;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IAdvancedTooltipItem;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IItemScrollable;
import pl.pabilo8.immersiveintelligence.api.utils.camera.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.client.fx.ScreenShake;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleSystem;
import pl.pabilo8.immersiveintelligence.client.gui.GuiWidgetAustralianTabs;
import pl.pabilo8.immersiveintelligence.client.gui.inworld_overlay.InWorldOverlayBase;
import pl.pabilo8.immersiveintelligence.client.gui.inworld_overlay.WrenchOverlay;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.GuiOverlayBase;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.GuiOverlayBase.GuiOverlayLayer;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.GuiOverlayMotorbike;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.GuiOverlayTripodPeriscope;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.GuiOverlayZoom;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.gun.GuiOverlayAssaultRifle;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.gun.GuiOverlayMachinegun;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.gun.GuiOverlayRifle;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.gun.GuiOverlaySubmachinegun;
import pl.pabilo8.immersiveintelligence.client.gui.tooltip.*;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageContributorSkin;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;
import pl.pabilo8.immersiveintelligence.client.render.item.BinocularsRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.ISpecificHandRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.MineDetectorRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.PrintedPageRenderer;
import pl.pabilo8.immersiveintelligence.client.util.CameraHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools.TripodPeriscope;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Vehicles.FieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Mortar;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMortar;
import pl.pabilo8.immersiveintelligence.common.entity.EntityParachute;
import pl.pabilo8.immersiveintelligence.common.entity.EntityTripodPeriscope;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityMotorbike;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.towable.gun.EntityFieldHowitzer;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIGunBase;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIRailgunOverride;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageEntityNBTSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageItemScrollableSwitch;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageManualClose;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradeableArmor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static pl.pabilo8.immersiveintelligence.api.ammo.utils.PenetrationCache.blockDamageClient;

/**
 * Handles events for client side.
 *
 * @author Pabilo8
 * @since 27-09-2019
 */
@SideOnly(Side.CLIENT)
public class ClientEventHandler implements ISelectiveResourceReloadListener
{
	private static final ListMultimap<GuiOverlayLayer, GuiOverlayBase> HUDs = MultimapBuilder.enumKeys(GuiOverlayLayer.class).arrayListValues().build();
	private static final ArrayList<GuiOverlayBase> HUD_BACKGROUNDS = new ArrayList<>();
	private static final ArrayList<TextOverlayBase> TEXT_OVERLAYS = new ArrayList<>();
	private static final ArrayList<InWorldOverlayBase> IN_WORLD_OVERLAYS = new ArrayList<>();
	private static final ArrayList<ScreenShake> SCREEN_SHAKE_EFFECTS = new ArrayList<>();

	static
	{
		//Systems
		HUD_BACKGROUNDS.add(new GuiOverlayZoom());

		//Items
		HUDs.put(GuiOverlayLayer.ITEM, new GuiOverlayMachinegun());
		HUDs.put(GuiOverlayLayer.ITEM, new GuiOverlaySubmachinegun());
		HUDs.put(GuiOverlayLayer.ITEM, new GuiOverlayAssaultRifle());
		HUDs.put(GuiOverlayLayer.ITEM, new GuiOverlayRifle());
		//Entities
		HUDs.put(GuiOverlayLayer.VEHICLE, new GuiOverlayTripodPeriscope());
		HUDs.put(GuiOverlayLayer.VEHICLE, new GuiOverlayMotorbike());
	}

	static
	{
		TEXT_OVERLAYS.add(new TextOverlayHeadgear());
		TEXT_OVERLAYS.add(new TextOverlayMechanical());
		TEXT_OVERLAYS.add(new TextOverlayUpgrade());
		TEXT_OVERLAYS.add(new TextOverlayAdvanced());
		TEXT_OVERLAYS.add(new TextOverlayVoltmeterEntities());

		IN_WORLD_OVERLAYS.add(new WrenchOverlay());
	}

	public static LinkedHashMap<EntityLivingBase, Float> gunshotEntities = new LinkedHashMap<>();
	public static boolean mgAiming = false;
	public static ArrayList<EntityLivingBase> aimingPlayers = new ArrayList<>();
	public static GuiScreen lastGui = null;
	//Whether the Light Engineer Armor is worn
	public static boolean gotTheDrip;

	@Override
	public void onResourceManagerReload(@Nonnull IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate)
	{
		if(resourcePredicate.test(VanillaResourceType.MODELS))
			IIModelRegistry.INSTANCE.reloadRegisteredModels();
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void renderAdditionalBlockBounds(DrawBlockHighlightEvent event)
	{
		WorldClient world = ClientUtils.mc().world;
		if(world==null||world.provider==null)
			return;

		//remove invalid positions
		int dimension = world.provider.getDimension();
		blockDamageClient.removeIf(d -> d.damage <= 0||d.dimension!=dimension);

		//render valid positions
		IIClientUtils.drawBlockBreak(world,
				event.getPartialTicks(),
				blockDamageClient.stream()
						.filter(Objects::nonNull)
						.filter(d -> d.dimension==world.provider.getDimension()&&world.isBlockLoaded(d))
						.toArray(DamageBlockPos[]::new)
		);
	}

	@SubscribeEvent()
	public void onFogUpdate(EntityViewRenderEvent.RenderFogEvent event)
	{
		Entity entity = event.getEntity();
		World world = entity.getEntityWorld();

		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase living = (EntityLivingBase)entity;
			//Nuke/Wasteland
			if(living.getActivePotionEffect(IIPotions.nuclearHeat)!=null)
			{
				PotionEffect effect = living.getActivePotionEffect(IIPotions.nuclearHeat);
				assert effect!=null;

				GlStateManager.setFog(FogMode.EXP2);
				GlStateManager.setFogStart(0); //(
				GlStateManager.setFogEnd(0.5f);
				GlStateManager.setFogDensity(.015f);
			}
			else if(world.getBiome(living.getPosition())==IIContent.biomeWasteland)
			{
				GlStateManager.setFog(FogMode.EXP2);
				GlStateManager.setFogStart(0); //(
				GlStateManager.setFogEnd(1.25f);
				GlStateManager.setFogDensity(.015f);
			}
			//Suppression
			if(living.getActivePotionEffect(IIPotions.suppression)!=null)
			{
				PotionEffect effect = living.getActivePotionEffect(IIPotions.suppression);
				assert effect!=null;
				int amplifier = effect.getAmplifier();
				if(amplifier < 0)
					amplifier = 254+amplifier;

				float f1 = MathHelper.clamp((float)amplifier/255f, 0f, 1f);
				//if(timeLeft < 20)
				//f1 += (event.getFarPlaneDistance()/4)*(1-timeLeft/20f);

				GlStateManager.setFog(FogMode.LINEAR);
				GlStateManager.setFogStart((float)Math.pow(1f-f1, 2)*12); //(
				GlStateManager.setFogEnd((float)Math.pow(1f-f1, 2)*16);
				GlStateManager.setFogDensity(.00625f+.00625f*f1);

				if(GLContext.getCapabilities().GL_NV_fog_distance)
					GlStateManager.glFogi(34138, 34139);
			}

		}
	}

	@SubscribeEvent()
	public void onFogColorUpdate(EntityViewRenderEvent.FogColors event)
	{
		Entity entity = event.getEntity();
		World world = entity.getEntityWorld();

		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase living = (EntityLivingBase)entity;

			//Nuke/Wasteland
			if((living).getActivePotionEffect(IIPotions.nuclearHeat)!=null)
			{
				float v = event.getEntity().getEntityWorld().provider.getSunBrightnessFactor(0);
				//float min = Math.min(Math.min(event.getRed(), event.getGreen()), event.getBlue());
				event.setRed(v);
				event.setGreen(v);
				event.setBlue(v);
			}
			else if(world.getBiome(living.getPosition())==IIContent.biomeWasteland)
			{
				float[] rgb = IIColor.fromPackedRGB(0x64604e)
						.withBrightness(0.2f*event.getEntity().getEntityWorld().provider.getSunBrightnessFactor(0.25f))
						.getFloatRGB();
				event.setRed(rgb[0]);
				event.setGreen(rgb[1]);
				event.setBlue(rgb[2]);
			}

			//Suppression
			if((living).getActivePotionEffect(IIPotions.suppression)!=null)
			{
				event.setRed(0);
				event.setGreen(0);
				event.setBlue(0);
			}

			//Infrared Vision Potion Effect
			if((living).isPotionActive(IIPotions.infraredVision))
			{
				float r = event.getRed(), g = event.getGreen(), b = event.getBlue();
				float f15 = Math.min(Objects.requireNonNull((living).getActivePotionEffect(IIPotions.infraredVision)).getAmplifier(), 4)/4f;
				float f6 = 1.0F/event.getRed();

				if(f6 > 1.0F/event.getGreen())
					f6 = 1.0F/event.getGreen();

				if(f6 > 1.0F/event.getBlue())
					f6 = 1.0F/event.getBlue();

				// Forge: fix MC-4647 and MC-10480
				if(Float.isInfinite(f6)) f6 = Math.nextAfter(f6, 0.0);

				event.setRed(r*(1.0F-f15)+r*f6*f15);
				event.setGreen(g*(1.0F-f15)+g*f6*f15);
				event.setBlue(b*(1.0F-f15)+b*f6*f15);
			}
		}

	}

	/**
	 * Handling zoom for player view (on items)
	 */
	@SubscribeEvent
	public void onFOVUpdate(FOVUpdateEvent event)
	{
		if(CameraHandler.zoom==null)
			CameraHandler.fovZoom = event.getFov();
		else
			event.setNewfov(event.getFov()*CameraHandler.fovZoom);
	}

	/**
	 * Handling zoom for camera (in vehicles/mounted weapons)
	 */
	@SubscribeEvent
	public void onFOVCamera(FOVModifier event)
	{
		CameraHandler.handleZoom();
		if(CameraHandler.isEnabled())
			if(CameraHandler.zoom==null)
				CameraHandler.fovZoom = event.getFOV();
			else
				event.setFOV(event.getFOV()*CameraHandler.fovZoom);
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent.Post event)
	{
		RayTraceResult mop = ClientUtils.mc().objectMouseOver;
		EntityPlayer player = ClientUtils.mc().player;

		if(ClientUtils.mc().player==null||event.getType()!=RenderGameOverlayEvent.ElementType.TEXT)
			return;

		//check for light engineer armor upgrade
		gotTheDrip = ItemIIUpgradeableArmor.isArmorWithUpgrade(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
				"technician_gear", "engineer_gear");

		//--- Tooltip Text ---//

		//can only display when looking at an object
		if(mop!=null&&mop.typeOfHit!=Type.MISS)
		{
			TileEntity te = mop.typeOfHit==Type.BLOCK?player.world.getTileEntity(mop.getBlockPos()): null;
			Entity entityHit = mop.entityHit;

			for(TextOverlayBase hud : TEXT_OVERLAYS)
				if(hud.shouldDraw(player, mop, te, entityHit))
				{
					//get parameters
					final String[] text = hud.getText(player, mop, te, entityHit);
					final FontRenderer font = hud.getFontRenderer();
					final int defaultColor = hud.getDefaultFontColor().getPackedARGB();

					//slightly below and right to the crosshair
					final int x = event.getResolution().getScaledWidth()/2+8;
					final int y = event.getResolution().getScaledHeight()/2+8;

					//start drawing text lines
					int i = 0;
					if(text!=null)
						for(String s : text)
							font.drawString(s, x+8, y+i++*font.FONT_HEIGHT, defaultColor, true);

					//one tooltip at once
					break;
				}
		}

		//--- Overlay GUIs ---//

		//Iterate HUD Layers
		//Iterate HUDs
		for(GuiOverlayLayer key : HUDs.keys())
			for(GuiOverlayBase hud : HUDs.get(key))
				if(hud.shouldDraw(player, mop))
				{
					hud.bindHUDTexture();
					hud.draw(player, mop, event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
					break;
				}
	}

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event)
	{
		//--- Handle Particles ---//
		ParticleSystem.INSTANCE.renderParticles(event.getPartialTicks());

		//--- Handle in-world projections ---//
		RayTraceResult mop = ClientUtils.mc().objectMouseOver;
		EntityPlayer player = ClientUtils.mc().player;

		for(InWorldOverlayBase overlay : IN_WORLD_OVERLAYS)
			overlay.draw(player, player.world, mop, event.getPartialTicks());

	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onRenderOverlayPre(RenderGameOverlayEvent.Pre event)
	{
		if(ClientUtils.mc().player==null||event.getType()!=ElementType.CROSSHAIRS)
			return;

		for(EnumHand hand : EnumHand.values())
		{
			ItemStack stack = ClientUtils.mc().player.getHeldItem(hand);
			if(stack.getItem().getTileEntityItemStackRenderer() instanceof ISpecificHandRenderer)
				if(((ISpecificHandRenderer)stack.getItem().getTileEntityItemStackRenderer()).shouldCancelCrosshair(stack, hand))
				{
					event.setCanceled(true);
					return;
				}
		}

		RayTraceResult mop = ClientUtils.mc().objectMouseOver;
		EntityPlayer player = ClientUtils.mc().player;
		//

		//Iterate HUD backgrounds
		for(GuiOverlayBase hud : HUD_BACKGROUNDS)
			if(hud.shouldDraw(player, mop))
			{
				hud.bindHUDTexture();
				hud.draw(player, mop, event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
				break;
			}

		Entity ridden = player.getRidingEntity();
		Entity lowestRidden = ridden==null?null: ridden.getLowestRidingEntity();

		//--- Entity Zoom Handling ---//

		if(lowestRidden instanceof IEntityZoomProvider)
		{
			boolean pressed = ClientProxy.keybind_zoom.isKeyDown();
			if(pressed^mgAiming&&ridden instanceof EntityMachinegun)
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setBoolean("clientMessage", true);
				tag.setBoolean("aiming", pressed);
				IIPacketHandler.sendToServer(new MessageEntityNBTSync(ridden, tag));
				((EntityMachinegun)ridden).aiming = pressed;
			}
			mgAiming = pressed;
		}
		else mgAiming = false;

		//--- Camera Handling ---//
		if(mgAiming)
		{
			ClientUtils.mc().gameSettings.thirdPersonView = 0;

			if(lowestRidden instanceof EntityMachinegun)
			{
				EntityMachinegun mg = (EntityMachinegun)lowestRidden;
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

				boolean hasScope = mg.getZoom().shouldZoom(mg.gun, null);

				Vec3d gun_end = IIMath.offsetPosDirection(2.25f-(hasScope?1.25f: 0), true_angle, true_angle2);
				Vec3d gun_height = IIMath.offsetPosDirection(0.25f+(hasScope?0.125f: 0f), true_angle, true_angle2+90);

				CameraHandler.setCameraPos(px+0.85*(gun_end.x+gun_height.x), py-1.5f+0.4025+0.85*(gun_end.y+gun_height.y), pz+0.85*(gun_end.z+gun_height.z));
				CameraHandler.setCameraAngle(mg.setYaw+yaw, pitch, 0);
				CameraHandler.setEnabled(true);
			}
			else if(lowestRidden instanceof EntityMortar)
			{
				EntityMortar mg = (EntityMortar)lowestRidden;
				//					CameraHandler.isZooming = false;
				if(mg.shootingProgress!=0)
					CameraHandler.fovZoom = 0;

				CameraHandler.setCameraPos(mg.posX, mg.posY+0.75, mg.posZ);
				CameraHandler.setCameraAngle(mg.rotationYaw, 1+(1f-mg.rotationPitch/-90f)*-1.5f, 0);
				CameraHandler.setEnabled(mg.shootingProgress==0);
			}
			else if(lowestRidden instanceof EntityTripodPeriscope)
			{
				EntityTripodPeriscope mg = (EntityTripodPeriscope)lowestRidden;
				float px = (float)mg.posX, py = (float)mg.posY, pz = (float)mg.posZ;

				CameraHandler.setCameraPos(px, py+1.25, pz);

				ClientUtils.mc().player.rotationPitch = MathHelper.clamp(ClientUtils.mc().player.rotationPitch, -50, 50);
				ClientUtils.mc().player.prevRotationPitch = MathHelper.clamp(ClientUtils.mc().player.prevRotationPitch, -50, 50);

				float y = MathHelper.wrapDegrees(360+mg.periscopeNextYaw-mg.periscopeYaw);
				float currentYaw = MathHelper.wrapDegrees(mg.periscopeYaw+event.getPartialTicks()*(Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, TripodPeriscope.turnSpeed)));

				CameraHandler.setCameraAngle(currentYaw, ClientUtils.mc().player.rotationPitch, 0);
				CameraHandler.setEnabled(true);
			}

		}
		else
			CameraHandler.setEnabled(false);
	}

	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();
		if(stack==null||event.getEntity()==null)
			return;


		if(ItemNBTHelper.hasKey(stack, "ii_FilledCasing"))
			event.getToolTip().add(TextFormatting.DARK_GRAY+I18n.format(IIReference.DESCRIPTION_KEY+"filled_casing"));

		/*if(stack.getItem()==IEContent.itemToolUpgrades)
		{
			Set<String> types = ((IUpgrade)IEContent.itemToolUpgrades).getUpgradeTypes(stack);
			for(String type : types)
			{
				WeaponTypes weaponType = Arrays.stream(WeaponTypes.values()).filter(w -> w.getName().equalsIgnoreCase(type)).findFirst().orElse(null);

				if(weaponType!=null)
					event.getToolTip().add(IIUtils.getHexCol(weaponType.color, weaponType.symbol+" "+I18n.format(IILib.DESC_TOOLUPGRADE+"item."+weaponType.getName())));
			}
		}*/

		if(stack.getItem() instanceof IAmmoTypeItem)
			IIAmmoUtils.createAmmoTooltip(((IAmmoTypeItem)stack.getItem()), stack, event.getEntity().world, event.getToolTip());
		else if(ItemNBTHelper.hasKey(stack, IIContent.NBT_AdvancedPowerpack))
		{
			ItemStack powerpack = ItemNBTHelper.getItemStack(stack, IIContent.NBT_AdvancedPowerpack);
			IIContent.itemAdvancedPowerPack.addInformation(powerpack, event.getEntity().world, event.getToolTip(), event.getFlags());
		}
	}

	@SubscribeEvent
	public void onMouseEvent(MouseEvent event)
	{
		if(event.getDwheel()!=0)
		{
			EntityPlayer player = ClientUtils.mc().player;

			if(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IItemScrollable&&player.isSneaking())
			{
				IIPacketHandler.sendToServer(new MessageItemScrollableSwitch(event.getDwheel() > 0));
				event.setCanceled(true);
			}
			else if(CameraHandler.zoom!=null)
			{
				float[] steps = CameraHandler.zoom.getZoomSteps(CameraHandler.stack, player);
				if(steps!=null&&steps.length > 0)
				{
					int curStep = -1;
					float dist = 0;
					for(int i = 0; i < steps.length; i++)
						if(curStep==-1||Math.abs(steps[i]-CameraHandler.fovZoom) < dist)
						{
							curStep = i;
							dist = Math.abs(steps[i]-CameraHandler.fovZoom);
						}
					if(curStep!=-1)
					{
						int newStep = curStep+(event.getDwheel() > 0?-1: 1);
						if(newStep >= 0&&newStep < steps.length)
							CameraHandler.fovZoom = steps[newStep];
						event.setCanceled(true);
					}
				}
			}
		}

		//Rightclick
		if(event.getButton()==1)
			if(ClientUtils.mc().player.getRidingEntity() instanceof EntityMachinegun)
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setBoolean("clientMessage", true);
				tag.setBoolean("shoot", event.isButtonstate());
				IIPacketHandler.sendToServer(new MessageEntityNBTSync(ClientUtils.mc().player.getRidingEntity(), tag));
			}
	}

	/**
	 * Draws an advanced item tooltip that can include images in text
	 */
	@SubscribeEvent()
	public void onRenderTooltip(RenderTooltipEvent.PostText event)
	{
		//Check whether the item can draw an Advanced Tooltip
		ItemStack stack = event.getStack();
		if(!(stack.getItem() instanceof IAdvancedTooltipItem))
			return;

		ArrayList<Integer> currentYs = ItemTooltipHandler.findTooltipY(event);
		if(currentYs.isEmpty())
			return;

		GlStateManager.pushMatrix();
		((IAdvancedTooltipItem)stack.getItem()).addAdvancedInformation(stack, event.getX(), currentYs);
		GlStateManager.popMatrix();
	}


	@SubscribeEvent
	public void cameraSetup(EntityViewRenderEvent.CameraSetup event)
	{
		double partialTicks = event.getRenderPartialTicks();

		//--- ScreenShake Handling ---//
		if(Graphics.cameraScreenShake)
		{
			//Display the strongest effect
			SCREEN_SHAKE_EFFECTS.stream()
					.max(ScreenShake::compareTo)
					.ifPresent(
							screenShake -> {
								double shakex = (Utils.RAND.nextGaussian()-0.5)*screenShake.getStrength();
								double shakey = (Utils.RAND.nextGaussian()-0.5)*screenShake.getStrength();
								double shakez = (Utils.RAND.nextGaussian()-0.5)*screenShake.getStrength();
								event.setRoll((float)shakez);
								event.setYaw((float)(event.getYaw()+shakex));
								event.setPitch((float)(event.getPitch()+shakey));
							}
					);
			//Tick and remove past effects
			SCREEN_SHAKE_EFFECTS.removeIf(screenShake -> screenShake.tick(partialTicks));
		}

		if(Minecraft.getMinecraft().gameSettings.thirdPersonView==0)
		{
			//--- Gun Recoil Handling ---//

			ItemStack stack = ClientUtils.mc().player.getHeldItemMainhand();
			if(stack.getItem() instanceof ItemIIGunBase&&Graphics.cameraRecoil)
			{
				//Prepare variables
				ItemIIGunBase item = ((ItemIIGunBase)stack.getItem());
				EasyNBT upgrades = EasyNBT.wrapNBT(item.getUpgrades(stack));

				boolean isAimed = ItemNBTHelper.getInt(stack, ItemIIGunBase.AIMING) > item.getAimingTime(stack, upgrades);
				double recoilH = ItemNBTHelper.getFloat(stack, ItemIIGunBase.RECOIL_H);
				double recoilV = ItemNBTHelper.getFloat(stack, ItemIIGunBase.RECOIL_V);
				float recoilDecay = item.getRecoilDecay(stack, upgrades, isAimed);

				//Calculate recoil decrease
				if(recoilH!=0)
					recoilH = item.getActualRecoil((float)Math.max(recoilH-(recoilDecay*partialTicks), 0),
							item.getHorizontalRecoil(stack, upgrades, isAimed));
				if(recoilV!=0)
					recoilV = item.getActualRecoil((float)Math.max(recoilV-(recoilDecay*partialTicks), 0),
							item.getVerticalRecoil(stack, upgrades, isAimed));

				event.setPitch((float)(event.getPitch()-recoilV));
				event.setYaw((float)(event.getYaw()+recoilH));
			}

			//--- Camera Roll in Vehicles Handling ---//
			if(ClientUtils.mc().player.getRidingEntity() instanceof EntityMotorbike)
			{
				EntityMotorbike entity = (EntityMotorbike)ClientUtils.mc().player.getRidingEntity();
				float tilt = entity.tilt;
				if(entity.turnLeft)
					tilt -= event.getRenderPartialTicks()*0.1;
				else if(entity.turnRight)
					tilt += event.getRenderPartialTicks()*0.1;
				else if(tilt!=0)
				{
					tilt = (float)(tilt < 0?tilt+event.getRenderPartialTicks()*0.1f: tilt-event.getRenderPartialTicks()*0.1f);
					if(Math.abs(tilt) < 0.01f)
						tilt = 0;
				}

				event.setRoll(MathHelper.clamp(tilt, -1f, 1f)*15f);
			}
		}
		if(CameraHandler.isEnabled()&&!ClientUtils.mc().player.isRiding())
			CameraHandler.setEnabled(false);

		if(Graphics.cameraRoll&&CameraHandler.isEnabled())
			event.setRoll(CameraHandler.getRoll());
	}

	@SuppressWarnings("unused")
	public static void handleBipedRotations(ModelBiped model, Entity entity)
	{
		if(entity instanceof EntityLivingBase&&((EntityLivingBase)entity).isPotionActive(IIPotions.concealed))
			model.setVisible(false);

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
				EntityMachinegun mg = (EntityMachinegun)ridingEntity;

				float ff = (float)(-1.35f-Math.toRadians(mg.gunPitch)*1.25);
				float true_head_angle = MathHelper.wrapDegrees(player.prevRotationYawHead-mg.setYaw);

				float partialTicks = ClientUtils.mc().getRenderPartialTicks();
				float wtime;

				ridingEntity.applyOrientationToEntity(player);
				model.bipedHead.rotateAngleX *= -0.35f;
				model.bipedHeadwear.rotateAngleX *= -0.35f;
				model.bipedLeftArm.rotateAngleY = .08726f+3.14f/6f;

				IBlockState state = player.world.getBlockState(player.getPosition());
				if(!mg.tripod&&state.getMaterial().isSolid()&&!(state.getBlock()==IIContent.blockMetalDevice&&state.getValue(IIContent.blockMetalDevice.property)==IIBlockTypes_MetalDevice.AMMUNITION_CRATE))
				{
					if(Math.abs(mg.gunYaw-true_head_angle) > 5)
					{
						wtime = Math.abs((entity.getEntityWorld().getTotalWorldTime()+partialTicks)%20/20f-0.5f)/0.5f;
						wtime *= 0.25f;

						if(mg.setupTime > 0)
							wtime = 0;
						if(mg.gunYaw < true_head_angle)
						{
							model.bipedRightLeg.rotateAngleY = -wtime*2f;
							model.bipedLeftLeg.rotateAngleY = wtime*2f;
						}
						else if(mg.gunYaw > true_head_angle)
						{
							model.bipedRightLeg.rotateAngleY = -wtime*2f;
							model.bipedLeftLeg.rotateAngleY = wtime*2f;
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

					model.bipedRightLeg.rotateAngleY += mg.gunYaw/maxRotation;
					model.bipedLeftLeg.rotateAngleY += mg.gunYaw/maxRotation;

					//model.bipedLeftLeg.rotateAngleY += ff+1f;

				}
				else
				{
					wtime = Math.abs((entity.getEntityWorld().getTotalWorldTime()+partialTicks)%40/40f-0.5f)/0.5f-0.5f;
					wtime *= 0.65f;
					if(mg.setupTime > 0)
						wtime = 0;
					model.bipedBody.rotateAngleX -= 0.0625f;
					if(Math.abs(mg.gunYaw-true_head_angle) > 5)
						if(mg.gunYaw < true_head_angle)
						{
							model.bipedRightLeg.rotateAngleX = wtime*2f;
							model.bipedLeftLeg.rotateAngleX = -wtime*2f;
						}
						else if(mg.gunYaw > true_head_angle)
						{
							model.bipedRightLeg.rotateAngleX = -wtime*2f;
							model.bipedLeftLeg.rotateAngleX = wtime*2f;
						}

					model.bipedRightArm.rotateAngleX = ff;
					model.bipedLeftArm.rotateAngleX = ff;
				}


				//model.bipedRightArm.rotateAngleY = -.08726f+model.bipedHead.rotateAngleY;
			}
			else if(ridingEntity instanceof EntityMortar)
			{
				EntityMortar mortar = (EntityMortar)ridingEntity;

				float ff = 1;
				if(mortar.shootingProgress > 0)
				{
					float v = mortar.shootingProgress/Mortar.shootTime;
					//rise up
					if(v < 0.1)
						ff = 1f-v/0.1f;
					else if(v < 0.2)
					{
						//turn, put shell
						ff = 0;
						float firing = (v-0.1f)/0.1f;

						model.bipedLeftArm.rotateAngleY = firing*0.15f;
						model.bipedRightArm.rotateAngleY = firing*-0.65f;
						model.bipedRightArm.rotationPointX += 1*firing;
						model.bipedRightArm.rotationPointZ -= 2*firing;

						model.bipedLeftArm.rotateAngleX = firing*-2.15f;
						model.bipedRightArm.rotateAngleX = firing*-2.15f;
					}
					else if(v < 0.3)
					{
						//unturn
						ff = 0;

						float firing = 1f-(v-0.2f)/0.1f;

						model.bipedLeftArm.rotateAngleY = firing*0.15f;
						model.bipedRightArm.rotateAngleY = firing*-0.65f;
						model.bipedRightArm.rotationPointX += 1*firing;
						model.bipedRightArm.rotationPointZ -= 2*firing;

						model.bipedLeftArm.rotateAngleX = firing*-2.15f;
						model.bipedRightArm.rotateAngleX = firing*-2.15f;
					}
					else //get down
						if(v < 0.4)
							ff = (v-0.3f)/0.1f;
						else
						{
							float firing = (v-0.4f)/0.6f;
							float progress = MathHelper.clamp(firing < 0.75?firing/0.2f: 1f-(firing-0.85f)/0.15f, 0, 1);
							model.bipedHead.rotateAngleX = Math.min(progress/0.85f, 1f)*0.85f;
							model.bipedHeadwear.rotateAngleX = Math.min(progress/0.85f, 1f)*0.85f;
							model.bipedHead.rotateAngleY = 0;
							model.bipedHeadwear.rotateAngleY = 0;

							model.bipedLeftArm.rotateAngleZ = progress*-0.15f;
							model.bipedRightArm.rotateAngleZ = progress*0.15f;
							model.bipedLeftArm.rotateAngleX = progress*-2.55f;
							model.bipedRightArm.rotateAngleX = progress*-2.55f;
						}
				}

				model.bipedLeftLeg.rotateAngleX = 1.45f*ff;
				model.bipedLeftLeg.rotateAngleY = 0.125f*ff;
				model.bipedRightLeg.rotateAngleX = 1.45f*ff;
				model.bipedRightLeg.rotateAngleY = -0.25f*ff;

			}
			else if(ridingEntity instanceof EntityTripodPeriscope)
			{
				EntityTripodPeriscope mg = (EntityTripodPeriscope)ridingEntity;
				float true_head_angle = MathHelper.wrapDegrees(player.prevRotationYawHead);

				float partialTicks = ClientUtils.mc().getRenderPartialTicks();
				float wtime = Math.abs((entity.getEntityWorld().getTotalWorldTime()+partialTicks)%40/40f-0.5f)/0.5f-0.5f;

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
						model.bipedRightLeg.rotateAngleZ = -(1f-wtime)*0.25f;
						model.bipedLeftLeg.rotateAngleZ = -wtime*0.25f-0.25f;
					}
					else if(mg.periscopeYaw > true_head_angle)
					{
						model.bipedRightLeg.rotateAngleZ = (1f-wtime)*0.25f+0.25f;
						model.bipedLeftLeg.rotateAngleZ = wtime*0.25f;
					}

			}
			else if(ridingEntity instanceof EntityParachute)
			{
				model.bipedLeftArm.rotateAngleX -= 2.75;
				model.bipedLeftArm.rotateAngleZ += 0.35;

				model.bipedRightArm.rotateAngleX += 3.5;
				model.bipedRightArm.rotateAngleZ -= 0.35;
			}
			else if(ridingEntity instanceof EntityVehicleSeat)
			{
				EntityVehicleSeat seat = (EntityVehicleSeat)ridingEntity;
				((IVehicleMultiPart)seat.getRidingEntity()).getSeatRidingAngle(seat.seatID, entity);


				if(player.getLowestRidingEntity() instanceof EntityMotorbike)
				{
					if(seat.seatID==0)
					{
						model.bipedBody.rotateAngleX += 0.25;
						model.bipedRightLeg.rotationPointZ = 2;
						model.bipedLeftLeg.rotationPointZ = 2;

						model.bipedRightArm.rotateAngleZ = 0;
						model.bipedLeftArm.rotateAngleZ = 0;

						model.bipedRightArm.rotationPointZ = -1.5f;
						model.bipedLeftArm.rotationPointZ = -1.5f;

						model.bipedRightArm.rotateAngleX = -1.35f;
						model.bipedRightArm.rotateAngleY = 0.5f;
						model.bipedLeftArm.rotateAngleX = -1.35f;
						model.bipedLeftArm.rotateAngleY = -0.5f;
					}

					model.bipedRightLeg.rotateAngleY = 0.65f;
					model.bipedLeftLeg.rotateAngleY = -0.65f;
					model.bipedRightLeg.rotateAngleX = -0.65f;
					model.bipedLeftLeg.rotateAngleX = -0.65f;

				}
				else if(player.getLowestRidingEntity() instanceof EntityFieldHowitzer)
				{
					EntityFieldHowitzer howitzer = (EntityFieldHowitzer)player.getLowestRidingEntity();
					float partialTicks = ClientUtils.mc().getRenderPartialTicks();
					float tt = entity.world.getTotalWorldTime()+partialTicks;
					float firing = (howitzer.shootingProgress+(howitzer.shootingProgress > 0?partialTicks: 0))/FieldHowitzer.fireTime;
					float reloading = (howitzer.reloadProgress+(howitzer.reloadProgress > 0?partialTicks: 0))/FieldHowitzer.reloadTime;
					//float reloading = (tt%60)/60f;

					if(howitzer.setupTime > 0)
					{
						float progress = MathHelper.clamp((howitzer.setupTime+
								partialTicks*(howitzer.turnLeft||howitzer.turnRight||howitzer.forward||howitzer.backward?1: -1)
						)/(FieldHowitzer.setupTime*0.2f), 0, 1);

						float wtime = Math.abs((entity.getEntityWorld().getTotalWorldTime()+partialTicks)%30/30f-0.5f)/0.5f;
						float stime = howitzer.setupTime/(float)FieldHowitzer.setupTime;
						wtime *= (float)Math.floor(stime);

						model.bipedRightLeg.rotationPointZ = progress*8f;
						model.bipedLeftLeg.rotationPointZ = progress*8f;

						//model.bipedBody.rotateAngleY=seat.seatID==0?0.25f:-0.25f;
						model.bipedBody.rotateAngleX += progress*0.385f;
						model.isSneak = true;
						if(seat.seatID==1)
						{
							model.bipedRightArm.rotateAngleX = progress*-0.5f;
							model.bipedRightArm.rotateAngleZ = progress*1.5f;
							model.bipedLeftArm.rotateAngleX = progress*-0.55f;

							if(howitzer.turnLeft||howitzer.forward)
							{
								model.bipedRightLeg.rotateAngleZ = -(1f-wtime)*0.25f*stime;
								model.bipedLeftLeg.rotateAngleZ = (-wtime*0.25f-0.25f)*stime;
							}
							else if(howitzer.turnRight||howitzer.backward)
							{
								model.bipedRightLeg.rotateAngleZ = ((1f-wtime)*0.25f+0.25f)*stime;
								model.bipedLeftLeg.rotateAngleZ = wtime*0.25f*stime;
							}
						}
						else
						{
							model.bipedLeftArm.rotateAngleX = progress*-0.5f;
							model.bipedLeftArm.rotateAngleZ = -progress*1.125f;
							model.bipedRightArm.rotateAngleX = progress*0.125f;
							model.bipedRightArm.rotateAngleZ = progress*0.5f;

							if(howitzer.turnLeft||howitzer.backward)
							{
								model.bipedRightLeg.rotateAngleZ = (-(1f-wtime)*0.25f+0.25f)*stime;
								model.bipedLeftLeg.rotateAngleZ = -wtime*0.25f*stime;
							}
							else if(howitzer.turnRight||howitzer.forward)
							{
								model.bipedRightLeg.rotateAngleZ = (1f-wtime)*0.25f*stime;
								model.bipedLeftLeg.rotateAngleZ = (wtime*0.25f-0.25f)*stime;
							}
						}
					}
					else if(reloading > 0)
					{
						if(seat.seatID==0)
						{
							if(reloading < 0.2)
								model.bipedLeftArm.rotateAngleZ = (1f-reloading/0.2f)*-1.25f;

							if(reloading > 0.2f)
							{
								float ff = 0;
								if(reloading < 0.4f)
									ff = (reloading-0.2f)/0.2f;
								else if(reloading < 0.5f)
									ff = 1f-(reloading-0.4f)/0.1f;
								model.bipedBody.rotateAngleX = ff*0.25f;
								model.bipedLeftLeg.rotationPointZ = ff*3;
								model.bipedRightLeg.rotationPointZ = ff*3;
								model.bipedRightArm.rotateAngleX = ff*-1.25f;
								model.bipedRightArm.rotateAngleZ = ff*-1.5f;
							}

							if(reloading < 0.1)
								model.bipedLeftArm.rotateAngleX = reloading/0.1f*-1.25f;
							else if(reloading < 0.9)
								model.bipedLeftArm.rotateAngleX = -1.25f;
							else
								model.bipedLeftArm.rotateAngleX = (1f-(reloading-0.9f)/0.1f)*-1.25f;
						}
					}
					else if(firing > 0)
						if(seat.seatID==1)
						{
							float progress = MathHelper.clamp(firing < 0.75?firing/0.2f: 1f-(firing-0.85f)/0.15f, 0, 1);
							model.isSneak = true;
							model.bipedHead.rotateAngleX = Math.min(progress/0.85f, 1f)*0.15f;
							model.bipedHeadwear.rotateAngleX = Math.min(progress/0.85f, 1f)*0.15f;
							model.bipedHead.rotateAngleY = 0;
							model.bipedHeadwear.rotateAngleY = 0;

							model.bipedBody.rotateAngleX += progress*0.25f;

							model.bipedLeftArm.rotateAngleX = progress*-3f;
							model.bipedRightArm.rotateAngleX = progress*-3f;
							model.bipedLeftLeg.rotateAngleX = Math.min(progress/0.65f, 1f)*-0.35f;

							model.bipedRightLeg.rotationPointZ = 5f+progress*2f;
							model.bipedLeftLeg.rotationPointZ = 5f+progress*2f;

							//model.bipedRightLeg.rotateAngleX = progress*0.25f;
							//model.bipedLeftArm.rotateAngleY = progress*3.14f;
						}
						else
						{
							if(firing < 0.3)
								model.bipedRightArm.rotateAngleY = -0.385f;
							if(firing < 0.1f)
								model.bipedRightArm.rotateAngleX = -1.65f*(firing/0.1f);
							else if(firing < 0.2f)
								model.bipedRightArm.rotateAngleX = -1.65f+0.8f*((firing-0.1f)/0.1f);
							else if(firing < 0.3)
								model.bipedRightArm.rotateAngleX = -0.85f*(1f-(firing-0.2f)/0.1f);
							else
							{
								firing = (firing-0.3f)/0.7f;
								float progress = MathHelper.clamp(firing < 0.75?firing/0.2f: 1f-(firing-0.85f)/0.15f, 0, 1);
								model.isSneak = true;
								model.bipedHead.rotateAngleX = Math.min(progress/0.85f, 1f)*0.15f;
								model.bipedHeadwear.rotateAngleX = Math.min(progress/0.85f, 1f)*0.15f;
								model.bipedHead.rotateAngleY = 0;
								model.bipedHeadwear.rotateAngleY = 0;

								model.bipedLeftArm.rotateAngleX = progress*-3f;
								model.bipedRightArm.rotateAngleX = progress*-3f;
								model.bipedRightLeg.rotateAngleX = Math.min(progress/0.65f, 1f)*0.35f;

								model.bipedRightLeg.rotationPointZ = 5f;
								model.bipedLeftLeg.rotationPointZ = 5f;
							}

						}
					else if(seat.seatID==0&&(howitzer.gunPitchDown||howitzer.gunPitchUp))
					{
						float limbSwing = Math.abs((tt%8-4)/8f);
						if(howitzer.gunPitchUp)
							limbSwing = -limbSwing;
						model.isSneak = true;
						model.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing*0.6662F)-1.125f;
						model.bipedRightArm.rotateAngleY = MathHelper.cos(limbSwing*0.6662F)-1f;
						model.bipedRightArm.rotateAngleZ = 0;

						model.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing*0.6662F)-2f;
						model.bipedLeftArm.rotateAngleY = MathHelper.cos(limbSwing*0.6662F)+0.5f;
						model.bipedLeftArm.rotateAngleZ = 0;
					}

				}

			}
			else
				for(EnumHand hand : EnumHand.values())
				{
					ItemStack heldItem = player.getHeldItem(hand);
					if(!heldItem.isEmpty())
					{
						Item item = heldItem.getItem();
						boolean right = (hand==EnumHand.MAIN_HAND)==(player.getPrimaryHand()==EnumHandSide.RIGHT);
						if(item==IIContent.itemMachinegun)
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
						else if(item==IIContent.itemMortar)
							if(right)
							{
								model.bipedRightArm.rotateAngleX *= 0.25f;
								model.bipedLeftArm.rotateAngleX = model.bipedRightArm.rotateAngleX-0.25f;
								model.bipedLeftArm.rotateAngleZ = -0.35f;
							}
							else
							{
								model.bipedLeftArm.rotateAngleX *= 0.25f;
								model.bipedRightArm.rotateAngleX = model.bipedLeftArm.rotateAngleX;
							}
						else if((item instanceof ItemIIGunBase
								||item instanceof ItemIIRailgunOverride)
								&&hand!=EnumHand.OFF_HAND)
							if(right)
							{
								player.setRenderYawOffset(player.rotationYawHead);
								boolean rail = item instanceof ItemIIRailgunOverride;

								model.bipedRightArm.rotateAngleX = -1.65f+model.bipedHead.rotateAngleX;
								model.bipedLeftArm.rotateAngleX = -1.65f+model.bipedHead.rotateAngleX+0.0625f;

								//-1.5707964 up
								//0 middle
								//1.5707964 down
								float v = (model.bipedHead.rotateAngleX+1.5707964f)/3.1415927f;

								model.bipedRightArm.rotateAngleY += IIMath.clampedLerp3Par(0, -0.45f, 0f, v);
								model.bipedRightArm.rotateAngleZ += IIMath.clampedLerp3Par(0.25f, 0, -0.45f, v);
								model.bipedLeftArm.rotateAngleZ += IIMath.clampedLerp3Par(rail?-0.25f: -0.65f, 0, rail?0.25f: 0.65f, v);
								model.bipedLeftArm.rotateAngleY += IIMath.clampedLerp3Par(0f, rail?0.25f: 0.7f, 0f, v);

								model.bipedLeftArm.rotationPointX += IIMath.clampedLerp3Par(-2f, -1f, -2f, v);
								model.bipedLeftArm.rotationPointZ += IIMath.clampedLerp3Par(0, -2f, 0, v);

								model.bipedRightArm.rotationPointZ += IIMath.clampedLerp3Par(0, 2f, 0, v);


								//up
								/*
								model.bipedRightArm.rotateAngleZ += 0.25f;
								model.bipedLeftArm.rotateAngleZ = -0.65f;
								model.bipedLeftArm.rotationPointX -= 2;
								 */

								//mid
								/*
								model.bipedRightArm.rotateAngleY -= 0.35f;
								model.bipedLeftArm.rotateAngleY = 0.65f;
								model.bipedLeftArm.rotateAngleZ = 0;

								model.bipedRightArm.rotationPointZ += 1;

								model.bipedLeftArm.rotationPointX -= 1;
								model.bipedLeftArm.rotationPointZ -= 2;
								 */

								//down
								/*
								model.bipedRightArm.rotateAngleZ -= 0.45f;
								model.bipedLeftArm.rotateAngleZ = 0.65f;
								model.bipedLeftArm.rotationPointX -= 2;
								 */
							}
							else
							{
								// TODO: 19.09.2021 animation for left hand (requires model offset changes)
							}
							// TODO: 19.05.2021 change railgun and chemthrower animation
						/*
						else if(heldItem.getItem() instanceof ItemIIRailgunOverride&&hand!=EnumHand.OFF_HAND)
						{
							if(right)
							{
								model.bipedRightArm.rotateAngleX = -1.39626f+model.bipedHead.rotateAngleX;
								model.bipedRightArm.rotateAngleY = -0.25f;
								model.bipedLeftArm.rotateAngleX = -1.39626f+model.bipedHead.rotateAngleX;
								model.bipedLeftArm.rotateAngleY = 0.75f;
								model.bipedLeftArm.rotationPointZ = -1.25F;
								//model.bipedRightArm.rotateAngleY = .08726f+model.bipedHead.rotateAngleY;

							}
							else
							{
								model.bipedLeftArm.rotateAngleX = -1.39626f+model.bipedHead.rotateAngleX;
								model.bipedLeftArm.rotateAngleY = .08726f+model.bipedHead.rotateAngleY;
							}
						}
						 */
						else if(player.isSneaking()&&item==IIContent.itemBinoculars)
						{
							model.bipedRightArm.rotateAngleY = model.bipedHead.rotateAngleY-0.25f;
							model.bipedLeftArm.rotateAngleY = model.bipedHead.rotateAngleY+0.25f;

							model.bipedRightArm.rotateAngleX = model.bipedHead.rotateAngleX-2f;
							model.bipedLeftArm.rotateAngleX = model.bipedRightArm.rotateAngleX;

							int id = heldItem.getMetadata();
							BinocularsRenderer.INSTANCE.render(id==1?ItemNBTHelper.getBoolean(heldItem, "wasUsed")?2: 1: id, model.bipedHead, true);
						}
						else if(item==IIContent.itemMineDetector)
						{
							float v = MineDetectorRenderer.instance.renderBase(player, 2.125f, true);

							model.bipedRightArm.rotateAngleY = model.bipedBody.rotateAngleY-0.45f;
							model.bipedLeftArm.rotateAngleY = model.bipedBody.rotateAngleY+0.45f;

							model.bipedRightArm.rotateAngleX = -1.25f-(1f-v)*0.5f;
							model.bipedLeftArm.rotateAngleX = model.bipedRightArm.rotateAngleX;
						}
						else if(item==IIContent.itemNavalMine)
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
						else if(item==IIContent.itemGrenade)
						{
							float use = 1f-MathHelper.clamp(((EntityLivingBase)entity).getItemInUseCount()/(float)heldItem.getMaxItemUseDuration(), 0, 1);
							if(right)
							{
								model.rightArmPose = ArmPose.EMPTY;
								//model.leftArmPose=ArmPose.EMPTY;
								float hh = -(4.5f-model.bipedHead.rotateAngleX);
								model.bipedRightArm.rotateAngleX = use!=1?use > 0f?use < 0.35f?use/0.35f*hh: hh: 0f: 0f;
							}
						}

					}
				}

			if(!gunshotEntities.isEmpty())
			{
				Float v = gunshotEntities.remove(entity);
				if(v!=null)
				{
					ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
					if(heldItem.getItem() instanceof ItemIIGunBase)
					{
						//float recoilH = ItemNBTHelper.getFloat(heldItem, "recoilH");
						//float recoilV = ItemNBTHelper.getFloat(heldItem, "recoilV");

						Vec3d vec =
								IIMath.getVectorForRotation(player.rotationPitch, player.getRotationYawHead())
										.scale(-1);

						double true_angle = Math.toRadians(-player.getRotationYawHead() > 180?360f- -player.getRotationYawHead(): -player.getRotationYawHead());
						double true_angle2 = Math.toRadians(-player.getRotationYawHead()-90 > 180?360f-(-player.getRotationYawHead()-90): -player.getRotationYawHead()-90);

						Vec3d pos1_x = IIMath.offsetPosDirection(-model.bipedRightArm.rotationPointZ/16f+0.185f, true_angle, 0);
						Vec3d pos1_z = IIMath.offsetPosDirection(-model.bipedRightArm.rotationPointX/16f-0.125f-0.0625f, true_angle2, 0);
						Vec3d pos1_y = IIMath.offsetPosDirection(3/16f, true_angle, 90);

						Vec3d vv = player.getPositionVector()
								.addVector(0, entity.isSneaking()?-0.275: 0, 0)
								.addVector(0, (24.0F-model.bipedRightArm.rotationPointY)/16f, 0)
								.add(pos1_x).add(pos1_z).add(pos1_y)
								.add(vec.scale(-1.25f));
						//.add(arm.rotatePitch(-90f).scale(0.5f));

						ParticleRegistry.spawnGunfireFX(vv, vec, v);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderHand(RenderSpecificHandEvent event)
	{
		ItemStack stack = event.getItemStack();
		ItemStack stackOpposite = ClientUtils.mc().player.getHeldItem(event.getHand()==EnumHand.MAIN_HAND?EnumHand.OFF_HAND: EnumHand.MAIN_HAND);

		if(stack.isEmpty())
			return;
		if(stack.getItem().getTileEntityItemStackRenderer() instanceof ISpecificHandRenderer)
			if(((ISpecificHandRenderer)stack.getItem().getTileEntityItemStackRenderer())
					.doHandRender(stack, event.getHand(), stackOpposite, event.getSwingProgress(), event.getPartialTicks()))
				event.setCanceled(true);

		if(stack.getItem()==IIContent.itemPrintedPage)
		{
			PrintedPageRenderer.renderItemFirstPerson(stack, event.getHand(), event.getEquipProgress(), event.getSwingProgress(), event.getInterpolatedPitch());
			event.setCanceled(true);
		}

		if(stack.getItem()==IIContent.itemMineDetector&&event.getHand()==EnumHand.MAIN_HAND)
		{
			MineDetectorRenderer.instance.renderBase(ClientUtils.mc().player, 2.125f, false);
			event.setCanceled(true);
		}

	}

	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event)
	{
		if(event.getGui() instanceof GuiManual)
			IISkinHandler.getManualPages();
		else if(ClientEventHandler.lastGui instanceof GuiManual)
		{
			GuiManual gui = (GuiManual)ClientEventHandler.lastGui;
			String name = null;

			ManualInstance inst = gui.getManual();
			if(inst!=null)
			{
				ManualEntry entry = inst.getEntry(gui.getSelectedEntry());
				if(entry!=null)
				{
					IManualPage page = entry.getPages()[gui.page];
					if(page instanceof IIManualPageContributorSkin)
						name = ((IIManualPageContributorSkin)page).skin.name;
				}
			}
			EntityPlayer p = ClientUtils.mc().player;

			ItemStack mainItem = p.getHeldItemMainhand();
			ItemStack offItem = p.getHeldItemOffhand();

			boolean main = !mainItem.isEmpty()&&mainItem.getItem()==IEContent.itemTool&&mainItem.getItemDamage()==3;
			boolean off = !offItem.isEmpty()&&offItem.getItem()==IEContent.itemTool&&offItem.getItemDamage()==3;
			ItemStack target = main?mainItem: offItem;

			if(main||off)
			{
				IIPacketHandler.sendToServer(new MessageManualClose(name==null?"": name));

				if(name==null&&ItemNBTHelper.hasKey(target, "lastSkin"))
					ItemNBTHelper.remove(target, "lastSkin");
				else if(name!=null)
					ItemNBTHelper.setString(target, "lastSkin", name);
			}
		}

		ClientEventHandler.lastGui = event.getGui();
	}

	@SubscribeEvent
	public void onInitGuiPost(InitGuiEvent.Post event)
	{
		//Add creative menu subtabs
		if(event.getGui() instanceof GuiContainerCreative&&IIConfig.australianCreativeTabs)
		{
			GuiContainerCreative gui = (GuiContainerCreative)event.getGui();
			try
			{
				event.getButtonList().add(new GuiWidgetAustralianTabs(gui.guiLeft-27, gui.guiTop+2, gui));
			} catch(Exception ignored)
			{
				IILogger.warn("Failed to add subtabs to creative inventory");
			}
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		aimingPlayers.clear();
		gunshotEntities.clear();
		blockDamageClient.clear();

		ParticleSystem.INSTANCE.reload();
	}

	@SubscribeEvent
	public void onPostClientTick(ClientTickEvent event)
	{
		if(event.phase==Phase.END)
		{
			ParticleSystem.INSTANCE.updateParticles();

			if(!Weapons.bulletsWhistleSound)
				return;
			//Make close bullets produce a whistling sound
			Minecraft mc = ClientUtils.mc();
			if(mc.world!=null&&mc.player!=null)
			{
				List<EntityAmmoProjectile> bullets = mc.world.getEntitiesWithinAABB(EntityAmmoProjectile.class, mc.player.getEntityBoundingBox().grow(3));
				for(EntityAmmoProjectile bullet : bullets)
					if(bullet.getOwner()!=mc.player)
						//higher the velocity (howitzers), lower the tone
						bullet.playSound(IISounds.bulletFlyby, 0.6f, 1.75f-MathHelper.clamp(bullet.getVelocity()/6f, 0.5f, 1.75f));
			}

		}
	}


	/**
	 * @param pos      position of the explosion / screenshake source
	 * @param strength strength of the shake
	 */
	public static void addScreenshakeSource(Vec3d pos, float strength, float duration)
	{
		SCREEN_SHAKE_EFFECTS.add(new ScreenShake(strength, duration, pos));
	}
}
