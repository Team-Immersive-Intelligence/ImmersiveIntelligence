package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityTeslaCoil.LightningAnimation;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel.DecoPanel;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage.DecoBar;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTemplates;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.TeslaCoil;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;

import java.util.ArrayList;

public class EmplacementWeaponTeslaCoil extends EmplacementWeapon
{
	private final FluxStorageAdvanced energy;
	private final ArrayList<Integer> targetedEntities = new ArrayList<>();
	private final ArrayList<LightningAnimation> effects = new ArrayList<>();

	public EmplacementWeaponTeslaCoil()
	{
		this.energy = new FluxStorageAdvanced(TeslaCoil.energyStorage);
	}

	@Override
	public String getName()
	{
		return "tesla";
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.visionAABB = this.visionAABB.grow(TeslaCoil.detectionRadius);
		this.attackAABB = this.attackAABB.grow(TeslaCoil.attackRadius);
	}

	@Override
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te, EmplacementStateNeeds baseNeeds, TargetCoordinateReference currentTarget)
	{
		for(Integer targetedEntity : targetedEntities)
			addEntityToAnimation(targetedEntity, te.getWorld(), new BlockPos(te.getWeaponCenter()));
		targetedEntities.clear();
		effects.removeIf(LightningAnimation::tick);

		return super.onUpdate(te, baseNeeds, currentTarget);
	}

	/*@Override
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
					if(e instanceof EntityLivingBase)
						IElectricEquipment.applyToEntity((EntityLivingBase)e, null, new ElectricSource(3f));
		}

	}*/

	private void addAnimation(LightningAnimation ani)
	{
		Minecraft.getMinecraft().addScheduledTask(() -> effects.add(ani));
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

	/*@Override
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
	}*/

	@Override
	public int getEnergyUpkeepCost()
	{
		return TeslaCoil.energyUpkeepCost;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void initializeGUI(DecoPanel panelBase, DecoPanel panelPlatform)
	{
		panelPlatform.addComponent(
				new DecoBar(4, 4+2)
						.withTemplate(DecoTemplates.BAR_ELECTRIC_ENERGY.apply(energy))
						.withHeight(panelPlatform.height-8)
		);
	}

	@Override
	public int getMaxHealth()
	{
		return TeslaCoil.maxHealth;
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
		if(nbt.hasKey("targetEntity"))
			targetedEntities.add(nbt.getInteger("targetEntity"));
	}
}
