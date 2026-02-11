package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.ILightEventConsumer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeEntity;
import pl.pabilo8.immersiveintelligence.api.style.IStyleCustomizable;
import pl.pabilo8.immersiveintelligence.api.style.StyleCustomization;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.MachineStyle;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeOperation;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFlagpole;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementFireMissionEntity;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementFireMissionPosition;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementFireMissionShells;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementTaskManager;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.UpgradeEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IManagedDamageResistantMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.MultiblockHealth;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 05.09.2025
 * @ii-approved 0.3.1
 * @since 27.10.2020
 */
@Interface(iface = "com.elytradev.mirage.lighting.ILightEventConsumer", modid = "mirage")
public class TileEntityEmplacement extends TileEntityMultiblockIIGeneric<TileEntityEmplacement> implements IBooleanAnimatedPartsBlock,
		IManagedUpgradableDevice<TileEntityEmplacement>, IOwnableProperty, IStyleCustomizable, IIIGuiMultiblockTile, IManagedDamageResistantMultiblock, ILightEventConsumer
{
	@SyncNBT(events = SyncEvents.TILE_OWNERSHIP_MODIFIED)
	public OwnerIdentity ownerIdentity;
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityEmplacement> upgradeManager;
	@SyncNBT(events = {SyncEvents.TILE_UPGRADES_MODIFIED, SyncEvents.TILE_CLIENT_MESSAGE})
	public StyleCustomization style;

	@SyncNBT(name = "tasks", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CUSTOM1})
	public EmplacementTaskManager taskManager = new EmplacementTaskManager();
	@SyncNBT(nullable = true, events = SyncEvents.TILE_CUSTOM2)
	public EmplacementWeapon currentWeapon;
	@SyncNBT(events = SyncEvents.TILE_DAMAGED)
	public MultiblockHealth baseHealth;
	@SyncNBT
	public boolean sendData = false;

	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public boolean redstoneControl = true, dataControl = true;
	@SyncNBT
	public MultiblockInteractablePart door;

	public TileEntityEmplacement()
	{
		super(MultiblockEmplacement.INSTANCE);
		this.energyStorage = new FluxStorageAdvanced(Emplacement.energyCapacity);
		this.door = new MultiblockInteractablePart(Emplacement.lidTime);
		this.upgradeManager = new UpgradeManager<>(this);
		this.style = new StyleCustomization(MultiblockFlagpole.STYLE_CONSTRAINTS);
		this.baseHealth = new MultiblockHealth(this, Emplacement.baseHealth);
		this.ownerIdentity = DiplomacyUtils.NEUTRAL;
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
	}

	@Override
	protected void onUpdate()
	{
		door.setState(getRedstoneAtPos(0));
		door.update();

		//Handle targeting

		//Handle weapon
		if(currentWeapon!=null)
			this.currentWeapon.onUpdate(this);
	}

	public List<BlockPos> getAllBlocks()
	{
		TileEntityEmplacement master = master();
		if(master==this||master==null)
		{
			ArrayList<BlockPos> blocks = new ArrayList<>();
			for(int i = 0; i < structureDimensions[0]*structureDimensions[1]*structureDimensions[2]; i++)
				blocks.add(getBlockPosForPos(i));
			return blocks;
		}
		else
			return master.getAllBlocks();
	}

	@Override
	public void disassemble()
	{
		super.disassemble();
		if(!isDummy()&&currentWeapon!=null&&currentWeapon.entity!=null)
			currentWeapon.entity.setDead();
		currentWeapon = null;
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ENERGY_INPUT:
				return getPOI("energy");
			case REDSTONE_INPUT:
				return getPOI("redstone");
			case DATA:
				return getPOI("data");
			case ITEM_INPUT:
			case FLUID_INPUT:
				return getPOI("input");
			case ITEM_OUTPUT:
			case FLUID_OUTPUT:
				return getPOI("output");
			case MISC_WEAPON:
				return getPOI("weapon");
			case MISC_HATCH:
				return getPOI("hatch");
			default:
				return new int[0];
		}
	}

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		if(!this.dataControl)
			return;

		//Let the weapon handle the data packet too
		if(this.currentWeapon!=null)
			this.currentWeapon.handleDataPacket(packet.clone());

		//Handle the command
		IIDataHandlingUtils.expectingStringParam('c', packet, command -> {
			switch(command)
			{
				//Door control
				case "opendoor":
					IIPacketHandler.sendToClient(new MessageBooleanAnimatedPartsSync(0, door.setState(true), this));
					break;
				case "closedoor":
					IIPacketHandler.sendToClient(new MessageBooleanAnimatedPartsSync(0, door.setState(false), this));
					break;
				case "door":
					if(IIDataHandlingUtils.asBoolean('b', packet))
						IIPacketHandler.sendToClient(new MessageBooleanAnimatedPartsSync(0, door.setState(true), this));
					break;

				//Settings
				case "rscontrol":
					IIDataHandlingUtils.optionalBoolean('b', packet).ifPresent(b -> this.redstoneControl = b);
					break;

				//Gun Action
				case "reload":
					break;
				case "stop":
					this.taskManager.stopTask(true);
					break;
				case "repair":
					//TODO: 01.01.2026 repairing
					break;
				case "target":
				case "targetreset":
					//Set the default task id
					if(command.equals("target"))
						this.taskManager.skipTask(IIDataHandlingUtils.optionalInt('i', packet).orElse(0));
					//Reset the current task to the default task
					this.taskManager.resumeTask(true);
					updateTileForEvent(SyncEvents.TILE_CUSTOM1);
					break;
				case "targetshells":
					this.taskManager.addTask(new EmplacementFireMissionShells());
					updateTileForEvent(SyncEvents.TILE_CUSTOM1);
					break;
				case "fire":
					Optional<DataTypeEntity> e = IIDataHandlingUtils.optionalEntity('e', packet);

					if(e.isPresent())
					{
						Entity entityByID = world.getEntityByID(e.get().entityID);
						if(entityByID!=null)
							this.taskManager.addTask(new EmplacementFireMissionEntity(entityByID));
						updateTileForEvent(SyncEvents.TILE_CUSTOM1);
					}
					else
					{
						int amount = IIDataHandlingUtils.optionalInt('a', packet).orElse(1);
						IIDataHandlingUtils.expectingVectorParam(packet, vec -> {
									//Block/Vector based
									this.taskManager.addTask(new EmplacementFireMissionPosition(new BlockPos(vec).add(getPOIPos("weapon")), amount));
								},
								angle -> {
									//Yaw+Pitch based
									double true_angle = Math.toRadians(-angle.x);
									double true_angle2 = Math.toRadians(angle.y);
									int distance = IIDataHandlingUtils.optionalInt('d', packet).orElse(40);

									this.taskManager.addTask(new EmplacementFireMissionPosition(new BlockPos(IIMath.offsetPosDirection(distance,
											true_angle, true_angle2)).add(getPOIPos("weapon")), amount));
								});
					}
					//Synchronize the task
					updateTileForEvent(SyncEvents.TILE_CUSTOM1);
					break;
			}
		});
	}

	/**
	 * Sends an array of spotted entities on the data port
	 *
	 * @param spottedEntity Array of spotted entities, can be empty
	 */
	public void handleSendingEnemyPos(Entity[] spottedEntity)
	{
		DataPacket packet = new DataPacket();
		final BlockPos center = getPOIPos("weapon");
		DataTypeEntity[] entities = Arrays.stream(spottedEntity)
				.map(entity -> new DataTypeEntity(entity, center))
				.toArray(DataTypeEntity[]::new);

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
			this.currentWeapon.onInit(this);
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
			this.currentWeapon = null;
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
		}
		return removed;
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

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
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
		return false;
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
		updateTileForEvent(SyncEvents.TILE_OWNERSHIP_MODIFIED);
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

	public enum EmplacementState
	{
		SURFACED,
		SURFACING,
		HIDING,
		HIDDEN;

		public EmplacementState getNextState()
		{
			switch(this)
			{
				case SURFACED:
					return HIDING;
				case SURFACING:
					return SURFACED;
				case HIDING:
					return HIDDEN;
				case HIDDEN:
					return SURFACING;
				default:
					return this;
			}
		}
	}

	public enum EmplacementStateNeeds
	{
		WANTS_SURFACE,
		WANTS_HIDE,
		MUST_HIDE;
	}

	private EmplacementState getNextState(EmplacementState currentState, EmplacementStateNeeds baseNeeds, @Nullable EmplacementStateNeeds weaponNeeds)
	{
		switch(currentState)
		{
			case SURFACED:
			{
				boolean wantHide = baseNeeds==EmplacementStateNeeds.WANTS_HIDE&&
						(weaponNeeds==null||weaponNeeds==EmplacementStateNeeds.WANTS_HIDE);
				boolean mustHide = baseNeeds==EmplacementStateNeeds.MUST_HIDE||weaponNeeds==EmplacementStateNeeds.MUST_HIDE;
				return (wantHide||mustHide)?currentState.getNextState(): currentState;
			}
			case HIDDEN:
			{
				boolean wantsSurface = baseNeeds==EmplacementStateNeeds.WANTS_SURFACE&&
						(weaponNeeds==null||weaponNeeds==EmplacementStateNeeds.WANTS_SURFACE);
				return wantsSurface?currentState.getNextState(): currentState;
			}
			default:
				return currentState.getNextState();
		}
	}
}
