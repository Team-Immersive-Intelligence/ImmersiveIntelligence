package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A middleman for syncing nullable fields.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.03.2026
 */
public class EasyNullableSyncMechanism<T extends INBTSerializable<NBT>, NBT extends NBTBase> implements INBTSerializable<NBTTagCompound>
{
	private final Supplier<T> getter;
	private final Consumer<T> setter;
	private final Function<NBT, T> generator;
	private boolean alwaysRegenerate = false;

	public EasyNullableSyncMechanism(Supplier<T> getter, Consumer<T> setter, Function<NBT, T> generator)
	{
		this.getter = getter;
		this.setter = setter;
		this.generator = generator;
	}

	public EasyNullableSyncMechanism<T, NBT> withAlwaysRegenerate(boolean alwaysRegenerate)
	{
		this.alwaysRegenerate = alwaysRegenerate;
		return this;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		T t = getter.get();
		NBTTagCompound nbt = new NBTTagCompound();
		if(t!=null)
			nbt.setTag("wrapped", t.serializeNBT());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("wrapped"))
		{
			T t = getter.get();
			if(t==null||alwaysRegenerate)
				//noinspection unchecked
				setter.accept(generator.apply((NBT)nbt.getTag("wrapped")));
			else
				//noinspection unchecked
				t.deserializeNBT((NBT)nbt.getTag("wrapped"));
		}
		else
			setter.accept(null);
	}
}
