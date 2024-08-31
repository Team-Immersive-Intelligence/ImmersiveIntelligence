package pl.pabilo8.immersiveintelligence.common.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage.IIMessageHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.*;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.MultiSound;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.RangedSound;

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
	public static int DEFAULT_RANGE = 40;

	public static void preInit()
	{
		registerMessage(MessageItemScrollableSwitch.class, false, true);
		registerMessage(MessageBooleanAnimatedPartsSync.class, true, true);
		registerMessage(MessageChatInfo.class, true, false);
		registerMessage(MessageIITileSync.class, true, true);
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
		registerMessage(MessagePlayIISound.class, true, false);
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

	//--- Message Sending Utils ---//

	public static void sendToServer(IIMessage message)
	{
		INSTANCE.sendToServer(message);
	}

	public static void sendToClient(IIMessage message)
	{
		if(message instanceof IPositionBoundMessage)
		{
			IPositionBoundMessage posMessage = (IPositionBoundMessage)message;
			INSTANCE.sendToAllAround(message, targetPointFromPos(posMessage.getPosition(), posMessage.getWorld(), DEFAULT_RANGE));
		}
		else if(message instanceof IEntityBoundMessage)
			INSTANCE.sendToAllTracking(message, targetPointFromEntity(((IEntityBoundMessage)message).getEntity(), DEFAULT_RANGE));
		else
			IILogger.error("Attempt to send a message without a valid position or entity!");
	}

	public static void sendToClient(BlockPos pos, World world, IIMessage message)
	{
		INSTANCE.sendToAllTracking(message, targetPointFromPos(pos, world, DEFAULT_RANGE));
	}

	public static void sendToClient(Vec3d pos, World world, IIMessage message)
	{
		INSTANCE.sendToAllTracking(message, targetPointFromPos(pos, world, DEFAULT_RANGE));
	}

	public static void sendToClient(Entity entity, IIMessage message)
	{
		INSTANCE.sendToAllTracking(message, entity);
	}

	public static void sendToClient(TileEntity tile, IIMessage message)
	{
		INSTANCE.sendToAllTracking(message, targetPointFromTile(tile, DEFAULT_RANGE));
	}

	public static void sendChatInfo(EntityPlayer player, ITextComponent... components)
	{
		INSTANCE.sendTo(new MessageChatInfo(components), ((EntityPlayerMP)player));
	}

	public static void sendChatString(EntityPlayer player, String string)
	{
		sendChatInfo(player, new TextComponentString(string));
	}

	public static void sendChatTranslation(EntityPlayer player, String translation, Object... args)
	{
		sendChatInfo(player, new TextComponentTranslation(translation, args));
	}

	public static void playMultiSound(World world, Vec3d pos, MultiSound sound, SoundCategory category, int distance, int time, float volume, float pitch)
	{
		INSTANCE.sendToAllAround(new MessagePlayIISound(sound, category, time, pos, volume, pitch), targetPointFromPos(pos, world, distance));
	}

	public static void playRangedSound(World world, Vec3d pos, RangedSound sound, SoundCategory category, int distance, float volume, float pitch)
	{
		INSTANCE.sendToAllAround(new MessagePlayIISound(sound, category, distance, pos, volume, pitch), targetPointFromPos(pos, world, distance));
	}

	//--- TargetPoint Utils ---//

	public static TargetPoint targetPointFromPos(BlockPos pos, World world, int range)
	{
		return new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
	}

	public static TargetPoint targetPointFromPos(Vec3d pos, World world, int range)
	{
		return new TargetPoint(world.provider.getDimension(), pos.x, pos.y, pos.z, range);
	}

	public static TargetPoint targetPointFromEntity(Entity entity, int range)
	{
		return new TargetPoint(entity.world.provider.getDimension(), entity.posX, entity.posY, entity.posZ, range);
	}

	public static TargetPoint targetPointFromTile(TileEntity tile, int range)
	{
		return new TargetPoint(tile.getWorld().provider.getDimension(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), range);
	}
}
