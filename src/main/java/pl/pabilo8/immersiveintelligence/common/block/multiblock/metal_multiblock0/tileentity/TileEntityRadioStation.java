package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.radio.IRadioDevice;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;
import pl.pabilo8.immersiveintelligence.api.utils.MultiblockConstructionManager;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.RadioStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockRadioStation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IConstructionRequiringDevice;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import java.util.ArrayList;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.08.2025
 * @ii-approved 0.3.1
 * @since 20.06.2019
 */
public class TileEntityRadioStation extends TileEntityMultiblockIIGeneric<TileEntityRadioStation> implements IRadioDevice, IConstructionRequiringDevice
{
	@SyncNBT(events = SyncEvents.TILE_CONSTRUCTION)
	public MultiblockConstructionManager construction;
	@SyncNBT(events = {SyncEvents.TILE_CLIENT_MESSAGE, SyncEvents.TILE_CUSTOM1})
	public int frequency;
	@SyncNBT(time = 0)
	public int radioCooldown;
	public int soundDelay = 0;

	public TileEntityRadioStation()
	{
		super(MultiblockRadioStation.INSTANCE);
		this.energyStorage = new FluxStorageAdvanced(RadioStation.energyCapacity);
		this.construction = new MultiblockConstructionManager(this, RadioStation.constructionEnergy);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.construction = null;
	}

	@Override
	protected void onUpdate()
	{
		if(!world.isRemote&&!isDummy())
			tickRadioCooldown();
		if(!construction.update())
			return;
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ENERGY_INPUT:
				return getPOI("energy");
			case DATA:
				return getPOI("data");
			default:
				return new int[0];
		}
	}

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		if(!isRadioAvailable())
			return;
		energyStorage.extractEnergy(RadioStation.energyUsage, false);
		RadioNetwork.INSTANCE.sendPacket(packet, this, new ArrayList<>());
	}

	//--- IAdvancedMultiblockTileEntity ---//

	@Override
	public MultiblockConstructionManager getConstructionManager()
	{
		return construction;
	}

	//--- IRadioDevice ---//

	@Override
	public void onRadioSend(DataPacket packet)
	{
		soundDelay = 10;
	}

	@Override
	public boolean onRadioReceive(DataPacket packet)
	{
		//Added because of getting double (and fake (with pos -1 and facing north) tile entities) when using world.getTileEntity
		if(isRadioAvailable()&&this.formed&&!this.isDummy()&&isConstructionFinished())
		{
			sendData(packet, facing, getPOI(MultiblockPOI.DATA)[0]);
			soundDelay = 10;
			return true;
		}
		return false;
	}

	@Override
	public boolean isBasicRadio()
	{
		return false;
	}

	@Override
	public int getFrequency()
	{
		if(!isDummy())
			return frequency;
		else
			return master().getFrequency();
	}

	@Override
	public void setFrequency(int value)
	{
		if(!isDummy())
		{
			this.frequency = value;
			updateTileForEvent(SyncEvents.TILE_CUSTOM1);
		}
		else
			master().setFrequency(value);
	}

	@Override
	public float getRange()
	{
		float factor = world.isRainingAt(getPos())?(float)RadioStation.weatherHarshness: 1f;
		return RadioStation.radioRange*factor;
	}

	@Override
	public DimensionBlockPos getDevicePosition()
	{
		return new DimensionBlockPos(getPOIPos("radio_center"), world);
	}

	@Override
	public int getRadioCooldown()
	{
		if(!isDummy())
			return radioCooldown;
		TileEntityRadioStation master = master();
		return master==null?0: master.getRadioCooldown();
	}

	@Override
	public void setRadioCooldown(int ticks)
	{
		if(!isDummy())
			radioCooldown = Math.max(0, ticks);
		else
		{
			TileEntityRadioStation master = master();
			if(master!=null)
				master.setRadioCooldown(ticks);
		}
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}
}
