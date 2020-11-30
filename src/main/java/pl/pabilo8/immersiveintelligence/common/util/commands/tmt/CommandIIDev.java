package pl.pabilo8.immersiveintelligence.common.util.commands.tmt;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 23-06-2020
 */
public class CommandIIDev extends CommandBase
{
	private static Set<String> options = new HashSet<>();

	static
	{
		options.add("slowmo");
		options.add("bulletspeed");
		options.add("killbullets");
		options.add("killitems");
		options.add("world_setup");
		options.add("decaybullets");
		options.add("test_enemies");
		//options.add("panzer");
		//options.add("fallschirm");
	}

	/**
	 * Gets the name of the command
	 */
	@Nonnull
	@Override
	public String getName()
	{
		return "dev";
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender)
	{
		return "Executes a cp,,amd, usage: ii dev <option>";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
	{
		if(args.length > 0)
		{
			switch(args[0])
			{
				case "slowmo":
					EntityBullet.DEV_SLOMO = 0.005f;
					EntityBullet.DEV_DECAY = false;
					break;
				case "bulletspeed":
					if(args.length > 1)
						EntityBullet.DEV_SLOMO = Float.parseFloat(args[1]);
					break;
				case "killbullets":
					server.getEntityWorld().getEntities(EntityBullet.class, input -> true).forEach(Entity::setDead);
					break;
				case "killitems":
					server.getEntityWorld().getEntities(EntityItem.class, input -> (input!=null?input.getPositionVector().distanceTo(sender.getPositionVector()): 25) < 25f).forEach(Entity::setDead);
					server.getEntityWorld().getEntities(EntityXPOrb.class, input -> (input!=null?input.getPositionVector().distanceTo(sender.getPositionVector()): 25) < 25f).forEach(Entity::setDead);
					break;
				case "decaybullets":
					EntityBullet.DEV_DECAY = !EntityBullet.DEV_DECAY;
					sender.sendMessage(new TextComponentString(String.valueOf(EntityBullet.DEV_DECAY)));
					break;
				case "world_setup":
					server.getEntityWorld().getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
					server.getEntityWorld().getGameRules().setOrCreateGameRule("doWeatherCycle", "false");
					server.getEntityWorld().getGameRules().setOrCreateGameRule("doMobSpawning", "false");
					break;
				case "test_enemies":
					BlockPos position = sender.getPosition();
					int num = 0;
					try
					{
						num = Math.abs(Integer.parseInt(args[1]));
					} catch(Exception ignored)
					{

					}
					if(!server.getEntityWorld().isRemote)
						for(int i = 0; i < num; i++)
						{
							EntityZombie z1 = new EntityZombie(server.getEntityWorld());
							z1.setArmsRaised(false);
							z1.setAIMoveSpeed(0.125f);
							z1.setPosition(position.getX(), position.getY(), position.getZ());
							z1.setCustomNameTag("Zombie #"+i);
							z1.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(CommonProxy.item_light_engineer_helmet));
							server.getEntityWorld().spawnEntity(z1);
						}
					break;
			}
		}
		else
			throw new WrongUsageException(getUsage(sender));
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel()
	{
		return 4;
	}

	/**
	 * Get a list of options for when the user presses the TAB key
	 */
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
	{
		return getListOfStringsMatchingLastWord(args, options);
	}

	/**
	 * Return whether the specified command parameter index is a username parameter.
	 */
	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index==0;
	}
}
