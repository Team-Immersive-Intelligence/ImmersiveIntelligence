package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author Pabilo8
 * @since 06.06.2022
 */
public class NBTTagCollector implements Collector<NBTTagCompound, NBTTagList, NBTTagList>
{
	@Override
	public Supplier<NBTTagList> supplier()
	{
		return NBTTagList::new;
	}

	@Override
	public BiConsumer<NBTTagList, NBTTagCompound> accumulator()
	{
		return NBTTagList::appendTag;
	}

	@Override
	public BinaryOperator<NBTTagList> combiner()
	{
		return (nbtBases, nbtBases2) -> {
			NBTTagList nbt = new NBTTagList();

			for(NBTBase n : nbtBases)
				nbt.appendTag(n);
			for(NBTBase n : nbtBases2)
				nbt.appendTag(n);

			return nbt;
		};
	}

	@Override
	public Function<NBTTagList, NBTTagList> finisher()
	{
		return nbtBases -> nbtBases;
	}

	@Override
	public Set<Characteristics> characteristics()
	{
		return Collections.emptySet();
	}
}
