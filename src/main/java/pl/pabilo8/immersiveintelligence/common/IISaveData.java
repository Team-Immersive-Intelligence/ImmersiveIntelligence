package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.api.ammo.IIPenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8
 * @since 23-06-2019
 */
public class IISaveData extends WorldSavedData
{
	public static final String dataName = "ImmersiveIntelligence-SaveData";
	private static IISaveData INSTANCE;

	public IISaveData(String s)
	{
		super(s);
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

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		EasyNBT enbt = EasyNBT.wrapNBT(nbt);

		//Load block damage data
		IIPenetrationRegistry.blockDamage.clear();
		IIPenetrationRegistry.blockDamageClient.clear();
		try
		{
			if(enbt.hasKey("block_dmg"))
			{
				enbt.streamList(NBTTagIntArray.class, "block_dmg", EasyNBT.TAG_INT_ARRAY)
						.map(NBTTagIntArray::getIntArray)
						.filter(t -> t.length==5)
						.map(t -> new DamageBlockPos(t[0], t[1], t[2], t[3], (float)t[4]/16))
						.forEach(IIPenetrationRegistry.blockDamage::add);
				IIPenetrationRegistry.blockDamageClient.addAll(IIPenetrationRegistry.blockDamage);
			}
		} catch(Exception e)
		{
			IILogger.info("Error in the block damage list!");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{

		//Save block damage data
		EasyNBT.wrapNBT(nbt)
				.withList("block_dmg", e -> new NBTTagIntArray(new int[]{
						e.getX(), e.getY(), e.getZ(), e.dimension, (int)(e.damage*16)
				}), IIPenetrationRegistry.blockDamage);
		return nbt;
	}

}