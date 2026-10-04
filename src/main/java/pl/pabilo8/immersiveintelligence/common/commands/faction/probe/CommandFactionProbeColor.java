package pl.pabilo8.immersiveintelligence.common.commands.faction.probe;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.commands.faction.CommandFactionBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

/**
 * Show the faction colour of this chunk.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.10.2026
 */
public class CommandFactionProbeColor extends CommandFactionBase
{
	public CommandFactionProbeColor(CommandTreeBase parent)
	{
		super(parent, "color");
	}

	@Override
	public String getSyntax()
	{
		return "";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Show the faction colour of this chunk";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		requireArgumentCount(sender, args, 0);
		OwnerIdentity owner = getChunkOwner(sender);
		sender.sendMessage(new TextComponentString("Faction colour: #"+owner.getColor().getHexRGB()));
	}
}
