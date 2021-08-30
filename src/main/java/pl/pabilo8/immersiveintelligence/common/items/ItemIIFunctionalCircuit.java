package pl.pabilo8.immersiveintelligence.common.items;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataHelper;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataStorageItem;
import pl.pabilo8.immersiveintelligence.api.data.operators.document.*;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author Pabilo8
 * @since 25-06-2019
 */
public class ItemIIFunctionalCircuit extends ItemIIBase implements IDataStorageItem
{
	public ItemIIFunctionalCircuit()
	{
		super("circuit_functional", 1, Arrays.stream(Circuit.values()).map(Circuit::getName).toArray(String[]::new));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
	{
		list.add(I18n.format(CommonProxy.DESCRIPTION_KEY+"functional_circuit"));
		for(String s : getOperationsList(stack))
		{
			list.add("-"+I18n.format(CommonProxy.DATA_KEY+"function."+s));
		}
	}

	@Override
	public DataPacket getStoredData(ItemStack stack)
	{
		NBTTagCompound tag = stack.serializeNBT();
		NBTTagCompound realtag = ItemNBTHelper.getTagCompound(stack, "operations");
		DataPacket data = new DataPacket();
		data.fromNBT(realtag);
		return data;
	}

	@Override
	public void writeDataToItem(DataPacket packet, ItemStack stack)
	{
		ItemNBTHelper.setTagCompound(stack, "operations", packet.toNBT());
	}

	@Override
	public String getDataStorageItemType(ItemStack stack)
	{
		return DataHelper.DATA_STORAGE_TYPE_CIRCUIT_FUNCTIONAL;
	}


	public List<String> getOperationsList(ItemStack stack)
	{
		if(stack.getMetadata() < Circuit.values().length)
		{
			Circuit circuit = Circuit.values()[stack.getMetadata()];
			return Arrays.asList(circuit.functions);
		}
		return Collections.emptyList();
	}

	public String getTESRRenderTexture(ItemStack stack)
	{
		if(stack.getMetadata() < Circuit.values().length)
			switch(Circuit.values()[stack.getMetadata()].tier)
			{
				case 0:
					return "redstone_circuits";
				case 1:
					return "electronic_circuits";
				case 2:
					return "advanced_circuits";
			}
		return "";
	}

	enum Circuit implements IStringSerializable
	{
		ARITHMETIC(1,
				"add",
				"subtract",
				"multiply",
				"divide",
				"modulo"
		),
		ADVANCED_ARITHMETIC(2, ARITHMETIC,
				"power",
				"root",
				"min",
				"max"
		),
		LOGIC(0,
				"and",
				"or",
				"not"
		),
		COMPARATOR(1,
				"greater",
				"less",
				"greater_or_equal",
				"less_or_equal",
				"equal"
		),
		ADVANCED_LOGIC(2, LOGIC,
				"nand",
				"nor",
				"xor",
				"xnor"
		),
		TEXT(1,
				"string_join",
				"string_equal",
				"string_split",
				"string_length",
				"string_char_at",
				"string_substring",
				"string_trim",
				"string_hexcol",
				"string_format",
				"string_contains",
				"string_contains_count",
				"string_lowercase",
				"string_uppercase",
				"string_snake_case",
				"string_camel_case",
				"string_reverse"
		),
		ITEMSTACK(2,
				"get_quantity",
				"set_quantity",
				"get_durability",
				"set_durability",
				"get_nbt",
				"set_nbt",
				"can_stack_with",
				"matches_oredict"
		),
		ARRAY(1,
				"array_get",
				"array_pop",
				"array_push"
		),
		ENTITY(2,
				"entity_get_id",
				"entity_get_type",
				"entity_get_name",
				"entity_get_dimension_id",
				"entity_get_x",
				"entity_get_y",
				"entity_get_z"
		),
		DOCUMENT(2,
				"document_read_page",
				"document_read_all_pages_array",
				"document_read_all_pages_string",
				"document_get_author",
				"document_get_title"
		),
		TYPE_CONVERSION(1,
				"is_null",
				"to_integer",
				"to_string",
				"to_boolean"
		);

		private final String[] functions;
		private final int tier;

		Circuit(int tier, String... functions)
		{
			this.tier = tier;
			this.functions = functions;
		}

		Circuit(int tier, Circuit parent, String... functions)
		{
			this(tier, ArrayUtils.addAll(parent.functions, functions));
		}

		@Override
		public String getName()
		{
			return this.toString().toLowerCase(Locale.ENGLISH);
		}
	}
}
