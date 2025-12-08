package pl.pabilo8.immersiveintelligence.api.rotary;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.Config.IEConfig.Machines;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.network.MessageObstructedConnection;
import mysticalmechanics.tileentity.TileEntityAxle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.MechanicalDevices;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalConnectable;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityTransmissionBox;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.MechanicalDevices.rofConversionRatio;

/**
 * Utility for Immersive Intelligence's Mechanical Power System.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author GabrielV (gabriel@iiteam.net)
 * @updated 01.08.2024
 * @updated 10.10.2025
 * @ii-approved 0.3.1
 * @since 26.12.2019
 */
public class IIRotaryUtils
{
	/**
	 * General {@link WireType} category for all motor belts
	 */
	public static final String BELT_GENERAL_CATEGORY = "MOTOR_BELT";
	/**
	 * Motor belts are fragments of cloth or rubber that are used to connect wheels
	 */
	public static final String BELT_CATEGORY = "MOTOR_BELT";
	/**
	 * Motor tracks are a series of connected metal links that allow higher torque than belts
	 */
	public static final String TRACK_CATEGORY = "TRACK";
	/**
	 * Map of blocks providing a constant torque (by their existence) to a rotary system
	 */
	public static final Map<Predicate<TileEntity>, Function<Float, Float>> TORQUE_BLOCKS = new HashMap<>();
	public static final Map<Predicate<TileEntity>, BiConsumer<TileEntityTransmissionBox, TileEntity>> TORQUE_TILES = new HashMap<>();
	private static final String ADVANCEMENT_DUMB_BELT_TRYING = "main/secret_connect_wire";
	private static final String NBT_LINKPOS = "linkingPos", NBT_TARGETTING_INFO = "targettingInfo", NBT_TRIES = "tries";

	/**
	 * @param start start of the connection
	 * @param end   end of the connection
	 * @param wire  the wire type
	 * @return whether two rotary endpoints can be connected
	 */
	public static boolean canConnect(TileEntity start, TileEntity end, WireType wire)
	{
		//Better be safe ^^
		if(!(start instanceof IRotaryConnector)||!(end instanceof IRotaryConnector))
			return false;
		if(start.getWorld()!=end.getWorld())
			return false;
		if(!(wire instanceof MotorBeltType))
			return false;

		return canConnectOnX(((TileEntity & IRotaryConnector)start), ((TileEntity & IRotaryConnector)end))||
				canConnectOnZ(((TileEntity & IRotaryConnector)start), ((TileEntity & IRotaryConnector)end));
	}

	/**
	 * Called when a player tries to connect a belt to a rotary device
	 *
	 * @param coil   the belt
	 * @param player the player
	 * @param world  the world
	 * @param pos    the position of the device
	 * @param hand   the hand holding the belt
	 * @param side   the side of the device trying to connect to
	 * @param hitX   the x position of interaction
	 * @param hitY   the y position of interaction
	 * @param hitZ   the z position of interaction
	 * @return {@link EnumActionResult#SUCCESS} if the connection was successful, {@link EnumActionResult#FAIL} if the connection failed, {@link EnumActionResult#PASS} if the connection was not attempted
	 */
	public static EnumActionResult useCoil(IWireCoil coil, EntityPlayer player, World world, BlockPos pos,
										   EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		//Tile entity is not a rotary device
		if(!(tileEntity instanceof IImmersiveConnectable)||!((IImmersiveConnectable)tileEntity).canConnect())
			return EnumActionResult.PASS;

		ItemStack stack = player.getHeldItem(hand);
		EasyNBT nbt = EasyNBT.wrapNBT(stack);

		TargetingInfo target = new TargetingInfo(side, hitX, hitY, hitZ);
		WireType wire = coil.getWireType(stack);
		BlockPos masterPos = ((IImmersiveConnectable)tileEntity).getConnectionMaster(wire, target);
		Vec3i offset = pos.subtract(masterPos);
		tileEntity = world.getTileEntity(masterPos);

		//Tile entity is a rotary device, but the master position refuses connection
		if(!(tileEntity instanceof IImmersiveConnectable)||!((IImmersiveConnectable)tileEntity).canConnect())
			return EnumActionResult.PASS;

		//Attempt connection
		if(!((IImmersiveConnectable)tileEntity).canConnectCable(wire, target, offset)||!coil.canConnectCable(stack
				, tileEntity))
		{
			if(tileEntity instanceof TileEntityMechanicalConnectable)
				IIUtils.sendToolbarMessage(player, IIReference.ROTARY_KEY+"belt_system.wrongBelt");
			else if(nbt.hasKey(NBT_LINKPOS))
			{
				if(nbt.hasKey(NBT_TRIES)&&!IIUtils.hasUnlockedIIAdvancement(player, ADVANCEMENT_DUMB_BELT_TRYING))
				{
					int tries = nbt.getInt(NBT_TRIES);
					if(tries < 5)
						nbt.withInt(NBT_TRIES, tries+1);
					else
					{
						IIUtils.unlockIIAdvancement(player, ADVANCEMENT_DUMB_BELT_TRYING);
						nbt.without(NBT_TRIES);
					}
					IIUtils.sendToolbarMessage(player, IIReference.ROTARY_KEY+"belt_system.wrongCable"+tries);
				}
				else if(IIUtils.hasUnlockedIIAdvancement(player, ADVANCEMENT_DUMB_BELT_TRYING))
					IIUtils.sendToolbarMessage(player, IIReference.ROTARY_KEY+"belt_system.itsBoringGetALife");
				else
				{
					IIUtils.sendToolbarMessage(player, IIReference.ROTARY_KEY+"belt_system.wrongCable");
					nbt.without(NBT_TRIES);
				}
			}
			return EnumActionResult.FAIL;
		}

		//Only apply on the server side
		if(world.isRemote)
			return EnumActionResult.SUCCESS;

		if(!nbt.hasKey(NBT_LINKPOS))
			nbt.withIntArray(NBT_LINKPOS, world.provider.getDimension(), masterPos.getX(), masterPos.getY(),
							masterPos.getZ(), offset.getX(), offset.getY(), offset.getZ())
					.withTag(NBT_TARGETTING_INFO, target::writeToNBT);
		else
		{
			int[] array = nbt.getIntArray(NBT_LINKPOS);
			BlockPos linkPos = new BlockPos(array[1], array[2], array[3]);
			Vec3i offsetLink = BlockPos.NULL_VECTOR;
			if(array.length==7)
				offsetLink = new Vec3i(array[4], array[5], array[6]);

			TileEntity tileEntityLinkingPos = world.getTileEntity(linkPos);
			int distanceSq = (int)Math.ceil(linkPos.distanceSq(masterPos));
			int maxLengthSq = IIMath.pow2(coil.getMaxLength(stack));

			if(array[0]!=world.provider.getDimension())
				IIUtils.sendToolbarMessage(player, IIReference.ROTARY_KEY+"belt_system.wrongDimension");
			else if(linkPos.equals(masterPos))
				IIUtils.sendToolbarMessage(player, IIReference.ROTARY_KEY+"belt_system.sameConnection");
			else if(distanceSq > maxLengthSq)
				IIUtils.sendToolbarMessage(player, IIReference.ROTARY_KEY+"belt_system.tooFar");
			else
			{
				TargetingInfo targetLink = TargetingInfo.readFromNBT(nbt.getCompound(NBT_TARGETTING_INFO));
				if(!(tileEntityLinkingPos instanceof IImmersiveConnectable)||
						!((IImmersiveConnectable)tileEntityLinkingPos).canConnectCable(wire, targetLink,
								offsetLink)||
						!((IImmersiveConnectable)tileEntityLinkingPos).getConnectionMaster(wire, targetLink).equals(linkPos)||
						!coil.canConnectCable(stack, tileEntityLinkingPos))
					IIUtils.sendToolbarMessage(player, IIReference.ROTARY_KEY+"belt_system.invalidPoint");
				else
				{
					IImmersiveConnectable nodeHere = (IImmersiveConnectable)tileEntity;
					IImmersiveConnectable nodeLink = (IImmersiveConnectable)tileEntityLinkingPos;
					boolean connectionExists = false;
					Set<Connection> outputs = ImmersiveNetHandler.INSTANCE.getConnections(world,
							Utils.toCC(nodeHere));
					if(outputs!=null)
						for(Connection con : outputs)
							if(con.end.equals(Utils.toCC(nodeLink)))
								connectionExists = true;
					if(connectionExists)
						IIUtils.sendToolbarMessage(player, IIReference.ROTARY_KEY+"belt_system.connectionExists");
					else if(canConnect(tileEntity, tileEntityLinkingPos, wire))
					{
						Set<BlockPos> ignore = new HashSet<>();
						ignore.addAll(nodeHere.getIgnored(nodeLink));
						ignore.addAll(nodeLink.getIgnored(nodeHere));

						//IE Connection raytrace
						Connection tmpConn = new Connection(Utils.toCC(nodeHere), Utils.toCC(nodeLink), wire, (int)Math.sqrt(distanceSq));
						BlockPos.MutableBlockPos failedReason = new BlockPos.MutableBlockPos();
						Vec3d start = nodeHere.getConnectionOffset(tmpConn, target, pos.subtract(masterPos));
						Vec3d end =
								nodeLink.getConnectionOffset(tmpConn, targetLink, offsetLink).addVector(linkPos.getX()-masterPos.getX(),
										linkPos.getY()-masterPos.getY(),
										linkPos.getZ()-masterPos.getZ());
						boolean canSee = ApiUtils.raytraceAlongCatenaryRelative(tmpConn, (p) ->
								{
									if(ignore.contains(p.getLeft()))
										return false;
									IBlockState state = world.getBlockState(p.getLeft());
									if(ApiUtils.preventsConnection(world, p.getLeft(), state, p.getMiddle(),
											p.getRight()))
									{
										failedReason.setPos(p.getLeft());
										return true;
									}
									return false;
								},
								(p) -> {
								}, start, end);

						//raytrace successful
						if(canSee)
						{
							Connection conn = ImmersiveNetHandler.INSTANCE.addAndGetConnection(world,
									Utils.toCC(nodeHere), Utils.toCC(nodeLink), (int)Math.sqrt(distanceSq), wire);

							nodeHere.connectCable(wire, target, nodeLink, offset);
							nodeLink.connectCable(wire, targetLink, nodeHere, offsetLink);
							ImmersiveNetHandler.INSTANCE.addBlockData(world, conn);
							IESaveData.setDirty(world.provider.getDimension());
							IIUtils.unlockIIAdvancement(player, "main/connect_belt");

							//consume belt stack
							if(!player.capabilities.isCreativeMode)
								coil.consumeWire(stack, (int)Math.sqrt(distanceSq));

							//
							((TileEntity)nodeHere).markDirty();
							world.addBlockEvent(masterPos, ((TileEntity)nodeHere).getBlockType(), -1, 0);
							IBlockState state = world.getBlockState(masterPos);
							world.notifyBlockUpdate(masterPos, state, state, 3);
							((TileEntity)nodeLink).markDirty();
							world.addBlockEvent(linkPos, ((TileEntity)nodeLink).getBlockType(), -1, 0);
							state = world.getBlockState(linkPos);
							world.notifyBlockUpdate(linkPos, state, state, 3);
						}
						else
						{
							//send the "unable to connect" message and point out the obstruction
							IIUtils.sendToolbarMessage(player, IIReference.ROTARY_KEY+"belt_system.cantSee");
							ImmersiveEngineering.packetHandler.sendToAllAround(new MessageObstructedConnection(tmpConn, failedReason, player.world),
									new NetworkRegistry.TargetPoint(player.world.provider.getDimension(),
											player.posX, player.posY, player.posZ,
											64));
						}
					}
					else
						IIUtils.sendToolbarMessage(player, IIReference.ROTARY_KEY+"belt_system.differentAxis");
				}
			}
			nbt.without(NBT_LINKPOS, NBT_TARGETTING_INFO);
		}

		return EnumActionResult.SUCCESS;
	}

	public static int getRPMMax()
	{
		return 1200;
	}

	/**
	 * @param start start of the connection
	 * @param end   end of the connection
	 * @param <T>   motor belt connector tile entity
	 * @return true, when two rotary endpoints transmit power on X axis and their position doesn't differ on Z axis
	 */
	private static <T extends TileEntity & IRotaryConnector> boolean canConnectOnX(T start, T end)
	{
		return start.getPos().getZ()==end.getPos().getZ()
				&&start.getConnectionAxis()==Axis.X&&end.getConnectionAxis()==Axis.X;
	}

	/**
	 * @param start start of the connection
	 * @param end   end of the connection
	 * @param <T>   motor belt connector tile entity
	 * @return true, when two rotary endpoints transmit power on Z axis and their position doesn't differ on X axis
	 */
	private static <T extends TileEntity & IRotaryConnector> boolean canConnectOnZ(T start, T end)
	{
		return start.getPos().getX()==end.getPos().getX()
				&&start.getConnectionAxis()==Axis.Z&&end.getConnectionAxis()==Axis.Z;
	}

	/**
	 * @param wire a wire coil
	 * @return whether the wire coil is a motor belt
	 */
	public static boolean isMotorBelt(WireType wire)
	{
		return BELT_GENERAL_CATEGORY.equals(wire.getCategory());
	}

	//--- Ratios ---//

	public static float getGearEfficiency(NonNullList<ItemStack> inventory)
	{
		float fraction = 1f/(inventory.size());
		float efficiency = 0;
		for(ItemStack stack : inventory)
			if(!stack.isEmpty())
				efficiency += fraction;
		return MathHelper.clamp(efficiency+fraction, 0, 1);
	}

	/**
	 * Calculates the torque ratio for given gears. Already includes the efficiency (presence modifier) of the gears, so don't combine it with {@link #getGearEfficiency(NonNullList)}
	 *
	 * @param inventory the gears to calculate the torque ratio for
	 * @return a speed to torque ratio for given gears
	 */
	public static float getGearTorqueRatio(NonNullList<ItemStack> inventory)
	{
		float torque = 0;
		for(ItemStack stack : inventory)
			if(!stack.isEmpty()&&stack.getItem() instanceof IMotorGear)
				torque += ((IMotorGear)stack.getItem()).getGearTorqueModifier(stack);
		return MathHelper.clamp(torque/inventory.size(), 0, 8);
	}

	public static float getDisplayRotation(TileEntity te, RotaryStorage rotaryStorage, float partialTicks)
	{
		double worldRPT = (te.getWorld().getTotalWorldTime()%getRPMMax()+partialTicks)/getRPMMax();
		return (float)(worldRPT*rotaryStorage.getRotationSpeed())%1;
	}

	/**
	 * @param facing the facing of the rotary connector
	 * @return whether the rotary connector should rotate clockwise ({@link AxisDirection#POSITIVE}) or counter-clockwise ({@link AxisDirection#NEGATIVE})
	 */
	public static boolean shouldRotateClockwise(EnumFacing facing)
	{
		return facing.getAxisDirection()==AxisDirection.POSITIVE;
	}

	public static Collection<MotorBeltType> getAllMotorBelts()
	{
		return WireType.getValues().stream()
				.filter(wire -> wire instanceof MotorBeltType)
				.map(wire -> (MotorBeltType)wire)
				.collect(Collectors.toSet());
	}

	//--- Conversion Methods ---//

	/**
	 * Calculates the torque for a rotary device based on its rotation speed.
	 *
	 * @param t        the tile entity of the rotary device
	 * @param rotation the rotation speed of the rotary device in RPM
	 * @return the torque for the rotary device, based on its rotation speed and the torque modifiers defined in {@link MechanicalDevices#dynamoDefaultTorque}
	 */
	public static float getTorqueForIEDevice(TileEntity t, double rotation)
	{
		for(Entry<Predicate<TileEntity>, Function<Float, Float>> e : TORQUE_BLOCKS.entrySet())
			if(e.getKey().test(t))
				return e.getValue().apply((float)rotation);
		return MechanicalDevices.dynamoDefaultTorque;
	}

	/**
	 * Calculate from IE dynamo output to II's rotary units
	 *
	 * @param rotation the rotation value of the IE dynamo
	 * @param device   the IE device to calculate the torque for
	 * @return an array containing the speed and torque in II's rotary units
	 */
	public static float[] IEToII(double rotation, TileEntity device)
	{
		float torque = getTorqueForIEDevice(device, 1);
		int output = (int)(20*Machines.dynamo_output*rotation*rofConversionRatio);
		float speed = output/torque;
		torque = output/speed;

		return new float[]{speed, torque};
	}

	public static double IIToIE(float energy)
	{
		return (energy/rofConversionRatio/Machines.dynamo_output);
	}

	public static double IEToMM(double rotation)
	{
		return rotation*rofConversionRatio;
	}

	public static double MMToIE(double power)
	{
		return power/rofConversionRatio;
	}

	public static float[] MMToII(double power)
	{
		double ii = MMToIE(power);
		return IEToII(ii, new TileEntityAxle());
	}
}
