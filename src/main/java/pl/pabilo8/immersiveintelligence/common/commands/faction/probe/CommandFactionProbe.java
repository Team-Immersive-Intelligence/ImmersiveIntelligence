package pl.pabilo8.immersiveintelligence.common.commands.faction.probe;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Provides commands to inspect factions and chunk ownership.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.10.2026
 */
public class CommandFactionProbe extends CommandTreeBase
{
	public CommandFactionProbe(CommandTreeBase parent)
	{
		addSubcommand(new CommandFactionProbeName(this));
		addSubcommand(new CommandFactionProbeColor(this));
		addSubcommand(new CommandFactionProbeAlly(this));
		addSubcommand(new CommandFactionProbeEnemy(this));
		addSubcommand(new CommandFactionProbeId(this));
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
