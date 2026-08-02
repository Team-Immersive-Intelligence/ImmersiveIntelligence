package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
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
						.withOnSelectedEntry((oldValue, newValue) -> {
							TextFilter previous = Optional.ofNullable(oldValue).orElse(TextFilter.DECIMAL);
							TextFilter selected = Optional.ofNullable(newValue).orElse(TextFilter.DECIMAL);
							int current = previous.parseInt(valueEdit.getText(), dataType.value);
							valueEdit.withFilter(selected).withText(selected.formatInt(current));
						})
						.withSelectedEntry(TextFilter.DECIMAL)
		);
		return super.initialize();
	}

	@Override
	public DataTypeInteger outputType()
	{
		TextFilter selected = Optional.ofNullable(valueDropdown.getSelectedEntry()).orElse(TextFilter.DECIMAL);
		dataType.value = selected.parseInt(valueEdit.getText(), dataType.value);
		return dataType;
	}
}
