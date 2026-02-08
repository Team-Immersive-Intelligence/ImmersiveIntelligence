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
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSlider;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.visual.DecoImage;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * {@link IngredientStack} picker panel ({@link ItemStack} + settings). Supports {@link FluidStack} picking via {@link #isFluidMode}.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 21.01.2026
 */
public class DecoIngredientStackPickerPanel extends DecoPanel
{
	private static final String TRANSLATION_KEY = IIReference.GUI_LABEL_KEY+"itemstack_editor.";
	@Nonnull
	protected IngredientStack stack = new IngredientStack(ItemStack.EMPTY);
	@Nonnull
	protected PickerPanelMode mode = PickerPanelMode.ITEM;

	protected DecoItemStackDisplay stackDisplay;
	protected DecoFluidTank fluidDisplay;
	protected DecoTextField countField, damageField;
	protected DecoSwitch toggleNBT, toggleOre;
	protected DecoSlider energySlider;
	protected DecoTextField energyField, fluidNameField;

	private Consumer<IngredientStack> onStackChanged;
	private int maxEnergyCount = 1000000;

	public DecoIngredientStackPickerPanel(int x, int y)
	{
		super(x, y);
		withSize(128, 32);
	}

	public DecoIngredientStackPickerPanel withStack(@Nonnull ItemStack stack)
	{
		return withIngredientStack(new IngredientStack(stack.copy()));
	}

	public DecoIngredientStackPickerPanel withIngredientStack(@Nonnull IngredientStack stack)
	{
		this.stack = stack;
		applyToUI();
		if(!initialized)
			return this;

		countField.withText(String.valueOf(Math.max(1, stack.inputSize)));
		switch(mode)
		{
			case ITEM:
			case ITEM_LOGISTIC_TAG:
			{
				damageField.withText(stack.stack.getMetadata()==OreDictionary.WILDCARD_VALUE?"": String.valueOf(Math.max(0, stack.stack.getMetadata())));
				toggleNBT.withCurrentState(this.stack.useNBT);
				toggleOre.withCurrentState(this.stack.oreName!=null);

			}
			break;
			case FLUID:
			{
				toggleNBT.withCurrentState(this.stack.useNBT);
			}
			break;
			case ENERGY:
			{
				energyField.withText(String.valueOf(stack.inputSize));
				energySlider.withValue(stack.inputSize);
			}
		}
		return this;
	}

	public DecoIngredientStackPickerPanel withMaxEnergy(int maxEnergy)
	{
		this.maxEnergyCount = maxEnergy;
		return this;
	}

	public DecoIngredientStackPickerPanel withCount(int count)
	{
		this.stack.inputSize = Math.max(1, count);
		applyToUI();
		return this;
	}

	public DecoIngredientStackPickerPanel withDamage(int damage)
	{
		if(this.stack.stack!=null)
		{
			this.stack.stack = this.stack.stack.copy();
			this.stack.stack.setItemDamage(Math.max(0, damage));
			applyToUI();
		}
		return this;
	}

	public DecoIngredientStackPickerPanel withMatchNBT(boolean match)
	{
		this.stack.useNBT = match;
		applyToUI();
		return this;
	}

	public DecoIngredientStackPickerPanel withOreDictBased(boolean oreDict)
	{
		if(oreDict)
			this.stack.oreName = findFirstOreName(this.stack.stack);
		else
			this.stack.oreName = null;
		applyToUI();
		return this;
	}

	public DecoIngredientStackPickerPanel withOnStackChanged(Consumer<IngredientStack> onStackChanged)
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
								.withImageLocation(DecoTextures.RES_ICON_ENERGY, true)
								.withSize(16, 16),
						energyField = new DecoTextField(4, 2+16)
								.withSize(width-4-4, 16)
								.withFilter(TextFilter.DECIMAL)
								.withText(String.valueOf(Math.max(0, stack.inputSize)))
								.withOnTextChanged(string -> {
									withCount(Math.max(0, IIStringUtil.parseInt(string)));
									if(energySlider!=null)
										energySlider.withValue(Math.max(0, stack.inputSize));
								})
								.withTranslatedTooltip(TRANSLATION_KEY+"energy.tooltip"),
						energySlider = new DecoSlider(4, 2+16+18)
								.withSize(width-4-4, 16)
								.withRange(0, maxEnergyCount)
								.withValue(Math.max(0, stack.inputSize))
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
										stack.fluid = null;
										applyToUI();
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
										IngredientStack fluidIng = new IngredientStack(fluid);
										fluidIng.inputSize = fluid.amount;
										withIngredientStack(fluidIng);
										applyToUI();
										return true;
									}
									return false;
								})
				);

				//shared "count" only (no damage in fluid mode)
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
								.withText(String.valueOf(stack.inputSize))
								.withOnTextChanged(string -> withCount(IIStringUtil.parseInt(string)))
								.withTranslatedTooltip(TRANSLATION_KEY+"count.tooltip")
				);

				//toggles not applicable (kept for backward behavior: allow NBT toggle in fluid mode if you really want)
				addComponents(
						toggleNBT = new DecoSwitch(4, height-16-2)
								.withSize(width/2-8, 16)
								.withText(TRANSLATION_KEY+"nbt")
								.withOnToggle(this::withMatchNBT)
								.withTranslatedTooltip(TRANSLATION_KEY+"nbt.fluid.tooltip")
				);
			}
			break;
			case ITEM:
			case ITEM_LOGISTIC_TAG:
			default:
			{
				addComponents(
						stackDisplay = new DecoItemStackDisplay(4, 4)
								.withSize(32, 32)
								.withPadding(new int[]{0, 0, 0, 0})
								.withIconSize(24)
								.withBackgroundTexture(DecoTextures.GUI_BG_DARK)
								.withIconAlignment(DecoAlignment.CENTER)
								.withOnPressed((gui, button, mouseX, mouseY) -> {
									//Reset stack
									if(button==MouseButton.RIGHT||(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&button==MouseButton.LEFT))
									{
										withIngredientStack(new IngredientStack(ItemStack.EMPTY));
										applyToUI();
										return true;
									}

									if(button!=MouseButton.LEFT||parentGui==null)
										return false;

									ItemStack held = parentGui.getMouseHeldItemStack();
									if(held.isEmpty())
										return false;

									withIngredientStack(new IngredientStack(held.copy()));
									applyToUI();
									return true;
								})
				);

				addLabel(TRANSLATION_KEY+"count", 32+2+4, 2)
						.withSize(48-4, 18)
						.withAlign(DecoAlignment.LEFT);
				addLabel(TRANSLATION_KEY+"damage", 32+2+4, 4+16)
						.withSize(48-4, 18)
						.withAlign(DecoAlignment.LEFT);

				addComponents(
						countField = new DecoTextField(32+2+48, 2)
								.withSize(width-(32+2+48+4), 16)
								.withFilter(TextFilter.DECIMAL)
								.withText(String.valueOf(stack.inputSize))
								.withOnTextChanged(string -> withCount(IIStringUtil.parseInt(string)))
								.withTranslatedTooltip(TRANSLATION_KEY+"count.tooltip"),
						damageField = new DecoTextField(32+2+48, 4+16)
								.withSize(width-(32+2+48+4), 16)
								.withFilter(TextFilter.DECIMAL)
								.withText(stack.stack.getMetadata())
								.withOnTextChanged(string -> withDamage(IIStringUtil.parseInt(string)))
								.withTranslatedTooltip(TRANSLATION_KEY+"damage.tooltip")
				);

				addComponents(
						toggleNBT = new DecoSwitch(4, height-16-2)
								.withSize(width/2-8, 16)
								.withText(TRANSLATION_KEY+"nbt")
								.withOnToggle(this::withMatchNBT)
								.withTranslatedTooltip(TRANSLATION_KEY+"nbt.item.tooltip"),
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
			{
				boolean fieldsHidden = isLogisticTagMode()&&getLogisticTag()!=null;
				damageField.visible = !fieldsHidden;
				toggleOre.visible = !fieldsHidden;
				toggleNBT.visible = !fieldsHidden;

				this.stack.stack.setCount(this.stack.inputSize);
				if(stackDisplay!=null)
					stackDisplay.withStack(stack);
			}
			break;
			case FLUID:
			{
				if(stack.fluid!=null)
					this.stack.fluid.amount = this.stack.inputSize;
				if(fluidDisplay!=null)
					fluidDisplay.withFluidTank(new FluidTank(stack.fluid, stack.fluid!=null?stack.fluid.amount*2: 1));
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
					energySlider.withValue(stack.inputSize).visible = true;
			}
			break;
		}

		if(this.onStackChanged!=null)
			this.onStackChanged.accept(this.stack);
	}

	@Nonnull
	public ItemStack getItemStack()
	{
		return isFluidMode()?ItemStack.EMPTY: stack.getExampleStack();
	}

	@Nonnull
	public IngredientStack getIngredientStack()
	{
		return stack;
	}

	public LogisticTag getLogisticTag()
	{
		if(isLogisticTagMode()&&stack.getExampleStack().getItem()==IIContent.itemLogisticTag)
		{
			LogisticTag tag = LogisticTag.getLogisticsTagFromStack(getItemStack());
			return tag==null?new LogisticTag(): tag;
		}
		return null;
	}

	@Nullable
	private static String findFirstOreName(@Nonnull ItemStack stack)
	{
		try
		{
			int[] ids = OreDictionary.getOreIDs(stack);
			if(ids.length==0)
				return null;
			return OreDictionary.getOreName(ids[0]);
		} catch(Exception ignored)
		{
			return null;
		}
	}

	@Override
	public void onGuiEvent(DecoGuiEvent event)
	{
		switch(event)
		{
			case COPY:
				DecoGuiUtils.setClipboardNBT(getIngredientStack().writeToNBT(new NBTTagCompound()));
				break;
			case CUT:
				DecoGuiUtils.setClipboardNBT(getIngredientStack().writeToNBT(new NBTTagCompound()));
				withIngredientStack(new IngredientStack(ItemStack.EMPTY));
				break;
			case PASTE:
				NBTTagCompound nbt = DecoGuiUtils.getClipboardNBT();
				if(!nbt.hasNoTags())
					withIngredientStack(IngredientStack.readFromNBT(nbt));
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
		FLUID,
		ENERGY
	}
}
