package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.style.StyleConstraints;
import pl.pabilo8.immersiveintelligence.api.style.StyleConstraints.PaintStyleConstraint;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import java.util.Collections;

public class MultiblockFlagpole extends MultiblockStuctureBase<TileEntityFlagpole>
{
	public static MultiblockFlagpole INSTANCE;
	public static StyleConstraints STYLE_CONSTRAINTS;

	public MultiblockFlagpole()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/flagpole"));
		offset = new Vec3i(1, 3, 0);
		INSTANCE = this;

		//POI
		addPOI(MultiblockPOI.MISC_FLAGPOLE, "pole");

		//Customization
		STYLE_CONSTRAINTS = new StyleConstraints("sandbags", PaintStyleConstraint.NOT_APPLICABLE,
				Sets.newHashSet("sandbags", "wooden", "steel", "bricks", "concrete"),
				Collections.emptySet()
		);

		//Upgrades
		UpgradeTechTree.getTreeFor(TileEntityFlagpole.class)
				.reset()
				.withUpgrade(IIContent.UPGRADE_FLAGPOLE_CAPTURE_DEFIANCE, UpgradeTier.TIER_1)
				.withUpgrade(IIContent.UPGRADE_FLAGPOLE_TASER_LOCKS, UpgradeTier.TIER_2)
				.withUpgrade(IIContent.UPGRADE_FLAGPOLE_UNIT_POST, UpgradeTier.TIER_1)
				.withUpgrade(IIContent.UPGRADE_FLAGPOLE_DISTRESS_SIGNAL, UpgradeTier.TIER_1)
				.withDependency(IIContent.UPGRADE_FLAGPOLE_CAPTURE_DEFIANCE, IIContent.UPGRADE_FLAGPOLE_TASER_LOCKS)
				.withLockOut(IIContent.UPGRADE_FLAGPOLE_UNIT_POST, IIContent.UPGRADE_FLAGPOLE_TASER_LOCKS);
	}

	@Override
	protected boolean useNewOffset()
	{
		return false;
	}

	@Override
	protected BlockIIMultiblock<MetalMultiblocks1> getBlock()
	{
		return IIContent.blockMetalMultiblock1;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks1.FLAGPOLE.getMeta();
	}

	@Override
	protected TileEntityFlagpole getMBInstance()
	{
		return new TileEntityFlagpole();
	}
}
