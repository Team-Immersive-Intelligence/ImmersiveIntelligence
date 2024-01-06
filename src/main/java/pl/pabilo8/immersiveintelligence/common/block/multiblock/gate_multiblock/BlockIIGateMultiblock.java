package pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
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
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.BlockIIGateMultiblock.IIBlockTypes_FenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockAluminiumChainFenceGate.TileEntityAluminiumChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockAluminiumFenceGate.TileEntityAluminiumFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockSteelChainFenceGate.TileEntitySteelChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockSteelFenceGate.TileEntitySteelFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockWoodenChainFenceGate.TileEntityWoodenChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockWoodenFenceGate.TileEntityWoodenFenceGate;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.EnumMultiblockProvider;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockProperties;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IITileMultiblockEnum;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 23.12.2021
 */
public class BlockIIGateMultiblock extends BlockIIMultiblock<IIBlockTypes_FenceGate>
{
	public enum IIBlockTypes_FenceGate implements IITileMultiblockEnum
	{
		@EnumMultiblockProvider(multiblock = MultiblockWoodenFenceGate.class, tile = TileEntityWoodenFenceGate.class)
		WOODEN,
		@EnumMultiblockProvider(multiblock = MultiblockWoodenChainFenceGate.class, tile = TileEntityWoodenChainFenceGate.class)
		WOODEN_CHAIN,
		@IIBlockProperties(hardness = 20, blastResistance = 10)
		@EnumMultiblockProvider(multiblock = MultiblockSteelFenceGate.class, tile = TileEntitySteelFenceGate.class)
		STEEL,
		@IIBlockProperties(hardness = 20, blastResistance = 10)
		@EnumMultiblockProvider(multiblock = MultiblockSteelChainFenceGate.class, tile = TileEntitySteelChainFenceGate.class)
		STEEL_CHAIN,
		@EnumMultiblockProvider(multiblock = MultiblockAluminiumFenceGate.class, tile = TileEntityAluminiumFenceGate.class)
		ALUMINIUM,
		@EnumMultiblockProvider(multiblock = MultiblockAluminiumChainFenceGate.class, tile = TileEntityAluminiumChainFenceGate.class)
		ALUMINIUM_CHAIN
	}

	public BlockIIGateMultiblock()
	{
		super("gate_multiblock", Material.IRON, PropertyEnum.create("type", IIBlockTypes_FenceGate.class),
				IEProperties.FACING_HORIZONTAL,
				IEProperties.BOOLEANS[0], IEProperties.BOOLEANS[1], IEProperties.CONNECTIONS, IEProperties.MULTIBLOCKSLAVE,
				Properties.AnimationProperty, IEProperties.DYNAMICRENDER, IOBJModelCallback.PROPERTY
		);
		setHardness(3.0F);
		setResistance(15.0F);

		setSubMaterial(IIBlockTypes_FenceGate.WOODEN, Material.WOOD);
		setSubMaterial(IIBlockTypes_FenceGate.WOODEN_CHAIN, Material.WOOD);
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
