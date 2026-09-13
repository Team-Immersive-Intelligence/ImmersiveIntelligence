package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.common.util.IESounds;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.IIAmmoUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFluidStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoFluidTank;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.HeavyChemthrower;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityIIChemthrowerShot;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Implements direct Platform-fluid firing and Base-to-Platform supply for the Heavy Chemthrower.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponHeavyChemthrower extends EmplacementWeaponTurretBase
{
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.WEAPON_MISC})
	public FluidTank baseTank = new FluidTank(HeavyChemthrower.tankCapacity);
	@SyncNBT(name = "tank", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.WEAPON_MISC, SyncEvents.WEAPON_RELOAD})
	public FluidTank platformTank = new FluidTank(HeavyChemthrower.tankCapacity);
	@SyncNBT(events = SyncEvents.WEAPON_MISC)
	public boolean shouldIgnite = true;

	private int sprayCooldown;
	private boolean useBallisticAngles;

	public EmplacementWeaponHeavyChemthrower()
	{
		this.setup = new MultiblockInteractablePart(HeavyChemthrower.setupTime);
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.visionAABB = this.visionAABB.grow(HeavyChemthrower.detectionRadius);
		this.attackAABB = this.attackAABB.grow(HeavyChemthrower.attackRadius);
		this.aim.withAimSpeed(HeavyChemthrower.yawRotateSpeed, HeavyChemthrower.pitchRotateSpeed)
				.withPitchLimit(-90, 22.5f)
				.withAimCorrectionFunction(this::getAnglePrediction);
	}

	@Override
	public String getName()
	{
		return "heavy_chemthrower";
	}

	@Override
	public boolean isArtilleryWeapon()
	{
		return true;
	}

	@Override
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te, EmplacementStateNeeds baseNeeds, TargetCoordinateReference currentTarget)
	{
		useBallisticAngles = te.shouldUseBallisticFire(currentTarget);
		if(sprayCooldown > 0)
			sprayCooldown--;
		return super.onUpdate(te, baseNeeds, currentTarget);
	}

	private float[] getAnglePrediction(Vec3d shooterPos, Vec3d shooterMotion, Vec3d targetPos, Vec3d targetMotion)
	{
		Vec3d direction = targetPos.add(targetMotion).subtract(shooterPos.add(shooterMotion));
		double horizontalDistance = Math.sqrt(direction.x*direction.x+direction.z*direction.z);
		float yaw = (float)Math.toDegrees(Math.atan2(-direction.x, direction.z));
		float directPitch = (float)-Math.toDegrees(Math.atan2(direction.y, horizontalDistance));
		FluidStack fluid = platformTank.getFluid();
		if(!useBallisticAngles||fluid==null||fluid.getFluid()==null)
			return new float[]{yaw, directPitch};

		boolean gas = fluid.getFluid().isGaseous(fluid)||ChemthrowerHandler.isGas(fluid.getFluid());
		float force = gas?HeavyChemthrower.rangeGas: HeavyChemthrower.rangeFluid;
		double gravity = EntityIIChemthrowerShot.getGravity(fluid);
		double height = -direction.y;
		float angle = IIAmmoUtils.calculateBallisticAngle(horizontalDistance, gravity < 0?-height: height,
				force, Math.abs(gravity), EntityIIChemthrowerShot.getMotionDecay(), 0.01d);
		if(!Float.isFinite(angle))
			return new float[]{yaw, directPitch};
		float pitch = gravity < 0?90f-angle: angle-90f;
		return new float[]{MathHelper.wrapDegrees(yaw), pitch};
	}

	@Override
	protected boolean handleSupplyService(TileEntityEmplacement te)
	{
		return updateResupplyState(te,
				() -> platformTank.getFluidAmount() < getFluidConsumption(),
				this::canTransferFluid,
				() -> {
					if(transferFluid(Math.max(0, HeavyChemthrower.fluidTransferRate)) > 0)
						syncWithClient(te, SyncEvents.WEAPON_MISC);
				});
	}

	private boolean canTransferFluid()
	{
		FluidStack available = baseTank.drain(1, false);
		return available!=null&&platformTank.fill(available, false) > 0;
	}

	private int transferFluid(int maxAmount)
	{
		FluidStack available = baseTank.drain(maxAmount, false);
		if(available==null)
			return 0;
		int accepted = platformTank.fill(available, false);
		if(accepted <= 0)
			return 0;

		FluidStack drained = baseTank.drain(accepted, true);
		return drained==null?0: platformTank.fill(drained, true);
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return sprayCooldown <= 0&&platformTank.getFluidAmount() >= getFluidConsumption();
	}

	@Override
	protected boolean shoot(TileEntityEmplacement te, TargetCoordinateReference target)
	{
		if(te.getWorld().isRemote||!canShoot(te))
			return false;

		FluidStack fluid = platformTank.drain(getFluidConsumption(), true);
		if(fluid==null||fluid.getFluid()==null)
			return false;

		super.shoot(te, target);
		Vec3d look = aim.getTarget(0).normalize();
		boolean gas = fluid.getFluid().isGaseous(fluid)||ChemthrowerHandler.isGas(fluid.getFluid());
		float range = gas?HeavyChemthrower.rangeGas: HeavyChemthrower.rangeFluid;
		float scatter = gas?HeavyChemthrower.scatterGas: HeavyChemthrower.scatterFluid;
		Vec3d position = te.getWeaponCenter();
		for(int i = 0; i < Math.max(1, HeavyChemthrower.shotsPerDischarge); i++)
		{
			Vec3d direction = look.addVector(
					te.getWorld().rand.nextGaussian()*scatter,
					te.getWorld().rand.nextGaussian()*scatter,
					te.getWorld().rand.nextGaussian()*scatter
			);
			EntityIIChemthrowerShot shot = new EntityIIChemthrowerShot(te.getWorld(), position.x, position.y, position.z,
					direction.x*0.25, direction.y*0.25, direction.z*0.25, fluid)
					.withMotion(direction.scale(range))
					.withShooters(te.getMultiblockBlocks())
					.withShooters(te.getTactileHandler().getEntities().toArray(new Entity[0]));
			if(shouldIgnite)
				shot.setFire(10);
			te.getWorld().spawnEntity(shot);
		}
		sprayCooldown = Math.max(0, getShotDelay());

		if(te.getWorld().getTotalWorldTime()%4==0)
			te.getWorld().playSound(null, position.x, position.y, position.z,
					shouldIgnite?IESounds.sprayFire: IESounds.spray, SoundCategory.BLOCKS, 0.75f, shouldIgnite?1.25f: 0.75f);
		return true;
	}

	private int getFluidConsumption()
	{
		return Math.max(1, HeavyChemthrower.fluidConsumption);
	}

	@Nullable
	@Override
	public IFluidHandler getBaseFluidHandler()
	{
		return baseTank;
	}

	@Nullable
	@Override
	public IFluidHandler getPlatformFluidHandler()
	{
		return platformTank;
	}

	@Override
	public void onUninstall()
	{
		baseTank.setFluid(null);
		platformTank.setFluid(null);
	}

	@Override
	public int getShotDelay()
	{
		return HeavyChemthrower.sprayTime;
	}

	@Override
	public int getReloadDelay()
	{
		return 0;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return HeavyChemthrower.energyUpkeepCost;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initializeGUI(DecoPanel panelBase, DecoPanel panelPlatform)
	{
		panelBase.addComponent(new DecoFluidTank(4, 6)
				.withFluidTank(baseTank)
				.withHeight(panelBase.height-8));
		panelPlatform.addComponent(new DecoFluidTank(4, 6)
				.withFluidTank(platformTank)
				.withHeight(panelPlatform.height-8));
	}

	@Override
	public int getMaxHealth()
	{
		return HeavyChemthrower.maxHealth;
	}

	@Override
	public boolean handleDataCommand(DataPacket packet)
	{
		DataType command = packet.get('c');
		if(command instanceof DataTypeString&&"ignite".equals(((DataTypeString)command).value))
			return IIDataHandlingUtils.expectingBooleanParam('i', packet, value -> shouldIgnite = value);
		return super.handleDataCommand(packet);
	}

	@Nonnull
	@Override
	public DataType getDataCallback(String string)
	{
		return switch(string)
		{
			case "weapon_fluid" -> platformTank.getFluid()==null?
					new DataTypeFluidStack(): new DataTypeFluidStack(platformTank.getFluid());
			case "weapon_fluid_remaining" -> new DataTypeInteger(platformTank.getFluidAmount());
			case "weapon_ignited" -> new DataTypeBoolean(shouldIgnite);
			default -> super.getDataCallback(string);
		};
	}
}
