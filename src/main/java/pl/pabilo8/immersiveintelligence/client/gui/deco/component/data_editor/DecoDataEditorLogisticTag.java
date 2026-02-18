package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import net.minecraft.item.EnumDyeColor;
import pl.pabilo8.immersiveintelligence.api.LogisticTag;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeLogisticTag;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoDropdown;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorLogisticTag extends DecoDataEditor<DataTypeLogisticTag>
{
	private static final String KEY = IIReference.GUI_LABEL_KEY+"data_editor.logistics_tag.";

	private LogisticTag logisticTag;

	public DecoDataEditorLogisticTag(int x, int y, DataTypeLogisticTag dataType)
	{
		super(x, y, dataType);
		this.logisticTag = dataType.value;
	}

	@Override
	protected boolean initialize()
	{
		//Header
		addLabel(IIReference.DESCRIPTION_KEY+"variable_value", 2, 2);
		//Layout
		final int labelX = 2;
		final int fieldX = 64;
		final int rowH = 17;
		int y = 2+10;

		//Name
		addLabel(KEY+"name", labelX, y+4);
		addComponent(new DecoTextField(fieldX, y)
				.withSize(width-fieldX-2, 16)
				.withText(logisticTag.getName())
				.withOnTextChanged(s -> logisticTag.withName(s))
				.withTranslatedTooltip(KEY+"name.tooltip")
		);
		y += rowH;

		//Description
		addLabel(KEY+"description", labelX, y+4);
		addComponent(new DecoTextField(fieldX, y)
				.withSize(width-fieldX-2, 16)
				.withText(logisticTag.getDescription())
				.withOnTextChanged(s -> logisticTag.withDescription(s))
				.withTranslatedTooltip(KEY+"description.tooltip")
		);
		y += rowH;

		//TODO: 27.01.2026 picker for the owner

		//Origin
		addLabel(KEY+"origin", labelX, y+4);
		addComponent(new DecoTextField(fieldX, y)
				.withSize(width-fieldX-2, 16)
				.withText(logisticTag.getOrigin())
				.withOnTextChanged(s -> logisticTag.withOrigin(s))
				.withTranslatedTooltip(KEY+"origin.tooltip")
		);
		y += rowH;

		//Destination
		addLabel(KEY+"destination", labelX, y+4);
		addComponent(new DecoTextField(fieldX, y)
				.withSize(width-fieldX-2, 16)
				.withText(logisticTag.getDestination())
				.withOnTextChanged(s -> logisticTag.withDestination(s))
				.withTranslatedTooltip(KEY+"destination.tooltip")
		);
		y += rowH;

		//Color
		addLabel(KEY+"color", labelX, y+4);
		addComponent(new DecoDropdown<EnumDyeColor>(fieldX, y)
				.withSize(width-fieldX-2, 12)
				.withDropdownWidth(width-fieldX-2)
				.withEntries(EnumDyeColor.values())
				.withDisplayFunction(new DecoEntryPanelBuilder<EnumDyeColor>()
						.withBackground(DecoTextures.BG_STEEL)
						.withHeight(12)
						.withComponent("icon", new DecoImage(2, 1)
								.withSize(8, 8)
								.withImageLocation(DecoTextures.COMPONENT_COLOR, true)
								.withUV(16, 4, 4, 12, 12)
						)
						.withLabel("label",
								new DecoLabel(IIClientUtils.fontRegular, 12, 1)
										.withSize(48, 12)
										.withAlign(DecoAlignment.LEFT)
										.withRawText("Color")
						)
						.withElementApplyMethod((tf, builder) -> {
							builder.component("icon", DecoImage.class).withColor(IIColor.fromDye(tf));
							builder.label("label").withText("item.fireworksCharge."+IIColor.fromDye(tf).getDyeColor().getUnlocalizedName());
						})
				)
				.withSelectedEntry(logisticTag.getColor()))
				.withOnSelectedEntry((oldColor, newColor) -> logisticTag.withColor(newColor));

		return super.initialize();
	}

	@Override
	public DataTypeLogisticTag outputType()
	{
		dataType.value = logisticTag.clone();
		return dataType;
	}
}
