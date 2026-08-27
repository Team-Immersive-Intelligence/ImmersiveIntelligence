package pl.pabilo8.immersiveintelligence.common.commands.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer;

import java.util.Collections;

/**
 * Removes the entity that the sender is looking at.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 14.09.2025
 */
public class CommandDevDeth extends CommandIIBase
{
	public CommandDevDeth(CommandTreeBase parent)
	{
		super(parent, "deth");
	}

	@Override
	public String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Removes the entity player is looking at";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity senderEntity = sender.getCommandSenderEntity();
		if(senderEntity==null) return;
		float blockReachDistance = 100f;
		Vec3d vec3d = senderEntity.getPositionEyes(0);
		Vec3d vec3d1 = senderEntity.getLook(0);
		Vec3d vec3d2 = vec3d.addVector(vec3d1.x*blockReachDistance, vec3d1.y*blockReachDistance, vec3d1.z*blockReachDistance);
		MultipleRayTracer rayTracer = MultipleRayTracer.volumetricTrace(sender.getEntityWorld(), vec3d, vec3d2, new AxisAlignedBB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5), Collections.emptyList(), true, Collections.singletonList(senderEntity), null);
		for(RayTraceResult hit : rayTracer)
		{
			if(hit.typeOfHit==RayTraceResult.Type.ENTITY)
			{
				hit.entityHit.setDead();
				sender.sendMessage(new TextComponentString(hit.entityHit.getDisplayName().getFormattedText()+" is dead, no big surprise.").setStyle(new Style().setColor(TextFormatting.RED)));
				break;
			}
		}
	}
}
