package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityVulcanizer;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.common.util.sound.IISoundAnimation;

public class MultiblockVulcanizer extends MultiblockStuctureBase<TileEntityVulcanizer>
{
	public static MultiblockVulcanizer INSTANCE;
	public static final int SLOT_RUBBER = 0, SLOT_COMPOUND = 1, SLOT_SULFUR = 2;
	public final IISoundAnimation workAnimation;

	public MultiblockVulcanizer()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/vulcanizer"));
		offset = new Vec3i(2, 1, 0);
		INSTANCE = this;

		//Vulcanizer
		addPOI(MultiblockPOI.ENERGY_INPUT, "energy");
		addPOI(MultiblockPOI.REDSTONE_INPUT, "redstone");
		addPOI(MultiblockPOI.ITEM_INPUT, "inputs");
		addPOI(MultiblockPOI.ITEM_OUTPUT, "outputs");

		//Animations
		workAnimation = new IISoundAnimation(1)
				.withRepeatedSound(0.015, 0.165, IISounds.rollingLoop)
				.withRepeatedSound(0.2, 0.8, IISounds.heatingLoop)
				.withRepeatedSound(0.78, 0.84, IISounds.turntableHeavyForwardLoop)
				.withRepeatedSound(0.85, 0.86, IISounds.electricMotorHeavyForwardLoop)
				.withSound(0.89, SoundEvents.BLOCK_LAVA_EXTINGUISH)
				.withRepeatedSound(0.93, 0.96, IISounds.electricMotorHeavyBackwardLoop)
				.compile(1000);

	}

	@Override
	protected BlockIIMultiblock<MetalMultiblocks1> getBlock()
	{
		return IIContent.blockMetalMultiblock1;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks1.VULCANIZER.getMeta();
	}

	@Override
	protected TileEntityVulcanizer getMBInstance()
	{
		return new TileEntityVulcanizer();
	}
}
