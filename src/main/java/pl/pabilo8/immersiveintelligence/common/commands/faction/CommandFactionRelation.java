package pl.pabilo8.immersiveintelligence.common.commands.faction;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomaticStatus;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

/**
 * Changes the acting faction relation towards another faction.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.10.2026
 */
public class CommandFactionRelation extends CommandFactionBase
{
	public CommandFactionRelation(CommandTreeBase parent)
	{
		super(parent, "relation", PermissionCategory.FOREIGN_AFFAIRS);
	}

	@Override
	public String getSyntax()
	{
		return "<allied|neutral|enemy> <faction_name|id>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Set your faction relation towards another faction";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		if(args.length==1)
			return getListOfStringsMatchingLastWord(args, "allied", "neutral", "enemy");
		return getFactionCompletions(args, 1, DiplomacyHandler.getInstance(false).getActivePlayerIdentities());
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 2)
			throw new WrongUsageException(getUsage(sender));
		OwnerIdentity faction = getFaction(sender);
		DiplomaticStatus status;
		try
		{
			status = DiplomaticStatus.valueOf(args[0].toUpperCase(Locale.ROOT));
		} catch(IllegalArgumentException exception)
		{
			throw new CommandException("Use allied, neutral, or enemy.");
		}
		if(status==DiplomaticStatus.MEMBER)
			throw new CommandException("Use allied, neutral, or enemy.");
		OwnerIdentity other = findFaction(args, 1);
		if(faction.equals(other))
			throw new CommandException("You cannot change your faction relation towards itself.");
		faction.setRelation(other, status);
		DiplomacyHandler.getInstance(false).saveAndSyncIdentity(faction);
		sender.sendMessage(new TextComponentString("Relation towards "+other.getDisplayName()+" set to "+status.getName()));
	}
}
