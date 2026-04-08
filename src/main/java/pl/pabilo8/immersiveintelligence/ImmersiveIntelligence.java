package pl.pabilo8.immersiveintelligence;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IISaveData;
import pl.pabilo8.immersiveintelligence.common.commands.CommandII;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.event.IEOverrideEventHandler;
import pl.pabilo8.immersiveintelligence.common.event.LightEngineerEventHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIReflectionUtils;
import pl.pabilo8.immersiveintelligence.common.util.IISkinHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;

import static pl.pabilo8.immersiveintelligence.ImmersiveIntelligence.MODID;
import static pl.pabilo8.immersiveintelligence.ImmersiveIntelligence.VERSION;

@Mod(modid = MODID, version = VERSION,
		//xaxaxa, trick! yuo can't steal mod if mod is steal-proof
		certificateFingerprint = "770570c49a2652e64a9b29b9b9d9919ca68b7065",
		dependencies = "required-after:forge@[14.23.5.2820,);required-after:immersiveengineering@[0.12,);after:immersiveengineering@[0.12,);after:immersiveposts@[0.2,)")
public class ImmersiveIntelligence
{
	public static final String MODID = "immersiveintelligence";
	public static final String VERSION = "@VERSION@";
	//If anyone wants to acquire a righteously certified loicense:tm:, ask @Pabilo8, it is probable he can grant you one
	private static final String[] alternativeCerts = {
			"011e706a5b5f954d1a99bcded5c51c3cc104d915", //Gabriel
			"068e23a5c5552d79ac66ece9dcaed54cfb74a992", //Automated Carver Device(tm)
			"312cffade27b8eeb91fca5f5ae219495ebc86ab1", //Avalon
			"6644e1691cd979e50755e3f86aa72a702ecd69d1", //Prism
	};
	@SidedProxy(clientSide = "pl.pabilo8.immersiveintelligence.client.ClientProxy", serverSide = "pl.pabilo8.immersiveintelligence.common.CommonProxy")
	public static CommonProxy proxy;
	@Instance(MODID)
	public static ImmersiveIntelligence INSTANCE;

	@Mod.EventHandler
	public void modIDMapping(FMLModIdMappingEvent event)
	{

	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		IILogger.logger = event.getModLog();

		//Check if the mod is inside of a development environment
		if((boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment"))
		{
			Configurator.setLevel(IILogger.logger.getName(), Level.DEBUG);
			IILogger.debug = true;
			IILogger.debug("Running in a development environment, enabling debug messages");
		}
		//Setup config
		IIConfigHandler.putConfigValues();
		//Pre-Init
		proxy.preInit(event);
		ForgeChunkManager.setForcedChunkLoadingCallback(this, proxy);
		//Start contributor skins json download thread
		new IISkinHandler.ThreadContributorSpecialsDownloader();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, proxy);
		new LightEngineerEventHandler().registerEventHandler();
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);

		//Redirecting IE event to our own
		IIReflectionUtils.getForgeEventListeners();
		IIReflectionUtils.overrideEventHandler(blusunrize.immersiveengineering.common.EventHandler.class, new IEOverrideEventHandler());
	}

	@Mod.EventHandler
	public void loadComplete(FMLLoadCompleteEvent event)
	{
		IICompatModule.doModulesLoadComplete();
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		IILogger.debug("Pre-World Load cleanup");
		CommonProxy.refreshFluidReferences();
		RadioNetwork.INSTANCE.clearDevices();

		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
		{
			IILogger.info("WorldData loading");
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();

			IISaveData worldData = (IISaveData)world.loadData(IISaveData.class, IISaveData.DATA_NAME);
			if(worldData==null)
			{
				worldData = new IISaveData(IISaveData.DATA_NAME);
				world.setData(IISaveData.DATA_NAME, worldData);
				IILogger.info("WorldData created");
			}
			else
				IILogger.info("WorldData retrieved");
			IISaveData.setInstance(world.provider.getDimension(), worldData);
		}
		IILogger.debug("Registering II Commands");
		event.registerServerCommand(new CommandII());
	}

	@EventHandler
	public void onFMLServerStopped(FMLServerStoppedEvent event)
	{
		IILogger.info("Post-World Unload cleanup");
		DiplomacyUtils.unload();
		RadioNetwork.INSTANCE.clearDevices();
	}


	@Mod.EventHandler
	public void wrongSignature(FMLFingerprintViolationEvent event)
	{
		boolean loicense = false;
		for(String altCert : alternativeCerts)
			if(event.getFingerprints().contains(altCert))
			{
				System.out.println("[Immersive Intelligence/Error] "+altCert+" is considered a righteously loicensed certificate. "+
						"The build may not be stable, thou shall be ware of the bugs lurking from the shadows.");
				loicense = true;
				break;
			}
		if(!loicense)
		{
			System.out.println("[Immersive Intelligence/Error]IT IS VERY PROBABLE, "+
					"THE ENTITY WHO RELEASED THIS BUILD DOES NOT POSSESS A PROPER IMMERSIVE INTELLIGENCE LOICENSE! ");
			System.out.println("[Immersive Intelligence/Error]"+
					"CONTACT THE MINISTRY OF INTELLIGENCE IMMEDIATELLY! ");
			System.out.println("[Immersive Intelligence/Error]"+
					"FOUND THESE ILLEGAL FINGERPRINTS: "+event.getFingerprints());
		}
	}
}
