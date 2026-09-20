package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Searchlight;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.gun.ChillingState;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Exposes hostile targets, combines overlapping searchlights, and forces airborne targets down.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponSearchlight extends EmplacementWeaponLightBase
{
	private static final Map<EntityLivingBase, ExposureCounter> EXPOSURE_COUNTERS = new WeakHashMap<>();

	public EmplacementWeaponSearchlight()
	{
		super();
		this.setup = new MultiblockInteractablePart(20);
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.visionAABB = this.visionAABB.grow(Searchlight.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Searchlight.attackRadius);
		this.chillingState = new ChillingState(200, 240, 80);
		this.aim.withAimSpeed(Searchlight.yawRotateSpeed, Searchlight.pitchRotateSpeed);
	}

	@Override
	public String getName()
	{
		return "searchlight";
	}

	@Override
	public int getShotDelay()
	{
		return Searchlight.shotFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return Searchlight.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return Searchlight.energyUpkeepCost;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initializeGUI(DecoPanel panelBase, DecoPanel panelPlatform)
	{

	}

	@Override
	public int getMaxHealth()
	{
		return Searchlight.maxHealth;
	}

	@Override
	protected float getIlluminationRange()
	{
		return Searchlight.attackRadius;
	}

	@Override
	protected float getExposureRadius()
	{
		return Searchlight.exposureRadius;
	}

	@Override
	protected int getExposureDuration()
	{
		return Searchlight.exposureDuration;
	}

	@Override
	protected int getExposureAmplifier(TileEntityEmplacement te, EntityLivingBase entity)
	{
		long tick = te.getWorld().getTotalWorldTime();
		ExposureCounter counter = EXPOSURE_COUNTERS.computeIfAbsent(entity, ignored -> new ExposureCounter());
		if(counter.tick!=tick)
		{
			counter.tick = tick;
			counter.count = 0;
		}
		return counter.count++;
	}

	@Override
	protected void onTargetIlluminated(TileEntityEmplacement te, Entity entity)
	{
		if(!(entity instanceof EntityLivingBase)||entity.onGround
				||!te.getOwnerIdentity().isHostile((EntityLivingBase)entity))
			return;
		entity.motionY = Math.min(entity.motionY, -Math.max(0f, Searchlight.pullDownSpeed));
		entity.fallDistance = 0;
		entity.velocityChanged = true;
	}

	private static class ExposureCounter
	{
		private long tick = Long.MIN_VALUE;
		private int count;
	}

	protected Vec3d getAimOrigin(TileEntityEmplacement te)
	{
		return super.getAimOrigin(te).addVector(0, 1.4375, 0);
	}
}
