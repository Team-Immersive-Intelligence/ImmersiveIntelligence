package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.conveyors;

import blusunrize.immersiveengineering.common.blocks.metal.conveyors.ConveyorCovered;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

/**
 * @author Pabilo8
 * @since 29.04.2021
 */
public class ConveyorRubberCovered extends ConveyorCovered
{
	@Override
	public ResourceLocation getActiveTexture()
	{
		return ConveyorRubber.texture_on;
	}

	@Override
	public ResourceLocation getInactiveTexture()
	{
		return ConveyorRubber.texture_off;
	}

	@Override
	public Vec3d getDirection(TileEntity conveyorTile, Entity entity, EnumFacing facing)
	{
		return super.getDirection(conveyorTile, entity, facing).scale(1.75);
	}
}
