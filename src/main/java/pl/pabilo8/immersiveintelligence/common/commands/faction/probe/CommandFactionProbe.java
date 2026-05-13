package pl.pabilo8.immersiveintelligence.common.commands.faction.probe;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandFactionProbe extends CommandTreeBase
{
	public CommandFactionProbe(CommandTreeBase parent)
	{
		addSubcommand(new CommandFactionProbeName(this));
		addSubcommand(new CommandFactionProbeColor(this));
		addSubcommand(new CommandFactionProbeAlly(this));
		addSubcommand(new CommandFactionProbeEnemy(this));
		addSubcommand(new CommandFactionProbeOwner(this));
	}

	@Override
	public String getName()
	{
		return "probe";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Inspect faction/chunk ownership";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}
}
