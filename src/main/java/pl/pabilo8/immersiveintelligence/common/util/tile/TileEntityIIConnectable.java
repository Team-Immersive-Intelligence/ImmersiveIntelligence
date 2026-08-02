package pl.pabilo8.immersiveintelligence.common.util.tile;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.*;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 16.07.2026
 */
public abstract class TileEntityIIConnectable extends TileEntityIIBase implements IImmersiveConnectable
{
	@SyncNBT(nullable = true)
	public WireType limitType = null;
	private List<Pair<Float, Consumer<Float>>> sources = new ArrayList<>();
	private long lastSourceUpdate = 0;

	//--- Abstract ---//

	public abstract boolean acceptsWireType(WireType category);

	public abstract boolean isRelay();

	//--- IRedstoneConnector ---//

	public World getConnectorWorld()
	{
		return getWorld();
	}

	//--- NBT ---//

	@Override
	public boolean receiveClientEvent(int id, int arg)
	{
		if(id==-1||id==255)
		{
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
			return true;
		}
		else if(id==254)
		{
			IBlockState state = world.getBlockState(pos);
			if(state instanceof IExtendedBlockState)
			{
				state = state.getActualState(world, getPos());
				state = state.getBlock().getExtendedState(state, world, getPos());
				ImmersiveEngineering.proxy.removeStateFromSmartModelCache((IExtendedBlockState)state);
				ImmersiveEngineering.proxy.removeStateFromConnectionModelCache((IExtendedBlockState)state);
			}
			world.notifyBlockUpdate(pos, state, state, 3);
			return true;
		}
		return super.receiveClientEvent(id, arg);
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(nbt.hasKey("connectionList"))
			try
			{
				loadConnsFromNBT(nbt);
			} catch(Exception e)
			{
				IILogger.error("TileEntityIIConnectable encountered an error reading connection NBT.");
				IILogger.error(e.getStackTrace());
			}
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(descPacket)
			try
			{
				writeConnsToNBT(nbt);
			} catch(Exception e)
			{
				IILogger.error("TileEntityIIConnectable encountered an error writing connection NBT.");
				IILogger.error(e.getStackTrace());
			}
	}

	private void loadConnsFromNBT(NBTTagCompound nbt)
	{
		if(world!=null&&world.isRemote&&!Minecraft.getMinecraft().isSingleplayer()&&nbt!=null)
		{
			NBTTagList connectionList = nbt.getTagList("connectionList", 10);
			ImmersiveNetHandler.INSTANCE.clearConnectionsOriginatingFrom(Utils.toCC(this), world);
			for(int i = 0; i < connectionList.tagCount(); i++)
			{
				NBTTagCompound conTag = connectionList.getCompoundTagAt(i);
				Connection con = Connection.readFromNBT(conTag);
				if(con!=null)
					ImmersiveNetHandler.INSTANCE.addConnection(world, Utils.toCC(this), con);
				else
					IILogger.error("Client read connection as null from {}", nbt);
			}
		}
	}

	private void writeConnsToNBT(NBTTagCompound nbt)
	{
		if(world!=null&&!world.isRemote&&nbt!=null)
		{
			NBTTagList connectionList = new NBTTagList();
			Set<Connection> conL = ImmersiveNetHandler.INSTANCE.getConnections(world, Utils.toCC(this));
			if(conL!=null)
				for(Connection con : conL)
					connectionList.appendTag(con.writeToNBT());
			nbt.setTag("connectionList", connectionList);
		}
	}

	//--- Validation ---//

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		if(!world.isRemote)
			ImmersiveNetHandler.INSTANCE.addProxy(new IICProxy(this));
	}

	/**
	 * validates a tile entity
	 */
	@Override
	public void validate()
	{
		super.validate();
		if(!world.isRemote)
			ApiUtils.addFutureServerTask(world, () -> ImmersiveNetHandler.INSTANCE.onTEValidated(this));
	}

	/**
	 * invalidates a tile entity
	 */
	@Override
	public void invalidate()
	{
		super.invalidate();
		if(world.isRemote&&!Minecraft.getMinecraft().isSingleplayer())
			ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(pos, world, this, false);
	}

	//--- Wire System Implementation ---//

	@Override
	public boolean moveConnectionTo(Connection c, BlockPos newEnd)
	{
		return true;
	}

	@Override
	public void onEnergyPassthrough(int amount)
	{

	}

	@Override
	public boolean allowEnergyToPass(Connection con)
	{
		return true;
	}

	@Override
	public boolean canConnect()
	{
		return true;
	}

	@Override
	public boolean isEnergyOutput()
	{
		return false;
	}

	@Override
	public int outputEnergy(int amount, boolean simulate, int energyType)
	{
		return 0;
	}

	@Override
	public BlockPos getConnectionMaster(WireType cableType, TargetingInfo target)
	{
		return getPos();
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		if(!acceptsWireType(cableType))
			return false;
		return limitType==null||(this.isRelay()&&WireApi.canMix(limitType, cableType));
	}

	@Override
	public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other)
	{
		this.limitType = cableType;
	}

	@Override
	public WireType getCableLimiter(TargetingInfo target)
	{
		return this.limitType;
	}

	@Override
	public void removeCable(Connection connection)
	{
		WireType type = connection!=null?connection.cableType: null;
		Set<Connection> outputs = ImmersiveNetHandler.INSTANCE.getConnections(world, Utils.toCC(this));
		if(outputs==null||outputs.isEmpty())
			if(type==limitType||type==null)
				this.limitType = null;
		this.markDirty();
		if(world!=null)
		{
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override
	public void addAvailableEnergy(float amount, Consumer<Float> consume)
	{
		long currentTime = world.getTotalWorldTime();
		if(lastSourceUpdate!=currentTime)
		{
			sources.clear();
			Pair<Float, Consumer<Float>> own = getOwnEnergy();
			if(own!=null)
				sources.add(own);
			lastSourceUpdate = currentTime;
		}
		if(amount > 0&&consume!=null)
			sources.add(new ImmutablePair<>(amount, consume));
	}

	@Nullable
	protected Pair<Float, Consumer<Float>> getOwnEnergy()
	{
		return null;
	}

	@Override
	public float getDamageAmount(Entity e, Connection c)
	{
		float baseDmg = getBaseDamage(c);
		float max = (float)c.cableType.getTransferRate()/8*getBaseDamage(c);
		if(baseDmg==0||world.getTotalWorldTime()-lastSourceUpdate > 1)
			return 0;
		float damage = 0;
		for(int i = 0; i < sources.size()&&damage < max; i++)
		{
			int consume = (int)Math.min(sources.get(i).getLeft(), (max-damage)/baseDmg);
			damage += baseDmg*consume;
		}
		return damage;
	}

	@Override
	public void processDamage(Entity e, float amount, Connection c)
	{
		float baseDmg = getBaseDamage(c);
		float damage = 0;
		for(int i = 0; i < sources.size()&&damage < amount; i++)
		{
			float consume = Math.min(sources.get(i).getLeft(), (amount-damage)/baseDmg);
			sources.get(i).getRight().accept(consume);
			damage += baseDmg*consume;
			if(consume==sources.get(i).getLeft())
			{
				sources.remove(i);
				i--;
			}
		}
	}

	protected float getBaseDamage(Connection c)
	{
		if(c.cableType.isEnergyWire()&&c.cableType.canCauseDamage())
		{
			if(c.cableType.getElectricSource().level < 0)
				return 0;
			//Rough approximation of the original, but it's universal for any new cables
			return 8*(7f*c.cableType.getElectricSource().level-1.5f)/c.cableType.getTransferRate();
		}
		return 0;
	}

	//--- Render Range ---//

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		int inc = getRenderRadiusIncrease();
		return new AxisAlignedBB(this.pos.getX()-inc, this.pos.getY()-inc, this.pos.getZ()-inc, this.pos.getX()+inc+1, this.pos.getY()+inc+1, this.pos.getZ()+inc+1);
	}

	int getRenderRadiusIncrease()
	{
		return WireType.COPPER.getMaxLength();
	}
}
