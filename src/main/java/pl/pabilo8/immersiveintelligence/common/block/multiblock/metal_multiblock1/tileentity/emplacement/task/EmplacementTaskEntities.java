package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.BlacklistedRayTracer;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 15.02.2024
 */
public abstract class EmplacementTaskEntities extends EmplacementTask
{
	private final Predicate<Entity> predicate;

	public EmplacementTaskEntities(Predicate<Entity> predicate)
	{
		this.predicate = predicate;
	}

	Entity[] spottedEntities = new Entity[0];
	Entity currentTarget = null;

	@Override
	public float[] getPositionVector(TileEntityEmplacement emplacement)
	{
		final List<BlockPos> allBlocks = emplacement.getAllBlocks();
		final Vec3d vEmplacement = emplacement.getWeaponCenter();

		if(currentTarget!=null)
		{
			if(predicate.test(currentTarget)&&currentTarget.isEntityAlive()&&canEntityBeSeen(currentTarget, vEmplacement, allBlocks, 2))
				return getPosForEntityTask(emplacement, currentTarget);
		}

		for(Entity entity : spottedEntities)
		{
			if(canEntityBeSeen(entity, vEmplacement, allBlocks, 2))
			{
				currentTarget = entity;
				return getPosForEntityTask(emplacement, entity);
			}
		}

		return null;
	}

	/**
	 * Scans for entity using volumetric raytrace
	 *
	 * @param entity       to be traced
	 * @param vEmplacement starting position
	 * @param allBlocks    all blocks of the emplacement, ignored in raytracing
	 * @param maxBlocks    how many blocks of wall can the entity be behind
	 * @return whether entity is visible
	 */
	private boolean canEntityBeSeen(Entity entity, Vec3d vEmplacement, List<BlockPos> allBlocks, int maxBlocks)
	{
		Vec3d vEntity = entity.getPositionVector().addVector(-entity.width/2f, entity.height/2f, -entity.width/2f);
		RayTraceResult rt = BlacklistedRayTracer.traceIgnoringBlocks(entity.world, vEmplacement, vEntity, allBlocks, maxBlocks);

		return rt==null||rt.typeOfHit==Type.MISS;
	}

	@Override
	public boolean shouldContinue()
	{
		return true;
	}

	@Override
	public NBTTagCompound saveToNBT()
	{
		return super.saveToNBT();
	}

	@Override
	public void updateTargets(TileEntityEmplacement emplacement)
	{
		spottedEntities = emplacement.getWorld().getEntitiesWithinAABB(Entity.class, emplacement.currentWeapon.getVisionAABB(), input -> predicate.test(input)&&emplacement.currentWeapon.canSeeEntity(input)).stream().sorted((o1, o2) -> (int)((o1.width*o1.height)-(o2.width*o2.height))*10).toArray(Entity[]::new);
		if(!emplacement.getWorld().isRemote&&emplacement.sendAttackSignal)
			emplacement.handleSendingEnemyPos(spottedEntities);
	}
}
