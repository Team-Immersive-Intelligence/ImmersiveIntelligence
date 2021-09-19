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
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataOperations;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDataLetterList;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDataLetterList.ArrowsAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.elements.buttons.GuiButtonDropdownList;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiFunction;

/**
 * @author Pabilo8
 * @since 07.09.2021
 */
public class GuiDataEditorExpression extends GuiDataEditor<DataPacketTypeExpression>
{
	int page = 0;
	@Nullable
	private GuiDataEditor<? extends IDataType> pageEditor;
	@Nonnull
	private List<String> operations;

	private GuiButtonDropdownList dropdownOperationPicker;
	private GuiButtonDataLetterList dropdownLetterPicker;

	private GuiButtonIE buttonPagePrev, buttonPageNext, buttonPageNumber, buttonUseAccessor;

	private int paramColor = 0;
	private ResourceLocation paramIcon;
	private String paramName = "value";

	public GuiDataEditorExpression(int buttonId, DataPacketTypeExpression dataType, ItemStack circuit)
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
					.setTranslationFunc(s -> I18n.format(CommonProxy.DATA_KEY+"function."+s));
			dropdownOperationPicker.selectedEntry = operations.indexOf(dataType.getOperation().name);
			dropdownOperationPicker.enabled = !operations.isEmpty();
		}
		else
		{
			buttonUseAccessor = addButton(new GuiButtonIE(buttonList.size(), x, y, 12, 12, "@",
					ImmersiveIntelligence.MODID+":textures/gui/emplacement_icons.png", 144, 89)
					.setHoverOffset(12, 0));

			IDataType edited = page==1?dataType.getType1(): dataType.getType2();
			if(edited instanceof GuiDataEditorExpression) //In case someone really does that: don't
				ImmersiveIntelligence.logger.info("Stop doing what you're doing right now! Have some mercy for the bandwidth!");
			else
			{
				if((page==1?dataType.getOperation().allowedType1: dataType.getOperation().allowedType2)==DataPacketTypeAccessor.class)
				{
					this.pageEditor = addButton(new GuiDataEditorAccessor(buttonList.size(), ((DataPacketTypeAccessor)edited)));
					buttonUseAccessor.enabled = false;
				}
				else
				{
					for(Entry<Class<? extends IDataType>, BiFunction<Integer, IDataType, GuiDataEditor<? extends IDataType>>> entry : GuiDataEditor.editors.entrySet())
					{
						if(entry.getKey()==edited.getClass())
						{
							this.pageEditor = addButton(entry.getValue().apply(buttonList.size(), edited));
							break;
						}
					}
					if(pageEditor==null)
					{
						this.pageEditor = addButton(new GuiDataEditorAccessor(buttonList.size(),
								edited instanceof DataPacketTypeAccessor?((DataPacketTypeAccessor)edited): new DataPacketTypeAccessor('a')
						));
						buttonUseAccessor.enabled = GuiDataEditor.editors.keySet().stream().anyMatch(c -> c==(page==1?dataType.getOperation().allowedType1: dataType.getOperation().allowedType2));
					}
				}
				if(pageEditor!=null)
				{
					this.pageEditor.setBounds(x, y+12, width, height-12);
					IDataType display = DataOperator.getVarInType(
							(Class<? extends IDataType>)(this.page==1?this.dataType.getOperation().allowedType1: this.dataType.getOperation().allowedType2),
							null, new DataPacket());
					paramColor = display.getTypeColour();
					paramIcon = new ResourceLocation(ImmersiveIntelligence.MODID, String.format("textures/gui/data_types/%s.png", display.getName()));

				}
			}
			buttonUseAccessor.visible = pageEditor!=null;
		}
	}

	private void validateExpression()
	{
		if(operations.isEmpty()) //
			return;

		if(dataType.getOperation()==null)
		{
			dataType.setDefaultValue();
		}

		DataOperator operation = dataType.getOperation();
		DataOperator op;
		if(operation==null||!operations.contains(operation.name))
			op = DataOperations.getOperatorInstance(operations.get(0));
		else
			op = dataType.getOperation();

		IDataType t1 = dataType.getType1(), t2 = dataType.getType2();
		if(!(t1.getClass()==DataPacketTypeAccessor.class||t1.getClass()==op.allowedType1))
			t1 = DataOperator.getVarInType(op.allowedType1, dataType.getType1(), null);

		if(!(t2.getClass()==DataPacketTypeAccessor.class||t2.getClass()==op.allowedType2))
			t2 = DataOperator.getVarInType(op.allowedType2, dataType.getType2(), null);

		dataType = new DataPacketTypeExpression(t1, t2, op, dataType.getRequiredVariable());
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if(page==0)
		{
			mc.fontRenderer.drawString(I18n.format(CommonProxy.DESCRIPTION_KEY+"operation"), x+2, y+2, Utils.COLOR_H2, false);
			mc.fontRenderer.drawString(I18n.format(CommonProxy.DESCRIPTION_KEY+"conditional_variable"), x+2, y+2+24, Utils.COLOR_H2, false);
		}
		else
		{
			ClientUtils.bindTexture("immersiveintelligence:textures/gui/emplacement_icons.png");
			drawTexturedModalRect(x+12, y, 181, 142, 12, 12);
			drawTexturedModalRect(x+width-12, y, 205, 142, 12, 12);
			for(int i = 0; i < Math.max(width-24, 0); i += 12)
				drawTexturedModalRect(x+12+i, y, 192, 142, MathHelper.clamp(width-24-i, 0, 12), 12);

			Utils.bindTexture(this.paramIcon);
			ClientUtils.drawTexturedRect(x+width-12, y,12,12,0d,1d,0d,1d);

			mc.fontRenderer.drawString("Parameter: ", x+2+12, y+2, Utils.COLOR_H2, false);
			mc.fontRenderer.drawString(TextFormatting.ITALIC+paramName,
					x+width-12-mc.fontRenderer.getStringWidth(paramName), y+2, paramColor, false);

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
				if(pageEditor.dataType instanceof DataPacketTypeAccessor)
				{
					if(page==1)
					{
						dataType.type1 = DataOperator.getVarInType(dataType.getOperation().allowedType1, null, new DataPacket());
						dataType.type1.setDefaultValue();
					}
					else
					{
						dataType.type2 = DataOperator.getVarInType(dataType.getOperation().allowedType2, null, new DataPacket());
						dataType.type2.setDefaultValue();
					}
				}
				else
				{
					if(page==1)
						dataType.type1 = new DataPacketTypeAccessor('a');
					else
						dataType.type2 = new DataPacketTypeAccessor('a');
				}
				init();
			}
		}
		if(buttonPageNext.mousePressed(mc, mouseX, mouseY)||
				buttonPagePrev.mousePressed(mc, mouseX, mouseY))
		{
			outputType();
			page = Utils.cycleInt(buttonPageNext.isMouseOver(), page, 0, 2);
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
	public DataPacketTypeExpression createType()
	{
		return new DataPacketTypeExpression();
	}

	@Override
	public DataPacketTypeExpression outputType()
	{
		if(page==0)
		{
			if(dropdownOperationPicker.selectedEntry!=-1)
				dataType.setOperation(DataOperations.getOperatorInstance(operations.get(dropdownOperationPicker.selectedEntry)));
			dataType.setRequiredVariable(dropdownLetterPicker.selectedEntry);
		}
		else if(pageEditor!=null)
		{
			if(page==1)
				dataType.type1 = pageEditor.outputType();
			else
				dataType.type2 = pageEditor.outputType();

		}
		validateExpression();
		return dataType;
	}

	@Override
	public boolean isFocused()
	{
		if(pageEditor!=null)
			return pageEditor.isFocused();
		if(dropdownLetterPicker!=null&&dropdownLetterPicker.dropped)
			return true;
		return false;
	}
}
