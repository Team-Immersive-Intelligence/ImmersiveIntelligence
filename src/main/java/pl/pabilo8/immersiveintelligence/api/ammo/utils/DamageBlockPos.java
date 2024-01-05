package pl.pabilo8.immersiveintelligence.api.ammo.utils;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 04.09.2020
 */
@SuppressWarnings("unused")
public class DamageBlockPos extends DimensionBlockPos
{
	/**
	 * Damage level of the block, 0 is undamaged, 1 is destroyed
	 */
	public float damage;

	/**
	 * @param x   The x position
	 * @param y   The y position
	 * @param z   The z position
	 * @param dim The dimension
	 * @param dmg The block damage
	 */
	public DamageBlockPos(int x, int y, int z, int dim, float dmg)
	{
		super(x, y, z, dim);
		damage = dmg;
	}

	public DamageBlockPos(BlockPos pos, int dim, float dmg)
	{
		this(pos.getX(), pos.getY(), pos.getZ(), dim, dmg);
	}

	/**
	 * @param x   The x position
	 * @param y   The y position
	 * @param z   The z position
	 * @param w   The world the position is in
	 * @param dmg The block damage
	 */
	public DamageBlockPos(int x, int y, int z, World w, float dmg)
	{
		this(x, y, z, w.provider.getDimension(), dmg);
	}

	/**
	 * @param pos The position
	 * @param w   The world to copy
	 * @param dmg The block damage
	 */
	public DamageBlockPos(BlockPos pos, World w, float dmg)
	{
		this(pos.getX(), pos.getY(), pos.getZ(), w.provider.getDimension(), dmg);
	}

	/**
	 * @param te  The TileEntity that holds the position
	 * @param dmg The block damage
	 */
	public DamageBlockPos(TileEntity te, float dmg)
	{
		this(te.getPos(), te.getWorld(), dmg);
	}

	/**
	 * @param pos The position and dimension
	 * @param dmg The damage to set
	 */
	public DamageBlockPos(DimensionBlockPos pos, float dmg)
	{
		this(pos, pos.dimension, dmg);
	}

}
