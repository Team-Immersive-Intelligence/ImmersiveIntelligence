package pl.pabilo8.immersiveintelligence.common.commands.faction.set;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;

public class CommandFactionSetColor extends CommandIIBase
{
	public CommandFactionSetColor(CommandTreeBase parent)
	{
		super(parent, "color");
	}

	@Override
	public String getSyntax()
	{
		return "<dyeColor|hex>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Set your faction's color (e.g. red, #FF0000)";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(!(sender instanceof EntityPlayer))
			throw new CommandException("Player only.");

		if(args.length < 1)
			throw new CommandException("Specify a color.");

		DiplomacyHandler diplomacy = DiplomacyHandler.getInstance(false);
		OwnerIdentity faction = diplomacy.getOwnerIdentityForEntity((EntityPlayer)sender);
		if(faction.isInvalid()||!faction.isOwner(((EntityPlayer)sender).getUniqueID()))
			throw new CommandException("You must be an owner.");

		TextFormatting color = TextFormatting.getValueByName(args[0]);
		if(color==null)
			throw new CommandException("Unknown color: "+args[0]);

		faction.withColor(IIColor.fromTextFormatting(color));
		diplomacy.saveAndSyncIdentity(faction);
		sender.sendMessage(new TextComponentString("Color set."));
	}
}
