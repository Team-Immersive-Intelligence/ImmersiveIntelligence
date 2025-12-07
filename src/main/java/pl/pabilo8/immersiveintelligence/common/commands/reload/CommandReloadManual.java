package pl.pabilo8.immersiveintelligence.common.commands.reload;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.06.2020
 */
public class CommandReloadManual extends CommandIIBase
{
	public CommandReloadManual(CommandTreeBase parent)
	{
		super(parent, "manual");
	}

	@Nullable
	@Override
	public String getSyntax()
	{
		return "[resource_location]";
	}

	@Nullable
	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Reload one or all Immersive Intelligence manual pages.";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
	{
		ImmersiveIntelligence.proxy.reloadManual();
		sender.sendMessage(new TextComponentString(("Succesfully reloaded all II manual pages")));
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		return getListOfStringsMatchingLastWord(args, IIModelRegistry.INSTANCE.getReloadableModels());
	}
}
