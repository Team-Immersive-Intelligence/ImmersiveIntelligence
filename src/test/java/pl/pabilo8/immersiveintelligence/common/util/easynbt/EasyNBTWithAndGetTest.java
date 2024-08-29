package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EasyNBTWithAndGetTest extends GameTestBasic
{
	@Test
	public void testWithInt()
	{
		EasyNBT nbt = EasyNBT.newNBT().withInt("key", 42);
		EasyNBT nbtAny = EasyNBT.newNBT().withAny("key", 42);
		assertEquals(nbt.unwrap(), nbtAny.unwrap());
		assertEquals(42, nbt.getInt("key"));
	}

	@Test
	public void testWithByte()
	{
		EasyNBT nbt = EasyNBT.newNBT().withByte("b", (byte)1);
		EasyNBT nbtAny = EasyNBT.newNBT().withAny("b", (byte)1);
		assertEquals(nbt.unwrap(), nbtAny.unwrap());
		assertEquals(1, nbt.getByte("b"));
	}

	@Test
	public void testWithFloat()
	{
		EasyNBT nbt = EasyNBT.newNBT().withFloat("f", 3.14f);
		EasyNBT nbtAny = EasyNBT.newNBT().withAny("f", 3.14f);
		assertEquals(nbt.unwrap(), nbtAny.unwrap());
		assertEquals(3.14f, nbt.getFloat("f"));
	}

	@Test
	public void testWithDouble()
	{
		EasyNBT nbt = EasyNBT.newNBT().withDouble("v", 3.14159);
		EasyNBT nbtAny = EasyNBT.newNBT().withAny("v", 3.14159);
		assertEquals(nbt.unwrap(), nbtAny.unwrap());
		assertEquals(3.14159, nbt.getDouble("v"));
	}

	@Test
	public void testWithBoolean()
	{
		EasyNBT nbt = EasyNBT.newNBT().withBoolean("bul", true);
		EasyNBT nbtAny = EasyNBT.newNBT().withAny("bul", true);
		assertEquals(nbt.unwrap(), nbtAny.unwrap());
		assertTrue(nbt.getBoolean("bul"));
	}

	@Test
	public void testWithString()
	{
		EasyNBT nbt = EasyNBT.newNBT().withString("key", "value");
		EasyNBT nbtAny = EasyNBT.newNBT().withAny("key", "value");
		assertEquals(nbt.unwrap(), nbtAny.unwrap());
		assertEquals("value", nbt.getString("key"));
	}

	@Test
	public void testWithPos()
	{
		BlockPos pos = new BlockPos(1, 2, 3);
		EasyNBT nbt = EasyNBT.newNBT().withPos("pos", pos);
		EasyNBT nbtAny = EasyNBT.newNBT().withAny("pos", pos);
		assertEquals(nbt.unwrap(), nbtAny.unwrap());
		assertEquals(pos, nbt.getPos("pos"));
	}

	@Test
	public void testWithDimPos()
	{
		DimensionBlockPos pos = new DimensionBlockPos(1, 2, 3, 4);
		EasyNBT nbt = EasyNBT.newNBT().withDimPos("pos", pos);
		EasyNBT nbtAny = EasyNBT.newNBT().withAny("pos", pos);
		assertEquals(nbt.unwrap(), nbtAny.unwrap());
		assertEquals(pos, nbt.getDimPos("pos"));
	}

	@Test
	public void testWithVec3d()
	{
		Vec3d vec = new Vec3d(1.0, 2.0, 3.0);
		EasyNBT nbt = EasyNBT.newNBT().withVec3d("pos", vec);
		EasyNBT nbtAny = EasyNBT.newNBT().withAny("pos", vec);
		assertEquals(nbt.unwrap(), nbtAny.unwrap());
		assertEquals(vec, nbt.getVec3d("pos"));
	}

	@Test
	public void testWithItemStack()
	{
		ItemStack stack = new ItemStack(Items.WOODEN_SWORD, 1, 0);
		stack.setTagInfo("key2", new NBTTagString("value2"));
		EasyNBT nbt = EasyNBT.newNBT().withItemStack("stack", stack);
		EasyNBT nbtAny = EasyNBT.newNBT().withAny("stack", stack);
		assertEquals(nbt.unwrap(), nbtAny.unwrap());
		assertEquals(stack.serializeNBT(), nbt.getItemStack("stack").serializeNBT());
	}

	@Test
	public void testWithFluidStack()
	{
		FluidStack fluid = new FluidStack(FluidRegistry.WATER, 1000);
		EasyNBT nbt = EasyNBT.newNBT().withFluidStack("fs", fluid);
		EasyNBT nbtAny = EasyNBT.newNBT().withAny("fs", fluid);
		assertEquals(nbt.unwrap(), nbtAny.unwrap());
		assertEquals(fluid, nbt.getFluidStack("fs"));
	}
}
