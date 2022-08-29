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
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataStorageItem;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Pabilo8
 * @since 25-06-2019
 */
public class ItemIIFunctionalCircuit extends ItemIIBase implements IDataStorageItem
{
	public static final String[] TEXTURES = {"redstone_circuits", "electronic_circuits", "advanced_circuits"};

	public ItemIIFunctionalCircuit()
	{
		super("circuit_functional", 1, Arrays.stream(Circuit.values()).map(Circuit::getName).toArray(String[]::new));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> list, @Nonnull ITooltipFlag flag)
	{
		boolean b = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
		list.add(I18n.format(CommonProxy.DESCRIPTION_KEY+(b?"functional_circuit": "functional_circuit_shift")));
		if(b)
			for(String s : getOperationsList(stack))
			{
				list.add("-"+I18n.format(CommonProxy.DATA_KEY+"function."+s));
			}
	}

	@Override
	public DataPacket getStoredData(ItemStack stack)
	{
		stack.serializeNBT();
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
			return TEXTURES[Circuit.values()[stack.getMetadata()].tier];
		return "";
	}

	public enum Circuit implements IStringSerializable
	{
		ARITHMETIC(1,
				"add", "subtract", "multiply", "divide",
				"modulo",
				"abs"
		),
		ADVANCED_ARITHMETIC(2, ARITHMETIC,
				"power", "root",
				"min", "max",
				"sign",
				"ceil", "round", "floor",
				"sin", "cos", "tan"
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
				"string_join", "equal",
				"string_split",
				"string_length",
				"string_char_at", "string_substring", "string_trim",
				"string_hexcol", "string_format",
				"string_contains", "string_contains_count",
				"string_replace_first", "string_replace_all",
				"string_lowercase", "string_uppercase", "string_snake_case", "string_camel_case",
				"string_reverse"
		),
		ITEMSTACK(2,
				"get_quantity",
				"set_quantity",
				"get_durability",
				"set_durability",
				"get_nbt",
				"set_nbt",
				"get_itemstack",
				"get_item_id",
				"can_stack_with",
				"matches_oredict"
		),
		ARRAY(1,
				"array_get", "array_set",
				"array_length",
				"array_push", "array_pop",
				"array_swap"
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
				"to_float",
				"to_string",
				"to_boolean",
				"to_null"
		);

		private final String[] functions;
		public final int tier;

		Circuit(int tier, String... functions)
		{
			this.tier = tier;
			this.functions = functions;
		}

		Circuit(int tier, Circuit parent, String... functions)
		{
			this(tier, ArrayUtils.addAll(parent.functions, functions));
		}

		public String[] getFunctions()
		{
			return functions;
		}

		@Nonnull
		@Override
		public String getName()
		{
			return this.toString().toLowerCase(Locale.ENGLISH);
		}
	}
}
