package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.ILightEventConsumer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeEntity;
import pl.pabilo8.immersiveintelligence.api.style.IStyleCustomizable;
import pl.pabilo8.immersiveintelligence.api.style.StyleCustomization;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.DeviceTier;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFlagpole;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementTaskEntity;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementTaskManager;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementTaskPosition;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task.EmplacementTaskShells;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyUtils;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 05.09.2025
 * @ii-approved 0.3.1
 * @since 27.10.2020
 */
@Interface(iface = "com.elytradev.mirage.lighting.ILightEventConsumer", modid = "mirage")
public class TileEntityEmplacement extends TileEntityMultiblockIIGeneric<TileEntityEmplacement> implements IBooleanAnimatedPartsBlock,
		IManagedUpgradableDevice<TileEntityEmplacement>, IOwnableProperty, IStyleCustomizable, IIIGuiMultiblockTile, ILightEventConsumer
{
	@SyncNBT(events = SyncEvents.TILE_OWNERSHIP_MODIFIED)
	public OwnerIdentity ownerIdentity;
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityEmplacement> upgradeManager;
	@SyncNBT(events = {SyncEvents.TILE_UPGRADES_MODIFIED, SyncEvents.TILE_CLIENT_MESSAGE})
	public StyleCustomization style;

	@SyncNBT(name = "tasks", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CUSTOM1})
	public EmplacementTaskManager taskManager = new EmplacementTaskManager();
	@SyncNBT(nullable = true)
	public EmplacementWeapon currentWeapon;
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
	}

	@Override
	protected void onUpdate()
	{
		door.update();

		if(door.setState(getRedstoneAtPos(0)))
			forceTileUpdate();

		/*boolean wasDoorOpened = isDoorOpened;
		if(upgradeManager.getCurrentUpgrade()!=null)
			isDoorOpened = true;
		else if(currentWeapon!=null&&((forcedRepair&&currentWeapon.getHealth()!=currentWeapon.getMaxHealth())||currentWeapon.requiresPlatformRefill()))
			isDoorOpened = false;
		else if(currentWeapon!=null&&(currentWeapon.getHealth()/(float)currentWeapon.getMaxHealth() <= autoRepairAmount))
		{
			forcedRepair = true;
			isDoorOpened = false;
		}
		else if(!world.isRemote&&redstoneControl)
			if(isDoorOpened^getRedstoneAtPos(0))
				isDoorOpened = getRedstoneAtPos(0);

		if(!world.isRemote&&wasDoorOpened^isDoorOpened)
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(0, isDoorOpened, this.getPos()), IIPacketHandler.targetPointFromTile(this, 48));

		if(currentWeapon!=null)
		{
			if(forcedRepair)
				forcedRepair = currentWeapon.getHealth()!=currentWeapon.getMaxHealth();

			if(!world.isRemote&&world.getTotalWorldTime()%60==0)
				currentWeapon.syncWeaponHealth(this);
			if(currentWeapon.isDead())
			{
				if(world.isRemote)
					currentWeapon.spawnDebrisExplosion(this);
				else
					currentWeapon.syncWeaponHealth(this);

				if(currentWeapon.entity!=null)
					currentWeapon.entity.setDead();
				currentWeapon = null;
			}
		}

		*//*if(world.isRemote)
			handleSounds(master());*//*

		if(isDoorOpened)
			if(progress < Emplacement.lidTime)
				progress++;
			else if(currentWeapon!=null&&energyStorage.extractEnergy(currentWeapon.getEnergyUpkeepCost(), true) >= currentWeapon.getEnergyUpkeepCost())
			{
				if(!world.isRemote)
					energyStorage.modifyEnergyStored(-currentWeapon.getEnergyUpkeepCost());

				if(currentWeapon.isSetUp(true))
				{
					currentWeapon.tick(this, true);
					if(this.currentTask!=null)
					{

						if(world.getTotalWorldTime()%Emplacement.sightUpdateTime==0)
							this.currentTask.updateTargets(this);
						target = this.currentTask.getPositionVector(this);

						if(target!=null)
						{
							target[0] = MathHelper.wrapDegrees(target[0]);
							target[1] = MathHelper.wrapDegrees(target[1]);

							currentWeapon.aimAt(target[0], target[1]);

							if(currentWeapon.isAimedAt(target[0], target[1]))
								if(currentWeapon.canShoot(this))
								{
									isShooting = true;
									currentWeapon.shoot(this);
									currentTask.onShot();
								}
								else
									isShooting = false;
							else
								isShooting = false;
						}
						else
						{
							isShooting = false;
							currentWeapon.aimAt(currentWeapon.yaw, currentWeapon.pitch);
						}
					}
					if(currentTask==null||!currentTask.shouldContinue())
						if(defaultTargetMode==-1)
							currentTask = null;
						else
							currentTask = new EmplacementTaskCustom(defaultTaskNBT[defaultTargetMode]);
				}
				else
					currentWeapon.doSetUp(true);
			}
			else if(currentWeapon!=null)
			{
				currentWeapon.tick(this, false);
				if(progress==0&&!forcedRepair&&currentWeapon.requiresPlatformRefill())
				{
					if(!world.isRemote)
						currentWeapon.performPlatformRefill(this);
				}
				else if(currentWeapon.health!=currentWeapon.getMaxHealth())
					if(progress==0)
					{
						if(firstRepairTick)
						{
							BlockPos repairPos = getBlockPosForPos(31);
							if(!world.isRemote)
								world.playSound(null, repairPos.getX(), repairPos.getY()+1, repairPos.getZ(), IISounds.weldingStart, SoundCategory.BLOCKS, 4f, 1f);
							firstRepairTick = false;
						}
						if(repairTick > 0)
							repairTick--;
						else if(energyStorage.getEnergyStored() >= Emplacement.repairCost)
						{
							energyStorage.extractEnergy(Emplacement.repairCost, false);
							currentWeapon.health = Math.min(currentWeapon.health+Emplacement.repairAmount, currentWeapon.getMaxHealth());
							repairTick = Emplacement.repairDelay;
						}

						if(currentWeapon.health==currentWeapon.getMaxHealth())
						{
							BlockPos repairPos = getBlockPosForPos(31);
							if(!world.isRemote)
								world.playSound(null, repairPos.getX(), repairPos.getY()+1, repairPos.getZ(), IISounds.weldingEnd, SoundCategory.BLOCKS, 4f, 1f);
							forcedRepair = false;
							firstRepairTick = true;
						}
					}

				if(currentWeapon.isSetUp(false))
				{
					if(progress > 0)
						progress--;
					//machine gun yaw is limited, use special method
					if(currentWeapon instanceof EmplacementWeaponMachinegun)
						((EmplacementWeaponMachinegun)currentWeapon).aimAtUnrestricted(facing.getHorizontalAngle(), -90);
					else
						currentWeapon.aimAt(facing.getHorizontalAngle(), -90);
				}
				else
					currentWeapon.doSetUp(false);
			}
			else if(progress > 0)
				progress--;*/
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
					IIPacketHandler.sendToClient(this, new MessageBooleanAnimatedPartsSync(0, door.setState(true), this.getPos()));
					break;
				case "closedoor":
					IIPacketHandler.sendToClient(this, new MessageBooleanAnimatedPartsSync(0, door.setState(false), this.getPos()));
					break;
				case "door":
					if(IIDataHandlingUtils.asBoolean('b', packet))
						IIPacketHandler.sendToClient(this, new MessageBooleanAnimatedPartsSync(0, door.setState(true), this.getPos()));
					break;

				//Settings
				case "rscontrol":
					IIDataHandlingUtils.optionalBoolean('b', packet).ifPresent(b -> this.redstoneControl = b);
					break;

				//Gun Action
				case "reload":
					break;
				case "stop":
					//TODO: 14.09.2025 stop repair
					this.taskManager.stopTask(true);
					break;
				case "repair":
					if(this.currentWeapon!=null)
					{
						//TODO: 14.09.2025 stop repair
						//this.forcedRepair = this.currentWeapon.getHealth()!=this.currentWeapon.getMaxHealth();
//						if(this.forcedRepair)
//							this.firstRepairTick = true;
					}
					break;
				case "target":
				case "targetreset":
					//Set the default task id
					if(command.equals("target"))
						this.taskManager.setCurrentTask(IIDataHandlingUtils.optionalInt('i', packet).orElse(0));
					//Reset the current task to the default task
					this.taskManager.resumeTask(true);
					updateTileForEvent(SyncEvents.TILE_CUSTOM1);
					break;
				case "targetshells":
					this.taskManager.setCurrentTask(new EmplacementTaskShells());
					updateTileForEvent(SyncEvents.TILE_CUSTOM1);
					break;
				case "fire":
					java.util.Optional<DataTypeEntity> e = IIDataHandlingUtils.optionalEntity('e', packet);

					if(e.isPresent())
					{
						Entity entityByID = world.getEntityByID(e.get().entityID);
						if(entityByID!=null)
							this.taskManager.setCurrentTask(new EmplacementTaskEntity(entityByID));
						updateTileForEvent(SyncEvents.TILE_CUSTOM1);
					}
					else
					{
						int amount = IIDataHandlingUtils.optionalInt('a', packet).orElse(1);
						IIDataHandlingUtils.expectingVectorParam(packet, vec -> {
									//Block/Vector based
									this.taskManager.setCurrentTask(new EmplacementTaskPosition(new BlockPos(vec).add(getPOIPos("weapon")), amount));
								},
								angle -> {
									//Yaw+Pitch based
									double true_angle = Math.toRadians(-angle.x);
									double true_angle2 = Math.toRadians(angle.y);
									int distance = IIDataHandlingUtils.optionalInt('d', packet).orElse(40);

									this.taskManager.setCurrentTask(new EmplacementTaskPosition(new BlockPos(IIMath.offsetPosDirection(distance,
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
	public DeviceTier getUpgradableMachineTier()
	{
		return DeviceTier.STEEL;
	}

	//TODO: 30.12.2025 on upgrade install event
	/*if(operation==UpgradeOperation.FORCE_ADD&&upgrade instanceof UpgradeEmplacementWeapon)
			if(currentWeapon==null)
			{
				currentWeapon = UpgradeEmplacementWeapon.getWeaponFromName(upgrade.getName());
				currentWeapon.init(this, true);
				if(!world.isRemote)
					currentWeapon.syncWithClient(this);
				return true;
			}*/

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
	@Optional.Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent gatherLightsEvent)
	{
		if(isDummy())
			return;

		/*if(currentWeapon!=null&&forcedRepair)
		{
			BlockPos pp = getBlockPosForPos(31);
			float f = Math.abs(((world.getTotalWorldTime()%6)/6f)-0.5f)*2f;

			gatherLightsEvent.add(Light.builder()
					.pos(pp)
					.color((159+f*40)/255f, (213+f*40)/255f, (215+f*40)/255f, 1f)
					.intensity(3f)
					.build()
			);

		}*/
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
}
