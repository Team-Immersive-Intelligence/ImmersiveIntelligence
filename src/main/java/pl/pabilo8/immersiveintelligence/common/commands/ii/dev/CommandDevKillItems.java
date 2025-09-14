package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevKillItems extends CommandIIBase
{
	public CommandDevKillItems(CommandTreeBase parent)
	{
		super(parent, "killitems");
	}

	@Override
	public String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Removes all items in 20 block radius";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		sender.getEntityWorld().getEntities(EntityItem.class, input -> (input!=null?input.getPositionVector().distanceTo(sender.getPositionVector()): 25) < 25f).forEach(Entity::setDead);
		sender.getEntityWorld().getEntities(EntityXPOrb.class, input -> (input!=null?input.getPositionVector().distanceTo(sender.getPositionVector()): 25) < 25f).forEach(Entity::setDead);
		sender.getEntityWorld().getEntities(EntityArrow.class, input -> (input!=null?input.getPositionVector().distanceTo(sender.getPositionVector()): 25) < 25f).forEach(Entity::setDead);
		sender.sendMessage(new net.minecraft.util.text.TextComponentString("Items Killed!"));
	}
}
