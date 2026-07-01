package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeVector;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorVector extends DecoDataEditor<DataTypeVector>
{
	private DecoTextField x, y, z;

	public DecoDataEditorVector(int x, int y, DataTypeVector dataType)
	{
		super(x, y, dataType);
	}

	@Override
	protected boolean initialize()
	{
		addLabel(IIReference.DESCRIPTION_KEY+"variable_value", 2, 2);
		addComponents(
				this.x = new DecoTextField(2, 12)
						.withSize(width-8, 16)
						.withText(dataType.x),
				this.y = new DecoTextField(2, 12+18)
						.withSize(width-8, 16)
						.withText(dataType.y),
				this.z = new DecoTextField(2, 12+18+18)
						.withSize(width-8, 16)
						.withText(dataType.z),

				new DecoSwitch(2, 12+18+18+18)
						.withText(IIReference.DESCRIPTION_KEY+"variable_value.integer_vector")
						.withCurrentState(dataType.integerVector)
						.withOnToggle(newValue ->
						{
							dataType.integerVector = newValue;
							refreshFieldFormatting(x);
							refreshFieldFormatting(y);
							refreshFieldFormatting(z);
						})
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"variable_value.integer_vector.tooltip")
		);
		refreshFieldFormatting(x);
		refreshFieldFormatting(y);
		refreshFieldFormatting(z);
		return super.initialize();
	}

	private void refreshFieldFormatting(DecoTextField field)
	{
		float currentValue = parseFloatValue(field.getText());
		field.withFilter(dataType.integerVector?TextFilter.DECIMAL: TextFilter.FLOAT)
				.withText(dataType.integerVector?Integer.toString((int)currentValue): Float.toString(currentValue));
	}

	private float parseFloatValue(String text)
	{
		try
		{
			return Float.parseFloat(text);
		} catch(NumberFormatException e)
		{
			return 0;
		}
	}

	@Override
	public DataTypeVector outputType()
	{
		dataType.x = parseFloatValue(x.getText());
		dataType.y = parseFloatValue(y.getText());
		dataType.z = parseFloatValue(z.getText());
		return dataType;
	}
}
