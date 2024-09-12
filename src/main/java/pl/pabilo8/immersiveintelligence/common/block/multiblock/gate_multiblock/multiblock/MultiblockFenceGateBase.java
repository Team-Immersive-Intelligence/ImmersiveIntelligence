package pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity.TileEntityGateBase;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

/**
 * @author Pabilo8
 * @since 27.11.2023
 */
public abstract class MultiblockFenceGateBase<T extends TileEntityGateBase<T>> extends MultiblockStuctureBase<T>
{
	public MultiblockFenceGateBase(ResourceLocation loc)
	{
		super(loc);
		offset = new Vec3i(0, 1, 0);
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
