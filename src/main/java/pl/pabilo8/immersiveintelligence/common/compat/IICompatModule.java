package pl.pabilo8.immersiveintelligence.common.compat;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.compat.it.ImmersiveTechnologyHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Pabilo8
 * @since 20.09.2020
 */
public abstract class IICompatModule
{
	public static HashMap<String, Class<? extends IICompatModule>> moduleClasses = new HashMap<>();
	public static HashMap<String, String> moduleMinModVersions = new HashMap<>();
	public static Set<IICompatModule> modules = new HashSet<>();

	public static boolean serene = false, baubles = false, petroleum = false;

	static
	{
		moduleClasses.put("immersiveengineering", ImmersiveEngineeringHelper.class);
		moduleClasses.put("immersivepetroleum", ImmersivePetroleumHelper.class);
		moduleClasses.put("immersiveposts", ImmersivePostsHelper.class);
		moduleClasses.put("immersivetech", ImmersiveTechnologyHelper.class);
		moduleMinModVersions.put("immersivetech", "1.7.50");
		//it is the only IE addon which has a _ between words
		moduleClasses.put("immersive_energy", ImmersiveEnergyHelper.class);
		moduleClasses.put("tfc", TerrafirmaHelper.class);
		moduleClasses.put("baubles", BaublesHelper.class);
		moduleClasses.put("opencomputers", OpenComputersHelper.class);
		moduleClasses.put("computercraft", ComputerCraftHelper.class);
	}

	public static void doModulesPreInit()
	{
		for(Entry<String, Class<? extends IICompatModule>> e : moduleClasses.entrySet())
			if(Loader.isModLoaded(e.getKey()))
				try
				{
					Boolean enabled = IIConfig.compat.get(e.getKey());
					IILogger.info(e.getKey()+Utils.getModVersion(e.getKey()));

					if(moduleMinModVersions.containsKey(e.getKey())&&
							new DefaultArtifactVersion(moduleMinModVersions.get(e.getKey()))
									.compareTo(new DefaultArtifactVersion(Utils.getModVersion(e.getKey()))) >= 0
					)
					{
						IILogger.info("Consider updating "+e.getKey()+", II adds additional compat for the new version");
						continue;
					}
					if(enabled==null||!enabled)
						continue;
					IICompatModule m = e.getValue().newInstance();
					modules.add(m);
					m.preInit();
				} catch(Exception exception)
				{
					IILogger.error("Compat module for "+e.getKey()+" could not be pre-initialized. Report this and include the error message below!", exception);
				}
	}

	private static void doModuleAction(Consumer<IICompatModule> action, String message)
	{
		for(IICompatModule compat : IICompatModule.modules)
			try
			{
				action.accept(compat);
			} catch(Exception exception)
			{
				IILogger.error("Compat module for %s %s. Report this and include the error message below!", compat, message);
				IILogger.error(exception);
			}
	}

	//--- Event callers ---//

	public static void doModulesRecipes()
	{
		doModuleAction(IICompatModule::registerRecipes, "could not register recipes");
	}

	public static void doModulesInit()
	{
		doModuleAction(IICompatModule::init, "could not be initialized");
	}

	public static void doModulesPostInit()
	{
		doModuleAction(IICompatModule::postInit, "could not be post-initialized");
	}

	public static void doModulesClientPreInit()
	{
		doModuleAction(IICompatModule::clientPreInit, "could not pre-initialize on client side");
	}

	public static void doModulesClientInit()
	{
		doModuleAction(IICompatModule::clientInit, "could not be initialized on client side");
	}

	public static void doModulesClientPostInit()
	{
		doModuleAction(IICompatModule::clientPostInit, "could not be post-initialized on client side");
	}

	//We don't want this to happen multiple times after all >_>
	public static boolean serverStartingDone = false;

	public static void doModulesLoadComplete()
	{
		if(!serverStartingDone)
		{
			serverStartingDone = true;
			doModuleAction(IICompatModule::loadComplete, "could not complete loading");
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
