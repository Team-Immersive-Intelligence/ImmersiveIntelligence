package pl.pabilo8.immersiveintelligence.common.util.sound;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.carversound.TimedCompoundSound;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.util.IIFileUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIFileUtils.ResourceException;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.sound.AdvancedSounds.MultiSound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class for storing sounds to be played during an animation
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 10.08.2022
 */
public class IISoundAnimation
{
	//--- Compiled ---//
	private final HashMap<Double, SoundEvent> sounds = new HashMap<>();
	private final HashMap<Double, Tuple<MultiSound, Double>> repeatedSounds = new HashMap<>();

	//--- Uncompiled ---//
	//Sounds played once at specific timeframe
	private final HashMap<Integer, SoundEvent[]> compiledSounds = new HashMap<>();
	//Repeated sounds starting at [Integer], and playing till Tuple.getB()
	private final HashMap<Integer, Tuple<MultiSound, Integer>> compiledRepeatedSounds = new HashMap<>();
	private final double animationDuration;

	public IISoundAnimation(double animationDuration)
	{
		this.animationDuration = animationDuration;
	}

	public IISoundAnimation(ResLoc location)
	{
		double duration = 1f;
		ResLoc fullRes = ResLoc.of(location.getResourceDomain()+":animations/"+location.getResourcePath()+".json");
		try
		{
			JsonObject json = IIFileUtils.readJSONFile(fullRes);

			//Parse duration
			if(json.has("duration"))
				duration = json.get("duration").getAsDouble();
			//Parse sounds
			if(json.has("sounds"))
			{
				JsonObject soundsObj = json.getAsJsonObject("sounds");

				soundsObj.entrySet().forEach(entry -> {
					//Get the element
					double time = Double.parseDouble(entry.getKey());
					JsonObject soundData = entry.getValue().getAsJsonObject();
					//Single sound
					if(soundData.has("sound"))
					{
						ResourceLocation res = new ResourceLocation(soundData.get("sound").getAsString());
						SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(res);
						if(soundEvent!=null)
							sounds.put(time, soundEvent);
						else
							IILogger.warn("Missing SoundEvent for sound animation "+fullRes+": "+res);
					}
					//Repeated compound sound
					else if(soundData.has("multisound"))
					{
						ResourceLocation res = new ResourceLocation(soundData.get("multisound").getAsString());
						double endTime = soundData.has("end")?soundData.get("end").getAsDouble(): time+0.1;

						MultiSound multiSound = IISounds.multiSounds.get(res);
						if(multiSound!=null)
							repeatedSounds.put(time, new Tuple<>(multiSound, endTime-time));
						else
							IILogger.warn("Missing MultiSound for sound animation "+fullRes+": "+res);
					}
				});
			}

		} catch(ResourceException e)
		{
			IILogger.error("[IISoundAnimation] Couldn't load sound animation "+fullRes+", "+e);
		}
		this.animationDuration = duration;
	}

	/**
	 * Compiles the 0-1 double values to integer timeframes based on animation duration
	 *
	 * @param maxTime animation duration
	 */
	public IISoundAnimation compile(int maxTime)
	{
		compiledSounds.clear();
		//Compile single sounds
		sounds.entrySet()
				.stream()
				.map(e -> new Tuple<>((int)(e.getKey()*maxTime), e.getValue()))
				.collect(Collectors.groupingBy(Tuple::getFirst))
				.forEach((time, sound) -> {
					SoundEvent[] sounds = sound.stream().map(Tuple::getSecond).toArray(SoundEvent[]::new);
					compiledSounds.put(time, sounds);
				});

		//Compile repeated sounds
		repeatedSounds.forEach((beginTime, sound) ->
				compiledRepeatedSounds.put(((int)(beginTime*maxTime)), new Tuple<>(
						sound.getFirst(),
						(int)(sound.getSecond()*maxTime)
				)));

		return this;
	}

	/**
	 * Puts a sound at a given keyframe
	 *
	 * @return this
	 */
	public IISoundAnimation withSound(double time, @Nonnull SoundEvent sound)
	{
		sounds.put(time/animationDuration, sound);
		return this;
	}

	/**
	 * Puts a repeated multi sound between keyframes
	 *
	 * @return this
	 */
	public IISoundAnimation withRepeatedSound(double begin, double end, @Nonnull MultiSound sound)
	{
		repeatedSounds.put(begin/animationDuration, new Tuple<>(sound, (end-begin)/animationDuration));
		return this;
	}

	//--- Manual sound getting ---//

	/**
	 * Gets sound(s) for time, requires compilation through {@link #compile(int)} first.
	 *
	 * @return the sounds for given time
	 */
	@Nullable
	public SoundEvent[] getSounds(int time)
	{
		return compiledSounds.get(time);
	}

	//--- Sound handling ---//

	public void handleSounds(SoundHandler soundStorage, int animationTime, float volume)
	{
		handleSingleSounds(soundStorage.getPosition(), animationTime, volume);
		handleRepeatedSounds(soundStorage.getCurrentPlayingList(), soundStorage.getPosition(), animationTime, volume);
	}

	@SideOnly(Side.CLIENT)
	protected void handleSingleSounds(BlockPos pos, int animationTime, float volume)
	{
		SoundEvent[] sounds = getSounds(animationTime);
		Minecraft mc = ClientUtils.mc();

		if(animationTime==0)
			return;

		if(sounds!=null)
			for(SoundEvent sound : sounds)
			{
				Vec3d vv = new Vec3d(pos).addVector(
						Utils.RAND.nextFloat(),
						Utils.RAND.nextFloat(),
						Utils.RAND.nextFloat()
				);
				mc.world.playSound(mc.player, vv.x, vv.y, vv.z, sound, SoundCategory.BLOCKS, volume, 1);
			}
	}

	@SideOnly(Side.CLIENT)
	protected void handleRepeatedSounds(List<TimedCompoundSound> current, BlockPos pos, int animationTime, float volume)
	{
		if(animationTime==0)
			return;

		//Play new sound
		Tuple<MultiSound, Integer> added = compiledRepeatedSounds.get(animationTime);
		if(added!=null)
		{
			TimedCompoundSound sound = new TimedCompoundSound(added.getFirst(), SoundCategory.BLOCKS, new Vec3d(pos), added.getSecond(), volume, 1);
			current.add(sound);
			sound.start();
		}

		//Remove sounds that ended playing
		current.removeIf(TimedCompoundSound::isDonePlaying);
		//Readjust timing
		//TODO: 21.04.2023 readjust timing
//		current.forEach(s -> s.readjustTime(s.getTime()));

		//TODO: 21.04.2023 set volume
		//current.forEach(s -> s.volu);
	}

}
