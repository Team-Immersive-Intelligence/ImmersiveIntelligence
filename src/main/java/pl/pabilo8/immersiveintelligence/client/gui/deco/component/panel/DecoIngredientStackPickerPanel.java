package pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoSwitch;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoItemStackDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.DecoTextField;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.util.TextFilter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * IngredientStack picker panel (ItemStack + settings).
 * <p>
 * Contains:
 * - ItemStack display (click with held item to set)
 * - Text fields for count and damage
 * - Toggle buttons: Match NBT, OreDictionary
 *
 * @author Pabilo8
 * @since 21.01.2026
 */
public class DecoIngredientStackPickerPanel extends DecoPanel
{
	@Nonnull
	protected IngredientStack stack = new IngredientStack(ItemStack.EMPTY);

	// Subcomponents
	protected DecoItemStackDisplay stackDisplay;
	protected DecoTextField countField, damageField;
	protected DecoSwitch toggleNBT, toggleOre;
	private Consumer<IngredientStack> onStackChanged;

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
		countField.withText(String.valueOf(Math.max(1, stack.inputSize)));
		damageField.withText(stack.stack.getMetadata()==OreDictionary.WILDCARD_VALUE?"": String.valueOf(Math.max(0, stack.stack.getMetadata())));
		toggleNBT.withCurrentState(this.stack.useNBT);
		toggleOre.withCurrentState(this.stack.oreName!=null);
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

	@Override
	protected boolean initialize()
	{
		if(!super.initialize())
			return false;

		// Simple internal layout (mirrors DecoColorPickerPanel style: a panel managing children)
		// Left: stack display
		addComponents(
				stackDisplay = new DecoItemStackDisplay(4, 4)
						.withSize(32, 32)
						.withPadding(new int[]{0, 0, 0, 0})
						.withIconSize(24)
						.withBackgroundTexture(DecoTextures.GUI_BG_DARK)
						.withIconAlignment(DecoAlignment.CENTER)
						.withStack(stack)
						.withOnPressed((gui, button, mouseX, mouseY) -> {
							if(button!=MouseButton.LEFT||parentGui==null)
								return false;
							ItemStack held = parentGui.getMouseHeldItemStack();
							if(!held.isEmpty())
							{
								withIngredientStack(new IngredientStack(held.copy()));
								applyToUI();
								return true;
							}
							return false;
						})
		);

		addLabel("Count:", 32+2+4, 2)
				.withSize(48-4, 18)
				.withAlign(DecoAlignment.LEFT);
		addLabel("Damage:", 32+2+4, 4+16)
				.withSize(48-4, 18)
				.withAlign(DecoAlignment.LEFT);

		addComponents(
				countField = new DecoTextField(32+2+48, 2)
						.withSize(width-(32+2+48+4), 16)
						.withFilter(TextFilter.DECIMAL)
						.withText(String.valueOf(stack.inputSize))
						.withOnTextChanged(string -> withCount(IIStringUtil.parseInt(string)))
						.withTranslatedTooltip("The amount of items in the stack"),
				damageField = new DecoTextField(32+2+48, 4+16)
						.withSize(width-(32+2+48+4), 16)
						.withFilter(TextFilter.DECIMAL)
						.withText(stack.stack.getMetadata())
						.withOnTextChanged(string -> withDamage(IIStringUtil.parseInt(string)))
						.withTranslatedTooltip("The damage value of the item (leave empty for wildcard)")
		);
		addComponents(
				toggleNBT = new DecoSwitch(4, height-16-2)
						.withSize(width/2-8, 16)
						.withText("NBT")
						.withOnToggle(this::withMatchNBT)
						.withTranslatedTooltip("When active, the item is matched by its NBT data"),
				toggleOre = new DecoSwitch(4+width/2, height-16-2)
						.withSize(width/2-8, 16)
						.withText("OreDict")
						.withOnToggle(this::withOreDictBased)
						.withTranslatedTooltip("When active, the item is matched by its OreDictionary name")
		);

		applyToUI();
		return true;
	}

	private void applyToUI()
	{
		this.stack.stack.setCount(this.stack.inputSize);
		this.stackDisplay.withStack(this.stack);
		if(this.onStackChanged!=null)
			this.onStackChanged.accept(this.stack);
	}

	@Nonnull
	public ItemStack getItemStack()
	{
		return this.stack.getExampleStack();
	}

	@Nonnull
	public IngredientStack getIngredientStack()
	{
		return stack;
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
}

