package pl.pabilo8.immersiveintelligence;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IICreativeTab;
import pl.pabilo8.immersiveintelligence.common.IISaveData;
import pl.pabilo8.immersiveintelligence.common.IISounds;

import static pl.pabilo8.immersiveintelligence.ImmersiveIntelligence.MODID;
import static pl.pabilo8.immersiveintelligence.ImmersiveIntelligence.VERSION;

@Mod(modid = MODID, version = VERSION, dependencies = "required-after:immersiveengineering@[0.12,);")
public class ImmersiveIntelligence
{
	public static final String MODID = "immersiveintelligence";
	public static final String VERSION = "@VERSION@";

	@SidedProxy(clientSide = "pl.pabilo8.immersiveintelligence.client.ClientProxy", serverSide = "pl.pabilo8.immersiveintelligence.common.CommonProxy")
	public static CommonProxy proxy;

	public static Logger logger;

	public static IICreativeTab creativeTab = new IICreativeTab(MODID);

	@Instance(MODID)
	public static ImmersiveIntelligence INSTANCE;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, proxy);

		proxy.init();

		IISounds.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}

	/*@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new IICommandHandler());
	}*/

	@EventHandler
	public void serverStarted(FMLServerStartedEvent event)
	{
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
		{
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
			if(!world.isRemote)
			{
				IISaveData worldData = (IISaveData)world.loadData(IISaveData.class, IISaveData.dataName);
				if(worldData==null)
				{
					worldData = new IISaveData(IISaveData.dataName);
					world.setData(IISaveData.dataName, worldData);
				}
				IISaveData.setInstance(world.provider.getDimension(), worldData);
			}
		}

		CommonProxy.refreshFluidReferences();
	}

}
