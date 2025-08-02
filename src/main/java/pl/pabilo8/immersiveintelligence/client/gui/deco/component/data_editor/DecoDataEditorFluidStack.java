package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFluidStack;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField.TextFilter;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorFluidStack extends DecoDataEditor<DataTypeFluidStack>
{
	private DecoTextField countEdit;
	private FluidStack fluidStack;

	public DecoDataEditorFluidStack(int x, int y, DataTypeFluidStack dataType)
	{
		super(x, y, dataType);
		this.fluidStack = dataType.value;
	}

	@Override
	protected boolean initialize()
	{
		//TODO: 10.07.2025 translations!
		addLabel(IIReference.DESCRIPTION_KEY+"variable_value", 2, 2);
		addLabel("Item:", 2, 12);
		addLabel("Count:", 2, 2+20+12);

		addComponents(
				countEdit = new DecoTextField(40, 2+20+12-4)
						.withSize(width-42, 16)
						.withFilter(TextFilter.DECIMAL)
						.withText(dataType.value==null?0: dataType.value.amount)
				/*new DecoItemStackDisplay((width/2)-8, 8)
						.withStack(scanned)*/
		);
		return super.initialize();
	}

	@Override
	public DataTypeFluidStack outputType()
	{
		//dataType.value = scanned.copy();
		//dataType.value.setCount(Integer.parseInt(countEdit.getText()));
		return dataType;
	}
}
