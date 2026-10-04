package pl.pabilo8.immersiveintelligence.common.commands.faction;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Factions;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.chunk.chunk.IChunkOwnership;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides player access, faction permission checks, and faction name resolution.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.10.2026
 */
public abstract class CommandFactionBase extends CommandIIBase
{
	@Nullable
	private final PermissionCategory permission;

	protected CommandFactionBase(CommandTreeBase parent, String name)
	{
		this(parent, name, null);
	}

	protected CommandFactionBase(CommandTreeBase parent, String name, @Nullable PermissionCategory permission)
	{
		super(parent, name);
		this.permission = permission;
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		if(!Factions.enableFactions||!(sender instanceof EntityPlayer))
			return false;
		if(permission==null)
			return true;
		EntityPlayer player = (EntityPlayer)sender;
		OwnerIdentity identity = DiplomacyHandler.getInstance(false).getOwnerIdentityForEntity(player);
		return !identity.isInvalid()&&identity.isMember(player)&&identity.isPermitted(player, permission);
	}

	protected EntityPlayer getPlayer(ICommandSender sender) throws CommandException
	{
		if(!Factions.enableFactions)
			throw new CommandException("Factions are disabled.");
		if(!(sender instanceof EntityPlayer))
			throw new CommandException("Player only.");
		return (EntityPlayer)sender;
	}

	protected OwnerIdentity getFaction(ICommandSender sender) throws CommandException
	{
		EntityPlayer player = getPlayer(sender);
		OwnerIdentity identity = DiplomacyHandler.getInstance(false).getOwnerIdentityForEntity(player);
		if(identity.isInvalid()||!identity.isMember(player))
			throw new CommandException("You must belong to a valid faction.");
		if(!identity.isPermitted(player, permission))
			throw new CommandException("commands.generic.permission");
		return identity;
	}

	protected OwnerIdentity getChunkOwner(ICommandSender sender) throws CommandException
	{
		EntityPlayer player = getPlayer(sender);
		IChunkOwnership ownership = DiplomacyHandler.getInstance(false)
				.getPositionOwnership(player.world, player.getPosition());
		OwnerIdentity owner = ownership==null?null: ownership.getOwner();
		return owner==null||owner.isInvalid()?DiplomacyHandler.NEUTRAL: owner;
	}

	protected void requireArgumentCount(ICommandSender sender, String[] args, int count) throws CommandException
	{
		if(args.length!=count)
			throw new WrongUsageException("/factions_ii "+(parent instanceof CommandIIFaction?"": parent.getName()+" ")
					+name+" "+getSyntax());
	}

	protected OwnerIdentity findFaction(String[] args, int offset) throws CommandException
	{
		return findFaction(args, offset, DiplomacyHandler.getInstance(false).getActivePlayerIdentities());
	}

	protected OwnerIdentity findFaction(String[] args, int offset, List<OwnerIdentity> candidates) throws CommandException
	{
		if(args.length <= offset)
			throw new CommandException("Specify a faction name or ID.");
		String name = String.join(" ", Arrays.copyOfRange(args, offset, args.length)).trim();
		OwnerIdentity match = null;
		boolean ambiguous = false;
		for(OwnerIdentity identity : candidates)
		{
			if(identity.getStringUUID().equalsIgnoreCase(name))
				return identity;
			if(identity.getDisplayName().equalsIgnoreCase(name))
			{
				ambiguous |= match!=null;
				match = identity;
			}
		}
		if(ambiguous)
			throw new CommandException("Several factions have that name. Use a faction ID.");
		if(match==null)
			throw new CommandException("Faction not found: %s", name);
		return match;
	}

	protected List<String> getFactionCompletions(String[] args, int offset, List<OwnerIdentity> candidates)
	{
		List<String> result = new ArrayList<>();
		if(args.length <= offset)
			return result;
		String typed = String.join(" ", Arrays.copyOfRange(args, offset, args.length));
		int prefixLength = typed.length()-args[args.length-1].length();
		for(OwnerIdentity identity : candidates)
		{
			String name = identity.getDisplayName();
			if(name.length() >= typed.length()&&name.regionMatches(true, 0, typed, 0, typed.length()))
				result.add(name.substring(prefixLength));
			String id = identity.getStringUUID();
			if(!typed.isEmpty()&&id.regionMatches(true, 0, typed, 0, typed.length()))
				result.add(id.substring(prefixLength));
		}
		return result;
	}
}
