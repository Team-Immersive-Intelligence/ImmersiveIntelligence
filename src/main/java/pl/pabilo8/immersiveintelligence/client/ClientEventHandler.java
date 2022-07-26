package pl.pabilo8.immersiveintelligence.client;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxReceiver;
import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import blusunrize.immersiveengineering.api.tool.ZoomHandler.IZoomTool;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.items.ItemChemthrower;
import blusunrize.immersiveengineering.common.items.ItemDrill;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.network.MessageRequestBlockUpdate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.FogMode;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.EntityViewRenderEvent.FOVModifier;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import org.lwjgl.opengl.GLContext;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Vehicles.FieldHowitzer;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Vehicles.Motorbike;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.Machinegun;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.Mortar;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.Submachinegun;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.camera.CameraHandler;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.api.utils.IEntityOverlayText;
import pl.pabilo8.immersiveintelligence.api.utils.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.client.render.MachinegunRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.BinocularsRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.MineDetectorRenderer;
import pl.pabilo8.immersiveintelligence.client.render.item.SubmachinegunItemStackRenderer;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.entity.*;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIAmmoGrenade;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIINavalMine;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIIUpgradeableArmor;
import pl.pabilo8.immersiveintelligence.common.items.tools.ItemIIBinoculars;
import pl.pabilo8.immersiveintelligence.common.items.tools.ItemIIMineDetector;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIMachinegun;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIMortar;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIRailgunOverride;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIISubmachinegun;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageEntityNBTSync;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 27-09-2019
 */
public class ClientEventHandler implements ISelectiveResourceReloadListener
{
	private static final String texture_gui = ImmersiveIntelligence.MODID+":textures/gui/hud_elements.png";
	public static LinkedHashMap<EntityLivingBase, Float> gunshotEntities = new LinkedHashMap<>();
	private static boolean mgAiming = false;
	public static ArrayList<EntityLivingBase> aimingPlayers = new ArrayList<>();
	public static GuiScreen lastGui = null;

	public static void drawSubmachinegunGui(ItemStack stack, RenderGameOverlayEvent.Post event)
	{
		ClientUtils.bindTexture(texture_gui);
		int width = event.getResolution().getScaledWidth();
		int height = event.getResolution().getScaledHeight();

		ItemStack magazine = ItemNBTHelper.getItemStack(stack, "magazine");
		//for drums
		if(magazine.getMetadata()==3)
		{
			int bullets = ItemIIBulletMagazine.getRemainingBulletCount(magazine);
			ClientUtils.drawTexturedRect(width-38, height-27, 36, 25, 15/256f, (15+36)/256f, 29/256f, (29+25)/256f);
			if(bullets > 0)
			{
				NonNullList<ItemStack> cartridge = ItemIIBulletMagazine.readInventory(ItemNBTHelper.getTagCompound(magazine, "bullets"), ItemIIBulletMagazine.getBulletCapactity(magazine));
				GlStateManager.pushMatrix();
				GlStateManager.translate(width-38+3, height-17, 0);
				for(int i = 0; i < bullets; i++)
				{
					if(cartridge.get(i).isEmpty())
						continue;
					final int x = ((bullets-i)%2==0?-1: 0);
					ClientUtils.drawTexturedRect(x, 0, 30, 6, 51/256f, (51+30)/256f, 33/256f, (33+6)/256f);
					int cc = IIContent.itemAmmoMachinegun.getPaintColor(cartridge.get(i));
					float[] rgb;
					if(cc!=-1)
					{
						rgb = pl.pabilo8.immersiveintelligence.api.Utils.rgbIntToRGB(cc);
						GlStateManager.color(rgb[0], rgb[1], rgb[2]);
						ClientUtils.drawTexturedRect(10, 0, 4, 6, 61/256f, (65)/256f, 27/256f, (27+6)/256f);
					}
					rgb = pl.pabilo8.immersiveintelligence.api.Utils.rgbIntToRGB(IIContent.itemAmmoMachinegun.getCore(cartridge.get(i)).getColour());
					GlStateManager.color(rgb[0], rgb[1], rgb[2]);
					ClientUtils.drawTexturedRect(24, 0, 6, 6, 75/256f, (81)/256f, 27/256f, (27+6)/256f);
					GlStateManager.color(1f, 1f, 1f);
					if(i < 6)
					{
						GlStateManager.translate(0, -6, 0);
					}
					else if(i < 16)
					{
						GlStateManager.rotate(-9f, 0, 0, 1);
						GlStateManager.translate(0, -4, 0);
					}
					else if(i < 20)
					{
						GlStateManager.translate(0, -5, 0);
					}
					else if(i < 40)
					{
						GlStateManager.rotate(-9f, 0, 0, 1);
						GlStateManager.translate(0, -4, 0);
					}
					else if(i < 52)
					{
						GlStateManager.rotate(-9f, 0, 0, 1);
						GlStateManager.translate(0, -3, 0);
					}
					else if(i < 65)
					{
						GlStateManager.rotate(-15f, 0, 0, 1);
						GlStateManager.translate(0, -3, 0);
					}
				}
				GlStateManager.popMatrix();
			}
			ClientUtils.drawTexturedRect(width-38+10, height-27+8, 16, 15, 51/256f, (51+16)/256f, 39/256f, (44+10)/256f);
			pl.pabilo8.immersiveintelligence.api.Utils.drawStringCentered(ClientUtils.font(),
					String.valueOf(bullets),
					width-38+12, height-27+12,
					12, 0,
					0xffffff
			);
		}
		//for stick
		else
		{
			int bullets = ItemIIBulletMagazine.getRemainingBulletCount(magazine);
			ClientUtils.drawTexturedRect(width-38, height-27, 36, 25, 15/256f, (15+36)/256f, 29/256f, (29+25)/256f);
			if(bullets > 0)
			{
				NonNullList<ItemStack> cartridge = ItemIIBulletMagazine.readInventory(ItemNBTHelper.getTagCompound(magazine, "bullets"), ItemIIBulletMagazine.getBulletCapactity(magazine));
				for(int i = 0; i < bullets; i++)
				{
					if(cartridge.get(i).isEmpty())
						continue;
					final int x = width-38+3+((64-i)%2==0?-1: 0);
					ClientUtils.drawTexturedRect(x, height-17-(i*6), 30, 6, 51/256f, (51+30)/256f, 33/256f, (33+6)/256f);
					int cc = IIContent.itemAmmoMachinegun.getPaintColor(cartridge.get(i));
					float[] rgb;
					if(cc!=-1)
					{
						rgb = pl.pabilo8.immersiveintelligence.api.Utils.rgbIntToRGB(cc);
						GlStateManager.color(rgb[0], rgb[1], rgb[2]);
						ClientUtils.drawTexturedRect(x+10, height-17-(i*6), 4, 6, 61/256f, (65)/256f, 27/256f, (27+6)/256f);
					}
					rgb = pl.pabilo8.immersiveintelligence.api.Utils.rgbIntToRGB(IIContent.itemAmmoMachinegun.getCore(cartridge.get(i)).getColour());
					GlStateManager.color(rgb[0], rgb[1], rgb[2]);
					ClientUtils.drawTexturedRect(x+24, height-17-(i*6), 6, 6, 75/256f, (81)/256f, 27/256f, (27+6)/256f);
					GlStateManager.color(1f, 1f, 1f);

				}
			}
			ClientUtils.drawTexturedRect(width-38+10, height-27+8, 16, 15, 51/256f, (51+16)/256f, 39/256f, (44+10)/256f);
			pl.pabilo8.immersiveintelligence.api.Utils.drawStringCentered(ClientUtils.font(),
					String.valueOf(bullets),
					width-38+12, height-27+12,
					12, 0,
					0xffffff
			);
		}
	}

	public static void drawMachinegunGui(EntityMachinegun mg, RenderGameOverlayEvent.Post event)
	{
		GlStateManager.pushMatrix();
		ClientUtils.bindTexture(texture_gui);
		int width = event.getResolution().getScaledWidth();
		int height = event.getResolution().getScaledHeight();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableAlpha();
		boolean drawWater = mg.tankCapacity > 0, hasShield = mg.maxShieldStrength > 0;

		//Magazine 1
		if(mg.hasSecondMag)
		{
			ClientUtils.drawTexturedRect(width-38, height-27, 36, 25, 15/256f, (15+36)/256f, 29/256f, (29+25)/256f);
			if(mg.bullets2 > 0)
			{
				NonNullList<ItemStack> cartridge = ItemIIBulletMagazine.readInventory(ItemNBTHelper.getTagCompound(mg.magazine2, "bullets"), ItemIIBulletMagazine.getBulletCapactity(mg.magazine2));
				for(int i = 0; i < mg.bullets2; i++)
				{
					if(cartridge.get(i).isEmpty())
						continue;
					final int x = width-38+3+((64-i)%2==0?-1: 0);
					ClientUtils.drawTexturedRect(x, height-17-(i*6), 30, 6, 51/256f, (51+30)/256f, 33/256f, (33+6)/256f);
					int cc = IIContent.itemAmmoMachinegun.getPaintColor(cartridge.get(i));
					float[] rgb;
					if(cc!=-1)
					{
						rgb = pl.pabilo8.immersiveintelligence.api.Utils.rgbIntToRGB(cc);
						GlStateManager.color(rgb[0], rgb[1], rgb[2]);
						ClientUtils.drawTexturedRect(x+10, height-17-(i*6), 4, 6, 61/256f, (65)/256f, 27/256f, (27+6)/256f);
					}
					rgb = pl.pabilo8.immersiveintelligence.api.Utils.rgbIntToRGB(IIContent.itemAmmoMachinegun.getCore(cartridge.get(i)).getColour());
					GlStateManager.color(rgb[0], rgb[1], rgb[2]);
					ClientUtils.drawTexturedRect(x+24, height-17-(i*6), 6, 6, 75/256f, (81)/256f, 27/256f, (27+6)/256f);
					GlStateManager.color(1f, 1f, 1f);

				}
			}
			ClientUtils.drawTexturedRect(width-38+10, height-27+8, 16, 15, 51/256f, (51+16)/256f, 39/256f, (44+10)/256f);
			pl.pabilo8.immersiveintelligence.api.Utils.drawStringCentered(ClientUtils.font(),
					String.valueOf(mg.bullets2),
					width-38+12, height-27+12,
					12, 0,
					0xffffff
			);
			width -= 38;
			ClientUtils.bindTexture(texture_gui);
		}

		ClientUtils.drawTexturedRect(width-38, height-27, 36, 25, 15/256f, (15+36)/256f, 29/256f, (29+25)/256f);
		if(mg.bullets1 > 0)
		{
			NonNullList<ItemStack> cartridge = ItemIIBulletMagazine.readInventory(ItemNBTHelper.getTagCompound(mg.magazine1, "bullets"), ItemIIBulletMagazine.getBulletCapactity(mg.magazine1));
			for(int i = 0; i < mg.bullets1; i++)
			{
				if(cartridge.get(i).isEmpty())
					continue;
				final int x = width-38+3+((64-i)%2==0?-1: 0);
				ClientUtils.drawTexturedRect(x, height-17-(i*6), 30, 6, 51/256f, (51+30)/256f, 33/256f, (33+6)/256f);
				int cc = IIContent.itemAmmoMachinegun.getPaintColor(cartridge.get(i));
				float[] rgb;
				if(cc!=-1)
				{
					rgb = pl.pabilo8.immersiveintelligence.api.Utils.rgbIntToRGB(cc);
					GlStateManager.color(rgb[0], rgb[1], rgb[2]);
					ClientUtils.drawTexturedRect(x+10, height-17-(i*6), 4, 6, 61/256f, (65)/256f, 27/256f, (27+6)/256f);
				}
				rgb = pl.pabilo8.immersiveintelligence.api.Utils.rgbIntToRGB(IIContent.itemAmmoMachinegun.getCore(cartridge.get(i)).getColour());
				GlStateManager.color(rgb[0], rgb[1], rgb[2]);
				ClientUtils.drawTexturedRect(x+24, height-17-(i*6), 6, 6, 75/256f, (81)/256f, 27/256f, (27+6)/256f);
				GlStateManager.color(1f, 1f, 1f);

			}
		}
		ClientUtils.drawTexturedRect(width-38+10, height-27+8, 16, 15, 51/256f, (51+16)/256f, 39/256f, (44+10)/256f);
		pl.pabilo8.immersiveintelligence.api.Utils.drawStringCentered(ClientUtils.font(),
				String.valueOf(mg.bullets1),
				width-38+12, height-27+12,
				12, 0,
				0xffffff
		);
		width -= 38;
		ClientUtils.bindTexture(texture_gui);

		GlStateManager.enableBlend();

		ClientUtils.drawTexturedRect(width-24, height-20, 22, 18, 0/256f, 22/256f, 62/256f, 80/256f);
		ClientUtils.drawGradientRect(width-23, height-3-(int)((mg.overheating/(float)Machinegun.maxOverheat)*16), width-20, height-3, 0xffdf9916, 0x0fba0f0f);
		ClientUtils.drawTexturedRect(width-19, height-19, 16, 16, 16/256f, 32/256f, 0, 16/256f);
		height -= 18;

		if(drawWater)
		{
			FluidStack fluid = mg.tank.getFluid();
			ClientUtils.drawTexturedRect(width-24, height-20, 22, 18, 0/256f, 22/256f, 62/256f, 80/256f);
			if(fluid!=null)
			{
				float hh = 16*((float)mg.tank.getFluidAmount()/(float)mg.tankCapacity);
				ClientUtils.drawRepeatedFluidSprite(fluid, width-23, height-3-hh, 3, hh);
			}
			ClientUtils.bindTexture(texture_gui);
			ClientUtils.drawTexturedRect(width-19, height-19, 16, 16, 0, 16/256f, 0, 16/256f);
			height -= 18;
		}

		if(hasShield)
		{
			ClientUtils.drawTexturedRect(width-24, height-20, 22, 18, 0/256f, 22/256f, 62/256f, 80/256f);
			pl.pabilo8.immersiveintelligence.api.Utils.drawArmorBar(width-23, height-3, 3, 16,
					mg.shieldStrength/mg.maxShieldStrength);
			ClientUtils.drawTexturedRect(width-19, height-19, 16, 16, 32/256f, 48/256f, 0, 16/256f);
			//height-=18;
		}

		/*
		ClientUtils.drawTexturedRect(width-16, height-64, 12, 46, 3/256f, 15/256f, 16/256f, 62/256f);

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
		 */

		//ClientUtils.drawTexturedRect(width-17,height-17,16,16,0,16/256f,0,16/256f);

		GlStateManager.popMatrix();
	}

	@Override
	public void onResourceManagerReload(@Nonnull IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate)
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
		if(event.getEntity() instanceof EntityLivingBase)
		{
			if(((EntityLivingBase)event.getEntity()).getActivePotionEffect(IIPotions.nuclear_heat)!=null)
			{
				PotionEffect effect = ((EntityLivingBase)event.getEntity()).getActivePotionEffect(IIPotions.nuclear_heat);
				assert effect!=null;

				GlStateManager.setFog(FogMode.EXP2);
				GlStateManager.setFogStart(20); //(
				GlStateManager.setFogEnd(24);
				GlStateManager.setFogDensity(.015f);
			}
			if(((EntityLivingBase)event.getEntity()).getActivePotionEffect(IIPotions.suppression)!=null)
			{
				PotionEffect effect = ((EntityLivingBase)event.getEntity()).getActivePotionEffect(IIPotions.suppression);
				assert effect!=null;
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
	}

	@SubscribeEvent()
	public void onFogColourUpdate(EntityViewRenderEvent.FogColors event)
	{
		Entity entity = event.getEntity();
		if(entity instanceof EntityLivingBase&&((EntityLivingBase)entity).isPotionActive(IIPotions.infrared_vision))
		{
			float r = event.getRed(), g = event.getGreen(), b = event.getBlue();
			float f15 = Math.min(Objects.requireNonNull(((EntityLivingBase)entity).getActivePotionEffect(IIPotions.infrared_vision)).getAmplifier(), 4)/4f;
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
		if(entity instanceof EntityLivingBase&&((EntityLivingBase)entity).getActivePotionEffect(IIPotions.suppression)!=null)
		{
			event.setRed(0);
			event.setGreen(0);
			event.setBlue(0);
		}
		if(entity instanceof EntityLivingBase&&((EntityLivingBase)entity).getActivePotionEffect(IIPotions.nuclear_heat)!=null)
		{
			float v = event.getEntity().getEntityWorld().provider.getSunBrightnessFactor(0);
			//float min = Math.min(Math.min(event.getRed(), event.getGreen()), event.getBlue());
			event.setRed(v);
			event.setGreen(v);
			event.setBlue(v);
		}
	}

	@SubscribeEvent
	public void onFOVUpdate(FOVModifier event)
	{
		EntityPlayer player = ClientUtils.mc().player;
		if(player.getLowestRidingEntity() instanceof IEntityZoomProvider&&mgAiming)
		{
			IEntityZoomProvider ridingEntity = (IEntityZoomProvider)player.getLowestRidingEntity();

			if(ridingEntity.getZoom()!=null)
			{
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
			else
				ZoomHandler.isZooming = false;

		}

	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent.Post event)
	{
		RayTraceResult mop = ClientUtils.mc().objectMouseOver;
		EntityPlayer player = ClientUtils.mc().player;

		if(ClientUtils.mc().player!=null&&event.getType()==RenderGameOverlayEvent.ElementType.TEXT)
		{
			boolean gotTheDrip = ItemIIUpgradeableArmor.isArmorWithUpgrade(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
					"technician_gear", "engineer_gear");
			// TODO: 08.01.2022 energy usage

			if(mop!=null)
				if(mop.typeOfHit==Type.BLOCK&&(gotTheDrip||!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()))
				{
					int col = IEConfig.nixietubeFont?Lib.colour_nixieTubeText: 0xffffff;
					String[] text = null;
					TileEntity tileEntity = player.world.getTileEntity(mop.getBlockPos());

					if(tileEntity!=null&&tileEntity.hasCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, mop.sideHit.getOpposite()))
					{
						if(gotTheDrip||pl.pabilo8.immersiveintelligence.api.Utils.isTachometer(player.getHeldItem(EnumHand.MAIN_HAND)))
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
					}
					if(gotTheDrip&&tileEntity instanceof IFluxReceiver)
					{
						int maxStorage = ((IFluxReceiver)tileEntity).getMaxEnergyStored(mop.sideHit);
						int storage = ((IFluxReceiver)tileEntity).getEnergyStored(mop.sideHit);
						if(maxStorage > 0)
							text = I18n.format(Lib.DESC_INFO+"energyStored", "<br>"+Utils.toScientificNotation(storage, "0##", 100000)+" / "+Utils.toScientificNotation(maxStorage, "0##", 100000)).split("<br>");
					}
					if(tileEntity instanceof IUpgradableMachine)
					{
						if(gotTheDrip||pl.pabilo8.immersiveintelligence.api.Utils.isWrench(player.getHeldItem(EnumHand.MAIN_HAND)))
						{
							IUpgradableMachine m = (IUpgradableMachine)tileEntity;
							m = m.getUpgradeMaster();
							if(m!=null&&m.getCurrentlyInstalled()!=null)
							{
								text = new String[]{
										I18n.format(CommonProxy.INFO_KEY+"machineupgrade.name", I18n.format("machineupgrade.immersiveintelligence."+m.getCurrentlyInstalled().getName())),
										I18n.format(CommonProxy.INFO_KEY+"machineupgrade.progress", m.getInstallProgress(), m.getCurrentlyInstalled().getProgressRequired())
								};
							}
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
				else if(mop.typeOfHit==Type.ENTITY)
				{
					String[] text = null;
					boolean useNixie = false;

					if(mop.entityHit instanceof IEntityOverlayText)
					{
						boolean hammer = !player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()&&(gotTheDrip||Utils.isHammer(player.getHeldItem(EnumHand.MAIN_HAND)));
						IEntityOverlayText overlayBlock = (IEntityOverlayText)mop.entityHit;
						text = overlayBlock.getOverlayText(ClientUtils.mc().player, mop, hammer);
						useNixie = overlayBlock.useNixieFont(ClientUtils.mc().player, mop);
					}
					//learn how if statements work, blu! (so I don't have to fix bugs in my mod for you)
					else if(mop.entityHit instanceof IFluxReceiver&&(gotTheDrip||pl.pabilo8.immersiveintelligence.api.Utils.isVoltmeter(player.getHeldItem(EnumHand.MAIN_HAND))))
					{
						int maxStorage = ((IFluxReceiver)mop.entityHit).getMaxEnergyStored(null);
						int storage = ((IFluxReceiver)mop.entityHit).getEnergyStored(null);
						if(maxStorage > 0)
							text = I18n.format(Lib.DESC_INFO+"energyStored", "<br>"+Utils.toScientificNotation(storage, "0##", 100000)+" / "+Utils.toScientificNotation(maxStorage, "0##", 100000)).split("<br>");
						useNixie = true;
					}

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
			if(player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemIISubmachinegun)
				drawSubmachinegunGui(player.getHeldItem(EnumHand.MAIN_HAND), event);
			else if(player.getRidingEntity() instanceof EntityMachinegun)
				drawMachinegunGui((EntityMachinegun)player.getRidingEntity(), event);
			else if(player.getLowestRidingEntity() instanceof EntityMotorbike)
				drawMotorbikeGui((EntityMotorbike)player.getLowestRidingEntity(), event);
			else if(ZoomHandler.isZooming&&player.getRidingEntity() instanceof EntityTripodPeriscope)
				drawTripodGui(event);
		}
	}

	private void drawMotorbikeGui(EntityMotorbike motorbike, Post event)
	{
		int offset = 0;
		EnumHand[] var3 = EnumHand.values();
		EntityPlayer player = ClientUtils.mc().player;

		for(EnumHand hand : var3)
			if(!player.getHeldItem(hand).isEmpty())
			{
				ItemStack equipped = player.getHeldItem(hand);
				if(equipped.getItem() instanceof ItemDrill||equipped.getItem() instanceof ItemChemthrower)
					offset -= 85;
			}

		ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/gui/hud_elements.png");
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		float dx = (float)(event.getResolution().getScaledWidth()-16);
		float dy = (float)event.getResolution().getScaledHeight();
		GlStateManager.pushMatrix();
		GlStateManager.translate(dx, dy+(float)offset, 0.0D);
		double uMin = 0;
		double uMax = 53/256d;
		double vMin = 80/256d;
		double vMax = (80+72)/256d;
		ClientUtils.drawTexturedRect(-41.0F+17f, -73.0F+5f, 31, 62, 54/256d, (54+31)/256d, 80/256d, (80+62)/256d);

		int capacity = motorbike.tank.getCapacity();
		int amount = motorbike.tank.getFluidAmount();
		float cap = (float)capacity;
		float angle = 83.0F-(float)(166*amount)/cap;

		GlStateManager.pushMatrix();
		GlStateManager.translate(-23.0D, -37.0D, 0.0D);
		GlStateManager.rotate(angle, 0, 0, 1);
		ClientUtils.drawTexturedRect(-3F+8, -1.5f, 24, 5, 0/256d, 32/256d, 152/256d, (152+7)/256d);
		GlStateManager.popMatrix();

		ClientUtils.drawTexturedRect(-41.0F, -73.0F, 53, 72, uMin, uMax, vMin, vMax);

		ClientUtils.drawTexturedRect(-51.0F, -73.0F, 22, 18, 22/256f, 44/256f, 62/256f, 80/256f);
		ClientUtils.drawTexturedRect(-51.0F+5, -73.0F+1, 16, 16, 48/256f, 64/256f, 0/256f, 16/256f);
		pl.pabilo8.immersiveintelligence.api.Utils.drawArmorBar(-51+1, -73+1, 3, 16,
				motorbike.frontWheelDurability/(float)Motorbike.wheelDurability);

		ClientUtils.drawTexturedRect(-59.0F, -73.0F+18, 22, 18, 22/256f, 44/256f, 62/256f, 80/256f);
		ClientUtils.drawTexturedRect(-59.0F+5, -73.0F+18+1, 16, 16, 80/256f, 96/256f, 0/256f, 16/256f);
		pl.pabilo8.immersiveintelligence.api.Utils.drawArmorBar(-59+1, -73+18+1, 3, 16,
				motorbike.engineDurability/(float)Motorbike.engineDurability);

		ClientUtils.drawTexturedRect(-59.0F, -73.0F+18*2, 22, 18, 22/256f, 44/256f, 62/256f, 80/256f);
		ClientUtils.drawTexturedRect(-59.0F+5, -73.0F+18*2+1, 16, 16, 96/256f, 112/256f, 0/256f, 16/256f);
		pl.pabilo8.immersiveintelligence.api.Utils.drawArmorBar(-59+1, -73+18*2+1, 3, 16,
				motorbike.fuelTankDurability/(float)Motorbike.fuelTankDurability);

		ClientUtils.drawTexturedRect(-51.0F, -73.0F+18*3, 22, 18, 22/256f, 44/256f, 62/256f, 80/256f);
		ClientUtils.drawTexturedRect(-51.0F+5, -73.0F+18*3+1, 16, 16, 64/256f, 80/256f, 0/256f, 16/256f);
		pl.pabilo8.immersiveintelligence.api.Utils.drawArmorBar(-51+1, -73+18*3+1, 3, 16,
				motorbike.backWheelDurability/(float)Motorbike.wheelDurability);


		GlStateManager.popMatrix();
	}

	private void drawTripodGui(RenderGameOverlayEvent.Post event)
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

		Entity ridingEntity = ClientUtils.mc().player.getLowestRidingEntity();

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
					if(entityZoomProvider.getZoom()!=null)
					{
						if(!entityZoomProvider.getZoom().canZoom(entityZoomProvider.getZoomStack(), ClientUtils.mc().player))
						{
							ZoomHandler.isZooming = false;
							break dothing;
						}
						ClientUtils.bindTexture(entityZoomProvider.getZoom().getZoomOverlayTexture(entityZoomProvider.getZoomStack(), ClientUtils.mc().player));
					}
					else
						ZoomHandler.isZooming = false;
				}
				else
					ClientUtils.bindTexture(((IAdvancedZoomTool)stack.getItem()).getZoomOverlayTexture(stack, ClientUtils.mc().player));

				if(!ZoomHandler.isZooming)
					return;

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
				else if(ridingEntity instanceof EntityMortar)
				{
					EntityMortar mg = (EntityMortar)ridingEntity;
					if(mg.shootingProgress!=0)
					{
						ZoomHandler.isZooming = false;
						ZoomHandler.fovZoom = 0;
					}

					CameraHandler.INSTANCE.setCameraPos(mg.posX, mg.posY+0.75, mg.posZ);
					CameraHandler.INSTANCE.setCameraAngle(mg.rotationYaw, 1+(((1f-(mg.rotationPitch/-90f))*-1.5f)), 0);
					CameraHandler.INSTANCE.setEnabled(mg.shootingProgress==0);
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
		if(ItemNBTHelper.hasKey(event.getItemStack(), "ii_FilledCasing"))
			event.getToolTip().add(I18n.format(CommonProxy.DESCRIPTION_KEY+"filled_casing"));
		if(ItemNBTHelper.hasKey(event.getItemStack(), IIContent.NBT_AdvancedPowerpack))
		{
			ItemStack powerpack = ItemNBTHelper.getItemStack(event.getItemStack(), IIContent.NBT_AdvancedPowerpack);
			if(!powerpack.isEmpty())
			{
				event.getToolTip().add(TextFormatting.GRAY+powerpack.getDisplayName());
				event.getToolTip().add(TextFormatting.GRAY.toString()+EnergyHelper.getEnergyStored(powerpack)+"/"+EnergyHelper.getMaxEnergyStored(powerpack)+" IF");
			}
		}
		if(event.getItemStack().getItem() instanceof ItemIIBulletMagazine)
		{
			if(ItemNBTHelper.hasKey(event.getItemStack(), "bullets"))
			{
				NBTTagList listDict = ItemNBTHelper.getTagCompound(event.getItemStack(), "bullets").getTagList("dictionary", 10);

				if(ItemNBTHelper.getTag(event.getItemStack()).hasKey("bullet0"))
					event.getToolTip().add("   "+new ItemStack(listDict.getCompoundTagAt(0)).getDisplayName());
				if(ItemNBTHelper.getTag(event.getItemStack()).hasKey("bullet1"))
					event.getToolTip().add("   "+new ItemStack(listDict.getCompoundTagAt(1)).getDisplayName());
				if(ItemNBTHelper.getTag(event.getItemStack()).hasKey("bullet2"))
					event.getToolTip().add("   "+new ItemStack(listDict.getCompoundTagAt(2)).getDisplayName());
				if(ItemNBTHelper.getTag(event.getItemStack()).hasKey("bullet3"))
					event.getToolTip().add("   "+new ItemStack(listDict.getCompoundTagAt(3)).getDisplayName());

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
		if(CameraHandler.INSTANCE.isEnabled()&&!ClientUtils.mc().player.isRiding())
			CameraHandler.INSTANCE.setEnabled(false);

		if(CameraHandler.INSTANCE.isEnabled())
		{
			event.setRoll(CameraHandler.INSTANCE.getRoll());
		}
	}

	@SuppressWarnings("unused")
	public static void handleBipedRotations(ModelBiped model, Entity entity)
	{
		if(entity instanceof EntityLivingBase&&((EntityLivingBase)entity).isPotionActive(IIPotions.concealed))
		{
			model.setVisible(false);
		}

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

				IBlockState state = player.world.getBlockState(player.getPosition());
				if(!mg.tripod&&state.getMaterial().isSolid()&&!(state.getBlock()==IIContent.blockMetalDevice&&state.getValue(IIContent.blockMetalDevice.property)==IIBlockTypes_MetalDevice.AMMUNITION_CRATE))
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
			else if(ridingEntity instanceof EntityMortar)
			{
				EntityMortar mortar = ((EntityMortar)ridingEntity);

				float ff = 1;
				if(mortar.shootingProgress > 0)
				{
					float v = mortar.shootingProgress/Mortar.shootTime;
					if(v < 0.1)
					{
						//rise up
						ff = 1f-(v/0.1f);
					}
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

						float firing = 1f-((v-0.2f)/0.1f);

						model.bipedLeftArm.rotateAngleY = firing*0.15f;
						model.bipedRightArm.rotateAngleY = firing*-0.65f;
						model.bipedRightArm.rotationPointX += 1*firing;
						model.bipedRightArm.rotationPointZ -= 2*firing;

						model.bipedLeftArm.rotateAngleX = firing*-2.15f;
						model.bipedRightArm.rotateAngleX = firing*-2.15f;
					}
					else if(v < 0.4)
					{
						//get down
						ff = (v-0.3f)/0.1f;
					}
					else
					{
						float firing = (v-0.4f)/0.6f;
						float progress = MathHelper.clamp(firing < 0.75?firing/0.2f: 1f-((firing-0.85f)/0.15f), 0, 1);
						model.bipedHead.rotateAngleX = Math.min((progress/0.85f), 1f)*0.85f;
						model.bipedHeadwear.rotateAngleX = Math.min((progress/0.85f), 1f)*0.85f;
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
				EntityTripodPeriscope mg = ((EntityTripodPeriscope)ridingEntity);
				float true_head_angle = MathHelper.wrapDegrees(player.prevRotationYawHead);

				float partialTicks = ClientUtils.mc().getRenderPartialTicks();
				float wtime = (Math.abs((((entity.getEntityWorld().getTotalWorldTime()+partialTicks)%40)/40f)-0.5f)/0.5f)-0.5f;

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
						model.bipedRightLeg.rotateAngleZ = (-(1f-wtime)*0.25f);
						model.bipedLeftLeg.rotateAngleZ = (-(wtime)*0.25f-0.25f);
					}
					else if(mg.periscopeYaw > true_head_angle)
					{
						model.bipedRightLeg.rotateAngleZ = ((1f-wtime)*0.25f+0.25f);
						model.bipedLeftLeg.rotateAngleZ = ((wtime)*0.25f);
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
				EntityVehicleSeat seat = ((EntityVehicleSeat)ridingEntity);
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
						float progress = MathHelper.clamp(((howitzer.setupTime+
								(partialTicks*(howitzer.turnLeft||howitzer.turnRight||howitzer.forward||howitzer.backward?1: -1))
						)/(FieldHowitzer.setupTime*0.2f)), 0, 1);

						float wtime = (Math.abs((((entity.getEntityWorld().getTotalWorldTime()+partialTicks)%30)/30f)-0.5f)/0.5f);
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
								model.bipedRightLeg.rotateAngleZ = (-(1f-wtime)*0.25f)*stime;
								model.bipedLeftLeg.rotateAngleZ = (-(wtime)*0.25f-0.25f)*stime;
							}
							else if(howitzer.turnRight||howitzer.backward)
							{
								model.bipedRightLeg.rotateAngleZ = ((1f-wtime)*0.25f+0.25f)*stime;
								model.bipedLeftLeg.rotateAngleZ = ((wtime)*0.25f)*stime;
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
								model.bipedLeftLeg.rotateAngleZ = (-(wtime)*0.25f)*stime;
							}
							else if(howitzer.turnRight||howitzer.forward)
							{
								model.bipedRightLeg.rotateAngleZ = ((1f-wtime)*0.25f)*stime;
								model.bipedLeftLeg.rotateAngleZ = ((wtime)*0.25f-0.25f)*stime;
							}
						}
					}
					else if(reloading > 0)
					{
						if(seat.seatID==0)
						{
							if(reloading < 0.2)
								model.bipedLeftArm.rotateAngleZ = (1f-(reloading/0.2f))*-1.25f;

							if(reloading > 0.2f)
							{
								float ff = 0;
								if(reloading < 0.4f)
									ff = (((reloading-0.2f)/0.2f));
								else if(reloading < 0.5f)
									ff = (1f-((reloading-0.4f)/0.1f));
								model.bipedBody.rotateAngleX = ff*0.25f;
								model.bipedLeftLeg.rotationPointZ = ff*3;
								model.bipedRightLeg.rotationPointZ = ff*3;
								model.bipedRightArm.rotateAngleX = ff*-1.25f;
								model.bipedRightArm.rotateAngleZ = ff*-1.5f;
							}

							if(reloading < 0.1)
								model.bipedLeftArm.rotateAngleX = (reloading/0.1f)*-1.25f;
							else if(reloading < 0.9)
								model.bipedLeftArm.rotateAngleX = -1.25f;
							else
								model.bipedLeftArm.rotateAngleX = (1f-((reloading-0.9f)/0.1f))*-1.25f;
						}
					}
					else if(firing > 0)
					{
						if(seat.seatID==1)
						{
							float progress = MathHelper.clamp(firing < 0.75?firing/0.2f: 1f-((firing-0.85f)/0.15f), 0, 1);
							model.isSneak = true;
							model.bipedHead.rotateAngleX = Math.min((progress/0.85f), 1f)*0.15f;
							model.bipedHeadwear.rotateAngleX = Math.min((progress/0.85f), 1f)*0.15f;
							model.bipedHead.rotateAngleY = 0;
							model.bipedHeadwear.rotateAngleY = 0;

							model.bipedBody.rotateAngleX += progress*0.25f;

							model.bipedLeftArm.rotateAngleX = progress*-3f;
							model.bipedRightArm.rotateAngleX = progress*-3f;
							model.bipedLeftLeg.rotateAngleX = Math.min((progress/0.65f), 1f)*-0.35f;

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
								model.bipedRightArm.rotateAngleX = -1.65f+(0.8f*((firing-0.1f)/0.1f));
							else if(firing < 0.3)
								model.bipedRightArm.rotateAngleX = -0.85f*(1f-((firing-0.2f)/0.1f));
							else
							{
								firing = (firing-0.3f)/0.7f;
								float progress = MathHelper.clamp(firing < 0.75?firing/0.2f: 1f-((firing-0.85f)/0.15f), 0, 1);
								model.isSneak = true;
								model.bipedHead.rotateAngleX = Math.min((progress/0.85f), 1f)*0.15f;
								model.bipedHeadwear.rotateAngleX = Math.min((progress/0.85f), 1f)*0.15f;
								model.bipedHead.rotateAngleY = 0;
								model.bipedHeadwear.rotateAngleY = 0;

								model.bipedLeftArm.rotateAngleX = progress*-3f;
								model.bipedRightArm.rotateAngleX = progress*-3f;
								model.bipedRightLeg.rotateAngleX = Math.min((progress/0.65f), 1f)*0.35f;

								model.bipedRightLeg.rotationPointZ = 5f;
								model.bipedLeftLeg.rotationPointZ = 5f;
							}

						}
					}
					else if(seat.seatID==0&&(howitzer.gunPitchDown||howitzer.gunPitchUp))
					{
						float limbSwing = Math.abs(((tt%8)-4)/8f);
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
						else if(heldItem.getItem() instanceof ItemIIMortar)
						{
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
						}
						else if((heldItem.getItem() instanceof ItemIISubmachinegun||heldItem.getItem() instanceof ItemIIRailgunOverride)&&hand!=EnumHand.OFF_HAND)
						{
							if(right)
							{
								player.setRenderYawOffset(player.rotationYawHead);
								boolean rail = heldItem.getItem() instanceof ItemIIRailgunOverride;

								model.bipedRightArm.rotateAngleX = -1.65f+model.bipedHead.rotateAngleX;
								model.bipedLeftArm.rotateAngleX = -1.65f+model.bipedHead.rotateAngleX+0.0625f;

								//-1.5707964 up
								//0 middle
								//1.5707964 down
								float v = (model.bipedHead.rotateAngleX+1.5707964f)/3.1415927f;

								model.bipedRightArm.rotateAngleY += pl.pabilo8.immersiveintelligence.api.Utils.clampedLerp3Par(0, -0.45f, 0f, v);
								model.bipedRightArm.rotateAngleZ += pl.pabilo8.immersiveintelligence.api.Utils.clampedLerp3Par(0.25f, 0, -0.45f, v);
								model.bipedLeftArm.rotateAngleZ += pl.pabilo8.immersiveintelligence.api.Utils.clampedLerp3Par(rail?-0.25f: -0.65f, 0, rail?0.25f: 0.65f, v);
								model.bipedLeftArm.rotateAngleY += pl.pabilo8.immersiveintelligence.api.Utils.clampedLerp3Par(0f, rail?0.25f: 0.7f, 0f, v);

								model.bipedLeftArm.rotationPointX += pl.pabilo8.immersiveintelligence.api.Utils.clampedLerp3Par(-2f, -1f, -2f, v);
								model.bipedLeftArm.rotationPointZ += pl.pabilo8.immersiveintelligence.api.Utils.clampedLerp3Par(0, -2f, 0, v);

								model.bipedRightArm.rotationPointZ += pl.pabilo8.immersiveintelligence.api.Utils.clampedLerp3Par(0, 2f, 0, v);


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
							float v = MineDetectorRenderer.instance.renderBase(player, 2.125f, true);

							model.bipedRightArm.rotateAngleY = model.bipedBody.rotateAngleY-0.45f;
							model.bipedLeftArm.rotateAngleY = model.bipedBody.rotateAngleY+0.45f;

							model.bipedRightArm.rotateAngleX = -1.25f-((1f-v)*0.5f);
							model.bipedLeftArm.rotateAngleX = model.bipedRightArm.rotateAngleX;
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
						else if(heldItem.getItem() instanceof ItemIIAmmoGrenade)
						{
							float use = 1f-MathHelper.clamp(((EntityLivingBase)entity).getItemInUseCount()/(float)heldItem.getMaxItemUseDuration(), 0, 1);
							if(right)
							{
								model.rightArmPose = ArmPose.EMPTY;
								//model.leftArmPose=ArmPose.EMPTY;
								float hh = -(4.5f-model.bipedHead.rotateAngleX);
								model.bipedRightArm.rotateAngleX = use!=1?(use > 0f?(use < 0.35f?((use/0.35f)*hh): hh): 0f): 0f;
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
					if(heldItem.getItem()==IIContent.itemSubmachinegun)
					{
						//float recoilH = ItemNBTHelper.getFloat(heldItem, "recoilH");
						//float recoilV = ItemNBTHelper.getFloat(heldItem, "recoilV");

						Vec3d vec =
								pl.pabilo8.immersiveintelligence.api.Utils.getVectorForRotation(player.rotationPitch, player.getRotationYawHead())
										.scale(-1);

						double true_angle = Math.toRadians((-player.getRotationYawHead()) > 180?360f-(-player.getRotationYawHead()): (-player.getRotationYawHead()));
						double true_angle2 = Math.toRadians((-player.getRotationYawHead()-90) > 180?360f-(-player.getRotationYawHead()-90): (-player.getRotationYawHead()-90));

						Vec3d pos1_x = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(-model.bipedRightArm.rotationPointZ/16f, true_angle, 0);
						Vec3d pos1_z = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(-model.bipedRightArm.rotationPointX/16f, true_angle2, 0);

						Vec3d vv = player.getPositionVector()
								.addVector(0, (24.0F-model.bipedRightArm.rotationPointY)/16f, 0)
								.add(pos1_x).add(pos1_z)
								.add(vec.scale(-1.25f));//
						//.add(arm.rotatePitch(-90f).scale(0.5f));

						ParticleUtils.spawnGunfireFX(vv.x, vv.y, vv.z, vec.x, vec.y, vec.z, v);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderHand(RenderSpecificHandEvent event)
	{
		if(event.getHand()==EnumHand.MAIN_HAND)
		{
			ItemStack stack = event.getItemStack();
			if(stack.getItem() instanceof ItemIIMineDetector)
			{
				MineDetectorRenderer.instance.renderBase(ClientUtils.mc().player, 2.125f, false);
				event.setCanceled(true);
			}
			else if(stack.getItem() instanceof ItemIISubmachinegun)
			{
				int aiming = ItemNBTHelper.getInt(stack, "aiming");
				int reloading = ItemNBTHelper.getInt(stack, "reloading");
				int maxReload = (ItemNBTHelper.getBoolean(stack, "isDrum")?Submachinegun.drumReloadTime: Submachinegun.clipReloadTime);
				float reload = MathHelper.clamp(
						reloading+(reloading > 0?event.getPartialTicks(): 0),
						0,
						maxReload
				);
				reload /= maxReload;

				boolean foldingStock = IIContent.itemSubmachinegun.getUpgrades(stack).hasKey("folding_stock");
				float preciseAim = MathHelper.clamp(
						aiming+(aiming > 0?(Minecraft.getMinecraft().player.isSneaking()?event.getPartialTicks(): -3*event.getPartialTicks()): 0),
						0,
						foldingStock?Submachinegun.aimTimeFoldedStock: Submachinegun.aimTime
				);
				preciseAim /= foldingStock?Submachinegun.aimTimeFoldedStock: Submachinegun.aimTime;

				GlStateManager.pushMatrix();
				GlStateManager.color(1f, 1f, 1f, 1f);
				GlStateManager.translate(11.5/16f, -11/16f, -1.25+2/16f);
				GlStateManager.rotate(2f, 1, 0, 0);
				GlStateManager.rotate(8.5f, 0, 1, 0);

				if(event.getSwingProgress() > 0)
					GlStateManager.translate(0, 0, -0.5f*(1f-Math.abs((event.getSwingProgress()-0.5f)/0.5f)));

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
			else if(stack.getItem() instanceof ItemIIMineDetector)
			{
				MineDetectorRenderer.instance.renderBase(ClientUtils.mc().player, 2.125f, true);
			}
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		aimingPlayers.clear();
	}

	@SubscribeEvent
	public void onPostClientTick(ClientTickEvent event)
	{
		if(event.phase==Phase.END)
			ParticleUtils.particleRenderer.updateParticles();
	}

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event)
	{
		ParticleUtils.particleRenderer.renderParticles(event.getPartialTicks());
	}
}
