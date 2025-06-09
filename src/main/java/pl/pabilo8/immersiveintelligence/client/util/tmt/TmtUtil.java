package pl.pabilo8.immersiveintelligence.client.util.tmt;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 01.06.2019
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
