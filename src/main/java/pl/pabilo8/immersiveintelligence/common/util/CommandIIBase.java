package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 22.09.2023
 */
public abstract class CommandIIBase extends CommandBase
{
	protected final String name;
	protected final CommandTreeBase parent;

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
	public abstract String getSyntax();

	@Nullable
	public abstract String getDescription(ICommandSender sender);

	@Override
	public abstract void execute(@Nonnull MinecraftServer server, @Nullable ICommandSender sender, @Nonnull String[] args) throws CommandException;

	//--- Utils ---//

	protected static <E extends Enum<E> & ISerializableEnum> List<String> getTabCompletionsEnum(String[] args, Class<E> enumType)
	{
		return getTabCompletionsEnum(args, enumType, e -> true);
	}

	protected static <E extends Enum<E> & ISerializableEnum> List<String> getTabCompletionsEnum(String[] args, Class<E> enumType, Predicate<E> filter)
	{
		return getListOfStringsMatchingLastWord(args, Arrays.stream(enumType.getEnumConstants())
				.filter(filter)
				.map(E::getName)
				.toArray(String[]::new));
	}
}
