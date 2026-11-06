/**
 * @author Avalon (avalon@iiteam.net)
 * @since 04.11.2026
 */
package pl.pabilo8.immersiveintelligence.common.gui;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import blusunrize.immersiveengineering.common.gui.IESlot;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityAtmosphericCondenser;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockAtmosphericCondenser;
import pl.pabilo8.immersiveintelligence.common.util.gui.ContainerIITileBase;

public class ContainerAtmosphericCondenser extends ContainerIITileBase<TileEntityAtmosphericCondenser>
{
	private final AxisAlignedBB interactionBounds;
	public final Slot[] bucketInputSlots, bucketOutputSlots;

	public ContainerAtmosphericCondenser(EntityPlayer player, TileEntityAtmosphericCondenser tile)
	{
		super(player, tile);
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minZ = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int maxZ = Integer.MIN_VALUE;
		for(BlockPos pos : tile.getMultiblockBlocks())
		{
			minX = Math.min(minX, pos.getX());
			minY = Math.min(minY, pos.getY());
			minZ = Math.min(minZ, pos.getZ());
			maxX = Math.max(maxX, pos.getX()+1);
			maxY = Math.max(maxY, pos.getY()+1);
			maxZ = Math.max(maxZ, pos.getZ()+1);
		}
		interactionBounds = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);

		bucketInputSlots = new Slot[]{
				addSlot(31, 8, MultiblockAtmosphericCondenser.BUCKET_NITROGEN_IN),
				addSlot(87, 8, MultiblockAtmosphericCondenser.BUCKET_OXYGEN_IN),
				addSlot(143, 8, MultiblockAtmosphericCondenser.BUCKET_ARGON_IN)
		};
		bucketOutputSlots = new Slot[]{
				addSlot(31, 36, MultiblockAtmosphericCondenser.BUCKET_NITROGEN_OUT, IESlot.Output::new),
				addSlot(87, 36, MultiblockAtmosphericCondenser.BUCKET_OXYGEN_OUT, IESlot.Output::new),
				addSlot(143, 36, MultiblockAtmosphericCondenser.BUCKET_ARGON_OUT, IESlot.Output::new)
		};
		addPlayerInventory(player.inventory, 8, 86);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		double x = Math.max(interactionBounds.minX, Math.min(player.posX, interactionBounds.maxX));
		double y = Math.max(interactionBounds.minY, Math.min(player.posY, interactionBounds.maxY));
		double z = Math.max(interactionBounds.minZ, Math.min(player.posZ, interactionBounds.maxZ));
		return tile.hasWorld()
				&&tile.getWorld().getTileEntity(tile.getPos())==tile
				&&player.getDistanceSq(x, y, z) <= 64.0D;
	}
}
