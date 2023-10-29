package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorBelt;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorTile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ScanningConveyor;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockScanningConveyor;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Pabilo8
 * @updated 28.10.2023
 * @since 28-06-2019
 */
public class TileEntityScanningConveyor extends TileEntityMultiblockIIGeneric<TileEntityScanningConveyor> implements IConveyorAttachable, IConveyorTile
{
	public HashMap<EntityItem, Integer> lastScanned = new HashMap<>();

	public TileEntityScanningConveyor()
	{
		super(MultiblockScanningConveyor.INSTANCE);
		this.energyStorage = new FluxStorageAdvanced(ScanningConveyor.energyCapacity);
	}

	@Override
	protected void onUpdate()
	{
		//Copy the set
		if(lastScanned.isEmpty())
			return;

		HashSet<EntityItem> items = new HashSet<>(lastScanned.keySet());
		for(EntityItem entityItem : items)
			lastScanned.computeIfPresent(entityItem, (entityItem1, integer) -> integer >= 1?integer-1: null);
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case DATA_OUTPUT:
				return getPOI("data");
			case ENERGY_INPUT:
				return getPOI("power");
			case REDSTONE_INPUT:
				return getPOI("redstone");
		}
		return new int[0];
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		if(isPOI("conveyor"))
		{
			if(getRedstoneAtPos(getPOI(MultiblockPOI.REDSTONE_INPUT)[0]))
				return;

			getConveyorSubtype().onEntityCollision(this, entity, facing);
			if(entity instanceof EntityItem)
			{
				if(!lastScanned.containsKey(entity))
				{
					if(energyStorage.extractEnergy(ScanningConveyor.energyUsage, false) < ScanningConveyor.energyUsage)
						return;

					if(!world.isRemote)
					{
						ItemStack stack = ((EntityItem)entity).getItem();
						DataPacket packet = new DataPacket();
						packet.setVariable('s', new DataTypeItemStack(stack));
						sendData(packet, facing.rotateY(), getPOI(MultiblockPOI.DATA_OUTPUT)[0]);

					}
					lastScanned.put(((EntityItem)entity), 5);
				}
				else
					lastScanned.compute(((EntityItem)entity), (entityItem, integer) -> 2);
			}
		}

		/*if(pos==1&&!world.isRemote&&entity!=null&&!entity.isDead&&entity instanceof EntityItem&&!((EntityItem)entity).getItem().isEmpty() &&pl.pabilo8.immersiveintelligence.common.Utils.getDistanceBetweenPos(entity.getPosition(),this.getPos().offset(facing.getOpposite()),false)==0f)
		{
			ItemStack stack = ((EntityItem)entity).getItem();
			if(stack.isEmpty())
				return;
			if(inventory.get(0).isEmpty() && inventoryHandler.insertItem(0, stack, false).isEmpty())
			{
				((EntityItem)entity).setItem(ItemStack.EMPTY);
				entity.setDead();
				processTime=0;
				processTimeMax=60;
			}

		}*/
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Override
	public IConveyorBelt getConveyorSubtype()
	{
		return ConveyorHandler.getConveyor(new ResourceLocation("immersiveengineering:conveyor"), this);
	}

	@Override
	public void setConveyorSubtype(IConveyorBelt conveyor)
	{

	}

	@Override
	public EnumFacing[] sigOutputDirections()
	{
		return new EnumFacing[]{facing};
	}
}
