package pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.SkycrateStation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryUtils;
import pl.pabilo8.immersiveintelligence.api.utils.ISkycrateMount;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden.ModelSkyCrateStation;
import pl.pabilo8.immersiveintelligence.client.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCrateStation;

import java.util.Set;

/**
 * Created by Pabilo8 on 2019-06-01.
 */
public class SkyCrateStationRenderer extends TileEntitySpecialRenderer<TileEntitySkyCrateStation>
{
	private static ModelSkyCrateStation model = new ModelSkyCrateStation();
	private static TileEntityBanner banner = new TileEntityBanner();
	private static ModelBanner modelBanner = new ModelBanner();
	private static String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/skycrate_station.png";

	static
	{
		modelBanner.bannerStand.isHidden = true;
		modelBanner.bannerSlate.isHidden = false;
		modelBanner.bannerTop.isHidden = true;
	}

	@Override
	public void render(TileEntitySkyCrateStation te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		if(te!=null&&!te.isDummy())
		{
			ClientUtils.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x+1, (float)y-1, (float)z);
			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			double ticks = Math.min(1, ((te.getWorld().getTotalWorldTime()+partialTicks)%100)/100/0.75);

			float rpm_pitch = 0, rpm_grab = 0, rpm_crate = 0, rpm_gears = 0;
			double motorTick = (te.getWorld().getTotalWorldTime()+partialTicks%RotaryUtils.getRPMMax())/RotaryUtils.getRPMMax();
			float progress = te.progress+(partialTicks*te.getEffectiveEnergy()*RotaryUtils.getGearEffectiveness(te.getInventory(), te.getEfficiencyMultiplier()));

			double railBlock = 0, pistonDoor = 0, pistonOnly = 0, cratePusher = 0, inserterAngle = 0, inserterLength = 0;
			//Math.abs(Math.min(Math.max(((Math.abs(ticks-0.5f)*2f)-0.5)/0.5, 0)/0.25, 1))
			double animProgress = 0;
			rpm_gears = te.rotation.getRotationSpeed();
			switch(te.animation)
			{
				case 0:
				{
					railBlock = 0;
					break;
				}
				case 1:
				{

					railBlock = 1;
					break;
				}
				case 2:
				{
					animProgress = progress/SkycrateStation.minecartInTime;
					railBlock = (progress > 0.5)?animProgress: 0;
					break;
				}
				case 3:
				{
					animProgress = progress/SkycrateStation.minecartOutTime;
					railBlock = 1f-Math.min(1, animProgress/0.35);
					double angle = Math.abs(animProgress-0.5d)/0.5;
					double angle2 = MathHelper.clamp(Math.abs(animProgress-0.65d)/0.15, 0, 1);
					pistonDoor = angle > 0.5?1f-((angle-0.5)/0.5): 1;
					pistonOnly = angle2 > 0.5?1f-((angle2-0.5)/0.5): 1;
					break;
				}
				case 4:
				{
					animProgress = progress/SkycrateStation.outputTime;
					railBlock = 1;

					rpm_grab = animProgress <= 0.3f?60f:
							animProgress <= 0.5f?60f-(60f*((float)animProgress-0.3f)/0.2f):
									animProgress <= 0.8d?0:
											-60f;

					rpm_pitch = animProgress <= 0.3f?45f:
							animProgress <= 0.8f?-45f:
									45f;

					inserterAngle = animProgress <= 0.3d?Math.min(0.5, animProgress/0.3*0.65d):
							animProgress <= 0.8d?0.65d-((animProgress-0.3d)/0.5d*1.65d):
									-1.25d+((animProgress-0.8d)/0.2d*1.25d);
					inserterLength = animProgress <= 0.3d?animProgress/0.3d:
							animProgress <= 0.5d?1d-(((animProgress-0.3d)/0.2d)*0.75):
									animProgress <= 0.8d?0.25d:
											(1d-((animProgress-0.8f)/0.3d))*0.25;

					break;
				}
				case 5:
				{
					animProgress = progress/SkycrateStation.inputTime;
					railBlock = 1;

					inserterAngle = animProgress <= 0.15d?animProgress/0.15*-1.25:
							animProgress <= 0.65d?-1.25+((animProgress-0.15)/0.5*2.35):
									animProgress <= 0.6d?1.25-((animProgress-0.65)/0.1*0.75):
											0.65*(1f-((animProgress-0.75)/0.25));

					inserterLength = animProgress <= 0.15?animProgress/0.15d*0.25:
							animProgress <= 0.65?0.25+((animProgress-0.15)/0.5*0.75):
									animProgress <= 0.75?1: 1-((animProgress-0.75)/0.25);

					rpm_pitch = animProgress <= 0.15?-60f:
							animProgress <= 0.65?60f:
									animProgress <= 0.75?-60f: -80f;

					rpm_grab = animProgress <= 0.15?35f:
							animProgress <= 0.65?70f:
									animProgress <= 0.75?0f: -80f;

					cratePusher = animProgress <= 0.75?0:
							animProgress <= 0.95?(animProgress-0.75)/0.2:
									1-((animProgress-0.95)/0.05);

					rpm_crate = animProgress <= 0.75?0:
							animProgress <= 0.95?35:
									250;
					break;
				}
			}

			model.getBlockRotation(te.facing, model);
			model.render();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0.65625f, -0.625f);
			GlStateManager.rotate(360f*(float)motorTick*rpm_pitch, 1, 0, 0);
			for(ModelRendererTurbo mod : model.backAxleModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0.65625f, -0.375f);
			GlStateManager.rotate(360f*(float)motorTick*rpm_grab, 1, 0, 0);
			for(ModelRendererTurbo mod : model.frontAxleModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.59375f, 2.84375f-0.0625f, 0.1875f);
			GlStateManager.rotate(360f*(float)motorTick*rpm_crate, 1, 0, 0);
			for(ModelRendererTurbo mod : model.cratePusherAxleModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, -cratePusher*0.425f);
			for(ModelRendererTurbo mod : model.cratePusherModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, pistonDoor*0.75f, 0);
			for(ModelRendererTurbo mod : model.pistonDoorModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(pistonOnly*0.5f, 0f, 0);
			for(ModelRendererTurbo mod : model.pistonModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(1.8125f, 0.71875f, 0.5f);
			GlStateManager.rotate(90f*(float)railBlock, 1, 0, 0);
			for(ModelRendererTurbo mod : model.trainBlockerModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();


			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 21f/16f, -7f/16f);
			GlStateManager.rotate(90f*(float)inserterAngle, 1, 0, 0);
			GlStateManager.translate(0, 2f/16f, 2f/16f);
			for(ModelRendererTurbo mod : model.inserterBaseModel)
				mod.render(0.0625f);
			GlStateManager.translate(0, -(1f-inserterLength)*0.5, 0);
			for(ModelRendererTurbo mod : model.inserterTopModel)
				mod.render(0.0625f);
			GlStateManager.translate(0, -(1f-inserterLength)*0.5, 0);
			for(ModelRendererTurbo mod : model.inserterTopperModel)
				mod.render(0.0625f);

			if(te.animation==4&&animProgress > 0.2&&animProgress < 0.8&&te.mount.getItem() instanceof ISkycrateMount)
			{
				ISkycrateMount mount = (ISkycrateMount)te.mount.getItem();

				GlStateManager.translate(0.5, 21f/16f+(1f-inserterLength)-0.625f+inserterLength, -0.125);
				GlStateManager.rotate(-90f*(float)inserterAngle, 1, 0, 0);
				GlStateManager.translate(0, -1, 0);
				GlStateManager.scale(0.85, 0.85, 0.85);
				mount.render(te.mount, getWorld(), partialTicks, -1);
			}
			else if(te.animation==5&&animProgress > 0.15&&animProgress <= 0.75&&te.mount.getItem() instanceof ISkycrateMount)
			{
				ISkycrateMount mount = (ISkycrateMount)te.mount.getItem();

				GlStateManager.translate(0.5, 21f/16f+(1f-inserterLength)-0.625f+inserterLength, -0.125);
				GlStateManager.rotate(-90f*(float)inserterAngle, 1, 0, 0);
				GlStateManager.translate(0, -1, 0);
				GlStateManager.scale(0.85, 0.85, 0.85);
				mount.render(te.mount, getWorld(), partialTicks, -1);

				if(animProgress >= 0.65)
				{
					GlStateManager.translate(0, 0.5, 0);
					ClientUtils.mc().getRenderItem().renderItem(te.crate, TransformType.NONE);
				}
			}

			GlStateManager.popMatrix();


			GlStateManager.pushMatrix();
			GlStateManager.translate(1.25+0.0625, 1.875, -0.125);

			GlStateManager.pushMatrix();
			GlStateManager.scale(0.55, 0.55, 0.55);
			GlStateManager.rotate(-360f*(float)motorTick*rpm_gears, 0, 0, 1);
			ClientUtils.mc().getRenderItem().renderItem(te.getInventory().get(1), TransformType.NONE);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.125+0.0625, 0.425, 0);
			GlStateManager.scale(0.55, 0.55, 0.55);
			GlStateManager.rotate(360f*(float)motorTick*rpm_gears, 0, 0, 1);
			ClientUtils.mc().getRenderItem().renderItem(te.getInventory().get(2), TransformType.NONE);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.125+0.0625, -0.425, 0);
			GlStateManager.scale(0.55, 0.55, 0.55);
			GlStateManager.rotate(360f*(float)motorTick*rpm_gears, 0, 0, 1);
			ClientUtils.mc().getRenderItem().renderItem(te.getInventory().get(0), TransformType.NONE);
			GlStateManager.popMatrix();

			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();

			Set<Connection> conns = ImmersiveNetHandler.INSTANCE.getConnections(getWorld(), te.getBlockPosForPos(te.getConnectionPos()[0]));
			if(conns!=null&&conns.size() > 0)
			{
				Connection conn = (Connection)conns.toArray()[0];
				float[] col = Utils.rgbIntToRGB(conn.cableType.getColour(conn));
				double diam = conn.cableType.getRenderDiameter();
				GlStateManager.pushMatrix();
				ClientUtils.bindTexture("immersiveengineering:textures/blocks/wire.png");
				GlStateManager.color(col[0], col[1], col[2]);
				GlStateManager.disableCull();
				GlStateManager.rotate(90, 0, 1, 0);
				GlStateManager.translate(-1.5, 2.5625, 0.5);
				GlStateManager.disableLighting();
				GlStateManager.rotate(2.5f, 0, 0, 1);
				GlStateManager.translate(0, -0.03125, 0);

				ClientUtils.drawTexturedRect(0, (float)diam, 1.125f, (float)diam, 0, 10/16f, 0f, 10/16f);
				GlStateManager.rotate(90, 1, 0, 0);
				GlStateManager.translate(0, -diam*1.5, -diam*1.5);
				ClientUtils.drawTexturedRect(0, (float)diam, 1.125f, (float)diam, 0, 10/16f, 0f, 10/16f);

				GlStateManager.enableLighting();
				GlStateManager.color(1f, 1f, 1f);
				GlStateManager.enableCull();

				GlStateManager.popMatrix();

				if(te.mount.getItem() instanceof ISkycrateMount)
				{
					boolean renderCrate = true, renderMount = true;
					ISkycrateMount mount = (ISkycrateMount)te.mount.getItem();
					GlStateManager.translate(0.5, 1.625+0.0625, 1.5);
					GlStateManager.scale(0.85, 0.85, 0.85);
					if(te.animation==4)
					{
						if(animProgress < 0.3)
						{
							GlStateManager.pushMatrix();
							GlStateManager.translate(0, 0, Math.min(1, (animProgress-0.2)/0.1)*-0.25);
							mount.render(te.mount, getWorld(), partialTicks, -1);
							GlStateManager.popMatrix();

						}
						GlStateManager.translate(0, 0.5, 0);
						GlStateManager.translate(0, -1.5*(MathHelper.clamp((animProgress-0.25)/0.1, 0, 1)), 0);
						ClientUtils.mc().getRenderItem().renderItem(te.crate, TransformType.NONE);
					}
					else if(te.animation==5)
					{
						if(animProgress > 0.75)
						{
							mount.render(te.mount, getWorld(), partialTicks, -1);
							GlStateManager.translate(0, 0.5, 0);
							ClientUtils.mc().getRenderItem().renderItem(te.crate, TransformType.NONE);
						}
						else if(animProgress < 0.65)
						{
							GlStateManager.translate(0, -1.25, 0);
							ClientUtils.mc().getRenderItem().renderItem(te.crate, TransformType.NONE);
						}
					}
					else
					{
						mount.render(te.mount, getWorld(), partialTicks, -1);
						GlStateManager.translate(0, 0.5, 0);
						ClientUtils.mc().getRenderItem().renderItem(te.crate, TransformType.NONE);
					}
				}

			}

			GlStateManager.popMatrix();

			if(!te.banner.isEmpty())
			{
				GlStateManager.pushMatrix();
				banner.setItemValues(te.banner, false);
				ResourceLocation res = BannerTextures.BANNER_DESIGNS.getResourceLocation(banner.getPatternResourceLocation(), banner.getPatternList(), banner.getColorList());
				if(res!=null)
				{
					GlStateManager.translate(1.3875, 3.5, 1.9);
					GlStateManager.scale(0.5, 0.5, 0.5);
					ClientUtils.mc().getTextureManager().bindTexture(res);
					float f3 = (float)(x*7+y*9+z*13+te.getWorld().getTotalWorldTime()+partialTicks);
					modelBanner.bannerSlate.rotateAngleZ = (float)Math.PI;
					modelBanner.bannerSlate.rotateAngleY = (float)Math.PI;
					modelBanner.bannerSlate.rotateAngleX = -(-0.0125F+0.01F*MathHelper.cos(f3*(float)Math.PI*0.02F))*(float)Math.PI;
					GlStateManager.enableRescaleNormal();

					modelBanner.renderBanner();

				}
				GlStateManager.popMatrix();
			}

			ClientUtils.bindAtlas();
			IBlockState state = Blocks.RAIL.getDefaultState();
			GlStateManager.translate(0, 0.125, 2);
			ClientUtils.mc().getBlockRendererDispatcher().renderBlockBrightness(state, 1f);
			GlStateManager.translate(1, -0.0625, 2);
			GlStateManager.rotate(90, 0, 1, 0);
			GlStateManager.rotate(3.5f, 0, 0, 1);
			ClientUtils.mc().getBlockRendererDispatcher().renderBlockBrightness(state, 1f);
			GlStateManager.popMatrix();
		}
	}
}
