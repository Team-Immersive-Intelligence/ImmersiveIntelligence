package pl.pabilo8.immersiveintelligence.common.util.commands.tmt;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
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
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBullet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 23-06-2020
 */
public class CommandIIGiveBullet extends CommandBase
{
	/**
	 * Gets the name of the command
	 */
	@Nonnull
	@Override
	public String getName()
	{
		return "bullet";
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "Gives a bullet, usage: ii bullet <receiver> <casing> <core> <comp1> <comp2> <prop> [amount1] [amount2] [nbt1] [nbt2]";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
	{
		if(args.length > 5)
		{
			float p1 = Float.parseFloat(args[5]);
			NBTTagCompound tag1 = new NBTTagCompound(), tag2 = new NBTTagCompound();
			float amount1 = 1f, amount2 = 1f;

			if(args.length > 6)
				amount1 = (float)CommandBase.parseDouble(args[6]);
			if(args.length > 7)
				amount1 = (float)CommandBase.parseDouble(args[7]);

			EntityPlayerMP player = CommandBase.getPlayer(server, sender, args[0]);
			ItemStack bullet = ItemIIBullet.getAmmoStack(1, args[1], args[2], args[3], args[4], p1, amount1, amount2);

			if(args.length > 8)
			{
				try
				{
					ItemNBTHelper.setTagCompound(bullet, "firstComponentNBT", JsonToNBT.getTagFromJson(args[8]));
				} catch(NBTException nbtexception)
				{
					throw new CommandException("Incorrect bullet1 NBT");
				}
			}
			if(args.length > 9)
			{
				try
				{
					ItemNBTHelper.setTagCompound(bullet, "secondComponentNBT", JsonToNBT.getTagFromJson(args[9]));
				} catch(NBTException nbtexception)
				{
					throw new CommandException("Incorrect bullet2 NBT");
				}
			}

			player.addItemStackToInventory(bullet);
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
		else if(args.length==2)
		{
			return getListOfStringsMatchingLastWord(args, BulletRegistry.INSTANCE.registeredCasings.keySet());
		}
		else if(args.length==3)
		{
			return getListOfStringsMatchingLastWord(args, BulletRegistry.INSTANCE.registeredBulletCores.keySet());
		}
		else if(args.length==4||args.length==5)
		{
			return getListOfStringsMatchingLastWord(args, BulletRegistry.INSTANCE.registeredComponents.keySet());
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
