package pl.pabilo8.immersiveintelligence.common.commands.reload;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 23-06-2020
 */
public class CommandReloadParticles extends CommandBase
{
	/**
	 * Gets the name of the command
	 */
	@Nonnull
	@Override
	public String getName()
	{
		return "relpart";
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "Reload all registered II Particles";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
	{
		ParticleRegistry.cleanBuilderRegistry();
		IIParticles.preInit();
		IIParticles.init();
		IIModelRegistry.INSTANCE.getReloadableModels().stream()
				.filter(res -> res.getResourcePath().startsWith("particle/"))
				.forEach(IIModelRegistry.INSTANCE::reloadModel);
		sender.sendMessage(new TextComponentString("Succesfully reloaded all II particles"));
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
		return Collections.emptyList();
	}
}
