package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.client.util.carversound.ConditionCompoundSound;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.FuelStation;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFuelStation;
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
public class TileEntityFuelStation extends TileEntityMultiblockIIGeneric<TileEntityFuelStation> implements IIIGuiMultiblockTile, IPlayerInteraction, IAdvancedTextOverlay
{
	@SyncNBT(events = {SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public MultiFluidTank tank = new MultiFluidTank(FuelStation.fluidCapacity);

	//Client only
	float inserterAnimation = 0f;
	float inserterAngle = 0f;
	float inserterDistance = 0f;

	@SyncNBT(events = SyncEvents.TILE_RECIPE_CHANGED)
	public EntityReference<Entity> focusedEntity;

	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound<TileEntityFuelStation> fuellingSound;

	public TileEntityFuelStation()
	{
		super(MultiblockFuelStation.INSTANCE);

		this.energyStorage = new FluxStorageAdvanced(FuelStation.energyCapacity);
		this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
		this.focusedEntity = new EntityReference<>(this.world);
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
			inserterAnimation = calculateInserterAnimation(0);
			inserterAngle = calculateInserterAngle(0);

			if(fuellingSound==null)
				fuellingSound = new ConditionCompoundSound<>(IISounds.fuelStationLoop, new Vec3d(getPOIPos("table")),
						this, TileEntityFuelStation::canFuelCurrentTarget
				);
		}
		else if(IIUtils.handleBucketTankInteraction(tank, inventory, 0, 1, false))
			updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);


		//get all in range
		//effect

		Vec3d vx = new Vec3d(facing.getOpposite().getDirectionVec()).scale(1.5f).add(new Vec3d(facing.rotateY().getDirectionVec()));
		List<Entity> entitiesWithinAABB = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(getBlockPosForPos(0)).expand(vx.x, vx.y, vx.z).expand(0, 1.5, 0).expand(0, -1.5, 0));
		entitiesWithinAABB.removeIf(entity -> !VehicleFuelHandler.isValidVehicle(entity));
		if(!entitiesWithinAABB.isEmpty())
		{
			Entity focused = focusedEntity.get();
			if(focused!=null&&entitiesWithinAABB.contains(focused))
			{
				IFluidHandler capability = focused.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
				if(capability!=null)
				{
					boolean canFill = false;
					for(FluidStack fluid : tank.fluids)
					{
						if(!VehicleFuelHandler.isFuelValidForVehicle(focused, fluid.getFluid()))
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
						focusedEntity.set(null);
						updateTileForEvent(SyncEvents.TILE_CUSTOM1);
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

					Optional<FluidStack> first = tank.fluids.stream()
							.filter(fs -> VehicleFuelHandler.isFuelValidForVehicle(entity, fs.getFluid()))
							.filter(fs -> capability.fill(fs, false) > 0)
							.findFirst();

					if(first.isPresent())
					{
						focusedEntity.set(entity);
						inserterAnimation = 0f;
						updateTileForEvent(SyncEvents.TILE_CUSTOM1);
						break;
					}
				}
			}
		}
		else if(focusedEntity.get()!=null)
		{
			focusedEntity.set(null);
			inserterAnimation = 0f;
			updateTileForEvent(SyncEvents.TILE_CUSTOM1);
		}
	}

	private boolean canFuelCurrentTarget()
	{
		if(tileEntityInvalid)
			return false;
		Entity entity = focusedEntity.get();
		return entity!=null&&tank.fluids.stream()
				.anyMatch(fluidStack -> VehicleFuelHandler.isFuelValidForVehicle(entity, fluidStack.getFluid()));
	}

	public float calculateInserterAnimation(float partialTicks)
	{
		float anim;
		if(focusedEntity.get()!=null)
			anim = Math.min(inserterAnimation+(0.05f*(1+partialTicks)), 1f);
		else
			anim = Math.max(inserterAnimation-(0.025f*(1+partialTicks)), 0f);
		return anim;
	}

	public float calculateInserterAngle(float partialTicks)
	{
		Entity entity = focusedEntity.get();
		if(entity!=null)
		{
			//Subtracts two vector and calculates angle (in degrees) using atan
			Vec3d vec3d = entity.getPositionVector().subtract(new Vec3d(getBlockPosForPos(1)).addVector(0.5, 0, 0.5));
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
		Entity entity = focusedEntity.get();
		if(entity!=null)
		{
			double v = entity.getPositionVector().distanceTo(new Vec3d(getBlockPosForPos(1)).addVector(0.5, 0, 0.5));
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
}
