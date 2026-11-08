package pl.pabilo8.immersiveintelligence.common.compat;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.weather2.api.weather.WeatherEnum.Type;
import net.mrbt0907.weather2.event.ServerTickHandler;
import net.mrbt0907.weather2.weather.WeatherManagerServer;
import net.mrbt0907.weather2.weather.storm.StormObject;
import net.mrbt0907.weather2.weather.storm.WeatherObject;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Radar;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.radar.RadarContact;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.*;

/**
 * Adds radar weather detection for Weather2 Remastered.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 12.10.2026
 */
public class Weather2CompatHandler extends IICompatModule
{
	@Override
	public String getName()
	{
		return "weather2remaster";
	}

	@Override
	public void preInit()
	{
		IIContent.UPGRADE_WEATHER_ANOMALY_DETECTORS = new Upgrade("weather_anomaly_detectors");
	}

	@Override
	public void registerRecipes()
	{
		IIContent.UPGRADE_WEATHER_ANOMALY_DETECTORS
				.withCost(new IngredientStack("plateSteel", 8))
				.withCost(new IngredientStack("circuitAdvanced", 12))
				.withCost(new IngredientStack("circuitAdvanced", 12))
				.withCost(new IngredientStack(new ItemStack(IIContent.itemDataWireCoil, 4, 0)))
				.withRequiredProgress(250000);
	}

	@Override
	public void init()
	{
		UpgradeTechTree.getTreeFor(TileEntityRadar.class)
				.withUpgrade(IIContent.UPGRADE_WEATHER_ANOMALY_DETECTORS, UpgradeTier.TIER_1);
		TileEntityRadar.setWeatherScanner(Weather2CompatHandler::scanWeather);
	}

	@Override
	public void postInit()
	{
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void clientPostInit()
	{
		super.clientPostInit();

		ResLoc modelDir = IIReference.RES_BLOCK_MODEL.with("multiblock/radar/");
		UpgradeTechTree.getTreeFor(TileEntityRadar.class)
				.withUpgradeModelLocation(IIContent.UPGRADE_WEATHER_ANOMALY_DETECTORS, modelDir.with("upgrade_weather.obj.ie"));
	}

	private static List<RadarContact> scanWeather(TileEntityRadar radar)
	{
		if(radar.getWorld().isRemote)
			return Collections.emptyList();
		WeatherManagerServer manager = ServerTickHandler.dimensionSystems.get(radar.getWorld().provider.getDimension());
		if(manager==null)
			return Collections.emptyList();
		Vec3d origin = radar.getRadarCenter();
		double rangeSquared = (double)Radar.detectionRadius*Radar.detectionRadius;
		long expiresAt = radar.getWorld().getTotalWorldTime()+TileEntityRadar.CONTACT_MARKER_DURATION;
		List<RadarContact> detected = new ArrayList<>();
		for(WeatherObject anomaly : manager.getWeatherObjects())
		{
			if(anomaly.isDead)
				continue;
			Vec3d position = new Vec3d(anomaly.pos.posX, anomaly.pos.posY, anomaly.pos.posZ);
			double dx = position.x-origin.x, dz = position.z-origin.z;
			//Cloud altitude does not limit horizontal radar range.
			if(dx*dx+dz*dz > rangeSquared)
				continue;
			String name = getAnomalyName(anomaly);
			if(!name.isEmpty())
				detected.add(new RadarContact(name, position, expiresAt, expiresAt));
		}
		//Use a stable order when several anomalies have the same name and distance.
		detected.sort(Comparator.comparing(RadarContact::getName)
				.thenComparingDouble(contact -> contact.getPosition().x)
				.thenComparingDouble(contact -> contact.getPosition().y)
				.thenComparingDouble(contact -> contact.getPosition().z));
		return detected;
	}

	private static String getAnomalyName(WeatherObject anomaly)
	{
		if(anomaly instanceof StormObject storm)
		{
			if(storm.isSpout)
				return "waterspout";
			if((anomaly.type==Type.RAIN||anomaly.type==Type.CLOUD)&&storm.isHailing())
				return "hailstorm";
			if(anomaly.type==Type.RAIN)
				return storm.hasDownfall()?(storm.temperature <= 0f?"snowstorm": "rainstorm"): "";
		}
		if(anomaly.type==Type.CLOUD)
			return "";
		return anomaly.type==Type.THUNDER?"thunderstorm": anomaly.type.name().toLowerCase(Locale.ROOT);
	}
}
