package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import lombok.Setter;
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
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.MachineStyle;
import pl.pabilo8.immersiveintelligence.api.utils.MultiblockConstructionManager;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Radar;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockRadar;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.radar.RadarContact;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.radar.RadarTargetManager;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager.ITactileListener;
import pl.pabilo8.immersiveintelligence.common.util.IIEnergyStorage;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Scans for entities, radio signals and weather anomalies and sends their positions through data.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 12.10.2026
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityRadar extends TileEntityMultiblockIIGeneric<TileEntityRadar> implements
		IConstructionRequiringDevice, IManagedUpgradableDevice<TileEntityRadar>, IOwnableProperty,
		IIIGuiMultiblockTile, ITactileListener, IManagedDamageResistantMultiblock, IRadioDevice
{
	private static final int RADIO_CONTACT_DURATION = 80;
	public static final int CONTACT_MARKER_DURATION = 20;
	private static final int WEATHER_SCAN_INTERVAL = 20;
	private static final int MAX_RADIO_CONTACTS = 255;
	/**
	 * -- SETTER --
	 * Sets the weather contact provider for an optional compatibility module.
	 */
	@Setter
	private static Function<TileEntityRadar, List<RadarContact>> weatherScanner = radar -> Collections.emptyList();

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
	@SyncNBT(time = 0, events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CUSTOM2})
	public EasyCollection<RadarContact, NBTTagCompound> radioContacts = new EasyCollection<>(RadarContact::new);
	@SyncNBT(time = 0, events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CUSTOM2})
	public EasyCollection<RadarContact, NBTTagCompound> weatherContacts = new EasyCollection<>(RadarContact::new);
	private long nextWeatherScan;
	private boolean contactsDirty;
	private Entity[] lastDetectedTargets = new Entity[0];

	public TileEntityRadar()
	{
		super(MultiblockRadar.INSTANCE);
		this.energyStorage = new IIEnergyStorage(Radar.energyCapacity);
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
		this.weatherContacts.clear();
		this.lastDetectedTargets = new Entity[0];
	}

	@Override
	public void onBeforeFirstTick()
	{
		super.onBeforeFirstTick();
		if(!this.world.isRemote)
		{
			this.tactileManager = new TactileManager(multiblock, this);
			//Contacts are valid only for the current loaded session.
			radioContacts.clear();
			weatherContacts.clear();
		}
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
			boolean contactsChanged = updateContacts()||contactsDirty;
			contactsDirty = false;
			targetManager.update(this, active, dataOutputEnabled);
			if(previousActive!=active||contactsChanged)
				updateTileForEvent(SyncEvents.TILE_CUSTOM2);
			if(contactsChanged)
				sendDetectedTargets(lastDetectedTargets);
			this.tactileManager.update(MultiblockRadar.INSTANCE.animationDish, ((dishRotation)%360)/360f);
		}

	}

	private boolean updateContacts()
	{
		long now = world.getTotalWorldTime();
		boolean changed = radioContacts.removeIf(contact -> contact.getExpiresAt() <= now);
		changed |= weatherContacts.removeIf(contact -> contact.getExpiresAt() <= now);
		if(!isUpgradeInstalled(IIContent.UPGRADE_RADIO_LOCATORS)&&!radioContacts.isEmpty())
		{
			radioContacts.clear();
			changed = true;
		}
		boolean weatherAvailable = IIContent.UPGRADE_WEATHER_ANOMALY_DETECTORS!=null&&
				isUpgradeInstalled(IIContent.UPGRADE_WEATHER_ANOMALY_DETECTORS);
		if(!weatherAvailable)
		{
			changed |= !weatherContacts.isEmpty();
			weatherContacts.clear();
			nextWeatherScan = now;
		}
		else if(active&&now >= nextWeatherScan)
		{
			List<RadarContact> detected = weatherScanner.apply(this);
			changed |= !weatherContacts.isEmpty()||!detected.isEmpty();
			weatherContacts.clear();
			weatherContacts.addAll(detected);
			nextWeatherScan = now+WEATHER_SCAN_INTERVAL;
		}
		return changed;
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
		return new AxisAlignedBB(center.x, center.y, center.z, center.x, center.y, center.z)
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
		Vec3d origin = getRadarCenter();
		for(int i = 0; i < positions.length; i++)
			positions[i] = new DataTypeVector(radioContacts.get(i).getPosition().subtract(origin));
		DataTypeMap weather = new DataTypeMap();
		Map<String, RadarContact> nearest = new LinkedHashMap<>();
		for(RadarContact contact : weatherContacts)
		{
			RadarContact previous = nearest.get(contact.getName());
			if(previous==null||contact.getPosition().squareDistanceTo(origin) < previous.getPosition().squareDistanceTo(origin))
				nearest.put(contact.getName(), contact);
		}
		for(RadarContact contact : nearest.values())
			weather.put(new DataTypeString(contact.getName()), new DataTypeVector(contact.getPosition().subtract(origin)));
		sendData(new DataPacket().with('e', new DataTypeArray(entities)).with('r', new DataTypeArray(positions)).with('w', weather),
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
		Vec3d position = new Vec3d(senderPos).addVector(0.5d, 0.5d, 0.5d);
		long now = world.getTotalWorldTime();
		RadarContact detected = new RadarContact("radio", position, now+RADIO_CONTACT_DURATION, now+CONTACT_MARKER_DURATION);
		for(int i = 0; i < radioContacts.size(); i++)
			if(radioContacts.get(i).getPosition().equals(position))
			{
				radioContacts.set(i, detected);
				contactsDirty = true;
				return true;
			}
		if(radioContacts.size() >= MAX_RADIO_CONTACTS)
			radioContacts.remove(0);
		radioContacts.add(detected);
		contactsDirty = true;
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

	@Override
	public void receiveMessageFromClient(@Nonnull NBTTagCompound message)
	{
		message.removeTag("radio_contacts");
		message.removeTag("weather_contacts");
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
