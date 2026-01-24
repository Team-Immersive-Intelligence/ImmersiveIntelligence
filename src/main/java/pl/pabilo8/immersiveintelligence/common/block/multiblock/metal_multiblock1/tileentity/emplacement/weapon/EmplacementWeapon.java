package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import com.elytradev.mirage.event.GatherLightsEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.ITypeNBTSerializable;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.02.2024
 */
public abstract class EmplacementWeapon implements ITypeNBTSerializable<NBTTagCompound>
{
	/**
	 * Acts as a hitbox container for the weapon
	 */
	public EntityEmplacementWeapon entity = null;
	public float health = getMaxHealth();
	protected AxisAlignedBB visionAABB, attackAABB;

	/**
	 * Called after the weapon is installed or loaded from NBT
	 * Initialize sight AABB here
	 */
	public void onInit(TileEntityEmplacement te)
	{
		this.health = getMaxHealth();
		this.visionAABB = new AxisAlignedBB(new BlockPos(te.getWeaponCenter()));
		this.attackAABB = new AxisAlignedBB(new BlockPos(te.getWeaponCenter()));
		te.sendData = false;

		//Setup entity
		if(!te.getWorld().isRemote)
		{
			Vec3d vv = te.getWeaponCenter().subtract(0, 1, 0);
			this.entity = new EntityEmplacementWeapon(te.getWorld());
			this.entity.setPosition(vv.x, vv.y, vv.z);
			te.getWorld().spawnEntity(this.entity);
		}
	}

	/**
	 * @return name of the emplacement, must be the same as the name in the weapon registry
	 */
	public abstract String getName();

	/**
	 * Used to update the weapon every tick.
	 *
	 * @param te the emplacement tile entity
	 * @return
	 */
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te)
	{
		return EmplacementStateNeeds.WANTS_SURFACE;
	}

	/**
	 * Internal method for sending an update packet for this weapon on client side.
	 *
	 * @param te the emplacement tile entity
	 */
	protected final void syncWithClient(TileEntityEmplacement te)
	{
		te.updateTileForEvent(SyncEvents.TILE_CUSTOM2);
	}


	public void handleDataPacket(DataPacket packet)
	{

	}

	public void syncWithEntity(EntityEmplacementWeapon entity)
	{
		if(this.entity==null)
			this.entity = entity;
		if(this.entity!=entity)
			return;

		entity.setHealth(health);
		entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getMaxHealth());

	}

	public abstract EmplacementHitboxEntity[] getCollisionBoxes();

	//--- Inventory ---//

	@Nullable
	public IItemHandler getBaseItemHandler()
	{
		return null;
	}

	@Nullable
	public IFluidHandler getBaseFluidHandler()
	{
		return null;
	}

	public abstract int getEnergyUpkeepCost();

	//--- GUI ---//

	@SideOnly(Side.CLIENT)
	public abstract void initializeGUI(DecoPanel panelPlatform);

	//--- Damage ---//

	public abstract int getMaxHealth();

	public final float getHealth()
	{
		return health;
	}

	public final void applyDamage(float damage)
	{
		this.health -= damage;

	}

	public final boolean isDead()
	{
		return health <= 0;
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
		nbt.setFloat("health", health);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		health = nbt.getFloat("health");
	}
}
