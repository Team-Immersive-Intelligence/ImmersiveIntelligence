package pl.pabilo8.immersiveintelligence.common.commands.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevNuke extends CommandIIBase
{
	public CommandDevNuke(CommandTreeBase parent)
	{
		super(parent, "nuke");
	}

	@Override
	public String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Plants a seed on ground zero";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity senderEntity = sender.getCommandSenderEntity();
		if(senderEntity==null||server.getEntityWorld().isRemote) return;
		float blockReachDistance = 100f;
		RayTraceResult traceResult = CommandIIDev.getRayTraceResult(senderEntity, blockReachDistance);
		if(traceResult==null||traceResult.typeOfHit==RayTraceResult.Type.MISS) return;
		BlockPos pos = traceResult.getBlockPos();
		ItemStack ammunition = IIContent.itemAmmoHeavyArtillery.getAmmoStack(IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentNuke);
		new AmmoFactory<>(senderEntity.getEntityWorld())
				.setStack(ammunition)
				.setPositionAndVelocity(new Vec3d(pos).addVector(0, 2, 0), new Vec3d(0, -1, 0), 1)
				.create();
	}
}
