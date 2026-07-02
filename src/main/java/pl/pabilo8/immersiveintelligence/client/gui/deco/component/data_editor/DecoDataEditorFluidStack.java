package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFluidStack;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoIngredientStackPickerPanel;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorFluidStack extends DecoDataEditor<DataTypeFluidStack>
{
	private DecoIngredientStackPickerPanel stackPicker;

	public DecoDataEditorFluidStack(int x, int y, DataTypeFluidStack dataType)
	{
		super(x, y, dataType);
	}

	@Override
	protected boolean initialize()
	{
		addLabel(IIReference.DESCRIPTION_KEY+"variable_value", 2, 2);
		addComponent((stackPicker = new DecoIngredientStackPickerPanel(0, 2+12))
				.withFluidMode(true)
				.withDataType(dataType)
				.withSize(width, height)
		);
		return super.initialize();
	}

	@Override
	public DataTypeFluidStack outputType()
	{
		return stackPicker.getFluidStackDataType(dataType);
	}
}
