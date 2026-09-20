package pl.pabilo8.immersiveintelligence.common.commands.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

/**
 * Kills all {@link EntityAmmoBase bullets} in 20 blocks radius from the sender.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 14.09.2025
 */
public class CommandDevKillBullets extends CommandIIBase
{
	public CommandDevKillBullets(CommandTreeBase parent)
	{
		super(parent, "killbullets");
	}

	@Override
	public String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Removes all bullets in 20 block radius";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		sender.getEntityWorld().getEntities(EntityAmmoBase.class, input -> true).forEach(Entity::setDead);
		sender.sendMessage(new TextComponentString("All bullets killed!"));
	}
}
