package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeArray;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeEntity;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.MachineStyle;
import pl.pabilo8.immersiveintelligence.api.utils.MultiblockConstructionManager;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Radar;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockRadar;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager.ITactileListener;
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
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 30.08.2025
 * @ii-approved 0.3.1
 * @since 04.03.2021
 */
public class TileEntityRadar extends TileEntityMultiblockIIGeneric<TileEntityRadar> implements
		IConstructionRequiringDevice, IManagedUpgradableDevice<TileEntityRadar>, IIIGuiMultiblockTile, ITactileListener, IManagedDamageResistantMultiblock
{
	@SyncNBT
	public int dishRotation = 0;
	@SyncNBT
	public boolean active = false;
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityRadar> upgrades;
	@SyncNBT(events = SyncEvents.TILE_CONSTRUCTION)
	public MultiblockConstructionManager construction;
	@SyncNBT(events = SyncEvents.TILE_DAMAGED)
	public MultiblockHealth health;
	private TactileManager tactileManager;

	public TileEntityRadar()
	{
		super(MultiblockRadar.INSTANCE);
		this.energyStorage = new FluxStorageAdvanced(Radar.energyCapacity);
		this.upgrades = new UpgradeManager<>(this);
		this.construction = new MultiblockConstructionManager(this, Radar.constructionEnergy);
		this.health = new MultiblockHealth(this, Radar.baseHealth);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.construction = null;
		this.upgrades = null;
		this.health = null;
	}

	@Override
	public void onBeforeFirstTick()
	{
		super.onBeforeFirstTick();
		if(!this.world.isRemote)
			this.tactileManager = new TactileManager(multiblock, this);
	}

	@Override
	protected void onUpdate()
	{
		//Check if constructed
		if(!construction.update())
			return;

		//Rotate dish if powered
		if(active = redstoneControlInverted^getRedstoneAtPos(0)&&energyStorage.extractEnergy(Radar.energyUsage, false)==Radar.energyUsage)
			dishRotation = dishRotation >= 360?0: dishRotation+1;

		//Scan for entities
		if(!world.isRemote)
		{
			if(active&&world.getTotalWorldTime()%20!=0)
			{
				final BlockPos center = getPOIPos("radar");
				final AxisAlignedBB aabb = new AxisAlignedBB(center).grow(Radar.detectionRadius, 0, Radar.detectionRadius).expand(0, Radar.detectionRadius, 0);
				List<EntityLivingBase> hostiles = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb, input -> input instanceof IMob);

				DataPacket packet = new DataPacket()
						.with('e', new DataTypeArray(
								hostiles.stream().map(entity -> new DataTypeEntity(entity, center)).toArray(DataTypeEntity[]::new)));
				sendData(packet, getDirection("data"), getPOI(MultiblockPOI.DATA_OUTPUT)[0]);
			}
			this.tactileManager.update(MultiblockRadar.INSTANCE.animationDish, dishRotation/360f);
		}

	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case DATA_OUTPUT:
				return getPOI("data");
			case REDSTONE_INPUT:
				return getPOI("redstone");
			case ENERGY_INPUT:
				return getPOI("energy");
		}
		return new int[0];
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
	public boolean canOpenGui()
	{
		return construction.isConstructionFinished();
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.RADAR;
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
