package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoIngredientStackPickerPanel;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorItemStack extends DecoDataEditor<DataTypeItemStack>
{
	private DecoIngredientStackPickerPanel stackPicker;

	public DecoDataEditorItemStack(int x, int y, DataTypeItemStack dataType)
	{
		super(x, y, dataType);
	}

	@Override
	protected boolean initialize()
	{
		addLabel(IIReference.DESCRIPTION_KEY+"variable_value", 2, 2);
		addComponent((stackPicker = new DecoIngredientStackPickerPanel(0, 2+12))
				.withItemDataTypeMode(true)
				.withDataType(dataType)
				.withSize(width, height-6)
		);
		return super.initialize();
	}

	@Override
	public DataTypeItemStack outputType()
	{
		return stackPicker.getItemStackDataType(dataType);
	}
}
