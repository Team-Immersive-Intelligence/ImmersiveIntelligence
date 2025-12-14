package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.common.gui.IESlot.Output;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIIBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@itteam.net)
 * @updated 30.08.2025
 * @since 17.05.2019
 */
public class ContainerPrecisionAssembler extends ContainerIIBase<TileEntityPrecisionAssembler>
{
	public Slot[] ingredientSlots, outputSlots, toolSlots;
	public Slot schemeSlot;

	public ContainerPrecisionAssembler(EntityPlayer player, TileEntityPrecisionAssembler tile)
	{
		super(player, tile);
		this.toolSlots = new Slot[3];
		this.ingredientSlots = new Slot[4];
		this.outputSlots = new Slot[2];

		//tool slots
		this.toolSlots = addSlotArray(62, 59-6, MultiblockPrecisionAssembler.SLOT_TOOL1, 3, 3, DefaultInputSlot::new);
		for(Slot toolSlot : toolSlots)
			((IISlot)toolSlot).withOnChanged(() -> tile.doGraphicalUpdates(MultiblockPrecisionAssembler.SLOT_TOOL1));

		//scheme slot
		this.schemeSlot = addSlot(80, 24-6, MultiblockPrecisionAssembler.SLOT_SCHEME);

		//ingredient slots
		this.ingredientSlots[0] = addSlot(30, 39-6, MultiblockPrecisionAssembler.SLOT_INGREDIENT1);

		this.ingredientSlots[1] = addSlot(10, 19-6, MultiblockPrecisionAssembler.SLOT_INGREDIENT2);
		this.ingredientSlots[2] = addSlot(10, 39-6, MultiblockPrecisionAssembler.SLOT_INGREDIENT3);
		this.ingredientSlots[3] = addSlot(10, 59-6, MultiblockPrecisionAssembler.SLOT_INGREDIENT4);

		//output slots
		this.outputSlots[0] = addSlot(137, 39-6, MultiblockPrecisionAssembler.SLOT_OUTPUT, Output::new);
		this.outputSlots[1] = addSlot(137, 59-6, MultiblockPrecisionAssembler.SLOT_OUTPUT_TRASH, Output::new);

		addPlayerInventory(player.inventory, 8, 86);

	}
}
