package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import pl.pabilo8.immersiveintelligence.common.IILogger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Polymorphic variant of EasyCollection for entries implementing ITypeNBTSerializable.
 * <p>
 * Stores each element as a wrapper compound: {type, value}
 * where type is resolved through {@link NBTSerialisation}'s polymorphic registry.
 *
 * @author Pabilo8
 * @since 08.02.2026
 */
public class EasyMultiTypeCollection<BASE extends ITypeNBTSerializable> extends EasyCollection<BASE, NBTTagCompound>
{
	@Nonnull
	private final Class<? extends ITypeNBTSerializable> baseType;

	public EasyMultiTypeCollection(@Nonnull Class<? extends ITypeNBTSerializable> baseType)
	{
		// constructor is unused (entries are created from type registry during deserialize)
		super(() -> {
			IILogger.error("Attempting to serialize an EasyMultiTypeCollection with a constructor! "+
					"Constructor is a no-op and should not be used, as entries are created from the type registry during deserialization. "+
					"Returning null."
			);
			return null;
		});
		this.baseType = baseType;
		NBTSerialisation.registerPolimorphicTypeClass(baseType);
	}

	@Override
	public NBTTagList serializeNBT()
	{
		NBTTagList list = new NBTTagList();
		for(ITypeNBTSerializable entry : this)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			if(entry==null)
			{
				// keep empty compound = null marker (matches NBTSerialisation's behavior)
				list.appendTag(nbt);
				continue;
			}
			nbt.setString("type", getClassId(entry));
			nbt.setTag("value", entry.serializeNBT());
			list.appendTag(nbt);
		}
		return list;
	}

	@Override
	public void deserializeNBT(NBTTagList nbt)
	{
		clear();
		for(NBTBase base : nbt)
		{
			if(!(base instanceof NBTTagCompound))
				continue;

			ITypeNBTSerializable entry = NBTSerialisation.deserializePolymorphic((NBTTagCompound)base);
			if(entry!=null)
				//noinspection unchecked
				add(((BASE)entry));
		}
	}

	private static String getClassId(@Nonnull ITypeNBTSerializable entry)
	{
		return entry.getClass().getSimpleName();
	}

	@Nullable
	@SuppressWarnings({"MethodDoesntCallSuperMethod"})
	@Override
	public EasyMultiTypeCollection<BASE> clone()
	{
		EasyMultiTypeCollection<BASE> copy = new EasyMultiTypeCollection<>(baseType);
		copy.deserializeNBT(this.serializeNBT());
		return copy;
	}
}
