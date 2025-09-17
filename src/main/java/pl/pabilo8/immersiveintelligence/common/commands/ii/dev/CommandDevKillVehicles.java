package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevKillVehicles extends CommandIIBase
{
	public CommandDevKillVehicles(CommandTreeBase parent)
	{
		super(parent, "killvehicles");
	}

	@Override
	public String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Removes all vehicles (II multipart entities) in 20 block radius";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		sender.sendMessage(new TextComponentString("Vehicles Killed!"));
	}
}

