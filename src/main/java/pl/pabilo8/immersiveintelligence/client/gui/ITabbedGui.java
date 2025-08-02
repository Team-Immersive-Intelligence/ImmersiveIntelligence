package pl.pabilo8.immersiveintelligence.client.gui;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.07.2019
 */
@SideOnly(Side.CLIENT)
@Deprecated
public interface ITabbedGui
{
	default boolean positionEqual(TileEntity tile)
	{
		assert ImmersiveIntelligence.proxy instanceof ClientProxy;
		EasyNBT nbt = ((ClientProxy)ImmersiveIntelligence.proxy).getStoredGuiData();

		if(!nbt.hasKey("pos"))
			return false;
		return new DimensionBlockPos(tile).equals(nbt.getDimPos("pos"));
	}

	default EasyNBT saveBasicData(TileEntity tile)
	{
		assert ImmersiveIntelligence.proxy instanceof ClientProxy;
		return ((ClientProxy)ImmersiveIntelligence.proxy).setStoredGuiData()
				.withDimPos("pos", new DimensionBlockPos(tile));
	}
}
