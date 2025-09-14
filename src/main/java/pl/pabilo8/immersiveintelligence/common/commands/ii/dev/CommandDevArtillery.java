package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import blusunrize.immersiveengineering.common.util.Utils;
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
public class CommandDevArtillery extends CommandIIBase
{
	public CommandDevArtillery(CommandTreeBase parent)
	{
		super(parent, "artillery");
	}

	@Override
	public String getSyntax()
	{
		return null;
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Creates an artillery barrage at look position";
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
		ItemStack ammunition = IIContent.itemAmmoHeavyArtillery.getAmmoStack(IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentRDX);
		AmmoFactory<?> factory = new AmmoFactory<>(senderEntity.getEntityWorld())
				.setStack(ammunition)
				.setDirection(new Vec3d(0, -1, 0))
				.setVelocityModifier(0.25f);
		for(int i = 0; i < 40; i++)
			factory.setPosition(new Vec3d(pos).addVector(0, 40+i*20, 0).addVector(Utils.RAND.nextGaussian()*10, 0, Utils.RAND.nextGaussian()*10)).create();
	}
}
