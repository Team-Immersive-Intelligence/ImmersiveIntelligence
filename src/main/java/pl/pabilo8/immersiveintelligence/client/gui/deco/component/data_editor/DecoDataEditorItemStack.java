package pl.pabilo8.immersiveintelligence.client.gui.deco.component.data_editor;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 07.09.2021
 */
public class DecoDataEditorItemStack extends DecoDataEditor<DataTypeItemStack>
{
	private DecoTextField metaEdit, countEdit;
	private ItemStack scanned;

	public DecoDataEditorItemStack(int x, int y, DataTypeItemStack dataType)
	{
		super(x, y, dataType);
		this.scanned = dataType.value;
	}

	@Override
	protected boolean initialize()
	{
		//TODO: 10.07.2025 translations!
		addLabel(IIReference.DESCRIPTION_KEY+"variable_value", 2, 2);
		addLabel("Item:", 2, 12);
		addLabel("Meta:", 2, 2+20+12);
		addLabel("Count:", 2, 2+20+12+18);

		addComponents(
				metaEdit = new DecoTextField(40, 2+20+12)
						.withSize(width-42, 16)
						.withFilter(TextFilter.DECIMAL)
						.withText(scanned.getMetadata()),
				countEdit = new DecoTextField(40, 2+20+12+18)
						.withSize(width-42, 16)
						.withFilter(TextFilter.DECIMAL)
						.withText(scanned.getCount()),
				new DecoItemStackDisplay((width/2)-8, 8)
						.withStack(scanned)
		);
		return super.initialize();
	}

	@Override
	public DataTypeItemStack outputType()
	{
		dataType.value = scanned.copy();
		dataType.value.setItemDamage(Integer.parseInt(metaEdit.getText()));
		dataType.value.setCount(Integer.parseInt(countEdit.getText()));
		return dataType;
	}
}
