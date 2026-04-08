package pl.pabilo8.immersiveintelligence.common.compat;

import net.minecraft.server.MinecraftServer;
import pl.pabilo8.immersiveintelligence.common.IILogger;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.07.2022
 */
public class FluidloggedAPIHelper extends IICompatModule
{
	@Override
	public String getName()
	{
		return "fluidlogged_api";
	}

	@Override
	public void preInit()
	{
		//TODO: 08.04.2026 remake this the correct way, once our dev environment becomes compatible with Fluidlogged-API
		File dir = new File(configurationDirectory, "fluidlogged_api/internal/immersiveintelligence/");
		if(dir.exists()||dir.mkdirs())
			try(InputStream stream = MinecraftServer.class.getResourceAsStream("/assets/immersiveintelligence/ii_fluidlogged.json"))
			{
				assert stream!=null;
				File output = new File(dir, "whitelist.jsonc");
				if(output.exists()||output.createNewFile())
				{
					IILogger.info("Adding Fluidlogged-API config file.");
					FileWriter writer = new FileWriter(output);
					writer.write(stream.read());
					writer.close();
				}
			} catch(Exception e)
			{
				IILogger.error("Could not add Fluidlogged-API config file, "+e.getMessage());
			}
	}

	@Override
	public void registerRecipes()
	{

	}

	@Override
	public void init()
	{

	}

	@Override
	public void postInit()
	{

	}
}
