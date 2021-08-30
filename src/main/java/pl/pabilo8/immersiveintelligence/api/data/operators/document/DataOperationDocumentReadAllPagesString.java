package pl.pabilo8.immersiveintelligence.api.data.operators.document;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationDocumentReadAllPagesString extends DataOperator
{
	public DataOperationDocumentReadAllPagesString()
	{
		name = "document_read_all_pages_string";
		sign = "";
		allowedType1 = DataPacketTypeItemStack.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		ItemStack stack = getVarInType(DataPacketTypeItemStack.class, data.getType1(), packet).value;
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
			return new DataPacketTypeString(builder.toString());
		}
		return new DataPacketTypeString();
	}
}
