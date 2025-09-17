package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevDecayBullets extends CommandIIBase
{
	public CommandDevDecayBullets(CommandTreeBase parent)
	{
		super(parent, "decaybullets");
	}

	@Override
	public String getSyntax()
	{
		return "<ticks>";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Sets time after which a projectile is forced to despawn";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length > 0)
		{
			sender.getEntityWorld().getGameRules().setOrCreateGameRule(IIReference.GAMERULE_AMMO_DECAY,
					String.valueOf(Integer.parseInt(args[0])));
			sender.sendMessage(new TextComponentString("Bullet decay set to "+args[0]));
		}
		else
			sender.sendMessage(new TextComponentString(TextFormatting.RED+"Please enter a tick value, default 1, current "+EntityAmmoProjectile.MAX_TICKS));
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if(args.length==1)
			return Arrays.asList("20", "100", "200", "600", "1200");
		return Collections.emptyList();
	}
}
