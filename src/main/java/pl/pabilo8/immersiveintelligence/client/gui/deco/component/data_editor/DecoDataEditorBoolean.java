package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorBoolean extends DecoDataEditor<DataTypeBoolean>
{
	private DecoSwitch component;

	public DecoDataEditorBoolean(int x, int y, DataTypeBoolean data)
	{
		super(x, y, data);
	}

	@Override
	protected boolean initialize()
	{
		//Add editor components
		component = addComponent(new DecoSwitch(2, 6)
				.withText(IIReference.DESCRIPTION_KEY+"variable_value")
				.withCurrentState(dataType.value)
		);
		return super.initialize();
	}

	@Override
	public DataTypeBoolean outputType()
	{
		dataType.value = component.getState();
		return dataType;
	}
}
