package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.gate;

import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IFaceShape;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.blocks.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public abstract class TileEntityGateBase<T extends TileEntityMultiblockMetal<T, IMultiblockRecipe>> extends TileEntityMultiblockMetal<T, IMultiblockRecipe> implements IBlockBounds, IPlayerInteraction, IBooleanAnimatedPartsBlock, IFaceShape
{
	public boolean open = false;
	public float gateAngle = 0;

	public TileEntityGateBase(MultiblockStuctureBase<? extends TileEntityGateBase> multiblock)
	{
		super(multiblock, multiblock.getSize(), 0, false);
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		open = nbt.getBoolean("open");
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setBoolean("open",this.open);
		nbt.setFloat("gateAngle",gateAngle);
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		this.open = message.getBoolean("open");
		this.gateAngle = message.getFloat("gateAngle");
	}

	@Override
	public void update()
	{
		super.update();

		if(isDummy())
			return;

		gateAngle = MathHelper.clamp(gateAngle+(open?3.5f: -6f), 0f, 115f);
	}

	public boolean isDoorPart()
	{
		return (pos > 9&&pos < 15)||(pos > 33&&pos < 38)||(pos > 57&&pos < 62)||(pos > 81&&pos < 86);
	}

	@Nonnull
	@Override
	public float[] getBlockBounds()
	{
		T master = master();
		if(!(master instanceof TileEntityGateBase))
			return new float[]{0, 0, 0, 1, 1, 1};

		switch(pos)
		{
			case 80:
			case 8:
			case 56:
			case 32:
			case 87:
			case 63:
			case 39:
			case 15:
				return new float[]{0.3125f, 0, 0.3125f, 0.625f, 1f, 0.625f};
			case 48:
			case 24:
			case 55:
			case 31:
			case 71:
			case 47:
			case 40:
			case 64:
				return new float[]{-5, -5, -5, -5, -5, -5};
			default:
				if(!((TileEntityGateBase<?>)master).open)
					return new float[]{0, 0, 0, 1, 1, 1};
		}

		return new float[]{-5, -5, -5, -5, -5, -5};
	}

	@Nonnull
	@Override
	public int[] getEnergyPos()
	{
		return new int[]{2};
	}

	@Nonnull
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
	public void doProcessOutput(@Nonnull ItemStack output)
	{

	}

	@Override
	public void doProcessFluidOutput(@Nonnull FluidStack output)
	{
	}

	@Override
	public void onProcessFinish(@Nonnull MultiblockProcess<IMultiblockRecipe> process)
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
	public float getMinProcessDistance(@Nonnull MultiblockProcess<IMultiblockRecipe> process)
	{
		return 0;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return NonNullList.create();
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

	@Nonnull
	@Override
	public int[] getOutputSlots()
	{
		return new int[0];
	}

	@Nonnull
	@Override
	public int[] getOutputTanks()
	{
		return new int[0];
	}

	@Override
	public boolean additionalCanProcessCheck(@Nonnull MultiblockProcess<IMultiblockRecipe> process)
	{
		return false;
	}

	@Nonnull
	@Override
	public IFluidTank[] getInternalTanks()
	{
		return new IFluidTank[]{};
	}

	@Nonnull
	@Override
	protected IFluidTank[] getAccessibleFluidTanks(@Nonnull EnumFacing side)
	{
		return new FluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, @Nonnull EnumFacing side, @Nonnull FluidStack resource)
	{
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, @Nonnull EnumFacing side)
	{
		return false;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		this.markDirty();
	}

	@Override
	public IMultiblockRecipe findRecipeForInsertion(@Nonnull ItemStack inserting)
	{
		return null;
	}

	@Override
	protected IMultiblockRecipe readRecipeFromNBT(@Nonnull NBTTagCompound tag)
	{
		return null;
	}

	@Override
	public boolean interact(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		T master = master();
		if(master instanceof TileEntityGateBase&&!player.isSneaking())
		{
			TileEntityGateBase<?> m = (TileEntityGateBase<?>)master;
			m.onAnimationChangeServer(!m.open, 0);
			return true;
		}

		return false;
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
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(open, 0, getPos()), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromPos(this.getPos(), this.world, 32));
		}
	}

	public abstract IBlockState getFenceState(@Nullable EnumFacing facingConnected);

	@Nonnull
	@Override
	public BlockFaceShape getFaceShape(@Nonnull EnumFacing side)
	{
		switch(pos)
		{
			case 80:
			case 8:
			case 56:
			case 32:
			case 87:
			case 63:
			case 39:
			case 15:
				return BlockFaceShape.MIDDLE_POLE;
			default:
				return BlockFaceShape.UNDEFINED;
		}
	}
}
