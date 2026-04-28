package pl.pabilo8.immersiveintelligence.common.commands.faction.probe;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.chunk.IChunkOwnership;

public class CommandFactionProbeColor extends CommandIIBase
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
		return "Show faction color of this chunk";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity entity = sender.getCommandSenderEntity();
		if(!(entity instanceof EntityPlayer))
			throw new CommandException("Player only.");

		IChunkOwnership ownership = DiplomacyUtils.getPositionOwnership(entity.getEntityWorld(), entity.getPosition());
		OwnerIdentity owner = (ownership!=null)?ownership.getOwner(): DiplomacyUtils.NEUTRAL;
		sender.sendMessage(new TextComponentString("Faction color: "+owner.getColor().getHexRGB()));
	}
}
