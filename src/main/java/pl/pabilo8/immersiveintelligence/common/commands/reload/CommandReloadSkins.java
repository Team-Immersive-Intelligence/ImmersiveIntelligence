package pl.pabilo8.immersiveintelligence.common.commands.reload;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler.ThreadContributorSpecialsDownloader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.06.2020
 */
public class CommandReloadSkins extends CommandIIBase
{
	public CommandReloadSkins(CommandTreeBase parent)
	{
		super(parent, "skins");
	}

	@Nullable
	@Override
	public String getSyntax()
	{
		return "";
	}

	@Nullable
	@Override
	public String getDescription(ICommandSender sender)
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
