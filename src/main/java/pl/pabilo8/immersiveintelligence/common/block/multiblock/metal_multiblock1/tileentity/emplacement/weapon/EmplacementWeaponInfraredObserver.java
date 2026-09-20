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
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;
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
 * @updated 08.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponInfraredObserver extends EmplacementWeapon
{
	private static final ResLoc ROTATE_PITCH = ResLoc.of(IIReference.RES_II,
			"emplacement/weapon/infrared_observer/rotate_pitch");
	@Nonnull
	@SyncNBT(events = SyncEvents.WEAPON_MISC)
	public EnumFacing facing, plannedFacing;
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_ROTATION)
	public GunAimCoordinate aim = new GunAimCoordinate();
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC)
	public MultiblockInteractablePart setup;

	public EmplacementWeaponInfraredObserver()
	{
		this.facing = this.plannedFacing = EnumFacing.NORTH;
		this.setup = new MultiblockInteractablePart(InfraredObserver.setupTime);
		this.aim.withPitchLimit(-90, 45.5f);
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
				.withYawLimit(-180f, 180f);
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
			if(!remote)
				aim.setTargetClamped(aim.getCenterYaw(), aim.clampPitchToRange(-90f));
			aim.update();
			if(!remote&&!aim.isAimed(0.001f))
				syncWithClient(te, SyncEvents.WEAPON_ROTATION);
		}
		else if(setup.isFullyOpened()&&!remote)
		{
			aim.setTargetClamped(aim.getCenterYaw(), aim.clampPitchToRange(0f));
			if(aim.isAimed(0.001f))
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

		if(!remote&&te.tactileHandler!=null)
			te.tactileHandler.update(ROTATE_PITCH, aim.getPitchNormalized(-90, 90, 0));
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

	private EnumFacing parseFacing(DataType input)
	{
		if(input instanceof NumericDataType)
		{
			NumericDataType numeric = (NumericDataType)input;
			int ordinal = numeric.intValue();
			return numeric.floatValue()==ordinal?facingFromOrdinal(ordinal): null;
		}
		if(!(input instanceof DataTypeString))
			return null;

		String value = ((DataTypeString)input).value.trim();
		try
		{
			return EnumFacing.valueOf(value.toUpperCase(Locale.ROOT));
		} catch(IllegalArgumentException ignored)
		{
			try
			{
				return facingFromOrdinal(Integer.parseInt(value));
			} catch(NumberFormatException ignoredNumber)
			{
				return null;
			}
		}
	}

	private EnumFacing facingFromOrdinal(int ordinal)
	{
		EnumFacing[] values = EnumFacing.values();
		return ordinal >= 0&&ordinal < values.length?values[ordinal]: null;
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
