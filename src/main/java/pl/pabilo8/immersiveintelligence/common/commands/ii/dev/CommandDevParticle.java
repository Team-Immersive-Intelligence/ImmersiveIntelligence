package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageParticleEffect;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevParticle extends CommandIIBase
{
	public CommandDevParticle(CommandTreeBase parent)
	{
		super(parent, "particle");
	}

	@Override
	public String getSyntax()
	{
		return "<id> <x> <y> <z> [motionX] [motionY] [motionZ]";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Spawns a particle";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length < 1) throw new net.minecraft.command.WrongUsageException(getUsage(sender));
		Vec3d pos = new Vec3d(args.length >= 4?parseBlockPos(sender, args, 1, true): sender.getPosition());
		Vec3d motion = Vec3d.ZERO;
		if(args.length >= 7)
		{
			double motionX = Double.parseDouble(args[4]);
			double motionY = Double.parseDouble(args[5]);
			double motionZ = Double.parseDouble(args[6]);
			motion = new Vec3d(motionX, motionY, motionZ);
		}
		IIPacketHandler.sendToClient(new MessageParticleEffect(args[0], sender.getEntityWorld(), pos, motion, 0, 0, null));
		sender.sendMessage(new net.minecraft.util.text.TextComponentString(String.format("Particle %s created!", args[0])));
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if(args.length==1)
			return getListOfStringsMatchingLastWord(args, ParticleRegistry.getRegisteredNames());
		else if(args.length >= 2&&args.length <= 4)
			return getTabCompletionCoordinate(args, 2, sender.getPosition());
		return Collections.emptyList();
	}
}
