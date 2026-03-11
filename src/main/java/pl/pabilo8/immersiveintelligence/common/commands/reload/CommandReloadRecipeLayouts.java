package pl.pabilo8.immersiveintelligence.common.commands.reload;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.06.2020
 */
public class CommandReloadRecipeLayouts extends CommandIIBase
{
	public CommandReloadRecipeLayouts(CommandTreeBase parent)
	{
		super(parent, "layouts");
	}

	@Nullable
	@Override
	public String getSyntax()
	{
		return "";
	}

	@Nullable
	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Reload all registered II multiblock recipe layouts";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
	{
		IIMultiblockRecipe.reloadAllRecipeLayouts();
		sender.sendMessage(new TextComponentString("Reloaded all Immersive Intelligence multiblock recipe layouts"));
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
