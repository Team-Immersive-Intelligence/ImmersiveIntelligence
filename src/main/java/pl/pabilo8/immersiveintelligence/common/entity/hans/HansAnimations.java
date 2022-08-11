package pl.pabilo8.immersiveintelligence.common.entity.hans;

import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.common.entity.EntityHans;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 14.05.2021
 */
public class HansAnimations
{
	public static void putMouthShape(EntityHans entityHans, char c, double start, double end)
	{
		MouthShapes shape = getShapeFromChar(c);
		if(shape!=null)
			entityHans.mouthShapeQueue.add(new Tuple<>((int)((end-start)*20), shape));
	}

	public enum EyeEmotions
	{
		NEUTRAL(0, 0, 1),
		FROWNING(-0.25, -0.25, 1),
		SURPRISED(0.25, 0.25, 1),
		HAPPY(0.0625, 0.0625, 0.75),
		SUSPICIOUS(0, 0, 0.25),
		SLEEPY(0.0625, -0.0625, 0.25);

		public final double eyebrowHeightDiffRight;
		public final double eyebrowHeightDiffLeft;
		public final double eyebrowThickness;

		EyeEmotions(double eyebrowHeightDiffRight, double eyebrowHeightDiffLeft, double eyebrowThickness)
		{
			this.eyebrowHeightDiffRight = eyebrowHeightDiffRight;
			this.eyebrowHeightDiffLeft = eyebrowHeightDiffLeft;
			this.eyebrowThickness = eyebrowThickness;
		}
	}

	public enum MouthEmotions
	{
		NEUTRAL(0, 0, 0, 0),
		ANGRY(0.75, 0, 0.5, 0),
		HAPPY(0.5, 0.125, 0.25, 0.5),
		LAUGHING(0.75, 0.125, 0.5, 0.5);

		public final double lipBottomOffset;
		public final double lipBottomWidth;
		public final double lipTopOffset;
		public final double lipTopWidth;

		MouthEmotions(double lipBottomOffset, double lipBottomWidth, double lipTopOffset, double lipTopWidth)
		{
			this.lipBottomOffset = lipBottomOffset;
			this.lipBottomWidth = lipBottomWidth;
			this.lipTopOffset = lipTopOffset;
			this.lipTopWidth = lipTopWidth;
		}
	}

	public enum MouthShapes
	{
		CLOSED('A', 0, 1, 0, 1, false, 0),
		SLIGHTLY_OPEN('B', 0.55, 1, 0.55, 1, false, 0),
		OPEN('C', 0.75, 1.25, 0.75, 1.25, false, 0),
		WIDE_OPEN('D', 1, 1.5, 1, 1.5, false, 0),
		SLIGHTLY_ROUNDED('E', 0.75, 0.75, 0.75, 0.75, false, 0),
		PUCKERED_LIPS('F', 0.5, 0.6, 0.5, 0.6, false, 0),
		UPPER_TEETH_CLENCHING('G', 0, 1, 0.45, 1, true, 0),
		L_SHAPE('H', 0.85, 1.35, 0.85, 1.35, false, 1),
		SPEAKING_PAUSE('X', 0.25, 1.25, 0.25, 1.25, false, 0);

		private final char alphabeticName;
		public final double lipBottomOffset;
		public final double lipBottomWidth;
		public final double lipTopOffset;
		public final double lipTopWidth;
		public final double tongueHeight;
		public final boolean upperTeethVisible;

		MouthShapes(char c, double lipBottomOffset, double lipBottomWidth, double lipTopOffset, double lipTopWidth, boolean upperTeethVisible, double tongueHeight)
		{
			this.alphabeticName = c;
			this.lipBottomOffset = lipBottomOffset;
			this.lipBottomWidth = lipBottomWidth;
			this.lipTopOffset = lipTopOffset;
			this.lipTopWidth = lipTopWidth;
			this.tongueHeight = tongueHeight;
			this.upperTeethVisible = upperTeethVisible;
		}

		public static MouthShapes v(String s)
		{
			String ss = s.toUpperCase();
			return Arrays.stream(values()).filter(e -> e.name().equals(ss)).findFirst().orElse(CLOSED);
		}
	}

	@Nullable
	private static MouthShapes getShapeFromChar(char c)
	{
		for(MouthShapes shape : MouthShapes.values())
		{
			if(shape.alphabeticName==c)
				return shape;
		}
		return null;
	}

	public static double[] getEyeEmotionInBetween(EyeEmotions e1, EyeEmotions e2, double percent)
	{
		return new double[]{
				MathHelper.clampedLerp(e1.eyebrowHeightDiffRight, e2.eyebrowHeightDiffRight, percent*2.5),
				MathHelper.clampedLerp(e1.eyebrowHeightDiffLeft, e2.eyebrowHeightDiffLeft, percent*2.5),
				MathHelper.clampedLerp(e1.eyebrowThickness, e2.eyebrowThickness, percent*2.5)
		};
	}

	public static double[] getMouthShapeInBetween(MouthShapes e1, MouthShapes e2, MouthEmotions e3, double percent)
	{
		return new double[]{
				MathHelper.clampedLerp(e1.lipBottomOffset, e2.lipBottomOffset, percent*2)+e3.lipBottomOffset,
				MathHelper.clampedLerp(e1.lipBottomWidth, e2.lipBottomWidth, percent*2)+e3.lipBottomWidth,
				MathHelper.clampedLerp(e1.lipTopOffset, e2.lipTopOffset, percent*2)+e3.lipTopOffset,
				MathHelper.clampedLerp(e1.lipTopWidth, e2.lipTopWidth, percent*2)+e3.lipTopWidth,
				MathHelper.clampedLerp(e1.tongueHeight, e2.tongueHeight, percent*2)
		};
	}

	public enum HansLegAnimation
	{
		STANDING(1),
		SNEAKING(1),
		KNEELING(0),
		SQUATTING(0),
		LYING(0.5f, 0.625f, 0.8f),
		KAZACHOK(0.5f),
		SWIMMING(1f);

		public final float walkSpeedModifier;
		public final float aabbWidth, aabbHeight;

		HansLegAnimation(float walkSpeedModifier)
		{
			this(walkSpeedModifier, 0.625f, 1.8f);
		}

		HansLegAnimation(float walkSpeedModifier, float aabbWidth, float aabbHeight)
		{
			this.walkSpeedModifier = walkSpeedModifier;
			this.aabbWidth = aabbWidth;
			this.aabbHeight = aabbHeight;
		}
	}

	public enum HansArmAnimation
	{
		NORMAL,
		SALUTE,
		SURRENDER,

		SQUAD_ORDER_HALT,
		SQUAD_ORDER_COVER_ME,

		SQUAD_ORDER_ONWARDS,
		SQUAD_ORDER_FALLBACK,
		SQUAD_ORDER_RIGHT,
		SQUAD_ORDER_LEFT
	}
}
