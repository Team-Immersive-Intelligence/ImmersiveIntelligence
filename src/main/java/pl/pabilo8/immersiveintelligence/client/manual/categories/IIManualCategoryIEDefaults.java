package pl.pabilo8.immersiveintelligence.client.manual.categories;

import blusunrize.immersiveengineering.api.ManualHelper;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIGearbox.IIBlockTypes_Gearbox;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalConnector.IIBlockTypes_MechanicalConnector;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalDevice.IIBlockTypes_MechanicalDevice;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorBelt.MotorBelt;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorGear.MotorGear;

/**
 * Adds II Markdown entries to the default IE manual categories.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.2
 * @since 06.10.2026
 */
public class IIManualCategoryIEDefaults extends IIManualCategory
{
	public static final IIManualCategoryIEDefaults INSTANCE = new IIManualCategoryIEDefaults();

	@Override
	public String getCategory()
	{
		return "ie_defaults";
	}

	/**
	 * Loads entries from ie_defaults into categories such as {@link ManualHelper#CAT_HEAVYMACHINES}.
	 * Use addEntry("ii_entry_name", targetCategory) to add an entry.
	 */
	@Override
	public void addPages()
	{
		super.addPages();
		//Tools
		addEntry("electric_tools", ManualHelper.CAT_TOOLS)
				.addSource("e_hammer", getSourceForItem(new ItemStack(IIContent.itemHammer)))
				.addSource("e_wrench", getSourceForItem(new ItemStack(IIContent.itemElectricWrench)))
				.addSource("e_cutter", getSourceForItem(new ItemStack(IIContent.itemWirecutter)));

		addEntry("lighter", ManualHelper.CAT_TOOLS)
				.addSource("lighter", getSourceForItem(new ItemStack(IIContent.itemLighter)));

		addEntry("measuring_cup", ManualHelper.CAT_TOOLS)
				.addSource("m_cup", getSourceForItem(new ItemStack(IIContent.itemMeasuringCup)));
		addEntry("engineers_clipboard", ManualHelper.CAT_TOOLS)
				.addSource("clipboard", getSourceForItem(new ItemStack(IIContent.itemClipboard)));

		//Simple Machines
		addEntry("carbon_filter", ManualHelper.CAT_MACHINES)
				.addSource("co2_filter", getSourceForItem(new ItemStack(IIContent.blockMetalDevice1)
				));

		//Power, Wires, Generators
		addEntry("rotary_power", ManualHelper.CAT_ENERGY)
				.addSource("gearbox", getSourceForItem(
						IIContent.blockGearbox.getStack(IIBlockTypes_Gearbox.WOODEN_GEARBOX)
				))
				.addSource("transmission", getSourceForItem(
						IIContent.blockMechanicalDevice.getStack(IIBlockTypes_MechanicalDevice.WOODEN_TRANSMISSION_BOX)
				))
				.addSource("wheels", getSourceForItems(
						IIContent.blockMechanicalConnector.getStack(IIBlockTypes_MechanicalConnector.IRON_WHEEL),
						IIContent.blockMechanicalConnector.getStack(IIBlockTypes_MechanicalConnector.STEEL_WHEEL)
				))
				.addSource("belt", getSourceForItem(
						new ItemStack(IIContent.itemMotorBelt)))
				.addSource("belt_cloth", getSourceForItem(IIContent.itemMotorBelt.getStack(MotorBelt.CLOTH)))
				.addSource("belt_steel", getSourceForItem(IIContent.itemMotorBelt.getStack(MotorBelt.STEEL)))
				.addSource("belt_rubber", getSourceForItem(IIContent.itemMotorBelt.getStack(MotorBelt.RUBBER)))
				.addSource("gears", getSourceForItems(
						IIContent.itemMotorGear.getStack(MotorGear.COPPER),
						IIContent.itemMotorGear.getStack(MotorGear.BRASS),
						IIContent.itemMotorGear.getStack(MotorGear.IRON),
						IIContent.itemMotorGear.getStack(MotorGear.STEEL),
						IIContent.itemMotorGear.getStack(MotorGear.TUNGSTEN)
				));
	}
}
