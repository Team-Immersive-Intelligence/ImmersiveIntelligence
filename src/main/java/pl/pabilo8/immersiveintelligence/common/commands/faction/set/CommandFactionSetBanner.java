package pl.pabilo8.immersiveintelligence.common.commands.faction.set;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.commands.faction.CommandFactionBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.permission.PermissionCategory;

/**
 * Copy the banner in your main hand to your faction.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.10.2026
 */
public class CommandFactionSetBanner extends CommandFactionBase
{
	public CommandFactionSetBanner(CommandTreeBase parent)
	{
		super(parent, "banner", PermissionCategory.MODIFY_INSIGNIA);
	}

	@Override
	public String getSyntax()
	{
		return "";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Copy the banner in your main hand to your faction";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		requireArgumentCount(sender, args, 0);
		OwnerIdentity faction = getFaction(sender);
		ItemStack held = getPlayer(sender).getHeldItemMainhand();
		if(held.isEmpty()||!(held.getItem() instanceof ItemBanner))
			throw new CommandException("You must hold a banner in your main hand.");
		ItemStack banner = held.copy();
		banner.setCount(1);
		faction.withBanner(banner);
		DiplomacyHandler.getInstance(false).saveAndSyncIdentity(faction);
		sender.sendMessage(new TextComponentString("Banner updated."));
	}
}
