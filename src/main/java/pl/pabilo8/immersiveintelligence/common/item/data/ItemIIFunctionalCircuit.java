package pl.pabilo8.immersiveintelligence.common.item.data;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.data.DataOperations;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataStorageItem;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IAdvancedTooltipItem;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit.Circuits;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8
 * @since 25-06-2019
 */
public class ItemIIFunctionalCircuit extends ItemIISubItemsBase<Circuits> implements IDataStorageItem, IAdvancedTooltipItem
{
	private static DataPacket lastStored = null;
	private static Circuits last = null;
	private static IDataType[] lastTooltip = null, lastStoredTooltip = null;

	public ItemIIFunctionalCircuit()
	{
		super("circuit_functional", 1, Circuits.values());
	}

	@GeneratedItemModels(itemName = "circuit_functional")
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
		),
		CRYPTOGRAPHER(CircuitTypes.CRYPTOGRAPHIC,
				"encrypt_text",
				"encrypt_number",
				"decrypt_text",
				"decrypt_number"
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
		if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LSHIFT, IIReference.DESCRIPTION_KEY+"functional_circuit_shift", tooltip))
		{
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"functional_circuit"));
			getOperationsList(stack).stream()
					.map(s -> "   "+I18n.format(IIReference.DATA_KEY+"function."+s))
					.forEach(tooltip::add);
		}
		if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LCONTROL, IIReference.DESCRIPTION_KEY+"functional_circuit_ctrl", tooltip))
		{
			tooltip.add(I18n.format(IIReference.DESCRIPTION_KEY+"functional_circuit_data"));
			for(IDataType type : getStoredData(stack))
				if(type instanceof DataTypeExpression)
					tooltip.add("   "+I18n.format(IIReference.DATA_KEY+"function."+((DataTypeExpression)type).getOperation().name));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addAdvancedInformation(ItemStack stack, int offsetX, List<Integer> offsetsY)
	{
		//Display all the operations
		GlStateManager.translate(offsetX, offsetsY.get(0), 700);
		GlStateManager.scale(.5f, .5f, 1);

		boolean b = ItemTooltipHandler.canExpandTooltip(Keyboard.KEY_LSHIFT);
		if(b)
		{
			IDataType[] types;
			if(last==IIContent.itemCircuit.stackToSub(stack))
				types = lastTooltip;
			else
				lastTooltip = types = IIContent.itemCircuit.getOperationsList(stack).stream()
						.map(o -> DataPacket.getVarInstance(DataOperations.getOperationInstance(o).expectedResult))
						.toArray(IDataType[]::new);

			GlStateManager.color(1f, 1f, 1f, 1f);
			for(int i = 0; i < types.length; i++)
			{
				ClientUtils.bindTexture(types[i].textureLocation());
				Gui.drawModalRectWithCustomSizedTexture(0, i*20, 0, 0, 16, 16, 16, 16);
			}
		}

		if(ItemTooltipHandler.canExpandTooltip(Keyboard.KEY_LCONTROL))
		{
			DataPacket storedData = getStoredData(stack);
			IDataType[] types;
			if(storedData.equals(lastStored))
				types = lastStoredTooltip;
			else
				lastStoredTooltip = types = storedData.variables.values().stream()
						.filter(o -> o instanceof DataTypeExpression)
						.map(o -> DataPacket.getVarInstance(((DataTypeExpression)o).getOperation().expectedResult))
						.toArray(IDataType[]::new);

			GlStateManager.color(1f, 1f, 1f, 1f);
			int off = offsetsY.get(b?1: 0)-offsetsY.get(0);
			for(int i = 0; i < types.length; i++)
			{
				ClientUtils.bindTexture(types[i].textureLocation());
				Gui.drawModalRectWithCustomSizedTexture(0, off+i*20, 0, 0, 16, 16, 16, 16);
			}
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
