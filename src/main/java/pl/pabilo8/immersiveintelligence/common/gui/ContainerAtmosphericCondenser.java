/**
 * @author Avalon (avalon@iiteam.net)
 * @since 04.11.2026
 */
package pl.pabilo8.immersiveintelligence.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import blusunrize.immersiveengineering.common.gui.IESlot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityAtmosphericCondenser;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockAtmosphericCondenser;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 04.11.2026
 */

public class ContainerAtmosphericCondenser extends ContainerIITileBase<TileEntityAtmosphericCondenser>
{
	public final Slot[] bucketInputSlots, bucketOutputSlots;

	public ContainerAtmosphericCondenser(EntityPlayer player, TileEntityAtmosphericCondenser tile)
	{
		super(player, tile);

		bucketInputSlots = new Slot[]{
				addSlot(31, 8+4+4, MultiblockAtmosphericCondenser.BUCKET_NITROGEN_IN),
				addSlot(87, 8+4+4, MultiblockAtmosphericCondenser.BUCKET_OXYGEN_IN),
				addSlot(143, 8+4+4, MultiblockAtmosphericCondenser.BUCKET_ARGON_IN)
		};
		bucketOutputSlots = new Slot[]{
				addSlot(31, 36+4+4, MultiblockAtmosphericCondenser.BUCKET_NITROGEN_OUT, IESlot.Output::new),
				addSlot(87, 36+4+4, MultiblockAtmosphericCondenser.BUCKET_OXYGEN_OUT, IESlot.Output::new),
				addSlot(143, 36+4+4, MultiblockAtmosphericCondenser.BUCKET_ARGON_OUT, IESlot.Output::new)
		};
		addPlayerInventory(player.inventory, 8, 86);
	}

}
