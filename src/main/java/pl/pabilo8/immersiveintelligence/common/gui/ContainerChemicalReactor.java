package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 17.10.2026
 */
public class ContainerChemicalReactor extends ContainerIITileBase<TileEntityChemicalReactor>
{
	public final Slot[] slotsSet1, slotsSet2, slotsSet3;

	public ContainerChemicalReactor(EntityPlayer player, TileEntityChemicalReactor tile)
	{
		super(player, tile);

		//Set 1 slots
		this.slotsSet1 = new Slot[]{
				addSlot(10, 14, MultiblockChemicalReactor.SLOT_SET1_FLUID_INPUT1, getFluidContainerSlot(SideConfig.INPUT)),
				addSlot(20, 14, MultiblockChemicalReactor.SLOT_SET1_FLUID_INPUT2, getFluidContainerSlot(SideConfig.INPUT)),
				addSlot(30, 14, MultiblockChemicalReactor.SLOT_SET1_FLUID_OUTPUT, getFluidContainerSlot(SideConfig.OUTPUT)),
				addSlot(10, 20, MultiblockChemicalReactor.SLOT_SET1_ITEM_INPUT, DefaultInputSlot::new),
				addSlot(20, 20, MultiblockChemicalReactor.SLOT_SET1_CATALYST_INPUT, DefaultInputSlot::new),
				addSlot(30, 20, MultiblockChemicalReactor.SLOT_SET1_ITEM_OUTPUT, IESlot.Output::new)
		};

		//Set 2 slots
		this.slotsSet2 = new Slot[]{
				addSlot(50, 14, MultiblockChemicalReactor.SLOT_SET2_FLUID_INPUT1, getFluidContainerSlot(SideConfig.INPUT)),
				addSlot(60, 14, MultiblockChemicalReactor.SLOT_SET2_FLUID_INPUT2, getFluidContainerSlot(SideConfig.INPUT)),
				addSlot(70, 14, MultiblockChemicalReactor.SLOT_SET2_FLUID_OUTPUT, getFluidContainerSlot(SideConfig.OUTPUT)),
				addSlot(50, 20, MultiblockChemicalReactor.SLOT_SET2_ITEM_INPUT, DefaultInputSlot::new),
				addSlot(60, 20, MultiblockChemicalReactor.SLOT_SET2_CATALYST_INPUT, DefaultInputSlot::new),
				addSlot(70, 20, MultiblockChemicalReactor.SLOT_SET2_ITEM_OUTPUT, IESlot.Output::new)
		};

		//Set 3 slots
		this.slotsSet3 = new Slot[]{
				addSlot(90, 14, MultiblockChemicalReactor.SLOT_SET3_FLUID_INPUT1, getFluidContainerSlot(SideConfig.INPUT)),
				addSlot(100, 14, MultiblockChemicalReactor.SLOT_SET3_FLUID_INPUT2, getFluidContainerSlot(SideConfig.INPUT)),
				addSlot(110, 14, MultiblockChemicalReactor.SLOT_SET3_FLUID_OUTPUT, getFluidContainerSlot(SideConfig.OUTPUT)),
				addSlot(90, 20, MultiblockChemicalReactor.SLOT_SET3_ITEM_INPUT, DefaultInputSlot::new),
				addSlot(100, 20, MultiblockChemicalReactor.SLOT_SET3_CATALYST_INPUT, DefaultInputSlot::new),
				addSlot(110, 20, MultiblockChemicalReactor.SLOT_SET3_ITEM_OUTPUT, IESlot.Output::new)
		};

		this.addPlayerInventory(player.inventory, 8, 86);
	}
}
