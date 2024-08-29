package pl.pabilo8.immersiveintelligence.api.crafting;

import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 29.08.2024
 **/
class DustStackTest extends GameTestBasic
{
	@Test
	void subtract()
	{
		DustStack stack1 = new DustStack("iron_dust", 200);
		DustStack stack2 = new DustStack("iron_dust", 100);
		DustStack result = stack1.subtract(stack2);
		assertEquals(new DustStack("iron_dust", 100), result);
	}

	@Test
	void isEmpty()
	{
		DustStack emptyStack = DustStack.getEmptyStack();
		assertTrue(emptyStack.isEmpty());
	}

	@Test
	void copy()
	{
		DustStack stack = new DustStack("gold_dust", 150);
		DustStack copy = stack.copy();
		assertEquals(stack, copy);
	}

	@Test
	void canMergeWith()
	{
		DustStack stack1 = new DustStack("copper_dust", 100);
		DustStack stack2 = new DustStack("copper_dust", 50);
		assertTrue(stack1.canMergeWith(stack2));
	}

	@Test
	void mergeWith()
	{
		DustStack stack1 = new DustStack("silver_dust", 100);
		DustStack stack2 = new DustStack("silver_dust", 50);
		DustStack result = stack1.mergeWith(stack2);
		assertEquals(new DustStack("silver_dust", 150), result);
	}

	@Test
	void serializeNBT()
	{
		DustStack stack = new DustStack("lead_dust", 200);
		NBTTagCompound nbt = stack.serializeNBT();
		assertEquals("lead_dust", nbt.getString("name"));
		assertEquals(200, nbt.getInteger("amount"));
	}

	@Test
	void deserializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("name", "tin_dust");
		nbt.setInteger("amount", 100);
		DustStack stack = new DustStack(nbt);
		assertEquals(new DustStack("tin_dust", 100), stack);
	}
}