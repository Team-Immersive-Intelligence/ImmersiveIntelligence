package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock.WoodenMultiblocks;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStructureBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.04.2020
 */
public class MultiblockSawmill extends MultiblockStructureBase<TileEntitySawmill>
{
	public static MultiblockSawmill INSTANCE;

	//Inventory Slots
	public static final int SLOT_INPUT = 0, SLOT_SAWBLADE = 1, SLOT_OUTPUT = 2, SLOT_SAWDUST = 3;

	public MultiblockSawmill()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/sawmill"));
		offset = new Vec3i(2, 0, 0);
		INSTANCE = this;

		UpgradeTechTree.getTreeFor(TileEntitySawmill.class)
				.withUpgrade(IIContent.UPGRADE_IMPROVED_GEARBOX, UpgradeTier.TIER_1)
				.withUpgrade(IIContent.UPGRADE_SAW_UNREGULATOR, UpgradeTier.TIER_1);

	}

	@Override
	protected boolean useNewOffset()
	{
		return false;
	}

	@Override
	protected BlockIIMultiblock<?> getBlock()
	{
		return IIContent.blockWoodenMultiblock;
	}

	@Override
	protected int getMeta()
	{
		return WoodenMultiblocks.SAWMILL.getMeta();
	}

	@Override
	protected TileEntitySawmill getMBInstance()
	{
		return new TileEntitySawmill();
	}
}
