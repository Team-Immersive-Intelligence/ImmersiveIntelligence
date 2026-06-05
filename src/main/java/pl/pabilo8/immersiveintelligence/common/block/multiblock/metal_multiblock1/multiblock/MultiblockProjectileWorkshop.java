package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStructureBase;

public class MultiblockProjectileWorkshop extends MultiblockStructureBase<TileEntityProjectileWorkshop>
{
	public static final int SLOT_INPUT = 0;
	public static final int SLOT_COMPONENT_INPUT = 1;
	public static final int SLOT_OUTPUT = 2;
	public static MultiblockProjectileWorkshop INSTANCE;

	public MultiblockProjectileWorkshop()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/projectile_workshop"));
		offset = new Vec3i(2, 1, 0);
		INSTANCE = this;
		UpgradeTechTree.getTreeFor(TileEntityProjectileWorkshop.class)
				.withUpgrade(IIContent.UPGRADE_CORE_FILLER, UpgradeTier.TIER_1);
	}

	@Override
	protected BlockIIMultiblock<MetalMultiblocks1> getBlock()
	{
		return IIContent.blockMetalMultiblock1;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks1.PROJECTILE_WORKSHOP.getMeta();
	}

	@Override
	protected TileEntityProjectileWorkshop getMBInstance()
	{
		return new TileEntityProjectileWorkshop();
	}
}
