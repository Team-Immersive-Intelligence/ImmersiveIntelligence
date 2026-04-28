package pl.pabilo8.immersiveintelligence.common.commands.faction.set;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

public class CommandFactionSetName extends CommandIIBase
{
	public CommandFactionSetName(CommandTreeBase parent)
	{
		super(parent, "name");
	}

	@Override
	public String getSyntax()
	{
		return "<new name>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Rename your faction";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(!(sender instanceof EntityPlayer))
			throw new CommandException("Player only.");

		if(args.length < 1)
			throw new CommandException("Specify a new name.");

		OwnerIdentity faction = DiplomacyUtils.getOwnerIdentityForEntity((EntityPlayer)sender);
		if(faction.isInvalid()||!faction.isOwner(((EntityPlayer)sender).getUniqueID()))
			throw new CommandException("You must be an owner to rename the faction.");

		faction.withDisplayName(String.join(" ", args));
		DiplomacyUtils.saveAndSyncIdentity(faction);
		sender.sendMessage(new TextComponentString("Faction renamed to "+args[0]));
	}
}
