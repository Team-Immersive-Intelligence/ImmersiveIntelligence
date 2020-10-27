package pl.pabilo8.immersiveintelligence.api.bullets;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Pabilo8
 * @since 04.09.2020
 */
public class DamageBlockPos extends DimensionBlockPos
{
	public float damage;

	public DamageBlockPos(int x, int y, int z, int dim, float dmg)
	{
		super(x, y, z, dim);
		damage = dmg;
	}

	public DamageBlockPos(BlockPos pos, int dim, float dmg)
	{
		this(pos.getX(), pos.getY(), pos.getZ(), dim, dmg);
	}

	public DamageBlockPos(int x, int y, int z, World w, float dmg)
	{
		this(x, y, z, w.provider.getDimension(), dmg);
	}

	public DamageBlockPos(BlockPos pos, World w, float dmg)
	{
		this(pos.getX(), pos.getY(), pos.getZ(), w.provider.getDimension(), dmg);
	}

	public DamageBlockPos(TileEntity te, float dmg)
	{
		this(te.getPos(), te.getWorld(), dmg);
	}

	public DamageBlockPos(DimensionBlockPos pos, float dmg)
	{
		this(pos, pos.dimension, dmg);
	}

}
