package pl.pabilo8.immersiveintelligence.common.entity.ammo.component;

import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.utils.armor.IGasmask;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Gas cloud entity that expands over time.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 26.05.2026
 * @ii-approved 0.3.1
 * @since 22.12.2020
 *
 */
public class EntityGasCloud extends Entity implements ISyncNBTEntity<EntityGasCloud>
{
	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1})
	public FluidStack fluidStack = null;
	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1})
	public float currentRadius = 1.0f;
	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1})
	public float maxRadius;
	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1})
	public int duration = 40;

	public EntityGasCloud(World world)
	{
		super(world);
		setSize(1.0f, 1.0f);
		this.noClip = true;
		this.isImmuneToFire = true;
	}

	/**
	 * Creates a new gas cloud with given position, fluid, and total amount (in mb).
	 * Max radius is derived from the fluid's expansion properties and total amount.
	 */
	public EntityGasCloud(World world, double x, double y, double z, @Nonnull FluidStack fluid)
	{
		this(world);
		this.setPosition(x, y, z);
		this.fluidStack = fluid.copy();
		this.maxRadius = calculateMaxRadiusFromAmount();
		this.currentRadius = 0.5f;
		this.duration = this.fluidStack.amount*2;
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	public void onUpdate()
	{
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;

		if(world.isRemote)
		{
			if(fluidStack!=null&&duration > 0)
			{
				//Particle effect every second
				if(ticksExisted%20==0)
					ParticleRegistry.spawnGasCloud(getPositionVector().addVector(0, 1, 0), currentRadius, fluidStack.getFluid());

				//Expand radius locally using the same growth rate, up to maxRadius
				if(currentRadius < maxRadius)
				{
					float newRadius = currentRadius+getGasGrowthRate();
					currentRadius = Math.min(newRadius, maxRadius);
					updateBoundingBox();
				}

				//Decrease lifetime
				duration--;
			}
			return;
		}

		//Remove the entity when out of time
		if(fluidStack==null||duration <= 0)
		{
			setDead();
			return;
		}

		duration--;

		//Apply effect to entities within the bounding box
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox());
		ChemthrowerEffect effect = ChemthrowerHandler.getEffect(fluidStack.getFluid());
		if(effect!=null)
			for(EntityLivingBase entity : entities)
			{
				boolean isProtected = false;
				for(EntityEquipmentSlot slot : EntityEquipmentSlot.values())
				{
					ItemStack stack = entity.getItemStackFromSlot(slot);
					if(!stack.isEmpty()&&stack.getItem() instanceof IGasmask)
						if(((IGasmask)stack.getItem()).protectsFromGasses(stack))
							isProtected = true;
				}
				if(!isProtected)
					effect.applyToEntity(entity, null, ItemStack.EMPTY, fluidStack);
			}

		//Expansion logic
		if(currentRadius < maxRadius)
		{
			float oldRadius = currentRadius;
			float newRadius = currentRadius+getGasGrowthRate();
			if(canExpandToRadius(newRadius))
			{
				currentRadius = Math.min(newRadius, maxRadius);
				updateBoundingBox();
			}
			else
				attemptSpawnChildren();
			if(oldRadius!=currentRadius)
				sendServerUpdateForEvent(SyncEvents.ENTITY_CUSTOM1);
		}

		//Lifetime end
		if(duration <= 0&&currentRadius <= 0.1f)
			setDead();

		//Periodic full sync (corrects client drift)
		if(ticksExisted%10==0)
			updateEntityForEvent(SyncEvents.ENTITY_CUSTOM1);

		//Try to move down
		if(!this.world.collidesWithAnyBlock(getEntityBoundingBox().expand(0, -0.005f, 0)))
			this.posY -= 0.005f;
	}

	//--- Utils ---//

	private float calculateMaxRadiusFromAmount()
	{
		float baseRadius = (float)Math.cbrt(this.fluidStack.amount/200.0);
		float fluidFactor = getGasExpansionFactor();
		return baseRadius*fluidFactor;
	}

	private float getSpreadTreshold()
	{
		return 0.2f;
	}

	private float getGasExpansionFactor()
	{
		return 1.25f;
	}

	private float getGasGrowthRate()
	{
		return 0.01f;
	}

	/**
	 * Checks whether the cloud can expand to the given radius without intersecting solid blocks.
	 * Uses a sphere-like expansion: checks blocks within a cube of side 2*radius around center.
	 */
	private boolean canExpandToRadius(float radius)
	{
		BlockPos center = getPosition().up();
		int r = (int)Math.ceil(radius);
		for(int x = -r; x <= r; x++)
			for(int y = 0; y <= 2*r; y++)
				for(int z = -r; z <= r; z++)
				{
					double distSq = x*x+y*y+z*z;
					if(distSq <= radius*radius)
					{
						BlockPos pos = center.add(x, y, z);
						IBlockState state = world.getBlockState(pos);
						if(!state.getBlock().isAir(state, world, pos)&&state.isFullBlock())
							return false;
					}
				}
		return true;
	}

	/**
	 * Attempts to spawn child clouds in directions where expansion is possible.
	 * Each child inherits a portion of totalGasAmount, and the parent reduces its own amount.
	 */
	private void attemptSpawnChildren()
	{
		final int directions = 6;
		EnumFacing[] faces = EnumFacing.values();
		float freeSpaceRatio = 0.0f;

		//Check how many directions are free for expansion
		for(EnumFacing face : faces)
			if(isDirectionFree(face))
				freeSpaceRatio += 1.0f/directions;

		if(freeSpaceRatio > getSpreadTreshold())
		{
			//Transfer a fraction of gas to children
			int amountPerChild = fluidStack.amount/(directions*2);
			if(amountPerChild < 10)
				return; //too little to expand

			for(EnumFacing face : faces)
				if(isDirectionFree(face))
				{
					//Calculate child position slightly offset in the direction of the face
					double childX = posX+face.getFrontOffsetX()*(currentRadius+1);
					double childY = posY+face.getFrontOffsetY()*(currentRadius+1);
					double childZ = posZ+face.getFrontOffsetZ()*(currentRadius+1);

					//Spawn child gas cloud
					FluidStack childFluid = fluidStack.copy();
					childFluid.amount = amountPerChild;
					EntityGasCloud child = new EntityGasCloud(world, childX, childY, childZ, childFluid);
					child.currentRadius = 0.5f;
					world.spawnEntity(child);

					//Reduce parent amount
					if((fluidStack.amount -= amountPerChild) <= 0)
					{
						setDead();
						return;
					}
				}
			//Recalculate maxRadius based on new totalGasAmount
			this.maxRadius = calculateMaxRadiusFromAmount();
			updateBoundingBox();
		}
	}

	/**
	 * @return true if the gas cloud can expand freely in given direction
	 */
	private boolean isDirectionFree(EnumFacing face)
	{
		Vec3d expansionVector = new Vec3d(face.getDirectionVec());
		AxisAlignedBB checked = new AxisAlignedBB(this.getPosition())
				.grow(currentRadius)
				.expand(0, -currentRadius, 0)
				.expand(expansionVector.x*(currentRadius+1), expansionVector.y*(currentRadius+1), expansionVector.z*(currentRadius+1))
				.contract(-expansionVector.x*(currentRadius)*2, -expansionVector.y*(currentRadius)*2, -expansionVector.z*(currentRadius)*2);

		for(int x = (int)checked.minX; x <= checked.maxX; x++)
			for(int y = (int)checked.minY; y <= checked.maxY; y++)
				for(int z = (int)checked.minZ; z <= checked.maxZ; z++)
				{
					BlockPos pos = new BlockPos(x, y, z);
					IBlockState state = world.getBlockState(pos);
					if(!state.getBlock().isAir(state, world, pos)&&state.isFullBlock())
						return false;
				}
		return true;
	}

	private void updateBoundingBox()
	{
		this.width = currentRadius*2;
		this.height = currentRadius*2;
		setEntityBoundingBox(new AxisAlignedBB(this.getPosition())
				.expand(currentRadius, currentRadius, currentRadius)
				.expand(-currentRadius, 0, -currentRadius)
		);
	}

	//--- NBT ---//

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		ISyncNBTEntity.super.readEntityFromNBT(compound);
		//Update the radius
		if(fluidStack!=null)
			maxRadius = calculateMaxRadiusFromAmount();
		updateBoundingBox();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		ISyncNBTEntity.super.writeEntityToNBT(compound);
	}

	//--- Overrides ---//

	@Override
	public void setFire(int seconds)
	{
		if(fluidStack!=null&&ChemthrowerHandler.isFlammable(fluidStack.getFluid()))
		{
			duration -= seconds*10;
			super.setFire(seconds);
		}
	}

	@Override
	public boolean shouldRenderInPass(int pass)
	{
		return pass==0;
	}

	@Override
	public void move(MoverType type, double x, double y, double z)
	{

	}
}
