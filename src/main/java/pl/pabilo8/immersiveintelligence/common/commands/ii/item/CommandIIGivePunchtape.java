package pl.pabilo8.immersiveintelligence.common.commands.ii.item;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 23-06-2020
 */
public class CommandIIGivePunchtape extends CommandBase
{
	/**
	 * Gets the name of the command
	 */
	@Nonnull
	@Override
	public String getName()
	{
		return "punchtape";
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "Gives a punchtape, usage: ii punchtape <receiver> <nbt>";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
	{
		if(args.length > 1)
		{
			EntityPlayerMP player = CommandBase.getPlayer(server, sender, args[0]);
			try
			{
				StringBuilder builder = new StringBuilder();
				for(int i = 1; i < args.length; i++)
					builder.append(args[i]);
				NBTTagCompound nbt = JsonToNBT.getTagFromJson(builder.toString());
				ItemStack stack = new ItemStack(IIContent.itemPunchtape, 1, 1);
				DataPacket dataPacket = new DataPacket();
				dataPacket.fromNBT(nbt);
				IIContent.itemPunchtape.writeDataToItem(dataPacket, stack);

				player.addItemStackToInventory(stack);
				sender.sendMessage(new TextComponentString("Punchtape given!"));
			} catch(NBTException e)
			{
				throw new WrongUsageException(getUsage(sender));
			}
		}
		else
			throw new WrongUsageException(getUsage(sender));
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel()
	{
		return 4;
	}

	/**
	 * Get a list of options for when the user presses the TAB key
	 */
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		if(args.length==1)
		{
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}
		else
			return Collections.emptyList();
	}

	/**
	 * Return whether the specified command parameter index is a username parameter.
	 */
	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index==0;
	}
}
