package pl.pabilo8.immersiveintelligence.client.manual.categories;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityAlarmSiren;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockRadar;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIIBinoculars.Binoculars;
import pl.pabilo8.immersiveintelligence.common.item.tools.ItemIITripodPeriscope;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 18-01-2020
 */
public class IIManualCategoryIntelligence extends IIManualCategory {
    public static IIManualCategoryIntelligence INSTANCE = new IIManualCategoryIntelligence();

    @Override
    public String getCategory() {
        return IIReference.CAT_INTELLIGENCE;
    }

    @Override
    public void addPages() {
        super.addPages();

        addEntry("intel_main");
        addEntry("binoculars")
                .addSource("crafting_binoculars", getSourceForItems(
                        IIContent.itemBinoculars.getStack(Binoculars.BINOCULARS)
                ))
                .addSource("crafting_infbinoculars", getSourceForItems(
                        IIContent.itemBinoculars.getStack(Binoculars.INFRARED_BINOCULARS)
                ));
        addEntry("tripod_periscope")
                .addSource("crafting_tripod_periscope", getSourceForItem(IIContent.itemTripodPeriscope.getStack(1)));
        addEntry("radar");
        addEntry("alarm_siren")
                .addSource("crafting_alarm_siren", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.ALARM_SIREN)));
    }
}
