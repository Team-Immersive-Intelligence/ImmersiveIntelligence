package pl.pabilo8.immersiveintelligence.api.data.pol;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

import java.util.HashMap;

/**
 * Stores POL data pages and compiled scripts.
 *
 * @author Pabilo8(pabilo@iiteam.net)
 * @updated 09.08.2026
 * @since 16.04.2022
 */
public class POLComputerMemory
{
	final HashMap<String, POLScript> scripts = new HashMap<>();
	public DataPacket[] pages;
	public DataPacket packet;
	public int page = 0;

	public POLComputerMemory(int memoryAmount)
	{
		pages = new DataPacket[memoryAmount];
		pages[0] = packet = new DataPacket();

		for(int i = 1; i < memoryAmount; i++)
			pages[i] = new DataPacket();
	}

	/**
	 * @param nbt the {@link NBTTagCompound} the memory was saved to
	 */
	public POLComputerMemory(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("pages", NBT.TAG_COMPOUND);
		pages = new DataPacket[Math.max(list.tagCount(), 1)];

		for(int i = 0; i < list.tagCount(); i++)
			pages[i] = new DataPacket((NBTTagCompound)list.get(i));
		if(list.tagCount()==0)
			pages[0] = new DataPacket();

		packet = pages[0];
	}

	/**
	 * @return Memory saved in a {@link NBTTagCompound}
	 */
	public NBTTagCompound toNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for(DataPacket p : pages)
			list.appendTag(p.serializeNBT());
		nbt.setTag("pages", list);
		return nbt;
	}

	/**
	 * @param id name of the script
	 * @return the compiled script
	 */
	public POLScript getScript(String id)
	{
		return scripts.get(id);
	}

	/**
	 * Puts a script into the memory, so it can be accessed later using the method above
	 */
	public void putScript(String name, POLScript script)
	{
		scripts.put(name, script);
	}

	/**
	 * Sets the current memory page
	 *
	 * @param value page ID
	 */
	public void setPage(int value)
	{
		pages[page] = packet;
		packet = pages[page = value];
	}

	@Override
	public String toString()
	{
		return "POLComputerMemory{packet="+packet+", page="+page+'}';
	}
}
