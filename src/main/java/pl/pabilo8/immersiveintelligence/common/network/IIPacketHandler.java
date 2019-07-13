package pl.pabilo8.immersiveintelligence.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Pabilo8 on 2019-05-26.
 */
public class IIPacketHandler
{
	/*
	Huge thanks to Choonster: https://www.minecraftforge.net/forum/topic/36769-18-the-received-string-length-is-longer-than-maximum-allowed-27-gt-20/
	According mc forum, the lenght of the SimpleNetworkWrapper name mustn't be longer than 20 letters
	Immersive Intelligence happens to have 21...
	Really, what's even the reason for that?!
	 */
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("immersiveintelli");

	public static void preInit()
	{
		INSTANCE.registerMessage(MessageChestSync.Handler.class, MessageChestSync.class, 0, Side.CLIENT);
		INSTANCE.registerMessage(MessageAlarmSirenSync.Handler.class, MessageAlarmSirenSync.class, 1, Side.CLIENT);
		INSTANCE.registerMessage(MessageItemScrollableSwitch.Handler.class, MessageItemScrollableSwitch.class, 2, Side.SERVER);
		INSTANCE.registerMessage(MessageBooleanAnimatedPartsSync.HandlerClient.class, MessageBooleanAnimatedPartsSync.class, 3, Side.CLIENT);
		INSTANCE.registerMessage(MessageBooleanAnimatedPartsSync.HandlerServer.class, MessageBooleanAnimatedPartsSync.class, 4, Side.SERVER);
		INSTANCE.registerMessage(MessageGuiNBT.HandlerServer.class, MessageGuiNBT.class, 6, Side.SERVER);
	}
}
