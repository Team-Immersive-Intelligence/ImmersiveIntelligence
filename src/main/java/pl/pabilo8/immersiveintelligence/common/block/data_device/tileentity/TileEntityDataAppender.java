package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.DataDeviceUtils;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIInventory;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional;

import javax.annotation.Nonnull;

/**
 * Applies ordered variable addition and removal rules to incoming packets.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.10.2026
 */
public class TileEntityDataAppender extends TileEntityIIDirectional implements IDataDevice, IBlockBounds, IIIGuiMultiblockTile, IIIInventory
{
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public EasyCollection<AppendRule, NBTTagCompound> entries = new EasyCollection<>(AppendRule::new);

	public DataPacket append(DataPacket source)
	{
		DataPacket result = new DataPacket(source.serializeNBT());
		for(AppendRule entry : entries)
			if(entry.remove)
				result.remove(entry.variable);
			else if(entry.overwrite||!result.has(entry.variable))
				result.set(entry.variable, entry.value.clone());
		return result;
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		if(world!=null&&!world.isRemote&&packet!=null&&side==facing.getOpposite())
			IIDataHandlingUtils.sendPacketAdjacently(append(packet), world, pos, facing);
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		if(message.hasKey("entries", 9))
		{
			entries.deserializeNBT(DataDeviceUtils.boundedList(message, "entries"));
			markDirty();
			updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
		}
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.DATA_APPENDER;
	}

	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL).withRotation(true);

	@Nonnull
	@Override
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	@Nonnull
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, .875f, 1};
	}

	@Override
	public boolean canOpenGui()
	{
		updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
		return true;
	}

	@Override
	public TileEntity master()
	{
		return this;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return NonNullList.create();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	/**
	 * Stores one variable addition or removal operation.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 24.10.2026
	 */
	public static class AppendRule implements INBTSerializable<NBTTagCompound>
	{
		public char variable = 'a';
		public boolean remove = false, overwrite = false;
		public DataType value = new DataTypeInteger();

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("variable", String.valueOf(variable));
			nbt.setBoolean("remove", remove);
			nbt.setBoolean("overwrite", overwrite);
			nbt.setTag("value", value.valueToNBT());
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			variable = DataDeviceUtils.readLetter(nbt, "variable");
			remove = nbt.getBoolean("remove");
			overwrite = nbt.getBoolean("overwrite");
			value = DataDeviceUtils.readValue(nbt, "value");
		}
	}
}
