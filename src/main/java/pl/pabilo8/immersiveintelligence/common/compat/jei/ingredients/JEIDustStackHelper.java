package pl.pabilo8.immersiveintelligence.common.compat.jei.ingredients;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;
import pl.pabilo8.immersiveintelligence.api.crafting.DustUtils;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 14.03.2026
 */
public class JEIDustStackHelper implements IIngredientHelper<DustStack>
{
	@Nullable
	@Override
	public DustStack getMatch(Iterable<DustStack> ingredients, DustStack ingredientToMatch)
	{
		return null;
	}

	@Override
	public IFocus<?> translateFocus(IFocus<DustStack> focus, IFocusFactory focusFactory)
	{
		DustStack dustStack = focus.getValue();
		List<ItemStack> stacks = DustUtils.getDustStacks(dustStack.name);
		if(stacks.isEmpty()||stacks.get(0).isEmpty())
			return focus;
		return focusFactory.createFocus(focus.getMode(), stacks.get(0));
	}

	@Override
	public String getDisplayName(DustStack ingredient)
	{
		return ingredient.name;
	}

	@Override
	public String getUniqueId(DustStack ingredient)
	{
		return "ii.dust."+ingredient.name;
	}

	@Override
	public String getWildcardId(DustStack ingredient)
	{
		return getUniqueId(ingredient);
	}

	@Override
	public String getModId(DustStack ingredient)
	{
		return ImmersiveIntelligence.MODID;
	}

	@Override
	public Iterable<Color> getColors(DustStack ingredient)
	{
		return Collections.singleton(new Color(DustUtils.getColor(ingredient).rgb));
	}

	@Override
	public String getErrorInfo(@Nullable DustStack ingredient)
	{
		return ingredient==null?"null": ingredient.name+"@"+ingredient.amount;
	}

	@Override
	public DustStack copyIngredient(DustStack ingredient)
	{
		return ingredient.copy();
	}

	@Override
	public String getResourceId(DustStack ingredient)
	{
		return ingredient.name;
	}
}
