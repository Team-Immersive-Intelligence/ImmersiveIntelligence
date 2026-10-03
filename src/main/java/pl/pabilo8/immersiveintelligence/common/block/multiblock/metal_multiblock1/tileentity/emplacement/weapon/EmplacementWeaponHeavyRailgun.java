package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.HeavyRailgun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIRailgunOverride;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageParticleEffect;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.gun.ChillingState;

import javax.annotation.Nonnull;

/**
 * Implements the four-round Heavy Railgun Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 17.08.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponHeavyRailgun extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponHeavyRailgun()
	{

	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.chillingState = new ChillingState(HeavyRailgun.minimumIdleTime,
				HeavyRailgun.idleAnimationInterval, HeavyRailgun.idleAnimationDuration);
		this.ammoFactory.setAmmo(IIContent.itemRailgunGrenade);
		this.visionAABB = this.visionAABB.grow(HeavyRailgun.detectionRadius);
		this.attackAABB = this.attackAABB.grow(HeavyRailgun.attackRadius);

		setupItemHandlers(te, 8, 0, 4, 0,
				ItemIIRailgunOverride::isAmmo, ItemIIRailgunOverride::isAmmo);
		this.aim.withAimSpeed(HeavyRailgun.yawRotateSpeed, HeavyRailgun.pitchRotateSpeed)
				.withYawLimit(HeavyRailgun.minYaw, HeavyRailgun.maxYaw)
				.withPitchLimit(HeavyRailgun.minPitch, HeavyRailgun.maxPitch);
		this.gunHandler.withShootSound(IISounds.heavyRailgunShot, 55);
		this.rotateAfterFiring = false;
	}

	@Override
	protected void configureProjectile(TileEntityEmplacement te, EntityAmmoProjectile projectile, TargetCoordinateReference target)
	{
		super.configureProjectile(te, projectile, target);
		IIPacketHandler.sendToClient(new MessageParticleEffect("ammo/railgun_fire", te.getWorld(), te.tactileHandler.getPosition("fire"),
				aim.getTarget(0).scale(0.01), aim.getYaw(0), aim.getPitch(0), EasyNBT.newNBT()));
	}

	@Override
	protected boolean storesSpentCasings()
	{
		return false;
	}

	@Override
	protected boolean isSpentCasing(ItemStack stack)
	{
		return false;
	}

	@Override
	protected int[] getReloadStages()
	{
		return new int[]{4};
	}

	/**
	 * @return queued Platform rounds, excluding the batch being loaded or already loaded
	 */
	@Nonnull
	public NonNullList<ItemStack> getUnloadedAmmo()
	{
		NonNullList<ItemStack> queued = NonNullList.create();
		if(platformAmmoHandler==null)
			return queued;

		for(int slot = 0; slot < platformAmmoHandler.getSlots(); slot++)
		{
			ItemStack stack = platformAmmoHandler.getStackInSlot(slot);
			if(ItemIIRailgunOverride.isAmmo(stack))
				queued.add(stack.copy());
		}

		//Loaded rounds stay in the physical inventory until fired. Subtract copies only.
		for(ItemStack loaded : getLoadedAmmo())
		{
			int remaining = loaded.getCount();
			for(ItemStack stack : queued)
			{
				if(remaining <= 0)
					break;
				if(!stack.isEmpty()&&ItemHandlerHelper.canItemStacksStack(stack, loaded))
				{
					int amount = Math.min(remaining, stack.getCount());
					stack.shrink(amount);
					remaining -= amount;
				}
			}
		}
		queued.removeIf(ItemStack::isEmpty);
		return queued;
	}

	@Override
	protected Float getLoadingPitch()
	{
		return HeavyRailgun.loadingPitch;
	}

	@Override
	public String getName()
	{
		return "heavy_railgun";
	}

	@Override
	public boolean isArtilleryWeapon()
	{
		return true;
	}

	@Override
	public int getShotDelay()
	{
		return HeavyRailgun.shotFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return HeavyRailgun.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return HeavyRailgun.energyUpkeepCost;
	}

	@Override
	public int getArmorForPart(String partName)
	{
		if(partName.startsWith("gun"))
			return 20;
		return switch(partName)
		{
			case "turret_child2", "turret_child3", "turret_child4", "turret_child5" -> 1;
			case "turret_child0" -> 12;
			default -> 8;
		};
	}

	@Override
	public int getMaxHealth()
	{
		return HeavyRailgun.maxHealth;
	}
}
