package pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IComparatorOverride;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IConfigurableSides;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import pl.pabilo8.immersiveintelligence.api.rotary.*;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageRotaryPowerSync;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileEntityGearbox extends TileEntityIEBase implements ITickable, IAdvancedTextOverlay, IConfigurableSides,
		IComparatorOverride, ITileDrop, IGuiTile, IIEInventory, IRotationalEnergyBlock
{
	public SideConfig[] sideConfig = {SideConfig.NONE, SideConfig.INPUT, SideConfig.NONE, SideConfig.NONE, SideConfig.NONE, SideConfig.NONE};
	public int comparatorOutput = 0;
	NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
	float efficiency = 0;
	public RotaryStorage rotation = new RotaryStorage(0, 0)
	{
		@Override
		public RotationSide getSide(@Nullable EnumFacing facing)
		{
			switch(sideConfig[facing.getIndex()])
			{
				case INPUT:
					return RotationSide.INPUT;
				case OUTPUT:
					return RotationSide.OUTPUT;
				default:
					return RotationSide.NONE;
			}
		}
	};

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityRotaryEnergy.ROTARY_ENERGY)
		{
			if(facing==null)
				return true;
			if(sideConfig[facing.getIndex()]!=SideConfig.NONE)
				return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityRotaryEnergy.ROTARY_ENERGY)
		{
			if(facing==null)
				return (T)rotation;
			if(sideConfig[facing.getIndex()]!=SideConfig.NONE)
				return (T)rotation;
		}
		return super.getCapability(capability, facing);
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		if(world.isRemote)
			return;

		//Do not output power if redstone is supplied
		if(world.isBlockPowered(pos))
		{
			rotation.grow(0, 0, 0.98f);
			return;
		}

		if(world.getTotalWorldTime()%20==0)
		{
			rotation.setRotationSpeed(0);
			rotation.setTorque(0);
			ArrayList<IRotaryEnergy> in = new ArrayList<>();
			ArrayList<IRotaryEnergy> out = new ArrayList<>();

			for(int i = 0; i < sideConfig.length; i++)
			{
				SideConfig s = sideConfig[i];

				if(s==SideConfig.NONE)
					continue;

				EnumFacing f = EnumFacing.getFront(i);
				BlockPos p = pos.offset(f);

				if(world.getTileEntity(p)==null)
					continue;
				if(!world.getTileEntity(p).hasCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, f.getOpposite()))
					continue;

				IRotaryEnergy energy = world.getTileEntity(p).getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, f.getOpposite());

				if(s==SideConfig.INPUT)
				{
					if(rotation.getRotationSpeed()==0)
						rotation.setRotationSpeed(energy.getOutputRotationSpeed());
					in.add(energy);
				}
				else
					out.add(energy);
			}

			in.forEach((iRotationalEnergy ->
			{
				rotation.setTorque(rotation.getCombinedTorque(iRotationalEnergy));
				//IILogger.info(rotation.getTorque());
			}));
			if(out.size() > 1)
				rotation.setTorque(rotation.getTorque()/(float)out.size());
			float eff = RotaryUtils.getGearEffectiveness(inventory, getEfficiencyMultiplier());
			float tmod = RotaryUtils.getGearTorqueRatio(inventory);
			rotation.setRotationSpeed((rotation.getRotationSpeed()*eff)/tmod);
			rotation.setTorque(rotation.getTorque()*eff*tmod);

			IIPacketHandler.INSTANCE.sendToAllAround(new MessageRotaryPowerSync(rotation, 0, getPos()), IIPacketHandler.targetPointFromTile(this, 32));
		}
	}

	@Override
	public void updateRotationStorage(float rpm, float torque, int part)
	{
		if(world.isRemote)
			if(part==0)
			{
				rotation.setRotationSpeed(rpm);
				rotation.setTorque(torque);
			}
	}

	@Override
	public SideConfig getSideConfig(int side)
	{
		return this.sideConfig[side];
	}

	@Override
	public boolean toggleSide(int side, EntityPlayer player)
	{
		sideConfig[side] = SideConfig.next(sideConfig[side]);
		this.markDirty();
		this.markContainingBlockForUpdate(null);
		world.addBlockEvent(getPos(), this.getBlockType(), 0, 0);
		return true;
	}

	/**
	 * See {@link Block#eventReceived} for more information. This must return true serverside before it is called
	 * clientside.
	 */
	@Override
	public boolean receiveClientEvent(int id, int arg)
	{
		if(id==0)
		{
			this.markContainingBlockForUpdate(null);
			return true;
		}
		return false;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		if(!descPacket)
			nbt.setTag("inventory", Utils.writeInventory(inventory));
		for(int i = 0; i < 6; i++)
			nbt.setInteger("sideConfig_"+i, sideConfig[i].ordinal());
		nbt.setTag("rotation", rotation.toNBT());
		nbt.setFloat("efficiency", efficiency);

	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		if(!descPacket&&nbt.hasKey("inventory"))
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 3);

		if(nbt.hasKey("sideConfig"))//old NBT style
		{
			int[] old = nbt.getIntArray("sideConfig");
			for(int i = 0; i < old.length; i++)
				sideConfig[i] = SideConfig.values()[old[i]+1];
		}
		else
			for(int i = 0; i < 6; i++)
				sideConfig[i] = SideConfig.values()[nbt.getInteger("sideConfig_"+i)];
		if(nbt.hasKey("rotation"))
			rotation.fromNBT(nbt.getCompoundTag("rotation"));
		efficiency = nbt.getFloat("efficiency");
	}

	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		if(Utils.isHammer(player.getHeldItem(EnumHand.MAIN_HAND))&&IEConfig.colourblindSupport)
		{
			SideConfig i = sideConfig[Math.min(sideConfig.length-1, mop.sideHit.ordinal())];
			SideConfig j = sideConfig[Math.min(sideConfig.length-1, mop.sideHit.getOpposite().ordinal())];
			return new String[]{
					I18n.format(Lib.DESC_INFO+"blockSide.facing")
							+": "+I18n.format(Lib.DESC_INFO+"blockSide.connectEnergy."+i),
					I18n.format(Lib.DESC_INFO+"blockSide.opposite")
							+": "+I18n.format(Lib.DESC_INFO+"blockSide.connectEnergy."+j)
			};
		}
		return null;
	}

	@Override
	public int getComparatorInputOverride()
	{
		return this.comparatorOutput;
	}

	@Override
	public ItemStack getTileDrop(EntityPlayer player, IBlockState state)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
		for(int i = 0; i < 6; i++)
			ItemNBTHelper.setInt(stack, "sideConfig_"+i, sideConfig[i].ordinal());
		return stack;
	}

	@Override
	public void readOnPlacement(EntityLivingBase placer, ItemStack stack)
	{
		for(int i = 0; i < 6; i++)
			if(ItemNBTHelper.hasKey(stack, "sideConfig_"+i))
				sideConfig[i] = SideConfig.values()[ItemNBTHelper.getInt(stack, "sideConfig_"+i)];
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_GEARBOX.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return this;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return stack.getItem() instanceof IMotorGear;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 1;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{

	}

	public float getEfficiencyMultiplier()
	{
		return 1.0f;
	}
}