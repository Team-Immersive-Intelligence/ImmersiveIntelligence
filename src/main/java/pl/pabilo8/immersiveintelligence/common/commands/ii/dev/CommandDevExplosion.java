package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevExplosion extends CommandIIBase
{
	public CommandDevExplosion(CommandTreeBase parent)
	{
		super(parent, "explosion");
	}

	@Override
	public String getSyntax()
	{
		return "[size] [power] [shape]";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Spawns an II explosion";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity senderEntity = sender.getCommandSenderEntity();
		if(senderEntity==null||server.getEntityWorld().isRemote) return;
		float blockReachDistance = 100f;
		RayTraceResult traceResult = CommandIIDev.getRayTraceResult(senderEntity, blockReachDistance);
		if(traceResult==null||traceResult.typeOfHit==RayTraceResult.Type.MISS) return;
		BlockPos pos = traceResult.getBlockPos();
		int size = 2, power = 7;
		ComponentEffectShape shape = ComponentEffectShape.ORB;
		try
		{
			if(args.length > 0) size = Math.abs(Integer.parseInt(args[0]));
			if(args.length > 1) power = Math.abs(Integer.parseInt(args[1]));
			if(args.length > 2) shape = ComponentEffectShape.valueOf(args[2].toUpperCase());
		} catch(Exception ignored) {}
		IIExplosion exp = new IIExplosion(server.getEntityWorld(), senderEntity, new Vec3d(pos), null, size, power, shape, false, true, false);
		exp.doExplosionA();
		exp.doExplosionB(true);
	}

	@Override
	public java.util.List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @javax.annotation.Nullable net.minecraft.util.math.BlockPos pos)
	{
		if(args.length==3)
			return getListOfStringsMatchingLastWord(args,
					Arrays.stream(ComponentEffectShape.values())
							.map(ComponentEffectShape::getName)
							.map(String::toUpperCase)
							.collect(Collectors.toList())
			);
		return java.util.Collections.emptyList();
	}
}
