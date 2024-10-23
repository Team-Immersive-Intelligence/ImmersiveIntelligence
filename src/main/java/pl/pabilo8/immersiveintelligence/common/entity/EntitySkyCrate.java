package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.tool.ITeslaEntity;
import blusunrize.immersiveengineering.common.Config.IEConfig.Machines;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools.SkycrateMounts;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.ISkyCrateConnector;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkycrateMount;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Pabilo8 on 07-06-2019.
 * I would really want to name it Skyblock, but it seems that the name is already taken...
 * Also, IE planned to add them some time ago (https://github.com/BluSunrize/ImmersiveEngineering/issues/2027)
 * The name skycrate derives from a name of a file inside IE's jar (skycrate.obj), but the model isn't used.
 */
public class EntitySkyCrate extends Entity implements ITeslaEntity
{
	//ATTENTION! UWAGA! ACHTUNG! - Do not EVER use DataSerializers.ITEM_STACK, it's broken af!
	private static final DataParameter<NBTTagCompound> dataMarkerMount = EntityDataManager.createKey(EntitySkyCrate.class, DataSerializers.COMPOUND_TAG);
	private static final DataParameter<NBTTagCompound> dataMarkerCrate = EntityDataManager.createKey(EntitySkyCrate.class, DataSerializers.COMPOUND_TAG);
	public final Set<BlockPos> ignoreCollisions = new HashSet<>();
	public Connection connection;
	public double linePos = 0;
	public ItemStack mount = ItemStack.EMPTY;
	public ItemStack crate = ItemStack.EMPTY;
	public double horizontalSpeedPowered = 0;
	public double horizontalSpeedUnpowered = 0;
	public double energy;
	private Ticket ticket = null;

	public EntitySkyCrate(World world)
	{
		super(world);
		this.setSize(1f, 1f);

		if(!world.isRemote)
		{
			ticket = ForgeChunkManager.requestTicket(ImmersiveIntelligence.INSTANCE, this.getEntityWorld(), Type.ENTITY);
			if(ticket!=null)
				ticket.bindEntity(this);
		}
	}

	public EntitySkyCrate(World world, Connection connection, ItemStack mount, ItemStack crate, BlockPos firstPos)
	{
		this(world);
		if(!world.isRemote)
		{
			setConnection(connection, firstPos, linePos);
			setSkycrate(mount, crate);
		}

	}

	@Override
	protected void entityInit()
	{
		//I'M REALLY PISSED NOW
		//I WAS WONDERING FOR TWO FUCKING HOURS ON WHAT'S WRONG WITH MY CODE
		//TURNS OUT... YES! FORGE'S FAULT! HOW GREAT!
		this.dataManager.register(dataMarkerCrate, new NBTTagCompound());
		this.dataManager.register(dataMarkerMount, new NBTTagCompound());
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if(world.isRemote)
		{
			crate = new ItemStack(dataManager.get(dataMarkerCrate));
			mount = new ItemStack(dataManager.get(dataMarkerMount));
		}
		else
		{
			if(linePos >= 1)
			{
				boolean success = false;
				if(world.getTileEntity(connection.end) instanceof ISkyCrateConnector)
				{
					success = ((ISkyCrateConnector)world.getTileEntity(connection.end)).onSkycrateMeeting(this);
				}
				if(!success)
				{
					/* if(crate.getItem() instanceof ItemBlock)
					{
						ItemBlock b = (ItemBlock)crate.getItem();
						EntityFallingBlock e = new EntityFallingBlock(world, (int)posX, (int)posY-2, (int)posZ, b.getBlock().getDefaultState());
						world.spawnEntity(e);
					}
					else
					{
						Utils.dropStackAtPos(world, getPosition(), crate.copy());
					} */
					BlockPos ePos = getPosition();
					BlockPos dropPos = new BlockPos(ePos.getX(), ePos.getY() - 2, ePos.getZ());
					Utils.dropStackAtPos(world, dropPos, mount.copy());
					Utils.dropStackAtPos(world, dropPos, crate.copy());
					this.setDead();
				}
			}
			else if(world.getTileEntity(getPosition()) instanceof ISkyCrateConnector)
			{
				nextPos();
			}
			else
				nextPos();
		}
		if(!world.isRemote)
			ForgeChunkManager.forceChunk(ticket, this.world.getChunkFromBlockCoords(this.getPosition()).getPos());

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey("crate"))
		{
			crate = new ItemStack(compound.getCompoundTag("crate"));
		}
		if(compound.hasKey("mount"))
		{
			mount = new ItemStack(compound.getCompoundTag("mount"));
		}
		if(compound.hasKey("connection"))
		{
			connection = Connection.readFromNBT(compound.getCompoundTag("connection"));
		}
		else if(connection==null)
		{
			setDead();
		}
		linePos = compound.getDouble("linePos");
		energy = compound.getDouble("energy");

		setSkycrate(mount, crate);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setTag("crate", crate.serializeNBT());
		compound.setTag("mount", mount.serializeNBT());
		compound.setDouble("linePos", linePos);
		compound.setDouble("energy", energy);
		if(connection!=null)
			compound.setTag("connection", connection.writeToNBT());
	}

	public void setSkycrate(ItemStack mount, ItemStack crate)
	{
		this.mount = mount.copy();
		this.crate = crate.copy();

		if(mount.getItem() instanceof ISkycrateMount)
		{
			ISkycrateMount s = (ISkycrateMount)mount.getItem();
			energy = s.getMountEnergy(mount);
			horizontalSpeedPowered = s.getPoweredSpeed(mount);
			horizontalSpeedUnpowered = s.getUnpoweredSpeed(mount);
		}
		else
			setDead();

		if(crate.isEmpty())
			setDead();

		dataManager.set(dataMarkerCrate, crate.serializeNBT());
		dataManager.set(dataMarkerMount, mount.serializeNBT());
	}

	public void setConnection(Connection connection, BlockPos firstPos, double linePos)
	{
		if(connection.start.equals(firstPos))
			this.connection = connection;
		else
			this.connection = ImmersiveNetHandler.INSTANCE.getReverseConnection(world.provider.getDimension(), connection);
		this.linePos = linePos;
		try
		{
			Vec3d v = this.connection.getVecAt(this.linePos);
			this.setPosition(firstPos.getX()+v.x, firstPos.getY()+v.y, firstPos.getZ()+v.z);
		} catch(NullPointerException e)
		{

		}

		if(world.getTileEntity(connection.start) instanceof IImmersiveConnectable&&world.getTileEntity(connection.end) instanceof IImmersiveConnectable)
		{
			ignoreCollisions.addAll(((IImmersiveConnectable)world.getTileEntity(connection.start)).getIgnored(((IImmersiveConnectable)world.getTileEntity(connection.end))));
		}
	}

	public void nextPos()
	{
		if(connection!=null&&!world.isRemote)
		{
			double speed = getSpeed();
			this.linePos += speed/connection.length;
			try
			{
				Vec3d pos = connection.getVecAt(this.linePos).add(new Vec3d(connection.start));
				setPosition(pos.x, pos.y, pos.z);
				setRotation((float)Math.atan2(pos.z, pos.x), (float)connection.getSlopeAt(this.linePos));
			} catch(NullPointerException e)
			{
				if(ticksExisted > 10)
					setDead();
			}
			this.linePos = MathHelper.clamp(this.linePos, 0, 1);
		}

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
		if(!world.isRemote)
		{
			if(((ISkycrateMount)mount.getItem()).isTesla(mount))
			{
				int cap = (int)Math.floor((Machines.teslacoil_consumption_active*(lowPower?0.5f: 1f))/SkycrateMounts.electricEnergyRatio);
				energy = Math.min(energy+cap, ((ISkycrateMount)mount.getItem()).getMountMaxEnergy(mount));
			}
		}
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return crate.copy();
	}
}