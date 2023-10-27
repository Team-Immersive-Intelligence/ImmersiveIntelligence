package pl.pabilo8.immersiveintelligence.common.util.lambda;

import java.util.ArrayList;
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
public class ArraylistJoinCollector<T> implements Collector<ArrayList<T>, ArrayList<T>, ArrayList<T>>
{
	@Override
	public Supplier<ArrayList<T>> supplier()
	{
		return ArrayList::new;
	}

	@Override
	public BiConsumer<ArrayList<T>, ArrayList<T>> accumulator()
	{
		return ArrayList::addAll;
	}

	@Override
	public BinaryOperator<ArrayList<T>> combiner()
	{
		return (l1, l2) -> {
			l1.addAll(l2);
			return l1;
		};
	}

	@Override
	public Function<ArrayList<T>, ArrayList<T>> finisher()
	{
		return ts -> ts;
	}

	@Override
	public Set<Characteristics> characteristics()
	{
		return Collections.emptySet();
	}
}
