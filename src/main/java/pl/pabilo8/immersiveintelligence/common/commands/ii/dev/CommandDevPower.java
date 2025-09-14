package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IAdvancedFluidItem;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevPower extends CommandIIBase
{
	public CommandDevPower(CommandTreeBase parent)
	{
		super(parent, "power");
	}

	@Override
	public String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Charges held item or looked entity with IF, absolutely free";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity senderEntity = sender.getCommandSenderEntity();
		if(senderEntity==null) return;
		float blockReachDistance = 6;
		net.minecraft.util.math.Vec3d vec3d = senderEntity.getPositionEyes(0);
		net.minecraft.util.math.Vec3d vec3d1 = senderEntity.getLook(0);
		net.minecraft.util.math.Vec3d vec3d2 = vec3d.addVector(vec3d1.x*blockReachDistance, vec3d1.y*blockReachDistance, vec3d1.z*blockReachDistance);
		pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer rayTracer = pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer.volumetricTrace(sender.getEntityWorld(), vec3d, vec3d2, new net.minecraft.util.math.AxisAlignedBB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5), java.util.Collections.emptyList(), true, java.util.Collections.singletonList(senderEntity), null);
		if(!rayTracer.getHits().isEmpty())
		{
			Entity entityHit = rayTracer.getHits().get(0).entityHit;
			if(entityHit!=null&&entityHit.hasCapability(CapabilityEnergy.ENERGY, null))
			{
				IEnergyStorage capability = entityHit.getCapability(CapabilityEnergy.ENERGY, null);
				if(capability!=null)
				{
					while(capability.getEnergyStored()!=capability.getMaxEnergyStored())
						capability.receiveEnergy(Integer.MAX_VALUE, false);
					return;
				}
			}
		}
		senderEntity.getHeldEquipment().forEach(stack -> {
			if(stack.getItem() instanceof IAdvancedFluidItem)
			{
				IAdvancedFluidItem item = (IAdvancedFluidItem)stack.getItem();
				for(Fluid f : FluidRegistry.getRegisteredFluids().values())
				{
					FluidStack fluidStack = new FluidStack(f, 10000000);
					if(item.allowFluid(stack, fluidStack))
					{
						IFluidHandler capability = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
						if(capability!=null)
						{
							capability.fill(fluidStack, true);
							break;
						}
					}
				}
			}
			if(stack.hasCapability(CapabilityEnergy.ENERGY, null))
				stack.getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(Integer.MAX_VALUE, false);
		});
	}
}
