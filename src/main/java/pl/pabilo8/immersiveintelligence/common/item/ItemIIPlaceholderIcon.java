package pl.pabilo8.immersiveintelligence.common.item;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIPlaceholderIcon.Icons;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.GeneratedSubItemModel;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 11.05.2019
 */
@IIItemProperties(category = IICategory.NULL, hidden = true, stackSize = 1)
public class ItemIIPlaceholderIcon extends ItemIISubItemsBase<Icons>
{
	public ItemIIPlaceholderIcon()
	{
		super("placeholder_icon", 1, Icons.values());
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return this.getUnlocalizedName();
	}

	@GeneratedItemModels(itemName = "placeholder_icon")
	public enum Icons implements IIItemEnum
	{
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/emplacement/autocannon")
		EMPLACEMENT_AUTOCANNON,
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/emplacement/cpds")
		EMPLACEMENT_CPDS,
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/emplacement/guided_missile_launcher")
		EMPLACEMENT_GUIDED_MISSILE_LAUNCHER,
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/emplacement/heavy_chemthrower")
		EMPLACEMENT_HEAVY_CHEMTHROWER,
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/emplacement/heavy_railgun")
		EMPLACEMENT_HEAVY_RAILGUN,
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/emplacement/infrared_observer")
		EMPLACEMENT_INFRARED_OBSERVER,
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/emplacement/light_howitzer")
		EMPLACEMENT_LIGHT_HOWITZER,
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/emplacement/machinegun")
		EMPLACEMENT_MACHINEGUN,
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/emplacement/mortar")
		EMPLACEMENT_MORTAR,
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/emplacement/rocket_launcher")
		EMPLACEMENT_ROCKET_LAUNCHER,
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/emplacement/searchlight")
		EMPLACEMENT_SEARCHLIGHT,
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/emplacement/spotlight_tower")
		EMPLACEMENT_SPOTLIGHT_TOWER,
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/emplacement/tesla")
		EMPLACEMENT_TESLA,
		@GeneratedSubItemModel(customTexturePath = "immersiveintelligence:gui/upgrade/flagpole/unit_post")
		FLAGPOLE_UNIT_POST,
	}
}
