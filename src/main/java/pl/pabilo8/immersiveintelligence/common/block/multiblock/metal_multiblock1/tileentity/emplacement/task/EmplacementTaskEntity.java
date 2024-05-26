package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;

/**
 * @author Pabilo8
 * @since 15.02.2024
 */
public class EmplacementTaskEntity extends EmplacementTask
{
	Entity entity;

	public EmplacementTaskEntity(Entity entity)
	{
		this.entity = entity;
	}

	public EmplacementTaskEntity(TileEntityEmplacement emplacement, NBTTagCompound tagCompound)
	{
		this(emplacement.getWorld().getEntityByID(tagCompound.getInteger("target_uuid")));
	}

	@Override
	public float[] getPositionVector(TileEntityEmplacement emplacement)
	{
		return getPosForEntityTask(emplacement, entity);
	}

	@Override
	public boolean shouldContinue()
	{
		return entity!=null&&entity.isEntityAlive();
	}

	@Override
	public String getName()
	{
		return "target_entity";
	}

	@Override
	public NBTTagCompound saveToNBT()
	{
		NBTTagCompound compound = super.saveToNBT();
		compound.setInteger("target_uuid", entity.getEntityId());
		return compound;
	}

	@Override
	public void updateTargets(TileEntityEmplacement emplacement)
	{

	}
}
