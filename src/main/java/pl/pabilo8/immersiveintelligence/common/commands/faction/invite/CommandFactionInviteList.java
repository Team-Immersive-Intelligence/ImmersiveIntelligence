package pl.pabilo8.immersiveintelligence.common.commands.faction.invite;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;

import java.util.Set;
import java.util.UUID;

public class CommandFactionInviteList extends CommandIIBase
{
	public CommandFactionInviteList(CommandTreeBase parent)
	{
		super(parent, "list");
	}

	@Override
	public String getSyntax()
	{
		return "";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "List all pending invitations you have received";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(!(sender instanceof EntityPlayer)) throw new CommandException("Player only.");
		UUID uuid = ((EntityPlayer)sender).getUniqueID();
		DiplomacyHandler diplomacy = DiplomacyHandler.getInstance(false);
		Set<String> invites = diplomacy.getPendingInvitationsForPlayer(uuid);
		if(invites.isEmpty())
			sender.sendMessage(new TextComponentString("No pending invitations."));
		else
			sender.sendMessage(new TextComponentString("Pending invitations: "+String.join(", ", invites)));
	}
}
