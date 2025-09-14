package pl.pabilo8.immersiveintelligence.common.util;

import com.google.gson.*;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.FMLFolderResourcePack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

	/**
	 * Retrieves all resource packs available to the client.
	 * Uses reflection to access the resourcePackList field in FMLClientHandler.
	 *
	 * @return List of IResourcePack
	 * @throws ResourceException if unable to access the resource pack list
	 */
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public static List<IResourcePack> getAllResourcePacks() throws ResourceException
	{
		try
		{
			Field field = FMLClientHandler.class.getDeclaredField("resourcePackList");
			field.setAccessible(true);
			return (List<IResourcePack>)field.get(FMLClientHandler.instance());
		} catch(Exception e)
		{
			throw new ResourceException("Couldn't get resource pack list");
		}
	}

	/**
	 * Finds all resource locations in a given resource pack under a folder, filtered by a predicate.
	 * Handles LegacyV2Adapter, FolderResourcePack, and FileResourcePack types.
	 *
	 * @param pack      Resource pack to search
	 * @param folder    Folder path inside the resource pack
	 * @param predicate Predicate to filter file names
	 * @return List of ResourceLocation
	 * @throws ResourceException if unable to access resources
	 */
	@SideOnly(Side.CLIENT)
	public static List<ResourceLocation> getLocationsInResourcePack(IResourcePack pack, String folder, Predicate<String> predicate) throws ResourceException
	{
		// Handle LegacyV2Adapter by unwrapping
		if(pack instanceof LegacyV2Adapter)
		{
			LegacyV2Adapter adapter = (LegacyV2Adapter)pack;
			for(Field field : adapter.getClass().getDeclaredFields())
				if(field.getType()==IResourcePack.class)
				{
					field.setAccessible(true);
					try
					{
						return getLocationsInResourcePack((IResourcePack)field.get(adapter), folder, predicate);
					} catch(Exception ignored) {}
					break;
				}
		}
		List<ResourceLocation> locations = new ArrayList<>();
		if(pack instanceof FolderResourcePack)
			handleFolderResourcePack((FolderResourcePack)pack, folder, predicate, locations);
		else if(pack instanceof FileResourcePack)
			handleZipResourcePack((FileResourcePack)pack, folder, predicate, locations);
		return locations;
	}

	/**
	 * Handles resource packs stored as folders, adding matching resource locations to the list.
	 *
	 * @param folderPack FolderResourcePack instance
	 * @param folder     Folder path inside the resource pack
	 * @param predicate  Predicate to filter file names
	 * @param locations  List to add found ResourceLocations
	 * @throws ResourceException if unable to access the folder pack
	 */
	@SideOnly(Side.CLIENT)
	private static void handleFolderResourcePack(FolderResourcePack folderPack, String folder, Predicate<String> predicate, List<ResourceLocation> locations) throws ResourceException
	{
		Field fileField = null;
		for(Field field : AbstractResourcePack.class.getDeclaredFields())
			if(field.getType()==File.class)
			{
				fileField = field;
				break;
			}
		if(fileField!=null)
		{
			fileField.setAccessible(true);
			try
			{
				File file = (File)fileField.get(folderPack);
				Set<String> domains = folderPack.getResourceDomains();
				if(folderPack instanceof FMLFolderResourcePack)
					domains.add(((FMLFolderResourcePack)folderPack).getFMLContainer().getModId());
				for(String domain : domains)
				{
					File pathFile = new File(file, "assets/"+domain+"/"+folder);
					enumerateFiles(folderPack, pathFile, predicate, locations, domain, folder);
				}
			} catch(IllegalAccessException e)
			{
				throw new ResourceException("Couldn't access folder pack");
			}
		}
	}

	/**
	 * Recursively enumerates files in a folder resource pack, adding matching resource locations.
	 *
	 * @param folderPack FolderResourcePack instance
	 * @param parent     Parent directory
	 * @param predicate  Predicate to filter file names
	 * @param locations  List to add found ResourceLocations
	 * @param domain     Resource domain
	 * @param prefix     Path prefix inside the domain
	 */
	@SideOnly(Side.CLIENT)
	private static void enumerateFiles(FolderResourcePack folderPack, File parent, Predicate<String> predicate, List<ResourceLocation> locations, String domain, String prefix) throws ResourceException
	{
		File[] files = parent.listFiles();
		if(files==null) return;
		for(File file : files)
			if(file.isFile()&&predicate.test(file.getName()))
				locations.add(new ResourceLocation(domain, prefix+"/"+file.getName()));
			else if(file.isDirectory())
				enumerateFiles(folderPack, file, predicate, locations, domain, prefix+"/"+file.getName());
	}

	/**
	 * Handles resource packs stored as zip files, adding matching resource locations to the list.
	 *
	 * @param filePack  FileResourcePack instance
	 * @param folder    Folder path inside the resource pack
	 * @param predicate Predicate to filter file names
	 * @param locations List to add found ResourceLocations
	 * @throws ResourceException if unable to access the zip file
	 */
	@SideOnly(Side.CLIENT)
	private static void handleZipResourcePack(FileResourcePack filePack, String folder, Predicate<String> predicate, List<ResourceLocation> locations) throws ResourceException
	{
		for(Field field : FileResourcePack.class.getDeclaredFields())
			if(field.getType()==ZipFile.class)
			{
				field.setAccessible(true);
				try
				{
					enumerateZipFile(filePack, folder, (ZipFile)field.get(filePack), predicate, locations);
				} catch(IllegalAccessException e)
				{
					throw new ResourceException("Couldn't read zip file");
				}
				break;
			}
	}

	/**
	 * Enumerates entries in a zip resource pack, adding matching resource locations.
	 *
	 * @param filePack  FileResourcePack instance
	 * @param folder    Folder path inside the resource pack
	 * @param file      ZipFile instance
	 * @param predicate Predicate to filter file names
	 * @param locations List to add found ResourceLocations
	 */
	@SideOnly(Side.CLIENT)
	private static void enumerateZipFile(FileResourcePack filePack, String folder, ZipFile file, Predicate<String> predicate, List<ResourceLocation> locations) throws ResourceException
	{
		Set<String> domains = filePack.getResourceDomains();
		Enumeration<? extends ZipEntry> it = file.entries();
		while(it.hasMoreElements())
		{
			String name = it.nextElement().getName();
			for(String domain : domains)
			{
				String assets = "assets/"+domain+"/";
				String path = assets+folder+"/";
				if(name.startsWith(path)&&predicate.test(name.substring(path.length())))
					locations.add(new ResourceLocation(domain, name.substring(assets.length())));
			}
		}
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
