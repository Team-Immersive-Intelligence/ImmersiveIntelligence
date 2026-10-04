package pl.pabilo8.immersiveintelligence.common.commands.faction;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Factions;
import pl.pabilo8.immersiveintelligence.common.commands.faction.invite.CommandFactionInviteAccept;
import pl.pabilo8.immersiveintelligence.common.commands.faction.invite.CommandFactionInviteList;
import pl.pabilo8.immersiveintelligence.common.commands.faction.invite.CommandFactionInvitePlayer;
import pl.pabilo8.immersiveintelligence.common.commands.faction.invite.CommandFactionInviteReject;
import pl.pabilo8.immersiveintelligence.common.commands.faction.probe.CommandFactionProbe;
import pl.pabilo8.immersiveintelligence.common.commands.faction.set.CommandFactionSet;

/**
 * Provides the root command for player faction management.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since II 0.3.1
 * @updated 04.10.2026
 */
public class CommandIIFaction extends CommandTreeBase
{
	public CommandIIFaction()
	{
		addSubcommand(new CommandFactionProbe(this));
		addSubcommand(new CommandFactionSet(this));
		addSubcommand(new CommandFactionInvitePlayer(this));
		addSubcommand(new CommandFactionInviteAccept(this));
		addSubcommand(new CommandFactionInviteReject(this));
		addSubcommand(new CommandFactionInviteList(this));
		addSubcommand(new CommandFactionRelation(this));
		addSubcommand(new CommandFactionHelp(this));
	}

	@Override
	public String getName()
	{
		return "factions_ii";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Use /factions_ii help for faction commands.";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return Factions.enableFactions&&sender instanceof EntityPlayer;
	}
}
