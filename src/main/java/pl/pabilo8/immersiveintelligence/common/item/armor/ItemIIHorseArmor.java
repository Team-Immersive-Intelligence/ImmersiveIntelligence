package pl.pabilo8.immersiveintelligence.common.item.armor;

import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;

import javax.annotation.Nonnull;

/**
 * Supplies a horse armor type for an II item.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.10.2026
 */
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIHorseArmor extends ItemIIBase
{
	private final HorseArmorType armorType;

	/**
	 * Creates a horse armor item with the specified name and type.
	 */
	public ItemIIHorseArmor(String name, HorseArmorType armorType)
	{
		super(name, 1);
		this.armorType = armorType;
	}

	@Nonnull
	@Override
	public HorseArmorType getHorseArmorType(@Nonnull ItemStack stack)
	{
		return armorType;
	}
}
