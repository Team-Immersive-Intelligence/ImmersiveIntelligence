package pl.pabilo8.immersiveintelligence.common.commands.faction.set;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandFactionSet extends CommandTreeBase
{
	public CommandFactionSet(CommandTreeBase parent)
	{
		addSubcommand(new CommandFactionSetName(this));
		addSubcommand(new CommandFactionSetColor(this));
		addSubcommand(new CommandFactionSetBanner(this));
		addSubcommand(new CommandFactionSetLawForm(this));
	}

	@Override
	public String getName()
	{
		return "set";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Change faction properties: name, color, banner";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}
}
