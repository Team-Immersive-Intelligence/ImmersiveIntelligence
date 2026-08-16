package pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.LogisticTag;
import pl.pabilo8.immersiveintelligence.api.crafting.IngredientReference;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFluidStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSlider;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoSprite;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Edits {@link IngredientReference} item, fluid, energy, and logistics filters.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 12.08.2026
 * @ii-approved 0.3.1
 * @since 21.01.2026
 */
public class DecoIngredientStackPickerPanel extends DecoPanel
{
	private static final String TRANSLATION_KEY = IIReference.GUI_LABEL_KEY+"itemstack_editor.";
	@Nonnull
	protected IngredientReference stack = new IngredientReference();
	@Nonnull
	protected PickerPanelMode mode = PickerPanelMode.ITEM;

	protected DecoItemStackDisplay stackDisplay;
	protected DecoFluidTank fluidDisplay;
	protected DecoTextField countField, metadataField;
	protected DecoSwitch toggleNBT, toggleOre;
	protected DecoSlider energySlider;
	protected DecoTextField energyField, fluidNameField;

	private Consumer<? super IngredientReference> onStackChanged;
	private int maxEnergyCount = 1000000;

	public DecoIngredientStackPickerPanel(int x, int y)
	{
		super(x, y);
		withSize(128, 32);
	}

	public DecoIngredientStackPickerPanel withStack(@Nonnull ItemStack stack)
	{
		return withIngredientReference(new IngredientReference(stack.copy()));
	}

	public DecoIngredientStackPickerPanel withIngredientStack(@Nonnull IngredientStack stack)
	{
		return withIngredientReference(IngredientReference.fromIngredientStack(stack));
	}

	/**
	 * Sets the ingredient reference edited by this panel.
	 */
	public DecoIngredientStackPickerPanel withIngredientReference(@Nonnull IngredientReference stack)
	{
		this.stack = stack.clone();
		this.stack.inputSize = clampInteger(this.stack.inputSize, getMinimumCount(), Integer.MAX_VALUE);
		if(isItemDataTypeMode())
		{
			this.stack.oreName = null;
			if(this.stack.stack==null)
				this.stack.stack = ItemStack.EMPTY;
			this.stack.withLogisticTag(null);
		}

		applyToUI();
		if(!initialized)
			return this;

		if(countField!=null)
			countField.withText(String.valueOf(Math.max(getMinimumCount(), this.stack.inputSize)));
		switch(mode)
		{
			case ITEM:
			case ITEM_LOGISTIC_TAG:
			case ITEM_DATA_TYPE:
			{
				if(metadataField!=null&&this.stack.stack!=null)
					metadataField.withText(this.stack.stack.getMetadata()==OreDictionary.WILDCARD_VALUE?"": String.valueOf(Math.max(0, this.stack.stack.getMetadata())));
				if(toggleNBT!=null)
					toggleNBT.withCurrentState(isItemDataTypeMode()?hasNonEmptyTag(this.stack.stack): this.stack.useNBT);
				if(toggleOre!=null)
					toggleOre.withCurrentState(this.stack.oreName!=null);

			}
			break;
			case FLUID:
			{
				if(toggleNBT!=null)
					toggleNBT.withCurrentState(this.stack.useNBT);
			}
			break;
			case ENERGY:
			{
				if(energyField!=null)
					energyField.withText(String.valueOf(this.stack.inputSize));
				if(energySlider!=null)
					energySlider.withValue(Math.min(this.stack.inputSize, maxEnergyCount));
			}
		}
		return this;
	}

	public DecoIngredientStackPickerPanel withDataType(@Nonnull DataType dataType)
	{
		if(dataType instanceof DataTypeItemStack)
			return withItemStackDataType((DataTypeItemStack)dataType);
		if(dataType instanceof DataTypeFluidStack)
			return withFluidStackDataType((DataTypeFluidStack)dataType);
		return this;
	}

	public DecoIngredientStackPickerPanel withItemStackDataType(@Nonnull DataTypeItemStack dataType)
	{
		ItemStack itemStack = dataType.value==null?ItemStack.EMPTY: dataType.value.copy();
		IngredientReference ingredientStack = new IngredientReference(itemStack);
		ingredientStack.inputSize = itemStack.isEmpty()?1: Math.max(1, itemStack.getCount());
		ingredientStack.oreName = null;
		ingredientStack.useNBT = false;
		return withIngredientStack(ingredientStack);
	}

	public DecoIngredientStackPickerPanel withFluidStackDataType(@Nonnull DataTypeFluidStack dataType)
	{
		IngredientReference ingredientStack;
		if(dataType.value==null)
			ingredientStack = new IngredientReference();
		else
		{
			ingredientStack = new IngredientReference(dataType.value.copy()).setUseNBT(dataType.value.tag!=null);
			ingredientStack.inputSize = Math.max(1, dataType.value.amount);
		}
		return withIngredientStack(ingredientStack);
	}

	public DecoIngredientStackPickerPanel withMaxEnergy(int maxEnergy)
	{
		this.maxEnergyCount = Math.max(0, maxEnergy);
		return this;
	}

	public DecoIngredientStackPickerPanel withCount(int count)
	{
		this.stack.inputSize = clampInteger(count, getMinimumCount(), Integer.MAX_VALUE);
		applyToUI();
		return this;
	}

	public DecoIngredientStackPickerPanel withMetadata(int metadata)
	{
		if(this.stack.stack!=null)
		{
			this.stack.stack = this.stack.stack.copy();
			this.stack.stack.setItemDamage(clampInteger(metadata, 0, Integer.MAX_VALUE));
			applyToUI();
		}
		return this;
	}

	@Deprecated
	public DecoIngredientStackPickerPanel withDamage(int damage)
	{
		return withMetadata(damage);
	}

	public DecoIngredientStackPickerPanel withMatchNBT(boolean match)
	{
		if(isItemDataTypeMode())
		{
			if(!match&&this.stack.stack!=null&&!this.stack.stack.isEmpty())
			{
				this.stack.stack = this.stack.stack.copy();
				this.stack.stack.setTagCompound(null);
			}
		}
		else
			this.stack.useNBT = match;
		applyToUI();
		return this;
	}

	public DecoIngredientStackPickerPanel withOreDictBased(boolean oreDict)
	{
		if(isItemDataTypeMode())
			this.stack.oreName = null;
		else if(oreDict&&this.stack.stack!=null&&!this.stack.stack.isEmpty())
			this.stack.oreName = findFirstOreName(this.stack.stack);
		else if(this.stack.stack!=null&&!this.stack.stack.isEmpty())
			this.stack.oreName = null;
		applyToUI();
		return this;
	}

	public DecoIngredientStackPickerPanel withOnStackChanged(Consumer<? super IngredientReference> onStackChanged)
	{
		this.onStackChanged = onStackChanged;
		return this;
	}

	private boolean isFluidMode()
	{
		return mode==PickerPanelMode.FLUID;
	}

	private boolean isEnergyMode()
	{
		return mode==PickerPanelMode.ENERGY;
	}

	private boolean isLogisticTagMode()
	{
		return mode==PickerPanelMode.ITEM_LOGISTIC_TAG;
	}

	private boolean isItemDataTypeMode()
	{
		return mode==PickerPanelMode.ITEM_DATA_TYPE;
	}

	public DecoIngredientStackPickerPanel withMode(@Nonnull PickerPanelMode mode)
	{
		this.mode = mode;
		this.initialized = false;
		return this;
	}

	public DecoIngredientStackPickerPanel withFluidMode(boolean fluidMode)
	{
		return withMode(fluidMode?PickerPanelMode.FLUID: PickerPanelMode.ITEM);
	}

	public DecoIngredientStackPickerPanel withEnergyMode(boolean energyMode)
	{
		return withMode(energyMode?PickerPanelMode.ENERGY: PickerPanelMode.ITEM);
	}

	public DecoIngredientStackPickerPanel withLogisticTagMode(boolean logisticTagMode)
	{
		return withMode(logisticTagMode?PickerPanelMode.ITEM_LOGISTIC_TAG: PickerPanelMode.ITEM);
	}

	public DecoIngredientStackPickerPanel withItemDataTypeMode(boolean itemDataTypeMode)
	{
		return withMode(itemDataTypeMode?PickerPanelMode.ITEM_DATA_TYPE: PickerPanelMode.ITEM);
	}

	@Override
	protected boolean initialize()
	{
		if(!super.initialize())
			return false;

		switch(mode)
		{
			case ENERGY:
			{
				//no item/fluid picker; just IF amount controls.
				addLabel(TRANSLATION_KEY+"energy", 4+16, 2)
						.withSize(width-8, 18)
						.withAlign(DecoAlignment.LEFT);

				addComponents(
						new DecoImage(4, 2)
								.withImageLocation(DecoTextures.ICON_ENERGY, true)
								.withSize(16, 16),
						energyField = new DecoTextField(4, 2+16)
								.withSize(width-4-4, 16)
								.withFilter(TextFilter.DECIMAL)
								.withText(String.valueOf(Math.max(0, stack.inputSize)))
								.withOnTextChanged(this::handleEnergyTextChanged)
								.withTranslatedTooltip(TRANSLATION_KEY+"energy.tooltip"),
						energySlider = new DecoSlider(4, 2+16+18)
								.withSize(width-4-4, 16)
								.withRange(0, maxEnergyCount)
								.withValue(Math.min(Math.max(0, stack.inputSize), maxEnergyCount))
								.withBarColors(IIColor.fromPackedRGB(0x663f26), IIColor.fromPackedRGB(0xb37e28))
								.withOnValueChanged(value -> {
									withCount(value.intValue());
									if(energyField!=null)
										energyField.withText(String.valueOf(Math.max(0, stack.inputSize)));
								})
				);
			}
			break;
			case FLUID:
			{
				addComponents(
						fluidDisplay = new DecoFluidTank(4, 4)
								.withSize(32, 32)
								.withOnPressed((gui, button, mouseX, mouseY) -> {
									//Reset stack
									if(button==MouseButton.RIGHT||(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&button==MouseButton.LEFT))
									{
										withIngredientReference(new IngredientReference("*", Math.max(1, stack.inputSize)));
										return true;
									}
									if(button!=MouseButton.LEFT||parentGui==null)
										return false;

									ItemStack held = parentGui.getMouseHeldItemStack();
									if(held.isEmpty())
										return false;

									FluidStack fluid = FluidUtil.getFluidContained(held);
									if(fluid!=null)
									{
										IngredientReference fluidReference = new IngredientReference(fluid)
												.setUseNBT(fluid.tag!=null);
										withIngredientReference(fluidReference);
										applyToUI();
										return true;
									}
									return false;
								})
				);

				//shared "count" only (no metadata in fluid mode)
				addLabel(TRANSLATION_KEY+"count", 32+2+4, 2)
						.withSize(48-4, 18)
						.withAlign(DecoAlignment.LEFT);

				addComponents(
						fluidNameField = new DecoTextField(32+2+4, 2+16)
								.withSize(width-32-4-4, 16)
								.withFilter(TextFilter.DECIMAL)
								.withDisabled(true),
						countField = new DecoTextField(32+2+48, 2)
								.withSize(width-(32+2+48+4), 16)
								.withFilter(TextFilter.DECIMAL)
								.withText(String.valueOf(Math.max(1, stack.inputSize)))
								.withOnTextChanged(this::handleCountTextChanged)
								.withTranslatedTooltip(TRANSLATION_KEY+"count.tooltip")
				);
			}
			break;
			case ITEM:
			case ITEM_LOGISTIC_TAG:
			case ITEM_DATA_TYPE:
			default:
			{
				addComponents(
						stackDisplay = new DecoItemStackDisplay(4, 4)
								.withSize(32, 32)
								.withPadding(new int[]{0, 0, 0, 0})
								.withIconSize(24)
								.withBackgroundTexture(DecoSprite.atlasSprite(DecoTextures.BG_DARK, 32, true))
								.withIconAlignment(DecoAlignment.CENTER)
								.withOnPressed((gui, button, mouseX, mouseY) -> {
									//Reset stack
									if(button==MouseButton.RIGHT||(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&button==MouseButton.LEFT))
									{
										withIngredientReference(new IngredientReference());
										applyToUI();
										return true;
									}

									if(button!=MouseButton.LEFT||parentGui==null)
										return false;

									ItemStack held = parentGui.getMouseHeldItemStack();
									if(held.isEmpty())
										return false;

									if(isLogisticTagMode()&&LogisticTag.hasLogisticsTag(held))
									{
										LogisticTag logisticTag = LogisticTag.getLogisticsTagFromStack(held);
										withIngredientReference(new IngredientReference().withLogisticTag(logisticTag));
									}
									else
										withIngredientReference(new IngredientReference(held.copy()));
									applyToUI();
									return true;
								})
				);

				addLabel(TRANSLATION_KEY+"count", 32+2+4, 2)
						.withSize(48-4, 18)
						.withAlign(DecoAlignment.LEFT);
				addLabel(TRANSLATION_KEY+"metadata", 32+2+4, 4+16)
						.withSize(48-4, 18)
						.withAlign(DecoAlignment.LEFT);

				addComponents(
						countField = new DecoTextField(32+2+48, 2)
								.withSize(width-(32+2+48+4), 16)
								.withFilter(TextFilter.DECIMAL)
								.withText(String.valueOf(Math.max(1, stack.inputSize)))
								.withOnTextChanged(this::handleCountTextChanged)
								.withTranslatedTooltip(TRANSLATION_KEY+"count.tooltip"),
						metadataField = new DecoTextField(32+2+48, 4+16)
								.withSize(width-(32+2+48+4), 16)
								.withFilter(TextFilter.DECIMAL)
								.withText(stack.stack!=null&&stack.stack.getMetadata()!=OreDictionary.WILDCARD_VALUE?String.valueOf(stack.stack.getMetadata()): "")
								.withOnTextChanged(this::handleMetadataTextChanged)
								.withTranslatedTooltip(TRANSLATION_KEY+"metadata.tooltip")
				);

				addComponents(
						toggleNBT = new DecoSwitch(4, height-16-2)
								.withSize(isItemDataTypeMode()?width-8: width/2-8, 16)
								.withText(TRANSLATION_KEY+"nbt")
								.withOnToggle(this::withMatchNBT)
								.withTranslatedTooltip(TRANSLATION_KEY+(isItemDataTypeMode()?"nbt.data_type.tooltip": "nbt.item.tooltip"))
				);

				if(!isItemDataTypeMode())
					addComponents(
							toggleOre = new DecoSwitch(4+width/2, height-16-2)
									.withSize(width/2-8, 16)
									.withText(TRANSLATION_KEY+"oredict")
									.withOnToggle(this::withOreDictBased)
									.withTranslatedTooltip(TRANSLATION_KEY+"oredict.tooltip")
					);
			}
			break;
		}

		initialized = true;
		applyToUI();
		return true;
	}

	private void applyToUI()
	{
		if(!initialized)
			return;

		switch(mode)
		{
			case ITEM:
			case ITEM_LOGISTIC_TAG:
			case ITEM_DATA_TYPE:
			{
				boolean fieldsHidden = isLogisticTagMode()&&stack.hasLogisticTag();
				if(metadataField!=null)
					metadataField.visible = !fieldsHidden;
				if(toggleOre!=null)
					toggleOre.visible = !fieldsHidden&&!isItemDataTypeMode();
				if(toggleNBT!=null)
				{
					toggleNBT.visible = !fieldsHidden;
					toggleNBT.withCurrentState(isItemDataTypeMode()?hasNonEmptyTag(this.stack.stack): this.stack.useNBT);
				}

				if(this.stack.stack!=null&&!this.stack.stack.isEmpty())
					this.stack.stack.setCount(this.stack.inputSize);
				if(isItemDataTypeMode())
				{
					this.stack.oreName = null;
					if(this.stack.stack==null)
						this.stack.stack = ItemStack.EMPTY;
					this.stack.withLogisticTag(null);
					this.stack.useNBT = false;
				}
				if(stackDisplay!=null)
				{
					ItemStack display = stack.hasLogisticTag()?
							IIContent.itemLogisticTag.getStack(stack.getLogisticTag(), 1): stack.getExampleStack();
					stackDisplay.withStack(display);
				}
			}
			break;
			case FLUID:
			{
				if(stack.fluid!=null)
					this.stack.fluid.amount = this.stack.inputSize;
				if(fluidDisplay!=null)
					fluidDisplay.withFluidTank(new FluidTank(stack.fluid, stack.fluid!=null?getDisplayTankCapacity(stack.fluid.amount): 1));
				if(fluidNameField!=null)
					fluidNameField.withText(stack.fluid!=null?FluidRegistry.getFluidName(stack.fluid): "*");
			}
			break;
			case ENERGY:
			{
				stack.inputSize = Math.max(0, stack.inputSize);

				if(energyField!=null)
					energyField.withText(String.valueOf(stack.inputSize)).visible = true;
				if(energySlider!=null)
					energySlider.withValue(Math.min(stack.inputSize, maxEnergyCount)).visible = true;
			}
			break;
		}

		if(this.onStackChanged!=null)
			this.onStackChanged.accept(this.stack);
	}

	@Nonnull
	public ItemStack getItemStack()
	{
		if(isFluidMode()||isEnergyMode())
			return ItemStack.EMPTY;
		if(isLogisticTagMode()&&stack.hasLogisticTag())
			return IIContent.itemLogisticTag.getStack(stack.getLogisticTag(), 1);
		if(isItemDataTypeMode()&&stack.stack!=null)
			return stack.stack.copy();
		return stack.getExampleStack();
	}

	/**
	 * @return the edited ingredient reference
	 */
	@Nonnull
	public IngredientReference getIngredientReference()
	{
		return stack;
	}

	@Nonnull
	public IngredientReference getIngredientStack()
	{
		return getIngredientReference();
	}

	@Nonnull
	public DataType getDataType()
	{
		switch(mode)
		{
			case FLUID:
				return getFluidStackDataType();
			case ITEM:
			case ITEM_LOGISTIC_TAG:
			case ITEM_DATA_TYPE:
			default:
				return getItemStackDataType();
		}
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public <T extends DataType> T getDataType(@Nonnull T dataType)
	{
		if(dataType instanceof DataTypeItemStack)
			return (T)getItemStackDataType((DataTypeItemStack)dataType);
		if(dataType instanceof DataTypeFluidStack)
			return (T)getFluidStackDataType((DataTypeFluidStack)dataType);
		return dataType;
	}

	@Nonnull
	public DataTypeItemStack getItemStackDataType()
	{
		return getItemStackDataType(new DataTypeItemStack());
	}

	@Nonnull
	public DataTypeItemStack getItemStackDataType(@Nonnull DataTypeItemStack dataType)
	{
		dataType.value = getItemStack();
		return dataType;
	}

	@Nonnull
	public DataTypeFluidStack getFluidStackDataType()
	{
		return getFluidStackDataType(new DataTypeFluidStack());
	}

	@Nonnull
	public DataTypeFluidStack getFluidStackDataType(@Nonnull DataTypeFluidStack dataType)
	{
		dataType.value = stack.fluid==null?null: stack.fluid.copy();
		if(dataType.value!=null)
			dataType.value.amount = Math.max(1, stack.inputSize);
		return dataType;
	}

	@Nullable
	public LogisticTag getLogisticTag()
	{
		return isLogisticTagMode()?stack.getLogisticTag(): null;
	}

	@Nullable
	private static String findFirstOreName(@Nonnull ItemStack stack)
	{
		try
		{
			if(stack.isEmpty())
				return null;
			int[] ids = OreDictionary.getOreIDs(stack);
			if(ids.length==0)
				return null;
			return OreDictionary.getOreName(ids[0]);
		} catch(Exception ignored)
		{
			return null;
		}
	}

	private void handleCountTextChanged(String string)
	{
		int count = parseClampedInteger(string, getMinimumCount());
		withCount(count);
		syncIntegerField(countField, count, string);
	}

	private void handleMetadataTextChanged(String string)
	{
		if(string.isEmpty())
		{
			withMetadata(OreDictionary.WILDCARD_VALUE);
			return;
		}

		int metadata = parseClampedInteger(string, 0);
		withMetadata(metadata);
		syncIntegerField(metadataField, metadata, string);
	}

	private void handleEnergyTextChanged(String string)
	{
		int energy = parseClampedInteger(string, 0);
		withCount(energy);
		if(energySlider!=null)
			energySlider.withValue(Math.min(stack.inputSize, maxEnergyCount));
		syncIntegerField(energyField, energy, string);
	}

	private int getMinimumCount()
	{
		return isEnergyMode()?0: 1;
	}

	private static int parseClampedInteger(@Nullable String string, int minValue)
	{
		if(string==null||string.isEmpty())
			return minValue;

		long value = 0;
		boolean hasDigit = false;
		for(int i = 0; i < string.length(); i++)
		{
			char c = string.charAt(i);
			if(c < '0'||c > '9')
				continue;
			hasDigit = true;
			value = value*10+(c-'0');
			if(value >= Integer.MAX_VALUE)
				return Integer.MAX_VALUE;
		}
		return hasDigit?clampInteger((int)value, minValue, Integer.MAX_VALUE): minValue;
	}

	private static int clampInteger(int value, int minValue, int maxValue)
	{
		return Math.max(minValue, Math.min(maxValue, value));
	}

	private static int getDisplayTankCapacity(int amount)
	{
		if(amount >= Integer.MAX_VALUE/2)
			return Integer.MAX_VALUE;
		return Math.max(1, amount*2);
	}

	private static boolean hasNonEmptyTag(@Nullable ItemStack itemStack)
	{
		return itemStack!=null&&!itemStack.isEmpty()&&itemStack.hasTagCompound()&&!itemStack.getTagCompound().hasNoTags();
	}

	private static void syncIntegerField(@Nullable DecoTextField field, int value, @Nullable String originalText)
	{
		if(field==null)
			return;
		String clampedText = String.valueOf(value);
		if(!clampedText.equals(originalText))
			field.withText(clampedText);
	}

	@Override
	public void onGuiEvent(DecoGuiEvent event)
	{
		switch(event)
		{
			case COPY:
				DecoGuiUtils.setClipboardNBT(getIngredientReference().serializeNBT());
				break;
			case CUT:
				DecoGuiUtils.setClipboardNBT(getIngredientReference().serializeNBT());
				withIngredientReference(new IngredientReference());
				break;
			case PASTE:
				NBTTagCompound nbt = DecoGuiUtils.getClipboardNBT();
				if(!nbt.hasNoTags())
					withIngredientReference(IngredientReference.readFromNBT(nbt));
				break;
			default:
				super.onGuiEvent(event);
				break;
		}
	}

	/**
	 * Represents the mode of the picker panel.
	 */
	public enum PickerPanelMode
	{
		ITEM,
		ITEM_LOGISTIC_TAG,
		ITEM_DATA_TYPE,
		FLUID,
		ENERGY
	}
}
