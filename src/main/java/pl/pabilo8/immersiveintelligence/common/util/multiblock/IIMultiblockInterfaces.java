package pl.pabilo8.immersiveintelligence.common.util.multiblock;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IComparatorOverride;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import pl.pabilo8.immersiveintelligence.api.utils.MultiblockConstructionManager;
import pl.pabilo8.immersiveintelligence.common.IIGUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all Immersive Intelligence multiblock interfaces.<br>
 * Feel free to extend.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 04.08.2022
 */
public class IIMultiblockInterfaces
{
	/**
	 * Returns a custom explosion resistance value or -1 for block default
	 */
	public interface IExplosionResistantMultiblock
	{
		float getExplosionResistance();
	}

	/**
	 * Allows for the block to act as a ladder
	 */
	public interface ILadderMultiblock
	{
		boolean isLadder();
	}

	public interface IAdvancedBounds extends IAdvancedCollisionBounds, IAdvancedSelectionBounds
	{
		/**
		 * @param collision whether it's checking collision or selection
		 * @return a list of AABB
		 */
		List<AxisAlignedBB> getBounds(boolean collision);

		@Override
		default List<AxisAlignedBB> getAdvancedColisionBounds()
		{
			return getBounds(true);
		}

		@Override
		default List<AxisAlignedBB> getAdvancedSelectionBounds()
		{
			return getBounds(false);
		}

		@Override
		default boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list)
		{
			return false;
		}

		@Override
		default float[] getBlockBounds()
		{
			return new float[]{0, 0, 0, 1, 1, 1};
		}
	}

	/**
	 * <b>I</b>
	 */
	public interface IIIInventory extends IIEInventory, IComparatorOverride
	{
		/**
		 * We don't do graphical updates here
		 */
		@Override
		default void doGraphicalUpdates(int slot)
		{

		}

		/**
		 * Unless Asie increases it, should be right, sometimes.
		 */
		@Override
		default int getSlotLimit(int slot)
		{
			return 64;
		}

		@Override
		default int getComparatorInputOverride()
		{
			return Utils.calcRedstoneFromInventory(this);
		}
	}

	/**
	 * A {@link net.minecraft.tileentity.TileEntity} that requires construction (transferring IF through player's hammer action) in order to be fully usable.
	 */
	public interface IConstructionRequiringDevice
	{
		/**
		 * @return The master block, in case this device is a multiblock dummy. Otherwise, return this.
		 */
		IConstructionRequiringDevice master();

		/**
		 * @return The {@link MultiblockConstructionManager} instance that handles construction progress
		 */
		MultiblockConstructionManager getConstructionManager();

		/**
		 * @return Cost of construction in IF
		 */
		default int getConstructionCost()
		{
			return getConstructionManager().getConstructionCost();
		}

		/**
		 * @param client Whether to get the client or server progress
		 * @return Current construction progress in IF
		 */
		default int getCurrentConstruction(boolean client)
		{
			return getConstructionManager().getCurrentConstruction(client);
		}

		/**
		 * Progresses construction by the specified amount of IF
		 *
		 * @param construction Amount of IF to progress construction by
		 */
		default void progressConstruction(int construction)
		{
			getConstructionManager().progressConstruction(construction);
		}

		/**
		 * @return true if construction is finished
		 */
		default boolean isConstructionFinished()
		{
			return getConstructionManager().isConstructionFinished();
		}
	}

	public interface IIIGuiMultiblockTile extends IGuiTile
	{
		@Override
		default int getGuiID()
		{
			return getGUI().ordinal();
		}

		IIGUI getGUI();
	}
}
