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

public class CommandFactionInvitePlayer extends CommandIIBase
{
	public CommandFactionInvitePlayer(CommandTreeBase parent)
	{
		super(parent, "player");
	}

	@Override
	public String getSyntax()
	{
		return "<player>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Invite a player to your faction";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 1)
			throw new CommandException("Specify a player name.");
		EntityPlayer target = server.getPlayerList().getPlayerByUsername(args[0]);
		if(target==null)
			throw new CommandException("Player not found.");
		EntityPlayer source = sender instanceof EntityPlayer?(EntityPlayer)sender: null;
		if(source==null)
			throw new CommandException("Only players can invite.");

		OwnerIdentity faction = DiplomacyUtils.getOwnerIdentityForEntity(source);
		if(faction.isInvalid()||faction==DiplomacyUtils.NEUTRAL)
			return;

		faction.invitePlayer(target.getUniqueID());
		sender.sendMessage(new TextComponentString("Invitation sent to "+target.getName()));
	}
}
