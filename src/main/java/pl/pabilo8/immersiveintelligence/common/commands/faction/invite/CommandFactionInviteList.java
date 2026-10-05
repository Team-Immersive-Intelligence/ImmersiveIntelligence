package pl.pabilo8.immersiveintelligence.common.commands.faction.invite;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.commands.faction.CommandFactionBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import java.util.List;

/**
 * List your pending faction invitations.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.10.2026
 */
public class CommandFactionInviteList extends CommandFactionBase
{
	public CommandFactionInviteList(CommandTreeBase parent)
	{
		super(parent, "list_invites");
	}

	@Override
	public String getSyntax()
	{
		return "";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "List your pending faction invitations";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		requireArgumentCount(sender, args, 0);
		List<OwnerIdentity> invitations = DiplomacyHandler.getInstance(false)
				.getPendingInvitationIdentitiesForPlayer(getPlayer(sender).getUniqueID());
		if(invitations.isEmpty())
			sender.sendMessage(new TextComponentString("No pending invitations."));
		else
			for(OwnerIdentity identity : invitations)
				sender.sendMessage(new TextComponentString("Invitation: "+identity.getDisplayName()+" ("+identity.getStringUUID()+")"));
	}
}
