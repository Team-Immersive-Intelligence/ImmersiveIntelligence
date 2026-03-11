package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import java.util.function.Predicate;

public class MultiblockChemicalPainter extends MultiblockStuctureBase<TileEntityChemicalPainter>
{
	public static MultiblockChemicalPainter INSTANCE;

	public static final int SLOT_INPUT = 0, SLOT_OUTPUT = 1, SLOT_BUCKET_INPUT = 2, SLOT_BUCKET_OUTPUT = 3;
	public static final Predicate<FluidStack> CYAN = fluidStack -> fluidStack!=null&&fluidStack.getFluid().getName().equals("ink_cyan");
	public static final Predicate<FluidStack> MAGENTA = fluidStack -> fluidStack!=null&&fluidStack.getFluid().getName().equals("ink_magenta");
	public static final Predicate<FluidStack> YELLOW = fluidStack -> fluidStack!=null&&fluidStack.getFluid().getName().equals("ink_yellow");
	public static final Predicate<FluidStack> BLACK = fluidStack -> fluidStack!=null&&fluidStack.getFluid().getName().equals("ink");

	public MultiblockChemicalPainter()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/chemical_painter"));
		offset = new Vec3i(2, 0, 0);
		INSTANCE = this;
	}

	@Override
	protected BlockIIMultiblock<MetalMultiblocks1> getBlock()
	{
		return IIContent.blockMetalMultiblock1;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks1.CHEMICAL_PAINTER.getMeta();
	}

	@Override
	protected TileEntityChemicalPainter getMBInstance()
	{
		return new TileEntityChemicalPainter();
	}
}
