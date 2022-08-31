package pl.pabilo8.immersiveintelligence.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage.IIMessageHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.*;

/**
 * @author Pabilo8
 * @since 2019-05-26
 */
public class IIPacketHandler
{
	/**
	 * Huge thanks to Choonster: <a href="https://www.minecraftforge.net/forum/topic/36769-18-the-received-string-length-is-longer-than-maximum-allowed-27-gt-20/">
	 * https://www.minecraftforge.net/forum/topic/36769-18-the-received-string-length-is-longer-than-maximum-allowed-27-gt-20/</a><br>
	 * According mc forum, the length of the SimpleNetworkWrapper name mustn't be longer than 20 letters<br>
	 * Immersive Intelligence happens to have 21...<br>
	 */
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("immersiveintelli");
	private static int messageID = 0;

	public static void preInit()
	{
		registerMessage(MessageItemScrollableSwitch.class, false, true);
		registerMessage(MessageBooleanAnimatedPartsSync.class, true, true);
		registerMessage(MessageGuiNBT.class, false, true);
		registerMessage(MessageEntityNBTSync.class, true, true);
		registerMessage(MessageFireworks.class, true, false);
		registerMessage(MessageRotaryPowerSync.class, true, false);
		registerMessage(MessageBlockDamageSync.class, true, false);
		registerMessage(MessagePlayerAimAnimationSync.class, true, false);
		registerMessage(MessageItemKeybind.class, true, true);
		registerMessage(MessageExplosion.class, true, false);
		registerMessage(MessageParticleEffect.class, true, false);
		registerMessage(MessageBeginMachineUpgrade.class, true, true);
		registerMessage(MessageParticleGunfire.class, true, false);
		registerMessage(MessageManualClose.class, false, true);
	}

	private static <T extends IIMessage> void registerMessage(Class<T> message, boolean clientSide, boolean serverSide)
	{
		//handles both sides
		IIMessageHandler<T> handler = new IIMessageHandler<>();
		if(clientSide)
			INSTANCE.registerMessage(handler, message, messageID++, Side.CLIENT);
		if(serverSide)
			INSTANCE.registerMessage(handler, message, messageID++, Side.SERVER);
	}
}
