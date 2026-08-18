package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataOperationUtils;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationNull;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.IGenericDataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.highlight.POLHighlighter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.highlight.TextHighlighter.Segment;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.AMTUtils;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit.Circuits;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Displays a data operation reference with parameters, result information, and a POL example.
 *
 * @author Pabilo8(pabilo@iiteam.net)
 * @updated 10.08.2026
 * @since 02.11.2022
 */
public class IIManualDataOperation extends IIManualObject
{
	private static final IIColor COLOR_CODE_BACKGROUND = IIColor.fromPackedARGB(0xAA000000L);
	private static final IIColor COLOR_SCROLLBAR_TRACK = IIColor.fromPackedARGB(0x663C3F41L);
	private static final IIColor COLOR_SCROLLBAR_THUMB = IIColor.fromPackedARGB(0xAAA9B7C6L);
	private static final POLHighlighter POL_HIGHLIGHTER = new POLHighlighter();

	private static final int VARIABLE_TEXT_OFFSET = 18;
	private static final int SCROLLBAR_HEIGHT = 4;
	private static final int SCROLLBAR_GAP = 2;
	private static final int SCROLLBAR_MIN_THUMB_WIDTH = 12;

	private static boolean lastState = false;
	private GuiButtonState codeSwitch;
	private DataOperationMeta dataOperation;
	private List<List<Segment>>[] highlightedCodeCache;
	private TypeMetaInfo<?>[][] parametersInfo;
	private TypeMetaInfo<?>[] resultTypes;
	private int codeWidth;
	private int codeLineCount;
	private int codeScrollbarY;
	private int codeScroll;

	//--- Setup ---//

	public IIManualDataOperation(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);

		dataOperation = IIDataOperationUtils.getOperationMeta(dataSource.getString("id"));
		if(dataOperation==null)
			dataOperation = DataOperationNull.INSTANCE_META;

		this.resultTypes = getCompatibleTypes(dataOperation.expectedResult());
		parametersInfo = new TypeMetaInfo[dataOperation.params().length][];
		for(int i = 0; i < dataOperation.params().length; i++)
			parametersInfo[i] = getCompatibleTypes(dataOperation.allowedTypes()[i]);
		//noinspection unchecked
		highlightedCodeCache = new List[resultTypes.length];
		for(int i = 0; i < resultTypes.length; i++)
			highlightedCodeCache[i] = highlightSnippet(generateSnippet(resultTypes[i]));

		boolean manualUnicode = manual.fontRenderer.getUnicodeFlag();
		boolean codeUnicode = IIClientUtils.fontRegular.getUnicodeFlag();
		manual.fontRenderer.setUnicodeFlag(true);
		IIClientUtils.fontRegular.setUnicodeFlag(true);

		codeWidth = getCodeWidth();
		codeLineCount = Arrays.stream(highlightedCodeCache).mapToInt(List::size).max().orElse(0);

		int headerHeight = getHeaderHeight();
		codeSwitch = new GuiButtonState(0, x+width-16, y+headerHeight-3, 16, 16, "",
				lastState, IIReference.RES_TEXTURES_GUI.with("manual").withExtension(ResLoc.EXT_PNG).toString(),
				30, 0, 0
		);

		int codeTop = y+headerHeight+manual.fontRenderer.FONT_HEIGHT*2;
		codeScrollbarY = codeTop+codeLineCount*IIClientUtils.fontRegular.FONT_HEIGHT+SCROLLBAR_GAP;
		height = Math.max(height, Math.max(getDataHeight(headerHeight), getCodeHeight(headerHeight)));

		manual.fontRenderer.setUnicodeFlag(manualUnicode);
		IIClientUtils.fontRegular.setUnicodeFlag(codeUnicode);
	}

	@Override
	protected int getDefaultHeight()
	{
		return 16;
	}

	//--- Content Preparation ---//

	private int getHeaderHeight()
	{
		String description = I18n.format("datasystem.immersiveintelligence.function."+dataOperation.name()+".desc");
		return manual.fontRenderer.FONT_HEIGHT+manual.fontRenderer.getWordWrappedHeight(description, width);
	}

	private int getDataHeight(int headerHeight)
	{
		int result = headerHeight;
		int fontHeight = manual.fontRenderer.FONT_HEIGHT;

		if(dataOperation.params().length > 0)
		{
			result += fontHeight+2;
			for(String param : dataOperation.params())
				result += getVariableHeight(I18n.format(
						"datasystem.immersiveintelligence.function."+dataOperation.name()+".param."+param+".desc"
				));
		}

		if(dataOperation.resultMatters())
		{
			result += fontHeight+2;
			result += getVariableHeight(I18n.format(
					"datasystem.immersiveintelligence.function."+dataOperation.name()+".result.desc"
			));
		}

		return result;
	}

	private int getCodeHeight(int headerHeight)
	{
		int codeHeight = codeLineCount*IIClientUtils.fontRegular.FONT_HEIGHT+4;
		if(hasHorizontalScroll())
			codeHeight += SCROLLBAR_GAP+SCROLLBAR_HEIGHT;
		return headerHeight+manual.fontRenderer.FONT_HEIGHT*2+codeHeight;
	}

	private int getVariableTextWidth()
	{
		return Math.max(1, width-VARIABLE_TEXT_OFFSET);
	}

	private int getVariableHeight(String description)
	{
		return Math.max(20, 8+manual.fontRenderer.getWordWrappedHeight(description, getVariableTextWidth()));
	}

	private int getCodeWidth()
	{
		return Arrays.stream(highlightedCodeCache)
				.flatMap(List::stream)
				.mapToInt(this::getHighlightedLineWidth)
				.max()
				.orElse(0);
	}

	private int getHighlightedLineWidth(List<Segment> line)
	{
		return line.stream()
				.mapToInt(segment -> IIClientUtils.fontRegular.getStringWidth(getFormattedSegmentText(segment)))
				.sum();
	}

	private boolean hasHorizontalScroll()
	{
		return codeWidth > width;
	}

	private int getMaxCodeScroll()
	{
		return Math.max(0, codeWidth-width);
	}

	private int getScrollbarThumbWidth()
	{
		if(!hasHorizontalScroll())
			return width;
		return Math.max(SCROLLBAR_MIN_THUMB_WIDTH, width*width/codeWidth);
	}

	private void updateCodeScroll(int mouseX)
	{
		int thumbWidth = getScrollbarThumbWidth();
		int trackRange = width-thumbWidth;
		if(trackRange <= 0)
		{
			codeScroll = 0;
			return;
		}

		float progress = (mouseX-x-thumbWidth*0.5f)/trackRange;
		progress = MathHelper.clamp(progress, 0, 1);
		codeScroll = Math.round(progress*getMaxCodeScroll());
	}

	private boolean isMouseOverScrollbar(int mouseX, int mouseY)
	{
		return codeSwitch.state&&hasHorizontalScroll()
				&&mouseX >= x&&mouseX < x+width
				&&mouseY >= codeScrollbarY&&mouseY < codeScrollbarY+SCROLLBAR_HEIGHT;
	}

	private TypeMetaInfo<?>[] getCompatibleTypes(Class<? extends DataType> type)
	{
		//Allowing all types
		if(type==DataType.class)
			return IIDataTypeUtils.metaTypesByClass.values().toArray(new TypeMetaInfo[0]);
		else //Allowing only specific types
		{
			if(type.isAnnotationPresent(IGenericDataType.class))
				return IIDataTypeUtils.metaTypesByClass.keySet().stream()
						.filter(type::isAssignableFrom)
						.filter(t -> t!=type)
						.map(IIDataTypeUtils.metaTypesByClass::get)
						.toArray(TypeMetaInfo[]::new);
			else
				return new TypeMetaInfo[]{IIDataTypeUtils.metaTypesByClass.get(type)};

		}
	}

	private String[] generateSnippet(TypeMetaInfo<?> resultingType)
	{
		StringBuilder builder;
		ArrayList<String> code = new ArrayList<>();

		//Comment about importing
		code.add(";"+I18n.format("ie.manual.entry.data_operation.comment.import"));

		//Operation Import
		boolean importPresent = false;
		for(Circuits value : Circuits.values())
			if(Arrays.stream(value.getFunctions()).anyMatch(s -> s.equals(dataOperation.name())))
			{
				if(importPresent)
					code.add(";"+I18n.format("ie.manual.entry.data_operation.comment.import_more"));
				code.add("use "+value.getName().toUpperCase());
				importPresent = true;
			}

		//Comment about use cases
		code.add(";"+I18n.format("ie.manual.entry.data_operation.comment.example"));

		//If the result is saved to a variable (a), start from letter b
		final int startFromLetter = dataOperation.resultMatters()?1: 0;

		//Operation example using expression
		if(!dataOperation.expression().isEmpty())
		{
			builder = new StringBuilder();
			if(dataOperation.resultMatters())
				builder.append(resultingType.name).append(" a = ");
			builder.append(dataOperation.expression());
			for(int i = 0; i < dataOperation.params().length; i++)
				builder.append(" @").append(DataPacket.VARIABLE_NAMES[startFromLetter+i]);
			code.add(builder.toString());
		}

		//Operation example using name
		builder = new StringBuilder();
		if(dataOperation.resultMatters())
			builder.append(resultingType.name).append(" a = ");
		builder.append(dataOperation.name());
		for(int i = 0; i < dataOperation.params().length; i++)
			builder.append(" @").append(DataPacket.VARIABLE_NAMES[startFromLetter+i]);
		code.add(builder.toString());

		return code.toArray(new String[0]);
	}

	private List<List<Segment>> highlightSnippet(String[] snippet)
	{
		ArrayList<List<Segment>> highlighted = new ArrayList<>(snippet.length);
		for(String line : snippet)
			highlighted.add(Collections.unmodifiableList(POL_HIGHLIGHTER.highlight(line)));
		return Collections.unmodifiableList(highlighted);
	}

	private String getFormattedSegmentText(Segment segment)
	{
		if(!segment.bold&&!segment.italic)
			return segment.text;
		if(segment.bold&&segment.italic)
			return TextFormatting.BOLD.toString()+TextFormatting.ITALIC+segment.text;
		return (segment.bold?TextFormatting.BOLD: TextFormatting.ITALIC)+segment.text;
	}

	<T> T getArrayElementForTime(T[] parameters)
	{
		float progress = AMTUtils.getDebugProgress(parameters.length*20, 0);
		if(parameters.length==1)
			return parameters[0];
		else
			return parameters[(int)(progress*parameters.length)];
	}

	private int drawVariable(@Nullable TypeMetaInfo<?> typeInfo, boolean inputVariable, String paramName, String paramDesc, int yOffset)
	{
		if(typeInfo==null)
			typeInfo = IIDataTypeUtils.metaTypesByClass.get(DataTypeNull.class);

		GlStateManager.color(1, 1, 1, 1);
		ClientUtils.bindAtlas();
		TextureAtlasSprite typeTexture = ClientUtils.getSprite(typeInfo.getTextureLocation());
		TextureAtlasSprite callbackTexture = ClientUtils.getSprite(inputVariable?IIReference.RES_CONTEXT_DATA_IN: IIReference.RES_CONTEXT_DATA_OUT);
		IIDrawUtils.startTextured()
				.drawTexRect(x, yOffset, 16, 16,
						typeTexture.getMinU(), typeTexture.getMaxU(), typeTexture.getMinV(), typeTexture.getMaxV())
				.drawTexRect(x-3, yOffset, 16, 16,
						callbackTexture.getMinU(), callbackTexture.getMaxU(), callbackTexture.getMinV(), callbackTexture.getMaxV())
				.finish();

		manual.fontRenderer.setUnicodeFlag(true);
		manual.fontRenderer.drawString(TextFormatting.BOLD+paramName, x+VARIABLE_TEXT_OFFSET, yOffset-4, manual.getTextColour());
		manual.fontRenderer.drawSplitString(paramDesc, x+VARIABLE_TEXT_OFFSET, yOffset+4, getVariableTextWidth(), manual.getTextColour());

		return getVariableHeight(paramDesc);
	}

	//--- Rendering, Reaction ---//

	private void drawCodeSnippet(Minecraft mc, int yOffset)
	{
		List<List<Segment>> snippet = getArrayElementForTime(highlightedCodeCache);
		int codeFontHeight = IIClientUtils.fontRegular.FONT_HEIGHT;
		int codeTextHeight = codeLineCount*codeFontHeight;
		int panelHeight = codeTextHeight+4+(hasHorizontalScroll()?SCROLLBAR_GAP+SCROLLBAR_HEIGHT: 0);

		boolean codeFontUnicode = IIClientUtils.fontRegular.getUnicodeFlag();
		IIClientUtils.fontRegular.setUnicodeFlag(true);
		drawCodePanel(yOffset, panelHeight);

		GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		ScaledResolution resolution = new ScaledResolution(mc);
		int scale = resolution.getScaleFactor();
		GL11.glScissor(
				x*scale,
				mc.displayHeight-(yOffset+codeTextHeight)*scale,
				width*scale,
				codeTextHeight*scale
		);

		int lineY = yOffset;
		for(List<Segment> line : snippet)
		{
			drawHighlightedLine(line, x-codeScroll, lineY);
			lineY += codeFontHeight;
		}
		GL11.glPopAttrib();

		IIClientUtils.fontRegular.setUnicodeFlag(codeFontUnicode);
	}

	private void drawCodePanel(int yOffset, int panelHeight)
	{
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		IIDrawUtils draw = IIDrawUtils.startColored()
				.drawColorRect(x-2, yOffset-2, width+4, panelHeight, COLOR_CODE_BACKGROUND);

		if(hasHorizontalScroll())
		{
			int thumbWidth = getScrollbarThumbWidth();
			int thumbRange = width-thumbWidth;
			int thumbX = x;
			if(getMaxCodeScroll() > 0)
				thumbX += Math.round(thumbRange*(codeScroll/(float)getMaxCodeScroll()));

			draw.drawColorRect(x, codeScrollbarY, width, SCROLLBAR_HEIGHT, COLOR_SCROLLBAR_TRACK)
					.drawColorRect(thumbX, codeScrollbarY, thumbWidth, SCROLLBAR_HEIGHT, COLOR_SCROLLBAR_THUMB);
		}

		draw.finish();
		GlStateManager.enableTexture2D();
	}

	private void drawHighlightedLine(List<Segment> line, int xOffset, int yOffset)
	{
		for(Segment segment : line)
		{
			String text = getFormattedSegmentText(segment);
			IIClientUtils.fontRegular.drawString(text, xOffset, yOffset, segment.color.getPackedRGB());
			xOffset += IIClientUtils.fontRegular.getStringWidth(text);
		}
	}

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);
		codeSwitch.drawButton(mc, mx, my, partialTicks);

		boolean unicode = manual.fontRenderer.getUnicodeFlag();
		int fontHeight = manual.fontRenderer.FONT_HEIGHT;
		int yOffset = y;

		manual.fontRenderer.setUnicodeFlag(true);
		GlStateManager.pushMatrix();

		String title = I18n.format("datasystem.immersiveintelligence.function."+dataOperation.name());
		String description = I18n.format("datasystem.immersiveintelligence.function."+dataOperation.name()+".desc");
		IIClientUtils.drawStringCentered(manual.fontRenderer, TextFormatting.BOLD+title, x, yOffset, width, 0, manual.getTextColour());
		yOffset += fontHeight;
		manual.fontRenderer.drawSplitString(description, x, yOffset, width, manual.getTextColour());
		yOffset += manual.fontRenderer.getWordWrappedHeight(description, width);

		if(codeSwitch.state)
		{
			//POL code
			String polCode = I18n.format("ie.manual.entry.data_operation.pol");
			IIClientUtils.drawStringCentered(manual.fontRenderer, TextFormatting.BOLD+polCode, x, yOffset, width, 0, manual.getTextColour());
			yOffset += fontHeight*2;

			drawCodeSnippet(mc, yOffset);
		}
		else
		{
			String inputs = I18n.format("ie.manual.entry.data_operation.inputs");
			String output = I18n.format("ie.manual.entry.data_operation.output");

			//inputs
			if(dataOperation.params().length > 0)
			{
				IIClientUtils.drawStringCentered(manual.fontRenderer, TextFormatting.BOLD+inputs, x, yOffset, width, 0, manual.getTextColour());
				yOffset += fontHeight+2;

				String[] params = dataOperation.params();
				for(int i = 0; i < params.length; i++)
				{
					String param = params[i];
					yOffset += drawVariable(getArrayElementForTime(parametersInfo[i]), true,
							I18n.format("datasystem.immersiveintelligence.function."+dataOperation.name()+".param."+param),
							I18n.format("datasystem.immersiveintelligence.function."+dataOperation.name()+".param."+param+".desc"),
							yOffset);
				}
			}

			if(dataOperation.resultMatters())
			{
				//outputs
				IIClientUtils.drawStringCentered(manual.fontRenderer, TextFormatting.BOLD+output, x, yOffset, width, 0, manual.getTextColour());
				yOffset += fontHeight+2;
				drawVariable(getArrayElementForTime(resultTypes), false,
						I18n.format("datasystem.immersiveintelligence.function."+dataOperation.name()+".result"),
						I18n.format("datasystem.immersiveintelligence.function."+dataOperation.name()+".result.desc"),
						yOffset);
			}
		}

		GlStateManager.popMatrix();
		manual.fontRenderer.setUnicodeFlag(unicode);
	}


	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if(codeSwitch.mousePressed(gui.mc, mouseX, mouseY))
		{
			lastState = codeSwitch.state;
			return true;
		}
		if(isMouseOverScrollbar(mouseX, mouseY))
		{
			updateCodeScroll(mouseX);
			return true;
		}
		return false;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{
		if(button==0&&isMouseOverScrollbar(x, y))
			updateCodeScroll(x);
	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		return codeSwitch.isMouseOver()?
				Collections.singletonList(I18n.format(codeSwitch.state?"ie.manual.entry.data_operation.tooltip_data":
													  "ie.manual.entry.data_operation.tooltip_pol")): null;
	}
}
