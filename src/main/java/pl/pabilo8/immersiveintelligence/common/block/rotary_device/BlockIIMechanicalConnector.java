package pl.pabilo8.immersiveintelligence.common.block.rotary_device;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalConnector.IIBlockTypes_MechanicalConnector;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalWheel;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIITileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumTileProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileProviderEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 2019-05-17
 */
public class BlockIIMechanicalConnector extends BlockIITileProvider<IIBlockTypes_MechanicalConnector>
{
	public enum IIBlockTypes_MechanicalConnector implements IITileProviderEnum
	{
		@EnumTileProvider(tile = TileEntityMechanicalWheel.class)
		WOODEN_WHEEL
	}

	public BlockIIMechanicalConnector()
	{
		super("mechanical_connector", Material.IRON, PropertyEnum.create("type", IIBlockTypes_MechanicalConnector.class), ItemBlockIIBase::new,
				IEProperties.FACING_ALL, IOBJModelCallback.PROPERTY, IEProperties.CONNECTIONS);
		setHardness(3.0F);
		setResistance(15.0F);

		setToolTypes(IIReference.TOOL_HAMMER);

		setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
		addToTESRMap(IIBlockTypes_MechanicalConnector.WOODEN_WHEEL);

	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		super.neighborChanged(state, world, pos, blockIn, fromPos);
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityMechanicalWheel)
		{
			TileEntityMechanicalWheel connector = (TileEntityMechanicalWheel)te;
			if(world.isAirBlock(pos.offset(connector.facing)))
			{
				this.dropBlockAsItem(connector.getWorld(), pos, world.getBlockState(pos), 0);
				connector.getWorld().setBlockToAir(pos);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public ItemStack getPickBlock(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, @Nullable EntityPlayer player)
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