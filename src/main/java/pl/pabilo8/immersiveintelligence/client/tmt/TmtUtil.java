package pl.pabilo8.immersiveintelligence.client.tmt;

/**
 * Created by Pabilo8 on 2019-06-01.
 */
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

	public static double[] getOffsetPos(double x, double y, double z, double offset_x, double offset_y, double offset_z, double rotx, double roty, double rotz)
	{
		//They didn't teach me trigonometry in school yet :/ (maybe it's time to learn on my own)
		//Source: https://math.stackexchange.com/a/2213821
		x = x+offset_x*Math.cos(roty)*Math.cos(rotz)-offset_x*Math.sin(rotx)*Math.sin(roty)*Math.sin(rotz)-offset_y*Math.cos(rotx)*Math.sin(rotz)+offset_z*Math.sin(roty)*Math.cos(rotz)+offset_z*Math.sin(rotx)*Math.cos(roty)*Math.sin(rotz);
		y = y+offset_x*Math.cos(roty)*Math.sin(rotz)+offset_x*Math.sin(rotx)*Math.sin(roty)*Math.cos(rotz)+offset_y*Math.cos(rotx)*Math.cos(rotz)+offset_z*Math.sin(roty)*Math.sin(rotz)-offset_z*Math.sin(rotx)*Math.cos(roty)*Math.cos(rotz);
		z = z-offset_x*Math.cos(roty)*Math.sin(roty)+offset_y*Math.sin(rotx)+offset_z*Math.cos(rotx)*Math.cos(roty);
		return new double[]{x, y, z};
	}
}
