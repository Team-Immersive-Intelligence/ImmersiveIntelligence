package pl.pabilo8.immersiveintelligence.common.commands.ii.item;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
		return "Gives a bullet, usage: ii bullet <receiver> <casing> <core> <coreType> <fuse> [component] [nbt]";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
	{
		if(args.length > 3)
		{
			IAmmoTypeItem<?, ?> ammoType = AmmoRegistry.getAmmoItem(args[1]);
			AmmoCore core = AmmoRegistry.getCore(args[2]);
			CoreTypes coreType = CoreTypes.v(args[3]);
			FuseTypes fuse = FuseTypes.v(args[4]);

			//Load components
			ArrayList<AmmoComponent> components = new ArrayList<>();
			for(int i = 5; i < args.length; i++)
				components.add(AmmoRegistry.getComponent(args[i]));

			//check if the ammo type and core are valid
			if(ammoType==null||core==AmmoRegistry.MISSING_CORE)
				throw new WrongUsageException(getUsage(sender));
			ItemStack ammoStack = ammoType.getBulletWithParams(core, coreType, components.toArray(new AmmoComponent[0]));

			//fire the bullet directly
			if(args[0].startsWith("fire@"))
			{
				EntityPlayerMP player = CommandBase.getPlayer(server, sender, args[0].substring(5));
				new AmmoFactory<>(player)
						.setStack(ammoStack)
						.setPositionAndVelocity(player.getPositionEyes(0), player.getLookVec().normalize(), 1)
						.create();
				sender.sendMessage(new TextComponentString("Fire!"));
			}
			//give the bullet to the player
			else
				CommandBase.getPlayer(server, sender, args[0]).addItemStackToInventory(ammoStack);

			sender.sendMessage(new TextComponentString("Bullets given!"));

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
			ArrayList<String> strings = Lists.newArrayList(server.getOnlinePlayerNames());
			strings.add("fire@@p");
			return getListOfStringsMatchingLastWord(args, strings);
		}
		else if(args.length==2)
			return getListOfStringsMatchingLastWord(args, AmmoRegistry.getAllAmmoItems().stream().map(IAmmoTypeItem::getName).collect(Collectors.toList()));
		else if(args.length==3)
			return getListOfStringsMatchingLastWord(args, AmmoRegistry.getAllCores().stream().map(AmmoCore::getName).collect(Collectors.toList()));
		else if(args.length==4)
		{
			IAmmoTypeItem<?, ?> bullet = AmmoRegistry.getAmmoItem(args[1]);
			return getListOfStringsMatchingLastWord(args, bullet==null?Collections.emptyList(): Arrays.stream(bullet.getAllowedCoreTypes()).map(CoreTypes::getName).collect(Collectors.toList()));
		}
		else if(args.length==5)
		{
			IAmmoTypeItem<?, ?> bullet = AmmoRegistry.getAmmoItem(args[1]);
			return getListOfStringsMatchingLastWord(args, bullet==null?Collections.emptyList(): Arrays.stream(bullet.getAllowedFuseTypes()).map(FuseTypes::getName).collect(Collectors.toList()));
		}
		else if(args.length > 5)
			return getListOfStringsMatchingLastWord(args, AmmoRegistry.getAllComponents().stream().map(AmmoComponent::getName).collect(Collectors.toList()));
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
