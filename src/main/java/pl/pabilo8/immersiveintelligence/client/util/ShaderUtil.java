package pl.pabilo8.immersiveintelligence.client.util;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import static pl.pabilo8.immersiveintelligence.client.util.ShaderUtil.Shaders.*;

/**
 * @author Pabilo8
 * @author Flaxbeard
 */
public class ShaderUtil
{
	private static final int VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB;
	private static final int FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;

	public static void init()
	{
		createShader(ALPHA, null, "alpha");
		createShader(BLUEPRINT, null, "blueprint");
		createShader(COLOR, null, "color");
	}

	/**
	 * "Blueprint" fragment shader, used for construction animations
	 *
	 * @param av    alpha value
	 * @param ticks 0-1 partial ticks
	 */
	@Deprecated
	public static boolean useBlueprint(float av, float ticks)
	{
		return useShader(BLUEPRINT, av, ticks);
	}

	/**
	 * Alpha fragment shader
	 *
	 * @param av alpha value
	 */
	@Deprecated
	public static boolean useAlpha(float av)
	{
		return useShader(ALPHA, av);
	}

	/**
	 * Color fragment shader
	 *
	 * @param r red value (0-1)
	 * @param g green value (0-1)
	 * @param b blue value (0-1)
	 */
	@Deprecated
	public static boolean useColor(float r, float g, float b)
	{
		return useShader(COLOR, r, g, b);
	}

	/**
	 * @param shader     shader to be used
	 * @param parameters floats passed to the shader
	 * @return whether the elements should be rendered, or will shader make them effectively transparent (which will make it wasteful)
	 */
	public static boolean useShader(@Nullable Shaders shader, @Nonnull float... parameters)
	{
		if(shader==null||!shader.use())
			return false;

		switch(shader)
		{
			case BLUEPRINT:
				shader.setFloat("alpha", parameters[0]);
				shader.setFloat("ticks", parameters[1]);
				break;
			case ALPHA:
				shader.setFloat("alpha", parameters[0]);
				break;
			case COLOR:
				COLOR.setVec3("color", parameters[0], parameters[1], parameters[2]);
				break;
		}
		return true;
	}

	public static boolean useShader(@Nullable Shaders shaders, @Nonnull Float[] parameters)
	{
		return useShader(shaders, ArrayUtils.toPrimitive(parameters));
	}

	public static void releaseShader()
	{
		ARBShaderObjects.glUseProgramObjectARB(0);
	}

	public enum Shaders implements ISerializableEnum
	{
		ALPHA,
		BLUEPRINT,
		COLOR;

		private int programID, fragID, vertID;

		//--- Utility ---//

		boolean use()
		{
			if(programID <= 0)
				return false;
			ARBShaderObjects.glUseProgramObjectARB(programID);
			return true;
		}

		int getRef(String name)
		{
			return ARBShaderObjects.glGetUniformLocationARB(programID, name);
		}

		void setFloat(String name, float value)
		{
			ARBShaderObjects.glUniform1fARB(getRef(name), value);
		}

		void setVec3(String name, float x, float y, float z)
		{
			ARBShaderObjects.glUniform3fARB(getRef(name), x, y, z);
		}

		public static Shaders getByName(String name)
		{
			return Arrays.stream(Shaders.values())
					.filter(shaders -> shaders.getName().equals(name))
					.findFirst().orElse(ALPHA);
		}
	}

	//--- Shader loading methods ---//

	private static void createShader(Shaders shader, @Nullable String vert, @Nullable String frag)
	{
		//Attempt loading the shader
		if(frag!=null)
			shader.fragID = createShader(frag, FRAG);
		if(vert!=null)
			shader.vertID = createShader(vert, VERT);

		//Create the program, its ID will be referenced when calling the shader
		shader.programID = ARBShaderObjects.glCreateProgramObjectARB();

		//Unable to get a program ID
		if(shader.programID==0)
			return;

		//Attach shader(s) to the program
		if(frag!=null)
			ARBShaderObjects.glAttachObjectARB(shader.programID, shader.fragID);
		if(vert!=null)
			ARBShaderObjects.glAttachObjectARB(shader.programID, shader.vertID);

		//Check if linked to program succesfully
		ARBShaderObjects.glLinkProgramARB(shader.programID);
		if(ARBShaderObjects.glGetObjectParameteriARB(shader.programID, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB)==GL11.GL_FALSE)
		{
			IILogger.error("Shader Error: "+getLogInfo(shader.programID));
			return;
		}

		//Validate the shader code
		if(ARBShaderObjects.glGetObjectParameteriARB(shader.programID, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB)==GL11.GL_FALSE)
		{
			IILogger.error("Shader Error: "+getLogInfo(shader.programID));
		}

		IILogger.info(String.format("Succesfully loaded shader '%s'", shader.getName()));
	}

	private static int createShader(String filename, int shaderType)
	{
		int shader = 0;
		try
		{
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

			if(shader==0)
				return 0;

			ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(String.format("/assets/immersiveintelligence/shaders/%s.frag", filename)));
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

		try(BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)))
		{
			return reader.lines().collect(Collectors.joining("\n"));
		}
	}

	private static String getLogInfo(int obj)
	{
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
}