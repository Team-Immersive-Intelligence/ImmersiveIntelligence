package pl.pabilo8.immersiveintelligence.client.util.tmt;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
@Deprecated
public class TmtUtil
{
	public static float AngleToTMT(float angle)
	{
		return angle/57.29578f;
	}

	public static float TMTToAngle(float angle)
	{
		return angle*57.29578f;
	}
}
