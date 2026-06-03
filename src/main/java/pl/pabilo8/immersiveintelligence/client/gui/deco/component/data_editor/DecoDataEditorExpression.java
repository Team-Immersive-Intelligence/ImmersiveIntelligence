package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.*;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoColors;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplates;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nullable;

/**
 * Visual editor for {@link DataTypeExpression} used by the Arithmetic-Logic Machine.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorExpression extends DecoDataEditor<DataTypeExpression>
{
	private int page = 0;
	@Nullable
	private DecoPanel editor;
	@Nullable
	private DecoDataEditor<? extends DataType> argumentEditor;
	@Nullable
	private DecoDropdownDataLetters argumentAccessorDropdown;
	@Nullable
	private DecoDropdownDataLetters conditionDropdown;
	private boolean conditionEnabled;

	public DecoDataEditorExpression(int x, int y, DataTypeExpression dataType)
	{
		super(x, y, dataType);
	}

	@Override
	protected boolean initialize()
	{
		cleanup();
		if(!super.initialize())
			return false;

		DataOperationMeta meta = dataType.getMeta();
		ensureArgumentArray(meta);
		if(page < 0||page > meta.params().length)
			page = 0;

		addComponent(buildTabs(meta));

		if(page==0)
			initializePropertiesPage();
		else
			initializeArgumentPage(meta, page-1);

		return true;
	}

	private DecoTabGroup buildTabs(DataOperationMeta meta)
	{
		DecoTabGroup group = new DecoTabGroup(0, 4)
				.withSize(width, 12)
				.withHorizontalAlignment(true)
				.withTab((DecoTab)new DecoTab()
						.withText(IIReference.DESCRIPTION_KEY+"variable_properties")
						.withPadding(4, 2, 4, 2)
						.withOnPressed((gui, button, mouseX, mouseY) -> setPage(0))
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"variable_properties.tooltip")
				);

		String[] params = meta.params();
		for(int paramID = 0; paramID < params.length; paramID++)
		{
			int finalParamID = paramID+1;
			group.withTab((DecoTab)new DecoTab()
					.withText("datasystem.immersiveintelligence.function."+meta.name()+".param."+params[paramID])
					.withPadding(4, 2, 4, 2)
					.withOnPressed((gui, button, mouseX, mouseY) -> setPage(finalParamID))
			);
		}
		return group;
	}

	private void initializePropertiesPage()
	{
		this.editor = addComponent(new DecoPanel(0, 16))
				.withSize(width, height-16)
				.withBackground(null)
				.withBackgroundMask(null);

		assert editor!=null;

		conditionEnabled = dataType.getRequiredVariable()!=' ';

		DecoPanel basePanel = editor.addComponent(new DecoPanel(0, 0)
				.withSize(width, 36)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
		);

		basePanel.addLabel(IIReference.DESCRIPTION_KEY+"conditional_variable", 4, 5)
				.withSize(width-8, 10)
				.withAlign(DecoAlignment.LEFT)
				.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"conditional_variable.enabled.tooltip");

		editor.addComponent(new DecoSwitch(6, 18)
						.withText(IIReference.DESCRIPTION_KEY+"conditional_variable.enabled")
						.withCurrentState(conditionEnabled)
						.withOnToggle(state -> {
							conditionEnabled = state;
							if(conditionDropdown!=null)
								conditionDropdown.visible = conditionDropdown.enabled = state;
						})
				).withSize(72, 12)
				.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"conditional_variable.enabled.tooltip");

		editor.addComponent((conditionDropdown = new DecoDropdownDataLetters(width-26, 16-4))
				.withConstraints(new DataPacket())
				.withSelectedEntry((Character)(conditionEnabled?dataType.getRequiredVariable(): 'a'))
				.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"conditional_variable.tooltip")
				.withTextColor(DecoColors.H1, IIColor.fromPackedRGB(0x35322c))
				.withBackground(DecoTextures.COMPONENT_DROPDOWN_DATA_LETTER_PAPER)
				.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
		);
		conditionDropdown.withSize(18, 18);
		conditionDropdown.visible = conditionDropdown.enabled = conditionEnabled;
	}

	private void initializeArgumentPage(DataOperationMeta meta, int argumentID)
	{
		Class<? extends DataType> expectedType = meta.allowedTypes()[argumentID];
		DataType argument = getArgument(argumentID, expectedType);
		boolean accessor = argument instanceof DataTypeAccessor;

		this.editor = addComponent(new DecoPanel(0, 16))
				.withSize(width, height-16)
				.withBackground(null)
				.withBackgroundMask(null);

		TypeMetaInfo<?> accessorMeta = IIDataTypeUtils.metaTypesByClass.get(DataTypeAccessor.class);
		TypeMetaInfo<?> expectedMeta = IIDataTypeUtils.metaTypesByClass.get(expectedType);

		DecoPanel paperPanel;
		editor.addComponents(
				paperPanel = new DecoPanel(0, 0)
						.withSize(width, 24)
						.withBackground(DecoTextures.BG_PAPER)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER),
				new DecoDropdown<TypeMetaInfo<?>>(34, 3)
						.withSize(width-34-22, 16)
						.withDropdownWidth(width-16)
						.withMaxDisplayedEntries(4)
						.withScrollBarBackground(DecoTextures.COMPONENT_SLIDER_PAPER)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withListBackground(DecoTextures.COMPONENT_TEXT_FIELD)
						.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
						.withEntries(accessorMeta, expectedMeta)
						.withSelectedEntry(accessor?accessorMeta: expectedMeta)
						.withDisplayFunction(DecoTemplates.getDataTypeEntryDisplay())
						.withOnSelectedEntry((oldType, newType) -> {
							storeCurrentPageOutput();
							if(newType!=null&&newType.type==DataTypeAccessor.class)
								setArgument(argumentID, new DataTypeAccessor(getCurrentAccessorVariable(argument)));
							else
								setArgument(argumentID, normalizeArgument(argument, expectedType));
							refreshPage();
						}),
				new DecoButton(width-20, 3)
						.withText("@")
						.withSize(16, 16)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"expression.accessor.tooltip")
						.withOnLMBPressed(() -> {
							storeCurrentPageOutput();
							DataType current = getArgument(argumentID, expectedType);
							if(current instanceof DataTypeAccessor)
								setArgument(argumentID, normalizeArgument(current, expectedType));
							else
								setArgument(argumentID, new DataTypeAccessor('a'));
							refreshPage();
						})
		);
		paperPanel.addLabel(IIReference.DESCRIPTION_KEY+"variable_type", 4, 5)
				.withSize(30, 16)
				.withAlign(DecoAlignment.LEFT);

		if(accessor)
			initializeAccessorArgument(argument);
		else
			initializeLiteralArgument(argument, expectedType);
	}

	private void initializeAccessorArgument(DataType argument)
	{
		assert editor!=null;
		editor.addLabel("datasystem.immersiveintelligence.datatype.accessor", 4, 30)
				.withSize(60, 16)
				.withAlign(DecoAlignment.LEFT);
		editor.addComponent(
				(argumentAccessorDropdown = new DecoDropdownDataLetters(64, 27))
						.withConstraints(new DataPacket())
						.withSelectedEntry((Character)getCurrentAccessorVariable(argument))
						.withTranslatedTooltip(IIReference.DESCRIPTION_KEY+"expression.accessor.tooltip")
						.withTextColor(IIColor.fromPackedRGB(0x161c26), IIColor.fromPackedRGB(0x35322c))
						.withBackground(DecoTextures.COMPONENT_DROPDOWN_DATA_LETTER_PAPER)
						.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
		);
		argumentAccessorDropdown.withSize(18, 18);
	}

	private void initializeLiteralArgument(DataType argument, Class<? extends DataType> expectedType)
	{
		argumentEditor = DecoDataEditor.getEditorFor(argument, 4, 27);
		assert editor!=null;
		if(argumentEditor!=null)
			editor.addComponent(argumentEditor)
					.withSize(width-8, height-43);
		else
			editor.addLabel(IIReference.DESCRIPTION_KEY+"no_editor", 4, 30)
					.withSize(width-8, 16)
					.withAlign(DecoAlignment.LEFT)
					.withTextColor(IIDataTypeUtils.metaTypesByClass.get(expectedType).color.withBrightness(0.4f));
	}

	//--- Utils ---//

	private boolean setPage(int page)
	{
		if(page==this.page)
			return false;

		storeCurrentPageOutput();
		this.page = page;
		refreshPage();
		return true;
	}

	private void refreshPage()
	{
		this.argumentEditor = null;
		this.argumentAccessorDropdown = null;
		this.conditionDropdown = null;
		this.initialized = false;
	}

	private void storeCurrentPageOutput()
	{
		if(page==0)
		{
			if(conditionDropdown!=null)
			{
				Character selected = conditionDropdown.getSelectedEntry();
				dataType.setRequiredVariable(conditionEnabled?(selected==null?'a': selected): ' ');
			}
			else if(!conditionEnabled)
				dataType.setRequiredVariable(' ');
			return;
		}

		int argumentID = page-1;
		if(dataType.data==null||argumentID < 0||argumentID >= dataType.data.length)
			return;

		DataType current = dataType.data[argumentID];
		if(current instanceof DataTypeAccessor)
		{
			Character selected = argumentAccessorDropdown==null?null: argumentAccessorDropdown.getSelectedEntry();
			setArgument(argumentID, new DataTypeAccessor(selected==null?'a': selected));
		}
		else if(argumentEditor!=null)
			setArgument(argumentID, argumentEditor.outputType());
	}

	private void ensureArgumentArray(DataOperationMeta meta)
	{
		if(dataType.data==null||dataType.data.length!=meta.allowedTypes().length)
			dataType.setOperation(dataType.getOperation());
	}

	private DataType getArgument(int index, Class<? extends DataType> expectedType)
	{
		ensureArgumentArray(dataType.getMeta());
		if(index < 0||index >= dataType.data.length)
			return IIDataTypeUtils.getVarInstance(expectedType);
		DataType argument = dataType.data[index];
		if(argument instanceof DataTypeAccessor)
			return argument;
		return normalizeArgument(argument, expectedType);
	}

	private DataType normalizeArgument(@Nullable DataType argument, Class<? extends DataType> expectedType)
	{
		if(argument!=null&&expectedType.isAssignableFrom(argument.getClass()))
			return argument.clone();
		return IIDataTypeUtils.getVarInstance(expectedType);
	}

	private void setArgument(int index, DataType value)
	{
		if(dataType.data==null||index < 0||index >= dataType.data.length)
			return;
		dataType.data[index] = value;
	}

	private char getCurrentAccessorVariable(DataType argument)
	{
		return argument instanceof DataTypeAccessor?((DataTypeAccessor)argument).variable: 'a';
	}

	@Override
	public DataTypeExpression outputType()
	{
		storeCurrentPageOutput();
		return dataType;
	}
}
