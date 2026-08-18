package pl.pabilo8.immersiveintelligence.api.api.protection.capability;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.api.protection.RadiationEmitter;

import javax.annotation.Nullable;

/**
 * Registration point for II protection and radiation-emitter capabilities.
 *
 * @since 0.3.1
 */
public final class ProtectionCapabilities
{
	public static final ResourceLocation RADIATION_EMITTER_ID = new ResourceLocation(ImmersiveIntelligence.MODID, "radiation_emitter");

	@CapabilityInject(IAcidProtection.class)
	public static Capability<IAcidProtection> ACID_PROTECTION = null;
	@CapabilityInject(ICorrosionProtection.class)
	public static Capability<ICorrosionProtection> CORROSION_PROTECTION = null;
	@CapabilityInject(IGasProtection.class)
	public static Capability<IGasProtection> GAS_PROTECTION = null;
	@CapabilityInject(IInfraredProtection.class)
	public static Capability<IInfraredProtection> INFRARED_PROTECTION = null;
	@CapabilityInject(IRadiationProtection.class)
	public static Capability<IRadiationProtection> RADIATION_PROTECTION = null;
	@CapabilityInject(IRadiationEmitter.class)
	public static Capability<IRadiationEmitter> RADIATION_EMITTER = null;

	private static boolean registered;

	private ProtectionCapabilities()
	{
	}

	public static void register()
	{
		if(registered)
			return;
		registered = true;

		CapabilityManager.INSTANCE.register(IAcidProtection.class, new EmptyStorage<>(), () -> () -> false);
		CapabilityManager.INSTANCE.register(ICorrosionProtection.class, new EmptyStorage<>(), () -> () -> false);
		CapabilityManager.INSTANCE.register(IGasProtection.class, new EmptyStorage<>(), () -> () -> false);
		CapabilityManager.INSTANCE.register(IInfraredProtection.class, new EmptyStorage<>(), () -> () -> false);
		CapabilityManager.INSTANCE.register(IRadiationProtection.class, new EmptyStorage<>(), () -> () -> false);
		CapabilityManager.INSTANCE.register(IRadiationEmitter.class, new SerializableStorage<>(), RadiationEmitter::new);
	}

	static boolean isSerializableCapabilityInstance(Object instance)
	{
		// Entity and tile entity instances own their NBT.
		// Do not serialize them as capability data because this calls the capability dispatcher again.
		return instance instanceof INBTSerializable
				&&!(instance instanceof Entity)
				&&!(instance instanceof TileEntity);
	}

	private static final class EmptyStorage<T> implements IStorage<T>
	{
		@Override
		public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side)
		{
			return new NBTTagCompound();
		}

		@Override
		public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt)
		{
		}
	}

	private static final class SerializableStorage<T> implements IStorage<T>
	{
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side)
		{
			if(isSerializableCapabilityInstance(instance))
				return ((INBTSerializable<?>)instance).serializeNBT();
			return null;
		}

		@Override
		@SuppressWarnings({"rawtypes", "unchecked"})
		public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt)
		{
			if(isSerializableCapabilityInstance(instance))
				((INBTSerializable)instance).deserializeNBT(nbt);
		}
	}
}
