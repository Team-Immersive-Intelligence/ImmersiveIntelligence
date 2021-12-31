package pl.pabilo8.immersiveintelligence.common.compat;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorBelt;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConveyorBelt;
import flaxbeard.immersivepetroleum.api.energy.FuelHandler;
import flaxbeard.immersivepetroleum.api.event.SchematicPlaceBlockPostEvent;
import flaxbeard.immersivepetroleum.common.entity.EntitySpeedboat;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.VehicleFuelHandler;
import pl.pabilo8.immersiveintelligence.common.blocks.MultiblockStuctureBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.HashMap;

public class ImmersivePetroleumHelper extends IICompatModule
{
	public static final ResourceLocation CAPABILITY_RES = new ResourceLocation(ImmersiveIntelligence.MODID, "fluid_handler");

	@Override
	public void preInit()
	{

	}

	@Override
	public void registerRecipes()
	{

	}

	@Override
	public void init()
	{
		//for compat that requires events
		MinecraftForge.EVENT_BUS.register(this);
		Config.manual_bool.put("petroleumHere", true);
	}

	@Override
	public void postInit()
	{
		try
		{
			Field motorboatAmountTick1 = FuelHandler.class.getDeclaredField("motorboatAmountTick");
			motorboatAmountTick1.setAccessible(true);
			Object motorboatAmountTick = motorboatAmountTick1.get(null);
			HashMap<String, Integer> map = (HashMap<String, Integer>)motorboatAmountTick;
			Fluid[] a = map.keySet().stream().map(FluidRegistry::getFluid).toArray(Fluid[]::new);
			motorboatAmountTick1.setAccessible(false);
			VehicleFuelHandler.addVehicle(EntitySpeedboat.class, a);
		}
		catch(NoSuchFieldException|IllegalAccessException ignored)
		{
			ImmersiveIntelligence.logger.info("Failed to add IP Motorboat fuel station compat!");
		}

	}

	@SubscribeEvent
	public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof EntitySpeedboat)
		{
			if(!event.getCapabilities().containsKey(CAPABILITY_RES))
				event.addCapability(CAPABILITY_RES, new SidedFluidHandler((EntitySpeedboat)event.getObject()));
		}
	}

	@SubscribeEvent
	public void handleConveyorPlace(SchematicPlaceBlockPostEvent event)
	{
		IMultiblock mb = event.getMultiblock();
		if(mb.getUniqueName().startsWith("II:"))
		{
			IBlockState state = event.getBlockState();
			TileEntity te = event.getWorld().getTileEntity(event.getPos());

			if(te instanceof TileEntityConveyorBelt)
			{
				TileEntityConveyorBelt conveyor = (TileEntityConveyorBelt)te;
				EnumFacing facing = null;
				ResourceLocation rl = null;

				if(mb instanceof MultiblockStuctureBase)
				{
					Tuple<ResourceLocation, EnumFacing> key = ((MultiblockStuctureBase<?>)mb).getConveyorKey(event.getH(), event.getL(), event.getW(), event.getRotate());
					rl = key.getFirst();
					facing = key.getSecond();
				}
				else
					switch(mb.getUniqueName())
					{
						case "II:ConveyorScanner":
							rl = new ResourceLocation(ImmersiveEngineering.MODID+":covered");
							break;
						case "II:Packer":
							if(event.getH()==0)
								rl = new ResourceLocation(ImmersiveEngineering.MODID+":covered");
							else
							{
								rl = new ResourceLocation(ImmersiveEngineering.MODID+":conveyor");
								facing = event.getRotate().rotateYCCW();
							}
							break;
						default:

							break;
					}

				if(rl==null)
					rl = new ResourceLocation("immersiveengineering", "conveyor");
				if(facing==null)
					facing = event.getRotate();

				IConveyorBelt subType = ConveyorHandler.getConveyor(rl, conveyor);
				conveyor.setConveyorSubtype(subType);
				conveyor.setFacing(facing);

			}
		}

	}

	//Yes, naybe it's hacky
	//But that's how capabilities work :)
	static class SidedFluidHandler implements IFluidHandler, ICapabilityProvider
	{
		EntitySpeedboat boat;

		SidedFluidHandler(EntitySpeedboat boat)
		{
			this.boat = boat;
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			if(resource==null)
				return 0;

			FluidTank tank = getTank();
			int i = tank.fill(resource, doFill);
			if(i > 0)
			{
				boat.setContainedFluid(tank.getFluid());
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
			FluidTank tank = getTank();
			FluidStack f = tank.drain(maxDrain, doDrain);
			if(f!=null&&f.amount > 0)
			{
				boat.setContainedFluid(tank.getFluid());
			}
			return f;
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			return new FluidTankProperties[]{new FluidTankProperties(boat.getContainedFluid(), boat.getMaxFuel(), true, true)};
		}

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing enumFacing)
		{
			return capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
		}

		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing enumFacing)
		{
			return capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY?(T)this: null;
		}

		public FluidTank getTank()
		{
			return new FluidTank(boat.getContainedFluid(), boat.getMaxFuel());
		}
	}
}