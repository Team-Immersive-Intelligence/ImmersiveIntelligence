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
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStructureBase;
import pl.pabilo8.immersiveintelligence.common.util.sound.IISoundAnimation;

public class MultiblockVulcanizer extends MultiblockStructureBase<TileEntityVulcanizer>
{
	public static MultiblockVulcanizer INSTANCE;
	public static final int SLOT_RUBBER = 0, SLOT_COMPOUND = 1, SLOT_SULFUR = 2;
	public final IISoundAnimation workAnimation;

	public MultiblockVulcanizer()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/vulcanizer"));
		offset = new Vec3i(2, 1, 0);
		INSTANCE = this;


		/*TileEntityVulcanizer master = master();
		if(master!=null&&master.processQueue.size() > 0)
		{
			MultiblockProcess<VulcanizerRecipe> process = master.processQueue.get(0);
			switch(sound)
			{
				case "immersiveintelligence:printing_press":
				{
					if(master.processQueue.size() > 1)
					{
						if(IIUtils.inRange(master.processQueue.get(1).processTick, master.processQueue.get(1).maxTicks, 0, 0.16))
							return true;
					}
					return IIUtils.inRange(process.processTick, process.maxTicks, 0, 0.165);
				}
				case "immersiveintelligence:vulcanizer_heating":
					return IIUtils.inRange(process.processTick, process.maxTicks, 0.2, 0.8);
				case "immersiveintelligence:howitzer_rotation_h":
					return IIUtils.inRange(process.processTick, process.maxTicks, 0.78, 0.84);
				case "immersiveintelligence:inserter_forward":
					return IIUtils.inRange(process.processTick, process.maxTicks, 0.93, 0.96);
				case "immersiveintelligence:inserter_backward":
					return IIUtils.inRange(process.processTick, process.maxTicks, 0.85, 0.86);
			}
		}*/


		workAnimation = new IISoundAnimation(1)
				.withRepeatedSound(0.015, 0.165, IISounds.rollingLoop)
				.withRepeatedSound(0.2, 0.8, IISounds.heatingLoop)
				.withRepeatedSound(0.78, 0.84, IISounds.turntableHeavyForwardLoop)
				.withRepeatedSound(0.85, 0.86, IISounds.electricMotorHeavyForwardLoop)
				.withSound(0.89, SoundEvents.BLOCK_LAVA_EXTINGUISH)
				.withRepeatedSound(0.93, 0.96, IISounds.electricMotorHeavyBackwardLoop)
				.compile(1000);

		/*if(process.processTick==Math.ceil(0.16*process.maxTicks))
				world.playSound(null, getBlockPosForPos(70), IISounds.vulcanizerPullStart, SoundCategory.BLOCKS, .65F, 1.5f);
			if(process.processTick==Math.ceil(0.86*process.maxTicks))
				world.playSound(null, getBlockPosForPos(49), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, .65F, 0.75f);
			else if(process.processTick==Math.ceil(0.77*process.maxTicks))
				world.playSound(null, getBlockPosForPos(49), IISounds.vulcanizerPullEnd, SoundCategory.BLOCKS, 1, 1f);
			else if(process.processTick==Math.ceil(0.83*process.maxTicks))
				world.playSound(null, getBlockPosForPos(49), IISounds.vulcanizerPullStart, SoundCategory.BLOCKS, 1, 1f);
			else if(process.processTick==Math.ceil(0.835*process.maxTicks))
				world.playSound(null, getBlockPosForPos(73), IISounds.vulcanizerPullStart, SoundCategory.BLOCKS, 1, 0.5f);
			else if(process.processTick==Math.ceil(0.85*process.maxTicks))
				world.playSound(null, getBlockPosForPos(73), IISounds.vulcanizerPullEnd, SoundCategory.BLOCKS, 1, 0.5f);
			else if(process.processTick==Math.ceil(0.91*process.maxTicks))
				world.playSound(null, getBlockPosForPos(49), IISounds.vulcanizerPullEnd, SoundCategory.BLOCKS, 1, 1f);
			else if(process.processTick==Math.ceil(0.93*process.maxTicks))
				world.playSound(null, getBlockPosForPos(73), IISounds.vulcanizerPullStart, SoundCategory.BLOCKS, 1, 1f);
			else if(process.processTick==Math.ceil(0.95*process.maxTicks))
				world.playSound(null, getBlockPosForPos(73), IISounds.vulcanizerPullStart, SoundCategory.BLOCKS, 1, 0.5f);*/

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
