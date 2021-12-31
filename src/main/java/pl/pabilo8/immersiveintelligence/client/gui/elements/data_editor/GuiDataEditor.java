package pl.pabilo8.immersiveintelligence.client.gui.elements.data_editor;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Pabilo8
 * @since 07.09.2021
 */
public abstract class GuiDataEditor<T extends IDataType> extends GuiButton
{
	public static LinkedHashMap<Class<? extends IDataType>, BiFunction<Integer, IDataType, GuiDataEditor<? extends IDataType>>> editors = new LinkedHashMap<>();

	static
	{
		editors.put(DataPacketTypeBoolean.class, (id, data) -> new GuiDataEditorBoolean(id, (DataPacketTypeBoolean)data));
		editors.put(DataPacketTypeString.class, (id, data) -> new GuiDataEditorString(id, (DataPacketTypeString)data));
		editors.put(DataPacketTypeInteger.class, (id, data) -> new GuiDataEditorInteger(id, (DataPacketTypeInteger)data));
		editors.put(DataPacketTypeNull.class, (id, data) -> new GuiDataEditorNull(id, (DataPacketTypeNull)data));
		editors.put(DataPacketTypeItemStack.class, (id, data) -> new GuiDataEditorItemStack(id, (DataPacketTypeItemStack)data));
		//editors.put(DataPacketTypeAccessor.class, (id, data) -> new GuiDataEditorAccessor(id, (DataPacketTypeAccessor)data));
	}

	protected T dataType;
	/**
	 * A list of all the buttons in this container.
	 */
	protected List<GuiButton> buttonList = Lists.newArrayList();

	public GuiDataEditor(int buttonId, T dataType)
	{
		super(buttonId, 0, 0, "");
		this.dataType = dataType;
	}

	public void setBounds(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		init();
	}

	public void init()
	{
		buttonList.clear();
	}

	public abstract T createType();

	public void update()
	{

	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		this.hovered = mouseX >= this.x&&mouseY >= this.y&&mouseX < this.x+this.width&&mouseY < this.y+this.height;
		buttonList.forEach(guiButton -> guiButton.drawButton(mc, mouseX, mouseY, partialTicks));
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		return false;
	}

	public void keyTyped(char typedChar, int keyCode) throws IOException
	{

	}

	public abstract T outputType();


	/**
	 * Adds a control to this GUI's button list. Any type that subclasses button may be added (particularly, GuiSlider,
	 * but not text fields).
	 *
	 * @param buttonIn The control to add
	 * @return The control passed in.
	 */
	protected <B extends GuiButton> B addButton(B buttonIn)
	{
		this.buttonList.add(buttonIn);
		return buttonIn;
	}

	public boolean isFocused()
	{
		return false;
	}
}
