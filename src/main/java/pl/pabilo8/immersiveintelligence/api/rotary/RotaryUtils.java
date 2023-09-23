package pl.pabilo8.immersiveintelligence.api.rotary;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.network.MessageObstructedConnection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.MechanicalDevices;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalConnectable;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 26-12-2019
 *
 * <p>
 * March 2020<br>
 * And thus began the cooperation
 * </p>
 * <br>
 * <p>
 * I'm keeping all my methods related to the Motor Belt system here, so I can make a seperate mod
 * out of it or put it in a common library with along with other addon makers APIs.<br>
 * Name ideas:<br>
 * Immersive Commons / Integrated Engineering / United Engineering<br>
 * If any addon maker wants to cooperate on such a library, I'm ready! ^^<br>
 * </p>
 */
public class RotaryUtils
{
	public static final String BELT_CATEGORY = "MOTOR_BELT";
	public static final Map<Predicate<TileEntity>, Function<Float, Float>> ie_rotational_blocks_torque = new HashMap<>();

	public static boolean canConnect(TileEntity start, TileEntity end, WireType wire)
	{
		//Better be safe ^^
		if(!(start instanceof IMotorBeltConnector)||!(end instanceof IMotorBeltConnector))
			return false;
		if(start.getWorld()!=end.getWorld())
			return false;
		if(!(wire instanceof MotorBeltType))
			return false;


		return ((TileEntity & IMotorBeltConnector)start).getConnectionAxis()==((TileEntity & IMotorBeltConnector)end).getConnectionAxis();
	}

	public static EnumActionResult useCoil(IWireCoil coil, EntityPlayer player, World world, BlockPos pos,
										   EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof IImmersiveConnectable&&((IImmersiveConnectable)tileEntity).canConnect())
		{
			ItemStack stack = player.getHeldItem(hand);
			TargetingInfo target = new TargetingInfo(side, hitX, hitY, hitZ);
			WireType wire = coil.getWireType(stack);
			BlockPos masterPos = ((IImmersiveConnectable)tileEntity).getConnectionMaster(wire, target);
			Vec3i offset = pos.subtract(masterPos);
			tileEntity = world.getTileEntity(masterPos);
			if(!(tileEntity instanceof IImmersiveConnectable)||!((IImmersiveConnectable)tileEntity).canConnect())
				return EnumActionResult.PASS;


			if(!((IImmersiveConnectable)tileEntity).canConnectCable(wire, target, offset)||!coil.canConnectCable(stack
					, tileEntity))
			{
				if(tileEntity instanceof TileEntityMechanicalConnectable)
					player.sendStatusMessage(new TextComponentTranslation(IIReference.ROTARY_KEY+"belt_system"+
							".wrongBelt"), true);
				else if(ItemNBTHelper.hasKey(stack, "linkingPos"))
					if(ItemNBTHelper.hasKey(stack, "tries")&&!IIUtils.hasUnlockedIIAdvancement(player, "main/secret_connect_wire"))
					{
						int tries = ItemNBTHelper.getInt(stack, "tries");
						if(tries < 5)
							ItemNBTHelper.setInt(stack, "tries", ++tries);
						else
						{
							IIUtils.unlockIIAdvancement(player, "main"+
									"/secret_connect_wire");
							ItemNBTHelper.remove(stack, "tries");
						}
						player.sendStatusMessage(new TextComponentTranslation(IIReference.ROTARY_KEY+"belt_system"+
								".wrongCable"+tries), true);
					}
					else if(IIUtils.hasUnlockedIIAdvancement(player, "main"+
							"/secret_connect_wire"))
						player.sendStatusMessage(new TextComponentTranslation(IIReference.ROTARY_KEY+"belt_system"+
								".itsBoringGetALife"), true);
					else
					{
						player.sendStatusMessage(new TextComponentTranslation(IIReference.ROTARY_KEY+"belt_system"+
								".wrongCable"), true);
						ItemNBTHelper.setInt(stack, "tries", 0);
					}

				return EnumActionResult.FAIL;
			}

			if(!world.isRemote)
				if(!ItemNBTHelper.hasKey(stack, "linkingPos"))
				{
					ItemNBTHelper.setIntArray(stack, "linkingPos", new int[]{world.provider.getDimension(),
							masterPos.getX(), masterPos.getY(), masterPos.getZ(),
							offset.getX(), offset.getY(), offset.getZ()});
					NBTTagCompound targetNbt = new NBTTagCompound();
					target.writeToNBT(targetNbt);
					ItemNBTHelper.setTagCompound(stack, "targettingInfo", targetNbt);
				}
				else
				{
					int[] array = ItemNBTHelper.getIntArray(stack, "linkingPos");
					BlockPos linkPos = new BlockPos(array[1], array[2], array[3]);
					Vec3i offsetLink = BlockPos.NULL_VECTOR;
					if(array.length==7)
						offsetLink = new Vec3i(array[4], array[5], array[6]);
					TileEntity tileEntityLinkingPos = world.getTileEntity(linkPos);
					int distanceSq = (int)Math.ceil(linkPos.distanceSq(masterPos));
					int maxLengthSq = coil.getMaxLength(stack); //not squared yet
					maxLengthSq *= maxLengthSq;
					if(array[0]!=world.provider.getDimension())
						player.sendStatusMessage(new TextComponentTranslation(IIReference.ROTARY_KEY+"belt_system"+
								".wrongDimension"), true);
					else if(linkPos.equals(masterPos))
						player.sendStatusMessage(new TextComponentTranslation(IIReference.ROTARY_KEY+"belt_system"+
								".sameConnection"), true);
					else if(distanceSq > maxLengthSq)
						player.sendStatusMessage(new TextComponentTranslation(IIReference.ROTARY_KEY+"belt_system"+
								".tooFar"), true);
					else
					{
						TargetingInfo targetLink = TargetingInfo.readFromNBT(ItemNBTHelper.getTagCompound(stack,
								"targettingInfo"));
						if(!(tileEntityLinkingPos instanceof IImmersiveConnectable)||
								!((IImmersiveConnectable)tileEntityLinkingPos).canConnectCable(wire, targetLink,
										offsetLink)||
								!((IImmersiveConnectable)tileEntityLinkingPos).getConnectionMaster(wire, targetLink).equals(linkPos)||
								!coil.canConnectCable(stack, tileEntityLinkingPos))
							player.sendStatusMessage(new TextComponentTranslation(IIReference.ROTARY_KEY+"belt_system"+
									".invalidPoint"), true);
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
								player.sendStatusMessage(new TextComponentTranslation(IIReference.ROTARY_KEY+
										"belt_system.connectionExists"), true);
							else if(canConnect(tileEntity, tileEntityLinkingPos, wire))
							{
								Set<BlockPos> ignore = new HashSet<>();
								ignore.addAll(nodeHere.getIgnored(nodeLink));
								ignore.addAll(nodeLink.getIgnored(nodeHere));
								Connection tmpConn = new Connection(Utils.toCC(nodeHere), Utils.toCC(nodeLink), wire,
										(int)Math.sqrt(distanceSq));
								Vec3d start = nodeHere.getConnectionOffset(tmpConn, target, pos.subtract(masterPos));
								Vec3d end =
										nodeLink.getConnectionOffset(tmpConn, targetLink, offsetLink).addVector(linkPos.getX()-masterPos.getX(),
												linkPos.getY()-masterPos.getY(),
												linkPos.getZ()-masterPos.getZ());
								BlockPos.MutableBlockPos failedReason = new BlockPos.MutableBlockPos();
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
										(p) -> {}, start, end);
								if(canSee)
								{
									Connection conn = ImmersiveNetHandler.INSTANCE.addAndGetConnection(world,
											Utils.toCC(nodeHere), Utils.toCC(nodeLink),
											(int)Math.sqrt(distanceSq), wire);


									nodeHere.connectCable(wire, target, nodeLink, offset);
									nodeLink.connectCable(wire, targetLink, nodeHere, offsetLink);
									ImmersiveNetHandler.INSTANCE.addBlockData(world, conn);
									IESaveData.setDirty(world.provider.getDimension());
									IIUtils.unlockIIAdvancement(player, "main"+
											"/connect_belt");

									if(!player.capabilities.isCreativeMode)
										coil.consumeWire(stack, (int)Math.sqrt(distanceSq));
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
									player.sendStatusMessage(new TextComponentTranslation(IIReference.ROTARY_KEY+
											"belt_system.cantSee"), true);
									ImmersiveEngineering.packetHandler.sendToAllAround(new MessageObstructedConnection(tmpConn, failedReason, player.world),
											new NetworkRegistry.TargetPoint(player.world.provider.getDimension(),
													player.posX, player.posY, player.posZ,
													64));
								}
							}
						}
					}
					ItemNBTHelper.remove(stack, "linkingPos");
					ItemNBTHelper.remove(stack, "targettingInfo");
				}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	public static int getRPMMax()
	{
		return 1200;
	}

	private static <T extends TileEntity & IMotorBeltConnector> boolean canConnectOnX(T conn_start, T conn_end)
	{
		return conn_start.getPos().getZ()==conn_start.getPos().getZ()&&conn_start.getConnectionAxis()==Axis.X&&conn_end.getConnectionAxis()==Axis.X;
	}

	private static <T extends TileEntity & IMotorBeltConnector> boolean canConnectOnZ(T conn_start, T conn_end)
	{
		return conn_start.getPos().getX()==conn_start.getPos().getX()&&conn_start.getConnectionAxis()==Axis.Z&&conn_end.getConnectionAxis()==Axis.Z;
	}

	public static boolean isMechanicalBelt(WireType wire)
	{
		return wire.getCategory()!=null&&wire.getCategory().equals(BELT_CATEGORY);
	}

	@SideOnly(Side.CLIENT)
	public static void tessellateMotorBelt(MotorBeltData data, double rpm, double world_rpm)
	{
		if(data==null)
			return;

		IModelMotorBelt model = data.model;

		GlStateManager.pushMatrix();
		//GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		//RenderHelper.enableStandardItemLighting();

		//GlStateManager.translate(data.offset.x, data.offset.y, data.offset.z);

		// TODO: 26.12.2021
		double speed = Math.abs(world_rpm*rpm)%(data.beltWidth/16f);

		model.setRotation(0);

		//GlStateManager.pushMatrix();
		//GlStateManager.translate(0, (data.radius+data.beltThickness)*0.0625f, 0);

		IIClientUtils.bindTexture(data.texture);
		//data.model.renderBelt();

		float angleSide = 90f/data.origins.length;

		for(int i = 0; i < data.origins.length; i++)
		{
			GlStateManager.pushMatrix();

			Vec3d origin = data.origins[i];
			GlStateManager.translate(origin.x, origin.y, origin.z);
			GlStateManager.rotate(180+data.yaw[i], 0, 1, 0);
			GlStateManager.rotate(data.pitch[i], 1, 0, 0);

			if(data.yaw[i]==0&&Math.abs(data.pitch[i])==90)
				GlStateManager.rotate(90, 0, 0, 1);

			float ff = Math.signum(data.pitch[i]);
			if(ff==0)
				ff = Math.signum(data.yaw[i]);
			if(ff==0)
				ff = -1;

			GlStateManager.pushMatrix();
			GlStateManager.rotate((float)(speed*angleSide*2f)*-ff, 1, 0, 0); //

			if(ff==-1)
				GlStateManager.rotate(-angleSide, 1, 0, 0);

			for(int j = 0; j < data.sidePoints[i]; j++)
			{
				GlStateManager.rotate(angleSide, 1, 0, 0);
				GlStateManager.pushMatrix();
				GlStateManager.translate(0, -(data.radii[i]+data.beltThickness+1)*0.0625f, 0);
				model.renderBelt();
				GlStateManager.popMatrix();

			}
			GlStateManager.popMatrix();

			GlStateManager.translate(0, -(data.radii[i]+data.beltThickness)*0.0625f*ff, speed); //
			for(int j = 0; j < data.lengths[i]; j += data.beltWidth)
			{
				data.model.renderBelt();
				GlStateManager.translate(0, 0, data.beltWidth*0.0625f);
			}

			GlStateManager.popMatrix();
		}

		/*
		Upper Belt
		 *//*
		for(int i = 0; i < data.points.length-1; i += 1)
		{
			Vec3d point = data.points[i];
			Vec3d point2 = data.points[Math.min(i+1, data.points.length-1)];
			GlStateManager.pushMatrix();

			GlStateManager.translate(point.x*(1f-speed), point.y*(1f-speed), point.z*(1f-speed));
			GlStateManager.translate(point2.x*speed, point2.y*speed, point2.z*speed);
			model.setRotation(data.slopes[i]);

			GlStateManager.rotate(data.yaw, 0, 1, 0);
			model.renderBelt();
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();

		*//*
		Lower Belt
		 *//*
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, (-data.radius-data.beltThickness)*0.0625f, 0);

		for(int i = data.points.length-1; i > 0; i -= 1)
		{
			Vec3d point = data.points[i];
			Vec3d point2 = data.points[Math.max(i-1, 0)];
			GlStateManager.pushMatrix();
			GlStateManager.translate(point.x*(1f-speed), point.y*(1f-speed), point.z*(1f-speed));
			GlStateManager.translate(point2.x*speed, point2.y*speed, point2.z*speed);
			model.setRotation((float)Math.PI+data.slopes[i]);

			GlStateManager.rotate(data.yaw, 0, 1, 0);
			model.renderBelt();
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();

		model.setRotation(0);

		if(data.totalPoints > 0)
		{
*//*
		Side Belt 1
		 *//*
			GlStateManager.pushMatrix();
			GlStateManager.translate(data.points[0].x, data.points[0].y, data.points[0].z);
			GlStateManager.rotate(data.yaw, 0, 1, 0);
			//GlStateManager.rotate(-(float)Math.toRadians(data.slopes[1]),0,0,1);
			GlStateManager.rotate((float)(data.angleFirst*(1f-speed)), 0, 0, 1);

			for(int i = 0; i < data.sidePointsFirst; i++)
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate(0, (data.radius+data.beltThickness)*0.0625f, 0);
				model.renderBelt();
				GlStateManager.popMatrix();
				GlStateManager.rotate(data.angleFirst, 0, 0, 1);
			}
			GlStateManager.popMatrix();

		*//*
		Side Belt 2
		 *//*
			GlStateManager.pushMatrix();
			GlStateManager.translate(data.points[data.points.length-1].x, data.points[data.points.length-1].y,
					data.points[data.points.length-1].z);
			GlStateManager.rotate(data.yaw, 0, 1, 0);
			GlStateManager.rotate(data.angleSecond+(float)(-data.angleSecond*(1+speed)), 0, 0, 1);

			for(int i = 0; i < data.sidePointsSecond; i++)
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate(0, (data.radius+data.beltThickness)*0.0625f, 0);
				model.renderBelt();
				GlStateManager.popMatrix();
				GlStateManager.rotate(-(data.angleSecond), 0, 0, 1);
			}
			GlStateManager.popMatrix();
		}*/

		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	public static void renderEnergyTooltip(ArrayList<String> tooltip, int mx, int my, int x, int y,
										   RotaryStorage storage, int w, int h, int spacing, int iconSize,
										   boolean iconsAbove, boolean tooltipIcons)
	{
		if(tooltipIcons)
		{
			int xx = (int)((spacing+w+(0.5*spacing))-(iconSize/2));
			int yy = iconsAbove?(y-iconSize): (y+(2*spacing)+h);
			int iconHeight = iconsAbove?spacing: -spacing;

			if(mx > xx&&mx < xx+iconSize&&my > yy&&my < yy+iconHeight)
			{
				tooltip.add(IIReference.ROTARY_KEY+"mechanical.speed");
				return;
			}
			xx += spacing+(0.5*w);
			if(mx > xx&&mx < xx+iconSize&&my > yy&&my < yy+iconHeight)
			{
				tooltip.add(IIReference.ROTARY_KEY+"mechanical.torque");
				return;
			}
		}
		if(mx >= x+spacing&&mx <= x+spacing+w&&my >= y+spacing&&my <= y+spacing+h)
			tooltip.add(I18n.format(IIReference.ROTARY_KEY+"mechanical.speed")+": "+storage.getRotationSpeed()+" "+I18n.format(IIReference.ROTARY_KEY+"mechanical.speed_unit"));
		if(mx >= x+w+(2*spacing)&&mx <= x+(2*w)+(2*spacing)&&my >= y+spacing&&my <= y+spacing+h)
			tooltip.add(I18n.format(IIReference.ROTARY_KEY+"mechanical.torque")+": "+storage.getTorque()+" "+I18n.format(IIReference.ROTARY_KEY+"mechanical.torque_unit"));
	}

	public static void renderEnergyTooltip(ArrayList<String> tooltip, int mx, int my, int x, int y,
										   RotaryStorage storage)
	{
		renderEnergyTooltip(tooltip, mx, my, x, y, storage, 7, 48, 2, 8, true, true);
	}

	public static void renderEnergyBars(int x, int y, int w, int h, int spacing, RotaryStorage storage, float maxRPM,
										float maxTorque)
	{
		int rpm = Math.round(h*Math.min((storage.getRotationSpeed()/maxRPM), 1));
		int torque = Math.round(h*Math.min((storage.getTorque()/maxTorque), 1));
		ClientUtils.drawGradientRect(x+spacing, y+spacing+(h-rpm), x+spacing+w, y+spacing+h, 0xffb51500, 0xff600b00);
		ClientUtils.drawGradientRect(x+(2*spacing)+w, y+spacing+(h-torque), x+(2*w)+(2*spacing), y+spacing+h, 0xff00b521, 0xff003a00);

	}

	public static void renderEnergyBars(int x, int y, RotaryStorage storage, float maxRPM, float maxTorque)
	{
		renderEnergyBars(x, y, 7, 48, 2, storage, maxRPM, maxTorque);
	}

	public static float getGearEffectiveness(NonNullList<ItemStack> inventory, float modifier, int slots)
	{
		float fraction = 1f/(slots+1);
		float effectiveness = 0;
		for(ItemStack stack : inventory)
			if(!stack.isEmpty())
				effectiveness += fraction;
		return MathHelper.clamp((effectiveness*modifier)+fraction, 0, 1);
	}

	public static float getGearEffectiveness(NonNullList<ItemStack> inventory, float modifier)
	{
		return getGearEffectiveness(inventory, modifier,inventory.size());
	}

	public static float getGearTorqueRatio(NonNullList<ItemStack> inventory)
	{
		float torque = 0;
		for(ItemStack stack : inventory)
			if(!stack.isEmpty()&&stack.getItem() instanceof IMotorGear)
				torque += ((IMotorGear)stack.getItem()).getGearTorqueModifier(stack);
		return MathHelper.clamp(torque/inventory.size(), 0, 10);
	}

	public static float getTorqueForIEDevice(TileEntity t, double rotation)
	{
		for(Entry<Predicate<TileEntity>, Function<Float, Float>> e : ie_rotational_blocks_torque.entrySet())
			if(e.getKey().test(t))
				return e.getValue().apply((float)rotation);
		return MechanicalDevices.dynamoDefaultTorque;
	}

	public static double getWorldRPM(World world, float partialTicks)
	{
		return (world.getTotalWorldTime()%getRPMMax()+partialTicks)/getRPMMax();
	}
}
