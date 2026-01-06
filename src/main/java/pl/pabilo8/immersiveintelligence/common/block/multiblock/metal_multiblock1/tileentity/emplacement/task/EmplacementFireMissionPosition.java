package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.02.2024
 */
public class EmplacementFireMissionPosition extends EmplacementFireMission
{
	protected BlockPos pos;
	protected int shotAmount;

	public EmplacementFireMissionPosition(BlockPos pos, int shotAmount)
	{
		this.pos = pos;
		this.shotAmount = shotAmount;
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
	public void updateTargets(TileEntityEmplacement emplacement)
	{

	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setInteger("x", pos.getX());
		nbt.setInteger("y", pos.getY());
		nbt.setInteger("z", pos.getZ());
		nbt.setInteger("shotAmount", shotAmount);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		shotAmount = nbt.getInteger("shotAmount");
		pos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
	}
}
