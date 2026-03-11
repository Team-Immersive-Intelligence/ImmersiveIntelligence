package pl.pabilo8.immersiveintelligence.common.commands.reload;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.commands.CommandIIHelp;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandIIReload extends CommandTreeBase
{
	public CommandIIReload()
	{
		addSubcommand(new CommandReloadSkins(this));
		addSubcommand(new CommandReloadModels(this));
		addSubcommand(new CommandReloadManual(this));
		addSubcommand(new CommandReloadMultiblock(this));
		addSubcommand(new CommandReloadParticles(this));
		addSubcommand(new CommandReloadVehicles(this));
		addSubcommand(new CommandReloadRecipeLayouts(this));
		addSubcommand(new CommandIIHelp(this, "reload"));
	}

	@Override
	public String getName()
	{
		return "reload";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Executes an Immersive Intelligence reload command, use /ii reload help for info";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}
}
