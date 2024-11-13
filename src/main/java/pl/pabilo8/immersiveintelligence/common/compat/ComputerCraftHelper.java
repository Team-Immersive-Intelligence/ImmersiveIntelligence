package pl.pabilo8.immersiveintelligence.common.compat;

import dan200.computercraft.api.lua.ArgumentHelper;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraftforge.fml.common.Optional;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataConnector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Pabilo8
 * @since 27.07.2021
 */
public class ComputerCraftHelper extends IICompatModule
{
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

	}

	@Override
	public void postInit()
	{

	}

	public static DataConnectorPeripheral createConnectorPeripheral(TileEntityDataConnector te)
	{
		return new DataConnectorPeripheral(te);
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
						Map<?, ?> map = ArgumentHelper.optTable(args, 0, new HashMap<>());
						for(char c : DataPacket.varCharacters)
							if(map.containsKey(String.valueOf(c))) //parse into IDataType
							{
								Object o = map.get(String.valueOf(c));
								DataType type;
								switch(ArgumentHelper.getType(o))
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
								packet.setVariable(c, type);
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
						for(Entry<Character, DataType> entry : te.lastReceived.variables.entrySet())
							map.put(entry.getKey().toString(), entry.getValue().toString());

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
