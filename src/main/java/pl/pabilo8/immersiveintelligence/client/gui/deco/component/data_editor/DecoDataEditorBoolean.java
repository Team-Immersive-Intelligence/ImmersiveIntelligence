package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoCheckbox;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorBoolean extends DecoDataEditor<DataTypeBoolean>
{
	private DecoCheckbox checkbox;

	public DecoDataEditorBoolean(int x, int y, DataTypeBoolean data)
	{
		super(x, y, data);
	}

	@Override
	protected boolean initialize()
	{
		//Add editor components
		addLabel(IIReference.DESCRIPTION_KEY+"variable_value", 2, 2);
		addComponents(
				checkbox = new DecoCheckbox(16, 48+16)
						.withText(IIReference.DESCRIPTION_KEY+"variable_value")
						.withChecked(dataType.value)
		);
		return super.initialize();
	}

	@Override
	public DataTypeBoolean outputType()
	{
		dataType.value = checkbox.isChecked();
		return dataType;
	}
}
