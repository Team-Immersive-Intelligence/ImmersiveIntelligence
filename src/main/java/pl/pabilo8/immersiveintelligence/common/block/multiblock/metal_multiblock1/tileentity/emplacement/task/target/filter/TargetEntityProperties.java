package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.util.entity.SyncedDurability;

import javax.annotation.Nonnull;

/**
 * Reads entity properties shared by target filters.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.08.2026
 */
public final class TargetEntityProperties
{
	private TargetEntityProperties()
	{
	}

	/**
	 * @return the parent entity for multipart targets, otherwise the input entity
	 */
	@Nonnull
	public static Entity normalize(@Nonnull Entity entity)
	{
		if(entity instanceof MultiPartEntityPart&&((MultiPartEntityPart)entity).parent instanceof Entity)
			return (Entity)((MultiPartEntityPart)entity).parent;
		return entity;
	}

	public static float getHealth(@Nonnull Entity entity)
	{
		if(entity instanceof EntityLivingBase)
			return ((EntityLivingBase)entity).getHealth();
		if(entity instanceof EntityVehicleBase)
		{
			SyncedDurability durability = ((EntityVehicleBase<?>)entity).durabilityMain;
			return durability==null?Float.NaN: (float)(durability.maxDurability*durability.getDamageFactor());
		}
		return Float.NaN;
	}

	public static float getMaxHealth(@Nonnull Entity entity)
	{
		if(entity instanceof EntityLivingBase)
			return ((EntityLivingBase)entity).getMaxHealth();
		if(entity instanceof EntityVehicleBase)
		{
			SyncedDurability durability = ((EntityVehicleBase<?>)entity).durabilityMain;
			return durability==null?Float.NaN: durability.maxDurability;
		}
		return Float.NaN;
	}
}
