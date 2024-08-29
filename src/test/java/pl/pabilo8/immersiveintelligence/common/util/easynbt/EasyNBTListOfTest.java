package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EasyNBTListOfTest extends GameTestBasic
{
	@Test
	public void testListOfNBTBase()
	{
		NBTTagInt tagInt = new NBTTagInt(42);
		NBTTagList list = EasyNBT.listOf(tagInt);
		assertEquals(1, list.tagCount());
		assertEquals(tagInt, list.get(0));
	}

	@Test
	public void testSingleType()
	{
		NBTTagList list = EasyNBT.listOf(42, 3.14f, 43, true, 44, "test");
		assertEquals(3, list.tagCount());

		assertEquals(new NBTTagInt(42), list.get(0));
		assertEquals(new NBTTagInt(43), list.get(1));
		assertEquals(new NBTTagInt(44), list.get(2));
	}

	@Test
	public void testPrimitives()
	{
		//Int
		NBTTagList ints = EasyNBT.listOf(1, 2, 3);
		assertEquals(3, ints.tagCount());
		for(int i = 1; i < 4; i++)
			assertEquals(new NBTTagInt(i), ints.get(i-1));

		//Float
		NBTTagList floats = EasyNBT.listOf(1.0f, 2.0f, 3.0f);
		assertEquals(3, floats.tagCount());
		for(float i = 1.0f; i < 4.0f; i++)
			assertEquals(new NBTTagFloat(i), floats.get((int)i-1));

		//Double
		NBTTagList doubles = EasyNBT.listOf(1.0, 2.0, 3.0);
		assertEquals(3, doubles.tagCount());
		for(double i = 1.0; i < 4.0; i++)
			assertEquals(new NBTTagDouble(i), doubles.get((int)i-1));

		//Long
		NBTTagList longs = EasyNBT.listOf(1L, 2L, 3L);
		assertEquals(3, longs.tagCount());
		for(long i = 1; i < 4; i++)
			assertEquals(new NBTTagLong(i), longs.get((int)i-1));

		//Short
		NBTTagList shorts = EasyNBT.listOf((short)1, (short)2, (short)3);
		assertEquals(3, shorts.tagCount());
		for(short i = 1; i < 4; i++)
			assertEquals(new NBTTagShort(i), shorts.get(i-1));

		//Byte
		NBTTagList bytes = EasyNBT.listOf((byte)1, (byte)2, (byte)3);
		assertEquals(3, bytes.tagCount());
		for(byte i = 1; i < 4; i++)
			assertEquals(new NBTTagByte(i), bytes.get(i-1));

		//Boolean
		NBTTagList booleans = EasyNBT.listOf(true, false, true);
		assertEquals(3, booleans.tagCount());
		assertEquals(new NBTTagByte((byte)1), booleans.get(0));
		assertEquals(new NBTTagByte((byte)0), booleans.get(1));
		assertEquals(new NBTTagByte((byte)1), booleans.get(2));

	}

	@Test
	public void testListOfEasyNBT()
	{
		EasyNBT easyNBT = EasyNBT.newNBT().withInt("key", 42);
		NBTTagList list = EasyNBT.listOf(easyNBT);
		assertEquals(1, list.tagCount());
		assertEquals(easyNBT.unwrap(), list.getCompoundTagAt(0));
	}

	@Test
	public void testListOfBlockPos()
	{
		BlockPos pos = new BlockPos(1, 2, 3);
		NBTTagList list = EasyNBT.listOf(pos);
		assertEquals(1, list.tagCount());
		assertEquals(makeListOf(new NBTTagInt(1), new NBTTagInt(2), new NBTTagInt(3)),
				list.get(0));
	}

	@Test
	public void testListOfDimensionBlockPos()
	{
		DimensionBlockPos pos = new DimensionBlockPos(1, 2, 3, 4);
		NBTTagList list = EasyNBT.listOf(pos);
		assertEquals(1, list.tagCount());
		assertEquals(makeListOf(new NBTTagInt(1), new NBTTagInt(2), new NBTTagInt(3), new NBTTagInt(4)),
				list.get(0));
	}

	@Test
	public void testListOfVec3d()
	{
		Vec3d vec = new Vec3d(1.0, 2.0, 3.0);
		NBTTagList list = EasyNBT.listOf(vec);
		assertEquals(1, list.tagCount());

		NBTTagList vecList = new NBTTagList();
		vecList.appendTag(new NBTTagDouble(1.0));
		vecList.appendTag(new NBTTagDouble(2.0));
		vecList.appendTag(new NBTTagDouble(3.0));
		assertEquals(3, vecList.tagCount());

		assertEquals(new NBTTagDouble(1.0), vecList.get(0));
		assertEquals(new NBTTagDouble(2.0), vecList.get(1));
		assertEquals(new NBTTagDouble(3.0), vecList.get(2));
	}

	@Test
	public void testListOfItemStack()
	{
		ItemStack stack = new ItemStack(Items.WOODEN_SWORD, 1, 0);
		stack.setTagInfo("key", new NBTTagString("value"));
		NBTTagList list = EasyNBT.listOf(stack);
		assertEquals(1, list.tagCount());
		assertEquals(stack.serializeNBT(), list.getCompoundTagAt(0));
	}

	@Test
	public void testListOfFluidStack()
	{
		FluidStack fluid = new FluidStack(FluidRegistry.WATER, 1000);
		NBTTagList list = EasyNBT.listOf(fluid);
		assertEquals(1, list.tagCount());
		assertEquals(fluid.writeToNBT(new NBTTagCompound()), list.getCompoundTagAt(0));
	}

	@Test
	public void testListOfArray()
	{
		NBTTagList list = EasyNBT.listOf(new int[]{1, 2, 3});
		assertEquals(1, list.tagCount());
		assertEquals(new NBTTagIntArray(new int[]{1, 2, 3}), list.get(0));
	}

	@Test
	public void testListOfCollection()
	{
		NBTTagList list = EasyNBT.listOf(Arrays.asList(1, 2, 3));

		NBTTagList vanilla = new NBTTagList();
		for(int i = 1; i < 4; i++)
			vanilla.appendTag(new NBTTagInt(i));

		assertEquals(1, list.tagCount());
		assertEquals(vanilla, list.get(0));
	}

	private NBTTagList makeListOf(NBTBase... values)
	{
		NBTTagList list = new NBTTagList();
		for(NBTBase value : values)
			list.appendTag(value);
		return list;
	}
}
