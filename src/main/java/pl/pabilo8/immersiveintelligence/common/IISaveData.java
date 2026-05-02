package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.api.ammo.penetration.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.PenetrationCache;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageDiplomacySync;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.06.2019
 */
public class IISaveData extends WorldSavedData
{
	public static final String DATA_NAME = "II-SaveData";
	private static IISaveData INSTANCE;

	public IISaveData(String name)
	{
		super(name);
	}

	public static void setDirty()
	{
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER&&INSTANCE!=null)
			INSTANCE.markDirty();
	}

	public static void setInstance(IISaveData in)
	{
		if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
			INSTANCE = in;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		EasyNBT enbt = EasyNBT.wrapNBT(nbt);

		//Load block damage data
		PenetrationCache.blockDamage.clear();
		PenetrationCache.blockDamageClient.clear();
		try
		{
			if(enbt.hasKey("block_dmg"))
			{
				enbt.streamList(NBTTagIntArray.class, "block_dmg", EasyNBT.TAG_INT_ARRAY)
						.map(NBTTagIntArray::getIntArray)
						.filter(t -> t.length==5)
						.map(t -> new DamageBlockPos(t[0], t[1], t[2], t[3], (float)t[4]/16))
						.forEach(PenetrationCache.blockDamage::add);
				PenetrationCache.blockDamageClient.addAll(PenetrationCache.blockDamage);
			}
		} catch(Exception e)
		{
			IILogger.info("Error in the block damage list!");
		}

		DiplomacyHandler.getInstance(false).loadAllFromNBT(enbt.getEasyCompound("diplomacy"));
		IIPacketHandler.sendToAllClients(MessageDiplomacySync.updateAllMessage());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		return EasyNBT.wrapNBT(nbt)
				.withList("block_dmg", e -> new NBTTagIntArray(new int[]{
						e.getX(), e.getY(), e.getZ(), e.dimension, (int)(e.damage*16)
				}), PenetrationCache.blockDamage)
				.withTag("diplomacy", DiplomacyHandler.getInstance(false).saveAllToNBT())
				.unwrap();
	}

}
