package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockSmallChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
/**
 * @author Avalon (avalon@iiteam.net)
 * @since 14.11.2026
 */
public class TileEntitySmallChemicalReactor extends TileEntityMultiblockIIGeneric<TileEntitySmallChemicalReactor>
		implements IIIGuiMultiblockTile
{
	public TileEntitySmallChemicalReactor()
	{
		super(MultiblockSmallChemicalReactor.INSTANCE);
	}

	@Override
	protected void onUpdate()
	{
	}

	@Override
	public boolean canOpenGui()
	{
		return formed;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.SMALL_CHEMICAL_REACTOR;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}
}
