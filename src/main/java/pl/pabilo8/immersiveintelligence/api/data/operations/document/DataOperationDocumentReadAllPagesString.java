package pl.pabilo8.immersiveintelligence.api.data.operations.document;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "document_read_all_pages_string",
		allowedTypes = {DataTypeItemStack.class}, params = {"document"},
		expectedResult = DataTypeArray.class)
public class DataOperationDocumentReadAllPagesString extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		ItemStack stack = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0)).value;
		if(ItemNBTHelper.hasKey(stack, "pages"))
		{
			StringBuilder builder = new StringBuilder();
			for(NBTBase page : ItemNBTHelper.getTag(stack).getTagList("pages", 8))
			{
				if(page instanceof NBTTagString)
				{
					ITextComponent json = Serializer.jsonToComponent(((NBTTagString)page).getString());
					if(json!=null)
					{
						if(builder.length() > 0)
							builder.append("\n");
						builder.append(json.getUnformattedText());
					}
				}
			}
			return new DataTypeString(builder.toString());
		}
		return new DataTypeString();
	}
}
