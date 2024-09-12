package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EasyNBTLambdaAndMergingTest
{
	@Test
	public void testCheckSetInt()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("key", 42);
		EasyNBT easyNBT = EasyNBT.wrapNBT(nbt);
		easyNBT.checkSetInt("key", value -> assertEquals(42, value));
	}

	@Test
	public void testCheckSetString()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("key", "value");
		EasyNBT easyNBT = EasyNBT.wrapNBT(nbt);
		easyNBT.checkSetString("key", value -> assertEquals("value", value));
	}

	@Test
	public void testCheckSetBoolean()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("key", true);
		EasyNBT easyNBT = EasyNBT.wrapNBT(nbt);
		easyNBT.checkSetBoolean("key", value -> assertTrue(value));
	}

	@Test
	public void testCheckSetEnum()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("key", TestEnum.VALUE1.name());
		EasyNBT easyNBT = EasyNBT.wrapNBT(nbt);
		easyNBT.checkSetEnum("key", TestEnum.class, value -> assertEquals(TestEnum.VALUE1, value));
	}

	@Test
	public void testMerge()
	{
		NBTTagCompound nbt1 = new NBTTagCompound();
		nbt1.setInteger("key1", 42);
		EasyNBT easyNBT1 = EasyNBT.wrapNBT(nbt1);

		NBTTagCompound nbt2 = new NBTTagCompound();
		nbt2.setString("key2", "value");
		EasyNBT easyNBT2 = EasyNBT.wrapNBT(nbt2);

		EasyNBT mergedNBT = easyNBT1.mergeWith(easyNBT2);
		assertEquals(42, mergedNBT.getInt("key1"));
		assertEquals("value", mergedNBT.getString("key2"));
	}

	enum TestEnum implements ISerializableEnum
	{
		VALUE1,
		VALUE2
	}
}
