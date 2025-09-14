package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevHelp extends CommandIIBase
{
	public CommandDevHelp(CommandTreeBase parent)
	{
		super(parent, "help");
	}

	@Override
	public String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Shows dev subcommands help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(sender==null)
			throw new CommandException("Sender cannot be null!");

		sender.sendMessage(new TextComponentString("Executes an Immersive Intelligence command, usage /ii dev <option>").setStyle(new Style().setColor(TextFormatting.GOLD)));
		for(ICommand subCommand : parent.getSubCommands())
			if(subCommand instanceof CommandIIBase)
			{
				CommandIIBase command = (CommandIIBase)subCommand;
				sender.sendMessage(getMessageForCommand(command.getName(), command.getDescription(sender), command.getSyntax()));
			}
	}

	private ITextComponent getMessageForCommand(String subcommand, String description)
	{
		return getMessageForCommand(subcommand, description, "");
	}

	private ITextComponent getMessageForCommand(String subcommand, String description, String arguments)
	{
		return new TextComponentString("/ii dev ").appendText(subcommand).appendText((arguments==null||arguments.isEmpty())?"": " "+arguments)
				.setStyle(new Style().setColor(TextFormatting.GOLD).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/ii dev "+subcommand)))
				.appendSibling(new TextComponentString(" - ").appendText(description).setStyle(new Style().setColor(TextFormatting.RESET)));
	}
}
