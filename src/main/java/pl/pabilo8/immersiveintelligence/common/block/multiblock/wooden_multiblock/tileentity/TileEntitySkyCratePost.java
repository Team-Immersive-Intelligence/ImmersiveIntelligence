package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.api.utils.ISkyCrateConnector;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCratePost;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIConnectable;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import java.util.ArrayList;
import java.util.Set;

import static blusunrize.immersiveengineering.api.energy.wires.WireType.STRUCTURE_CATEGORY;

/**
 * Routes Skycrates between two zipline connections.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 * @updated 08.10.2026
 */
public class TileEntitySkyCratePost extends TileEntityMultiblockIIConnectable<TileEntitySkyCratePost> implements ISkyCrateConnector
{
	public TileEntitySkyCratePost()
	{
		super(MultiblockSkyCratePost.INSTANCE);
	}

	@Override
	protected void dummyCleanup()
	{
	}

	@Override
	protected void onUpdate()
	{
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Override
	protected boolean isMatchingCable(WireType cableType)
	{
		return STRUCTURE_CATEGORY.equals(cableType.getCategory());
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		Set<Connection> connections = world==null?null: ImmersiveNetHandler.INSTANCE.getConnections(world, getPos());
		return formed&&canConnect()&&isMatchingCable(cableType)&&(connections==null||connections.size() < 2)
				&&(limitType==null||limitType==cableType);
	}

	@Override
	public Vec3d getConnectionOffset(Connection connection)
	{
		return new Vec3d(.5, .625, .5);
	}

	@Override
	public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other)
	{
		super.connectCable(cableType, target, other);
		markDirty();
		if(!world.isRemote&&!(other instanceof ISkyCrateConnector))
		{
			Set<Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(world, getPos());
			if(connections!=null)
				for(Connection connection : new ArrayList<>(connections))
					if(connection.cableType==cableType&&connection.end.equals(other.getConnectionMaster(cableType, target)))
						ImmersiveNetHandler.INSTANCE.removeConnectionAndDrop(connection, world, getPos());
		}
	}

	@Override
	public boolean onSkycrateMeeting(EntitySkyCrate skyCrate)
	{
		TileEntitySkyCratePost master = master();
		if(world==null||world.isRemote||master==null||!master.isValid()||!master.formed||skyCrate.connection==null)
			return false;
		BlockPos wirePos = master.getPOIPos(MultiblockPOI.SKYCRATE_WIRE_MOUNT);
		Set<Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(world, wirePos);
		if(connections!=null)
			for(Connection connection : connections)
				if(connection!=null&&!connection.hasSameConnectors(skyCrate.connection)
						&&STRUCTURE_CATEGORY.equals(connection.cableType.getCategory())&&connection.length > 0)
				{
					skyCrate.setConnection(connection, wirePos, 0);
					return !skyCrate.isDead;
				}
		return false;
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		//Wire and transport state is controlled by the server.
	}
}
