package pl.pabilo8.immersiveintelligence.common.commands.dev;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.commands.CommandIIHelp;

/**
 * Root command tree for all Immersive Intelligence developer util commands.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandIIDev extends CommandTreeBase
{
	public CommandIIDev()
	{
		addSubcommand(new CommandDevDeth(this));
		addSubcommand(new CommandDevTree(this));
		addSubcommand(new CommandDevKillBullets(this));
		addSubcommand(new CommandDevKillVehicles(this));
		addSubcommand(new CommandDevKillItems(this));
		addSubcommand(new CommandDevKillHanses(this));
		addSubcommand(new CommandDevWorldSetup(this));
		addSubcommand(new CommandDevTpd(this));
		addSubcommand(new CommandDevTestEnemies(this));
		addSubcommand(new CommandDevArtillery(this));
		addSubcommand(new CommandDevExplosion(this));
		addSubcommand(new CommandDevNuke(this));
		addSubcommand(new CommandDevPower(this));
		addSubcommand(new CommandDevGetMb(this));
		addSubcommand(new CommandDevPlaceMb(this));
		addSubcommand(new CommandDevParticle(this));
		addSubcommand(new CommandDevInyerface(this));
		addSubcommand(new CommandIIHelp(this, "dev"));
	}

	public static RayTraceResult getRayTraceResult(Entity entity, float traceDistance)
	{
		if(entity==null) return null;
		Vec3d eyesPos = entity.getPositionEyes(0);
		Vec3d lookVector = entity.getLook(0);
		Vec3d traceVector = eyesPos.addVector(lookVector.x*traceDistance, lookVector.y*traceDistance, lookVector.z*traceDistance);
		return entity.getEntityWorld().rayTraceBlocks(eyesPos, traceVector, false, false, true);
	}

	@Override
	public String getName()
	{
		return "dev";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Executes an Immersive Intelligence dev command, use /ii dev help for info";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}
}
