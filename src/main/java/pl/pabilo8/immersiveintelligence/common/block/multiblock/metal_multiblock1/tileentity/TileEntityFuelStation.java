package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.FuelStation;
import pl.pabilo8.immersiveintelligence.api.VehicleFuelHandler;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFuelStation;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityFuelStation extends TileEntityMultiblockMetal<TileEntityFuelStation, IMultiblockRecipe> implements IAdvancedCollisionBounds, IAdvancedSelectionBounds, IGuiTile, ISoundTile
{
	public MultiFluidTank[] tanks = {new MultiFluidTank(FuelStation.fluidCapacity)};
	public NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);

	//Client only
	float inserterAnimation = 0f;
	float inserterAngle = 0f;
	float inserterDistance = 0f;
	Entity focusedEntity = null;

	public TileEntityFuelStation()
	{
		super(MultiblockFuelStation.INSTANCE, new int[]{3, 3, 2}, FuelStation.energyCapacity, true);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		tanks[0].readFromNBT(nbt.getCompoundTag("tank"));
		if(!descPacket)
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 4);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setTag("tank", tanks[0].writeToNBT(new NBTTagCompound()));
		if(!descPacket)
			nbt.setTag("inventory", Utils.writeInventory(inventory));
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("tank"))
			tanks[0].readFromNBT(message.getCompoundTag("tank"));
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 4);
		if(message.hasKey("focused"))
			focusedEntity = world.getEntityByID(message.getInteger("focused"));
		else
			focusedEntity = null;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		super.update();
		if(isDummy())
			return;

		if(world.isRemote)
		{
			inserterAnimation = calculateInserterAnimation(0);
			inserterAngle = calculateInserterAngle(0);

			ImmersiveEngineering.proxy.handleTileSound(IISounds.fuelStationMid, this, this.inserterAnimation > 0.35&&focusedEntity!=null, 0.5f, 1);
		}
		else
		{
			boolean update = IIUtils.handleBucketTankInteraction(tanks, inventory, 0, 1, 0, false);
			if(update)
			{
				IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
						.withTag("inventory", Utils.writeInventory(inventory))
						.withTag("tank", tanks[0].writeToNBT(new NBTTagCompound()))
				));
			}
		}


		//get all in range
		//effect

		Vec3d vx = new Vec3d(facing.getOpposite().getDirectionVec()).scale(1.5f).add(new Vec3d(facing.rotateY().getDirectionVec()));
		List<Entity> entitiesWithinAABB = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(getBlockPosForPos(0)).expand(vx.x, vx.y, vx.z).expand(0, 1.5, 0).expand(0, -1.5, 0));
		entitiesWithinAABB.removeIf(entity -> !VehicleFuelHandler.isValidVehicle(entity));
		if(entitiesWithinAABB.size() > 0)
		{
			if(entitiesWithinAABB.contains(focusedEntity))
			{
				IFluidHandler capability = focusedEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
				if(capability!=null)
				{
					boolean canFill = false;
					for(FluidStack fluid : tanks[0].fluids)
					{
						if(!VehicleFuelHandler.isFuelValidForVehicle(focusedEntity, fluid.getFluid()))
							continue;
						FluidStack fs = new FluidStack(fluid, Math.min(fluid.amount, FuelStation.fluidTransfer));

						int i = capability.fill(fs, false);
						i = (energyStorage.extractEnergy(i*FuelStation.energyUsage, false)/FuelStation.energyUsage);
						capability.fill(new FluidStack(fs, i), true);
						tanks[0].drain(new FluidStack(fs, i), true);
						if(i > 0)
							canFill = true;

						break;
					}

					if(!canFill)
					{
						focusedEntity = null;
						IIPacketHandler.sendToClient(this, new MessageIITileSync(this,makeSyncEntity()));
						world.playSound(null, getPos().up(), IISounds.fuelStationEnd, SoundCategory.BLOCKS, 0.5f, 1f);
					}
				}
			}
			else
			{
				for(Entity entity : entitiesWithinAABB)
				{
					IFluidHandler capability = entity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
					if(capability==null)
						break;
					boolean canFill = false;

					for(FluidStack fluid : tanks[0].fluids)
					{
						if(!VehicleFuelHandler.isFuelValidForVehicle(entity, fluid.getFluid()))
							continue;
						FluidStack fs = new FluidStack(fluid, Math.min(fluid.amount, FuelStation.fluidTransfer));
						if(capability.fill(fs, false) > 0)
						{
							canFill = true;
							break;
						}
					}

					if(canFill)
					{
						focusedEntity = entity;
						inserterAnimation = 0f;
						IIPacketHandler.sendToClient(this, new MessageIITileSync(this,makeSyncEntity()));
						world.playSound(null, getPos().up(), IISounds.fuelStationStart, SoundCategory.BLOCKS, 0.5f, 1f);
						break;
					}
				}
			}
		}
		else if(focusedEntity!=null)
		{
			focusedEntity = null;
			inserterAnimation = 0f;
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this,makeSyncEntity()));
			world.playSound(null, getPos().up(), IISounds.fuelStationEnd, SoundCategory.BLOCKS, 0.5f, 1f);
		}
	}

	private NBTTagCompound makeSyncEntity()
	{
		NBTTagCompound tag = new NBTTagCompound();
		if(focusedEntity!=null)
			tag.setInteger("focused", focusedEntity.getEntityId());
		else
			tag.setBoolean("hasNoFocus", true);
		return tag;
	}

	public float calculateInserterAnimation(float partialTicks)
	{
		float anim;
		if(focusedEntity!=null)
			anim = Math.min(inserterAnimation+(0.05f*(1+partialTicks)), 1f);
		else
			anim = Math.max(inserterAnimation-(0.025f*(1+partialTicks)), 0f);
		return anim;
	}

	public float calculateInserterAngle(float partialTicks)
	{
		if(focusedEntity!=null)
		{
			//Subtracts two vector and calculates angle (in degrees) using atan
			Vec3d vec3d = focusedEntity.getPositionVector().subtract(new Vec3d(getBlockPosForPos(1)).addVector(0.5, 0, 0.5));
			float yaw;
			if(vec3d.x < 0&&vec3d.z >= 0)
				yaw = (float)(Math.atan(Math.abs(vec3d.x/vec3d.z))/Math.PI*180D);
			else if(vec3d.x <= 0&&vec3d.z <= 0)
				yaw = (float)(Math.atan(Math.abs(vec3d.z/vec3d.x))/Math.PI*180D)+90;
			else if(vec3d.x >= 0&&vec3d.z < 0)
				yaw = (float)(Math.atan(Math.abs(vec3d.x/vec3d.z))/Math.PI*180D)+180;
			else
				yaw = (float)(Math.atan(Math.abs(vec3d.z/vec3d.x))/Math.PI*180D)+270;

			return yaw;
		}
		return inserterAngle;
	}

	public float calculateDistance(float partialTicks)
	{
		if(focusedEntity!=null)
		{
			double v = focusedEntity.getPositionVector().distanceTo(new Vec3d(getBlockPosForPos(1)).addVector(0.5, 0, 0.5));
			inserterDistance = 0.125f+(float)((v/2f)*0.75f);
		}
		return inserterDistance;
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{6};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{};
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
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
		return 0;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 0;
	}

	@Override
	public float getMinProcessDistance(MultiblockProcess<IMultiblockRecipe> process)
	{
		return 0;
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return false;
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return tanks;
	}

	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		TileEntityFuelStation master = this.master();
		if(master!=null)
		{
			if((pos==10&&(side==facing||side==facing.rotateYCCW())))
				return new MultiFluidTank[]{master.tanks[0]};
		}
		return new MultiFluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource)
	{
		if((pos==10&&(side==facing||side==facing.rotateYCCW())))
		{
			TileEntityFuelStation master = this.master();
			return master!=null&&master.tanks[iTank].getFluidAmount() < master.tanks[iTank].getCapacity();
		}
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
		this.markDirty();
		this.markContainingBlockForUpdate(null);
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
	public List<AxisAlignedBB> getAdvancedColisionBounds()
	{
		return getAdvancedSelectionBounds();
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		ArrayList<AxisAlignedBB> list = new ArrayList<>();
		if(pos==0||pos==1)
		{
			list.add(new AxisAlignedBB(0, 0.8125-0.0625, 0, 1, 1-0.0625, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

			switch(mirrored^pos==1?facing.getOpposite(): facing)
			{
				case NORTH:
					list.add(new AxisAlignedBB(0.0625, 0, 0.0625, 0.0625+0.1875, 0.75, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.0625, 0, 0.75, 0.0625+0.1875, 0.75, 1-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case SOUTH:
					list.add(new AxisAlignedBB(0.75, 0, 0.0625, 0.9375, 0.75, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.75, 0, 0.75, 0.9375, 0.75, 1-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case EAST:
					list.add(new AxisAlignedBB(0.0625, 0, 0.0625, 0.0625+0.1875, 0.75, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.75, 0, 0.0625, 1-0.0625, 0.75, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case WEST:
					list.add(new AxisAlignedBB(0.0625, 0, 0.75, 0.0625+0.1875, 0.75, 0.9375).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.75, 0, 0.75, 1-0.0625, 0.75, 0.9375).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
			}
		}
		else if(pos==6)
		{
			list.add(new AxisAlignedBB(0.25, 0.375, 0.25, 0.75, 1, 0.75)
					.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			list.add(new AxisAlignedBB(0.25-0.125, 0.375-0.125, 0.25-0.125, 0.75+0.125, 0.375, 0.75+0.125)
					.offset(getPos().getX(), getPos().getY(), getPos().getZ()));

			list.add(new AxisAlignedBB(0.25, 0, 0.25, 0.75, 0.375-0.125, 0.75)
					.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		}
		else if(pos==7)
		{
			list.add(new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.0625, 0.875)
					.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		}
		return list;
	}

	@Override
	public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list)
	{
		return false;
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_FUEL_STATION.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public boolean shoudlPlaySound(String sound)
	{
		return true;
	}
}
