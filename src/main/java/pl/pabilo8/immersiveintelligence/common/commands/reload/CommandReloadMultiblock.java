package pl.pabilo8.immersiveintelligence.common.commands.reload;

import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 23-06-2020
 */
public class CommandReloadMultiblock extends CommandBase
{
	/**
	 * Gets the name of the command
	 */
	@Nonnull
	@Override
	public String getName()
	{
		return "relmb";
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "Reload all registered II Multiblocks";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
	{
		boolean success = false;
		if(args.length > 0)
		{
			for(String arg : args)
			{
				IMultiblock multiblock = IIContent.MULTIBLOCKS.stream()
						.filter(mb -> Objects.equals(mb.getUniqueName(), arg))
						.findFirst().orElse(null);

				if(multiblock instanceof MultiblockStuctureBase)
				{
					((MultiblockStuctureBase<?>)multiblock).updateStructure();
					success = true;

					for(TileEntity te : sender.getEntityWorld().loadedTileEntityList)
						if(te instanceof TileEntityMultiblockIIGeneric)
						{
							TileEntityMultiblockIIGeneric<?> teMB = (TileEntityMultiblockIIGeneric<?>)te;
							(teMB).forceReCacheAABB();
							if((teMB).isDummy())
								continue;
							(teMB).sendNBTMessageClient(
									EasyNBT.newNBT()
											.withBoolean(TileEntityMultiblockIIGeneric.KEY_SYNC_AABB, true)
											.unwrap()
							);
						}
				}
			}
		}
		else
		{
			IIContent.MULTIBLOCKS.stream()
					.filter(mb -> mb instanceof MultiblockStuctureBase)
					.map(mb -> ((MultiblockStuctureBase<?>)mb))
					.forEach(MultiblockStuctureBase::updateStructure);

			//Technically, it should always be true, but who knows ^^
			success = IIContent.MULTIBLOCKS.size() > 0;
		}
		sender.sendMessage(new TextComponentString((success?"Succesfully reloaded ": "Couldn't reload ")+(args.length==1?args[0]: "multiblocks")));
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		List<String> multiblocks = IIContent.MULTIBLOCKS.stream()
				.filter(mb -> mb instanceof MultiblockStuctureBase)
				.map(IMultiblock::getUniqueName)
				.collect(Collectors.toList());

		return getListOfStringsMatchingLastWord(args, multiblocks);
	}
}
