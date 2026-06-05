package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrintingPress;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStructureBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class MultiblockPrintingPress extends MultiblockStructureBase<TileEntityPrintingPress>
{
	public static final int SLOT_PAPER = 0;
	public static final int SLOT_OUTPUT = 1;
	public static final int SLOT_BUCKET_IN = 2;
	public static final int SLOT_BUCKET_OUT = 3;
	public static MultiblockPrintingPress INSTANCE;

	public MultiblockPrintingPress()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/printing_press"));
		offset = new Vec3i(1, 1, 0);
		INSTANCE = this;

		UpgradeTechTree.getTreeFor(TileEntityPrintingPress.class)
				.withUpgrade(IIContent.UPGRADE_PRESS_PUNCHTAPES, UpgradeTier.TIER_1)
				.withUpgrade(IIContent.UPGRADE_PRESS_BATCHING, UpgradeTier.TIER_1)
				.withUpgrade(IIContent.UPGRADE_PRESS_ENVELOPER, UpgradeTier.TIER_1);
	}

	@Override
	protected boolean useNewOffset()
	{
		return false;
	}

	@Override
	protected BlockIIMultiblock<?> getBlock()
	{
		return IIContent.blockMetalMultiblock0;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks0.PRINTING_PRESS.getMeta();
	}

	@Override
	protected TileEntityPrintingPress getMBInstance()
	{
		return new TileEntityPrintingPress();
	}
}
