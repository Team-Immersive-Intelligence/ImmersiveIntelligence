package pl.pabilo8.immersiveintelligence.common.commands.reload;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticles;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.06.2020
 */
public class CommandReloadParticles extends CommandIIBase
{
	public CommandReloadParticles(CommandTreeBase parent)
	{
		super(parent, "particle");
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
		return "Reload one or all registered II Particles";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
	{
		//Reload the particle system
		ImmersiveIntelligence.proxy.reloadParticles();
		//Clear and re-init the particle registry
		ParticleRegistry.cleanBuilderRegistry();
		IIParticles.preInit();
		IIParticles.init();
		ParticleRegistry.loadAllParticleFiles();
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

}
