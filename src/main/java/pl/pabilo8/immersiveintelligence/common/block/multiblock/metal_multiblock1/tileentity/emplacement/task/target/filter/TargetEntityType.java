package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.target.filter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Defines entity categories available to Emplacement target trees.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2026
 */
public enum TargetEntityType implements ISerializableEnum
{
	MOB(1),
	ANIMAL(1<<1),
	PLAYER(1<<2),
	NPC(1<<3),
	VEHICLE(1<<4),
	PROJECTILE(1<<5);

	private final int mask;

	TargetEntityType(int mask)
	{
		this.mask = mask;
	}

	public boolean matches(int entityTypeMask)
	{
		return (entityTypeMask&mask)!=0;
	}

	public static int getTypeMask(@Nonnull Entity entity)
	{
		int result = 0;
		if(entity instanceof IMob)
			result |= MOB.mask;
		if(entity instanceof EntityAnimal)
			result |= ANIMAL.mask;
		if(entity instanceof EntityPlayer)
			result |= PLAYER.mask;
		if(entity instanceof INpc)
			result |= NPC.mask;
		if(entity instanceof EntityVehicleBase)
			result |= VEHICLE.mask;
		if(entity instanceof EntityAmmoArtilleryProjectile)
			result |= PROJECTILE.mask;
		return result;
	}

	@Nullable
	public static TargetEntityType fromName(String name)
	{
		if(name==null)
			return null;
		for(TargetEntityType type : values())
			if(type.getName().equals(name))
				return type;
		return null;
	}
}
