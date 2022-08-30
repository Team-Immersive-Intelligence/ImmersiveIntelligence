package pl.pabilo8.immersiveintelligence.common.commands.ii;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoCore;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine;

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
public class CommandIIGiveMagazine extends CommandBase
{
	/**
	 * Gets the name of the command
	 */
	@Nonnull
	@Override
	public String getName()
	{
		return "magazine";
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "Gives a bullet, usage: ii magazine <receiver> <name> <core> <coreType> [comps]";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
	{
		if(args.length > 3)
		{
			EntityPlayerMP player = CommandBase.getPlayer(server, sender, args[0]);
			ItemStack magazine = IIUtils.getStackWithMetaName(IIContent.itemBulletMagazine, args[1]);
			IAmmo casing = ItemIIBulletMagazine.getMatchingType(magazine);
			IAmmoCore core = AmmoRegistry.INSTANCE.getCore(args[2]);
			EnumCoreTypes coreType = EnumCoreTypes.v(args[3]);
			ArrayList<IAmmoComponent> components = new ArrayList<>();
			for(int i = 4; i < args.length; i++)
				components.add(AmmoRegistry.INSTANCE.getComponent(args[i]));

			if(casing!=null&&core!=null)
			{
				ItemStack bulletWithParams = casing.getBulletWithParams(core, coreType, components.toArray(new IAmmoComponent[0]));
				NonNullList<ItemStack> l = NonNullList.withSize(ItemIIBulletMagazine.getBulletCapactity(magazine), bulletWithParams);
				ItemNBTHelper.getTag(magazine).setTag("bullets", ItemIIBulletMagazine.writeInventory(l));
				ItemIIBulletMagazine.makeDefault(magazine);
				player.addItemStackToInventory(magazine);
				sender.sendMessage(new TextComponentString("Magazine given!"));
			}
			else
				throw new WrongUsageException(getUsage(sender));
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
			return getListOfStringsMatchingLastWord(args, IIContent.itemBulletMagazine.getSubNames());
		}
		else if(args.length==3)
		{
			return getListOfStringsMatchingLastWord(args, AmmoRegistry.INSTANCE.registeredBulletCores.keySet());
		}
		else if(args.length==4)
		{
			ItemStack magazine = IIUtils.getStackWithMetaName(IIContent.itemBulletMagazine, args[1]);
			IAmmo matchingType = ItemIIBulletMagazine.getMatchingType(magazine);
			return getListOfStringsMatchingLastWord(args,matchingType==null?Collections.emptyList():Arrays.stream(matchingType.getAllowedCoreTypes()).map(EnumCoreTypes::getName).collect(Collectors.toList()));
		}
		else if(args.length > 4)
		{
			return getListOfStringsMatchingLastWord(args, AmmoRegistry.INSTANCE.registeredComponents.keySet());
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
