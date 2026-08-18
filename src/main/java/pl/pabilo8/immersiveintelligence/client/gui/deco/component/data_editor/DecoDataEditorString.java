package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextArea;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorString extends DecoDataEditor<DataTypeString>
{
	private DecoTextArea valueEdit;

	public DecoDataEditorString(int x, int y, DataTypeString dataType)
	{
		super(x, y, dataType);
	}

	@Override
	protected boolean initialize()
	{
		addLabel(IIReference.DESCRIPTION_KEY+"variable_value", 2, 2);
		addComponent(this.valueEdit = new DecoTextArea(2, 12)
				.withSize(width-8, height-20)
				.withText(dataType.toString())
		);
		return super.initialize();
	}

	@Override
	public DataTypeString outputType()
	{
		dataType.value = this.valueEdit.getText();
		return dataType;
	}
}
