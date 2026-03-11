package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTab;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoTabGroup;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorExpression extends DecoDataEditor<DataTypeExpression>
{
	private int page = 0;
	private DecoPanel editor;

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

		//Initialize tabs
		DecoTabGroup groups = new DecoTabGroup(0, 4)
				.withSize(width, 12)
				.withHorizontalAlignment(true)
				.withTab((DecoTab)new DecoTab()
						.withText("Properties")
						.withPadding(4, 2, 4, 2)
						.withOnPressed((gui, button, mouseX, mouseY) -> setPage(0))
						.withTranslatedTooltip("Properties", IIStringUtil.getItalicString(TextFormatting.GRAY+"Edit expression properties, like conditional variables."))
				);
		String[] params = meta.params();
		for(int paramID = 0; paramID < params.length; paramID++)
		{
			int finalParamID = paramID+1;
			groups.withTab((DecoTab)new DecoTab()
					.withText("datasystem.immersiveintelligence.function."+meta.name()+".param."+params[paramID])
					.withPadding(4, 2, 4, 2)
					.withOnPressed((gui, button, mouseX, mouseY) -> setPage(finalParamID))
			);
		}
		addComponents(groups);

		//Initialize editor
		removeComponent(this.editor);
		if(page==0)
		{
			this.editor = addComponent(new DecoPanel(0, 16))
					.withSize(width, height-16)
					.withBackground(null);
			this.editor.addLabel("Here be parameters", 2, 2);
		}
		else
		{
			DataType argument = dataType.getArgument(page-1);

			DecoPanel typePanel = addComponent(new DecoPanel(0, 16)
					.withSize(width, 20)
					.withBackground(DecoTextures.BG_PAPER)
					.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
			);

			typePanel.addLabel("Type", 4, 2)
					.withSize(30, 16)
					.withAlign(DecoAlignment.LEFT);
			typePanel.addComponents(new DecoDropdown<TypeMetaInfo<?>>(34, 2)
							.withSize(width-32-2-16-2-2, 16)
							.withDropdownWidth(width-16)
							.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
							.withDropdownSymbol(DecoTextures.COMPONENT_DROPDOWN_SYMBOL_PAPER)
							.withEntries(IIDataTypeUtils.metaTypesByClass.get(DataTypeAccessor.class),
									IIDataTypeUtils.metaTypesByClass.get(meta.allowedTypes()[page-1])
							),
					new DecoButton(width-17-2, 2)
							.withText("@")
							.withSize(16, 16)
							.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
			);

			//Get editor
			this.editor = DecoDataEditor.getEditorFor(argument, 0, 36);
			if(editor!=null)
				addComponent(editor.withSize(this.width, this.height-36)
						.withBackground(DecoTextures.BG_STEEL)
						.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
				);
		}
		return true;
	}

	private boolean setPage(int page)
	{
		if(page==this.page)
			return false;

		this.page = page;
		this.initialized = false;
		return true;
	}

	@Override
	public DataTypeExpression outputType()
	{
		return dataType;
	}
}
