package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.api.tool.ITeslaEntity;
import blusunrize.immersiveengineering.common.Config.IEConfig.Machines;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.ISkyCrateConnector;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkycrateMount;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools.SkycrateMounts;

import java.util.HashSet;
import java.util.Set;

/**
 * Carries a container along a loaded structural wire and through Skycrate connectors.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.10.2026
 * @since 07.06.2019
 */
public class EntitySkyCrate extends Entity implements ITeslaEntity
{
	private static final DataParameter<NBTTagCompound> dataMarkerMount = EntityDataManager.createKey(EntitySkyCrate.class, DataSerializers.COMPOUND_TAG);
	private static final DataParameter<NBTTagCompound> dataMarkerCrate = EntityDataManager.createKey(EntitySkyCrate.class, DataSerializers.COMPOUND_TAG);
	private static final DataParameter<Float> dataMarkerEnergy = EntityDataManager.createKey(EntitySkyCrate.class, DataSerializers.FLOAT);
	public final Set<BlockPos> ignoreCollisions = new HashSet<>();
	public Connection connection;
	public double linePos;
	public ItemStack mount = ItemStack.EMPTY;
	public ItemStack crate = ItemStack.EMPTY;
	public double horizontalSpeedPowered, horizontalSpeedUnpowered, energy;
	private Ticket ticket;
	private ChunkPos forcedChunk;
	private int nextTicketRequest;

	public EntitySkyCrate(World world)
	{
		super(world);
		setSize(1, 1);
	}

	public EntitySkyCrate(World world, Connection connection, ItemStack mount, ItemStack crate, BlockPos firstPos)
	{
		this(world);
		if(!world.isRemote)
		{
			setSkycrate(mount, crate);
			setConnection(connection, firstPos, 0);
		}
	}

	@Override
	protected void entityInit()
	{
		dataManager.register(dataMarkerCrate, new NBTTagCompound());
		dataManager.register(dataMarkerMount, new NBTTagCompound());
		dataManager.register(dataMarkerEnergy, 0f);
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key)
	{
		super.notifyDataManagerChange(key);
		if(world!=null&&world.isRemote)
		{
			if(dataMarkerCrate.equals(key))
				crate = new ItemStack(dataManager.get(dataMarkerCrate));
			else if(dataMarkerMount.equals(key))
				mount = new ItemStack(dataManager.get(dataMarkerMount));
			else if(dataMarkerEnergy.equals(key))
				energy = dataManager.get(dataMarkerEnergy);
		}
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(world.isRemote||isDead)
			return;
		if(!validConnection(connection)||crate.isEmpty()||!(mount.getItem() instanceof ISkycrateMount))
		{
			dropPayload();
			return;
		}
		//Do not load route endpoints as a side effect of transport.
		if(!world.isBlockLoaded(connection.start)||!world.isBlockLoaded(connection.end))
			return;
		Set<Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(world, connection.start);
		if(connections==null||!connections.contains(connection))
		{
			dropPayload();
			return;
		}
		connection.getSubVertices(world);
		if(linePos >= 1)
		{
			TileEntity endpoint = world.getTileEntity(connection.end);
			boolean retained = endpoint instanceof ISkyCrateConnector&&((ISkyCrateConnector)endpoint).onSkycrateMeeting(this);
			if(!retained&&!isDead)
				dropPayload();
		}
		else
			nextPos();
		if(!isDead)
			requestChunkLoadingTicket();
		if(!isDead&&ticket!=null)
		{
			ChunkPos current = new ChunkPos(getPosition());
			if(!current.equals(forcedChunk))
			{
				ForgeChunkManager.forceChunk(ticket, current);
				if(forcedChunk!=null)
					ForgeChunkManager.unforceChunk(ticket, forcedChunk);
				forcedChunk = current;
			}
		}
		dataManager.set(dataMarkerEnergy, (float)energy);
	}

	private static boolean validConnection(Connection connection)
	{
		return connection!=null&&connection.start!=null&&connection.end!=null&&connection.length > 0
				&&connection.cableType!=null&&WireType.STRUCTURE_CATEGORY.equals(connection.cableType.getCategory());
	}

	private void dropPayload()
	{
		if(isDead)
			return;
		BlockPos dropPos = getPosition().down(2);
		if(!mount.isEmpty())
			Utils.dropStackAtPos(world, dropPos, mount.copy());
		if(!crate.isEmpty())
			Utils.dropStackAtPos(world, dropPos, crate.copy());
		mount = crate = ItemStack.EMPTY;
		setDead();
	}

	@Override
	public void onAddedToWorld()
	{
		super.onAddedToWorld();
		if(!world.isRemote&&!isDead)
			requestChunkLoadingTicket();
	}

	private void requestChunkLoadingTicket()
	{
		if(ticket!=null||ticksExisted < nextTicketRequest)
			return;
		ticket = ForgeChunkManager.requestTicket(ImmersiveIntelligence.INSTANCE, world, Type.ENTITY);
		if(ticket!=null)
			ticket.bindEntity(this);
		else
			nextTicketRequest = ticksExisted+20;
	}

	/**
	 * Uses the entity's saved chunk ticket after a world reload.
	 */
	public void restoreChunkLoadingTicket(Ticket restored)
	{
		if(world.isRemote||isDead||restored.world!=world||restored.getEntity()!=this)
		{
			ForgeChunkManager.releaseTicket(restored);
			return;
		}
		if(ticket!=null&&ticket!=restored)
			ForgeChunkManager.releaseTicket(ticket);
		ticket = restored;
		for(ChunkPos chunk : ticket.getChunkList())
			ForgeChunkManager.unforceChunk(ticket, chunk);
		forcedChunk = new ChunkPos(getPosition());
		ForgeChunkManager.forceChunk(ticket, forcedChunk);
	}

	@Override
	public void onRemovedFromWorld()
	{
		if(ticket!=null)
		{
			ForgeChunkManager.releaseTicket(ticket);
			ticket = null;
			forcedChunk = null;
		}
		super.onRemovedFromWorld();
	}

	@Override
	public void setDead()
	{
		if(ticket!=null)
		{
			ForgeChunkManager.releaseTicket(ticket);
			ticket = null;
			forcedChunk = null;
		}
		super.setDead();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		setSkycrate(new ItemStack(compound.getCompoundTag("mount")), new ItemStack(compound.getCompoundTag("crate")));
		energy = compound.getDouble("energy");
		dataManager.set(dataMarkerEnergy, (float)energy);
		linePos = MathHelper.clamp(compound.getDouble("linePos"), 0, 1);
		NBTTagCompound wire = compound.getCompoundTag("connection");
		if(wire.getIntArray("start").length==3&&wire.getIntArray("end").length==3)
			connection = Connection.readFromNBT(wire);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setTag("crate", crate.serializeNBT());
		compound.setTag("mount", mount.serializeNBT());
		compound.setDouble("linePos", linePos);
		compound.setDouble("energy", energy);
		if(validConnection(connection))
			compound.setTag("connection", connection.writeToNBT());
	}

	public void setSkycrate(ItemStack mount, ItemStack crate)
	{
		this.mount = mount.copy();
		this.crate = crate.copy();
		if(mount.getItem() instanceof ISkycrateMount&&!crate.isEmpty())
		{
			ISkycrateMount type = (ISkycrateMount)mount.getItem();
			energy = type.getMountEnergy(mount);
			horizontalSpeedPowered = type.getPoweredSpeed(mount);
			horizontalSpeedUnpowered = type.getUnpoweredSpeed(mount);
		}
		else
			setDead();
		dataManager.set(dataMarkerCrate, crate.serializeNBT());
		dataManager.set(dataMarkerMount, mount.serializeNBT());
		dataManager.set(dataMarkerEnergy, (float)energy);
	}

	public void setConnection(Connection connection, BlockPos firstPos, double linePos)
	{
		if(!validConnection(connection)||(!connection.start.equals(firstPos)&&!connection.end.equals(firstPos)))
		{
			this.connection = null;
			return;
		}
		this.connection = connection.start.equals(firstPos)?connection:
				ImmersiveNetHandler.INSTANCE.getReverseConnection(world.provider.getDimension(), connection);
		this.linePos = MathHelper.clamp(linePos, 0, 1);
		ignoreCollisions.clear();
		if(this.connection==null||!world.isBlockLoaded(connection.start)||!world.isBlockLoaded(connection.end))
			return;
		this.connection.getSubVertices(world);
		updatePositionOnWire();
		TileEntity start = world.getTileEntity(connection.start);
		TileEntity end = world.getTileEntity(connection.end);
		if(start instanceof IImmersiveConnectable&&end instanceof IImmersiveConnectable)
		{
			ignoreCollisions.addAll(((IImmersiveConnectable)start).getIgnored((IImmersiveConnectable)end));
			ignoreCollisions.addAll(((IImmersiveConnectable)end).getIgnored((IImmersiveConnectable)start));
		}
	}

	public void nextPos()
	{
		if(!world.isRemote&&validConnection(connection))
		{
			linePos = MathHelper.clamp(linePos+Math.max(0, getSpeed())/connection.length, 0, 1);
			updatePositionOnWire();
		}
	}

	private void updatePositionOnWire()
	{
		Vec3d position = connection.getVecAt(linePos).add(new Vec3d(connection.start));
		setPosition(position.x, position.y, position.z);
		Vec3d direction = new Vec3d(connection.end.subtract(connection.start));
		setRotation((float)Math.toDegrees(Math.atan2(direction.z, direction.x)),
				(float)Math.toDegrees(Math.atan(connection.getSlopeAt(linePos))));
	}

	public Connection getConnection()
	{
		return connection;
	}

	public double getSpeed()
	{
		return energy > 0?horizontalSpeedPowered: horizontalSpeedUnpowered;
	}

	@Override
	public void onHit(TileEntity teslaCoil, boolean lowPower)
	{
		if(!world.isRemote&&mount.getItem() instanceof ISkycrateMount)
		{
			ISkycrateMount type = (ISkycrateMount)mount.getItem();
			if(type.isTesla(mount))
			{
				int charge = (int)Math.floor((Machines.teslacoil_consumption_active*(lowPower?.5f: 1f))/SkycrateMounts.electricEnergyRatio);
				energy = Math.min(energy+charge, type.getMountMaxEnergy(mount));
				dataManager.set(dataMarkerEnergy, (float)energy);
			}
		}
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return crate.copy();
	}
}
