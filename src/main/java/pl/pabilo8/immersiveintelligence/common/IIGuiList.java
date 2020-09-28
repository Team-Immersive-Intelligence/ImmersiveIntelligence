package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.gui.*;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticLogicMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticLogicMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.arithmetic_logic_machine.GuiArithmeticMachineVariables;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineEdit;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineStorage;
import pl.pabilo8.immersiveintelligence.client.gui.data_input_machine.GuiDataInputMachineVariables;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityDataMerger;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityMetalCrate;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntitySmallCrate;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.*;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityGearbox;
import pl.pabilo8.immersiveintelligence.common.gui.*;
import pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine.ContainerArithmeticLogicMachineEdit;
import pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine.ContainerArithmeticLogicMachineStorage;
import pl.pabilo8.immersiveintelligence.common.gui.arithmetic_logic_machine.ContainerArithmeticLogicMachineVariables;
import pl.pabilo8.immersiveintelligence.common.gui.data_input_machine.ContainerDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.gui.data_input_machine.ContainerDataInputMachineVariables;

import java.util.function.BiFunction;

/**
 * Created by Pabilo8 on 2019-05-17.
 * Major update on 2020-06-08
 */

public enum IIGuiList {
    GUI_METAL_CRATE(TileEntityMetalCrate.class),
    GUI_AMMUNITION_CRATE(TileEntityAmmunitionCrate.class),
    GUI_SMALL_CRATE(TileEntitySmallCrate.class),

    GUI_SKYCRATE_STATION(TileEntitySkyCrateStation.class),
    GUI_SKYCART_STATION(TileEntitySkyCartStation.class),

    GUI_DATA_INPUT_MACHINE_STORAGE(TileEntityDataInputMachine.class),
    GUI_DATA_INPUT_MACHINE_VARIABLES(TileEntityDataInputMachine.class),
    GUI_DATA_INPUT_MACHINE_EDIT(TileEntityDataInputMachine.class),

    GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE(TileEntityArithmeticLogicMachine.class),
    GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1(TileEntityArithmeticLogicMachine.class),
    GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2(TileEntityArithmeticLogicMachine.class),
    GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3(TileEntityArithmeticLogicMachine.class),
    GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_4(TileEntityArithmeticLogicMachine.class),

    GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_1(TileEntityArithmeticLogicMachine.class),
    GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_2(TileEntityArithmeticLogicMachine.class),
    GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_3(TileEntityArithmeticLogicMachine.class),
    GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_4(TileEntityArithmeticLogicMachine.class),

    GUI_PRINTED_PAGE_BLANK(null),
    GUI_PRINTED_PAGE_TEXT(null),
    GUI_PRINTED_PAGE_CODE(null),
    GUI_PRINTED_PAGE_BLUEPRINT(null),

    GUI_DATA_REDSTONE_INTERFACE_DATA(TileEntityRedstoneInterface.class),
    GUI_DATA_REDSTONE_INTERFACE_REDSTONE(TileEntityRedstoneInterface.class),

    GUI_PRINTING_PRESS(TileEntityPrintingPress.class),

    GUI_CHEMICAL_BATH(TileEntityChemicalBath.class),
    GUI_ELECTROLYZER(TileEntityElectrolyzer.class),

    GUI_PRECISSION_ASSEMBLER(TileEntityPrecissionAssembler.class),

    GUI_AMMUNITION_FACTORY(TileEntityAmmunitionFactory.class),


    GUI_DATA_MERGER(TileEntityDataMerger.class),

    GUI_GEARBOX(TileEntityGearbox.class),

    GUI_PACKER(TileEntityPacker.class),
    //GUI_UNPACKER,

    GUI_SAWMILL(TileEntitySawmill.class);

    //GUI_PERISCOPE,

    public Class<? extends TileEntity> teClass;

    IIGuiList(Class<? extends TileEntity> teClass) {
        this.teClass = teClass;
    }

    /**
     * The GUI class does not exist on the server side, hence the {@link SideOnly} annotation.
     * @param player Player for who to get the GUI.
     * @param te TileEntity referenced.
     * @return Returns a newly instantiated GUI screen.
     */
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, TileEntity te) {
        switch (this) {
            case GUI_METAL_CRATE:
                return new GuiMetalCrate(player.inventory, (TileEntityMetalCrate) te);
            case GUI_AMMUNITION_CRATE:
                return new GuiAmmunitionCrate(player.inventory, (TileEntityAmmunitionCrate) te);
            case GUI_SMALL_CRATE:
                return new GuiSmallCrate(player.inventory, (TileEntitySmallCrate) te);
            case GUI_SKYCRATE_STATION:
                return new GuiSkycrateStation(player.inventory, (TileEntitySkyCrateStation) te);
            case GUI_SKYCART_STATION:
                return new GuiSkycartStation(player.inventory, (TileEntitySkyCartStation) te);
            case GUI_DATA_INPUT_MACHINE_STORAGE:
                return new GuiDataInputMachineStorage(player.inventory, (TileEntityDataInputMachine) te);
            case GUI_DATA_INPUT_MACHINE_VARIABLES:
                return new GuiDataInputMachineVariables(player.inventory, (TileEntityDataInputMachine) te);
            case GUI_DATA_INPUT_MACHINE_EDIT:
                return new GuiDataInputMachineEdit(player.inventory, (TileEntityDataInputMachine) te);
            case GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE:
                return new GuiArithmeticLogicMachineStorage(player.inventory, (TileEntityArithmeticLogicMachine) te);
            case GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1:
                return new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine) te, 0);
            case GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2:
                return new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine) te, 1);
            case GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3:
                return new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine) te, 2);
            case GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_4:
                return new GuiArithmeticMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine) te, 3);
            case GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_1:
                return new GuiArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine) te, 0);
            case GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_2:
                return new GuiArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine) te, 1);
            case GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_3:
                return new GuiArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine) te, 2);
            case GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_4:
                return new GuiArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine) te, 3);
            case GUI_PRINTED_PAGE_BLANK:
                return null;
            case GUI_PRINTED_PAGE_TEXT:
                return null;
            case GUI_PRINTED_PAGE_CODE:
                return null;
            case GUI_PRINTED_PAGE_BLUEPRINT:
                return null;
            case GUI_DATA_REDSTONE_INTERFACE_DATA:
                return new GuiDataRedstoneInterfaceData(player.inventory, (TileEntityRedstoneInterface) te);
            case GUI_DATA_REDSTONE_INTERFACE_REDSTONE:
                return new GuiDataRedstoneInterfaceRedstone(player.inventory, (TileEntityRedstoneInterface) te);
            case GUI_PRINTING_PRESS:
                return new GuiPrintingPress(player.inventory, (TileEntityPrintingPress) te);
            case GUI_CHEMICAL_BATH:
                return new GuiChemicalBath(player.inventory, (TileEntityChemicalBath) te);
            case GUI_ELECTROLYZER:
                return new GuiElectrolyzer(player.inventory, (TileEntityElectrolyzer) te);
            case GUI_PRECISSION_ASSEMBLER:
                return new GuiPrecissionAssembler(player.inventory, (TileEntityPrecissionAssembler) te);
            case GUI_AMMUNITION_FACTORY:
                return new GuiAmmunitionFactory(player.inventory, (TileEntityAmmunitionFactory) te);
            case GUI_DATA_MERGER:
                return new GuiDataMerger(player.inventory, (TileEntityDataMerger) te);
            case GUI_GEARBOX:
                return new GuiGearbox(player.inventory, (TileEntityGearbox) te);
            case GUI_PACKER:
                return new GuiPacker(player.inventory, (TileEntityPacker) te);
            case GUI_SAWMILL:
                return new GuiSawmill(player.inventory, (TileEntitySawmill) te);
        }
        return null;
    }

    /**
     * Creates a new container for the provided player and TileEntity.
     * @param player Player reference.
     * @param te TileEntity reference.
     * @return Returns a newly instantiated {@link Container}.
     */
    Container getContainer(EntityPlayer player, TileEntity te) {
        switch (this) {
            case GUI_METAL_CRATE:
                return new ContainerMetalCrate(player.inventory, (TileEntityMetalCrate) te);
            case GUI_AMMUNITION_CRATE:
                return new ContainerAmmunitionCrate(player.inventory, (TileEntityAmmunitionCrate) te);
            case GUI_SMALL_CRATE:
                return new ContainerSmallCrate(player.inventory, (TileEntitySmallCrate) te);
            case GUI_SKYCRATE_STATION:
                return new ContainerSkycrateStation(player.inventory, (TileEntitySkyCrateStation) te);
            case GUI_SKYCART_STATION:
                return new ContainerSkycartStation(player.inventory, (TileEntitySkyCartStation) te);
            case GUI_DATA_INPUT_MACHINE_STORAGE:
                return new ContainerDataInputMachine(player.inventory, (TileEntityDataInputMachine) te);
            case GUI_DATA_INPUT_MACHINE_VARIABLES:
                return new ContainerDataInputMachineVariables(player.inventory, (TileEntityDataInputMachine) te);
            case GUI_DATA_INPUT_MACHINE_EDIT:
                return new ContainerDataInputMachineVariables(player.inventory, (TileEntityDataInputMachine) te);
            case GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE:
                return new ContainerArithmeticLogicMachineStorage(player.inventory, (TileEntityArithmeticLogicMachine) te);
            case GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_1:
                return new ContainerArithmeticLogicMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine) te, 0);
            case GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_2:
                return new ContainerArithmeticLogicMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine) te, 1);
            case GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_3:
                return new ContainerArithmeticLogicMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine) te, 2);
            case GUI_ARITHMETIC_LOGIC_MACHINE_VARIABLES_4:
                return new ContainerArithmeticLogicMachineVariables(player.inventory, (TileEntityArithmeticLogicMachine) te, 3);
            case GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_1:
                return new ContainerArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine) te);
            case GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_2:
                return new ContainerArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine) te);
            case GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_3:
                return new ContainerArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine) te);
            case GUI_ARITHMETIC_LOGIC_MACHINE_EDIT_4:
                return new ContainerArithmeticLogicMachineEdit(player.inventory, (TileEntityArithmeticLogicMachine) te);
            case GUI_PRINTED_PAGE_BLANK:
                return null;
            case GUI_PRINTED_PAGE_TEXT:
                return null;
            case GUI_PRINTED_PAGE_CODE:
                return null;
            case GUI_PRINTED_PAGE_BLUEPRINT:
                return null;
            case GUI_DATA_REDSTONE_INTERFACE_DATA:
                return new ContainerRedstoneDataInterface(player.inventory, (TileEntityRedstoneInterface) te);
            case GUI_DATA_REDSTONE_INTERFACE_REDSTONE:
                return new ContainerRedstoneDataInterface(player.inventory, (TileEntityRedstoneInterface) te);
            case GUI_PRINTING_PRESS:
                return new ContainerPrintingPress(player.inventory, (TileEntityPrintingPress) te);
            case GUI_CHEMICAL_BATH:
                return new ContainerChemicalBath(player.inventory, (TileEntityChemicalBath) te);
            case GUI_ELECTROLYZER:
                return new ContainerElectrolyzer(player.inventory, (TileEntityElectrolyzer) te);
            case GUI_PRECISSION_ASSEMBLER:
                return new ContainerPrecissionAssembler(player.inventory, (TileEntityPrecissionAssembler) te);
            case GUI_AMMUNITION_FACTORY:
                return new ContainerAmmunitionFactory(player.inventory, (TileEntityAmmunitionFactory) te);
            case GUI_DATA_MERGER:
                return new ContainerDataMerger(player.inventory, (TileEntityDataMerger) te);
            case GUI_GEARBOX:
                return new ContainerGearbox(player.inventory, (TileEntityGearbox) te);
            case GUI_PACKER:
                return new ContainerPacker(player.inventory, (TileEntityPacker) te);
            case GUI_SAWMILL:
                return new ContainerSawmill(player.inventory, (TileEntitySawmill) te);
            default:
                return null;
        }
    }
}
