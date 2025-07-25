package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorNull extends DecoDataEditor<DataTypeNull>
{
	public DecoDataEditorNull(int x, int y, DataTypeNull dataType)
	{
		super(x, y, dataType);
	}

	@Override
	public DataTypeNull outputType()
	{
		return dataType;
	}
}
