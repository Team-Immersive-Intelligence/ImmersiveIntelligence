package pl.pabilo8.immersiveintelligence.api.api.protection.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Provides protection capabilities and delegates missing capability queries to a parent provider.
 * Forge serialises attached providers separately, so parent NBT is not copied.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.08.2026
 */
public class ProtectionCapabilityProvider implements ICapabilitySerializable<NBTTagCompound>
{
	private static final String SERIALIZED_CAPABILITIES = "iiProtectionCapabilities";
	private final Map<Capability<?>, Object> capabilities = new IdentityHashMap<>();
	@Nullable
	private final ICapabilityProvider parent;

	public ProtectionCapabilityProvider()
	{
		this(null);
	}

	public ProtectionCapabilityProvider(@Nullable ICapabilityProvider parent)
	{
		this.parent = parent;
	}

	public <T> ProtectionCapabilityProvider with(@Nonnull Capability<T> capability, @Nonnull T instance)
	{
		capabilities.put(capability, instance);
		return this;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capabilities.containsKey(capability)||(parent!=null&&parent.hasCapability(capability, facing));
	}

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
	{
		Object value = capabilities.get(capability);
		if(value!=null)
			return (T)value;
		return parent==null?null: parent.getCapability(capability, facing);
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound serializedCapabilities = new NBTTagCompound();
		for(Map.Entry<Capability<?>, Object> entry : capabilities.entrySet())
		{
			Object instance = entry.getValue();
			if(!(instance instanceof INBTSerializable))
				continue;

			Object serialized = ((INBTSerializable<?>)instance).serializeNBT();
			if(serialized instanceof NBTBase)
				serializedCapabilities.setTag(entry.getKey().getName(), (NBTBase)serialized);
		}
		if(!serializedCapabilities.hasNoTags())
			nbt.setTag(SERIALIZED_CAPABILITIES, serializedCapabilities);
		return nbt;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void deserializeNBT(NBTTagCompound nbt)
	{
		if(!nbt.hasKey(SERIALIZED_CAPABILITIES, Constants.NBT.TAG_COMPOUND))
			return;
		NBTTagCompound serializedCapabilities = nbt.getCompoundTag(SERIALIZED_CAPABILITIES);
		for(Map.Entry<Capability<?>, Object> entry : capabilities.entrySet())
		{
			Object instance = entry.getValue();
			String name = entry.getKey().getName();
			if(instance instanceof INBTSerializable&&serializedCapabilities.hasKey(name))
				((INBTSerializable)instance).deserializeNBT(serializedCapabilities.getTag(name));
		}
	}

}
