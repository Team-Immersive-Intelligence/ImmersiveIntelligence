package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeTechTree;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeTier;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStructureBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class MultiblockArithmeticLogicMachine extends MultiblockStructureBase<TileEntityArithmeticLogicMachine>
{
	public static MultiblockArithmeticLogicMachine INSTANCE;
	/**
	 * ALM has 4 circuits by default, 6 with upgrade<br>
	 * and 16 storage slots for additional circuits
	 */
	public static final int CIRCUITS_BASE = 4;
	public static final int CIRCUITS_UPGRADED = 6;
	public static final int STORAGE_SLOTS = 18;

	public MultiblockArithmeticLogicMachine()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/arithmetic_logic_machine"));
		offset = new Vec3i(0, 1, 1);
		INSTANCE = this;

		//Init Tech Tree
		UpgradeTechTree.getTreeFor(TileEntityArithmeticLogicMachine.class)
				.reset()
				.withUpgrade(IIContent.UPGRADE_CIRCUIT_RACKS, UpgradeTier.TIER_1)
				.withUpgrade(IIContent.UPGRADE_MEMORY, UpgradeTier.TIER_1);
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
		return MetalMultiblocks0.ARITHMETIC_LOGIC_MACHINE.getMeta();
	}

	@Override
	protected TileEntityArithmeticLogicMachine getMBInstance()
	{
		return new TileEntityArithmeticLogicMachine();
	}
}
