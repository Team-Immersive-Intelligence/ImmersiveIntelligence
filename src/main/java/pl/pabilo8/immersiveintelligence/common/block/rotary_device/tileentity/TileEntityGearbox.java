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
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.rotary.*;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageRotaryPowerSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

import javax.annotation.Nullable;

public class TileEntityGearbox extends TileEntityIEBase implements ITickable, IAdvancedTextOverlay, IConfigurableSides,
		IComparatorOverride, ITileDrop, IGuiTile, IIEInventory, IRotationalEnergyBlock
{
	public static final int GEAR_SLOTS = 3;

	public SideConfig[] sideConfig = {SideConfig.NONE, SideConfig.INPUT, SideConfig.NONE, SideConfig.NONE, SideConfig.NONE, SideConfig.NONE};
	@SyncNBT
	public GearboxRotaryStorage rotation = new GearboxRotaryStorage();
	@SyncNBT
	public NonNullList<ItemStack> inventory = NonNullList.withSize(GEAR_SLOTS, ItemStack.EMPTY);

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

		//Update and calculate rotary power
		if(world.getTotalWorldTime()%10==0)
		{
			rotation.setRotationSpeed(0);
			rotation.setTorque(0);
			float totalTorque = 0;
			float totalSpeed = Float.POSITIVE_INFINITY;
			int inputs = 0, outputs = 0;

			for(EnumFacing facing : EnumFacing.values())
			{
				//Get the side config
				SideConfig config = sideConfig[facing.getIndex()];
				if(config==SideConfig.NONE)
					continue;
				TileEntity tile = world.getTileEntity(pos.offset(facing));

				//Skip non-existant or non-rotary capable tiles
				if(tile==null||!(tile.hasCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, facing.getOpposite())))
					continue;

				//Get the rotary energy capability
				IRotaryEnergy energy = tile.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, facing.getOpposite());
				assert energy!=null;

				//Calculate the energy components
				if(config==SideConfig.INPUT)
				{
					totalTorque += energy.getOutputTorque();
					totalSpeed = Math.min(totalSpeed, energy.getOutputRotationSpeed());
					inputs++;
				}
				else
					outputs++;
			}

			//Prevents division by zero, while maintaining a good GUI torque display value
			if(outputs==0)
				outputs = 1;

			//Set input values
			rotation.setTorque(totalTorque/inputs);
			rotation.setRotationSpeed(totalSpeed);

			//Set output values
			float torqueRatio = IIRotaryUtils.getGearTorqueRatio(inventory);
			if(inputs==0||torqueRatio==0)
				rotation.setOutput(0, 0);
			else
			{
				//Calculate the efficiency and torque ratio
				rotation.setOutput(
						rotation.getTorque()/outputs*torqueRatio,
						(rotation.getRotationSpeed()*IIRotaryUtils.getGearEfficiency(inventory))/torqueRatio
				);
			}

			//Sync with clients
			IIPacketHandler.sendToClient(new MessageRotaryPowerSync(world, getPos(), 0, rotation));
		}
	}

	@Override
	public void updateRotationStorage(float speed, float torque, int partID)
	{
		if(world.isRemote)
			if(partID==0)
			{
				rotation.setRotationSpeed(speed);
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
		nbt.setTag("rotation", rotation.serializeNBT());

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
			rotation.deserializeNBT(nbt.getCompoundTag("rotation"));
	}

	@SideOnly(Side.CLIENT)
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
		//TODO: 19.07.2026 comparator output
		return 0;
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
		return IIGUI.GEARBOX.ordinal();
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

	public class GearboxRotaryStorage extends RotaryStorage
	{
		private float outputTorque = 0, outputSpeed = 0;

		public void setOutput(float torque, float speed)
		{
			this.outputTorque = torque;
			this.outputSpeed = speed;
		}

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

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			super.deserializeNBT(nbt);
			outputTorque = nbt.getFloat("outputTorque");
			outputSpeed = nbt.getFloat("outputSpeed");
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = super.serializeNBT();
			nbt.setFloat("outputTorque", outputTorque);
			nbt.setFloat("outputSpeed", outputSpeed);
			return nbt;
		}

		@Override
		public float getOutputTorque()
		{
			return outputTorque;
		}

		@Override
		public float getOutputRotationSpeed()
		{
			return outputSpeed;
		}
	}

}
