package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.lib.manual.IManualPage;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualInstance.ManualEntry;
import blusunrize.lib.manual.gui.GuiManual;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.FogMode;
import net.minecraft.client.renderer.entity.Render;
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
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.client.event.RenderTooltipEvent.PostText;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.event.GameRuleChangeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GLContext;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.LogisticTag;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IAdvancedTooltipItem;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IItemScrollable;
import pl.pabilo8.immersiveintelligence.api.utils.camera.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.client.fx.ScreenShake;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleSystem;
import pl.pabilo8.immersiveintelligence.client.gui.GuiWidgetAustralianTabs;
import pl.pabilo8.immersiveintelligence.client.gui.inworld_overlay.InWorldOverlayBase;
import pl.pabilo8.immersiveintelligence.client.gui.inworld_overlay.OwnershipOverlay;
import pl.pabilo8.immersiveintelligence.client.gui.inworld_overlay.VehicleDebugOverlay;
import pl.pabilo8.immersiveintelligence.client.gui.inworld_overlay.WrenchOverlay;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.GuiOverlayBase;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.GuiOverlayBase.GuiOverlayLayer;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.GuiOverlayMotorbike;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.GuiOverlayTripodPeriscope;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.GuiOverlayZoom;
import pl.pabilo8.immersiveintelligence.client.gui.overlay.gun.*;
import pl.pabilo8.immersiveintelligence.client.gui.tooltip.*;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageContributorSkin;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;
import pl.pabilo8.immersiveintelligence.client.render.IPassengerAnimationsRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.BinocularsRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.ISpecificHandRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.MineDetectorRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.PrintedPageRenderer;
import pl.pabilo8.immersiveintelligence.client.util.CameraHandler;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTBipedAdapter;
import pl.pabilo8.immersiveintelligence.common.*;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools.TripodPeriscope;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMachinegun;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMortar;
import pl.pabilo8.immersiveintelligence.common.entity.EntityTripodPeriscope;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIPrintedPage.PageType;
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
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIUpgradeableArmor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static pl.pabilo8.immersiveintelligence.api.ammo.utils.PenetrationCache.blockDamageClient;

/**
 * Handles events for client side.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.09.2019
 */
@SideOnly(Side.CLIENT)
public class ClientEventHandler implements ISelectiveResourceReloadListener
{
	private static final ListMultimap<GuiOverlayLayer, GuiOverlayBase> HUDs = MultimapBuilder.enumKeys(GuiOverlayLayer.class).arrayListValues().build();
	private static final ArrayList<GuiOverlayBase> HUD_BACKGROUNDS = new ArrayList<>();
	private static final ArrayList<TextOverlayBase> TEXT_OVERLAYS = new ArrayList<>();
	private static final ArrayList<InWorldOverlayBase> IN_WORLD_OVERLAYS = new ArrayList<>();
	private static final ArrayList<ScreenShake> SCREEN_SHAKE_EFFECTS = new ArrayList<>();
	public static boolean mgAiming = false;
	public static ArrayList<EntityLivingBase> aimingPlayers = new ArrayList<>();
	public static GuiScreen lastGui = null;
	//Whether the Light Engineer Armor is worn
	public static boolean gotTheDrip;

	static
	{
		//Systems
		HUD_BACKGROUNDS.add(new GuiOverlayZoom());

		//Items
		HUDs.put(GuiOverlayLayer.ITEM, new GuiOverlayMachinegun());
		HUDs.put(GuiOverlayLayer.ITEM, new GuiOverlaySubmachinegun());
		HUDs.put(GuiOverlayLayer.ITEM, new GuiOverlayAssaultRifle());
		HUDs.put(GuiOverlayLayer.ITEM, new GuiOverlayRifle());
		HUDs.put(GuiOverlayLayer.ITEM, new GuiOverlayMineDetector());
		//Entities
		HUDs.put(GuiOverlayLayer.VEHICLE, new GuiOverlayTripodPeriscope());
		HUDs.put(GuiOverlayLayer.VEHICLE, new GuiOverlayMotorbike());
	}

	static
	{
		TEXT_OVERLAYS.add(new TextOverlayHeadgear());
		TEXT_OVERLAYS.add(new TextOverlayMechanical());
		TEXT_OVERLAYS.add(new TextOverlayUpgrade());
		TEXT_OVERLAYS.add(new TextOverlayConstruction());
		TEXT_OVERLAYS.add(new TextOverlayAdvanced());
		TEXT_OVERLAYS.add(new TextOverlayVoltmeterEntities());
		TEXT_OVERLAYS.add(new TextOverlayOwnership());

		IN_WORLD_OVERLAYS.add(new WrenchOverlay());
		IN_WORLD_OVERLAYS.add(new VehicleDebugOverlay());
		IN_WORLD_OVERLAYS.add(new OwnershipOverlay());
	}

	@SuppressWarnings("unused")
	public static void handleBipedRotations(ModelBiped model, Entity entity)
	{
		if(!(entity instanceof EntityLivingBase))
			return;
		EntityLivingBase living = (EntityLivingBase)entity;

		//Concealed potion effect
		if(((EntityLivingBase)entity).isPotionActive(IIPotions.concealed))
			model.setVisible(false);
		//Vehicle passenger animations
		if(Graphics.passengerAnimations)
		{
			model.bipedHead.rotateAngleZ = 0f;
			model.bipedHeadwear.rotateAngleZ = 0f;


			if(aimingPlayers.contains(entity))
			{
				model.bipedHead.rotateAngleZ = -0.35f;
				model.bipedHeadwear.rotateAngleZ = -0.35f;
			}

			//Handle a vehicle's passenger animations
			Entity vehicle = living.getRidingEntity();
			//Vehicle seats have no renderer, use the vehicle renderer for the action
			if(vehicle instanceof EntityVehicleSeat)
				vehicle = vehicle.getRidingEntity();

			Minecraft mc = ClientUtils.mc();
			if(vehicle!=null)
			{
				Render<Entity> renderer = mc.getRenderManager().getEntityClassRenderObject(vehicle.getClass());
				if(renderer instanceof IPassengerAnimationsRenderer)
				{
					//noinspection rawtypes
					IPassengerAnimationsRenderer par = (IPassengerAnimationsRenderer)renderer;
					//noinspection unchecked
					if(par.handleBipedRotations(model, vehicle, living, mc.getRenderPartialTicks()))
						return;
				}
			}
		}

		//Item holding animations
		if(IEConfig.fancyItemHolding)
			for(EnumHand hand : EnumHand.values())
			{
				ItemStack heldItem = living.getHeldItem(hand);
				if(!heldItem.isEmpty())
				{
					Item item = heldItem.getItem();
					boolean right = (hand==EnumHand.MAIN_HAND)==(living.getPrimaryHand()==EnumHandSide.RIGHT);
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
					else if((item instanceof ItemIIGunBase||item instanceof ItemIIRailgunOverride)&&hand!=EnumHand.OFF_HAND)
						if(right)
						{
							living.setRenderYawOffset(living.rotationYawHead);
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
						}
						else
						{
							// TODO: 19.09.2021 animation for left hand (requires model offset changes)
						}
					else if(living.isSneaking()&&item==IIContent.itemBinoculars)
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
						float v = MineDetectorRenderer.instance.renderBase(living, 2.125f, true);

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
	}

	/**
	 * Called by ASM for post-AMT animation player model angle defaulting.
	 *
	 * @param model  the biped model
	 * @param entity the entity that was rendered
	 */
	@SuppressWarnings("unused")
	public static void resetBipedRotations(ModelBiped model, Entity entity, boolean finalCall)
	{
		//Prevent calling earlier from a player model
		if(model instanceof ModelPlayer&&!finalCall)
			return;
		//Get the default model
		ModelBiped biped = AMTBipedAdapter.getPreviousBipedModel();
		if(biped==null)
			return;

		//Restore previous angles
		ModelBase.copyModelAngles(biped.bipedHead, model.bipedHead);
		ModelBase.copyModelAngles(biped.bipedHeadwear, model.bipedHeadwear);
		ModelBase.copyModelAngles(biped.bipedBody, model.bipedBody);
		ModelBase.copyModelAngles(biped.bipedLeftArm, model.bipedLeftArm);
		ModelBase.copyModelAngles(biped.bipedRightArm, model.bipedRightArm);
		ModelBase.copyModelAngles(biped.bipedLeftLeg, model.bipedLeftLeg);
		ModelBase.copyModelAngles(biped.bipedRightLeg, model.bipedRightLeg);
	}

	/**
	 * @param pos      position of the explosion / screenshake source
	 * @param strength strength of the shake
	 * @param duration duration of the shake in ticks
	 */
	public static void addScreenshakeSource(Vec3d pos, float strength, float duration)
	{
		SCREEN_SHAKE_EFFECTS.add(new ScreenShake(strength, duration, pos));
	}

	/**
	 * @param pos      position of the explosion / screenshake source
	 * @param strength strength of the shake
	 * @param duration duration of the shake in ticks
	 */
	public static void addScreenshakeSource(Vec3d pos, float strength, float duration, float delay)
	{
		SCREEN_SHAKE_EFFECTS.add(new ScreenShake(strength, duration, delay, pos));
	}

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
	public void onFogUpdate(RenderFogEvent event)
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
	public void onFogColorUpdate(FogColors event)
	{
		Entity entity = event.getEntity();
		World world = entity.getEntityWorld();

		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase living = (EntityLivingBase)entity;

			//Nuke/Wasteland
			if(living.getActivePotionEffect(IIPotions.nuclearHeat)!=null)
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
			if(living.getActivePotionEffect(IIPotions.suppression)!=null)
			{
				event.setRed(0);
				event.setGreen(0);
				event.setBlue(0);
			}

			//Infrared Vision Potion Effect
			if(living.isPotionActive(IIPotions.infraredVision))
			{
				float r = event.getRed(), g = event.getGreen(), b = event.getBlue();
				float f15 = Math.min(Objects.requireNonNull(living.getActivePotionEffect(IIPotions.infraredVision)).getAmplifier(), 4)/4f;
				float f6 = 1.0F/event.getRed();

				if(f6 > 1.0F/event.getGreen())
					f6 = 1.0F/event.getGreen();

				if(f6 > 1.0F/event.getBlue())
					f6 = 1.0F/event.getBlue();

				// Forge: fix MC-4647 and MC-10480
				if(Float.isInfinite(f6))
					f6 = Math.nextAfter(f6, 0.0);

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
	public void onRenderOverlayPost(Pre event)
	{
		RayTraceResult mop = ClientUtils.mc().objectMouseOver;
		EntityPlayer player = ClientUtils.mc().player;
		if(player==null)
			return;

		switch(event.getType())
		{
			case HOTBAR:
				onRenderHotbar(event, player, mop);
				break;
			case TEXT:
				onRenderTextOverlay(event, player, mop);
				break;
		}

	}

	private void onRenderTextOverlay(Pre event, EntityPlayer player, RayTraceResult mouseOver)
	{
		//check for light engineer armor upgrade
		gotTheDrip = ItemIIUpgradeableArmor.isArmorWithUpgrade(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
				"technician_gear", "engineer_gear");

		//--- Tooltip Text ---//

		//can only display when looking at an object
		if(mouseOver!=null&&mouseOver.typeOfHit!=Type.MISS)
		{
			TileEntity te = mouseOver.typeOfHit==Type.BLOCK?player.world.getTileEntity(mouseOver.getBlockPos()): null;
			Entity entityHit = mouseOver.entityHit;

			for(TextOverlayBase hud : TEXT_OVERLAYS)
				if(hud.shouldDraw(player, mouseOver, te, entityHit))
				{
					//get parameters
					final String[] text = hud.getText(player, mouseOver, te, entityHit);
					final FontRenderer font = hud.getFontRenderer();
					final int defaultColor = hud.getDefaultFontColor().getPackedARGB();

					//slightly below and right to the crosshair
					final int x = event.getResolution().getScaledWidth()/2+8;
					final int y = event.getResolution().getScaledHeight()/2+8;

					//start drawing text lines
					int i = 0;
					if(text!=null)
					{
						for(String s : text)
							font.drawString(s, x+8, y+i++*font.FONT_HEIGHT, defaultColor, true);
						//one tooltip at once
						break;
					}
				}
		}

		//--- Overlay GUIs ---//

		//Iterate HUD Layers
		//Iterate HUDs
		int width = event.getResolution().getScaledWidth();
		int height = event.getResolution().getScaledHeight();
		if(ClientUtils.mc().gameSettings.showSubtitles)
			height -= 40;
		for(GuiOverlayLayer key : HUDs.keys())
			for(GuiOverlayBase hud : HUDs.get(key))
				if(hud.shouldDraw(player, mouseOver))
				{
					hud.bindHUDTexture();
					hud.draw(player, mouseOver, width, height);
					break;
				}
	}

	private void onRenderHotbar(Pre event, EntityPlayer player, RayTraceResult mouseOver)
	{
		if(!(player.getRidingEntity() instanceof EntityVehicleSeat))
			return;

		event.setCanceled(true);
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
	public void onRenderOverlayPre(Pre event)
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
			boolean pressed = ClientProxy.keybindZoom.isKeyDown();
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

		LogisticTag logiTag = LogisticTag.getLogisticsTagFromStack(stack);
		if(logiTag!=null)
			logiTag.addLogisticsTooltip(event.getToolTip());

		if(ItemNBTHelper.hasKey(stack, "ii_FilledCasing"))
			event.getToolTip().add(TextFormatting.DARK_GRAY+I18n.format(IIReference.DESCRIPTION_KEY+"filled_casing"));

		if(stack.getItem() instanceof IAmmoTypeItem)
			IIAmmoUtils.createAmmoTooltip((IAmmoTypeItem<?, ?>)stack.getItem(), stack, event.getEntity().world, event.getToolTip());
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

		Entity ridingEntity = ClientUtils.mc().player.getRidingEntity();
		if(ridingEntity instanceof EntityVehicleSeat)
		{
			EntityVehicleSeat riding = (EntityVehicleSeat)ridingEntity;
			if(riding.info!=null&&riding.info.passMouseButtonEvent(event))
				event.setCanceled(true);
		}
		else if(ridingEntity instanceof EntityMachinegun)
			if(event.getButton()==1)
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setBoolean("clientMessage", true);
				tag.setBoolean("shoot", event.isButtonstate());
				IIPacketHandler.sendToServer(new MessageEntityNBTSync(ridingEntity, tag));
				event.setCanceled(true);
			}
	}

	/**
	 * Draws an advanced item tooltip that can include images in text
	 */
	@SubscribeEvent()
	public void onRenderTooltip(PostText event)
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
	public void cameraSetup(CameraSetup event)
	{
		EntityPlayer player = ClientUtils.mc().player;
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

		//TODO: 15.11.2025 revisit, fix camera bug at -180/180 degrees
		/*if(player.getRidingEntity() instanceof EntityVehicleSeat)
		{
			EntityVehicleSeat seat = (EntityVehicleSeat)player.getRidingEntity();
			Entity vehicle = seat.getRidingEntity();

			if(vehicle!=null)
			{
				// Smoothly rotate camera's yaw towards vehicle yaw
				float vehicleYaw = vehicle.prevRotationYaw+(vehicle.rotationYaw-vehicle.prevRotationYaw)*(float)partialTicks;
				if(Float.isNaN(vehicleYaw))
					vehicleYaw = 0;
				float playerYaw = player.prevRotationYaw+(player.rotationYaw-player.prevRotationYaw)*(float)partialTicks;
				if(Float.isNaN(playerYaw))
					playerYaw = 0;
				float yawDiff = (vehicleYaw-playerYaw);
				if(Float.isNaN(yawDiff))
					yawDiff = 0;

				event.setYaw((float)MathHelper.wrapDegrees(event.getYaw()+yawDiff));
			}
		}*/

		//Gun recoil
		if(ClientUtils.mc().gameSettings.thirdPersonView==0)
		{
			//--- Gun Recoil Handling ---//

			ItemStack stack = player.getHeldItemMainhand();
			if(stack.getItem() instanceof ItemIIGunBase&&Graphics.cameraRecoil)
			{
				//Prepare variables
				ItemIIGunBase item = (ItemIIGunBase)stack.getItem();
				EasyNBT upgrades = EasyNBT.wrapNBT(item.getUpgrades(stack));

				boolean isAimed = ItemNBTHelper.getInt(stack, ItemIIGunBase.AIMING) > item.getAimingTime(stack, upgrades);
				double recoilH = ItemNBTHelper.getFloat(stack, ItemIIGunBase.RECOIL_H);
				double recoilV = ItemNBTHelper.getFloat(stack, ItemIIGunBase.RECOIL_V);
				float recoilDecay = item.getRecoilDecay(stack, upgrades, isAimed);

				//Calculate recoil decrease
				if(recoilH!=0)
					recoilH = item.getActualRecoil((float)Math.max(recoilH-recoilDecay*partialTicks, 0),
							item.getHorizontalRecoil(stack, upgrades, isAimed));
				if(recoilV!=0)
					recoilV = item.getActualRecoil((float)Math.max(recoilV-recoilDecay*partialTicks, 0),
							item.getVerticalRecoil(stack, upgrades, isAimed));

				event.setPitch((float)(event.getPitch()-recoilV));
				event.setYaw((float)(event.getYaw()+recoilH));
			}
		}
		if(CameraHandler.isEnabled()&&!player.isRiding())
			CameraHandler.setEnabled(false);

		if(Graphics.cameraRoll&&CameraHandler.isEnabled())
			event.setRoll(CameraHandler.getRoll());
	}

	@SubscribeEvent
	public void onRenderHand(RenderSpecificHandEvent event)
	{
		//Check ridden entity for custom hand rendering
		Entity ridden = ClientUtils.mc().player.getRidingEntity();
		if(ridden instanceof EntityVehicleSeat)
		{
			event.setCanceled(true);
			return;
		}

		//Check held stack for custom hand rendering
		ItemStack stack = event.getItemStack();
		ItemStack stackOpposite = ClientUtils.mc().player.getHeldItem(event.getHand()==EnumHand.MAIN_HAND?EnumHand.OFF_HAND: EnumHand.MAIN_HAND);
		if(stack.isEmpty())
			return;
		if(stack.getItem().getTileEntityItemStackRenderer() instanceof ISpecificHandRenderer)
			if(((ISpecificHandRenderer)stack.getItem().getTileEntityItemStackRenderer())
					.doHandRender(stack, event.getHand(), stackOpposite, event.getSwingProgress(), event.getPartialTicks()))
				event.setCanceled(true);

		if(stack.getItem()==IIContent.itemPrintedPage&&PageType.fromStack(stack).useHandSpecialRender())
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
	public void onInitGuiPost(Post event)
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
	public void onWorldLoad(Load event)
	{
		if(!event.getWorld().isRemote)
			return;

		//Reset static variables
		aimingPlayers.clear();
		blockDamageClient.clear();

		DiplomacyUtils.init();

		//Reload the particle system
		ImmersiveIntelligence.proxy.reloadParticles();
	}

	@SubscribeEvent
	public void onWorldUnload(Unload event)
	{
		if(!event.getWorld().isRemote)
			return;
		DiplomacyUtils.unload();
	}

	@SubscribeEvent
	public void onGameRuleChange(GameRuleChangeEvent event)
	{
		EventHandler.applyGameRuleValue(event.getRules(), event.getRuleName());
	}

	@SubscribeEvent
	public void onPostClientTick(ClientTickEvent event)
	{
		if(event.phase==Phase.END)
		{
			if(ParticleSystem.INSTANCE!=null)
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


}
