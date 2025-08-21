package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import pl.pabilo8.immersiveintelligence.api.VehicleFuelHandler;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.FuelStation;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFuelStation;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class TileEntityFuelStation extends TileEntityMultiblockIIGeneric<TileEntityFuelStation> implements IIIGuiMultiblockTile, ISoundTile
{
	@SyncNBT(events = {SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public MultiFluidTank tank = new MultiFluidTank(FuelStation.fluidCapacity);

	//Client only
	float inserterAnimation = 0f;
	float inserterAngle = 0f;
	float inserterDistance = 0f;
	Entity focusedEntity = null;

	public TileEntityFuelStation()
	{
		super(MultiblockFuelStation.INSTANCE);

		energyStorage = new FluxStorageAdvanced(FuelStation.energyCapacity);
		inventory = NonNullList.withSize(2, ItemStack.EMPTY);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.focusedEntity = null;
		this.tank = null;
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		focusedEntity = message.hasKey("focused")?world.getEntityByID(message.getInteger("focused")): null;
	}

	@Override
	protected void onUpdate()
	{
		if(world.isRemote)
		{
			inserterAnimation = calculateInserterAnimation(0);
			inserterAngle = calculateInserterAngle(0);

			ImmersiveEngineering.proxy.handleTileSound(IISounds.fuelStationMid, this, this.inserterAnimation > 0.35&&focusedEntity!=null, 0.5f, 1);
		}
		else if(IIUtils.handleBucketTankInteraction(tank, inventory, 0, 1, false))
			updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);


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
					for(FluidStack fluid : tank.fluids)
					{
						if(!VehicleFuelHandler.isFuelValidForVehicle(focusedEntity, fluid.getFluid()))
							continue;
						FluidStack fs = new FluidStack(fluid, Math.min(fluid.amount, FuelStation.fluidTransfer));

						int i = capability.fill(fs, false);
						i = (energyStorage.extractEnergy(i*FuelStation.energyUsage, false)/FuelStation.energyUsage);
						capability.fill(new FluidStack(fs, i), true);
						tank.drain(new FluidStack(fs, i), true);
						if(i > 0)
							canFill = true;

						break;
					}

					if(!canFill)
					{
						focusedEntity = null;
						IIPacketHandler.sendToClient(this, new MessageIITileSync(this, makeSyncEntity()));
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

					for(FluidStack fluid : tank.fluids)
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
						IIPacketHandler.sendToClient(this, new MessageIITileSync(this, makeSyncEntity()));
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
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, makeSyncEntity()));
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
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ENERGY_INPUT:
				return getPOI("energy_input");
			case FLUID_INPUT:
				return getPOI("fluid_input");
			case REDSTONE:
				return getPOI("redstone");
			case MISC_CONTROL_PANEL:
				return getPOI("table");
			default:
				return new int[0];
		}
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	//--- Fluid Handling ---//

	@Override
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		return new IFluidTank[]{tank};
	}

	@Override
	protected boolean isTankAvailable(int pos, int tank)
	{
		return true;
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.FUEL_STATION;
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
