package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.radio.IRadioDevice;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeEntity;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeVector;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.MachineStyle;
import pl.pabilo8.immersiveintelligence.api.utils.MultiblockConstructionManager;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Radar;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockRadar;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.radar.RadarTargetManager;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager.ITactileListener;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IConstructionRequiringDevice;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IManagedDamageResistantMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.MultiblockHealth;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Scans for entities and upgraded radio contacts and sends their positions through data.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.09.2026
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityRadar extends TileEntityMultiblockIIGeneric<TileEntityRadar> implements
		IConstructionRequiringDevice, IManagedUpgradableDevice<TileEntityRadar>, IOwnableProperty,
		IIIGuiMultiblockTile, ITactileListener, IManagedDamageResistantMultiblock, IRadioDevice
{
	private static final int RADIO_CONTACT_DURATION = 80;
	private static final int MAX_RADIO_CONTACTS = 255;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CUSTOM2})
	public int dishRotation = 0;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CUSTOM2})
	public boolean active = false;
	@SyncNBT(events = SyncEvents.TILE_OWNERSHIP_MODIFIED)
	public OwnerIdentity ownerIdentity;
	@SyncNBT(name = "targets", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CUSTOM1})
	public RadarTargetManager targetManager;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public boolean redstoneControlEnabled = true, dataOutputEnabled = true;
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityRadar> upgrades;
	@SyncNBT(events = SyncEvents.TILE_CONSTRUCTION)
	public MultiblockConstructionManager construction;
	@SyncNBT(events = SyncEvents.TILE_DAMAGED)
	public MultiblockHealth health;
	private TactileManager tactileManager;
	@SyncNBT(events = {SyncEvents.TILE_CLIENT_MESSAGE, SyncEvents.TILE_CUSTOM1})
	public int frequency;
	@SyncNBT(time = 0)
	public int radioCooldown;
	private final List<RadioContact> radioContacts = new ArrayList<>();
	private Entity[] lastDetectedTargets = new Entity[0];

	public TileEntityRadar()
	{
		super(MultiblockRadar.INSTANCE);
		this.energyStorage = new FluxStorageAdvanced(Radar.energyCapacity);
		this.upgrades = new UpgradeManager<>(this);
		this.construction = new MultiblockConstructionManager(this, Radar.constructionEnergy);
		this.health = new MultiblockHealth(this, Radar.baseHealth);
		this.ownerIdentity = DiplomacyHandler.NEUTRAL;
		this.targetManager = new RadarTargetManager();
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.construction = null;
		this.upgrades = null;
		this.health = null;
		this.ownerIdentity = null;
		this.targetManager = null;
		this.tactileManager = null;
		this.radioContacts.clear();
		this.lastDetectedTargets = new Entity[0];
	}

	@Override
	public void onBeforeFirstTick()
	{
		super.onBeforeFirstTick();
		if(!this.world.isRemote)
			this.tactileManager = new TactileManager(multiblock, this);
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
		RadioNetwork.INSTANCE.removeDevice(this);
	}

	@Override
	protected void onUpdate()
	{
		//Check if constructed
		if(!construction.update())
			return;
		if(!world.isRemote)
		{
			tickRadioCooldown();
			RadioNetwork.INSTANCE.addDevice(this);
			if(!dataOutputEnabled)
				lastDetectedTargets = new Entity[0];
		}

		//Rotate dish if powered
		boolean previousActive = active;
		boolean redstoneActive = !redstoneControlEnabled||(redstoneControlInverted^getRedstoneAtPos(0));
		active = redstoneActive&&energyStorage.extractEnergy(Radar.energyUsage, false)==Radar.energyUsage;
		if(active)
			dishRotation = dishRotation >= 360?0: dishRotation+1;

		//Scan and report filtered contacts
		if(!world.isRemote)
		{
			if(previousActive!=active)
				updateTileForEvent(SyncEvents.TILE_CUSTOM2);
			targetManager.update(this, active, dataOutputEnabled);
			if(!isUpgradeInstalled(IIContent.UPGRADE_RADIO_LOCATORS)&&!radioContacts.isEmpty())
			{
				radioContacts.clear();
				sendDetectedTargets(lastDetectedTargets);
			}
			boolean expired = false;
			long now = world.getTotalWorldTime();
			for(Iterator<RadioContact> it = radioContacts.iterator(); it.hasNext(); )
				if(it.next().expiresAt <= now)
				{
					it.remove();
					expired = true;
				}
			if(expired)
				sendDetectedTargets(lastDetectedTargets);
			this.tactileManager.update(MultiblockRadar.INSTANCE.animationDish, ((dishRotation)%360)/360f);
		}

	}

	/**
	 * @return the Radar dish position used as the visibility-test origin
	 */
	public Vec3d getRadarCenter()
	{
		return new Vec3d(getPOIPos("radar")).addVector(0.5d, 0.5d, 0.5d);
	}

	/**
	 * @return the terrain region in which the Radar can detect contacts
	 */
	public AxisAlignedBB getDetectionRangeBB()
	{
		Vec3d center = getRadarCenter();
		return new AxisAlignedBB(center, center)
				.grow(Radar.detectionRadius, 0, Radar.detectionRadius)
				.expand(0, Radar.detectionRadius, 0)
				.expand(0, -Radar.detectionRadius*0.25, 0);
	}

	/**
	 * @return world positions belonging to this Radar which should not obstruct its own visibility
	 */
	public List<BlockPos> getTerrainVisibilityIgnoredPositions()
	{
		return getMultiblockBlocks();
	}

	/**
	 * Sends filtered Radar contacts through the data output.
	 */
	public void sendDetectedTargets(Entity[] targets)
	{
		lastDetectedTargets = targets;
		if(!dataOutputEnabled||world.isRemote)
			return;
		BlockPos center = getPOIPos("radar");
		DataTypeEntity[] entities = new DataTypeEntity[targets.length];
		for(int i = 0; i < targets.length; i++)
			entities[i] = new DataTypeEntity(targets[i], center);
		DataTypeVector[] positions = new DataTypeVector[radioContacts.size()];
		for(int i = 0; i < positions.length; i++)
			positions[i] = new DataTypeVector(radioContacts.get(i).position.getX(),
					radioContacts.get(i).position.getY(), radioContacts.get(i).position.getZ());
		sendData(new DataPacket().with('e', new DataTypeArray(entities)).with('r', new DataTypeArray(positions)),
				getDirection("data"), getPOI(MultiblockPOI.DATA_OUTPUT)[0]);
	}

	//--- IRadioDevice ---//

	@Override
	public void onRadioSend(DataPacket packet)
	{
	}

	@Override
	public boolean onRadioReceive(DataPacket packet)
	{
		return false;
	}

	@Override
	public boolean onRadioReceive(DataPacket packet, IRadioDevice sender)
	{
		if(!isRadioAvailable()||sender==null)
			return false;
		DimensionBlockPos senderPos = sender.getDevicePosition();
		if(senderPos.dimension!=world.provider.getDimension()||
				senderPos.distanceSq(getDevicePosition()) > (double)Radar.detectionRadius*Radar.detectionRadius)
			return false;
		BlockPos position = new BlockPos(senderPos);
		long expiresAt = world.getTotalWorldTime()+RADIO_CONTACT_DURATION;
		for(RadioContact contact : radioContacts)
			if(contact.position.equals(position))
			{
				contact.expiresAt = expiresAt;
				return true;
			}
		if(radioContacts.size() >= MAX_RADIO_CONTACTS)
			radioContacts.remove(0);
		radioContacts.add(new RadioContact(position, expiresAt));
		sendDetectedTargets(lastDetectedTargets);
		return true;
	}

	@Override
	public boolean canRelayRadio()
	{
		return false;
	}

	@Override
	public boolean isRadioAvailable()
	{
		return !world.isRemote&&formed&&!isDummy()&&construction.isConstructionFinished()&&
				isUpgradeInstalled(IIContent.UPGRADE_RADIO_LOCATORS)&&IRadioDevice.super.isRadioAvailable();
	}

	@Override
	public int getFrequency()
	{
		TileEntityRadar master = isDummy()?master(): this;
		return master==null?0: master.frequency;
	}

	@Override
	public void setFrequency(int value)
	{
		TileEntityRadar master = isDummy()?master(): this;
		if(master!=null)
		{
			master.frequency = value;
			if(!world.isRemote)
				master.updateTileForEvent(SyncEvents.TILE_CUSTOM1);
		}
	}

	@Override
	public boolean isBasicRadio()
	{
		return false;
	}

	@Override
	public float getRange()
	{
		TileEntityRadar master = isDummy()?master(): this;
		return master!=null&&master.isUpgradeInstalled(IIContent.UPGRADE_RADIO_LOCATORS)?Radar.detectionRadius: 0;
	}

	@Override
	public DimensionBlockPos getDevicePosition()
	{
		TileEntityRadar master = isDummy()?master(): this;
		return new DimensionBlockPos(master==null?getPos(): master.getPOIPos("radar"), world);
	}

	@Override
	public int getRadioCooldown()
	{
		TileEntityRadar master = isDummy()?master(): this;
		return master==null?0: master.radioCooldown;
	}

	@Override
	public void setRadioCooldown(int ticks)
	{
		TileEntityRadar master = isDummy()?master(): this;
		if(master!=null)
			master.radioCooldown = Math.max(0, ticks);
	}

	private static class RadioContact
	{
		private final BlockPos position;
		private long expiresAt;

		private RadioContact(BlockPos position, long expiresAt)
		{
			this.position = position;
			this.expiresAt = expiresAt;
		}
	}

	@Override
	public void receiveMessageFromClient(@Nonnull NBTTagCompound message)
	{
		boolean changed = false;
		if(targetManager!=null&&message.hasKey("targets", EasyNBT.TAG_COMPOUND))
		{
			changed = targetManager.applyClientUpdate(message.getCompoundTag("targets"));
			message.removeTag("targets");
		}
		super.receiveMessageFromClient(message);
		if(changed)
		{
			markDirty();
			updateTileForEvent(SyncEvents.TILE_CUSTOM1);
		}
	}


	@Override
	public MultiblockConstructionManager getConstructionManager()
	{
		return construction;
	}

	@Nonnull
	@Override
	public UpgradeManager<TileEntityRadar> getUpgradeManager()
	{
		return upgrades;
	}

	@Override
	public MachineStyle getUpgradableMachineStyle()
	{
		return MachineStyle.STEEL;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Override
	public void onGuiOpened(@Nullable EntityPlayer player, boolean clientside)
	{
		if(!clientside)
			updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
	}

	@Override
	public boolean canOpenGui()
	{
		return construction.isConstructionFinished();
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.RADAR;
	}

	//--- IOwnableProperty ---//

	@Override
	public OwnerIdentity getOwnerIdentity()
	{
		return ownerIdentity;
	}

	@Override
	public void setOwnerIdentity(OwnerIdentity ownerIdentity)
	{
		this.ownerIdentity = ownerIdentity;
		if(!world.isRemote)
			updateTileForEvent(SyncEvents.TILE_OWNERSHIP_MODIFIED);
	}

	//--- ITactileListener ---//

	@Nullable
	@Override
	public TactileManager getTactileHandler()
	{
		return tactileManager;
	}

	@Override
	public boolean onTactileDamage(EntityAMTTactile tactile, DamageSource source, float amount)
	{
		return health.damageHealth(amount*2f);
	}

	@Override
	public boolean onTactileInteract(EntityAMTTactile tactile, EntityPlayer player, EnumHand hand)
	{
		player.openGui(ImmersiveIntelligence.INSTANCE, getGuiID(), getWorld(),
				getPos().getX(), getPos().getY(), getPos().getZ());
		return true;
	}

	//--- IManagedDamageResistantMultiblock ---//

	@Override
	public MultiblockHealth getHealthManager()
	{
		return health;
	}

	@Override
	public float getExplosionResistance()
	{
		return 2;
	}
}
