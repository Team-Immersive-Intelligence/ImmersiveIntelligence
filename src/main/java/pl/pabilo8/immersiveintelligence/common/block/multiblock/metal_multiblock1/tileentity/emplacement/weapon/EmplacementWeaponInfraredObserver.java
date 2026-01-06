package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.utils.armor.IInfraredProtectionEquipment;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.InfraredObserver;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.stream.StreamSupport;

public class EmplacementWeaponInfraredObserver extends EmplacementWeapon
{
	private int setupDelay = 0;
	@Nonnull
	private EnumFacing facing = EnumFacing.NORTH, plannedFacing = EnumFacing.NORTH;
	private float pitch, nextPitch;

	public EmplacementWeaponInfraredObserver()
	{

	}

	@Override
	public void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.facing = this.plannedFacing = te.facing;

		Vec3i viewFront = this.facing.getDirectionVec();
		Vec3i viewSides = this.facing.rotateY().getDirectionVec();
		this.attackAABB = this.visionAABB = this.visionAABB
				.expand(viewFront.getX()*InfraredObserver.detectionRadius, 0, viewFront.getZ()*InfraredObserver.detectionRadius)
				.grow(viewSides.getX()*InfraredObserver.detectionRadius, InfraredObserver.detectionRadius, viewSides.getZ()*InfraredObserver.detectionRadius);
		te.sendData = true;
	}

	@Override
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te)
	{
		return super.onUpdate(te);
	}

	@Override
	public String getName()
	{
		return "infrared_observer";
	}

	public void aimAt(float yaw, float pitch)
	{
		//Only pitch, no yaw rotation
		nextPitch = pitch;
		float p = pitch-this.pitch;
		this.pitch += Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, InfraredObserver.pitchRotateSpeed);
		this.pitch = this.pitch%180;
	}

	@Override
	public void handleDataPacket(DataPacket packet)
	{
		super.handleDataPacket(packet);
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
	}

	@Override
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
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return InfraredObserver.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return InfraredObserver.maxHealth;
	}

	@Override
	public boolean canSeeEntity(Entity entity)
	{
		return StreamSupport.stream(entity.getArmorInventoryList().spliterator(), false)
				.noneMatch(stack -> stack.getItem() instanceof IInfraredProtectionEquipment&&((IInfraredProtectionEquipment)stack.getItem()).invisibleToInfrared(stack));
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
