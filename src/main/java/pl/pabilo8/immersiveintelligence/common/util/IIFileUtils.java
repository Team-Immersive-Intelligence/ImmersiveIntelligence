package pl.pabilo8.immersiveintelligence.common.util;

import com.google.gson.*;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.structure.template.Template;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Used for loading content from files inside the jar / resource packs.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 19.05.2024
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class IIFileUtils
{
	/**
	 * Reads a file from the given resource location.
	 *
	 * @param res Resource location of the file
	 * @return The content of the file
	 * @throws ResourceException Thrown when the file could not be loaded
	 */
	public static String readFile(ResLoc res) throws ResourceException
	{
		IResource resource = getResource(res);
		try
		{
			return resource.getInputStream().toString();
		} catch(Exception e)
		{
			throw new ResourceException("Couldn't read file");
		}
	}

	/**
	 * Reads a JSON file from the given resource location.
	 *
	 * @param res Resource location of the file
	 * @return The content of the file
	 * @throws ResourceException Thrown when the file could not be loaded
	 */
	public static JsonObject readJSONFile(ResLoc res) throws ResourceException
	{
		IResource resource = getResource(res);
		InputStream stream = resource.getInputStream();

		try
		{
			JsonElement next = new JsonStreamParser(new InputStreamReader(stream)).next();
			try
			{
				return next.getAsJsonObject();
			} catch(Exception e)
			{
				throw new ResourceException("Invalid JSON structure");
			}
		} catch(JsonParseException e)
		{
			throw new ResourceException("Couldn't parse file");
		}
	}

	private static IResource getResource(ResLoc res) throws ResourceException
	{
		try
		{
			return Minecraft.getMinecraft().getResourceManager().getResource(res);
		} catch(IOException e)
		{
			throw new ResourceException("File not found");
		}
	}
	//TODO: 19.05.2024 re-add

	/**
	 * Reads a block template file from the given resource location.
	 *
	 * @param res Resource location of the file
	 * @return The content of the file
	 * @throws ResourceException Thrown when the file could not be loaded
	 **/
	public static Template readTemplateFile(ResLoc res) throws ResourceException
	{
		return null;
	}

	/**
	 * Turns a json [x,y,z] array into Vec3D
	 */
	public static Vec3d jsonToVec3d(JsonArray array)
	{
		return new Vec3d(
				array.get(0).getAsNumber().doubleValue(),
				array.get(1).getAsNumber().doubleValue(),
				array.get(2).getAsNumber().doubleValue()
		);
	}

	/**
	 * Thrown when a resource couldn't be loaded.
	 */
	public static class ResourceException extends Exception
	{
		public ResourceException(String message)
		{
			super(message);
		}
	}
}
