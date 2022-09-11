package pl.pabilo8.immersiveintelligence.api.rotary;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * @author Pabilo8
 * @since 05-01-2020
 */
public class CapabilityRotaryEnergy
{
	@CapabilityInject(IRotaryEnergy.class)
	public static Capability<IRotaryEnergy> ROTARY_ENERGY = null;

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IRotaryEnergy.class, new IStorage<IRotaryEnergy>()
				{
					@Override
					public NBTBase writeNBT(Capability<IRotaryEnergy> capability, IRotaryEnergy instance, EnumFacing side)
					{
						NBTTagCompound tag = new NBTTagCompound();
						tag.setFloat("rpm", instance.getRotationSpeed());
						tag.setFloat("torque", instance.getTorque());
						return tag;
					}

					@Override
					public void readNBT(Capability<IRotaryEnergy> capability, IRotaryEnergy instance, EnumFacing side, NBTBase nbt)
					{
						if(!(instance instanceof RotaryStorage))
							throw new IllegalArgumentException("Can not deserialize to an INSTANCE that isn't the default implementation");
						((RotaryStorage)instance).speed = ((NBTTagCompound)nbt).getFloat("rpm");
						((RotaryStorage)instance).torque = ((NBTTagCompound)nbt).getFloat("torque");
					}
				},
				RotaryStorage::new);
	}
}
