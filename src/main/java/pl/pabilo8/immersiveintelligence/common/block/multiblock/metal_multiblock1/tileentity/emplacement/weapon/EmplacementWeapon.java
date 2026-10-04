package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import com.elytradev.mirage.event.GatherLightsEvent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
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
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityEmplacementSmoke;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityAMTTactile;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.EntityTactileLivingBase;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.ITypeNBTSerializable;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.gun.ChillingState;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.sound.SoundHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.BooleanSupplier;

/**
 * Defines common state, servicing, and lifecycle behavior for an Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 27.09.2026
 * @since 15.02.2024
 */
public abstract class EmplacementWeapon implements ITypeNBTSerializable
{
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC)
	public float health = getMaxHealth();
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC)
	public boolean resupplying = false;
	@Nullable
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC, nullable = true)
	public ChillingState chillingState = null;
	@Nullable
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC, nullable = true)
	public MultiblockInteractablePart setup = null;
	protected AxisAlignedBB visionAABB, attackAABB;
	protected boolean initialized = false;
	protected boolean restoredFromNBT = false;
	private transient boolean partialSync = false;
	@Nullable
	private transient SyncEvents partialSyncEvent = null;
	@Nullable
	protected EntityTactileLivingBase baseEntity;
	@Nullable
	private transient TileEntityEmplacement emplacement;
	private transient ResLoc setupAnimation, chillingAnimation;

	/**
	 * Called after the weapon is installed or loaded from NBT.
	 */
	protected void onInit(TileEntityEmplacement te)
	{
		this.emplacement = te;
		this.initialized = true;
		this.visionAABB = new AxisAlignedBB(new BlockPos(te.getWeaponCenter()));
		this.attackAABB = new AxisAlignedBB(new BlockPos(te.getWeaponCenter()));
		this.setupAnimation = ResLoc.of(IIReference.RES_II,
				"emplacement/weapon/", getName(), "/", getSetupAnimationName());
		this.chillingAnimation = ResLoc.of(IIReference.RES_II, "emplacement/weapon/", getName(), "/chill");

		if(!te.getWorld().isRemote)
		{
			te.tactileHandler.setAdditionalModel("weapon", IIReference.RES_II.with("aabb/emplacement_weapon/"+getName())
					.withExtension(ResLoc.EXT_JSON));
			this.baseEntity = null;
		}
	}

	/**
	 * Runs after the complete class hierarchy has initialized this weapon.
	 */
	protected void onInitComplete(TileEntityEmplacement te)
	{

	}

	public final void init(TileEntityEmplacement te)
	{
		if(!initialized)
		{
			onInit(te);
			onInitComplete(te);
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
		init(te);
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
	 * Handles optional client-side weapon sounds for the current animation state.
	 */
	@SideOnly(Side.CLIENT)
	public void handleClientSounds(SoundHandler soundHandler)
	{

	}

	/**
	 * Stops client-side sounds owned by this weapon before it is replaced.
	 */
	@SideOnly(Side.CLIENT)
	public void stopClientSounds()
	{

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
	 * Applies the complete server-side collision animation pose for this tick.
	 */
	public void applyTactileAnimations(TileEntityEmplacement te)
	{
		applyTactileAnimationSet(te, new ResLoc[0], new float[0]);
	}

	/**
	 * Adds weapon-specific channels to the shared Platform, setup, and chilling animation set.
	 */
	protected final void applyTactileAnimationSet(TileEntityEmplacement te, @Nonnull ResLoc[] weaponAnimations,
												  @Nonnull float[] weaponAnimationTimes)
	{
		if(te.tactileHandler==null)
			return;
		if(weaponAnimations.length!=weaponAnimationTimes.length)
			throw new IllegalArgumentException("Animation and time arrays must have equal lengths");

		ResLoc[] animations = new ResLoc[3+weaponAnimations.length];
		float[] animationTimes = new float[animations.length];
		animations[0] = MultiblockEmplacement.animationPlatform;
		animations[1] = setup==null?null: setupAnimation;
		animations[2] = chillingState==null?null: chillingAnimation;
		animationTimes[0] = te.door.getProgress(0);
		animationTimes[1] = setup==null?0f: setup.getProgress(0);
		animationTimes[2] = getChillProgress(0);
		System.arraycopy(weaponAnimations, 0, animations, 3, weaponAnimations.length);
		System.arraycopy(weaponAnimationTimes, 0, animationTimes, 3, weaponAnimationTimes.length);
		te.tactileHandler.update(animations, animationTimes);
	}

	/**
	 * @return EWR animation name used while installing or setting up this weapon
	 */
	protected String getSetupAnimationName()
	{
		return "install";
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
	 * @param minimumRepairThreshold health ratio below which automatic repair starts
	 * @param maximumRepairThreshold health ratio required before automatic repair ends
	 */
	public EmplacementStateNeeds getServiceNeeds(TileEntityEmplacement te, @Nullable TargetCoordinateReference currentTarget,
												 float minimumRepairThreshold, float maximumRepairThreshold)
	{
		float minimum = MathHelper.clamp(minimumRepairThreshold, 0f, 1f);
		float maximum = MathHelper.clamp(Math.max(maximumRepairThreshold, minimum), 0f, 1f);
		boolean belowMinimum = isBelowHealthThreshold(minimum);
		//Once retreat starts, keep repairing until satisfactory health, even if a target appears.
		boolean automaticRepair = belowMinimum||(te.weaponRepairing&&!isRepairedTo(maximum));
		boolean forcedRepair = te.isWeaponRepairForced();
		if(forcedRepair&&isRepairedTo(1f))
		{
			te.setWeaponRepairForced(false);
			forcedRepair = false;
		}

		te.setWeaponRepairing(automaticRepair||forcedRepair);
		if(handleSupplyService(te)||automaticRepair||forcedRepair)
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

	/**
	 * @return true while the Base resupply loop has useful work to perform
	 */
	public boolean shouldLoopReloadSound()
	{
		return isResupplying();
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

	/**
	 * Reads a facing name or an integral EnumFacing ordinal from a data command.
	 */
	@Nullable
	protected static EnumFacing parseFacing(DataType input)
	{
		int ordinal;
		if(input instanceof NumericDataType)
		{
			NumericDataType numeric = (NumericDataType)input;
			ordinal = numeric.intValue();
			if(numeric.floatValue()!=ordinal)
				return null;
		}
		else if(input instanceof DataTypeString)
		{
			String value = ((DataTypeString)input).value.trim();
			try
			{
				return EnumFacing.valueOf(value.toUpperCase(Locale.ROOT));
			} catch(IllegalArgumentException ignored)
			{
				try
				{
					ordinal = Integer.parseInt(value);
				} catch(NumberFormatException ignoredNumber)
				{
					return null;
				}
			}
		}
		else
			return null;

		EnumFacing[] values = EnumFacing.values();
		return ordinal >= 0&&ordinal < values.length?values[ordinal]: null;
	}

	@Nonnull
	public DataType getDataCallback(String string)
	{
		return switch(string)
		{
			case "weapon_name" -> new DataTypeString(getName());
			case "weapon_health" -> new DataTypeFloat(getHealth());
			default -> new DataTypeNull();
		};
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
	public void onUninstall()
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
			tactile.world.playSound(null, tactile.getPosition(), IISounds.hitMetal.getRicochetSound(), SoundCategory.BLOCKS, 1.5f, armor/amount*0.95f);
			return false;
		}

		float previousHealth = this.health;
		this.health -= amount-armor;
		//The upgrade promises cover when the weapon becomes heavily damaged. The previous
		//zero-health check ran only as the weapon was being destroyed and removed.
		float smokeThreshold = emplacement==null?0:
				getMaxHealth()*MathHelper.clamp(emplacement.weaponHideHealthThreshold, 0f, 1f);
		if(previousHealth > smokeThreshold&&this.health <= smokeThreshold)
			deployEmergencySmoke();
		tactile.world.playSound(null, tactile.getPosition(), IISounds.hitMetal.getImpactSound(), SoundCategory.BLOCKS, 1.5f, 0.95f);
		return false;
	}

	/**
	 * Releases the Emergency Smoke upgrade when damage pushes the weapon below its retreat threshold.
	 */
	private void deployEmergencySmoke()
	{
		if(emplacement==null||emplacement.getWorld()==null||emplacement.getWorld().isRemote
				||!emplacement.isUpgradeInstalled(IIContent.UPGRADE_EMPLACEMENT_FALLBACK_GRENADES))
			return;

		Vec3d center = emplacement.getWeaponCenter();
		int amount = Math.max(1, Emplacement.emergencySmokeFluidAmount);
		double cornerOffset = Math.max(0, Emplacement.emergencySmokeDistance)/Math.sqrt(2d);
		for(int xSign = -1; xSign <= 1; xSign += 2)
			for(int zSign = -1; zSign <= 1; zSign += 2)
				emplacement.getWorld().spawnEntity(new EntityEmplacementSmoke(emplacement.getWorld(),
						center.x+xSign*cornerOffset, center.y, center.z+zSign*cornerOffset,
						amount));
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
		return entity!=null&&entity.isEntityAlive()&&entity!=baseEntity&&!(entity instanceof EntityAMTTactile)&&canSeeEntity(entity);
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

	/**
	 * @return whether the weapon supports selectable direct and ballistic fire modes
	 */
	public boolean isArtilleryWeapon()
	{
		return false;
	}

	/**
	 * @return initial fire mode selected when this weapon is installed
	 */
	public boolean usesBallisticFireByDefault()
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
			nbt.setBoolean("_partial_sync", true);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		boolean canSkip = nbt.getBoolean("_partial_sync");
		NBTSerialisation.synchroniseFor(this, (serializer, weapon) -> serializer.deserializeAll(weapon, nbt, canSkip));
		restoredFromNBT = !initialized;
	}
}
