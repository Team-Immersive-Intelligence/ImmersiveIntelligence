package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;

/**
 * @author Pabilo8
 * @since 07.09.2021
 */
public class GuiDataEditorNull extends GuiDataEditor<DataTypeNull>
{
	public GuiDataEditorNull(int buttonId, DataTypeNull dataType)
	{
		super(buttonId, dataType);
	}

	@Override
	public void init()
	{
		super.init();
	}

	@Override
	public DataTypeNull createType()
	{
		return new DataTypeNull();
	}

	@Override
	public DataTypeNull outputType()
	{
		return dataType;
	}
}
