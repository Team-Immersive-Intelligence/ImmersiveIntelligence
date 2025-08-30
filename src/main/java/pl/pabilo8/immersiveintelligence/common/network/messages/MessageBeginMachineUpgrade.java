package pl.pabilo8.immersiveintelligence.common.network.messages;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade.IUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade.UpgradeUtils.UpgradeOperation;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

public class MessageBeginMachineUpgrade extends IIMessage implements IPositionBoundMessage
{
	private Upgrade upgrade;
	private int installingUserID;
	private World world;
	private BlockPos pos;
	private boolean install;

	public MessageBeginMachineUpgrade(TileEntity tile, Upgrade upgrade, Entity user, boolean install)
	{
		this.installingUserID = user.getEntityId();
		this.pos = tile.getPos();
		this.install = install;
		this.upgrade = upgrade;
	}

	public MessageBeginMachineUpgrade()
	{

	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		Entity entity = world.getEntityByID(this.installingUserID);
		if(!(entity instanceof EntityLivingBase)||!world.isBlockLoaded(this.pos))
			return;

		TileEntity tile = world.getTileEntity(this.pos);
		if(!(tile instanceof IUpgradableDevice))
			return;

		IUpgradableDevice machine = (IUpgradableDevice)tile;
		if(!this.install)
			machine.removeUpgrade(upgrade);
		else if(machine.addUpgrade(upgrade, UpgradeOperation.PROBE))
		{
			IItemHandler capability = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(capability==null)
				return;

			//check if ingredients are sufficient
			if(!(entity instanceof EntityPlayer&&((EntityPlayer)entity).isCreative()))
				for(IngredientStack requiredStack : upgrade.getRequiredStacks())
				{
					int reqSize = requiredStack.inputSize;
					for(int slot = 0; slot < capability.getSlots(); slot++)
					{
						ItemStack inSlot = capability.getStackInSlot(slot);
						if(!inSlot.isEmpty()&&requiredStack.matchesItemStackIgnoringSize(inSlot))
						{
							int ii = Math.min(inSlot.getCount(), reqSize);
							capability.extractItem(slot, ii, false);
							if((reqSize -= ii) <= 0)
								break;
						}
					}
					if(reqSize > 0)
						return;
				}
			machine.addUpgrade(upgrade, UpgradeOperation.INSTALL);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		if(world!=null) // This can happen if the task is scheduled right before leaving the world
		{
			TileEntity tile = world.getTileEntity(this.pos);
			if(!(tile instanceof IUpgradableDevice))
				return;
			IUpgradableDevice machine = (IUpgradableDevice)tile;

			if(!install)
				machine.removeUpgrade(upgrade);
			else
				machine.addUpgrade(upgrade, UpgradeOperation.INSTALL);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.installingUserID = buf.readInt();
		this.pos = readPos(buf);
		this.install = buf.readBoolean();
		this.upgrade = Upgrade.getUpgradeByID(ResLoc.of(readString(buf)));
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(installingUserID);
		writePos(buf, pos);
		buf.writeBoolean(install);
		writeString(buf, upgrade.getId().toString());
	}

	@Override
	public World getWorld()
	{
		return world;
	}

	@Override
	public Vec3d getPosition()
	{
		return new Vec3d(pos);
	}
}
