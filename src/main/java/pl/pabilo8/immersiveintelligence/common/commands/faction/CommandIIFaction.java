package pl.pabilo8.immersiveintelligence.common.commands.faction;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.commands.CommandIIHelp;
import pl.pabilo8.immersiveintelligence.common.commands.faction.invite.CommandFactionInvite;
import pl.pabilo8.immersiveintelligence.common.commands.faction.probe.CommandFactionProbe;
import pl.pabilo8.immersiveintelligence.common.commands.faction.set.CommandFactionSet;

/**
 * Root command for faction management.
 *
 * @author Pabilo8
 * @since II 0.3.1
 */
public class CommandIIFaction extends CommandTreeBase
{
	public CommandIIFaction()
	{
		addSubcommand(new CommandFactionInvite(this));
		addSubcommand(new CommandFactionSet(this));
		addSubcommand(new CommandFactionProbe(this));
		addSubcommand(new CommandIIHelp(this, "faction"));
	}

	@Override
	public String getName()
	{
		return "faction";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Faction management. Use /ii faction help for subcommands.";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0; // any player can use
	}
}
