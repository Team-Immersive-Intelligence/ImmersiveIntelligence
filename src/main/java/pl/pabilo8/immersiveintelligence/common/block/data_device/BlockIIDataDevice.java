package pl.pabilo8.immersiveintelligence.common.block.data_device;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.*;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityChemicalDispenser;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityFluidInserter;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityAdvancedInserter;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter.TileEntityInserter;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumTileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.TernaryValue;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 2019-05-31
 */
public class BlockIIDataDevice extends BlockIITileProvider<IIBlockTypes_Connector>
{
	public BlockIIDataDevice()
	{
		super("data_connector", Material.IRON, PropertyEnum.create("type", IIBlockTypes_Connector.class), ItemBlockIIBase::new,
				IEProperties.FACING_ALL, IEProperties.BOOLEANS[0], IEProperties.CONNECTIONS, IEProperties.DYNAMICRENDER, IOBJModelCallback.PROPERTY, Properties.AnimationProperty);
		setHardness(3.0F);
		setResistance(15.0F);
		setLightOpacity(0);
		setToolTypes(IIReference.TOOL_HAMMER);

		addToTESRMap(IIBlockTypes_Connector.FLUID_INSERTER);
	}

	public enum IIBlockTypes_Connector implements IITileProviderEnum
	{
		@IIBlockProperties(category = IICategory.ELECTRONICS)
		@EnumTileProvider(tile = TileEntityDataConnector.class)
		DATA_CONNECTOR,
		@IIBlockProperties(category = IICategory.ELECTRONICS)
		@EnumTileProvider(tile = TileEntityDataRelay.class)
		DATA_RELAY,
		@IIBlockProperties(category = IICategory.ELECTRONICS)
		@EnumTileProvider(tile = TileEntityAlarmSiren.class)
		ALARM_SIREN,
		@IIBlockProperties(category = IICategory.LOGISTICS)
		@EnumTileProvider(tile = TileEntityInserter.class)
		INSERTER,
		@IIBlockProperties(category = IICategory.LOGISTICS)
		@EnumTileProvider(tile = TileEntityFluidInserter.class)
		FLUID_INSERTER,
		@IIBlockProperties(category = IICategory.LOGISTICS)
		@EnumTileProvider(tile = TileEntityAdvancedInserter.class)
		ADVANCED_INSERTER,
		@IIBlockProperties(hidden = TernaryValue.TRUE, category = IICategory.LOGISTICS)
		ADVANCED_FLUID_INSERTER,
		@IIBlockProperties(category = IICategory.ELECTRONICS)
		@EnumTileProvider(tile = TileEntityChemicalDispenser.class)
		CHEMICAL_DISPENSER,
		@IIBlockProperties(category = IICategory.ELECTRONICS)
		@EnumTileProvider(tile = TileEntityProgrammableSpeaker.class)
		PROGRAMMABLE_SPEAKER,
		@IIBlockProperties(category = IICategory.ELECTRONICS)
		@EnumTileProvider(tile = TileEntityDataDebugger.class)
		DATA_DEBUGGER,
		@IIBlockProperties(category = IICategory.ELECTRONICS)
		@EnumTileProvider(tile = TileEntityDataCallbackConnector.class)
		DATA_DUPLEX_CONNECTOR
	}

	@Override
	public String getMappingsExtension(int meta, boolean itemBlock)
	{
		switch(enumValues[meta])
		{
			default:
				return null;

			case DATA_CONNECTOR:
			case DATA_RELAY:
			case DATA_DUPLEX_CONNECTOR:
				return "plug";

			case INSERTER:
				return "inserter";
			case ADVANCED_INSERTER:
				return "advanced_inserter";

			case DATA_DEBUGGER:
				return "data_debugger";

			case CHEMICAL_DISPENSER:
				return "chemical_dispenser";
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, world, pos, blockIn, fromPos);

		switch(state.getValue(property))
		{
			case DATA_CONNECTOR:
			case DATA_DUPLEX_CONNECTOR:
			case DATA_RELAY:
			{
				TileEntity te = world.getTileEntity(pos);
				if(te==null)
					break;

				TileEntityImmersiveConnectable connector = (TileEntityImmersiveConnectable & IDirectionalTile)te;
				if(world.isAirBlock(pos.offset(((IDirectionalTile)connector).getFacing())))
				{
					this.dropBlockAsItem(connector.getWorld(), pos, world.getBlockState(pos), 0);
					connector.getWorld().setBlockToAir(pos);
				}
			}
			break;
			default:
				break;
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		//Select the wire if the player is sneaking
		if(player!=null&&player.isSneaking())
		{
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof IImmersiveConnectable)
			{
				TargetingInfo subTarget;
				if(target.hitVec!=null)
					subTarget = new TargetingInfo(target.sideHit, (float)target.hitVec.x-pos.getX(), (float)target.hitVec.y-pos.getY(), (float)target.hitVec.z-pos.getZ());
				else
					subTarget = new TargetingInfo(target.sideHit, 0, 0, 0);
				BlockPos masterPos = ((IImmersiveConnectable)te).getConnectionMaster(null, subTarget);
				if(masterPos!=pos)
					te = world.getTileEntity(masterPos);
				if(te instanceof IImmersiveConnectable)
				{
					IImmersiveConnectable connectable = (IImmersiveConnectable)te;
					WireType wire = connectable.getCableLimiter(subTarget);
					if(wire!=null)
						return wire.getWireCoil();
					ArrayList<ItemStack> applicableWires = new ArrayList<>();
					NonNullList<ItemStack> pInventory = player.inventory.mainInventory;
					for(ItemStack s : pInventory)
					{
						if(s.getItem() instanceof IWireCoil)
						{
							IWireCoil coilItem = (IWireCoil)s.getItem();
							wire = coilItem.getWireType(s);
							if(connectable.canConnectCable(wire, subTarget, pos.subtract(masterPos))&&coilItem.canConnectCable(s, te))
							{
								ItemStack coil = wire.getWireCoil();
								boolean unique = true;
								int insertIndex = applicableWires.size();
								for(int j = 0; j < applicableWires.size(); j++)
								{
									ItemStack priorWire = applicableWires.get(j);
									if(coil.getItem()==priorWire.getItem()) //sort same item by metadata
									{
										if(coil.getMetadata()==priorWire.getMetadata())
										{
											unique = false;
											break;
										}
										if(coil.getMetadata() < priorWire.getMetadata())
										{
											insertIndex = j;
											break;
										}
									}
									/*sort different item by itemID (can't guarantee a static list otherwise. switching items by pickBlock changes the order in which things are looked at,
									making for scenarios in which applicable wires are possibly skipped when 3 or more wire Items are present)*/
									else
									{
										int coilID = Item.REGISTRY.getIDForObject(coil.getItem());
										int priorID = Item.REGISTRY.getIDForObject(priorWire.getItem());
										if(coilID < priorID)
										{
											insertIndex = j;
											break;
										}
									}
								}
								if(unique)
									applicableWires.add(insertIndex, coil);
							}
						}
					}
					if(applicableWires.size() > 0)
					{
						ItemStack heldItem = pInventory.get(player.inventory.currentItem);
						if(heldItem.getItem() instanceof IWireCoil)
							//cycle through to the next applicable wire, if currently held wire is already applicable
							for(int i = 0; i < applicableWires.size(); i++)
								if(heldItem.isItemEqual(applicableWires.get(i)))
									return applicableWires.get((i+1)%applicableWires.size()); //wrap around on i+1 >= applicableWires.size()
						return applicableWires.get(0);
					}
				}
			}
		}
		return super.getPickBlock(state, target, world, pos, player);
	}
}
