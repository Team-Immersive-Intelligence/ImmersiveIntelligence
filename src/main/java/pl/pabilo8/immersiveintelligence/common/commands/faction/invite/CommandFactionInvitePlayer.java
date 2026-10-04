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
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Invite a player to your faction.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.10.2026
 */
public class CommandFactionInvitePlayer extends CommandFactionBase
{
	public CommandFactionInvitePlayer(CommandTreeBase parent)
	{
		super(parent, "invite", PermissionCategory.INVITE_MEMBERS);
	}

	@Override
	public String getSyntax()
	{
		return "<player_name>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Invite a player to your faction";
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index==0;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		return args.length==1?getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()): Collections.emptyList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		requireArgumentCount(sender, args, 1);
		OwnerIdentity faction = getFaction(sender);
		EntityPlayer target = server.getPlayerList().getPlayerByUsername(args[0]);
		if(target==null)
			throw new CommandException("Player not found: %s", args[0]);
		if(faction.isMember(target))
			throw new CommandException("That player already belongs to your faction.");
		if(faction.isInvited(target.getUniqueID()))
			throw new CommandException("That player already has a pending invitation.");
		faction.invitePlayer(target.getUniqueID());
		DiplomacyHandler.getInstance(false).saveAndSyncIdentity(faction);
		sender.sendMessage(new TextComponentString("Invitation sent to "+target.getName()));
		target.sendMessage(new TextComponentString("You were invited to "+faction.getDisplayName()
				+". Use /factions_ii accept "+faction.getDisplayName()+" or /factions_ii reject "+faction.getDisplayName()+"."));
	}
}
