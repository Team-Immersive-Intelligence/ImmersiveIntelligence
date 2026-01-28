package pl.pabilo8.immersiveintelligence.common.util.easynbt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.api.crafting.DustStack;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 28.01.2026
 */
class EasyCollectionTest
{
	@Test
	void serializeNBT()
	{
		//Setup collection
		EasyCollection<DustStack, NBTTagCompound> list = new EasyCollection<>(DustStack::getEmptyStack);
		DustStack dust1 = new DustStack("test", 1);
		DustStack dust2 = new DustStack("test2", 2);
		DustStack dust3 = new DustStack("test3", 3);

		//Add elements
		list.add(dust1);
		list.add(dust2);
		list.add(dust3);

		//Setup a manual NBTTagList for comparison
		NBTTagList manualList = new NBTTagList();
		manualList.appendTag(dust1.serializeNBT());
		manualList.appendTag(dust2.serializeNBT());
		manualList.appendTag(dust3.serializeNBT());

		//Compare
		assertEquals(manualList, list.serializeNBT());
	}

	@Test
	void deserializeNBT()
	{
		//Setup manual NBTTagList
		DustStack dust1 = new DustStack("test", 1);
		DustStack dust2 = new DustStack("test2", 2);
		DustStack dust3 = new DustStack("test3", 3);

		NBTTagList manualList = new NBTTagList();
		manualList.appendTag(dust1.serializeNBT());
		manualList.appendTag(dust2.serializeNBT());
		manualList.appendTag(dust3.serializeNBT());

		//Setup collection and deserialize
		EasyCollection<DustStack, NBTTagCompound> list = new EasyCollection<>(DustStack::getEmptyStack);
		list.deserializeNBT(manualList);

		//Compare
		assertEquals(manualList, list.serializeNBT());
	}
}
