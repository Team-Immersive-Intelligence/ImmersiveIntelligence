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
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
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
 * Maps typed input values to configured output variables without modifying the source packet.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.10.2026
 */
public class TileEntityDataMapper extends TileEntityIIDirectional implements IDataDevice, IBlockBounds, IIIGuiMultiblockTile, IIIInventory
{
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public EasyCollection<MappingRule, NBTTagCompound> mappings = new EasyCollection<>(MappingRule::new);

	public DataPacket map(DataPacket source)
	{
		DataPacket result = new DataPacket(source.serializeNBT());
		for(MappingRule rule : mappings)
		{
			if(!source.has(rule.input))
				continue;
			DataType actual = source.get(rule.input);
			if(actual.getClass()!=rule.inputType.getClass())
				continue;
			for(ValueMapping mapping : rule.values)
				if(DataDeviceUtils.matches(actual, mapping.expected)&&mapping.output.getClass()==rule.outputType.getClass())
				{
					if(rule.input!=rule.output)
						result.remove(rule.input);
					result.set(rule.output, mapping.output.clone());
					break;
				}
		}
		return result;
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		if(world!=null&&!world.isRemote&&packet!=null&&side==facing.getOpposite())
			IIDataHandlingUtils.sendPacketAdjacently(map(packet), world, pos, facing);
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		if(message.hasKey("mappings", 9))
		{
			mappings.deserializeNBT(DataDeviceUtils.boundedList(message, "mappings"));
			markDirty();
			updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
		}
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.DATA_MAPPER;
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
	 * Maps exact typed input values to one output variable.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 24.10.2026
	 */
	public static class MappingRule implements INBTSerializable<NBTTagCompound>
	{
		public char input = 'a', output = 'a';
		public DataType inputType = new DataTypeInteger(), outputType = new DataTypeString();
		public EasyCollection<ValueMapping, NBTTagCompound> values = new EasyCollection<>(ValueMapping::new);

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("input", String.valueOf(input));
			nbt.setString("output", String.valueOf(output));
			nbt.setTag("inputType", inputType.valueToNBT());
			nbt.setTag("outputType", outputType.valueToNBT());
			nbt.setTag("values", values.serializeNBT());
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			input = DataDeviceUtils.readLetter(nbt, "input");
			output = DataDeviceUtils.readLetter(nbt, "output");
			inputType = DataDeviceUtils.readValue(nbt, "inputType");
			outputType = DataDeviceUtils.readValue(nbt, "outputType");
			values.deserializeNBT(DataDeviceUtils.boundedList(nbt, "values"));
		}
	}

	/**
	 * Stores one expected input value and its replacement.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 24.10.2026
	 */
	public static class ValueMapping implements INBTSerializable<NBTTagCompound>
	{
		public DataType expected = new DataTypeInteger(), output = new DataTypeString();

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setTag("expected", expected.valueToNBT());
			nbt.setTag("output", output.valueToNBT());
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			expected = DataDeviceUtils.readValue(nbt, "expected");
			output = DataDeviceUtils.readValue(nbt, "output");
		}
	}
}
