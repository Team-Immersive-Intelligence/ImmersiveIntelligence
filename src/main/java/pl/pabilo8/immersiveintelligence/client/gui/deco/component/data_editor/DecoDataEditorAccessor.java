package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoDropdownDataLetters;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.Optional;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorAccessor extends DecoDataEditor<DataTypeAccessor>
{
	private DecoDropdownDataLetters buttonVariable;

	public DecoDataEditorAccessor(int x, int y, DataTypeAccessor dataType)
	{
		super(x, y, dataType);
	}

	@Override
	protected boolean initialize()
	{
		addLabel(IIReference.DESCRIPTION_KEY+"variable", 2, 10);
		addComponent(this.buttonVariable = new DecoDropdownDataLetters(x+width-12-18-2, y+10+1-6));
		return super.initialize();
	}

	@Override
	public DataTypeAccessor outputType()
	{
		dataType.variable = Optional.ofNullable(this.buttonVariable.getSelectedEntry())
				.orElse(DataPacket.VARIABLE_NAMES[0]);
		return dataType;
	}

}
