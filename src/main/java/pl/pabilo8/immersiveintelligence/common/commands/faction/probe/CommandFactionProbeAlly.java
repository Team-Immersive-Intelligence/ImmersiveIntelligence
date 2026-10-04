package pl.pabilo8.immersiveintelligence.common.commands.faction.probe;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.commands.faction.CommandFactionBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomaticStatus;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Check your relation towards a faction or this chunk owner.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.10.2026
 */
public class CommandFactionProbeAlly extends CommandFactionBase
{
	public CommandFactionProbeAlly(CommandTreeBase parent)
	{
		super(parent, "ally");
	}

	@Override
	public String getSyntax()
	{
		return "[faction_name|id]";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Check your relation towards a faction or this chunk owner";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		if(!(sender instanceof EntityPlayer))
			return Collections.emptyList();
		return getFactionCompletions(args, 0, DiplomacyHandler.getInstance(false).getActivePlayerIdentities());
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		getPlayer(sender);
		OwnerIdentity other = args.length==0?getChunkOwner(sender): findFaction(args, 0);
		boolean result = getFaction(sender).getRelationTowards(other).atLeast(DiplomaticStatus.ALLIED);
		sender.sendMessage(new TextComponentString(result?"Allied.": "Not allied."));
	}
}
