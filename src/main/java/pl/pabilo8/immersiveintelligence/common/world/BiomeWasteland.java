package pl.pabilo8.immersiveintelligence.common.world;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * @author Pabilo8
 * @since 28.06.2021
 */
public class BiomeWasteland extends Biome
{
	public BiomeWasteland()
	{
		super((new Biome.BiomeProperties("IIWasteland")).setBaseHeight(0.125F).setHeightVariation(0.05F).setTemperature(2.0F).setRainfall(0.0F).setWaterColor(0x26332E).setRainDisabled());
		setRegistryName(new ResourceLocation(ImmersiveIntelligence.MODID, "wasteland"));
	}

	@Override
	public int getModdedBiomeGrassColor(int original)
	{
		return 0xFFF7C7;
	}

	@Override
	public int getModdedBiomeFoliageColor(int original)
	{
		return 0xC0C0BB;
	}

	@Override
	public int getSkyColorByTemp(float currentTemperature)
	{
		return 0xd0d0d0;
	}

	@Override
	public float getSpawningChance()
	{
		return 0;
	}
}
