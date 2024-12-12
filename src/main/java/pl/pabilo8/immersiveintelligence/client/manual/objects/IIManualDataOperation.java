package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
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
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
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
 * @author Pabilo8
 * @since 02.11.2022
 */
public class IIManualDataOperation extends IIManualObject
{
	private final static ResLoc TEXTURE_IN = ResLoc.of(IIReference.RES_TEXTURES_MANUAL, "data/input").withExtension(ResLoc.EXT_PNG);
	private final static ResLoc TEXTURE_OUT = ResLoc.of(IIReference.RES_TEXTURES_MANUAL, "data/output").withExtension(ResLoc.EXT_PNG);
	private static IIColor colorCodePlainText = IIColor.fromPackedRGB(0xA9B7C6);
	private static IIColor colorCodeKeyword = IIColor.fromPackedRGB(0xCC7832);
	private static IIColor colorCodeVariable = IIColor.fromPackedRGB(0x7e6b80);
	private static IIColor colorCodeString = IIColor.fromPackedRGB(0x6A8759);
	private static IIColor colorCodeComment = IIColor.fromPackedRGB(0x49633f);
	private static IIColor colorCodeNumber = IIColor.fromPackedRGB(0x6897BB);

	private static boolean lastState = false;
	private GuiButtonState codeSwitch;
	private DataOperationMeta dataOperation;
	private String[][] codeSnippets;
	private TypeMetaInfo<?>[][] parametersInfo;
	private TypeMetaInfo<?>[] resultTypes;

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

		int yOffset = y+manual.fontRenderer.FONT_HEIGHT
				+manual.fontRenderer.getWordWrappedHeight(I18n.format("datasystem.immersiveintelligence.function."+dataOperation.name()+".desc"), width)
				-3;
		codeSwitch = new GuiButtonState(0, x+width-16, yOffset, 16, 16, "",
				lastState, IIReference.RES_TEXTURES_GUI.with("manual").withExtension(ResLoc.EXT_PNG).toString(),
				30, 0, 0
		);

		this.resultTypes = getCompatibleTypes(dataOperation.expectedResult());
		parametersInfo = new TypeMetaInfo[dataOperation.params().length][];
		for(int i = 0; i < dataOperation.params().length; i++)
			parametersInfo[i] = getCompatibleTypes(dataOperation.allowedTypes()[i]);
		codeSnippets = new String[resultTypes.length][];
		for(int i = 0; i < resultTypes.length; i++)
			codeSnippets[i] = generateSnippet(resultTypes[i]);
	}

	@Override
	protected int getDefaultHeight()
	{
		return 16;
	}

	@Override
	protected int getDefaultWidth()
	{
		return 120;
	}

	//--- Content Preparation ---//

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
		code.add(colorCodeComment.getHexCol(TextFormatting.ITALIC+";"+I18n.format("ie.manual.entry.data_operation.comment.import")));

		//Operation Import
		boolean importPresent = false;
		for(Circuits value : Circuits.values())
			if(Arrays.stream(value.getFunctions()).anyMatch(s -> s.equals(dataOperation.name())))
			{
				if(importPresent)
					code.add(colorCodeComment.getHexCol(TextFormatting.ITALIC+";"+I18n.format("ie.manual.entry.data_operation.comment.import_more")));
				builder = new StringBuilder()
						.append(colorCodeKeyword.getHexCol("use "))
						.append(colorCodeVariable.getHexCol(value.getName().toUpperCase()));
				code.add(builder.toString());
				importPresent = true;
			}

		//Comment about use cases
		code.add(colorCodeComment.getHexCol(TextFormatting.ITALIC+";"+I18n.format("ie.manual.entry.data_operation.comment.example")));

		//If the result is saved to a variable (a), start from letter b
		final int startFromLetter = dataOperation.resultMatters()?1: 0;

		//Operation example using expression
		if(!dataOperation.expression().isEmpty())
		{
			builder = new StringBuilder();
			if(dataOperation.resultMatters())
				builder.append(colorCodeKeyword.getHexCol(resultingType.name)).append(' ');
			builder.append(colorCodeVariable.getHexCol("a"))
					.append(" = ");
			builder.append(colorCodePlainText.getHexCol(dataOperation.expression()));
			for(int i = 0; i < dataOperation.params().length; i++)
				builder.append(" @").append(DataPacket.varCharacters[startFromLetter+i]);
			code.add(builder.toString());
		}

		//Operation example using name
		builder = new StringBuilder();
		if(dataOperation.resultMatters())
			builder.append(colorCodeKeyword.getHexCol(resultingType.name)).append(' ')
					.append(colorCodeVariable.getHexCol("a"))
					.append(" = ");
		builder.append(colorCodePlainText.getHexCol(dataOperation.name()));
		for(int i = 0; i < dataOperation.params().length; i++)
			builder.append(" @").append(DataPacket.varCharacters[startFromLetter+i]);
		code.add(builder.toString());

		return code.toArray(new String[0]);
	}

	<T> T getArrayElementForTime(T[] parameters)
	{
		float progress = IIAnimationUtils.getDebugProgress(parameters.length*20, 0);
		if(parameters.length==1)
			return parameters[0];
		else
			return parameters[(int)(progress*parameters.length)];
	}

	private int drawVariable(@Nullable TypeMetaInfo<?> typeInfo, boolean inputVariable, String paramName, String paramDesc, int yOffset)
	{
		if(typeInfo==null)
			typeInfo = IIDataTypeUtils.metaTypesByClass.get(DataTypeNull.class);

		GlStateManager.pushMatrix();
		IIClientUtils.bindTexture(typeInfo.getTextureLocation());
		GlStateManager.color(1f, 1f, 1f, 1f);
		GlStateManager.enableBlend();
		Gui.drawModalRectWithCustomSizedTexture(x, yOffset, 0, 0, 16, 16, 16, 16);

		IIClientUtils.bindTexture(inputVariable?TEXTURE_IN: TEXTURE_OUT);
		Gui.drawModalRectWithCustomSizedTexture(x-3, yOffset, 0, 0, 16, 16, 16, 16);
		GlStateManager.popMatrix();

		manual.fontRenderer.setUnicodeFlag(true);
		manual.fontRenderer.drawString(TextFormatting.BOLD+paramName, x+18, yOffset-4, manual.getTextColour());
		manual.fontRenderer.drawSplitString(paramDesc, x+18, yOffset+4, 110, manual.getTextColour());

		return Math.max(20, 8+manual.fontRenderer.getWordWrappedHeight(paramDesc, width));
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);
		codeSwitch.drawButton(mc, mx, my, partialTicks);

		boolean unicode = mc.fontRenderer.getUnicodeFlag();
		int fontHeight = manual.fontRenderer.FONT_HEIGHT;
		int yOffset = y;

		mc.fontRenderer.setUnicodeFlag(true);
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
			yOffset += (int)(fontHeight*2f);

			boolean codeFontUnicode = IIClientUtils.fontRegular.getUnicodeFlag();
			IIClientUtils.fontRegular.setUnicodeFlag(true);
			ClientUtils.drawColouredRect(x-2, yOffset-2, width+4, manual.getGui().height-yOffset-80-12, 0xaa000000);
			for(String codeSnippet : getArrayElementForTime(codeSnippets))
			{
				IIClientUtils.fontRegular.drawString(codeSnippet, x, yOffset, colorCodePlainText.getPackedRGB());
				yOffset += fontHeight;
			}
			IIClientUtils.fontRegular.setUnicodeFlag(codeFontUnicode);
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
		mc.fontRenderer.setUnicodeFlag(unicode);
	}


	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if(codeSwitch.mousePressed(gui.mc, mouseX, mouseY))
		{
			lastState = codeSwitch.state;
			return true;
		}
		return false;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{
	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		return codeSwitch.isMouseOver()?
				Collections.singletonList(I18n.format(codeSwitch.state?"ie.manual.entry.data_operation.tooltip_pol":
						"ie.manual.entry.data_operation.tooltip_data")): null;
	}
}
