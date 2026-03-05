package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.CPDS;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;

public class EmplacementWeaponSearchlight extends EmplacementWeaponTurretBase
{
	public EmplacementWeaponSearchlight()
	{

	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.visionAABB = this.visionAABB.grow(Autocannon.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Autocannon.attackRadius);
		this.aim.withAimSpeed(CPDS.yawRotateSpeed, CPDS.pitchRotateSpeed);
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return false;
	}

	@Override
	public String getName()
	{
		return "searchlight";
	}

	@Override
	public int getShotDelay()
	{
		return Autocannon.bulletFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return Autocannon.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return Autocannon.energyUpkeepCost;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initializeGUI(DecoPanel panelBase, DecoPanel panelPlatform)
	{

	}

	@Override
	public int getMaxHealth()
	{
		return Autocannon.maxHealth;
	}
}
