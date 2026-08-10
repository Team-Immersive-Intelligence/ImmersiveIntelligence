package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class MultiblockDataInputMachine extends MultiblockStuctureBase<TileEntityDataInputMachine>
{
	public static final int SLOT_INPUT = 0;
	public static final int SLOT_OUTPUT = 1;
	public static MultiblockDataInputMachine INSTANCE;

	public MultiblockDataInputMachine()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/data_input_machine"));
		offset = new Vec3i(0, 1, 0);
		INSTANCE = this;

		//POI
		addPOI(MultiblockPOI.ENERGY_INPUT, "energy");
		addPOI(MultiblockPOI.REDSTONE_INPUT, "redstone");
		addPOI(MultiblockPOI.DATA_OUTPUT, "data");
		addPOI(MultiblockPOI.MISC_HATCH, "hatch");

		//Init Tech Tree
		UpgradeTechTree.getTreeFor(TileEntityDataInputMachine.class)
				.reset()
				.withUpgrade(IIContent.UPGRADE_ADVANCED_DATA, UpgradeTier.TIER_1);
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
		return MetalMultiblocks0.DATA_INPUT_MACHINE.getMeta();
	}

	@Override
	protected TileEntityDataInputMachine getMBInstance()
	{
		return new TileEntityDataInputMachine();
	}
}
