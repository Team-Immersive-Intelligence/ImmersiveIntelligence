package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevSlowmo extends CommandIIBase
{
	public CommandDevSlowmo(CommandTreeBase parent)
	{
		super(parent, "slowmo");
	}

	@Override
	public String getSyntax()
	{
		return "<0.0-1.0>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Sets bullets slowmo speed";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length > 0)
		{
			sender.getEntityWorld().getGameRules().setOrCreateGameRule(IIReference.GAMERULE_AMMO_SLOWMO, String.valueOf((int)(100*Double.parseDouble(args[0]))));
			sender.sendMessage(new TextComponentString("Bullet speed set to "+args[0]));
		}
		else
			sender.sendMessage(new TextComponentString(TextFormatting.RED+"Please enter a speed value, default 1, current "+EntityAmmoProjectile.SLOWMO));
	}
}
