package pl.pabilo8.immersiveintelligence.common.commands.item;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine.Magazines;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gives magazines that contain validated ammunition.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 26.08.2026
 * @since 23.06.2020
 */
public class CommandIIGiveMagazine extends CommandBase
{
	@Nonnull
	@Override
	public String getName()
	{
		return "magazine";
	}

	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "/ii magazine <receiver> <magazine> <core material> <core type> [component ...]";
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
	{
		if(args.length < 4)
			throw new WrongUsageException(getUsage(sender));
		if(!ArrayUtils.contains(IIContent.itemBulletMagazine.getSubNames(), args[1]))
			throw new CommandException("Unknown magazine type '%s'.", args[1]);

		EntityPlayerMP player = CommandBase.getPlayer(server, sender, args[0]);
		Magazines magazineType = IIContent.itemBulletMagazine.nameToSub(args[1]);
		IAmmoTypeItem<?, ?> ammoType = magazineType.ammo;
		AmmoCore core = CommandAmmoUtils.resolveCore(args[2]);
		CoreType coreType = CommandAmmoUtils.resolveCoreType(ammoType, args[3]);
		CommandAmmoUtils.resolveFuseType(ammoType, FuseType.CONTACT.getName());
		AmmoComponent[] components = CommandAmmoUtils.resolveComponents(ammoType, coreType, Arrays.copyOfRange(args, 4, args.length));

		ItemStack bullet = ammoType.getAmmoStack(core, coreType, FuseType.CONTACT, components);
		if(bullet.isEmpty())
			throw new CommandException("Ammunition type '%s' did not create a valid item.", ammoType.getName());

		ItemStack magazine = IIContent.itemBulletMagazine.getMagazine(magazineType, bullet);
		if(magazine.isEmpty())
			throw new CommandException("Magazine type '%s' did not create a valid item.", magazineType.getName());

		if(!player.addItemStackToInventory(magazine))
			player.dropItem(magazine, false);
		sender.sendMessage(new TextComponentString("Gave a "+magazineType.getName()+" magazine to "+player.getName()+"."));
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 4;
	}

	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		if(args.length==1)
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		else if(args.length==2)
			return getListOfStringsMatchingLastWord(args, IIContent.itemBulletMagazine.getSubNames());
		else if(args.length==3)
			return getListOfStringsMatchingLastWord(args, AmmoRegistry.getAllCores().stream().map(AmmoCore::getName).collect(Collectors.toList()));
		else if(args.length==4)
		{
			IAmmoTypeItem<?, ?> ammoType = getMagazineAmmoType(args[1]);
			return getListOfStringsMatchingLastWord(args, ammoType==null?Collections.emptyList(): Arrays.stream(ammoType.getAllowedCoreTypes()).map(CoreType::getName).collect(Collectors.toList()));
		}
		else if(args.length > 4)
		{
			IAmmoTypeItem<?, ?> ammoType = getMagazineAmmoType(args[1]);
			CoreType coreType = findCoreType(args[3]);
			if(ammoType==null||coreType==null||!Arrays.asList(ammoType.getAllowedCoreTypes()).contains(coreType))
				return Collections.emptyList();

			String[] existingComponents = Arrays.copyOfRange(args, 4, args.length-1);
			return getListOfStringsMatchingLastWord(args, CommandAmmoUtils.getValidComponentNames(ammoType, coreType, existingComponents));
		}
		return Collections.emptyList();
	}

	@Override
	public boolean isUsernameIndex(@Nonnull String[] args, int index)
	{
		return index==0;
	}

	@Nullable
	private static IAmmoTypeItem<?, ?> getMagazineAmmoType(String name)
	{
		return ArrayUtils.contains(IIContent.itemBulletMagazine.getSubNames(), name)?IIContent.itemBulletMagazine.nameToSub(name).ammo: null;
	}

	@Nullable
	private static CoreType findCoreType(String name)
	{
		return Arrays.stream(CoreType.values())
				.filter(type -> type.getName().equalsIgnoreCase(name))
				.findFirst()
				.orElse(null);
	}
}
