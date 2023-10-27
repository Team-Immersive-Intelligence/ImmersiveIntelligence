package pl.pabilo8.immersiveintelligence.common.commands.reload;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8
 * @since 23-06-2020
 */
public class CommandReloadModels extends CommandBase
{
	/**
	 * Gets the name of the command
	 */
	@Nonnull
	@Override
	public String getName()
	{
		return "relmod";
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "Reload all registered Turbo Model Thingy models from Immersive Intelligence";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
	{
		boolean success = false;
		if(args.length > 0)
		{
			for(String arg : args)
				success = IIModelRegistry.instance.reloadModel(new ResourceLocation(arg))||success;
		}
		else
		{
			ImmersiveIntelligence.proxy.reloadModels();
			success = true;
		}
		sender.sendMessage(new TextComponentString((success?"Succesfully reloaded ": "Couldn't reload ")+(args.length==1?args[0]: "all models")));
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
		return getListOfStringsMatchingLastWord(args, IIModelRegistry.instance.getReloadableModels());
	}
}
