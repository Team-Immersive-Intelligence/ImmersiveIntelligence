package pl.pabilo8.immersiveintelligence.client.gui.deco.util.clipboard;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import pl.pabilo8.immersiveintelligence.api.DustTank;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataVariable;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoArrows;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.label.DecoLabel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoEntryPanelBuilder;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoDustTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextArea;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoColors;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Extensible, typed copy/paste registry used by Deco components and clipboard entries.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 21.09.2026
 */
public final class DecoClipboardUtils
{
	public static final String MAGIC = "decoClipboard", TYPE = "type", VALUE = "value";
	private static final List<ClipboardProvider<?>> PROVIDERS = new ArrayList<>();
	private static final Map<String, ClipboardProvider<?>> PROVIDERS_BY_ID = new LinkedHashMap<>();

	static
	{
		registerProvider(new ClipboardProvider<>("plain_text", String.class)
		{
			@Override
			public NBTTagCompound write(String value)
			{
				return tagString("text", value);
			}

			@Override
			public String read(NBTTagCompound nbt)
			{
				return nbt.getString("text");
			}

			@Override
			public String preview(String value)
			{
				return value;
			}
		});
		registerProvider(new ClipboardProvider<>("boolean", Boolean.class)
		{
			@Override
			public NBTTagCompound write(Boolean value)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setBoolean("boolean", value);
				return nbt;
			}

			@Override
			public Boolean read(NBTTagCompound nbt)
			{
				return nbt.getBoolean("boolean");
			}

			@Override
			public String preview(Boolean value)
			{
				return value.toString();
			}
		});
		registerProvider(new ClipboardProvider<>("data_variable", DataVariable.class)
		{
			@Override
			public NBTTagCompound write(DataVariable value)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("name", String.valueOf(value.getName()));
				nbt.setTag("data", value.getValue().valueToNBT());
				return nbt;
			}

			@Override
			public DataVariable read(NBTTagCompound nbt)
			{
				String name = nbt.getString("name");
				return new DataVariable(name.isEmpty()?'a': name.charAt(0), IIDataTypeUtils.getVarFromNBT(nbt.getCompoundTag("data")));
			}

			@Override
			public String preview(DataVariable value)
			{
				return value.getName()+" = "+value.getValue();
			}
		});
		registerProvider(new ClipboardProvider<>("data_type", DataType.class)
		{
			@Override
			public NBTTagCompound write(DataType value)
			{
				return value.valueToNBT();
			}

			@Override
			public DataType read(NBTTagCompound nbt)
			{
				return IIDataTypeUtils.getVarFromNBT(nbt);
			}

			@Override
			public String preview(DataType value)
			{
				return value.getName()+" = "+value;
			}
		});
		registerProvider(new ClipboardProvider<>("data_packet", DataPacket.class)
		{
			@Override
			public NBTTagCompound write(DataPacket value)
			{
				return value.serializeNBT();
			}

			@Override
			public DataPacket read(NBTTagCompound nbt)
			{
				return new DataPacket(nbt);
			}

			@Override
			public String preview(DataPacket value)
			{
				return I18n.format("ii.gui.clipboard.packet_variables", value.getAllVariables().size());
			}
		});
		registerProvider(new ClipboardProvider<>("item_stack", ItemStack.class)
		{
			@Override
			public NBTTagCompound write(ItemStack value)
			{
				return value.writeToNBT(new NBTTagCompound());
			}

			@Override
			public ItemStack read(NBTTagCompound nbt)
			{
				return new ItemStack(nbt);
			}

			@Override
			public String preview(ItemStack value)
			{
				return value.getDisplayName()+" x"+value.getCount();
			}
		});
		registerProvider(new ClipboardProvider<>("ingredient_stack", IngredientStack.class)
		{
			@Override
			public NBTTagCompound write(IngredientStack value)
			{
				return value.writeToNBT(new NBTTagCompound());
			}

			@Override
			public IngredientStack read(NBTTagCompound nbt)
			{
				return IngredientStack.readFromNBT(nbt);
			}

			@Override
			public String preview(IngredientStack value)
			{
				return String.valueOf(value);
			}
		});
		registerProvider(new ClipboardProvider<>("fluid_stack", FluidStack.class)
		{
			@Override
			public NBTTagCompound write(FluidStack value)
			{
				return value.writeToNBT(new NBTTagCompound());
			}

			@Override
			public FluidStack read(NBTTagCompound nbt)
			{
				return FluidStack.loadFluidStackFromNBT(nbt);
			}

			@Override
			public String preview(FluidStack value)
			{
				return value==null?"": value.getLocalizedName()+" ("+value.amount+" mB)";
			}
		});
		registerProvider(new ClipboardProvider<>("dust_stack", DustStack.class)
		{
			@Override
			public NBTTagCompound write(DustStack value)
			{
				return value.serializeNBT();
			}

			@Override
			public DustStack read(NBTTagCompound nbt)
			{
				return new DustStack(nbt);
			}

			@Override
			public String preview(DustStack value)
			{
				return DustUtils.getDustName(value)+" ("+value.amount+" mB)";
			}
		});
		registerProvider(numberProvider("integer", Integer.class));
		registerProvider(numberProvider("float", Float.class));
		registerProvider(numberProvider("double", Double.class));
		registerProvider(new ClipboardProvider<>("color", IIColor.class)
		{
			@Override
			public NBTTagCompound write(IIColor value)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("argb", value.getPackedARGB());
				return nbt;
			}

			@Override
			public IIColor read(NBTTagCompound nbt)
			{
				return IIColor.fromPackedARGB(Integer.toUnsignedLong(nbt.getInteger("argb")));
			}

			@Override
			public String preview(IIColor value)
			{
				return "#"+value.getHexARGB();
			}
		});
		registerProvider(new ClipboardProvider<Enum<?>>("enum", null)
		{
			@Override
			public boolean accepts(Object value)
			{
				return value instanceof Enum;
			}

			@Override
			public NBTTagCompound write(Enum<?> value)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("class", value.getDeclaringClass().getName());
				nbt.setString("name", value.name());
				return nbt;
			}

			@SuppressWarnings({"rawtypes", "unchecked"})
			@Override
			public Enum<?> read(NBTTagCompound nbt)
			{
				try
				{
					Class<?> klass = Class.forName(nbt.getString("class"), false, DecoClipboardUtils.class.getClassLoader());
					return klass.isEnum()?Enum.valueOf((Class<? extends Enum>)klass, nbt.getString("name")): null;
				} catch(ReflectiveOperationException|IllegalArgumentException ignored) {return null;}
			}

			@Override
			public String preview(Enum<?> value)
			{
				return value instanceof ILocalizedEnum?((ILocalizedEnum)value).getLocalizedName(): value.name();
			}
		});
		registerProvider(new ClipboardProvider<>("manual_page", ManualPageReference.class)
		{
			@Override
			public NBTTagCompound write(ManualPageReference value)
			{
				NBTTagCompound nbt = tagString("entry", value.entry);
				nbt.setInteger("page", value.page);
				nbt.setString("title", value.title);
				return nbt;
			}

			@Override
			public ManualPageReference read(NBTTagCompound nbt)
			{
				return new ManualPageReference(nbt.getString("entry"), nbt.getInteger("page"), nbt.getString("title"));
			}

			@Override
			public String preview(ManualPageReference value)
			{
				return I18n.hasKey(value.title)?I18n.format(value.title): value.title;
			}

			@Override
			public boolean isLink()
			{
				return true;
			}

			@Override
			public void activate(ManualPageReference value)
			{
				value.open();
			}
		});
	}

	private DecoClipboardUtils()
	{
	}

	public static synchronized <T> void registerProvider(ClipboardProvider<T> provider)
	{
		ClipboardProvider<?> old = PROVIDERS_BY_ID.put(provider.id, provider);
		if(old!=null)
			PROVIDERS.remove(old);
		PROVIDERS.add(provider);
	}

	@Nullable
	public static ClipboardProvider<?> getProvider(String typeId)
	{
		return DecoClipboardUtils.PROVIDERS_BY_ID.get(typeId);
	}

	@Nullable
	public static ClipboardProvider<?> findProvider(Object value)
	{
		if(value==null)
			return null;
		for(ClipboardProvider<?> provider : PROVIDERS)
			if(provider.accepts(value))
				return provider;
		return null;
	}

	@Nullable
	@SuppressWarnings("unchecked")
	public static ClipboardEntry createEntry(Object value)
	{
		ClipboardProvider<Object> provider = (ClipboardProvider<Object>)findProvider(value);
		return provider==null?null: new ClipboardEntry(provider.id, provider.write(value));
	}

	public static boolean copy(Object value)
	{
		if(value instanceof String)
		{
			setClipboardString((String)value);
			return true;
		}
		ClipboardEntry entry = createEntry(value);
		if(entry==null)
			return false;
		NBTTagCompound nbt = entry.toNBT();
		nbt.setInteger(MAGIC, 1);
		setClipboardNBT(nbt);
		return true;
	}

	public static boolean valuesEqual(Object first, Object second)
	{
		ClipboardEntry firstEntry = createEntry(first);
		ClipboardEntry secondEntry = createEntry(second);
		return firstEntry!=null&&secondEntry!=null&&firstEntry.getTypeId().equals(secondEntry.getTypeId())
				&&firstEntry.getValue().equals(secondEntry.getValue());
	}

	@Nullable
	public static ClipboardEntry pasteEntry()
	{
		NBTTagCompound nbt = getClipboardNBT();
		if(nbt.getInteger(MAGIC)==1&&nbt.hasKey(TYPE, 8)&&nbt.hasKey(VALUE, 10))
		{
			ClipboardEntry entry = ClipboardEntry.fromNBT(nbt);
			return entry!=null&&entry.isValid()?entry: null;
		}
		return createEntry(getClipboardString());
	}

	@Nullable
	public static Object paste()
	{
		ClipboardEntry entry = pasteEntry();
		return entry==null?null: entry.getValue();
	}

	public static String pasteText()
	{
		Object value = paste();
		return value instanceof String?(String)value: String.valueOf(value==null?"": value);
	}

	public static NBTTagList writeEntries(Collection<ClipboardEntry> entries)
	{
		NBTTagList list = new NBTTagList();
		int count = 0;
		for(ClipboardEntry entry : entries)
			if(entry!=null&&entry.isValid()&&count++ < 64)
				list.appendTag(entry.toNBT());
		return list;
	}

	public static List<ClipboardEntry> readEntries(NBTTagList list)
	{
		List<ClipboardEntry> result = new ArrayList<>();
		for(int i = 0; i < Math.min(64, list.tagCount()); i++)
		{
			ClipboardEntry entry = ClipboardEntry.fromNBT(list.getCompoundTagAt(i));
			if(entry!=null&&entry.isValid())
				result.add(entry);
		}
		return result;
	}

	public static DecoEntryPanelBuilder<ClipboardEntry> createEntryPanel(Consumer<ClipboardEntry> onDelete,
																		 Consumer<ClipboardEntry> onChanged)
	{
		return new DecoEntryPanelBuilder<ClipboardEntry>()
				.withHeight(18)
				.withElementHeight(DecoClipboardUtils::entryHeight)
				.withBackground(DecoTextures.BG_PAPER)
				.withBackgroundMask(DecoTextures.TEMPLATE_PAPER)
				.withLabel("preview", panel -> label(panel, 4, (panel.height-10)/2, panel.width-8))
				.withLabel("visual_name", panel -> label(panel, 20, 2, panel.width-60).withWrapping(true))
				.withLabel("amount", panel -> label(panel, panel.width-64, (panel.height-10)/2, 40-12)
						.withAlign(DecoAlignment.RIGHT))
				.withComponent("color", panel -> new DecoButton(3, (panel.height-12)/2)
						.withSize(12, 12)
						.withBackgroundColor(IIColor.WHITE)
				)
				.withComponent("text", panel -> new DecoTextArea(2, 2)
						.withSize(panel.width-4, panel.height-4)
						.withPadding(2)
						.withBackgroundLocation(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withTextColor(DecoColors.H1)
						.withOnTextChanged(text -> {
							ClipboardEntry replacement = createEntry(text);
							if(panel.getCurrentElement()!=null&&replacement!=null)
							{
								panel.getCurrentElement().replaceWith(replacement);
								if(panel.getCurrentList()!=null)
									panel.getCurrentList().requestLayout();
								onChanged.accept(panel.getCurrentElement());
							}
						}))
				.withComponent("item", panel -> new DecoItemStackDisplay(2, 1)
						.withSize(16, 16)
						.withPadding(new int[]{0, 0, 0, 0})
						.withIconSize(16)
						.withBackgroundTexture(null)
						.withRenderOverlay(false)
						.withIconAlignment(DecoAlignment.LEFT))
				.withComponent("fluid", panel -> new DecoFluidTank(2, 1)
						.withSize(16, 16)
						.withTankBackgroundLocation(null)
						.withTankOverlayLocation(null))
				.withComponent("dust", panel -> new DecoDustTank(2, 2)
						.withSize(16, 14)
						.withTankBackgroundLocation(null)
						.withTankOverlayLocation(null))
				.withComponent("data_type", panel -> new DecoImage(2, 1).withSize(16, 16))
				.withComponent("amount_arrows", panel -> new DecoArrows(panel.width-10, 1)
						.withSize(8, 16)
						.withBackground(DecoTextures.COMPONENT_ARROWS_PAPER)
						.withOnArrow(increase -> changeAmount(panel.getCurrentElement(), increase, panel, onChanged)))
				.withComponent("link", panel -> new DecoButton(2, 2)
						.withSize(panel.width-4, panel.height-4)
						.withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
						.withTextColor(IIColor.MC_BLUE, IIColor.MC_BLUE)
						.withOnLMBPressed(() -> activateLink(panel.getCurrentElement())))
				.withComponent("copy", panel -> actionButton(panel.width-70, DecoTextures.ICON_ACTION_DUPLICATE,
						"ii.gui.clipboard.action.copy", () -> copy(panel.getCurrentElement().getValue()))
						.withVisibility(panel::isMouseOver))
				.withComponent("edit", panel -> actionButton(panel.width-60, DecoTextures.ICON_ACTION_EDIT,
						"ii.gui.clipboard.action.edit", () -> {
							ClipboardEntry pasted = pasteEntry();
							if(pasted!=null)
							{
								panel.getCurrentElement().replaceWith(pasted);
								panel.refreshElement(panel.getCurrentElement());
								if(panel.getCurrentList()!=null)
									panel.getCurrentList().requestLayout();
								onChanged.accept(panel.getCurrentElement());
							}
						}).withVisibility(panel::isMouseOver))
				.withComponent("remove", panel -> actionButton(panel.width-50, DecoTextures.ICON_ACTION_REMOVE,
						"ii.gui.clipboard.action.remove", () -> onDelete.accept(panel.getCurrentElement()))
						.withVisibility(panel::isMouseOver))
				.withElementApplyMethod((entry, panel) -> {
					ClipboardProvider<?> provider = DecoClipboardUtils.getProvider(entry.getTypeId());
					Object value = entry.getValue();
					boolean link = provider!=null&&provider.isLink();
					boolean editableText = value instanceof String;
					boolean item = value instanceof ItemStack||value instanceof IngredientStack;
					boolean fluid = value instanceof FluidStack;
					boolean dust = value instanceof DustStack;
					boolean dataVariable = value instanceof DataVariable;
					boolean color = value instanceof IIColor;
					boolean hasAmount = item||fluid||dust;

					panel.label("preview").x = dataVariable||color?20: 4;
					panel.label("preview").y = (panel.height-10)/2;
					panel.label("preview").withWidth(panel.width-(dataVariable||color?24: 8));
					panel.label("preview").withRawText(entry.getPreview()).withTextColor(DecoColors.H1);
					panel.label("preview").visible = !link&&!editableText&&!item&&!fluid&&!dust;
					panel.label("visual_name").x = 20;
					panel.label("visual_name").y = 2;
					panel.label("visual_name").withSize(Math.max(1, panel.width-60), panel.height-4)
							.withRawText(visualName(value)).withTextColor(DecoColors.H1);
					panel.label("visual_name").visible = item||fluid||dust;
					panel.label("amount").x = panel.width-64;
					panel.label("amount").y = (panel.height-10)/2;
					panel.label("amount").withWidth(64-12);
					panel.label("amount").withRawText(amountText(value)).withTextColor(DecoColors.H1);
					panel.label("amount").visible = hasAmount;
					panel.component("color", DecoButton.class);
					panel.component("color", DecoButton.class).x = 3;
					panel.component("color", DecoButton.class).y = (panel.height-12)/2;
					panel.component("color", DecoButton.class).withBackground(DecoTextures.COMPONENT_COLOR);
					panel.component("color", DecoButton.class).withBackgroundColor(color?((IIColor)value): IIColor.WHITE);
					panel.component("color", DecoButton.class).visible = color;
					DecoTextArea textField = panel.component("text", DecoTextArea.class);
					textField.visible = editableText;
					textField.enabled = editableText;
					textField.withSize(panel.width-4, panel.height-4);
					String text = editableText?(String)value: "";
					if(!textField.getText().equals(text))
						textField.withText(text);
					DecoItemStackDisplay itemDisplay = panel.component("item", DecoItemStackDisplay.class);
					itemDisplay.visible = item;
					if(value instanceof ItemStack)
						itemDisplay.withStack((ItemStack)value);
					else if(value instanceof IngredientStack)
						itemDisplay.withStack((IngredientStack)value);
					else
						itemDisplay.withStack(ItemStack.EMPTY);
					DecoFluidTank fluidDisplay = panel.component("fluid", DecoFluidTank.class);
					fluidDisplay.visible = fluid;
					fluidDisplay.withFluidTank(fluid?fluidTank((FluidStack)value): null);
					DecoDustTank dustDisplay = panel.component("dust", DecoDustTank.class);
					dustDisplay.visible = dust;
					dustDisplay.withDustTank(dust?dustTank((DustStack)value): null,
							dust?Math.max(1, ((DustStack)value).amount): 1);
					DecoImage dataTypeIcon = panel.component("data_type", DecoImage.class);
					dataTypeIcon.visible = dataVariable;
					if(dataVariable)
						dataTypeIcon.withImageLocation(((DataVariable)value).getValue().getTextureLocation(), true);
					DecoArrows amountArrows = panel.component("amount_arrows", DecoArrows.class);
					amountArrows.x = panel.width-10;
					amountArrows.y = (panel.height-16)/2;
					amountArrows.visible = hasAmount;
					amountArrows.enabled = hasAmount;
					DecoButton linkButton = panel.component("link", DecoButton.class);
					linkButton.visible = link;
					linkButton.withSize(panel.width-4, panel.height-4);
					linkButton.withRawText(link&&value!=null?TextFormatting.UNDERLINE+entry.getPreview(): "");
				});
	}

	private static DecoLabel label(DecoEntryPanelBuilder<ClipboardEntry> panel, int x, int y, int width)
	{
		return new DecoLabel(IIClientUtils.fontRegular, x, y).withSize(width, 10).withTextColor(DecoColors.H1);
	}

	private static DecoButton actionButton(int x, net.minecraft.util.ResourceLocation icon, String tooltip, Runnable action)
	{
		return new DecoButton(x, 2).withSize(8, 8).withPadding(0, 0, 0, 0)
				.withIcon(icon, 8).withBackground(DecoTextures.COMPONENT_BUTTON_PAPER)
				.withTranslatedTooltip(tooltip).withOnLMBPressed(action);
	}

	private static int entryHeight(ClipboardEntry entry, int width)
	{
		Object value = entry.getValue();
		int fontHeight = IIClientUtils.fontRegular.FONT_HEIGHT;
		if(value instanceof String)
		{
			int lines = ((String)value).split("\\n", -1).length;
			return MathHelper.clamp(8+lines*fontHeight, 18, 8+6*fontHeight);
		}
		if(value instanceof ItemStack||value instanceof IngredientStack||value instanceof FluidStack)
		{
			int wrappedHeight = IIClientUtils.fontRegular.getWordWrappedHeight(visualName(value), Math.max(1, width-60));
			return MathHelper.clamp(wrappedHeight+4, 18, 4+4*fontHeight);
		}
		return 18;
	}

	private static String visualName(Object value)
	{
		if(value instanceof ItemStack)
			return ((ItemStack)value).getDisplayName();
		if(value instanceof IngredientStack)
		{
			ItemStack example = ((IngredientStack)value).getExampleStack();
			return example.isEmpty()?String.valueOf(value): example.getDisplayName();
		}
		if(value instanceof FluidStack)
			return ((FluidStack)value).getLocalizedName();
		if(value instanceof DustStack)
			return DustUtils.getDustName((DustStack)value);
		return "";
	}

	private static String amountText(Object value)
	{
		if(value instanceof ItemStack)
			return "x"+((ItemStack)value).getCount();
		if(value instanceof IngredientStack)
			return "x"+((IngredientStack)value).inputSize;
		if(value instanceof FluidStack)
			return ((FluidStack)value).amount+" mB";
		if(value instanceof DustStack)
			return ((DustStack)value).amount+" mB";
		return "";
	}

	private static void changeAmount(ClipboardEntry entry, boolean increase,
									 DecoEntryPanelBuilder<ClipboardEntry> panel, Consumer<ClipboardEntry> onChanged)
	{
		if(entry==null)
			return;
		Object value = entry.getValue();
		int delta = increase?1: -1;
		if(value instanceof ItemStack)
		{
			ItemStack stack = ((ItemStack)value).copy();
			stack.setCount(MathHelper.clamp(stack.getCount()+delta, 1, stack.getMaxStackSize()));
			value = stack;
		}
		else if(value instanceof IngredientStack)
		{
			IngredientStack ingredient = IngredientStack.readFromNBT(((IngredientStack)value).writeToNBT(new NBTTagCompound()));
			ingredient.inputSize = Math.max(1, ingredient.inputSize+delta);
			value = ingredient;
		}
		else if(value instanceof FluidStack)
		{
			FluidStack fluid = ((FluidStack)value).copy();
			fluid.amount = Math.max(1, fluid.amount+delta);
			value = fluid;
		}
		else
			return;

		ClipboardEntry replacement = createEntry(value);
		if(replacement!=null)
		{
			entry.replaceWith(replacement);
			panel.refreshElement(entry);
			panel.getCurrentList().requestLayout();
			onChanged.accept(entry);
		}
	}

	private static FluidTank fluidTank(FluidStack stack)
	{
		return new FluidTank(stack.copy(), Math.max(1, stack.amount));
	}

	private static DustTank dustTank(DustStack stack)
	{
		DustTank tank = new DustTank(Math.max(1, stack.amount));
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag("contents", stack.serializeNBT());
		tank.deserializeNBT(nbt);
		return tank;
	}

	private static void activateLink(ClipboardEntry entry)
	{
		if(entry==null)
			return;
		ClipboardProvider provider = getProvider(entry.getTypeId());
		Object value = entry.getValue();
		if(provider!=null&&provider.isLink()&&value!=null)
			provider.activate(value);
	}

	private static NBTTagCompound tagString(String key, String value)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString(key, value==null?"": value);
		return nbt;
	}

	private static <N extends Number> ClipboardProvider<N> numberProvider(String id, Class<N> type)
	{
		return new ClipboardProvider<>(id, type)
		{
			@Override
			public NBTTagCompound write(N value)
			{
				return tagString("number", value.toString());
			}

			@SuppressWarnings("unchecked")
			@Override
			public N read(NBTTagCompound nbt)
			{
				String value = nbt.getString("number");
				try
				{
					if(type==Integer.class) return (N)Integer.valueOf(value);
					if(type==Float.class) return (N)Float.valueOf(value);
					return (N)Double.valueOf(value);
				} catch(NumberFormatException ignored) {return null;}
			}

			@Override
			public String preview(N value)
			{
				return value.toString();
			}
		};
	}

	//--- Internal Utils ---//

	/**
	 * Sets the system clipboard string, with a fallback to Minecraft's clipboard handling
	 *
	 * @param string String to set
	 */
	private static void setClipboardString(String string)
	{
		try
		{
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(string), null);
		} catch(Exception ignored)
		{
			GuiScreen.setClipboardString(string);
		}
	}

	/**
	 * Sets NBT to the system clipboard, with a fallback to Minecraft's clipboard handling
	 *
	 * @param compound NBT to set
	 */
	private static void setClipboardNBT(NBTTagCompound compound)
	{
		setClipboardString(compound.toString());
	}

	/**
	 * Reads a string from the system clipboard, with a fallback to Minecraft's clipboard handling
	 *
	 * @return Clipboard string
	 */
	private static String getClipboardString()
	{
		try
		{
			Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
			if(t!=null&&t.isDataFlavorSupported(DataFlavor.stringFlavor))
				return (String)t.getTransferData(DataFlavor.stringFlavor);
		} catch(Exception ignored) {}
		return GuiScreen.getClipboardString();
	}

	/**
	 * Reads NBT from the system clipboard, with a fallback to Minecraft's clipboard handling
	 *
	 * @return Clipboard NBT
	 */
	private static NBTTagCompound getClipboardNBT()
	{
		String clipboardString = getClipboardString();
		try
		{
			return EasyNBT.parseNBT(clipboardString);
		} catch(Exception e)
		{
			return new NBTTagCompound();
		}
	}

}
