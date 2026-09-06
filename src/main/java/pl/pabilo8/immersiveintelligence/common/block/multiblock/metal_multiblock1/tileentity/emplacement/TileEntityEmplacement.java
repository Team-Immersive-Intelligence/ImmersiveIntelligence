package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.util.Utils;
import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.ILightEventConsumer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.style.IStyleCustomizable;
import pl.pabilo8.immersiveintelligence.api.style.StyleCustomization;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.MachineStyle;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeOperation;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.client.util.carversound.ConditionCompoundSound;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFlagpole;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementTargetManager;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.UpgradeEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager.ITactileListener;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IManagedDamageResistantMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.MultiblockHealth;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.common.util.sound.SoundHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Coordinates Emplacement platform movement, servicing, weapon operation, and external storage access.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 31.08.2026
 * @ii-approved 0.3.1
 * @since 27.10.2020
 */
@Interface(iface = "com.elytradev.mirage.lighting.ILightEventConsumer", modid = "mirage")
public class TileEntityEmplacement extends TileEntityMultiblockIIGeneric<TileEntityEmplacement> implements IBooleanAnimatedPartsBlock,
		IManagedUpgradableDevice<TileEntityEmplacement>, IOwnableProperty, IStyleCustomizable, IIIGuiMultiblockTile, IManagedDamageResistantMultiblock, ITactileListener, ILightEventConsumer
{
	private static final int WEAPON_REPAIR_INTERVAL = 20;
	private static final int WEAPON_REPAIR_ENERGY_COST = 80;
	private static final float WEAPON_REPAIR_AMOUNT = 1.0f;

	@SyncNBT(events = SyncEvents.TILE_OWNERSHIP_MODIFIED)
	public OwnerIdentity ownerIdentity;
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityEmplacement> upgradeManager;
	@SyncNBT(events = {SyncEvents.TILE_UPGRADES_MODIFIED, SyncEvents.TILE_CLIENT_MESSAGE})
	public StyleCustomization style;
	public TactileManager tactileHandler;

	@SyncNBT(name = "tasks", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CUSTOM1})
	public EmplacementTargetManager taskManager = new EmplacementTargetManager();
	public TargetCoordinateReference currentTarget;

	@SyncNBT(nullable = true, events = {
			SyncEvents.TILE_CUSTOM2,
			SyncEvents.WEAPON_ROTATION,
			SyncEvents.WEAPON_RELOAD,
			SyncEvents.WEAPON_MISC
	})
	public EmplacementWeapon currentWeapon;
	@SyncNBT(events = SyncEvents.TILE_DAMAGED)
	public MultiblockHealth baseHealth;

	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public boolean redstoneControlEnabled = true, dataControlEnabled = true;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE, SyncEvents.TILE_CUSTOM2})
	public boolean weaponRepairing = false;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CUSTOM2})
	public boolean weaponRepairForced = false;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public float weaponHideHealthThreshold = 0.25f, weaponRepairSatisfactoryThreshold = 0.85f;
	private int weaponRepairTicker = 0;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CUSTOM2, SyncEvents.WEAPON_MISC})
	public MultiblockInteractablePart door;

	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound<TileEntityEmplacement> repairSound, resupplySound;
	@SideOnly(Side.CLIENT)
	private SoundHandler sounds;

	public TileEntityEmplacement()
	{
		super(MultiblockEmplacement.INSTANCE);
		this.energyStorage = new FluxStorageAdvanced(Emplacement.energyCapacity);
		this.inventory = NonNullList.withSize(64, ItemStack.EMPTY);
		this.door = new MultiblockInteractablePart(Emplacement.lidTime);
		this.upgradeManager = new UpgradeManager<>(this);
		this.style = new StyleCustomization(MultiblockFlagpole.STYLE_CONSTRAINTS);
		this.baseHealth = new MultiblockHealth(this, Emplacement.baseHealth);
		this.ownerIdentity = DiplomacyHandler.NEUTRAL;
		this.currentTarget = new TargetCoordinateReference(this::getWorld);
		this.taskManager.setWorldSupplier(this::getWorld);
		this.currentWeapon = null;
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.door = null;
		this.upgradeManager = null;
		this.style = null;
		this.ownerIdentity = null;
		this.taskManager = null;
		this.baseHealth = null;
		this.tactileHandler = null;
	}

	@Override
	public void onBeforeFirstTick()
	{
		super.onBeforeFirstTick();
		if(world.isRemote)
			this.sounds = new SoundHandler(this);
		else
			this.tactileHandler = new TactileManager(this.multiblock, this);
	}

	@Override
	protected void onUpdate()
	{
		//Initialize the weapon even when the Emplacement does not have operating power.
		if(this.currentWeapon!=null)
			this.currentWeapon.init(this);

		//The client only advances synchronized animation state. All operating decisions are server-owned.
		if(world.isRemote)
		{
			door.update();
			if(currentWeapon!=null)
				currentWeapon.onClientUpdate(this);
			handleSounds();
			return;
		}

		//Advance passive state before making this tick's server decisions.
		door.update();
		if(currentWeapon!=null)
			currentWeapon.onPlatformUpdate(this);

		boolean powered = energyStorage.extractEnergy(Emplacement.baseEnergyUsage, false)==Emplacement.baseEnergyUsage;
		if(powered)
		{
			TargetCoordinateReference previousTarget = currentTarget;
			currentTarget = taskManager.updateAndGetTarget(this, currentTarget);
			if(previousTarget!=currentTarget)
				updateTileForEvent(SyncEvents.TILE_CUSTOM1);

			EmplacementStateNeeds baseNeeds = redstoneControlEnabled?
					(getRedstoneAtPos(0)?EmplacementStateNeeds.WANTS_SURFACE: EmplacementStateNeeds.MUST_HIDE):
					EmplacementStateNeeds.WANTS_SURFACE;
			EmplacementStateNeeds serviceNeeds = currentWeapon==null?EmplacementStateNeeds.WANTS_HIDE:
					currentWeapon.getServiceNeeds(this, currentTarget, weaponHideHealthThreshold, weaponRepairSatisfactoryThreshold);
			EmplacementStateNeeds requestedNeeds = combineNeeds(baseNeeds, serviceNeeds);

			//Apply service/redstone requirements before weapon logic so an unsafe weapon cannot fire while hiding starts.
			setPlatformState(requestedNeeds==EmplacementStateNeeds.WANTS_SURFACE);

			EmplacementStateNeeds weaponNeeds = requestedNeeds;
			if(currentWeapon!=null&&serviceNeeds!=EmplacementStateNeeds.MUST_HIDE
					&&energyStorage.extractEnergy(currentWeapon.getEnergyUpkeepCost(), false)==currentWeapon.getEnergyUpkeepCost())
				weaponNeeds = currentWeapon.onUpdate(this, requestedNeeds, currentTarget);

			setPlatformState(combineNeeds(requestedNeeds, weaponNeeds)==EmplacementStateNeeds.WANTS_SURFACE);

			if(!door.getState()&&door.isFullyClosed())
				repairWeaponInBase();
		}

		if(currentWeapon!=null)
			currentWeapon.onServerTick(this, currentTarget, powered);
		this.tactileHandler.update(MultiblockEmplacement.animationPlatform, door.getProgress(0));
	}

	private void setPlatformState(boolean surface)
	{
		if(door.setState(surface)&&!world.isRemote)
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
	}

	private EmplacementStateNeeds combineNeeds(EmplacementStateNeeds base, EmplacementStateNeeds weapon)
	{
		//Hard requirements win over soft wishes. Otherwise a redstone signal could keep a broken or empty weapon exposed.
		if(base==EmplacementStateNeeds.MUST_HIDE||weapon==EmplacementStateNeeds.MUST_HIDE)
			return EmplacementStateNeeds.MUST_HIDE;
		if(base==EmplacementStateNeeds.WANTS_SURFACE||weapon==EmplacementStateNeeds.WANTS_SURFACE)
			return EmplacementStateNeeds.WANTS_SURFACE;
		return EmplacementStateNeeds.WANTS_HIDE;
	}

	public boolean isWeaponRepairForced()
	{
		return weaponRepairForced;
	}

	public void setWeaponRepairForced(boolean forced)
	{
		boolean requested = forced&&currentWeapon!=null&&!currentWeapon.isRepairedTo(1f);
		if(weaponRepairForced==requested)
			return;
		weaponRepairForced = requested;
		if(!requested)
			weaponRepairTicker = 0;
		if(!world.isRemote)
		{
			markDirty();
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
		}
	}

	public void setWeaponRepairing(boolean repairing)
	{
		if(weaponRepairing==repairing)
			return;
		weaponRepairing = repairing;
		if(!world.isRemote)
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
	}

	private void repairWeaponInBase()
	{
		if(currentWeapon==null||world.isRemote)
			return;
		if(!weaponRepairing)
		{
			weaponRepairTicker = 0;
			return;
		}
		if(currentWeapon.isRepairedTo(1f))
		{
			weaponRepairTicker = 0;
			setWeaponRepairForced(false);
			setWeaponRepairing(false);
			return;
		}

		boolean changed = false;
		boolean energyChanged = false;
		if(currentWeapon.getHealth() < currentWeapon.getMaxHealth())
		{
			weaponRepairTicker++;
			if(weaponRepairTicker >= WEAPON_REPAIR_INTERVAL)
			{
				weaponRepairTicker = 0;
				if(energyStorage.extractEnergy(WEAPON_REPAIR_ENERGY_COST, true)==WEAPON_REPAIR_ENERGY_COST)
				{
					energyStorage.extractEnergy(WEAPON_REPAIR_ENERGY_COST, false);
					energyChanged = true;
					changed = currentWeapon.repair(WEAPON_REPAIR_AMOUNT);
				}
			}
		}
		else
			weaponRepairTicker = 0;

		if(energyChanged)
			updateTileForEvent(SyncEvents.TILE_ENERGY_CHANGED);
		if(changed)
		{
			if(currentWeapon.isRepairedTo(1f))
				setWeaponRepairForced(false);
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
		}
	}

	@Override
	public void disassemble()
	{
		super.disassemble();
		if(!isDummy()&&currentWeapon!=null)
			currentWeapon.setDead();
		currentWeapon = null;
	}

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		if(!this.dataControlEnabled)
			return;

		//Let the weapon handle the data packet too
		if(this.currentWeapon!=null)
			if(this.currentWeapon.handleDataCommand(packet.clone()))
				return;

		//Handle callback
		if(IIDataHandlingUtils.isCallbackPacket(packet))
		{
			DataPacket callbackPacket = IIDataHandlingUtils.handleCallback(packet, string -> {
				switch(string)
				{
					case "door":
						return new DataTypeBoolean(door.getState());
					case "door_closed":
						return new DataTypeBoolean(door.isFullyClosed());
					case "door_open":
						return new DataTypeBoolean(door.isFullyOpened());
					case "energy":
						return new DataTypeInteger(energyStorage.getEnergyStored());
					case "repairing":
						return new DataTypeBoolean(weaponRepairing);
					case "repair_forced":
						return new DataTypeBoolean(weaponRepairForced);
					case "resupplying":
						return new DataTypeBoolean(currentWeapon!=null&&currentWeapon.isResupplying());
					case "data_control":
						return new DataTypeBoolean(dataControlEnabled);
					default:
						return (currentWeapon!=null)?currentWeapon.getDataCallback(string): new DataTypeNull();
				}
			});
			if(callbackPacket!=null)
				sendData(callbackPacket, getDirection("data"), pos);
			return;
		}

		//Handle the command
		IIDataHandlingUtils.expectingStringParam('c', packet, command -> {
			switch(command)
			{
				//Door control
				case "opendoor" -> IIPacketHandler.sendToClient(new MessageBooleanAnimatedPartsSync(0, door.setState(true), this));
				case "closedoor" -> IIPacketHandler.sendToClient(new MessageBooleanAnimatedPartsSync(0, door.setState(false), this));
				case "door" ->
				{
					if(IIDataHandlingUtils.asBoolean('b', packet))
						IIPacketHandler.sendToClient(new MessageBooleanAnimatedPartsSync(0, door.setState(true), this));
				}

				//Settings
				case "rscontrol" -> IIDataHandlingUtils.optionalBoolean('b', packet).ifPresent(b -> this.redstoneControlEnabled = b);


				//Gun Action
				case "reload" ->
				{
				}
				case "stop" ->
				{
					this.taskManager.stopTask(true);
					markDirty();
				}
				case "resume" ->
				{
					this.taskManager.resumeTask(false);
					markDirty();
				}
				case "clear" ->
				{
					this.taskManager.stopTask(true);
					this.taskManager.clearFireMissions();
					this.taskManager.resumeTask(true);
					markDirty();
				}
				case "repair" -> setWeaponRepairForced(IIDataHandlingUtils.optionalBoolean('b', packet).orElse(true));
				case "target", "targetreset" ->
				{
					//Set the default task id
					if(command.equals("target"))
						this.taskManager.skipTask(IIDataHandlingUtils.optionalInt('i', packet).orElse(0));
					//Reset the current task to the default task
					this.taskManager.resumeTask(true);
					markDirty();
					updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				}
				case "fire" ->
				{
					Optional<DataTypeEntity> e = IIDataHandlingUtils.optionalEntity('e', packet);

					if(e.isPresent())
					{
						Entity entityByID = world.getEntityByID(e.get().entityID);
						if(entityByID!=null)
							this.taskManager.addEntityMission(entityByID, IIDataHandlingUtils.optionalInt('a', packet).orElse(1));
						updateTileForEvent(SyncEvents.TILE_CUSTOM1);
					}
					else
					{
						int amount = IIDataHandlingUtils.optionalInt('a', packet).orElse(1);
						IIDataHandlingUtils.expectingVectorParam(packet, vec -> {
									//Block/Vector based
									this.taskManager.addPositionMission(new BlockPos(vec).add(getPOIPos("weapon")), amount);
								},
								angle -> {
									//Yaw+Pitch based
									double true_angle = Math.toRadians(-angle.x);
									double true_angle2 = Math.toRadians(angle.y);
									int distance = IIDataHandlingUtils.optionalInt('d', packet).orElse(40);

									this.taskManager.addPositionMission(new BlockPos(IIMath.offsetPosDirection(distance,
											true_angle, true_angle2)).add(getPOIPos("weapon")), amount);
								});
					}
					//Synchronize the task
					markDirty();
					updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				}
			}
		});
	}

	@Override
	public void receiveMessageFromClient(@Nonnull NBTTagCompound message)
	{
		NBTTagCompound sanitized = message.copy();
		boolean tasksChanged = false;
		if(taskManager!=null&&sanitized.hasKey("tasks", EasyNBT.TAG_COMPOUND))
		{
			tasksChanged = taskManager.applyClientUpdate(sanitized.getCompoundTag("tasks"));
			sanitized.removeTag("tasks");
		}
		super.receiveMessageFromClient(sanitized);
		if(tasksChanged)
		{
			markDirty();
			updateTileForEvent(SyncEvents.TILE_CUSTOM1);
		}
	}

	/**
	 * Sends an array of spotted entities on the data port
	 *
	 * @param spottedEntity Array of spotted entities, can be empty
	 */
	public void handleSendingEnemyPos(Entity[] spottedEntity)
	{
		if(!dataControlEnabled)
			return;
		DataPacket packet = new DataPacket();
		final BlockPos center = getPOIPos("weapon");
		DataTypeEntity[] entities = new DataTypeEntity[spottedEntity.length];
		for(int i = 0; i < spottedEntity.length; i++)
			entities[i] = new DataTypeEntity(spottedEntity[i], center);

		packet.set('e', new DataTypeArray(entities));
		IIDataHandlingUtils.sendPacketAdjacently(packet, world, getPOIPos(MultiblockPOI.DATA), facing.rotateYCCW());
	}

	@Nonnull
	@Override
	public UpgradeManager<TileEntityEmplacement> getUpgradeManager()
	{
		return upgradeManager;
	}

	@Override
	public MachineStyle getUpgradableMachineStyle()
	{
		switch(style.getStyle())
		{
			case "sandbags":
				return MachineStyle.SANDBAGS;
			case "wooden":
				return MachineStyle.WOODEN;
			case "steel":
				return MachineStyle.STEEL;
			case "bricks":
				return MachineStyle.BRICKS;
			case "concrete":
				return MachineStyle.CONCRETE;
		}
		return MachineStyle.STEEL;
	}

	@Override
	public boolean addUpgrade(Upgrade upgrade, UpgradeOperation operation)
	{
		boolean added = IManagedUpgradableDevice.super.addUpgrade(upgrade, operation);
		if(!world.isRemote&&operation==UpgradeOperation.FORCE_ADD&&added&&upgrade instanceof UpgradeEmplacementWeapon)
		{
			assert currentWeapon==null;
			this.currentWeapon = ((UpgradeEmplacementWeapon<?>)upgrade).createWeapon();
			this.currentWeapon.init(this);
			this.weaponRepairTicker = 0;
			this.weaponRepairing = false;
			this.weaponRepairForced = false;
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
		}
		return added;
	}

	@Override
	public boolean removeUpgrade(Upgrade upgrade)
	{
		boolean removed = IManagedUpgradableDevice.super.removeUpgrade(upgrade);
		//Removing the weapon
		if(!world.isRemote&&removed&&upgrade instanceof UpgradeEmplacementWeapon)
		{
			if(this.currentWeapon!=null)
			{
				dropWeaponStorage();
				this.currentWeapon.setDead();
			}
			this.currentWeapon = null;
			this.weaponRepairing = false;
			this.weaponRepairForced = false;
			if(this.tactileHandler!=null) this.tactileHandler.setAdditionalModel("weapon", null);
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
		}
		return removed;
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
		return true;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.EMPLACEMENT_STORAGE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent gatherLightsEvent)
	{
		if(isDummy())
			return;

		if(currentWeapon!=null)
			currentWeapon.gatherLights(gatherLightsEvent);
	}

	public Vec3d getWeaponCenter()
	{
		return new Vec3d(getPOIPos("weapon").up()).addVector(0.5, 0, 0.5);
	}


	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return currentWeapon!=null&&currentWeapon.isStackValid(slot, stack);
	}

	//--- Capabilities ---//

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
	{
		TileEntityEmplacement master = master();
		if(master!=null&&master.currentWeapon!=null)
		{
			boolean input = isPOI("input")&&facing==getDirection("input");
			boolean output = isPOI("output")&&facing==getDirection("output");
			if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&(input||output))
				return master.currentWeapon.getBaseItemHandler(input)!=null;
			if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY&&input)
				return master.currentWeapon.getBaseFluidHandler()!=null;
		}
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
	{
		TileEntityEmplacement master = master();
		if(master!=null&&master.currentWeapon!=null)
		{
			boolean input = isPOI("input")&&facing==getDirection("input");
			boolean output = isPOI("output")&&facing==getDirection("output");
			if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&(input||output))
			{
				IItemHandler handler = master.currentWeapon.getBaseItemHandler(input);
				if(handler!=null)
					return (T)handler;
			}
			if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY&&input)
			{
				IFluidHandler handler = master.currentWeapon.getBaseFluidHandler();
				if(handler!=null)
					return (T)handler;
			}
		}
		return super.getCapability(capability, facing);
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
		IILogger.debug("Owner Identity for "+uuid+" : "+ownerIdentity+" / world is "+(world.isRemote?"remote": "local"));
	}

	@Override
	public int getChunkOwnershipRadius()
	{
		return Emplacement.chunkClaimRadius;
	}

	//--- IStyleCustomizable ---//

	@Override
	public StyleCustomization getStyle()
	{
		return style;
	}

	//--- IManagedDamageResistantMultiblock ---//

	@Override
	public MultiblockHealth getHealthManager()
	{
		return baseHealth;
	}

	@Override
	public float getExplosionResistance()
	{
		return 3;
	}


	//--- IBooleanAnimatedPartsBlock ---//

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		door.setState(state);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(door.setState(state))
			IIPacketHandler.sendToClient(this, new MessageBooleanAnimatedPartsSync(door, this));
	}

	//--- ITactileListener ---//

	@Nullable
	@Override
	public TactileManager getTactileHandler()
	{
		return tactileHandler;
	}

	@Override
	public boolean onTactileInteract(EntityAMTTactile tactile, EntityPlayer player, EnumHand hand)
	{
		BlockPos pos = this.getPos();
		player.openGui(ImmersiveIntelligence.INSTANCE, IIGUI.EMPLACEMENT_STORAGE.ordinal(),
				getWorld(), pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public boolean onTactileDamage(EntityAMTTactile tactile, DamageSource source, float amount)
	{
		if(tactile.name.equals("door1")||tactile.name.equals("door2"))
			return damageHealth(amount*0.5f);
		if(currentWeapon!=null)
		{
			float previousHealth = currentWeapon.getHealth();
			boolean result = currentWeapon.applyDamage(tactile, source, amount);
			if(!world.isRemote&&Float.compare(previousHealth, currentWeapon.getHealth())!=0)
				updateTileForEvent(SyncEvents.TILE_CUSTOM2);
			return result;
		}

		return ITactileListener.super.onTactileDamage(tactile, source, amount);
	}

	public enum EmplacementStateNeeds
	{
		WANTS_SURFACE,
		WANTS_HIDE,
		MUST_HIDE;
	}

	@SideOnly(Side.CLIENT)
	private void handleSounds()
	{
		if(sounds==null)
			sounds = new SoundHandler(this);
		if(!door.isFullyClosed()&&!door.isFullyOpened())
		{
			float progress = door.getProgress(0);
			int animationTime = Math.round((door.getState()?progress: 1f-progress)*Emplacement.lidTime);
			if(door.getState())
				MultiblockEmplacement.INSTANCE.openingSoundAnimation.handleSounds(sounds, animationTime, 0.75f);
			else
				MultiblockEmplacement.INSTANCE.closingSoundAnimation.handleSounds(sounds, animationTime, 0.75f);
		}

		if(repairSound==null)
			repairSound = new ConditionCompoundSound<>(IISounds.weldingLoop, getWeaponCenter(), this,
					te -> !te.isInvalid()&&te.currentWeapon!=null&&te.weaponRepairing&&te.door.isFullyClosed()
							&&te.currentWeapon.getHealth() < te.currentWeapon.getMaxHealth());
		if(resupplySound==null)
			resupplySound = new ConditionCompoundSound<>(IISounds.rollingLoop, getWeaponCenter(), this,
					te -> !te.isInvalid()&&te.currentWeapon!=null&&te.currentWeapon.isResupplying()&&te.door.isFullyClosed());
	}

	//--- Item Dropping ---//

	/**
	 * Sends an item through the Emplacement output port and drops any remainder outside it.
	 */
	public boolean outputItem(ItemStack stack)
	{
		if(stack.isEmpty()||world.isRemote)
			return false;

		BlockPos output = getPOIPos("output");
		EnumFacing direction = getDirection("output");
		assert direction!=null;
		BlockPos outside = output.offset(direction);
		TileEntity target = world.getTileEntity(outside);
		ItemStack remainder = stack;

		if(target!=null)
		{
			EnumFacing targetSide = direction.getOpposite();
			if(target.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, targetSide))
			{
				IItemHandler handler = target.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, targetSide);
				if(handler!=null)
					remainder = ItemHandlerHelper.insertItemStacked(handler, stack, false);
			}
		}

		if(!remainder.isEmpty())
			Utils.dropStackAtPos(world, outside, remainder);
		return true;
	}

	private void dropWeaponStorage()
	{
		if(currentWeapon==null||world.isRemote)
			return;

		currentWeapon.clearFluids();
		EnumFacing direction = getDirection("output");
		BlockPos output = getPOIPos("output");
		assert direction!=null;
		BlockPos dropPos = output.offset(direction);
		for(int slot = 0; slot < inventory.size(); slot++)
		{
			ItemStack stack = inventory.get(slot);
			if(stack.isEmpty())
				continue;
			Utils.dropStackAtPos(world, dropPos, stack.copy());
			inventory.set(slot, ItemStack.EMPTY);
		}
		weaponRepairTicker = 0;
		setWeaponRepairing(false);
	}
}
