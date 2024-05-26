package pl.pabilo8.immersiveintelligence.common.block.mines.tileentity;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 06.02.2021
 */
public class TileEntityTellermine extends TileEntityMineBase implements IBlockBounds, IAdvancedCollisionBounds
{
	public ItemStack mineStack = ItemStack.EMPTY;
	private static final ArrayList<AxisAlignedBB> AABB = new ArrayList<>();

	static
	{
		AABB.add(new AxisAlignedBB(0.25f, 0, 0.25f, 0.75f, 0.125f, 0.75f));
	}

	@Override
	public List<AxisAlignedBB> getAdvancedColisionBounds()
	{
		return AABB;
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0.125f, 0, 0.125f,
				0.875f, 0.1875f, 0.875f};
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		super.onEntityCollision(world, entity);
		this.explode();
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return null;
	}
}
