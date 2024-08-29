package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 29.08.2024
 **/
class DustUtilsTest extends GameTestBasic
{
	private IngredientStack ingredientStack;
	private ItemStack itemStack;
	private DustStack dustStack;

	@BeforeEach
	void setUp()
	{
		DustUtils.cleanRegistry();
		ingredientStack = new IngredientStack(new ItemStack(Items.DIAMOND, 1));
		itemStack = new ItemStack(Items.DIAMOND, 1);
		dustStack = new DustStack("diamond_dust", 100);

		DustUtils.registerDust(ingredientStack.copyWithSize(100), "diamond_dust", IIColor.MC_BLUE);
		assertTrue(DustUtils.isDustStack(itemStack));
	}

	@Test
	void isDustStack()
	{
		assertTrue(DustUtils.isDustStack(itemStack));
	}

	@Test
	void fromItemStack()
	{
		DustStack result = DustUtils.fromItemStack(itemStack);
		assertEquals(dustStack, result);
	}

	@Test
	void getColor()
	{
		IIColor color = DustUtils.getColor(dustStack);
		assertEquals(IIColor.MC_BLUE, color);
	}

	@Test
	void getDustStacks()
	{
		List<ItemStack> stacks = DustUtils.getDustStacks("diamond_dust");
		assertFalse(stacks.isEmpty());
		assertTrue(itemStack.isItemEqual(stacks.get(0)));
	}

	@Test
	void fromDustStack()
	{
		ItemStack[] stacks = DustUtils.fromDustStack(dustStack);
		assertEquals(1, stacks.length);
		assertTrue(itemStack.isItemEqual(stacks[0]));
	}
}