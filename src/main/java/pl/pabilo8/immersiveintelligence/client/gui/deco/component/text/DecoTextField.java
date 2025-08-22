package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * A text field component that supports both single-line and multi-line input.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 12.07.2025
 */
public class DecoTextField extends GuiComponentDecoBase<DecoTextField>
{
	//Content state
	private final List<String> lines = new ArrayList<>();
	//Settings
	private boolean multiLine = false;
	private TextFilter filter = TextFilter.NONE;
	private Predicate<String> customFilter = (s) -> true;
	private IIFontRenderer fontRenderer = IIClientUtils.fontRegular;
	private int maxStringLength = 32767;
	private ResLoc backgroundLocation = DecoTextures.RES_TEXTURES_DECO_COMPONENT_TEXT_FIELD;
	private IIColor textColor = IIColor.WHITE;
	private IIColor cursorColor = IIReference.COLOR_IMMERSIVE_ORANGE;
	private IIColor selectionColor = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.35f);
	private int padding = 4;
	private int currentLine = 0, cursorPosition = 0, selectionEnd = 0;

	//Scroll state
	private int verticalScroll = 0, maxVerticalScroll = 0, lineScrollOffset = 0;

	//Render state
	private int cursorCounter = 0, blinkRate = 30;

	/**
	 * Creates a new text field
	 *
	 * @param x X position
	 * @param y Y position
	 */
	public DecoTextField(int x, int y)
	{
		super(x, y);
		lines.add("");

		withSize(36, (fontRenderer.FONT_HEIGHT+2)+1+4);
		withOnKeyTyped(this::onKeyTyped);
		withOnPressed(this::onMousePressed);
		withOnDragged(this::onMouseDragged);
		withOnScroll(this::onMouseScroll);
	}

	@Override
	protected boolean initialize()
	{
		calculateMaxScroll();
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		//Draw background if enabled
		if(backgroundLocation!=null)
		{
			bindAtlas();
			IIDrawUtils.startTexturedColored()
					.drawConnectedColorRect(x, y, width, height,
							IIColor.WHITE, backgroundLocation, 32, 32, 8, 8)
					.finish();
		}

		//Begin scissoring to prevent drawing outside bounds
		GlStateManager.pushMatrix();
		assert parentGui!=null;
		parentGui.scissorStart(x+padding, y+padding, width-(padding*2), height-(padding*2));

		//Calculate visible lines
		int visibleLines = multiLine?((height-(padding*2))/fontRenderer.FONT_HEIGHT): 1;
		int startLine = multiLine?verticalScroll: 0;
		int endLine = multiLine?Math.min(startLine+visibleLines, lines.size()): 1;

		//Draw text
		for(int lineIdx = startLine; lineIdx < endLine; lineIdx++)
		{
			String line = lines.get(lineIdx);
			if(lineScrollOffset > line.length())
				lineScrollOffset = Math.max(0, line.length()-1);

			String visibleText = fontRenderer.trimStringToWidth(
					line.substring(lineScrollOffset), width-(padding*2));

			int lineY = y+padding+((lineIdx-startLine)*fontRenderer.FONT_HEIGHT);

			//Draw the text
			fontRenderer.drawString(visibleText, x+padding, lineY, textColor.getPackedARGB());

			//Draw cursor and selection if this is the current line and we're focused
			if(isFocused()&&lineIdx==currentLine&&cursorCounter/blinkRate%2==0)
			{
				//Only handle cursor/selection if this is the active line
				if(cursorPosition >= 0&&cursorPosition <= line.length())
				{
					//Determine cursor X position
					int cursorX = x+padding;
					if(cursorPosition > 0)
						cursorX += fontRenderer.getStringWidth(
								line.substring(lineScrollOffset, Math.min(cursorPosition, line.length())));

					//Draw cursor
					fontRenderer.drawString("|", cursorX, lineY, textColor.getPackedARGB());
				}

				//Draw selection if needed
				if(selectionEnd!=cursorPosition)
				{
					int selStart = Math.min(cursorPosition, selectionEnd);
					int selEnd = Math.max(cursorPosition, selectionEnd);

					if(selStart >= lineScrollOffset)
					{
						int startX = x+padding+fontRenderer.getStringWidth(
								line.substring(lineScrollOffset, selStart));
						int endX = x+padding+fontRenderer.getStringWidth(
								line.substring(lineScrollOffset, Math.min(selEnd, line.length())));

						//TODO: 02.08.2025 fix
						IIDrawUtils.startColored()
								.drawColorRect(startX, lineY, endX-startX, fontRenderer.FONT_HEIGHT, selectionColor)
								.finish();
					}
				}
			}
		}

		//End scissoring
		parentGui.scissorEnd();
		GlStateManager.popMatrix();

		cursorCounter++;
	}

	@Override
	public void cleanup()
	{
		//No cleanup needed
	}

	@Override
	public void onGuiEvent(DecoGuiEvent event)
	{
		super.onGuiEvent(event);
		switch(event)
		{
			case COPY:
				GuiScreen.setClipboardString(getSelectedText());
				break;
			case PASTE:
				writeText(GuiScreen.getClipboardString());
				break;
			case CUT:
				GuiScreen.setClipboardString(getSelectedText());
				deleteSelectedText();
				break;
			case SELECT_ALL:
				setCursorPositionEnd();
				setSelectionPos(0);
				break;
			//TODO: 12.07.2025 undo/redo 
		}
	}

	/**
	 * Handle key input
	 */
	private boolean onKeyTyped(DecoTextField textField, char typedChar, int keyCode)
	{
		switch(keyCode)
		{
			//Enter adds a newline or defocuses (confirms) for single-line fields
			case Keyboard.KEY_RETURN:
				if(multiLine)
					insertNewLine();
				else
					parentGui.requestFocus(null);
				return true;

			//Text deletion
			case Keyboard.KEY_BACK:
				deleteFromCursor(-1);
				return true;
			case Keyboard.KEY_DELETE:
				deleteFromCursor(1);
				return true;

			//Cursor movement
			case Keyboard.KEY_HOME:
				if(GuiScreen.isShiftKeyDown())
					setSelectionPos(0);
				else
					setCursorPositionZero();
				return true;
			case Keyboard.KEY_END:
				if(GuiScreen.isShiftKeyDown())
					setSelectionPos(getCurrentLine().length());
				else
					setCursorPositionEnd();
				return true;
			case Keyboard.KEY_LEFT:
				moveCursorBy(-1);
				return true;
			case Keyboard.KEY_RIGHT:
				moveCursorBy(1);
				return true;
			case Keyboard.KEY_UP:
				if(multiLine&&currentLine > 0)
					moveToLine(currentLine-1);
				return true;
			case Keyboard.KEY_DOWN:
				if(multiLine&&currentLine < lines.size()-1)
					moveToLine(currentLine+1);
				return true;

			//Add text in case of any other character
			default:
				if(ChatAllowedCharacters.isAllowedCharacter(typedChar))
				{
					writeText(Character.toString(typedChar));
					return true;
				}
				return false;
		}
	}

	/**
	 * Handle mouse input
	 */
	private boolean onMousePressed(DecoTextField textField, MouseButton button, int mouseX, int mouseY)
	{
		if(!this.enabled||button!=MouseButton.LEFT)
			return false;

		//Calculate click position in text
		int clickX = mouseX-(x+padding);
		int clickY = mouseY-(y+padding);

		//Change current line
		if(multiLine)
			currentLine = MathHelper.clamp(verticalScroll+(clickY/fontRenderer.FONT_HEIGHT), 0, lines.size()-1);

		//Find cursor position in line
		String line = getCurrentLine();
		cursorPosition = MathHelper.clamp(fontRenderer.trimStringToWidth(line, clickX).length(), 0, line.length());
		cursorCounter = 0;

		//Set selection end to cursor position
		setSelectionPos(cursorPosition);
		return true;
	}

	/**
	 * Handle mouse drag (for selection)
	 */
	private boolean onMouseDragged(DecoTextField textField, MouseButton button, int mouseX, int mouseY)
	{
		if(!this.enabled||button!=MouseButton.LEFT) return false;

		//Calculate drag position in text
		int dragX = mouseX-(x+padding);
		int dragY = mouseY-(y+padding);

		if(multiLine)
		{
			int lineIndex = verticalScroll+(dragY/fontRenderer.FONT_HEIGHT);
			if(lineIndex >= 0&&lineIndex < lines.size())
			{
				String line = lines.get(lineIndex);
				int position = fontRenderer.trimStringToWidth(line, dragX).length();
				setSelectionPos(position);
				return true;
			}
		}
		else
		{
			//Single line
			String line = getCurrentLine();
			int position = fontRenderer.trimStringToWidth(line, dragX).length();
			setSelectionPos(position);
			return true;
		}

		return false;
	}

	/**
	 * Handle mouse scroll
	 */
	private boolean onMouseScroll(DecoTextField textField, int mouseScroll, int mouseX, int mouseY)
	{
		if(!multiLine||!IIMath.isPointInRectangle(x, y, x+width, y+height, mouseX, mouseY))
			return false;

		verticalScroll = MathHelper.clamp(verticalScroll-mouseScroll, 0, maxVerticalScroll);
		return true;
	}

	/**
	 * Write text at cursor position
	 */
	public void writeText(String text)
	{
		if(text.isEmpty()) return;

		//Filter text based on mode
		String filteredText = filterText(text);
		if(filteredText.isEmpty()) return;

		//Delete selected text first
		if(hasSelection())
			deleteSelectedText();

		//Get current line
		String currentLineText = getCurrentLine();

		//Check if adding this text would exceed max length
		if(currentLineText.length()+filteredText.length() > maxStringLength)
			filteredText = filteredText.substring(0, maxStringLength-currentLineText.length());

		//Insert text
		StringBuilder newLine = new StringBuilder(currentLineText);
		newLine.insert(cursorPosition, filteredText);
		setCurrentLine(newLine.toString());

		//Move cursor
		moveCursorBy(filteredText.length());
	}

	/**
	 * Filter text based on mode
	 */
	private String filterText(String input)
	{
		if(filter==TextFilter.NONE&&customFilter==null)
			return input;

		StringBuilder filtered = new StringBuilder();
		for(char c : input.toCharArray())
		{
			String charStr = String.valueOf(c);
			if((filter==TextFilter.NONE||filter.test(charStr))&&
					(customFilter==null||customFilter.test(charStr)))
				filtered.append(c);
		}

		return filtered.toString();
	}

	/**
	 * Delete text from cursor position
	 */
	public void deleteFromCursor(int offset)
	{
		if(hasSelection())
		{
			deleteSelectedText();
			return;
		}

		String currentLineText = getCurrentLine();

		//Backspace
		if(offset < 0)
			if(cursorPosition+offset < 0)
			{
				if(multiLine&&currentLine > 0)
				{
					//Join with previous line
					String previousLine = lines.get(currentLine-1);
					int previousLength = previousLine.length();

					lines.set(currentLine-1, previousLine+currentLineText);
					lines.remove(currentLine);

					currentLine--;
					cursorPosition = previousLength;
					selectionEnd = cursorPosition;
				}
			}
			else
			{
				//Delete character before cursor
				StringBuilder newLine = new StringBuilder(currentLineText);
				newLine.deleteCharAt(cursorPosition+offset);
				setCurrentLine(newLine.toString());

				moveCursorBy(offset);
			}
		else //Delete
			if(offset > 0)
				if(cursorPosition+offset > currentLineText.length())
				{
					if(multiLine&&currentLine < lines.size()-1)
					{
						//Join with next line
						String nextLine = lines.get(currentLine+1);

						lines.set(currentLine, currentLineText+nextLine);
						lines.remove(currentLine+1);
					}
				}
				else
				{
					//Delete character after cursor
					StringBuilder newLine = new StringBuilder(currentLineText);
					newLine.deleteCharAt(cursorPosition);
					setCurrentLine(newLine.toString());
				}

		calculateMaxScroll();
	}

	/**
	 * Delete selected text
	 */
	public void deleteSelectedText()
	{
		if(!hasSelection()) return;

		String currentLineText = getCurrentLine();

		int start = Math.min(cursorPosition, selectionEnd);
		int end = Math.max(cursorPosition, selectionEnd);

		if(start >= 0&&end <= currentLineText.length())
		{
			StringBuilder newLine = new StringBuilder(currentLineText);
			newLine.delete(start, end);
			setCurrentLine(newLine.toString());

			cursorPosition = start;
			selectionEnd = start;
		}
	}

	/**
	 * Insert a new line at cursor position
	 */
	private void insertNewLine()
	{
		if(!multiLine) return;

		String currentLineText = getCurrentLine();

		//Split the line at cursor position
		String beforeCursor = currentLineText.substring(0, cursorPosition);
		String afterCursor = currentLineText.substring(cursorPosition);

		//Update current line
		lines.set(currentLine, beforeCursor);

		//Insert new line
		currentLine++;
		lines.add(currentLine, afterCursor);

		//Reset cursor position
		cursorPosition = 0;
		selectionEnd = 0;

		calculateMaxScroll();
	}

	/**
	 * Move cursor by offset
	 */
	public void moveCursorBy(int offset)
	{
		setCursorPosition(cursorPosition+offset);
	}

	/**
	 * Set cursor position to beginning
	 */
	public void setCursorPositionZero()
	{
		setCursorPosition(0);
	}

	/**
	 * Set cursor position to end
	 */
	public void setCursorPositionEnd()
	{
		setCursorPosition(getCurrentLine().length());
	}

	/**
	 * Set selection position
	 */
	public void setSelectionPos(int position)
	{
		String currentLineText = getCurrentLine();
		selectionEnd = MathHelper.clamp(position, 0, currentLineText.length());
		ensureCursorVisible();
	}

	/**
	 * Move cursor to specific line
	 */
	private void moveToLine(int lineIndex)
	{
		if(!multiLine) return;

		if(lineIndex < 0||lineIndex >= lines.size()) return;

		currentLine = lineIndex;
		String newLine = getCurrentLine();

		//Keep horizontal position if possible
		cursorPosition = Math.min(cursorPosition, newLine.length());
		selectionEnd = cursorPosition;

		ensureCursorVisible();
	}

	/**
	 * Ensure cursor is visible by adjusting scroll
	 */
	private void ensureCursorVisible()
	{
		if(!multiLine)
		{
			//Horizontal scrolling for single-line
			int visibleWidth = width-(padding*2);
			String currentLineText = getCurrentLine();

			//Get width of text before cursor
			String textBeforeCursor = currentLineText.substring(0, cursorPosition);
			int cursorX = fontRenderer.getStringWidth(textBeforeCursor);

			//Adjust scroll if needed
			if(cursorX < lineScrollOffset)
				lineScrollOffset = cursorX;
			else if(cursorX > lineScrollOffset+visibleWidth)
				lineScrollOffset = cursorX-visibleWidth+1;
		}
		else
		{
			//Vertical scrolling for multi-line
			int visibleLines = (height-(padding*2))/fontRenderer.FONT_HEIGHT;

			//Adjust vertical scroll if needed
			if(currentLine < verticalScroll)
				verticalScroll = currentLine;
			else if(currentLine >= verticalScroll+visibleLines)
				verticalScroll = currentLine-visibleLines+1;
		}
	}

	/**
	 * Calculate maximum scroll values
	 */
	private void calculateMaxScroll()
	{
		if(multiLine)
		{
			int visibleLines = (height-(padding*2))/fontRenderer.FONT_HEIGHT;
			maxVerticalScroll = Math.max(0, lines.size()-visibleLines);
			verticalScroll = MathHelper.clamp(verticalScroll, 0, maxVerticalScroll);
		}
	}

	/**
	 * Get selected text
	 */
	public String getSelectedText()
	{
		if(!hasSelection()) return "";

		String currentLineText = getCurrentLine();
		int start = Math.min(cursorPosition, selectionEnd);
		int end = Math.max(cursorPosition, selectionEnd);

		return currentLineText.substring(start, end);
	}

	/**
	 * Check if there is selected text
	 */
	public boolean hasSelection()
	{
		return cursorPosition!=selectionEnd;
	}

	/**
	 * Get current line text
	 */
	private String getCurrentLine()
	{
		return lines.get(currentLine);
	}

	/**
	 * Set current line text
	 */
	private void setCurrentLine(String text)
	{
		lines.set(currentLine, text);
	}

	/**
	 * Get complete text
	 */
	public String getText()
	{
		if(!multiLine) return lines.get(0);

		StringBuilder text = new StringBuilder();
		for(int i = 0; i < lines.size(); i++)
		{
			text.append(lines.get(i));
			if(i < lines.size()-1)
				text.append("\n");
		}
		return text.toString();
	}

	public DecoTextField withText(Object object)
	{
		if(object==null)
			return this;

		if(object instanceof String)
			return withText((String)object);
		else if(object instanceof Integer)
			return withText(String.valueOf(object));
		else if(object instanceof Float)
			return withText(String.valueOf(object));
		else if(object instanceof Double)
			return withText(String.valueOf(object));
		else if(object instanceof Long)
			return withText(String.valueOf(object));
		else if(object instanceof Boolean)
			return withText(String.valueOf(object));
		else if(object instanceof Character)
			return withText(String.valueOf(object));
		else if(object instanceof Enum)
			return withText(String.valueOf(object));
		else if(object instanceof String[])
			return withText(String.join("\n", ((String[])object)));
		else
			return withText(object.toString());
	}

	private DecoTextField withText(String text)
	{
		if(text==null) text = "";

		if(!multiLine)
		{
			lines.clear();
			//Single-line mode, replace special characters
			lines.add(text.replace("\n", "").replace("\r", "").replace("\t", ""));
			cursorPosition = text.length();
			selectionEnd = cursorPosition;
		}
		else
		{
			lines.clear();
			Collections.addAll(lines, text.split("\n"));

			if(lines.isEmpty())
				lines.add("");

			cursorPosition = Math.min(cursorPosition, getCurrentLine().length());
			selectionEnd = cursorPosition;
			calculateMaxScroll();
		}
		return this;
	}

	public DecoTextField withMultiLine(boolean multiLine)
	{
		this.multiLine = multiLine;
		calculateMaxScroll();
		return this;
	}

	public DecoTextField withFilter(TextFilter filter)
	{
		this.filter = filter;
		return this;
	}

	public DecoTextField withCustomFilter(Predicate<String> filter)
	{
		this.customFilter = filter;
		return this;
	}

	public DecoTextField withMaxStringLength(int length)
	{
		this.maxStringLength = length;
		return this;
	}

	public DecoTextField withBackgroundLocation(@Nullable ResLoc backgroundLocation)
	{
		this.backgroundLocation = backgroundLocation;
		return this;
	}

	public DecoTextField withTextColor(IIColor color)
	{
		this.textColor = color;
		return this;
	}

	public DecoTextField withCursorColor(IIColor color)
	{
		this.cursorColor = color;
		return this;
	}

	public DecoTextField withSelectionColor(IIColor color)
	{
		this.selectionColor = color;
		return this;
	}

	public DecoTextField withPadding(int padding)
	{
		this.padding = padding;
		return this;
	}

	/**
	 * Get cursor position
	 */
	public int getCursorPosition()
	{
		return cursorPosition;
	}

	//--- Getters ---//

	/**
	 * Set cursor position
	 */
	public void setCursorPosition(int position)
	{
		if(!multiLine)
		{
			//Single-line mode
			String currentLineText = getCurrentLine();
			cursorPosition = MathHelper.clamp(position, 0, currentLineText.length());
			selectionEnd = cursorPosition;
			return;
		}

		//Multi-line mode
		String currentLineText = getCurrentLine();

		if(position < 0&&currentLine > 0)
		{
			//Move to end of previous line
			currentLine--;
			cursorPosition = getCurrentLine().length();
		}
		else //Move within current line
			if(position > currentLineText.length()&&currentLine < lines.size()-1)
			{
				//Move to start of next line
				currentLine++;
				cursorPosition = 0;
			}
			else
				cursorPosition = MathHelper.clamp(position, 0, currentLineText.length());

		selectionEnd = cursorPosition;
		ensureCursorVisible();
	}

	/**
	 * Get current line index
	 */
	public int getCurrentLineIndex()
	{
		return currentLine;
	}

	/**
	 * Get total line count
	 */
	public int getLineCount()
	{
		return lines.size();
	}

	@Override
	public void playPressSound(SoundHandler soundHandlerIn)
	{

	}

	//--- Sub-Classes ---//

	/**
	 * Defines base text filtering modes, the GUI author can also define custom string-based filters
	 */
	public enum TextFilter
	{
		NONE((s) -> true),
		ALPHANUMERIC((s) -> s.matches("[a-zA-Z0-9_]*")),
		LOWERCASE((s) -> s.matches("[a-z0-9_]*")),
		UPPERCASE((s) -> s.matches("[A-Z0-9_]*")),
		DECIMAL((s) -> s.matches("[0-9.-]*")),
		HEXADECIMAL((s) -> s.matches("[0-9A-Fa-f]*")),
		BINARY((s) -> s.matches("[01]*")),
		FLOAT((s) -> s.matches("[0-9.-]*")||s.matches("[0-9.-]*[eE][+-]?[0-9]+"));

		private final Predicate<String> filter;

		TextFilter(Predicate<String> filter)
		{
			this.filter = filter;
		}

		public boolean test(String input)
		{
			return filter.test(input);
		}
	}
}
