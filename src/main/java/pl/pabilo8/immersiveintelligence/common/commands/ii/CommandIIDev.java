package pl.pabilo8.immersiveintelligence.common.commands.ii;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IAdvancedFluidItem;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;
import org.apache.commons.lang3.time.StopWatch;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.ComponentEffectShape;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;
import pl.pabilo8.immersiveintelligence.common.entity.EntityParachute;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.EntityAmmoBase;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageParticleEffect;
import pl.pabilo8.immersiveintelligence.common.util.IIExplosion;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.BlacklistedRayTracer;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer;
import pl.pabilo8.immersiveintelligence.common.world.IIWorldGen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 23-06-2020
 */
public class CommandIIDev extends CommandTreeHelp
{
	public CommandIIDev(CommandTreeBase parent)
	{
		super(parent);
	}

	private static final Set<String> OPTIONS = new HashSet<>();

	static
	{
		OPTIONS.add("help");

		OPTIONS.add("slowmo");
		OPTIONS.add("decaybullets");

		OPTIONS.add("killbullets");
		OPTIONS.add("killvehicles");
		OPTIONS.add("killitems");
		OPTIONS.add("killhanses");

		OPTIONS.add("world_setup");

		OPTIONS.add("tpd");
		OPTIONS.add("test_enemies");

		OPTIONS.add("explosion");
		OPTIONS.add("nuke");
		OPTIONS.add("power");
		OPTIONS.add("tree");
		OPTIONS.add("parachute");
		OPTIONS.add("deth");
		OPTIONS.add("get_mb");
		OPTIONS.add("place_mb");

		OPTIONS.add("particle");
		OPTIONS.add("inyerface");
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
		return "Executes an Immersive Intelligence command, for more info use /ii dev help";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
	{
		if(args.length > 0)
		{
			Entity senderEntity = sender.getCommandSenderEntity();
			switch(args[0])
			{
				case "help":
				{
					sender.sendMessage(new TextComponentString("Executes an Immersive Intelligence command, usage /ii dev <option>").setStyle(new Style().setColor(TextFormatting.GOLD)));
					sender.sendMessage(getMessageForCommand("slowmo", "sets bullets slowmo speed", "<0.0-1.0>"));
					sender.sendMessage(getMessageForCommand("decaybullets", "sets time after which a projectile is forced to despawn", "<ticks>"));
					sender.sendMessage(getMessageForCommand("killbullets", "removes all bullets in 20 block radius"));
					sender.sendMessage(getMessageForCommand("killvehicles", "removes all vehicles (II multipart entities) in 20 block radius"));
					sender.sendMessage(getMessageForCommand("killitems", "removes all items in 20 block radius"));
					sender.sendMessage(getMessageForCommand("killhanses", "removes all ze Hanses in 20 block radius"));
					sender.sendMessage(getMessageForCommand("world_setup", "disables day and night and weather cycles, disables mob spawning"));
					sender.sendMessage(getMessageForCommand("tpd", "teleports the player to a dimension", "<dim>"));
					sender.sendMessage(getMessageForCommand("test_enemies", "spawns enemies", "<amount>"));
					sender.sendMessage(getMessageForCommand("explosion", "spawns an II explosion", "<size>"));
					sender.sendMessage(getMessageForCommand("nuke", "plants a seed on ground zero"));
					sender.sendMessage(getMessageForCommand("power", "charges held item or looked entity with IF, absolutely free"));
					sender.sendMessage(getMessageForCommand("tree", "creates a happy little [R E B B U R] tree"));
					sender.sendMessage(getMessageForCommand("parachute", "spawns and mounts the command user on a parachute"));
					sender.sendMessage(getMessageForCommand("deth", "removes the entity player is looking at"));
					sender.sendMessage(getMessageForCommand("get_mb", "gets the internal data of a multiblock player is looking at"));
					sender.sendMessage(getMessageForCommand("place_mb", "places a multiblock", "<id>"));
					sender.sendMessage(getMessageForCommand("particle", "spawns a particle", "<id> <x> <y> <z> [motionX] [motionY] [motionZ]"));
					sender.sendMessage(getMessageForCommand("inyerface", "throw a stone, will 'ye?!"));

				}
				break;
				case "deth":
				{
					if(senderEntity==null)
						return;
					float blockReachDistance = 100f;
					Vec3d vec3d = senderEntity.getPositionEyes(0);
					Vec3d vec3d1 = senderEntity.getLook(0);
					Vec3d vec3d2 = vec3d.addVector(vec3d1.x*blockReachDistance, vec3d1.y*blockReachDistance, vec3d1.z*blockReachDistance);

					MultipleRayTracer rayTracer = MultipleRayTracer.volumetricTrace(sender.getEntityWorld(), vec3d, vec3d2, new AxisAlignedBB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5), Collections.emptyList(), true, Collections.singletonList(senderEntity), null);
					for(RayTraceResult hit : rayTracer)
						if(hit.typeOfHit==Type.ENTITY)
						{
							hit.entityHit.setDead();
							sender.sendMessage(new TextComponentString(hit.entityHit.getDisplayName().getFormattedText()+" is dead, no big surprise."));
							break;
						}

				}
				break;
				case "tree":
				{
					if(senderEntity==null)
						return;

					float blockReachDistance = 100f;
					RayTraceResult traceResult = getRayTraceResult(senderEntity, blockReachDistance);
					if(traceResult==null||traceResult.typeOfHit==Type.MISS)
						return;

					IIWorldGen.worldGenRubberTree.generate(sender.getEntityWorld(), Utils.RAND, traceResult.getBlockPos().up());
					sender.sendMessage(new TextComponentString("Adding a happy little tree :)"));

				}
				break;
				case "slowmo":
					if(args.length > 1)
					{
						sender.getEntityWorld().getGameRules().setOrCreateGameRule(IIReference.GAMERULE_AMMO_SLOWMO,
								String.valueOf((int)(100*parseDouble(args[1])))
						);
						sender.sendMessage(new TextComponentString("Bullet speed set to "+args[1]));
					}
					else
						sender.sendMessage(new TextComponentString(TextFormatting.RED+"Please enter a speed value, default 1, current "+EntityAmmoProjectile.SLOWMO));
					break;
				case "decaybullets":
					if(args.length > 1)
					{
						sender.getEntityWorld().getGameRules().setOrCreateGameRule(IIReference.GAMERULE_AMMO_DECAY,
								String.valueOf(parseInt(args[1]))
						);
						sender.sendMessage(new TextComponentString("Bullet speed set to "+args[1]));
					}
					else
						sender.sendMessage(new TextComponentString(TextFormatting.RED+"Please enter a speed value, default 1, current "+EntityAmmoProjectile.MAX_TICKS));
					break;
				case "killbullets":
					sender.getEntityWorld().getEntities(EntityAmmoBase.class, input -> true).forEach(Entity::setDead);
					sender.sendMessage(new TextComponentString("All bullets killed!"));
					break;
				case "killitems":
					sender.getEntityWorld().getEntities(EntityItem.class, input -> (input!=null?input.getPositionVector().distanceTo(sender.getPositionVector()): 25) < 25f).forEach(Entity::setDead);
					sender.getEntityWorld().getEntities(EntityXPOrb.class, input -> (input!=null?input.getPositionVector().distanceTo(sender.getPositionVector()): 25) < 25f).forEach(Entity::setDead);
					sender.getEntityWorld().getEntities(EntityArrow.class, input -> (input!=null?input.getPositionVector().distanceTo(sender.getPositionVector()): 25) < 25f).forEach(Entity::setDead);
					sender.sendMessage(new TextComponentString("Items Killed!"));
					break;
				case "killvehicles":
					sender.getEntityWorld().getEntities(Entity.class, input -> (input instanceof IVehicleMultiPart?input.getPositionVector().distanceTo(sender.getPositionVector()): 25) < 25f).forEach(Entity::setDead);
					sender.sendMessage(new TextComponentString("Vehicles Killed!"));
					break;
				case "killhanses":
					sender.getEntityWorld().getEntities(EntityHans.class, input -> (input!=null?input.getPositionVector().distanceTo(sender.getPositionVector()): 25) < 25f).forEach(Entity::setDead);
					sender.sendMessage(new TextComponentString("All Hanses killed :("));
					break;
				case "world_setup":
					//Set weather rules
					server.getEntityWorld().getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
					server.getEntityWorld().getGameRules().setOrCreateGameRule("doWeatherCycle", "false");
					server.getEntityWorld().getGameRules().setOrCreateGameRule("doMobSpawning", "false");
					//Add the hostile Hans team
					if(server.getEntityWorld().getScoreboard().getTeam("GlobalEnemy")==null)
					{
						ScorePlayerTeam globalEnemy = server.getEntityWorld().getScoreboard().createTeam("GlobalEnemy");
						globalEnemy.setColor(TextFormatting.DARK_GRAY);
						globalEnemy.setDisplayName("Hostile Forces");
						globalEnemy.setPrefix("Enemy");
					}
					sender.sendMessage(new TextComponentString("World setup done!"));
					break;
				case "tpd":
					if(args.length > 1&&senderEntity!=null)
					{
						senderEntity.changeDimension(Integer.parseInt(args[1]), new IITeleporter());
						sender.sendMessage(new TextComponentString("Preparing to jump!"));
					}
					break;
				case "vtrace":
				{
					float blockReachDistance = 6;
					Vec3d vec3d = senderEntity.getPositionEyes(0);
					Vec3d vec3d1 = senderEntity.getLook(0);
					Vec3d vec3d2 = vec3d.addVector(vec3d1.x*blockReachDistance, vec3d1.y*blockReachDistance, vec3d1.z*blockReachDistance);

					//Blacklisted tracer
					StopWatch watch = new StopWatch();
					watch.start();
					BlacklistedRayTracer.traceIgnoringBlocks(senderEntity.world, vec3d, vec3d2, Collections.emptyList(), 0);
					watch.stop();
					sender.sendMessage(new TextComponentString("Time: "+watch.getNanoTime()+" ns"));

					//Vanilla tracer
					watch.reset();
					watch.start();
					senderEntity.world.rayTraceBlocks(vec3d, vec3d2);
					watch.stop();
					sender.sendMessage(new TextComponentString("Time: "+watch.getNanoTime()+" ns"));

					//Volumetric tracer
					watch.reset();
					watch.start();
					MultipleRayTracer.volumetricTrace(sender.getEntityWorld(), vec3d, vec3d2,
							new AxisAlignedBB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5), Collections.emptyList(), false, Collections.singletonList(senderEntity), null);
					watch.stop();
					sender.sendMessage(new TextComponentString("Time: "+watch.getNanoTime()+" ns"));

				}
				break;
				case "power":
				{
					if(senderEntity==null)
						return;

					float blockReachDistance = 6;
					Vec3d vec3d = senderEntity.getPositionEyes(0);
					Vec3d vec3d1 = senderEntity.getLook(0);
					Vec3d vec3d2 = vec3d.addVector(vec3d1.x*blockReachDistance, vec3d1.y*blockReachDistance, vec3d1.z*blockReachDistance);
					MultipleRayTracer rayTracer = MultipleRayTracer.volumetricTrace(sender.getEntityWorld(), vec3d, vec3d2, new AxisAlignedBB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5), Collections.emptyList(), true, Collections.singletonList(senderEntity), null);
					if(!rayTracer.getHits().isEmpty())
					{
						Entity entityHit = rayTracer.getHits().get(0).entityHit;
						if(entityHit!=null&&entityHit.hasCapability(CapabilityEnergy.ENERGY, null))
						{
							IEnergyStorage capability = entityHit.getCapability(CapabilityEnergy.ENERGY, null);
							if(capability!=null)
							{
								while(capability.getEnergyStored()!=capability.getMaxEnergyStored())
									capability.receiveEnergy(Integer.MAX_VALUE, false);
								return;
							}
						}
					}

					senderEntity.getHeldEquipment().forEach(stack -> {
						if(stack.getItem() instanceof IAdvancedFluidItem)
						{
							IAdvancedFluidItem item = (IAdvancedFluidItem)stack.getItem();
							for(Fluid f : FluidRegistry.getRegisteredFluids().values())
							{
								FluidStack fluidStack = new FluidStack(f, 10000000);
								if(item.allowFluid(stack, fluidStack))
								{
									IFluidHandler capability = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
									if(capability!=null)
									{
										capability.fill(fluidStack, true);
										break;
									}
								}

							}
						}
						if(stack.hasCapability(CapabilityEnergy.ENERGY, null))
							stack.getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(Integer.MAX_VALUE, false);
					});
				}
				break;
				case "explosion":
				case "nuke":
				{
					if(senderEntity==null||server.getEntityWorld().isRemote)
						return;

					float blockReachDistance = 100f;
					RayTraceResult traceResult = getRayTraceResult(senderEntity, blockReachDistance);
					if(traceResult==null||traceResult.typeOfHit==Type.MISS)
						return;

					BlockPos pos = traceResult.getBlockPos();

					if(args[0].equals("nuke"))
					{
						ItemStack s2 = IIContent.itemAmmoHeavyArtillery.getAmmoStack(IIContent.ammoCoreBrass, CoreType.CANISTER, FuseType.CONTACT, IIContent.ammoComponentNuke);
						new AmmoFactory<>(senderEntity.getEntityWorld())
								.setStack(s2)
								.setPositionAndVelocity(new Vec3d(pos).addVector(0, 2, 0), new Vec3d(0, -1, 0), 1)
								.create();
						return;
					}
					int num = 0;
					try
					{
						num = Math.abs(Integer.parseInt(args[1]));
					} catch(Exception ignored)
					{

					}
					IIExplosion exp = new IIExplosion(server.getEntityWorld(), senderEntity, new Vec3d(pos), null, num, 7f, ComponentEffectShape.STAR, false, true, false);
					exp.doExplosionA();
					exp.doExplosionB(true);
				}
				break;
				case "test_enemies":
				{
					if(senderEntity==null||server.getEntityWorld().isRemote)
						return;

					float blockReachDistance = 100f;
					RayTraceResult traceResult = getRayTraceResult(senderEntity, blockReachDistance);
					if(traceResult==null||traceResult.typeOfHit==Type.MISS)
						return;

					Vec3d pos = new Vec3d(traceResult.getBlockPos().up());

					int num = 0;
					try
					{
						num = Math.abs(Integer.parseInt(args[1]));
					} catch(Exception ignored)
					{

					}
					if(!server.getEntityWorld().isRemote)
					{
						World world = senderEntity.getEntityWorld();
						final int row = (int)Math.floor(Math.sqrt(num));
						final int roff = (int)Math.floor(row/2f);
						int i = 0, missed = 0;

						while(i < num)
						{
							int c = i+missed;
							//Vec3d offset = pos.add(new Vec3d(EnumFacing.getHorizontal(i).getDirectionVec()).scale(parachute?3f:1f).scale(1+(Math.floor(i/4f))));
							Vec3d offset = pos
									.addVector(-roff, 0, -roff)
									.add(new Vec3d(Math.floor(c/(float)row), 0, (c%row))
									);

							if(world.getBlockState(new BlockPos(offset)).causesSuffocation())
							{
								missed += 1;
								continue;
							}

							EntitySkeleton z1 = new EntitySkeleton(senderEntity.getEntityWorld());
							z1.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BOW));
							//z1.setArmsRaised(false);
							z1.setAIMoveSpeed(0.125f);
							z1.setPosition(offset.x+0.5f, offset.y, offset.z+0.5f);
							z1.setCustomNameTag("Zombie #"+i);
							z1.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(IIContent.itemLightEngineerHelmet));
							senderEntity.getEntityWorld().spawnEntity(z1);

							i++;
						}

						sender.sendMessage(new TextComponentString("Test enemies summoned!"));
					}

				}
				break;
				case "parachute":
				{
					if(senderEntity!=null)
					{
						EntityParachute para = new EntityParachute(senderEntity.getEntityWorld());
						para.setPosition(senderEntity.posX, senderEntity.posY, senderEntity.posZ);
						senderEntity.getEntityWorld().spawnEntity(para);
						senderEntity.startRiding(para);
					}
				}
				break;
				case "get_mb":
				{
					RayTraceResult traceResult = getRayTraceResult(senderEntity, 40f);
					if(traceResult==null||traceResult.typeOfHit==Type.MISS)
						return;
					TileEntity te = senderEntity.getEntityWorld().getTileEntity(traceResult.getBlockPos());

					if(te instanceof TileEntityMultiblockPart<?>)
						senderEntity.sendMessage(
								new TextComponentString(TextFormatting.GOLD+"ID: "+TextFormatting.RESET+((TileEntityMultiblockPart<?>)te).pos+" | ")
										.appendSibling(new TextComponentString(TextFormatting.GOLD+"Mirrored: "+TextFormatting.RESET+((TileEntityMultiblockPart<?>)te).mirrored+" | "))
										.appendSibling(new TextComponentString(TextFormatting.GOLD+"Facing: "+TextFormatting.RESET+((TileEntityMultiblockPart<?>)te).facing.name()))
						);
				}
				break;
				case "place_mb":
				{
					RayTraceResult traceResult = getRayTraceResult(senderEntity, 40f);
					if(traceResult==null||traceResult.typeOfHit==Type.MISS)
						return;

					for(IMultiblock mb : MultiblockHandler.getMultiblocks())
					{
						if(mb.getUniqueName().equals(args[1]))
						{
							BlockPos placed = traceResult.getBlockPos().up();

							ItemStack[][][] manual = mb.getStructureManual();
							int hh = manual.length, ww = manual[0][0].length, ll = manual[0].length;

							for(int y = 0; y < hh; y++)
								for(int z = 0; z < ll; z++)
									for(int x = 0; x < ww; x++)
									{
										ItemStack stack = manual[y][z][x];
										if(stack==null||stack.isEmpty())
											continue;
										IBlockState state = mb.getBlockstateFromStack((y*ww*ll)+(z*ww)+x, stack);

										if(state!=null)
											senderEntity.world.setBlockState(placed.add(x, y, z), state);

									}
							break;
						}
					}

				}
				break;
				case "inyerface":
					execute(server, sender, new String[]{"particle", "debris_big_brick", "~", "~", "~", "1", "0", "0"});
					break;
				case "particle":
				{
					//No ID specified
					if(args.length < 2)
						throw new WrongUsageException(getUsage(sender));

					//Set position, else use the sender's position
					Vec3d pos = new Vec3d(args.length >= 5?parseBlockPos(sender, args, 2, true): sender.getPosition());
					Vec3d motion = Vec3d.ZERO;

					//Set motion
					if(args.length >= 8)
					{
						double motionX = parseCoordinate(0, args[5], -1, 1, false).getResult();
						double motionY = parseCoordinate(0, args[6], -1, 1, false).getResult();
						double motionZ = parseCoordinate(0, args[7], -1, 1, false).getResult();
						motion = new Vec3d(motionX, motionY, motionZ);
					}

					//Send the packet, so clients can spawn the particle on their side
					IIPacketHandler.sendToClient(new MessageParticleEffect(args[1], sender.getEntityWorld(), pos, motion));
					sender.sendMessage(new TextComponentString(String.format("Particle %s created!", args[1])));
				}
				break;
			}

		}
		else
			throw new WrongUsageException(getUsage(sender));
	}

	/**
	 * @param entity        entity being the origin point
	 * @param traceDistance length in which blocks will be traced
	 * @return a nullable {@link RayTraceResult} of type {@link Type#BLOCK} or {@link Type#MISS}
	 */
	@Nullable
	private static RayTraceResult getRayTraceResult(@Nullable Entity entity, float traceDistance)
	{
		if(entity==null)
			return null;

		Vec3d eyesPos = entity.getPositionEyes(0);
		Vec3d lookVector = entity.getLook(0);
		Vec3d traceVector = eyesPos.addVector(lookVector.x*traceDistance, lookVector.y*traceDistance, lookVector.z*traceDistance);

		return entity.getEntityWorld().rayTraceBlocks(eyesPos, traceVector, false, false, true);
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
		switch(args.length)
		{
			case 1:
				return getListOfStringsMatchingLastWord(args, OPTIONS);
			case 2:
			{
				switch(args[0])
				{
					case "place_mb":
						return getListOfStringsMatchingLastWord(args, MultiblockHandler.getMultiblocks()
								.stream()
								.map(IMultiblock::getUniqueName)
								.map(s -> s.startsWith("II:")?(TextFormatting.GOLD+s+TextFormatting.RESET): s)
								.collect(Collectors.toList()));
					case "particle":
						return getListOfStringsMatchingLastWord(args, ParticleRegistry.getRegisteredNames());
					default:
						return Collections.emptyList();
				}
			}
			case 3:
			case 4:
			case 5:
				return getTabCompletionCoordinate(args, 3, sender.getPosition());
		}

		return super.getTabCompletions(server, sender, args, targetPos);
	}

	/**
	 * Return whether the specified command parameter index is a username parameter.
	 */
	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index==0;
	}

	public ITextComponent getMessageForCommand(String subcommand, String description)
	{
		return getMessageForCommand(subcommand, description, "");
	}

	public ITextComponent getMessageForCommand(String subcommand, String description, String arguments)
	{
		return new TextComponentString("/ii dev ").appendText(subcommand).appendText(arguments.isEmpty()?arguments: (" "+arguments))
				.setStyle(new Style().setColor(TextFormatting.GOLD).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/ii dev "+subcommand)))
				.appendSibling(new TextComponentString(" - ").appendText(description).setStyle(new Style().setColor(TextFormatting.RESET)));
	}

	private static class IITeleporter implements ITeleporter
	{
		@Override
		public void placeEntity(World world, Entity entity, float yaw)
		{
			entity.moveToBlockPosAndAngles(world.getSpawnPoint(), yaw, entity.rotationPitch);
		}
	}
}
