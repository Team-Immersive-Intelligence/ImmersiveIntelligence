package pl.pabilo8.immersiveintelligence.api.data.operations.document;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationDocumentReadAllPagesArray extends DataOperation
{
	public DataOperationDocumentReadAllPagesArray()
	{
		name = "document_read_all_pages_array";
		allowedTypes = new Class[]{DataTypeItemStack.class};
		params = new String[]{"document"};
		expectedResult = DataTypeArray.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		ItemStack stack = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0)).value;
		if(ItemNBTHelper.hasKey(stack, "pages"))
		{
			ArrayList<String> list = new ArrayList<>();
			for(NBTBase page : ItemNBTHelper.getTag(stack).getTagList("pages", 8))
			{
				if(page instanceof NBTTagString)
				{
					ITextComponent json = Serializer.jsonToComponent(((NBTTagString)page).getString());
					if(json!=null)
					list.add(json.getUnformattedText());
				}
			}
			return new DataTypeArray(list.stream().map(DataTypeString::new).toArray(IDataType[]::new));
		}
		return new DataTypeArray();
	}
}
