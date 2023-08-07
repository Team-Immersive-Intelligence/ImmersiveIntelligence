package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.utils.vehicles.IVehicleMultiPart;

/**
 * @author Pabilo8
 * @since 23.12.2022
 */
public class EntityVehicleParticleEmitter extends EntityVehiclePart
{
	private final static AxisAlignedBB EMPTY = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	public EntityVehicleParticleEmitter(IVehicleMultiPart parent, String partName, Vec3d offset)
	{
		super(parent, partName, offset, EMPTY);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}
}
