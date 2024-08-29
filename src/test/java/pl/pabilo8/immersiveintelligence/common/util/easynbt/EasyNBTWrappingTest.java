package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import com.google.gson.JsonObject;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import static org.junit.jupiter.api.Assertions.*;

public class EasyNBTWrappingTest extends GameTestBasic
{
	@Test
	public void testNewNBT()
	{
		EasyNBT easyNBT = EasyNBT.newNBT();
		assertNotNull(easyNBT);
		assertTrue(easyNBT.unwrap().hasNoTags());
	}

	@Test
	public void testWrapNBTWithNBTTagCompound()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("key", "value");
		EasyNBT easyNBT = EasyNBT.wrapNBT(nbt);
		assertNotNull(easyNBT);
		assertEquals("value", easyNBT.getString("key"));
	}

	@Test
	public void testWrapNBTWithJsonObject()
	{
		JsonObject json = new JsonObject();
		json.addProperty("key", "value");
		EasyNBT easyNBT = EasyNBT.wrapNBT(json);
		assertNotNull(easyNBT);
		assertEquals("value", easyNBT.getString("key"));
	}

	@Test
	public void testWrapNBTWithItemStack()
	{
		ItemStack itemStack = new ItemStack(Items.WOODEN_SWORD, 1, 0);
		itemStack.setTagInfo("key", new NBTTagString("value"));

		EasyNBT easyNBT = EasyNBT.wrapNBT(itemStack);
		assertNotNull(easyNBT);
		assertEquals("value", easyNBT.getString("key"));
	}

	@Test
	public void testParseEasyNBT()
	{
		EasyNBT easyNBT = EasyNBT.parseEasyNBT("{\"key\":\"value\"}");
		assertNotNull(easyNBT);
		assertEquals("value", easyNBT.getString("key"));
	}
}
