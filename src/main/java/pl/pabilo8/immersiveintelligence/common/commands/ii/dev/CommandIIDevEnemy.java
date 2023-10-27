package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 22.09.2023
 */
public class CommandIIDevEnemy extends CommandIIBase
{
	private final List<String> enemies = Arrays.asList("zombie", "skeleton", "wither_skeleton", "pigman");
	private final List<String> armor = Arrays.asList("helmet_only", "light_engineer", "player_worn");

	public CommandIIDevEnemy(CommandTreeBase parent)
	{
		super(parent, "enemy");
	}

	@Override
	protected String getSyntax()
	{
		return "<amount> [type] [armor]";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Spawns multiple test enemies";
	}

	@Override
	public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nullable ICommandSender sender,
										  @Nonnull String[] args, @Nullable BlockPos targetPos)
	{
		switch(args.length)
		{
			case 0:
				return Collections.singletonList("5");
			case 1:
				return enemies;
			case 2:
				return armor;
			default:
				return Collections.emptyList();
		}
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nullable ICommandSender sender, @Nonnull String[] args) throws CommandException
	{

	}
}
