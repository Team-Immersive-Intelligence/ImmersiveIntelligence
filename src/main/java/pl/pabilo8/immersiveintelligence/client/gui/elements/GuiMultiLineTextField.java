package pl.pabilo8.immersiveintelligence.client.gui.elements;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is a direct copy of a gist made by Draco18s
 * @see <a href=https://gist.github.com/Draco18s/2b02762b597e67a9b887aed241f25077#file-guimultilinetextfield">https://gist.github.com/Draco18s/2b02762b597e67a9b887aed241f25077#file-guimultilinetextfield</a>
 * <p>
 * Huge thanks, that made me not copy-paste vanilla code and spend hours tweaking it ^^
 * If you want to copy it from here, feel free to
 * Only changes to the original are the text offset fix to be in top left corner (see line 698) and this JavaDoc
 *
 * @author Draco18s
 * @since 15.07.2021
 */
public class GuiMultiLineTextField extends GuiTextField
{

	private final int id;
	private final FontRenderer fontRenderer;
	public int x;
	public int y;
	/** The width of this text field. */
	public int width;
	public int height;
	/** Has the current text being edited on the textbox. */
	private final ArrayList<String> text = new ArrayList<>();
	private int currentLine = 0;
	private int verticalScrollOffset = 0;
	private int maxStringLength = 32;
	private int cursorCounter;
	private boolean enableBackgroundDrawing = true;
	/** if true the textbox can lose focus by clicking elsewhere on the screen */
	private boolean canLoseFocus = true;
	/** If this value is true along with isEnabled, keyTyped will process the keys. */
	private boolean isFocused;
	/** If this value is true along with isFocused, keyTyped will process the keys. */
	private boolean isEnabled = true;
	/** The current character index that should be used as start of the rendered text. */
	private int lineScrollOffset;
	private int cursorPosition;
	/** other selection position, maybe the same as the cursor */
	private int selectionEnd;
	private int enabledColor = 14737632;
	private int disabledColor = 7368816;
	/** True if this textbox is visible */
	private boolean visible = true;
	private GuiPageButtonList.GuiResponder guiResponder;
	/** Called to check if the text is valid */
	private Predicate<String> validator = Predicates.alwaysTrue();

	public GuiMultiLineTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height)
	{
		super(componentId, fontrendererObj, x, y, par5Width, par6Height);
		this.id = componentId;
		this.fontRenderer = fontrendererObj;
		this.x = x;
		this.y = y;
		this.width = par5Width;
		this.height = Math.max(par6Height, fontRenderer.FONT_HEIGHT * 2);
		text.add("");
	}

	/**
	 * Sets the GuiResponder associated with this text box.
	 */
	@Override
	public void setGuiResponder(GuiPageButtonList.GuiResponder guiResponderIn)
	{
		this.guiResponder = guiResponderIn;
	}

	/**
	 * Increments the cursor counter
	 */
	@Override
	public void updateCursorCounter()
	{
		++this.cursorCounter;
	}

	/**
	 * Sets the text of the textbox, and moves the cursor to the end.
	 */
	@Override
	public void setText(String textIn)
	{
		if (this.validator.apply(textIn))
		{
			String[] lines = textIn.split("\n");
			text.clear();
			text.addAll(Arrays.asList(lines));
			this.setCursorPositionEnd();
		}
	}

	/**
	 * Returns the contents of the textbox
	 */
	@Override
	public String getText()
	{
		StringBuilder str = new StringBuilder();
		for(String s : text)
			str.append(s).append(text.indexOf(s) < text.size()-1?"\n": "");
		return str.toString();
	}

	/**
	 * returns the text between the cursor and selectionEnd
	 */
	@Override
	public String getSelectedText()
	{
		int i = Math.min(this.cursorPosition, this.selectionEnd);
		int j = Math.max(this.cursorPosition, this.selectionEnd);
		return this.text.get(currentLine).substring(i, j);
	}

	@Override
	public void setValidator(Predicate<String> theValidator)
	{
		this.validator = theValidator;
	}

	/**
	 * Adds the given text after the cursor, or replaces the currently selected text if there is a selection.
	 */
	@Override
	public void writeText(String textToWrite)
	{
		String s = "";
		String s1 = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
		int i = Math.min(this.cursorPosition, this.selectionEnd);
		int j = Math.max(this.cursorPosition, this.selectionEnd);
		int k = this.maxStringLength - this.text.get(currentLine).length() - (i - j);

		if (!this.text.isEmpty())
			s = s+this.text.get(currentLine).substring(0, i);

		int l;

		if (k < s1.length())
		{
			s = s + s1.substring(0, k);
			l = k;
		}
		else
		{
			s = s + s1;
			l = s1.length();
		}

		if (!this.text.isEmpty() && j < this.text.get(currentLine).length())
			s = s+this.text.get(currentLine).substring(j);

		if (this.validator.apply(s))
		{
			this.text.set(currentLine, s);
			this.moveCursorBy(i - this.selectionEnd + l);
			this.setResponderEntryValue(this.id, this.text.get(currentLine));
		}
	}

	/**
	 * Notifies this text box's {@linkplain GuiPageButtonList.GuiResponder responder} that the text has changed.
	 */
	@Override
	public void setResponderEntryValue(int idIn, String textIn)
	{
		if (this.guiResponder != null)
			this.guiResponder.setEntryValue(idIn, textIn);
	}

	/**
	 * Deletes the given number of words from the current cursor's position, unless there is currently a selection, in
	 * which case the selection is deleted instead.
	 */
	@Override
	public void deleteWords(int num)
	{
		if (!this.text.isEmpty())
			if(this.selectionEnd!=this.cursorPosition)
				this.writeText("");
			else
				this.deleteFromCursor(this.getNthWordFromCursor(num)-this.cursorPosition);
	}

	/**
	 * Deletes the given number of characters from the current cursor's position, unless there is currently a selection,
	 * in which case the selection is deleted instead.
	 */
	@Override
	public void deleteFromCursor(int num)
	{
		if (!this.text.isEmpty())
			if(this.selectionEnd!=this.cursorPosition)
				this.writeText("");
			else
			{
				boolean flag = num < 0;
				int i = flag?this.cursorPosition+num: this.cursorPosition;
				int j = flag?this.cursorPosition: this.cursorPosition+num;
				String s = "";

				if(i >= 0)
					if(num > 0&&this.cursorPosition==this.text.get(currentLine).length()&&this.text.size() > currentLine+1)
					{
						s = this.text.get(currentLine)+this.text.get(currentLine+1);
						this.text.remove(currentLine+1);
					}
					else s = this.text.get(currentLine).substring(0, i);

				if(j < this.text.get(currentLine).length())
					if(num < 0&&this.cursorPosition==0&&currentLine > 0)
					{
						String curline = this.text.get(currentLine);
						this.text.remove(currentLine);
						currentLine--;
						this.setCursorPosition(this.text.get(currentLine).length()+1);
						flag = true;
						num = 0;
						s = this.text.get(currentLine)+curline;
					}
					else s = s+this.text.get(currentLine).substring(j);
				else if(j==0&&this.text.size() > 1&&currentLine > 0)
				{
					text.remove(currentLine);
					currentLine--;
					this.setCursorPositionEnd();
					return;
				}

				if(this.validator.apply(s))
				{
					this.text.set(currentLine, s);

					if(flag)
						this.moveCursorBy(num);
					this.setResponderEntryValue(this.id, this.text.get(currentLine));
				}
			}
	}

	@Override
	public int getId()
	{
		return this.id;
	}

	/**
	 * Gets the starting index of the word at the specified number of words away from the cursor position.
	 */
	@Override
	public int getNthWordFromCursor(int numWords)
	{
		return this.getNthWordFromPos(numWords, this.getCursorPosition());
	}

	/**
	 * Gets the starting index of the word at a distance of the specified number of words away from the given position.
	 */
	@Override
	public int getNthWordFromPos(int n, int pos)
	{
		return this.getNthWordFromPosWS(n, pos, true);
	}

	/**
	 * Like getNthWordFromPos (which wraps this), but adds option for skipping consecutive spaces
	 */
	@Override
	public int getNthWordFromPosWS(int n, int pos, boolean skipWs)
	{
		int i = pos;
		boolean flag = n < 0;
		int j = Math.abs(n);

		for (int k = 0; k < j; ++k)
			if(!flag)
			{
				int l = this.text.get(currentLine).length();
				i = this.text.get(currentLine).indexOf(32, i);

				if(i==-1)
					i = l;
				else
					while(skipWs&&i < l&&this.text.get(currentLine).charAt(i)==' ')
						++i;
			}
			else
			{
				while(skipWs&&i > 0&&this.text.get(currentLine).charAt(i-1)==' ')
					--i;

				while(i > 0&&this.text.get(currentLine).charAt(i-1)!=' ')
					--i;
			}

		return i;
	}

	/**
	 * Moves the text cursor by a specified number of characters and clears the selection
	 */
	@Override
	public void moveCursorBy(int num)
	{
		this.setCursorPosition(this.selectionEnd + num);
	}

	/**
	 * Sets the current position of the cursor.
	 */
	@Override
	public void setCursorPosition(int pos)
	{
		this.cursorPosition = pos;
		int i = this.text.get(currentLine).length();
		if(this.cursorPosition > i && currentLine + 1 < text.size()) {
			currentLine++;
			this.cursorPosition =  0;
			i = this.text.get(currentLine).length();
			if (this.lineScrollOffset > i) this.lineScrollOffset = i;
		}
		if(this.cursorPosition < 0 && currentLine > 0) {
			currentLine--;
			this.cursorPosition = i = this.text.get(currentLine).length();
			if (this.lineScrollOffset > i) this.lineScrollOffset = i;
		}
		this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i);
		this.setSelectionPos(this.cursorPosition);
	}

	/**
	 * Moves the cursor to the very start of this text box.
	 */
	@Override
	public void setCursorPositionZero()
	{
		this.setCursorPosition(0);
	}

	/**
	 * Moves the cursor to the very end of this text box.
	 */
	@Override
	public void setCursorPositionEnd()
	{
		this.setCursorPosition(this.text.get(currentLine).length());
	}

	/**
	 * Call this method from your GuiScreen to process the keys into the textbox
	 */
	@Override
	public boolean textboxKeyTyped(char typedChar, int keyCode)
	{
		if (!this.isFocused)
			return false;
		else if (GuiScreen.isKeyComboCtrlA(keyCode))
		{
			this.setCursorPositionEnd();
			this.setSelectionPos(0);
			return true;
		}
		else if (GuiScreen.isKeyComboCtrlC(keyCode))
		{
			GuiScreen.setClipboardString(this.getSelectedText());
			return true;
		}
		else if (GuiScreen.isKeyComboCtrlV(keyCode))
		{
			if (this.isEnabled)
			{
				String[] lines = GuiScreen.getClipboardString().split("\n");
				this.writeText(lines[0]);
				for(int l = 1; l < lines.length; l++) {
					currentLine++;
					text.add(currentLine, lines[l]);
				}
			}

			return true;
		}
		else if (GuiScreen.isKeyComboCtrlX(keyCode))
		{
			GuiScreen.setClipboardString(this.getSelectedText());

			if (this.isEnabled)
				this.writeText("");

			return true;
		}
		else
			switch(keyCode)
			{
				case 28:
					int lin = text.get(currentLine).length();
					if(this.cursorPosition==lin)
					{
						currentLine++;
						text.add(currentLine, "");
						setCursorPosition(0);
					}
					else
					{
						String rem = text.get(currentLine).substring(cursorPosition);
						text.set(currentLine, text.get(currentLine).substring(0, cursorPosition));
						currentLine++;
						this.text.add(currentLine, rem);
						this.setCursorPositionZero();
					}
					return true;
				case 200:
					currentLine = Math.max(currentLine-1, 0);
					if(this.cursorPosition > text.get(currentLine).length())
						this.selectionEnd = this.cursorPosition = text.get(currentLine).length();
					else this.moveCursorBy(0);
					return true;
				case 208:
					currentLine = Math.min(currentLine+1, text.size()-1);
					if(this.cursorPosition > text.get(currentLine).length())
						this.selectionEnd = this.cursorPosition = text.get(currentLine).length();
					else this.moveCursorBy(0);
					return true;
				case 14:

					if(GuiScreen.isCtrlKeyDown())
					{
						if(this.isEnabled)
							this.deleteWords(-1);
					}
					else if(this.isEnabled)
						this.deleteFromCursor(-1);

					return true;
				case 199:

					if(GuiScreen.isShiftKeyDown())
						this.setSelectionPos(0);
					else
						this.setCursorPositionZero();

					return true;
				case 203:

					if(GuiScreen.isShiftKeyDown())
						if(GuiScreen.isCtrlKeyDown())
							this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
						else
							this.setSelectionPos(this.getSelectionEnd()-1);
					else if(GuiScreen.isCtrlKeyDown())
						this.setCursorPosition(this.getNthWordFromCursor(-1));
					else
						this.moveCursorBy(-1);

					return true;
				case 205:

					if(GuiScreen.isShiftKeyDown())
						if(GuiScreen.isCtrlKeyDown())
							this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
						else
							this.setSelectionPos(this.getSelectionEnd()+1);
					else if(GuiScreen.isCtrlKeyDown())
						this.setCursorPosition(this.getNthWordFromCursor(1));
					else
						this.moveCursorBy(1);

					return true;
				case 207:

					if(GuiScreen.isShiftKeyDown())
						this.setSelectionPos(this.text.get(currentLine).length());
					else
						this.setCursorPositionEnd();

					return true;
				case 211:

					if(GuiScreen.isCtrlKeyDown())
					{
						if(this.isEnabled)
							this.deleteWords(1);
					}
					else if(this.isEnabled)
						this.deleteFromCursor(1);

					return true;
				default:

					if(ChatAllowedCharacters.isAllowedCharacter(typedChar))
					{
						if(this.isEnabled)
							this.writeText(Character.toString(typedChar));

						return true;
					}
					else
						return false;
			}
	}

	/**
	 * Called when mouse is clicked, regardless as to whether it is over this button or not.
	 */
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton)
	{
		boolean flag = mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;

		if (this.canLoseFocus)
			this.setFocused(flag);

		if (this.isFocused && flag && mouseButton == 0)
		{
			int i = mouseX - this.x;

			if (this.enableBackgroundDrawing)
				i -= 4;

			int j = mouseY - this.y;
			j /= fontRenderer.FONT_HEIGHT;

			currentLine = MathHelper.clamp(j + this.verticalScrollOffset,0,text.size()-1);

			String s = this.fontRenderer.trimStringToWidth(this.text.get(currentLine).substring(this.lineScrollOffset), this.getWidth());

			this.setCursorPosition(this.fontRenderer.trimStringToWidth(s, i).length() + this.lineScrollOffset);

			return true;
		}
		else
			return false;
	}

	/**
	 * Draws the textbox
	 */
	@Override
	public void drawTextBox()
	{
		if (this.getVisible())
		{
			if (this.getEnableBackgroundDrawing())
			{
				drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
				drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
			}

			int i = this.isEnabled ? this.enabledColor : this.disabledColor;
			int j = this.cursorPosition - this.lineScrollOffset;
			int k = this.selectionEnd - this.lineScrollOffset;
			int cls = verticalScrollOffset;
			int maxLines = this.height / fontRenderer.FONT_HEIGHT;
			while(currentLine >= (maxLines+cls)) {
				cls += 1;
				verticalScrollOffset = cls;
			}
			while(currentLine < cls) {
				cls -= 1;
				verticalScrollOffset = cls;
			}
			for(int cl = cls; cl < (maxLines+cls) && cl < text.size();cl++){
				int pos = this.lineScrollOffset;
				if(pos < 0 || pos > text.get(cl).length()) continue;
				String s = this.fontRenderer.trimStringToWidth(this.text.get(cl).substring(pos), this.getWidth());
				boolean flag = cl == currentLine && j >= 0 && j <= s.length();
				boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
				int l = this.enableBackgroundDrawing ? this.x + 4 : this.x;
				int i1 = this.enableBackgroundDrawing ? this.y + (this.height - 8) / 2 : this.y;
				i1-=(height-fontRenderer.FONT_HEIGHT)/2;
				i1 += fontRenderer.FONT_HEIGHT * (cl-cls);
				int j1 = l;

				if (!s.isEmpty())
				{
					String s1 = flag ? s.substring(0, j) : s;
					j1 = this.fontRenderer.drawStringWithShadow(s1, (float)l, (float)i1, i);
				}

				boolean flag2 = cl == currentLine && (this.cursorPosition < this.text.get(cl).length() || this.text.get(cl).length() >= this.getMaxStringLength());
				int k1 = j1;

				if (!flag)
					k1 = j > 0?l+this.width: l;
				else if (flag2)
				{
					k1 = j1 - 1;
					--j1;
				}

				if(cl == currentLine) {

					if (k > s.length())
						k = s.length();

					if (!s.isEmpty() && flag && j < s.length())
						this.fontRenderer.drawStringWithShadow(s.substring(j), (float)j1, (float)i1, i);

					if (flag1)
						if(flag2)
							Gui.drawRect(k1, i1-1, k1+1, i1+1+this.fontRenderer.FONT_HEIGHT, -3092272);
						else
							this.fontRenderer.drawStringWithShadow("_", (float)k1, (float)i1, i);

					if (k != j)
					{
						int l1 = l + this.fontRenderer.getStringWidth(s.substring(0, k));
						this.drawSelectionBox(k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRenderer.FONT_HEIGHT);
					}
				}
			}
		}
		float md = Mouse.getDWheel();
		if(md != 0) {
			int m = md > 0 ? -1 : 1;
			this.verticalScrollOffset += m;
			verticalScrollOffset = MathHelper.clamp(verticalScrollOffset,0,text.size()-1);
		}
	}

	/**
	 * Draws the blue selection box.
	 */
	private void drawSelectionBox(int startX, int startY, int endX, int endY)
	{
		if (startX < endX)
		{
			int i = startX;
			startX = endX;
			endX = i;
		}

		if (startY < endY)
		{
			int j = startY;
			startY = endY;
			endY = j;
		}

		if (endX > this.x + this.width)
			endX = this.x+this.width;

		if (startX > this.x + this.width)
			startX = this.x+this.width;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
		GlStateManager.disableTexture2D();
		GlStateManager.enableColorLogic();
		GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos(startX, endY, 0.0D).endVertex();
		bufferbuilder.pos(endX, endY, 0.0D).endVertex();
		bufferbuilder.pos(endX, startY, 0.0D).endVertex();
		bufferbuilder.pos(startX, startY, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.disableColorLogic();
		GlStateManager.enableTexture2D();
	}

	/**
	 * Sets the maximum length for the text in this text box. If the current text is longer than this length, the
	 * current text will be trimmed.
	 */
	@Override
	public void setMaxStringLength(int length)
	{
		this.maxStringLength = length;

		if (this.text.get(currentLine).length() > length)
			this.text.set(currentLine, this.text.get(currentLine).substring(0, length));
	}

	/**
	 * returns the maximum number of character that can be contained in this textbox
	 */
	@Override
	public int getMaxStringLength()
	{
		return this.maxStringLength;
	}

	/**
	 * returns the current position of the cursor
	 */
	@Override
	public int getCursorPosition()
	{
		return this.cursorPosition;
	}

	public char getCharAtCursor(int offset) {
		if(getCursorPosition() == 0) return ' ';
		if(text.get(currentLine).length() < getCursorPosition()+offset) return ' ';
		return text.get(currentLine).charAt(getCursorPosition()-1+offset);
	}

	/**
	 * Gets whether the background and outline of this text box should be drawn (true if so).
	 */
	@Override
	public boolean getEnableBackgroundDrawing()
	{
		return this.enableBackgroundDrawing;
	}

	/**
	 * Sets whether or not the background and outline of this text box should be drawn.
	 */
	@Override
	public void setEnableBackgroundDrawing(boolean enableBackgroundDrawingIn)
	{
		this.enableBackgroundDrawing = enableBackgroundDrawingIn;
	}

	/**
	 * Sets the color to use when drawing this text box's text. A different color is used if this text box is disabled.
	 */
	@Override
	public void setTextColor(int color)
	{
		this.enabledColor = color;
	}

	/**
	 * Sets the color to use for text in this text box when this text box is disabled.
	 */
	@Override
	public void setDisabledTextColour(int color)
	{
		this.disabledColor = color;
	}

	/**
	 * Sets focus to this gui element
	 */
	@Override
	public void setFocused(boolean isFocusedIn)
	{
		if (isFocusedIn && !this.isFocused)
			this.cursorCounter = 0;

		this.isFocused = isFocusedIn;

		if (Minecraft.getMinecraft().currentScreen != null)
			Minecraft.getMinecraft().currentScreen.setFocused(isFocusedIn);
	}

	/**
	 * Getter for the focused field
	 */
	@Override
	public boolean isFocused()
	{
		return this.isFocused;
	}

	/**
	 * Sets whether this text box is enabled. Disabled text boxes cannot be typed in.
	 */
	@Override
	public void setEnabled(boolean enabled)
	{
		this.isEnabled = enabled;
	}

	/**
	 * the side of the selection that is not the cursor, may be the same as the cursor
	 */
	@Override
	public int getSelectionEnd()
	{
		return this.selectionEnd;
	}

	/**
	 * returns the width of the textbox depending on if background drawing is enabled
	 */
	@Override
	public int getWidth()
	{
		return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
	}

	/**
	 * Sets the position of the selection anchor (the selection anchor and the cursor position mark the edges of the
	 * selection). If the anchor is set beyond the bounds of the current text, it will be put back inside.
	 */
	@Override
	public void setSelectionPos(int position)
	{
		int i = this.text.get(currentLine).length();

		if (position > i)
			position = i;

		if (position < 0)
			position = 0;

		this.selectionEnd = position;

		if (this.fontRenderer != null)
		{
			if (this.lineScrollOffset > i)
				this.lineScrollOffset = i;

			int j = this.getWidth();
			String s = this.fontRenderer.trimStringToWidth(this.text.get(currentLine).substring(this.lineScrollOffset), j);
			int k = s.length() + this.lineScrollOffset;

			if (position == this.lineScrollOffset)
				this.lineScrollOffset -= this.fontRenderer.trimStringToWidth(this.text.get(currentLine), j, true).length();

			if (position > k)
				this.lineScrollOffset += position-k;
			else if (position <= this.lineScrollOffset)
				this.lineScrollOffset -= this.lineScrollOffset-position;

			this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
		}
	}

	/**
	 * Sets whether this text box loses focus when something other than it is clicked.
	 */
	@Override
	public void setCanLoseFocus(boolean canLoseFocusIn)
	{
		this.canLoseFocus = canLoseFocusIn;
	}

	/**
	 * returns true if this textbox is visible
	 */
	@Override
	public boolean getVisible()
	{
		return this.visible;
	}

	/**
	 * Sets whether or not this textbox is visible
	 */
	@Override
	public void setVisible(boolean isVisible)
	{
		this.visible = isVisible;
	}
}
