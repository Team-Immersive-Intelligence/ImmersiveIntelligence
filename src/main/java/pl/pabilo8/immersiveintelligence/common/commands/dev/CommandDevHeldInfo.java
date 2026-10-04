package pl.pabilo8.immersiveintelligence.common.commands.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemUtils;

/**
 * Displays information about currently held item.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 14.09.2025
 */
public class CommandDevHeldInfo extends CommandIIBase
{
	public CommandDevHeldInfo(CommandTreeBase parent)
	{
		super(parent, "held_info");
	}

	@Override
	public String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Shows information about currently held item";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(sender==null)
			throw new CommandException("This command requires a sender.");
		Entity entity = sender.getCommandSenderEntity();
		if(!(entity instanceof EntityLivingBase livingBase))
			throw new CommandException("No command sender entity found.");

		ItemStack stack = livingBase.getHeldItem(EnumHand.MAIN_HAND);
		sender.sendMessage(new TextComponentString(IIItemUtils.getUniqueStackString(stack)));
	}
}
