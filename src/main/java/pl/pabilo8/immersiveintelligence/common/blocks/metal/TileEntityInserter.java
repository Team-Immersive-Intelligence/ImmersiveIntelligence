package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEProperties.PropertyBoolInverted;
import blusunrize.immersiveengineering.api.energy.IRotationAcceptor;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.*;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Pabilo8 on 15-07-2019.
 */
public class TileEntityInserter extends TileEntityImmersiveConnectable implements IDirectionalTile, IIEInventory, ITileDrop, IComparatorOverride, IHammerInteraction, ITickable, IBlockBounds, IRotationAcceptor, IDataDevice, IDualState
{
	WireType secondCable;
	//The held item
	NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
	public EnumFacing outputFacing = EnumFacing.NORTH;
	public EnumFacing inputFacing = EnumFacing.SOUTH;


	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 1);
		outputFacing = EnumFacing.getFront(nbt.getInteger("outputFacing"));
		inputFacing = EnumFacing.getFront(nbt.getInteger("inputFacing"));
		if(nbt.hasKey("secondCable"))
			secondCable = ApiUtils.getWireTypeFromNBT(nbt, "secondCable");
		else
			secondCable = null;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setTag("inventory", Utils.writeInventory(inventory));
		nbt.setInteger("outputFacing", outputFacing.ordinal());
		nbt.setInteger("inputFacing", inputFacing.ordinal());
		if(secondCable!=null)
			nbt.setString("secondCable", secondCable.getUniqueName());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	IItemHandler insertionHandler = new IEInventoryHandler(1, this);

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)insertionHandler;
		return super.getCapability(capability, facing);
	}

	@Override
	public void inputRotation(double rotation, @Nonnull EnumFacing side)
	{

	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[0];
	}

	@Override
	public int getComparatorInputOverride()
	{
		return 0;
	}

	@Override
	public void readOnPlacement(@Nullable EntityLivingBase placer, ItemStack stack)
	{

	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return null;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 0;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{

	}

	@Override
	public void update()
	{

	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(side==inputFacing)
		{
			inputFacing = null;
			outputFacing = side;
			return true;
		}
		else if(side==outputFacing)
		{
			outputFacing = null;
			return true;
		}
		else
		{
			inputFacing = side;
			return true;
		}
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return null;
	}

	@Override
	public EnumFacing getFacing()
	{
		return outputFacing;
	}

	@Override
	public void setFacing(EnumFacing facing)
	{
		outputFacing = facing;
	}

	@Override
	public int getFacingLimitation()
	{
		return 2;
	}

	@Override
	public boolean mirrorFacingOnPlacement(EntityLivingBase placer)
	{
		return false;
	}

	@Override
	public boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity)
	{
		return false;
	}

	@Override
	public boolean canRotate(EnumFacing axis)
	{
		return false;
	}

	@Override
	public void onReceive(DataPacket packet)
	{

	}

	@Override
	public void onSend()
	{

	}

	@Override
	public boolean getIsSecondState()
	{
		return false;
	}

	@Override
	public PropertyBoolInverted getBoolProperty(Class<? extends IUsesBooleanProperty> inf)
	{
		return null;
	}
}
