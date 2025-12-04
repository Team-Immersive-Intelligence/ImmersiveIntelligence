package pl.pabilo8.immersiveintelligence.common.commands.reload;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.06.2020
 */
public class CommandReloadVehicles extends CommandBase
{
	/**
	 * Gets the name of the command
	 */
	@Nonnull
	@Override
	public String getName()
	{
		return "relveh";
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "Reload all registered II Vehicles";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
	{
		World world = server.getEntityWorld();
		List<Class<EntityVehicleBase<?>>> reloadedEntities = new ArrayList<>();
		List<ResourceLocation> resourceLocations = new ArrayList<>();
		for(String arg : args)
		{
			//Try to get entity by the given ResourceLocation
			ResourceLocation rl = new ResourceLocation(arg);
			EntityEntry entry = ForgeRegistries.ENTITIES.getValue(rl);
			//Add it to reload list if it's a vehicle
			if(entry!=null&&EntityVehicleBase.class.isAssignableFrom(entry.getEntityClass()))
			{
				//noinspection unchecked
				reloadedEntities.add((Class<EntityVehicleBase<?>>)entry.getEntityClass());
				resourceLocations.add(rl);
			}
		}

		boolean success;
		//Reload all vehicles
		if(reloadedEntities.isEmpty())
		{
			success = !world.getEntities(EntityVehicleBase.class, EntityVehicleBase::reloadEntity).isEmpty();
			sender.sendMessage(new TextComponentString((success?"Succesfully reloaded ": "Couldn't reload ")+"all vehicles"));
		}
		else
		{
			//Reload only specified vehicles
			success = false;
			for(Class<EntityVehicleBase<?>> clazz : reloadedEntities)
				success = !world.getEntities(clazz, EntityVehicleBase::reloadEntity).isEmpty()||success;
			sender.sendMessage(new TextComponentString((success?"Succesfully reloaded ": "Couldn't reload ")+
					resourceLocations.stream().map(ResourceLocation::toString).collect(Collectors.joining(", "))));
		}
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		//Get all entities extending EntityVehicleBase
		List<String> vehicleLocations = ForgeRegistries.ENTITIES.getValuesCollection()
				.stream()
				.filter(e -> EntityVehicleBase.class.isAssignableFrom(e.getEntityClass()))
				.map(Impl::getRegistryName)
				.filter(Objects::nonNull)
				.map(ResourceLocation::toString)
				.collect(Collectors.toList());
		return getListOfStringsMatchingLastWord(args, vehicleLocations);
	}
}
