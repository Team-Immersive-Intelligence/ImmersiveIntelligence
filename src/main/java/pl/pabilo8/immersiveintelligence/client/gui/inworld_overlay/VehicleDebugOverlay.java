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
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehiclePart;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleWheel;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.VehicleOBB;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.VerticalForces;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.entity.IIEntityUtils;
import pl.pabilo8.immersiveintelligence.common.util.entity.SyncedDurability;

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
	public static boolean displayWheelBoxes = true;
	public static boolean displayNames = false;
	public static boolean displayDirectionArrows = true;

	@Override
	@SuppressWarnings("rawtypes")
	public void draw(@Nonnull EntityPlayer player, @Nonnull World world, RayTraceResult mouseOver, float partialTicks)
	{
		//get rendering centre position
		double posX = player.lastTickPosX+(player.posX-player.lastTickPosX)*(double)partialTicks;
		double posY = player.lastTickPosY+(player.posY-player.lastTickPosY)*(double)partialTicks;
		double posZ = player.lastTickPosZ+(player.posZ-player.lastTickPosZ)*(double)partialTicks;

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

				IIColor color = IIColor.MC_GRAY;
				if(part instanceof EntityVehicleWheel)
					color = IIColor.MC_LIGHT_PURPLE;
				else if(part.partName.contains("seat"))
					color = IIColor.MC_GOLD;
				else if(part.durability!=vehicle.durabilityMain)
					color = IIColor.MC_DARK_GREEN;

				VehicleOBB obb = part.getCollisionOBB().offset(motionOffset);
				Vec3d center = obb.center;
				drawOBB(obb, posX, posY, posZ, color);

				if(displayDirectionArrows&&part instanceof EntityVehicleWheel)
				{
					float len = 0.75f;
					drawArrow3D(obb.axisZ.scale(-1), center.x-posX, center.y-posY, center.z-posZ, len, IIColor.MC_BLUE);
					drawArrow3D(obb.axisX, center.x-posX, center.y-posY, center.z-posZ, len*0.75f, IIColor.MC_AQUA);
					drawArrow3D(obb.axisY, center.x-posX, center.y-posY, center.z-posZ, len*0.55f, IIColor.MC_GREEN);
				}

				if(part instanceof EntityVehicleWheel)
				{
					EntityVehicleWheel wheel = (EntityVehicleWheel)part;
					if(displayDirectionArrows)
					{
						float yaw = vehicle.rotationYaw+wheel.getSteeringAngle();
						drawArrow(yaw, center.x-posX, center.y-posY, center.z-posZ, 1, IIColor.MC_BLUE);
						Vec3d force = wheel.getLastForces().force;
						if(force.lengthSquared() > 1.0E-6)
							drawArrow(force.x, force.z, center.x-posX, center.y-posY, center.z-posZ, 1f, IIColor.MC_RED);
					}

					//Climbing
					VerticalForces verticalForces = wheel.getLastVerticalForces();
					if(verticalForces.canClimb&&verticalForces.climbedBox!=null)
					{
						GlStateManager.pushMatrix();
						GlStateManager.translate(motionOffset.x-posX, motionOffset.y-posY, motionOffset.z-posZ);
						RenderGlobal.drawSelectionBoundingBox(verticalForces.climbedBox.grow(0.002D),
								IIColor.MC_YELLOW.red/255f, IIColor.MC_YELLOW.green/255f, IIColor.MC_YELLOW.blue/255f, 1.0F);
						if(displayDirectionArrows)
						{
							Vec3d climbCenter = verticalForces.climbedBox.getCenter();
							drawArrow(verticalForces.obstacleNormal.x, verticalForces.obstacleNormal.z, climbCenter.x, climbCenter.y, climbCenter.z,
									(float)verticalForces.climbedBox.getAverageEdgeLength()*0.5f, IIColor.MC_YELLOW);
						}
						GlStateManager.popMatrix();
					}
				}

				if(displayNames)
				{
					GlStateManager.pushMatrix();
					GlStateManager.enableTexture2D();
					GlStateManager.translate(center.x-posX, center.y-posY+part.aabb.getAverageEdgeLength()*0.5, center.z-posZ);
					GlStateManager.rotate(180-player.rotationYaw, 0, 1, 0);
					GlStateManager.rotate(-player.rotationPitch, 1, 0, 0);
					GlStateManager.scale(0.0625f/2, -0.0625f/2, 0.0625f/2);

					SyncedDurability durability = part.getDurability();
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
					GlStateManager.popMatrix();
				}
			}

			//Velocity
			AxisAlignedBB bbWorld = vehicle.getRenderBoundingBox();
			double cx = (bbWorld.minX+bbWorld.maxX)*0.5-posX;
			double cy = (bbWorld.minY+bbWorld.maxY)*0.5-posY;
			double cz = (bbWorld.minZ+bbWorld.maxZ)*0.5-posZ;
			Vec3d velocity = vehicle.getVelocity();
			if(displayDirectionArrows&&velocity.lengthSquared() > 1.0E-6)
				drawArrow(velocity.x, velocity.z, cx, cy, cz, 8, IIColor.MC_RED);
		}

		//Finish Draw
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
	}

	private static void drawOBB(VehicleOBB obb, double viewX, double viewY, double viewZ, IIColor color)
	{
		Vec3d[] c = obb.getCorners();
		int[][] edges = new int[][]{
				{0, 1}, {1, 2}, {2, 3}, {3, 0},
				{4, 5}, {5, 6}, {6, 7}, {7, 4},
				{0, 4}, {1, 5}, {2, 6}, {3, 7}
		};

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buf = tess.getBuffer();
		buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		float r = color.red/255f, g = color.green/255f, b = color.blue/255f;
		for(int[] edge : edges)
		{
			Vec3d a = c[edge[0]];
			Vec3d d = c[edge[1]];
			buf.pos(a.x-viewX, a.y-viewY, a.z-viewZ).color(r, g, b, 1.0f).endVertex();
			buf.pos(d.x-viewX, d.y-viewY, d.z-viewZ).color(r, g, b, 1.0f).endVertex();
		}
		tess.draw();
	}

	private static void drawArrow(float yaw, double x, double y, double z, float len, IIColor color)
	{
		double dirX = -MathHelper.sin(yaw*0.017453292F);
		double dirZ = MathHelper.cos(yaw*0.017453292F);
		drawArrow(dirX, dirZ, x, y, z, len, color);

	}

	private static void drawArrow(double dirX, double dirZ, double x, double y, double z, float len, IIColor color)
	{
		drawArrow3D(new Vec3d(dirX, 0, dirZ), x, y, z, len, color);
	}

	private static void drawArrow3D(Vec3d direction, double x, double y, double z, float len, IIColor color)
	{
		if(direction.lengthSquared() < 1.0E-6)
			return;
		Vec3d dir = direction.normalize();
		double head = 0.2;

		Vec3d side = new Vec3d(-dir.z, 0, dir.x);
		if(side.lengthSquared() < 1.0E-6)
			side = new Vec3d(1, 0, 0);
		side = side.normalize();

		double ex = x+dir.x*len;
		double ey = y+dir.y*len;
		double ez = z+dir.z*len;

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder buf = tess.getBuffer();
		buf.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

		float r = color.red/255f, g = color.green/255f, b = color.blue/255f;

		//Main line
		buf.pos(x, y, z).color(r, g, b, 1.0f).endVertex();
		buf.pos(ex, ey, ez).color(r, g, b, 1.0f).endVertex();

		//Arrowhead
		buf.pos(ex, ey, ez).color(r, g, b, 1.0f).endVertex();
		buf.pos(ex-dir.x*head+side.x*head*0.6, ey-dir.y*head+side.y*head*0.6, ez-dir.z*head+side.z*head*0.6).color(r, g, b, 1.0f).endVertex();

		buf.pos(ex, ey, ez).color(r, g, b, 1.0f).endVertex();
		buf.pos(ex-dir.x*head-side.x*head*0.6, ey-dir.y*head-side.y*head*0.6, ez-dir.z*head-side.z*head*0.6).color(r, g, b, 1.0f).endVertex();

		tess.draw();
	}
}
