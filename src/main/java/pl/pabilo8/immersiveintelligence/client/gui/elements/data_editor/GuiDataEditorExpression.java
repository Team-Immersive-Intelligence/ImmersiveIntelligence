package pl.pabilo8.immersiveintelligence.client.gui.elements.data_editor;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataOperationUtils;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.IGenericDataType;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDataLetterList;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDataLetterList.ArrowsAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDropdownList;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonII;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 07.09.2021
 */
public class GuiDataEditorExpression extends GuiDataEditor<DataTypeExpression>
{
	int page = 0;
	@Nullable
	private GuiDataEditor<? extends DataType> pageEditor;
	@Nonnull
	private final List<String> operations;

	private GuiButtonDropdownList dropdownOperationPicker;
	private GuiButtonDataLetterList dropdownLetterPicker;

	private GuiButtonIE buttonPagePrev, buttonPageNext, buttonPageNumber, buttonUseAccessor, buttonSwitchType;
	private GuiButtonIE buttonTypePrev, buttonTypeNext;
	private boolean hasTypeSwitch;

	private IIColor paramColor = IIColor.WHITE;
	private String paramName;

	public GuiDataEditorExpression(int buttonId, DataTypeExpression dataType, ItemStack circuit)
	{
		super(buttonId, dataType);
		this.operations = IIContent.itemCircuit.getOperationsList(circuit);
	}

	@Override
	public void init()
	{
		super.init();
		validateExpression();
		pageEditor = null;
		buttonUseAccessor = null;

		buttonTypePrev = null;
		buttonTypeNext = null;

		buttonPagePrev = addButton(new GuiButtonIE(buttonList.size(), x+4, y+height-5, 12, 12, "",
				ImmersiveIntelligence.MODID+":textures/gui/arithmetic_logic_machine_editing.png", 96, 234)
				.setHoverOffset(12, 0));
		buttonPageNumber = addButton(new GuiButtonIE(buttonList.size(), x+4+12, y+height-5, 12, 12, String.valueOf(page),
				ImmersiveIntelligence.MODID+":textures/gui/arithmetic_logic_machine_editing.png", 144, 234)
				.setHoverOffset(12, 0));
		buttonPageNext = addButton(new GuiButtonIE(buttonList.size(), x+4+24, y+height-5, 12, 12, "",
				ImmersiveIntelligence.MODID+":textures/gui/arithmetic_logic_machine_editing.png", 120, 234)
				.setHoverOffset(12, 0));

		if(page==0)
		{
			//add the lower-placed buttons first, so rendering (lack of) order is happy
			dropdownLetterPicker = addButton(new GuiButtonDataLetterList(buttonList.size(), x+2, y+2+24+14, true, dataType.getRequiredVariable(), ArrowsAlignment.RIGHT));

			dropdownOperationPicker = addButton(new GuiButtonDropdownList(buttonList.size(), x+2, y+14, width-4, 20, 4, operations.toArray(new String[0])))
					.setTranslationFunc(s -> I18n.format(IIReference.DATA_KEY+"function."+s));
			dropdownOperationPicker.selectedEntry = operations.indexOf(dataType.getMeta().name());
			dropdownOperationPicker.enabled = !operations.isEmpty();
		}
		else
		{
			buttonUseAccessor = addButton(new GuiButtonIE(buttonList.size(), x, y, 12, 12, "@",
					ImmersiveIntelligence.MODID+":textures/gui/emplacement_icons.png", 144, 89)
					.setHoverOffset(12, 0));

			Class<? extends DataType> currentType = dataType.getMeta().allowedTypes()[page-1];
			DataType edited = dataType.getArgument(page-1);
			if(edited instanceof DataTypeExpression) //In case someone really does that: don't
				IILogger.info("Stop doing what you're doing right now! Have some mercy for the bandwidth!");
			else
			{
				if(currentType==DataTypeAccessor.class)
				{
					this.pageEditor = addButton(new GuiDataEditorAccessor(buttonList.size(), ((DataTypeAccessor)edited)));
					buttonUseAccessor.enabled = false;
				}
				else
				{
					for(Entry<Class<? extends DataType>, BiFunction<Integer, DataType, GuiDataEditor<? extends DataType>>> entry : GuiDataEditor.editors.entrySet())
						if(entry.getKey()==edited.getClass())
						{
							this.pageEditor = addButton(entry.getValue().apply(buttonList.size(), edited));
							break;
						}
					if(pageEditor==null)
					{
						this.pageEditor = addButton(new GuiDataEditorAccessor(buttonList.size(),
								edited instanceof DataTypeAccessor?((DataTypeAccessor)edited): new DataTypeAccessor('a')
						));
						buttonUseAccessor.enabled = GuiDataEditor.editors.keySet().stream().anyMatch(currentType::isAssignableFrom);
					}
				}
				if(pageEditor!=null)
				{
					this.pageEditor.setBounds(x, y+12, width, height-12);
					DataType display = new DataPacket().getVarInType(currentType, dataType.data[page-1]);
					paramColor = display.getTypeColor();
					ResourceLocation paramIcon = new ResourceLocation(ImmersiveIntelligence.MODID, String.format("textures/gui/data_types/%s.png", display.getName()));

					hasTypeSwitch = currentType==DataType.class||currentType.isAnnotationPresent(IGenericDataType.class);
					buttonSwitchType = addButton(new GuiButtonII(buttonList.size(), x+width-12-(hasTypeSwitch?7: 0), y, 12, 12, paramIcon.toString(), 0f, 0f, 1f, 1f));

					if(hasTypeSwitch)
					{
						this.buttonTypeNext = addButton(new GuiButtonIE(0, x+width-7, y+1, 6, 4, "",
								ImmersiveIntelligence.MODID+":textures/gui/emplacement_icons.png", 144, 81)
								.setHoverOffset(6, 0));
						this.buttonTypePrev = addButton(new GuiButtonIE(1, x+width-7, y+6, 6, 4, "",
								ImmersiveIntelligence.MODID+":textures/gui/emplacement_icons.png", 144, 81+4)
								.setHoverOffset(6, 0));
					}

				}
			}
			buttonUseAccessor.visible = pageEditor!=null;
			this.paramName = dataType.getMeta().params()!=null?
					dataType.getMeta().params()[(page-1)%dataType.getMeta().params().length]:
					"value";
		}
	}

	private void validateExpression()
	{
		if(operations.isEmpty())
			return;

		DataOperation op;
		if(dataType.getOperation()==null||!operations.contains(dataType.getMeta().name()))
			op = IIDataOperationUtils.getOperationInstance(operations.get(0));
		else
			op = dataType.getOperation();
		dataType.setOperation(op);

		dataType = new DataTypeExpression(dataType.data, op, dataType.getRequiredVariable());
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if(page==0)
		{
			mc.fontRenderer.drawString(I18n.format(IIReference.DESCRIPTION_KEY+"operation"), x+2, y+2, IIReference.COLOR_H2.getPackedRGB(), false);
			mc.fontRenderer.drawString(I18n.format(IIReference.DESCRIPTION_KEY+"conditional_variable"), x+2, y+2+24, IIReference.COLOR_H2.getPackedRGB(), false);
		}
		else
		{
			ClientUtils.bindTexture("immersiveintelligence:textures/gui/emplacement_icons.png");
			drawTexturedModalRect(x+12, y, 181, 142, 12, 12);
			drawTexturedModalRect(x+width-12, y, 205, 142, 12, 12);
			for(int i = 0; i < Math.max(width-24, 0); i += 12)
				drawTexturedModalRect(x+12+i, y, 192, 142, MathHelper.clamp(width-24-i, 0, 12), 12);

			mc.fontRenderer.drawString("Parameter: ", x+2+12, y+2, IIReference.COLOR_H2.getPackedRGB(), false);
			mc.fontRenderer.drawString(TextFormatting.ITALIC+paramName,
					x+width-12-(hasTypeSwitch?7: 0)-mc.fontRenderer.getStringWidth(paramName), y+2, paramColor.withBrightness(0.4f).getPackedRGB(), false);

		}
		super.drawButton(mc, mouseX, mouseY, partialTicks);

	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(dropdownOperationPicker!=null&&dropdownLetterPicker.keyTyped(typedChar, keyCode))
			return;

		if(pageEditor!=null)
			pageEditor.keyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if(page==0)
		{
			if(!dropdownLetterPicker.dropped&&dropdownOperationPicker.mousePressed(mc, mouseX, mouseY))
			{
				if(dropdownOperationPicker.selectedEntry!=-1)
					outputType();
				return true;
			}
			if(!dropdownOperationPicker.dropped&&dropdownLetterPicker.mousePressed(mc, mouseX, mouseY))
			{
				outputType();
				return true;
			}
		}
		else
		{
			if(pageEditor!=null)
				pageEditor.mousePressed(mc, mouseX, mouseY);
			if(buttonUseAccessor!=null&&buttonUseAccessor.mousePressed(mc, mouseX, mouseY))
			{
				outputType();
				if(pageEditor.dataType instanceof DataTypeAccessor)
					dataType.data[page-1] = new DataPacket().getVarInType(dataType.getMeta().allowedTypes()[page-1], null);
				else
					dataType.data[page-1] = new DataTypeAccessor('a');
				init();
				return true;
			}
			if(buttonTypeNext!=null&&(buttonTypeNext.mousePressed(mc, mouseX, mouseY)||buttonTypePrev.mousePressed(mc, mouseX, mouseY)))
			{
				boolean forward = buttonSwitchType.mousePressed(mc, mouseX, mouseY);
				boolean isAny = dataType.getMeta().allowedTypes()[page-1]==DataType.class;
				boolean isGeneric = isAny||dataType.getMeta().allowedTypes()[page-1].isAnnotationPresent(IGenericDataType.class);

				if(isGeneric)
				{
					Class<? extends DataType> dClass = dataType.data[page-1].getClass();

					List<Class<? extends DataType>> collect;

					if(isAny)
						collect = new ArrayList<>(editors.keySet());
					else
					{
						Class<?> sc = dClass.getInterfaces()[0];
						collect = editors.keySet().stream()
								.filter(sc::isAssignableFrom)
								.collect(Collectors.toList());
					}

					if(!collect.isEmpty())
					{
						int i = IIUtils.cycleInt(forward, collect.indexOf(dClass), 0, collect.size()-1);

						dataType.data[page-1] = IIDataTypeUtils.getVarInstance(collect.get(i));
//						dataType.data[page-1].setDefaultValue();

						pageEditor = null;
						outputType();
						init();
					}
					return true;
				}
			}
		}
		if(buttonPageNext.mousePressed(mc, mouseX, mouseY)||
				buttonPagePrev.mousePressed(mc, mouseX, mouseY))
		{
			outputType();
			page = IIUtils.cycleInt(buttonPageNext.isMouseOver(), page, 0, dataType.getMeta().allowedTypes().length);
			init();
			return true;
		}
		if(buttonPageNumber.mousePressed(mc, mouseX, mouseY))
		{
			outputType();
			page = 0;
			init();
			return true;
		}

		return super.mousePressed(mc, mouseX, mouseY);
	}

	@Override
	public DataTypeExpression createType()
	{
		return new DataTypeExpression();
	}

	@Override
	public DataTypeExpression outputType()
	{
		if(page==0)
		{
			if(dropdownOperationPicker.selectedEntry!=-1)
				dataType.setOperation(IIDataOperationUtils.getOperationInstance(operations.get(dropdownOperationPicker.selectedEntry)));
			dataType.setRequiredVariable(dropdownLetterPicker.selectedEntry);
		}
		else if(pageEditor!=null)
			dataType.data[page-1] = pageEditor.outputType();
		validateExpression();
		return dataType;
	}

	@Override
	public boolean isFocused()
	{
		if(pageEditor!=null)
			return pageEditor.isFocused();
		return dropdownLetterPicker!=null&&dropdownLetterPicker.dropped;
	}

	@Override
	public void getTooltip(ArrayList<String> tooltip, int mx, int my)
	{
		if(page==0)
		{

		}
		else if(pageEditor!=null)
			pageEditor.getTooltip(tooltip, mx, my);
	}
}
