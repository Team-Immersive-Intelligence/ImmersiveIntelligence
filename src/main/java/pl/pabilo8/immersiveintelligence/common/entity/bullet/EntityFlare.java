package pl.pabilo8.immersiveintelligence.common.entity.bullet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;

/**
 * @author Pabilo8
 * @since 19.01.2021
 */
public class EntityFlare extends Entity implements IEntityAdditionalSpawnData
{
	int colour = 0xffffff;

	public EntityFlare(World worldIn)
	{
		super(worldIn);
	}

	public EntityFlare(World worldIn, int color)
	{
		super(worldIn);
		this.colour = color;
	}

	@Override
	public void onUpdate()
	{
		motionY = -0.01f;
		move(MoverType.SELF, 0, -0.01f, 0);
		super.onUpdate();

		if(world.isRemote)
			spawnParticles();

		if(ticksExisted > 400)
			setDead();
	}

	@SideOnly(Side.CLIENT)
	private void spawnParticles()
	{
		ParticleUtils.spawnFlareTraceFX(getPositionVector(), colour, 2f);
		ParticleUtils.spawnFlareFX(getPositionVector().subtract(0, 0.1f, 0), colour, 1f);
	}

	@Override
	protected void entityInit()
	{
		setSize(0.5f, 0.5f);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		colour = compound.getInteger("colour");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setFloat("colour", colour);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeInt(colour);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		colour = additionalData.readInt();
	}
}
