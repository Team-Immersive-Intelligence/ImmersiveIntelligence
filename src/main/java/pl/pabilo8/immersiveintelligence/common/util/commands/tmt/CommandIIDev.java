package pl.pabilo8.immersiveintelligence.common.util.commands.tmt;

import blusunrize.immersiveengineering.common.util.Utils;
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
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.energy.CapabilityEnergy;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.*;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;

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
		options.add("killvehicles");
		options.add("killitems");
		options.add("world_setup");
		options.add("decaybullets");
		options.add("test_enemies");
		options.add("hans");
		options.add("explosion");
		options.add("nuke");
		options.add("power");
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
				case "killvehicles":
					server.getEntityWorld().getEntities(Entity.class, input -> (input instanceof IVehicleMultiPart?input.getPositionVector().distanceTo(sender.getPositionVector()): 25) < 25f).forEach(Entity::setDead);
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
				case "power":
					sender.getCommandSenderEntity().getHeldEquipment().forEach(stack -> {
						if(stack.hasCapability(CapabilityEnergy.ENERGY, null))
							stack.getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(Integer.MAX_VALUE, false);
					});
					break;
				case "explosion":
				case "nuke":
				{
					Entity commandSenderEntity = sender.getCommandSenderEntity();
					if(commandSenderEntity==null||server.getEntityWorld().isRemote)
						return;

					float blockReachDistance = 100f;
					Vec3d vec3d = commandSenderEntity.getPositionEyes(0);
					Vec3d vec3d1 = commandSenderEntity.getLook(0);
					Vec3d vec3d2 = vec3d.addVector(vec3d1.x*blockReachDistance, vec3d1.y*blockReachDistance, vec3d1.z*blockReachDistance);
					RayTraceResult traceResult = commandSenderEntity.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
					if(traceResult==null||traceResult.typeOfHit==Type.MISS)
						return;

					BlockPos pos = traceResult.getBlockPos();

					if(args[0].equals("nuke"))
					{
						EntityAtomicBoom entityAtomicBoom = new EntityAtomicBoom(server.getEntityWorld(), 0.65f);
						entityAtomicBoom.setPosition(pos.getX(), pos.getY(), pos.getZ());
						server.getEntityWorld().spawnEntity(entityAtomicBoom);
						return;
					}
					int num = 0;
					try
					{
						num = Math.abs(Integer.parseInt(args[1]));
					} catch(Exception ignored)
					{

					}
					IIExplosion exp = new IIExplosion(server.getEntityWorld(), commandSenderEntity, pos.getX(), pos.getY()+1, pos.getZ(), num, 1f, false, true);
					exp.doExplosionA();
					exp.doExplosionB(true);
				}
				break;
				case "test_enemies":
				case "hans":
				{
					Entity commandSenderEntity = sender.getCommandSenderEntity();
					if(commandSenderEntity==null||server.getEntityWorld().isRemote)
						return;

					float blockReachDistance = 100f;
					Vec3d vec3d = commandSenderEntity.getPositionEyes(0);
					Vec3d vec3d1 = commandSenderEntity.getLook(0);
					Vec3d vec3d2 = vec3d.addVector(vec3d1.x*blockReachDistance, vec3d1.y*blockReachDistance, vec3d1.z*blockReachDistance);
					RayTraceResult traceResult = commandSenderEntity.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
					if(traceResult==null||traceResult.typeOfHit==Type.MISS)
						return;

					BlockPos position = traceResult.getBlockPos().up();

					int num = 0;
					try
					{
						num = Math.abs(Integer.parseInt(args[1]));
					} catch(Exception ignored)
					{

					}
					if(!server.getEntityWorld().isRemote)
					{
						if(args[0].equals("hans"))
						{
							String[] hansLines = {
									"Hans ist Einsatzbereit!",
									"Ein neues Hans is Bereit zum Kampf",
									"Hans - bereit zum Einsatz",
									"Hans wartet auf deinem Befehle",
									"Hans ist Kampfbereit",
									"Wir haben ein neues Hans!",
									"Der Hans ist bereit!",
									"Hans - bereit zum Angriff!",
									"Hans - bereit zum Apel!",
									"Ein neues Hans ist Kriegsbereit!"
							};

							commandSenderEntity.sendMessage(new TextComponentString(
									hansLines[(int)((hansLines.length-1)*Utils.RAND.nextDouble())]
							));
							if(num==2)
							{
								EntityHans hans = new EntityHans(server.getEntityWorld());
								hans.setPosition(position.getX()+0.5, position.getY(), position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(hans);

								ItemStack mgstack = new ItemStack(IIContent.itemMachinegun);
								EntityMachinegun mg = new EntityMachinegun(server.getEntityWorld(), position.down(), commandSenderEntity.getMirroredYaw(Mirror.FRONT_BACK), -commandSenderEntity.rotationPitch, mgstack);
								server.getEntityWorld().spawnEntity(mg);
								hans.startRiding(mg);
							}
							else if(num==3)
							{
								EntityFieldHowitzer howi = new EntityFieldHowitzer(server.getEntityWorld());
								howi.setPositionAndRotation(position.getX()+0.5, position.getY(), position.getZ()+0.5, commandSenderEntity.getMirroredYaw(Mirror.FRONT_BACK), -commandSenderEntity.rotationPitch);
								server.getEntityWorld().spawnEntity(howi);

								EntityHans hans1 = new EntityHans(server.getEntityWorld());
								hans1.equipItems(num);
								hans1.setPosition(position.getX()+0.5, position.getY()+4, position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(hans1);

								EntityHans hans2 = new EntityHans(server.getEntityWorld());
								hans2.equipItems(num);
								hans2.setPosition(position.getX()+0.5, position.getY()+2, position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(hans2);

								hans1.startRiding(EntityVehicleSeat.getOrCreateSeat(howi, 0));
								hans2.startRiding(EntityVehicleSeat.getOrCreateSeat(howi, 1));
							}
							else if(num > 2&&num < 7)
							{
								EntityMotorbike motorbike = new EntityMotorbike(server.getEntityWorld());
								motorbike.setPositionAndRotation(position.getX()+0.5, position.getY(), position.getZ()+0.5, commandSenderEntity.getMirroredYaw(Mirror.FRONT_BACK), -commandSenderEntity.rotationPitch);
								motorbike.setUpgrade(num==4?"seat": (num==5?"storage": "tank"));
								server.getEntityWorld().spawnEntity(motorbike);

								EntityHans hans1 = new EntityHans(server.getEntityWorld());
								hans1.equipItems(2);
								hans1.setPosition(position.getX()+0.5, position.getY()+4, position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(hans1);
								hans1.startRiding(EntityVehicleSeat.getOrCreateSeat(motorbike, 0));

								if(num==4)
								{
									EntityHans hans2 = new EntityHans(server.getEntityWorld());
									hans2.equipItems(1);
									hans2.setPosition(position.getX()+0.5, position.getY()+2, position.getZ()+0.5);
									server.getEntityWorld().spawnEntity(hans2);
									hans2.startRiding(EntityVehicleSeat.getOrCreateSeat(motorbike, 1));
								}
							}
							else if(num==9)
							{
								EntityHans hans = new EntityHans(server.getEntityWorld());
								hans.setPosition(position.getX()+0.5, position.getY()+2, position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(hans);

								EntityTripodPeriscope tripodPeriscope = new EntityTripodPeriscope(server.getEntityWorld());
								tripodPeriscope.setPosition(position.getX()+0.5, position.getY(), position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(tripodPeriscope);
								hans.startRiding(tripodPeriscope);
							}
							else
							{
								EntityHans hans = new EntityHans(server.getEntityWorld());
								hans.equipItems(num);
								hans.setPosition(position.getX()+0.5, position.getY(), position.getZ()+0.5);
								server.getEntityWorld().spawnEntity(hans);
							}
						}
						else
						{
							for(int i = 0; i < num; i++)
							{
								EntityZombie z1 = new EntityZombie(server.getEntityWorld());
								z1.setArmsRaised(false);
								z1.setAIMoveSpeed(0.125f);
								z1.setPosition(position.getX(), position.getY(), position.getZ());
								z1.setCustomNameTag("Zombie #"+i);
								z1.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(IIContent.itemLightEngineerHelmet));
								server.getEntityWorld().spawnEntity(z1);
							}
						}
					}
					break;
				}
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
