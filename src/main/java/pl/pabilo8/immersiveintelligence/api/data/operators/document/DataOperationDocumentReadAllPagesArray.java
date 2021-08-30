package pl.pabilo8.immersiveintelligence.api.data.operators.document;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextComponent.Serializer;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationDocumentReadAllPagesArray extends DataOperator
{
	public DataOperationDocumentReadAllPagesArray()
	{
		name = "document_read_all_pages_array";
		sign = "";
		allowedType1 = DataPacketTypeItemStack.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeArray.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		ItemStack stack = getVarInType(DataPacketTypeItemStack.class, data.getType1(), packet).value;
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
			return new DataPacketTypeArray(list.stream().map(DataPacketTypeString::new).toArray(IDataType[]::new));
		}
		return new DataPacketTypeArray();
	}
}
