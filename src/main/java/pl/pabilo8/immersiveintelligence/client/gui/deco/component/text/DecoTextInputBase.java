package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.MoveUnit;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextCaret;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextHistoryState;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Shared editing, validation, selection, history, clipboard and rendering engine for Deco text inputs.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 12.07.2025
 */
@SuppressWarnings("unchecked")
public abstract class DecoTextInputBase<TYPE extends DecoTextInputBase<TYPE>> extends DecoComponent<TYPE>
{
	//Text and Carets
	private final List<String> lines = new ArrayList<>();
	private final List<TextCaret> carets = new ArrayList<>();
	private TextFilter filter = TextFilter.NONE;
	/**
	 * Character-level compatibility filter used by the historical withCustomFilter API.
	 */
	@Nullable
	private Predicate<String> customCharacterFilter = null;
	/**
	 * Whole-document validator used for contextual constraints.
	 */
	@Nullable
	private Predicate<String> validator = null;
	private Consumer<String> onTextChanged = null;
	private boolean editable = true;

	//Config
	private IIFontRenderer fontRenderer = IIClientUtils.fontRegular;
	private int maxStringLength = 32767;
	private ResLoc backgroundLocation = DecoTextures.COMPONENT_TEXT_FIELD;
	private IIColor textColor = IIColor.WHITE;
	private IIColor invalidTextColor = IIColor.MC_RED;
	private IIColor cursorColor = IIReference.COLOR_IMMERSIVE_ORANGE;
	private IIColor selectionColor = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.35f).withAlpha(0.60f);
	private int padding = 4;

	//Scroll
	private int verticalScroll = 0, maxVerticalScroll = 0;
	private int lineScrollOffset = 0; //char offset for single line horizontal scrolling

	//Cursor blink
	private int cursorCounter = 0;
	private final int blinkRate = 30;

	//History
	private static final int HISTORY_LIMIT = 100;
	private static final int HISTORY_CHARACTER_LIMIT = 1_000_000;
	private final Deque<TextHistoryState> undoStack = new ArrayDeque<>();
	private final Deque<TextHistoryState> redoStack = new ArrayDeque<>();
	private int undoCharacters = 0, redoCharacters = 0;
	@Nullable
	private EditKind lastEditKind = null;
	private int lastEditTick = Integer.MIN_VALUE;

	//Preferred column for vertical navigation
	private int preferredColumn = -1;

	protected DecoTextInputBase(int x, int y)
	{
		super(x, y);
		lines.add("");
		carets.add(new TextCaret(0, 0));
		withSize(160, (fontRenderer.FONT_HEIGHT+2)+1+padding*2);
		withOnKeyTyped(this::onKeyTyped);
		withOnPressed(this::onMousePressed);
		withOnDragged(this::onMouseDragged);
		withOnScroll(this::onMouseScroll);
	}

	//--- Setters ---//

	@Override
	public TYPE withPosition(int x, int y)
	{
		super.withPosition(x, y);
		onBoundsChanged();
		return (TYPE)this;
	}

	@Override
	public TYPE withSize(int width, int height)
	{
		super.withSize(width, height);
		calculateMaxScroll();
		onBoundsChanged();
		return (TYPE)this;
	}

	@Override
	public TYPE withWidth(int width)
	{
		super.withWidth(width);
		ensureCursorVisible();
		onBoundsChanged();
		return (TYPE)this;
	}

	@Override
	public TYPE withHeight(int height)
	{
		super.withHeight(height);
		calculateMaxScroll();
		onBoundsChanged();
		return (TYPE)this;
	}

	public TYPE withFilter(@Nullable TextFilter f)
	{
		this.filter = f==null?TextFilter.NONE: f;
		setTextInternal(getText(), false, true);
		onFilterChanged();
		return (TYPE)this;
	}

	/**
	 * Adds a character-level filter. The predicate receives one character as a one-character string.
	 * This preserves the historical API; use {@link #withValidator(Predicate)} for contextual validation.
	 */
	public TYPE withCustomFilter(@Nullable Predicate<String> filter)
	{
		this.customCharacterFilter = filter;
		setTextInternal(getText(), false, true);
		return (TYPE)this;
	}

	/**
	 * Adds a whole-document validator. Intermediate text rejected by this predicate is not inserted.
	 */
	public TYPE withValidator(@Nullable Predicate<String> validator)
	{
		this.validator = validator;
		return (TYPE)this;
	}

	public TYPE withEditable(boolean editable)
	{
		this.editable = editable;
		return (TYPE)this;
	}

	public TYPE withOnTextChanged(Consumer<String> onTextChanged)
	{
		this.onTextChanged = onTextChanged;
		return (TYPE)this;
	}

	public TYPE withMaxStringLength(int length)
	{
		this.maxStringLength = Math.max(0, length);
		setTextInternal(getText(), false, true);
		return (TYPE)this;
	}

	public TYPE withBackgroundLocation(@Nullable ResLoc backgroundLocation)
	{
		this.backgroundLocation = backgroundLocation;
		return (TYPE)this;
	}

	public TYPE withTextColor(IIColor color)
	{
		this.textColor = color;
		return (TYPE)this;
	}

	public TYPE withInvalidTextColor(IIColor color)
	{
		this.invalidTextColor = color;
		return (TYPE)this;
	}

	public TYPE withCursorColor(IIColor color)
	{
		this.cursorColor = color;
		return (TYPE)this;
	}

	public TYPE withSelectionColor(IIColor color)
	{
		this.selectionColor = color;
		return (TYPE)this;
	}

	public TYPE withPadding(int padding)
	{
		this.padding = Math.max(0, padding);
		calculateMaxScroll();
		ensureCursorVisible();
		return (TYPE)this;
	}

	public TYPE withFontRenderer(IIFontRenderer fontRenderer)
	{
		this.fontRenderer = fontRenderer==null?IIClientUtils.fontRegular: fontRenderer;
		calculateMaxScroll();
		ensureCursorVisible();
		return (TYPE)this;
	}


	public TYPE withText(@Nullable Object object)
	{
		String text;
		if(object==null)
			text = "";
		else if(object instanceof String[])
			text = String.join("\n", (String[])object);
		else
			text = String.valueOf(object);
		setTextInternal(text, false, true);
		return (TYPE)this;
	}

	/**
	 * Replaces the text and emits the normal change callback.
	 */
	public TYPE withTextAndNotify(@Nullable Object object)
	{
		String text = object instanceof String[]?String.join("\n", (String[])object): String.valueOf(object==null?"": object);
		setTextInternal(text, true, true);
		return (TYPE)this;
	}

	//--- Shared extension hooks ---//

	/**
	 * @return whether this concrete control supports line breaks, vertical navigation and multiple carets
	 */
	protected boolean isMultiLineInput()
	{
		return false;
	}

	/**
	 * @return horizontal space reserved by a concrete control for trailing child components
	 */
	protected int getTrailingDecorationWidth()
	{
		return 0;
	}

	protected void onBoundsChanged()
	{

	}

	protected void onFilterChanged()
	{

	}

	protected final TextFilter getFilter()
	{
		return filter;
	}

	protected final IIFontRenderer getTextFontRenderer()
	{
		return fontRenderer;
	}

	protected final int getContentWidth()
	{
		return Math.max(0, width-padding*2-Math.max(0, getTrailingDecorationWidth()));
	}

	public int getCursorPosition()
	{
		return primary().pos;
	}

	public void setCursorPosition(int p)
	{
		TextCaret c = primary();
		c.pos = MathHelper.clamp(p, 0, lines.get(c.line).length());
		c.anchorLine = c.line;
		c.anchorPos = c.pos;
		ensureCursorVisible();
	}

	public int getCurrentLineIndex()
	{
		return primary().line;
	}

	public int getLineCount()
	{
		return lines.size();
	}

	//--- Drawing ---//

	@Override
	protected boolean initialize()
	{
		calculateMaxScroll();
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		if(backgroundLocation!=null)
		{
			bindAtlas();
			IIDrawUtils.startTexturedColored()
					.drawConnectedTexColorRect(x, y, width, height, IIColor.WHITE, backgroundLocation, 32, 32, 8, 8)
					.finish();
		}

		int innerWidth = getContentWidth();
		int innerHeight = Math.max(0, height-padding*2);
		if(innerWidth==0||innerHeight==0)
		{
			cursorCounter++;
			return;
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		boolean scissor = parentGui!=null;
		if(scissor)
			parentGui.scissorStart(x+padding, y+padding, innerWidth, innerHeight);
		try
		{
			int visibleLines = getVisibleLineCount();
			int startLine = isMultiLineInput()?verticalScroll: 0;
			int endLine = isMultiLineInput()?Math.min(startLine+visibleLines, lines.size()): 1;
			boolean showCursor = editable&&isFocused()&&(cursorCounter/blinkRate%2==0);
			IIColor drawnTextColor = isTextValid()?textColor: invalidTextColor;

			lineScrollOffset = Math.max(0, lineScrollOffset);
			for(int lineIdx = startLine; lineIdx < endLine; lineIdx++)
			{
				String fullLine = lines.get(lineIdx);
				int sliceStart = Math.min(lineScrollOffset, fullLine.length());
				String slice = fullLine.substring(sliceStart);
				int lineY = y+padding+(lineIdx-startLine)*fontRenderer.FONT_HEIGHT;
				int drawX = getLineDrawX(fullLine, sliceStart, innerWidth);

				drawSelections(lineIdx, fullLine, sliceStart, drawX, lineY);
				drawTextLine(fullLine, sliceStart, drawX, lineY, innerWidth, drawnTextColor);
				if(showCursor)
					drawCarets(lineIdx, slice, sliceStart, drawX, lineY, innerWidth);
			}
		} finally
		{
			GlStateManager.enableTexture2D();
			if(scissor)
				parentGui.scissorEnd();
			GlStateManager.popMatrix();
		}
		cursorCounter++;
	}

	private int getLineDrawX(String fullLine, int sliceStart, int innerWidth)
	{
		if(filter.isNumeric()&&sliceStart==0)
		{
			int textWidth = fontRenderer.getStringWidth(fullLine);
			if(textWidth < innerWidth)
				return x+padding+innerWidth-textWidth;
		}
		return x+padding;
	}

	private void drawSelections(int lineIdx, String fullLine, int sliceStart, int drawX, int lineY)
	{
		GlStateManager.disableTexture2D();
		IIDrawUtils draw = IIDrawUtils.startColored();
		for(TextCaret caret : carets)
		{
			if(!caret.hasSelection()||caret.startLine() > lineIdx||lineIdx > caret.endLine())
				continue;

			int selectionStart = lineIdx==caret.startLine()?caret.startPos(): 0;
			int selectionEnd = lineIdx==caret.endLine()?caret.endPos(): fullLine.length();
			selectionStart = MathHelper.clamp(selectionStart, 0, fullLine.length());
			selectionEnd = MathHelper.clamp(selectionEnd, 0, fullLine.length());
			if(selectionEnd <= sliceStart)
				continue;

			int visibleStart = Math.max(sliceStart, Math.min(selectionStart, selectionEnd));
			int visibleEnd = Math.max(visibleStart, Math.max(selectionStart, selectionEnd));
			visibleEnd = Math.min(visibleEnd, fullLine.length());
			if(visibleStart==visibleEnd)
				continue;

			int startX = drawX+fontRenderer.getStringWidth(fullLine.substring(sliceStart, visibleStart));
			int endX = drawX+fontRenderer.getStringWidth(fullLine.substring(sliceStart, visibleEnd));
			draw.drawColorRect(startX, lineY-1, Math.max(1, endX-startX), fontRenderer.FONT_HEIGHT+2, selectionColor);
		}
		draw.finish();
		GlStateManager.enableTexture2D();
	}

	/**
	 * Draws one text line. Multiline controls may override this to provide syntax highlighting.
	 */
	protected void drawTextLine(String fullLine, int sliceStart, int drawX, int lineY, int maxWidth, IIColor fallbackColor)
	{
		String slice = fullLine.substring(Math.min(sliceStart, fullLine.length()));
		fontRenderer.drawString(fontRenderer.trimStringToWidth(slice, maxWidth), drawX, lineY, fallbackColor.getPackedARGB());
	}

	private void drawCarets(int lineIdx, String slice, int sliceStart, int drawX, int lineY, int innerWidth)
	{
		GlStateManager.disableTexture2D();
		IIDrawUtils draw = IIDrawUtils.startColored();
		int viewportLeft = x+padding;
		int viewportRight = viewportLeft+Math.max(0, innerWidth-1);
		for(int i = 0; i < carets.size(); i++)
		{
			TextCaret caret = carets.get(i);
			if(caret.line!=lineIdx||caret.pos < sliceStart)
				continue;
			int column = Math.min(caret.pos-sliceStart, slice.length());
			int caretX = drawX+fontRenderer.getStringWidth(slice.substring(0, column));
			//OpenGL scissor rectangles exclude their right edge, so the final insertion
			//position must use the last pixel inside the text viewport.
			caretX = MathHelper.clamp(caretX, viewportLeft, viewportRight);
			draw.drawColorRect(caretX, lineY-1, 1, fontRenderer.FONT_HEIGHT+2,
					i==0?cursorColor: cursorColor.withAlpha(0.6f));
		}
		draw.finish();
		GlStateManager.enableTexture2D();
	}

	@Override
	public void cleanup()
	{

	}

	//Public text insertion
	public void writeText(String text)
	{
		writeText(text, EditKind.INSERT);
	}

	private void writeText(@Nullable String text, EditKind editKind)
	{
		if(!editable||text==null||text.isEmpty())
			return;

		TextHistoryState before = prepareHistory(editKind);
		String beforeText = getText();
		deleteSelections();

		List<TextCaret> ordered = new ArrayList<>(carets);
		ordered.sort(Comparator.comparingInt((TextCaret caret) -> caret.line)
				.thenComparingInt(caret -> caret.pos).reversed());
		for(TextCaret caret : ordered)
		{
			String insertion = filterInsertion(caret, normalizeInput(text));
			if(!insertion.isEmpty())
				insertMultilineAtCaret(caret, insertion);
		}

		finishUserEdit(before, beforeText, editKind);
	}

	private void insertMultilineAtCaret(TextCaret caret, String text)
	{
		if(!isMultiLineInput()||!text.contains("\n"))
		{
			insertTextAtCaret(caret, text);
			return;
		}

		String[] parts = text.split("\n", -1);
		String line = lines.get(caret.line);
		String tail = line.substring(caret.pos);
		lines.set(caret.line, line.substring(0, caret.pos)+parts[0]);
		int insertAt = caret.line+1;
		for(int i = 1; i < parts.length; i++)
			lines.add(insertAt++, i==parts.length-1?parts[i]+tail: parts[i]);

		caret.line = insertAt-1;
		caret.pos = lines.get(caret.line).length()-tail.length();
		caret.anchorLine = caret.line;
		caret.anchorPos = caret.pos;
	}

	private void insertTextAtCaret(TextCaret caret, String text)
	{
		if(text.isEmpty())
			return;
		String line = lines.get(caret.line);
		lines.set(caret.line, line.substring(0, caret.pos)+text+line.substring(caret.pos));
		caret.pos += text.length();
		caret.anchorLine = caret.line;
		caret.anchorPos = caret.pos;
	}

	//Deletion
	private void deleteCarets(MoveUnit moveUnit, int dir)
	{
		if(!editable)
			return;
		TextHistoryState before = prepareHistory(EditKind.DELETE);
		String beforeText = getText();

		if(anySelection())
			deleteSelections();
		else
		{
			List<TextCaret> ordered = new ArrayList<>(carets);
			ordered.sort(Comparator.comparingInt((TextCaret caret) -> caret.line)
					.thenComparingInt(caret -> caret.pos).reversed());

			for(TextCaret caret : ordered)
			{
				if(moveUnit==MoveUnit.CHAR)
				{
					if(dir < 0)
					{
						if(caret.pos > 0)
						{
							String line = lines.get(caret.line);
							lines.set(caret.line, line.substring(0, caret.pos-1)+line.substring(caret.pos));
							caret.pos--;
						}
						else if(isMultiLineInput()&&caret.line > 0)
						{
							int previousLength = lines.get(caret.line-1).length();
							lines.set(caret.line-1, lines.get(caret.line-1)+lines.get(caret.line));
							lines.remove(caret.line);
							caret.line--;
							caret.pos = previousLength;
						}
					}
					else
					{
						String line = lines.get(caret.line);
						if(caret.pos < line.length())
							lines.set(caret.line, line.substring(0, caret.pos)+line.substring(caret.pos+1));
						else if(isMultiLineInput()&&caret.line < lines.size()-1)
						{
							lines.set(caret.line, line+lines.get(caret.line+1));
							lines.remove(caret.line+1);
						}
					}
					caret.anchorLine = caret.line;
					caret.anchorPos = caret.pos;
				}
				else
				{
					int originalLine = caret.line, originalPosition = caret.pos;
					caret.moveHorizontal(dir < 0, MoveUnit.WORD, lines, isMultiLineInput(), this::isWordChar);
					caret.anchorLine = originalLine;
					caret.anchorPos = originalPosition;
				}
			}
			deleteSelections();
		}

		finishUserEdit(before, beforeText, EditKind.DELETE);
	}

	private void newlineAtCarets()
	{
		if(!editable||!isMultiLineInput()||documentLength() >= maxStringLength)
			return;
		TextHistoryState before = prepareHistory(EditKind.STRUCTURAL);
		String beforeText = getText();
		deleteSelections();

		List<TextCaret> ordered = new ArrayList<>(carets);
		ordered.sort(Comparator.comparingInt((TextCaret caret) -> caret.line)
				.thenComparingInt(caret -> caret.pos).reversed());
		for(TextCaret caret : ordered)
		{
			if(documentLength() >= maxStringLength)
				break;
			String line = lines.get(caret.line);
			lines.set(caret.line, line.substring(0, caret.pos));
			lines.add(caret.line+1, line.substring(caret.pos));
			caret.line++;
			caret.pos = 0;
			caret.anchorLine = caret.line;
			caret.anchorPos = 0;
		}
		finishUserEdit(before, beforeText, EditKind.STRUCTURAL);
	}

	private void deleteSelections()
	{
		if(!anySelection())
			return;

		String document = getText();
		List<TextRange> ranges = new ArrayList<>();
		for(TextCaret caret : carets)
			if(caret.hasSelection())
			{
				int first = documentOffset(caret.anchorLine, caret.anchorPos);
				int second = documentOffset(caret.line, caret.pos);
				ranges.add(new TextRange(Math.min(first, second), Math.max(first, second)));
			}
		ranges.sort(Comparator.comparingInt(range -> range.start));

		List<TextRange> merged = new ArrayList<>();
		for(TextRange range : ranges)
		{
			if(merged.isEmpty()||range.start > merged.get(merged.size()-1).end)
				merged.add(range);
			else
				merged.get(merged.size()-1).end = Math.max(merged.get(merged.size()-1).end, range.end);
		}

		Map<TextCaret, Integer> resultingOffsets = new IdentityHashMap<>();
		for(TextCaret caret : carets)
		{
			int caretOffset = documentOffset(caret.line, caret.pos);
			int anchorOffset = documentOffset(caret.anchorLine, caret.anchorPos);
			int target = caret.hasSelection()?Math.min(caretOffset, anchorOffset): caretOffset;
			resultingOffsets.put(caret, transformOffsetAfterDeletion(target, merged));
		}

		StringBuilder edited = new StringBuilder(document);
		for(int i = merged.size()-1; i >= 0; i--)
		{
			TextRange range = merged.get(i);
			edited.delete(range.start, range.end);
		}
		setDocumentRaw(edited.toString());
		for(TextCaret caret : carets)
		{
			setCaretFromDocumentOffset(caret, resultingOffsets.get(caret));
			caret.anchorLine = caret.line;
			caret.anchorPos = caret.pos;
		}
		normalizeCarets();
	}

	private int transformOffsetAfterDeletion(int offset, List<TextRange> ranges)
	{
		int removed = 0;
		for(TextRange range : ranges)
		{
			if(offset < range.start)
				break;
			if(offset <= range.end)
				return range.start-removed;
			removed += range.end-range.start;
		}
		return offset-removed;
	}

	private void setDocumentRaw(String document)
	{
		lines.clear();
		if(isMultiLineInput())
			Collections.addAll(lines, document.split("\n", -1));
		else
			lines.add(document.replace('\n', ' '));
		if(lines.isEmpty())
			lines.add("");
	}

	private void setCaretFromDocumentOffset(TextCaret caret, int offset)
	{
		int remaining = MathHelper.clamp(offset, 0, documentLength());
		for(int line = 0; line < lines.size(); line++)
		{
			int length = lines.get(line).length();
			if(remaining <= length||line==lines.size()-1)
			{
				caret.line = line;
				caret.pos = Math.min(remaining, length);
				return;
			}
			remaining -= length+1;
		}
	}

	//--- Unified Caret Movement --- //
	private void moveCarets(MoveUnit unit, int dx, int dy, boolean shift)
	{
		//capture preferred column for vertical navigation
		if(dy!=0&&preferredColumn < 0)
			preferredColumn = primary().pos;
		if(dy==0&&dx!=0)
			preferredColumn = -1; //reset when horizontal only

		boolean allowCross = isMultiLineInput(); //cross-line wrapping only when multi-line

		for(TextCaret c : carets)
		{
			//vertical first
			if(dy!=0&&isMultiLineInput())
			{
				int targetLine = MathHelper.clamp(c.line+dy, 0, lines.size()-1);
				String tgt = lines.get(targetLine);
				int col = (preferredColumn >= 0)?preferredColumn: c.pos;
				c.line = targetLine;
				c.pos = Math.min(col, tgt.length());
			}
			//horizontal / word / end movement via caret helper
			if(dx!=0)
				c.moveHorizontal(dx < 0, unit, lines, allowCross, this::isWordChar);
			//deselect
			if(!shift)
			{
				c.anchorLine = c.line;
				c.anchorPos = c.pos;
			}
		}
		normalizeCarets();
		ensureCursorVisible();
	}

	private boolean isWordChar(char ch)
	{
		return Character.isLetterOrDigit(ch)||ch=='_';
	}

	private void addCaretVertical(int delta)
	{
		List<TextCaret> added = new ArrayList<>();
		for(TextCaret c : carets)
		{
			int nl = c.line+delta;
			if(nl < 0||nl >= lines.size()) continue;
			int p = Math.min(c.pos, lines.get(nl).length());
			added.add(new TextCaret(nl, p));
		}
		carets.addAll(added);
		normalizeCarets();
	}

	//Mouse
	private boolean onMousePressed(TYPE self, MouseButton button, int mouseX, int mouseY)
	{
		if(button!=MouseButton.LEFT)
			return false;

		int line = lineAt(mouseY);
		int position = positionAt(lines.get(line), mouseX);
		boolean shift = GuiScreen.isShiftKeyDown();
		boolean alt = isMultiLineInput()&&(Keyboard.isKeyDown(Keyboard.KEY_LMENU)||Keyboard.isKeyDown(Keyboard.KEY_RMENU));
		if(!shift&&!alt)
		{
			carets.clear();
			carets.add(new TextCaret(line, position));
		}
		else if(shift)
		{
			TextCaret caret = primary();
			caret.line = line;
			caret.pos = position;
		}
		else
			carets.add(new TextCaret(line, position));

		breakHistoryCoalescing();
		normalizeCarets();
		ensureCursorVisible();
		return true;
	}

	private boolean onMouseDragged(TYPE self, MouseButton button, int mouseX, int mouseY)
	{
		if(button!=MouseButton.LEFT)
			return false;
		int line = lineAt(mouseY);
		TextCaret caret = primary();
		caret.line = line;
		caret.pos = positionAt(lines.get(line), mouseX);
		breakHistoryCoalescing();
		ensureCursorVisible();
		return true;
	}

	private int lineAt(int mouseY)
	{
		if(!isMultiLineInput())
			return 0;
		int innerY = Math.max(0, mouseY-(y+padding));
		return MathHelper.clamp(verticalScroll+(innerY/fontRenderer.FONT_HEIGHT), 0, lines.size()-1);
	}

	private int positionAt(String line, int mouseX)
	{
		int sliceStart = Math.min(lineScrollOffset, line.length());
		String visible = line.substring(sliceStart);
		int drawX = getLineDrawX(line, sliceStart, getContentWidth());
		int innerX = Math.max(0, mouseX-drawX);
		return MathHelper.clamp(sliceStart+fontRenderer.trimStringToWidth(visible, innerX).length(), 0, line.length());
	}

	private boolean onMouseScroll(TYPE self, int wheel, int mouseX, int mouseY)
	{
		if(!isMultiLineInput()||!IIMath.isPointInRectangle(x, y, x+width, y+height, mouseX, mouseY))
			return false;
		verticalScroll = MathHelper.clamp(verticalScroll-wheel, 0, maxVerticalScroll);
		return true;
	}

	//Key handling
	private boolean onKeyTyped(TYPE input, char character, int key)
	{
		boolean ctrl = GuiScreen.isCtrlKeyDown();
		boolean shift = GuiScreen.isShiftKeyDown();
		boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU)||Keyboard.isKeyDown(Keyboard.KEY_RMENU);

		if(ctrl&&key==Keyboard.KEY_A)
		{
			selectAll();
			return true;
		}
		if(isMultiLineInput()&&alt&&(key==Keyboard.KEY_UP||key==Keyboard.KEY_DOWN))
		{
			addCaretVertical(key==Keyboard.KEY_UP?-1: 1);
			breakHistoryCoalescing();
			return true;
		}

		MoveUnit unit;
		switch(key)
		{
			case Keyboard.KEY_RETURN:
				if(!isMultiLineInput())
				{
					if(parentGui!=null)
						parentGui.requestFocus(null);
					return true;
				}
				newlineAtCarets();
				return true;
			case Keyboard.KEY_BACK:
				deleteCarets(ctrl?MoveUnit.WORD: MoveUnit.CHAR, -1);
				return true;
			case Keyboard.KEY_DELETE:
				deleteCarets(ctrl?MoveUnit.WORD: MoveUnit.CHAR, 1);
				return true;
			case Keyboard.KEY_LEFT:
				unit = ctrl?MoveUnit.WORD: MoveUnit.CHAR;
				moveCarets(unit, -1, 0, shift);
				breakHistoryCoalescing();
				return true;
			case Keyboard.KEY_RIGHT:
				unit = ctrl?MoveUnit.WORD: MoveUnit.CHAR;
				moveCarets(unit, 1, 0, shift);
				breakHistoryCoalescing();
				return true;
			case Keyboard.KEY_HOME:
				moveCarets(MoveUnit.END, -1, 0, shift);
				breakHistoryCoalescing();
				return true;
			case Keyboard.KEY_END:
				moveCarets(MoveUnit.END, 1, 0, shift);
				breakHistoryCoalescing();
				return true;
			case Keyboard.KEY_UP:
				if(isMultiLineInput())
				{
					moveCarets(MoveUnit.CHAR, 0, -1, shift);
					breakHistoryCoalescing();
					return true;
				}
				return false;
			case Keyboard.KEY_DOWN:
				if(isMultiLineInput())
				{
					moveCarets(MoveUnit.CHAR, 0, 1, shift);
					breakHistoryCoalescing();
					return true;
				}
				return false;
			default:
				if(editable&&ChatAllowedCharacters.isAllowedCharacter(character))
				{
					writeText(Character.toString(character));
					return true;
				}
		}
		return false;
	}

	//GUI events
	@Override
	public void onGuiEvent(DecoGuiEvent event)
	{
		switch(event)
		{
			case COPY:
				DecoGuiUtils.setClipboardString(buildCopyString());
				break;
			case CUT:
				if(editable&&anySelection())
				{
					TextHistoryState before = prepareHistory(EditKind.CUT);
					String beforeText = getText();
					DecoGuiUtils.setClipboardString(buildCopyString());
					deleteSelections();
					finishUserEdit(before, beforeText, EditKind.CUT);
				}
				break;
			case PASTE:
				if(editable)
					pasteString(DecoGuiUtils.getClipboardString());
				break;
			case UNDO:
				undo();
				break;
			case REDO:
				redo();
				break;
			case SELECT_ALL:
				selectAll();
				break;
			default:
				super.onGuiEvent(event);
		}
	}

	private void selectAll()
	{
		if(!isMultiLineInput())
		{
			clearToSingleCaret(0, lines.get(0).length());
			primary().anchorPos = 0;
		}
		else
		{
			clearToSingleCaret(lines.size()-1, lines.get(lines.size()-1).length());
			primary().anchorLine = 0;
			primary().anchorPos = 0;
		}
		breakHistoryCoalescing();
		ensureCursorVisible();
	}

	//Selection & caret utilities
	private TextCaret primary()
	{
		if(carets.isEmpty())
			carets.add(new TextCaret(0, 0));
		return carets.get(0);
	}

	private boolean anySelection()
	{
		return carets.stream().anyMatch(TextCaret::hasSelection);
	}

	private void normalizeCarets()
	{
		ensureCaretsInBounds();
		Set<Long> seen = new HashSet<>();
		List<TextCaret> uniq = new ArrayList<>();
		for(TextCaret c : carets)
		{
			long k = (((long)c.line)<<32)|c.pos;
			if(seen.add(k)) uniq.add(c);
		}
		carets.clear();
		carets.addAll(uniq);
		if(carets.isEmpty())
			carets.add(new TextCaret(0, 0));
	}

	private void ensureCaretsInBounds()
	{
		for(TextCaret c : carets)
		{
			c.line = MathHelper.clamp(c.line, 0, lines.size()-1);
			String ln = lines.get(c.line);
			c.pos = MathHelper.clamp(c.pos, 0, ln.length());
			c.anchorLine = MathHelper.clamp(c.anchorLine, 0, lines.size()-1);
			String aln = lines.get(c.anchorLine);
			c.anchorPos = MathHelper.clamp(c.anchorPos, 0, aln.length());
		}
	}

	//--- History and edit completion ---//

	private TextHistoryState snapshot()
	{
		return new TextHistoryState(lines, carets);
	}

	@Nullable
	private TextHistoryState prepareHistory(EditKind editKind)
	{
		boolean coalesce = (editKind==EditKind.INSERT||editKind==EditKind.DELETE)
				&&editKind==lastEditKind
				&&cursorCounter-lastEditTick <= 20
				&&carets.size()==1
				&&!anySelection();
		return coalesce?null: snapshot();
	}

	private void finishUserEdit(@Nullable TextHistoryState before, String beforeText, EditKind editKind)
	{
		postEdit();
		if(beforeText.equals(getText()))
			return;
		if(before!=null)
			pushUndo(before);
		clearRedo();
		lastEditKind = editKind;
		lastEditTick = cursorCounter;
		notifyTextChanged();
	}

	private void pushUndo(TextHistoryState state)
	{
		undoStack.push(state);
		undoCharacters += state.characterCount();
		while(undoStack.size() > HISTORY_LIMIT||undoCharacters > HISTORY_CHARACTER_LIMIT)
			undoCharacters -= undoStack.removeLast().characterCount();
	}

	private void pushRedo(TextHistoryState state)
	{
		redoStack.push(state);
		redoCharacters += state.characterCount();
		while(redoStack.size() > HISTORY_LIMIT||redoCharacters > HISTORY_CHARACTER_LIMIT)
			redoCharacters -= redoStack.removeLast().characterCount();
	}

	private void clearHistory()
	{
		undoStack.clear();
		redoStack.clear();
		undoCharacters = redoCharacters = 0;
		breakHistoryCoalescing();
	}

	private void clearRedo()
	{
		redoStack.clear();
		redoCharacters = 0;
	}

	private void breakHistoryCoalescing()
	{
		lastEditKind = null;
		lastEditTick = Integer.MIN_VALUE;
	}

	private void undo()
	{
		if(!editable||undoStack.isEmpty())
			return;
		TextHistoryState current = snapshot();
		TextHistoryState previous = undoStack.pop();
		undoCharacters -= previous.characterCount();
		pushRedo(current);
		restore(previous);
		breakHistoryCoalescing();
		notifyTextChanged();
	}

	private void redo()
	{
		if(!editable||redoStack.isEmpty())
			return;
		TextHistoryState current = snapshot();
		TextHistoryState next = redoStack.pop();
		redoCharacters -= next.characterCount();
		pushUndo(current);
		restore(next);
		breakHistoryCoalescing();
		notifyTextChanged();
	}

	private void restore(TextHistoryState state)
	{
		lines.clear();
		lines.addAll(state.lines);
		if(lines.isEmpty())
			lines.add("");
		carets.clear();
		for(TextCaret stored : state.carets)
			carets.add(stored.copy());
		normalizeCarets();
		calculateMaxScroll();
		ensureCursorVisible();
	}

	private void postEdit()
	{
		if(lines.isEmpty())
			lines.add("");
		calculateMaxScroll();
		normalizeCarets();
		ensureCursorVisible();
	}

	private void notifyTextChanged()
	{
		if(onTextChanged!=null)
			onTextChanged.accept(getText());
	}

	//--- Scroll ---//

	private int getVisibleLineCount()
	{
		return Math.max(1, Math.max(0, height-padding*2)/Math.max(1, fontRenderer.FONT_HEIGHT));
	}

	private void calculateMaxScroll()
	{
		if(!isMultiLineInput())
		{
			verticalScroll = maxVerticalScroll = 0;
			return;
		}
		maxVerticalScroll = Math.max(0, lines.size()-getVisibleLineCount());
		verticalScroll = MathHelper.clamp(verticalScroll, 0, maxVerticalScroll);
	}

	//--- Filtering and document access ---//

	private String normalizeInput(String input)
	{
		String normalized = input.replace("\r\n", "\n").replace('\r', '\n').replace('\t', ' ');
		return isMultiLineInput()?normalized: normalized.replace('\n', ' ');
	}

	private String sanitizeWholeText(String input)
	{
		String normalized = normalizeInput(input==null?"": input);
		StringBuilder characters = new StringBuilder(Math.min(normalized.length(), maxStringLength));
		for(int i = 0; i < normalized.length()&&characters.length() < maxStringLength; i++)
		{
			char character = normalized.charAt(i);
			if(customCharacterFilter==null||customCharacterFilter.test(String.valueOf(character)))
				characters.append(character);
		}

		String candidate = characters.toString();
		if(acceptsCandidate(candidate))
			return candidate;

		StringBuilder accepted = new StringBuilder(candidate.length());
		for(int i = 0; i < candidate.length(); i++)
		{
			String next = accepted.toString()+candidate.charAt(i);
			if(acceptsCandidate(next))
				accepted.append(candidate.charAt(i));
		}
		return accepted.toString();
	}

	private String filterInsertion(TextCaret caret, String input)
	{
		int remaining = Math.max(0, maxStringLength-documentLength());
		if(remaining==0||input.isEmpty())
			return "";

		StringBuilder accepted = new StringBuilder(Math.min(input.length(), remaining));
		if(filter==TextFilter.NONE&&validator==null)
		{
			for(int i = 0; i < input.length()&&accepted.length() < remaining; i++)
			{
				char character = input.charAt(i);
				if(customCharacterFilter==null||customCharacterFilter.test(String.valueOf(character)))
					accepted.append(character);
			}
			return accepted.toString();
		}

		for(int i = 0; i < input.length()&&accepted.length() < remaining; i++)
		{
			char character = input.charAt(i);
			if(customCharacterFilter!=null&&!customCharacterFilter.test(String.valueOf(character)))
				continue;
			String trial = accepted.toString()+character;
			if(acceptsCandidate(documentWithInsertion(caret, trial)))
				accepted.append(character);
		}
		return accepted.toString();
	}

	private boolean acceptsCandidate(String candidate)
	{
		return filter.accepts(candidate)&&(validator==null||validator.test(candidate));
	}

	public boolean isTextValid()
	{
		String text = getText();
		return filter.isValid(text)&&(validator==null||validator.test(text));
	}

	private String documentWithInsertion(TextCaret caret, String insertion)
	{
		String document = getText();
		int offset = documentOffset(caret.line, caret.pos);
		return document.substring(0, offset)+insertion+document.substring(offset);
	}

	private int documentOffset(int line, int position)
	{
		int offset = 0;
		for(int i = 0; i < line; i++)
			offset += lines.get(i).length()+1;
		return offset+position;
	}

	private int documentLength()
	{
		int length = Math.max(0, lines.size()-1);
		for(String line : lines)
			length += line.length();
		return length;
	}

	private void setTextInternal(String text, boolean notify, boolean resetHistory)
	{
		String previous = getText();
		String sanitized = sanitizeWholeText(text);
		lines.clear();
		if(isMultiLineInput())
			Collections.addAll(lines, sanitized.split("\n", -1));
		else
			lines.add(sanitized);
		if(lines.isEmpty())
			lines.add("");
		clearToSingleCaret(isMultiLineInput()?0: lines.size()-1, isMultiLineInput()?0: lines.get(0).length());
		verticalScroll = lineScrollOffset = 0;
		calculateMaxScroll();
		ensureCursorVisible();
		if(resetHistory)
			clearHistory();
		if(notify&&!previous.equals(getText()))
			notifyTextChanged();
	}

	public String getText()
	{
		if(!isMultiLineInput())
			return lines.get(0);
		StringBuilder result = new StringBuilder(documentLength());
		for(int i = 0; i < lines.size(); i++)
		{
			if(i > 0)
				result.append('\n');
			result.append(lines.get(i));
		}
		return result.toString();
	}

	private void clearToSingleCaret(int line, int pos)
	{
		carets.clear();
		line = MathHelper.clamp(line, 0, lines.size()-1);
		String ln = lines.get(line);
		pos = MathHelper.clamp(pos, 0, ln.length());
		carets.add(new TextCaret(line, pos));
	}

	public void ensureCursorVisible()
	{
		TextCaret caret = primary();
		if(isMultiLineInput())
		{
			int visibleLines = getVisibleLineCount();
			if(caret.line < verticalScroll)
				verticalScroll = caret.line;
			else if(caret.line >= verticalScroll+visibleLines)
				verticalScroll = caret.line-visibleLines+1;
			verticalScroll = MathHelper.clamp(verticalScroll, 0, maxVerticalScroll);
		}

		String line = lines.get(caret.line);
		int visibleWidth = Math.max(1, getContentWidth()-1);
		lineScrollOffset = MathHelper.clamp(lineScrollOffset, 0, line.length());
		if(caret.pos < lineScrollOffset)
			lineScrollOffset = caret.pos;
		while(lineScrollOffset < caret.pos&&fontRenderer.getStringWidth(line.substring(lineScrollOffset, caret.pos)) > visibleWidth)
			lineScrollOffset++;
		cursorCounter = 20;
	}

	@Override
	public void playPressSound(SoundHandler soundHandlerIn)
	{

	}

	//--- Clipboard ---//

	private String buildCopyString()
	{
		if(carets.size()==1)
		{
			TextCaret c = primary();
			if(!c.hasSelection()) return "";
			return extractSelection(c);
		}
		boolean any = false;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < carets.size(); i++)
		{
			TextCaret c = carets.get(i);
			String part = c.hasSelection()?extractSelection(c): "";
			any |= c.hasSelection();
			sb.append(part);
			if(i < carets.size()-1) sb.append('\n');
		}
		return any?sb.toString(): "";
	}

	private String extractSelection(TextCaret c)
	{
		int sL = c.startLine(), eL = c.endLine();
		if(sL==eL)
		{
			String line = lines.get(sL);
			int a = Math.min(c.startPos(), line.length());
			int b = Math.min(c.endPos(), line.length());
			if(a > b)
			{
				int t = a;
				a = b;
				b = t;
			}
			return line.substring(a, b);
		}
		StringBuilder sb = new StringBuilder();
		for(int l = sL; l <= eL; l++)
		{
			String line = lines.get(l);
			int from = (l==sL)?Math.min(c.startPos(), line.length()): 0;
			int to = (l==eL)?Math.min(c.endPos(), line.length()): line.length();
			if(from > to)
			{
				int t = from;
				from = to;
				to = t;
			}
			sb.append(line, from, to);
			if(l < eL) sb.append('\n');
		}
		return sb.toString();
	}

	private void pasteString(@Nullable String clipboard)
	{
		if(!editable||clipboard==null||clipboard.isEmpty())
			return;

		String normalized = normalizeInput(clipboard);
		String[] parts = normalized.split("\n", -1);
		if(carets.size() <= 1||parts.length!=carets.size())
		{
			writeText(normalized, EditKind.PASTE);
			return;
		}

		TextHistoryState before = prepareHistory(EditKind.PASTE);
		String beforeText = getText();
		List<TextCaret> ordered = new ArrayList<>(carets);
		ordered.sort(Comparator.comparingInt((TextCaret caret) -> caret.line)
				.thenComparingInt(caret -> caret.pos));
		deleteSelections();
		for(int i = ordered.size()-1; i >= 0; i--)
		{
			TextCaret caret = ordered.get(i);
			String insertion = filterInsertion(caret, parts[i]);
			if(!insertion.isEmpty())
				insertMultilineAtCaret(caret, insertion);
		}
		finishUserEdit(before, beforeText, EditKind.PASTE);
	}

	private static class TextRange
	{
		private final int start;
		private int end;

		private TextRange(int start, int end)
		{
			this.start = start;
			this.end = end;
		}
	}

	private enum EditKind
	{
		INSERT,
		DELETE,
		PASTE,
		CUT,
		STRUCTURAL
	}

}
