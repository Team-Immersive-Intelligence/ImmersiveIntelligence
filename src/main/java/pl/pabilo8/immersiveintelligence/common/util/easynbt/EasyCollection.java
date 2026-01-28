package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.util.lambda.NBTTagCollector;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 17.09.2025
 */
public class EasyCollection<T extends INBTSerializable<NBT>, NBT extends NBTBase> implements List<T>, INBTSerializable<NBTTagList>, Cloneable
{
	private final ArrayList<T> collection = new ArrayList<>();
	private final Supplier<T> constructor;

	public EasyCollection(Supplier<T> constructor)
	{
		this.constructor = constructor;
	}

	//--- List ---//

	@Override
	public int size()
	{
		return collection.size();
	}

	@Override
	public boolean isEmpty()
	{
		return collection.isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		return collection.contains(o);
	}

	@Nonnull
	@Override
	public Iterator<T> iterator()
	{
		return collection.iterator();
	}

	@Nonnull
	@Override
	public Object[] toArray()
	{
		return collection.toArray();
	}

	@Nonnull
	@Override
	public <T1> T1[] toArray(@Nonnull T1[] a)
	{
		return collection.toArray(a);
	}

	@Override
	public boolean add(T t)
	{
		return collection.add(t);
	}

	@Override
	public boolean remove(Object o)
	{
		return collection.remove(o);
	}

	@Override
	public boolean containsAll(@Nonnull Collection<?> c)
	{
		return collection.containsAll(c);
	}

	@Override
	public boolean addAll(@Nonnull Collection<? extends T> c)
	{
		return collection.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c)
	{
		return collection.addAll(c);
	}

	@Override
	public boolean removeAll(@Nonnull Collection<?> c)
	{
		return collection.removeAll(c);
	}

	@Override
	public boolean retainAll(@Nonnull Collection<?> c)
	{
		return collection.retainAll(c);
	}

	@Override
	public void clear()
	{
		collection.clear();
	}

	@Override
	public T get(int index)
	{
		return collection.get(index);
	}

	@Override
	public T set(int index, T element)
	{
		return collection.set(index, element);
	}

	@Override
	public void add(int index, T element)
	{
		collection.add(index, element);
	}

	@Override
	public T remove(int index)
	{
		return collection.remove(index);
	}

	@Override
	public int indexOf(Object o)
	{
		return collection.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return collection.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return collection.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index)
	{
		return collection.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex)
	{
		return collection.subList(fromIndex, toIndex);
	}

	//--- INBTSerializable ---//

	@Override
	public NBTTagList serializeNBT()
	{
		return collection.stream()
				.map(INBTSerializable::serializeNBT)
				.collect(NBTTagCollector.collect());
	}

	@Override
	public void deserializeNBT(NBTTagList nbt)
	{
		collection.clear();
		for(NBTBase nbtBase : nbt)
		{
			T t = constructor.get();
			//noinspection unchecked
			t.deserializeNBT((NBT)nbtBase);
			collection.add(t);
		}
	}

	//--- Cloneable ---//

	@SuppressWarnings({"MethodDoesntCallSuperMethod"})
	@Override
	public EasyCollection<T, NBT> clone()
	{
		EasyCollection<T, NBT> collection = new EasyCollection<>(constructor);
		collection.deserializeNBT(this.serializeNBT());
		return collection;
	}
}
