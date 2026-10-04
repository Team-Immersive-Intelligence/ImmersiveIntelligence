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
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataConnector;
import scala.collection.JavaConverters;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Connects II data networks to OpenComputers components.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 03.10.2026
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

		@Callback(doc = "function(packet):nil -- sends primitive values, arrays, maps and typed objects to the data network")
		public Object[] send(Context context, Arguments args)
		{
			DataPacket packet = args.count()==0?new DataPacket():
					LuaDataConverter.fromLua((Map<?, ?>)normaliseLuaValue(args.checkTable(0), 0));

			getTileEntity().sendPacket(packet);
			return new Object[]{};
		}

		private static Object normaliseLuaValue(Object value, int depth)
		{
			if(value instanceof byte[])
				return new String((byte[])value, StandardCharsets.UTF_8);
			// The LuaJ architecture can retain Scala maps inside a Java table view.
			if(value instanceof scala.collection.Map)
				value = JavaConverters.mapAsJavaMapConverter((scala.collection.Map<?, ?>)value).asJava();
			if(!(value instanceof Map))
				return value;
			if(depth > 64)
				throw new IllegalArgumentException("Tables are cyclic or exceed 64 nested levels");
			Map<Object, Object> result = new LinkedHashMap<>();
			for(Map.Entry<?, ?> entry : ((Map<?, ?>)value).entrySet())
				result.put(normaliseLuaValue(entry.getKey(), depth+1), normaliseLuaValue(entry.getValue(), depth+1));
			return result;
		}

		@Callback(doc = "function():boolean -- returns true if a new data packet has been received")
		public Object[] canReceive(Context context, Arguments args)
		{
			return new Object[]{!getTileEntity().compatReceived};
		}

		@Callback(doc = "function():table -- returns the last packet; use ipairs for arrays and pairs for maps")
		public Object[] receive(Context context, Arguments args)
		{
			TileEntityDataConnector te = getTileEntity();
			if(!te.compatReceived)
			{
				Map<String, Object> map = LuaDataConverter.toLua(te.lastReceived);

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
