package pl.pabilo8.immersiveintelligence.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Pabilo8
 * @since 2019-05-26
 */
public class IIPacketHandler
{
	/**
	 Huge thanks to Choonster: <a href="https://www.minecraftforge.net/forum/topic/36769-18-the-received-string-length-is-longer-than-maximum-allowed-27-gt-20/">
	 https://www.minecraftforge.net/forum/topic/36769-18-the-received-string-length-is-longer-than-maximum-allowed-27-gt-20/</a><br>
	 According mc forum, the length of the SimpleNetworkWrapper name mustn't be longer than 20 letters<br>
	 Immersive Intelligence happens to have 21...<br>
	 */
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("immersiveintelli");

	public static void preInit()
	{
		INSTANCE.registerMessage(MessageAlarmSirenSync.Handler.class, MessageAlarmSirenSync.class, 0, Side.CLIENT);
		INSTANCE.registerMessage(MessageItemScrollableSwitch.Handler.class, MessageItemScrollableSwitch.class, 1, Side.SERVER);
		INSTANCE.registerMessage(MessageBooleanAnimatedPartsSync.HandlerClient.class, MessageBooleanAnimatedPartsSync.class, 2, Side.CLIENT);
		INSTANCE.registerMessage(MessageBooleanAnimatedPartsSync.HandlerServer.class, MessageBooleanAnimatedPartsSync.class, 3, Side.SERVER);
		INSTANCE.registerMessage(MessageGuiNBT.HandlerServer.class, MessageGuiNBT.class, 4, Side.SERVER);
		INSTANCE.registerMessage(MessageEntityNBTSync.HandlerClient.class, MessageEntityNBTSync.class, 5, Side.CLIENT);
		INSTANCE.registerMessage(MessageEntityNBTSync.HandlerServer.class, MessageEntityNBTSync.class, 6, Side.SERVER);
		INSTANCE.registerMessage(MessageFireworks.HandlerClient.class, MessageFireworks.class, 7, Side.CLIENT);
		INSTANCE.registerMessage(MessageRotaryPowerSync.HandlerClient.class, MessageRotaryPowerSync.class, 8, Side.CLIENT);
		INSTANCE.registerMessage(MessageBlockDamageSync.HandlerClient.class, MessageBlockDamageSync.class, 9, Side.CLIENT);
		INSTANCE.registerMessage(MessageProgrammableSpeakerSync.Handler.class, MessageProgrammableSpeakerSync.class, 10, Side.CLIENT);
		INSTANCE.registerMessage(MessagePlayerAimAnimationSync.HandlerClient.class, MessagePlayerAimAnimationSync.class, 11, Side.CLIENT);
		INSTANCE.registerMessage(MessageItemKeybind.Handler.class, MessageItemKeybind.class, 12, Side.SERVER);
		INSTANCE.registerMessage(MessageExplosion.HandlerClient.class, MessageExplosion.class, 13, Side.CLIENT);
		INSTANCE.registerMessage(MessageParticleEffect.HandlerClient.class, MessageParticleEffect.class, 14, Side.CLIENT);
		INSTANCE.registerMessage(MessageBeginMachineUpgrade.HandlerClient.class, MessageBeginMachineUpgrade.class, 15, Side.CLIENT);
		INSTANCE.registerMessage(MessageBeginMachineUpgrade.HandlerServer.class, MessageBeginMachineUpgrade.class, 16, Side.SERVER);
		INSTANCE.registerMessage(MessageParticleGunfire.HandlerClient.class, MessageParticleGunfire.class, 17, Side.CLIENT);
		INSTANCE.registerMessage(MessageManualClose.HandlerServer.class, MessageManualClose.class, 18, Side.SERVER);
	}
}
