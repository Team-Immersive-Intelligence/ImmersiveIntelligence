package pl.pabilo8.immersiveintelligence.common.entity.minecart.barrel;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDevice0;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWoodenBarrel;
import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.IMinecartBlockPickable;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.EntityMinecartFluidContainer;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class EntityMinecartBarrelWooden extends EntityMinecartFluidContainer implements IMinecartBlockPickable
{
	public EntityMinecartBarrelWooden(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartBarrelWooden(World worldIn, Vec3d vv)
	{
		super(worldIn, vv);
	}

	@Override
	protected Block getCarriedBlock()
	{
		return IEContent.blockWoodenDevice0;
	}

	@Override
	protected int getBlockMetaID()
	{
		return BlockTypes_WoodenDevice0.BARREL.getMeta();
	}

	@Override
	public boolean isGasAllowed()
	{
		return false;
	}

	@Override
	public int getMaxTemperature()
	{
		return TileEntityWoodenBarrel.IGNITION_TEMPERATURE;
	}

	@Override
	public int getTankCapacity()
	{
		return 12000;
	}


}
