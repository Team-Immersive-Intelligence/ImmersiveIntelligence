package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.IESounds;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.radio.IRadioDevice;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.style.IStyleCustomizable;
import pl.pabilo8.immersiveintelligence.api.style.StyleCustomization;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.MachineStyle;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradeOperation;
import pl.pabilo8.immersiveintelligence.client.util.carversound.ConditionCompoundSound;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Flagpole;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFlagpole;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageExplosion;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.DiplomacyHandler;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.OwnerIdentity;
import pl.pabilo8.immersiveintelligence.common.util.diplomacy.property.IOwnableProperty;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIInventory;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IManagedDamageResistantMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.MultiblockHealth;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIConnectable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static blusunrize.immersiveengineering.api.energy.wires.WireType.MV_CATEGORY;

/**
 * Multiblock responsible for claiming terrain and chunkloading.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.08.2025
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityFlagpole extends TileEntityMultiblockIIConnectable<TileEntityFlagpole> implements IPlayerInteraction,
		IManagedUpgradableDevice<TileEntityFlagpole>, IStyleCustomizable, IOwnableProperty, IIIGuiMultiblockTile,
		IManagedDamageResistantMultiblock, IRadioDevice, IIIInventory
{
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public ItemStack flag = ItemStack.EMPTY;
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityFlagpole> upgradeManager;
	@SyncNBT(events = {SyncEvents.TILE_UPGRADES_MODIFIED, SyncEvents.TILE_CLIENT_MESSAGE})
	public StyleCustomization style;
	@SyncNBT(events = SyncEvents.TILE_DAMAGED)
	public MultiblockHealth health;
	@SyncNBT(events = SyncEvents.TILE_OWNERSHIP_MODIFIED)
	public OwnerIdentity ownerIdentity;
	@SyncNBT(name = "ifluxEnergy", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_ENERGY_CHANGED})
	public FluxStorageAdvanced energyStorage;

	//Settings
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public int chunkLoadingRange = 0;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public boolean displayFactionFlag = true, prioritizeFactionFlag = false,
			soundDistressAlarm = true, sendDistressPacket = true, teslaCoilActive = true;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE, SyncEvents.TILE_CUSTOM1})
	public int frequency;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public boolean distressAlarmActive;

	//Cooldowns
	private int radioCooldown, distressPacketCooldown;

	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound<TileEntityFlagpole> distressAlarmSound;

	public TileEntityFlagpole()
	{
		super(MultiblockFlagpole.INSTANCE);
		this.upgradeManager = new UpgradeManager<>(this);
		this.ownerIdentity = DiplomacyHandler.NEUTRAL;
		this.style = new StyleCustomization(MultiblockFlagpole.STYLE_CONSTRAINTS);
		this.health = new MultiblockHealth(this, Flagpole.baseHealth);
		this.energyStorage = new FluxStorageAdvanced(Flagpole.energyCapacity);
	}

	@Override
	protected void dummyCleanup()
	{
		this.flag = null;
		this.upgradeManager = null;
		this.ownerIdentity = null;
		this.style = null;
		this.health = null;
		this.energyStorage = null;
	}

	@Override
	protected void onUpdate()
	{
		if(isDummy())
			return;

		//On client only update the siren sound
		if(world.isRemote)
		{
			updateDistressAlarmSound();
			return;
		}
		long time = world.getTotalWorldTime();

		//Distress signal upgrade
		if(isUpgradeInstalled(IIContent.UPGRADE_FLAGPOLE_DISTRESS_SIGNAL))
		{
			tickRadioCooldown();
			if(distressPacketCooldown > 0)
				distressPacketCooldown--;

			if(time%Flagpole.distressScanInterval==0)
				updateDistressSignal();
		}
		else
			setDistressAlarmActive(false);

		//Tesla upgrade
		if(!teslaCoilActive||!isUpgradeInstalled(IIContent.UPGRADE_FLAGPOLE_TASER_LOCKS))
			return;
		if(time%Flagpole.teslaScanInterval==0)
			updateTeslaCoil();
	}

	private void updateDistressSignal()
	{
		boolean hostilePresent = hasHostilesNearby();

		//Play alarm sound
		boolean alarmPowered = hostilePresent&&soundDistressAlarm&&
				energyStorage.extractEnergy(Flagpole.distressAlarmEnergyUsage, true)==Flagpole.distressAlarmEnergyUsage;
		energyStorage.extractEnergy(Flagpole.distressAlarmEnergyUsage, !alarmPowered);
		setDistressAlarmActive(alarmPowered);

		//Send the distress packet
		if(hostilePresent&&sendDistressPacket&&distressPacketCooldown <= 0&&isRadioAvailable()&&
				energyStorage.extractEnergy(Flagpole.distressPacketEnergyUsage, true)==Flagpole.distressPacketEnergyUsage)
		{
			energyStorage.extractEnergy(Flagpole.distressPacketEnergyUsage, false);
			BlockPos pos = getPos();
			DataPacket packet = new DataPacket()
					.with('e', new DataTypeString("distress"))
					.with('x', new DataTypeInteger(pos.getX()))
					.with('y', new DataTypeInteger(pos.getY()))
					.with('z', new DataTypeInteger(pos.getZ()));
			RadioNetwork.INSTANCE.sendPacket(packet, this, new ArrayList<>());
			distressPacketCooldown = Flagpole.distressPacketInterval;
		}
	}

	private boolean hasHostilesNearby()
	{
		BlockPos pos = getPos();
		int minX = (pos.getX()>>4)<<4;
		int minZ = (pos.getZ()>>4)<<4;
		AxisAlignedBB chunkBounds = new AxisAlignedBB(minX, 0, minZ, minX+16, world.getHeight(), minZ+16);
		return !world.getEntitiesWithinAABB(EntityLivingBase.class, chunkBounds,
				living -> living.isEntityAlive()&&ownerIdentity.isHostile(living)).isEmpty();
	}

	private void updateTeslaCoil()
	{
		//Drain energy
		int energyCost = Flagpole.teslaEnergyUsage;
		if(energyStorage.extractEnergy(energyCost, true) < energyCost)
			return;

		//Find and strike all hostile entities in range
		Vec3d center = new Vec3d(getPOIPos("tesla")).addVector(0.5, 0.5, 0.5);
		double rangeSq = Flagpole.teslaRange*Flagpole.teslaRange;
		List<EntityLivingBase> targets = world.getEntitiesWithinAABB(EntityLivingBase.class,
				new AxisAlignedBB(getPos()).grow(Flagpole.teslaRange),
				entity -> entity.isEntityAlive()&&ownerIdentity.isHostile(entity)&&
						entity.getDistanceSq(center.x, center.y, center.z) <= rangeSq);

		for(EntityLivingBase target : targets)
		{
			//Payment first
			if(energyStorage.extractEnergy(energyCost, true) < energyCost)
				break;
			energyStorage.extractEnergy(energyCost, false);

			//Apply damage
			IEDamageSources.causeTeslaDamage(Flagpole.teslaDamage, true).apply(target);
			target.addPotionEffect(new PotionEffect(IEPotions.stunned, 128));
		}

		//Play sound
		if(!targets.isEmpty())
		{
			//Send a particle effect message
			IIPacketHandler.sendToClient(MessageExplosion.createTeslaMessage(world, center.addVector(0, 2.5f, 0), targets.stream()
					.map(EntityLivingBase::getPositionVector)
					.collect(Collectors.toList()))
			);
			world.playSound(null, getPos(), IESounds.tesla, SoundCategory.NEUTRAL, 1, 0.5f);
		}
	}

	private void setDistressAlarmActive(boolean active)
	{
		if(distressAlarmActive==active)
			return;
		distressAlarmActive = active;
		updateTileForEvent(SyncEvents.TILE_CUSTOM1);
	}

	@SideOnly(Side.CLIENT)
	private void updateDistressAlarmSound()
	{
		if(!distressAlarmActive)
			return;
		if(distressAlarmSound==null||distressAlarmSound.isDonePlaying())
		{
			distressAlarmSound = new ConditionCompoundSound<>(IISounds.siren,
					new Vec3d(getPos()).addVector(0.5, 0.5, 0.5), this,
					tile -> !tile.isInvalid()&&tile.distressAlarmActive);
			distressAlarmSound.setMaxRange(Flagpole.distressAlarmSoundRange);
		}
	}

	@Override
	public void onChunkUnload()
	{
		RadioNetwork.INSTANCE.removeDevice(this);
		super.onChunkUnload();
	}

	@Override
	public void invalidate()
	{
		RadioNetwork.INSTANCE.removeDevice(this);
		super.invalidate();
	}

	@Override
	public void disassemble()
	{
		super.disassemble();
		if(isDummy()||flag.isEmpty())
			return;

		Utils.dropStackAtPos(world, getBlockPosForPos(67), flag.copy());
		flag = ItemStack.EMPTY;
	}

	//--- IIIInventory ---//

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}


	//--- TileEntityMultiblockIIConnectable ---//


	@Override
	public boolean canConnect()
	{
		TileEntityFlagpole master = master();

		return master!=null&&(master.isUpgradeInstalled(IIContent.UPGRADE_FLAGPOLE_TASER_LOCKS)||
				master.isUpgradeInstalled(IIContent.UPGRADE_FLAGPOLE_DISTRESS_SIGNAL))
				&&super.canConnect();
	}

	@Override
	protected boolean isMatchingCable(WireType cableType)
	{
		return MV_CATEGORY.equals(cableType.getCategory());
	}

	@Override
	public boolean isEnergyOutput()
	{
		return true;
	}

	@Override
	public int outputEnergy(int amount, boolean simulate, int energyType)
	{
		TileEntityFlagpole master = isDummy()?master(): this;
		return master==null||master.energyStorage==null?0: master.energyStorage.receiveEnergy(amount, simulate);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(0.5, 0.5, 0.5)
				.add(new Vec3d(getDirection("power").getDirectionVec()).scale(0.85))
				.add(new Vec3d(getDirection("power").rotateYCCW().getDirectionVec()).scale(mirrored?0.385: -0.385));
	}

	//--- IManagedUpgradableDevice ---//

	@Nonnull
	@Override
	public UpgradeManager<TileEntityFlagpole> getUpgradeManager()
	{
		return upgradeManager;
	}

	@Override
	public boolean removeUpgrade(Upgrade upgrade)
	{
		boolean success = IManagedUpgradableDevice.super.removeUpgrade(upgrade);
		//Remove from current radio network
		if(success&upgrade==IIContent.UPGRADE_FLAGPOLE_DISTRESS_SIGNAL)
		{
			RadioNetwork.INSTANCE.removeDevice(this);
			setDistressAlarmActive(false);
		}
		//Remove from wired network
		if(success&upgrade==IIContent.UPGRADE_FLAGPOLE_TASER_LOCKS||upgrade==IIContent.UPGRADE_FLAGPOLE_DISTRESS_SIGNAL)
			removeCable(null);

		return success;
	}

	@Override
	public boolean addUpgrade(Upgrade upgrade, UpgradeOperation operation)
	{
		boolean success = IManagedUpgradableDevice.super.addUpgrade(upgrade, operation);
		//Register new radio receiver to the network
		if(success&&operation==UpgradeOperation.INSTALL&&upgrade==IIContent.UPGRADE_FLAGPOLE_DISTRESS_SIGNAL)
			RadioNetwork.INSTANCE.addDevice(this);
		return success;
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
	public int getFrequency()
	{
		TileEntityFlagpole master = master();
		return master==null?0: master.frequency;
	}

	@Override
	public void setFrequency(int value)
	{
		TileEntityFlagpole master = master();
		if(master!=null)
		{
			master.frequency = MathHelper.clamp(value, 0, IIConfig.radioBasicMaxFrequency);
			if(!world.isRemote)
				master.updateTileForEvent(SyncEvents.TILE_CUSTOM1);
		}
	}

	@Override
	public boolean isBasicRadio()
	{
		return true;
	}

	@Override
	public float getRange()
	{
		float weatherFactor = world.isRainingAt(getPos())?(float)Flagpole.distressRadioWeatherHarshness: 1f;
		return Flagpole.distressRadioRange*weatherFactor;
	}

	@Override
	public DimensionBlockPos getDevicePosition()
	{
		TileEntityFlagpole target = isDummy()?master(): this;
		return new DimensionBlockPos(target==null?getPos(): target.getPos(), world);
	}

	@Override
	public int getRadioCooldown()
	{
		if(!isDummy())
			return radioCooldown;
		TileEntityFlagpole master = master();
		return master==null?0: master.getRadioCooldown();
	}

	@Override
	public void setRadioCooldown(int ticks)
	{
		if(!isDummy())
			radioCooldown = Math.max(0, ticks);
		else
		{
			TileEntityFlagpole master = master();
			if(master!=null)
				master.setRadioCooldown(ticks);
		}
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
		return Flagpole.chunkClaimRadius;
	}

	@Override
	public int getChunkLoadingRange()
	{
		return MathHelper.clamp(chunkLoadingRange, 0, Math.max(0, Math.min(Flagpole.chunkClaimRadius, Flagpole.maxChunksLoadedRadius)));
	}

	//--- IStyleCustomizable ---//

	@Override
	public StyleCustomization getStyle()
	{
		return style;
	}

	@Override
	public MachineStyle getUpgradableMachineStyle()
	{
		return switch(style.getStyle())
		{
			case "sandbags" -> MachineStyle.SANDBAGS;
			case "wooden" -> MachineStyle.WOODEN;
			case "steel" -> MachineStyle.STEEL;
			case "bricks" -> MachineStyle.BRICKS;
			case "concrete" -> MachineStyle.CONCRETE;
			default -> MachineStyle.STEEL;
		};
	}

	/**
	 * Gets the banner that the flagpole must render.
	 *
	 * @return the decorative banner or the faction banner, based on this flagpole configuration
	 */
	public ItemStack getFlagOrFactionFlag()
	{
		ItemStack decorativeFlag = flag==null?ItemStack.EMPTY: flag;
		ItemStack factionFlag = displayFactionFlag&&ownerIdentity!=null?ownerIdentity.getBanner(): ItemStack.EMPTY;
		boolean hasFactionFlag = factionFlag!=null&&!factionFlag.isEmpty();
		if(hasFactionFlag&&(prioritizeFactionFlag||decorativeFlag.isEmpty()))
			return factionFlag;
		return decorativeFlag;
	}

	//--- IManagedDamageResistantMultiblock ---//

	@Override
	public MultiblockHealth getHealthManager()
	{
		return health;
	}

	@Override
	public boolean damageHealth(float damage)
	{
		if(isUpgradeInstalled(IIContent.UPGRADE_FLAGPOLE_CAPTURE_DEFIANCE))
			damage /= 2;
		return IManagedDamageResistantMultiblock.super.damageHealth(damage);
	}

	@Override
	public float getExplosionResistance()
	{
		return isUpgradeInstalled(IIContent.UPGRADE_FLAGPOLE_CAPTURE_DEFIANCE)?6: 3;
	}

	//--- IIIGuiMultiblockTile ---//


	@Override
	public boolean canOpenGui(EntityPlayer player)
	{
		//Set frequency instead
		if(player.getHeldItemMainhand().getItem()==IIContent.itemRadioTuner)
			return false;
		return IIIGuiMultiblockTile.super.canOpenGui(player);
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.FLAGPOLE;
	}

	//--- IPlayerInteraction ---//

	@Override
	public boolean interact(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, @Nonnull EnumHand hand,
	                        @Nonnull ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntityFlagpole master = master();
		if(!world.isRemote&&master!=null&&isPOI("pole"))
			if(master.flag.isEmpty()&&heldItem.getItem()==Items.BANNER)
			{
				master.flag = heldItem.copy();
				master.flag.setCount(1);
				heldItem.shrink(1);
				master.updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				return true;
			}
			else if(!master.flag.isEmpty()&&Utils.isWirecutter(heldItem))
			{
				player.inventory.addItemStackToInventory(master.flag.copy());
				master.flag = ItemStack.EMPTY;
				master.updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				return true;
			}

		return false;
	}
}
