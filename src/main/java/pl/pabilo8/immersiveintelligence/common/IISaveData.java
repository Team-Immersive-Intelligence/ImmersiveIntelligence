package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;

/**
 * Created by Pabilo8 on 23-06-2019.
 */
public class IISaveData extends WorldSavedData
{
	private static IISaveData INSTANCE;
	public static final String dataName = "ImmersiveIntelligence-SaveData";

	public IISaveData(String s)
	{
		super(s);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList radioList = (NBTTagList)nbt.getTag("radioList");
		for(int i = 0; i < radioList.tagCount(); i++)
		{
			RadioNetwork.INSTANCE.clearDevices();
			NBTTagCompound tag = radioList.getCompoundTagAt(i);
			DimensionBlockPos dim = new DimensionBlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"), tag.getInteger("dim"));
			RadioNetwork.INSTANCE.addDevice(dim);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList radioList = new NBTTagList();
		for(DimensionBlockPos pos : RadioNetwork.INSTANCE.getDevices())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("x", pos.getX());
			tag.setInteger("y", pos.getY());
			tag.setInteger("z", pos.getZ());
			tag.setInteger("dim", pos.dimension);
			radioList.appendTag(tag);
		}
		nbt.setTag("radioList", radioList);

		return nbt;
	}


	public static void setDirty(int dimension)
	{
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER&&INSTANCE!=null)
			INSTANCE.markDirty();
	}

	public static void setInstance(int dimension, IISaveData in)
	{
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
			INSTANCE = in;
	}

}