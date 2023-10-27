package pl.pabilo8.immersiveintelligence.common.compat.it;

import mctmods.immersivetechnology.common.Config.ITConfig.Barrels;
import mctmods.immersivetechnology.common.ITContent;
import mctmods.immersivetechnology.common.blocks.metal.types.BlockType_MetalBarrel;
import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.IMinecartBlockPickable;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.EntityMinecartFluidContainer;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class EntityMinecartBarrelOpen extends EntityMinecartFluidContainer implements IMinecartBlockPickable
{
	public EntityMinecartBarrelOpen(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartBarrelOpen(World worldIn, Vec3d vv)
	{
		super(worldIn, vv);
	}

	@Override
	protected Block getCarriedBlock()
	{
		return ITContent.blockMetalBarrel;
	}

	@Override
	protected int getBlockMetaID()
	{
		return BlockType_MetalBarrel.BARREL_OPEN.getMeta();
	}

	@Override
	public boolean isGasAllowed()
	{
		return true;
	}

	@Override
	public int getMaxTemperature()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public int getTankCapacity()
	{
		return Barrels.barrel_open_tankSize;
	}


}
