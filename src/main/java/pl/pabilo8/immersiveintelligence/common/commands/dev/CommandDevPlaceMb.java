package pl.pabilo8.immersiveintelligence.common.commands.dev;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevPlaceMb extends CommandIIBase
{
	public CommandDevPlaceMb(CommandTreeBase parent)
	{
		super(parent, "place_mb");
	}

	@Override
	public String getSyntax()
	{
		return "<id>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Places a multiblock";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity senderEntity = sender.getCommandSenderEntity();
		RayTraceResult traceResult = CommandIIDev.getRayTraceResult(senderEntity, 40f);
		if(traceResult==null||traceResult.typeOfHit==RayTraceResult.Type.MISS) return;
		for(IMultiblock mb : MultiblockHandler.getMultiblocks())
			if(mb.getUniqueName().equals(args[0]))
			{
				BlockPos placed = traceResult.getBlockPos().up();
				ItemStack[][][] manual = mb.getStructureManual();
				int hh = manual.length, ww = manual[0][0].length, ll = manual[0].length;
				for(int y = 0; y < hh; y++)
					for(int z = 0; z < ll; z++)
						for(int x = 0; x < ww; x++)
						{
							ItemStack stack = manual[y][z][x];
							if(stack==null||stack.isEmpty()) continue;
							IBlockState state = mb.getBlockstateFromStack(y*ww*ll+z*ww+x, stack);
							if(state!=null)
								senderEntity.world.setBlockState(placed.add(x, y, z), state);
						}
				break;
			}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if(args.length==1)
			return getListOfStringsMatchingLastWord(args, MultiblockHandler.getMultiblocks().stream()
					.map(IMultiblock::getUniqueName)
					.collect(Collectors.toList()));
		return Collections.emptyList();
	}
}
