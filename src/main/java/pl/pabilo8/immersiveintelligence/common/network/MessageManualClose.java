package pl.pabilo8.immersiveintelligence.common.network;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;

/**
 * @author Pabilo8
 * @since 20.07.2021
 */
public class MessageManualClose implements IMessage
{
	public String skin = null;

	public MessageManualClose(String skin)
	{
		this.skin = skin;
	}

	public MessageManualClose()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.skin = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, skin);
	}

	public static class HandlerServer implements IMessageHandler<MessageManualClose, IMessage>
	{
		@Override
		public IMessage onMessage(MessageManualClose message, MessageContext ctx)
		{
			ctx.getServerHandler().player.mcServer.addScheduledTask(() ->
			{
				EntityPlayerMP player = ctx.getServerHandler().player;
				if(player!=null)
				{

					ItemStack mainItem = player.getHeldItemMainhand();
					ItemStack offItem = player.getHeldItemOffhand();

					boolean main = !mainItem.isEmpty()&&mainItem.getItem()==IEContent.itemTool&&mainItem.getItemDamage()==3;
					boolean off = !offItem.isEmpty()&&offItem.getItem()==IEContent.itemTool&&offItem.getItemDamage()==3;
					ItemStack target = main?mainItem: offItem;

					if(main||off)
					{

						if((message.skin==null||message.skin.isEmpty())&&ItemNBTHelper.hasKey(target, "lastSkin"))
						{
							ItemNBTHelper.remove(target, "lastSkin");
						}
						else if(message.skin!=null)
						{
							ItemNBTHelper.setString(target, "lastSkin", message.skin);
						}
					}
				}

			});
			return null;
		}
	}
}
