package pl.pabilo8.immersiveintelligence.client.render.mechanical_device;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Tuple;
import pl.pabilo8.immersiveintelligence.api.rotary.IIRotaryUtils;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer;
import pl.pabilo8.immersiveintelligence.client.render.IITileRenderer.RegisteredTileRenderer;
import pl.pabilo8.immersiveintelligence.client.util.amt.*;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityWheelBase;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityWheelIron;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityWheelSteel;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Pabilo8
 * @updated 01.08.2024
 * @ii-approved 0.3.1
 * @since 2019-06-01
 */
@RegisteredTileRenderer(name = "mechanical/iron_wheel", clazz = TileEntityWheelIron.class)
@RegisteredTileRenderer(name = "mechanical/steel_wheel", clazz = TileEntityWheelSteel.class)
public class WheelRenderer extends IITileRenderer<TileEntityWheelBase>
{
	private IIAnimationCompiledMap rotationClockwise, rotationCounterCw;
	private final HashMap<TileEntityWheelBase, AMTChain> connections = new HashMap<>();
	AMT[] models;

	@Override
	public void draw(TileEntityWheelBase te, BufferBuilder buf, float partialTicks, Tessellator tes)
	{
		applyStandardRotation(te.facing);
		boolean clockwise = IIRotaryUtils.shouldRotateClockwise(te.facing);

		//Apply rotation
		(clockwise?rotationClockwise: rotationCounterCw).apply(
				IIRotaryUtils.getDisplayRotation(te, te.getNetwork().getEnergyStorage(), partialTicks));

		for(AMT amt : models)
			amt.render(tes, buf);

		Set<Connection> outputs = ImmersiveNetHandler.INSTANCE.getConnections(te.getWorld(), te.getPos());
		//Make or get the connection model
		if(outputs==null)
		{
			//Preventive method, for when a wheel is replaced
			connections.remove(te);
			return;
		}
		for(Connection connection : outputs)
		{
			if(!shouldRenderConnection(te, connection))
				continue;
			AMTChain chain = connections.computeIfAbsent(te, t -> AMTChain.getChainForNetwork(t, connection));
			//Apply rotation
			float rpm = (float)te.getOutputRPM();
			if(rpm==0)
				chain.setProgress(0);
			else
			{
				float progress = IIAnimationUtils.getDebugProgress(rpm, partialTicks);
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
	public void compileModels(Tuple<IBlockState, IBakedModel> sModel)
	{
		models = IIAnimationUtils.getAMT(sModel, IIAnimationLoader.loadHeader(sModel.getSecond()));
		rotationClockwise = IIAnimationCompiledMap.create(models, IIReference.RES_II.with("wheel/rotate_cw"));
		rotationCounterCw = IIAnimationCompiledMap.create(models, IIReference.RES_II.with("wheel/rotate_ccw"));
		connections.clear();
	}

	@Override
	protected void nullifyModels()
	{
		IIAnimationUtils.disposeOf(models);
	}
}
