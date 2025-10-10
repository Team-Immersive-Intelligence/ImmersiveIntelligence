package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part;


import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleDurability;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 02.10.2025
 */
public interface IVehicleComponent
{
	@Nullable
	VehicleDurability getDurability();

	void onUpdate();
}
