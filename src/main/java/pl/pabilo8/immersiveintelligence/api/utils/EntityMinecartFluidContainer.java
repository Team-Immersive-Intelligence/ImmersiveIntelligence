package pl.pabilo8.immersiveintelligence.api.utils;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.util.ChatUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

/**
 * Created by Pabilo8 on 27-12-2019.
 */
public abstract class EntityMinecartFluidContainer extends EntityMinecart implements IEntityOverlayText
{
	public FluidTank tank = new FluidTank(getTankCapacity());

	SidedFluidHandler fluidHandler = new SidedFluidHandler(this, null);

	public EntityMinecartFluidContainer(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartFluidContainer(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		readTank(compound);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		writeTank(compound, false);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		return super.getCapability(capability, facing);
	}

	public void writeTank(NBTTagCompound nbt, boolean toItem)
	{
		boolean write = tank.getFluidAmount() > 0;
		NBTTagCompound tankTag = tank.writeToNBT(new NBTTagCompound());
		if(!toItem||write)
			nbt.setTag("tank", tankTag);
	}

	public void readTank(NBTTagCompound nbt)
	{
		if(nbt.hasKey("tank"))
		{
			tank.readFromNBT(nbt.getCompoundTag("tank"));
		}
		tank.setCapacity(getTankCapacity());
	}

	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand)
	{
		FluidStack f = FluidUtil.getFluidContained(player.getHeldItem(hand));
		if(f!=null)
			if(f.getFluid().isGaseous(f)&&!isGasAllowed())
			{
				ChatUtils.sendServerNoSpamMessages(player, new TextComponentTranslation(Lib.CHAT_INFO+"noGasAllowed"));
				return EnumActionResult.FAIL;
			}
			else if(f.getFluid().getTemperature(f) >= getMaxTemperature())
			{
				ChatUtils.sendServerNoSpamMessages(player, new TextComponentTranslation(Lib.CHAT_INFO+"tooHot"));
				return EnumActionResult.FAIL;
			}

		if(FluidUtil.interactWithFluidHandler(player, hand, tank))
		{
			//update
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	public abstract boolean isGasAllowed();

	public abstract int getMaxTemperature();

	public abstract int getTankCapacity();

	static class SidedFluidHandler implements IFluidHandler
	{
		EntityMinecartFluidContainer barrel;
		EnumFacing facing;

		SidedFluidHandler(EntityMinecartFluidContainer barrel, EnumFacing facing)
		{
			this.barrel = barrel;
			this.facing = facing;
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			if(resource==null)
				return 0;

			int i = barrel.tank.fill(resource, doFill);
			if(i > 0)
			{
				//Update
			}
			return i;
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			if(resource==null)
				return null;
			return this.drain(resource.amount, doDrain);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			FluidStack f = barrel.tank.drain(maxDrain, doDrain);
			if(f!=null&&f.amount > 0)
			{
				//Update
			}
			return f;
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			return barrel.tank.getTankProperties();
		}
	}

	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop, boolean hammer)
	{
		if(Utils.isFluidRelatedItemStack(player.getHeldItem(EnumHand.MAIN_HAND)))
		{
			String s = null;
			if(tank.getFluid()!=null)
				s = tank.getFluid().getLocalizedName()+": "+tank.getFluidAmount()+"mB";
			else
				s = I18n.format(Lib.GUI+"empty");
			return new String[]{s};
		}
		return null;
	}

	@Override
	public boolean useNixieFont(EntityPlayer player, RayTraceResult mop)
	{
		return false;
	}
}
