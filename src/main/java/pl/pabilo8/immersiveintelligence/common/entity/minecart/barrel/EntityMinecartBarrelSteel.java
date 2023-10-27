package pl.pabilo8.immersiveintelligence.common.entity.minecart.barrel;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.IMinecartBlockPickable;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.EntityMinecartFluidContainer;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class EntityMinecartBarrelSteel extends EntityMinecartFluidContainer implements IMinecartBlockPickable
{
	public EntityMinecartBarrelSteel(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartBarrelSteel(World worldIn, Vec3d vv)
	{
		super(worldIn, vv);
	}

	@Override
	protected Block getCarriedBlock()
	{
		return IEContent.blockMetalDevice0;
	}

	@Override
	protected int getBlockMetaID()
	{
		return BlockTypes_MetalDevice0.BARREL.getMeta();
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
		return 12000;
	}


}
