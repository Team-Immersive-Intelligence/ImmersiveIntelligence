package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField.TextFilter;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.Optional;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorInteger extends DecoDataEditor<DataTypeInteger>
{
	private DecoTextField valueEdit;
	private DecoDropdown<TextFilter> valueDropdown;

	public DecoDataEditorInteger(int x, int y, DataTypeInteger dataType)
	{
		super(x, y, dataType);
	}

	@Override
	protected boolean initialize()
	{
		addLabel(IIReference.DESCRIPTION_KEY+"variable_value", 2, 2);
		addComponents(
				//Text field
				valueEdit = new DecoTextField(2, 12)
						.withSize(width-8, 16)
						.withFilter(TextFilter.DECIMAL)
						.withText(dataType.value),
				//Mode select dropdown
				valueDropdown = new DecoDropdown<TextFilter>(2, 32+2)
						.withEntries(TextFilter.BINARY, TextFilter.DECIMAL, TextFilter.HEXADECIMAL)
						.withOnSelectedEntry((oldValue, newValue) ->
								valueEdit.withFilter(newValue))
						.withSelectedEntry(TextFilter.DECIMAL)
		);
		return super.initialize();
	}

	@Override
	public DataTypeInteger outputType()
	{
		switch(Optional.ofNullable(valueDropdown.getSelectedEntry()).orElse(TextFilter.DECIMAL))
		{
			case DECIMAL:
				dataType.value = Integer.parseInt(valueEdit.getText(), 10);
				break;
			case HEXADECIMAL:
				dataType.value = Integer.parseInt(valueEdit.getText(), 16);
				break;
			case BINARY:
				dataType.value = Integer.parseInt(valueEdit.getText(), 2);
				break;
		}
		return dataType;
	}
}
