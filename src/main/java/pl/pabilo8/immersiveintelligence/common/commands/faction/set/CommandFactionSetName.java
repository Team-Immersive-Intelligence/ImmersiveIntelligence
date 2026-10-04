package pl.pabilo8.immersiveintelligence.common.commands.faction.set;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.commands.faction.CommandFactionBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;

/**
 * Rename your faction.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.10.2026
 */
public class CommandFactionSetName extends CommandFactionBase
{
	public CommandFactionSetName(CommandTreeBase parent)
	{
		super(parent, "name", PermissionCategory.MODIFY_INSIGNIA);
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
		OwnerIdentity faction = getFaction(sender);
		if(args.length==0)
			throw new CommandException("Specify a new name.");
		String name = String.join(" ", args).trim();
		if(name.isEmpty())
			throw new CommandException("The faction name cannot be empty.");
		for(OwnerIdentity other : DiplomacyHandler.getInstance(false).getActivePlayerIdentities())
			if(!other.equals(faction)&&other.getDisplayName().equalsIgnoreCase(name))
				throw new CommandException("That faction name is already in use.");
		faction.withDisplayName(name);
		DiplomacyHandler.getInstance(false).saveAndSyncIdentity(faction);
		sender.sendMessage(new TextComponentString("Faction renamed to "+name));
	}
}
