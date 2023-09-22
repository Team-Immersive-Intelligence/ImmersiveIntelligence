package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect;
import blusunrize.immersiveengineering.common.util.IEFluid;
import com.google.common.base.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.utils.armor.IGasmask;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;

import java.util.List;

/**
 * @author Pabilo8
 * @since 22.12.2020
 */
public class EntityGasCloud extends Entity
{
	private FluidStack fluid;
	private static final DataParameter<Optional<FluidStack>> dataMarker_fluid = EntityDataManager.createKey(EntityGasCloud.class, IEFluid.OPTIONAL_FLUID_STACK);
	private static final DataParameter<Float> dataMarker_radius = EntityDataManager.createKey(EntityGasCloud.class, DataSerializers.FLOAT);

	int duration, maxDuration;
	float radius;

	public EntityGasCloud(World worldIn)
	{
		super(worldIn);
		this.duration = 600;
		this.maxDuration = duration;
		this.noClip = true;
		this.isImmuneToFire = true;
		this.radius = 3F;
	}

	public EntityGasCloud(World worldIn, double x, double y, double z, FluidStack fs)
	{
		this(worldIn);
		this.setPosition(x, y, z);
		this.fluid = fs;
		duration = (int)((fluid.amount)*0.65f);
		this.maxDuration = duration;
		this.dataManager.set(dataMarker_fluid, Optional.of(fluid));
		this.dataManager.set(dataMarker_radius, radius);
	}

	@Override
	protected void entityInit()
	{
		this.dataManager.register(dataMarker_fluid, Optional.absent());
		this.dataManager.register(dataMarker_radius, radius);
	}

	@Override
	public void onUpdate()
	{
		move(MoverType.SELF, 0.01f, 0, 0.01f);
		super.onUpdate();
		if(this.fluid==null&&this.world.isRemote)
		{
			this.fluid = this.dataManager.get(dataMarker_fluid).orNull();
			this.radius = this.dataManager.get(dataMarker_radius);
		}

		if(!world.isRemote)
		{
			if(duration > 0&&fluid!=null)
			{
				duration -= 1;
				ChemthrowerEffect effect = ChemthrowerHandler.getEffect(fluid.getFluid());
				if(effect!=null)
				{
					List<EntityLivingBase> entitiesWithinAABB = world.getEntitiesWithinAABB(EntityLivingBase.class,
							new AxisAlignedBB(posX, posY, posZ, posX, posY, posZ).grow(radius));
					for(EntityLivingBase entityLivingBase : entitiesWithinAABB)
					{
						boolean apply = true;
						for(EntityEquipmentSlot slot : EntityEquipmentSlot.values())
						{
							ItemStack s = entityLivingBase.getItemStackFromSlot(slot);
							if(!s.isEmpty()&&s.getItem() instanceof IGasmask)
								if(apply)
									apply = !((IGasmask)s.getItem()).protectsFromGasses(s);
						}
						if(apply)
							effect.applyToEntity(entityLivingBase, null, ItemStack.EMPTY, fluid);
					}
				}

			}
			else
				setDead();
		}
		else if(ticksExisted%20==0)
			ParticleUtils.spawnGasCloud(getPositionVector().addVector(0, 1, 0), radius, fluid.getFluid());
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{

	}

	@Override
	public void setFire(int seconds)
	{
		if(ChemthrowerHandler.isFlammable(fluid.getFluid()))
		{
			duration -= seconds*10;
			super.setFire(seconds);
		}
	}
}
