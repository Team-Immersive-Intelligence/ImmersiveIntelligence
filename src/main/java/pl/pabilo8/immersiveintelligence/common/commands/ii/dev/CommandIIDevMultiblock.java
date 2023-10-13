package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 22.09.2023
 */
public class CommandIIDevMultiblock extends CommandIIBase
{
	public CommandIIDevMultiblock(CommandTreeBase parent)
	{
		super(parent, "mb");
	}

	@Override
	protected String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return null;
	}

	@Override
	public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nullable ICommandSender sender,
										  @Nonnull String[] args, @Nullable BlockPos targetPos)
	{
		return Collections.emptyList();
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nullable ICommandSender sender, @Nonnull String[] args) throws CommandException
	{

	}
}
