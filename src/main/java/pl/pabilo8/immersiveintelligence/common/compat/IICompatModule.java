package pl.pabilo8.immersiveintelligence.common.compat;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import pl.pabilo8.immersiveintelligence.Config.IIConfig;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.compat.it.ImmersiveTechnologyHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 20.09.2020
 */
public abstract class IICompatModule
{
	public static HashMap<String, Class<? extends IICompatModule>> moduleClasses = new HashMap<>();
	public static HashMap<String, String> moduleMinModVersions = new HashMap<>();
	public static Set<IICompatModule> modules = new HashSet<>();

	public static boolean serene = false;
	public static boolean ii = false;

	static
	{
		moduleClasses.put("immersivepetroleum", ImmersivePetroleumHelper.class);
		moduleClasses.put("immersiveposts", ImmersivePostsHelper.class);
		moduleClasses.put("immersivetech", ImmersiveTechnologyHelper.class);
		moduleMinModVersions.put("immersivetech", "1.7.50");
		//it is the only IE addon which has a _ between words
		moduleClasses.put("immersive_energy", ImmersiveEnergyHelper.class);
		moduleClasses.put("tfc", TerrafirmaHelper.class);
		moduleClasses.put("baubles", BaublesHelper.class);
	}

	public static void doModulesPreInit()
	{
		for(Entry<String, Class<? extends IICompatModule>> e : moduleClasses.entrySet())
			if(Loader.isModLoaded(e.getKey()))
				try
				{
					Boolean enabled = IIConfig.compat.get(e.getKey());

					ImmersiveIntelligence.logger.info(e.getKey()+Utils.getModVersion(e.getKey()));

					if(moduleMinModVersions.containsKey(e.getKey())&&
							new DefaultArtifactVersion(moduleMinModVersions.get(e.getKey()))
									.compareTo(new DefaultArtifactVersion(Utils.getModVersion(e.getKey()))) >= 0
					)
					{
						ImmersiveIntelligence.logger.info("Consider updating %s, II adds additional compat for the new version", e.getKey());
						continue;
					}
					if(enabled==null||!enabled)
						continue;
					IICompatModule m = e.getValue().newInstance();
					modules.add(m);
					m.preInit();
				}
				catch(Exception exception)
				{
					ImmersiveIntelligence.logger.error("Compat module for "+e.getKey()+" could not be preInitialized. Report this and include the error message below!", exception);
				}
	}

	public static void doModulesRecipes()
	{
		for(IICompatModule compat : IICompatModule.modules)
			try
			{
				compat.registerRecipes();
			}
			catch(Exception exception)
			{
				ImmersiveIntelligence.logger.error("Compat module for "+compat+" could not register recipes. Report this and include the error message below!", exception);
			}
	}

	public static void doModulesInit()
	{
		for(IICompatModule compat : IICompatModule.modules)
			try
			{
				compat.init();
			}
			catch(Exception exception)
			{
				ImmersiveIntelligence.logger.error("Compat module for "+compat+" could not be initialized. Report this and include the error message below!", exception);
			}
	}

	public static void doModulesPostInit()
	{
		for(IICompatModule compat : IICompatModule.modules)
			try
			{
				compat.postInit();
			}
			catch(Exception exception)
			{
				ImmersiveIntelligence.logger.error("Compat module for "+compat+" could not be postInitialized. Report this and include the error message below!", exception);
			}
	}

	//We don't want this to happen multiple times after all >_>
	public static boolean serverStartingDone = false;

	public static void doModulesLoadComplete()
	{
		if(!serverStartingDone)
		{
			serverStartingDone = true;
			for(IICompatModule compat : IICompatModule.modules)
				try
				{
					compat.loadComplete();

				}
				catch(Exception exception)
				{
					ImmersiveIntelligence.logger.error("Compat module for "+compat+" could not be initialized. Report this and include the error message below!", exception);
				}
		}
	}

	public abstract void preInit();

	public abstract void registerRecipes();

	public abstract void init();

	public abstract void postInit();

	public void loadComplete()
	{
	}

	@SideOnly(Side.CLIENT)
	public void clientPreInit()
	{
	}

	@SideOnly(Side.CLIENT)
	public void clientInit()
	{
	}

	@SideOnly(Side.CLIENT)
	public void clientPostInit()
	{
	}
}
