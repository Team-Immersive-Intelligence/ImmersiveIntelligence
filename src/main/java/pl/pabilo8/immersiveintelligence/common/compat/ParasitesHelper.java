package pl.pabilo8.immersiveintelligence.common.compat;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 19.09.2026
 */
public class ParasitesHelper extends IICompatModule
{
	@Override
	public String getName()
	{
		return "Scape and Run: Parasites";
	}

	@Override
	public void preInit()
	{

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

	@Override
	@SuppressWarnings("DataFlowIssue")
	public void loadComplete()
	{
		super.loadComplete();
		this.configChanged("", false, false);
	}

	@Override
	public void configChanged(String configID, boolean requiresMcRestart, boolean worldRunning)
	{
		List<String> blockBlacklist = new ArrayList<>(Arrays.asList(SRPConfig.parasiteGriefingBlackList));
		blockBlacklist.add(IIContent.blockMetalMultiblock0.getRegistryName().toString());
		blockBlacklist.add(IIContent.blockMetalMultiblock1.getRegistryName().toString());
		blockBlacklist.add(IIContent.blockWoodenMultiblock.getRegistryName().toString());
		blockBlacklist.add(IIContent.blockFenceGateMultiblock.getRegistryName().toString());
		blockBlacklist.add(IIContent.blockMetalFortification.getRegistryName().toString());
		SRPConfig.parasiteGriefingBlackList = blockBlacklist.toArray(new String[0]);
	}
}
