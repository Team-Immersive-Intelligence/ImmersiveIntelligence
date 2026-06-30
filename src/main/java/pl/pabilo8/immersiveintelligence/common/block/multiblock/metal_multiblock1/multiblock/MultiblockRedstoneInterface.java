package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import blusunrize.immersiveengineering.api.IEProperties;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneDataInterface;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class MultiblockRedstoneInterface extends MultiblockStuctureBase<TileEntityRedstoneDataInterface>
{
	public static MultiblockRedstoneInterface INSTANCE;
	public static final int SLOT_PUNCHTAPE_REDSTONE = 0, SLOT_PUNCHTAPE_DATA = 1, SLOT_PUNCHTAPE_OUTPUT = 2;

	public MultiblockRedstoneInterface()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/redstone_data_interface"));
		offset = new Vec3i(0, 0, 0);
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
		return MetalMultiblocks1.REDSTONE_DATA_INTERFACE.getMeta();
	}

	@Override
	protected TileEntityRedstoneDataInterface getMBInstance()
	{
		return new TileEntityRedstoneDataInterface();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderFormedStructure()
	{
		GlStateManager.pushMatrix();

		GlStateManager.rotate(EnumFacing.EAST.getHorizontalAngle(), 0, 1, 0);
		GlStateManager.translate(offset.getX()+1, offset.getY(), -offset.getZ()-(useNewOffset()?1: 0));
		GlStateManager.rotate(EnumFacing.EAST.getHorizontalAngle(), 0, -1, 0);

		AMTUtils.getBRD().renderBlockBrightness(
				IIContent.blockMetalMultiblock1.getStateFromMeta(MetalMultiblocks1.REDSTONE_DATA_INTERFACE.getMeta())
						.withProperty(IEProperties.FACING_HORIZONTAL, EnumFacing.SOUTH)
						.withProperty(IEProperties.DYNAMICRENDER, true),
				1f
		);
		GlStateManager.popMatrix();
	}
}
