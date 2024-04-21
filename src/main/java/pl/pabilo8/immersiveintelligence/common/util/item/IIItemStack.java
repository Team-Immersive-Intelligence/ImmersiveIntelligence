package pl.pabilo8.immersiveintelligence.common.util.item;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IICategory;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 20/04/2024 - 11:49 AM
 * Small dirty class for dirty purposes
 */
public class IIItemStack
{
	private final ItemStack stack;
	private final IICategory category;

	public IIItemStack(ItemStack stack, IICategory category)
	{
		this.stack = stack;
		this.category = category;
	}

	public ItemStack stack()
	{
		return this.stack;
	}

	public IICategory category()
	{
		return this.category;
	}
}
