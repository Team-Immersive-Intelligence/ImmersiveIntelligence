package pl.pabilo8.immersiveintelligence.client.gui.elements.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeNull;

/**
 * @author Pabilo8
 * @since 07.09.2021
 */
public class GuiDataEditorNull extends GuiDataEditor<DataPacketTypeNull>
{
	public GuiDataEditorNull(int buttonId, DataPacketTypeNull dataType)
	{
		super(buttonId, dataType);
	}

	@Override
	public void init()
	{
		super.init();
	}

	@Override
	public DataPacketTypeNull createType()
	{
		return new DataPacketTypeNull();
	}

	@Override
	public DataPacketTypeNull outputType()
	{
		return dataType;
	}
}
