package pl.pabilo8.immersiveintelligence.client.gui;

import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.09.2021
 */
public interface IDataMachineGui
{
	void editVariable(char name, DataType initialValue);

	default void editVariable(DataVariable variable)
	{
		editVariable(variable.getName(), variable.getValue());
	}
}
