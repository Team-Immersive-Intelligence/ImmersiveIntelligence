package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.PenetrationRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.PenetrationCache;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplates;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.TeslaCoil;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageExplosion;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;

/**
 * Discharges stored energy into entity or block targets without rotating.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponTeslaCoil extends EmplacementWeapon
{
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC)
	public final FluxStorageAdvanced energy = new FluxStorageAdvanced(TeslaCoil.energyStorage);
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC)
	public int chargeTicks;
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC)
	public int targetlessTicks;

	public EmplacementWeaponTeslaCoil()
	{

	}

	@Override
	public String getName()
	{
		return "tesla";
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.visionAABB = this.visionAABB.grow(TeslaCoil.detectionRadius);
		this.attackAABB = this.attackAABB.grow(TeslaCoil.attackRadius);
	}

	@Override
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te, EmplacementStateNeeds baseNeeds,
										  @Nullable TargetCoordinateReference currentTarget)
	{
		//The Base already paid this tick's upkeep before invoking the weapon.
		energy.receiveEnergy(TeslaCoil.energyUpkeepCost, false);
		if(!te.door.isFullyOpened()||currentTarget==null||!currentTarget.shouldBeExecuted(te.getWorld())
				||!isCharged()
				||te.getWorld().getTotalWorldTime()%Math.max(1, TeslaCoil.damageDelay)!=0)
			return super.onUpdate(te, baseNeeds, currentTarget);

		Vec3d targetPosition = currentTarget.supplyCoordinates();
		Vec3d origin = te.getWeaponCenter().addVector(0, 3, 0);
		if(targetPosition==null||energy.extractEnergy(TeslaCoil.energyUsage, true) < TeslaCoil.energyUsage)
			return super.onUpdate(te, baseNeeds, currentTarget);

		Entity target = currentTarget.getEntity();
		if(target instanceof EntityLivingBase)
		{
			EntityLivingBase living = (EntityLivingBase)target;
			living.hurtResistantTime = 0;
			IEDamageSources.causeTeslaDamage(TeslaCoil.damage, false).apply(living);
		}
		else if(currentTarget.isPositionTarget())
		{
			BlockPos blockPosition = currentTarget.getPosition();
			if(blockPosition==null||!te.getWorld().isBlockLoaded(blockPosition))
				return super.onUpdate(te, baseNeeds, currentTarget);
			PenetrationCache.dealBlockBurnDamage(te.getWorld(), TeslaCoil.damage, blockPosition,
					PenetrationRegistry.getPenetrationHandler(te.getWorld().getBlockState(blockPosition)));
		}
		else
			return super.onUpdate(te, baseNeeds, currentTarget);

		energy.extractEnergy(TeslaCoil.energyUsage, false);
		IIPacketHandler.sendToClient(MessageExplosion.createTeslaMessage(te.getWorld(), origin,
				Collections.singletonList(targetPosition)));
		if(te.taskManager.notifyAfterShot(currentTarget))
			te.markDirty();
		return super.onUpdate(te, baseNeeds, currentTarget);
	}

	@Override
	public void onServerTick(TileEntityEmplacement te, @Nullable TargetCoordinateReference currentTarget, boolean canOperate)
	{
		super.onServerTick(te, currentTarget, canOperate);
		int previousCharge = chargeTicks;
		int previousTargetless = targetlessTicks;
		int chargeTime = Math.max(0, TeslaCoil.chargeTime);
		boolean hasTarget = currentTarget!=null&&currentTarget.shouldBeExecuted(te.getWorld());

		if(hasTarget)
		{
			targetlessTicks = 0;
			if(canOperate&&te.door.isFullyOpened()&&chargeTicks < chargeTime)
				chargeTicks++;
		}
		else if(chargeTicks > 0)
		{
			if(targetlessTicks < Math.max(0, TeslaCoil.chargeDownDelay))
				targetlessTicks++;
			else
				chargeTicks--;
		}
		else
			targetlessTicks = 0;

		chargeTicks = Math.min(chargeTicks, chargeTime);
		if(previousCharge!=chargeTicks||previousTargetless!=targetlessTicks)
			syncWithClient(te, SyncEvents.WEAPON_MISC);
	}

	private boolean isCharged()
	{
		return chargeTicks >= Math.max(0, TeslaCoil.chargeTime);
	}

	@Override
	public boolean canSelectAutonomousTarget(Entity entity)
	{
		return entity instanceof EntityLivingBase&&entity.isEntityAlive()&&attackAABB!=null
				&&attackAABB.intersects(entity.getEntityBoundingBox());
	}

	@Override
	public boolean canExecuteFireMission(TargetCoordinateReference target)
	{
		if(target.isAimingOnly())
			return false;
		Entity entity = target.getEntity();
		if(entity!=null)
			return canSelectAutonomousTarget(entity);
		Vec3d coordinates = target.supplyCoordinates();
		return coordinates!=null&&attackAABB!=null&&attackAABB.contains(coordinates);
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return TeslaCoil.energyUpkeepCost;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initializeGUI(DecoPanel panelBase, DecoPanel panelPlatform)
	{
		panelPlatform.addComponent(
				new DecoBar(4, 4+2)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(energy))
						.withHeight(panelPlatform.height-8)
		);
	}

	@Override
	public int getMaxHealth()
	{
		return TeslaCoil.maxHealth;
	}

	@Nonnull
	@Override
	public DataType getDataCallback(String string)
	{
		return switch(string)
		{
			case "weapon_energy" -> new DataTypeInteger(energy.getEnergyStored());
			case "weapon_charge" -> new DataTypeInteger(chargeTicks);
			case "weapon_charge_progress" -> new DataTypeFloat(
					TeslaCoil.chargeTime <= 0?1f: chargeTicks/(float)TeslaCoil.chargeTime);
			case "weapon_charged" -> new DataTypeBoolean(isCharged());
			default -> super.getDataCallback(string);
		};
	}
}
