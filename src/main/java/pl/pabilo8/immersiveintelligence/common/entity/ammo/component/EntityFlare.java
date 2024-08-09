package pl.pabilo8.immersiveintelligence.common.entity.ammo.component;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleProperties;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8
 * @since 19.01.2021
 */
public class EntityFlare extends Entity implements IEntityAdditionalSpawnData
{
	IIColor color = IIColor.WHITE;

	public EntityFlare(World worldIn)
	{
		super(worldIn);
	}

	public EntityFlare(World worldIn, IIColor color)
	{
		super(worldIn);
		this.color = color;
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
		ParticleRegistry.spawnParticle("flare", getPositionVector().subtract(0, 0.1f, 0), Vec3d.ZERO, Vec3d.ZERO)
				.setProperties(EasyNBT.newNBT().withColor(IIParticleProperties.COLOR, color));
	}

	@Override
	protected void entityInit()
	{
		setSize(0.5f, 0.5f);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		color = IIColor.fromPackedRGB(compound.getInteger("color"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("color", color.getPackedRGB());
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeInt(color.getPackedRGB());
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		color = IIColor.fromPackedRGB(additionalData.readInt());
	}
}
