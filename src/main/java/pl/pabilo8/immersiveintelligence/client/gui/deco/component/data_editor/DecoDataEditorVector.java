package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeVector;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField.TextFilter;
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
						.withText(dataType.x)
						.withFilter(TextFilter.FLOAT),
				this.y = new DecoTextField(2, 12+18)
						.withSize(width-8, 16)
						.withText(dataType.y)
						.withFilter(TextFilter.FLOAT),
				this.z = new DecoTextField(2, 12+18+18)
						.withSize(width-8, 16)
						.withText(dataType.z)
						.withFilter(TextFilter.FLOAT),

				new DecoSwitch(2, 12+18+18+18)
						.withText("Integer Vector")
						.withCurrentState(dataType.integerVector)
						.withOnToggle(newValue ->
						{
							dataType.integerVector = newValue;
							x.withFilter(newValue?TextFilter.DECIMAL: TextFilter.FLOAT);
							y.withFilter(newValue?TextFilter.DECIMAL: TextFilter.FLOAT);
							z.withFilter(newValue?TextFilter.DECIMAL: TextFilter.FLOAT);
						})
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"variable_value.integer_vector")
		);
		return super.initialize();
	}

	@Override
	public DataTypeVector outputType()
	{
		dataType.x = Float.parseFloat(x.getText());
		dataType.y = Float.parseFloat(y.getText());
		dataType.z = Float.parseFloat(z.getText());
		return dataType;
	}
}
