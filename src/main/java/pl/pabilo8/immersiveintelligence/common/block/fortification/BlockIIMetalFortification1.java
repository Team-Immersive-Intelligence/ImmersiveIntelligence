package pl.pabilo8.immersiveintelligence.common.block.fortification;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIMetalFortification1.IIBlockTypes_MetalFortification1;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.util.block.IIBlockInterfaces.IIBlockEnum;
import pl.pabilo8.immersiveintelligence.common.util.block.ItemBlockIIBase;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Pabilo8
 * @since 29.08.2020
 */
public class BlockIIMetalFortification1 extends BlockIIBase<IIBlockTypes_MetalFortification1>
{
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.5D, 1.0D);

	public BlockIIMetalFortification1()
	{
		super("metal_fortification1", PropertyEnum.create("type", IIBlockTypes_MetalFortification1.class), Material.IRON, ItemBlockIIBase::new);
		setHardness(3.0F);
		setResistance(15.0F);

		setToolTypes(IIReference.TOOL_HAMMER);

		this.setBlockLayer(BlockRenderLayer.CUTOUT_MIPPED);
	}

	public enum IIBlockTypes_MetalFortification1 implements IIBlockEnum
	{
		TANK_TRAP
	}


	@Override
	@ParametersAreNonnullByDefault
	@SuppressWarnings("deprecation")
	@Nonnull
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
	}
}
