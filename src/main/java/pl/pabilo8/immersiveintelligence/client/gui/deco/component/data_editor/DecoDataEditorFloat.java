package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorFloat extends DecoDataEditor<DataTypeFloat>
{
	private DecoTextField valueEdit;

	public DecoDataEditorFloat(int x, int y, DataTypeFloat dataType)
	{
		super(x, y, dataType);
	}

	@Override
	protected boolean initialize()
	{
		addLabel(IIReference.DESCRIPTION_KEY+"variable_value", 2, 2);
		addComponent(this.valueEdit = new DecoTextField(2, 12)
				.withSize(width-8, 16)
				.withText(dataType.toString())
				.withFilter(TextFilter.FLOAT)
		);
		return super.initialize();
	}

	@Override
	public DataTypeFloat outputType()
	{
		dataType.value = Float.parseFloat(valueEdit.getText());
		return dataType;
	}
}
