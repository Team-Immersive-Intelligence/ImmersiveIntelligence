package pl.pabilo8.immersiveintelligence.common.commands.faction.set;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.commands.faction.CommandFactionBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.LawForm;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Change your faction law form.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.10.2026
 */
public class CommandFactionSetLawForm extends CommandFactionBase
{
	public CommandFactionSetLawForm(CommandTreeBase parent)
	{
		super(parent, "law_form", PermissionCategory.MODIFY_INSIGNIA);
	}

	@Override
	public String getSyntax()
	{
		return "<law_form>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Change your faction law form";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		return args.length==1?getTabCompletionsEnum(args, LawForm.class): Collections.emptyList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		requireArgumentCount(sender, args, 1);
		OwnerIdentity faction = getFaction(sender);
		LawForm lawForm;
		try
		{
			lawForm = LawForm.valueOf(args[0].toUpperCase(Locale.ROOT));
		} catch(IllegalArgumentException exception)
		{
			throw new CommandException("Invalid law form: %s", args[0]);
		}
		faction.withLawForm(lawForm);
		DiplomacyHandler.getInstance(false).saveAndSyncIdentity(faction);
		sender.sendMessage(new TextComponentString("Faction law form set to "+lawForm.getName()));
	}
}
