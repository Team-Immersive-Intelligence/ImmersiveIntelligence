package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IRedstoneOutput;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataStorageItem;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 20.07.2026
 * @ii-approved 0.3.1
 * @since 11.06.2019
 */
public class TileEntityPunchtapeReader extends TileEntityIIDirectional implements ITickable, IRedstoneOutput, IDataDevice, IPlayerInteraction, IHammerInteraction
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL)
			.withMirroringOnPlacement(true);
	public boolean hadRedstone = false;
	public int rsTime = 0;

	@SyncNBT(nullable = true)
	public DataPacket received = null;
	@SyncNBT
	public PunchtapeReaderMode mode = PunchtapeReaderMode.REDSTONE_INDIFFERENT;

	@Override
	public void update()
	{
		if(mode==PunchtapeReaderMode.PACKET_ON_REDSTONE)
		{
			if(hadRedstone^world.isBlockPowered(pos))
			{
				if(received!=null&&!hadRedstone)
				{
					world.playSound(null, this.pos, IISounds.punchtapeReader, SoundCategory.BLOCKS, 1f, 1f);
					IIDataHandlingUtils.sendPacketAdjacently(received.clone(), world, pos, facing.getOpposite());
				}
				hadRedstone = world.isBlockPowered(pos);
			}
		}
		else if(mode==PunchtapeReaderMode.REDSTONE_ON_PACKET)
			if(rsTime > 0)
				rsTime--;
	}

	@Override
	public int getWeakRSOutput(IBlockState state, EnumFacing side)
	{
		return 0;
	}

	@Override
	public int getStrongRSOutput(IBlockState state, EnumFacing side)
	{
		return mode==PunchtapeReaderMode.REDSTONE_ON_PACKET?(side!=facing&&rsTime > 0?15: 0): 0;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, EnumFacing side)
	{
		return mode!=PunchtapeReaderMode.REDSTONE_INDIFFERENT;
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{

	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(player.isSneaking())
		{
			mode = IIUtils.cycleEnum(true, PunchtapeReaderMode.class, mode);
			IIPacketHandler.sendChatTranslation(player, IIReference.INFO_KEY+"punchtape_reader_mode",
					new TextComponentTranslation(IIReference.INFO_KEY+"punchtape_reader_mode."+mode.ordinal()));
			markDirty();
			markBlockForUpdate(getPos(), null);
		}
		return true;
	}

	@Nonnull
	@Override
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		if(!(heldItem.getItem() instanceof IDataStorageItem))
			return false;

		received = ((IDataStorageItem)heldItem.getItem()).getStoredData(heldItem).clone();
		IIDataHandlingUtils.sendPacketAdjacently(received, world, pos, facing.getOpposite());
		if(mode==PunchtapeReaderMode.REDSTONE_ON_PACKET)
			rsTime = 20;

		world.playSound(null, this.pos, IISounds.punchtapeReader, SoundCategory.BLOCKS, 1f, 1f);
		return true;
	}

	public enum PunchtapeReaderMode implements ISerializableEnum
	{
		REDSTONE_INDIFFERENT,
		PACKET_ON_REDSTONE,
		REDSTONE_ON_PACKET,
	}
}
