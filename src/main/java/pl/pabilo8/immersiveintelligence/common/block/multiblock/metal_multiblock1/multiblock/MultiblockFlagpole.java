package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.style.StyleConstraints;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

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
		STYLE_CONSTRAINTS = new StyleConstraints("sandbags", false,
				Sets.newHashSet("sandbags", "wooden", "steel", "bricks", "concrete"),
				Collections.emptySet()
		);
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
