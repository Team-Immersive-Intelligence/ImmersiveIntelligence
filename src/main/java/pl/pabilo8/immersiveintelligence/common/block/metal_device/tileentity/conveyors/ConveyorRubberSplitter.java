package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.conveyors;

import blusunrize.immersiveengineering.common.blocks.metal.conveyors.ConveyorSplit;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * @author Pabilo8
 * @since 29.04.2021
 */
public class ConveyorRubberSplitter extends ConveyorSplit
{
	public ConveyorRubberSplitter(EnumFacing startingOutputFace)
	{
		super(startingOutputFace);
	}

	public static ResourceLocation texture_on = new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/conveyors/split");
	public static ResourceLocation texture_off = new ResourceLocation(ImmersiveIntelligence.MODID+":blocks/conveyors/split_off");

	@Override
	public ResourceLocation getActiveTexture()
	{
		return texture_on;
	}

	@Override
	public ResourceLocation getInactiveTexture()
	{
		return texture_off;
	}


	@Override
	public Vec3d getDirection(TileEntity conveyorTile, Entity entity, EnumFacing facing)
	{
		return super.getDirection(conveyorTile, entity, facing).scale(1.75);
	}
}
