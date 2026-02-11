package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Marker interface for polymorphic NBT-serializable types.
 *
 * @param <NBT> NBT type used by the implementation (typically NBTTagCompound)
 * @author Pabilo8 (pabilo@iiteam.net)
 * @implSpec Implementations should provide a public no-arg constructor (used by the registry factory)
 * @implNote The interface intentionally does NOT require a factory method (value can be null), all instantiation responsibility is handled by NBTSerialisation's registry.
 * @since 14.09.2025
 */
public interface ITypeNBTSerializable extends INBTSerializable<NBTTagCompound>
{

}
