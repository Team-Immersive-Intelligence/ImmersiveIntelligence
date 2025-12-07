package pl.pabilo8.immersiveintelligence.common.commands.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevWorldSetup extends CommandIIBase
{
	public CommandDevWorldSetup(CommandTreeBase parent)
	{
		super(parent, "world_setup");
	}

	@Override
	public String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Disables day and night and weather cycles, disables mob spawning";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		server.getEntityWorld().getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
		server.getEntityWorld().getGameRules().setOrCreateGameRule("doWeatherCycle", "false");
		server.getEntityWorld().getGameRules().setOrCreateGameRule("doMobSpawning", "false");
		if(server.getEntityWorld().getScoreboard().getTeam("GlobalEnemy")==null)
		{
			ScorePlayerTeam globalEnemy = server.getEntityWorld().getScoreboard().createTeam("GlobalEnemy");
			globalEnemy.setColor(TextFormatting.DARK_GRAY);
			globalEnemy.setDisplayName("Hostile Forces");
			globalEnemy.setPrefix("Enemy");
		}
		sender.sendMessage(new TextComponentString("World setup done!"));
	}
}
