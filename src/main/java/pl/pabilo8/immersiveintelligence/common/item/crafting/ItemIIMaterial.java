package pl.pabilo8.immersiveintelligence.common.item.crafting;

import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial.Materials;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

/**
 * @author Pabilo8
 * @since 2019-05-11
 */
public class ItemIIMaterial extends ItemIISubItemsBase<Materials>
{
	public ItemIIMaterial()
	{
		super("material", 64, Materials.values());
	}

	@GeneratedItemModels(itemName = "material")
	public enum Materials implements IIItemEnum
	{
		@IIItemProperties(oreDict = "electronTubeAdvanced")
		ADVANCED_ELECTRON_TUBE,

		@IIItemProperties(oreDict = {"chipBasic", "oc:circuitChip1"})
		BASIC_ELECTRONIC_ELEMENT,
		@IIItemProperties(oreDict = {"circuitBasicRaw", "oc:materialCircuitBoardRaw"})
		BASIC_CIRCUIT_BOARD_RAW,
		@IIItemProperties(oreDict = {"circuitBasicEtched", "oc:materialCircuitBoardPrinted"})
		BASIC_CIRCUIT_BOARD_ETCHED,

		@IIItemProperties(oreDict = {"chipAdvanced", "oc:circuitChip2"})
		ADVANCED_ELECTRONIC_ELEMENT,
		@IIItemProperties(oreDict = {"circuitAdvancedRaw", "oc:materialCircuitBoardRaw"})
		ADVANCED_CIRCUIT_BOARD_RAW,
		@IIItemProperties(oreDict = {"circuitAdvancedEtched", "oc:materialCircuitBoardPrinted"})
		ADVANCED_CIRCUIT_BOARD_ETCHED,
		@IIItemProperties(oreDict = "circuitAdvanced")
		ADVANCED_CIRCUIT_BOARD,

		@IIItemProperties(oreDict = {"transistor", "oc:materialTransistor"})
		TRANSISTOR,
		@IIItemProperties(oreDict = {"chipProcessor", "chipElite", "oc:circuitChip3"})
		PROCESSOR_ELECTRONIC_ELEMENT,
		@IIItemProperties(oreDict = {"circuitProcessorRaw", "circuitEliteRaw", "oc:materialCircuitBoardRaw"})
		PROCESSOR_CIRCUIT_BOARD_RAW,
		@IIItemProperties(oreDict = {"circuitProcessorEtched", "circuitEliteEtched", "oc:materialCircuitBoardPrinted"})
		PROCESSOR_CIRCUIT_BOARD_ETCHED,
		@IIItemProperties(oreDict = {"circuitProcessor", "circuitElite"})
		PROCESSOR_CIRCUIT_BOARD,

		@IIItemProperties(oreDict = {"engineElectricSmall", "engineElectricCompact"})
		COMPACT_ELECTRIC_ENGINE,
		@IIItemProperties(oreDict = {"engineElectricSmallAdvanced", "engineElectricCompactAdvanced"})
		COMPACT_ELECTRIC_ENGINE_ADVANCED,

		@IIItemProperties(oreDict = "punchtapeEmpty")
		PUNCHTAPE_EMPTY,

		@IIItemProperties(oreDict = {"materialRDX", "materialHexogen"})
		DUST_RDX,
		@IIItemProperties(oreDict = {"materialHMX", "materialOctogen"})
		DUST_HMX,
		@IIItemProperties(oreDict = {"dustWhitePhosphorus", "whitePhosphorus"})
		WHITE_PHOSPHORUS,
		@IIItemProperties(oreDict = "dustSalt")
		DUST_SALT,

		@IIItemProperties(oreDict = "brushCarbon")
		CARBON_BRUSH,
		@IIItemProperties(hidden = true)
		GLASS_FIBRE_CABLE,

		@IIItemProperties(oreDict = "dustWood")
		DUST_WOOD,
		@IIItemProperties(oreDict = "dustFormaldehyde")
		DUST_FORMALDEHYDE,
		@IIItemProperties(oreDict = "dustHexamine")
		DUST_HEXAMINE,
		@IIItemProperties(oreDict = "pulpWood")
		PULP_WOOD,
		@IIItemProperties(oreDict = "pulpWoodTreated")
		PULP_WOOD_TREATED,
		@IIItemProperties(oreDict = {"leather", "leatherArtificial"})
		ARTIFICIAL_LEATHER,

		@IIItemProperties(oreDict = "rubberRaw")
		NATURAL_RUBBER,
		@IIItemProperties(oreDict = {"itemRubber", "materialRubber"})
		RUBBER_BELT,
		@IIItemProperties(oreDict = "tireRubber")
		RUBBER_TIRE,
		@IIItemProperties(oreDict = "dustVulcanizationCompound")
		RUBBER_COMPOUND,

		@IIItemProperties(oreDict = "templateCircuit", hidden = true)
		CIRCUIT_TEMPLATE,

		@IIItemProperties(oreDict = "circuitCryptographic")
		CRYPTOGRAPHIC_CIRCUIT_BOARD,

		@IIItemProperties(oreDict = "gunbarrelIron")
		IRON_GUN_BARREL,
		@IIItemProperties(oreDict = "gunbarrelTungsten")
		TUNGSTEN_GUN_BARREL,
		@IIItemProperties(oreDict = "gunstockWood")
		GUN_STOCK,
		@IIItemProperties(oreDict = "gunbodyWood")
		GUN_CASING,
		@IIItemProperties(oreDict = "gunpartBasic")
		FIRING_MECHANISM,
		@IIItemProperties(oreDict = "gunpartAdvanced")
		ADVANCED_FIRING_MECHANISM,

		@IIItemProperties(oreDict = "ingotSand")
		SANDBAG,
	}
}
