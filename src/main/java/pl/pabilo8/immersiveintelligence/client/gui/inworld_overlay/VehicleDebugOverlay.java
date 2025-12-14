package pl.pabilo8.immersiveintelligence.client.gui.inworld_overlay;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Graphics;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleDurability;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehiclePart;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.VerticalForces;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Debug overlay for II vehicles
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 29.05.2023
 */
public class VehicleDebugOverlay extends InWorldOverlayBase
{
	@Override
	@SuppressWarnings("rawtypes")
	public void draw(@Nonnull EntityPlayer player, @Nonnull World world, RayTraceResult mouseOver, float partialTicks)
	{
		//get rendering centre position
		double posX = player.lastTickPosX+(player.posX-player.lastTickPosX)*(double)partialTicks;
		double posY = player.lastTickPosY+(player.posY-player.lastTickPosY)*(double)partialTicks;
		double posZ = player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*(double)partialTicks;
		boolean displayWheelBoxes = true;
		boolean displayNames = true;

		if(!Graphics.vehicleDebugOverlay)
			return;

		//Start Draw
		GlStateManager.enableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.glLineWidth(2.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		GlStateManager.disableDepth();

		//Draw
		List<EntityVehicleBase> vehicles = world.getEntities(EntityVehicleBase.class, input -> true);
		for(EntityVehicleBase vehicle : vehicles)
		{
			Vec3d motionOffset = IIEntityUtils.getEntityMotion(vehicle).scale(partialTicks);
			for(EntityVehiclePart<?> part : vehicle.getVehicleParts())
			{
				if(!displayWheelBoxes&&part instanceof EntityVehicleWheel)
					continue;
				GlStateManager.pushMatrix();
				IIColor color = IIColor.MC_GRAY;
				if(part instanceof EntityVehicleWheel)
				{
					EntityVehicleWheel wheel = (EntityVehicleWheel)part;
					color = IIColor.MC_LIGHT_PURPLE;
					AxisAlignedBB bbWorld = part.aabb.offset(part.posX, part.posY, part.posZ);
					double cx = (bbWorld.minX+bbWorld.maxX)*0.5-posX;
					double cy = (bbWorld.minY+bbWorld.maxY)*0.5-posY;
					double cz = (bbWorld.minZ+bbWorld.maxZ)*0.5-posZ;

					// Forward direction from yaw (degrees -> radians factor 0.017453292F)
					float yaw = vehicle.rotationYaw+wheel.getSteeringAngle();
					drawArrow(yaw, cx, cy, cz, 1, IIColor.MC_BLUE);
					Vec3d force = wheel.getLastForces().force.normalize();
					drawArrow(force.x, force.z, cx, cy, cz, 1f, IIColor.MC_RED);

					//Climbing
					VerticalForces verticalForces = wheel.getLastVerticalForces();
					if(verticalForces.canClimb)
					{
						GlStateManager.pushMatrix();
						GlStateManager.translate(motionOffset.x-posX, motionOffset.y-posY, motionOffset.z-posZ);
						RenderGlobal.drawSelectionBoundingBox(verticalForces.climbedBox.grow(0.002D),
								IIColor.MC_YELLOW.red/255f, IIColor.MC_YELLOW.green/255f, IIColor.MC_YELLOW.blue/255f, 1.0F);
						Vec3d center = verticalForces.climbedBox.getCenter();
						drawArrow(verticalForces.obstacleNormal.x, verticalForces.obstacleNormal.z, center.x, center.y, center.z,
								(float)verticalForces.climbedBox.getAverageEdgeLength()*0.5f, IIColor.MC_YELLOW);
						GlStateManager.popMatrix();
					}
				}
				else if(part.partName.contains("seat"))
					color = IIColor.MC_GOLD;
				else if(part.durability!=vehicle.durabilityMain)
					color = IIColor.MC_DARK_GREEN;

				//Draw part bounds
				GlStateManager.translate(part.posX+motionOffset.x-posX, part.posY+motionOffset.y-posY, part.posZ+motionOffset.z-posZ);
				RenderGlobal.drawSelectionBoundingBox(part.aabb.grow(0.002D),
						color.red/255f, color.green/255f, color.blue/255f, 1.0F);

				if(displayNames)
				{
					//Draw part name
					GlStateManager.enableTexture2D();
					GlStateManager.translate(0, (part.aabb.maxY-part.aabb.minY)/2, 0);
					GlStateManager.rotate(180-player.rotationYaw, 0, 1, 0);
					GlStateManager.rotate(-player.rotationPitch, 1, 0, 0);
					GlStateManager.scale(0.0625f/2, -0.0625f/2, 0.0625f/2);

					VehicleDurability durability = part.getDurability();
					String[] lines;
					if(durability!=null)
						lines = new String[]{
								part.partName,
								(int)(durability.maxDurability*durability.getDamageFactor())+" / "+durability.maxDurability+" @ "+durability.armor
						};
					else
						lines = new String[]{part.partName};

					for(int i = 0; i < lines.length; i++)
						IIClientUtils.fontRegular.drawString(lines[i],
								(int)(-IIClientUtils.fontRegular.getStringWidth(lines[i])/2f), -((lines.length-i)*11)/2,
								color.getPackedRGB()
						);
					GlStateManager.disableTexture2D();
				}
				GlStateManager.popMatrix();
			}

			//Velocity
			AxisAlignedBB bbWorld = vehicle.getRenderBoundingBox();
			double cx = (bbWorld.minX+bbWorld.maxX)*0.5-posX;
			double cy = (bbWorld.minY+bbWorld.maxY)*0.5-posY;
			double cz = (bbWorld.minZ+bbWorld.maxZ)*0.5-posZ;
			Vec3d velocity = vehicle.getVelocity();
			drawArrow(velocity.x, velocity.z, cx, cy, cz, 8, IIColor.MC_RED);
		}

		//Finish Draw
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
	}

	private static void drawArrow(float yaw, double x, double y, double z, float len, IIColor color)
	{
		double dirX = -MathHelper.sin(yaw*0.017453292F);
		double dirZ = MathHelper.cos(yaw*0.017453292F);
		drawArrow(dirX, dirZ, x, y, z, len, color);

	}

	private static void drawArrow(double dirX, double dirZ, double x, double y, double z, float len, IIColor color)
	{
		double head = 0.2;

		double ex = x+dirX*len;
		double ez = z+dirZ*len;

		//Perpendicular for arrow head
		double px = -dirZ;
		double pz = dirX;

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buf = tess.getBuffer();
		buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

		float r = color.red/255f, g = color.green/255f, b = color.blue/255f;

		//Main line
		buf.pos(x, y, z).color(r, g, b, 1.0f).endVertex();
		buf.pos(ex, y, ez).color(r, g, b, 1.0f).endVertex();

		//Arrowhead
		buf.pos(ex, y, ez).color(r, g, b, 1.0f).endVertex();
		buf.pos(ex-dirX*head+px*head*0.6, y, ez-dirZ*head+pz*head*0.6).color(r, g, b, 1.0f).endVertex();

		buf.pos(ex, y, ez).color(r, g, b, 1.0f).endVertex();
		buf.pos(ex-dirX*head-px*head*0.6, y, ez-dirZ*head-pz*head*0.6).color(r, g, b, 1.0f).endVertex();

		tess.draw();
	}
}
