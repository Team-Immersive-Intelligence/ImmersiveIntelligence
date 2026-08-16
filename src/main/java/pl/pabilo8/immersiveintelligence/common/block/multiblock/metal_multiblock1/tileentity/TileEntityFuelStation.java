package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.VehicleFuelHandler;
import pl.pabilo8.immersiveintelligence.api.style.IStyleCustomizable;
import pl.pabilo8.immersiveintelligence.api.style.StyleCustomization;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.client.util.carversound.ConditionCompoundSound;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.FuelStation;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFuelStation;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EntityReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class TileEntityFuelStation extends TileEntityMultiblockIIGeneric<TileEntityFuelStation> implements IIIGuiMultiblockTile, IPlayerInteraction, IAdvancedTextOverlay, IStyleCustomizable
{
	@SyncNBT(events = {SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public MultiFluidTank tank = new MultiFluidTank(FuelStation.fluidCapacity);
	@SyncNBT(events = {SyncEvents.TILE_UPGRADES_MODIFIED, SyncEvents.TILE_CLIENT_MESSAGE})
	public StyleCustomization style;

	//Client only
	float inserterAngle = 0f;
	float inserterDistance = 0f;

	@SyncNBT(events = SyncEvents.TILE_CUSTOM1, nullable = true)
	public EntityReference<Entity> focusedEntity;

	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound<TileEntityFuelStation> fuellingSound;

	public TileEntityFuelStation()
	{
		super(MultiblockFuelStation.INSTANCE);

		this.style = new StyleCustomization(MultiblockFuelStation.STYLE_CONSTRAINTS);
		this.energyStorage = new FluxStorageAdvanced(FuelStation.energyCapacity);
		this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
		this.focusedEntity = new EntityReference<>(this::getWorld);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.focusedEntity = null;
		this.tank = null;
	}

	@Override
	protected void onUpdate()
	{
		if(world.isRemote)
		{
			//Update animations
			this.inserterDistance = calculateDistance(0);
			this.inserterAngle = calculateInserterAngle(0);

			//Loop fueling sound
			if(fuellingSound==null||fuellingSound.isDonePlaying())
				fuellingSound = new ConditionCompoundSound<>(IISounds.fuelStationLoop, new Vec3d(getPOIPos("table")),
						this, TileEntityFuelStation::canFuelCurrentTarget
				);
		}
		else
		{
			if(IIUtils.handleBucketTankInteraction(tank, inventory, 0, 1, false))
				updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);

			//Get AABB where clients will be served
			EnumFacing facingClients = getDirection("facing_clients");
			assert facingClients!=null;
			AxisAlignedBB clientsBox = new AxisAlignedBB(
					getBlockPosForPos(getPOI("table")[0]).offset(facingClients),
					getBlockPosForPos(getPOI("table")[1]).offset(facingClients)
			).grow(0.5, 1.5, 0.5);

			//Only handle valid vehicles
			List<Entity> entitiesWithinAABB = world.getEntitiesWithinAABB(Entity.class, clientsBox);
			entitiesWithinAABB.removeIf(entity -> !VehicleFuelHandler.isValidVehicle(entity));

			//Attempt fueling
			if(!entitiesWithinAABB.isEmpty())
			{
				//Prefer the client that's already being fueled
				Entity focused = focusedEntity.get();
				if(focused!=null&&entitiesWithinAABB.contains(focused))
				{
					IFluidHandler capability = focused.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
					if(capability!=null)
					{
						boolean canFill = false;
						//Iterate through all available fuels from the tank and check
						for(FluidStack fluid : tank.fluids)
						{
							if(!VehicleFuelHandler.isFuelValidForVehicle(focused, fluid.getFluid()))
								continue;
							FluidStack fs = new FluidStack(fluid, Math.min(fluid.amount, FuelStation.fluidTransfer));

							int i = capability.fill(fs, false);
							//Use energy, drain station's tank, fill vehicle's tank
							i = (energyStorage.extractEnergy(i*FuelStation.energyUsage, false)/FuelStation.energyUsage);
							capability.fill(new FluidStack(fs, i), true);
							tank.drain(new FluidStack(fs, i), true);
							if(i > 0)
								canFill = true;

							break;
						}

						//Stop fueling
						if(!canFill)
						{
							focusedEntity.set(null);
							updateTileForEvent(SyncEvents.TILE_CUSTOM1);
							world.playSound(null, getPos().up(), IISounds.fuelStationEnd, SoundCategory.BLOCKS, 0.5f, 1f);
						}
					}
				}
				else
				{
					//Find a new client
					for(Entity entity : entitiesWithinAABB)
					{
						IFluidHandler capability = entity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
						if(capability==null)
							break;

						Optional<FluidStack> first = tank.fluids.stream()
								.filter(fs -> VehicleFuelHandler.isFuelValidForVehicle(entity, fs.getFluid()))
								.filter(fs -> capability.fill(fs, false) > 0)
								.findFirst();

						//Find new client
						if(first.isPresent())
						{
							focusedEntity.set(entity);
							updateTileForEvent(SyncEvents.TILE_CUSTOM1);
							break;
						}
					}
				}
			}
			//No clients in reach
			else if(focusedEntity.get()!=null)
			{
				focusedEntity.set(null);
				updateTileForEvent(SyncEvents.TILE_CUSTOM1);
			}
		}

	}

	private boolean canFuelCurrentTarget()
	{
		if(tileEntityInvalid)
			return false;
		return this.focusedEntity.get()!=null;
	}

	public float calculateInserterAngle(float partialTicks)
	{
		Entity entity = focusedEntity.get();
		float yaw = facing.getOpposite().getHorizontalAngle();
		if(entity!=null)
		{
			//Subtracts two vector and calculates angle (in degrees) using atan
			Vec3d vec3d = entity.getPositionVector().subtract(new Vec3d(getBlockPosForPos(1)).addVector(0.5, 0, 0.5));

			if(vec3d.x < 0&&vec3d.z >= 0)
				yaw = (float)(Math.atan(Math.abs(vec3d.x/vec3d.z))/Math.PI*180D);
			else if(vec3d.x <= 0&&vec3d.z <= 0)
				yaw = (float)(Math.atan(Math.abs(vec3d.z/vec3d.x))/Math.PI*180D)+90;
			else if(vec3d.x >= 0&&vec3d.z < 0)
				yaw = (float)(Math.atan(Math.abs(vec3d.x/vec3d.z))/Math.PI*180D)+180;
			else
				yaw = (float)(Math.atan(Math.abs(vec3d.z/vec3d.x))/Math.PI*180D)+270;
		}
		return (IIMath.progressValue(inserterAngle, yaw, 2.5f, partialTicks)+360)%360;
	}

	public float calculateDistance(float partialTicks)
	{
		Entity entity = focusedEntity.get();
		float goalDistance = 0;
		if(entity!=null)
		{
			goalDistance = (float)entity.getPositionVector().distanceTo(new Vec3d(getBlockPosForPos(1)).addVector(0.5, 0, 0.5));
			goalDistance = 0.125f+((goalDistance/2f)*0.75f);
		}
		return IIMath.progressValue(inserterDistance, goalDistance, 0.125f, partialTicks);
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
	public void onGuiOpened(@Nullable EntityPlayer player, boolean clientside)
	{
		if(!clientside)
			updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
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

	//--- IPlayerInteraction ---//

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(isPOI(MultiblockPOI.FLUID_INPUT))
		{
			TileEntityFuelStation master = master();
			return master!=null&&FluidUtil.interactWithFluidHandler(player, hand, master.tank);
		}
		return false;
	}

	//--- IAdvancedTextOverlay ---//

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		if(!Utils.isFluidRelatedItemStack(player.getHeldItem(EnumHand.MAIN_HAND)))
			return new String[0];

		TileEntityFuelStation master = master();
		if(master!=null&&isPOI(MultiblockPOI.FLUID_INPUT))
			return new String[]{IIUtils.getFluidNameOverlayText(master.tank.getFluid())};
		return new String[0];
	}

	//--- IStyleCustomizable ---//

	@Override
	public StyleCustomization getStyle()
	{
		return style;
	}
}
