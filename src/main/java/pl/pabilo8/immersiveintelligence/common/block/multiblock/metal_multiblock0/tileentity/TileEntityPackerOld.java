package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.IEApi;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDevice0;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Packer;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPackerOld;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
@Deprecated
public class TileEntityPackerOld extends TileEntityMultiblockMetal<TileEntityPackerOld, IMultiblockRecipe> implements IDataDevice, IConveyorAttachable
{
	//First - the crate to which items are inserted, second - stack to insert, returns the stack if failed
	public static HashMap<Predicate<ItemStack>, Function<Tuple<ItemStack, ItemStack>, ItemStack>> predicates = new HashMap<>();

	public NonNullList<ItemStack> inventory = NonNullList.withSize(28, ItemStack.EMPTY);
	public int processTime = 0, animation = 0;
	public boolean active = false;
	public boolean ignoreEmptyStacks = false;
	public int packingMode = 0;
	public int itemsToPack = 0;
	public int currentItemsToPack = 0;
	public int insertQuantity = 64;
	IItemHandler crateHandler = new IEInventoryHandler(1, this, 0, true, true);
	IItemHandler inventoryHandler = new IEInventoryHandler(27, this, 1, true, true);

	public TileEntityPackerOld()
	{
		super(MultiblockPackerOld.INSTANCE, new int[]{3, 3, 6}, Packer.energyCapacity, false);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!isDummy())
		{
			if(!descPacket)
				inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 28);
			itemsToPack = nbt.getInteger("itemsToPack");
			currentItemsToPack = nbt.getInteger("currentItemsToPack");
			packingMode = nbt.getInteger("packingMode");
			ignoreEmptyStacks = nbt.getBoolean("ignoreEmptyStacks");
			insertQuantity = nbt.getInteger("insertQuantity");

			processTime = nbt.getInteger("processTime");
			animation = nbt.getInteger("animation");

		}
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(!isDummy())
		{
			if(!descPacket)
				nbt.setTag("inventory", Utils.writeInventory(inventory));
			nbt.setInteger("itemsToPack", itemsToPack);
			nbt.setInteger("currentItemsToPack", currentItemsToPack);
			nbt.setInteger("packingMode", packingMode);
			nbt.setBoolean("ignoreEmptyStacks", ignoreEmptyStacks);
			nbt.setInteger("insertQuantity", insertQuantity);

			nbt.setInteger("processTime", processTime);
			nbt.setInteger("animation", animation);

		}
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("processTime"))
			this.processTime = message.getInteger("processTime");
		if(message.hasKey("animation"))
			this.animation = message.getInteger("animation");
		if(message.hasKey("active"))
			this.active = message.getBoolean("active");
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 28);

		if(message.hasKey("itemsToPack"))
			itemsToPack = message.getInteger("itemsToPack");
		if(message.hasKey("currentItemsToPack"))
			currentItemsToPack = message.getInteger("currentItemsToPack");
		if(message.hasKey("packingMode"))
			packingMode = message.getInteger("packingMode");
		if(message.hasKey("ignoreEmptyStacks"))
			ignoreEmptyStacks = message.getBoolean("ignoreEmptyStacks");
		if(message.hasKey("insertQuantity"))
			insertQuantity = message.getInteger("insertQuantity");

		if(message.hasKey("processTime"))
			processTime = message.getInteger("processTime");
		if(message.hasKey("animation"))
			animation = message.getInteger("animation");
	}

	@Override
	public void update()
	{
		super.update();
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{41};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{};
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return false;
	}

	@Override
	public void doProcessOutput(ItemStack output)
	{

	}

	@Override
	public void doProcessFluidOutput(FluidStack output)
	{
	}

	@Override
	public void onProcessFinish(MultiblockProcess<IMultiblockRecipe> process)
	{

	}

	@Override
	public int getMaxProcessPerTick()
	{
		return 1;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 1;
	}

	@Override
	public float getMinProcessDistance(MultiblockProcess<IMultiblockRecipe> process)
	{
		return 0;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		if(slot==0)
		{
			for(Predicate<ItemStack> p : predicates.keySet())
				if(p.test(stack))
					return true;
		}
		return true;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return slot==0?1: 64;
	}

	@Override
	public int[] getOutputSlots()
	{
		return new int[]{1};
	}

	@Override
	public int[] getOutputTanks()
	{
		return new int[]{};
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<IMultiblockRecipe> process)
	{
		return false;
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return new IFluidTank[]{};
	}

	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		return new FluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource)
	{
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, EnumFacing side)
	{
		return false;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		NBTTagCompound tag = new NBTTagCompound();

		if(slot==1)
		{
			tag.setTag("inventory", Utils.writeInventory(inventory));
			tag.setInteger("itemsToPack", itemsToPack);
			tag.setInteger("packingMode", packingMode);
			tag.setBoolean("ignoreEmptyStacks", ignoreEmptyStacks);
			tag.setInteger("insertQuantity", insertQuantity);
		}

		tag.setInteger("processTime", processTime);
		tag.setInteger("animation", animation);

		ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, tag), IIUtils.targetPointFromTile(this, 32));
	}

	@Override
	public IMultiblockRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	protected IMultiblockRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return null;
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		TileEntityPackerOld master = master();
		if(master==null)
			return;
		if(this.pos==30&&master.energyStorage.getEnergyStored() >= Packer.energyUsage)
		{
			master.energyStorage.extractEnergy(Packer.energyUsage, false);

			if(packet.getPacketVariable('c') instanceof DataTypeInteger)
				master.itemsToPack = ((DataTypeInteger)packet.getPacketVariable('c')).value;
			if(packet.variables.containsKey('m'))
			{
				if(packet.getPacketVariable('m') instanceof DataTypeInteger)
				{
					master.packingMode = MathHelper.clamp(((DataTypeInteger)packet.getPacketVariable('m')).value, 0, PackingModes.values().length-1);
				}
				else if(packet.getPacketVariable('m') instanceof DataTypeString)
				{
					try
					{
						PackingModes mode = PackingModes.valueOf(((DataTypeString)packet.getPacketVariable('m')).value.toUpperCase());
						master.packingMode = mode.ordinal();
					}
					catch(IllegalArgumentException e)
					{

					}
				}
			}

			if(packet.getPacketVariable('q') instanceof DataTypeInteger)
				master.insertQuantity = ((DataTypeInteger)packet.getPacketVariable('q')).value;

			if(packet.getPacketVariable('i') instanceof DataTypeBoolean)
				master.ignoreEmptyStacks = ((DataTypeBoolean)packet.getPacketVariable('i')).value;

		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return master()!=null&&(pos==24||pos==2);
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityPackerOld master = master();
			if(master==null)
				return null;
			if(pos==24)
				return (T)master.inventoryHandler;
			else if(pos==2)
				return (T)master.crateHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public EnumFacing[] sigOutputDirections()
	{
		if(pos==2||pos==14)
			return new EnumFacing[]{facing};
		else if(pos==28)
			return new EnumFacing[]{facing.rotateY()};
		return new EnumFacing[0];
	}

	enum PackingModes
	{
		ONE_BY_ONE,
		ONE_OF_ALL
	}
}
