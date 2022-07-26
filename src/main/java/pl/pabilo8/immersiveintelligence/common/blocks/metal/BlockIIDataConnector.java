package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.inserter.TileEntityAdvancedInserter;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.inserter.TileEntityInserter;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Connector;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 2019-05-31
 */
public class BlockIIDataConnector extends BlockIITileProvider<IIBlockTypes_Connector>
{
	public BlockIIDataConnector()
	{
		super("data_connector", Material.IRON, PropertyEnum.create("type", IIBlockTypes_Connector.class), ItemBlockIEBase.class,
				IEProperties.FACING_ALL, IEProperties.BOOLEANS[0], IEProperties.DYNAMICRENDER, IOBJModelCallback.PROPERTY, Properties.AnimationProperty);
		setHardness(3.0F);
		setResistance(15.0F);
		lightOpacity = 0;
		setAllNotNormalBlock();

		addToTESRMap(IIBlockTypes_Connector.FLUID_INSERTER);
		setMetaHidden(IIBlockTypes_Connector.ADVANCED_FLUID_INSERTER.getMeta());

	}

	@Override
	public boolean useCustomStateMapper()
	{
		return true;
	}

	@Override
	public String getCustomStateMapping(int meta, boolean itemBlock)
	{
		switch(IIBlockTypes_Connector.values()[meta])
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
	protected BlockStateContainer createBlockState()
	{
		BlockStateContainer base = super.createBlockState();
		IUnlistedProperty[] unlisted = (base instanceof ExtendedBlockState)?((ExtendedBlockState)base).getUnlistedProperties().toArray(new IUnlistedProperty[0]): new IUnlistedProperty[0];
		unlisted = Arrays.copyOf(unlisted, unlisted.length+1);
		unlisted[unlisted.length-1] = IEProperties.CONNECTIONS;
		return new ExtendedBlockState(this, base.getProperties().toArray(new IProperty[0]), unlisted);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getExtendedState(state, world, pos);
		if(state instanceof IExtendedBlockState)
		{
			IExtendedBlockState ext = (IExtendedBlockState)state;
			TileEntity te = world.getTileEntity(pos);
			if(!(te instanceof TileEntityImmersiveConnectable))
				return state;
			state = ext.withProperty(IEProperties.CONNECTIONS, ((TileEntityImmersiveConnectable)te).genConnBlockstate());
		}
		return state;
	}

	@Override
	public boolean canIEBlockBePlaced(World world, BlockPos pos, IBlockState newState, EnumFacing side, float hitX, float hitY, float hitZ, EntityPlayer player, ItemStack stack)
	{
		return true;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, world, pos, blockIn, fromPos);
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityDataConnector||te instanceof TileEntityDataRelay)
		{
			TileEntityImmersiveConnectable connector = (TileEntityImmersiveConnectable & IDirectionalTile)te;
			if(world.isAirBlock(pos.offset(((IDirectionalTile)connector).getFacing())))
			{
				this.dropBlockAsItem(connector.getWorld(), pos, world.getBlockState(pos), 0);
				connector.getWorld().setBlockToAir(pos);
			}
		}
	}

	@Override
	public TileEntity createBasicTE(World world, IIBlockTypes_Connector type)
	{
		switch(type)
		{
			case DATA_CONNECTOR:
				return new TileEntityDataConnector();
			case DATA_RELAY:
				return new TileEntityDataRelay();
			case ALARM_SIREN:
				return new TileEntityAlarmSiren();
			case PROGRAMMABLE_SPEAKER:
				return new TileEntityProgrammableSpeaker();
			case INSERTER:
				return new TileEntityInserter();
			case ADVANCED_INSERTER:
				return new TileEntityAdvancedInserter();
			case FLUID_INSERTER:
				return new TileEntityFluidInserter();
			case CHEMICAL_DISPENSER:
				return new TileEntityChemicalDispenser();
			case DATA_DEBUGGER:
				return new TileEntityDataDebugger();
			case DATA_DUPLEX_CONNECTOR:
				return new TileEntityDataCallbackConnector();
			//case ADVANCED_FLUID_INSERTER:
			//	return new TileEntityAdvancedFluidInserter();
		}
		return null;
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
				TargetingInfo subTarget = null;
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
					ArrayList<ItemStack> applicableWires = new ArrayList<ItemStack>();
					NonNullList<ItemStack> pInventory = player.inventory.mainInventory;
					for(int i = 0; i < pInventory.size(); i++)
					{
						ItemStack s = pInventory.get(i);
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

	@Override
	public boolean allowHammerHarvest(IBlockState state)
	{
		return true;
	}
}
