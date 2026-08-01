package pl.pabilo8.immersiveintelligence.common.compat;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataConnector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.07.2021
 */
public class ComputerCraftHelper extends IICompatModule
{
	public static DataConnectorPeripheral createConnectorPeripheral(TileEntityDataConnector te)
	{
		return new DataConnectorPeripheral(te);
	}

	public static boolean isCCTweaked() {
		try {
			Class.forName("dan200.computercraft.api.peripheral.IPeripheralTile");
			return true;
		} catch(ClassNotFoundException e)
		{
			return false;
		}
	}

	@Override
	public void preInit()
	{
	}

	@Override
	public String getName()
	{
		return "ComputerCraft";
	}

	@Override
	public void registerRecipes()
	{

	}

	@Override
	public void init()
	{
		ComputerCraftAPI.registerPeripheralProvider(new DataConnectorPeripheralProvider());
	}

	@Override
	public void postInit()
	{

	}

	@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheralProvider", modid = "computercraft")
	public static class DataConnectorPeripheralProvider implements IPeripheralProvider
	{
		@Nullable
		@Override
		@Optional.Method(modid = "computercraft")
		public IPeripheral getPeripheral(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile != null && tile instanceof TileEntityDataConnector)
			{
				TileEntityDataConnector te = (TileEntityDataConnector)tile;
				return facing==te.getFacing()?ComputerCraftHelper.createConnectorPeripheral(te): null;
			}
			return null;
		}
	}

	@Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = "computercraft")
	public static class DataConnectorPeripheral implements IPeripheral
	{
		private final TileEntityDataConnector te;

		public DataConnectorPeripheral(TileEntityDataConnector te)
		{
			this.te = te;
		}

		@Nonnull
		@Override
		public String getType()
		{
			return "data_connector";
		}

		@Nonnull
		@Override
		public String[] getMethodNames()
		{
			return new String[]{
					"send",
					"canReceive",
					"receive",
					"getNetworkSize"
			};
		}

		@Nullable
		@Override
		public Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext context, int method, @Nonnull Object[] args) throws LuaException
		{
			switch(method)
			{
				case 0:
				{
					//takes a table and converts to a data packet
					DataPacket packet = new DataPacket();
					if(args.length > 0)
					{
						Map<?,?> map;
						if (ComputerCraftHelper.isCCTweaked())
							map = dan200.computercraft.api.lua.ArgumentHelper.optTable(args, 0, new HashMap<>());
						else
							map = dan200.computercraft.core.apis.ArgumentHelper.optTable(args, 0, new HashMap<>());

						for(char c : DataPacket.VARIABLE_NAMES)
							if(map.containsKey(String.valueOf(c))) //parse into IDataType
							{
								Object o = map.get(String.valueOf(c));
								DataType type;

								String strType = "";
								if (ComputerCraftHelper.isCCTweaked())
									strType = dan200.computercraft.api.lua.ArgumentHelper.getType(o);
								else
									strType = dan200.computercraft.core.apis.ArgumentHelper.getType(o);


								switch(strType)
								{
									default:
									case "string":
										type = new DataTypeString(o.toString());
										break;
									case "nil":
										type = new DataTypeNull();
										break;
									case "boolean":
										type = new DataTypeBoolean(((Boolean)o));
										break;
									case "number":
									{
										Number num = (Number)o;
										if(num.floatValue()%1 > 0) //is a float
											type = new DataTypeFloat(num.floatValue());
										else
											type = new DataTypeInteger(num.intValue());
									}
									break;
								}
								packet.set(c, type);
							}
					}

					te.sendPacket(packet);
					return new Object[]{};
				}
				case 1:
					return new Object[]{!te.compatReceived};
				case 2:
				{
					if(!te.compatReceived)
					{
						Map<String, Object> map = new HashMap<>();
						for(DataVariable dataVariable : te.lastReceived)
							map.put(String.valueOf(dataVariable.getName()), dataVariable.getValue().toString());

						te.compatReceived = true;
						return new Object[]{map};
					}
					return null;
				}
				case 3:
					return new Object[]{te.getDataNetwork().connectors.size()};
				default:
					return null;
			}
		}

		//generally shouldn't happen
		@Override
		public boolean equals(@Nullable IPeripheral other)
		{
			return other instanceof DataConnectorPeripheral&&((DataConnectorPeripheral)other).te==this.te;
		}
	}
}
