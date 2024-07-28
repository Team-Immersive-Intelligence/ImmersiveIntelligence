package pl.pabilo8.immersiveintelligence.client.render.gas;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.*;
import pl.pabilo8.immersiveintelligence.common.IILogger;

import java.io.File;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;

public class GasShader
{
	private int computeShader;
	private int computeProgram;
	private int gasProgram;
	private int pos;



	public GasShader()
	{
		try {
			pos = GL45.glCreateBuffers();
			GL45.glNamedBufferData(pos, 2 * Float.SIZE, GL15.GL_DYNAMIC_DRAW);
			GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 0, pos);

			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream is = cl.getResourceAsStream("shaders/volumetric.glsl");
			String compute = IOUtils.toString(is, StandardCharsets.UTF_8);

			computeShader = GL20.glCreateShader(ARBComputeShader.GL_COMPUTE_SHADER);
			GL20.glShaderSource(computeShader, compute);
			GL20.glCompileShader(computeShader);
			IILogger.info("Shader info: "+GL20.glGetShaderInfoLog(computeShader, 512));

			computeProgram = GL20.glCreateProgram();
			GL20.glAttachShader(computeProgram, computeShader);
			GL20.glLinkProgram(computeProgram);
			GL20.glDeleteShader(computeShader);
			IILogger.info("Program info: "+GL20.glGetProgramInfoLog(computeProgram, 512));

		} catch(Exception e) {
			IILogger.error("GasShader error! " + e);
		}
	}

	public void update()
	{
		GL20.glUseProgram(computeProgram);
		ARBComputeShader.glDispatchCompute(100, 1, 1);

	}
}