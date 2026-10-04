package pl.pabilo8.immersiveintelligence.common.commands.faction;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

/**
 * Displays faction command help with command suggestions.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.10.2026
 */
public class CommandFactionHelp extends CommandFactionBase
{
	public CommandFactionHelp(CommandTreeBase parent)
	{
		super(parent, "help");
	}

	@Override
	public String getSyntax()
	{
		return "";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Show faction commands";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		getPlayer(sender);
		requireArgumentCount(sender, args, 0);
		for(ICommand command : parent.getSortedCommandList())
			if(command.checkPermission(server, sender)&&command!=this)
				displayCommand(server, sender, command, "/"+parent.getName()+" ");
	}

	private void displayCommand(MinecraftServer server, ICommandSender sender, ICommand command, String prefix)
	{
		String path = prefix+command.getName();
		if(command instanceof CommandTreeBase)
		{
			for(ICommand child : ((CommandTreeBase)command).getSortedCommandList())
				if(child.checkPermission(server, sender))
					displayCommand(server, sender, child, path+" ");
			return;
		}
		CommandIIBase leaf = (CommandIIBase)command;
		TextComponentString message = new TextComponentString(path+" "+leaf.getSyntax());
		message.getStyle().setColor(TextFormatting.GOLD)
				.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, path+" "));
		message.appendSibling(new TextComponentString(" - "+leaf.getDescription(sender)));
		sender.sendMessage(message);
	}
}
