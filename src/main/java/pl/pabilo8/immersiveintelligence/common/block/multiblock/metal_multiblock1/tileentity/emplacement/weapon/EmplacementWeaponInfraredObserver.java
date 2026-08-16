package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.api.protection.ProtectionHandler;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.InfraredObserver;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAimCoordinate;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nonnull;

public class EmplacementWeaponInfraredObserver extends EmplacementWeapon
{
	@Nonnull
	@SyncNBT
	private EnumFacing facing, plannedFacing;
	@SyncNBT
	public GunAimCoordinate aim = new GunAimCoordinate();
	@SyncNBT
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

		Vec3i viewFront = this.facing.getDirectionVec();
		Vec3i viewSides = this.facing.rotateY().getDirectionVec();
		this.attackAABB = this.visionAABB = this.visionAABB
				.expand(viewFront.getX()*InfraredObserver.detectionRadius, 0, viewFront.getZ()*InfraredObserver.detectionRadius)
				.grow(viewSides.getX()*InfraredObserver.detectionRadius, InfraredObserver.detectionRadius, viewSides.getZ()*InfraredObserver.detectionRadius);

		this.aim.withAimSpeed(360, InfraredObserver.pitchRotateSpeed)
				.withYawLimit(facing.getHorizontalAngle(), facing.getHorizontalAngle());
	}

	@Override
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te, EmplacementStateNeeds baseNeeds, TargetCoordinateReference currentTarget)
	{
		if(plannedFacing!=facing)
			return EmplacementStateNeeds.MUST_HIDE;

		return super.onUpdate(te, baseNeeds, currentTarget);
	}

	@Override
	public String getName()
	{
		return "infrared_observer";
	}

	@Override
	public boolean handleDataCommand(DataPacket packet)
	{
		/*String c = packet.get('c').toString();
		if(c.equals("facing"))
		{
			DataType f = packet.get('f');
			if(f instanceof DataTypeInteger)
				nextYaw = EnumFacing.getHorizontal(((DataTypeInteger)f).value).getHorizontalAngle();
			else if(f instanceof DataTypeString)
			{
				EnumFacing facing = EnumFacing.byName(f.toString());
				if(facing==EnumFacing.NORTH||facing==EnumFacing.SOUTH)
					facing = facing.getOpposite();
				if(facing!=null)
					nextYaw = facing.getHorizontalAngle();
			}

			if(nextYaw!=yaw)
				requiresPlatformRefill = true;
		}*/
		return super.handleDataCommand(packet);
	}

	/*@Override
	public EmplacementHitboxEntity[] getCollisionBoxes()
	{
		if(entity==null)
			return new EmplacementHitboxEntity[0];

		//new Vec3d(0,0,0)
		ArrayList<EmplacementHitboxEntity> list = new ArrayList<>();
		list.add(new EmplacementHitboxEntity(entity, "baseBox", 1f, 0.25f,
				new Vec3d(0, 0.25, 0), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "backBox", 0.5f, 0.75f,
				new Vec3d(0.75, 0.75, 0), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "observeBox", 1.25f, 1.25f,
				new Vec3d(-0.25, 1.5, 0), Vec3d.ZERO, 4));
		list.add(new EmplacementHitboxEntity(entity, "observeBox", 0.5f, 0.5f,
				new Vec3d(-0.25, 1.5, 0), new Vec3d(-1, 0, -0.25), 4));
		list.add(new EmplacementHitboxEntity(entity, "observeBox", 0.25f, 0.25f,
				new Vec3d(-0.25, 1.5, 0), new Vec3d(-1.325, 0, -0.25), 4));

		return list.toArray(new EmplacementHitboxEntity[0]);
	}*/

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

	@Override
	public NBTTagCompound serializeNBT()
	{
		return super.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
	}
}
