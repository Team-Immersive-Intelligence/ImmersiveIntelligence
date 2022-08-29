package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IUpgradableMachine;

public class MessageBeginMachineUpgrade implements IMessage
{
	int entityID;
	BlockPos pos;
	boolean install;
	String machineID;

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
	public void fromBytes(ByteBuf buf)
	{
		this.entityID = buf.readInt();
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		install = buf.readBoolean();
		this.machineID = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(entityID);
		buf.writeInt(pos.getX()).writeInt(pos.getY()).writeInt(pos.getZ());
		buf.writeBoolean(install);
		ByteBufUtils.writeUTF8String(buf, machineID);
	}

	public static class HandlerServer implements IMessageHandler<MessageBeginMachineUpgrade, IMessage>
	{
		@Override
		public IMessage onMessage(MessageBeginMachineUpgrade message, MessageContext ctx)
		{
			WorldServer world = ctx.getServerHandler().player.getServerWorld();
			world.addScheduledTask(() -> {
				Entity entity = world.getEntityByID(message.entityID);
				if(entity instanceof EntityLivingBase)
				{
					if(world.isBlockLoaded(message.pos))
					{
						TileEntity tile = world.getTileEntity(message.pos);
						MachineUpgrade upgrade = MachineUpgrade.getUpgradeByID(message.machineID);
						if(message.install)
						{
							if(tile instanceof IUpgradableMachine&&((IUpgradableMachine)tile).getInstallProgress()==0&&upgrade!=null&&((IUpgradableMachine)tile).upgradeMatches(upgrade))
							{
								IItemHandler capability = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
								if(capability!=null)
								{
									if(!(entity instanceof EntityPlayer&&((EntityPlayer)entity).isCreative()))
									{
										for(IngredientStack requiredStack : upgrade.getRequiredStacks())
										{
											int reqSize = requiredStack.inputSize;
											for(int slot = 0; slot < capability.getSlots(); slot++)
											{
												ItemStack inSlot = capability.getStackInSlot(slot);
												if(!inSlot.isEmpty()&&requiredStack.matchesItemStackIgnoringSize(inSlot))
												{
													int ii = Math.min(inSlot.getCount(),reqSize);
													capability.extractItem(slot,ii,false);
													if((reqSize -= ii) <= 0)
														break;
												}
											}
											if(reqSize>0)
												return;
										}
									}

									((IUpgradableMachine)tile).startUpgrade(upgrade);
									IIPacketHandler.INSTANCE.sendToAllTracking(message, Utils.targetPointFromTile(tile, 32));
								}
							}
						}
						else
						{
							if(tile instanceof IUpgradableMachine)
							{
								((IUpgradableMachine)tile).removeUpgrade(upgrade);
								IIPacketHandler.INSTANCE.sendToAllTracking(message, Utils.targetPointFromTile(tile, 32));
							}
						}
					}
				}

			});
			return null;
		}
	}

	public static class HandlerClient implements IMessageHandler<MessageBeginMachineUpgrade, IMessage>
	{
		@Override
		public IMessage onMessage(MessageBeginMachineUpgrade message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(() -> {
				World world = ImmersiveEngineering.proxy.getClientWorld();
				if (world!=null) // This can happen if the task is scheduled right before leaving the world
				{
					TileEntity tile = world.getTileEntity(message.pos);
					MachineUpgrade upgrade = MachineUpgrade.getUpgradeByID(message.machineID);
					if(message.install)
					{
						if(tile instanceof IUpgradableMachine&&((IUpgradableMachine)tile).getInstallProgress()==0&&upgrade!=null&&((IUpgradableMachine)tile).upgradeMatches(upgrade))
							((IUpgradableMachine)tile).startUpgrade(upgrade);
					}
					else if(tile instanceof IUpgradableMachine)
						((IUpgradableMachine)tile).removeUpgrade(upgrade);
				}
			});
			return null;
		}
	}
}