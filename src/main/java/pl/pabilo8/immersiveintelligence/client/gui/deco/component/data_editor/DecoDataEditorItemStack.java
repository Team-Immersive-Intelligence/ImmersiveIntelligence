package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoIngredientStackPickerPanel;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorItemStack extends DecoDataEditor<DataTypeItemStack>
{
	private ItemStack scanned;

	public DecoDataEditorItemStack(int x, int y, DataTypeItemStack dataType)
	{
		super(x, y, dataType);
		this.scanned = dataType.value;
	}

	@Override
	protected boolean initialize()
	{
		addLabel(IIReference.DESCRIPTION_KEY+"variable_value", 2, 2);
		addComponent(new DecoIngredientStackPickerPanel(0, 2+12)
				.withOnStackChanged(ingredientStack -> scanned = ingredientStack.getExampleStack())
				.withSize(width, height)
		);
		return super.initialize();
	}

	@Override
	public DataTypeItemStack outputType()
	{
		dataType.value = scanned.copy();
		return dataType;
	}
}
