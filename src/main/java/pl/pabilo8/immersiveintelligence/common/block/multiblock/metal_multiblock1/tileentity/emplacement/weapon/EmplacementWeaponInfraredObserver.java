package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.protection.protection.ProtectionHandler;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.InfraredObserver;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAimCoordinate;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * Implements the fixed-yaw Infrared Observer with safe stow and setup behavior.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 27.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponInfraredObserver extends EmplacementWeapon
{
	private static final ResLoc ROTATE_YAW = ResLoc.of(IIReference.RES_II,
			"emplacement/weapon/infrared_observer/rotate_yaw");
	private static final ResLoc ROTATE_PITCH = ResLoc.of(IIReference.RES_II,
			"emplacement/weapon/infrared_observer/rotate_pitch");
	@Nonnull
	@SyncNBT(events = SyncEvents.WEAPON_MISC)
	public EnumFacing facing, plannedFacing;
	@SyncNBT(time = 0, events = {SyncEvents.WEAPON_ROTATION, SyncEvents.WEAPON_MISC})
	public GunAimCoordinate aim = new GunAimCoordinate();

	public EmplacementWeaponInfraredObserver()
	{
		this.facing = this.plannedFacing = EnumFacing.NORTH;
		this.setup = new MultiblockInteractablePart(240);
		this.aim.withPitchLimit(InfraredObserver.minPitch, InfraredObserver.maxPitch);
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
				.withAimSpeed(InfraredObserver.yawRotateSpeed, InfraredObserver.pitchRotateSpeed)
				.withYawLimit(InfraredObserver.minYaw, InfraredObserver.maxYaw)
				.withPitchLimit(InfraredObserver.minPitch, InfraredObserver.maxPitch);
		if(resetAngles)
			this.aim.withCurrentAngles(this.aim.getCenterYaw(), InfraredObserver.hidingPitch);
	}

	@Override
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te, EmplacementStateNeeds baseNeeds, TargetCoordinateReference currentTarget)
	{
		if(plannedFacing!=facing)
			return EmplacementStateNeeds.MUST_HIDE;
		return super.onUpdate(te, baseNeeds, currentTarget);
	}

	@Override
	public void onPlatformUpdate(TileEntityEmplacement te)
	{
		boolean exposed = te.door.getState()&&te.door.isFullyOpened();
		boolean setupChanged = setup.setState(exposed);
		setup.update();

		float previousTargetYaw = aim.getTargetYaw();
		float previousTargetPitch = aim.getTargetPitch();
		boolean wasRotating = !aim.isAimed(0.001f);
		this.aim.withCenterYaw(facing.getHorizontalAngle());
		//Keep the lens stowed during setup, then publish its operating target immediately.
		aim.setTargetClamped(aim.getCenterYaw(), exposed&&setup.isFullyOpened()?
				InfraredObserver.operatingPitch: InfraredObserver.hidingPitch);
		aim.update();
		if(previousTargetYaw!=aim.getTargetYaw()||previousTargetPitch!=aim.getTargetPitch()
				||wasRotating||!aim.isAimed(0.001f))
			syncWithClient(te, SyncEvents.WEAPON_ROTATION);

		if(!te.door.getState()&&te.door.isFullyClosed()&&plannedFacing!=facing)
		{
			facing = plannedFacing;
			configureFacing(te, true);
			syncWithClient(te, SyncEvents.WEAPON_MISC);
			syncWithClient(te, SyncEvents.WEAPON_ROTATION);
		}
		else if(setupChanged)
			syncWithClient(te, SyncEvents.WEAPON_MISC);
	}

	@Override
	public void applyTactileAnimations(TileEntityEmplacement te)
	{
		applyTactileAnimationSet(te,
				new ResLoc[]{ROTATE_YAW, ROTATE_PITCH},
				new float[]{(aim.getYawNormalized(0)+0.5f)%1f, aim.getPitchNormalized(-90, 90, 0)});
	}

	@Override
	public void onClientUpdate(TileEntityEmplacement te)
	{
		setup.update();
		aim.update();
		super.onClientUpdate(te);
	}

	@Override
	public String getName()
	{
		return "infrared_observer";
	}

	@Override
	public boolean handleDataCommand(DataPacket packet)
	{
		DataType command = packet.get('c');
		if(!(command instanceof DataTypeString)
				||!"facing".equals(((DataTypeString)command).value.trim().toLowerCase(Locale.ROOT)))
			return super.handleDataCommand(packet);

		EnumFacing requested = parseFacing(packet.get('f'));
		if(requested==null||requested.getAxis()==EnumFacing.Axis.Y)
			return false;
		plannedFacing = requested;
		return true;
	}

	@Nonnull
	@Override
	public DataType getDataCallback(String string)
	{
		return switch(string)
		{
			case "weapon_yaw" -> new DataTypeFloat(aim.getYaw(0));
			case "weapon_pitch" -> new DataTypeFloat(aim.getPitch(0));
			case "weapon_target_yaw" -> new DataTypeFloat(aim.getTargetYaw());
			case "weapon_target_pitch" -> new DataTypeFloat(aim.getTargetPitch());
			case "weapon_setup", "weapon_setup_progress" -> new DataTypeFloat(setup.getProgress(0));
			case "weapon_facing" -> new DataTypeString(facing.getName());
			default -> super.getDataCallback(string);
		};
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
	public int getArmorForPart(String partName)
	{
		return switch(partName)
		{
			case "base_child11", "turret_child3", "turret_child4" -> 1;
			case "turret_child1", "turret_child2" -> 16;
			case "base_child0", "base_child1" -> 12;
			default -> 8;
		};
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
