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
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataConnector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * Connects II data networks to ComputerCraft peripherals.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 03.10.2026
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
					DataPacket packet = new DataPacket();
					if(args.length > 0)
					{
						// Both 1.12 APIs supply Lua tables as Java maps.
						if(!(args[0] instanceof Map))
							throw new LuaException("Expected a packet table");
						try
						{
							packet = LuaDataConverter.fromLua((Map<?, ?>)args[0]);
						} catch(IllegalArgumentException exception)
						{
							throw new LuaException(exception.getMessage());
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
						// Both APIs convert numeric and keyed Java maps to Lua tables.
						Map<String, Object> map = LuaDataConverter.toLua(te.lastReceived);

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
