package pl.pabilo8.immersiveintelligence.common.item.crafting.material;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.ItemIIMaterialDye.MaterialsDye;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIISubItemsBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 11.05.2019
 */
@IBatchOredictRegister(oreDict = "dye")
@IIItemProperties(category = IICategory.RESOURCES)
public class ItemIIMaterialDye extends ItemIISubItemsBase<MaterialsDye>
{
	public ItemIIMaterialDye()
	{
		super("material_dye", 64, MaterialsDye.values());
	}

	@GeneratedItemModels(itemName = "material_dye", texturePath = "material/dye")
	@AllArgsConstructor
	public enum MaterialsDye implements IIItemEnum
	{
		RED(EnumDyeColor.RED),
		GREEN(EnumDyeColor.GREEN),
		BLUE(EnumDyeColor.BLUE),
		YELLOW(EnumDyeColor.YELLOW),
		BLACK(EnumDyeColor.BLACK);

		@Getter
		private final EnumDyeColor dye;
	}


	/**
	 * Returns true if the item can be used on the given entity, e.g. shears on sheep.
	 */
	public boolean itemInteractionForEntity(@Nonnull ItemStack stack, @Nonnull EntityPlayer playerIn, @Nonnull EntityLivingBase target, @Nonnull EnumHand hand)
	{
		if(target instanceof EntitySheep entitysheep)
		{
			EnumDyeColor color = stackToSub(stack).getDye();
			if(!entitysheep.getSheared()&&entitysheep.getFleeceColor()!=color)
			{
				entitysheep.setFleeceColor(color);
				stack.shrink(1);
			}
			return true;
		}
		return false;
	}
}
