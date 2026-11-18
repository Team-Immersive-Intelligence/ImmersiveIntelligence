package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.common.gui.IESlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockSmallChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntitySmallChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 17.11.2026
 */

public class ContainerSmallChemicalReactor extends ContainerIITileBase<TileEntitySmallChemicalReactor>
{
	public final Slot itemInputSlot, itemOutputSlot, catalystSlot;
	public final Slot[] bucketInputSlots, bucketOutputSlots;
	public final Slot[] slotsInput, slotsOutput;

	public ContainerSmallChemicalReactor(EntityPlayer player, TileEntitySmallChemicalReactor tile)
	{
		super(player, tile);

		itemInputSlot = 		addSlot(108+2, 14, MultiblockSmallChemicalReactor.SLOT_ITEM_INPUT);
				catalystSlot = 				addSlot(144+2, 14, MultiblockSmallChemicalReactor.SLOT_CATALYST);
				itemOutputSlot = addSlot(224, 52, MultiblockSmallChemicalReactor.SLOT_ITEM_OUTPUT, IESlot.Output::new);

		bucketInputSlots = new Slot[]{
				addSlot(6, 14, MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_1_BUCKET_INPUT, getFluidContainerSlot(SideConfig.INPUT)),
				addSlot(56, 14, MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_2_BUCKET_INPUT, getFluidContainerSlot(SideConfig.INPUT)),
				addSlot(176, 14, MultiblockSmallChemicalReactor.SLOT_TANK_OUTPUT_1_BUCKET_INPUT, getFluidContainerSlot(SideConfig.INPUT))
		};
		bucketOutputSlots = new Slot[]{
				addSlot(6, 52, MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_1_BUCKET_OUTPUT, getFluidContainerSlot(SideConfig.OUTPUT)),
				addSlot(56, 52, MultiblockSmallChemicalReactor.SLOT_TANK_INPUT_2_BUCKET_OUTPUT, getFluidContainerSlot(SideConfig.OUTPUT)),
				addSlot(176, 52, MultiblockSmallChemicalReactor.SLOT_TANK_OUTPUT_1_BUCKET_OUTPUT, getFluidContainerSlot(SideConfig.OUTPUT))
		};
		slotsInput = new Slot[]{itemInputSlot, catalystSlot,
				bucketInputSlots[0], bucketInputSlots[1], bucketInputSlots[2]};
		slotsOutput = new Slot[]{itemOutputSlot,
				bucketOutputSlots[0], bucketOutputSlots[1], bucketOutputSlots[2]};

		addPlayerInventory(player.inventory, 43, 90);
	}
}
