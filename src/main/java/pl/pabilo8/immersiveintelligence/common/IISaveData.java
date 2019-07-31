package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

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

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
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