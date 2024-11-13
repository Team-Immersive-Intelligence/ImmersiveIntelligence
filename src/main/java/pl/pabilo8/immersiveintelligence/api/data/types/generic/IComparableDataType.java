package pl.pabilo8.immersiveintelligence.api.data.types.generic;

public interface IComparableDataType<T extends DataType & IComparableDataType<T>>
{
	/**
	 * @param other data type to compare to
	 * @return true if this data type can be compared with the other
	 */
	boolean canCompareWith(DataType other);

	int compareTo(T other);

	/**
	 * @param other data type to compare to
	 * @return true if this data type is less than or equal to the other
	 */
	default boolean lessOrEqual(T other)
	{
		return compareTo(other) <= 0;
	}

	/**
	 * @param other data type to compare to
	 * @return true if this data type is less than the other
	 */
	default boolean lessThan(T other)
	{
		return compareTo(other) < 0;
	}

	/**
	 * @param other data type to compare to
	 * @return true if this data type is greater than or equal to the other
	 */
	default boolean greaterOrEqual(T other)
	{
		return compareTo(other) >= 0;
	}

	/**
	 * @param other data type to compare to
	 * @return true if this data type is greater than the other
	 */
	default boolean greaterThan(T other)
	{
		return compareTo(other) > 0;
	}
}
