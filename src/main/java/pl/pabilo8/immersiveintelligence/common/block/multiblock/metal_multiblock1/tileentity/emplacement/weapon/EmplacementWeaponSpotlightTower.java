package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.SpotlightTower;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nullable;

/**
 * Tracks targets and exposes hostile entities inside the illuminated area.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponSpotlightTower extends EmplacementWeaponLightBase
{
	public EmplacementWeaponSpotlightTower()
	{
		this.setup = new MultiblockInteractablePart(SpotlightTower.setupTime);
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.visionAABB = this.visionAABB.grow(SpotlightTower.detectionRadius);
		this.attackAABB = this.attackAABB.grow(SpotlightTower.attackRadius);
		this.aim.withAimSpeed(SpotlightTower.yawRotateSpeed, SpotlightTower.pitchRotateSpeed);
	}

	@Override
	public String getName()
	{
		return "spotlight_tower";
	}

	@Override
	public int getShotDelay()
	{
		return SpotlightTower.shotFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return SpotlightTower.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return SpotlightTower.energyUpkeepCost;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initializeGUI(DecoPanel panelBase, DecoPanel panelPlatform)
	{

	}

	@Override
	public int getMaxHealth()
	{
		return SpotlightTower.maxHealth;
	}

	@Override
	protected float getIlluminationRange()
	{
		return SpotlightTower.attackRadius;
	}

	@Override
	protected float getExposureRadius()
	{
		return SpotlightTower.exposureRadius;
	}

	@Override
	protected int getExposureDuration()
	{
		return SpotlightTower.exposureDuration;
	}

	@Nullable
	@Override
	protected Float getHidingPitch()
	{
		return 0f;
	}

	@Nullable
	@Override
	protected Float getHidingYaw()
	{
		return 0f;
	}

	@Override
	protected boolean canTrackTarget(TileEntityEmplacement te)
	{
		return true;
	}
}
