package pl.pabilo8.immersiveintelligence.common.commands.faction.probe;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomaticStatus;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

public class CommandFactionProbeAlly extends CommandIIBase
{
	public CommandFactionProbeAlly(CommandTreeBase parent)
	{
		super(parent, "ally");
	}

	@Override
	public String getSyntax()
	{
		return "<faction>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Check if the given faction is allied with yours";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(!(sender instanceof EntityPlayer))
			throw new CommandException("Player only.");
		if(args.length < 1) throw
				new CommandException("Specify a faction name.");

		OwnerIdentity myFaction = DiplomacyUtils.getOwnerIdentityForEntity((EntityPlayer)sender);
		OwnerIdentity other = DiplomacyUtils.getIdentityByName(args[0]);

		boolean allied = myFaction.getRelationTowards(other).atLeast(DiplomaticStatus.ALLIED);
		sender.sendMessage(new TextComponentString(allied?"Allied.": "Not allied."));
	}
}
