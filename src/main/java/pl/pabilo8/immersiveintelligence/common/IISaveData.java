package pl.pabilo8.immersiveintelligence.common;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.DamageBlockPos;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry;

import java.util.Iterator;

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
		NBTTagList list = nbt.getTagList("block_damage", 10);
		Iterator<NBTBase> i = list.iterator();
		PenetrationRegistry.blockDamage.clear();
		while(i.hasNext())
		{
			try
			{
				NBTTagCompound compound = (NBTTagCompound)i.next();
				NBTTagCompound posTag = (NBTTagCompound)compound.getTag("pos");
				BlockPos pos = NBTUtil.getPosFromTag(posTag);
				int dimension = posTag.getInteger("dim");
				DamageBlockPos dpos = new DamageBlockPos(pos, dimension, compound.getFloat("damage"));
				PenetrationRegistry.blockDamage.add(dpos);
			} catch(ClassCastException|NullPointerException e)
			{
				IILogger.info("Error in the block damage list!");
			}

		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList blockDamage = new NBTTagList();
		for(DamageBlockPos entry : PenetrationRegistry.blockDamage)
		{
			NBTTagCompound compound = new NBTTagCompound();
			NBTTagCompound posTag = NBTUtil.createPosTag(entry);
			posTag.setInteger("dim", entry.dimension);
			compound.setTag("pos", posTag);
			compound.setFloat("damage", entry.damage);
			blockDamage.appendTag(compound);
		}
		nbt.setTag("block_damage", blockDamage);
		return nbt;
	}

}