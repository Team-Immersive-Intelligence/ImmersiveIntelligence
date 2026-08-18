package pl.pabilo8.immersiveintelligence.client.render.metal_device;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMT;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ChemicalDispenser;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityChemicalDispenser;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 14.08.2026
 * @since 01.06.2019
 */
@RegisteredTileRenderer(name = "block/device/chemical_dispenser", clazz = TileEntityChemicalDispenser.class)
public class ChemicalDispenserRenderer extends IITileRenderer<TileEntityChemicalDispenser>
{
	private AMTModel model = null;

	@Override
	public void draw(TileEntityChemicalDispenser te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		//Apply the nozzle rotation in model-local axes.
		Vec3d rotation = getNozzleRotation(te, partialTicks);
		for(AMT part : model)
			part.setRotation(rotation);

		//Rotate the complete model to the block mounting direction.
		applyStandardRotation(te.facing);

		model.render(tes, buf);
	}

	private static Vec3d getNozzleRotation(TileEntityChemicalDispenser te, float partialTicks)
	{
		float pitch = interpolateAngle(te.pitch, te.plannedPitch, ChemicalDispenser.rotateVTime, partialTicks);
		float yaw = interpolateAngle(te.yaw, te.plannedYaw, ChemicalDispenser.rotateHTime, partialTicks);

		boolean vertical = te.facing.getAxis()==EnumFacing.Axis.Y;
		boolean positive = te.facing.getAxisDirection()==AxisDirection.POSITIVE;

		if(vertical)
			pitch = (float)Math.toDegrees(Math.atan(
					Math.tan(Math.toRadians(pitch))*Math.cos(Math.toRadians(yaw))
			));

		float localYaw = yaw*(vertical?(positive?-1f: 1f): (positive?1f: -1f));
		return new Vec3d(pitch, 0, localYaw);
	}

	private static float interpolateAngle(float current, float target, int rotationTime, float partialTicks)
	{
		if(rotationTime <= 0)
			return target;

		float next = IIMath.progressValue(current, target, 900f/rotationTime, 1);
		return (float)MathHelper.clampedLerp(current, next, partialTicks);
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model);
	}

	@Override
	protected void nullifyModels()
	{
		model = AMTUtils.disposeOf(model);
	}

	@Override
	protected void applyStandardRotation(EnumFacing facing)
	{
		GlStateManager.translate(0.5, 0.5, 0.5);
		switch(facing)
		{
			default:
				break;
			case DOWN:
			{
				GlStateManager.rotate(90, 0, 1, 0);
			}
			break;
			case UP:
			{
				GlStateManager.rotate(180, 1, 0, 0);
				GlStateManager.rotate(90, 0, 1, 0);
			}
			break;
			case NORTH:
			{
				GlStateManager.rotate(90, 1, 0, 0);
			}
			break;
			case SOUTH:
			{
				GlStateManager.rotate(-90, 1, 0, 0);
				GlStateManager.rotate(-180, 0, 1, 0);
			}
			break;
			case WEST:
			{
				GlStateManager.rotate(90, 0, 1, 0);
				GlStateManager.rotate(90, 1, 0, 0);
			}
			break;
			case EAST:
			{
				GlStateManager.rotate(-90, 0, 1, 0);
				GlStateManager.rotate(90, 1, 0, 0);
			}
			break;
		}
		GlStateManager.translate(-0.5, -0.5, -0.5);
	}
}
