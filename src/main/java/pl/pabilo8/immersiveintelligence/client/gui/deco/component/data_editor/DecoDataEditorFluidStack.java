package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFluidStack;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoIngredientStackPickerPanel;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorFluidStack extends DecoDataEditor<DataTypeFluidStack>
{
	private FluidStack fluidStack;

	public DecoDataEditorFluidStack(int x, int y, DataTypeFluidStack dataType)
	{
		super(x, y, dataType);
		this.fluidStack = dataType.value==null?null: dataType.value.copy();
	}

	@Override
	protected boolean initialize()
	{
		addLabel(IIReference.DESCRIPTION_KEY+"variable_value", 2, 2);
		addComponent(new DecoIngredientStackPickerPanel(0, 2+12)
				.withFluidMode(true)
				.withOnStackChanged(ingredientStack -> fluidStack = ingredientStack.fluid)
				.withSize(width, height)
		);
		return super.initialize();
	}

	@Override
	public DataTypeFluidStack outputType()
	{
		dataType.value = fluidStack;
		return dataType;
	}
}
