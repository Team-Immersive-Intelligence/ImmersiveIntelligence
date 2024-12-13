package pl.pabilo8.immersiveintelligence.common.item.data;

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
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataOperationUtils;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataStorageItem;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.IGenericDataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IAdvancedTooltipItem;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.amt.IIAnimationUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit.Circuits;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
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
 * @author Avalon
 * @since 20-09-2024
 */
@IIItemProperties(category = IICategory.ELECTRONICS)
public class ItemIIFunctionalCircuit extends ItemIISubItemsBase<Circuits> implements IDataStorageItem, IAdvancedTooltipItem
{
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
				"itemstack_create",
				"item_get_count",
				"item_set_count",
				"item_get_meta",
				"item_set_meta",
				"item_get_nbt",
				"item_set_nbt",
				"item_get_id",
				"item_is_empty",
				"item_stacks_with"
		),
		ARRAY(CircuitTypes.BASIC,
				"array_create",
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
				"entity_get_pos",
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
		FLUIDSTACK(CircuitTypes.ADVANCED,
				"fluidstack_create",
				"fluid_get_id",
				"fluid_get_amount",
				"fluid_set_amount",
				"fluid_get_nbt",
				"fluid_set_nbt",
				"fluid_is_empty",
				"fluid_stacks_with"
		),
		MAP(CircuitTypes.ADVANCED,
				"map_create",
				"map_set", "map_get", "map_remove", "map_clear",
				"map_contains",
				"map_keys", "map_values"
		),
		VECTOR_ARITHMETIC(CircuitTypes.PROCESSOR,
				"vector_create", "vector_create_angle",
				"vector_add", "vector_sub", "vector_mul", "vector_scale",
				"vector_length", "vector_dot", "vector_cross", "vector_normalize", "vector_distance",
				"vector_get_x", "vector_get_y", "vector_get_z",
				"vector_set_x", "vector_set_y", "vector_set_z",
				"vector_get_yaw", "vector_get_pitch"
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
			for(DataType type : getStoredData(stack))
				if(type instanceof DataTypeExpression)
					tooltip.add("   "+I18n.format(IIReference.DATA_KEY+"function."+((DataTypeExpression)type).getMeta().name()));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addAdvancedInformation(ItemStack stack, int offsetX, List<Integer> offsetsY)
	{
		// Ensure that offsetsY contains at least one element
		if(!offsetsY.isEmpty())
		{
			GlStateManager.translate(offsetX, offsetsY.get(0), 700);
			GlStateManager.scale(.5f, .5f, 1);
		}
		else
		{
			// Handle case where offsetsY is empty
			GlStateManager.translate(offsetX, 0, 700);
			GlStateManager.scale(.5f, .5f, 1);
		}

		boolean b = ItemTooltipHandler.canExpandTooltip(Keyboard.KEY_LSHIFT);
		if(b)
		{
			TypeMetaInfo<?>[] types = IIContent.itemCircuit.getOperationsList(stack).stream()
					.map(IIDataOperationUtils::getOperationMeta)
					.map(DataOperationMeta::expectedResult)
					.map(this::getDisplayedType)
					.toArray(TypeMetaInfo[]::new);

			GlStateManager.color(1f, 1f, 1f, 1f);
			for(int i = 0; i < types.length; i++)
			{
				IIClientUtils.bindTexture(types[i].getTextureLocation());
				Gui.drawModalRectWithCustomSizedTexture(0, i*20, 0, 0, 16, 16, 16, 16);
			}
		}

		if(ItemTooltipHandler.canExpandTooltip(Keyboard.KEY_LCONTROL))
		{
			DataPacket storedData = getStoredData(stack);
			TypeMetaInfo<?>[] types = storedData.variables.values().stream()
					.filter(o -> o instanceof DataTypeExpression)
					.map(o -> (DataTypeExpression)o)
					.map(DataTypeExpression::getMeta)
					.map(DataOperationMeta::expectedResult)
					.map(this::getDisplayedType)
					.toArray(TypeMetaInfo[]::new);

			GlStateManager.color(1f, 1f, 1f, 1f);

			// Check if offsetsY has enough elements for the secondary offset (b ? 1 : 0)
			int off = 0;
			if(offsetsY.size() > (b?1: 0))
				off = offsetsY.get(b?1: 0)-offsetsY.get(0);

			for(int i = 0; i < types.length; i++)
			{
				IIClientUtils.bindTexture(types[i].getTextureLocation());
				Gui.drawModalRectWithCustomSizedTexture(0, off+i*20, 0, 0, 16, 16, 16, 16);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private TypeMetaInfo<?> getDisplayedType(Class<? extends DataType> type)
	{
		//Allowing all types
		if(type==DataType.class)
			return getArrayElementForTime(IIDataTypeUtils.metaTypesByClass.values().toArray(new TypeMetaInfo[0]));
		else //Allowing only specific types
		{
			if(type.isAnnotationPresent(IGenericDataType.class))
				return getArrayElementForTime(IIDataTypeUtils.metaTypesByClass.keySet().stream()
						.filter(type::isAssignableFrom)
						.filter(t -> t!=type)
						.map(IIDataTypeUtils.metaTypesByClass::get)
						.toArray(TypeMetaInfo[]::new));
			else
				return IIDataTypeUtils.metaTypesByClass.get(type);

		}
	}

	@SideOnly(Side.CLIENT)
	private <T> T getArrayElementForTime(T[] parameters)
	{
		float progress = IIAnimationUtils.getDebugProgress(parameters.length*20, 0);
		if(parameters.length==1)
			return parameters[0];
		else
			return parameters[(int)(progress*parameters.length)];
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
