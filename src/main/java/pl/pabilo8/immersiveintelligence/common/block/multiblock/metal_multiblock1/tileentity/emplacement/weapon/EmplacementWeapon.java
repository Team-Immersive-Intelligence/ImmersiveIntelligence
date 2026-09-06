package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import com.elytradev.mirage.event.GatherLightsEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.ITypeNBTSerializable;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.gun.ChillingState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

/**
 * Defines common state, servicing, and lifecycle behavior for an Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 31.08.2026
 * @since 15.02.2024
 */
public abstract class EmplacementWeapon implements ITypeNBTSerializable
{
	private static final String NBT_PARTIAL_SYNC = "_partial_sync";

	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC)
	public float health = getMaxHealth();
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC)
	protected boolean resupplying = false;
	@Nullable
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC, nullable = true)
	public ChillingState chillingState = null;
	protected AxisAlignedBB visionAABB, attackAABB;
	protected boolean initialized = false;
	protected boolean restoredFromNBT = false;
	private transient boolean partialSync = false;
	@Nullable
	private transient SyncEvents partialSyncEvent = null;
	@Nullable
	protected EntityLivingBase baseEntity;

	/**
	 * Called after the weapon is installed or loaded from NBT.
	 */
	protected void onInit(TileEntityEmplacement te)
	{
		this.initialized = true;
		this.visionAABB = new AxisAlignedBB(new BlockPos(te.getWeaponCenter()));
		this.attackAABB = new AxisAlignedBB(new BlockPos(te.getWeaponCenter()));

		if(!te.getWorld().isRemote)
		{
			te.tactileHandler.setAdditionalModel("weapon", IIReference.RES_II.with("aabb/emplacement_weapon/"+getName())
					.withExtension(ResLoc.EXT_JSON));
			this.baseEntity = null;
		}
	}

	public final void init(TileEntityEmplacement te)
	{
		if(!initialized)
		{
			onInit(te);
			restoredFromNBT = false;
		}
	}

	/**
	 * @return weapon registry name
	 */
	public abstract String getName();

	/**
	 * Updates authoritative weapon logic while the platform can operate.
	 */
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te, EmplacementStateNeeds baseNeeds, TargetCoordinateReference currentTarget)
	{
		if(!initialized)
			this.onInit(te);
		return baseNeeds;
	}

	/**
	 * Updates authoritative state that must continue while the platform moves or stays hidden.
	 */
	public void onPlatformUpdate(TileEntityEmplacement te)
	{

	}

	/**
	 * Advances client-side interpolation only. This method must not make operating decisions.
	 *
	 * @param te owning Emplacement
	 */
	public void onClientUpdate(TileEntityEmplacement te)
	{
		if(chillingState!=null)
			chillingState.updateClient();
	}

	/**
	 * Updates optional idle-animation timing on the server.
	 *
	 * @param te            owning Emplacement
	 * @param currentTarget active fire mission, if present
	 * @param canOperate    true when the Emplacement has base operating power
	 */
	public void onServerTick(TileEntityEmplacement te, @Nullable TargetCoordinateReference currentTarget, boolean canOperate)
	{
		if(chillingState!=null&&chillingState.updateServer(currentTarget!=null, canOperate&&canChill(te)))
			syncWithClient(te, SyncEvents.WEAPON_MISC);
	}

	/**
	 * @return true when an idle animation may start
	 */
	protected boolean canChill(TileEntityEmplacement te)
	{
		return true;
	}

	/**
	 * @param partialTicks partial render tick
	 * @return idle animation progress in the 0.0-1.0 range, or 0 when unsupported/inactive
	 */
	public float getChillProgress(float partialTicks)
	{
		return chillingState==null?0f: chillingState.getProgress(partialTicks);
	}

	/**
	 * Determines if the weapon must stay in the Base for supply or repair.
	 *
	 * @param minimumRepairThreshold minimum health ratio required for a fire mission
	 * @param maximumRepairThreshold health ratio required before routine repair ends
	 */
	public EmplacementStateNeeds getServiceNeeds(TileEntityEmplacement te, @Nullable TargetCoordinateReference currentTarget,
	                                             float minimumRepairThreshold, float maximumRepairThreshold)
	{
		float minimum = MathHelper.clamp(minimumRepairThreshold, 0f, 1f);
		float maximum = MathHelper.clamp(Math.max(maximumRepairThreshold, minimum), 0f, 1f);
		boolean belowMinimum = isBelowHealthThreshold(minimum);
		boolean routineRepair = currentTarget==null&&!isRepairedTo(maximum);
		boolean forcedRepair = te.isWeaponRepairForced();
		if(forcedRepair&&isRepairedTo(1f))
		{
			te.setWeaponRepairForced(false);
			forcedRepair = false;
		}

		te.setWeaponRepairing(belowMinimum||routineRepair||forcedRepair);
		if(handleSupplyService(te)||belowMinimum||routineRepair||forcedRepair)
			return EmplacementStateNeeds.MUST_HIDE;
		return EmplacementStateNeeds.WANTS_SURFACE;
	}

	/**
	 * Handles weapon-specific supply work and returns true while the platform must stay hidden.
	 */
	protected boolean handleSupplyService(TileEntityEmplacement te)
	{
		return false;
	}

	/**
	 * Runs a latched supply cycle shared by item- and fluid-based weapons.
	 */
	protected final boolean updateResupplyState(TileEntityEmplacement te, BooleanSupplier supplyRequired,
	                                            BooleanSupplier servicePending, Runnable serviceAction)
	{
		if(supplyRequired.getAsBoolean())
			setResupplying(te, true);

		//Use any hidden downtime for servicing, even when repair or redstone caused the descent.
		if(!te.door.getState()&&te.door.isFullyClosed())
			serviceAction.run();
		if(!resupplying)
			return false;

		boolean keepHidden = supplyRequired.getAsBoolean()||servicePending.getAsBoolean();
		setResupplying(te, keepHidden);
		return keepHidden;
	}

	/**
	 * @return true while this weapon is in a latched Base resupply cycle
	 */
	public boolean isResupplying()
	{
		return resupplying;
	}

	private void setResupplying(TileEntityEmplacement te, boolean resupplying)
	{
		if(this.resupplying==resupplying)
			return;
		this.resupplying = resupplying;
		syncWithClient(te, SyncEvents.WEAPON_MISC);
	}

	/**
	 * Sends a partial weapon update through the owning tile entity.
	 */
	protected final void syncWithClient(TileEntityEmplacement te, SyncEvents event)
	{
		if(te.getWorld().isRemote)
			return;

		boolean previousPartial = this.partialSync;
		SyncEvents previousEvent = this.partialSyncEvent;
		this.partialSync = true;
		this.partialSyncEvent = event;
		try
		{
			te.updateTileForEvent(event);
		} finally
		{
			this.partialSync = previousPartial;
			this.partialSyncEvent = previousEvent;
		}
	}

	public boolean handleDataCommand(DataPacket packet)
	{
		return false;
	}

	@Nonnull
	public DataType getDataCallback(String string)
	{
		return new DataTypeNull();
	}

	//--- Inventory ---//

	/**
	 * Gets the Base item view for external or GUI access.
	 *
	 * @param input true for supply input, false for spent-item output
	 * @return item handler for the requested direction
	 */
	@Nullable
	public IItemHandler getBaseItemHandler(boolean input)
	{
		return null;
	}

	/**
	 * Gets the Platform item view for GUI access.
	 *
	 * @param input true for supply input, false for spent-item output
	 * @return item handler for the requested direction
	 */
	@Nullable
	public IItemHandler getPlatformItemHandler(boolean input)
	{
		return null;
	}

	/**
	 * Checks if an item can be inserted into an absolute tile inventory slot.
	 */
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Nullable
	public IFluidHandler getBaseFluidHandler()
	{
		return null;
	}

	@Nullable
	public IFluidHandler getPlatformFluidHandler()
	{
		return null;
	}

	/**
	 * Voids fluid owned by the weapon when it is uninstalled.
	 */
	public void clearFluids()
	{

	}

	public boolean isBelowHealthThreshold(float threshold)
	{
		return getMaxHealth() > 0&&getHealth()/getMaxHealth() < threshold;
	}

	public boolean isRepairedTo(float threshold)
	{
		return getMaxHealth() <= 0||getHealth()/getMaxHealth() >= threshold;
	}

	public boolean repair(float amount)
	{
		if(amount <= 0||health >= getMaxHealth())
			return false;
		health = Math.min(getMaxHealth(), health+amount);
		return true;
	}

	public abstract int getEnergyUpkeepCost();

	//--- GUI ---//

	@SideOnly(Side.CLIENT)
	public abstract void initializeGUI(DecoPanel panelBase, DecoPanel panelPlatform);

	//--- Damage ---//

	public abstract int getMaxHealth();

	public final float getHealth()
	{
		return health;
	}

	public int getArmorForPart(String partName)
	{
		return 0;
	}

	public final boolean isDead()
	{
		return health <= 0;
	}

	public void setDead()
	{
		this.health = 0;
	}

	public boolean applyDamage(EntityAMTTactile tactile, DamageSource source, float amount)
	{
		if(source.damageType.equals("bullet"))
			source = new DamageSource("bullet").setProjectile();

		if(source.isFireDamage()||source.isMagicDamage())
			return false;
		if(source.damageType.equals("iiShrapnel")||source.damageType.equals("iiShrapnelNoShooter"))
			return false;

		int armor = getArmorForPart(tactile.getName());
		if((source instanceof ElectricDamageSource||source==IEDamageSources.acid))
			armor = 0;

		if(armor-amount > 0)
		{
			tactile.world.playSound(null, tactile.getPosition(), IISounds.hitMetal.getImpactSound(), SoundCategory.BLOCKS, 1.5f, armor/amount*0.95f);
			return true;
		}

		this.health -= amount-armor;
		tactile.world.playSound(null, tactile.getPosition(), IISounds.hitMetal.getImpactSound(), SoundCategory.BLOCKS, 1.5f, 0.95f);
		return false;
	}

	//--- Range ---//

	public AxisAlignedBB getDetectionRangeBB()
	{
		return visionAABB;
	}

	public AxisAlignedBB getAttackRangeBB()
	{
		return attackAABB;
	}

	public boolean canSeeEntity(Entity entity)
	{
		return !entity.isInvisible();
	}

	/**
	 * Checks cheap common visibility rules before target-tree evaluation.
	 */
	public boolean isVisibleTarget(Entity entity)
	{
		return entity!=null&&!entity.isDead&&entity!=baseEntity&&!(entity instanceof EntityAMTTactile)&&canSeeEntity(entity);
	}

	/**
	 * Checks whether an autonomous entity target can be engaged by this weapon.
	 */
	public boolean canSelectAutonomousTarget(Entity entity)
	{
		return false;
	}

	/**
	 * Checks whether a Fire Mission can execute without removing it when unavailable.
	 */
	public boolean canExecuteFireMission(TargetCoordinateReference target)
	{
		return false;
	}

	//--- Graphics ---//

	@SideOnly(Side.CLIENT)
	public void spawnDebrisExplosion(Vec3d weaponPosition)
	{

	}

	@SideOnly(Side.CLIENT)
	@Optional.Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent gatherLightsEvent)
	{

	}

	//--- NBT ---//

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		SyncEvents event = this.partialSyncEvent;
		NBTSerialisation.synchroniseFor(this, (serializer, weapon) -> {
			if(partialSync&&event!=null)
				serializer.serializeForEvent(weapon, nbt, event);
			else
				serializer.serializeAll(weapon, nbt);
		});
		if(partialSync&&event!=null)
			nbt.setBoolean(NBT_PARTIAL_SYNC, true);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		boolean canSkip = nbt.getBoolean(NBT_PARTIAL_SYNC);
		NBTSerialisation.synchroniseFor(this, (serializer, weapon) -> serializer.deserializeAll(weapon, nbt, canSkip));
		restoredFromNBT = !initialized;
	}
}
