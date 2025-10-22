package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.utils.armor.IInfraredProtectionEquipment;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.InfraredObserver;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.stream.StreamSupport;

public class EmplacementWeaponInfraredObserver extends EmplacementWeapon
{
	int setupDelay = 0;
	boolean requiresPlatformRefill = false;
	private AxisAlignedBB vision;

	public EmplacementWeaponInfraredObserver()
	{

	}

	@Override
	public String getName()
	{
		return "infrared_observer";
	}

	@Override
	public float getYawTurnSpeed()
	{
		return InfraredObserver.yawRotateTime;
	}

	@Override
	public float getPitchTurnSpeed()
	{
		return InfraredObserver.pitchRotateSpeed;
	}

	@Override
	public void aimAt(float yaw, float pitch)
	{
		//Only pitch, no yaw rotation
		nextPitch = pitch;
		float p = pitch-this.pitch;
		this.pitch += Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, this.getPitchTurnSpeed());
		this.pitch = this.pitch%180;
	}

	public boolean isSetUp(boolean door)
	{
		return setupDelay==(door?InfraredObserver.setupTime: 0);
	}

	@Override
	public void doSetUp(boolean door)
	{
		if(door)
		{
			if(!isAimedAt(yaw, -90))
			{
				aimAt(yaw, -90);
				return;
			}
			if(setupDelay < InfraredObserver.setupTime)
				setupDelay += 1;
		}
		else
		{
			if(!isAimedAt(yaw, -90))
			{
				aimAt(yaw, -90);
				return;
			}
			if(setupDelay > 0)
				setupDelay -= 1;
		}
	}

	@Override
	public boolean requiresPlatformRefill()
	{
		return requiresPlatformRefill;
	}

	@Override
	public void init(TileEntityEmplacement te, boolean firstTime)
	{
		super.init(te, firstTime);
		EnumFacing facing = EnumFacing.fromAngle(MathHelper.wrapDegrees(yaw));
		Vec3i vv = facing.getDirectionVec();
		Vec3i vv2 = facing.rotateY().getDirectionVec();
		vision = new AxisAlignedBB(te.getPos()).offset(-0.5, 0, -0.5)
				.expand(vv.getX()*InfraredObserver.detectionRadius, 0, vv.getZ()*InfraredObserver.detectionRadius)
				.grow(vv2.getX()*InfraredObserver.detectionRadius, InfraredObserver.detectionRadius, vv2.getZ()*InfraredObserver.detectionRadius);
		if(firstTime)
			te.sendData = true;
	}

	@Nonnull
	@Override
	public NBTTagCompound saveToNBT(boolean forClient)
	{
		NBTTagCompound tag = super.saveToNBT(forClient);
		tag.setInteger("setupDelay", setupDelay);
		tag.setBoolean("requiresPlatformRefill", requiresPlatformRefill);
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		setupDelay = tagCompound.getInteger("setupDelay");
		requiresPlatformRefill = tagCompound.getBoolean("requiresPlatformRefill");
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return true;
	}

	@Override
	public void handleDataPacket(DataPacket packet)
	{
		super.handleDataPacket(packet);
		String c = packet.get('c').toString();
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
		}
	}

	@Override
	public AxisAlignedBB getVisionAABB()
	{
		return vision;
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
	public NonNullList<ItemStack> getBaseInventory()
	{
		return NonNullList.create();
	}

	@Override
	public void renderStorageInventory(GuiEmplacementPageStorage gui, int mx, int my, float partialTicks, boolean first)
	{

	}

	@Override
	public void performPlatformRefill(TileEntityEmplacement te)
	{
		float y = MathHelper.wrapDegrees(360+nextYaw-this.yaw);
		if(Math.abs(y) < this.getYawTurnSpeed()*0.5f)
			this.yaw = this.nextYaw;
		else
			this.yaw = MathHelper.wrapDegrees(this.yaw+(Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, this.getYawTurnSpeed())));

		if(yaw==nextYaw)
		{
			requiresPlatformRefill = false;
			syncWithClient(te);
		}

		EnumFacing facing = EnumFacing.fromAngle(MathHelper.wrapDegrees(yaw)).getOpposite();
		Vec3i vv = facing.getDirectionVec();
		Vec3i vv2 = facing.rotateY().getDirectionVec();
		vision = new AxisAlignedBB(te.getPos()).offset(-0.5, 0, -0.5)
				.expand(vv.getX()*InfraredObserver.detectionRadius, 0, vv.getZ()*InfraredObserver.detectionRadius)
				.grow(vv2.getX()*InfraredObserver.detectionRadius, InfraredObserver.detectionRadius, vv2.getZ()*InfraredObserver.detectionRadius);
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
		return StreamSupport.stream(entity.getArmorInventoryList().spliterator(), false).noneMatch(stack -> stack.getItem() instanceof IInfraredProtectionEquipment&&((IInfraredProtectionEquipment)stack.getItem()).invisibleToInfrared(stack));
	}
}
