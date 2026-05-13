package pl.pabilo8.immersiveintelligence.common.commands.faction.invite;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandFactionInvite extends CommandTreeBase
{
	public CommandFactionInvite(CommandTreeBase parent)
	{
		addSubcommand(new CommandFactionInvitePlayer(this));
		addSubcommand(new CommandFactionInviteList(this));
		addSubcommand(new CommandFactionInviteAccept(this));
		addSubcommand(new CommandFactionInviteReject(this));
	}

	@Override
	public String getName()
	{
		return "invite";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Manage invitations: invite player, list, accept, reject";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}
}
