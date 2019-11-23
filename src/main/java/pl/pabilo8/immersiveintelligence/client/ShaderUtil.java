package pl.pabilo8.immersiveintelligence.client;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * @author Flaxbeard
 */
public class ShaderUtil
{
	private static final int VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB;
	private static final int FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;

	public static int blueprint = 0;

	public static void init()
	{
		blueprint = createShader(null, "/assets/immersiveintelligence/shaders/blueprint.frag");
	}

	public static void blueprint_static(float av, float ticks)
	{
		ARBShaderObjects.glUseProgramObjectARB(blueprint);
		int a = ARBShaderObjects.glGetUniformLocationARB(blueprint, "alpha");
		ARBShaderObjects.glUniform1fARB(a, av);
		a = ARBShaderObjects.glGetUniformLocationARB(blueprint, "time");
		ARBShaderObjects.glUniform1fARB(a, ticks);
	}

	public static void releaseShader()
	{
		ARBShaderObjects.glUseProgramObjectARB(0);
	}

	private static int createShader(String vert, String frag)
	{
		int fragid = 0;
		int vertid = 0;

		if(frag!=null)
		{
			fragid = createShader(frag, FRAG);
		}
		if(vert!=null)
		{
			vertid = createShader(vert, VERT);
		}

		int program = ARBShaderObjects.glCreateProgramObjectARB();
		if(program==0)
		{
			return 0;
		}

		if(frag!=null)
		{
			ARBShaderObjects.glAttachObjectARB(program, fragid);
		}

		if(vert!=null)
		{
			ARBShaderObjects.glAttachObjectARB(program, vertid);
		}

		ARBShaderObjects.glLinkProgramARB(program);
		if(ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB)==GL11.GL_FALSE)
		{
			ImmersiveIntelligence.logger.error("Shader Error: "+getLogInfo(program));
			return 0;
		}

		if(ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB)==GL11.GL_FALSE)
		{
			ImmersiveIntelligence.logger.error("Shader Error: "+getLogInfo(program));
			return 0;
		}

		return program;
	}

	private static int createShader(String filename, int shaderType)
	{
		int shader = 0;
		try
		{
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

			if(shader==0)
			{
				return 0;
			}

			ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
			ARBShaderObjects.glCompileShaderARB(shader);

			if(ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB)==GL11.GL_FALSE)
				throw new RuntimeException("Error creating shader: "+getLogInfo(shader));

			return shader;
		} catch(Exception e)
		{
			ARBShaderObjects.glDeleteObjectARB(shader);
			e.printStackTrace();
			return -1;
		}
	}

	private static String readFileAsString(String filename) throws Exception
	{
		InputStream in = ShaderUtil.class.getResourceAsStream(filename);

		if(in==null)
			return "";

		try(BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8")))
		{
			return reader.lines().collect(Collectors.joining("\n"));
		}
	}

	private static String getLogInfo(int obj)
	{
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
}