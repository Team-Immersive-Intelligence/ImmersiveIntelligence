package pl.pabilo8.immersiveintelligence.common.block.simple.tileentity.backpack;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools.Backpacks;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIBackpack.BackpackType;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Stores fluid and transfers it to the block below or a held container.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 18.11.2026
 */
public class TileEntityCanisterBackpack extends TileEntityBackpack implements ITickable
{
	@SyncNBT
	public FluidTank tank = new FluidTank(Backpacks.fluidpackCapacity)
	{
		@Override
		protected void onContentsChanged()
		{
			markDirty();
		}
	};

	public TileEntityCanisterBackpack()
	{
		super(BackpackType.CANISTER);
	}

	@Override
	public void readOnPlacement(@Nullable EntityLivingBase placer, ItemStack stack)
	{
		super.readOnPlacement(placer, stack);
		IFluidHandlerItem handler = FluidUtil.getFluidHandler(backpack);
		if(handler!=null)
			tank.setFluid(handler.drain(Integer.MAX_VALUE, true));
	}

	@Nonnull
	@Override
	public ItemStack getTileDrop(@Nullable EntityPlayer player, @Nonnull IBlockState state)
	{
		ItemStack stack = backpack.copy();
		IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack);
		if(handler!=null&&tank.getFluid()!=null)
			handler.fill(tank.getFluid(), true);
		return stack;
	}

	@Override
	public void update()
	{
		if(world.isRemote||tank.getFluidAmount()==0)
			return;
		BlockPos below = pos.down();
		if(!world.isBlockLoaded(below))
			return;
		IFluidHandler target = FluidUtil.getFluidHandler(world, below, EnumFacing.UP);
		if(target!=null)
			FluidUtil.tryFluidTransfer(target, tank, Backpacks.fluidpackTransferRate, true);
	}

	@Override
	public boolean onInteract(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem)
	{
		if(FluidUtil.getFluidHandler(heldItem)!=null)
			return world.isRemote||FluidUtil.interactWithFluidHandler(player, hand, tank);
		return super.onInteract(side, player, hand, heldItem);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing side)
	{
		return capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY&&(side==null||side==EnumFacing.DOWN)
				||super.hasCapability(capability, side);
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing side)
	{
		if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY&&(side==null||side==EnumFacing.DOWN))
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
		return super.getCapability(capability, side);
	}
}
