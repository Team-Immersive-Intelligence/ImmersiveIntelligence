package pl.pabilo8.immersiveintelligence.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.commands.dev.CommandIIDev;
import pl.pabilo8.immersiveintelligence.common.commands.faction.CommandIIFaction;
import pl.pabilo8.immersiveintelligence.common.commands.item.CommandIIGiveBullet;
import pl.pabilo8.immersiveintelligence.common.commands.item.CommandIIGiveMagazine;
import pl.pabilo8.immersiveintelligence.common.commands.item.CommandIIGivePunchtape;
import pl.pabilo8.immersiveintelligence.common.commands.reload.CommandIIReload;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Root command tree for all Immersive Intelligence commands.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 26.08.2026
 * @ii-approved 0.3.1
 * @since 23.06.2019
 */
public class CommandII extends CommandTreeBase
{
	private static final String start = "<";
	private static final String end = ">";

	public CommandII()
	{
		addSubcommand(new CommandIIFaction());
		addSubcommand(new CommandIIDev());

		addSubcommand(new CommandIIHans());

		addSubcommand(new CommandIIGiveBullet(false));
		addSubcommand(new CommandIIGiveBullet(true));
		addSubcommand(new CommandIIGiveMagazine());

		addSubcommand(new CommandIIGivePunchtape());
		addSubcommand(new CommandIIReload());

		addSubcommand(new CommandIIHelp(this, ""));
	}

	@Nonnull
	@Override
	public String getName()
	{
		return "ii";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "Use \"/ii help\" for more information";
	}

	@Nonnull
	@Override
	public List<String> getTabCompletions(@Nullable MinecraftServer server, @Nonnull ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		List<String> ret = super.getTabCompletions(server, sender, args, pos);
		for(int i = 0; i < ret.size(); i++)
		{
			String curr = ret.get(i);
			if(curr.indexOf(' ') >= 0)
				ret.set(i, start+curr+end);
		}
		return ret;
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException
	{
		List<String> argsCleaned = new ArrayList<>(args.length);
		StringBuilder currentPart = null;
		for(String s : args)
		{
			if(s.startsWith(start))
			{
				if(currentPart!=null)
					throw new CommandException("A quoted argument starts before the previous quoted argument is closed: '%s'.", s);
				currentPart = new StringBuilder(s);
			}
			else if(currentPart!=null)
				currentPart.append(" ").append(s);
			else
				argsCleaned.add(s);

			if(s.endsWith(end))
			{
				if(currentPart==null)
					throw new CommandException("A quoted argument closes without an opening '<': '%s'.", s);
				if(currentPart.length() >= 2)
					argsCleaned.add(currentPart.substring(1, currentPart.length()-1));
				currentPart = null;
			}
		}
		if(currentPart!=null)
			throw new CommandException("A quoted argument is not closed: '%s'.", currentPart);
		super.execute(server, sender, argsCleaned.toArray(new String[0]));
	}
}
