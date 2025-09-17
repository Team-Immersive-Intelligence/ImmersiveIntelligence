package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.api.tool.IElectricEquipment.ElectricSource;
import blusunrize.immersiveengineering.common.Config.IEConfig;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityTeslaCoil.LightningAnimation;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.IEDamageSources.ElectricDamageSource;
import blusunrize.immersiveengineering.common.util.IEPotions;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageStorage;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.TeslaCoil;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon.EmplacementHitboxEntity;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class EmplacementWeaponTeslaCoil extends EmplacementWeapon
{
	private final ArrayList<Integer> targetedEntities = new ArrayList<>();
	private final ArrayList<LightningAnimation> effects = new ArrayList<>();
	private AxisAlignedBB vision;
	private AxisAlignedBB attack;

	public EmplacementWeaponTeslaCoil()
	{

	}

	@Override
	public String getName()
	{
		return "tesla";
	}

	@Override
	public float getYawTurnSpeed()
	{
		return 360;
	}

	@Override
	public float getPitchTurnSpeed()
	{
		return 360;
	}

	@Override
	public boolean isAimedAt(float yaw, float pitch)
	{
		return true;
	}

	@Override
	public float[] getAnglePrediction(Vec3d posTurret, Vec3d posTarget, Vec3d motion)
	{
		return new float[]{0, 0};
	}

	@Override
	public void init(TileEntityEmplacement te, boolean firstTime)
	{
		super.init(te, firstTime);
		//vision = new AxisAlignedBB(te.getPos()).offset(-0.5, 0, -0.5).grow(TeslaCoil.detectionRadius);
		vision = new AxisAlignedBB(te.getPos()).offset(-0.5, 0, -0.5).grow(16);
		attack = new AxisAlignedBB(te.getPos()).offset(-0.5, 0, -0.5).grow(TeslaCoil.attackRadius);
	}

	@Override
	public void tick(TileEntityEmplacement te, boolean active)
	{
		if(!active)
			return;

		for(Integer targetedEntity : targetedEntities)
		{
			addEntityToAnimation(targetedEntity, te.getWorld(), te.getBlockPosForPos(49).up());
		}
		targetedEntities.clear();
		effects.removeIf(LightningAnimation::tick);
	}

	@Override
	public void shoot(TileEntityEmplacement te)
	{
		super.shoot(te);
		if(te.getWorld().getTotalWorldTime()%10==0)
		{
			List<Entity> targets = te.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, this.attack, input -> input!=entity);
			EntityLivingBase target = null;
			if(!targets.isEmpty())
			{
				ElectricDamageSource dmgsrc = IEDamageSources.causeTeslaDamage(IEConfig.Machines.teslacoil_damage*2.5f, false);
				int randomTarget = Utils.RAND.nextInt(targets.size());
				target = (EntityLivingBase)targets.get(randomTarget);
				if(target!=null)
				{
					// TODO: 26.08.2021 energy usage
					//energyDrain = IEConfig.Machines.teslacoil_consumption_active;
					//if(energyStorage.extractEnergy(energyDrain, true)==energyDrain)
					//						{
					//energyStorage.extractEnergy(energyDrain, false);
					if(dmgsrc.apply(target))
					{
						int prevFire = target.fire;
						target.fire = 1;
						target.addPotionEffect(new PotionEffect(IEPotions.stunned, 128));
						target.fire = prevFire;
					}
					this.syncAttackedEntity(te, target);
				}
			}

			for(Entity e : targets)
				if(e!=target)
				{
					if(e instanceof EntityLivingBase)
						IElectricEquipment.applyToEntity((EntityLivingBase)e, null, new ElectricSource(3f));
				}
		}

	}

	private void addAnimation(LightningAnimation ani)
	{
		Minecraft.getMinecraft().addScheduledTask(() -> effects.add(ani));
	}

	@Override
	public void aimAt(float yaw, float pitch)
	{
		super.aimAt(yaw, pitch);

	}

	@Override
	public boolean isSetUp(boolean door)
	{
		return true;
	}

	@Override
	public boolean requiresPlatformRefill()
	{
		return false;
	}

	@Nonnull
	@Override
	public NBTTagCompound saveToNBT(boolean forClient)
	{
		return super.saveToNBT(forClient);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		if(tagCompound.hasKey("targetEntity"))
		{
			targetedEntities.add(tagCompound.getInteger("targetEntity"));
		}
		else
			super.readFromNBT(tagCompound);
	}

	@Override
	public void syncWithEntity(EntityEmplacementWeapon entity)
	{
		super.syncWithEntity(entity);
		if(entity==this.entity)
			entity.aabb = new AxisAlignedBB(-1, 0, -1, 1, 3.75, 1);
	}

	private void addEntityToAnimation(int id, World world, BlockPos pos)
	{
		Entity target = world.getEntityByID(id);
		if(target instanceof EntityLivingBase)
		{
			double dx = target.posX-pos.getX();
			double dy = target.posY-pos.getY();
			double dz = target.posZ-pos.getZ();

			EnumFacing f;
			if(Math.abs(dz) > Math.abs(dx))
				f = dz < 0?EnumFacing.NORTH: EnumFacing.SOUTH;
			else
				f = dx < 0?EnumFacing.WEST: EnumFacing.EAST;

			double verticalOffset = 1+Utils.RAND.nextDouble()*.25;
			Vec3d coilPos = new Vec3d(pos).addVector(.5, .5, .5);
			//Vertical offset
			coilPos = coilPos.addVector(0, verticalOffset, 0);
			//offset to direction
			coilPos = coilPos.addVector(f.getFrontOffsetX()*.375, f.getFrontOffsetY()*.375, f.getFrontOffsetZ()*.375);
			//random side offset
			f = f.rotateAround(Axis.Y);
			double dShift = (Utils.RAND.nextDouble()-.5)*.75;
			coilPos = coilPos.addVector(f.getFrontOffsetX()*dShift, f.getFrontOffsetY()*dShift, f.getFrontOffsetZ()*dShift);

			addAnimation(new LightningAnimation(coilPos, (EntityLivingBase)target));
		}
	}

	public void syncAttackedEntity(TileEntityEmplacement te, Entity e)
	{
		if(!te.getWorld().isRemote)
		{
			IIPacketHandler.sendToClient(te, new MessageIITileSync(te, EasyNBT.newNBT()
					.withString("weaponName", getName())
					.withTag("currentWeapon", EasyNBT.newNBT()
							.withInt("targetEntity", e.getEntityId())
					)
			));
		}
	}

	@Override
	public boolean canShoot(TileEntityEmplacement te)
	{
		return true;
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

		ArrayList<EmplacementHitboxEntity> list = new ArrayList<>();
		list.add(new EmplacementHitboxEntity(entity, "baseBox", 1f, 1f,
				new Vec3d(0, 0.5, 0), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "rod", 0.375f, 1.5f,
				new Vec3d(0, 2, 0), Vec3d.ZERO, 12));

		list.add(new EmplacementHitboxEntity(entity, "rodTop", 0.75f, 0.75f,
				new Vec3d(0, 3, 0), Vec3d.ZERO, 20));

		list.add(new EmplacementHitboxEntity(entity, "ring1", 0.75f, 0.1875f,
				new Vec3d(0, 2.55f, 0), Vec3d.ZERO, 12));

		list.add(new EmplacementHitboxEntity(entity, "ring2", 1f, 0.1875f,
				new Vec3d(0, 2.15f, 0), Vec3d.ZERO, 12));

		list.add(new EmplacementHitboxEntity(entity, "ring3", 1.0625f, 0.1875f,
				new Vec3d(0, 1.8f, 0), Vec3d.ZERO, 12));

		list.add(new EmplacementHitboxEntity(entity, "ring4", 1.1875f, 0.1875f,
				new Vec3d(0, 1.5f, 0), Vec3d.ZERO, 12));

		list.add(new EmplacementHitboxEntity(entity, "sideBox1", 0.5f, 0.5f,
				new Vec3d(0.75, 0.5f, 0), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "sideBox2", 0.5f, 0.5f,
				new Vec3d(-0.75, 0.5f, 0), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "sideBox3", 0.5f, 0.5f,
				new Vec3d(0, 0.5f, 0.75), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "sideBox4", 0.5f, 0.5f,
				new Vec3d(0, 0.5f, -0.75), Vec3d.ZERO, 4));


		list.add(new EmplacementHitboxEntity(entity, "pipe1", 0.5f, 0.35f,
				new Vec3d(0.6, 0.35f, 0.5), Vec3d.ZERO, 4));

		list.add(new EmplacementHitboxEntity(entity, "pipe2", 0.5f, 0.35f,
				new Vec3d(0.6, 0.35f, -0.5), Vec3d.ZERO, 4));

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

	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return TeslaCoil.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return TeslaCoil.maxHealth;
	}

}
