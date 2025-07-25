package pl.pabilo8.immersiveintelligence.client.gui.deco.util;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A utility class that listens for changes in a value provided by a supplier.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 30.06.2025
 */
public class ValueListener<T>
{
	private final ArrayList<Consumer<T>> observers = new ArrayList<>();
	private final Supplier<T> supplier;
	private T cachedValue;
	private boolean dirty = false;

	public ValueListener(Supplier<T> supplier)
	{
		this.supplier = supplier;
		this.cachedValue = supplier.get();
	}

	/**
	 * Registers an observer that will be called when the value changes.
	 */
	public void addObserver(Consumer<T> listener)
	{
		if(!observers.contains(listener))
			observers.add(listener);
	}

	/**
	 * Registers an observer that will be called when the value changes.
	 *
	 * @return this for method chaining
	 */
	public ValueListener<T> withObserver(Consumer<T> listener)
	{
		addObserver(listener);
		return this;
	}

	/**
	 * Updates the cached value by calling the supplier. Calls observers if the value has changed.
	 */
	public boolean update()
	{
		T newValue = supplier.get();
		if(!newValue.equals(cachedValue))
		{
			cachedValue = newValue;
			dirty = true;
			observers.forEach(l -> l.accept(cachedValue));
		}
		else
		{
			dirty = false;
		}

		return dirty;
	}

	/**
	 * Checks if the value has changed since the last update.
	 */
	public T getValue()
	{
		return cachedValue;
	}
}
