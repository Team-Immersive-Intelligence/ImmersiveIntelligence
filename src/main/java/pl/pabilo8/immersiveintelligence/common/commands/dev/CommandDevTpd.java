package pl.pabilo8.immersiveintelligence.common.commands.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevTpd extends CommandIIBase
{
	public CommandDevTpd(CommandTreeBase parent)
	{
		super(parent, "tpd");
	}

	@Override
	public String getSyntax()
	{
		return "<dim>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Teleports the player to a dimension";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity senderEntity = sender.getCommandSenderEntity();
		if(args.length > 0&&senderEntity!=null)
		{
			Optional<DimensionType> found = Arrays.stream(DimensionType.values())
					.filter(d -> d.getName().equals(args[0])||String.valueOf(d.getId()).equals(args[0]))
					.findFirst();

			if(found.isPresent())
			{
				senderEntity.changeDimension(found.get().getId(), new IITeleporter());
				sender.sendMessage(new TextComponentString("Preparing to jump!"));
			}
			else
				throw new CommandException("Invalid dimension!");
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if(args.length==1)
			return Arrays.stream(DimensionType.values())
					.map(DimensionType::getName)
					.collect(Collectors.toList());
		return Collections.emptyList();
	}

	public static class IITeleporter implements ITeleporter
	{
		@Override
		public void placeEntity(World world, Entity entity, float yaw)
		{
			entity.moveToBlockPosAndAngles(world.getSpawnPoint(), yaw, entity.rotationPitch);
		}
	}
}
