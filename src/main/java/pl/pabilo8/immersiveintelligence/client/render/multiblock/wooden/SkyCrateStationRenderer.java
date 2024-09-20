package pl.pabilo8.immersiveintelligence.client.render.multiblock.wooden;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.ConveyorDirection;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorBelt;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.ModelConveyor;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkycrateMount;
import pl.pabilo8.immersiveintelligence.client.model.multiblock.wooden.ModelSkyCrateStation;
import pl.pabilo8.immersiveintelligence.client.render.IReloadableModelContainer;
import pl.pabilo8.immersiveintelligence.client.util.tmt.ModelRendererTurbo;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.SkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class SkyCrateStationRenderer extends TileEntitySpecialRenderer<TileEntitySkyCrateStation> implements IReloadableModelContainer<SkyCrateStationRenderer>
{
	private static ModelSkyCrateStation model;
	private static ModelSkyCrateStation modelFlipped;
	private static final TileEntityBanner banner = new TileEntityBanner();
	private static final ModelBanner modelBanner = new ModelBanner();
	private static final String texture = ImmersiveIntelligence.MODID+":textures/blocks/multiblock/skycrate_station.png";

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
			GlStateManager.translate((float)x, (float)y, (float)z);
			GlStateManager.disableLighting();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

			float rpm_pitch = 0, rpm_grab = 0, rpm_crate = 0, rpm_gears;
			double motorTick = 0f;
			float progress = 0f;
			IConveyorBelt con = ConveyorHandler.getConveyor(new ResourceLocation("immersiveengineering:conveyor"), null);

			ItemStack crateItem = ItemStack.EMPTY;
			ItemStack mountItem = ItemStack.EMPTY;

			if(te.hasWorld())
			{
				motorTick = (te.getWorld().getTotalWorldTime()%IIRotaryUtils.getRPMMax()+partialTicks)/IIRotaryUtils.getRPMMax();
				progress = te.progress+(partialTicks*te.getEffectiveEnergy()*IIRotaryUtils.getGearEffectiveness(te.getInventory(), te.getEfficiencyMultiplier()));
				crateItem = te.getInventory().get(3);
				mountItem = te.getInventory().get(4);

			}
			double pistonOnly = 0, cratePusher = 0, inserterAngle = 0, inserterLength = 0;

			double animProgress = 0;
			boolean convInWork = false, convOutWork = false;

			rpm_gears = te.rotation.getRotationSpeed();

			ModelSkyCrateStation modelCurrent = te.mirrored?modelFlipped: model;
			modelCurrent.getBlockRotation(te.facing, te.mirrored);
			modelCurrent.render();
			switch(te.animation)
			{
				case 1:
				{
					drawCrate(te, 0, null
					);
					break;
				}
				case 2:
				{
					animProgress = progress/SkyCrateStation.crateInTime;
					convInWork = true;
					float oneThird = (float)Math.min(animProgress/0.45f, 1f);
					float half = (float)Math.min(animProgress/0.65f, 1f);
					float secondHalf = (float)Math.min(Math.max(animProgress-0.65f, 0f)/0.35f, 1f);
					float secondHalfQuarter = (float)MathHelper.clamp((animProgress-0.45)/0.2f, 0f, 1f);
					drawCrate(te, animProgress, (double1) ->
							{
								GlStateManager.translate(1.5-half-secondHalf*0.5, -1+half, 0);
								GlStateManager.rotate(35+(oneThird*10)-(secondHalfQuarter*35)-(secondHalf*10), 0, 0, -1);
								GlStateManager.translate(0, 0.125f-(secondHalfQuarter*0.125), 0);
							}
					);

					break;
				}
				case 3:
				{
					animProgress = progress/SkyCrateStation.crateOutTime;
					float finalAnimProgress = (float)animProgress;
					drawCrate(te, animProgress, (double1) ->
							GlStateManager.translate(-1.5*finalAnimProgress, 0, 0)
					);
					convOutWork = true;
					break;
				}
				case 4:
				{
					animProgress = progress/SkyCrateStation.inputTime;
					mountItem = te.getInventory().get(4);
					double angle2 = MathHelper.clamp(Math.abs(animProgress-0.35d)/0.15, 0, 1);
					pistonOnly = angle2 > 0.5?1f-((angle2-0.5)/0.5): 1;

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
					animProgress = progress/SkyCrateStation.outputTime;

					inserterAngle = animProgress <= 0.15d?animProgress/0.15*-1:
							animProgress <= 0.65d?-1+((animProgress-0.15)/0.5*1.75):
									animProgress <= 0.6d?1.75-((animProgress-0.65)/0.1*0.75):
											0.65*(1f-((animProgress-0.75)/0.25));

					inserterLength = animProgress <= 0.15?animProgress/0.15d*0.25:
							animProgress <= 0.65?0.25+((animProgress-0.15)/0.5):
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

			float flipMod = te.mirrored?-1: 1;

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0.65625f, -0.625f*flipMod);
			GlStateManager.rotate(360f*(float)motorTick*rpm_pitch, 1, 0, 0);
			for(ModelRendererTurbo mod : modelCurrent.backAxleModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0.65625f, -0.375f*flipMod);
			GlStateManager.rotate(360f*(float)motorTick*rpm_grab, 1, 0, 0);
			for(ModelRendererTurbo mod : modelCurrent.frontAxleModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.59375f, 2.84375f-0.0625f, flipMod*0.1875f);
			GlStateManager.rotate(360f*(float)motorTick*rpm_crate, 1, 0, 0);
			for(ModelRendererTurbo mod : modelCurrent.cratePusherAxleModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 0, -cratePusher*0.425f*flipMod);
			for(ModelRendererTurbo mod : modelCurrent.cratePusherModel)
				mod.render(0.0625f);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, pistonOnly*-0.5f, 0);
			for(ModelRendererTurbo mod : modelCurrent.pistonDoorModel)
				mod.render(0.0625f);
			GlStateManager.translate(0, 1, te.mirrored?-2: 1);
			ClientUtils.bindAtlas();
			List<BakedQuad> quads = ModelConveyor.getBaseConveyor(te.mirrored?EnumFacing.EAST: EnumFacing.WEST, 1, new Matrix4(EnumFacing.WEST), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite((convInWork||convOutWork)?con.getActiveTexture(): con.getInactiveTexture()), new boolean[]{true, true}, new boolean[]{true, true}, null, 0);
			ClientUtils.renderQuads(quads, 1, 1, 1, 1);
			ClientUtils.bindTexture(texture);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate(0, 21f/16f, -7f/16f*flipMod);
			GlStateManager.rotate(90f*(float)inserterAngle*flipMod, 1, 0, 0);
			GlStateManager.translate(0, 2f/16f, 2f/16f*flipMod);
			for(ModelRendererTurbo mod : modelCurrent.inserterBaseModel)
				mod.render(0.0625f);
			GlStateManager.translate(0, -(1f-inserterLength)*0.5, 0);
			for(ModelRendererTurbo mod : modelCurrent.inserterTopModel)
				mod.render(0.0625f);
			GlStateManager.translate(0, -(1f-inserterLength)*0.5, 0);
			for(ModelRendererTurbo mod : modelCurrent.inserterTopperModel)
				mod.render(0.0625f);

			if(te.animation==4&&animProgress > 0.3&&animProgress < 0.8&&mountItem.getItem() instanceof ISkycrateMount)
			{
				ISkycrateMount mount = (ISkycrateMount)mountItem.getItem();

				GlStateManager.translate(0.5, 21f/16f+(1f-inserterLength)-0.625f+inserterLength, -0.125);
				GlStateManager.rotate(-90f*(float)inserterAngle*flipMod, 1, 0, 0);
				GlStateManager.translate(0, -1, 0);
				GlStateManager.scale(0.85, 0.85, 0.85);
				mount.render(mountItem, getWorld(), partialTicks, -1);
			}
			else if(te.animation==5&&animProgress > 0.15&&animProgress <= 0.75&&mountItem.getItem() instanceof ISkycrateMount)
			{
				ISkycrateMount mount = (ISkycrateMount)mountItem.getItem();

				GlStateManager.translate(0.5, 21f/16f+(1f-inserterLength)-0.625f+inserterLength, -0.125);
				GlStateManager.rotate(-90f*(float)inserterAngle*flipMod, 1, 0, 0);
				GlStateManager.translate(0, -1, 0);
				GlStateManager.scale(0.85, 0.85, 0.85);
				mount.render(mountItem, getWorld(), partialTicks, -1);

				if(animProgress >= 0.65)
				{
					GlStateManager.translate(0, 0.5, 0);
					ClientUtils.mc().getRenderItem().renderItem(crateItem, TransformType.NONE);
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
				double diam = conn.cableType.getRenderDiameter();
				GlStateManager.pushMatrix();
				ClientUtils.bindTexture("immersiveengineering:textures/blocks/wire.png");
				IIColor.fromPackedRGB(conn.cableType.getColour(conn)).glColor();
				GlStateManager.disableCull();
				GlStateManager.rotate(te.mirrored?270: 90, 0, 1, 0);
				GlStateManager.translate(-1.5, 2.5625, 0.5*flipMod);
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

				if(mountItem.getItem() instanceof ISkycrateMount)
				{
					ISkycrateMount mount = (ISkycrateMount)mountItem.getItem();
					GlStateManager.translate(0.5, 1.625+0.0625, 1.5*flipMod);
					GlStateManager.scale(0.85, 0.85, 0.85);
					if(te.animation==4)
					{
						if(animProgress < 0.3)
						{
							GlStateManager.pushMatrix();
							GlStateManager.translate(0, 0, -MathHelper.clamp((animProgress-0.25)/0.1, 0, 1));
							mount.render(mountItem, getWorld(), partialTicks, -1);
							GlStateManager.popMatrix();

						}
						GlStateManager.translate(0, 0.5, 0);
						float pushPiston = animProgress > 0.45?(float)(1f-pistonOnly)*-0.6f: 0;
						GlStateManager.translate(0, -1.25*(MathHelper.clamp((animProgress-0.25)/0.1, 0, 1))-pushPiston, 0);
						ClientUtils.mc().getRenderItem().renderItem(crateItem, TransformType.NONE);
					}
					else if(te.animation==5)
					{
						if(animProgress > 0.75)
						{
							mount.render(mountItem, getWorld(), partialTicks, -1);
							GlStateManager.translate(0, 0.5, 0);
							ClientUtils.mc().getRenderItem().renderItem(crateItem, TransformType.NONE);
						}
						else if(animProgress < 0.65)
						{
							GlStateManager.translate(0, -0.25, 0);
							ClientUtils.mc().getRenderItem().renderItem(crateItem, TransformType.NONE);
						}
					}
					else
					{
						mount.render(mountItem, getWorld(), partialTicks, -1);
						GlStateManager.translate(0, 0.5, 0);
						ClientUtils.mc().getRenderItem().renderItem(crateItem, TransformType.NONE);
					}
				}

			}

			GlStateManager.popMatrix();

			ItemStack bannerItem = te.getInventory().get(5);

			if(!bannerItem.isEmpty())
			{
				GlStateManager.pushMatrix();
				banner.setItemValues(bannerItem, false);
				ResourceLocation res = BannerTextures.BANNER_DESIGNS.getResourceLocation(banner.getPatternResourceLocation(), banner.getPatternList(), banner.getColorList());
				if(res!=null)
				{
					GlStateManager.translate(1.5, 3.65, te.mirrored?-2: 1.9);
					GlStateManager.scale(0.55, 0.55, 0.55);
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
			GlStateManager.translate(1f, 0, te.mirrored?-2: 1);
			quads = ModelConveyor.getBaseConveyor(EnumFacing.WEST, 1, new Matrix4(EnumFacing.WEST), ConveyorDirection.UP,
					ClientUtils.getSprite(convInWork?con.getActiveTexture(): con.getInactiveTexture()), new boolean[]{true, true}, new boolean[]{true, true}, null, 0);
//			GlStateManager.translate(0, 0, 1);
			ClientUtils.renderQuads(quads, 1, 1, 1, 1);

			//Second conveyor
			quads = ModelConveyor.getBaseConveyor(EnumFacing.WEST, 1, new Matrix4(EnumFacing.WEST), ConveyorDirection.HORIZONTAL,
					ClientUtils.getSprite(convOutWork?con.getActiveTexture(): con.getInactiveTexture()), new boolean[]{true, true}, new boolean[]{true, true}, null, 0);
			GlStateManager.translate(-2f, 1, 0);
			ClientUtils.renderQuads(quads, 1, 1, 1, 1);


			GlStateManager.popMatrix();
		}
	}

	private void drawCrate(TileEntitySkyCrateStation te, double animation, @Nullable Consumer<Double> executed)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5, 1.55, te.mirrored?-1.5: 1.5);
		if(executed!=null)
			executed.accept(animation);
		GlStateManager.scale(0.85, 0.85, 0.85);
		ClientUtils.mc().getRenderItem().renderItem(te.getInventory().get(3), TransformType.NONE);
		GlStateManager.popMatrix();
		ClientUtils.bindTexture(texture);
	}

	@Override
	public void reloadModels()
	{
		model = new ModelSkyCrateStation();
		modelFlipped = new ModelSkyCrateStation();
		modelFlipped.flipAllZ();
	}
}
