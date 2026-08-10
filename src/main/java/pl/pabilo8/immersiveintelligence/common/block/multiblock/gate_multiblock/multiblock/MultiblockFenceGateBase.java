package pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity.TileEntityGateBase;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.11.2023
 */
public abstract class MultiblockFenceGateBase<T extends TileEntityGateBase<T>> extends MultiblockStuctureBase<T>
{
	public MultiblockFenceGateBase(Class<T> klass, ResourceLocation loc)
	{
		super(loc);
		offset = new Vec3i(0, 1, 0);
		addPOI(MultiblockPOI.MISC_DOOR, "gate");
		addPOI(MultiblockPOI.REDSTONE_CABLE_MOUNT, "redstone");
		UpgradeTechTree.getTreeFor(klass)
				.withUpgrade(IIContent.UPGRADE_REDSTONE_ACTIVATION, UpgradeTier.TIER_1)
				.withUpgrade(IIContent.UPGRADE_RAZOR_WIRE, UpgradeTier.TIER_1);
	}

	@Override
	protected BlockIIMultiblock<?> getBlock()
	{
		return IIContent.blockFenceGateMultiblock;
	}

	@Override
	public ResLoc getAABBFileLocation()
	{
		return ResLoc.of(IIReference.RES_AABB, "multiblock/gate")
				.withExtension(ResLoc.EXT_JSON);
	}
}
