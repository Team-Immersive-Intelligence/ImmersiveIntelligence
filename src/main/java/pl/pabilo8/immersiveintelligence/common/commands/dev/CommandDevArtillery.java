package pl.pabilo8.immersiveintelligence.common.commands.dev;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.commands.item.CommandAmmoUtils;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Creates a configurable artillery barrage at the sender's look position.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 26.08.2026
 * @ii-approved 0.3.1
 * @since 14.09.2025
 */
public class CommandDevArtillery extends CommandIIBase
{
	private static final float BLOCK_REACH_DISTANCE = 100f;
	private static final CoreType DEFAULT_CORE_TYPE = CoreType.CANISTER;
	private static final FuseType DEFAULT_FUSE_TYPE = FuseType.CONTACT;

	public CommandDevArtillery(CommandTreeBase parent)
	{
		super(parent, "artillery");
	}

	@Override
	public String getSyntax()
	{
		return "[component] [ammunition type]";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Creates an artillery barrage at the look position";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if(args.length > 2)
			throw new WrongUsageException(getUsage(sender));

		Entity senderEntity = sender.getCommandSenderEntity();
		if(senderEntity==null)
			throw new CommandException("The artillery command must be run by an entity.");
		if(senderEntity.getEntityWorld().isRemote)
			return;

		AmmoComponent component = args.length >= 1?CommandAmmoUtils.resolveComponent(args[0]): IIContent.ammoComponentRDX;
		IAmmoTypeItem<?, ?> ammoType = args.length >= 2?CommandAmmoUtils.resolveAmmoType(args[1]): IIContent.itemAmmoHeavyArtillery;
		if(!CommandAmmoUtils.supports(ammoType, DEFAULT_CORE_TYPE, DEFAULT_FUSE_TYPE, null))
			throw new CommandException("Ammunition type '%s' does not support the %s core type and %s fuse required by this barrage.",
					ammoType.getName(), DEFAULT_CORE_TYPE.getName(), DEFAULT_FUSE_TYPE.getName());
		CommandAmmoUtils.validateComponent(ammoType, DEFAULT_CORE_TYPE, component, 0);

		RayTraceResult traceResult = CommandIIDev.getRayTraceResult(senderEntity, BLOCK_REACH_DISTANCE);
		if(traceResult==null||traceResult.typeOfHit!=RayTraceResult.Type.BLOCK||traceResult.getBlockPos()==null)
			throw new CommandException("No block is targeted within %d blocks.", (int)BLOCK_REACH_DISTANCE);

		BlockPos pos = traceResult.getBlockPos();
		ItemStack ammunition = ammoType.getAmmoStack(IIContent.ammoCoreBrass, DEFAULT_CORE_TYPE, DEFAULT_FUSE_TYPE, component);
		if(ammunition.isEmpty())
			throw new CommandException("Ammunition type '%s' did not create a valid item.", ammoType.getName());

		AmmoFactory<?> factory = new AmmoFactory<>(senderEntity.getEntityWorld())
				.setStack(ammunition)
				.setDirection(new Vec3d(0, -1, 0))
				.setVelocityModifier(0.25f);
		for(int i = 0; i < 40; i++)
			factory.setPosition(new Vec3d(pos).addVector(0, 40+i*20, 0).addVector(Utils.RAND.nextGaussian()*10, 0, Utils.RAND.nextGaussian()*10)).create();
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		if(args.length==1)
			return getListOfStringsMatchingLastWord(args,
					CommandAmmoUtils.getValidComponentNames(IIContent.itemAmmoHeavyArtillery, DEFAULT_CORE_TYPE, new String[0]));
		if(args.length==2)
		{
			AmmoComponent component = AmmoRegistry.getComponent(args[0]);
			if(component==null)
				return Collections.emptyList();
			return getListOfStringsMatchingLastWord(args, AmmoRegistry.getAllAmmoItems().stream()
					.filter(ammoType -> CommandAmmoUtils.supports(ammoType, DEFAULT_CORE_TYPE, DEFAULT_FUSE_TYPE, component))
					.map(IAmmoTypeItem::getName)
					.collect(Collectors.toList()));
		}
		return Collections.emptyList();
	}
}
