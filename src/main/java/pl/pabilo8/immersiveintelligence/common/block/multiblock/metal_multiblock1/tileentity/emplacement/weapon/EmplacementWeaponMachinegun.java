package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.MachinegunCoolantHandler;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFluidStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Machinegun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.FilteredFluidTank;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Implements the belt-fed Machinegun Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponMachinegun extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	@SyncNBT(name = "coolant", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.WEAPON_MISC, SyncEvents.WEAPON_RELOAD})
	public FilteredFluidTank coolantTank = new FilteredFluidTank(0)
			.withInputFilter(MachinegunCoolantHandler::isValidCoolant);
	private transient boolean hasHeavyBarrel;
	private transient boolean hasWaterCooling;

	public EmplacementWeaponMachinegun()
	{
		super();
		this.setup = new MultiblockInteractablePart(Machinegun.setupTime);
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.ammoFactory.setAmmo(IIContent.itemAmmoMachinegun);
		this.visionAABB = this.visionAABB.grow(Machinegun.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Machinegun.attackRadius);

		setupItemHandlers(te, 16, 2, 8, 8,
				this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(Machinegun.yawRotateSpeed, Machinegun.pitchRotateSpeed)
				.withPitchLimit(Machinegun.minPitch, Machinegun.maxPitch);

		this.rotateAfterFiring = true;
		this.gunHandler.withShootSound(IISounds.machinegunShot, 55)
				.withDryFireSound(IISounds.machinegunShotDry);
		configureUpgrades(te);
	}

	@Override
	public void onPlatformUpdate(TileEntityEmplacement te)
	{
		configureUpgrades(te);
		super.onPlatformUpdate(te);
	}

	@Override
	public void onClientUpdate(TileEntityEmplacement te)
	{
		configureUpgrades(te);
		super.onClientUpdate(te);
	}

	private void configureUpgrades(TileEntityEmplacement te)
	{
		this.hasHeavyBarrel = te.isUpgradeInstalled(IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_HEAVYBARREL);
		this.hasWaterCooling = te.isUpgradeInstalled(IIContent.UPGRADE_EMPLACEMENT_MACHINEGUN_WATERCOOLED);

		int coolantCapacity = hasWaterCooling?Math.max(0, Machinegun.waterCoolingTankCapacity): 0;
		coolantTank.setCapacity(coolantCapacity);
		if(coolantTank.getFluidAmount() > coolantCapacity)
			coolantTank.drain(coolantTank.getFluidAmount()-coolantCapacity, true);
		if(coolantCapacity==0&&coolantTank.getFluidAmount() > 0)
			coolantTank.setFluid(null);

		recoil.withOverheating(hasHeavyBarrel?Machinegun.heavyBarrelOverheat: Machinegun.maxOverheat, Machinegun.passiveCooling, Machinegun.heatPerShot)
				.withCoolantTank(hasWaterCooling?() -> coolantTank: null,
						Math.max(1, Machinegun.waterCoolingFluidUsage));
		gunHandler.withMaxShotDelay(getShotDelay())
				.withShootSound(hasWaterCooling?IISounds.machinegunShotWaterCooled:
						hasHeavyBarrel?IISounds.machinegunShotHeavyBarrel: IISounds.machinegunShot, 55);
	}

	@Override
	public String getName()
	{
		return "machinegun";
	}

	@Override
	public int getShotDelay()
	{
		return hasHeavyBarrel?Machinegun.heavyBarrelFireTime: Machinegun.bulletFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return Machinegun.reloadTime;
	}

	@Override
	protected int getItemTransferSpeed()
	{
		return Machinegun.itemTransferInterval;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return Machinegun.energyUpkeepCost;
	}

	@Nullable
	@Override
	public IFluidHandler getBaseFluidHandler()
	{
		return hasWaterCooling?coolantTank: null;
	}

	@Nullable
	@Override
	public IFluidHandler getPlatformFluidHandler()
	{
		return hasWaterCooling?coolantTank: null;
	}

	@Override
	public void onUninstall()
	{
		coolantTank.setFluid(null);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initializeGUI(DecoPanel panelBase, DecoPanel panelPlatform)
	{
		if(hasWaterCooling)
			panelPlatform.addComponent(new DecoFluidTank(panelPlatform.width-4-34-6, 6)
					.withFluidTank(coolantTank)
					.withHeight(panelPlatform.height-8));
	}

	@Nullable
	@Override
	protected Float getHidingPitch()
	{
		return 0f;
	}

	@Override
	public int getMaxHealth()
	{
		return Machinegun.maxHealth;
	}

	@Nonnull
	@Override
	public DataType getDataCallback(String string)
	{
		return switch(string)
		{
			case "weapon_overheat" -> new DataTypeFloat(recoil.getOverheat(0));
			case "weapon_coolant" -> coolantTank.getFluid()==null?
					new DataTypeFluidStack(): new DataTypeFluidStack(coolantTank.getFluid());
			case "weapon_coolant_remaining" -> new DataTypeInteger(coolantTank.getFluidAmount());
			case "weapon_heavy_barrel" -> new DataTypeBoolean(hasHeavyBarrel);
			case "weapon_water_cooled" -> new DataTypeBoolean(hasWaterCooling);
			default -> super.getDataCallback(string);
		};
	}
}
