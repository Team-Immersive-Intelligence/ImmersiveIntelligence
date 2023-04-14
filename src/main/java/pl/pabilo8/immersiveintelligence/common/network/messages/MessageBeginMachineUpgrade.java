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
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;

public class MessageBeginMachineUpgrade extends IIMessage
{
	private int entityID;
	private BlockPos pos;
	private boolean install;
	private String machineID;

	public MessageBeginMachineUpgrade(TileEntity tile, String machineID, Entity user, boolean install)
	{
		this.entityID = user.getEntityId();
		this.pos = tile.getPos();
		this.install = install;
		this.machineID = machineID;
	}

	public MessageBeginMachineUpgrade()
	{

	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{
		Entity entity = world.getEntityByID(this.entityID);

		if(!(entity instanceof EntityLivingBase)||!world.isBlockLoaded(this.pos))
			return;

		TileEntity tile = world.getTileEntity(this.pos);
		MachineUpgrade upgrade = MachineUpgrade.getUpgradeByID(this.machineID);
		if(tile instanceof IUpgradableMachine)
		{
			IUpgradableMachine machine = (IUpgradableMachine)tile;
			if((machine.getInstallProgress()==0&&upgrade!=null&&machine.upgradeMatches(upgrade)))
			{
				if(this.install)
				{
					IItemHandler capability = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

					if(capability==null)
						return;

					if(!(entity instanceof EntityPlayer&&((EntityPlayer)entity).isCreative()))
					{
						//check if ingredients are sufficient
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
					}
					machine.startUpgrade(upgrade);
				}
				else
					machine.removeUpgrade(upgrade);
				IIPacketHandler.INSTANCE.sendToAllTracking(this, IIPacketHandler.targetPointFromTile(tile, 32));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		if(world!=null) // This can happen if the task is scheduled right before leaving the world
		{
			TileEntity tile = world.getTileEntity(this.pos);
			MachineUpgrade upgrade = MachineUpgrade.getUpgradeByID(this.machineID);

			if(!(tile instanceof IUpgradableMachine))
				return;
			IUpgradableMachine machine = (IUpgradableMachine)tile;

			if(this.install)
			{
				if(machine.getInstallProgress()==0&&upgrade!=null&&machine.upgradeMatches(upgrade))
					machine.startUpgrade(upgrade);
			}
			else
				machine.removeUpgrade(upgrade);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityID = buf.readInt();
		this.pos = readPos(buf);
		this.install = buf.readBoolean();
		this.machineID = readString(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(entityID);
		writePos(buf, pos);
		buf.writeBoolean(install);
		writeString(buf, machineID);
	}
}