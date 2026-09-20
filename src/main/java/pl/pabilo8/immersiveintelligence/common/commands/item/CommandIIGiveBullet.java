package pl.pabilo8.immersiveintelligence.common.commands.item;

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
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
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
 * Gives or fires validated ammunition items and ammunition cores.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 26.08.2026
 * @since 23.06.2020
 */
public class CommandIIGiveBullet extends CommandBase
{
	private final boolean giveCore;

	/**
	 * Creates the ammunition item command.
	 */
	public CommandIIGiveBullet()
	{
		this(false);
	}

	/**
	 * Creates an ammunition item or ammunition core command.
	 *
	 * @param giveCore true to give ammunition cores
	 */
	public CommandIIGiveBullet(boolean giveCore)
	{
		this.giveCore = giveCore;
	}

	@Nonnull
	@Override
	public String getName()
	{
		return giveCore?"ammocore": "ammo";
	}

	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return giveCore?
				"/ii ammocore <receiver> <ammunition type> <core material> <core type>":
				"/ii ammo <receiver|fire@receiver> <ammunition type> <core material> <core type> <fuse> [component ...]";
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
	{
		int requiredArguments = giveCore?4: 5;
		if(args.length < requiredArguments||(giveCore&&args.length > requiredArguments))
			throw new WrongUsageException(getUsage(sender));

		IAmmoTypeItem<?, ?> ammoType = CommandAmmoUtils.resolveAmmoType(args[1]);
		AmmoCore core = CommandAmmoUtils.resolveCore(args[2]);
		CoreType coreType = CommandAmmoUtils.resolveCoreType(ammoType, args[3]);

		ItemStack ammoStack;
		if(giveCore)
		{
			if(args[0].startsWith("fire@"))
				throw new CommandException("The /ii ammocore command cannot fire ammunition cores.");
			ammoStack = ammoType.getAmmoCoreStack(core, coreType);
		}
		else
		{
			FuseType fuse = CommandAmmoUtils.resolveFuseType(ammoType, args[4]);
			AmmoComponent[] components = CommandAmmoUtils.resolveComponents(ammoType, coreType, Arrays.copyOfRange(args, 5, args.length));
			ammoStack = ammoType.getAmmoStack(core, coreType, fuse, components);

			switch(fuse)
			{
				case TIMED:
					ammoType.setFuseParameter(ammoStack, 200);
					break;
				case PROXIMITY:
					ammoType.setFuseParameter(ammoStack, 2);
					break;
				default:
					break;
			}
		}

		if(ammoStack.isEmpty())
			throw new CommandException("Ammunition type '%s' did not create a valid item.", ammoType.getName());

		if(!giveCore&&args[0].startsWith("fire@"))
		{
			String target = args[0].substring(5);
			if(target.isEmpty())
				throw new CommandException("Missing player after 'fire@'. Example: fire@@p");

			EntityPlayerMP player = CommandBase.getPlayer(server, sender, target);
			new AmmoFactory<>(player)
					.setStack(ammoStack)
					.setPositionAndVelocity(player.getPositionEyes(0), player.getLookVec().normalize(), 1)
					.create();
			sender.sendMessage(new TextComponentString("Fired "+ammoType.getName()+" ammunition as "+player.getName()+"."));
			return;
		}

		EntityPlayerMP player = CommandBase.getPlayer(server, sender, args[0]);
		if(!player.addItemStackToInventory(ammoStack))
			player.dropItem(ammoStack, false);
		sender.sendMessage(new TextComponentString("Gave "+(giveCore?"an ammunition core": "ammunition")+" to "+player.getName()+"."));
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
		{
			ArrayList<String> strings = Lists.newArrayList(server.getOnlinePlayerNames());
			if(!giveCore)
				strings.add("fire@@p");
			return getListOfStringsMatchingLastWord(args, strings);
		}
		else if(args.length==2)
			return getListOfStringsMatchingLastWord(args, AmmoRegistry.getAllAmmoItems().stream().map(IAmmoTypeItem::getName).collect(Collectors.toList()));
		else if(args.length==3)
			return getListOfStringsMatchingLastWord(args, AmmoRegistry.getAllCores().stream().map(AmmoCore::getName).collect(Collectors.toList()));
		else if(args.length==4)
		{
			IAmmoTypeItem<?, ?> ammoType = AmmoRegistry.getAmmoItem(args[1]);
			return getListOfStringsMatchingLastWord(args, ammoType==null?Collections.emptyList(): Arrays.stream(ammoType.getAllowedCoreTypes()).map(CoreType::getName).collect(Collectors.toList()));
		}
		else if(!giveCore&&args.length==5)
		{
			IAmmoTypeItem<?, ?> ammoType = AmmoRegistry.getAmmoItem(args[1]);
			return getListOfStringsMatchingLastWord(args, ammoType==null?Collections.emptyList(): Arrays.stream(ammoType.getAllowedFuseTypes()).map(FuseType::getName).collect(Collectors.toList()));
		}
		else if(!giveCore&&args.length > 5)
		{
			IAmmoTypeItem<?, ?> ammoType = AmmoRegistry.getAmmoItem(args[1]);
			CoreType coreType = findCoreType(args[3]);
			if(ammoType==null||coreType==null||!Arrays.asList(ammoType.getAllowedCoreTypes()).contains(coreType))
				return Collections.emptyList();

			String[] existingComponents = Arrays.copyOfRange(args, 5, args.length-1);
			return getListOfStringsMatchingLastWord(args, CommandAmmoUtils.getValidComponentNames(ammoType, coreType, existingComponents));
		}
		return Collections.emptyList();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index==0;
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
