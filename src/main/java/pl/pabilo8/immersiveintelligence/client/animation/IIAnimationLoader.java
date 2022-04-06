package pl.pabilo8.immersiveintelligence.client.animation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Pabilo8
 * @since 05.04.2022
 */
public class IIAnimationLoader
{
	// TODO: 06.04.2022 more safety (?)
	public static JsonObject readFileToJSON(ResourceLocation res)
	{
		try
		{
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(res);
			JsonElement object = new JsonStreamParser(new InputStreamReader(resource.getInputStream())).next();
			return object.getAsJsonObject();
		}
		catch(IOException e)
		{

		}
		return new JsonObject();
	}

	public static IIAnimation loadAnimation(ResourceLocation res)
	{
		ResourceLocation fullRes = new ResourceLocation(res.getResourceDomain(), "animations/"+res.getResourcePath()+".json");

		return new IIAnimation(res, readFileToJSON(fullRes));
	}
}
