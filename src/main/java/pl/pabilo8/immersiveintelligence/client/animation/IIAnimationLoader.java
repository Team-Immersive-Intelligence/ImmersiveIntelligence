package pl.pabilo8.immersiveintelligence.client.animation;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.models.IESmartObjModel;
import blusunrize.immersiveengineering.client.models.obj.IEOBJModel;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Pabilo8
 * @since 05.04.2022
 */
public class IIAnimationLoader
{
	// TODO: 06.04.2022 more safety (?)
	public static JsonObject readFileToJSON(@Nonnull ResourceLocation res)
	{
		try
		{
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(res);
			JsonElement object = new JsonStreamParser(new InputStreamReader(resource.getInputStream())).next();
			return object.getAsJsonObject();
		} catch(IOException ignored)
		{

		}
		return new JsonObject();
	}

	public static IIAnimation loadAnimation(@Nonnull ResourceLocation res)
	{
		ResourceLocation fullRes = new ResourceLocation(res.getResourceDomain(), "animations/"+res.getResourcePath()+".json");

		return new IIAnimation(res, readFileToJSON(fullRes));
	}

	public static IIModelHeader loadHeader(@Nonnull IBakedModel model)
	{
		IEOBJModel ieobjModel = (IEOBJModel)((IESmartObjModel)model).getModel();
		ResourceLocation res = ieobjModel.getResourceLocation();
		ResourceLocation fullRes = new ResourceLocation(res.getResourceDomain(), res.getResourcePath().replace(".obj.ie", ".obj.amt"));

		return loadHeader(fullRes);
	}

	public static IIModelHeader loadHeader(@Nonnull ResourceLocation res)
	{
		return new IIModelHeader(readFileToJSON(res));
	}

	// TODO: 22.07.2022 make it work
	public static void preloadTexturesFromMTL(@Nonnull ResourceLocation res)
	{
		try
		{
			//try to register each texture, used by direct model loading
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(res);
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			reader.lines()
					.filter(s -> s.startsWith("map_Kd"))
					.map(s -> s.split(" ")[1])
					.forEach(s -> ApiUtils.getRegisterSprite(ClientUtils.mc().getTextureMapBlocks(), s+".png"));


		} catch(IOException ignored)
		{

		}

	}
}
