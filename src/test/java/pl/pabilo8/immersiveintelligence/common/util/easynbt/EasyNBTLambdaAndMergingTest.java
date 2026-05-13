package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 28.04.2026
 * @since 28.01.2026
 */
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
	public void testCheckSetLong()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("key", 42);
		EasyNBT easyNBT = EasyNBT.wrapNBT(nbt);
		easyNBT.checkSetLong("key", value -> assertEquals(42, value));
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
		easyNBT.checkSetBoolean("key", Assertions::assertTrue);
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

	@Test
	public void testCheckSetByte()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByte("key", (byte)7);
		EasyNBT easyNBT = EasyNBT.wrapNBT(nbt);
		easyNBT.checkSetByte("key", value -> assertEquals((byte)7, value));
	}

	@Test
	public void testCheckSetFloat()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("key", 3.5f);
		EasyNBT easyNBT = EasyNBT.wrapNBT(nbt);
		easyNBT.checkSetFloat("key", value -> assertEquals(3.5f, value));
	}

	@Test
	public void testCheckSetDouble()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setDouble("key", 7.25d);
		EasyNBT easyNBT = EasyNBT.wrapNBT(nbt);
		easyNBT.checkSetDouble("key", value -> assertEquals(7.25d, value));
	}

	@Test
	public void testCheckSetCompound()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound nested = new NBTTagCompound();
		nested.setString("nestedKey", "nestedValue");
		nbt.setTag("key", nested);
		EasyNBT easyNBT = EasyNBT.wrapNBT(nbt);
		easyNBT.checkSetCompound("key", value -> assertEquals("nestedValue", value.getString("nestedKey")));
	}

	@Test
	public void testCheckSetItemStack()
	{
		ItemStack stack = new ItemStack(Items.WOODEN_SWORD, 1, 0);
		stack.setTagInfo("key2", new NBTTagString("value2"));
		EasyNBT easyNBT = EasyNBT.newNBT().withItemStack("key", stack);
		easyNBT.checkSetItemStack("key", value -> assertEquals(stack.serializeNBT(), value.serializeNBT()));
	}

	@Test
	public void testCheckSetUUID()
	{
		UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
		EasyNBT easyNBT = EasyNBT.newNBT().withUUID("key", uuid);
		easyNBT.checkSetUUID("key", value -> assertEquals(uuid, value));
	}

	@Test
	public void testCheckSetPos()
	{
		BlockPos pos = new BlockPos(1, 2, 3);
		EasyNBT easyNBT = EasyNBT.newNBT().withPos("key", pos);
		easyNBT.checkSetPos("key", value -> assertEquals(pos, value));
	}

	@Test
	public void testCheckSetDimPos()
	{
		DimensionBlockPos pos = new DimensionBlockPos(1, 2, 3, 4);
		EasyNBT easyNBT = EasyNBT.newNBT().withDimPos("key", pos);
		easyNBT.checkSetDimPos("key", value -> assertEquals(pos, value));
	}

	@Test
	public void testCheckSetFluidStack()
	{
		FluidStack fluid = new FluidStack(FluidRegistry.WATER, 1000);
		EasyNBT easyNBT = EasyNBT.newNBT().withFluidStack("key", fluid);
		easyNBT.checkSetFluidStack("key", value -> assertEquals(fluid, value));
	}

	@Test
	public void testCheckSetNotPresent()
	{
		EasyNBT easyNBT = EasyNBT.newNBT();
		final boolean[] executed = {false};
		easyNBT.checkSetInt("missing", value -> executed[0] = true);
		assertFalse(executed[0]);
	}

	@Test
	public void testWithLongAndGetLong()
	{
		EasyNBT easyNBT = EasyNBT.newNBT().withLong("key", 123456789L);
		assertEquals(123456789L, easyNBT.getLong("key"));
	}

	@Test
	public void testWithUUIDAndGetUUID()
	{
		UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
		EasyNBT easyNBT = EasyNBT.newNBT().withUUID("key", uuid);
		assertEquals(uuid, easyNBT.getUUID("key"));
	}

	enum TestEnum implements ISerializableEnum
	{
		VALUE1,
		VALUE2
	}
}
