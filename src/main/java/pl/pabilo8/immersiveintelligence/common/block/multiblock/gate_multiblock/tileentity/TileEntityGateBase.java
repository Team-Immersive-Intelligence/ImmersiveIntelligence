package pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public abstract class TileEntityGateBase<T extends TileEntityGateBase<T>> extends TileEntityMultiblockIIBase<T> implements IBooleanAnimatedPartsBlock
{
	public boolean open = false;
	public float gateAngle = 0;

	public TileEntityGateBase(MultiblockStuctureBase<T> multiblock)
	{
		super(multiblock);
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		open = nbt.getBoolean("open");
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setBoolean("open", this.open);
		nbt.setFloat("gateAngle", gateAngle);
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		this.open = message.getBoolean("open");
		this.gateAngle = message.getFloat("gateAngle");
	}

	@Override
	protected void onUpdate()
	{
		gateAngle = MathHelper.clamp(gateAngle+(open?3.5f: -6f), 0f, 115f);
	}

	public boolean isDoorPart()
	{
		return (pos > 9&&pos < 15)||(pos > 33&&pos < 38)||(pos > 57&&pos < 62)||(pos > 81&&pos < 86);
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
	public void onAnimationChangeClient(boolean state, int part)
	{
		open = state;
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(state!=open)
		{
			open = state;
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(open, 0, getPos()), IIPacketHandler.targetPointFromPos(this.getPos(), this.world, 32));
		}
	}

	public abstract IBlockState getFenceState(@Nullable EnumFacing facingConnected);
}
