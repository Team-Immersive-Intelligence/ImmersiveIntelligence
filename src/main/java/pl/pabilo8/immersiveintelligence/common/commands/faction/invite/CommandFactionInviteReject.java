package pl.pabilo8.immersiveintelligence.common.commands.faction.invite;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.commands.faction.CommandFactionBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Reject a pending faction invitation.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.10.2026
 */
public class CommandFactionInviteReject extends CommandFactionBase
{
	public CommandFactionInviteReject(CommandTreeBase parent)
	{
		super(parent, "reject");
	}

	@Override
	public String getSyntax()
	{
		return "<faction_name|id>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Reject a pending faction invitation";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		if(!(sender instanceof EntityPlayer))
			return Collections.emptyList();
		return getFactionCompletions(args, 0, DiplomacyHandler.getInstance(false)
				.getPendingInvitationIdentitiesForPlayer(((EntityPlayer)sender).getUniqueID()));
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		EntityPlayer player = getPlayer(sender);
		DiplomacyHandler diplomacy = DiplomacyHandler.getInstance(false);
		OwnerIdentity identity = findFaction(args, 0, diplomacy.getPendingInvitationIdentitiesForPlayer(player.getUniqueID()));
		if(!diplomacy.denyInvitation(identity, player.getUniqueID()))
			throw new CommandException("The invitation is no longer valid.");
		sender.sendMessage(new TextComponentString("Invitation rejected from "+identity.getDisplayName()));
	}
}
