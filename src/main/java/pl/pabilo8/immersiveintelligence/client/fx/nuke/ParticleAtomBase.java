package pl.pabilo8.immersiveintelligence.client.fx.nuke;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.client.fx.IIParticle;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleRenderer.DrawingStages;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 04.01.2023
 */
public abstract class ParticleAtomBase extends IIParticle
{
	public static TextureAtlasSprite[] TEXTURES;
	protected final float actualParticleScale;

	public ParticleAtomBase(World world, Vec3d pos, Vec3d motion, float size)
	{
		super(world, pos, motion);

		this.motionX *= 1.55;
		this.motionY *= 0.65;
		this.motionZ *= 1.55;

		this.actualParticleScale = getActualScale(size);
		this.particleGravity = 0.25f;
	}

	protected abstract float getActualScale(float size);

	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if(this.particleAge++ >= this.particleMaxAge)
			this.setExpired();

		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9599999785423279D;
		this.motionY *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;
		if(world==null)
			return;

		EntityPlayer entityplayer = this.world.getClosestPlayer(this.posX, this.posY, this.posZ, 2.0D, false);

		if(entityplayer!=null)
		{
			AxisAlignedBB axisalignedbb = entityplayer.getEntityBoundingBox();

			if(this.posY > axisalignedbb.minY)
			{
				this.posY += (axisalignedbb.minY-this.posY)*0.2D;
				this.motionY += (entityplayer.motionY-this.motionY)*0.2D;
				this.setPosition(this.posX, this.posY, this.posZ);
			}
		}

		if(this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
	}

	@Override
	public int getFXLayer()
	{
		return 1;
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_)
	{
		return 240<<16|240;
	}

	void setScale(float progress)
	{
		this.particleScale = this.actualParticleScale*progress;
	}

	void setIndex(int index)
	{
		particleTexture = TEXTURES[index];
	}

	@Nonnull
	@Override
	public ParticleRenderer.DrawingStages getDrawStage()
	{
		return DrawingStages.CUSTOM;
	}
}
