package pl.pabilo8.immersiveintelligence.common.network.messages;

import blusunrize.immersiveengineering.client.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.carversound.RangedCompoundSound;
import pl.pabilo8.immersiveintelligence.client.util.carversound.TimedCompoundSound;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.network.IIMessage;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.MultiSound;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.RangedSound;

/**
 * @author Pabilo8
 * @since 23.09.2022
 */
public class MessagePlayIISound extends IIMessage
{
	//type dependant
	private RangedSound soundRanged;
	private MultiSound soundCompound;
	private Type type;

	private SoundCategory category;
	private int duration;
	private int distance;

	private Vec3d pos;
	private float volume, pitch;

	public MessagePlayIISound(MultiSound sound, SoundCategory category, int duration, Vec3d pos, float volume, float pitch)
	{
		this.soundCompound = sound;
		this.category = category;
		this.duration = duration;

		this.type = Type.COMPOUND;

		this.pos = pos;
		this.volume = volume;
		this.pitch = pitch;
	}

	public MessagePlayIISound(RangedSound sound, SoundCategory category, int fullDistance, Vec3d pos, float volume, float pitch)
	{
		this.soundRanged = sound;
		this.category = category;
		this.distance=fullDistance;

		this.type = Type.RANGED;

		this.pos = pos;
		this.volume = volume;
		this.pitch = pitch;
	}

	public MessagePlayIISound()
	{

	}

	@Override
	protected void onServerReceive(WorldServer world, NetHandlerPlayServer handler)
	{

	}

	@SideOnly(Side.CLIENT)
	@Override
	protected void onClientReceive(WorldClient world, NetHandlerPlayClient handler)
	{
		//empty or invalid
		if(soundCompound==null&&soundRanged==null)
			return;

		switch(type)
		{
			case COMPOUND:
				ClientUtils.mc().getSoundHandler().playSound(new TimedCompoundSound(soundCompound, category, pos, duration, volume, pitch));
				break;
			case RANGED:
				ClientUtils.mc().getSoundHandler().playSound(new RangedCompoundSound(soundRanged,distance, category, pos, volume, pitch));
				break;
		}
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.type = Type.values()[buf.readInt()];
		this.category = SoundCategory.values()[buf.readInt()];
		this.pos = readVec3(buf);
		this.volume = buf.readFloat();
		this.pitch = buf.readFloat();

		switch(type)
		{
			case RANGED:
				soundRanged = IISounds.rangedSounds.get(buf.readInt());
				distance = buf.readInt();
				break;
			case COMPOUND:
				soundCompound = IISounds.multiSounds.get(buf.readInt());
				duration = buf.readInt();
				break;
		}

	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		//all types
		buf.writeInt(type.ordinal());
		buf.writeInt(category.ordinal());
		writeVec3(buf, pos);
		buf.writeFloat(volume);
		buf.writeFloat(pitch);

		//type dependant
		switch(type)
		{
			case RANGED:
				buf.writeInt(soundRanged.id);
				buf.writeInt(distance);
				break;
			case COMPOUND:
				buf.writeInt(soundCompound.id);
				buf.writeInt(duration);
				break;

		}
	}

	private enum Type
	{
		RANGED,
		COMPOUND
	}
}
