package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.style.StyleConstraints;
import pl.pabilo8.immersiveintelligence.api.style.StyleConstraints.PaintStyleConstraint;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFuelStation;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import java.util.Collections;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class MultiblockFuelStation extends MultiblockStuctureBase<TileEntityFuelStation>
{
	public static MultiblockFuelStation INSTANCE;
	public static StyleConstraints STYLE_CONSTRAINTS;

	public MultiblockFuelStation()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/fuel_station"));
		offset = new Vec3i(0, 1, 0);
		INSTANCE = this;

		//POI
		addPOI(MultiblockPOI.ENERGY_INPUT, "energy_input");
		addPOI(MultiblockPOI.FLUID_INPUT, "fluid_input");
		addPOI(MultiblockPOI.REDSTONE, "redstone");
		addPOI(MultiblockPOI.MISC_CONTROL_PANEL, "table");

		//Customization
		STYLE_CONSTRAINTS = new StyleConstraints("wooden", PaintStyleConstraint.NOT_APPLICABLE,
				Sets.newHashSet("wooden", "naval", "steel"),
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
		return MetalMultiblocks1.FUEL_STATION.getMeta();
	}

	@Override
	protected TileEntityFuelStation getMBInstance()
	{
		return new TileEntityFuelStation();
	}
}
