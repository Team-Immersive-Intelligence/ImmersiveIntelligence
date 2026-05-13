package pl.pabilo8.immersiveintelligence.common.commands.faction.probe;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionRole;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.chunk.chunk.IChunkOwnership;

import java.util.UUID;

public class CommandFactionProbeOwner extends CommandIIBase
{
	public CommandFactionProbeOwner(CommandTreeBase parent)
	{
		super(parent, "owner");
	}

	@Override
	public String getSyntax()
	{
		return "";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Show the faction and its owner players that own this chunk";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity entity = sender.getCommandSenderEntity();
		if(!(entity instanceof EntityPlayer))
			throw new CommandException("Player only.");
		DiplomacyHandler diplomacy = DiplomacyHandler.getInstance(false);
		IChunkOwnership ownership = diplomacy.getPositionOwnership(entity.getEntityWorld(), entity.getPosition());
		OwnerIdentity faction = (ownership!=null)?ownership.getOwner(): DiplomacyHandler.NEUTRAL;
		StringBuilder sb = new StringBuilder("Chunk owner: ").append(faction);

		//List players with role isOwner
		sb.append(" | Owners: ");
		boolean first = true;
		for(UUID uuid : faction.getMembers())
		{
			PermissionRole role = faction.getRoleOf(uuid);
			if(role!=null&&role.isOwner())
			{
				if(!first)
					sb.append(", ");
				EntityPlayer player = server.getPlayerList().getPlayerByUUID(uuid);
				sb.append(player!=null?player.getName(): uuid.toString().substring(0, 8));
				first = false;
			}
		}
		if(first)
			sb.append("none");
		sender.sendMessage(new TextComponentString(sb.toString()));
	}
}
