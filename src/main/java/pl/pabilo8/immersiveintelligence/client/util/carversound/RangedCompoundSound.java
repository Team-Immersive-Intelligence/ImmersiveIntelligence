package pl.pabilo8.immersiveintelligence.client.util.carversound;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.RangedSound;

import java.util.Comparator;

/**
 * A quick, non-repeating sound with different variants depending on distance
 *
 * @author Pabilo8
 * @since 21.09.2022
 */
@SideOnly(Side.CLIENT)
public class RangedCompoundSound extends PositionedSound
{
	public RangedCompoundSound(RangedSound sound, int fullDistance, SoundCategory category, Vec3d pos, float volume, float pitch)
	{
		super(getValidSound(sound, pos, fullDistance), category);
		xPosF = (float)pos.x;
		yPosF = (float)pos.y;
		zPosF = (float)pos.z;

		//calculate the fraction of full distance
		double distance = ClientUtils.mc().player.getPositionVector().distanceTo(pos)/fullDistance;

		this.volume = volume*(float)(0.25+0.75*distance);
		this.pitch = pitch;
		repeat = false;
		attenuationType=AttenuationType.NONE;
	}

	private static SoundEvent getValidSound(RangedSound sound, Vec3d pos, int fullDistance)
	{
		//calculate the fraction of full distance
		double distance = ClientUtils.mc().player.getPositionVector().distanceTo(pos)/fullDistance;

		//ohno, is lambda expression again
		return sound.getSounds().stream()
				//filter sounds for further distances
				.filter(e -> e.getFirst() < distance)
				//sort matching sounds, use negative for reverse order
				.sorted(Comparator.comparingDouble(e -> -e.getFirst()))
				.map(Tuple::getSecond)
				.findFirst()
				.orElse(null);
	}

	@Override
	public float getVolume()
	{
		return super.getVolume();
	}
}
