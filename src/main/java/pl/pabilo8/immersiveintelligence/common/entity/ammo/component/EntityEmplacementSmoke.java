package pl.pabilo8.immersiveintelligence.common.entity.ammo.component;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;

/**
 * Expanding, non-flammable smoke released by the Emplacement's Emergency Smoke upgrade.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 03.10.2026
 */
public class EntityEmplacementSmoke extends Entity implements ISyncNBTEntity<EntityEmplacementSmoke>
{
	private static final float GROWTH_RATE = 0.01f;
	@SyncNBT(events = SyncEvents.ENTITY_CUSTOM1)
	public int smokeAmount;
	@SyncNBT(events = SyncEvents.ENTITY_CUSTOM1)
	public float currentRadius = 0.5f;
	@SyncNBT(events = SyncEvents.ENTITY_CUSTOM1)
	public float maxRadius = 0.5f;
	@SyncNBT(events = SyncEvents.ENTITY_CUSTOM1)
	public int duration = 40;

	public EntityEmplacementSmoke(World world)
	{
		super(world);
		setSize(1f, 1f);
		this.noClip = true;
		this.isImmuneToFire = true;
	}

	public EntityEmplacementSmoke(World world, double x, double y, double z, int amount)
	{
		this(world);
		this.smokeAmount = Math.max(1, amount);
		this.maxRadius = calculateMaxRadius();
		this.currentRadius = Math.min(0.5f, maxRadius);
		this.duration = (int)Math.min(Integer.MAX_VALUE, (long)smokeAmount*2);
		setPosition(x, y, z);
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	public void onUpdate()
	{
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if(smokeAmount <= 0||duration <= 0)
		{
			setDead();
			return;
		}

		duration--;
		if(world.isRemote)
		{
			if(ticksExisted%20==0)
				ParticleRegistry.spawnEmplacementSmoke(getPositionVector().addVector(0, currentRadius, 0), currentRadius);
			currentRadius = Math.min(currentRadius+GROWTH_RATE, maxRadius);
			updateBoundingBox();
			if(duration <= 0)
				setDead();
			return;
		}

		float previousRadius = currentRadius;
		int previousAmount = smokeAmount;
		if(currentRadius < maxRadius)
		{
			float nextRadius = Math.min(currentRadius+GROWTH_RATE, maxRadius);
			if(world.collidesWithAnyBlock(new AxisAlignedBB(posX-nextRadius, posY, posZ-nextRadius,
					posX+nextRadius, posY+nextRadius*2, posZ+nextRadius)))
				attemptSpawnChildren();
			else
				currentRadius = nextRadius;
			updateBoundingBox();
		}

		//Set the position as well as the box, so clients and fractional coordinates stay consistent.
		if(!world.collidesWithAnyBlock(getEntityBoundingBox().offset(0, -0.005, 0)))
			setPosition(posX, posY-0.005, posZ);
		if(previousRadius!=currentRadius||previousAmount!=smokeAmount||ticksExisted%10==0)
			updateEntityForEvent(SyncEvents.ENTITY_CUSTOM1);
		if(duration <= 0)
			setDead();
	}

	private float calculateMaxRadius()
	{
		return (float)Math.cbrt(smokeAmount/200d)*1.25f;
	}

	private void attemptSpawnChildren()
	{
		int amountPerChild = smokeAmount/12;
		if(amountPerChild < 10||duration <= 0)
			return;

		int freeDirections = 0;
		for(EnumFacing face : EnumFacing.values())
			if(isDirectionFree(face))
				freeDirections++;
		if(freeDirections < 2)
			return;

		for(EnumFacing face : EnumFacing.values())
			if(isDirectionFree(face))
			{
				EntityEmplacementSmoke child = new EntityEmplacementSmoke(world,
						posX+face.getFrontOffsetX()*(currentRadius+1),
						posY+face.getFrontOffsetY()*(currentRadius+1),
						posZ+face.getFrontOffsetZ()*(currentRadius+1), amountPerChild);
				child.duration = Math.min(child.duration, duration);
				if(world.spawnEntity(child))
					smokeAmount -= amountPerChild;
			}
		maxRadius = calculateMaxRadius();
		currentRadius = Math.min(currentRadius, maxRadius);
	}

	private boolean isDirectionFree(EnumFacing face)
	{
		//Check the whole path so a child cannot jump through a wall into empty space beyond it.
		return !world.collidesWithAnyBlock(getEntityBoundingBox().expand(
				face.getFrontOffsetX()*(currentRadius+1), face.getFrontOffsetY()*(currentRadius+1),
				face.getFrontOffsetZ()*(currentRadius+1)));
	}

	@Override
	public void setPosition(double x, double y, double z)
	{
		super.setPosition(x, y, z);
		updateBoundingBox();
	}

	private void updateBoundingBox()
	{
		width = height = currentRadius*2;
		setEntityBoundingBox(new AxisAlignedBB(posX-currentRadius, posY, posZ-currentRadius,
				posX+currentRadius, posY+currentRadius*2, posZ+currentRadius));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		ISyncNBTEntity.super.readEntityFromNBT(compound);
		updateBoundingBox();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		ISyncNBTEntity.super.writeEntityToNBT(compound);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void receiveNBTMessageClient(NBTTagCompound compound)
	{
		ISyncNBTEntity.super.receiveNBTMessageClient(compound);
		updateBoundingBox();
	}

	@Override
	public void setFire(int seconds)
	{

	}

	@Override
	public void move(MoverType type, double x, double y, double z)
	{

	}
}
