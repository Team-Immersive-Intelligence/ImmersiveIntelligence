package pl.pabilo8.immersiveintelligence.client.gui.deco.component.text;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.highlight.TextHighlighter;
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
 * A multi-line, multi-caret text field component with selection, undo/redo, clipboard & syntax highlighting.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 12.07.2025
 */
public class DecoTextField extends DecoComponent<DecoTextField>
{
	//Text and Carets
	private final List<String> lines = new ArrayList<>();
	private final List<TextCaret> carets = new ArrayList<>();
	private boolean multiLine = false;
	private TextFilter filter = TextFilter.NONE;
	private Predicate<String> customFilter = (s) -> true;
	private Consumer<String> onTextChanged = null;

	//Config
	private IIFontRenderer fontRenderer = IIClientUtils.fontRegular;
	private int maxStringLength = 32767;
	private ResLoc backgroundLocation = DecoTextures.RES_TEXTURES_DECO_COMPONENT_TEXT_FIELD;
	private IIColor textColor = IIColor.WHITE;
	private IIColor cursorColor = IIReference.COLOR_IMMERSIVE_ORANGE;
	private IIColor selectionColor = IIReference.COLOR_IMMERSIVE_ORANGE.withBrightness(0.35f).withAlpha(0.60f);
	private TextHighlighter highlighter = null;
	private int padding = 4;

	//Scroll
	private int verticalScroll = 0, maxVerticalScroll = 0;
	private int lineScrollOffset = 0; // char offset for single line horizontal scrolling

	//Cursor blink
	private int cursorCounter = 0;
	private final int blinkRate = 30;

	//History
	private static final int HISTORY_LIMIT = 100;
	private final Deque<TextHistoryState> undoStack = new ArrayDeque<>();
	private final Deque<TextHistoryState> redoStack = new ArrayDeque<>();

	//Preferred column for vertical navigation
	private int preferredColumn = -1;

	public DecoTextField(int x, int y)
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

	public DecoTextField withMultiLine(boolean multiline)
	{
		this.multiLine = multiline;
		calculateMaxScroll();
		return this;
	}

	public DecoTextField withFilter(TextFilter f)
	{
		this.filter = f;
		return this;
	}

	public DecoTextField withCustomFilter(Predicate<String> filter)
	{
		this.customFilter = filter;
		return this;
	}

	public DecoTextField withOnTextChanged(Consumer<String> onTextChanged)
	{
		this.onTextChanged = onTextChanged;
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

	public DecoTextField withHighlighter(TextHighlighter highlighter)
	{
		this.highlighter = highlighter;
		return this;
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

	private DecoTextField withText(String t)
	{
		if(t==null) t = "";
		lines.clear();
		if(!multiLine)
		{
			lines.add(t.replace('\r', ' ').replace('\n', ' ').replace('\t', ' '));
			clearToSingleCaret(0, lines.get(0).length());
		}
		else
		{
			Collections.addAll(lines, t.split("\n", -1));
			if(lines.isEmpty()) lines.add("");
			clearToSingleCaret(0, 0);
		}
		calculateMaxScroll();
		return this;
	}

	//--- Setters ---//


	public TextHighlighter getHighlighter()
	{
		return highlighter;
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
		pushHistory(); // base state
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		if(backgroundLocation!=null)
		{
			bindAtlas();
			IIDrawUtils.startTexturedColored().drawConnectedTexColorRect(x, y, width, height, IIColor.WHITE, backgroundLocation, 32, 32, 8, 8).finish();
		}
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		assert parentGui!=null;
		parentGui.scissorStart(x+padding, y+padding, width-padding*2, height-padding*2);

		int visibleLines = multiLine?(height-(padding*2))/fontRenderer.FONT_HEIGHT: 1;
		int startLine = multiLine?verticalScroll: 0;
		int endLine = multiLine?Math.min(startLine+visibleLines, lines.size()): 1;
		boolean showCursor = isFocused()&&(cursorCounter/blinkRate%2==0);

		//Ensure global offset not negative
		if(lineScrollOffset < 0) lineScrollOffset = 0;

		for(int lineIdx = startLine; lineIdx < endLine; lineIdx++)
		{
			String fullLine = lines.get(lineIdx);
			int sliceStart = Math.min(lineScrollOffset, fullLine.length());
			String slice = fullLine.substring(sliceStart);
			int lineY = y+padding+(lineIdx-startLine)*fontRenderer.FONT_HEIGHT;

			//Selections (global offset)
			GlStateManager.disableTexture2D();
			IIDrawUtils selDraw = IIDrawUtils.startColored();
			for(TextCaret c : carets)
			{
				//Skip if no selection
				if(!c.hasSelection()||(c.startLine() > lineIdx||lineIdx > c.endLine()))
					continue;

				int selStart = 0, selEnd = fullLine.length();
				if(lineIdx==c.startLine())
					selStart = c.startPos();
				if(lineIdx==c.endLine())
					selEnd = c.endPos();


				int startX = x+padding+fontRenderer.getStringWidth(fullLine.substring(sliceStart, selStart));
				int endX = x+padding+fontRenderer.getStringWidth(fullLine.substring(sliceStart, selEnd));
				selDraw.drawColorRect(startX, lineY-1,
						Math.max(1, endX-startX), fontRenderer.FONT_HEIGHT+2, selectionColor);
			}
			selDraw.finish();
			GlStateManager.enableTexture2D();

			//Text / highlighting
			int maxPixel = width-(padding*2);
			int drawX = x+padding;
			if(highlighter==null)
			{
				String visible = fontRenderer.trimStringToWidth(slice, maxPixel);
				fontRenderer.drawString(visible, drawX, lineY, textColor.getPackedARGB());
			}
			else
			{
				List<TextHighlighter.Segment> segs = highlighter.highlight(slice);
				int used = 0;
				for(TextHighlighter.Segment seg : segs)
				{
					if(seg.text.isEmpty()) continue;
					String remain = seg.text;
					while(!remain.isEmpty())
					{
						int len = remain.length();
						String candidate = remain;
						while(len > 0&&used+fontRenderer.getStringWidth(candidate) > maxPixel)
						{
							len--;
							candidate = remain.substring(0, len);
						}
						if(len==0)
							break;
						fontRenderer.drawString(candidate, drawX+used, lineY, seg.color.getPackedARGB());
						used += fontRenderer.getStringWidth(candidate);
						remain = (len < remain.length())?"": remain.substring(len);
						if(used >= maxPixel)
							break;
					}
					if(used >= maxPixel) break;
				}
			}

			// Carets
			if(showCursor)
			{
				GlStateManager.disableTexture2D();
				IIDrawUtils caretDraw = IIDrawUtils.startColored();
				for(int i = 0; i < carets.size(); i++)
				{
					TextCaret c = carets.get(i);
					if(c.line!=lineIdx) continue;
					int colPos = c.pos-sliceStart;
					if(colPos < 0) continue;
					if(colPos > slice.length()) colPos = slice.length();
					int caretX = x+padding+fontRenderer.getStringWidth(slice.substring(0, colPos));
					caretDraw.drawColorRect(caretX, lineY-1, 1, fontRenderer.FONT_HEIGHT+2, (i==0)?cursorColor: cursorColor.withAlpha(0.6f));
				}
				caretDraw.finish();
				GlStateManager.enableTexture2D();
			}
		}

		parentGui.scissorEnd();
		GlStateManager.popMatrix();
		cursorCounter++;
	}

	@Override
	public void cleanup()
	{

	}

	// Public text insertion
	public void writeText(String text)
	{
		if(text==null||text.isEmpty())
			return;
		String filtered = filterText(text);
		if(filtered.isEmpty())
			return;
		pushHistory();
		deleteSelections();
		List<TextCaret> ordered = new ArrayList<>(carets);
		ordered.sort((a, b) -> (a.line==b.line?Integer.compare(b.pos, a.pos): Integer.compare(a.line, b.line)));
		for(TextCaret c : ordered)
			insertMultilineAtCaret(c, filtered);

		calculateMaxScroll();
		normalizeCarets();
		ensureCursorVisible();
	}

	private void insertMultilineAtCaret(TextCaret c, String text)
	{
		if(!multiLine||!text.contains("\n"))
		{
			insertTextAtCaret(c, text);
			return;
		}
		String[] parts = text.split("\n", -1);
		String line = lines.get(c.line);
		String head = line.substring(0, c.pos)+parts[0];
		String tail = line.substring(c.pos);
		lines.set(c.line, head);
		int insertAt = c.line+1;
		for(int i = 1; i < parts.length; i++)
		{
			boolean last = i==parts.length-1;
			String seg = last?parts[i]+tail: parts[i];
			lines.add(insertAt, seg);
			insertAt++;
		}
		c.line = insertAt-1;
		String lastLine = lines.get(c.line);
		c.pos = Math.max(0, lastLine.length()-tail.length());
		c.anchorLine = c.line;
		c.anchorPos = c.pos;
	}

	private void insertTextAtCaret(TextCaret c, String text)
	{
		String line = lines.get(c.line);
		int allowed = Math.max(0, maxStringLength-line.length());
		String ins = text.length() > allowed?text.substring(0, allowed): text;
		if(ins.isEmpty())
			return;
		lines.set(c.line, line.substring(0, c.pos)+ins+line.substring(c.pos));
		c.pos += ins.length();
		c.anchorLine = c.line;
		c.anchorPos = c.pos;
	}

	//Deletion
	private void deleteCarets(MoveUnit moveUnit, int dir)
	{
		//record state
		pushHistory();
		if(anySelection())
		{
			deleteSelections();
			postEdit();
			return;
		}

		// process carets from end -> start
		List<TextCaret> ordered = new ArrayList<>(carets);
		ordered.sort(Comparator.comparingInt((TextCaret c) -> c.line).thenComparingInt(c -> c.pos).reversed());

		for(TextCaret caret : ordered)
			if(moveUnit==MoveUnit.CHAR)
				//Backspace
				if(dir < 0)
				{
					if(caret.pos > 0)
					{
						String line = lines.get(caret.line);
						lines.set(caret.line, line.substring(0, caret.pos-1)+line.substring(caret.pos));
						caret.pos--;
					}
					else if(caret.line > 0)
					{
						int prevLen = lines.get(caret.line-1).length();
						lines.set(caret.line-1, lines.get(caret.line-1)+lines.get(caret.line));
						lines.remove(caret.line);
						caret.line--;
						caret.pos = prevLen;
					}
					caret.anchorLine = caret.line;
					caret.anchorPos = caret.pos;
				}
				//Delete
				else
				{
					String line = lines.get(caret.line);
					if(caret.pos < line.length())
						lines.set(caret.line, line.substring(0, caret.pos)+line.substring(caret.pos+1));
					else if(caret.line < lines.size()-1)
					{
						lines.set(caret.line, line+lines.get(caret.line+1));
						lines.remove(caret.line+1);
					}
				}
			else if(moveUnit==MoveUnit.WORD)
			{
				int origLine = caret.line, origPos = caret.pos;
				caret.moveHorizontal(dir < 0, MoveUnit.WORD, lines, multiLine, this::isWordChar);
				//Anchor original position to form a selection for deletion afterwards
				caret.anchorLine = origLine;
				caret.anchorPos = origPos;
			}

		deleteSelections();
		postEdit();
	}

	private void newlineAtCarets()
	{
		pushHistory();
		deleteSelections();
		List<TextCaret> ordered = new ArrayList<>(carets);
		ordered.sort((a, b) -> (a.line==b.line?Integer.compare(a.pos, b.pos): Integer.compare(a.line, b.line)));
		Collections.reverse(ordered);
		//Process from bottom to top
		for(TextCaret c : ordered)
		{
			String line = lines.get(c.line);
			String before = line.substring(0, c.pos);
			String after = line.substring(c.pos);
			lines.set(c.line, before);
			lines.add(c.line+1, after);
			c.line++;
			c.pos = 0;
			c.anchorLine = c.line;
			c.anchorPos = 0;
		}
		postEdit();
	}

	private void deleteSelections()
	{
		if(!anySelection()) return;
		List<TextCaret> ordered = new ArrayList<>(carets);
		ordered.sort((a, b) -> {
			if(a.endLine()!=b.endLine()) return Integer.compare(b.endLine(), a.endLine());
			return Integer.compare(b.endPos(), a.endPos());
		});
		for(TextCaret c : ordered) if(c.hasSelection()) deleteCaretSelection(c);
		normalizeCarets();
	}

	private void deleteCaretSelection(TextCaret c)
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
			if(a==b) return;
			lines.set(sL, line.substring(0, a)+line.substring(b));
			c.line = c.anchorLine = sL;
			c.pos = c.anchorPos = a;
		}
		else
		{
			String first = lines.get(sL);
			String last = lines.get(eL);
			int a = Math.min(c.startPos(), first.length());
			int b = Math.min(c.endPos(), last.length());
			if(a > first.length()) a = first.length();
			if(b > last.length()) b = last.length();
			String merged = first.substring(0, a)+last.substring(b);
			for(int i = eL; i >= sL; i--)
				if(i==sL) lines.set(i, merged);
				else lines.remove(i);
			c.line = c.anchorLine = sL;
			c.pos = c.anchorPos = a;
		}
	}

	// --- Unified Caret Movement --- //
	private void moveCarets(MoveUnit unit, int dx, int dy, boolean shift)
	{
		// capture preferred column for vertical navigation
		if(dy!=0&&preferredColumn < 0)
			preferredColumn = primary().pos;
		if(dy==0&&dx!=0)
			preferredColumn = -1; // reset when horizontal only

		boolean allowCross = multiLine; // cross-line wrapping only when multi-line

		for(TextCaret c : carets)
		{
			// vertical first
			if(dy!=0&&multiLine)
			{
				int targetLine = MathHelper.clamp(c.line+dy, 0, lines.size()-1);
				String tgt = lines.get(targetLine);
				int col = (preferredColumn >= 0)?preferredColumn: c.pos;
				c.line = targetLine;
				c.pos = Math.min(col, tgt.length());
			}
			// horizontal / word / end movement via caret helper
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

	// Mouse
	private boolean onMousePressed(DecoTextField self, MouseButton button, int mx, int my)
	{
		if(button!=MouseButton.LEFT)
			return false;

		int innerX = mx-(x+padding);
		int innerY = my-(y+padding);
		int line = multiLine?MathHelper.clamp(verticalScroll+(innerY/fontRenderer.FONT_HEIGHT), 0, lines.size()-1): 0;
		String ln = lines.get(line);
		int pos = fontRenderer.trimStringToWidth(ln, innerX).length();
		boolean shift = GuiScreen.isShiftKeyDown();
		boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU)||Keyboard.isKeyDown(Keyboard.KEY_RMENU);
		if(!shift&&!alt)
		{
			carets.clear();
			carets.add(new TextCaret(line, pos));
		}
		else if(shift)
		{
			TextCaret p = primary();
			p.line = line;
			p.pos = pos;
		}
		else
			carets.add(new TextCaret(line, pos));
		normalizeCarets();
		ensureCursorVisible();
		return true;
	}

	private boolean onMouseDragged(DecoTextField self, MouseButton button, int mx, int my)
	{
		if(button!=MouseButton.LEFT)
			return false;

		int innerX = mx-(x+padding);
		int innerY = my-(y+padding);
		int line = multiLine?MathHelper.clamp(verticalScroll+(innerY/fontRenderer.FONT_HEIGHT), 0, lines.size()-1): 0;
		String ln = lines.get(line);
		int pos = fontRenderer.trimStringToWidth(ln, innerX).length();
		TextCaret p = primary();
		p.line = line;
		p.pos = MathHelper.clamp(pos, 0, ln.length());
		ensureCursorVisible();
		return true;
	}

	private boolean onMouseScroll(DecoTextField self, int wheel, int mx, int my)
	{
		if(!multiLine||!IIMath.isPointInRectangle(x, y, x+width, y+height, mx, my)) return false;
		verticalScroll = MathHelper.clamp(verticalScroll-wheel, 0, maxVerticalScroll);
		return true;
	}

	// Key handling
	private boolean onKeyTyped(DecoTextField f, char ch, int key)
	{
		boolean ctrl = GuiScreen.isCtrlKeyDown();
		boolean shift = GuiScreen.isShiftKeyDown();
		boolean alt = Keyboard.isKeyDown(Keyboard.KEY_LMENU)||Keyboard.isKeyDown(Keyboard.KEY_RMENU);
		if(multiLine&&alt&&(key==Keyboard.KEY_UP||key==Keyboard.KEY_DOWN))
		{
			addCaretVertical(key==Keyboard.KEY_UP?-1: 1);
			return true;
		}
		MoveUnit unit;
		switch(key)
		{
			case Keyboard.KEY_RETURN:
				if(!multiLine)
				{
					parentGui.requestFocus(null);
					if(onTextChanged!=null)
						onTextChanged.accept(getText());
					return true;
				}
				newlineAtCarets();
				return true;
			case Keyboard.KEY_BACK:
				deleteCarets(ctrl?MoveUnit.WORD: MoveUnit.CHAR, -1);
				if(onTextChanged!=null)
					onTextChanged.accept(getText());
				return true;
			case Keyboard.KEY_DELETE:
				deleteCarets(ctrl?MoveUnit.WORD: MoveUnit.CHAR, 1);
				if(onTextChanged!=null)
					onTextChanged.accept(getText());
				return true;
			case Keyboard.KEY_LEFT:
				unit = ctrl?MoveUnit.WORD: MoveUnit.CHAR;
				moveCarets(unit, -1, 0, shift);
				return true;
			case Keyboard.KEY_RIGHT:
				unit = ctrl?MoveUnit.WORD: MoveUnit.CHAR;
				moveCarets(unit, 1, 0, shift);
				return true;
			case Keyboard.KEY_HOME:
				moveCarets(MoveUnit.END, -1, 0, shift);
				return true;
			case Keyboard.KEY_END:
				moveCarets(MoveUnit.END, 1, 0, shift);
				return true;
			case Keyboard.KEY_UP:
				if(multiLine)
				{
					moveCarets(MoveUnit.CHAR, 0, -1, shift);
					return true;
				}
				return false;
			case Keyboard.KEY_DOWN:
				if(multiLine)
				{
					moveCarets(MoveUnit.CHAR, 0, 1, shift);
					return true;
				}
				return false;
			default:
				if(ChatAllowedCharacters.isAllowedCharacter(ch))
				{
					writeText(Character.toString(ch));
					if(onTextChanged!=null)
						onTextChanged.accept(getText());
					return true;
				}
		}
		return false;
	}

	// GUI events
	@Override
	public void onGuiEvent(DecoGuiEvent event)
	{
		switch(event)
		{
			case COPY:
				DecoGuiUtils.setClipboardString(buildCopyString());
				break;
			case CUT:
				if(anySelection())
				{
					pushHistory();
					DecoGuiUtils.setClipboardString(buildCopyString());
					deleteSelections();
					postEdit();
				}
				break;
			case PASTE:
				pasteString(DecoGuiUtils.getClipboardString());
				break;
			case UNDO:
			{
				if(undoStack.isEmpty())
					return;
				redoStack.push(new TextHistoryState(lines, carets));
				TextHistoryState prev = undoStack.pop();
				restore(prev);
			}
			break;
			case REDO:
			{
				if(redoStack.isEmpty())
					return;
				undoStack.push(new TextHistoryState(lines, carets));
				TextHistoryState next = redoStack.pop();
				restore(next);
			}
			break;
			case SELECT_ALL:
				pushHistory();
				if(!multiLine)
				{
					clearToSingleCaret(0, 0);
					TextCaret p = primary();
					p.pos = lines.get(0).length();
					p.anchorPos = 0;
				}
				else
				{
					clearToSingleCaret(lines.size()-1, lines.get(lines.size()-1).length());
					TextCaret p = primary();
					p.anchorLine = 0;
					p.anchorPos = 0;
				}
				ensureCursorVisible();
				break;
			default:
				super.onGuiEvent(event);
		}
	}

	// Selection & caret utilities
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

	//--- History ---//

	private void pushHistory()
	{
		undoStack.push(new TextHistoryState(lines, carets));
		while(undoStack.size() > HISTORY_LIMIT) undoStack.removeLast();
		redoStack.clear();
	}

	private void restore(TextHistoryState st)
	{
		lines.clear();
		lines.addAll(st.lines);
		carets.clear();
		for(TextCaret tc : st.carets)
		{
			TextCaret c = new TextCaret(tc.line, tc.pos);
			c.anchorLine = tc.anchorLine;
			c.anchorPos = tc.anchorPos;
			carets.add(c);
		}
		calculateMaxScroll();
		ensureCursorVisible();
	}

	private void postEdit()
	{
		calculateMaxScroll();
		normalizeCarets();
		ensureCursorVisible();
	}

	// Scroll calc
	private void calculateMaxScroll()
	{
		if(multiLine)
		{
			int visible = (height-(padding*2))/fontRenderer.FONT_HEIGHT;
			maxVerticalScroll = Math.max(0, lines.size()-visible);
			verticalScroll = MathHelper.clamp(verticalScroll, 0, maxVerticalScroll);
		}
	}

	//--- Filtering ---//

	private String filterText(String input)
	{
		if(filter==TextFilter.NONE&&customFilter==null) return input;
		StringBuilder sb = new StringBuilder();
		for(char ch : input.toCharArray())
		{
			String s = String.valueOf(ch);
			if((filter==TextFilter.NONE||filter.test(s))&&(customFilter==null||customFilter.test(s))) sb.append(ch);
		}
		return sb.toString();
	}


	public String getText()
	{
		if(!multiLine)
			return lines.get(0);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < lines.size(); i++)
		{
			sb.append(lines.get(i));
			if(i < lines.size()-1)
				sb.append('\n');
		}
		return sb.toString();
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
		TextCaret p = primary();
		if(multiLine)
		{
			int visibleLines = (height-(padding*2))/fontRenderer.FONT_HEIGHT;
			if(p.line < verticalScroll) verticalScroll = p.line;
			else if(p.line >= verticalScroll+visibleLines) verticalScroll = p.line-visibleLines+1;
		}
		String line = lines.get(p.line);
		int visibleWidth = width-(padding*2);
		if(p.pos < lineScrollOffset) lineScrollOffset = p.pos;
		// scroll right if needed
		while(lineScrollOffset < p.pos)
		{
			int w = fontRenderer.getStringWidth(line.substring(lineScrollOffset, p.pos));
			if(w <= visibleWidth) break;
			lineScrollOffset++;
		}
		cursorCounter = 20;
	}

	@Override
	public void playPressSound(SoundHandler soundHandlerIn)
	{

	}

	// === Clipboard (multi-caret) === //
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

	private void pasteString(String clip)
	{
		if(clip==null) return;
		pushHistory();
		String[] parts = clip.split("\r?\n", -1);
		if(carets.size() > 1&&parts.length==carets.size())
		{
			// map each selection to a part (ascending order), then insert in reverse order to keep positions stable
			List<TextCaret> ordered = new ArrayList<>(carets);
			ordered.sort((a, b) -> (a.line==b.line?Integer.compare(a.pos, b.pos): Integer.compare(a.line, b.line)));
			// delete selections first (in reverse so indices stable)
			List<TextCaret> rev = new ArrayList<>(ordered);
			Collections.reverse(rev);
			for(TextCaret c : rev) if(c.hasSelection()) deleteCaretSelection(c);
			// insert per caret reverse
			Collections.reverse(ordered); // now descending
			for(TextCaret c : ordered)
			{
				int idx = ordered.size()-1-ordered.indexOf(c); // recover ascending index -> part index
				String part = filterText(parts[idx]);
				insertMultilineAtCaret(c, part);
			}
		}
		else
		{
			// same text at all carets, process descending order
			List<TextCaret> ordered = new ArrayList<>(carets);
			ordered.sort((a, b) -> (a.line==b.line?Integer.compare(a.pos, b.pos): Integer.compare(a.line, b.line)));
			Collections.reverse(ordered);
			for(TextCaret c : ordered) if(c.hasSelection()) deleteCaretSelection(c);
			String filtered = filterText(clip);
			for(TextCaret c : ordered) insertMultilineAtCaret(c, filtered);
		}
		postEdit();
	}
}
