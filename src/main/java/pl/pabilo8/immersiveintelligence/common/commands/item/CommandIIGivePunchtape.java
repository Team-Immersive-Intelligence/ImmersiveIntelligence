package pl.pabilo8.immersiveintelligence.common.commands.item;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Gives a punchtape from validated NBT data.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 26.08.2026
 * @since 23.06.2020
 */
public class CommandIIGivePunchtape extends CommandBase
{
	@Nonnull
	@Override
	public String getName()
	{
		return "punchtape";
	}

	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "/ii punchtape <receiver> <nbt>";
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
	{
		if(args.length < 2)
			throw new WrongUsageException(getUsage(sender));

		EntityPlayerMP player = CommandBase.getPlayer(server, sender, args[0]);
		try
		{
			String nbtText = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
			NBTTagCompound nbt = JsonToNBT.getTagFromJson(nbtText);
			ItemStack stack = new ItemStack(IIContent.itemPunchtape, 1, 1);
			IIContent.itemPunchtape.writeDataToItem(stack, new DataPacket(nbt));

			if(!player.addItemStackToInventory(stack))
				player.dropItem(stack, false);
			sender.sendMessage(new TextComponentString("Gave a punchtape to "+player.getName()+"."));
		} catch(NBTException e)
		{
			throw new CommandException("Invalid punchtape NBT: %s", e.getMessage());
		}
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 4;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		if(args.length==1)
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		return Collections.emptyList();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index==0;
	}
}
