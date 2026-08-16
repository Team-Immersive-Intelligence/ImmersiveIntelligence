package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import com.elytradev.mirage.event.GatherLightsEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 16.08.2026
 * @since 15.02.2024
 */
public abstract class EmplacementWeapon implements ITypeNBTSerializable
{
	private static final String NBT_PARTIAL_SYNC = "_partial_sync";

	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC)
	public float health = getMaxHealth();
	protected AxisAlignedBB visionAABB, attackAABB;
	protected boolean initialized = false;
	protected boolean restoredFromNBT = false;
	private transient boolean partialSync = false;
	@Nullable
	private transient SyncEvents partialSyncEvent = null;
	@Nullable
	protected EntityLivingBase baseEntity;

	/**
	 * Called after the weapon is installed or loaded from NBT
	 * Initialize sight AABB here
	 */
	protected void onInit(TileEntityEmplacement te)
	{
		this.initialized = true;
		this.visionAABB = new AxisAlignedBB(new BlockPos(te.getWeaponCenter()));
		this.attackAABB = new AxisAlignedBB(new BlockPos(te.getWeaponCenter()));

		//Setup entity (AMT Tactiles)
		if(!te.getWorld().isRemote)
		{
			te.tactileHandler.setAdditionalModel("weapon", IIReference.RES_II.with("aabb/emplacement_weapon/"+getName())
					.withExtension(ResLoc.EXT_JSON)
			);
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
	 * @return name of the emplacement, must be the same as the name in the weapon registry
	 */
	public abstract String getName();

	/**
	 * Used to update the weapon every tick.
	 *
	 * @param te            the emplacement tile entity
	 * @param baseNeeds
	 * @param currentTarget
	 * @return
	 */
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te, EmplacementStateNeeds baseNeeds, TargetCoordinateReference currentTarget)
	{
		if(!initialized)
			this.onInit(te);
		return baseNeeds;
	}

	/**
	 * Sends a partial weapon update through the owning tile entity.
	 *
	 * @param te    owning emplacement
	 * @param event event that selects weapon fields
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

	@Nullable
	public IItemHandler getBaseItemHandler()
	{
		return null;
	}

	@Nullable
	public IItemHandler getPlatformItemHandler()
	{
		return null;
	}

	@Nullable
	public IFluidHandler getBaseFluidHandler()
	{
		return null;
	}

	/**
	 * @return true when this weapon cannot operate until the platform is lowered and serviced.
	 */
	public boolean needsSupply(TileEntityEmplacement te)
	{
		return false;
	}

	public boolean needsRestock(TileEntityEmplacement te)
	{
		return false;
	}

	public boolean restockFromBase(TileEntityEmplacement te)
	{
		return false;
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
		//Immersive Vehicles(tm) compat
		if(source.damageType.equals("bullet"))
			source = new DamageSource("bullet").setProjectile();

		//Resistant to fire and magic damage by default
		if(source.isFireDamage()||source.isMagicDamage())
			return false;
		//Resistant to shrapnel by default
		if(source.damageType.equals("iiShrapnel")||source.damageType.equals("iiShrapnelNoShooter"))
			return false;

		int armor = getArmorForPart(tactile.getName());
		//EMP and acid damage bypass armor
		if((source instanceof ElectricDamageSource||source==IEDamageSources.acid))
			armor = 0;

		//Damage or ricochet
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
