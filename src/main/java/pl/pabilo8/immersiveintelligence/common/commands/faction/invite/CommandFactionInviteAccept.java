package pl.pabilo8.immersiveintelligence.common.commands.faction.invite;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

public class CommandFactionInviteAccept extends CommandIIBase
{
	public CommandFactionInviteAccept(CommandTreeBase parent)
	{
		super(parent, "accept");
	}

	@Override
	public String getSyntax()
	{
		return "<faction>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Accept a pending invitation from a faction";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(!(sender instanceof EntityPlayer))
			throw new CommandException("Player only.");
		if(args.length < 1)
			throw new CommandException("Specify faction name.");

		OwnerIdentity faction = DiplomacyUtils.getIdentityByName(String.join(" ", args));
		if(faction!=null)
		{
			DiplomacyUtils.acceptInvitation(faction, ((EntityPlayer)sender).getUniqueID());
			sender.sendMessage(new TextComponentString("Invitation accepted."));
		}
	}
}
