package pl.pabilo8.immersiveintelligence.common.commands.faction.set;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

public class CommandFactionSetBanner extends CommandIIBase
{
	public CommandFactionSetBanner(CommandTreeBase parent)
	{
		super(parent, "banner");
	}

	@Override
	public String getSyntax()
	{
		return "";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Set your faction's banner to the banner you are holding";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(!(sender instanceof EntityPlayer))
			throw new CommandException("Player only.");

		EntityPlayer player = (EntityPlayer)sender;
		ItemStack held = player.getHeldItemMainhand();
		if(held.isEmpty())
			throw new CommandException("You must hold a banner.");

		DiplomacyHandler diplomacy = DiplomacyHandler.getInstance(false);
		OwnerIdentity faction = diplomacy.getOwnerIdentityForEntity(player);
		if(faction.isInvalid()||!faction.isOwner(player.getUniqueID()))
			throw new CommandException("You must be an owner.");

		faction.withBanner(held.copy());
		diplomacy.saveAndSyncIdentity(faction);
		sender.sendMessage(new TextComponentString("Banner updated."));
	}
}
