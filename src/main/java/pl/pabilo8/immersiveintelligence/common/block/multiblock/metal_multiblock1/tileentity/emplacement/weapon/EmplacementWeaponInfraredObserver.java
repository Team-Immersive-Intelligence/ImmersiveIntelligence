package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.protection.protection.ProtectionHandler;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.InfraredObserver;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAimCoordinate;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nonnull;

/**
 * Implements the fixed-yaw Infrared Observer with safe stow and setup behavior.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 17.08.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponInfraredObserver extends EmplacementWeapon
{
	@Nonnull
	@SyncNBT(events = SyncEvents.WEAPON_MISC)
	private EnumFacing facing, plannedFacing;
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_ROTATION)
	public GunAimCoordinate aim = new GunAimCoordinate();
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC)
	public MultiblockInteractablePart setup;

	public EmplacementWeaponInfraredObserver()
	{
		this.facing = this.plannedFacing = EnumFacing.NORTH;
		this.setup = new MultiblockInteractablePart(InfraredObserver.setupTime);
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		if(!restoredFromNBT)
			this.facing = this.plannedFacing = te.facing;
		configureFacing(te, !restoredFromNBT);
	}

	private void configureFacing(TileEntityEmplacement te, boolean resetAngles)
	{
		Vec3i viewFront = this.facing.getDirectionVec();
		Vec3i viewSides = this.facing.rotateY().getDirectionVec();
		this.attackAABB = this.visionAABB = new net.minecraft.util.math.AxisAlignedBB(new net.minecraft.util.math.BlockPos(te.getWeaponCenter()))
				.expand(viewFront.getX()*InfraredObserver.detectionRadius, 0, viewFront.getZ()*InfraredObserver.detectionRadius)
				.grow(Math.abs(viewSides.getX())*InfraredObserver.detectionRadius, InfraredObserver.detectionRadius,
						Math.abs(viewSides.getZ())*InfraredObserver.detectionRadius);
		this.aim.withCenterYaw(facing.getHorizontalAngle())
				.withAimSpeed(360, InfraredObserver.pitchRotateSpeed)
				.withYawLimit(0, 0);
		if(resetAngles)
			this.aim.withCurrentAngles(this.aim.getCenterYaw(), this.aim.clampPitchToRange(90f));
	}

	@Override
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te, EmplacementStateNeeds baseNeeds, TargetCoordinateReference currentTarget)
	{
		if(plannedFacing!=facing)
			return EmplacementStateNeeds.MUST_HIDE;
		if(te.door.getState()&&te.door.isFullyOpened())
			this.aim.update();
		return super.onUpdate(te, baseNeeds, currentTarget);
	}

	@Override
	public void onPlatformUpdate(TileEntityEmplacement te)
	{
		boolean remote = te.getWorld().isRemote;
		boolean exposed = te.door.getState()&&te.door.isFullyOpened();
		boolean setupChanged = false;
		if(!remote)
			setupChanged = setup.setState(exposed);
		setup.update();

		if(!exposed)
		{
			float previousYaw = aim.getYaw(0);
			float previousPitch = aim.getPitch(0);
			if(!remote)
				aim.setTargetClamped(aim.getCenterYaw(), aim.clampPitchToRange(90f));
			aim.update();
			if(!remote&&(Math.abs(MathHelper.wrapDegrees(aim.getYaw(0)-previousYaw)) > 0.001f
					||Math.abs(aim.getPitch(0)-previousPitch) > 0.001f))
				syncWithClient(te, SyncEvents.WEAPON_ROTATION);
		}

		if(!remote&&!te.door.getState()&&te.door.isFullyClosed()&&plannedFacing!=facing)
		{
			facing = plannedFacing;
			configureFacing(te, true);
			syncWithClient(te, SyncEvents.WEAPON_MISC);
			syncWithClient(te, SyncEvents.WEAPON_ROTATION);
		}
		else if(!remote&&setupChanged)
			syncWithClient(te, SyncEvents.WEAPON_MISC);
	}

	@Override
	public String getName()
	{
		return "infrared_observer";
	}

	@Override
	public boolean handleDataCommand(DataPacket packet)
	{
		return super.handleDataCommand(packet);
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return InfraredObserver.energyUpkeepCost;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initializeGUI(DecoPanel panelBase, DecoPanel panelPlatform)
	{

	}

	@Override
	public int getMaxHealth()
	{
		return InfraredObserver.maxHealth;
	}

	@Override
	public boolean canSeeEntity(Entity entity)
	{
		return !(entity instanceof EntityLivingBase)
				||!ProtectionHandler.isInvisibleToInfrared((EntityLivingBase)entity);
	}
}
