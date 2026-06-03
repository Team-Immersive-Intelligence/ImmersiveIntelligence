package pl.pabilo8.immersiveintelligence.client.util.amt;

import blusunrize.immersiveengineering.api.ApiUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.model.TextureRecoloringRegistry;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.amt.AMTModelHeader;
import pl.pabilo8.immersiveintelligence.common.util.amt.IIAnimation;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handles loading of AMT animations for client and server side.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.04.2022
 */
public class AMTLoader
{
	@SideOnly(Side.CLIENT)
	public static JsonObject readFileToJSON(@Nonnull ResourceLocation res, String description)
	{
		try
		{
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(res);
			InputStream stream = resource.getInputStream();
			JsonElement object = new JsonStreamParser(new InputStreamReader(stream)).next();
			return object.getAsJsonObject();
		} catch(Exception exception)
		{
			IILogger.error("[AMT] Couldn't load "+
					description+" "+
					TextFormatting.GOLD+
					res.toString()
							.replaceFirst("animations/", "")
							.replaceFirst(".json", "")+
					TextFormatting.RESET+
					", "+exception.getClass().getCanonicalName());
		}
		return new JsonObject();
	}

	/**
	 * This reads a structure template from the given location and stores it.
	 * This first attempts get the template from an external folder.
	 * If it isn't there then it attempts to take it from the minecraft jar.
	 */
	public static JsonObject readServerFileToJson(@Nonnull ResourceLocation res, String description)
	{
		try
		{
			InputStream stream = MinecraftServer.class.getResourceAsStream("/assets/"+res.getResourceDomain()+"/"+res.getResourcePath());
			assert stream!=null;
			return ((JsonObject)new JsonStreamParser(new InputStreamReader(stream)).next());
		} catch(Exception exception)
		{
			IILogger.error("[AMT/Server] Couldn't load "+
					description+" "+
					TextFormatting.GOLD+
					res.toString()
							.replaceFirst(".json", "")+
					TextFormatting.RESET+
					", "+exception.getClass().getCanonicalName());
		}
		return new JsonObject();
	}

	@SideOnly(Side.CLIENT)
	public static IIAnimation loadAnimation(@Nonnull ResourceLocation res)
	{
		ResourceLocation fullRes = new ResourceLocation(res.getResourceDomain(), "animations/"+res.getResourcePath()+".json");
		return new IIAnimation(res, readFileToJSON(fullRes, "animation"));
	}

	public static IIAnimation loadAnimationServer(@Nonnull ResourceLocation res)
	{
		ResourceLocation fullRes = new ResourceLocation(res.getResourceDomain(), "animations/"+res.getResourcePath()+".json");
		return new IIAnimation(res, readServerFileToJson(fullRes, "animation"));
	}


	@SideOnly(Side.CLIENT)
	public static AMTModelHeader loadHeader(@Nonnull OBJModel model)
	{
		ResourceLocation res = ObfuscationReflectionHelper.getPrivateValue(OBJModel.class, model, "modelLocation");
		ResLoc fullRes = ResLoc.of(res).withExtension(ResLoc.EXT_OBJAMT);
		return loadHeader(fullRes);
	}

	@SideOnly(Side.CLIENT)
	public static AMTModelHeader loadHeader(@Nonnull ResourceLocation res)
	{
		return new AMTModelHeader(readFileToJSON(res, "model header"));
	}

	public static AMTModelHeader loadHeaderServer(@Nonnull ResourceLocation res)
	{
		return new AMTModelHeader(readServerFileToJson(res, "animation"));
	}

	@SideOnly(Side.CLIENT)
	public static HashMap<String, ResourceLocation> loadMTL(@Nonnull ResourceLocation res)
	{
		HashMap<String, ResourceLocation> map = new HashMap<>();
		try
		{
			//try to register each texture, used by direct model loading
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(res);
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

			String line, lastMaterial = null;
			while((line = reader.readLine())!=null)
			{
				String[] split = line.split(" ");
				if(split.length < 2)
					continue;

				switch(split[0])
				{
					case "newmtl":
						lastMaterial = split[1];
						break;
					case "map_Kd":
					{
						if(lastMaterial==null)
							throw new IOException("No material defined before map_Kd");
						map.put(lastMaterial, new ResourceLocation(split[1]));
						lastMaterial = null;
					}
					break;
					default:
						break;
				}
			}
		} catch(IOException exception)
		{
			IILogger.error("[AMT] Couldn't load MTL file "+
					TextFormatting.GOLD+
					res.toString()
							.replaceFirst("models/", "")+
					TextFormatting.RESET+
					", "+exception.getClass().getCanonicalName());
		}
		return map;
	}

	@SideOnly(Side.CLIENT)
	public static void preloadTexturesFromOBJ(@Nonnull ResourceLocation obj, TextureMap map)
	{
		ArrayList<String> mtlNames = new ArrayList<>();
		try
		{
			//try to register each texture, used by direct model loading
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(obj);
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			String line;

			search:
			while((line = reader.readLine())!=null)
			{
				String[] split = line.split(" ");
				if(split.length < 2)
					continue;

				switch(split[0])
				{
					case "mtllib":
						mtlNames.add(split[1]);
						break;
					case "o":
					case "v":
					case "vt":
						break search;
					default:
						break;
				}
			}
			ResLoc directory = ResLoc.of(obj).asDirectory();
			mtlNames.forEach(s -> preloadTexturesFromMTL(ResLoc.of(directory, s).withExtension(ResLoc.EXT_MTL), map));

		} catch(IOException exception)
		{
			IILogger.error("[AMT] Couldn't load OBJ file in search of materials :"+
					TextFormatting.GOLD+
					obj.toString()
							.replaceFirst("models/", "")+
					TextFormatting.RESET+
					", "+exception.getClass().getCanonicalName());
		}
	}

	@SideOnly(Side.CLIENT)
	public static void preloadTexturesFromMTL(@Nonnull ResourceLocation mtl, TextureMap map)
	{
		loadMTL(mtl).values().forEach(res -> {
			ApiUtils.getRegisterSprite(map, res);
			//Register paintable textures
			if(res.getResourcePath().contains("_painted"))
				TextureRecoloringRegistry.registerRecolorableTexture(res);

		});
	}
}
