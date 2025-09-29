package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.highlight.POLHighlighter;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 24.09.2025
 */
public class DecoCodeEditor extends DecoDataEditor<DataType>
{
	public DecoCodeEditor(int x, int y, DataType dataType)
	{
		super(x, y, dataType);
	}

	@Override
	protected boolean initialize()
	{
		if(!super.initialize())
			return false;

		addComponent(new DecoTextField(0, 0)
				.withSize(width, height)
				.withMultiLine(true)
				.withHighlighter(new POLHighlighter())
				.withText(new String[]{
						";This is an editor for data variables",
						";POL keywords will be highlighted, but",
						";the machine does not allow code execution",
						"",
						"use arithmetic",
						dataType.toString()
				})
		);

		return true;
	}

	@Override
	public DataType outputType()
	{
		return null;
	}
}
