package pl.pabilo8.immersiveintelligence.common.item;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataStorageItem;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIFunctionalCircuit.Circuits;
import pl.pabilo8.immersiveintelligence.common.util.IILib;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 25-06-2019
 */
public class ItemIIFunctionalCircuit extends ItemIISubItemsBase<Circuits> implements IDataStorageItem
{
	public ItemIIFunctionalCircuit()
	{
		super("circuit_functional", 1, Circuits.values());
	}

	public enum Circuits implements IIItemEnum
	{
		ARITHMETIC(CircuitTypes.BASIC,
				"add", "subtract", "multiply", "divide",
				"modulo",
				"abs"
		),
		ADVANCED_ARITHMETIC(CircuitTypes.ADVANCED, ARITHMETIC,
				"power", "root",
				"min", "max",
				"sign",
				"ceil", "round", "floor",
				"sin", "cos", "tan"
		),
		LOGIC(CircuitTypes.BASIC,
				"and",
				"or",
				"not"
		),
		COMPARATOR(CircuitTypes.BASIC,
				"greater",
				"less",
				"greater_or_equal",
				"less_or_equal",
				"equal"
		),
		ADVANCED_LOGIC(CircuitTypes.ADVANCED, LOGIC,
				"nand",
				"nor",
				"xor",
				"xnor"
		),
		TEXT(CircuitTypes.BASIC,
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
		ITEMSTACK(CircuitTypes.ADVANCED,
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
		ARRAY(CircuitTypes.BASIC,
				"array_get", "array_set",
				"array_length",
				"array_push", "array_pop",
				"array_swap"
		),
		ENTITY(CircuitTypes.ADVANCED,
				"entity_get_id",
				"entity_get_type",
				"entity_get_name",
				"entity_get_dimension_id",
				"entity_get_x",
				"entity_get_y",
				"entity_get_z"
		),
		DOCUMENT(CircuitTypes.ADVANCED,
				"document_read_page",
				"document_read_all_pages_array",
				"document_read_all_pages_string",
				"document_get_author",
				"document_get_title"
		),
		TYPE_CONVERSION(CircuitTypes.BASIC,
				"is_null",
				"to_integer",
				"to_float",
				"to_string",
				"to_boolean",
				"to_null"
		),
		FLUIDSTACK(CircuitTypes.BASIC,
				"is_null",
				"to_integer",
				"to_float",
				"to_string",
				"to_boolean",
				"to_null"
		),
		MAP(CircuitTypes.BASIC,
				"is_null",
				"to_integer",
				"to_float",
				"to_string",
				"to_boolean",
				"to_null"
		),
		VECTOR_ARITHMETIC(CircuitTypes.PROCESSOR,
				"is_null",
				"to_integer",
				"to_float",
				"to_string",
				"to_boolean",
				"to_null"
		);

		private final String[] functions;
		public final CircuitTypes tier;

		Circuits(CircuitTypes tier, String... functions)
		{
			this.tier = tier;
			this.functions = functions;
		}

		Circuits(CircuitTypes tier, Circuits parent, String... functions)
		{
			this(tier, ArrayUtils.addAll(parent.functions, functions));
		}

		public String[] getFunctions()
		{
			return functions;
		}
	}

	public enum CircuitTypes implements ISerializableEnum
	{
		BASIC("basic_circuits", "circuitBasic"),
		ADVANCED("advanced_circuits", "circuitAdvanced"),
		CRYPTOGRAPHIC("cryptography_circuits", "circuitCryptographic"),
		PROCESSOR("processor_circuits", "circuitProcessor");

		public final String texture, material;

		CircuitTypes(String texture, String material)
		{
			this.texture = texture;
			this.material = material;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> tooltip, @Nonnull ITooltipFlag flag)
	{
		if(IIClientUtils.addExpandableTooltip(Keyboard.KEY_LSHIFT, IILib.DESCRIPTION_KEY+"functional_circuit_shift", tooltip))
		{
			tooltip.add(I18n.format(IILib.DESCRIPTION_KEY+"functional_circuit"));
			getOperationsList(stack).stream()
					.map(s -> "   "+I18n.format(IILib.DATA_KEY+"function."+s))
					.forEach(tooltip::add);
		}
	}

	@SideOnly(Side.CLIENT)
	@Nullable
	@Override
	public FontRenderer getFontRenderer(@Nonnull ItemStack stack)
	{
		return IIClientUtils.fontRegular;
	}

	@Override
	public DataPacket getStoredData(ItemStack stack)
	{
		DataPacket data = new DataPacket();
		data.fromNBT(ItemNBTHelper.getTagCompound(stack, "operations"));
		return data;
	}

	@Override
	public void writeDataToItem(DataPacket packet, ItemStack stack)
	{
		ItemNBTHelper.setTagCompound(stack, "operations", packet.toNBT());
	}


	public List<String> getOperationsList(ItemStack stack)
	{
		if(stack.getMetadata() < Circuits.values().length)
		{
			Circuits circuit = Circuits.values()[stack.getMetadata()];
			return Arrays.asList(circuit.functions);
		}
		return Collections.emptyList();
	}

	public String getTESRRenderTexture(ItemStack stack)
	{
		return stackToSub(stack).tier.texture;
	}
}
