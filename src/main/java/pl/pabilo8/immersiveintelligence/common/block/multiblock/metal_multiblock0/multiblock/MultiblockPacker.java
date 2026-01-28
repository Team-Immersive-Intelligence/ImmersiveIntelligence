package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPacker;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

public class MultiblockPacker extends MultiblockStuctureBase<TileEntityPacker>
{
	public static final int SLOT_CRATE = 0;
	public static MultiblockPacker INSTANCE;

	public MultiblockPacker()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/packer"));
		offset = new Vec3i(1, 1, 0);
		INSTANCE = this;

		UpgradeTechTree.getTreeFor(TileEntityPacker.class)
				.reset()
				.withUpgrade(IIContent.UPGRADE_PACKER_FLUID, UpgradeTier.TIER_1)
				.withUpgrade(IIContent.UPGRADE_PACKER_ENERGY, UpgradeTier.TIER_1)
				.withLockOut(IIContent.UPGRADE_PACKER_FLUID, IIContent.UPGRADE_PACKER_ENERGY)
				.withUpgrade(IIContent.UPGRADE_PACKER_NAMING, UpgradeTier.TIER_1)
				.withUpgrade(IIContent.UPGRADE_PACKER_RAILWAY, UpgradeTier.TIER_1);
	}

	@Override
	protected BlockIIMultiblock<?> getBlock()
	{
		return IIContent.blockMetalMultiblock0;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks0.PACKER.getMeta();
	}

	@Override
	protected TileEntityPacker getMBInstance()
	{
		return new TileEntityPacker();
	}
}
