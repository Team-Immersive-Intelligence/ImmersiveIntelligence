package pl.pabilo8.immersiveintelligence.common.commands.ii.dev;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.CommandIIBase;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 14.09.2025
 */
public class CommandDevTestEnemies extends CommandIIBase
{
	public CommandDevTestEnemies(CommandTreeBase parent)
	{
		super(parent, "test_enemies");
	}

	@Override
	public String getSyntax()
	{
		return "[amount]";
	}

	@Override
	public String getDescription(ICommandSender sender)
	{
		return "Spawns enemies";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Entity senderEntity = sender.getCommandSenderEntity();
		if(senderEntity==null||server.getEntityWorld().isRemote) return;
		float blockReachDistance = 100f;
		RayTraceResult traceResult = CommandIIDev.getRayTraceResult(senderEntity, blockReachDistance);
		if(traceResult==null||traceResult.typeOfHit==RayTraceResult.Type.MISS) return;
		Vec3d pos = new Vec3d(traceResult.getBlockPos().up());
		int num = 1;
		try {num = Math.abs(Integer.parseInt(args[0]));} catch(Exception ignored) {}
		if(!server.getEntityWorld().isRemote)
		{
			final int row = (int)Math.floor(Math.sqrt(num));
			final int roff = (int)Math.floor(row/2f);
			int i = 0, missed = 0;
			while(i < num)
			{
				int c = i+missed;
				Vec3d offset = pos.addVector(-roff, 0, -roff).add(new Vec3d(Math.floor(c/(float)row), 0, c%row));
				if(server.getEntityWorld().getBlockState(new BlockPos(offset)).causesSuffocation())
				{
					missed += 1;
					continue;
				}
				EntitySkeleton skeleton = new EntitySkeleton(senderEntity.getEntityWorld());
				skeleton.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BOW));
				skeleton.setAIMoveSpeed(0.125f);
				skeleton.setPosition(offset.x+0.5f, offset.y, offset.z+0.5f);
				skeleton.setCustomNameTag("Skeleton #"+i);
				skeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(IIContent.itemLightEngineerHelmet));
				senderEntity.getEntityWorld().spawnEntity(skeleton);
				i++;
			}
			sender.sendMessage(new TextComponentString("Test enemies summoned!"));
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if(args.length==1)
			return Arrays.asList("1", "5", "10", "20", "50");
		return Collections.emptyList();
	}
}
