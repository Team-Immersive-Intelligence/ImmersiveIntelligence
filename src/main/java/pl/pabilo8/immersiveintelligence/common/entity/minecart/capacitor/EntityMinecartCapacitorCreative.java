package pl.pabilo8.immersiveintelligence.common.entity.minecart.capacitor;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDevice0;
import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.IMinecartBlockPickable;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.EntityMinecartEnergyContainer;

/**
 * @author Pabilo8
 * @since 07.11.2021
 */
public class EntityMinecartCapacitorCreative extends EntityMinecartEnergyContainer implements IMinecartBlockPickable
{
	public EntityMinecartCapacitorCreative(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartCapacitorCreative(World worldIn, Vec3d vv)
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
		return BlockTypes_MetalDevice0.CAPACITOR_CREATIVE.getMeta();
	}

	@Override
	public int getEnergyCapacity()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public int getMaxReceive()
	{
		return 0;
	}

	@Override
	public int getMaxExtract()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isInfinite()
	{
		return true;
	}
}
