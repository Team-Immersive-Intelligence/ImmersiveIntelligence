package pl.pabilo8.immersiveintelligence.client.render.mechanical_device;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraftforge.client.model.obj.OBJModel;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.animation.IIAnimationCompiledMap;
import pl.pabilo8.immersiveintelligence.client.util.amt.models.AMTModel;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTChain;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.renderer.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityWheelBase;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityWheelIron;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityWheelSteel;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.Set;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 01.08.2024
 * @ii-approved 0.3.1
 * @since 01.06.2019
 */
@RegisteredTileRenderer(name = "mechanical/iron_wheel", clazz = TileEntityWheelIron.class)
@RegisteredTileRenderer(name = "mechanical/steel_wheel", clazz = TileEntityWheelSteel.class)
public class WheelRenderer extends IITileRenderer<TileEntityWheelBase>
{
	private AMTModel model;
	private IIAnimationCompiledMap rotationClockwise, rotationCounterCw;

	@Override
	public void draw(TileEntityWheelBase te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		applyStandardRotation(te.facing);
		boolean clockwise = IIRotaryUtils.shouldRotateClockwise(te.facing);

		//Apply rotation
		(clockwise?rotationClockwise: rotationCounterCw).apply(
				IIRotaryUtils.getDisplayRotation(te, te.getNetwork().getEnergyStorage(), partialTicks));

		model.render(tes, buf);

		Set<Connection> outputs = ImmersiveNetHandler.INSTANCE.getConnections(te.getWorld(), te.getPos());
		//Make or get the connection model
		if(outputs==null)
			return;
		for(Connection connection : outputs)
		{
			if(!shouldRenderConnection(te, connection))
				continue;
			AMTChain chain = IIModelRegistry.INSTANCE.getMotorBeltConnectionModel(te, connection);
			//Apply rotation
			float rpm = (float)te.getOutputRPM();
			if(rpm < 1)
				chain.setProgress(0);
			else
			{
				float progress = AMTUtils.getDebugProgress(rpm, partialTicks);
				chain.setProgress(clockwise?(1f-progress): progress);
			}
			chain.render(tes, buf);
		}
	}

	private boolean shouldRenderConnection(TileEntityWheelBase te, Connection connection)
	{
		boolean axisX = te.facing.getAxis()==Axis.Z;
		if(connection.start.getY() > connection.end.getY())
			return false;
		return connection.start.getY()!=connection.end.getY()||((
				(axisX?connection.start.getX(): connection.start.getZ()) <= (axisX?connection.end.getX(): connection.end.getZ())));
	}

	@Override
	public void compileModels(IBlockState state, OBJModel model)
	{
		this.model = new AMTModel(state, model);
		rotationClockwise = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("wheel/rotate_cw"));
		rotationCounterCw = IIAnimationCompiledMap.create(this.model, IIReference.RES_II.with("wheel/rotate_ccw"));
	}

	@Override
	protected void nullifyModels()
	{
		AMTUtils.disposeOf(model);
	}
}
