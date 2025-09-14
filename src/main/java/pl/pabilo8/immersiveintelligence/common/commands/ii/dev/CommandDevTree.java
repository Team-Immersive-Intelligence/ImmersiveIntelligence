package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.world.IIWorldGen;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevTree extends CommandIIBase
{
	public CommandDevTree(CommandTreeBase parent)
	{
		super(parent, "tree");
	}

	@Override
	public String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Creates a happy little [R E B B U R] tree";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity senderEntity = sender.getCommandSenderEntity();
		if(senderEntity==null) return;
		float blockReachDistance = 100f;
		RayTraceResult traceResult = CommandIIDev.getRayTraceResult(senderEntity, blockReachDistance);
		if(traceResult==null||traceResult.typeOfHit==RayTraceResult.Type.MISS) return;
		IIWorldGen.worldGenRubberTree.generate(sender.getEntityWorld(), Utils.RAND, traceResult.getBlockPos().up());
		sender.sendMessage(new TextComponentString("Adding a happy little tree :)"));
	}
}
