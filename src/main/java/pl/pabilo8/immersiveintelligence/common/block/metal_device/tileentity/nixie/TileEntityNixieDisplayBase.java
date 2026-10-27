package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.nixie;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHasObjProperty;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIInventory;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Displays packet characters and forwards the remaining values to the right.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.10.2026
 */
public abstract class TileEntityNixieDisplayBase extends TileEntityIIDirectional implements IDataDevice, IBlockBounds, IHasObjProperty, IIIGuiMultiblockTile, IIIInventory
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL).withRotation(true).withMirroringOnPlacement(true);
	private final ArrayList<String> solidParts = new ArrayList<>(Arrays.asList("base", "variant_"+getTubeCount()));

	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public DisplayMode mode = DisplayMode.NUMBER_TEXT;
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public String variables = "abcd";
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public String displayedText = "";

	public abstract int getTubeCount();

	@Override
	@Nonnull
	public ArrayList<String> compileDisplayList()
	{
		return solidParts;
	}

	/**
	 * Updates the displayed characters and returns a copy with the remaining values.
	 */
	public DataPacket display(DataPacket source)
	{
		DataPacket remainder = new DataPacket(source.serializeNBT());
		StringBuilder text = new StringBuilder(getTubeCount());
		for(int i = 0; i < getTubeCount(); i++)
		{
			char letter = variables.charAt(mode==DisplayMode.DIGITS?i: 0);
			DataType value = remainder.get(letter);
			String characters = value instanceof DataTypeString||value instanceof NumericDataType?value.toString(): "";
			text.append(characters.isEmpty()?' ': characters.charAt(0));
			if(value instanceof DataTypeString||value instanceof NumericDataType)
				remainder.set(letter, new DataTypeString(characters.isEmpty()?"": characters.substring(1)));
		}
		displayedText = text.toString();
		return remainder;
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		if(world==null||world.isRemote||packet==null||(side!=EnumFacing.DOWN&&side!=facing.rotateY()))
			return;
		String previous = displayedText;
		DataPacket remainder = display(packet);
		if(!previous.equals(displayedText))
		{
			markDirty();
			updateTileForEvent(SyncEvents.TILE_CUSTOM1);
		}
		IIDataHandlingUtils.sendPacketAdjacently(remainder, world, pos, facing.rotateYCCW());
	}

	@Override
	public void receiveMessageFromClient(@Nonnull NBTTagCompound message)
	{
		NBTTagCompound settings = new NBTTagCompound();
		if(message.hasKey("mode", 8))
			for(DisplayMode candidate : DisplayMode.values())
				if(candidate.getName().equals(message.getString("mode")))
					settings.setString("mode", candidate.getName());
		if(message.hasKey("variables", 8)&&validVariables(message.getString("variables")))
			settings.setString("variables", message.getString("variables"));
		if(!settings.hasNoTags())
		{
			super.receiveMessageFromClient(settings);
			markDirty();
		}
	}

	private static boolean validVariables(String letters)
	{
		if(letters.length()!=4)
			return false;
		for(int i = 0; i < letters.length(); i++)
			if(!DataPacket.isValidVariable(letters.charAt(i)))
				return false;
		return true;
	}

	@Nonnull
	@Override
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS.withMirroringOnPlacement(true);
	}

	@Nonnull
	@Override
	public float[] getBlockBounds()
	{
		return facing.getAxis()==EnumFacing.Axis.Z?
				new float[]{0, 0, .25f, 1, .8125f, .75f}: new float[]{.25f, 0, 0, .75f, .8125f, 1};
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.NIXIE_DISPLAY;
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

	public enum DisplayMode implements ISerializableEnum
	{
		DIGITS, NUMBER_TEXT
	}
}
