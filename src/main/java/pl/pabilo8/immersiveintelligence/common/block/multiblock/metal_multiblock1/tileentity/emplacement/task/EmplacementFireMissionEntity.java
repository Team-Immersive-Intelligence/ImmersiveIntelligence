package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.02.2024
 */
public class EmplacementFireMissionEntity extends EmplacementFireMission
{
	Entity entity;

	public EmplacementFireMissionEntity(Entity entity)
	{
		this.entity = entity;
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
	public void updateTargets(TileEntityEmplacement emplacement)
	{

	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		int entityId = nbt.getInteger("entity");
		/*if(entityId!=-1)
			entity = emplacement.getWorld().getEntityByID(entityId);*/
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setInteger("entity", entity.getEntityId());
		return nbt;
	}
}
