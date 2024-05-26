package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;

/**
 * @author Pabilo8
 * @since 15.02.2024
 */
public class EmplacementTaskPosition extends EmplacementTask
{
	int shotAmount;
	final BlockPos pos;

	public EmplacementTaskPosition(BlockPos pos, int shotAmount)
	{
		this.pos = pos;
		this.shotAmount = shotAmount;
	}

	public EmplacementTaskPosition(NBTTagCompound tagCompound)
	{
		this(new BlockPos(tagCompound.getInteger("x"), tagCompound.getInteger("y"), tagCompound.getInteger("z")),
				tagCompound.getInteger("shotAmount")
		);
	}

	@Override
	public float[] getPositionVector(TileEntityEmplacement emplacement)
	{
		return emplacement.currentWeapon.getAnglePrediction(emplacement.getWeaponCenter(),
				new Vec3d(pos).addVector(0.5, 0, 0.5),
				Vec3d.ZERO);
	}

	@Override
	public void onShot()
	{
		shotAmount--;
	}

	@Override
	public boolean shouldContinue()
	{
		return shotAmount > 0;
	}

	@Override
	public String getName()
	{
		return "target_position";
	}

	@Override
	public NBTTagCompound saveToNBT()
	{
		NBTTagCompound compound = super.saveToNBT();
		compound.setInteger("x", pos.getX());
		compound.setInteger("y", pos.getY());
		compound.setInteger("z", pos.getZ());
		compound.setInteger("shotAmount", shotAmount);
		return compound;
	}

	@Override
	public void updateTargets(TileEntityEmplacement emplacement)
	{

	}
}
