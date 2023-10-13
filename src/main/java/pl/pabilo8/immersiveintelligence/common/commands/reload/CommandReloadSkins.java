package pl.pabilo8.immersiveintelligence.common.commands.reload;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler.ThreadContributorSpecialsDownloader;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 23-06-2020
 */
public class CommandReloadSkins extends CommandBase
{
	/**
	 * Gets the name of the command
	 */
	@Nonnull
	@Override
	public String getName()
	{
		return "relskin";
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "Reload all Immersive Intelligence contributor skins (from GitHub)";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
	{
		new ThreadContributorSpecialsDownloader();
		sender.sendMessage(new TextComponentString("Reloading all contributor skins..."));
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}
}
