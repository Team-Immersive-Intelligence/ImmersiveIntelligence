package pl.pabilo8.immersiveintelligence.common.entity.minecart.capacitor;

import blusunrize.immersiveengineering.common.Config.IEConfig.Machines;
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
public class EntityMinecartCapacitorLV extends EntityMinecartEnergyContainer implements IMinecartBlockPickable
{
	public EntityMinecartCapacitorLV(World worldIn)
	{
		super(worldIn);
	}

	public EntityMinecartCapacitorLV(World worldIn, Vec3d vv)
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
		return BlockTypes_MetalDevice0.CAPACITOR_LV.getMeta();
	}

	@Override
	public int getEnergyCapacity()
	{
		return Machines.capacitorLV_storage;
	}

	@Override
	public int getMaxReceive()
	{
		return Machines.capacitorLV_input;
	}

	@Override
	public int getMaxExtract()
	{
		return Machines.capacitorLV_output;
	}
}
