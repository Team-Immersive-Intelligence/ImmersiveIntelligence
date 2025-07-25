package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public abstract class DecoDataEditor<T extends DataType> extends DecoPanel
{
	private static final LinkedHashMap<Class<? extends DataType>, DataEditorConstructor<?>>
			EDITORS = new LinkedHashMap<>();
	protected T dataType;

	static
	{
		registerEditor(DataTypeBoolean.class, DecoDataEditorBoolean::new);
		registerEditor(DataTypeString.class, DecoDataEditorString::new);
		registerEditor(DataTypeInteger.class, DecoDataEditorInteger::new);
		registerEditor(DataTypeFloat.class, DecoDataEditorFloat::new);
		registerEditor(DataTypeNull.class, DecoDataEditorNull::new);
		registerEditor(DataTypeItemStack.class, DecoDataEditorItemStack::new);
		//registerEditor(DataPacketTypeAccessor.class, GuiDataEditorAccessor::new);
	}


	public DecoDataEditor(int x, int y, T dataType)
	{
		super(x, y);
		withBackground(null);
		withBackgroundMask(null);
		withDataType(dataType);
	}

	public DecoDataEditor<T> withDataType(T dataType)
	{
		this.dataType = dataType;
		return this;
	}

	public abstract T outputType();

	/**
	 * Standard constructor interface for DecoDataEditor.
	 *
	 * @param <T> The type of DataType that the editor will handle.
	 */
	@FunctionalInterface
	private interface DataEditorConstructor<T extends DataType>
	{
		DecoDataEditor<T> construct(int x, int y, T dataType);
	}

	/**
	 * Registers a new editor for a specific DataType.
	 *
	 * @param type        The DataType class for which to register the editor.
	 * @param constructor Standard constructor for the editor
	 * @param <T>         The type of data the registered editor will handle
	 */
	private static <T extends DataType> void registerEditor(Class<T> type, DataEditorConstructor<T> constructor)
	{
		EDITORS.put(type, constructor);
	}

	/**
	 * @param type The DataType for which to get the editor.
	 * @param x    x position of the editor
	 * @param y    y position of the editor
	 * @return A new instance of a GuiDataEditor for the given DataType, or null if no editor is available for that type.
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public static <T extends DataType> DecoDataEditor<T> getEditorFor(T type, int x, int y)
	{
		DataEditorConstructor<T> constructor = (DataEditorConstructor<T>)EDITORS.get(type.getClass());
		return constructor==null?null: constructor.construct(x, y, type);
	}
}
