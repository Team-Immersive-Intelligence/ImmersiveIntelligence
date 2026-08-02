package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 18.07.2026
 * @ii-approved 0.3.1
 * @since 31.05.2019
 */
public class TileEntityDataCallbackConnector extends TileEntityDataConnector
{
	@SyncNBT(name = "colorIn")
	public int colorIn = 0;
	@SyncNBT(name = "colorOut")
	public int colorOut = 1;

	@Override
	public void onPacketReceive(DataPacket packet)
	{
		//computercraft/opencomputers
		this.lastReceived = packet.clone();
		compatReceived = false;

		if(packet.matchesConnector(EnumDyeColor.byMetadata(this.colorIn), -1))
		{
			BlockPos devicePos = this.pos.offset(facing);
			TileEntity device = world.getTileEntity(devicePos);

			if(world.isBlockLoaded(devicePos)&&device instanceof IDataDevice)
			{
				IDataDevice d = (IDataDevice)device;
				d.onReceive(packet, facing.getOpposite());
			}
		}

	}

	@Override
	public void sendPacket(DataPacket packet)
	{
		this.getDataNetwork().sendPacket(packet.withPacketColor(EnumDyeColor.byMetadata(this.colorOut)), this);
	}

	@Override
	public boolean isCallbackCapable()
	{
		return true;
	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(player.isSneaking())
			colorIn = IIUtils.cycleIntAvoid(true, colorIn, 0, 15, colorOut);
		else
			colorOut = IIUtils.cycleIntAvoid(true, colorOut, 0, 15, colorIn);
		onDataChange();
		this.markContainingBlockForUpdate(null);
		world.addBlockEvent(getPos(), this.getBlockType(), 254, 0);
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderColour(IBlockState object, String group)
	{
		if("Color_In".equals(group))
			return 0xff000000|EnumDyeColor.byMetadata(this.colorIn).getColorValue();
		if("Color_Out".equals(group))
			return 0xff000000|EnumDyeColor.byMetadata(this.colorOut).getColorValue();
		return 0xffffffff;
	}

	@Override
	public String getCacheKey(IBlockState object)
	{
		return this.colorIn+";"+this.colorOut;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		if(Utils.isHammer(player.getHeldItem(EnumHand.MAIN_HAND)))
			return new String[]{
					I18n.format(Lib.DESC_INFO+"blockSide.io.0")+": "+I18n.format("item.fireworksCharge."+EnumDyeColor.byMetadata(colorIn).getUnlocalizedName()),
					I18n.format(Lib.DESC_INFO+"blockSide.io.1")+": "+I18n.format("item.fireworksCharge."+EnumDyeColor.byMetadata(colorOut).getUnlocalizedName())
			};
		else
			return new String[0];
	}


}
