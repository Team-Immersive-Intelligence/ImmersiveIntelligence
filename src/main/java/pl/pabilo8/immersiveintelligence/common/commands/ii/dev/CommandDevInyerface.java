package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageParticleEffect;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevInyerface extends CommandIIBase
{
	public CommandDevInyerface(CommandTreeBase parent)
	{
		super(parent, "inyerface");
	}

	@Override
	public String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Throw a stone, will 'ye?!";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity entity = sender.getCommandSenderEntity();
		if(entity==null)
			throw new CommandException("Entity not found");

		Vec3d pos = entity.getPositionEyes(0f);
		Vec3d look = entity.getLookVec();
		IIPacketHandler.sendToClient(new MessageParticleEffect("debris/big_brick", sender.getEntityWorld(), pos, look, 0, 0, null));
		sender.sendMessage(new TextComponentString(String.format("A boulder has been thrown!")));
	}
}
