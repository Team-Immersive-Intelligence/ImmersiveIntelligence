package pl.pabilo8.immersiveintelligence.client.util;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon.EntityMountedWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;

/**
 * Immersive Intelligence's extension of KeyBinding offering builder-pattern methods.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 04.12.2025
 */
public class IIKeybind extends KeyBinding
{
	public static final IKeyConflictContext VEHICLE_KEY_CONTEXT = new IKeyConflictContext()
	{
		@Override
		public boolean isActive()
		{
			Entity seat = ClientUtils.mc().player.getRidingEntity();
			return seat instanceof EntityVehicleSeat||seat instanceof EntityMountedWeapon;
		}

		@Override
		public boolean conflicts(IKeyConflictContext other)
		{
			return other==this;
		}
	};
	public static final String CATEGORY_GAMEPLAY = "key.categories.gameplay";
	public static final String CATEGORY_VEHICLES = "key.categories.iivehicle";

	public IIKeybind(String name, int keyCode, String category)
	{
		super("key."+ImmersiveIntelligence.MODID+"."+name, keyCode, category);
	}

	public IIKeybind withContext(IKeyConflictContext context)
	{
		this.setKeyConflictContext(context);
		return this;
	}


	public IIKeybind register()
	{
		ClientRegistry.registerKeyBinding(this);
		return this;
	}
}
