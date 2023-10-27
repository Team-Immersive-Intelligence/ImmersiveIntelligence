package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author Pabilo8
 * @since 22.09.2023
 */
public abstract class CommandIIBase extends CommandBase
{
	private final CommandTreeBase parent;
	protected final String name;

	public CommandIIBase(CommandTreeBase parent, String name)
	{
		this.parent = parent;
		this.name = name;
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public final String getUsage(@Nullable ICommandSender sender)
	{
		return String.format("%s %s %s",
				parent.getName(),
				name,
				Optional.ofNullable(getSyntax()).orElse("")
		).trim();
	}

	@Nullable
	protected abstract String getSyntax();

	@Nullable
	public abstract String getDescription(ICommandSender sender);

	@Override
	public abstract void execute(@Nonnull MinecraftServer server, @Nullable ICommandSender sender, @Nonnull String[] args) throws CommandException;
}
