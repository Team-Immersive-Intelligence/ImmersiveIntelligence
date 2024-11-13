package pl.pabilo8.immersiveintelligence.common.compat;

import blusunrize.immersiveengineering.common.util.compat.opencomputers.ManagedEnvironmentIE;
import li.cil.oc.api.API;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataConnector;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Pabilo8
 * @since 27.07.2021
 */
public class OpenComputersHelper extends IICompatModule
{
	@Override
	public void preInit()
	{

	}

	@Override
	public String getName()
	{
		return "OpenComputers";
	}

	@Override
	public void registerRecipes()
	{

	}

	@Override
	public void init()
	{
		API.driver.add(new DataConnectorDriver());
	}

	@Override
	public void postInit()
	{

	}

	public static class DataConnectorDriver extends DriverSidedTileEntity
	{
		@Override
		public ManagedEnvironment createEnvironment(World w, BlockPos bp, EnumFacing f)
		{
			TileEntity te = w.getTileEntity(bp);
			if(te instanceof TileEntityDataConnector)
				return new DataConnectorEnvironment(w, bp);
			return null;
		}

		@Override
		public Class<?> getTileEntityClass()
		{
			return TileEntityDataConnector.class;
		}

	}

	@SuppressWarnings("unused")
	public static class DataConnectorEnvironment extends ManagedEnvironmentIE<TileEntityDataConnector>
	{
		public DataConnectorEnvironment(World w, BlockPos bp)
		{
			super(w, bp, TileEntityDataConnector.class);
		}

		@Callback(doc = "function(packet):nil -- sends a packet to the data network")
		public Object[] send(Context context, Arguments args)
		{
			//takes a table and converts to a data packet
			DataPacket packet = new DataPacket();
			if(args.isTable(0))
			{
				Map<?, ?> map = args.checkTable(0);
				for(char c : DataPacket.varCharacters)
					if(map.containsKey(String.valueOf(c))) //parse into IDataType
					{
						Object o = map.get(String.valueOf(c));
						DataType type;

						if(o instanceof Boolean)
							type = new DataTypeBoolean(((Boolean)o));
						else if(o==null) // TODO: 15.08.2022 not sure
							type = new DataTypeNull();
						else if(o instanceof Number)
						{
							Number num = (Number)o;
							if(num.floatValue()%1 > 0) //is a float
								type = new DataTypeFloat(num.floatValue());
							else
								type = new DataTypeInteger(num.intValue());
						}
						else //string or other type
							type = new DataTypeString(o.toString());
						packet.setVariable(c, type);
					}
			}

			getTileEntity().sendPacket(packet);
			return new Object[]{};
		}

		@Callback(doc = "function():boolean -- returns true if a new data packet has been received")
		public Object[] canReceive(Context context, Arguments args)
		{
			return new Object[]{!getTileEntity().compatReceived};
		}

		@Callback(doc = "function():table -- returns the last received data packet")
		public Object[] receive(Context context, Arguments args)
		{
			TileEntityDataConnector te = getTileEntity();
			if(!te.compatReceived)
			{
				Map<String, Object> map = new HashMap<>();
				for(Entry<Character, DataType> entry : te.lastReceived.variables.entrySet())
					map.put(entry.getKey().toString(), entry.getValue().toString());

				te.compatReceived = true;
				return new Object[]{map};
			}
			return null;
		}

		@Callback(doc = "function():number -- gets amount of devices connected to the data network")
		public Object[] getNetworkSize(Context context, Arguments args)
		{
			return new Object[]{getTileEntity().getDataNetwork().connectors.size()};
		}

		@Override
		public String preferredName()
		{
			return "ii_data_connector";
		}

		@Override
		public int priority()
		{
			return 1000;
		}
	}
}
